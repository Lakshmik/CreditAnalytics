
package org.drip.product.credit;

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
 *	This class implements the credit default swap product contract details. Contains effective date, maturity
 *		date, coupon, coupon day count, coupon frequency, contingent credit, currency, basket notional,
 *		credit valuation parameters, and optionally the outstanding notional schedule.
 *
 * @author Lakshmi Krishnamurthy
 *
 */

public class CDSComponent extends org.drip.product.definition.CreditDefaultSwap {
	private double _dblNotional = 100.;
	private java.lang.String _strIR = "";
	private java.lang.String _strCode = "";
	private java.lang.String _strName = "";
	private boolean _bApplyAccEOMAdj = false;
	private boolean _bApplyCpnEOMAdj = false;
	private double _dblCoupon = java.lang.Double.NaN;
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private org.drip.product.params.FactorSchedule _notlSchedule = null;
	private org.drip.product.params.CreditSetting _crValParams = null;
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

	private java.util.Map<java.lang.String, java.lang.Double> calcMeasureSet (
		final java.lang.String strMeasureSetPrefix,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == pricerParams || null == mktParams || null ==
			mktParams.getCreditCurve() || null == mktParams.getDiscountCurve())
			return null;

		long lStart = System.nanoTime();

		double dblDV01 = 0.;
		double dblLossPV = 0.;
		double dblExpLoss = 0.;
		double dblAccrued01 = 0.;
		double dblLossNoRecPV = 0.;
		double dblExpLossNoRec = 0.;
		boolean bFirstPeriod = true;
		double dblAccrualDays = java.lang.Double.NaN;

