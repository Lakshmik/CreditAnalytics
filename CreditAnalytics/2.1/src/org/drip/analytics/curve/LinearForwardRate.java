
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
 * This class contains the linear forward bootstrapped discount curve holder object. It maintains term
 * 		structure for the period begin rates, the calibration instruments, calibration measures, calibration
 * 		quotes, and parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public class LinearForwardRate extends org.drip.analytics.definition.DiscountCurve {
	private static final int NUM_DF_QUADRATURES = 5;

	private double[] _adblDate = null;
	private double[] _adblEndRate = null;
	private boolean _bCalibModeOn = false;
	private double[] _adblCalibQuote = null;
	private java.lang.String _strCurrency = "";
	private double _dblStartDate = java.lang.Double.NaN;
	private java.lang.String[] _astrCalibMeasure = null;
	private org.drip.math.spline.SpanInterpolator _csi = null;
	private org.drip.param.valuation.ValuationParams _valParam = null;
	private org.drip.param.valuation.QuotingParams _quotingParams = null;
	private java.util.Map<java.lang.String, java.lang.Double> _mapQuote = null;
	private java.util.Map<java.lang.Double, java.lang.Double> _mapDateDF = null;
	private java.util.Map<java.lang.String, java.lang.String> _mapMeasure = null;
	private org.drip.product.definition.CalibratableComponent[] _aCalibInst = null;
	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixing = null;

	/**
	 * Boot-straps a linear forward discount curve from an array of dates and discount rates
	 * 
	 * @param dtStart Epoch Date
	 * @param strCurrency Currency
	 * @param adblDate Array of Dates
	 * @param adblEndRate Array of Rates
	 * 
	 * @throws java.lang.Exception Thrown if the curve cannot be created
	 */

	public LinearForwardRate (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double[] adblDate,
		final double[] adblEndRate)
		throws java.lang.Exception
	{
		if (null == adblDate || 0 == adblDate.length || null == adblEndRate || adblDate.length !=
			adblEndRate.length || null == dtStart || null == strCurrency || strCurrency.isEmpty())
			throw new java.lang.Exception ("Invalid inputs into LinearForwardRate constructor");

		_bCalibModeOn = true;
		_strCurrency = strCurrency;
		_adblDate = new double[adblDate.length];
		_adblEndRate = new double[adblEndRate.length];

		_mapDateDF = new java.util.TreeMap<java.lang.Double, java.lang.Double>();

		for (int i = 0; i < _adblDate.length; ++i)
			_adblDate[i] = adblDate[i];

		for (int i = 0; i < _adblEndRate.length; ++i)
			_adblEndRate[i] = adblEndRate[i];

		_dblStartDate = dtStart.getJulian();
	}

	protected LinearForwardRate (
		final LinearForwardRate dc)
		throws java.lang.Exception
	{
		if (null == dc) throw new java.lang.Exception ("Invalid input LinearForwardRate");

		_valParam = dc._valParam;
		_mapQuote = dc._mapQuote;
		_mmFixing = dc._mmFixing;
		_adblDate = dc._adblDate;
		_aCalibInst = dc._aCalibInst;
		_mapMeasure = dc._mapMeasure;
		_strCurrency = dc._strCurrency;
		_adblEndRate = dc._adblEndRate;
		_dblStartDate = dc._dblStartDate;
		_quotingParams = dc._quotingParams;
		_adblCalibQuote = dc._adblCalibQuote;
		_astrCalibMeasure = dc._astrCalibMeasure;
	}

	/**
	 * LinearForwardRate de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if LinearForwardRate cannot be properly de-serialized
	 */

	public LinearForwardRate (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception
				("LinearForwardRate de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("LinearForwardRate de-serializer: Empty state");

		java.lang.String strSerializedLinearForwardRate = strRawString.substring (0,
			strRawString.indexOf (getObjectTrailer()));

		if (null == strSerializedLinearForwardRate ||
			strSerializedLinearForwardRate.isEmpty())
			throw new java.lang.Exception ("LinearForwardRate de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedLinearForwardRate, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception
				("LinearForwardRate de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("LinearForwardRate de-serializer: Cannot locate start state");

		_dblStartDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("LinearForwardRate de-serializer: Cannot locate currency");

		_strCurrency = astrField[2];

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblRate = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("LinearForwardRate de-serializer: Cannot decode state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblRate,
			astrField[3], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("LinearForwardRate de-serializer: Cannot decode state");

		if (0 == lsdblDate.size() || 0 == lsdblRate.size() || lsdblDate.size() != lsdblRate.size())
			throw new java.lang.Exception ("LinearForwardRate de-serializer: Cannot decode state");

		_adblDate = new double[lsdblDate.size()];

		_adblEndRate = new double[lsdblRate.size()];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			_adblEndRate[i] = lsdblRate.get (i);
		}
	}

	@Override public boolean initializeCalibrationRun (
		final double dblLeftSlope)
	{
		return true;
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

	@Override public LinearForwardRate createParallelShiftedCurve (
		final double dblShift)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblShift)) return null;

		if (null == _valParam || null == _aCalibInst || 0 == _aCalibInst.length || null == _adblCalibQuote ||
			0 == _adblCalibQuote.length || null == _astrCalibMeasure || 0 == _astrCalibMeasure.length ||
				_astrCalibMeasure.length != _adblCalibQuote.length || _adblCalibQuote.length !=
					_aCalibInst.length)
			return createParallelRateShiftedCurve (dblShift);

		LinearForwardRate dc = null;

		try {
			dc = new LinearForwardRate (new org.drip.analytics.date.JulianDate (_dblStartDate),
				_strCurrency, _adblDate, _adblEndRate);
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

	@Override public LinearForwardRate createParallelRateShiftedCurve (
		final double dblShift)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblShift)) return null;

		double[] adblCDFRate = new double[_adblEndRate.length];

		for (int i = 0; i < adblCDFRate.length; ++i)
			adblCDFRate[i] = _adblEndRate[i] + dblShift;

		try {
			return new LinearForwardRate (new org.drip.analytics.date.JulianDate (_dblStartDate),
				_strCurrency, _adblDate, adblCDFRate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public LinearForwardRate createBasisRateShiftedCurve (
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

			return new LinearForwardRate (new org.drip.analytics.date.JulianDate (_dblStartDate),
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

		double[] adblCDFBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode (_adblEndRate, ntp);

		if (null == adblCDFBumped || 0 == adblCDFBumped.length) return null;

		try {
			return new LinearForwardRate (new org.drip.analytics.date.JulianDate (_dblStartDate),
				_strCurrency, _adblDate, adblCDFBumped);
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
			throw new java.lang.Exception ("LinearForwardRate.getDF got NaN for date");

		if (!_bCalibModeOn) {
			if (_mapDateDF.containsKey (dblDate)) return _mapDateDF.get (dblDate);

			if (dblDate <= _dblStartDate) return 1.;

			if (dblDate <= _adblDate[0])
				return java.lang.Math.exp (-1. * _adblEndRate[0] * (dblDate - _dblStartDate) / 365.25);

			return dblDate < _adblDate[_adblDate.length - 1] ? _csi.calcValue (dblDate) : java.lang.Math.exp
				(-1. * _adblEndRate[_adblDate.length - 1] * (dblDate - _dblStartDate) / 365.25);
		}

		if (dblDate <= _dblStartDate) {
			_mapDateDF.put (dblDate, 1.);

			return 1.;
		}

		int i = 0;
		double dblExpArg = 0.;
		double dblStartDate = _dblStartDate;

		while (i < _adblEndRate.length && (int) dblDate >= (int) _adblDate[i]) {
			if (0 == i)
				dblExpArg -= _adblEndRate[i] * (_adblDate[i] - dblStartDate);
			else
				dblExpArg -= 0.5 * (_adblEndRate[i - 1] + _adblEndRate[i]) * (_adblDate[i] - dblStartDate);

			dblStartDate = _adblDate[i++];
		}

		if (i >= _adblEndRate.length) i = _adblEndRate.length - 1;

		if (0 == i || _adblEndRate.length - 1 == i)
			dblExpArg -= _adblEndRate[i] * (dblDate - dblStartDate);
		else {
			double dblFinalRate = _adblEndRate[i - 1] + ((_adblEndRate[i] - _adblEndRate[i - 1]) /
				(_adblDate[i] - dblStartDate) * (dblDate - dblStartDate));

			dblExpArg -= 0.5 * (_adblEndRate[i - 1] + dblFinalRate) * (dblDate - dblStartDate);
		}

		double dblDF = java.lang.Math.exp (dblExpArg / 365.25);

		_mapDateDF.put (dblDate, dblDF);

		return dblDF;
	}

	@Override public org.drip.math.algodiff.WengertJacobian getDFJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		double dblDF = java.lang.Double.NaN;
		org.drip.math.algodiff.WengertJacobian wj = null;

		try {
			wj = new org.drip.math.algodiff.WengertJacobian (1, _adblEndRate.length);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (dblDate <= _dblStartDate) {
			wj.setWengert (0, 0.);

			return wj;
		}

		try {
			if (!wj.setWengert (0, dblDF = getDF (dblDate))) return null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		int i = 0;
		double dblStartDate = _dblStartDate;

		while (i < _adblEndRate.length && (int) dblDate >= (int) _adblDate[i]) {
			if (0 == i) {
				if (!wj.accumulatePartialFirstDerivative (0, i, dblDF * (dblStartDate - _adblDate[i]) /
					365.25))
					return null;
			} else {
				if (!wj.accumulatePartialFirstDerivative (0, i - 1, 0.5 * dblDF * (dblStartDate -
					_adblDate[i]) / 365.25) || !wj.accumulatePartialFirstDerivative (0, i, 0.5 * dblDF *
						(dblStartDate - _adblDate[i]) / 365.25))
					return null;
			}

			dblStartDate = _adblDate[i++];
		}

		if (i >= _adblEndRate.length) i = _adblEndRate.length - 1;

		if (0 == i || _adblEndRate.length - 1 == i) {
			if (!wj.accumulatePartialFirstDerivative (0, i, dblDF * (dblStartDate - dblDate) / 365.25))
				return null;
		} else {
			if (!wj.accumulatePartialFirstDerivative (0, i, dblDF * 0.5 * (dblDate + _adblEndRate[i] - 2. *
				_adblEndRate[i - 1]) * (dblStartDate - dblDate) / (_adblEndRate[i] - _adblEndRate[i - 1]) /
					365.25) || !wj.accumulatePartialFirstDerivative (0, i - 1, dblDF * 0.5 * (dblDate -
						_adblEndRate[i]) * (dblDate - dblStartDate) / (_adblEndRate[i] - _adblEndRate[i - 1])
							/ 365.25))
				return null;
		}

		return wj;
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
			throw new java.lang.Exception ("LinearForwardRate.getEffectiveDF got null for date");

		return getEffectiveDF (dt1.getJulian(), dt2.getJulian());
	}

	@Override public double getEffectiveDF (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("LinearForwardRate.getEffectiveDF got bad tenor");

		return getEffectiveDF (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor1),
			new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor2));
	}

	@Override public boolean setNodeValue (
		final int iNodeIndex,
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue) || iNodeIndex > _adblEndRate.length)
			return false;

		for (int i = iNodeIndex; i < _adblEndRate.length; ++i)
			_adblEndRate[i] = dblValue;

		return true;
	}

	@Override public boolean bumpNodeValue (
		final int iNodeIndex,
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue) || iNodeIndex > _adblEndRate.length)
			return false;

		for (int i = iNodeIndex; i < _adblEndRate.length; ++i)
			_adblEndRate[i] += dblValue;

		return true;
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue)) return false;

		for (int i = 0; i < _adblEndRate.length; ++i)
			_adblEndRate[i] = dblValue;

		return true;
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

		return 365.25 / (dblDt2 - dblDt1) * java.lang.Math.log (getDF (dblDt1) / getDF (dblDt2));
	}

	@Override public double calcImpliedRate (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("LinearForwardRate.calcImpliedRate got NaN for date");

		return calcImpliedRate (_dblStartDate, dblDate);
	}

	@Override public double calcImpliedRate (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("LinearForwardRate.getDF got empty date");

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

	@Override public boolean buildInterpolator()
	{
		if (!_bCalibModeOn) return false;

		org.drip.math.spline.BasisSplineElasticParams bfp = new
			org.drip.math.spline.BasisSplineElasticParams();

		if (!bfp.addParam ("NormalizedTension", .001)) return false;

		int i = 0;

		double[] adblDF = new double[_mapDateDF.size()];

		double[] adblDate = new double[_mapDateDF.size()];

		for (java.util.Map.Entry<java.lang.Double, java.lang.Double> me : _mapDateDF.entrySet()) {
			if (null == me || null == me.getKey() || null == me.getValue()) continue;

			adblDate[i] = me.getKey();

			/* try {
				System.out.println (i + " | " + new org.drip.analytics.date.JulianDate (me.getKey()) + " = "
					+ me.getValue());
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			} */

			adblDF[i++] = me.getValue();
		}

		if (null == (_csi = org.drip.math.spline.SpanInterpolator.CreateSpanInterpolator (adblDate, adblDF,
			org.drip.math.spline.SpanInterpolator.BASIS_SPLINE_CUBIC_POLYNOMIAL, bfp,
				org.drip.math.spline.SpanInterpolator.SPLINE_BOUNDARY_MODE_NATURAL,
					org.drip.math.spline.SpanInterpolator.SET_ITEP |
						org.drip.math.spline.SpanInterpolator.CALIBRATE_SPAN)))
			return false;

		/* try {
			for (i = 0; i < adblDate.length; ++i)
				System.out.println ("DF[" + adblDate[i] + "]: " +
					org.drip.analytics.support.GenericUtil.FormatPrice (adblDF[i], 1, 6, 1) + " / " +
						org.drip.analytics.support.GenericUtil.FormatPrice (_csi.calcValue (adblDate[i]), 1,
							6, 1));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		} */

		_bCalibModeOn = false;
		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _dblStartDate +
			getFieldDelimiter() + _strCurrency + getFieldDelimiter());

		if (null == _adblEndRate || 0 == _adblEndRate.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			for (int i = 0; i < _adblEndRate.length; ++i) {
				if (0 != i) sb.append (getCollectionRecordDelimiter());

				sb.append (_adblDate[i] + getCollectionKeyValueDelimiter() + _adblEndRate[i]);
			}
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new LinearForwardRate (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String displayString()
	{
		try {
			java.lang.StringBuffer sb = new java.lang.StringBuffer();

			for (int i = 0; i < _adblEndRate.length; ++i) {
				if (0 != i) sb.append (" | ");

				sb.append (new org.drip.analytics.date.JulianDate (_adblDate[i]) + "=" + _adblEndRate[i]);
			}

			return sb.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblRate = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblDate[i] = dblStart + 365. * (i + 1);
			adblRate[i] = 0.01 * (i + 1);
		}

		LinearForwardRate dc = new LinearForwardRate
			(org.drip.analytics.date.JulianDate.Today(), "ABC", adblDate, adblRate);

		byte[] abDC = dc.serialize();

		System.out.println ("Input: " + new java.lang.String (abDC));

		System.out.println ("DF[12/12/20]=" + dc.getDF
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")));

		LinearForwardRate dcDeser = (LinearForwardRate) dc.deserialize (abDC);

		System.out.println ("Output: " + new java.lang.String (dcDeser.serialize()));

		System.out.println ("DF[12/12/20]=" + dcDeser.getDF
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")));
	}
}