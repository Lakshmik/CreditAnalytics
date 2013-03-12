
package org.drip.analytics.curve;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.credit-trader.org
 * 
 * CreditAnalytics is a free, full featured, fixed income credit analytics library, developed with a special
 * 		focus towards the needs of the bonds and credit products community.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * This class contains the polynomial spline discount factor based discount curve holder object. It maintains
 * 		term structure for the period begin rates, the calibration instruments, calibration measures,
 * 		calibration quotes, and parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public class PolynomialSplineDF extends org.drip.analytics.definition.DiscountCurve {
	private static final int NUM_DF_QUADRATURES = 5;

	/**
	 * Log Relation between Discount Factor and Forward Rate
	 */

	public static final int DISC_FACTOR_FWD_RATE_RELATION_LOG = 0;

	/**
	 * Linear Relation between Discount Factor and Forward Rate
	 */

	public static final int DISC_FACTOR_FWD_RATE_RELATION_LINEAR = 1;

	private double[] _adblDate = null;
	private double[] _adblCalibQuote = null;
	private java.lang.String _strCurrency = "";
	private double _dblStartDate = java.lang.Double.NaN;
	private java.lang.String[] _astrCalibMeasure = null;
	private double _dblLeftNodeDF = java.lang.Double.NaN;
	private double _dblLeftNodeDFSlope = java.lang.Double.NaN;
	private org.drip.math.spline.SpanInterpolator _csi = null;
	private double _dblLeftFlatForwardRate = java.lang.Double.NaN;
	private double _dblRightFlatForwardRate = java.lang.Double.NaN;
	private org.drip.param.valuation.ValuationParams _valParam = null;
	private int _iDFFwdRateRelation = DISC_FACTOR_FWD_RATE_RELATION_LOG;
	private org.drip.param.valuation.QuotingParams _quotingParams = null;
	private java.util.Map<java.lang.String, java.lang.Double> _mapQuote = null;
	private java.util.Map<java.lang.String, java.lang.String> _mapMeasure = null;
	private org.drip.product.definition.CalibratableComponent[] _aCalibInst = null;
	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixing = null;

	private PolynomialSplineDF (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double[] adblDate,
		final org.drip.math.spline.SpanInterpolator csi)
		throws java.lang.Exception
	{
		_csi = csi;
		_strCurrency = strCurrency;
		_adblDate = new double[adblDate.length];

		for (int i = 0; i < _adblDate.length; ++i)
			_adblDate[i] = adblDate[i];
	}

	/**
	 * Constructs PolynomialSplineDF Curve from an array of dates and forward rates
	 * 
	 * @param dtStart Epoch Date
	 * @param strCurrency Currency
	 * @param adblDate Array of Dates
	 * @param adblRate Array of Forward Rates
	 * 
	 * @throws java.lang.Exception Thrown if the curve cannot be created
	 */

	public PolynomialSplineDF (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double[] adblDate,
		final double[] adblRate)
		throws java.lang.Exception
	{
		if (null == adblDate || 0 == adblDate.length || null == adblRate || adblDate.length !=
			adblRate.length || null == dtStart || null == (_strCurrency = strCurrency) ||
				_strCurrency.isEmpty())
			throw new java.lang.Exception ("Invalid inputs into PolynomialSplineDF constructor");

		_dblStartDate = dtStart.getJulian();

		_adblDate = new double[adblDate.length];
		double[] adblDF = new double[adblDate.length];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = adblDate[i];

			if (0 == i)
				adblDF[0] = DISC_FACTOR_FWD_RATE_RELATION_LOG == _iDFFwdRateRelation ? java.lang.Math.exp
					(adblRate[0] * (_dblStartDate - _adblDate[0]) / 365.25) : 1. / (1. + (adblRate[0] *
						(_dblStartDate - _adblDate[0]) / 365.25));
			else
				adblDF[i] = DISC_FACTOR_FWD_RATE_RELATION_LOG == _iDFFwdRateRelation ? java.lang.Math.exp
					(adblRate[i] * (_adblDate[i - 1] - _adblDate[i]) / 365.25) * adblDF[i - 1] :
						adblDF[i - 1] / (1. + (adblRate[0] * (_dblStartDate - _adblDate[0]) / 365.25));
		}

		_dblLeftFlatForwardRate = -365.25 * java.lang.Math.log (adblDF[0]) / (_adblDate[0] - _dblStartDate);

		_dblRightFlatForwardRate = -365.25 * java.lang.Math.log (adblDF[adblDF.length - 1]) /
			(_adblDate[_adblDate.length - 1] - _dblStartDate);

		org.drip.math.spline.BasisSplineElasticParams bfp = new
			org.drip.math.spline.BasisSplineElasticParams();

		bfp.addParam ("DenormalizedTension", 0.007);

		_csi = org.drip.math.spline.SpanInterpolator.CreateSpanInterpolator (adblDate, adblDF,
			org.drip.math.spline.SpanInterpolator.BASIS_SPLINE_LINEAR_POLYNOMIAL, bfp,
				org.drip.math.spline.SpanInterpolator.SPLINE_BOUNDARY_MODE_NATURAL,
					org.drip.math.spline.SpanInterpolator.SET_ITEP |
						org.drip.math.spline.SpanInterpolator.CALIBRATE_SPAN);
	}

	/**
	 * PolynomialSplineDF de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if PolynomialSplineDF cannot be properly de-serialized
	 */

	public PolynomialSplineDF (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Empty state");

		java.lang.String strSerializedPolynomialSplineDF = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedPolynomialSplineDF || strSerializedPolynomialSplineDF.isEmpty())
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedPolynomialSplineDF, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot locate start state");

		_dblStartDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot locate currency");

		_strCurrency = astrField[2];

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblRate = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot decode state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblRate,
			astrField[3], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot decode state");

		if (0 == lsdblDate.size() || 0 == lsdblRate.size() || lsdblDate.size() != lsdblRate.size())
			throw new java.lang.Exception ("PolynomialSplineDF de-serializer: Cannot decode state");

		_adblDate = new double[lsdblDate.size()];

		// _adblEndRate = new double[lsdblRate.size()];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			// _adblEndRate[i] = lsdblRate.get (i);
		}
	}

	@Override public boolean initializeCalibrationRun (
		final double dblLeftSlope)
	{
		return org.drip.math.common.NumberUtil.IsValid (_dblLeftNodeDFSlope = dblLeftSlope);
	}

	@Override public int numCalibNodes()
	{
		return _adblDate.length;
	}

	@Override public java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getCalibFixings()
	{
		return _mmFixing;
	}

	/**
	 * Calculate the calibration metric for the node
	 * 
	 * @return Calibration Metric
	 * 
	 * @throws java.lang.Exception
	 */

	public double getCalibrationMetric()
		throws java.lang.Exception
	{
		return _csi.calcTailDerivative (2);
	}

	@Override public void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure, final java.util.Map<org.drip.analytics.date.JulianDate,
			java.util.Map<java.lang.String, java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		_valParam = valParam;
		_mmFixing = mmFixing;
		_quotingParams = quotingParams;
		_adblCalibQuote = adblCalibQuote;
		_astrCalibMeasure = astrCalibMeasure;

		if (null == (_aCalibInst = aCalibInst) || 0 == _aCalibInst.length) return;

		_mapQuote = new java.util.HashMap<java.lang.String, java.lang.Double>();

		_mapMeasure = new java.util.HashMap<java.lang.String, java.lang.String>();

		for (int i = 0; i < _aCalibInst.length; ++i) {
			_mapMeasure.put (_aCalibInst[i].getPrimaryCode(), astrCalibMeasure[i]);

			_mapQuote.put (_aCalibInst[i].getPrimaryCode(), adblCalibQuote[i]);

			java.lang.String[] astrSecCode = _aCalibInst[i].getSecondaryCode();

			if (null != astrSecCode) {
				for (int j = 0; j < astrSecCode.length; ++j)
					_mapQuote.put (astrSecCode[j], adblCalibQuote[i]);
			}
		}
	}

	@Override public org.drip.product.definition.CalibratableComponent[] getCalibComponents()
	{
		return _aCalibInst;
	}

	@Override public double[] getCompQuotes()
	{
		return _adblCalibQuote;
	}

	@Override public java.lang.String[] getCompMeasures()
	{
		return _astrCalibMeasure;
	}

	@Override public org.drip.analytics.date.JulianDate getNodeDate (
		final int iNode)
	{
		if (iNode >= _adblDate.length) return null;

		try {
			return new org.drip.analytics.date.JulianDate (_adblDate[iNode]);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public double getQuote (
		final java.lang.String strInstr)
		throws java.lang.Exception
	{
		if (null == _mapQuote || 0 == _mapQuote.size() || null == strInstr || strInstr.isEmpty() ||
			!_mapQuote.containsKey (strInstr))
			throw new java.lang.Exception ("Cannot get " + strInstr);

		return _mapQuote.get (strInstr);
	}

	@Override public PolynomialSplineDF createParallelRateShiftedCurve (
		final double dblShift)
	{
		return null;
	}

	@Override public PolynomialSplineDF createParallelShiftedCurve (
		final double dblShift)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblShift)) return null;

		if (null == _valParam || null == _aCalibInst || 0 == _aCalibInst.length || null == _adblCalibQuote ||
			0 == _adblCalibQuote.length || null == _astrCalibMeasure || 0 == _astrCalibMeasure.length ||
				_astrCalibMeasure.length != _adblCalibQuote.length || _adblCalibQuote.length !=
					_aCalibInst.length)
			return createParallelRateShiftedCurve (dblShift);

		PolynomialSplineDF dc = null;

		try {
			dc = new PolynomialSplineDF (new org.drip.analytics.date.JulianDate (_dblStartDate),
				_strCurrency, _adblDate, _csi);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		double[] adblCalibQuoteShifted = new double[_adblCalibQuote.length];

		try {
			org.drip.analytics.calibration.CurveCalibrator calibrator = new
				org.drip.analytics.calibration.CurveCalibrator();

			for (int i = 0; i < _adblCalibQuote.length; ++i)
				calibrator.calibrateIRNode (dc, null, null, _aCalibInst[i], i, _valParam,
					_astrCalibMeasure[i], adblCalibQuoteShifted[i] = _adblCalibQuote[i] + dblShift,
						_mmFixing, _quotingParams, false, java.lang.Double.NaN);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		dc.setInstrCalibInputs (_valParam, _aCalibInst, adblCalibQuoteShifted, _astrCalibMeasure, _mmFixing,
			_quotingParams);

		return dc;
	}

	@Override public PolynomialSplineDF createBasisRateShiftedCurve (
		final double[] adblDate,
		final double[] adblBasis)
	{
		if (null == adblDate || 0 == adblDate.length || null == adblBasis || 0 == adblBasis.length ||
			adblDate.length != adblBasis.length)
			return null;

		try {
			double[] adblCDFRate = new double[adblBasis.length];

			for (int i = 0; i < adblDate.length; ++i)
				adblCDFRate[i] = calcImpliedRate (adblDate[i]) + adblBasis[i];

			return new PolynomialSplineDF (new org.drip.analytics.date.JulianDate (_dblStartDate),
				_strCurrency, adblDate, adblCDFRate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.definition.DiscountCurve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp)
	{
		if (null == ntp) return null;

		double[] adblEndRate = new double[_adblDate.length];

		for (int i = 0; i < adblEndRate.length; ++i) {
			try {
				adblEndRate[i] = calcImpliedRate (_adblDate[i]);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		double[] adblCDFBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode (adblEndRate, ntp);

		if (null == adblCDFBumped || 0 == adblCDFBumped.length) return null;

		try {
			return new  org.drip.analytics.curve.ConstantForwardRate (new org.drip.analytics.date.JulianDate
				(_dblStartDate), _strCurrency, _adblDate, adblCDFBumped);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String getName()
	{
		return _strCurrency;
	}

	@Override public java.lang.String getCurrency()
	{
		return _strCurrency;
	}

	@Override public double getDF (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("PolynomialSplineDF.getDF got NaN for date");

		if (dblDate <= _dblStartDate) return 1.;

		if (dblDate <= _adblDate[0])
			return java.lang.Math.exp (-1. * _dblLeftFlatForwardRate * (dblDate - _dblStartDate) / 365.25);

		return dblDate <= _adblDate[_adblDate.length - 1] ? _csi.calcValue (dblDate) : java.lang.Math.exp
			(-1. * _dblRightFlatForwardRate * (dblDate - _dblStartDate) / 365.25);
	}

	@Override public org.drip.math.algodiff.WengertJacobian getDFJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		org.drip.math.algodiff.WengertJacobian wj = null;

		try {
			wj = new org.drip.math.algodiff.WengertJacobian (1, _adblDate.length);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i < _adblDate.length; ++i) {
			if (!wj.accumulatePartialFirstDerivative (0, i, 0.)) return null;
		}

		if (dblDate <= _dblStartDate) return wj;

		if (dblDate <= _adblDate[0]) {
			try {
				return wj.accumulatePartialFirstDerivative (0, 0, (dblDate - _dblStartDate) / (_adblDate[0] -
					_dblStartDate) * java.lang.Math.exp (_dblLeftFlatForwardRate * (_adblDate[0] - dblDate) /
						365.25)) ? wj : null;
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		if (dblDate <= _adblDate[_adblDate.length - 1]) return _csi.calcValueJacobian (dblDate);

		try {
			return wj.accumulatePartialFirstDerivative (0, _adblDate.length - 1, (dblDate - _dblStartDate) /
				(_adblDate[_adblDate.length - 1] - _dblStartDate) * java.lang.Math.exp
					(_dblRightFlatForwardRate * (_adblDate[_adblDate.length - 1] - dblDate) / 365.25)) ? wj :
						null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public double getEffectiveDF (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (dblDate1 == dblDate2) return getDF (dblDate1);

		int iNumQuadratures = 0;
		double dblEffectiveDF = 0.;
		double dblQuadratureWidth = (dblDate2 - dblDate1) / NUM_DF_QUADRATURES;

		for (double dblDate = dblDate1; dblDate <= dblDate2; dblDate += dblQuadratureWidth) {
			++iNumQuadratures;

			dblEffectiveDF += (getDF (dblDate) + getDF (dblDate + dblQuadratureWidth));
		}

		return dblEffectiveDF / (2. * iNumQuadratures);
	}

	@Override public double getEffectiveDF (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2)
			throw new java.lang.Exception ("PolynomialSplineDF.getEffectiveDF got null for date");

		return getEffectiveDF (dt1.getJulian(), dt2.getJulian());
	}

	@Override public double getEffectiveDF (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("PolynomialSplineDF.getEffectiveDF got bad tenor");

		return getEffectiveDF (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor1),
			new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor2));
	}

	@Override public boolean setNodeValue (
		final int iNodeIndex,
		final double dblNodeDF)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblNodeDF) || iNodeIndex > _adblDate.length)
			return false;

		if (0 == iNodeIndex) {
			_dblLeftFlatForwardRate = -365.25 * java.lang.Math.log (_dblLeftNodeDF = dblNodeDF) /
				(_adblDate[0] - _dblStartDate);

			return true;
		}

		if (1 == iNodeIndex) return _csi.setLeftNode (_dblLeftNodeDF, _dblLeftNodeDFSlope, dblNodeDF);

		if (_adblDate.length - 1 == iNodeIndex) {
			try {
				_dblRightFlatForwardRate = -365.25 * java.lang.Math.log (_csi.calcValue
					(_adblDate[iNodeIndex])) / (_adblDate[iNodeIndex] - _dblStartDate);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		return _csi.resetNode (iNodeIndex, dblNodeDF);
	}

	@Override public boolean bumpNodeValue (
		final int iNodeIndex,
		final double dblValue)
	{
		return false;
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		return false;
	}

	@Override public double calcImpliedRate (
		final double dblDt1,
		final double dblDt2)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDt1) || !org.drip.math.common.NumberUtil.IsValid
			(dblDt2))
			throw new java.lang.Exception ("Invalid input dates");

		if (dblDt1 < _dblStartDate || dblDt2 < _dblStartDate) return 0.;

		return DISC_FACTOR_FWD_RATE_RELATION_LOG == _iDFFwdRateRelation ? 365.25 / (dblDt2 - dblDt1) *
			java.lang.Math.log (getDF (dblDt1) / getDF (dblDt2)) : 365.25 / (dblDt2 - dblDt1) * ((getDF
				(dblDt1) / getDF (dblDt2)) - 1.);
	}

	@Override public double calcImpliedRate (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("PolynomialSplineDF.calcImpliedRate got NaN for date");

		return calcImpliedRate (_dblStartDate, dblDate);
	}

	@Override public double calcImpliedRate (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("PolynomialSplineDF.getDF got empty date");

		return calcImpliedRate (_dblStartDate, new org.drip.analytics.date.JulianDate
			(_dblStartDate).addTenor (strTenor).getJulian());
	}

	@Override public double calcImpliedRate (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("DC.getDF got empty date");

		org.drip.analytics.date.JulianDate dtStart = new org.drip.analytics.date.JulianDate (_dblStartDate);

		return calcImpliedRate (dtStart.addTenor (strTenor1).getJulian(), dtStart.addTenor
			(strTenor2).getJulian());
	}

	@Override public org.drip.analytics.date.JulianDate getStartDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblStartDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ConstantForwardRate (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String displayString()
	{
		return _csi.displayDerivatives();
	}

	@Override public boolean buildInterpolator()
	{
		return false;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblRate = new double[3];

		for (int i = 0; i < adblDate.length; ++i) {
			adblRate[i] = 0.02;
			adblDate[i] = dblStart + 365. * (i + 1);
		}

		PolynomialSplineDF dc = new PolynomialSplineDF (org.drip.analytics.date.JulianDate.Today(), "ABC",
			adblDate, adblRate);

		System.out.println ("DF[12/12/14]=" + dc.getDF
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2014")));

		System.out.println ("DF/DF Micro Jack[12/12/14]=" + dc.getDFJacobian
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2014")).displayString());

		System.out.println ("Zero/Zero Micro Jack[12/12/14]=" + dc.getZeroRateJacobian
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2014")).displayString());

		System.out.println ("DF[12/12/20]=" + dc.getDF
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")));

		System.out.println ("DF/DF Micro Jack[12/12/20]=" + dc.getDFJacobian
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")).displayString());

		System.out.println ("Zero/Zero Micro Jack[12/12/20]=" + dc.getZeroRateJacobian
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")).displayString());

		for (int i = 0; i < adblDate.length; ++i) {
			org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate (adblDate[i]);

			System.out.println (dt + " => " + dc.getDF (adblDate[i]));

			System.out.println ("DF/DF Micro Jack[" + dt + "]=" + dc.getDFJacobian (dt).displayString());

			System.out.println ("Zero/Zero Micro Jack[" + dt + "]=" + dc.getZeroRateJacobian
				(dt).displayString());
		}

		org.drip.math.algodiff.WengertJacobian wjQuoteDF = dc.compQuoteDFJacobian
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020"));

		System.out.println ("Quote/DF Micro Jack[12/12/20]=" + (null == wjQuoteDF ? null :
			wjQuoteDF.displayString()));
	}
}
