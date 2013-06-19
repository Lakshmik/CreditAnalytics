
package org.drip.product.definition;

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
 * This base abstract class implements the pricing, the valuation, and the RV analytics functionality for the
 * 		bond product.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class Bond extends CreditComponent {

	/**
	 * Retrieves the work-out information from price
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Bond Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price
	 * 
	 * @return The Optimal Work-out Information
	 */

	public abstract org.drip.param.valuation.WorkoutInfo calcExerciseYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice);

	/**
	 * Retrieves the array of double for the bond's secondary treasury spreads from the Valuation
	 * 	Parameters and the component market parameters
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams ComponentMarketParams
	 * 
	 * @return Array of double for the bond's secondary treasury spreads
	 */

	public abstract double[] getSecTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams);

	/**
	 * Retrieves the effective treasury benchmark yield from the valuation, the component market parameters,
	 * 	and the market price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams ComponentMarketParams
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Market price
	 * 
	 * @return Effective treasury benchmark yield
	 * 
	 * @throws java.lang.Exception Thrown if the effective benchmark cannot be calculated
	 */

	public abstract double getEffectiveTsyBmkYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Gets the ISIN
	 * 
	 * @return ISIN string
	 */

	public abstract java.lang.String getISIN();

	/**
	 * Gets the CUSIP
	 * 
	 * @return CUSIP string
	 */

	public abstract java.lang.String getCUSIP();

	/**
	 * Gets the bond's loss flow from price
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mktParams ComponentMarketParams
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Input price
	 * 
	 * @return List of ProductLossPeriodCurveMeasures
	 */

	public abstract java.util.List<org.drip.analytics.period.LossPeriodCurveFactors>
		getLossFlowFromPrice (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.param.pricer.PricerParams pricerParams,
			final org.drip.param.definition.ComponentMarketParams mktParams,
			final org.drip.param.valuation.QuotingParams quotingParams,
			final double dblPrice);

	/**
	 * Returns whether the bond is a floater
	 * 
	 * @return True if the bond is a floater
	 */

	public abstract boolean isFloater();

	/**
	 * Returns the rate index of the bond
	 * 
	 * @return Rate index
	 */

	public abstract java.lang.String getRateIndex();

	/**
	 * Returns the current bond coupon
	 * 
	 * @return Current coupon
	 */

	public abstract double getCurrentCoupon();

	/**
	 * Returns the floating spread of the bond
	 * 
	 * @return Floating spread
	 */

	public abstract double getFloatSpread();

	/**
	 * Returns the bond ticker
	 * 
	 * @return Bond Ticker
	 */

	public abstract java.lang.String getTicker();

	/**
	 * Indicates if the bond is callable
	 * 
	 * @return True - callable
	 */

	public abstract boolean isCallable();

	/**
	 * Indicates if the bond is putable
	 * 
	 * @return True - putable
	 */

	public abstract boolean isPutable();

	/**
	 * Indicates if the bond is sinkable
	 * 
	 * @return True - sinkable
	 */

	public abstract boolean isSinkable();

	/**
	 * Indicates if the bond has variable coupon
	 * 
	 * @return True - has variable coupon
	 */

	public abstract boolean hasVariableCoupon();

	/**
	 * Indicates if the bond has been exercised
	 * 
	 * @return True - Has been exercised
	 */

	public abstract boolean hasBeenExercised();

	/**
	 * Indicates if the bond has defaulted
	 * 
	 * @return True - Bond has defaulted
	 */

	public abstract boolean hasDefaulted();

	/**
	 * Indicates if the bond is perpetual
	 * 
	 * @return True - Bond is Perpetual
	 */

	public abstract boolean isPerpetual();

	/**
	 * Calculates if the bond is tradeable on the given date
	 * 
	 * @param valParams Valuation Parameters
	 * 
	 * @return True indicates the bond is tradeable
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public abstract boolean isTradeable (
		final org.drip.param.valuation.ValuationParams valParams)
		throws java.lang.Exception;

	/**
	 * Return the bond's embedded call schedule
	 * 
	 * @return EOS Call
	 */

	public abstract org.drip.product.params.EmbeddedOptionSchedule getEmbeddedCallSchedule();

	/**
	 * Return the bond's embedded put schedule
	 * 
	 * @return EOS Put
	 */

	public abstract org.drip.product.params.EmbeddedOptionSchedule getEmbeddedPutSchedule();

	/**
	 * Return the bond's coupon type
	 * 
	 * @return Bond's coupon Type
	 */

	public abstract java.lang.String getCouponType();

	/**
	 * Return the bond's coupon day count
	 * 
	 * @return Coupon day count string
	 */

	public abstract java.lang.String getCouponDC();

	/**
	 * Return the bond's accrual day count
	 * 
	 * @return Accrual day count string
	 */

	public abstract java.lang.String getAccrualDC();

	/**
	 * Return the bond's maturity type
	 * 
	 * @return Bond's maturity type
	 */

	public abstract java.lang.String getMaturityType();

	/**
	 * Return the bond's coupon frequency
	 * 
	 * @return Bond's coupon frequency
	 */

	public abstract int getCouponFreq();

	/**
	 * Return the bond's final maturity
	 * 
	 * @return Bond's final maturity
	 */

	public abstract org.drip.analytics.date.JulianDate getFinalMaturity();

	/**
	 * Return the bond's calculation type
	 * 
	 * @return Bond's calculation type
	 */

	public abstract java.lang.String getCalculationType();

	/**
	 * Return the bond's redemption value
	 * 
	 * @return Bond's redemption value
	 */

	public abstract double getRedemptionValue();

	/**
	 * Return the bond's coupon currency
	 * 
	 * @return Bond's coupon currency
	 */

	public abstract java.lang.String getCouponCurrency();

	/**
	 * Return the bond's redemption currency
	 * 
	 * @return Bond's redemption currency
	 */

	public abstract java.lang.String getRedemptionCurrency();

	/**
	 * Indicates whether the given date is in the first coupon period
	 * 
	 * @param dblDate Valuation Date
	 * 
	 * @return True => The given date is in the first coupon period
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public abstract boolean inFirstCouponPeriod (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Indicates whether the given date is in the final coupon period
	 * 
	 * @param dblDate Valuation Date
	 * 
	 * @return True => The given date is in the last coupon period
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public abstract boolean inLastCouponPeriod (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Return the bond's trade currency
	 * 
	 * @return Bond's trade currency
	 */

	public abstract java.lang.String getTradeCurrency();

	/**
	 * Return the bond's floating coupon convention
	 * 
	 * @return Bond's floating coupon convention
	 */

	public abstract java.lang.String getFloatCouponConvention();

	/**
	 * Get the bond's reset date for the period identified by the valuation date
	 * 
	 * @param dblValue Valuation Date
	 * 
	 * @return Reset JulianDate
	 */

	public abstract org.drip.analytics.date.JulianDate getPeriodResetDate (
		final double dblValue);

	/**
	 * Returns the coupon date for the period prior to the specified date
	 * 
	 * @param dt Valuation Date
	 * 
	 * @return Previous Coupon Date
	 */

	public abstract org.drip.analytics.date.JulianDate calcPreviousCouponDate (
		final org.drip.analytics.date.JulianDate dt);

	/**
	 * Returns the coupon rate for the period prior to the specified date
	 * 
	 * @param dt Valuation Date
	 * @param mktParams Component Market Params
	 * 
	 * @return Previous Coupon Rate
	 * 
	 * @throws java.lang.Exception Thrown if the previous coupon rate cannot be calculated
	 */

	public abstract double calcPreviousCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception;

	/**
	 * Returns the coupon date for the period containing the specified date
	 * 
	 * @param dt Valuation Date
	 * 
	 * @return Current Coupon Date
	 */

	public abstract org.drip.analytics.date.JulianDate calcCurrentCouponDate (
		final org.drip.analytics.date.JulianDate dt);

	/**
	 * Returns the coupon date for the period subsequent to the specified date
	 * 
	 * @param dt Valuation Date
	 * 
	 * @return Next Coupon Date
	 */

	public abstract org.drip.analytics.date.JulianDate calcNextCouponDate (
		final org.drip.analytics.date.JulianDate dt);

	/**
	 * Returns the next exercise info of the given exercise type (call/put) subsequent to the specified date
	 * 
	 * @param dt Valuation Date
	 * @param bGetPut TRUE => Gets the next put date
	 * 
	 * @return Next Exercise Information
	 */

	public abstract org.drip.analytics.output.ExerciseInfo calcNextValidExerciseDateOfType (
		final org.drip.analytics.date.JulianDate dt,
		final boolean bGetPut);

	/**
	 * Returns the next exercise info subsequent to the specified date
	 * 
	 * @param dt Valuation Date
	 * 
	 * @return Next Exercise Info
	 */

	public abstract org.drip.analytics.output.ExerciseInfo calcNextValidExerciseInfo (
		final org.drip.analytics.date.JulianDate dt);

	/**
	 * Returns the coupon rate for the period corresponding to the specified date
	 * 
	 * @param dt Valuation Date
	 * @param mktParams Component Market Params
	 * 
	 * @return Next Coupon Rate
	 * 
	 * @throws java.lang.Exception Thrown if the current period coupon rate cannot be calculated
	 */

	public abstract double calcCurrentCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception;

	/**
	 * Returns the coupon rate for the period subsequent to the specified date
	 * 
	 * @param dt Valuation Date
	 * @param mktParams Component Market Params
	 * 
	 * @return Next Coupon Rate
	 * 
	 * @throws java.lang.Exception Thrown if the subsequent coupon rate cannot be calculated
	 */

	public abstract double calcNextCouponRate (
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception;

	/**
	 * Calculate the bond's accrued for the period identified by the valuation date
	 * 
	 * @param dblDate Valuation Date
	 * @param mktParams Bond market parameters
	 * 
	 * @return The coupon accrued in the current period
	 * 
	 * @throws java.lang.Exception Thrown if accrual cannot be calculated
	 */

	public abstract double calcAccrued (
		final double dblDate,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception;

	/**
	 * Calculate the bond's non-credit risky theoretical price from the bumped zero curve
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams ComponentMarketParams
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Double Work-out date
	 * @param dblWorkoutFactor Double Work-out factor
	 * @param dblZCBump Bump to be applied to the zero curve
	 * 
	 * @return Bond's non-credit risky theoretical price
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromBumpedZC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZCBump)
		throws java.lang.Exception;

	/**
	 * Calculate the bond's non-credit risky theoretical price from the bumped discount curve
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams ComponentMarketParams
	 * @param dblWorkoutDate Double Work-out date
	 * @param dblWorkoutFactor Double Work-out factor
	 * @param dblDCBump Bump to be applied to the DC
	 * 
	 * @return Bond's non-credit risky theoretical price
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromBumpedDC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDCBump)
		throws java.lang.Exception;

	/**
	 * Calculate the bond's credit risky theoretical price from the bumped credit curve
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams ComponentMarketParams
	 * @param dblWorkoutDate Double Work-out date
	 * @param dblWorkoutFactor Double Work-out factor
	 * @param dblCreditBasis Bump to be applied to the credit curve
	 * @param bFlat Is the CDS Curve flat (for PECS)
	 * 
	 * @return Bond's credit risky theoretical price
	 * 
	 * @throws java.lang.Exception Thrown if the bond's credit risky theoretical price cannot be calculated
	 */

	public abstract double calcPriceFromBumpedCC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis,
		final boolean bFlat)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return ASW from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return ASW from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return ASW from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return ASW from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return ASW from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return ASW from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return ASW from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return ASW from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return ASW from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return ASW from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return ASW from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return ASW from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return ASW from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return ASW from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return ASW from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return ASW from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return ASW from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return ASW from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return ASW from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return ASW from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return ASW from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return ASW from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return ASW from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return ASW from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return ASW from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return ASW from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return ASW from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return ASW from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return ASW from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return ASW from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return ASW from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return ASW from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return ASW from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return ASW from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the ASW cannot be calculated
	 */

	public abstract double calcASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return ASW from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate ASW from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return ASW from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if ASW cannot be calculated
	 */

	public abstract double calcASWFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Bond Basis from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Bond Basis from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Bond Basis from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Bond Basis from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Bond Basis from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Bond Basis from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Bond Basis from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Bond Basis from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Bond Basis from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Bond Basis from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Bond Basis from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Bond Basis from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Bond Basis from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Bond Basis from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Bond Basis from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Bond Basis from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Bond Basis from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Bond Basis from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Bond Basis from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Bond Basis from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Bond Basis from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Bond Basis from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Bond Basis from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Bond Basis from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Bond Basis from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Bond Basis from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Bond Basis from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Bond Basis from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Bond Basis from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Bond Basis from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Bond Basis from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Bond Basis from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Bond Basis from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Bond Basis from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Bond Basis from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Bond Basis from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Bond Basis from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Convexity from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Convexity from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Convexity from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Convexity from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Convexity from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Convexity from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Convexity from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Convexity from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Convexity from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Convexity from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Convexity from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Convexity from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Convexity from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Convexity from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Convexity from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Convexity from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Convexity from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Convexity from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Convexity from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Convexity from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Convexity from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Convexity from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Convexity from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Convexity from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Convexity from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Convexity from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Convexity from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Convexity from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Convexity from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Convexity from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Convexity from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Convexity from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Convexity from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Convexity from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Convexity from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Convexity from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Convexity from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Convexity from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Convexity from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Convexity from Z to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Credit Basis from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Credit Basis from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Credit Basis from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Credit Basis from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Credit Basis from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Credit Basis from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Credit Basis from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Credit Basis from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Credit Basis from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcCreditBasisFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Credit Basis from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Credit Basis from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Credit Basis from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Credit Basis from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Credit Basis from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Credit Basis from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Credit Basis from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Credit Basis from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Credit Basis from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Credit Basis from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Credit Basis from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Credit Basis from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Credit Basis from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Credit Basis from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Credit Basis from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Credit Basis from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Credit Basis from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Credit Basis from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Credit Basis from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Credit Basis from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Credit Basis from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Credit Basis from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Credit Basis from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Credit Basis from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Credit Basis from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Credit Basis from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Credit Basis from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Credit Basis from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Discount Margin from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Discount Margin from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Discount Margin from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Discount Margin from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Discount Margin from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Discount Margin from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Discount Margin from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Discount Margin from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Discount Margin from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Discount Margin from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Discount Margin from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Discount Margin from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Discount Margin from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Discount Margin from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Discount Margin from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Discount Margin from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Discount Margin from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Discount Margin from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Discount Margin from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Discount Margin from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Discount Margin from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Discount Margin from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Discount Margin from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Discount Margin from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Discount Margin from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Discount Margin from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Discount Margin from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Discount Margin from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Discount Margin from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Discount Margin from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Discount Margin from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Discount Margin from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Discount Margin from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Discount Margin from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Discount Margin from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Discount Margin from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Discount Margin from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Duration from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Duration from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Duration from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Duration from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Duration from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Duration from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Duration from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Duration from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Duration from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Duration from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Duration from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Duration from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Duration from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Duration from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Duration from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Duration from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Duration from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Duration from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Duration from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Duration from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Duration from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Duration from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Duration from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Duration from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Duration from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Duration from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Duration from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Duration from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Duration from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Duration from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Duration from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Duration from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Duration from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Duration from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Duration from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Duration from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Duration from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Duration from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Duration from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Duration from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Duration cannot be calculated
	 */

	public abstract double calcDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return G Spread from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return G Spread from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return G Spread from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return G Spread from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return G Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return G Spread from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return G Spread from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return G Spread from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return G Spread from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return G Spread from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return G Spread from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return G Spread from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return G Spread from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return G Spread from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return G Spread from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return G Spread from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return G Spread from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return G Spread from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return G Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return G Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return G Spread from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return G Spread from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return G Spread from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return G Spread from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return G Spread from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return G Spread from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return G Spread from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return G Spread from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return G Spread from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return G Spread from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return G Spread from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return G Spread from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return G Spread from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return G Spread from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return G Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate G Spread from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return G Spread from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return I Spread from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return I Spread from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return I Spread from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return I Spread from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return I Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return I Spread from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return I Spread from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return I Spread from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return I Spread from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return I Spread from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return I Spread from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return I Spread from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return I Spread from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return I Spread from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return I Spread from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return I Spread from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return I Spread from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return I Spread from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return I Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return I Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return I Spread from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return I Spread from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return I Spread from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return I Spread from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return I Spread from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return I Spread from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return I Spread from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return I Spread from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return I Spread from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return I Spread from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return I Spread from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return I Spread from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return I Spread from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return I Spread from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return I Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate I Spread from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return I Spread from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Macaulay Duration from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Macaulay Duration from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Macaulay Duration from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Macaulay Duration from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Macaulay Duration from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Macaulay Duration from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Macaulay Duration from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Macaulay Duration from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Macaulay Duration from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Macaulay Duration from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Macaulay Duration from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Macaulay Duration from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Macaulay Duration from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Macaulay Duration from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Macaulay Duration from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Macaulay Duration from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Macaulay Duration from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Macaulay Duration from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Macaulay Duration from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Macaulay Duration from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Macaulay Duration from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Macaulay Duration from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Macaulay Duration from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Macaulay Duration from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Macaulay Duration from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Macaulay Duration from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Macaulay Duration from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Macaulay Duration from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Macaulay Duration from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Macaulay Duration from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Macaulay Duration from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Macaulay Duration from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Macaulay Duration from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Macaulay Duration from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Macaulay Duration cannot be calculated
	 */

	public abstract double calcMacaulayDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Modified Duration from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Modified Duration from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Modified Duration from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Modified Duration from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Modified Duration from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Modified Duration from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Modified Duration from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Modified Duration from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Modified Duration from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Modified Duration from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Modified Duration from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Modified Duration from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Modified Duration from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Modified Duration from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Modified Duration from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Modified Duration from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Modified Duration from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return Modified Duration from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Modified Duration from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Modified Duration from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Modified Duration from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Modified Duration from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Modified Duration from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Modified Duration from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Modified Duration from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Modified Duration from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Modified Duration from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Modified Duration from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Modified Duration from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Modified Duration from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Modified Duration from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Modified Duration from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Modified Duration from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Modified Duration from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Modified Duration from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Modified Duration from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Modified Duration from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Modified Duration from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Modified Duration from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Modified Duration from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Modified Duration cannot be calculated
	 */

	public abstract double calcModifiedDurationFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return OAS from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return OAS from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return OAS from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return OAS from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return OAS from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return OAS from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return OAS from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return OAS from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return OAS from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return OAS from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return OAS from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return OAS from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return OAS from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return OAS from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return OAS from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return OAS from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return OAS from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return OAS from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return OAS from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return OAS from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return OAS from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return OAS from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return OAS from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return OAS from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return OAS from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return OAS from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return OAS from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return OAS from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return OAS from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return OAS from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return OAS from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return OAS from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return OAS from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return OAS from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the OAS cannot be calculated
	 */

	public abstract double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return OAS from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate OAS from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return OAS from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if OAS cannot be calculated
	 */

	public abstract double calcOASFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return PECS from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return PECS from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return PECS from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return PECS from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return PECS from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return PECS from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return PECS from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return PECS from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return PECS from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return PECS from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return PECS from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return PECS from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return PECS from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return PECS from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return PECS from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return PECS from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return PECS from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return PECS from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return PECS from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return PECS from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return PECS from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return PECS from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return PECS from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return PECS from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return PECS from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return PECS from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return PECS from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return PECS from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return PECS from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return PECS from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return PECS from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return PECS from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return PECS from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return PECS from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return PECS from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate PECS from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return PECS from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if PECS cannot be calculated
	 */

	public abstract double calcPECSFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Price from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Price from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Price from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Price from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Price from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Price from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Price from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Price from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Price from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Price from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Price from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Price from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Price from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Price from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Price from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Price from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Price from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Price from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Price from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Price from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return Price from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Price from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Price from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Price from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Price from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Price from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Price from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Price from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Price from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Price from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Price from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Price from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Price from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Price from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Price from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Price from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Price from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Price from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Price from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Price from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Price from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Price cannot be calculated
	 */

	public abstract double calcPriceFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return TSY Spread from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return TSY Spread from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return TSY Spread from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return TSY Spread from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return TSY Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return TSY Spread from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return TSY Spread from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return TSY Spread from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return TSY Spread from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return TSY Spread from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return TSY Spread from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return TSY Spread from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return TSY Spread from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return TSY Spread from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Optimal Exercise
	 * 
	 * @return TSY Spread from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return TSY Spread from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return TSY Spread from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return TSY Spread from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return TSY Spread from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return TSY Spread from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return TSY Spread from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return TSY Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return TSY Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return TSY Spread from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return TSY Spread from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return TSY Spread from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return TSY Spread from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return TSY Spread from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return TSY Spread from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return TSY Spread from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return TSY Spread from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return TSY Spread from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return TSY Spread from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return TSY Spread from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return TSY Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate TSY Spread from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return TSY Spread from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if TSY Spread cannot be calculated
	 */

	public abstract double calcTSYSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Yield from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Yield from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Yield from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Yield from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Yield from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Yield from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Yield from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Yield from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Yield from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Yield from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Yield from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Yield from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Yield from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Yield from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Yield from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Yield from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Yield from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return Yield from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Yield from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Yield from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Yield from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Yield from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Yield from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Yield from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Yield from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Yield from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Yield from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Yield from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Yield from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Yield from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Yield from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Yield from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Yield from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Yield from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Yield from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Yield from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield cannot be calculated
	 */

	public abstract double calcYieldFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Yield01 from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Yield01 from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Yield01 from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Yield01 from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Yield01 from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Yield01 from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Yield01 from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Yield01 from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Yield01 from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Yield01 from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Yield01 from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Yield01 from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Yield01 from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Yield01 from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Yield01 from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Yield01 from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Yield01 from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return Yield01 from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Yield01 from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Yield01 from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Yield01 from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Yield01 from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Yield01 from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Yield01 from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Yield01 from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Yield01 from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Yield01 from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Yield01 from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Yield01 from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Yield01 from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Yield01 from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Yield01 from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Yield01 from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Yield01 from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Yield01 from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Yield01 from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Yield01 from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Yield01 from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield01 from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Yield01 from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Yield Spread from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Yield Spread from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Yield Spread from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Yield Spread from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Yield Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Yield Spread from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Yield Spread from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Yield Spread from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Yield Spread from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Yield Spread from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Yield Spread from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Yield Spread from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Yield Spread from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Yield Spread from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Yield Spread from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Yield Spread from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Yield Spread from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return Yield Spread from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Yield Spread from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Yield Spread from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Yield Spread from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Yield Spread from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Yield Spread from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Yield Spread from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Yield Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Yield Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Yield Spread from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Yield Spread from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Yield Spread from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Yield Spread from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Yield Spread from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Yield Spread from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Yield Spread from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Z Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblZSpread Z Spread to Work-out
	 * 
	 * @return Yield Spread from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Z Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Maturity
	 * 
	 * @return Yield Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Yield Spread from Z Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblZSpread Z Spread to Optimal Exercise
	 * 
	 * @return Yield Spread from Z Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromZSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from ASW to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblASW ASW to Work-out
	 * 
	 * @return Z Spread from ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from ASW to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Maturity
	 * 
	 * @return Z Spread from ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from ASW to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblASW ASW to Optimal Exercise
	 * 
	 * @return Z Spread from ASW to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromASWToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblASW)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Bond Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblBondBasis Bond Basis to Work-out
	 * 
	 * @return Z Spread from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Bond Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Maturity
	 * 
	 * @return Z Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Bond Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblBondBasis Bond Basis to Optimal Exercise
	 * 
	 * @return Z Spread from Bond Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromBondBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Credit Basis to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblCreditBasis Credit Basis to Work-out
	 * 
	 * @return Z Spread from Credit Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Credit Basis to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Maturity
	 * 
	 * @return Z Spread from Credit Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Credit Basis to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblCreditBasis Credit Basis to Optimal Exercise
	 * 
	 * @return Z Spread from Credit Basis to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromCreditBasisToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Discount Margin to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblDiscountMargin Discount Margin to Work-out
	 * 
	 * @return Z Spread from Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Discount Margin to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Maturity
	 * 
	 * @return Z Spread from Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Discount Margin to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblDiscountMargin Discount Margin to Optimal Exercise
	 * 
	 * @return Z Spread from Discount Margin to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromDiscountMarginToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from G Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblGSpread G Spread to Work-out
	 * 
	 * @return Z Spread from G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from G Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Maturity
	 * 
	 * @return Z Spread from G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from G Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblGSpread G Spread to Optimal Exercise
	 * 
	 * @return Z Spread from G Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromGSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from I Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblISpread I Spread to Work-out
	 * 
	 * @return Z Spread from I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from I Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread I Spread to Maturity
	 * 
	 * @return Z Spread from I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from I Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblISpread ISpread to Optimal Exercise
	 * 
	 * @return Z Spread from I Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromISpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from OAS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblOAS OAS to Work-out
	 * 
	 * @return Z Spread from OAS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from OAS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Maturity
	 * 
	 * @return Z Spread from OAS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from OAS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblOAS OAS to Optimal Exercise
	 * 
	 * @return Z Spread from OAS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromOASToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Price to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPrice Price to Work-out
	 * 
	 * @return Z Spread from Price to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Price to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Maturity
	 * 
	 * @return Z Spread from Price to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Price to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPrice Price to Optimal Exercise
	 * 
	 * @return Z Spread from Price to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPriceToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from PECS to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblPECS PECS to Work-out
	 * 
	 * @return Z Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from PECS to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Maturity
	 * 
	 * @return Z Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from PECS to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblPECS PECS to Optimal Exercise
	 * 
	 * @return Z Spread from PECS to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPECSToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from TSY Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblTSYSpread TSY Spread to Work-out
	 * 
	 * @return Z Spread from TSY Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from TSY Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Maturity
	 * 
	 * @return Z Spread from TSY Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from TSY Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblTSYSpread TSY Spread to Optimal Exercise
	 * 
	 * @return Z Spread from TSY Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromTSYSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYield Yield to Work-out
	 * 
	 * @return Z Spread from Yield to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Maturity
	 * 
	 * @return Z Spread from Yield to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYield Yield to Optimal Exercise
	 * 
	 * @return Z Spread from Yield to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYieldToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield Spread to Work-out
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblWorkoutDate Work-out Date
	 * @param dblWorkoutFactor Work-out Factor
	 * @param dblYieldSpread Yield Spread to Work-out
	 * 
	 * @return Z Spread from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield Spread to Maturity
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Maturity
	 * 
	 * @return Z Spread from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate Z Spread from Yield Spread to Optimal Exercise
	 * 
	 * @param valParams Valuation Parameters
	 * @param mktParams Market Parameters
	 * @param quotingParams Quoting Parameters
	 * @param dblYieldSpread Yield Spread to Optimal Exercise
	 * 
	 * @return Z Spread from Yield Spread to Optimal Exercise
	 * 
	 * @throws java.lang.Exception Thrown if Yield Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYieldSpreadToOptimalExercise (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the full set of Bond RV Measures from the Price Input
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams Pricing Parameters
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param wi Work out Information
	 * @param dblPrice Input Price
	 * 
	 * @return Bond RV Measure Set
	 */

	public abstract org.drip.analytics.output.BondRVMeasures standardMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.valuation.WorkoutInfo wi,
		final double dblPrice);

	/**
	 * Displays all the coupon periods onto stdout
	 * 
	 * @throws java.lang.Exception Thrown if the coupon periods cannot be displayed onto stdout
	 */

	public abstract void showPeriods()
		throws java.lang.Exception;
}