		try {
			for (org.drip.analytics.period.Period period : _lPeriods) {
				if (period.getPayDate() < valParams._dblValue) continue;

				if (bFirstPeriod) {
					bFirstPeriod = false;

					if (period.getStartDate() < valParams._dblValue) {
						dblAccrualDays = valParams._dblValue - period.getAccrualStartDate();

						dblAccrued01 = period.getAccrualDCF (valParams._dblValue) * 0.01 * getNotional
							(period.getAccrualStartDate(), valParams._dblValue);
					}
				}

				double dblSurvProb = java.lang.Double.NaN;

				if (pricerParams._bSurvToPayDate)
					dblSurvProb = mktParams.getCreditCurve().getSurvival (period.getPayDate());
				else
					dblSurvProb = mktParams.getCreditCurve().getSurvival (period.getEndDate());

				dblDV01 += 0.01 * period.getCouponDCF() * mktParams.getDiscountCurve().getDF
					(period.getPayDate()) * dblSurvProb * getNotional (period.getAccrualStartDate(),
						period.getEndDate());

				java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
					org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
						pricerParams, period, period.getEndDate(), mktParams);

				if (null == sLPSub || 0 == sLPSub.size()) continue;

				for (org.drip.analytics.period.LossPeriodCurveFactors lp : sLPSub) {
					if (null == lp) continue;

					double dblSubPeriodEnd = lp.getEndDate();

					double dblSubPeriodStart = lp.getStartDate();

					double dblSubPeriodDF = mktParams.getDiscountCurve().getEffectiveDF (dblSubPeriodStart +
						_crValParams._iDefPayLag, dblSubPeriodEnd + _crValParams._iDefPayLag);

					double dblSubPeriodNotional = getNotional (dblSubPeriodStart, dblSubPeriodEnd);

					double dblSubPeriodSurvival = mktParams.getCreditCurve().getSurvival (dblSubPeriodStart)
						- mktParams.getCreditCurve().getSurvival (dblSubPeriodEnd);

					double dblRec = _crValParams._bUseCurveRec ?
						mktParams.getCreditCurve().getEffectiveRecovery (dblSubPeriodStart, dblSubPeriodEnd)
							: _crValParams._dblRecovery;

					double dblSubPeriodExpLoss = (1 - dblRec) * 100. * dblSubPeriodSurvival *
						dblSubPeriodNotional;
					double dblSubPeriodExpLossNoRec = 100. * dblSubPeriodSurvival * dblSubPeriodNotional;
					dblLossPV += dblSubPeriodExpLoss * dblSubPeriodDF;
					dblLossNoRecPV += dblSubPeriodExpLossNoRec * dblSubPeriodDF;
					dblExpLoss += dblSubPeriodExpLoss;
					dblExpLossNoRec += dblSubPeriodExpLossNoRec;

					dblDV01 += 0.01 * lp.getAccrualDCF() * dblSubPeriodSurvival * dblSubPeriodDF *
						dblSubPeriodNotional;
				}
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
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

		dblDV01 /= dblCashPayDF;
		dblLossPV /= dblCashPayDF;
		dblLossNoRecPV /= dblCashPayDF;
		double dblNotlFactor = _dblNotional * 0.01;
		double dblCleanDV01 = dblDV01 - dblAccrued01;
		double dblPV = dblDV01 * 10000. * _dblCoupon - dblLossPV;
		double dblCleanPV = (dblDV01 - dblAccrued01) * 10000. *_dblCoupon - dblLossPV;

		java.util.Map<java.lang.String, java.lang.Double> mapResult = new java.util.TreeMap<java.lang.String,
			java.lang.Double>();

		mapResult.put (strMeasureSetPrefix + "PV", dblPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "DV01", dblDV01 * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "DirtyDV01", dblDV01 * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "DirtyPV", dblPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "LossPV", dblLossPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "LossNoRecPV", dblLossNoRecPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "ExpLoss", dblExpLoss * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "ExpLossNoRec", dblExpLossNoRec * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "CleanPV", dblCleanPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "Upfront", dblCleanPV * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "ParSpread", dblLossPV / dblCleanDV01);

		mapResult.put (strMeasureSetPrefix + "FairPremium", dblLossPV / dblCleanDV01);

		mapResult.put (strMeasureSetPrefix + "Accrued01", dblAccrued01 * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "CleanDV01", dblCleanDV01 * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "PremiumPV", dblDV01 * _dblCoupon * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "Accrued", dblAccrued01 * _dblCoupon * dblNotlFactor);

		mapResult.put (strMeasureSetPrefix + "AccrualDays", dblAccrualDays);

		try {
			mapResult.put (strMeasureSetPrefix + "LossOnInstantaneousDefault", _dblNotional * (1. -
				mktParams.getCreditCurve().getRecovery (valParams._dblValue)));

			mapResult.put (strMeasureSetPrefix + "Price", 100. * (1. + (dblPV / _dblNotional / getNotional
				(valParams._dblValue))));

			mapResult.put (strMeasureSetPrefix + "CleanPrice", 100. * (1. + (dblCleanPV / _dblNotional /
				getNotional (valParams._dblValue))));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		mapResult.put (strMeasureSetPrefix + "CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	private org.drip.math.calculus.WengertJacobian calcPeriodOnDefaultPVDFMicroJack (
		final double dblFairPremium,
		final org.drip.analytics.period.Period period,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
			org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
				pricerParams, period, period.getEndDate(), mktParams);

		if (null == sLPSub || 0 == sLPSub.size()) return null;

		int iNumParameters = 0;
		org.drip.math.calculus.WengertJacobian wjPeriodOnDefaultPVDF = null;

		for (org.drip.analytics.period.LossPeriodCurveFactors lpcf : sLPSub) {
			org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF =
				mktParams.getDiscountCurve().getDFJacobian (0.5 * (lpcf.getStartDate() + lpcf.getEndDate()) +
					_crValParams._iDefPayLag);

			try {
				if (null == wjPeriodOnDefaultPVDF)
					wjPeriodOnDefaultPVDF = new org.drip.math.calculus.WengertJacobian (1, iNumParameters =
						wjPeriodPayDFDF.numParameters());

				double dblPeriodIncrementalCashFlow = getNotional (lpcf.getStartDate(), lpcf.getEndDate()) *
					(dblFairPremium * lpcf.getAccrualDCF() - 1. + lpcf.getEffectiveRecovery()) *
						(lpcf.getStartSurvival() - lpcf.getEndSurvival());

				for (int k = 0; k < iNumParameters; ++k) {
					if (!wjPeriodOnDefaultPVDF.accumulatePartialFirstDerivative (0, k,
						wjPeriodPayDFDF.getFirstDerivative (0, k) * dblPeriodIncrementalCashFlow))
						return null;
				}
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return wjPeriodOnDefaultPVDF;
	}

	private PeriodLossMicroJack calcPeriodLossMicroJack (
		final org.drip.analytics.period.Period period,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
			org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
				pricerParams, period, period.getEndDate(), mktParams);

		if (null == sLPSub || 0 == sLPSub.size()) return null;

		PeriodLossMicroJack plmj = null;

		for (org.drip.analytics.period.LossPeriodCurveFactors lpcf : sLPSub) {
			double dblPeriodNotional = java.lang.Double.NaN;
			double dblPeriodIncrementalLoss = java.lang.Double.NaN;
			double dblPeriodIncrementalAccrual = java.lang.Double.NaN;
			double dblPeriodIncrementalSurvival = java.lang.Double.NaN;

			double dblPeriodEffectiveDate = 0.5 * (lpcf.getStartDate() + lpcf.getEndDate());

			org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF =
				mktParams.getDiscountCurve().getDFJacobian (dblPeriodEffectiveDate +
					_crValParams._iDefPayLag);

			try {
				dblPeriodNotional = getNotional (lpcf.getStartDate(), lpcf.getEndDate());

				dblPeriodIncrementalSurvival = lpcf.getStartSurvival() - lpcf.getEndSurvival();

				dblPeriodIncrementalLoss = dblPeriodNotional * (1. - lpcf.getEffectiveRecovery()) *
					dblPeriodIncrementalSurvival;

				dblPeriodIncrementalAccrual = dblPeriodNotional * lpcf.getAccrualDCF() *
					dblPeriodIncrementalSurvival;

				if (null == plmj) plmj = new PeriodLossMicroJack (wjPeriodPayDFDF.numParameters());

				plmj._dblAccrOnDef01 += dblPeriodIncrementalAccrual * mktParams.getDiscountCurve().getDF
					(dblPeriodEffectiveDate + _crValParams._iDefPayLag);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			for (int k = 0; k < wjPeriodPayDFDF.numParameters(); ++k) {
				if (!plmj._wjLossPVMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodIncrementalLoss
					* wjPeriodPayDFDF.getFirstDerivative (0, k)))
					return null;

				if (!plmj._wjAccrOnDef01MicroJack.accumulatePartialFirstDerivative (0, k,
					dblPeriodIncrementalAccrual * wjPeriodPayDFDF.getFirstDerivative (0, k)))
					return null;
			}
		}

		return plmj;
	}

	/**
	 * Most generic CDS creation functionality
	 * 
	 * @param dblEffective Effective Date
	 * @param dblMaturity Maturity Date
	 * @param dblCoupon Coupon
	 * @param iFreq Frequency
	 * @param strCouponDC Coupon DC
	 * @param strAccrualDC Accrual DC
	 * @param strFloatingRateIndex Floating Rate Index
	 * @param bConvCDS Is CDS Conventional
	 * @param dapEffective Effective DAP
	 * @param dapMaturity Maturity DAP
	 * @param dapPeriodStart Period Start DAP
	 * @param dapPeriodEnd Period End DAP
	 * @param dapAccrualStart Accrual Start DAP
	 * @param dapAccrualEnd Accrual End DAP
	 * @param dapPay Pay DAP
	 * @param dapReset Reset DAP
	 * @param notlSchedule Notional Schedule
	 * @param dblNotional Notional Amount
	 * @param strIR IR Curve Name
	 * @param crValParams Credit Valuation Parameters
	 * @param strCalendar Calendar
	 * 
	 * @throws java.lang.Exception
	 */

	public CDSComponent (
		final double dblEffective,
		final double dblMaturity,
		final double dblCoupon,
		final int iFreq,
		final java.lang.String strCouponDC,
		final java.lang.String strAccrualDC,
		final java.lang.String strFloatingRateIndex,
		final boolean bConvCDS,
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
		final org.drip.product.params.CreditSetting crValParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == strIR || strIR.isEmpty() || null == crValParams ||
			!org.drip.math.common.NumberUtil.IsValid (dblEffective) ||
				!org.drip.math.common.NumberUtil.IsValid (dblMaturity) ||
					!org.drip.math.common.NumberUtil.IsValid (dblCoupon))
			throw new java.lang.Exception ("Invalid CDS ctr params!");

		_strIR = strIR;
		_dblCoupon = dblCoupon;
		_dblNotional = dblNotional;
		_crValParams = crValParams;

		if (null == (_notlSchedule = notlSchedule))
			_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();

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
			bConvCDS, // Full First Coupon Period?
			false, // Merge the first 2 Periods - create a long stub?
			strCalendar);
	}

