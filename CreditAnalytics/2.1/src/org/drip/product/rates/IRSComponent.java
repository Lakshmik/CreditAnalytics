
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
 * Implements the InterestRateSwap product contract/valuation details.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IRSComponent extends org.drip.product.definition.RatesComponent {
	private double _dblNotional = 100.;
	private double _dblCoupon = 0.0001;
	private java.lang.String _strIR = "";
	private java.lang.String _strCode = "";
	private boolean _bApplyAccEOMAdj = false;
	private boolean _bApplyCpnEOMAdj = false;
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private java.lang.String _strFloatingRateIndex = "USD-LIBOR-6M";
	private org.drip.product.params.FactorSchedule _notlSchedule = null;
	private org.drip.param.valuation.CashSettleParams _settleParams = null;
	private java.util.List<org.drip.analytics.period.Period> _lPeriods = null;

	@Override protected java.util.Map<java.lang.String, java.lang.Double> calibMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		return null;
	}

	public IRSComponent (
		final double dblEffective,
		final double dblMaturity,
		final double dblCoupon,
		final int iFreq,
		final java.lang.String strCouponDC,
		final java.lang.String strAccrualDC,
		final java.lang.String strFloatingRateIndex,
		final boolean bFullStub,
		final org.drip.analytics.daycount.DateAdjustParams dapEffective,
		final org.drip.analytics.daycount.DateAdjustParams dapMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodStart,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualStart,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapPay,
		final org.drip.analytics.daycount.DateAdjustParams dapReset,
		final org.drip.product.params.FactorSchedule notlSchedule,
		final double dblNotional,
		final java.lang.String strIR,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == strIR || strIR.isEmpty() || !org.drip.math.common.NumberUtil.IsValid (dblEffective) ||
			!org.drip.math.common.NumberUtil.IsValid (dblMaturity) ||
				!org.drip.math.common.NumberUtil.IsValid (dblCoupon) ||
					!org.drip.math.common.NumberUtil.IsValid (dblNotional))
			throw new java.lang.Exception ("Invalid IRS ctr params!");

		_strIR = strIR;
		_dblCoupon = dblCoupon;
		_dblNotional = dblNotional;

		if (null == (_notlSchedule = notlSchedule))
			_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();

		_strFloatingRateIndex = strIR + "-LIBOR-6M";

		if (null != strFloatingRateIndex && strFloatingRateIndex.isEmpty())
			_strFloatingRateIndex = strFloatingRateIndex;

		_lPeriods = org.drip.analytics.period.CouponPeriod.GeneratePeriodsBackward (
			_dblEffective = dblEffective, // Effective
			_dblMaturity = dblMaturity, // Maturity
			dapEffective, // Effective DAP
			dapMaturity, // Maturity DAP
			dapPeriodStart, // Period Start DAP
			dapPeriodEnd, // Period End DAP
			dapAccrualStart, // Accrual Start DAP
			dapAccrualEnd, // Accrual End DAP
			dapPay, // Pay DAP
			dapReset, // Reset DAP
			iFreq, // Coupon Freq
			strCouponDC, // Coupon Day Count
			_bApplyCpnEOMAdj,
			strAccrualDC, // Accrual Day Count
			_bApplyAccEOMAdj,
			bFullStub, // Full First Coupon Period?
			false, // Merge the first 2 Periods - create a long stub?
			strCalendar);
	}

	/**
	 * InterestRateSwap de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if InterestRateSwap cannot be properly de-serialized
	 */

	public IRSComponent (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Empty state");

		java.lang.String strSerializedInterestRateSwap = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedInterestRateSwap || strSerializedInterestRateSwap.isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedInterestRateSwap, getFieldDelimiter());

		if (null == astrField || 13 > astrField.length)
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]).doubleValue();

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate coupon");

		_dblCoupon = new java.lang.Double (astrField[2]).doubleValue();

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate IR curve name");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strIR = astrField[3];
		else
			_strIR = "";

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate code");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_strCode = astrField[4];
		else
			_strCode = "";

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("InterestRateSwap de-serializer: Cannot locate Apply Acc EOM Adj");

		_bApplyAccEOMAdj = new java.lang.Boolean (astrField[5]).booleanValue();

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			throw new java.lang.Exception
				("InterestRateSwap de-serializer: Cannot locate Apply Cpn EOM Adj");

		_bApplyCpnEOMAdj = new java.lang.Boolean (astrField[6]).booleanValue();

		if (null == astrField[7] || astrField[7].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate maturity date");

		_dblMaturity = new java.lang.Double (astrField[7]).doubleValue();

		if (null == astrField[8] || astrField[8].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate effective date");

		_dblEffective = new java.lang.Double (astrField[8]).doubleValue();

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate rate index");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			_strFloatingRateIndex = astrField[9];
		else
			_strFloatingRateIndex = "";

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception
				("InterestRateSwap de-serializer: Cannot locate notional schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[10]))
			_notlSchedule = null;
		else
			_notlSchedule = new org.drip.product.params.FactorSchedule (astrField[10].getBytes());

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception
				("InterestRateSwap de-serializer: Cannot locate cash settle params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[11]))
			_settleParams = null;
		else
			_settleParams = new org.drip.param.valuation.CashSettleParams (astrField[11].getBytes());

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception ("InterestRateSwap de-serializer: Cannot locate the periods");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[12]))
			_lPeriods = null;
		else {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[12],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					if (null == _lPeriods)
						_lPeriods = new java.util.ArrayList<org.drip.analytics.period.Period>();

					_lPeriods.add (new org.drip.analytics.period.Period (astrRecord[i].getBytes()));
				}
			}
		}
	}

	@Override public java.lang.String getPrimaryCode()
	{
		return _strCode;
	}

	@Override public void setPrimaryCode (
		final java.lang.String strCode)
	{
		_strCode = strCode;
	}

	@Override public java.lang.String getComponentName()
	{
		return "IRS=" + org.drip.analytics.date.JulianDate.fromJulian (_dblMaturity);
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

	@Override public boolean setCurves (
		final java.lang.String strIR,
		final java.lang.String strIRTSY, final
		java.lang.String strCC)
	{
		if (null == strIR || strIR.isEmpty()) return false;

		_strIR = strIR;
		return true;
	}

	@Override public double getCoupon (
		final double dblValue,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		return _dblCoupon;
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
		try {
			return new org.drip.analytics.date.JulianDate (_lPeriods.get (0).getEndDate());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.util.List<org.drip.analytics.period.Period> getCouponPeriod()
	{
		return _lPeriods;
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

		double dblAccrued01 = 0.;
		double dblDirtyDV01 = 0.;
		boolean bFirstPeriod = true;
		double dblDirtyFloatingPV = 0.;
		double dblResetDate = java.lang.Double.NaN;
		double dblResetRate = java.lang.Double.NaN;

		for (org.drip.analytics.period.Period period : _lPeriods) {
			double dblFloatingRate = 0.;
			double dblDirtyPeriodDV01 = java.lang.Double.NaN;

			double dblPeriodPayDate = period.getPayDate();

			if (dblPeriodPayDate < valParams._dblValue) continue;

			try {
				if (bFirstPeriod) {
					bFirstPeriod = false;

					if (null == mktParams.getFixings() || null == mktParams.getFixings().get (new
						org.drip.analytics.date.JulianDate (period.getResetDate())) || null ==
							mktParams.getFixings().get (new org.drip.analytics.date.JulianDate
								(period.getResetDate())).get (_strFloatingRateIndex)) {
						System.out.println ("IRS reset for index " + _strFloatingRateIndex +
							" and reset date " + org.drip.analytics.date.JulianDate.fromJulian
								(period.getResetDate()) + " not found!");

						return null;
					}

					if (period.getStartDate() < valParams._dblValue)
						dblAccrued01 = period.getAccrualDCF (valParams._dblValue) * 0.01 * getNotional
							(period.getAccrualStartDate(), valParams._dblValue);

					dblResetRate = dblFloatingRate = mktParams.getFixings().get (new
						org.drip.analytics.date.JulianDate (period.getResetDate())).get
							(_strFloatingRateIndex);

					dblResetDate = period.getResetDate();
				} else
					dblFloatingRate = mktParams.getDiscountCurve().calcLIBOR (period.getStartDate(),
						period.getEndDate());

				dblDirtyPeriodDV01 = 0.01 * period.getCouponDCF() * mktParams.getDiscountCurve().getDF
					(dblPeriodPayDate) * getNotional (period.getAccrualStartDate(), period.getEndDate());
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			dblDirtyDV01 += dblDirtyPeriodDV01;
			dblDirtyFloatingPV += dblDirtyPeriodDV01 * dblFloatingRate;
		}

		double dblCashPayDF = java.lang.Double.NaN;

		try {
			double dblCashSettle = valParams._dblCashPay;

			if (null != _settleParams) dblCashSettle = _settleParams.cashSettleDate (valParams._dblValue);

			dblCashPayDF = mktParams.getDiscountCurve().getDF (dblCashSettle);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		dblDirtyDV01 /= dblCashPayDF;
		dblDirtyFloatingPV /= dblCashPayDF;
		double dblNotlFactor = _dblNotional * 0.01;
		double dblCleanDV01 = dblDirtyDV01 - dblAccrued01;
		double dblCleanFloatingPV = dblDirtyFloatingPV - dblAccrued01 * dblResetRate;

		java.util.Map<java.lang.String, java.lang.Double> mapResult = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		mapResult.put ("ResetDate", dblResetDate);

		mapResult.put ("ResetRate", dblResetRate);

		mapResult.put ("Accrued01", dblAccrued01 * dblNotlFactor);

		mapResult.put ("FixAccrued", dblAccrued01 * _dblCoupon * dblNotlFactor);

		mapResult.put ("FloatAccrued", dblAccrued01 * dblResetRate * dblNotlFactor);

		mapResult.put ("DV01", dblCleanDV01 * dblNotlFactor);

		mapResult.put ("CleanDV01", dblCleanDV01 * dblNotlFactor);

		mapResult.put ("DirtyDV01", dblDirtyDV01 * dblNotlFactor);

		mapResult.put ("CleanFixedPV", dblCleanDV01 * _dblCoupon * dblNotlFactor);

		mapResult.put ("DirtyFixedPV", dblDirtyDV01 * _dblCoupon * dblNotlFactor);

		mapResult.put ("CleanFloatingPV", dblCleanFloatingPV * dblNotlFactor);

		mapResult.put ("DirtyFloatingPV", dblDirtyFloatingPV * dblNotlFactor);

		mapResult.put ("PV", (dblCleanDV01 * _dblCoupon - dblCleanFloatingPV) * dblNotlFactor);

		mapResult.put ("CleanPV", (dblCleanDV01 * _dblCoupon - dblCleanFloatingPV) * dblNotlFactor);

		mapResult.put ("DirtyPV", (dblDirtyDV01 * _dblCoupon - dblDirtyFloatingPV) * dblNotlFactor);

		mapResult.put ("Upfront", (dblCleanDV01 * _dblCoupon - dblCleanFloatingPV) * dblNotlFactor);

		mapResult.put ("FairPremium", dblCleanFloatingPV / dblCleanDV01);

		mapResult.put ("Rate", dblCleanFloatingPV / dblCleanDV01);

		mapResult.put ("SwapRate", dblCleanFloatingPV / dblCleanDV01);

		double dblValueNotional = java.lang.Double.NaN;

		try {
			dblValueNotional = getNotional (valParams._dblValue);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (org.drip.math.common.NumberUtil.IsValid (dblValueNotional)) {
			mapResult.put ("Price", 100. * (1. + ((dblCleanDV01 * _dblCoupon - dblCleanFloatingPV) /
				_dblNotional / dblValueNotional)));

			mapResult.put ("CleanPrice", 100. * (1. + ((dblCleanDV01 * _dblCoupon - dblCleanFloatingPV) /
				_dblNotional / dblValueNotional)));
		}

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcPVDFMicroJack (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == mktParams || null ==
			mktParams.getDiscountCurve())
			return null;

		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = value (valParams, pricerParams,
			mktParams, quotingParams);

		if (null == mapMeasures) return null;

		double dblPV = mapMeasures.get ("PV");

		double dblParSwapRate = mapMeasures.get ("SwapRate");

		try {
			org.drip.math.calculus.WengertJacobian wjPVDFMicroJack = null;

			org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

			for (org.drip.analytics.period.Period p : _lPeriods) {
				double dblPeriodPayDate = p.getPayDate();

				if (dblPeriodPayDate < valParams._dblValue) continue;

				org.drip.math.calculus.WengertJacobian wjPeriodFwdRateDF = dc.getForwardRateJacobian
					(p.getStartDate(), p.getEndDate());

				org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian (dblPeriodPayDate);

				if (null == wjPeriodFwdRateDF || null == wjPeriodPayDFDF) continue;

				double dblForwardRate = dc.calcLIBOR (p.getStartDate(), p.getEndDate());

				double dblPeriodPayDF = dc.getDF (dblPeriodPayDate);

				if (null == wjPVDFMicroJack)
					wjPVDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
						wjPeriodFwdRateDF.numParameters());

				double dblPeriodNotional = getNotional (p.getStartDate(), p.getEndDate());

				double dblPeriodDCF = p.getCouponDCF();

				for (int k = 0; k < wjPeriodFwdRateDF.numParameters(); ++k) {
					double dblPeriodPVDFMicroJack = dblPeriodDCF * ((dblParSwapRate - dblForwardRate) *
						wjPeriodPayDFDF.getFirstDerivative (0, k) - dblPeriodPayDF *
							wjPeriodFwdRateDF.getFirstDerivative (0, k));

					if (!wjPVDFMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodNotional *
						dblPeriodDCF * dblPeriodPVDFMicroJack))
						return null;
				}
			}

			return adjustPVDFMicroJackForCashSettle (valParams._dblCashPay, dblPV, dc, wjPVDFMicroJack) ?
				wjPVDFMicroJack : null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcQuoteDFMicroJack (
		final java.lang.String strQuote,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == strQuote || null == mktParams
			|| null == mktParams.getDiscountCurve())
			return null;

		if ("Rate".equalsIgnoreCase (strQuote) || "SwapRate".equalsIgnoreCase (strQuote)) {
			java.util.Map<java.lang.String, java.lang.Double> mapMeasures = value (valParams, pricerParams,
				mktParams, quotingParams);

			if (null == mapMeasures) return null;

			double dblDirtyDV01 = mapMeasures.get ("DirtyDV01");

			double dblParSwapRate = mapMeasures.get ("SwapRate");

			try {
				org.drip.math.calculus.WengertJacobian wjSwapRateDFMicroJack = null;

				org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

				for (org.drip.analytics.period.Period p : _lPeriods) {
					double dblPeriodPayDate = p.getPayDate();

					if (dblPeriodPayDate < valParams._dblValue) continue;

					org.drip.math.calculus.WengertJacobian wjPeriodFwdRateDF = dc.getForwardRateJacobian
						(p.getStartDate(), p.getEndDate());

					org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian
						(dblPeriodPayDate);

					if (null == wjPeriodFwdRateDF || null == wjPeriodPayDFDF) continue;

					double dblForwardRate = dc.calcLIBOR (p.getStartDate(), p.getEndDate());

					double dblPeriodPayDF = dc.getDF (dblPeriodPayDate);

					if (null == wjSwapRateDFMicroJack)
						wjSwapRateDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
							wjPeriodFwdRateDF.numParameters());

					double dblPeriodNotional = getNotional (p.getStartDate(), p.getEndDate());

					double dblPeriodDCF = p.getCouponDCF();

					for (int k = 0; k < wjPeriodFwdRateDF.numParameters(); ++k) {
						double dblPeriodMicroJack = (dblForwardRate - dblParSwapRate) *
							wjPeriodPayDFDF.getFirstDerivative (0, k) + dblPeriodPayDF *
								wjPeriodFwdRateDF.getFirstDerivative (0, k);

						if (!wjSwapRateDFMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodNotional
							* dblPeriodDCF * dblPeriodMicroJack / dblDirtyDV01))
							return null;
					}
				}

				return wjSwapRateDFMicroJack;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "!";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "&";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		sb.append (_dblNotional + getFieldDelimiter());

		sb.append (_dblCoupon + getFieldDelimiter());

		if (null == _strIR || _strIR.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIR + getFieldDelimiter());

		if (null == _strCode || _strCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCode + getFieldDelimiter());

		sb.append (_bApplyAccEOMAdj + getFieldDelimiter());

		sb.append (_bApplyCpnEOMAdj + getFieldDelimiter());

		sb.append (_dblMaturity + getFieldDelimiter());

		sb.append (_dblEffective + getFieldDelimiter());

		if (null == _strFloatingRateIndex || _strFloatingRateIndex.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strFloatingRateIndex + getFieldDelimiter());

		if (null == _notlSchedule)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlSchedule.serialize()) + getFieldDelimiter());

		if (null == _settleParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_settleParams.serialize()) + getFieldDelimiter());

		if (null == _lPeriods)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbPeriods = new java.lang.StringBuffer();

			for (org.drip.analytics.period.Period p : _lPeriods) {
				if (null == p) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbPeriods.append (getCollectionRecordDelimiter());

				sbPeriods.append (new java.lang.String (p.serialize()));
			}

			if (sbPeriods.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbPeriods.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new IRSComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		IRSComponent irs = new org.drip.product.rates.IRSComponent
			(org.drip.analytics.date.JulianDate.Today().getJulian(),
				org.drip.analytics.date.JulianDate.Today().addTenor ("4Y").getJulian(), 0.03, 2, "30/360",
					"30/360", "JPY-LIBOR", false, null, null, null, null, null, null, null, null, null, 100.,
						"JPY", "JPY");

		byte[] abIRS = irs.serialize();

		System.out.println (new java.lang.String (abIRS));

		IRSComponent irsDeser = new IRSComponent (abIRS);

		System.out.println (new java.lang.String (irsDeser.serialize()));
	}
}
