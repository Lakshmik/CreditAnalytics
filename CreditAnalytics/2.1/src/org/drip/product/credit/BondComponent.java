
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
 * This is the base class that extends CreditComponent abstract class and implements the functionality behind
 * 		bonds of all kinds. Bond static data is captured in a set of 11 container classes – BondTSYParams,
 * 		BondCouponParams, BondNotionalParams, BondFloaterParams, BondCurrencyParams, BondIdentifierParams,
 * 		BondIRValuationParams, CompCRValParams, BondCFTerminationEvent, BondFixedPeriodGenerationParams, and
 * 		one EmbeddedOptionSchedule object instance each for the call and the put objects. Each of these
 * 		parameter set can be set separately.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BondComponent extends org.drip.product.definition.Bond implements
	org.drip.product.definition.BondProduct {
	private static final boolean s_bBlog = false;
	private static final boolean s_bSuppressErrors = true;
	private static final boolean s_bYieldDFOffofCouponAccrualDCF = true;

	/*
	 * EOS Control
	 */

	private static final int LEFT_EOS_SNIP = 1;

	/*
	 * Width for calculating local forward rate width
	 */

	private static final int LOCAL_FORWARD_RATE_WIDTH = 1;

	/*
	 * Recovery Period discretization Mode
	 */

	private static final int s_iDiscretizationScheme =
		org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_DAY_STEP;

	private org.drip.product.params.TreasuryBenchmark _tsyParams = null;
	private org.drip.product.params.CouponSetting _cpnParams = null;
	private org.drip.product.params.NotionalSetting _notlParams = null;
	private org.drip.product.params.FloaterSetting _fltParams = null;
	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixings = null;
	private org.drip.product.params.CurrencySet _ccyParams = null;
	private org.drip.product.params.IdentifierSet _idParams = null;
	private org.drip.product.params.QuoteConvention _mktConv = null;
	private org.drip.product.params.RatesSetting _irValParams = null;
	private org.drip.product.params.CreditSetting _crValParams = null;
	private org.drip.product.params.TerminationSetting _cfteParams = null;
	private org.drip.product.params.PeriodSet _periodParams = null;

	/*
	 * Bond EOS Params
	 */

	protected org.drip.product.params.EmbeddedOptionSchedule _eosPut = null;
	protected org.drip.product.params.EmbeddedOptionSchedule _eosCall = null;

	private double getTsyBmkYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate))
			throw new java.lang.Exception ("Bond.getTsyBmkYield: Bad val/mkt Params");

		java.lang.String strTsyBmk = null;
		org.drip.param.definition.ComponentQuote cqTsyBmkYield = null;

		if (null != _tsyParams._tsyBmkSet) strTsyBmk = _tsyParams._tsyBmkSet.getPrimaryBmk();

		if (null == strTsyBmk || strTsyBmk.isEmpty())
			strTsyBmk = org.drip.analytics.support.AnalyticsHelper.BaseTsyBmk (valParams._dblValue,
				dblWorkoutDate);

		if (null != mktParams.getTSYBenchmarkQuotes() && null != strTsyBmk && !strTsyBmk.isEmpty())
			cqTsyBmkYield = mktParams.getTSYBenchmarkQuotes().get (strTsyBmk);

		if (null != cqTsyBmkYield && null != cqTsyBmkYield.getQuote ("Yield"))
			return cqTsyBmkYield.getQuote ("Yield").getQuote ("mid");

		if (null == mktParams.getEDSFDiscountCurve()) return java.lang.Double.NaN;

		return mktParams.getEDSFDiscountCurve().calcLIBOR (dblWorkoutDate);
	}

	private org.drip.param.valuation.WorkoutInfo calcExerciseCallYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice) || null == _eosCall)
			return null;

		int iExercise = -1;
		double dblExerciseYield = java.lang.Double.NaN;

		try {
			dblExerciseYield = calcYieldFromPrice (valParams, mktParams, quotingParams,
				_periodParams._dblMaturity, 1., dblPrice);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			return null;
		}

		double[] adblEOSDates = _eosCall.getDates();

		double[] adblEOSFactors = _eosCall.getFactors();

		for (int i = 0; i < adblEOSDates.length; ++i) {
			if (valParams._dblValue > adblEOSDates[i] + LEFT_EOS_SNIP || adblEOSDates[i] -
				valParams._dblValue < _eosCall.getExerciseNoticePeriod())
				continue;

			try {
				double dblYield = calcYieldFromPrice (valParams, mktParams, quotingParams, adblEOSDates[i],
					adblEOSFactors[i], dblPrice);

				if (dblYield < dblExerciseYield) {
					iExercise = i;
					dblExerciseYield = dblYield;
				}
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		try {
			if (-1 == iExercise)
				return new org.drip.param.valuation.WorkoutInfo (_periodParams._dblMaturity,
					dblExerciseYield, 1., org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);

			return new org.drip.param.valuation.WorkoutInfo (adblEOSDates[iExercise], dblExerciseYield,
				adblEOSFactors[iExercise], org.drip.param.valuation.WorkoutInfo.WO_TYPE_CALL);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	private org.drip.param.valuation.WorkoutInfo calcExercisePutYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice) || null == _eosPut)
			return null;

		int iExercise = -1;
		double dblExerciseYield = java.lang.Double.NaN;

		try {
			dblExerciseYield = calcYieldFromPrice (valParams, mktParams, quotingParams,
				_periodParams._dblMaturity, 1., dblPrice);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			return null;
		}

		double[] adblEOSDates = _eosPut.getDates();

		double[] adblEOSFactors = _eosPut.getFactors();

		for (int i = 0; i < adblEOSDates.length; ++i) {
			if (valParams._dblValue > adblEOSDates[i] + LEFT_EOS_SNIP || adblEOSDates[i] -
				valParams._dblValue < _eosPut.getExerciseNoticePeriod())
				continue;

			try {
				double dblYield = calcYieldFromPrice (valParams, mktParams, quotingParams, adblEOSDates[i],
					adblEOSFactors[i], dblPrice);

				if (dblYield > dblExerciseYield) {
					iExercise = i;
					dblExerciseYield = dblYield;
				}
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		try {
			if (-1 == iExercise)
				return new org.drip.param.valuation.WorkoutInfo (_periodParams._dblMaturity,
					dblExerciseYield, 1., org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);

			return new org.drip.param.valuation.WorkoutInfo (adblEOSDates[iExercise], dblExerciseYield,
				adblEOSFactors[iExercise], org.drip.param.valuation.WorkoutInfo.WO_TYPE_PUT);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.param.valuation.WorkoutInfo calcExerciseYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice)) return null;

		try {
			if (null == _eosCall && null == _eosPut)
				return new org.drip.param.valuation.WorkoutInfo (_periodParams._dblMaturity,
					calcYieldFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
						dblPrice), 1., org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);

			if (null == _eosCall && null != _eosPut)
				return calcExercisePutYieldFromPrice (valParams, mktParams, quotingParams, dblPrice);

			if (null != _eosCall && null == _eosPut)
				return calcExerciseCallYieldFromPrice (valParams, mktParams, quotingParams, dblPrice);

			org.drip.param.valuation.WorkoutInfo wiPut = calcExercisePutYieldFromPrice (valParams, mktParams,
				quotingParams, dblPrice);

			org.drip.param.valuation.WorkoutInfo wiCall = calcExerciseCallYieldFromPrice (valParams,
				mktParams, quotingParams, dblPrice);

			return wiPut._dblDate < wiCall._dblDate ? wiPut : wiCall;
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	private double getIndexRate (
		final double dblValue,
		final org.drip.analytics.definition.DiscountCurve dc,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmMarketFixings,
		final org.drip.analytics.period.Period period)
		throws java.lang.Exception
	{
		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = _mmFixings;

		if (null == mmFixings) mmFixings = mmMarketFixings;

		if (null != period) {
			if (null == mmFixings || null == mmFixings.get (new org.drip.analytics.date.JulianDate
				(period.getResetDate())) || null == mmFixings.get (new org.drip.analytics.date.JulianDate
					(period.getResetDate())).get (_fltParams._strRateIndex)) {
				if (s_bBlog)
					System.out.println ("IRS reset for index " + _fltParams._strRateIndex +
						" and reset date " + org.drip.analytics.date.JulianDate.fromJulian
							(period.getResetDate()) + " not found; defaulting to implied");

				if (period.getStartDate() < dblValue && 0 != _periodParams._iFreq)
					return dc.calcLIBOR ((12 / _periodParams._iFreq) + "M");

				return dc.calcLIBOR (period.getStartDate(), period.getEndDate());
			}

			return mmFixings.get (new org.drip.analytics.date.JulianDate (period.getResetDate())).get
				(_fltParams._strRateIndex);
		}

		double dblRateRefEndDate = dblValue + LOCAL_FORWARD_RATE_WIDTH;

		if (0 != _periodParams._iFreq) dblRateRefEndDate = dblValue + 365.25 / _periodParams._iFreq;

		double dblIndexRate = dc.calcLIBOR (dblValue, dblRateRefEndDate);

		if (s_bBlog) System.out.println ("All else fails! " + dblIndexRate);

		return dblIndexRate;
	}

	private double applyCouponFactorAndWindow (
		final double dblCoupon,
		final double dblDate)
		throws java.lang.Exception
	{
		return _cpnParams.processCouponWindow (dblCoupon * _cpnParams._fsCoupon.getFactor (dblDate),
			dblDate);
	}

	private double getFixedCoupon (
		final double dblDate)
		throws java.lang.Exception
	{
		return applyCouponFactorAndWindow (_cpnParams._dblCoupon, dblDate);
	}

	private double getFloatingCoupon (
		final double dblValue,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		org.drip.analytics.period.Period period = calcCurrentPeriod (dblValue);

		double dblPeriodEndDate = dblValue;

		if (null != period) dblPeriodEndDate = period.getEndDate();

		if (!java.lang.Double.isNaN (_fltParams._dblCurrentCoupon) && null != period && period.contains
			(dblValue))
			return applyCouponFactorAndWindow (_fltParams._dblCurrentCoupon, dblPeriodEndDate);

		if (null == mktParams)
			throw new java.lang.Exception ("Valid market params needed for floaters to get index rate!");

		double dblIndexRate = getIndexRate (dblValue, mktParams.getDiscountCurve(), mktParams.getFixings(),
			period);

		if (java.lang.Double.isNaN (dblIndexRate))
			throw new java.lang.Exception ("Cannot find the index rate for " + new
				org.drip.analytics.date.JulianDate (dblValue));

		return applyCouponFactorAndWindow (dblIndexRate + _cpnParams._dblCoupon, dblPeriodEndDate);
	}

	private org.drip.analytics.output.BondWorkoutMeasures calcBondWorkoutMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor)
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				valParams._dblValue >= dblWorkoutDate)
			return null;

		double dblAccrued01 = 0.;
		double dblRecoveryPV = 0.;
		boolean bPeriodZero = true;
		double dblExpectedRecovery = 0.;
		double dblCreditRiskyDirtyDV01 = 0.;
		boolean bTerminateCouponFlow = false;
		double dblCreditRiskyPrincipalPV = 0.;
		double dblCreditRisklessDirtyDV01 = 0.;
		double dblCreditRiskyDirtyCouponPV = 0.;
		double dblCreditRisklessPrincipalPV = 0.;
		double dblCreditRisklessDirtyCouponPV = 0.;
		double dblFirstCoupon = java.lang.Double.NaN;
		double dblCreditRiskyDirtyIndexCouponPV = 0.;
		double dblFirstIndexRate = java.lang.Double.NaN;
		double dblCreditRisklessDirtyIndexCouponPV = 0.;
		double dblCreditRiskyParPV = java.lang.Double.NaN;
		double dblCreditRisklessParPV = java.lang.Double.NaN;

		try {
			for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
				if (null == period || period.getPayDate() < valParams._dblValue) continue;

				double dblPeriodStartDate = period.getStartDate() > valParams._dblValue ?
					period.getStartDate() : valParams._dblValue;

				double dblPeriodEndDate = period.getEndDate();

				if (dblWorkoutDate <= dblPeriodEndDate) {
					bTerminateCouponFlow = true;
					dblPeriodEndDate = dblWorkoutDate;
				}

				double dblPeriodCoupon = 0.;

				double dblPeriodDF = mktParams.getDiscountCurve().getDF (period.getPayDate());

				if (null == _fltParams) dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

				double dblPeriodIndexRate = getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), period);

				if (bPeriodZero) {
					bPeriodZero = false;
					dblFirstCoupon = dblPeriodCoupon;

					if (period.getStartDate() < valParams._dblValue)
						dblAccrued01 = 0.0001 * period.getAccrualDCF (valParams._dblValue) * getNotional
							(period.getStartDate(), valParams._dblValue);

					if (null != _fltParams) dblFirstIndexRate = dblPeriodIndexRate;
				}

				double dblPeriodCreditRisklessDirtyDV01 = 0.0001 * period.getAccrualDCF (dblPeriodEndDate) *
					dblPeriodDF * getNotional (dblPeriodStartDate, dblPeriodEndDate);

				double dblPeriodCreditRiskessPrincipalPV = (getNotional (dblPeriodStartDate) - getNotional
					(dblPeriodEndDate)) * dblPeriodDF;

				double dblPeriodCreditRiskyDirtyDV01 = dblPeriodCreditRisklessDirtyDV01;
				double dblPeriodCreditRiskyPrincipalPV = dblPeriodCreditRiskessPrincipalPV;

				if (null != mktParams.getCreditCurve() && null != pricerParams) {
					double dblSurvProb = java.lang.Double.NaN;

					if (dblPeriodEndDate < period.getEndDate())
						dblSurvProb = mktParams.getCreditCurve().getSurvival (dblPeriodEndDate);
					else {
						if (pricerParams._bSurvToPayDate)
							dblSurvProb = mktParams.getCreditCurve().getSurvival (period.getPayDate());
						else
							dblSurvProb = mktParams.getCreditCurve().getSurvival (dblPeriodEndDate);
					}

					dblPeriodCreditRiskyDirtyDV01 *= dblSurvProb;
					dblPeriodCreditRiskyPrincipalPV *= dblSurvProb;

					java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
						org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
							pricerParams, period, dblPeriodEndDate, mktParams);

					if (null == sLPSub || 0 == sLPSub.size()) continue;

					for (org.drip.analytics.period.LossPeriodCurveFactors lp : sLPSub) {
						if (null == lp) continue;

						double dblSubPeriodEnd = lp.getEndDate();

						double dblSubPeriodStart = lp.getStartDate();

						double dblSubPeriodDF = mktParams.getDiscountCurve().getEffectiveDF
							(dblSubPeriodStart + _crValParams._iDefPayLag, dblSubPeriodEnd +
								_crValParams._iDefPayLag);

						double dblSubPeriodNotional = getNotional (dblSubPeriodStart, dblSubPeriodEnd);

						double dblSubPeriodSurvival = mktParams.getCreditCurve().getSurvival
							(dblSubPeriodStart) - mktParams.getCreditCurve().getSurvival (dblSubPeriodEnd);

						if (_crValParams._bAccrOnDefault)
							dblPeriodCreditRiskyDirtyDV01 += 0.0001 * lp.getAccrualDCF() *
								dblSubPeriodSurvival * dblSubPeriodDF * dblSubPeriodNotional;

						double dblRecovery = _crValParams._bUseCurveRec ?
							mktParams.getCreditCurve().getEffectiveRecovery (dblSubPeriodStart,
								dblSubPeriodEnd) : _crValParams._dblRecovery;

						double dblSubPeriodExpRecovery = dblRecovery * dblSubPeriodSurvival *
							dblSubPeriodNotional;
						dblRecoveryPV += dblSubPeriodExpRecovery * dblSubPeriodDF;
						dblExpectedRecovery += dblSubPeriodExpRecovery;
					}
				}

				dblCreditRiskyDirtyDV01 += dblPeriodCreditRiskyDirtyDV01;
				dblCreditRiskyPrincipalPV += dblPeriodCreditRiskyPrincipalPV;
				dblCreditRisklessDirtyDV01 += dblPeriodCreditRisklessDirtyDV01;
				dblCreditRisklessPrincipalPV += dblPeriodCreditRiskessPrincipalPV;
				dblCreditRiskyDirtyCouponPV += 10000. * dblPeriodCoupon * dblPeriodCreditRiskyDirtyDV01;
				dblCreditRisklessDirtyCouponPV += 10000. * dblPeriodCoupon *
					dblPeriodCreditRisklessDirtyDV01;
				dblCreditRiskyDirtyIndexCouponPV += 10000. * dblPeriodIndexRate *
					dblPeriodCreditRiskyDirtyDV01;
				dblCreditRisklessDirtyIndexCouponPV += 10000. * dblPeriodIndexRate *
					dblPeriodCreditRisklessDirtyDV01;

				if (bTerminateCouponFlow) break;
			}
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			return null;
		}

		double dblCashPayDate = java.lang.Double.NaN;

		try {
			dblCashPayDate = _mktConv.getSettleDate (valParams);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			dblCashPayDate = valParams._dblCashPay;
		}

		try {
			double dblCashPayDF = mktParams.getDiscountCurve().getDF (dblCashPayDate);

			dblCreditRisklessParPV = mktParams.getDiscountCurve().getDF (_periodParams._dblMaturity) *
				getNotional (_periodParams._dblMaturity) * dblWorkoutFactor;

			if (null != mktParams.getCreditCurve() && null != pricerParams)
				dblCreditRiskyParPV = dblCreditRisklessParPV * mktParams.getCreditCurve().getSurvival
					(_periodParams._dblMaturity);

			org.drip.analytics.output.BondCouponMeasures bcmCreditRisklessDirty = new
				org.drip.analytics.output.BondCouponMeasures (dblCreditRisklessDirtyDV01,
					dblCreditRisklessDirtyIndexCouponPV, dblCreditRisklessDirtyCouponPV,
						dblCreditRisklessDirtyCouponPV + dblCreditRisklessPrincipalPV +
							dblCreditRisklessParPV);

			double dblDefaultExposure = java.lang.Double.NaN;
			double dblDefaultExposureNoRec = java.lang.Double.NaN;
			double dblLossOnInstantaneousDefault = java.lang.Double.NaN;
			org.drip.analytics.output.BondCouponMeasures bcmCreditRiskyDirty = null;

			if (null != mktParams.getCreditCurve() && null != pricerParams) {
				bcmCreditRiskyDirty = new org.drip.analytics.output.BondCouponMeasures
					(dblCreditRiskyDirtyDV01, dblCreditRiskyDirtyIndexCouponPV, dblCreditRiskyDirtyCouponPV,
						dblCreditRiskyDirtyCouponPV + dblCreditRiskyPrincipalPV + dblCreditRiskyParPV);

				dblDefaultExposure = (dblDefaultExposureNoRec = getNotional (valParams._dblValue)) *
					mktParams.getCreditCurve().getRecovery (valParams._dblValue);

				dblLossOnInstantaneousDefault = getNotional (valParams._dblValue) * (1. -
					mktParams.getCreditCurve().getRecovery (valParams._dblValue));
			}

			return new org.drip.analytics.output.BondWorkoutMeasures (bcmCreditRiskyDirty,
				bcmCreditRisklessDirty, dblCreditRiskyParPV, dblCreditRisklessParPV,
					dblCreditRiskyPrincipalPV, dblCreditRisklessPrincipalPV, dblRecoveryPV,
						dblExpectedRecovery, dblDefaultExposure, dblDefaultExposureNoRec,
							dblLossOnInstantaneousDefault, dblAccrued01, dblFirstCoupon, dblFirstIndexRate,
								dblCashPayDF);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	public java.util.Map<java.lang.String, java.lang.Double> standardRVMeasureMap (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.valuation.WorkoutInfo wi,
		final double dblPrice,
		final java.lang.String strPrefix)
	{
		if (null == strPrefix) return null;

		org.drip.analytics.output.BondRVMeasures bmRV = standardMeasures (valParams, pricerParams, mktParams,
			quotingParams, wi, dblPrice);

		if (null == bmRV) return null;

		return bmRV.toMap (strPrefix);
	}

	private java.util.Map<java.lang.String, java.lang.Double> calcFairMeasureSet (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		org.drip.analytics.output.BondWorkoutMeasures bwmFair = calcBondWorkoutMeasures (valParams,
			pricerParams, mktParams, getMaturityDate().getJulian(), 1.);

		if (null == bwmFair) return null;

		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = bwmFair.toMap ("");

		double dblPrice = (null == bwmFair._bcmCreditRiskyClean || java.lang.Double.isNaN
			(bwmFair._bcmCreditRiskyClean._dblPV)) ? bwmFair._bcmCreditRisklessClean._dblPV :
				bwmFair._bcmCreditRiskyClean._dblPV;

		try {
			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, standardRVMeasureMap
				(valParams, pricerParams, mktParams, quotingParams, new org.drip.param.valuation.WorkoutInfo
					(getMaturityDate().getJulian(), calcYieldFromPrice (valParams, mktParams, quotingParams,
						dblPrice / getNotional (valParams._dblValue)), 1.,
							org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY), dblPrice, ""));

			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures,
				org.drip.analytics.support.GenericUtil.PrefixKeys (mapMeasures, "Fair"));

			return mapMeasures;
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	private java.util.Map<java.lang.String, java.lang.Double> calcMarketMeasureSet (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.valuation.WorkoutInfo wiMarket)
	{
		try {
			java.util.Map<java.lang.String, java.lang.Double> mapMeasures = standardRVMeasureMap (valParams,
				pricerParams, mktParams, quotingParams, wiMarket, calcPriceFromYield (valParams, mktParams,
					quotingParams, wiMarket._dblDate, wiMarket._dblExerciseFactor, wiMarket._dblYield), "");

			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures,
				org.drip.analytics.support.GenericUtil.PrefixKeys (mapMeasures, "Market"));

			return mapMeasures;
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	private org.drip.analytics.period.Period calcCurrentPeriod (
		final double dblDate)
	{
		if (java.lang.Double.isNaN (dblDate)) return null;

		try {
			int iIndex = _periodParams.getPeriodIndex (dblDate);
			
			return _periodParams.getPeriod (iIndex);
		} catch (java.lang.Exception e) {
		}

		return null;
	}

	@Override protected java.util.Map<java.lang.String, java.lang.Double> calibMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		double dblExerciseFactor = 1.;
		double dblCleanPrice = java.lang.Double.NaN;

		double dblExerciseDate = getMaturityDate().getJulian();

		java.util.Map<java.lang.String, java.lang.Double> mapCalibMeasures = new
			java.util.TreeMap<java.lang.String, java.lang.Double>();

		if (null != pricerParams._calibParams._wi) {
			dblExerciseDate = pricerParams._calibParams._wi._dblDate;
			dblExerciseFactor = pricerParams._calibParams._wi._dblExerciseFactor;
		}

		try {
			if (null == mktParams.getCreditCurve())
				dblCleanPrice = calcPriceFromBumpedDC (valParams, mktParams, dblExerciseDate,
					dblExerciseFactor, 0.);
			else
				dblCleanPrice = calcPriceFromBumpedCC (valParams, mktParams, dblExerciseDate,
					dblExerciseFactor, 0., false);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			return null;
		}

		if (java.lang.Double.isNaN (dblCleanPrice)) return null;

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"CleanPrice", "FairCleanPrice", "FairPrice", "Price"}, false)) {
			mapCalibMeasures.put (pricerParams._calibParams._strMeasure, dblCleanPrice);

			return mapCalibMeasures;
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"DirtyPrice", "FairDirtyPrice"}, false)) {
			try {
				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, dblCleanPrice + calcAccrued
					(valParams._dblValue, mktParams));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"Yield", "FairYield"}, false)) {
			try {
				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcYieldFromPrice (valParams,
					mktParams, quotingParams, dblExerciseDate, dblExerciseFactor, dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"TSYSpread", "FairTSYSpread"}, false)) {
			try {
				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcTSYSpreadFromPrice
					(valParams, mktParams, quotingParams, dblExerciseDate, dblExerciseFactor,
						dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"OAS", "OASpread", "OptionAdjustedSpread"}, false)) {
			try {
				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcOASFromPrice (valParams,
					mktParams, quotingParams, dblExerciseDate, dblExerciseFactor, dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"BondBasis", "YieldBasis", "YieldSpread"}, false)) {
			try {
				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcBondBasisFromPrice
					(valParams, mktParams, quotingParams, dblExerciseDate, dblExerciseFactor,
						dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"CreditBasis"}, false)) {
			try {
				if (null == mktParams.getCreditCurve()) return null;

				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcCreditBasisFromPrice
					(valParams, mktParams, quotingParams, dblExerciseDate, dblExerciseFactor,
						dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		if (org.drip.analytics.support.GenericUtil.MatchInStringArray (pricerParams._calibParams._strMeasure,
			new java.lang.String[] {"PECS", "ParEquivalentCDSSpread"}, false)) {
			try {
				if (null == mktParams.getCreditCurve()) return null;

				mapCalibMeasures.put (pricerParams._calibParams._strMeasure, calcPECSFromPrice (valParams,
					mktParams, quotingParams, dblExerciseDate, dblExerciseFactor, dblCleanPrice));

				return mapCalibMeasures;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Constructor: Constructs an empty bond object
	 */

	public BondComponent()
	{
	}

	/**
	 * Bond de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if Bond cannot be properly de-serialized
	 */

	public BondComponent (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Bond de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Empty state");

		java.lang.String strSerializedBond = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedBond || strSerializedBond.isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedBond,
			getFieldDelimiter());

		if (null == astrField || 14 > astrField.length)
			throw new java.lang.Exception ("Bond de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate tsy params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_tsyParams = null;
		else
			_tsyParams = new org.drip.product.params.TreasuryBenchmark (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate cpn params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_cpnParams = null;
		else
			_cpnParams = new org.drip.product.params.CouponSetting (astrField[2].getBytes());

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate notional params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_notlParams = null;
		else
			_notlParams = new org.drip.product.params.NotionalSetting (astrField[3].getBytes());

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate floater params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_fltParams = null;
		else
			_fltParams = new org.drip.product.params.FloaterSetting (astrField[4].getBytes());

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate fixings");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5])) {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[5],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty()) continue;

					java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split
						(astrRecord[i], getCollectionKeyValueDelimiter());
					
					if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
						astrKVPair[0].isEmpty() ||
							org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
								(astrKVPair[0]) || null == astrKVPair[1] || astrKVPair[1].isEmpty() ||
									org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
										(astrKVPair[1]))
						continue;

					java.lang.String[] astrKeySet = org.drip.analytics.support.GenericUtil.Split
						(astrKVPair[0], getCollectionMultiLevelKeyDelimiter());

					if (null == astrKeySet || 2 != astrKeySet.length || null == astrKeySet[0] ||
						astrKeySet[0].isEmpty() ||
							org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
								(astrKeySet[0]) || null == astrKeySet[1] || astrKeySet[1].isEmpty() ||
									org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
										(astrKeySet[1]))
						continue;

					if (null == getFixings())
						_mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
							java.util.Map<java.lang.String, java.lang.Double>>();

					java.util.Map<java.lang.String, java.lang.Double> map2D = getFixings().get
						(astrKeySet[0]);

					if (null == map2D) map2D = new java.util.HashMap<java.lang.String, java.lang.Double>();

					map2D.put (astrKeySet[1], new java.lang.Double (astrKVPair[1]));

					getFixings().put (new org.drip.analytics.date.JulianDate (new java.lang.Double
						(astrKeySet[0])), map2D);
				}
			}
		}

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate currency params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			_ccyParams = null;
		else
			_ccyParams = new org.drip.product.params.CurrencySet (astrField[6].getBytes());

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond Identifier params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			_idParams = null;
		else
			_idParams = new org.drip.product.params.IdentifierSet (astrField[7].getBytes());

		if (null == astrField[8] || astrField[8].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond IR Valuation params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			_mktConv = null;
		else
			_mktConv = new org.drip.product.params.QuoteConvention (astrField[8].getBytes());

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond Credit Valuation params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			_crValParams = null;
		else
			_crValParams = new org.drip.product.params.CreditSetting
				(astrField[9].getBytes());

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond Termination params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[10]))
			_cfteParams = null;
		else
			_cfteParams = new org.drip.product.params.TerminationSetting (astrField[10].getBytes());

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond Period params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[11]))
			_periodParams = null;
		else
			_periodParams = new org.drip.product.params.PeriodSet
				(astrField[11].getBytes());

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond EOS Put params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[12]))
			_eosPut = null;
		else
			_eosPut = new org.drip.product.params.EmbeddedOptionSchedule (astrField[12].getBytes());

		if (null == astrField[13] || astrField[13].isEmpty())
			throw new java.lang.Exception ("Bond de-serializer: Cannot locate bond EOS Call params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[13]))
			_eosCall = null;
		else
			_eosCall = new org.drip.product.params.EmbeddedOptionSchedule (astrField[13].getBytes());
	}

	@Override public double[] getSecTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYBenchmarkQuotes() || null ==
			_tsyParams._tsyBmkSet || null == _tsyParams._tsyBmkSet.getSecBmk())
			return null;

		double[] adblSecTSYSpread = new double[_tsyParams._tsyBmkSet.getSecBmk().length];

		for (int i = 0; i < _tsyParams._tsyBmkSet.getSecBmk().length; ++i) {
			adblSecTSYSpread[i] = java.lang.Double.NaN;
			org.drip.param.definition.ComponentQuote cqTsyBmkYield = null;

			java.lang.String strTsyBmk = _tsyParams._tsyBmkSet.getSecBmk()[i];

			if (null != strTsyBmk && !strTsyBmk.isEmpty())
				cqTsyBmkYield = mktParams.getTSYBenchmarkQuotes().get (strTsyBmk);

			if (null != cqTsyBmkYield && null != cqTsyBmkYield.getQuote ("Yield"))
				adblSecTSYSpread[i] = cqTsyBmkYield.getQuote ("Yield").getQuote ("mid");
			else if (null != mktParams.getEDSFDiscountCurve()) {
				try {
					adblSecTSYSpread[i] = mktParams.getEDSFDiscountCurve().calcLIBOR
						(_periodParams._dblMaturity);
				} catch (java.lang.Exception e) {
					if (!s_bSuppressErrors) e.printStackTrace();
				}
			}
		}

		return adblSecTSYSpread;
	}

	@Override public double getEffectiveTsyBmkYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Bond.getEffectiveTsyBmkYield: Bad val/mkt Params");

		java.lang.String strTsyBmk = null;
		org.drip.param.definition.ComponentQuote cqTsyBmkYield = null;

		if (null != _tsyParams._tsyBmkSet) strTsyBmk = _tsyParams._tsyBmkSet.getPrimaryBmk();

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Bond.getEffectiveTsyBmkYield: Cant do TSY wkout for px!");

		if (null == strTsyBmk || strTsyBmk.isEmpty())
			strTsyBmk = org.drip.analytics.support.AnalyticsHelper.BaseTsyBmk (valParams._dblValue,
				wi._dblDate);

		if (null != mktParams.getTSYBenchmarkQuotes() && null != strTsyBmk && !strTsyBmk.isEmpty())
			cqTsyBmkYield = mktParams.getTSYBenchmarkQuotes().get (strTsyBmk);

		if (null != cqTsyBmkYield && null != cqTsyBmkYield.getQuote ("Yield"))
			return cqTsyBmkYield.getQuote ("Yield").getQuote ("mid");

		if (null == mktParams.getEDSFDiscountCurve()) return java.lang.Double.NaN;

		return mktParams.getEDSFDiscountCurve().calcLIBOR (wi._dblDate);
	}

	@Override public boolean setTreasuryBenchmark (
		final org.drip.product.params.TreasuryBenchmark tsyParams)
	{
		if (null == (_tsyParams = tsyParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.TreasuryBenchmark getTreasuryBenchmark()
	{
		return _tsyParams;
	}

	@Override public boolean setIdentifierSet (
		final org.drip.product.params.IdentifierSet idParams)
	{
		if (null == (_idParams = idParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.IdentifierSet getIdentifierSet()
	{
		return _idParams;
	}

	@Override public boolean setCouponSetting (
		final org.drip.product.params.CouponSetting cpnParams)
	{
		if (null == (_cpnParams = cpnParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.CouponSetting getCouponSetting()
	{
		return _cpnParams;
	}

	@Override public boolean setCurrencySet (
		final org.drip.product.params.CurrencySet ccyParams)
	{
		if (null == (_ccyParams = ccyParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.CurrencySet getCurrencyParams()
	{
		return _ccyParams;
	}

	@Override public boolean setFloaterSetting (
		final org.drip.product.params.FloaterSetting fltParams)
	{
		if (null == (_fltParams = fltParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.FloaterSetting getFloaterSetting()
	{
		return _fltParams;
	}

	@Override public boolean setFixings (
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings)
	{
		_mmFixings = mmFixings;
		return true;
	}

	@Override public java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>>
		getFixings()
	{
		return _mmFixings;
	}

	@Override public boolean setMarketConvention (
		final org.drip.product.params.QuoteConvention mktConv)
	{
		if (null == (_mktConv = mktConv)) return false;

		return true;
	}

	@Override public org.drip.product.params.QuoteConvention getMarketConvention()
	{
		return _mktConv;
	}

	@Override public boolean setRatesSetting (
		final org.drip.product.params.RatesSetting irValParams)
	{
		if (null == (_irValParams = irValParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.RatesSetting setRatesSetting()
	{
		return _irValParams;
	}

	@Override public boolean setCreditSetting (
		final org.drip.product.params.CreditSetting crValParams)
	{
		if (null == (_crValParams = crValParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.CreditSetting getCreditSetting()
	{
		return _crValParams;
	}

	@Override public boolean setTerminationSetting (
		final org.drip.product.params.TerminationSetting cfteParams)
	{
		if (null == (_cfteParams = cfteParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.TerminationSetting getTerminationSetting()
	{
		return _cfteParams;
	}

	@Override public boolean setPeriodSet (
		final org.drip.product.params.PeriodSet periodParams)
	{
		if (null == (_periodParams = periodParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.PeriodSet getPeriodSet()
	{
		return _periodParams;
	}

	@Override public boolean setNotionalSetting (
		final org.drip.product.params.NotionalSetting notlParams)
	{
		if (null == (_notlParams = notlParams)) return false;

		return true;
	}

	@Override public org.drip.product.params.NotionalSetting getNotionalSetting()
	{
		return _notlParams;
	}

	@Override public java.lang.String getPrimaryCode()
	{
		if (null == _idParams) return null;

		return "BOND." + _idParams._strID;
	}

	@Override public void setPrimaryCode (final java.lang.String strCode)
	{
		// _strCode = strCode;
	}

	@Override public java.lang.String[] getSecondaryCode()
	{
		return new java.lang.String[] {_idParams._strID};
	}

	@Override public java.lang.String getISIN() {
		if (null == _idParams) return null;

		return _idParams._strISIN;
	}

	@Override public java.lang.String getCUSIP()
	{
		if (null == _idParams) return null;

		return _idParams._strCUSIP;
	}

	@Override public java.lang.String getComponentName()
	{
		if (null == _idParams) return null;

		return _idParams._strID;
	}

	@Override public double getNotional (
		final double dblDate)
		throws java.lang.Exception
	{
		if (null == _notlParams || null == _notlParams._fsPrincipalOutstanding || java.lang.Double.isNaN
			(dblDate))
			throw new java.lang.Exception ("Bond.getNotional: Bad state/inputs");

		return _notlParams._fsPrincipalOutstanding.getFactor (dblDate);
	}

	@Override public double getNotional (
		final double dblDateStart,
		final double dblDateEnd)
		throws java.lang.Exception
	{
		if (null == _notlParams || null == _notlParams._fsPrincipalOutstanding || java.lang.Double.isNaN
			(dblDateStart) || java.lang.Double.isNaN (dblDateEnd))
			throw new java.lang.Exception ("Bond.getNotional: Bad state/inputs");

		return _notlParams._fsPrincipalOutstanding.getFactor (dblDateStart, dblDateEnd);
	}

	@Override public double getInitialNotional()
		throws java.lang.Exception
	{
		if (null == _notlParams) throw new java.lang.Exception ("Bond.getInitialNotional: Bad state/inputs");

		return _notlParams._dblNotional;
	}

	@Override public double getRecovery (
		final double dblDate,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate) || null == cc)
			throw new java.lang.Exception ("Bond.getRecovery: Bad state/inputs");

		return _crValParams._bUseCurveRec ? cc.getRecovery (dblDate) : _crValParams._dblRecovery;
	}

	@Override public double getRecovery (
		final double dblDateStart,
		final double dblDateEnd,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDateStart) || java.lang.Double.isNaN (dblDateEnd) || null == cc)
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
		if (java.lang.Double.isNaN (dblValue)) throw new java.lang.Exception ("Invalid date into getCoupon");

		if (null == _fltParams) return getFixedCoupon (dblValue);

		return getFloatingCoupon (dblValue, mktParams);
	}

	@Override public boolean setCurves (
		final java.lang.String strIR,
		final java.lang.String strIRTSY,
		final java.lang.String strCC)
	{
		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty() || null == _mktConv ||
			null == _crValParams || null == _tsyParams)
			return false;

		_irValParams._strTradeDiscountCurve = strIR;
		_crValParams._strCC = strCC;
		_tsyParams._strIRTSY = strIRTSY;
		return true;
	}

	@Override public java.lang.String getIRCurveName()
	{
		if (null == _irValParams) return "";

		return _irValParams._strTradeDiscountCurve;
	}

	@Override public java.lang.String getCreditCurveName()
	{
		if (null == _crValParams) return "";

		return _crValParams._strCC;
	}

	@Override public java.lang.String getTreasuryCurveName()
	{
		if (null == _tsyParams) return "";

		return _tsyParams._strIRTSY;
	}

	@Override public java.lang.String getEDSFCurveName()
	{
		if (null == _tsyParams) return "";

		return _tsyParams._strIREDSF;
	}

	@Override public org.drip.analytics.date.JulianDate getEffectiveDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_periodParams._dblEffective);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getMaturityDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_periodParams._dblMaturity);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getFirstCouponDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_periodParams.getPeriods().get (0).getEndDate());
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public java.util.List<org.drip.analytics.period.Period> getCouponPeriod()
	{
		if (null == _periodParams) return null;

		return _periodParams.getPeriods();
	}

	@Override public org.drip.param.valuation.CashSettleParams getCashSettleParams()
	{
		return _mktConv._settleParams;
	}

	@Override public java.util.List<org.drip.analytics.period.CouponPeriodCurveFactors> getCouponFlow (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve())
			return null;

		java.util.List<org.drip.analytics.period.CouponPeriodCurveFactors> lsCP = new
			java.util.ArrayList<org.drip.analytics.period.CouponPeriodCurveFactors>();

		double dblDFStart = java.lang.Double.NaN;
		double dblSurvProbStart = java.lang.Double.NaN;

		for (org.drip.analytics.period.Period fp : _periodParams.getPeriods()) {
			if (null == fp) continue;

			org.drip.analytics.period.CouponPeriodCurveFactors cp = null;

			try {
				double dblIndexRate = java.lang.Double.NaN;
				double dblFloatSpread = java.lang.Double.NaN;
				double dblSurvProbEnd = java.lang.Double.NaN;

				if (java.lang.Double.isNaN (dblDFStart))
					dblDFStart = mktParams.getDiscountCurve().getDF (fp.getStartDate());

				if (java.lang.Double.isNaN (dblSurvProbStart))
					dblSurvProbStart = mktParams.getCreditCurve().getSurvival (fp.getStartDate());

				if (pricerParams._bSurvToPayDate)
					dblSurvProbEnd = mktParams.getCreditCurve().getSurvival (fp.getPayDate());
				else
					dblSurvProbEnd = mktParams.getCreditCurve().getSurvival (fp.getEndDate());

				if (null != _fltParams) {
					double dblCpnFactor = _cpnParams._fsCoupon.getFactor (fp.getEndDate());

					if (null != mktParams)
						dblIndexRate = getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
							mktParams.getFixings(), fp) * dblCpnFactor;

					dblFloatSpread = _cpnParams._dblCoupon * dblCpnFactor;
				}

				double dblDFEnd = mktParams.getDiscountCurve().getDF (fp.getPayDate());

				cp = new org.drip.analytics.period.CouponPeriodCurveFactors (fp.getStartDate(),
					fp.getEndDate(), fp.getAccrualStartDate(), fp.getAccrualEndDate(), fp.getPayDate(),
						fp.getCouponDCF(), getCoupon (valParams._dblValue, mktParams), getNotional
							(fp.getStartDate()), getNotional (fp.getEndDate()), dblDFStart, dblDFEnd,
								dblSurvProbStart, dblSurvProbEnd, dblFloatSpread, dblIndexRate);

				dblDFStart = dblDFEnd;
				dblSurvProbStart = dblSurvProbEnd;
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
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
		if (null == valParams || null == pricerParams || null == mktParams) return null;

		java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLP = new
			java.util.ArrayList<org.drip.analytics.period.LossPeriodCurveFactors>();

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (null == period) continue;

			java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
				org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
					pricerParams, period, period.getEndDate(), mktParams);

			if (null != sLPSub) sLP.addAll (sLPSub);
		}

		return sLP;
	}

	@Override public java.util.List<org.drip.analytics.period.LossPeriodCurveFactors>
		getLossFlowFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.pricer.PricerParams pricerParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final org.drip.param.valuation.QuotingParams quotingParams,
			final double dblPrice)
	{
		if (null == valParams || null == pricerParams || null == mktParams || java.lang.Double.isNaN
			(dblPrice))
			return null;

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi) return null;

		java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLP = new
			java.util.ArrayList<org.drip.analytics.period.LossPeriodCurveFactors>();

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (null == period || period.getEndDate() < valParams._dblValue) continue;

			if (period.getStartDate() > wi._dblDate) break;

			java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
				org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
					pricerParams, period, period.getEndDate() < wi._dblDate ? period.getEndDate() :
						wi._dblDate, mktParams);

			if (null != sLPSub) sLP.addAll (sLPSub);
		}

		return sLP;
	}

	@Override public boolean isFloater()
	{
		if (null == _fltParams) return false;

		return true;
	}

	@Override public java.lang.String getRateIndex()
	{
		if (null == _fltParams) return "";

		return _fltParams._strRateIndex;
	}

	@Override public double getCurrentCoupon()
	{
		if (null == _fltParams) return java.lang.Double.NaN;

		return _fltParams._dblCurrentCoupon;
	}

	@Override public double getFloatSpread()
	{
		if (null == _fltParams) return java.lang.Double.NaN;

		return _fltParams._dblFloatSpread;
	}

	@Override public java.lang.String getTicker()
	{
		if (null == _idParams) return null;

		return _idParams._strTicker;
	}

	@Override public void setEmbeddedCallSchedule (
		final org.drip.product.params.EmbeddedOptionSchedule eos)
	{
		if (null == eos || eos.isPut()) return;

		_eosCall = new org.drip.product.params.EmbeddedOptionSchedule (eos);
	}

	@Override public void setEmbeddedPutSchedule (
		final org.drip.product.params.EmbeddedOptionSchedule eos)
	{
		if (null == eos || !eos.isPut()) return;

		_eosPut = new org.drip.product.params.EmbeddedOptionSchedule (eos);
	}

	@Override public boolean isCallable()
	{
		return null != _eosCall;
	}

	@Override public boolean isPutable()
	{
		return null != _eosPut;
	}

	@Override public boolean isSinkable()
	{
		if (null == _notlParams) return false;

		return true;
	}

	@Override public boolean hasVariableCoupon()
	{
		if (null == _cpnParams || null == _cpnParams._strCouponType || !"variable".equalsIgnoreCase
			(_cpnParams._strCouponType))
			return false;

		return true;
	}

	@Override public boolean hasBeenExercised()
	{
		if (null == _cfteParams) return false;

		return _cfteParams._bHasBeenExercised;
	}

	@Override public boolean hasDefaulted()
	{
		if (null == _cfteParams) return false;

		return _cfteParams._bIsDefaulted;
	}

	@Override public boolean isPerpetual()
	{
		if (null == _cfteParams) return false;

		return _cfteParams._bIsPerpetual;
	}

	@Override public boolean isTradeable (
		final org.drip.param.valuation.ValuationParams valParams)
		throws java.lang.Exception
	{
		if (null == valParams) throw new java.lang.Exception ("Null valParams in BondComponent::isTradeable!");

		return !_cfteParams._bHasBeenExercised && !_cfteParams._bIsDefaulted && valParams._dblValue <
			_periodParams._dblMaturity;
	}

	@Override public org.drip.product.params.EmbeddedOptionSchedule getEmbeddedCallSchedule()
	{
		return _eosCall;
	}

	@Override public org.drip.product.params.EmbeddedOptionSchedule getEmbeddedPutSchedule()
	{
		return _eosPut;
	}

	@Override public java.lang.String getCouponType()
	{
		if (null == _cpnParams) return "";

		return _cpnParams._strCouponType;
	}

	@Override public java.lang.String getCouponDC()
	{
		if (null == _periodParams) return "";

		return _periodParams._strCouponDC;
	}

	@Override public java.lang.String getAccrualDC()
	{
		if (null == _periodParams) return "";

		return _periodParams._strAccrualDC;
	}

	@Override public java.lang.String getMaturityType()
	{
		if (null == _periodParams) return "";

		return _periodParams._strMaturityType;
	}

	@Override public int getCouponFreq()
	{
		if (null == _periodParams) return -1;

		return _periodParams._iFreq;
	}

	@Override public org.drip.analytics.date.JulianDate getFinalMaturity()
	{
		if (null == _periodParams) return null;

		try {
			return new org.drip.analytics.date.JulianDate (_periodParams._dblFinalMaturity);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String getCalculationType()
	{
		if (null == _mktConv) return "";

		return _mktConv._strCalculationType;
	}

	@Override public double getRedemptionValue()
	{
		if (null == _mktConv) return java.lang.Double.NaN;

		return _mktConv._dblRedemptionValue;
	}

	@Override public java.lang.String getCouponCurrency()
	{
		if (null == _ccyParams) return "";

		return _ccyParams._strCouponCurrency;
	}

	@Override public java.lang.String getRedemptionCurrency()
	{
		if (null == _ccyParams) return "";

		return _ccyParams._strRedemptionCurrency;
	}

	@Override public boolean inFirstCouponPeriod (
		final double dblDate)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate)) throw new java.lang.Exception ("Input date is NaN");

		return _periodParams.getFirstPeriod().contains (dblDate);
	}

	@Override public boolean inLastCouponPeriod (
		final double dblDate)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate)) throw new java.lang.Exception ("Input date is NaN");

		return _periodParams.getLastPeriod().contains (dblDate);
	}

	@Override public java.lang.String getTradeCurrency()
	{
		if (null == _ccyParams) return "";

		return _ccyParams._strTradeCurrency;
	}

	@Override public java.lang.String getFloatCouponConvention()
	{
		if (null == _fltParams) return "";

		return _fltParams._strFloatDayCount;
	}

	@Override public org.drip.analytics.date.JulianDate getPeriodResetDate (
		final double dblValue)
	{
		if (null == _fltParams || java.lang.Double.isNaN (dblValue) || dblValue >=
			_periodParams._dblMaturity)
			return null;

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < dblValue) continue;

			try {
				return new org.drip.analytics.date.JulianDate (period.getResetDate());
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				return null;
			}
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate calcPreviousCouponDate (
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt) return null;

		try {
			int iIndex = _periodParams.getPeriodIndex (dt.getJulian());

			if (0 == iIndex) return null;
			
			org.drip.analytics.period.Period period = _periodParams.getPeriod (iIndex - 1);

			if (null == period) return null;

			return new org.drip.analytics.date.JulianDate (period.getPayDate());
		} catch (java.lang.Exception e) {
		}

		return null;
	}

	@Override public double calcPreviousCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		if (null == dt || null == mktParams) throw new java.lang.Exception ("Null val/mkt params!");

		int iIndex = _periodParams.getPeriodIndex (dt.getJulian());

		org.drip.analytics.period.Period period = _periodParams.getPeriod (iIndex - 1);

		if (null == _fltParams) return getCoupon (dt.getJulian(), mktParams);

		if (null == period) throw new java.lang.Exception ("Cannot find previous period!");

		return getCoupon (dt.getJulian(), mktParams);
	}

	@Override public org.drip.analytics.date.JulianDate calcCurrentCouponDate (
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt) return null;

		try {
			int iIndex = _periodParams.getPeriodIndex (dt.getJulian());
			
			org.drip.analytics.period.Period period = _periodParams.getPeriod (iIndex);

			if (null == period) return null;

			return new org.drip.analytics.date.JulianDate (period.getPayDate());
		} catch (java.lang.Exception e) {
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate calcNextCouponDate (
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt) return null;

		try {
			int iIndex = _periodParams.getPeriodIndex (dt.getJulian());
			
			org.drip.analytics.period.Period period = _periodParams.getPeriod (iIndex + 1);

			if (null == period) return null;

			return new org.drip.analytics.date.JulianDate (period.getPayDate());
		} catch (java.lang.Exception e) {
		}

		return null;
	}

	@Override public org.drip.analytics.output.ExerciseInfo calcNextValidExerciseDateOfType (
		final org.drip.analytics.date.JulianDate dt,
		final boolean bGetPut)
	{
		if (null == dt || (bGetPut && null == _eosPut) || (!bGetPut && null == _eosCall)) return null;

		double[] adblEOSExerciseDates = null;
		org.drip.product.params.EmbeddedOptionSchedule eos = null;

		if (bGetPut)
			adblEOSExerciseDates = (eos = _eosPut).getDates();
		else
			adblEOSExerciseDates = (eos = _eosCall).getDates();

		if (null == eos || null == adblEOSExerciseDates || 0 == adblEOSExerciseDates.length) return null;

		for (int i = 0; i < adblEOSExerciseDates.length; ++i) {
			if (dt.getJulian() > adblEOSExerciseDates[i] + LEFT_EOS_SNIP || adblEOSExerciseDates[i] -
				dt.getJulian() < eos.getExerciseNoticePeriod())
				continue;

			try {
				return new org.drip.analytics.output.ExerciseInfo (adblEOSExerciseDates[i], eos.getFactor
					(i), bGetPut ? org.drip.param.valuation.WorkoutInfo.WO_TYPE_PUT :
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_CALL);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				return null;
			}
		}

		return null;
	}

	@Override public org.drip.analytics.output.ExerciseInfo calcNextValidExerciseInfo (
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt) return null;

		org.drip.analytics.output.ExerciseInfo neiNextCall = calcNextValidExerciseDateOfType (dt, false);

		org.drip.analytics.output.ExerciseInfo neiNextPut = calcNextValidExerciseDateOfType (dt, true);

		if (null == neiNextCall && null == neiNextPut) {
			try {
				return new org.drip.analytics.output.ExerciseInfo (getMaturityDate().getJulian(), 1.,
					org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				return null;
			}
		}

		if (null != neiNextCall && null == neiNextPut) return neiNextCall;

		if (null == neiNextCall && null != neiNextPut) return neiNextPut;

		return neiNextCall._dblDate < neiNextPut._dblDate ? neiNextCall : neiNextPut;
	}

	@Override public double calcCurrentCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		if (null == dt || null == mktParams) throw new java.lang.Exception ("Null val/mkt params!");

		if (null == _fltParams) return getCoupon (dt.getJulian(), mktParams);

		if (!java.lang.Double.isNaN (_fltParams._dblCurrentCoupon)) return _fltParams._dblCurrentCoupon;

		return getCoupon (dt.getJulian(), mktParams);
	}

	@Override public double calcNextCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		if (null == dt || null == mktParams) throw new java.lang.Exception ("Null val/mkt params!");

		if (null == _fltParams) return getCoupon (dt.getJulian(), mktParams);

		int iIndex = _periodParams.getPeriodIndex (dt.getJulian());

		org.drip.analytics.period.Period period = _periodParams.getPeriod (iIndex + 1);

		if (null == period) throw new java.lang.Exception ("Cannot find next period!");

		return getCoupon (dt.getJulian(), mktParams);
	}

	@Override public double calcAccrued (
		final double dblDate,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate) || null == mktParams)
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcAccrued");

		if (dblDate >= _periodParams._dblMaturity)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(dblDate) + " greater than maturity " + org.drip.analytics.date.JulianDate.fromJulian
					(_periodParams._dblMaturity));

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < dblDate) continue;

			double dblCoupon = getCoupon (dblDate, mktParams);

			if (java.lang.Double.isNaN (dblCoupon)) return java.lang.Double.NaN;

			if (period.getStartDate() < dblDate && period.getEndDate() >= dblDate) {
				// System.out.println ("Num Days: " + (dblDate - period.getStartDate()));

				double dblAccrued = period.getAccrualDCF (dblDate) * dblCoupon * getNotional
					(period.getStartDate());

				if (s_bBlog) {
					System.out.println ("Accrued DCF: " + (int) (period.getAccrualDCF (dblDate) * 366. +
						0.5));

					System.out.println ("Accrued: " + dblAccrued);
				}

				return dblAccrued;
			}

			return 0.;
		}

		return 0.;
	}

	@Override public double calcPriceFromBumpedZC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZCBump)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZCBump))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromZC");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPVFromZC = 0.;
		boolean bTerminateCouponFlow = false;
		double dblCashPayDate = java.lang.Double.NaN;
		double dblScalingNotional = java.lang.Double.NaN;
		org.drip.analytics.definition.ZeroCurve zc = null;

		try {
			dblCashPayDate = _mktConv.getSettleDate (valParams);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			dblCashPayDate = valParams._dblCashPay;
		}

		if (null != _notlParams && _notlParams._bPriceOffOriginalNotional) dblScalingNotional = 1.;

		try {
			zc = org.drip.analytics.creator.ZeroCurveBuilder.CreateZeroCurve (_periodParams._iFreq,
				_periodParams._strCouponDC, _ccyParams._strCouponCurrency, _periodParams._bApplyCpnEOMAdj,
					_periodParams.getPeriods(), dblWorkoutDate, dblCashPayDate, mktParams.getDiscountCurve(),
						null == quotingParams ? (null == _mktConv ? null : _mktConv._quotingParams) :
							quotingParams, dblZCBump);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		if (null == zc)
			throw new java.lang.Exception
				("Cannot create shifted ZC in BondComponent::calcPriceFromBumpedZC");

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

			if (java.lang.Double.isNaN (dblScalingNotional))
				dblScalingNotional = getNotional (period.getStartDate());

			double dblAccrualEndDate = period.getAccrualEndDate();

			double dblNotionalEndDate = period.getEndDate();

			if (dblAccrualEndDate >= dblWorkoutDate) {
				bTerminateCouponFlow = true;
				dblAccrualEndDate = dblWorkoutDate;
				dblNotionalEndDate = dblWorkoutDate;
			}

			double dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

			double dblZCDF = zc.getDF (period.getPayDate());

			double dblCouponNotional = getNotional (period.getStartDate());

			if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_END ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (dblNotionalEndDate);
			else if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_EFFECTIVE ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (period.getStartDate(), dblNotionalEndDate);

			dblPVFromZC += period.getAccrualDCF (dblAccrualEndDate) * dblZCDF * dblPeriodCoupon *
				dblCouponNotional;

			dblPVFromZC += (getNotional (period.getStartDate()) - getNotional (dblNotionalEndDate)) *
				dblZCDF;

			if (bTerminateCouponFlow) break;
		}

		return ((dblPVFromZC + dblWorkoutFactor * zc.getDF (dblWorkoutDate) * getNotional (dblWorkoutDate)) /
			zc.getDF (dblCashPayDate) - calcAccrued (valParams._dblValue, mktParams)) / dblScalingNotional;
	}

	@Override public double calcPriceFromBumpedDC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDCBump)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDCBump))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromBumpedDC");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPVFromDC = 0.;
		boolean bTerminateCouponFlow = false;
		double dblScalingNotional = java.lang.Double.NaN;

		if (null != _notlParams && _notlParams._bPriceOffOriginalNotional) dblScalingNotional = 1.;

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (0. != dblDCBump)
			dc = (org.drip.analytics.definition.DiscountCurve) dc.createParallelShiftedCurve (dblDCBump);

		if (null == dc)
			throw new java.lang.Exception
				("Cannot create shifted DC in BondComponent::calcPriceFromBumpedDC");

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

			if (java.lang.Double.isNaN (dblScalingNotional))
				dblScalingNotional = getNotional (period.getStartDate());

			double dblAccrualEndDate = period.getAccrualEndDate();

			double dblNotionalEndDate = period.getEndDate();

			if (dblAccrualEndDate >= dblWorkoutDate) {
				bTerminateCouponFlow = true;
				dblAccrualEndDate = dblWorkoutDate;
				dblNotionalEndDate = dblWorkoutDate;
			}

			double dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

			double dblDF = dc.getDF (period.getPayDate());

			double dblCouponNotional = getNotional (period.getStartDate());

			if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_END ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (dblNotionalEndDate);
			else if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_EFFECTIVE ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (period.getStartDate(), dblNotionalEndDate);

			dblPVFromDC += period.getAccrualDCF (dblAccrualEndDate) * dblDF * dblPeriodCoupon *
				dblCouponNotional;

			dblPVFromDC += (getNotional (period.getStartDate()) - getNotional (dblNotionalEndDate)) * dblDF;

			if (bTerminateCouponFlow) break;
		}

		double dblCashPayDate = java.lang.Double.NaN;

		try {
			dblCashPayDate = _mktConv.getSettleDate (valParams);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			dblCashPayDate = valParams._dblCashPay;
		}

		return ((dblPVFromDC + dblWorkoutFactor * dc.getDF (dblWorkoutDate) * getNotional (dblWorkoutDate)) /
			dc.getDF (dblCashPayDate) - calcAccrued (valParams._dblValue, mktParams)) / dblScalingNotional;
	}

	@Override public double calcPriceFromBumpedCC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis,
		final boolean bFlat)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromBumpedCC");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		org.drip.analytics.definition.CreditCurve cc = null;

		if (bFlat) {
			double dblRecoveryCalib = java.lang.Double.NaN;

			if (null != _crValParams && !_crValParams._bUseCurveRec)
				dblRecoveryCalib = _crValParams._dblRecovery;

			cc = mktParams.getCreditCurve().createFlatCurve (dblCreditBasis, true, dblRecoveryCalib);
		} else
			cc = (org.drip.analytics.definition.CreditCurve)
				mktParams.getCreditCurve().createParallelShiftedCurve (dblCreditBasis);

		if (null == cc)
			throw new java.lang.Exception
				("Cannot create adjusted CC in BondComponent::calcPriceFromBumpedCC");

		double dblPVFromCC = 0.;

		org.drip.param.pricer.PricerParams pricerParams = new org.drip.param.pricer.PricerParams (7, null,
			false, s_iDiscretizationScheme);

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

			double dblAccrualEndDate = period.getAccrualEndDate();

			double dblNotionalEndDate = period.getEndDate();

			if (dblAccrualEndDate >= dblWorkoutDate) {
				dblAccrualEndDate = dblWorkoutDate;
				dblNotionalEndDate = dblWorkoutDate;
			}

			double dblPeriodStart = period.getStartDate();

			if (dblPeriodStart < valParams._dblValue) dblPeriodStart = valParams._dblValue;

			double dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

			double dblPeriodEndSurv = cc.getSurvival (period.getEndDate());

			double dblCouponNotional = getNotional (period.getStartDate());

			if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_END ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (dblNotionalEndDate);
			else if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_EFFECTIVE ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (period.getStartDate(), dblNotionalEndDate);

			dblPVFromCC += period.getAccrualDCF (dblAccrualEndDate) * mktParams.getDiscountCurve().getDF
				(period.getPayDate()) * dblPeriodEndSurv * dblPeriodCoupon * dblCouponNotional;

			dblPVFromCC += (getNotional (period.getStartDate()) - getNotional (period.getEndDate())) *
				mktParams.getDiscountCurve().getDF (period.getPayDate()) * dblPeriodEndSurv;

			if (s_bBlog)
				System.out.println (org.drip.analytics.date.JulianDate.fromJulian (dblPeriodStart) + "=>" +
					org.drip.analytics.date.JulianDate.fromJulian (period.getEndDate()) + ": " +
						org.drip.math.common.FormatUtil.FormatDouble (dblPVFromCC, 1, 3, 100.));

			java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> sLPSub =
				org.drip.analytics.support.AnalyticsHelper.GenerateLossPeriods (this, valParams,
					pricerParams, period, period.getEndDate() < dblWorkoutDate ? period.getEndDate() :
						dblWorkoutDate, mktParams);

			if (null == sLPSub || 0 == sLPSub.size()) continue;

			for (org.drip.analytics.period.LossPeriodCurveFactors lp : sLPSub) {
				if (null == lp) continue;

				double dblSubPeriodEnd = lp.getEndDate();

				double dblSubPeriodStart = lp.getStartDate();

				double dblSubPeriodDF = mktParams.getDiscountCurve().getEffectiveDF (dblSubPeriodStart +
					_crValParams._iDefPayLag, dblSubPeriodEnd + _crValParams._iDefPayLag);

				double dblSubPeriodNotional = getNotional (dblSubPeriodStart, dblSubPeriodEnd);

				double dblSubPeriodSurvival = mktParams.getCreditCurve().getSurvival (dblSubPeriodStart) -
					mktParams.getCreditCurve().getSurvival (dblSubPeriodEnd);

				if (_crValParams._bAccrOnDefault)
					dblPVFromCC += 0.0001 * lp.getAccrualDCF() * dblSubPeriodSurvival * dblSubPeriodDF *
						dblSubPeriodNotional * dblPeriodCoupon;

				double dblRec = _crValParams._bUseCurveRec ? mktParams.getCreditCurve().getEffectiveRecovery
					(dblSubPeriodStart, dblSubPeriodEnd) : _crValParams._dblRecovery;

				dblPVFromCC += dblRec * dblSubPeriodSurvival * dblSubPeriodNotional * dblSubPeriodDF;

				if (s_bBlog)
					System.out.println ("\t" + org.drip.analytics.date.JulianDate.fromJulian
						(lp.getStartDate()) + "=>" + org.drip.analytics.date.JulianDate.fromJulian
							(lp.getEndDate()) + ": " + org.drip.math.common.FormatUtil.FormatDouble
								(dblPVFromCC, 1, 3, 100.));
			}
		}

		double dblCashPayDate = java.lang.Double.NaN;

		try {
			dblCashPayDate = _mktConv.getSettleDate (valParams);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			dblCashPayDate = valParams._dblCashPay;
		}

		double dblScalingNotional = 1.;

		if (!_notlParams._bPriceOffOriginalNotional) dblScalingNotional = getNotional (dblWorkoutDate);

		return ((dblPVFromCC + dblWorkoutFactor * mktParams.getDiscountCurve().getDF (dblWorkoutDate) *
			cc.getSurvival (dblWorkoutDate) * getNotional (dblWorkoutDate)) /
				mktParams.getDiscountCurve().getDF (dblCashPayDate) - calcAccrued (valParams._dblValue,
					mktParams)) / dblScalingNotional;
	}

	@Override public double calcASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcASWFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcASWFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromBondBasisToOptimalExercise => " +
				"Cannot calc ASW from Bond Basis to Optimal Exercise for bonds w emb option");

		return calcASWFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcASWFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcASWFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromCreditBasisToOptimalExercise => " +
				"Cannot calc ASW from Credit Basis to Optimal Exercise for bonds w emb option");

		return calcASWFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcASWFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcASWFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromDiscountMarginToOptimalExercise => " +
				"Cannot calc ASW from Discount Margin to optimal exercise for bonds w emb option");

		return calcASWFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcASWFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcASWFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromGSpreadToOptimalExercise => " +
				"Cannot calc ASW from G Spread to optimal exercise for bonds w emb option");

		return calcASWFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcASWFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcASWFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromISpreadToOptimalExercise => " +
				"Cannot calc ASW from I Spread to optimal exercise for bonds w emb option");

		return calcASWFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcASWFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
	}

	@Override public double calcASWFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromOASToOptimalExercise => " +
				"Cannot calc ASW from OAS to optimal exercise for bonds w emb option");

		return calcASWFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
	}

	@Override public double calcASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcASWFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcASWFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromPECSToOptimalExercise => " +
				"Cannot calc ASW from PECS to optimal exercise for bonds w emb option");

		return calcASWFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblPrice) || valParams._dblValue >=
					dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcASWFromPrice => Invalid Inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc) throw new java.lang.Exception ("BondComponent::calcASWFromPrice => Invalid Inputs");

		return getCoupon (dblWorkoutDate, mktParams) - dc.interpMeasure (dblWorkoutDate) + 0.01 *
			(dblWorkoutFactor - dblPrice) / dc.calcLIBORDV01 (dblWorkoutDate);
	}

	@Override public double calcASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcASWFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcASWFromPriceToOptimalExercise => Can't determine Work-out");

		return calcASWFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
	}

	@Override public double calcASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcASWFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcASWFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromTSYSpreadToOptimalExercise => " +
				"Cannot calc ASW from TSY Spread to optimal exercise for bonds w emb option");

		return calcASWFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcASWFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcASWFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromYieldToOptimalExercise => " +
				"Cannot calc ASW from Yield to optimal exercise for bonds w emb option");

		return calcASWFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcASWFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcASWFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromYieldSpreadToOptimalExercise => " +
				"Cannot calc ASW from Yield Spread to optimal exercise for bonds w emb option");

		return calcASWFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcASWFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcASWFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcASWFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcASWFromZSpreadToOptimalExercise => " +
				"Cannot calc ASW from Yield Spread to optimal exercise for bonds w emb option");

		return calcASWFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcBondBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcBondBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcBondBasisFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcBondBasisFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromASWToOptimalExercise => " +
				"Cannot calc Bond Basis from ASW to optimal exercise for bonds w emb option");

		return calcBondBasisFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcBondBasisFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromCreditBasisToOptimalExercise => "
				+ "Cannot calc Bond Basis from Credit Basis to optimal exercise for bonds w emb option");

		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcBondBasisFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromDiscountMarginToOptimalExercise "
				+ "=> Cant calc Bond Basis from Discount Margin to optimal exercise for bonds w emb option");

		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcBondBasisFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromGSpreadToOptimalExercise => " +
				"Cant calc Bond Basis from G Spread to optimal exercise for bonds w emb option");

		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcBondBasisFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromISpreadToOptimalExercise => " +
				"Cant calc Bond Basis from I Spread to optimal exercise for bonds w emb option");

		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcBondBasisFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromOASToOptimalExercise => " +
				"Cant calc Bond Basis from OAS to optimal exercise for bonds w emb option");

		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcBondBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcBondBasisFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromPECSToOptimalExercise => " +
				"Cant calc Bond Basis from PECS to optimal exercise for bonds w emb option");

		return calcBondBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice));
	}

	@Override public double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcBondBasisFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);
		
		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcBondBasisFromPriceToOptimalExercise => cant calc Work-out info");

		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcBondBasisFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromTSYSpreadToOptimalExercise => " +
				"Cant calc Bond Basis from TSY Spread to optimal exercise for bonds w emb option");

		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblYield))
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromYield => Invalid inputs");

		return dblYield - calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0.));
	}

	@Override public double calcBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcBondBasisFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromYieldToOptimalExercise => " +
				"Cant calc Bond Basis from Yield to optimal exercise for bonds w emb option");

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcBondBasisFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromYieldSpreadToOptimalExercise " +
				"=> Cant calc Bond Basis from Yield Spread to optimal exercise for bonds w emb option");

		return calcBondBasisFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcBondBasisFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcBondBasisFromZSpreadToOptimalExercise => " +
				"Cant calc Bond Basis from Z Spread to optimal exercise for bonds w emb option");

		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcConvexityFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcConvexityFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcConvexityFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcConvexityFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromASWToOptimalExercise => " +
				"Cant calc Convexity from ASW to optimal exercise for bonds w emb option");

		return calcConvexityFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcConvexityFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcConvexityFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromBondBasisToOptimalExercise => " +
				"Cant calc Convexity from Bond Basis to optimal exercise for bonds w emb option");

		return calcConvexityFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcConvexityFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcConvexityFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromCreditBasisToOptimalExercise => "
				+ "Cant calc Convexity from Credit Basis to optimal exercise for bonds w emb option");

		return calcConvexityFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblDiscountMargin));
	}

	@Override public double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcConvexityFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcConvexityFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromDiscountMarginToOptimalExercise "
				+ "=> Cant calc Convexity from Discount Margin to optimal exercise for bonds w emb option");

		return calcDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcConvexityFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromGSpreadToOptimalExercise => " +
				"Cant calc Convexity from G Spread to optimal exercise for bonds w emb option");

		return calcConvexityFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcConvexityFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcConvexityFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromISpreadToOptimalExercise => " +
				"Cant calc Convexity from I Spread to optimal exercise for bonds w emb option");

		return calcConvexityFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcConvexityFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcConvexityFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromOASToOptimalExercise => " +
				"Cant calc Convexity from OAS to optimal exercise for bonds w emb option");

		return calcConvexityFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcConvexityFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcConvexityFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromPECSToOptimalExercise => " +
				"Cant calc Convexity from PECS to optimal exercise for bonds w emb option");

		return calcConvexityFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP ||
			!org.drip.math.common.NumberUtil.IsValid (dblPrice))
			throw new java.lang.Exception ("BondComponent::calcConvexityFromPrice => Input inputs");

		double dblPriceForYieldMinus1bp = calcPriceFromYield (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblPrice) - 0.0001);

		double dblPriceForYieldPlus1bp = calcPriceFromYield (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblPrice) + 0.0001);

		double dblDirtyPrice = dblPrice + calcAccrued (valParams._dblValue, mktParams);

		return (dblPriceForYieldMinus1bp + dblPriceForYieldPlus1bp - 2. * dblPrice) / dblDirtyPrice;
	}

	@Override public double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcConvexityFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromPriceToOptimalExercise => " +
				"Cant calc Convexity from Price to optimal exercise for bonds w emb option");

		return calcConvexityFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcConvexityFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromTSYSpreadToOptimalExercise => " +
				"Cant calc Convexity from TSY Sprd to optimal exercise for bonds w emb option");

		return calcConvexityFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcConvexityFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcConvexityFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromYieldToOptimalExercise => " +
				"Cant calc Convexity from Yield to optimal exercise for bonds w emb option");

		return calcConvexityFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcConvexityFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromYieldSpreadToOptimalExercise => "
				+ "Cant calc Convexity from Yld Sprd to optimal exercise for bonds w emb option");

		return calcConvexityFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcConvexityFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcConvexityFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcConvexityFromZSpreadToOptimalExercise => " +
				"Cant calc Convexity from Z Spread to optimal exercise for bonds w emb option");

		return calcConvexityFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcCreditBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblASW));
	}

	@Override public double calcCreditBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcCreditBasisFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcCreditBasisFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromASWToOptimalExercise => " +
				"Cannot calc Credit Basis from ASW to optimal exercise for bonds w emb option");

		return calcCreditBasisFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis));
	}

	@Override public double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcCreditBasisFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcCreditBasisFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromBondBasisToOptimalExercise " +
				"=> Cant calc Credit Basis from Bond Basis to optimal exercise for bonds w emb option");

		return calcCreditBasisFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromDiscountMargin (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcCreditBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcCreditBasisFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcCreditBasisFromDiscountMarginToOptimalExercise => " +
					"Cant calc Credit Basis from Discnt Margin to optimal exercise for bonds w emb option");

		return calcCreditBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread));
	}

	@Override public double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
	}

	@Override public double calcCreditBasisFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromGSpreadToOptimalExercise => " +
				"Cant calc Credit Basis from G Spread to optimal exercise for bonds w emb option");

		return calcCreditBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
	}

	@Override public double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread));
	}

	@Override public double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
	}

	@Override public double calcCreditBasisFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromISpreadToOptimalExercise => " +
				"Cant calc Credit Basis from I Spread to optimal exercise for bonds w emb option");

		return calcCreditBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
	}

	@Override public double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS));
	}

	@Override public double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcCreditBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcCreditBasisFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromOASToOptimalExercise => " +
				"Cant calc Credit Basis from OAS to optimal exercise for bonds w emb option");

		return calcCreditBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS));
	}

	@Override public double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcCreditBasisFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromPECSToOptimalExercise => " +
				"Cant calc Credit Basis from PECS to optimal exercise for bonds w emb option");

		return calcCreditBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice, false);
	}

	@Override public double calcCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcCreditBasisFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcCreditBasisFromPriceToOptimalExercise => cant calc Work-out");

		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread));
	}

	@Override public double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcCreditBasisFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromTSYSpreadToOptimalExercise => "
				+ "Cant calc Credit Basis from TSY Spread to optimal exercise for bonds w emb option");

		return calcCreditBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield));
	}

	@Override public double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcCreditBasisFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromYieldToOptimalExercise => " +
				"Cant calc Credit Basis from Yield to optimal exercise for bonds w emb option");

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYieldSpread));
	}

	@Override public double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcCreditBasisFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws	java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromYieldSpreadToOptimalExercise "
				+ "=> Cant calc Credit Basis from Yield Spread to optimal exercise for bonds w emb option");

		return calcCreditBasisFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread));
	}

	@Override public double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcCreditBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
	}

	@Override public double calcCreditBasisFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcCreditBasisFromZSpreadToOptimalExercise => " +
				"Cant calc Credit Basis from Z Spread to optimal exercise for bonds w emb option");

		return calcCreditBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
	}

	@Override public double calcDiscountMarginFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblASW));
	}

	@Override public double calcDiscountMarginFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcDiscountMarginFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromASWToOptimalExercise => " +
				"Cant calc Discount Margin from ASW to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis));
	}

	@Override public double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcDiscountMarginFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromBondBasisToOptimalExercise "
				+ "=> Cant calc Discount Margin from Bond Basis to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis));
	}

	@Override public double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcDiscountMarginFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcDiscountMarginFromCreditBasisToOptimalExercise => " +
					"Cant calc Discount Margin from Crdit Basis to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread));
	}

	@Override public double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcDiscountMarginFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromGSpreadToOptimalExercise =>"
				+ " => Cant calc Discount Margin from G Spread to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcDiscountMarginFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread));
	}

	@Override public double calcDiscountMarginFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcDiscountMarginFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromISpreadToOptimalExercise " +
				"=> Cant calc Discount Margin from I Spread to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS));
	}

	@Override public double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcDiscountMarginFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromOASToOptimalExercise => " +
				"Cant calc Discount Margin from OAS to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS));
	}

	@Override public double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcDiscountMarginFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromPECSToOptimalExercise => " +
				"Cant calc Discount Margin from PECS to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice));
	}

	@Override public double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPrice);
	}

	@Override public double calcDiscountMarginFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcDiscountMarginFromPriceToOptimalExercise => Can't do Work-out");

		return calcDiscountMarginFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread));
	}

	@Override public double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcDiscountMarginFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromTSYSpreadToOptimalExercise "
				+ "=> Cant calc Discount Margin from TSY Spread to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblYield))
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromYield => Invalid inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromYield => Invalid inputs");

		return null == _fltParams ? dblYield - dc.calcLIBOR (((int) (12. / (0 == _periodParams._iFreq ? 2 :
			_periodParams._iFreq))) + "M") : dblYield - getIndexRate (valParams._dblValue, dc,
				mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));
	}

	@Override public double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYield);
	}

	@Override public double calcDiscountMarginFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromYieldToOptimalExercise =>" +
				" Cant calc Discount Margin from Yield to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYield);
	}

	@Override public double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYieldSpread));
	}

	@Override public double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcDiscountMarginFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcDiscountMarginFromYieldSpreadToOptimalExercise => " +
					"Cant calc Discount Margin from Yield Sprd to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread));
	}

	@Override public double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcDiscountMarginFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcDiscountMarginFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDiscountMarginFromZSpreadToOptimalExercise =>"
				+ " Cant calc Discount Margin from Z Spread to optimal exercise for bonds w emb option");

		return calcDiscountMarginFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromASWToOptimalExercise => " +
				"Cant calc Duration from ASW to optimal exercise for bonds w emb option");

		return calcDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcDurationFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromBondBasisToOptimalExercise => " +
				"Cant calc Duration from Bond Basis to optimal exercise for bonds w emb option");

		return calcDurationFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcDurationFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromCreditBasisToOptimalExercise => "
				+ "Cant calc Duration from Credit Basis to optimal exercise for bonds w emb option");

		return calcDurationFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromDiscountMarginToOptimalExercise "
				+ "=> Cant calc Duration from Discount Margin to optimal exercise for bonds w emb option");

		return calcDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcDurationFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromGSpreadToOptimalExercise => " +
				"Cant calc Duration from G Spread to optimal exercise for bonds w emb option");

		return calcDurationFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcDurationFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromISpreadToOptimalExercise => " +
				"Cant calc Duration from I Spread to optimal exercise for bonds w emb option");

		return calcDurationFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromOASToOptimalExercise => " +
				"Cant calc Duration from OAS to optimal exercise for bonds w emb option");

		return calcDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromPECSToOptimalExercise => " +
				"Cant calc Duration from PECS to optimal exercise for bonds w emb option");

		return calcDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);
	}

	@Override public double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromPriceToOptimalExercise => " +
				"Cant calc Duration from Price to optimal exercise for bonds w emb option");

		return calcDurationFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcDurationFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromTSYSpreadToOptimalExercise => " +
				"Cant calc Duration from TSY Sprd to optimal exercise for bonds w emb option");

		return calcDurationFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcDurationFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromYieldToOptimalExercise => " +
				"Cant calc Duration from Yield to optimal exercise for bonds w emb option");

		return calcDurationFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcDurationFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromYieldSpreadToOptimalExercise => "
				+ "Cant calc Duration from Yield Spread to optimal exercise for bonds w emb option");

		return calcDurationFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcDurationFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcDurationFromZSpreadToOptimalExercise => " +
				"Cant calc Duration from Z Spread to optimal exercise for bonds w emb option");

		return calcDurationFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcGSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcGSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcGSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcGSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromASWToOptimalExercise => " +
				"Cant calc G Spread from ASW to optimal exercise for bonds w emb option");

		return calcGSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcGSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcGSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromBondBasisToOptimalExercise => " +
				"Cant calc G Spread from Bond Basis to optimal exercise for bonds w emb option");

		return calcGSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcGSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcGSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromCreditBasisToOptimalExercise => " +
				"Cant calc G Spread from Credit Basis to optimal exercise for bonds w emb option");

		return calcGSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcGSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcGSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromDiscountMarginToOptimalExercise =>"
				+ " Cant calc G Spread from Discount Margin to optimal exercise for bonds w emb option");

		return calcGSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcGSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromISpreadToOptimalExercise => " +
				"Cant calc G Spread from I Spread to optimal exercise for bonds w emb option");

		return calcGSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcGSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcGSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromOASToOptimalExercise => " +
				"Cant calc G Spread from OAS to optimal exercise for bonds w emb option");

		return calcGSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcGSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcGSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromPECSToOptimalExercise => " +
				"Cant calc G Spread from PECS to optimal exercise for bonds w emb option");

		return calcGSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice));
	}

	@Override public double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcGSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcGSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcGSpreadFromPriceToOptimalExercise => Can't do Work-out");

		return calcGSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcGSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromTSYSpreadToOptimalExercise => " +
				"Cant calc G Spread from TSY Spread to optimal exercise for bonds w emb option");

		return calcGSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblYield) || valParams._dblValue >= dblWorkoutDate
					+ LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromYield => Invalid inputs");

		org.drip.analytics.definition.DiscountCurve dcTSY = mktParams.getTSYDiscountCurve();

		if (null == dcTSY)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromYield => Invalid inputs");

		return dblYield - dcTSY.interpMeasure (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcGSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromYieldToOptimalExercise => " +
				"Cant calc G Spread from Yield to optimal exercise for bonds w emb option");

		return calcGSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcGSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromYieldSpreadToOptimalExercise => " +
				"Cant calc G Spread from Yield Spread to optimal exercise for bonds w emb option");

		return calcGSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcGSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcGSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcGSpreadFromZSpreadToOptimalExercise => " +
				"Cant calc G Spread from Z Spread to optimal exercise for bonds w emb option");

		return calcGSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcISpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcISpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcISpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcISpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromASWToOptimalExercise => " +
				"Cant calc I Spread from ASW to optimal exercise for bonds w emb option");

		return calcISpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcISpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcISpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromBondBasisToOptimalExercise => " +
				"Cant calc I Spread from Bond Basis to optimal exercise for bonds w emb option");

		return calcISpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcISpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcISpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromCreditBasisToOptimalExercise => " +
				"Cant calc I Spread from Credit Basis to optimal exercise for bonds w emb option");

		return calcISpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcISpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcISpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcISpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcISpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromDiscountMarginToOptimalExercise =>"
				+ " Cant calc I Spread from Discount Margin to optimal exercise for bonds w emb option");

		return calcISpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcISpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromGSpreadToOptimalExercise => " +
				"Cant calc I Spread from G Spread to optimal exercise for bonds w emb option");

		return calcISpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcISpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcISpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromOASToOptimalExercise => " +
				"Cant calc I Spread from OAS to optimal exercise for bonds w emb option");

		return calcISpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcISpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcISpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromPECSToOptimalExercise => " +
				"Cant calc I Spread from PECS to optimal exercise for bonds w emb option");

		return calcISpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice));
	}

	@Override public double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcISpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcISpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcISpreadFromPriceToOptimalExercise => Can't do Work-out");

		return calcISpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcISpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromTSYSpreadToOptimalExercise => " +
				"Cant calc I Spread from TSY Spread to optimal exercise for bonds w emb option");

		return calcISpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblYield))
			throw new java.lang.Exception ("BondComponent::calcISpreadFromYield => Invalid inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromYield => Invalid inputs");

		return dblYield - dc.interpMeasure (dblWorkoutDate);
	}

	@Override public double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcISpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromYieldToOptimalExercise => " +
				"Cant calc I Spread from Yield to optimal exercise for bonds w emb option");

		return calcISpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcISpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromYieldSpreadToOptimalExercise => " +
				"Cant calc I Spread from Yield Spread to optimal exercise for bonds w emb option");

		return calcISpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcISpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcISpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcISpreadFromZSpreadToOptimalExercise => " +
				"Cant calc I Spread from Z Spread to optimal exercise for bonds w emb option");

		return calcISpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcMacaulayDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblASW));
	}

	@Override public double calcMacaulayDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcMacaulayDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromASWToOptimalExercise => " +
					"Cant calc Macaulay Duration from ASW to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcMacaulayDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis));
	}

	@Override public double calcMacaulayDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcMacaulayDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromBondBasisToOptimalExercise => " +
					"Cant calc Macaulay Duration from Bnd Basis to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcMacaulayDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis));
	}

	@Override public double calcMacaulayDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcMacaulayDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromCreditBasisToOptimalExercise => " +
					"Cant calc Macaulay Duration from Crd Basis to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcMacaulayDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromDiscountMargin (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcMacaulayDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcMacaulayDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromDiscountMarginToOptimalExercise => " +
					"Cant calc Macaulay Duration from Disc Marg to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcMacaulayDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread));
	}

	@Override public double calcMacaulayDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcMacaulayDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromGSpreadToOptimalExercise => " +
					"Cant calc Macaulay Duration from G Spread to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcMacaulayDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread));
	}

	@Override public double calcMacaulayDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcMacaulayDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromISpreadToOptimalExercise => " +
					"Cant calc Macaulay Duration from I Spread to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcMacaulayDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS));
	}

	@Override public double calcMacaulayDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcMacaulayDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromOASToOptimalExercise => " +
					"Cant calc Macaulay Duration from OAS to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcMacaulayDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS));
	}

	@Override public double calcMacaulayDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcMacaulayDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromPECSToOptimalExercise => " +
					"Cant calc Macaulay Duration from PECS to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcMacaulayDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice));
	}

	@Override public double calcMacaulayDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromPrice (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblPrice);
	}

	@Override public double calcMacaulayDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromPriceToOptimalExercise => Cant determine Work-out");

		return calcMacaulayDurationFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcMacaulayDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread));
	}

	@Override public double calcMacaulayDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcMacaulayDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromTSYSpreadToOptimalExercise => " +
					"Cant calc Macaulay Duration from TSY Sprd to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcMacaulayDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcMacaulayDurationFromYield => Invalid inputs");

		double dblYieldPV = 0.;
		double dblCFPeriod = 0.;
		double dblDuration = 0.;
		boolean bFirstPeriod = true;
		boolean bTerminateCouponFlow = false;
		org.drip.analytics.period.Period periodRef = null;

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

			if (bFirstPeriod) {
				bFirstPeriod = false;

				dblCFPeriod = period.getCouponDCF() - period.getAccrualDCF (valParams._dblValue);
			} else
				dblCFPeriod += period.getCouponDCF();

			periodRef = period;

			double dblAccrualEndDate = period.getAccrualEndDate();

			double dblNotionalEndDate = period.getEndDate();

			if (dblAccrualEndDate >= dblWorkoutDate) {
				bTerminateCouponFlow = true;
				dblAccrualEndDate = dblWorkoutDate;
				dblNotionalEndDate = dblWorkoutDate;
			}

			double dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

			int iFrequency = _periodParams._iFreq;
			java.lang.String strDC = _periodParams._strCouponDC;
			boolean bApplyCpnEOMAdj = _periodParams._bApplyCpnEOMAdj;
			java.lang.String strCalendar = _ccyParams._strCouponCurrency;

			org.drip.analytics.daycount.ActActDCParams aap = new org.drip.analytics.daycount.ActActDCParams
				(iFrequency, period.getAccrualStartDate(), period.getAccrualEndDate());

			if (null != quotingParams) {
				strDC = quotingParams._strYieldDC;
				iFrequency = quotingParams._iYieldFrequency;
				strCalendar = quotingParams._strYieldCalendar;
				bApplyCpnEOMAdj = quotingParams._bYieldApplyEOMAdj;

				if (null == (aap = quotingParams._aapYield))
					aap = new org.drip.analytics.daycount.ActActDCParams (quotingParams._iYieldFrequency,
						period.getAccrualStartDate(), period.getAccrualEndDate());
			} else if (null != _mktConv && null != _mktConv._quotingParams) {
				strDC = _mktConv._quotingParams._strYieldDC;
				iFrequency = _mktConv._quotingParams._iYieldFrequency;
				strCalendar = _mktConv._quotingParams._strYieldCalendar;
				bApplyCpnEOMAdj = _mktConv._quotingParams._bYieldApplyEOMAdj;

				if (null == (aap = _mktConv._quotingParams._aapYield))
					aap = new org.drip.analytics.daycount.ActActDCParams
						(_mktConv._quotingParams._iYieldFrequency, period.getAccrualStartDate(),
							period.getAccrualEndDate());
			}

			double dblYearFract = org.drip.analytics.daycount.Convention.YearFraction
				(valParams._dblValue, period.getPayDate(), strDC, bApplyCpnEOMAdj, dblWorkoutDate, aap,
					strCalendar);

			double dblYieldDF = org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFrequency, dblYield,
				s_bYieldDFOffofCouponAccrualDCF ? dblCFPeriod : dblYearFract);

			double dblCouponNotional = getNotional (period.getStartDate());

			if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_END ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (dblNotionalEndDate);
			else if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_EFFECTIVE ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (period.getStartDate(), dblNotionalEndDate);

			double dblCouponPV = period.getAccrualDCF (dblAccrualEndDate) * dblPeriodCoupon * dblYieldDF *
				dblCouponNotional;

			double dblPeriodNotionalPV = (getNotional (period.getStartDate()) - getNotional
				(dblNotionalEndDate)) * dblYieldDF;

			dblYieldPV += (dblCouponPV + dblPeriodNotionalPV);
			dblDuration += dblCFPeriod * (dblCouponPV + dblPeriodNotionalPV);

			if (bTerminateCouponFlow) break;
		}

		int iFrequency = _periodParams._iFreq;
		java.lang.String strDC = _periodParams._strCouponDC;
		org.drip.analytics.daycount.ActActDCParams aap = null;
		boolean bApplyCpnEOMAdj = _periodParams._bApplyCpnEOMAdj;
		java.lang.String strCalendar = _ccyParams._strCouponCurrency;

		if (null != periodRef)
			aap = new org.drip.analytics.daycount.ActActDCParams (iFrequency,
				periodRef.getAccrualStartDate(), periodRef.getAccrualEndDate());

		if (null != quotingParams) {
			strDC = quotingParams._strYieldDC;
			iFrequency = quotingParams._iYieldFrequency;
			strCalendar = quotingParams._strYieldCalendar;
			bApplyCpnEOMAdj = quotingParams._bYieldApplyEOMAdj;

			if (null != periodRef)
				aap = new org.drip.analytics.daycount.ActActDCParams (quotingParams._iYieldFrequency,
					periodRef.getAccrualStartDate(), periodRef.getAccrualEndDate());
		} else if (null != _mktConv && null != _mktConv._quotingParams) {
			strDC = _mktConv._quotingParams._strYieldDC;
			iFrequency = _mktConv._quotingParams._iYieldFrequency;
			strCalendar = _mktConv._quotingParams._strYieldCalendar;
			bApplyCpnEOMAdj = _mktConv._quotingParams._bYieldApplyEOMAdj;

			if (null != periodRef)
				aap = new org.drip.analytics.daycount.ActActDCParams
					(_mktConv._quotingParams._iYieldFrequency, periodRef.getAccrualStartDate(),
						periodRef.getAccrualEndDate());
		}

		double dblYearFractWorkout = org.drip.analytics.daycount.Convention.YearFraction
			(valParams._dblValue, dblWorkoutDate, strDC, bApplyCpnEOMAdj, dblWorkoutDate, aap, strCalendar);

		double dblDFWorkout = org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFrequency, dblYield,
			s_bYieldDFOffofCouponAccrualDCF ? dblCFPeriod : dblYearFractWorkout);

		double dblRedemptionPV = dblWorkoutFactor * dblDFWorkout * getNotional (dblWorkoutDate);

		return (dblDuration + dblCFPeriod * dblRedemptionPV) / (dblYieldPV + dblRedemptionPV);
	}

	@Override public double calcMacaulayDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcMacaulayDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcMacaulayDurationFromYieldToOptimalExercise =>"
				+ " Cant calc Macaulay Duration from Yield to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcMacaulayDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYieldSpread));
	}

	@Override public double calcMacaulayDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcMacaulayDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromYieldSpreadToOptimalExercise => " +
					"Cant calc Macaulay Duration from Yld Sprd to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcMacaulayDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread));
	}

	@Override public double calcMacaulayDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcMacaulayDurationFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcMacaulayDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcMacaulayDurationFromZSpreadToOptimalExercise => " +
					"Cant calc Macaulay Duration from Z Spread to optimal exercise for bonds w emb option");

		return calcMacaulayDurationFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcModifiedDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblASW));
	}

	@Override public double calcModifiedDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcModifiedDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromASWToOptimalExercise => " +
					"Cant calc Modified Duration from ASW to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblASW);
	}

	@Override public double calcModifiedDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis));
	}

	@Override public double calcModifiedDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcModifiedDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromBondBasisToOptimalExercise => " +
					"Cant calc Modified Duration from Bnd Basis to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcModifiedDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis));
	}

	@Override public double calcModifiedDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcModifiedDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromCreditBasisToOptimalExercise => " +
					"Cant calc Modified Duration from Crd Basis to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcModifiedDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromDiscountMargin (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcModifiedDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcModifiedDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromDiscountMarginToOptimalExercise => " +
					"Cant calc Modified Duration from Disc Marg to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcModifiedDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread));
	}

	@Override public double calcModifiedDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcModifiedDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromGSpreadToOptimalExercise => " +
					"Cant calc Modified Duration from G Spread to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromGSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcModifiedDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread));
	}

	@Override public double calcModifiedDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcModifiedDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromISpreadToOptimalExercise => " +
					"Cant calc Modified Duration from I Spread to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromISpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcModifiedDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS));
	}

	@Override public double calcModifiedDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcModifiedDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromOASToOptimalExercise => " +
					"Cant calc Modified Duration from OAS to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcModifiedDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS));
	}

	@Override public double calcModifiedDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcModifiedDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromPECSToOptimalExercise => " +
					"Cant calc Modified Duration from PECS to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcModifiedDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP ||
			!org.drip.math.common.NumberUtil.IsValid (dblPrice))
			throw new java.lang.Exception ("BondComponent::calcModifiedDurationFromPrice => Input inputs");

		return (dblPrice - calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice) + 0.0001)) / (dblPrice + calcAccrued (valParams._dblValue,
					mktParams));
	}

	@Override public double calcModifiedDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblPrice);
	}

	@Override public double calcModifiedDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromPriceToOptimalExercise => Cant determine Work-out");

		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcModifiedDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread));
	}

	@Override public double calcModifiedDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcModifiedDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromTSYSpreadToOptimalExercise => " +
					"Cant calc Modified Duration from TSY Sprd to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromTSYSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcModifiedDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield));
	}

	@Override public double calcModifiedDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromYield (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcModifiedDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcModifiedDurationFromYieldToOptimalExercise =>"
				+ " Cant calc Modified Duration from Yield to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromYield (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcModifiedDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYieldSpread));
	}

	@Override public double calcModifiedDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcModifiedDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromYieldSpreadToOptimalExercise => " +
					"Cant calc Modified Duration from Yld Sprd to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcModifiedDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread));
	}

	@Override public double calcModifiedDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcModifiedDurationFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcModifiedDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcModifiedDurationFromZSpreadToOptimalExercise => " +
					"Cant calc Modified Duration from Z Spread to optimal exercise for bonds w emb option");

		return calcModifiedDurationFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcOASFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcOASFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcOASFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblASW);
	}

	@Override public double calcOASFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromASWToOptimalExercise => " +
				"Cant calc OAS from ASW to optimal exercise for bonds w emb option");

		return calcOASFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblASW);
	}

	@Override public double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcOASFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcOASFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromBondBasisToOptimalExercise => " +
				"Cant calc OAS from Bnd Basis to optimal exercise for bonds w emb option");

		return calcOASFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcOASFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcOASFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromCreditBasisToOptimalExercise => " +
				"Cant calc OAS from Credit Basis to optimal exercise for bonds w emb option");

		return calcOASFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcOASFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcOASFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromDiscountMarginToOptimalExercise => " +
				"Cant calc OAS from Discount Margin to optimal exercise for bonds w emb option");

		return calcOASFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcOASFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcOASFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromGSpreadToOptimalExercise => " +
				"Cant calc OAS from G Spread to optimal exercise for bonds w emb option");

		return calcOASFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcOASFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcOASFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromISpreadToOptimalExercise => " +
				"Cant calc OAS from I Spread to optimal exercise for bonds w emb option");

		return calcOASFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcOASFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcOASFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromPECSToOptimalExercise => " +
				"Cant calc OAS from PECS to optimal exercise for bonds w emb option");

		return calcOASFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP ||
			!org.drip.math.common.NumberUtil.IsValid (dblPrice))
			throw new java.lang.Exception ("BondComponent::calcOASFromPrice => Input inputs");

		return new BondCalibrator (this).calibDiscCurveSpreadFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);
	}

	@Override public double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcOASFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcOASFromPriceToOptimalExercise - cant calc Work-out");

		return calcOASFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
	}

	@Override public double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcOASFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcOASFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromTSYSpreadToOptimalExercise => " +
				"Cant calc OAS from TSY Sprd to optimal exercise for bonds w emb option");

		return calcOASFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcOASFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcOASFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromYieldToOptimalExercise => " +
				"Cant calc OAS from Yield to optimal exercise for bonds w emb option");

		return calcOASFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcOASFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcOASFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromYieldSpreadToOptimalExercise => " +
				"Cant calc OAS from Yield Sprd to optimal exercise for bonds w emb option");

		return calcOASFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcOASFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcOASFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcOASFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcOASFromZSpreadToOptimalExercise => " +
				"Cant calc OAS from Z Spread to optimal exercise for bonds w emb option");

		return calcOASFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcPECSFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcPECSFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcPECSFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblASW);
	}

	@Override public double calcPECSFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromASWToOptimalExercise => " +
				"Cant calc PECS from ASW to optimal exercise for bonds w emb option");

		return calcPECSFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblASW);
	}

	@Override public double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcPECSFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcPECSFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromBondBasisToOptimalExercise => " +
				"Cant calc PECS from Bond Basis to optimal exercise for bonds w emb option");

		return calcPECSFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcPECSFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcPECSFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromCreditBasisToOptimalExercise => " +
				"Cant calc PECS from Credit Basis to optimal exercise for bonds w emb option");

		return calcPECSFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcPECSFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcPECSFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromDiscountMarginToOptimalExercise => " +
				"Cant calc PECS from Discount Margin to optimal exercise for bonds w emb option");

		return calcPECSFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcPECSFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcPECSFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromGSpreadToOptimalExercise => " +
				"Cant calc PECS from G Spread to optimal exercise for bonds w emb option");

		return calcPECSFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcPECSFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcPECSFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromISpreadToOptimalExercise => " +
				"Cant calc PECS from I Spread to optimal exercise for bonds w emb option");

		return calcPECSFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcPECSFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
	}

	@Override public double calcPECSFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromOASToOptimalExercise => " +
				"Cant calc PECS from OAS to optimal exercise for bonds w emb option");

		return calcPECSFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
	}

	@Override public double calcPECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice, true);
	}

	@Override public double calcPECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcPECSFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcPECSFromPriceToOptimalExercise => Cant determine Work-out");

		return calcPECSFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
	}

	@Override public double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcPECSFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcPECSFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromTSYSpreadToOptimalExercise => " +
				"Cant calc PECS from TSY Spread to optimal exercise for bonds w emb option");

		return calcPECSFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcPECSFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPECSFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromYieldToOptimalExercise => " +
				"Cant calc PECS from Yield to optimal exercise for bonds w emb option");

		return calcPECSFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcPECSFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcPECSFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromYieldSpreadToOptimalExercise => " +
				"Cant calc PECS from Yield Spread to optimal exercise for bonds w emb option");

		return calcPECSFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcPECSFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcPECSFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcPECSFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPECSFromZSpreadToOptimalExercise => " +
				"Cant calc PECS from Z Spread to optimal exercise for bonds w emb option");

		return calcPECSFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcPriceFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblASW) || valParams._dblValue >=
					dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcPriceFromASW => Invalid Inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc) throw new java.lang.Exception ("BondComponent::calcPriceFromASW => Invalid Inputs");

		return dblWorkoutFactor - 100. * dc.calcLIBORDV01 (dblWorkoutDate) * (dblASW + dc.interpMeasure
			(dblWorkoutDate) - getCoupon (dblWorkoutDate, mktParams));
	}

	@Override public double calcPriceFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcPriceFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcPriceFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromASWToOptimalExercise => " +
				"Cant calc Price from ASW to optimal exercise for bonds w emb option");

		return calcPriceFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcPriceFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromBondBasisToOptimalExercise => " +
				"Cant calc Price from Bond Basis to optimal exercise for bonds w emb option");

		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcPriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblCreditBasis,
			false);
	}

	@Override public double calcPriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcPriceFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcPriceFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromCreditBasisToOptimalExercise => " +
				"Cant calc Price from Credit Basis to optimal exercise for bonds w emb option");

		return calcPriceFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblDiscountMargin));
	}

	@Override public double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcPriceFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromDiscountMarginToOptimalExercise => "
				+ "Cant calc Price from Discount Margin to optimal exercise for bonds w emb option");

		return calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcPriceFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcPriceFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromGSpreadToOptimalExercise => " +
				"Cant calc Price from G Spread to optimal exercise for bonds w emb option");

		return calcPriceFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcPriceFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcPriceFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromISpreadToOptimalExercise => " +
				"Cant calc Price from I Spread to optimal exercise for bonds w emb option");

		return calcPriceFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblOAS);
	}

	@Override public double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcPriceFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcPriceFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromOASToOptimalExercise => " +
				"Cant calc Price from OAS to optimal exercise for bonds w emb option");

		return calcPriceFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblPECS, true);
	}

	@Override public double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcPriceFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcPriceFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromPECSToOptimalExercise => " +
				"Cant calc Price from PECS to optimal exercise for bonds w emb option");

		return calcPriceFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcPriceFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcPriceFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromTSYSpreadToOptimalExercise => " +
				"Cant calc Price from TSY Spread to optimal exercise for bonds w emb option");

		return calcPriceFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcPriceFromYield => Invalid inputs");

		double dblYieldPV = 0.;
		double dblCFPeriod = 0.;
		boolean bFirstPeriod = true;
		boolean bTerminateCouponFlow = false;
		double dblScalingNotional = java.lang.Double.NaN;
		org.drip.analytics.period.Period periodRef = null;

		if (null != _notlParams && _notlParams._bPriceOffOriginalNotional) dblScalingNotional = 1.;

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

			if (bFirstPeriod) {
				bFirstPeriod = false;

				dblCFPeriod = period.getCouponDCF() - period.getAccrualDCF (valParams._dblValue);
			} else
				dblCFPeriod += period.getCouponDCF();

			periodRef = period;

			double dblAccrualEndDate = period.getAccrualEndDate();

			if (s_bBlog)
				System.out.println ("Unadjusted Accrual End: " + new org.drip.analytics.date.JulianDate
					(dblAccrualEndDate));

			double dblNotionalEndDate = period.getEndDate();

			if (dblAccrualEndDate >= dblWorkoutDate) {
				bTerminateCouponFlow = true;
				dblAccrualEndDate = dblWorkoutDate;
				dblNotionalEndDate = dblWorkoutDate;
			}

			if (s_bBlog)
				System.out.println ("Adjusted Accrual End: " + new org.drip.analytics.date.JulianDate
					(dblAccrualEndDate));

			if (java.lang.Double.isNaN (dblScalingNotional))
				dblScalingNotional = getNotional (period.getStartDate());

			double dblPeriodCoupon = getCoupon (valParams._dblValue, mktParams);

			int iFrequency = _periodParams._iFreq;
			java.lang.String strDC = _periodParams._strCouponDC;
			boolean bApplyCpnEOMAdj = _periodParams._bApplyCpnEOMAdj;
			java.lang.String strCalendar = _ccyParams._strCouponCurrency;

			org.drip.analytics.daycount.ActActDCParams aap = new org.drip.analytics.daycount.ActActDCParams
				(iFrequency, period.getAccrualStartDate(), period.getAccrualEndDate());

			if (null != quotingParams) {
				strDC = quotingParams._strYieldDC;
				iFrequency = quotingParams._iYieldFrequency;
				strCalendar = quotingParams._strYieldCalendar;
				bApplyCpnEOMAdj = quotingParams._bYieldApplyEOMAdj;

				if (null == (aap = quotingParams._aapYield))
					aap = new org.drip.analytics.daycount.ActActDCParams (quotingParams._iYieldFrequency,
						period.getAccrualStartDate(), period.getAccrualEndDate());
			} else if (null != _mktConv && null != _mktConv._quotingParams) {
				strDC = _mktConv._quotingParams._strYieldDC;
				iFrequency = _mktConv._quotingParams._iYieldFrequency;
				strCalendar = _mktConv._quotingParams._strYieldCalendar;
				bApplyCpnEOMAdj = _mktConv._quotingParams._bYieldApplyEOMAdj;

				if (null == (aap = _mktConv._quotingParams._aapYield))
					aap = new org.drip.analytics.daycount.ActActDCParams
						(_mktConv._quotingParams._iYieldFrequency, period.getAccrualStartDate(),
							period.getAccrualEndDate());
			}

			double dblYearFract = org.drip.analytics.daycount.Convention.YearFraction
				(valParams._dblValue, period.getPayDate(), strDC, bApplyCpnEOMAdj, dblWorkoutDate, aap,
					strCalendar);

			double dblYieldDF = org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFrequency, dblYield,
				s_bYieldDFOffofCouponAccrualDCF ? dblCFPeriod : dblYearFract);

			double dblCouponNotional = getNotional (period.getStartDate());

			if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_END ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (dblNotionalEndDate);
			else if (org.drip.product.params.NotionalSetting.PERIOD_AMORT_EFFECTIVE ==
				_notlParams._iPeriodAmortizationMode)
				dblCouponNotional = getNotional (period.getStartDate(), dblNotionalEndDate);

			double dblCouponPV = period.getAccrualDCF (dblAccrualEndDate) * dblPeriodCoupon * dblYieldDF *
				dblCouponNotional;

			dblYieldPV += dblCouponPV;

			if (s_bBlog) {
				System.out.println ("Coupon Notional: " + dblCouponNotional);

				System.out.println ("Period Coupon: " + dblPeriodCoupon);

				System.out.println ("\n" + org.drip.analytics.date.JulianDate.fromJulian (dblAccrualEndDate)
					+ "; DCF=" + org.drip.math.common.FormatUtil.FormatDouble
						(period.getAccrualDCF (dblAccrualEndDate), 1, 3, 100.) + "; Eff Notl=" +
							org.drip.math.common.FormatUtil.FormatDouble
								(getNotional (period.getStartDate(), dblNotionalEndDate), 1, 3, 100.) +
									"; PV: " + org.drip.math.common.FormatUtil.FormatDouble (dblYieldPV, 1,
										3, 100.));

				System.out.println ("Incremental Cpn PV: " + org.drip.math.common.FormatUtil.FormatDouble
					(dblCouponPV, 1, 3, 100.));
			}

			dblYieldPV += (getNotional (period.getStartDate()) - getNotional (dblNotionalEndDate)) *
				dblYieldDF;

			if (s_bBlog) {
				System.out.println (org.drip.analytics.date.JulianDate.fromJulian (period.getStartDate()) +
					"->" + org.drip.analytics.date.JulianDate.fromJulian (dblNotionalEndDate) + "; Notl:" +
						org.drip.math.common.FormatUtil.FormatDouble (getNotional (period.getStartDate()), 1,
							3, 100.) + "->" + org.drip.math.common.FormatUtil.FormatDouble (getNotional
								(period.getEndDate()), 1, 3, 100.) + "; Coupon=" +
									org.drip.math.common.FormatUtil.FormatDouble (dblPeriodCoupon, 1, 3,
										100.));

				System.out.println ("Incremental Notl PV: " + org.drip.math.common.FormatUtil.FormatDouble
					((getNotional (period.getStartDate()) - getNotional (dblNotionalEndDate)) * dblYieldDF,
						1, 3, 100.));

				System.out.println ("YF: " + org.drip.math.common.FormatUtil.FormatDouble (dblYearFract, 1,
					3, 100.) + "; DF: " + dblYieldDF + "; PV: " +
						org.drip.math.common.FormatUtil.FormatDouble (dblYieldPV, 1, 3, 100.));
			}

			if (bTerminateCouponFlow) break;
		}

		double dblCashPayDate = java.lang.Double.NaN;

		try {
			dblCashPayDate = _mktConv.getSettleDate (valParams);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();

			dblCashPayDate = valParams._dblCashPay;
		}

		int iFrequency = _periodParams._iFreq;
		java.lang.String strDC = _periodParams._strCouponDC;
		org.drip.analytics.daycount.ActActDCParams aap = null;
		boolean bApplyCpnEOMAdj = _periodParams._bApplyCpnEOMAdj;
		java.lang.String strCalendar = _ccyParams._strCouponCurrency;

		if (null != periodRef)
			aap = new org.drip.analytics.daycount.ActActDCParams (iFrequency,
				periodRef.getAccrualStartDate(), periodRef.getAccrualEndDate());

		if (null != quotingParams) {
			strDC = quotingParams._strYieldDC;
			iFrequency = quotingParams._iYieldFrequency;
			strCalendar = quotingParams._strYieldCalendar;
			bApplyCpnEOMAdj = quotingParams._bYieldApplyEOMAdj;

			if (null != periodRef)
				aap = new org.drip.analytics.daycount.ActActDCParams (quotingParams._iYieldFrequency,
					periodRef.getAccrualStartDate(), periodRef.getAccrualEndDate());
		} else if (null != _mktConv && null != _mktConv._quotingParams) {
			strDC = _mktConv._quotingParams._strYieldDC;
			iFrequency = _mktConv._quotingParams._iYieldFrequency;
			strCalendar = _mktConv._quotingParams._strYieldCalendar;
			bApplyCpnEOMAdj = _mktConv._quotingParams._bYieldApplyEOMAdj;

			if (null != periodRef)
				aap = new org.drip.analytics.daycount.ActActDCParams
					(_mktConv._quotingParams._iYieldFrequency, periodRef.getAccrualStartDate(),
						periodRef.getAccrualEndDate());
		}

		double dblYearFractCashPay = org.drip.analytics.daycount.Convention.YearFraction
			(valParams._dblValue, dblCashPayDate, strDC, bApplyCpnEOMAdj, dblWorkoutDate, aap, strCalendar);

		double dblDFCashPay = org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFrequency, dblYield,
			dblYearFractCashPay);

		if (s_bBlog)
			System.out.println ("CP Date: " + new org.drip.analytics.date.JulianDate (dblCashPayDate) +
				"; DF: " + dblDFCashPay);

		double dblAccrued = calcAccrued (valParams._dblValue, mktParams);

		double dblYearFractWorkout = org.drip.analytics.daycount.Convention.YearFraction
			(valParams._dblValue, dblWorkoutDate, strDC, bApplyCpnEOMAdj, dblWorkoutDate, aap, strCalendar);

		double dblDFWorkout = org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFrequency, dblYield,
			s_bYieldDFOffofCouponAccrualDCF ? dblCFPeriod : dblYearFractWorkout);

		if (s_bBlog) System.out.println ("DF Workout: " + dblDFWorkout);

		double dblPV = (((dblYieldPV + dblWorkoutFactor * dblDFWorkout * getNotional (dblWorkoutDate)) /
			dblDFCashPay) - dblAccrued);

		if (s_bBlog)
			System.out.println ("Accrued: " + dblAccrued + "; Clean PV: " +
				org.drip.math.common.FormatUtil.FormatDouble (dblPV, 1, 3, 100.) + "; PV Scale: " +
					getNotional (valParams._dblValue));

		return dblPV / dblScalingNotional;
	}

	@Override public double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPriceFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromYieldToOptimalExercise => " +
				"Cannot calc exercise px from yld for bonds w emb option");

		return calcPriceFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcPriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcPriceFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcPriceFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromYieldSpreadToOptimalExercise => " +
				"Cant calc Price from Yield Spread to optimal exercise for bonds w emb option");

		return calcPriceFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcPriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcPriceFromBumpedZC (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread);
	}

	@Override public double calcPriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcPriceFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcPriceFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcPriceFromZSpreadToOptimalExercise => " +
				"Cant calc Price from Z Spread to optimal exercise for bonds w emb option");

		return calcPriceFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcTSYSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcTSYSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcTSYSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromASWToOptimalExercise => " +
				"Cant calc TSY Spread from ASW to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcTSYSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromBondBasisToOptimalExercise => " +
				"Cant calc TSY Spread from Bond Basis to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcTSYSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromCreditBasisToOptimalExercise => "
				+ "Cant calc TSY Spread from Credit Basis to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblDiscountMargin));
	}

	@Override public double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcTSYSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromDiscountMarginToOptimalExercise "
				+ "=> Cant calc TSY Spread from Discount Margin to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcTSYSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromGSpreadToOptimalExercise => " +
				"Cant calc TSY Spread from G Spread to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcTSYSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromISpreadToOptimalExercise => " +
				"Cant calc TSY Spread from I Spread to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcTSYSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromOASToOptimalExercise => " +
				"Cant calc TSY Spread from OAS to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcTSYSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromPECSToOptimalExercise => " +
				"Cant calc TSY Spread from PECS to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice));
	}

	@Override public double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcTSYSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception
				("BondComponent::calcTSYSpreadFromPriceToOptimalExercise => Cant determine Work-out");

		return calcTSYSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		double dblBmkYield = getTsyBmkYield (valParams, mktParams, dblWorkoutDate);

		if (!org.drip.math.common.NumberUtil.IsValid (dblBmkYield))
			throw new java.lang.Exception
				("BondComponent::calcTSYSpreadFromYield => Cannot calculate TSY Bmk Yield");

		return dblYield - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcTSYSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromYieldToOptimalExercise => " +
				"Cant calc TSY Spread from Yield to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcTSYSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromYieldSpreadToOptimalExercise => "
				+ "Cant calc TSY Spread from Yield Spread to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcTSYSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcTSYSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcTSYSpreadFromZSpreadToOptimalExercise => " +
				"Cant calc TSY Spread from Z Spread to optimal exercise for bonds w emb option");

		return calcTSYSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcYieldFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcYieldFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYieldFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYieldFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromASWToOptimalExercise => " +
				"Cant calc Yield from ASW to optimal exercise for bonds w emb option");

		return calcYieldFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblBondBasis) || valParams._dblValue >= dblWorkoutDate
			+ LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcYieldFromBondBasis => Invalid Inputs");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.)) +
				dblBondBasis;
	}

	@Override public double calcYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcYieldFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromBondBasisToOptimalExercise => " +
				"Cant calc Yield from Bond Basis to optimal exercise for bonds w emb option");

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcYieldFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromCreditBasisToOptimalExercise => " +
				"Cant calc Yield from Credit Basis to optimal exercise for bonds w emb option");

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || !org.drip.math.common.NumberUtil.IsValid
			(dblWorkoutDate) || !org.drip.math.common.NumberUtil.IsValid (dblWorkoutFactor) ||
				!org.drip.math.common.NumberUtil.IsValid (dblDiscountMargin))
			throw new java.lang.Exception ("BondComponent::calcYieldFromDiscountMargin => Invalid inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc)
			throw new java.lang.Exception ("BondComponent::calcYieldFromDiscountMargin => Invalid inputs");

		return null == _fltParams ? dblDiscountMargin + dc.calcLIBOR (((int) (12. / (0 ==
			_periodParams._iFreq ? 2 : _periodParams._iFreq))) + "M") : dblDiscountMargin - getIndexRate
				(valParams._dblValue, dc, mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));
	}

	@Override public double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcYieldFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromDiscountMarginToOptimalExercise => "
				+ "Cant calc Yield from Discount Margin to optimal exercise for bonds w emb option");

		return calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblGSpread) || valParams._dblValue >= dblWorkoutDate +
			LEFT_EOS_SNIP || null == mktParams)
			throw new java.lang.Exception ("BondComponent::calcYieldFromGSpread => Invalid Inputs");

		org.drip.analytics.definition.DiscountCurve dcGovvie = mktParams.getTSYDiscountCurve();

		if (null == dcGovvie)
			throw new java.lang.Exception ("BondComponent::calcYieldFromGSpread => Invalid Inputs");

		return dcGovvie.interpMeasure (dblWorkoutDate) + dblGSpread;
	}

	@Override public double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcYieldFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcYieldFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromGSpreadToOptimalExercise => " +
				"Cant calc Yield from G Spread to optimal exercise for bonds w emb option");

		return calcYieldFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblISpread) || valParams._dblValue >= dblWorkoutDate +
			LEFT_EOS_SNIP || null == mktParams)
			throw new java.lang.Exception ("BondComponent::calcYieldFromISpread => Invalid Inputs");

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getTSYDiscountCurve();

		if (null == dc)
			throw new java.lang.Exception ("BondComponent::calcYieldFromISpread => Invalid Inputs");

		return dc.interpMeasure (dblWorkoutDate) + dblISpread;
	}

	@Override public double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcYieldFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcYieldFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromISpreadToOptimalExercise => " +
				"Cant calc Yield from I Spread to optimal exercise for bonds w emb option");

		return calcYieldFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYieldFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYieldFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromOASToOptimalExercise => " +
				"Cant calc Yield from OAS to optimal exercise for bonds w emb option");

		return calcYieldFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYieldFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYieldFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromPECSToOptimalExercise => " +
				"Cant calc Yield from PECS to optimal exercise for bonds w emb option");

		return calcYieldFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return new BondCalibrator (this).calibrateYieldFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);
	}

	@Override public double calcYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYieldFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYieldFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcYieldFromPriceToOptimalExercise => " +
				"Cant calc Workout from Price to optimal exercise for bonds w emb option");

		return wi._dblYield;
	}

	@Override public double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblTSYSpread) || valParams._dblValue >= dblWorkoutDate
			+ LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcYieldFromTSYSpread => Invalid Inputs");

		return getTsyBmkYield (valParams, mktParams, dblWorkoutDate) + dblTSYSpread;
	}

	@Override public double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYieldFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYieldFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromTSYSpreadToOptimalExercise => " +
				"Cant calc Yield from TSY Spread to optimal exercise for bonds w emb option");

		return calcYieldFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblYieldSpread) || valParams._dblValue >=
			dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcYieldFromYieldSpread => Invalid Inputs");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.)) +
				dblYieldSpread;
	}

	@Override public double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcYieldFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcYieldFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromYieldSpreadToOptimalExercise => " +
				"Cant calc Yield from Yield Spread to optimal exercise for bonds w emb option");

		return calcYieldFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblZSpread) || valParams._dblValue >= dblWorkoutDate +
			LEFT_EOS_SNIP)
			throw new java.lang.Exception ("BondComponent::calcYieldFromZSpread => Invalid Inputs");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedZC (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcYieldFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcYieldFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldFromZSpreadToOptimalExercise => " +
				"Cant calc Yield from Z Spread to optimal exercise for bonds w emb option");

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcYield01FromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcYield01FromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYield01FromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYield01FromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromASWToOptimalExercise => " +
				"Cant calc Yield from ASW to optimal exercise for bonds w emb option");

		return calcYield01FromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcYield01FromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromBondBasisToOptimalExercise => " +
				"Cant calc Yield01 from Bond Basis to optimal exercise for bonds w emb option");

		return calcYield01FromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYield01FromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcYield01FromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromCreditBasisToOptimalExercise => " +
				"Cant calc Yield01 from Credit Basis to optimal exercise for bonds w emb option");

		return calcYield01FromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcYield01FromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcYield01FromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromDiscountMarginToOptimalExercise =>"
				+ " Cant calc Yield01 from Discount Margin to optimal exercise for bonds w emb option");

		return calcYield01FromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcYield01FromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcYield01FromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromGSpreadToOptimalExercise => " +
				"Cant calc Yield01 from G Spread to optimal exercise for bonds w emb option");

		return calcYield01FromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcYield01FromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcYield01FromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromISpreadToOptimalExercise => " +
				"Cant calc Yield01 from I Spread to optimal exercise for bonds w emb option");

		return calcYield01FromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYield01FromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYield01FromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromOASToOptimalExercise => " +
				"Cant calc Yield01 from OAS to optimal exercise for bonds w emb option");

		return calcYield01FromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYield01FromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYield01FromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromPECSToOptimalExercise => " +
				"Cant calc Yield01 from PECS to optimal exercise for bonds w emb option");

		return calcYield01FromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice));
	}

	@Override public double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYield01FromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYield01FromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcYield01FromPriceToOptimalExercise => " +
				"Cant calc Workout from Price to optimal exercise for bonds w emb option");

		return calcYield01FromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
	}

	@Override public double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYield01FromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYield01FromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromTSYSpreadToOptimalExercise => " +
				"Cant calc Yield01 from TSY Spread to optimal exercise for bonds w emb option");

		return calcYield01FromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblYield))
			throw new java.lang.Exception ("BondComponent::calcYield01FromYield => Invalid Inputs");

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYield) - calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield + 0.0001);
	}

	@Override public double calcYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcYield01FromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromYieldToOptimalExercise => " +
				"Cant calc Yield01 from Yield to optimal exercise for bonds w emb option");

		return calcYield01FromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcYield01FromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcYield01FromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromYieldSpreadToOptimalExercise => " +
				"Cant calc Yield01 from Yield Spread to optimal exercise for bonds w emb option");

		return calcYield01FromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread));
	}

	@Override public double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcYield01FromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcYield01FromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYield01FromZSpreadToOptimalExercise => " +
				"Cant calc Yield01 from Z Spread to optimal exercise for bonds w emb option");

		return calcYield01FromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcYieldSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromASW (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblASW));
	}

	@Override public double calcYieldSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYieldSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromASWToOptimalExercise => " +
				"Cant calc Yield Spread from ASW to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis));
	}

	@Override public double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcYieldSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromBondBasisToOptimalExercise => "
				+ "Cant calc Yield Spread from Bond Basis to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis));
	}

	@Override public double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcYieldSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromCreditBasisToOptimalExercise "
				+ "=> Cant calc Yield Spread from Credit Basis to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromDiscountMargin (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcYieldSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcYieldSpreadFromDiscountMarginToOptimalExercise => " +
					"Cant calc Yield Spread from Disc Margin to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread));
	}

	@Override public double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
	}

	@Override public double calcYieldSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromGSpreadToOptimalExercise => " +
				"Cant calc Yield Spread from G Spread to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
	}

	@Override public double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread));
	}

	@Override public double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
	}

	@Override public double calcYieldSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromISpreadToOptimalExercise => " +
				"Cant calc Yield Spread from I Spread to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
	}

	@Override public double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS));
	}

	@Override public double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYieldSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromOASToOptimalExercise => " +
				"Cant calc Yield Spread from OAS to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS));
	}

	@Override public double calcYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYieldSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromPECSToOptimalExercise => " +
				"Cant calc Yield Spread from PECS to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice));
	}

	@Override public double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYieldSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromPriceToOptimalExercise => " +
				"Cant calc Workout from Price to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread));
	}

	@Override public double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPrice);
	}

	@Override public double calcYieldSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromTSYSpreadToOptimalExercise => "
				+ "Cant calc Yield Spread from TSY Spread to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPrice);
	}

	@Override public double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblYield))
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromYield => Invalid Inputs");

		return dblYield - calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0.));
	}

	@Override public double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcYieldSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromYieldToOptimalExercise => " +
				"Cant calc Yield Spread from Yield to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread));
	}

	@Override public double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcYieldSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
	}

	@Override public double calcYieldSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcYieldSpreadFromZSpreadToOptimalExercise => " +
				"Cant calc Yield Spread from Z Spread to optimal exercise for bonds w emb option");

		return calcYieldSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
	}

	@Override public double calcZSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromASW (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblASW));
	}

	@Override public double calcZSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		return calcZSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcZSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromASWToOptimalExercise => " +
				"Cant calc Z Spread from ASW to optimal exercise for bonds w emb option");

		return calcZSpreadFromASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblASW);
	}

	@Override public double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblBondBasis));
	}

	@Override public double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcZSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromBondBasisToOptimalExercise => "
				+ "Cant calc Z Spread from Bond Basis to optimal exercise for bonds w emb option");

		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis));
	}

	@Override public double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcZSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcZSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromCreditBasisToOptimalExercise => " +
				"Cant calc Z Spread from Credit Basis to optimal exercise for bonds w emb option");

		return calcZSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin));
	}

	@Override public double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcZSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcZSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("BondComponent::calcZSpreadFromDiscountMarginToOptimalExercise => " +
					"Cant calc Z Spread from Discount Margin to optimal exercise for bonds w emb option");

		return calcZSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblGSpread));
	}

	@Override public double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcZSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromGSpreadToOptimalExercise => " +
				"Cant calc Z Spread from G Spread to optimal exercise for bonds w emb option");

		return calcZSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblISpread));
	}

	@Override public double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcZSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromISpreadToOptimalExercise => " +
				"Cant calc Z Spread from I Spread to optimal exercise for bonds w emb option");

		return calcZSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS));
	}

	@Override public double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcZSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcZSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromOASToOptimalExercise => " +
				"Cant calc Z Spread from OAS to optimal exercise for bonds w emb option");

		return calcZSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS));
	}

	@Override public double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		return calcZSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcZSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromPECSToOptimalExercise => " +
				"Cant calc Z Spread from PECS to optimal exercise for bonds w emb option");

		return calcZSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		return new BondCalibrator (this).calibrateZSpreadFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);
	}

	@Override public double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcZSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromPriceToOptimalExercise => " +
				"Cant calc Workout from Price to optimal exercise for bonds w emb option");

		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
	}

	@Override public double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread));
	}

	@Override public double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcZSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcZSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromTSYSpreadToOptimalExercise => " +
				"Cant calc Z Spread from TSY Spread to optimal exercise for bonds w emb option");

		return calcZSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYield));
	}

	@Override public double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcZSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcZSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromYieldToOptimalExercise => " +
				"Cant calc Z Spread from Yield to optimal exercise for bonds w emb option");

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblYieldSpread));
	}

	@Override public double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcZSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcZSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("BondComponent::calcZSpreadFromYieldSpreadToOptimalExercise => " +
				"Cant calc Z Spread from Yield Spread to optimal exercise for bonds w emb option");

		return calcZSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public org.drip.analytics.output.BondRVMeasures standardMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.valuation.WorkoutInfo wi,
		final double dblPrice)
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null == wi ||
			java.lang.Double.isNaN (dblPrice) || valParams._dblValue >= wi._dblDate + LEFT_EOS_SNIP)
			return null;

		double dblStdPECS = java.lang.Double.NaN;
		double dblBmkYield = java.lang.Double.NaN;
		double dblStdPrice = java.lang.Double.NaN;
		double dblStdYield = java.lang.Double.NaN;
		double dblStdGSpread = java.lang.Double.NaN;
		double dblStdZSpread = java.lang.Double.NaN;
		double dblStdOASpread = java.lang.Double.NaN;
		double dblStdDuration = java.lang.Double.NaN;
		double dblStdConvexity = java.lang.Double.NaN;
		double dblStdTSYSpread = java.lang.Double.NaN;
		double dblStdPriceAtY01 = java.lang.Double.NaN;
		double dblStdCreditBasis = java.lang.Double.NaN;
		double dblStdDiscountMargin = java.lang.Double.NaN;
		double dblStdAssetSwapSpread = java.lang.Double.NaN;

		try {
			dblStdPrice = calcPriceFromBumpedDC (valParams, mktParams, wi._dblDate, wi._dblExerciseFactor,
				0.);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			dblStdYield = calcYieldFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
				wi._dblExerciseFactor, dblStdPrice);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			dblStdDiscountMargin = calcDiscountMarginFromPrice (valParams, mktParams, quotingParams,
				wi._dblDate, wi._dblExerciseFactor, dblStdPrice);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		if (null == _fltParams) {
			try {
				dblStdZSpread = new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams,
					mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor, dblPrice);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}

			try {
				dblStdOASpread = new BondCalibrator (this).calibDiscCurveSpreadFromPrice (valParams,
					mktParams, wi._dblDate, wi._dblExerciseFactor, dblPrice);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		}

		try {
			if (null != mktParams.getTSYDiscountCurve())
				dblStdGSpread = wi._dblYield - mktParams.getTSYDiscountCurve().calcLIBOR (wi._dblDate);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			dblBmkYield = getTsyBmkYield (valParams, mktParams, wi._dblDate);

			if (!java.lang.Double.isNaN (dblBmkYield)) dblStdTSYSpread = wi._dblYield - dblBmkYield;
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			dblStdPriceAtY01 = calcPriceFromYield (valParams, mktParams, quotingParams, wi._dblDate,
				wi._dblExerciseFactor, wi._dblYield + 0.0001);

			dblStdDuration = 10000. * (dblPrice - dblStdPriceAtY01) / dblPrice;
			dblStdAssetSwapSpread = 0.0001 * (dblStdPrice - dblPrice) / (dblPrice - dblStdPriceAtY01);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			dblStdConvexity = (dblStdPriceAtY01 + calcPriceFromYield (valParams, mktParams, quotingParams,
				wi._dblDate, wi._dblExerciseFactor, wi._dblYield - 0.0001) - 2. * dblPrice) / dblPrice;
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			if (null != mktParams.getCreditCurve())
				dblStdCreditBasis = new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams,
					mktParams, wi._dblDate, wi._dblExerciseFactor, dblPrice, false);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			if (null != mktParams.getCreditCurve())
				dblStdPECS = new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams,
					wi._dblDate, wi._dblExerciseFactor, dblPrice, true);
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		try {
			return new org.drip.analytics.output.BondRVMeasures (dblPrice, wi._dblYield - dblStdYield,
				dblStdZSpread, dblStdGSpread, wi._dblYield - mktParams.getDiscountCurve().calcLIBOR
					(wi._dblDate), dblStdOASpread, dblStdTSYSpread, dblStdDiscountMargin,
						dblStdAssetSwapSpread, dblStdCreditBasis, dblStdPECS, dblStdDuration,
							dblStdConvexity, wi);

			/* return new org.drip.analytics.output.BondRVMeasures (dblPrice, wi._dblYield - dblStdYield,
				dblStdZSpread, dblStdGSpread, wi._dblYield - mktParams.getDiscountCurve().calcLIBOR
					(wi._dblDate), dblStdOASpread, dblStdTSYSpread, dblStdDiscountMargin,
					 	dblStdAssetSwapSpread, dblStdCreditBasis, dblStdPECS, dblStdDuration,
					 		dblStdConvexity, wi).toMap (strPrefix); */
		} catch (java.lang.Exception e) {
			if (!s_bSuppressErrors) e.printStackTrace();
		}

		return null;
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve()) return null;

		if (null != pricerParams && null != pricerParams._calibParams) {
			java.util.Map<java.lang.String, java.lang.Double> mapCalibMeasures = calibMeasures (valParams,
				pricerParams, mktParams, quotingParams);

			if (null != mapCalibMeasures && mapCalibMeasures.containsKey
				(pricerParams._calibParams._strMeasure))
				return mapCalibMeasures;
		}

		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = calcFairMeasureSet (valParams,
			pricerParams, mktParams, quotingParams);

		if (null == mapMeasures || null == mktParams.getComponentQuote()) return mapMeasures;

		if (null == _fltParams) {
			double dblParSpread = (mapMeasures.get ("FairDirtyPV") - mapMeasures.get ("FairParPV") -
				mapMeasures.get ("FairPrincipalPV")) / mapMeasures.get ("FairDirtyDV01");

			mapMeasures.put ("ParSpread", dblParSpread);

			mapMeasures.put ("FairParSpread", dblParSpread);
		} else {
			double dblZeroDiscountMargin = (mapMeasures.get ("FairDirtyPV") - mapMeasures.get ("FairParPV") -
				mapMeasures.get ("FairIndexCouponPV") - mapMeasures.get ("FairPrincipalPV")) /
					mapMeasures.get ("FairDirtyDV01");

			mapMeasures.put ("ZeroDiscountMargin", dblZeroDiscountMargin);

			mapMeasures.put ("FairZeroDiscountMargin", dblZeroDiscountMargin);
		}

		org.drip.param.valuation.WorkoutInfo wiMarket = null;

		if (null != mktParams.getComponentQuote().getQuote ("Price")) {
			double dblMarketPrice = mktParams.getComponentQuote().getQuote ("Price").getQuote ("mid");

			mapMeasures.put ("MarketInputType=CleanPrice", dblMarketPrice);

			wiMarket = calcExerciseYieldFromPrice (valParams, mktParams, quotingParams, dblMarketPrice);
		} else if (null != mktParams.getComponentQuote().getQuote ("CleanPrice")) {
			double dblCleanMarketPrice = mktParams.getComponentQuote().getQuote ("CleanPrice").getQuote
				("mid");

			mapMeasures.put ("MarketInputType=CleanPrice", dblCleanMarketPrice);

			wiMarket = calcExerciseYieldFromPrice (valParams, mktParams, quotingParams, dblCleanMarketPrice);
		} else if (null != mktParams.getComponentQuote().getQuote ("QuotedMargin")) {
			double dblQuotedMargin = mktParams.getComponentQuote().getQuote ("QuotedMargin").getQuote
				("mid");

			mapMeasures.put ("MarketInputType=QuotedMargin", dblQuotedMargin);

			try {
				wiMarket = calcExerciseYieldFromPrice (valParams, mktParams, quotingParams,
					calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, dblQuotedMargin));
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("DirtyPrice")) {
			try {
				double dblDirtyMarketPrice = mktParams.getComponentQuote().getQuote ("DirtyPrice").getQuote
					("mid");

				mapMeasures.put ("MarketInputType=DirtyPrice", dblDirtyMarketPrice);

				wiMarket = calcExerciseYieldFromPrice (valParams, mktParams, quotingParams,
					dblDirtyMarketPrice - calcAccrued (valParams._dblValue, mktParams));
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("TSYSpread")) {
			try {
				double dblTSYSpread = mktParams.getComponentQuote().getQuote ("TSYSpread").getQuote ("mid");

				mapMeasures.put ("MarketInputType=TSYSpread", dblTSYSpread);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					getTsyBmkYield (valParams, mktParams, getMaturityDate().getJulian()) + dblTSYSpread, 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("Yield")) {
			try {
				double dblYield = mktParams.getComponentQuote().getQuote ("Yield").getQuote ("mid");

				mapMeasures.put ("MarketInputType=Yield", dblYield);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(), dblYield,
					1., org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("ZSpread")) {
			try {
				double dblZSpread = mktParams.getComponentQuote().getQuote ("ZSpread").getQuote ("mid");

				mapMeasures.put ("MarketInputType=ZSpread", dblZSpread);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					calcYieldFromZSpread (valParams, mktParams, quotingParams, dblZSpread), 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("ISpread")) {
			try {
				double dblISpread = mktParams.getComponentQuote().getQuote ("ISpread").getQuote ("mid");

				mapMeasures.put ("MarketInputType=ISpread", dblISpread);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					calcYieldFromISpread (valParams, mktParams, quotingParams, dblISpread), 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("CreditBasis")) {
			try {
				double dblCreditBasis = mktParams.getComponentQuote().getQuote ("CreditBasis").getQuote
					("mid");

				mapMeasures.put ("MarketInputType=CreditBasis", dblCreditBasis);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblCreditBasis), 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("PECS")) {
			try {
				double dblCreditBasis = mktParams.getComponentQuote().getQuote ("PECS").getQuote ("mid");

				mapMeasures.put ("MarketInputType=PECS", dblCreditBasis);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					calcYieldFromPECS (valParams, mktParams, quotingParams, dblCreditBasis), 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		}

		if (null != wiMarket) {
			java.util.Map<java.lang.String, java.lang.Double> mapWorkoutMeasures = calcMarketMeasureSet
				(valParams, pricerParams, mktParams, quotingParams, wiMarket);

			if (null == _fltParams) {
				double dblParSpread = (mapWorkoutMeasures.get ("Price") - mapMeasures.get ("FairParPV") -
					mapMeasures.get ("FairPrincipalPV")) / mapMeasures.get ("FairCleanDV01");

				mapMeasures.put ("ParSpread", dblParSpread);

				mapMeasures.put ("MarketParSpread", dblParSpread);
			} else {
				double dblZeroDiscountMargin = (mapMeasures.get ("Price") - mapMeasures.get ("FairParPV") -
					mapMeasures.get ("FairIndexCouponPV") - mapMeasures.get ("FairPrincipalPV")) /
						mapMeasures.get ("FairCleanDV01");

				mapMeasures.put ("ZeroDiscountMargin", dblZeroDiscountMargin);

				mapMeasures.put ("MarketZeroDiscountMargin", dblZeroDiscountMargin);
			}

			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, mapWorkoutMeasures);

			if (null != mapMeasures.get ("FairYield") && !java.lang.Double.isNaN (wiMarket._dblYield)) {
				org.drip.param.definition.ComponentMarketParams cmpMarket =
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
						(mktParams.getDiscountCurve().createParallelRateShiftedCurve
							(wiMarket._dblYield - mapMeasures.get ("FairYield")),
								mktParams.getTSYDiscountCurve(), mktParams.getEDSFDiscountCurve(),
									mktParams.getCreditCurve(), mktParams.getComponentQuote(),
										mktParams.getTSYBenchmarkQuotes(), mktParams.getFixings());

				if (null != cmpMarket) {
					org.drip.analytics.output.BondWorkoutMeasures bwmMarket = calcBondWorkoutMeasures
						(valParams, pricerParams, cmpMarket, wiMarket._dblDate, wiMarket._dblExerciseFactor);

					if (null != bwmMarket) {
						java.util.Map<java.lang.String, java.lang.Double> mapMarketMeasures = bwmMarket.toMap
							("");

						org.drip.analytics.support.GenericUtil.MergeWithMain (mapMarketMeasures,
							org.drip.analytics.support.GenericUtil.PrefixKeys (mapMarketMeasures, "Market"));

						org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures,
							mapMarketMeasures);
					}
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
		return null;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcQuoteDFMicroJack (
		final java.lang.String strQuote,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= getMaturityDate().getJulian()|| null ==
			strQuote || null == mktParams || null == mktParams.getDiscountCurve())
			return null;

		return null;
	}

	/**
	 * The BondCalibrator implements a calibrator that calibrates the yield, the credit basis, or the Z
	 * 		Spread for the bond given the price input. Calibration happens via either Newton-Raphson method,
	 * 		or via bracketing/root searching.
	 * 
	 * @author Lakshmi Krishnamurthy
	 *
	 */

	public class BondCalibrator {
		private BondComponent _bond = null;

		/**
		 * Constructor: Constructs the calibrator from the parent bond.
		 * 
		 * @param bond Parent
		 * 
		 * @throws java.lang.Exception Thrown if the inputs are invalid
		 */

		public BondCalibrator (
			final BondComponent bond)
			throws java.lang.Exception
		{
			if (null == (_bond = bond))
				throw new java.lang.Exception ("No NULL bond into BondCalibrator constructor");
		}

		/**
		 * Calibrates the bond yield from the market price using the root bracketing technique.
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblPrice Price to be calibrated to
		 * 
		 * @return The calibrated Yield
		 * 
		 * @throws java.lang.Exception Thrown if the yield cannot be calibrated
		 */

		public double calibrateYieldFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblPrice)
			throws java.lang.Exception
		{
			org.drip.math.function.AbstractUnivariate ofYieldToPrice = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblYield)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromYield (valParams, mktParams, null, dblWorkoutDate,
						dblWorkoutFactor, dblYield) - dblPrice;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new
				org.drip.math.solver1D.FixedPointFinderNewton (0., ofYieldToPrice).findRoot();

			if (null == rfop || !rfop.containsRoot()) {
				rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0., ofYieldToPrice).findRoot();

				if (null == rfop || !rfop.containsRoot())
					throw new java.lang.Exception
						("BondComponent.calibrateYieldFromPrice => Cannot get root!");
			}

			return rfop.getRoot();
		}

		/**
		 * Calibrates the bond Z Spread from the market price using the root bracketing technique.
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblPrice Price to be calibrated to
		 * 
		 * @return The calibrated Z Spread
		 * 
		 * @throws java.lang.Exception Thrown if the Z Spread cannot be calibrated
		 */

		public double calibrateZSpreadFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblPrice)
			throws java.lang.Exception
		{
			if (null != _fltParams)
				throw new java.lang.Exception ("Z Spread Calculation turned off for floaters!");

			org.drip.math.function.AbstractUnivariate ofZSpreadToPrice = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblZSpread)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromBumpedZC (valParams, mktParams, null, dblWorkoutDate,
						dblWorkoutFactor, dblZSpread) - dblPrice;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new
				org.drip.math.solver1D.FixedPointFinderBrent (0., ofZSpreadToPrice).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception
					("BondComponent.calibrateZSpreadFromPrice => Cannot get root!");

			return rfop.getRoot();
		}

		/**
		 * Calibrates the bond Z Spread from the market price. Calibration is done by bumping the discount
		 * 		curve.
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblPriceCalib Price to be calibrated to
		 * 
		 * @return The calibrated Z Spread
		 * 
		 * @throws java.lang.Exception Thrown if the yield cannot be calibrated
		 */

		public double calibDiscCurveSpreadFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblPriceCalib)
			throws java.lang.Exception
		{
			org.drip.math.function.AbstractUnivariate ofZSpreadToPrice = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblZSpread)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate,
						dblWorkoutFactor, dblZSpread) - dblPriceCalib;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofZSpreadToPrice).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception
					("BondComponent.calibDiscCurveSpreadFromPrice => Cannot get root!");

			return rfop.getRoot();
		}

		/**
		 * Calibrates the bond Z Spread from the market price. Calibration is done by bumping the Zero Curve.
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param quotingParams Quoting Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblPriceCalib Price to be calibrated to
		 * 
		 * @return The calibrated Z Spread
		 * 
		 * @throws java.lang.Exception Thrown if the yield cannot be calibrated
		 */

		public double calibZeroCurveSpreadFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final org.drip.param.valuation.QuotingParams quotingParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblPriceCalib)
			throws java.lang.Exception
		{
			if (null != _fltParams)
				throw new java.lang.Exception ("Z Spread Calculation turned off for floaters!");

			org.drip.math.function.AbstractUnivariate ofZSpreadToPrice = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblZSpread)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate,
						dblWorkoutFactor, dblZSpread) - dblPriceCalib;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofZSpreadToPrice).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception
					("BondComponent.calibZeroCurveSpreadFromPrice => Cannot get root!");

			return rfop.getRoot();
		}

		/**
		 * Calibrates the bond Credit Basis from the market price
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblPriceCalib Price to be calibrated to
		 * 
		 * @return The calibrated Credit Basis
		 * 
		 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calibrated
		 */

		public double calibrateCreditBasisFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblPriceCalib,
			final boolean bFlatCalib)
			throws java.lang.Exception
		{
			org.drip.math.function.AbstractUnivariate ofCreditBasisToPrice = new
				org.drip.math.function.AbstractUnivariate (null) {
				public double evaluate (
					final double dblCreditBasis)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate,
						dblWorkoutFactor, dblCreditBasis, bFlatCalib) - dblPriceCalib;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofCreditBasisToPrice).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception
					("BondComponent.calibrateCreditBasisFromPrice => Cannot get root!");

			return rfop.getRoot();
		}
	}

	@Override public void showPeriods()
		throws java.lang.Exception
	{
		for (org.drip.analytics.period.Period period : _periodParams.getPeriods())
			System.out.println ("\t" + org.drip.analytics.date.JulianDate.fromJulian (period.getStartDate())
				+ "->" + org.drip.analytics.date.JulianDate.fromJulian (period.getEndDate()) + "    " +
					period.getAccrualDCF (period.getAccrualEndDate()));
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "@";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return ":";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _tsyParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_tsyParams.serialize()) + getFieldDelimiter());

		if (null == _cpnParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_cpnParams.serialize()) + getFieldDelimiter());

		if (null == _notlParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlParams.serialize()) + getFieldDelimiter());

		if (null == _fltParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_fltParams.serialize()) + getFieldDelimiter());

		if (null == getFixings() || null == getFixings().entrySet())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbFixings = new java.lang.StringBuffer();

			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
				java.lang.Double>> meOut : getFixings().entrySet()) {
				if (null == meOut || null == meOut.getValue() || null == meOut.getValue().entrySet())
					continue;

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> meIn :
					meOut.getValue().entrySet()) {
					if (null == meIn || null == meIn.getKey() || meIn.getKey().isEmpty()) continue;

					if (bFirstEntry)
						bFirstEntry = false;
					else
						sb.append (getCollectionRecordDelimiter());

					sbFixings.append (meOut.getKey().getJulian() + getCollectionMultiLevelKeyDelimiter() +
						meIn.getKey() + getCollectionKeyValueDelimiter() + meIn.getValue());
				}
			}

			if (sbFixings.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbFixings.toString() + getFieldDelimiter());
		}

		if (null == _ccyParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_ccyParams.serialize()) + getFieldDelimiter());

		if (null == _idParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_idParams.serialize()) + getFieldDelimiter());

		if (null == _mktConv)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_mktConv.serialize()) + getFieldDelimiter());

		if (null == _crValParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_crValParams.serialize()) + getFieldDelimiter());

		if (null == _cfteParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_cfteParams.serialize()) + getFieldDelimiter());

		if (null == _periodParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_periodParams.serialize()) + getFieldDelimiter());

		if (null == _eosPut)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_eosPut.serialize()) + getFieldDelimiter());

		if (null == _eosCall)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (new java.lang.String (_eosCall.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new BondComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.support.Logger.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblPutDate = new double[3];
		double[] adblCallDate = new double[3];
		double[] adblPutFactor = new double[3];
		double[] adblCallFactor = new double[3];
		double[] adblCouponFactor = new double[3];
		double[] adblNotionalFactor = new double[3];
		adblPutFactor[0] = 0.80;
		adblPutFactor[1] = 0.90;
		adblPutFactor[2] = 1.00;
		adblCallFactor[0] = 1.20;
		adblCallFactor[1] = 1.10;
		adblCallFactor[2] = 1.00;
		adblPutDate[0] = dblStart + 30.;
		adblPutDate[1] = dblStart + 396.;
		adblPutDate[2] = dblStart + 761.;
		adblCallDate[0] = dblStart + 1126.;
		adblCallDate[1] = dblStart + 1492.;
		adblCallDate[2] = dblStart + 1857.;

		for (int i = 0; i < 3; ++i) {
			adblCouponFactor[i] = 1 - 0.1 * i;
			adblNotionalFactor[i] = 1 - 0.05 * i;
			adblDate[i] = dblStart + 365. * (i + 1);
		}

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0402);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (org.drip.analytics.date.JulianDate.Today().addDays (2), mIndexFixings);

		org.drip.product.params.PeriodGenerator bpgp = new
			org.drip.product.params.PeriodGenerator (dblStart + 3653., dblStart, dblStart + 3653.,
				dblStart + 182., dblStart, 2, "30/360", "30/360", null, null, null, null, null, null, null,
					null, "IGNORE", false, "USD");

		if (!bpgp.validate()) {
			System.out.println ("Cannot validate BPGP!");

			System.exit (125);
		}

		BondComponent bond = new BondComponent();

		if (!bond.setTreasuryBenchmark (new org.drip.product.params.TreasuryBenchmark (new
			org.drip.product.params.TsyBmkSet ("USD5YON", new java.lang.String[] {"USD3YON", "USD7YON"}),
				"USDTSY", "USDEDSF"))) {
			System.out.println ("Cannot initialize bond TSY params!");

			System.exit (126);
		}

		if (!bond.setCouponSetting (new org.drip.product.params.CouponSetting
			(org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblCouponFactor),
				"FLOATER", 0.01, java.lang.Double.NaN, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Coupon params!");

			System.exit (127);
		}

		if (!bond.setNotionalSetting (new org.drip.product.params.NotionalSetting
			(org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblNotionalFactor),
				1., org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_START, false))) {
			System.out.println ("Cannot initialize bond Notional params!");

			System.exit (128);
		}

		if (!bond.setFloaterSetting (new org.drip.product.params.FloaterSetting ("USD-LIBOR-6M", "30/360",
			0.01, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Floater params!");

			System.exit (129);
		}

		if (!bond.setFixings (mmFixings)) {
			System.out.println ("Cannot initialize bond Fixings!");

			System.exit (130);
		}

		if (!bond.setCurrencySet (new org.drip.product.params.CurrencySet ("USD", "USD", "USD"))) {
			System.out.println ("Cannot initialize bond currency params!");

			System.exit (131);
		}

		if (!bond.setIdentifierSet (new org.drip.product.params.IdentifierSet ("US07942381EZ",
			"07942381E", "IBM-US07942381EZ", "IBM"))) {
			System.out.println ("Cannot initialize bond Identifier params!");

			System.exit (132);
		}

		if (!bond.setMarketConvention (new org.drip.product.params.QuoteConvention (new
			org.drip.param.valuation.QuotingParams ("30/360", 2, true, null, "DKK", false), "REGULAR",
				dblStart + 2, 1., 3, "USD", org.drip.analytics.daycount.Convention.DR_FOLL))) {
			System.out.println ("Cannot initialize bond IR Valuation params!");

			System.exit (133);
		}

		if (!bond.setRatesSetting (new org.drip.product.params.RatesSetting ("USD",
			"USD", "USD", "USD"))) {
			System.out.println ("Cannot initialize Bond Rates Valuation params!");

			System.exit (153);
		}

		if (!bond.setCreditSetting (new org.drip.product.params.CreditSetting (30,
			java.lang.Double.NaN, true, "IBMSUB", false))) {
			System.out.println ("Cannot initialize bond Credit Valuation params!");

			System.exit (134);
		}

		if (!bond.setTerminationSetting (new org.drip.product.params.TerminationSetting (false, false,
			false))) {
			System.out.println ("Cannot initialize bond CFTE params!");

			System.exit (135);
		}

		if (!bond.setPeriodSet (bpgp)) {
			System.out.println ("Cannot initialize bond Period Generation params!");

			System.exit (136);
		}

		bond.setEmbeddedPutSchedule (org.drip.product.params.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblPutDate, adblPutFactor, true, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		bond.setEmbeddedCallSchedule (org.drip.product.params.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblCallDate, adblCallFactor, false, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		byte[] abBond = bond.serialize();

		System.out.println (new java.lang.String (abBond));

		BondComponent bondDeser = new BondComponent (abBond);

		System.out.println ("\n" + new java.lang.String (bondDeser.serialize()));
	}
}