	/**
	 * CreditDefaultSwap de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CreditDefaultSwap cannot be properly de-serialized
	 */

	public CDSComponent (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Empty state");

		java.lang.String strSerializedCreditDefaultSwap = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCreditDefaultSwap || strSerializedCreditDefaultSwap.isEmpty())
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCreditDefaultSwap, getFieldDelimiter());

		if (null == astrField || 13 > astrField.length)
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate IR curve name");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strIR = astrField[2];
		else
			_strIR = "";

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate code");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strCode = astrField[3];
		else
			_strCode = "";

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("CreditDefaultSwap de-serializer: Cannot locate Apply Acc EOM Adj");

		_bApplyAccEOMAdj = new java.lang.Boolean (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("CreditDefaultSwap de-serializer: Cannot locate Apply Cpn EOM Adj");

		_bApplyCpnEOMAdj = new java.lang.Boolean (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate coupon");

		_dblCoupon = new java.lang.Double (astrField[6]);

		if (null == astrField[7] || astrField[7].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate maturity date");

		_dblMaturity = new java.lang.Double (astrField[7]);

		if (null == astrField[8] || astrField[8].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate effective date");

		_dblEffective = new java.lang.Double (astrField[8]);

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception
				("CreditDefaultSwap de-serializer: Cannot locate notional schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			_notlSchedule = null;
		else
			_notlSchedule = new org.drip.product.params.FactorSchedule (astrField[9].getBytes());

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception
				("CreditDefaultSwap de-serializer: Cannot locate credit valuation params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[10]))
			_crValParams = null;
		else
			_crValParams = new org.drip.product.params.CreditSetting
				(astrField[10].getBytes());

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception
				("CreditDefaultSwap de-serializer: Cannot locate cash settle params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[11]))
			_settleParams = null;
		else
			_settleParams = new org.drip.param.valuation.CashSettleParams (astrField[11].getBytes());

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception ("CreditDefaultSwap de-serializer: Cannot locate the periods");

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

	public boolean setName (
		final java.lang.String strName)
	{
		_strName = strName;
		return true;
	}

	@Override public java.lang.String getComponentName()
	{
		if (null != _strName && !_strName.isEmpty()) return _strName;

		return "CDS=" + org.drip.analytics.date.JulianDate.fromJulian (_dblMaturity);
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

	@Override public double getRecovery (
		final double dblDate,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate) || null == cc)
			throw new java.lang.Exception ("Bond.getRecovery: Bad state/inputs");

		return _crValParams._bUseCurveRec ? cc.getRecovery (dblDate) : _crValParams._dblRecovery;
	}

	@Override public double getRecovery (
		final double dblDateStart,
		final double dblDateEnd,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDateStart) ||
			!org.drip.math.common.NumberUtil.IsValid (dblDateEnd) || null == cc)
			throw new java.lang.Exception ("Bond.getRecovery: Bad state/inputs");

		return _crValParams._bUseCurveRec ? cc.getEffectiveRecovery (dblDateStart, dblDateEnd) :
			_crValParams._dblRecovery;
	}

	@Override public org.drip.product.params.CreditSetting getCRValParams()
	{
		return _crValParams;
	}

	@Override public double getCoupon (
		final double dblValue,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		return _dblCoupon;
	}

	/**
	 * Resets the CDS's coupon
	 * 
	 * @param dblCoupon The new Coupon
	 * 
	 * @return The old Coupon
	 * 
	 * @throws java.lang.Exception Thrown if the coupon cannot be reset
	 */

	public double resetCoupon (
		final double dblCoupon)
		throws java.lang.Exception
	{
		double dblOldCoupon = _dblCoupon;

		if (!org.drip.math.common.NumberUtil.IsValid (_dblCoupon = dblCoupon))
			throw new java.lang.Exception ("Cannot reset coupon with a NaN!");

		return dblOldCoupon;
	}

	@Override public boolean setCurves (
		final java.lang.String strIR,
		final java.lang.String strIRTSY,
		final java.lang.String strCC)
	{
		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty()) return false;

		_strIR = strIR;
		_crValParams._strCC = strCC;
		return true;
	}

	@Override public java.lang.String getIRCurveName()
	{
		return _strIR;
	}

	@Override public java.lang.String getCreditCurveName()
	{
		return _crValParams._strCC;
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

	@Override public java.util.List<org.drip.analytics.period.CouponPeriodCurveFactors> getCouponFlow
		(final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		if (null == valParams || null == pricerParams || null == mktParams || null ==
			mktParams.getDiscountCurve() || null == mktParams.getCreditCurve())
			return null;

		java.util.List<org.drip.analytics.period.CouponPeriodCurveFactors> lsCP = new
			java.util.ArrayList<org.drip.analytics.period.CouponPeriodCurveFactors>();

		double dblDFStart = java.lang.Double.NaN;
		double dblSurvProbStart = java.lang.Double.NaN;

		for (org.drip.analytics.period.Period fp : _lPeriods) {
			if (null == fp) continue;

			org.drip.analytics.period.CouponPeriodCurveFactors cp = null;

			try {
				double dblSurvProbEnd = java.lang.Double.NaN;

				if (!org.drip.math.common.NumberUtil.IsValid (dblDFStart))
					dblDFStart = mktParams.getDiscountCurve().getDF (fp.getStartDate());

				if (!org.drip.math.common.NumberUtil.IsValid (dblSurvProbStart))
					dblSurvProbStart = mktParams.getCreditCurve().getSurvival (fp.getStartDate());

				if (pricerParams._bSurvToPayDate)
					dblSurvProbEnd = mktParams.getCreditCurve().getSurvival (fp.getPayDate());
				else
					dblSurvProbEnd = mktParams.getCreditCurve().getSurvival (fp.getEndDate());

				double dblDFEnd = mktParams.getDiscountCurve().getDF (fp.getPayDate());

				cp = new org.drip.analytics.period.CouponPeriodCurveFactors (fp.getStartDate(),
					fp.getEndDate(), fp.getAccrualStartDate(), fp.getAccrualEndDate(), fp.getPayDate(),
						fp.getCouponDCF(), getCoupon (valParams._dblValue, mktParams), getNotional
							(fp.getStartDate()), getNotional (fp.getEndDate()), dblDFStart, dblDFEnd,
								dblSurvProbStart, dblSurvProbEnd, java.lang.Double.NaN,
									java.lang.Double.NaN);

				dblDFStart = dblDFEnd;
				dblSurvProbStart = dblSurvProbEnd;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			if (null != cp) lsCP.add (cp);
		}

		return lsCP;
	}

	@Override public java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> getLossFlow (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		if (null == valParams || null == pricerParams) return null;

		java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLP = new
			java.util.ArrayList<org.drip.analytics.period.LossPeriodCurveFactors>();

		for (org.drip.analytics.period.Period period : _lPeriods) {
			if (null == period || period.getEndDate() < valParams._dblValue) continue;

			java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
				org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
					pricerParams, period, period.getEndDate(), mktParams);

			if (null != sLPSub) sLP.addAll (sLPSub);
		}

		return sLP;
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		java.util.Map<java.lang.String, java.lang.Double> mapFairMeasures = calcMeasureSet ("", valParams,
			pricerParams, mktParams, quotingParams);

		if (null == mapFairMeasures) return null;

		org.drip.analytics.support.GenericUtil.MergeWithMain (mapFairMeasures,
			org.drip.analytics.support.GenericUtil.PrefixKeys (mapFairMeasures, "Fair"));

		if ((null != pricerParams && null != pricerParams._calibParams) || null == mapFairMeasures || null ==
			mktParams.getComponentQuote())
			return mapFairMeasures;

		double dblCreditBasis = java.lang.Double.NaN;
		double dblMarketMeasure = java.lang.Double.NaN;
		org.drip.analytics.definition.CreditCurve ccMarket = null;

		if (null != mktParams.getComponentQuote().getQuote ("Price"))
			mapFairMeasures.put ("MarketInputType=Price", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("Price").getQuote ("mid"));
		else if (null != mktParams.getComponentQuote().getQuote ("CleanPrice"))
			mapFairMeasures.put ("MarketInputType=CleanPrice", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("CleanPrice").getQuote ("mid"));
		else if (null != mktParams.getComponentQuote().getQuote ("Upfront"))
			mapFairMeasures.put ("MarketInputType=Upfront", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("Upfront").getQuote ("mid"));
		else if (null != mktParams.getComponentQuote().getQuote ("FairPremium"))
			mapFairMeasures.put ("MarketInputType=FairPremium", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("FairPremium").getQuote ("mid"));
		else if (null != mktParams.getComponentQuote().getQuote ("PV"))
			mapFairMeasures.put ("MarketInputType=PV", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("PV").getQuote ("mid"));
		else if (null != mktParams.getComponentQuote().getQuote ("CleanPV"))
			mapFairMeasures.put ("MarketInputType=CleanPV", dblMarketMeasure =
				mktParams.getComponentQuote().getQuote ("CleanPV").getQuote ("mid"));

		try {
			SpreadCalibOP scop = new SpreadCalibrator (this,
				SpreadCalibrator.CALIBRATION_TYPE_NODE_PARALLEL_BUMP).calibrateHazardFromPrice (valParams,
					new org.drip.param.pricer.PricerParams (7,
						org.drip.param.definition.CalibrationParams.MakeStdCalibParams(), false,
							org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_DAY_STEP), mktParams,
								quotingParams, dblMarketMeasure);

			if (null != scop) {
				ccMarket = scop._ccCalib;
				dblCreditBasis = scop._dblCalibResult;
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (org.drip.math.common.NumberUtil.IsValid (dblCreditBasis)) {
			mapFairMeasures.put ("MarketCreditBasis", dblCreditBasis);

			try {
				ccMarket = (org.drip.analytics.definition.CreditCurve)
					mktParams.getCreditCurve().createTweakedCurve (new
						org.drip.param.definition.NodeTweakParams
							(org.drip.param.definition.NodeTweakParams.NODE_FLAT_TWEAK, false,
								dblCreditBasis));
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = mapFairMeasures;

		if (null != ccMarket) {
			org.drip.param.definition.ComponentMarketParams cmpMarket =
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(mktParams.getDiscountCurve(), mktParams.getTSYDiscountCurve(),
					mktParams.getEDSFDiscountCurve(), ccMarket, mktParams.getComponentQuote(),
						mktParams.getTSYBenchmarkQuotes(), mktParams.getFixings());

			if (null != cmpMarket) {
				java.util.Map<java.lang.String, java.lang.Double> mapMarketMeasures = calcMeasureSet ("",
					valParams, pricerParams, cmpMarket, quotingParams);

				if (null != mapMarketMeasures) {
					org.drip.analytics.support.GenericUtil.MergeWithMain (mapMarketMeasures,
						org.drip.analytics.support.GenericUtil.PrefixKeys (mapMarketMeasures, "Market"));

					org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, mapMarketMeasures);
				}
			}
		}

		return mapMeasures;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcPVDFMicroJack (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == mktParams || null ==
			mktParams.getDiscountCurve() || null == mktParams.getCreditCurve())
			return null;

		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = value (valParams, pricerParams,
			mktParams, quotingParams);

		if (null == mapMeasures) return null;

		double dblPV = mapMeasures.get ("PV");

		double dblFairPremium = mapMeasures.get ("FairPremium");

		try {
			org.drip.math.calculus.WengertJacobian wjPVDFMicroJack = null;

			org.drip.analytics.definition.CreditCurve cc = mktParams.getCreditCurve();

			org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

			for (org.drip.analytics.period.Period p : _lPeriods) {
				double dblPeriodPayDate = p.getPayDate();

				if (dblPeriodPayDate < valParams._dblValue) continue;

				org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian (dblPeriodPayDate);

				org.drip.math.calculus.WengertJacobian wjPeriodOnDefaultPVMicroJack =
					calcPeriodOnDefaultPVDFMicroJack (dblFairPremium, p, valParams, pricerParams, mktParams);

				if (null == wjPeriodPayDFDF | null == wjPeriodOnDefaultPVMicroJack) continue;

				if (null == wjPVDFMicroJack)
					wjPVDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
						wjPeriodPayDFDF.numParameters());

				double dblPeriodCashFlow = dblFairPremium * getNotional (p.getStartDate(), p.getEndDate()) *
					p.getCouponDCF() * cc.getSurvival (dblPeriodPayDate);

				for (int k = 0; k < wjPeriodPayDFDF.numParameters(); ++k) {
					if (!wjPVDFMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodCashFlow *
						wjPeriodPayDFDF.getFirstDerivative (0, k) +
							wjPeriodOnDefaultPVMicroJack.getFirstDerivative (0, k)))
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
			|| null == mktParams.getDiscountCurve() || null == mktParams.getCreditCurve())
			return null;

		if ("Rate".equalsIgnoreCase (strQuote) || "FairPremium".equalsIgnoreCase (strQuote) ||
			"ParSpread".equalsIgnoreCase (strQuote)) {
			java.util.Map<java.lang.String, java.lang.Double> mapMeasures = value (valParams, pricerParams,
				mktParams, quotingParams);

			if (null == mapMeasures) return null;

			double dblFairPremium = mapMeasures.get ("FairPremium");

			try {
				double dblDV01 = 0.;
				org.drip.math.calculus.WengertJacobian wjFairPremiumDFMicroJack = null;

				org.drip.analytics.definition.CreditCurve cc = mktParams.getCreditCurve();

				org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

				for (org.drip.analytics.period.Period p : _lPeriods) {
					double dblPeriodPayDate = p.getPayDate();

					if (dblPeriodPayDate < valParams._dblValue) continue;

					org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian (p.getEndDate());

					PeriodLossMicroJack plmj = calcPeriodLossMicroJack (p, valParams, pricerParams, mktParams);

					if (null == wjPeriodPayDFDF | null == plmj) continue;

					if (null == wjFairPremiumDFMicroJack)
						wjFairPremiumDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
							wjPeriodPayDFDF.numParameters());

					double dblPeriodCoupon01 = getNotional (p.getStartDate(), p.getEndDate()) * p.getCouponDCF()
						* cc.getSurvival (p.getEndDate());

					dblDV01 += dblPeriodCoupon01 * dc.getDF (p.getPayDate()) + plmj._dblAccrOnDef01;

					for (int k = 0; k < wjPeriodPayDFDF.numParameters(); ++k) {
						double dblPeriodNetLossJack = plmj._wjLossPVMicroJack.getFirstDerivative (0, k) -
							dblFairPremium * (plmj._wjAccrOnDef01MicroJack.getFirstDerivative (0, k) +
								dblPeriodCoupon01 * wjPeriodPayDFDF.getFirstDerivative (0, k));

						if (!wjFairPremiumDFMicroJack.accumulatePartialFirstDerivative (0, k,
							dblPeriodNetLossJack))
							return null;
					}
				}

				return wjFairPremiumDFMicroJack.scale (dblDV01) ? wjFairPremiumDFMicroJack : null;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Calibrates the CDS's flat spread from the calculated up-front points
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mktParams ComponentMarketParams
	 * 
	 * @return Calibrated flat spread
	 * 
	 * @throws java.lang.Exception Thrown if cannot calibrate
	 */

	public double calibFlatSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		SpreadCalibOP scop = new SpreadCalibrator (this,
			SpreadCalibrator.CALIBRATION_TYPE_FLAT_CURVE_NODES).calibrateHazardFromPrice (valParams,
				pricerParams, mktParams, quotingParams, calcMeasureValue (valParams, pricerParams, mktParams,
					quotingParams, "Upfront"));

		if (null == scop) throw new java.lang.Exception ("Cannot calibrate flat spread!");

		return scop._dblCalibResult;
	}

	/**
	 *	CDS spread calibration output
	 *
	 * @author Lakshmi Krishnamurthy
	 */

	public class SpreadCalibOP {
		public double _dblCalibResult = java.lang.Double.NaN;
		public org.drip.analytics.definition.CreditCurve _ccCalib = null;

		public SpreadCalibOP (
			final double dblCalibResult,
			final org.drip.analytics.definition.CreditCurve ccCalib)
			throws java.lang.Exception
		{
			if (!org.drip.math.common.NumberUtil.IsValid (_dblCalibResult = dblCalibResult) || null ==
				(_ccCalib = ccCalib))
				throw new java.lang.Exception ("Invalid inputs into SpreadCalibOP ctr!");
		}
	}

	/**
	 *	Implementation of the CDS spread calibrator
	 *
	 * @author Lakshmi Krishnamurthy
	 */

	public class SpreadCalibrator {
		private org.drip.product.definition.CreditDefaultSwap _cds = null;

		/*
		 * Calibration Type
		 */

		public static final int CALIBRATION_TYPE_FLAT_INSTRUMENT_NODE = 1;
		public static final int CALIBRATION_TYPE_FLAT_CURVE_NODES = 2;
		public static final int CALIBRATION_TYPE_NODE_PARALLEL_BUMP = 4;

		private int _iCalibType = CALIBRATION_TYPE_FLAT_CURVE_NODES;

		/**
		 * Constructor: Constructs the SpreadCalibrator from the CDS parent, and whether the calibration is
		 * 	off of a single node
		 * 
		 * @param cds CDS parent
		 * @param iCalibType Calibration type indicating whether the calibration is PARALLEL, FLAT SINGLE
		 * 		NODE, or FLAT TERM
		 * 
		 * @throws java.lang.Exception Thrown if inputs are invalid
		 */

		public SpreadCalibrator (
			final org.drip.product.definition.CreditDefaultSwap cds,
			final int iCalibType)
			throws java.lang.Exception
		{
			if (null == (_cds = cds) || (CALIBRATION_TYPE_FLAT_INSTRUMENT_NODE != (_iCalibType = iCalibType)
				&& CALIBRATION_TYPE_FLAT_CURVE_NODES != iCalibType && CALIBRATION_TYPE_NODE_PARALLEL_BUMP !=
					iCalibType))
				throw new java.lang.Exception ("Invalid inputs into SpreadCalibrator");
		}

		/**
		 * Calibrate the hazard rate from calibration price
		 * 
		 * @param valParams ValuationParams
		 * @param pricerParams PricerParams
		 * @param mktParams ComponentMarketParams
		 * @param dblPriceCalib Market price to be calibrated
		 * @param quotingParams Quoting Parameters
		 * 
		 * @return Calibrated hazard
		 * 
		 * @throws java.lang.Exception Thrown if calibration failed
		 */

		public SpreadCalibOP calibrateHazardFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.pricer.PricerParams pricerParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final org.drip.param.valuation.QuotingParams quotingParams,
			final double dblPriceCalib)
			throws java.lang.Exception
		{
			if (null == valParams || null == pricerParams || null == mktParams  || null ==
				mktParams.getCreditCurve() || !org.drip.math.common.NumberUtil.IsValid (dblPriceCalib))
				throw new java.lang.Exception ("calibrateHazardFromPriceNR - bad inputs");

			final org.drip.analytics.definition.CreditCurve ccOld = mktParams.getCreditCurve();

			org.drip.math.function.AbstractUnivariate ofCDSPriceFromFlatSpread = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblFlatSpread)
					throws java.lang.Exception
				{
					if (CALIBRATION_TYPE_NODE_PARALLEL_BUMP != _iCalibType)
						mktParams.setCreditCurve (ccOld.createFlatCurve (dblFlatSpread,
							CALIBRATION_TYPE_FLAT_CURVE_NODES == _iCalibType, java.lang.Double.NaN));
					else
						mktParams.setCreditCurve ((org.drip.analytics.definition.CreditCurve)
							ccOld.createTweakedCurve (new org.drip.param.definition.NodeTweakParams
								(org.drip.param.definition.NodeTweakParams.NODE_FLAT_TWEAK, false,
									dblFlatSpread)));

					return _cds.calcMeasureValue (valParams, pricerParams, mktParams, quotingParams,
						"Upfront") - dblPriceCalib;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofCDSPriceFromFlatSpread).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception ("BondComponent.calibrateYieldFromParASW => Cannot get root!");

			try {
				if (mktParams.setCreditCurve (ccOld)) return new SpreadCalibOP (rfop.getRoot(), ccOld);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			return null;
		}
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

		sb.append (_dblCoupon + getFieldDelimiter());

		sb.append (_dblMaturity + getFieldDelimiter());

		sb.append (_dblEffective + getFieldDelimiter());

		if (null == _notlSchedule)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlSchedule.serialize()) + getFieldDelimiter());

		if (null == _crValParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_crValParams.serialize()) + getFieldDelimiter());

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
			return new CDSComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	class PeriodLossMicroJack {
		public double _dblAccrOnDef01 = 0.;
		public org.drip.math.calculus.WengertJacobian _wjLossPVMicroJack = null;
		public org.drip.math.calculus.WengertJacobian _wjAccrOnDef01MicroJack = null;

		public PeriodLossMicroJack (
			final int iNumParameters)
			throws java.lang.Exception
		{
			_wjLossPVMicroJack = new org.drip.math.calculus.WengertJacobian (1, iNumParameters);

			_wjAccrOnDef01MicroJack = new org.drip.math.calculus.WengertJacobian (1, iNumParameters);
		}
	}
}
