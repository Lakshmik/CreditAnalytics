
package org.drip.analytics.curve;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 * This class contains the baseline hazard curve holder object. This maintains term structure for recovery,
 * 		the calibration instruments, calibration measures, calibration quotes, and parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ConstantForwardHazard extends org.drip.analytics.definition.CreditCurve {
	private static final int NUM_DF_QUADRATURES = 5;

	private java.lang.String _strName = "";
	private double[] _adblCalibQuote = null;
	private double[] _adblHazardDate = null;
	private double[] _adblHazardRate = null;
	private double[] _adblRecoveryDate = null;
	private double[] _adblRecoveryRate = null;
	private double _dblStartDate = java.lang.Double.NaN;
	private java.lang.String[] _astrCalibMeasure = null;
	private double _dblSpecificDefaultDate = java.lang.Double.NaN;
	private org.drip.param.valuation.ValuationParams _valParam = null;
	private org.drip.param.valuation.QuotingParams _quotingParams = null;
	private org.drip.product.definition.CalibratableComponent[] _aCalibInst = null;
	private java.util.Map<java.lang.String, java.lang.Double> _mapQuote = null;
	private java.util.Map<java.lang.String, java.lang.String> _mapMeasure = null;
	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixing = null;

	private boolean _bFlat = false;
	private org.drip.analytics.definition.DiscountCurve _dc = null;
	private org.drip.analytics.definition.DiscountCurve _dcTSY = null;
	private org.drip.analytics.definition.DiscountCurve _dcEDSF = null;
	private org.drip.param.pricer.PricerParams _pricerParam = null;

	private org.drip.analytics.definition.CreditCurve createFromBaseNTP (
		final org.drip.param.definition.NodeTweakParams ntp)
	{
		double[] adblHazardBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode
			(_adblHazardRate, ntp);

		if (null == adblHazardBumped || _adblHazardRate.length != adblHazardBumped.length) return null;

		try {
			return new ConstantForwardHazard (_dblStartDate, _strName, adblHazardBumped, _adblHazardDate,
				_adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a credit curve from hazard rate and recovery rate term structures
	 * 
	 * @param dblStart Curve Epoch date
	 * @param strName Credit Curve Name
	 * @param adblHazardRate Matched array of hazard rates
	 * @param adblHazardDate Matched array of hazard dates
	 * @param adblRecoveryRate Matched array of recovery rates
	 * @param adblRecoveryDate Matched array of recovery dates
	 * @param dblSpecificDefaultDate (Optional) Specific Default Date
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public ConstantForwardHazard (
		final double dblStart,
		final java.lang.String strName,
		final double adblHazardRate[],
		final double adblHazardDate[],
		final double[] adblRecoveryRate,
		final double[] adblRecoveryDate,
		final double dblSpecificDefaultDate)
		throws java.lang.Exception
	{
		if (null == adblHazardRate || 0 == adblHazardRate.length || null == adblHazardDate || 0 ==
			adblHazardDate.length || adblHazardRate.length != adblHazardDate.length || null ==
				adblRecoveryRate || 0 == adblRecoveryRate.length || null == adblRecoveryDate || 0 ==
					adblRecoveryDate.length || adblRecoveryRate.length != adblRecoveryDate.length ||
						!org.drip.math.common.NumberUtil.IsValid (dblStart))
			throw new java.lang.Exception ("Invalid Credit curve init params!");

		_dblStartDate = dblStart;
		_dblSpecificDefaultDate = dblSpecificDefaultDate;
		_adblHazardRate = new double[adblHazardRate.length];
		_adblRecoveryRate = new double[adblRecoveryRate.length];
		_adblHazardDate = new double[adblHazardDate.length];
		_adblRecoveryDate = new double[adblRecoveryDate.length];

		for (int i = 0; i < adblHazardRate.length; ++i)
			_adblHazardRate[i] = adblHazardRate[i];

		for (int i = 0; i < _adblHazardDate.length; ++i)
			_adblHazardDate[i] = adblHazardDate[i];

		for (int i = 0; i < adblRecoveryRate.length; ++i)
			_adblRecoveryRate[i] = adblRecoveryRate[i];

		for (int i = 0; i < adblRecoveryDate.length; ++i)
			_adblRecoveryDate[i] = adblRecoveryDate[i];
	}

	/**
	 * CreditCurve de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CreditCurve cannot be properly de-serialized
	 */

	public ConstantForwardHazard (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CreditCurve de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CreditCurve de-serializer: Empty state");

		java.lang.String strSerializedCreditCurve = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCreditCurve || strSerializedCreditCurve.isEmpty())
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCreditCurve, getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("CreditCurve de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot locate curve name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (_strName = astrField[1]))
			_strName = "";

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot locate start date");

		_dblStartDate = new java.lang.Double (astrField[2]);

		java.util.List<java.lang.Double> lsdblHazardDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblHazardRate = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode hazard state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblHazardDate,
			lsdblHazardRate, astrField[3], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode hazard state");

		if (0 == lsdblHazardDate.size() || 0 == lsdblHazardRate.size() || lsdblHazardDate.size() !=
			lsdblHazardRate.size())
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode hazard state");

		_adblHazardDate = new double[lsdblHazardDate.size()];

		_adblHazardRate = new double[lsdblHazardRate.size()];

		for (int i = 0; i < _adblHazardRate.length; ++i) {
			_adblHazardDate[i] = lsdblHazardDate.get (i);

			_adblHazardRate[i] = lsdblHazardRate.get (i);
		}

		java.util.List<java.lang.Double> lsdblRecoveryDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblRecoveryRate = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode recovery state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblRecoveryDate,
			lsdblRecoveryRate, astrField[4], getCollectionRecordDelimiter(),
				getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode recovery state");

		if (0 == lsdblRecoveryDate.size() || 0 == lsdblRecoveryRate.size() || lsdblRecoveryDate.size() !=
			lsdblRecoveryRate.size())
			throw new java.lang.Exception ("CreditCurve de-serializer: Cannot decode recovery state");

		_adblRecoveryDate = new double[lsdblRecoveryDate.size()];

		_adblRecoveryRate = new double[lsdblRecoveryRate.size()];

		for (int i = 0; i < _adblRecoveryRate.length; ++i) {
			_adblRecoveryDate[i] = lsdblRecoveryDate.get (i);

			_adblRecoveryRate[i] = lsdblRecoveryRate.get (i);
		}

		_dblSpecificDefaultDate = new java.lang.Double (astrField[5]);
	}

	@Override public boolean initializeCalibrationRun (
		final double dblLeftSlope)
	{
		return true;
	}

	@Override public int numCalibNodes()
	{
		return _adblHazardDate.length;
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

	@Override public void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final boolean bFlat,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.param.pricer.PricerParams pricerParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		_dc = dc;
		_bFlat = bFlat;
		_dcTSY = dcTSY;
		_dcEDSF = dcEDSF;
		_valParam = valParam;
		_mmFixing = mmFixing;
		_aCalibInst = aCalibInst;
		_pricerParam = pricerParam;
		_quotingParams = quotingParams;
		_adblCalibQuote = adblCalibQuote;
		_astrCalibMeasure = astrCalibMeasure;

		_mapQuote = new java.util.HashMap<java.lang.String, java.lang.Double>();

		_mapMeasure = new java.util.HashMap<java.lang.String, java.lang.String>();

		for (int i = 0; i < aCalibInst.length; ++i) {
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
		if (iNode >= _adblHazardDate.length) return null;

		try {
			return new org.drip.analytics.date.JulianDate (_adblHazardDate[iNode]);
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

	@Override public ConstantForwardHazard createParallelHazardShiftedCurve (
		final double dblShift)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblShift)) return null;

		double[] adblHazard = new double[_adblHazardRate.length];

		for (int i = 0; i < _adblHazardRate.length; ++i)
			adblHazard[i] = _adblHazardRate[i] + dblShift;

		try {
			return new ConstantForwardHazard (_dblStartDate, _strName, adblHazard, _adblHazardDate,
				_adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public ConstantForwardHazard createParallelShiftedCurve (
		final double dblShift)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblShift)) return null;

		if (null == _valParam || null == _aCalibInst || 0 == _aCalibInst.length || null == _adblCalibQuote ||
			0 == _adblCalibQuote.length || null == _astrCalibMeasure || 0 == _astrCalibMeasure.length ||
				_astrCalibMeasure.length != _adblCalibQuote.length || _adblCalibQuote.length !=
					_aCalibInst.length)
			return createParallelHazardShiftedCurve (dblShift);

		ConstantForwardHazard cc = null;
		double[] adblCalibQuote = new double[_adblCalibQuote.length];

		org.drip.analytics.calibration.CurveCalibrator calibrator = new
			org.drip.analytics.calibration.CurveCalibrator();

		try {
			cc = new ConstantForwardHazard (_dblStartDate, _strName, _adblHazardRate, _adblHazardDate,
				_adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i < _adblCalibQuote.length; ++i) {
			try {
				calibrator.bootstrapHazardRate (cc, _aCalibInst[i], i, _valParam, _dc, _dcTSY, _dcEDSF,
					_pricerParam, _astrCalibMeasure[i], adblCalibQuote[i] = _adblCalibQuote[i] + dblShift,
						_mmFixing, _quotingParams, _bFlat);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		cc.setInstrCalibInputs (_valParam, _bFlat, _dc, _dcTSY, _dcEDSF, _pricerParam, _aCalibInst,
			adblCalibQuote, _astrCalibMeasure, _mmFixing, _quotingParams);

		return cc;
	}

	@Override public org.drip.analytics.definition.CreditCurve createFlatCurve (
		final double dblFlatNodeValue,
		final boolean bSingleNode,
		final double dblRecovery)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblFlatNodeValue) || 0. >= dblFlatNodeValue || null ==
			_valParam || null == _aCalibInst || 0 == _aCalibInst.length || null == _adblCalibQuote || 0 ==
				_adblCalibQuote.length || null == _astrCalibMeasure || 0 == _astrCalibMeasure.length ||
					_astrCalibMeasure.length != _adblCalibQuote.length || _adblCalibQuote.length !=
						_aCalibInst.length)
			return null;

		org.drip.analytics.definition.CreditCurve cc = null;

		org.drip.analytics.calibration.CurveCalibrator calibrator = new
			org.drip.analytics.calibration.CurveCalibrator();

		try {
			if (bSingleNode)
				cc = org.drip.analytics.creator.CreditCurveBuilder.FromHazardNode (_dblStartDate, _strName,
					_adblHazardRate[0], _adblHazardDate[0], !org.drip.math.common.NumberUtil.IsValid
						(dblRecovery) ? _adblRecoveryRate[0] : dblRecovery);
			else
				cc = new ConstantForwardHazard (_dblStartDate, _strName, _adblHazardRate, _adblHazardDate,
					_adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i < _adblCalibQuote.length; ++i) {
			try {
				calibrator.bootstrapHazardRate (cc, _aCalibInst[i], i, _valParam, _dc, _dcTSY, _dcEDSF,
					_pricerParam, _astrCalibMeasure[i], dblFlatNodeValue, _mmFixing, _quotingParams, true);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		if (bSingleNode)
			cc.setInstrCalibInputs (_valParam, true, _dc, _dcTSY, _dcEDSF, _pricerParam, new
				org.drip.product.definition.CalibratableComponent[] {_aCalibInst[0]}, new double[]
					{dblFlatNodeValue}, _astrCalibMeasure, _mmFixing, _quotingParams);
		else {
			double[] adblCalibValue = new double[_adblCalibQuote.length];

			for (int i = 0; i < _adblCalibQuote.length; ++i)
				adblCalibValue[i] = dblFlatNodeValue;

			cc.setInstrCalibInputs (_valParam, true, _dc, _dcTSY, _dcEDSF, _pricerParam, _aCalibInst,
				adblCalibValue, _astrCalibMeasure, _mmFixing, _quotingParams);
		}

		return cc;
	}

	@Override public java.lang.String getName()
	{
		return _strName;
	}

	@Override public boolean setNodeValue (
		final int iNodeIndex,
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue) || iNodeIndex > _adblHazardRate.length)
			return false;

		for (int i = iNodeIndex; i < _adblHazardRate.length; ++i)
			_adblHazardRate[i] = dblValue;

		return true;
	}

	@Override public boolean bumpNodeValue (
		final int iNodeIndex,
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue) || iNodeIndex > _adblHazardRate.length)
			return false;

		for (int i = iNodeIndex; i < _adblHazardRate.length; ++i)
			_adblHazardRate[i] += dblValue;

		return true;
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue)) return false;

		for (int i = 0; i < _adblHazardRate.length; ++i)
			_adblHazardRate[i] = dblValue;

		return true;
	}

	@Override  public org.drip.analytics.definition.CreditCurve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp)
	{
		if (null == ntp) return null;

		if (!(ntp instanceof org.drip.param.definition.CreditNodeTweakParams))
			return createFromBaseNTP (ntp);

		org.drip.param.definition.CreditNodeTweakParams cntp =
			(org.drip.param.definition.CreditNodeTweakParams) ntp;

		if (org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_PARAM_RECOVERY.equalsIgnoreCase
			(cntp._strTweakParamType)) {
			double[] adblRecoveryRateBumped = null;

			if (null == (adblRecoveryRateBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode
				(_adblRecoveryRate, cntp)) || adblRecoveryRateBumped.length != _adblRecoveryRate.length)
				return null;

			try {
				return new ConstantForwardHazard (_dblStartDate, _strName, _adblHazardRate, _adblHazardDate,
					adblRecoveryRateBumped, _adblRecoveryDate, _dblSpecificDefaultDate);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		} else if
			(org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_PARAM_QUOTE.equalsIgnoreCase
				(cntp._strTweakParamType)) {
			if (org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_MEASURE_HAZARD.equalsIgnoreCase
				(cntp._strTweakMeasureType)) {
				double[] adblHazardBumped = null;

				if (null == (adblHazardBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode
					(_adblHazardRate, cntp)) || adblHazardBumped.length != _adblHazardRate.length)
					return null;

				try {
					return new ConstantForwardHazard (_dblStartDate, _strName, adblHazardBumped,
						_adblHazardDate, _adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			} else if
				(org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_MEASURE_QUOTE.equalsIgnoreCase
					(cntp._strTweakMeasureType)) {
				double[] adblQuoteBumped = null;

				if (null == (adblQuoteBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode
					(_adblHazardRate, cntp)) || adblQuoteBumped.length != _adblHazardRate.length)
					return null;

				org.drip.analytics.definition.CreditCurve cc = null;

				org.drip.analytics.calibration.CurveCalibrator calibrator = new
					org.drip.analytics.calibration.CurveCalibrator();

				try {
					if (cntp._bSingleNodeCalib)
						cc = org.drip.analytics.creator.CreditCurveBuilder.FromHazardNode (_dblStartDate,
							_strName, _adblHazardRate[0], _adblHazardDate[0], _adblRecoveryRate[0]);
					else
						cc = new ConstantForwardHazard (_dblStartDate, _strName, _adblHazardRate,
							_adblHazardDate, _adblRecoveryRate, _adblRecoveryDate, _dblSpecificDefaultDate);
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}

				for (int i = 0; i < adblQuoteBumped.length; ++i) {
					try {
						calibrator.bootstrapHazardRate (cc, _aCalibInst[i], i, _valParam, _dc, _dcTSY,
							_dcEDSF, _pricerParam, _astrCalibMeasure[i], adblQuoteBumped[i], _mmFixing,
								_quotingParams, _bFlat);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return null;
					}
				}

				cc.setInstrCalibInputs (_valParam, _bFlat, _dc, _dcTSY, _dcEDSF, _pricerParam, _aCalibInst,
					adblQuoteBumped, _astrCalibMeasure, _mmFixing, _quotingParams);

				return cc;
			}
		}

		return null;
	}

	@Override public boolean setSpecificDefault (
		final double dblSpecificDefaultDate)
	{
		_dblSpecificDefaultDate = dblSpecificDefaultDate;
		return true;
	}

	@Override public boolean unsetSpecificDefault()
	{
		_dblSpecificDefaultDate = java.lang.Double.NaN;
		return true;
	}

	@Override public double getSurvival (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("No surv for NaN date");

		if (dblDate <= _dblStartDate) return 1.;

		if (org.drip.math.common.NumberUtil.IsValid (_dblSpecificDefaultDate) && dblDate >=
			_dblSpecificDefaultDate)
			return 0.;

		int i = 0;
		double dblExpArg = 0.;
		double dblStartDate = _dblStartDate;

		while (i < _adblHazardRate.length && dblDate > _adblHazardDate[i]) {
			dblExpArg -= _adblHazardRate[i] * (_adblHazardDate[i] - dblStartDate);
			dblStartDate = _adblHazardDate[i++];
		}

		if (i >= _adblHazardRate.length) i = _adblHazardRate.length - 1;

		dblExpArg -= _adblHazardRate[i] * (dblDate - dblStartDate);

		return java.lang.Math.exp (dblExpArg / 365.25);
	}

	@Override public double getSurvival (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("No surv for null date");

		return getSurvival (dt.getJulian());
	}

	@Override public double getSurvival (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CC.getSurvival got bad tenor");

		return getSurvival (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor));
	}

	@Override public double getEffectiveSurvival (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (dblDate1 == dblDate2) return getSurvival (dblDate1);

		int iNumQuadratures = 0;
		double dblEffectiveSurvival = 0.;
		double dblQuadratureWidth = (dblDate2 - dblDate1) / NUM_DF_QUADRATURES;

		for (double dblDate = dblDate1; dblDate <= dblDate2; dblDate += dblQuadratureWidth) {
			++iNumQuadratures;

			dblEffectiveSurvival += (getSurvival (dblDate) + getSurvival (dblDate + dblQuadratureWidth));
		}

		return dblEffectiveSurvival / (2. * iNumQuadratures);
	}

	@Override public double getEffectiveSurvival (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2) throw new java.lang.Exception ("No surv for null date");

		return getEffectiveSurvival (dt1.getJulian(), dt2.getJulian());
	}

	@Override public double getEffectiveSurvival (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("CC.getEffectiveSurvival got bad tenor");

		return getEffectiveSurvival (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor
			(strTenor1), new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor2));
	}

	@Override public double calcHazard (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2) throw new java.lang.Exception ("No hazard for null dates");

		if (dt1.getJulian() < _dblStartDate || dt2.getJulian() < _dblStartDate) return 0.;

		return 365.25 / (dt2.getJulian() - dt1.getJulian()) * java.lang.Math.log (getSurvival (dt1) /
			getSurvival (dt2));
	}

	@Override public double calcHazard (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		return calcHazard (dt, new org.drip.analytics.date.JulianDate (_dblStartDate));
	}

	@Override public double calcHazard (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CC.calcHazard got bad tenor");

		return calcHazard (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor));
	}

	@Override public double getRecovery (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("No rec for NaN date");

		for (int i = 0; i < _adblRecoveryDate.length; ++i) {
			if (dblDate < _adblRecoveryDate[i]) return _adblRecoveryRate[i];
		}

		return _adblRecoveryRate[_adblRecoveryDate.length - 1];
	}

	@Override public double getRecovery (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("No rec for null date");

		return getRecovery (dt.getJulian());
	}

	@Override public double getRecovery (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CC.getRecovery got bad tenor");

		return getRecovery (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor));
	}

	@Override public double getEffectiveRecovery (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (dblDate1 == dblDate2) return getRecovery (dblDate1);

		int iNumQuadratures = 0;
		double dblEffectiveRecovery = 0.;
		double dblQuadratureWidth = (dblDate2 - dblDate1) / NUM_DF_QUADRATURES;

		for (double dblDate = dblDate1; dblDate <= dblDate2; dblDate += dblQuadratureWidth) {
			++iNumQuadratures;

			dblEffectiveRecovery += (getRecovery (dblDate) + getRecovery (dblDate + dblQuadratureWidth));
		}

		return dblEffectiveRecovery / (2. * iNumQuadratures);
	}

	@Override public double getEffectiveRecovery (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2) throw new java.lang.Exception ("No rec for null date");

		return getEffectiveRecovery (dt1.getJulian(), dt2.getJulian());
	}

	@Override public double getEffectiveRecovery (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("CC.getEffectiveRecovery got bad tenor");

		return getEffectiveRecovery (new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor
			(strTenor1), new org.drip.analytics.date.JulianDate (_dblStartDate).addTenor (strTenor2));
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		java.lang.String strNameSer = _strName;

		if (null == strNameSer || strNameSer.isEmpty())
			strNameSer = org.drip.service.stream.Serializer.NULL_SER_STRING;

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + strNameSer +
			getFieldDelimiter() + _dblStartDate + getFieldDelimiter());

		for (int i = 0; i < _adblHazardDate.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_adblHazardDate[i] + getCollectionKeyValueDelimiter() + _adblHazardRate[i]);
		}

		sb.append (getFieldDelimiter());

		for (int i = 0; i < _adblRecoveryDate.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_adblRecoveryDate[i] + getCollectionKeyValueDelimiter() + _adblRecoveryRate[i]);
		}

		sb.append (getFieldDelimiter() + _dblSpecificDefaultDate);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ConstantForwardHazard (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String displayString()
	{
		try {
			java.lang.StringBuffer sb = new java.lang.StringBuffer();

			for (int i = 0; i < _adblHazardRate.length; ++i) {
				if (0 != i) sb.append (" | ");

				sb.append (new org.drip.analytics.date.JulianDate (_adblHazardDate[i]) + "=" +
					_adblHazardRate[i]);
			}

			return sb.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
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

		double[] adblHazardDate = new double[3];
		double[] adblHazardRate = new double[3];
		double[] adblRecoveryDate = new double[3];
		double[] adblRecoveryRate = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblHazardDate[i] = dblStart + 365. * (i + 1);
			adblHazardRate[i] = 0.01 * (i + 1);
			adblRecoveryDate[i] = dblStart + 365. * (i + 1);
			adblRecoveryRate[i] = 0.40;
		}

		ConstantForwardHazard cc = new ConstantForwardHazard (dblStart, "XXS", adblHazardRate,
			adblHazardDate, adblRecoveryRate, adblRecoveryDate, java.lang.Double.NaN);

		byte[] abCC = cc.serialize();

		System.out.println ("Input: " + new java.lang.String (abCC));

		System.out.println ("Surv[12/12/20]=" + cc.getSurvival
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")));

		ConstantForwardHazard ccDeser = new ConstantForwardHazard (abCC);

		System.out.println ("Output: " + new java.lang.String (ccDeser.serialize()));

		System.out.println ("Surv[12/12/20]=" + ccDeser.getSurvival
			(org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY ("12-DEC-2020")));
	}
}
