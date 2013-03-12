
package org.drip.product.rates;

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
 * Implementation of the Cash IR product and its contract/valuation details.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CashComponent extends org.drip.product.definition.RatesComponent {
	private double _dblNotional = 100.;
	private java.lang.String _strIR = "";
	private java.lang.String _strCode = "";
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private org.drip.product.params.FactorSchedule _notlSchedule = null;
	private org.drip.param.valuation.CashSettleParams _settleParams = null;

	public CashComponent (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final java.lang.String strIR)
		throws java.lang.Exception
	{
		if (null == dtEffective || null == dtMaturity || null == (_strIR = strIR) || strIR.isEmpty())
			throw new java.lang.Exception ("Invalid Cash.CreateCash params!");

		if ((_dblMaturity = dtMaturity.getJulian()) <= (_dblEffective = dtEffective.getJulian()))
			throw new java.lang.Exception ("Cash: mat <= effective!");

		_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();
	}

	@Override protected java.util.Map<java.lang.String, java.lang.Double> calibMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		return null;
	}

	/**
	 * Cash de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if Cash cannot be properly de-serialized
	 */

	public CashComponent (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Cash de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Empty state");

		java.lang.String strSerializedCash = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCash || strSerializedCash.isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedCash,
			getFieldDelimiter());

		if (null == astrField || 7 > astrField.length)
			throw new java.lang.Exception ("Cash de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]).doubleValue();

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate IR curve name");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strIR = astrField[2];
		else
			_strIR = "";

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate cash code");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strCode = astrField[3];
		else
			_strCode = "";

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate maturity date");

		_dblMaturity = new java.lang.Double (astrField[4]).doubleValue();

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate effective date");

		_dblEffective = new java.lang.Double (astrField[5]).doubleValue();

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate notional schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			_notlSchedule = null;
		else
			_notlSchedule = new org.drip.product.params.FactorSchedule (astrField[6].getBytes());

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("Cash de-serializer: Cannot locate cash settle params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			_settleParams = null;
		else
			_settleParams = new org.drip.param.valuation.CashSettleParams (astrField[7].getBytes());
	}

	@Override public java.lang.String getPrimaryCode()
	{
		return _strCode;
	}

	@Override public void setPrimaryCode (final java.lang.String strCode)
	{
		_strCode = strCode;
	}

	@Override public java.lang.String getComponentName()
	{
		return "CD=" + org.drip.analytics.date.JulianDate.fromJulian (_dblMaturity);
	}

	@Override public java.lang.String getTreasuryCurveName()
	{
		return "";
	}

	@Override public java.lang.String getEDSFCurveName()
	{
		return "";
	}

	@Override public double getInitialNotional()
	{
		return _dblNotional;
	}

	@Override public double getNotional (
		final double dblDate)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Bad date into getNotional");

		return _notlSchedule.getFactor (dblDate);
	}

	@Override public double getNotional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate1) ||
			!org.drip.math.common.NumberUtil.IsValid (dblDate2))
			throw new java.lang.Exception ("Bad date into getNotional");

		return _notlSchedule.getFactor (dblDate1, dblDate2);
	}

	@Override public double getCoupon (
		final double dblValue,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		return 0.;
	}

	@Override public boolean setCurves (
		final java.lang.String strIR,
		final java.lang.String strIRTSY,
		final java.lang.String strCC)
	{
		if (null == strIR || strIR.isEmpty()) return false;

		_strIR = strIR;
		return true;
	}

	@Override public java.lang.String getIRCurveName()
	{
		return _strIR;
	}

	@Override public java.lang.String getCreditCurveName()
	{
		return "";
	}

	@Override public org.drip.analytics.date.JulianDate getEffectiveDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblEffective);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getMaturityDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblMaturity);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getFirstCouponDate()
	{
		return null;
	}

	@Override public java.util.List<org.drip.analytics.period.Period> getCouponPeriod()
	{
		return null;
	}

	@Override public org.drip.param.valuation.CashSettleParams getCashSettleParams()
	{
		return _settleParams;
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve()) return null;

		long lStart = System.nanoTime();

		java.util.Map<java.lang.String, java.lang.Double> mapResult = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		if (valParams._dblValue >= _dblMaturity) return mapResult;

		try {
			double dblCashSettle = valParams._dblCashPay;

			if (null != _settleParams) dblCashSettle = _settleParams.cashSettleDate (valParams._dblValue);

			mapResult.put ("PV", mktParams.getDiscountCurve().getDF (_dblMaturity) /
				mktParams.getDiscountCurve().getDF (_dblEffective) / mktParams.getDiscountCurve().getDF
					(dblCashSettle) * _dblNotional * 0.01 * getNotional (_dblEffective, _dblMaturity));

			mapResult.put ("Price", 100. * (1 - mktParams.getDiscountCurve().calcImpliedRate (_dblEffective,
				_dblMaturity)));

			mapResult.put ("Rate", mktParams.getDiscountCurve().calcImpliedRate (_dblEffective,
				_dblMaturity));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcPVDFMicroJack (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == mktParams || null ==
			mktParams.getDiscountCurve())
			return null;

		try {
			java.util.Map<java.lang.String, java.lang.Double> mapMeasures = value (valParams, pricerParams,
				mktParams, quotingParams);

			if (null == mapMeasures) return null;

			double dblPV = mapMeasures.get ("PV");

			org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

			org.drip.math.algodiff.WengertJacobian wjDFDF = dc.getDFJacobian (_dblMaturity);

			if (null == wjDFDF) return null;

			org.drip.math.algodiff.WengertJacobian wjPVDFMicroJack = new
				org.drip.math.algodiff.WengertJacobian (1, wjDFDF.numParameters());

			for (int k = 0; k < wjDFDF.numParameters(); ++k) {
				if (!wjPVDFMicroJack.accumulatePartialFirstDerivative (0, k, wjDFDF.getFirstDerivative (0,
					k)))
					return null;
			}

			return adjustPVDFMicroJackForCashSettle (valParams._dblCashPay, dblPV, dc, wjPVDFMicroJack) ?
				wjPVDFMicroJack : null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcQuoteDFMicroJack (
		final java.lang.String strQuote,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == strQuote || null == mktParams
			|| null == mktParams.getDiscountCurve())
			return null;

		if ("Rate".equalsIgnoreCase (strQuote)) {
			try {
				org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

				org.drip.math.algodiff.WengertJacobian wjDF = dc.getDFJacobian (_dblMaturity);

				if (null == wjDF) return null;

				org.drip.math.algodiff.WengertJacobian wjDFMicroJack = new
					org.drip.math.algodiff.WengertJacobian (1, wjDF.numParameters());

				for (int k = 0; k < wjDF.numParameters(); ++k) {
					if (!wjDFMicroJack.accumulatePartialFirstDerivative (0, k, -365.25 / (_dblMaturity -
						_dblEffective) / dc.getDF (_dblMaturity) * wjDF.getFirstDerivative (0, k)))
						return null;
				}

				return wjDFMicroJack;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		sb.append (_dblNotional + getFieldDelimiter());

		if (null == _strIR || _strIR.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIR + getFieldDelimiter());

		if (null == _strCode || _strCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCode + getFieldDelimiter());

		sb.append (_dblMaturity + getFieldDelimiter());

		sb.append (_dblEffective + getFieldDelimiter());

		if (null == _notlSchedule)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlSchedule.serialize()) + getFieldDelimiter());

		if (null == _settleParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (new java.lang.String (_settleParams.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CashComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CashComponent cash = new CashComponent (org.drip.analytics.date.JulianDate.Today(),
			org.drip.analytics.date.JulianDate.Today().addTenor ("1Y"), "AUD");

		byte[] abCash = cash.serialize();

		System.out.println (new java.lang.String (abCash));

		CashComponent cashDeser = new CashComponent (abCash);

		System.out.println (new java.lang.String (cashDeser.serialize()));
	}
}
