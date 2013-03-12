
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

		return mktParams.getEDSFDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	private double getTsyBmkYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		return getTsyBmkYield (valParams, mktParams, _periodParams._dblMaturity);
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
					return dc.calcImpliedRate ((12 / _periodParams._iFreq) + "M");

				return dc.calcImpliedRate (period.getStartDate(), period.getEndDate());
			}

			return mmFixings.get (new org.drip.analytics.date.JulianDate (period.getResetDate())).get
				(_fltParams._strRateIndex);
		}

		double dblRateRefEndDate = dblValue + LOCAL_FORWARD_RATE_WIDTH;

		if (0 != _periodParams._iFreq) dblRateRefEndDate = dblValue + 365.25 / _periodParams._iFreq;

		double dblIndexRate = dc.calcImpliedRate (dblValue, dblRateRefEndDate);

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
					adblSecTSYSpread[i] = mktParams.getEDSFDiscountCurve().calcImpliedRate
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

		return mktParams.getEDSFDiscountCurve().calcImpliedRate (wi._dblDate);
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

			if (period.getStartDate() < dblDate && period.getEndDate() > dblDate) {
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
			zc = org.drip.analytics.creator.ZeroCurveBuilder.CreateZeroCurve (_periodParams.getPeriods(),
				dblWorkoutDate, dblCashPayDate, mktParams.getDiscountCurve(), null == quotingParams ? (null
					== _mktConv ? null : _mktConv._quotingParams) : quotingParams, dblZCBump);
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
						org.drip.analytics.support.GenericUtil.FormatDouble (dblPVFromCC));

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
							(lp.getEndDate()) + ": " +
								org.drip.analytics.support.GenericUtil.FormatDouble (dblPVFromCC));
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

	@Override public double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblYieldPV = 0.;
		boolean bTerminateCouponFlow = false;
		double dblScalingNotional = java.lang.Double.NaN;
		org.drip.analytics.period.Period periodRef = null;

		if (null != _notlParams && _notlParams._bPriceOffOriginalNotional) dblScalingNotional = 1.;

		for (org.drip.analytics.period.Period period : _periodParams.getPeriods()) {
			if (period.getPayDate() < valParams._dblValue) continue;

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
				dblYearFract);

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
					+ "; DCF=" + org.drip.analytics.support.GenericUtil.FormatDouble
						(period.getAccrualDCF (dblAccrualEndDate)) + "; Eff Notl=" +
							org.drip.analytics.support.GenericUtil.FormatDouble
								(getNotional (period.getStartDate(), dblNotionalEndDate)) + "; PV: " +
									org.drip.analytics.support.GenericUtil.FormatDouble (dblYieldPV));

				System.out.println ("Incremental Cpn PV: " +
					org.drip.analytics.support.GenericUtil.FormatDouble (dblCouponPV));
			}

			dblYieldPV += (getNotional (period.getStartDate()) - getNotional (dblNotionalEndDate)) *
				dblYieldDF;

			if (s_bBlog) {
				System.out.println (org.drip.analytics.date.JulianDate.fromJulian (period.getStartDate()) +
					"->" + org.drip.analytics.date.JulianDate.fromJulian (dblNotionalEndDate) + "; Notl:" +
						org.drip.analytics.support.GenericUtil.FormatDouble (getNotional
							(period.getStartDate())) + "->" +
								org.drip.analytics.support.GenericUtil.FormatDouble (getNotional
									(period.getEndDate())) + "; Coupon=" +
										org.drip.analytics.support.GenericUtil.FormatDouble
											(dblPeriodCoupon));

				System.out.println ("Incremental Notl PV: " +
					org.drip.analytics.support.GenericUtil.FormatDouble ((getNotional
						(period.getStartDate()) - getNotional (dblNotionalEndDate)) * dblYieldDF));

				System.out.println ("YF: " + org.drip.analytics.support.GenericUtil.FormatDouble
					(dblYearFract) + "; DF: " + dblYieldDF + "; PV: " +
						org.drip.analytics.support.GenericUtil.FormatDouble (dblYieldPV));
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
			dblYearFractWorkout);

		if (s_bBlog) System.out.println ("DF Workout: " + dblDFWorkout);

		double dblPV = (((dblYieldPV + dblWorkoutFactor * dblDFWorkout * getNotional (dblWorkoutDate)) /
			dblDFCashPay) - dblAccrued);

		if (s_bBlog)
			System.out.println ("Accrued: " + dblAccrued + "; Clean PV: " +
				org.drip.analytics.support.GenericUtil.FormatDouble (dblPV) + "; PV Scale: " +
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromYield");

		return calcPriceFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPriceFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromYTM");

		return calcPriceFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExercisePriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise px from yld for bonds w emb option");

		return calcPriceFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblYield));
	}

	@Override public double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromYield");

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcZSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromYTM");

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception
				("Invalid inputs into BondComponent::calcExerciseZSpreadFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise z spread from yld for bonds w emb option");

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYield);
	}

	@Override public double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromYield");

		return calcOASFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcOASFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromYTM");

		return calcOASFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spread from yld for bonds w emb option");

		return calcOASFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromYield");

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcBondBasisFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromYTM");

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise bond basis from yld for bonds w emb option");

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYield);
	}

	@Override public double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcYieldSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcBondBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		return calcExerciseBondBasisFromYield (valParams, mktParams, quotingParams, dblYield);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYield");

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield), false);
	}

	@Override public double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYield");

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcCreditBasisFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYTM");

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseCreditBasisFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from yld for bonds w emb option");

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYield");

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield), true);
	}

	@Override public double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYield");

		return calcPECSFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcPECSFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYTM");

		return calcPECSFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExercisePECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePECSFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise PECS from Yield for bonds w emb option");

		return calcPECSFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) throw new java.lang.Exception ("Bad TsyBmkYield!");

		return dblYield - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromYield");

		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcTSYSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromYTM");

		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseTSYSpreadFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spread from yld for bonds w emb option");

		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null == mktParams.getTSYDiscountCurve()) return java.lang.Double.NaN;

		return dblYield - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromYield");

		return calcGSpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcGSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromYTM");

		return calcGSpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcExerciseGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spread from yld for bonds w emb option");

		return calcGSpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromYield");

		return dblYield - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromYield");

		return calcISpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcISpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromYTM");

		return calcISpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcExerciseISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spread from yld for bonds w emb option");

		return calcISpreadFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromYield");

		if (null != _fltParams)
			return dblYield - getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
				mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return dblYield - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromYield");

		return calcDiscountMarginFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcDiscountMarginFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromYTM");

		return calcDiscountMarginFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
	}

	@Override public double calcExerciseDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from yld for bonds w emb option");

		return calcDiscountMarginFromYield (valParams, mktParams, _periodParams._dblMaturity, 1., dblYield);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBasePrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYield);

		return (dblBasePrice - calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYield + 0.0001)) / dblBasePrice;
	}

	@Override public double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromYield");

		return calcDurationFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcDurationFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromYield");

		return calcDurationFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise duration from yld for bonds w emb option");

		return calcDurationFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
				java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
					java.lang.Double.isNaN (dblYield))
				throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromYield");

			if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
				throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
					(valParams._dblValue) + " greater than Work-out " +
						org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromYield");

		return calcYield01FromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcYield01FromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromYield");

		return calcYield01FromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise duration from yld for bonds w emb option");

		return calcYield01FromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYield);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield) * dblPrice;
	}

	@Override public double calcParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromYield");

		return calcParASWFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcParASWFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromYTM");

		return calcParASWFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromYield");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise par asw from yld for bonds w emb option");

		return calcParASWFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromYield");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBasePrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYield);

		return (calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYield + 0.0001) + calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield - 0.0001) - 2. * dblBasePrice) / dblBasePrice;
	}

	@Override public double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromYield");

		return calcConvexityFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcConvexityFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromYTM");

		return calcConvexityFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
	}

	@Override public double calcExerciseConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromYTM");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise convexity from yld for bonds w emb option");

		return calcConvexityFromYield (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYield);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromPrice");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcYTMFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYTMFromPrice");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
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

	@Override public double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromPrice");

		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);
		
		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcExerciseZSpreadFromPrice - cant calc Work-out info");

		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice);
	}

	@Override public double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromPrice");

		return calcOASFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);
		
		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcExerciseOASFromPrice - cant calc Work-out info");

		return calcOASFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice) - calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate,
					dblWorkoutFactor, 0.));
	}

	@Override public double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromPrice");

		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);
		
		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcExerciseBondBasisFromPrice - cant calc Work-out info");

		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice);
	}

	@Override public double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcBondBasisFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		return calcExerciseBondBasisFromPrice (valParams, mktParams, quotingParams, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromPrice");

		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseCreditBasisFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcExerciseCreditBasisFromPrice - cant calc Work-out");

		return calcCreditBasisFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromPrice");

		return calcPECSFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExercisePECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePECSFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("BondComponent::calcExercisePECSFromPrice - cant calc Work-out");

		return calcPECSFromPrice (valParams, mktParams, quotingParams, wi._dblDate, wi._dblExerciseFactor,
			dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams, dblWorkoutDate);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("No bmk set for " + _idParams._strISIN +
				"in ::calcTSYSpreadFromPrice");

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromPrice");

		return calcTSYSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcTSYSpreadFromPrice");

		return calcTSYSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice) - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromPrice");

		return calcGSpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseGSpreadFromPrice");

		return calcGSpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice) - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromPrice");

		return calcISpreadFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseISpreadFromPrice");

		return calcISpreadFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPrice) - getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPrice) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromPrice");

		return calcDiscountMarginFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPrice);
	}

	@Override public double calcExerciseDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseDiscountMarginFromPrice");

		return calcDiscountMarginFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBaseYield = calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);

		return (dblPrice - calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBaseYield + 0.0001)) / dblPrice;
	}

	@Override public double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromPrice");

		return calcDurationFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseDurationFromPrice");

		return calcDurationFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblPrice - calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice) + 0.0001);
	}

	@Override public double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromPrice");

		return calcDurationFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseYield01FromPrice");

		return calcYield01FromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve())
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromPrice");

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblPrice) * dblPrice;
	}

	@Override public double calcParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromPrice");

		return calcParASWFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseParASWFromPrice");

		return calcParASWFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
	}

	@Override public double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice) throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromPrice");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblYield = calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblPrice);

		return (calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYield + 0.0001) + calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblYield - 0.0001) - 2. * dblPrice) / dblPrice;
	}

	@Override public double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromPrice");

		return calcConvexityFromPrice (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPrice);
	}

	@Override public double calcExerciseConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromPrice");

		org.drip.param.valuation.WorkoutInfo wi = calcExerciseYieldFromPrice (valParams, mktParams,
			quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Can't do Work-out for " + _idParams._strISIN +
				" BondComponent::calcExerciseConvexityFromPrice");

		return calcConvexityFromPrice (valParams, mktParams, quotingParams, wi._dblDate,
			wi._dblExerciseFactor, dblPrice);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromZSpread");

		return calcPriceFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExercisePriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise price from z spd for bonds w emb option");

		return calcPriceFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread)) - calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
					dblWorkoutFactor, calcPriceFromZSpread (valParams, mktParams, quotingParams,
						dblWorkoutDate, dblWorkoutFactor, 0.));
	}

	@Override public double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromZSpread");

		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Bond Basis from z spd for bonds w emb option");

		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblZSpread);
	}

	@Override public double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromZSpread (valParams, mktParams, quotingParams, dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromZSpread");

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yld from z spd for bonds w emb option");

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblZSpread;
	}

	@Override public double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromZSpread");

		return calcOASFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise OAS from z spd for bonds w emb option");

		return calcOASFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), false);
	}

	@Override public double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromZSpread");

		return calcCreditBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
	}

	@Override public double calcExerciseCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yld from z spd for bonds w emb option");

		return calcCreditBasisFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), true);
	}

	@Override public double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromZSpread");

		return calcPECSFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExercisePECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from Z Spread for bonds with embedded option");

		return calcPECSFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) throw new java.lang.Exception ("Bad bmk yield!");

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromZSpread");

		return calcTSYSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise tsy spd from z spd for bonds w emb option");

		return calcTSYSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread) - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromZSpread");

		return calcGSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spd from z spd for bonds w emb option");

		return calcGSpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread) - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromZSpread");

		return calcISpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spd from z spd for bonds w emb option");

		return calcISpreadFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread) - getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromZSpread");

		return calcDiscountMarginFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
	}

	@Override public double calcExerciseDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception
				("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from z spd for bonds w emb option");

		return calcDiscountMarginFromZSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblZSpread);

		return (dblPrice - calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblZSpread + 0.0001)) / dblPrice;
	}

	@Override public double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromZSpread");

		return calcDurationFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise dur from z spd for bonds w emb option");

		return calcDurationFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread) - calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread + 0.0001);
	}

	@Override public double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromZSpread");

		return calcYield01FromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise Yield01 from z spd for bonds w emb option");

		return calcYield01FromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dbPrice = calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) - dbPrice)
			/ calcDurationFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblZSpread) * dbPrice;
	}

	@Override public double calcParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromZSpread");

		return calcParASWFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise par ASW from z spd for bonds w emb option");

		return calcParASWFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromZSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblZSpread);

		return (calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblZSpread + 0.0001) + calcPriceFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblZSpread - 0.0001) - 2. * dblPrice) / dblPrice;
	}

	@Override public double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromZSpread");

		return calcConvexityFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
	}

	@Override public double calcExerciseConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblZSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromZSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from z spd for bonds w emb option");

		return calcConvexityFromZSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblZSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblOAS);
	}

	@Override public double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromOAS");

		return calcPriceFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExercisePriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise price from OAS for bonds w emb option");

		return calcPriceFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromOAS (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblOAS));
	}

	@Override public double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromOAS");

		return calcZSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Z Spread from OAS for bonds with embedded option");

		return calcZSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor, dblOAS))
				- calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
					calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
						0.));
	}

	@Override public double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromOAS");

		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Bond Basis from OAS for bonds w emb option");

		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS);
	}

	@Override public double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcBondBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		return calcExerciseBondBasisFromOAS (valParams, mktParams, quotingParams, dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblOAS));
	}

	@Override public double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromOAS");

		return calcYieldFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yld from OAS for bonds w emb option");

		return calcYieldFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), false);
	}

	@Override public double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromOAS");

		return calcCreditBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yld from OAS for bonds w emb option");

		return calcCreditBasisFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), true);
	}

	@Override public double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromOAS");

		return calcPECSFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
	}

	@Override public double calcExercisePECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise Yield from OAS for bonds with embedded option");

		return calcPECSFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1., dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) throw new java.lang.Exception ("Bad bmk yield!");

		return calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromOAS");

		return calcTSYSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise tsy spd from OAS for bonds w emb option");

		return calcTSYSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS) - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromOAS");

		return calcGSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spd from OAS for bonds w emb option");

		return calcGSpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS) - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromOAS");

		return calcISpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise i spd from Option Adjusted spd for bonds w emb option");

		return calcISpreadFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS) - getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromOAS");

		return calcDiscountMarginFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
	}

	@Override public double calcExerciseDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from Option Adjusted spd for bonds w emb option");

		return calcDiscountMarginFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblOAS);

		return (dblPrice - calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblOAS + 0.0001)) / dblPrice;
	}

	@Override public double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromOAS");

		return calcDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise dur from z spd for bonds w emb option");

		return calcDurationFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS) - calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS + 0.0001);
	}

	@Override public double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromOAS");

		return calcYield01FromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise Yield01 from z spd for bonds w emb option");

		return calcYield01FromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dbPrice = calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) - dbPrice)
			/ calcDurationFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblOAS) * dbPrice;
	}

	@Override public double calcParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromOAS");

		return calcParASWFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise par ASW from Option Adjusted spd for bonds w emb option");

		return calcParASWFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromOAS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblOAS);

		return (calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblOAS + 0.0001) + calcPriceFromOAS (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblOAS - 0.0001) - 2. * dblPrice) / dblPrice;
	}

	@Override public double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromOAS");

		return calcConvexityFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
	}

	@Override public double calcExerciseConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblOAS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromOAS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from Option Adjusted spd for bonds w emb option");

		return calcConvexityFromOAS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblOAS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.)) +
					dblBondBasis);
	}

	@Override public double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromBondBasis");

		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExercisePriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise price from Bond Basis for bonds w emb option");

		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblYield = calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis);

		return new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromYield (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, dblYield));
	}

	@Override public double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromBondBasis");

		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise z spread from Bond Basis for bonds w emb option");

		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblWorkoutDate) ||
			java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis);
	}

	@Override public double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromBondBasis");

		return calcOASFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spread from Bond Basis for bonds w emb option");

		return calcOASFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromBondBasis");

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise yld from Bond Basis for bonds w emb option");

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (java.lang.Double.isNaN (dblBondBasis)) throw new java.lang.Exception ("Bond Basis is NaN!");

		return dblBondBasis;
	}

	@Override public double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblBondBasis)) throw new java.lang.Exception ("Bond Basis is NaN!");

		return dblBondBasis;
	}

	@Override public double calcExerciseYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblBondBasis)) throw new java.lang.Exception ("Bond Basis is NaN!");

		return dblBondBasis;
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), false);
	}

	@Override public double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromBondBasis");

		return calcCreditBasisFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcExerciseCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from Bond Basis for bonds w emb option");

		return calcCreditBasisFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateCreditBasisFromPrice (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				0., false), true);
	}

	@Override public double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromBondBasis");

		return calcPECSFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExercisePECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from Bond Basis for bonds with embedded option");

		return calcPECSFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) throw new java.lang.Exception ("Bad bmk yield!");

		return calcYieldFromZSpread (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblBondBasis) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromBondBasis");

		return calcTSYSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcExerciseTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spd from Bond Basis for bonds w emb option");

		return calcTSYSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblBondBasis) - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromBondBasis");

		return calcGSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise G Spread from Bond Basis for bonds w emb option");

		return calcGSpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblBondBasis) - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromBondBasis");

		return calcISpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise I Spread from Bond Basis for bonds w emb option");

		return calcISpreadFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis) || null == mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis) - getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblBondBasis) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromBondBasis");

		return calcDiscountMarginFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
	}

	@Override public double calcExerciseDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception
				("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from Bond Basis for bonds w emb option");

		return calcDiscountMarginFromBondBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis);

		return (dblPrice - calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis + 0.0001)) / dblPrice;
	}

	@Override public double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromBondBasis");

		return calcDurationFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcExerciseDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Duration from Bond Basis for bonds w emb option");

		return calcDurationFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblBondBasis) - calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis + 0.0001);
	}

	@Override public double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromBondBasis");

		return calcYield01FromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcExerciseYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Yield01 from Bond Basis for bonds w emb option");

		return calcYield01FromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dbPrice = calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) - dbPrice)
			/ calcDurationFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblBondBasis) * dbPrice;
	}

	@Override public double calcParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromBondBasis");

		return calcParASWFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
	}

	@Override public double calcExerciseParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Par ASW from Bond Basis for bonds w emb option");

		return calcParASWFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblBondBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromBondBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis);

		return (calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblBondBasis + 0.0001) + calcPriceFromBondBasis (valParams, mktParams,
				quotingParams, dblWorkoutDate, dblWorkoutFactor, dblBondBasis - 0.0001) - 2. * dblPrice) /
					dblPrice;
	}

	@Override public double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromBondBasis");

		return calcConvexityFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
	}

	@Override public double calcExerciseConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblBondBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromBondBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Convexity from Bond Basis for bonds w emb option");

		return calcConvexityFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblBondBasis);
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
		return calcPriceFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYieldSpread);
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

	@Override public double calcExercisePriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise price from Yield Spread for bonds w emb option");

		return calcPriceFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
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
		return calcZSpreadFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromYieldSpread");

		return calcZSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise z spread from Yield Spread for bonds w emb option");

		return calcZSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcOASFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYieldSpread);
	}

	@Override public double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromYieldSpread");

		return calcOASFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcExerciseOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spread from Yield Spread for bonds w emb option");

		return calcOASFromBondBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
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
		return calcYieldFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYieldSpread);
	}

	@Override public double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromYieldSpread");

		return calcYieldFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcExerciseYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise yld from Yield Spread for bonds w emb option");

		return calcYieldFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
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
		return dblYieldSpread;
	}

	@Override public double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromYieldSpread");

		return calcBondBasisFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise Bond Basis from Yield Spread for bonds w emb option");

		return calcBondBasisFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcCreditBasisFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYieldSpread");

		return calcCreditBasisFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcExerciseCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws	java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from Yield Spread for bonds w emb option");

		return calcCreditBasisFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
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
		return calcPECSFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblYieldSpread);
	}

	@Override public double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYieldSpread");

		return calcPECSFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
	}

	@Override public double calcExercisePECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from Yield Spread for bonds with embedded option");

		return calcPECSFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblYieldSpread);
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
		return calcTSYSpreadFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromYieldSpread");

		return calcTSYSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spd from Yield Spread for bonds w emb option");

		return calcTSYSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcGSpreadFromYieldSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromYieldSpread");

		return calcGSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise G Spread from Yield Spread for bonds w emb option");

		return calcGSpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcISpreadFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromYieldSpread");

		return calcISpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise I Spread from Yield Spread for bonds w emb option");

		return calcISpreadFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcDiscountMarginFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromYieldSpread");

		return calcDiscountMarginFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
	}

	@Override public double calcExerciseDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getTSYDiscountCurve() ||
			java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception
				("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from Yield Spread for bonds w emb option");

		return calcDiscountMarginFromYieldSpread (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblYieldSpread);
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
		return calcDurationFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromYieldSpread");

		return calcDurationFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Duration from Yield Spread for bonds w emb option");

		return calcDurationFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcYield01FromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromYieldSpread");

		return calcYield01FromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Yield01 from Yield Spread for bonds w emb option");

		return calcYield01FromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		return calcParASWFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromYieldSpread");

		return calcParASWFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Par ASW from Yield Spread for bonds w emb option");

		return calcParASWFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		return calcConvexityFromBondBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblYieldSpread);
	}

	@Override public double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromYieldSpread");

		return calcConvexityFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
	}

	@Override public double calcExerciseConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblYieldSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromYieldSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc Exercise Convexity from Yield Spread for bonds w emb option");

		return calcConvexityFromYieldSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblYieldSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

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
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromCreditBasis");

		return calcPriceFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExercisePriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise price from CreditBasis for bonds w emb option");

		return calcPriceFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblCreditBasis,
				false));
	}

	@Override public double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromCreditBasis");

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise yld from CreditBasis for bonds w emb option");

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis, false));
	}

	@Override public double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromCreditBasis");

		return calcZSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcExerciseZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Z Spread from Credit Basis for bonds with embedded option");

		return calcZSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		/* return new BondCalibrator (this, false).calibDiscCurveSpreadFromPriceNR (valParams, mktParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis)); */

		return calcZSpreadFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis);
	}

	@Override public double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromCreditBasis");

		return calcOASFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise OAS from Credit Basis for bonds with embedded option");

		return calcOASFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis) - calcYieldFromPrice (valParams, mktParams, quotingParams,
				dblWorkoutDate, dblWorkoutFactor, calcPriceFromBumpedDC (valParams, mktParams,
					dblWorkoutDate, dblWorkoutFactor, 0.));
	}

	@Override public double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromCreditBasis");

		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcExerciseBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Bond Basis from Credit Basis for bonds with embedded option");

		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
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
		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis);
	}

	@Override public double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcBondBasisFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcExerciseYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		return calcExerciseBondBasisFromCreditBasis (valParams, mktParams, quotingParams, dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("cant determine bmk for calcTSYSpreadFromCreditBasis");

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromCreditBasis");

		return calcTSYSpreadFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcExerciseTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseTSYSpreadFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spd from CreditBasis for bonds w emb option");

		return calcTSYSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis) || null ==
					mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis) - mktParams.getTSYDiscountCurve().calcImpliedRate
				(dblWorkoutDate);
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

	@Override public double calcExerciseGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise g spd from CreditBasis for bonds w emb option");

		return calcGSpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis) - mktParams.getDiscountCurve().calcImpliedRate
				(dblWorkoutDate);
	}

	@Override public double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromCreditBasis");

		return calcISpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
	}

	@Override public double calcExerciseISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise i spd from CreditBasis for bonds w emb option");

		return calcISpreadFromCreditBasis (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis) - getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromCreditBasis (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblCreditBasis) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. /
				iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromCreditBasis");

		return calcDiscountMarginFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcExerciseDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception
				("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from CreditBasis for bonds w emb option");

		return calcDiscountMarginFromCreditBasis (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblCreditBasis);
	}

	@Override public double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblCreditBasis, false);

		if (java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Cannot calculate Price from Credit Basis");

		return calcPECSFromPrice (valParams, mktParams, null, dblWorkoutDate, dblWorkoutFactor, dblPrice);
	}

	@Override public double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPECSFromCreditBasis");

		return calcPECSFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExercisePECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePECSFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise PECS from Credit Basis for Bonds with embedded option");

		return calcPECSFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblCreditBasis, false);

		return (dblPrice - calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblCreditBasis + 0.0001, false)) / dblPrice;
	}

	@Override public double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromCreditBasis");

		return calcDurationFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise dur from cred basis for bonds w emb option");

		return calcDurationFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblCreditBasis,
			false) - calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis + 0.0001, false);
	}

	@Override public double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromCreditBasis");

		return calcYield01FromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from cred basis for bonds w emb option");

		return calcYield01FromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblCreditBasis, false);

		return (calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0., false) -
			dblPrice) / calcDurationFromCreditBasis (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				dblCreditBasis) * dblPrice;
	}

	@Override public double calcParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromCreditBasis");

		return calcParASWFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise par ASW from CreditBasis for bonds w emb option");

		return calcParASWFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromCreditBasis");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblCreditBasis, false);

		return (calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.0001 +
			dblCreditBasis, false) + calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblCreditBasis - 0.0001, false) - 2. * dblPrice) / dblPrice;
	}

	@Override public double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromCreditBasis");

		return calcConvexityFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
	}

	@Override public double calcExerciseConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblCreditBasis))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromCreditBasis");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from CreditBasis for bonds w emb option");

		return calcConvexityFromCreditBasis (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblCreditBasis);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblPECS, true);
	}

	@Override public double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcPriceFromPECS");

		return calcPriceFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExercisePriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Price from PECS for bonds with embedded option");

		return calcPriceFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYieldFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblPECS, true));
	}

	@Override public double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExercisePriceFromPECS");

		return calcYieldFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Yield from PECS for bonds with embedded option");

		return calcYieldFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibZeroCurveSpreadFromPrice (valParams, mktParams, quotingParams,
			dblWorkoutDate, dblWorkoutFactor, calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblPECS, true));
	}

	@Override public double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcZSpreadFromPECS");

		return calcZSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseZSpreadFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Z Spread from PECS for bonds with embedded option");

		return calcZSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS);
	}

	@Override public double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcOASFromPECS");

		return calcOASFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseOASFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise OAS from PECS for bonds with embedded option");

		return calcOASFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS) - calcYieldFromPrice (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate,
					dblWorkoutFactor, 0.));
	}

	@Override public double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcBondBasisFromPECS");

		return calcBondBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseBondBasisFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Bond Basis from PECS for bonds with embedded option");

		return calcBondBasisFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		return calcBondBasisFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS);
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

	@Override public double calcExerciseYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYieldSpreadFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Yield Spread from PECS for bonds with embedded option");

		return calcYieldSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Cannot determine benchmark for calcTSYSpreadFromPECS");

		return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromPECS");

		return calcTSYSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseTSYSpreadFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise TSY Spread from PECS for bonds with embedded option");

		return calcTSYSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS) || null ==
					mktParams.getTSYDiscountCurve())
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcGSpreadFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS) - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
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

	@Override public double calcExerciseGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseGSpreadFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise G Spread from PECS for bonds with embedded option");

		return calcGSpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS) - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcISpreadFromPECS");

		return calcISpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
	}

	@Override public double calcExerciseISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseISpreadFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise I Spread from PECS for bonds with embedded option");

		return calcISpreadFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS) - getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcYieldFromPECS (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS) - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDiscountMarginFromPECS");

		return calcDiscountMarginFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcExerciseDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDiscountMarginFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Discount Margin from PECS for bonds with embedded option");

		return calcDiscountMarginFromPECS (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblPECS);
	}

	@Override public double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS, true);

		if (java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("Cannot calculate Price from PECS");

		return calcCreditBasisFromPrice (valParams, mktParams, null, dblWorkoutDate, dblWorkoutFactor,
			dblPrice);
	}

	@Override public double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcCreditBasisFromPECS");

		return calcCreditBasisFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcExerciseCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseCreditBasisFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Credit Basis from PECS for Bonds with embedded option");

		return calcCreditBasisFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS, true);

		return (dblPrice - calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS + 0.0001, true)) / dblPrice;
	}

	@Override public double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcDurationFromPECS");

		return calcDurationFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcExerciseDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseDurationFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Duration from PECS for Bonds with embedded option");

		return calcDurationFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblPECS, true)
			- calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblPECS +
				0.0001, true);
	}

	@Override public double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcYield01FromPECS");

		return calcYield01FromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcExerciseYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseYield01FromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Yield01 from PECS for Bonds with embedded option");

		return calcYield01FromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS, true);

		return (calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0., false) -
			dblPrice) / calcDurationFromPECS (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS) * dblPrice;
	}

	@Override public double calcParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcParASWFromPECS");

		return calcParASWFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcExerciseParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseParASWFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Par ASW from PECS for Bonds with embedded option");

		return calcParASWFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN
				(dblWorkoutFactor) || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromPECS");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblPECS, true);

		return (calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.0001 +
			dblPECS, true) + calcPriceFromBumpedCC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				dblPECS - 0.0001, true) - 2. * dblPrice) / dblPrice;
	}

	@Override public double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcConvexityFromPECS");

		return calcConvexityFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
	}

	@Override public double calcExerciseConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getCreditCurve() || java.lang.Double.isNaN (dblPECS))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcExerciseConvexityFromPECS");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate Exercise Convexity from PECS for Bonds with embedded option");

		return calcConvexityFromPECS (valParams, mktParams, _periodParams._dblMaturity, 1., dblPECS);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Invalid tsy bmk for " + _idParams._strISIN +
				"in ::calcPriceFromTSYSpread");

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromTSYSpread");

		return calcPriceFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExercisePriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExercisePriceFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise px from tsy spd for bonds w emb option");

		return calcPriceFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcYieldFromTSYSpread");

		return dblTSYSpread + dblBmkYield;
	}

	@Override public double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromTSYSpread");

		return calcYieldFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcExerciseYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYieldFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yield from tsy spd for bonds w emb option");

		return calcYieldFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcZSpreadFromTSYSpread");

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromTSYSpread");

		return calcZSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExerciseZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise z spd from tsy spd for bonds w emb option");

		return calcZSpreadFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN + "in calcOASFromTSYSpread");

		return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromTSYSpread");

		return calcOASFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExerciseOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spd from tsy spd for bonds w emb option");

		return calcOASFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcBondBasisFromTSYSpread");

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromTSYSpread");

		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcExerciseBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Bond Basis from tsy spd for bonds w emb option");

		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
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
		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblTSYSpread);
	}

	@Override public double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcExerciseYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromTSYSpread (valParams, mktParams, quotingParams, dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcCreditBasisFromTSYSpread");

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblTSYSpread + dblBmkYield);
	}

	@Override public double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromTSYSpread");

		return calcCreditBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcExerciseCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseCreditBasisFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from tsy spd for bonds w emb option");

		return calcCreditBasisFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				" in calcPECSFromTSYSpread");

		return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromTSYSpread");

		return calcPECSFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExercisePECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcExercisePECSFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from TSY Spread for bonds with embedded option");

		return calcPECSFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromTSYSpread");

		if (null == mktParams.getTSYDiscountCurve()) return java.lang.Double.NaN;

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcGSpreadFromTSYSpread");

		return dblTSYSpread + dblBmkYield - mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromTSYSpread");

		return calcGSpreadFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcExerciseGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseGSpreadFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spd from tsy spd for bonds w emb option");

		return calcGSpreadFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcISpreadFromTSYSpread");

		return dblTSYSpread + dblBmkYield - mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromTSYSpread");

		return calcISpreadFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcExerciseISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spd from tsy spd for bonds w emb option");

		return calcISpreadFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblTSYSpread);
	}

	@Override public double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcDiscountMarginFromTSYSpread");

		if (null != _fltParams)
			return calcDiscountMarginFromYield (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
				dblTSYSpread + dblBmkYield);

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return dblTSYSpread + dblBmkYield - mktParams.getDiscountCurve().calcImpliedRate (((int) (12. /
			iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromTSYSpread");

		return calcDiscountMarginFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExerciseDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from tsy spd for bonds w emb option");

		return calcDiscountMarginFromTSYSpread (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) return java.lang.Double.NaN;

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcDurationFromTSYSpread");

		return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromTSYSpread");

		return calcDurationFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcExerciseDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDurationFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from tsy spd for bonds w emb option");

		return calcDurationFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield)) return java.lang.Double.NaN;

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				" in calcYield01FromTSYSpread");

		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromTSYSpread");

		return calcYield01FromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExerciseYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYield01FromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from tsy spd for bonds w emb option");

		return calcYield01FromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcParASWFromTSYSpread");

		double dblPrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblTSYSpread + dblBmkYield);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblTSYSpread + dblBmkYield) * dblPrice;
	}

	@Override public double calcParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromTSYSpread");

		return calcParASWFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
	}

	@Override public double calcExerciseParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise par ASW from tsy spd for bonds w emb option");

		return calcParASWFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromTSYSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Got NaN for " + _idParams._strISIN +
				"in calcConvexityFromTSYSpread");

		return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblTSYSpread + dblBmkYield);
	}

	@Override public double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromTSYSpread");

		return calcConvexityFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
	}

	@Override public double calcExerciseConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblTSYSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseConvexityFromTSYSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from tsy spd for bonds w emb option");

		return calcConvexityFromTSYSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblTSYSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromGSpread");

		return calcPriceFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExercisePriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExercisePriceFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise px from g spd for bonds w emb option");

		return calcPriceFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromGSpread");

		return calcYieldFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcExerciseYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yield from g spd for bonds w emb option");

		return calcYieldFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromGSpread");

		return calcZSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseZSpreadFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise z spd from g spd for bonds w emb option");

		return calcZSpreadFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromGSpread");

		return calcOASFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseOASFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spd from g spd for bonds w emb option");

		return calcOASFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromGSpread");

		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseBondBasisFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Bond Basis from G Spread for bonds w emb option");

		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblGSpread);
	}

	@Override public double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromGSpread (valParams, mktParams, quotingParams, dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN
					(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromGSpread");

		return calcCreditBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
	}

	@Override public double calcExerciseCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseCreditBasisFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from g spd for bonds w emb option");

		return calcCreditBasisFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN
					(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcPECSFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcPECSFromGSpread");

		return calcPECSFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExercisePECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcExercisePECSFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from G Spread for bonds with embedded option");

		return calcPECSFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || null == mktParams.getCreditCurve() || java.lang.Double.isNaN
				(dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN
					(dblGSpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Cant determine Work-out in BondComponent::calcTSYSpreadFromGSpread");

		return calcYieldFromGSpread (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblGSpread) -
			dblBmkYield;
	}

	@Override public double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromGSpread");

		return calcTSYSpreadFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcExerciseTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise tsy spd from g spd for bonds w emb option");

		return calcTSYSpreadFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate) -
			mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromGSpread");

		return calcISpreadFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcExerciseISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spd from g spd for bonds w emb option");

		return calcISpreadFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblGSpread);
	}

	@Override public double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate) -
				getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(), mktParams.getFixings(),
					calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate) -
			mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromGSpread");

		return calcDiscountMarginFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from G Spread for bonds w emb option");

		return calcDiscountMarginFromGSpread (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromGSpread");

		return calcDurationFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDurationFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from g spread for bonds w emb option");

		return calcDurationFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromGSpread");

		return calcYield01FromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYield01FromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from g spread for bonds w emb option");

		return calcYield01FromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblGSpread);

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromGSpread (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblGSpread) * dblPrice;
	}

	@Override public double calcParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromGSpread");

		return calcParASWFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseParASWFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spd from g spd for bonds w emb option");

		return calcParASWFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblWorkoutDate) ||
				java.lang.Double.isNaN (dblWorkoutFactor) || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromGSpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblGSpread + mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromGSpread");

		return calcConvexityFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblGSpread);
	}

	@Override public double calcExerciseConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() || null ==
			mktParams.getTSYDiscountCurve() || java.lang.Double.isNaN (dblGSpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromGSpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from g spread for bonds w emb option");

		return calcConvexityFromGSpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromISpread");

		return calcPriceFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExercisePriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExercisePriceFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise px from i spd for bonds w emb option");

		return calcPriceFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromISpread");

		return calcYieldFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcExerciseYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYieldFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yield from i spd for bonds w emb option");

		return calcYieldFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromISpread");

		return calcZSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseZSpreadFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise z spd from i spd for bonds w emb option");

		return calcZSpreadFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromISpread");

		return calcOASFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseOASFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spd from i spd for bonds w emb option");

		return calcOASFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromISpread");

		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseBondBasisFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise BondBasis from i spd for bonds w emb option");

		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblISpread);
	}

	@Override public double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcBondBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromISpread (valParams, mktParams, quotingParams, dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception {
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromISpread");

		return calcCreditBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
	}

	@Override public double calcExerciseCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from i spd for bonds w emb option");

		return calcCreditBasisFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcPECSFromISpread");

		return calcPECSFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExercisePECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from I Spread for bonds with embedded option");

		return calcPECSFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception ("Cant determine Work-out in BondComponent::calcTSYSpreadFromISpread");

		return calcYieldFromISpread (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, dblISpread) -
			dblBmkYield;
	}

	@Override public double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromISpread");

		return calcTSYSpreadFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcExerciseTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise tsy spd from i spd for bonds w emb option");

		return calcTSYSpreadFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate) -
			mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);
	}

	@Override public double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromISpread");

		return calcGSpreadFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
	}

	@Override public double calcExerciseGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception
		{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spd from i spd for bonds w emb option");

		return calcGSpreadFromISpread (valParams, mktParams, _periodParams._dblMaturity, 1., dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromISpread");

		return calcDurationFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDurationFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from i spread for bonds w emb option");

		return calcDurationFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromISpread");

		return calcYield01FromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYield01FromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from i spread for bonds w emb option");

		return calcYield01FromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblPrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate))
					* dblPrice;
	}

	@Override public double calcParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromISpread");

		return calcParASWFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from i spread for bonds w emb option");

		return calcParASWFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromISpread");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblISpread + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate));
	}

	@Override public double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromISpread");

		return calcConvexityFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
	}

	@Override public double calcExerciseConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN
			(dblISpread))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseConvexityFromISpread");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from i spread for bonds w emb option");

		return calcConvexityFromISpread (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblISpread);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromDiscountMargin");

		return calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcExercisePriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExercisePriceFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise px from Discount Margin for bonds w emb option");

		return calcPriceFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return dblDiscountMargin + getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
				mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) +
			"M");
	}

	@Override public double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromDiscountMargin");

		return calcYieldFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
	}

	@Override public double calcExerciseYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYieldFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise yield from Discount Margin for bonds w emb option");

		return calcYieldFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. /
				iFreq)) + "M"));
	}

	@Override public double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromDiscountMargin");

		return calcZSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseZSpreadFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise z spd from Discount Margin for bonds w emb option");

		return calcZSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblDiscountMargin + getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromDiscountMargin");

		return calcOASFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcExerciseOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseOASFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spd from Discount Margin for bonds w emb option");

		return calcOASFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN
			(dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromDiscountMargin");

		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseBondBasisFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise BondBasis from Discount Margin for bonds w emb option");

		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
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
		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblDiscountMargin);
	}

	@Override public double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcBondBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromDiscountMargin (valParams, mktParams, quotingParams,
			dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. /
				iFreq)) + "M"));
	}

	@Override public double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromDiscountMargin");

		return calcCreditBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from Discount Margin for bonds w emb option");

		return calcCreditBasisFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcPECSFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
				dblDiscountMargin + getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
					mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				" ::calcPECSFromDiscountMargin");

		return calcPECSFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcExercisePECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from Discount Margin for bonds with embedded option");

		return calcPECSFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid inputs into BondComponent::calcTSYSpreadFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		double dblBmkYield = getTsyBmkYield (valParams, mktParams);

		if (java.lang.Double.isNaN (dblBmkYield))
			throw new java.lang.Exception
				("Cant determine Work-out in BondComponent::calcTSYSpreadFromDiscountMargin");

		return calcYieldFromDiscountMargin (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin) - dblBmkYield;
	}

	@Override public double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromDiscountMargin");

		return calcTSYSpreadFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
	}

	@Override public double calcExerciseTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spd from Discount Margin for bonds w emb option");

		return calcTSYSpreadFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
	}

	@Override public double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return dblDiscountMargin + getIndexRate (valParams._dblValue, mktParams.getDiscountCurve(),
				mktParams.getFixings(), calcCurrentPeriod (valParams._dblValue)) -
					mktParams.getTSYDiscountCurve().calcImpliedRate (dblWorkoutDate);

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (dblWorkoutDate) -
			mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M");
	}

	@Override public double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromDiscountMargin");

		return calcGSpreadFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
	}

	@Override public double calcExerciseGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise g spd from Discount Margin for bonds w emb option");

		return calcGSpreadFromDiscountMargin (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = _periodParams._iFreq;

		return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromDiscountMargin");

		return calcDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDurationFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from Discount Margin for bonds w emb option");

		return calcDurationFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = _periodParams._iFreq;

		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromDiscountMargin");

		return calcYield01FromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYield01FromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from Discount Margin for bonds w emb option");

		return calcYield01FromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams) {
			double dblFullFirstCoupon = dblDiscountMargin + getIndexRate (valParams._dblValue,
				mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
					(valParams._dblValue));

			double dblPrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblFullFirstCoupon);

			return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
				dblPrice) / calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
					dblWorkoutFactor, dblFullFirstCoupon) * dblPrice;
		}

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		double dblPrice = calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. /
				iFreq)) + "M"));

		return (calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, 0.) -
			dblPrice) / calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate
					(dblWorkoutDate)) * dblPrice;
	}

	@Override public double calcParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromDiscountMargin");

		return calcParASWFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
	}

	@Override public double calcExerciseParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcParASWFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from Discount Margin for bonds w emb option");

		return calcParASWFromDiscountMargin (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblDiscountMargin);
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
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromDiscountMargin");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		if (null != _fltParams)
			return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
				dblWorkoutFactor, dblDiscountMargin + getIndexRate (valParams._dblValue,
					mktParams.getDiscountCurve(), mktParams.getFixings(), calcCurrentPeriod
						(valParams._dblValue)));

		int iFreq = _periodParams._iFreq;

		if (0 == iFreq) iFreq = 2;

		return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			dblDiscountMargin + mktParams.getDiscountCurve().calcImpliedRate (((int) (12. / iFreq)) + "M"));
	}

	@Override public double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromDiscountMargin");

		return calcConvexityFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcExerciseConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblDiscountMargin))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseConvexityFromDiscountMargin");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from Discount Margin for bonds w emb option");

		return calcConvexityFromDiscountMargin (valParams, mktParams, quotingParams,
			_periodParams._dblMaturity, 1., dblDiscountMargin);
	}

	@Override public double calcPriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPriceFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcPriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception {
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromParASW");

		return calcPriceFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExercisePriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPriceFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise px from par asw for bonds w emb option");

		return calcPriceFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
			dblWorkoutFactor, dblParASW);
	}

	@Override public double calcYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYieldFromParASW");

		return calcYieldFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcExerciseYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYieldFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise yield from asw spd for bonds w emb option");

		return calcYieldFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcZSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromParASW");

		return calcZSpreadFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcZSpreadFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise z spd from par asw for bonds w emb option");

		return calcZSpreadFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcOASFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromParASW");

		return calcOASFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcOASFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Option Adjusted spd from par asw for bonds w emb option");

		return calcOASFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcBondBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcBondBasisFromParASW");

		return calcBondBasisFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseBondBasisFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Bond Basis from par asw for bonds w emb option");

		return calcBondBasisFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		return calcBondBasisFromParASW (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, dblParASW);
	}

	@Override public double calcYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		return calcBondBasisFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		return calcExerciseYieldSpreadFromParASW (valParams, mktParams, quotingParams, dblParASW);
	}

	@Override public double calcCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcCreditBasisFromYield (valParams, mktParams, quotingParams, dblWorkoutDate,
			dblWorkoutFactor, new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams,
				dblWorkoutDate, dblWorkoutFactor, dblParASW));
	}

	@Override public double calcCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcCreditBasisFromParASW");

		return calcCreditBasisFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblParASW);
	}

	@Override public double calcExerciseCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseCreditBasisFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise credit basis from par asw for bonds w emb option");

		return calcCreditBasisFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity,
			1., dblParASW);
	}

	@Override public double calcPECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcPECSFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcPECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcPECSFromParASW");

		return calcPECSFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExercisePECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExercisePECSFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calculate exercise PECS from Par ASW for bonds with embedded option");

		return calcPECSFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcTSYSpreadFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcTSYSpreadFromParASW");

		return calcTSYSpreadFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseTSYSpreadFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise tsy spd from par asw for bonds w emb option");

		return calcTSYSpreadFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcGSpreadFromYield (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcGSpreadFromParASW");

		return calcGSpreadFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcExerciseGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseGSpreadFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise g spd from par asw for bonds w emb option");

		return calcGSpreadFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcISpreadFromYield (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcISpreadFromParASW");

		return calcISpreadFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcExerciseISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseISpreadFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception ("Cannot calc exercise i spd from par asw for bonds w emb option");

		return calcISpreadFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1., dblParASW);
	}

	@Override public double calcDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcDiscountMarginFromYield (valParams, mktParams, dblWorkoutDate, dblWorkoutFactor, new
			BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDiscountMarginFromParASW");

		return calcDiscountMarginFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDiscountMarginFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Discount Margin from par asw for bonds w emb option");

		return calcDiscountMarginFromParASW (valParams, mktParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcDurationFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcDurationFromParASW");

		return calcDurationFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseDurationFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise duration from par asw for bonds w emb option");

		return calcDurationFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcYield01FromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcYield01FromParASW");

		return calcYield01FromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseYield01FromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise Yield01 from par asw for bonds w emb option");

		return calcYield01FromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblWorkoutDate) || java.lang.Double.isNaN (dblWorkoutFactor) ||
				java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromParASW");

		if (valParams._dblValue >= dblWorkoutDate + LEFT_EOS_SNIP)
			throw new java.lang.Exception ("Val date " + org.drip.analytics.date.JulianDate.fromJulian
				(valParams._dblValue) + " greater than Work-out " +
					org.drip.analytics.date.JulianDate.fromJulian (dblWorkoutDate));

		return calcConvexityFromYield (valParams, mktParams, quotingParams, dblWorkoutDate, dblWorkoutFactor,
			new BondCalibrator (this).calibrateYieldFromParASW (valParams, mktParams, dblWorkoutDate,
				dblWorkoutFactor, dblParASW));
	}

	@Override public double calcConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcConvexityFromParASW");

		return calcConvexityFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
	}

	@Override public double calcExerciseConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception
	{
		if (null == valParams || null == mktParams || null == mktParams.getDiscountCurve() ||
			java.lang.Double.isNaN (dblParASW))
			throw new java.lang.Exception ("Invalid params into " + _idParams._strISIN +
				"::calcExerciseConvexityFromParASW");

		if (null != _eosCall || null != _eosPut)
			throw new java.lang.Exception
				("Cannot calc exercise convexity from par asw for bonds w emb option");

		return calcConvexityFromParASW (valParams, mktParams, quotingParams, _periodParams._dblMaturity, 1.,
			dblParASW);
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
				dblStdGSpread = wi._dblYield - mktParams.getTSYDiscountCurve().calcImpliedRate (wi._dblDate);
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
				dblStdZSpread, dblStdGSpread, wi._dblYield - mktParams.getDiscountCurve().calcImpliedRate
					(wi._dblDate), dblStdOASpread, dblStdTSYSpread, dblStdDiscountMargin,
						dblStdAssetSwapSpread, dblStdCreditBasis, dblStdPECS, dblStdDuration,
							dblStdConvexity, wi);

			/* return new org.drip.analytics.output.BondRVMeasures (dblPrice, wi._dblYield - dblStdYield,
				dblStdZSpread, dblStdGSpread, wi._dblYield - mktParams.getDiscountCurve().calcImpliedRate
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
					calcYieldFromISpread (valParams, mktParams, dblISpread), 1.,
						org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY);
			} catch (java.lang.Exception e) {
				if (!s_bSuppressErrors) e.printStackTrace();

				wiMarket = null;
			}
		} else if (null != mktParams.getComponentQuote().getQuote ("ParASW")) {
			try {
				double dblParASW = mktParams.getComponentQuote().getQuote ("ParASW").getQuote ("mid");

				mapMeasures.put ("MarketInputType=ParASW", dblParASW);

				wiMarket = new org.drip.param.valuation.WorkoutInfo (getMaturityDate().getJulian(),
					calcYieldFromParASW (valParams, mktParams, dblParASW), 1.,
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

	@Override public org.drip.math.algodiff.WengertJacobian calcPVDFMicroJack (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		return null;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcQuoteDFMicroJack (
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
			org.drip.math.algodiff.ObjectiveFunction ofYieldToPrice = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
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

			org.drip.math.algodiff.ObjectiveFunction ofZSpreadToPrice = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
				public double evaluate (
					final double dblZSpread)
					throws java.lang.Exception
				{
					return _bond.calcPriceFromBumpedDC (valParams, mktParams, dblWorkoutDate,
						dblWorkoutFactor, dblZSpread) - dblPrice;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofZSpreadToPrice).findRoot();

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
			org.drip.math.algodiff.ObjectiveFunction ofZSpreadToPrice = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
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

			org.drip.math.algodiff.ObjectiveFunction ofZSpreadToPrice = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
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
			org.drip.math.algodiff.ObjectiveFunction ofCreditBasisToPrice = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
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

		/**
		 * Calibrates the bond Yield from the market Par ASW
		 * 
		 * @param valParams Valuation Parameters
		 * @param mktParams Bond Market Parameters
		 * @param dblWorkoutDate JulianDate Work-out
		 * @param dblWorkoutFactor Work-out factor
		 * @param dblParASWCalib Price to be calibrated to
		 * 
		 * @return The calibrated Yield
		 * 
		 * @throws java.lang.Exception Thrown if the Yield cannot be calibrated
		 */

		public double calibrateYieldFromParASW (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final double dblWorkoutDate,
			final double dblWorkoutFactor,
			final double dblParASWCalib)
			throws java.lang.Exception
		{
			org.drip.math.algodiff.ObjectiveFunction ofYieldToParASW = new
				org.drip.math.algodiff.ObjectiveFunction (null) {
				public double evaluate (
					final double dblYield)
					throws java.lang.Exception
				{
					return _bond.calcParASWFromYield (valParams, mktParams, null, dblWorkoutDate,
						dblWorkoutFactor, dblYield) - dblParASWCalib;
				}
			};

			org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent (0.,
				ofYieldToParASW).findRoot();

			if (null == rfop || !rfop.containsRoot())
				throw new java.lang.Exception ("BondComponent.calibrateYieldFromParASW => Cannot get root!");

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
