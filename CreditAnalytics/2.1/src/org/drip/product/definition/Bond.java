
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
	 * Calculate the bond price from yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated price from yield to work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated price from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated price from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from exercise yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Exercise Yield input
	 * 
	 * @return Calculated price from exercise yield
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated Z Spread from yield to work-out
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
	 * Calculate the bond Z Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Z Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Z Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated Z Spread from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from yield to work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated Bond Basis from yield to work-out
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
	 * Calculate the bond Basis from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Bond Basis from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Basis from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Bond Basis from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Basis from yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated Bond Basis from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated Yield Spread from yield to work-out
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
	 * Calculate the Yield Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Yield Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Yield Spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated Yield Spread from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated Credit Basis from yield to work-out
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
	 * Calculate the bond credit basis from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Credit Basis from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Credit Basis from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated Credit Basis from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Yield to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Yield to work-out input
	 * 
	 * @return Calculated PECS from yield to work-out
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
	 * Calculate the Bond PECS from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated PECS from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated PECS from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated PECS from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond spread to treasury from Work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond spread to treasury from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond spread to treasury from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from exercise Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond spread to treasury from exercise yield
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond G spread from Work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the G spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond G spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond G spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond G spread from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond I spread from work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the I spread cannot be calculated
	 */

	public abstract double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond I spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I spread cannot be calculated
	 */

	public abstract double calcISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond I spread from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I spread cannot be calculated
	 */

	public abstract double calcISpreadFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond I spread from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond Discount Margin from work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond Discount Margin from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond Discount Margin from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond Discount Margin from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond duration from Work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the bond duration from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond duration from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond duration from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond duration from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond Yield01 from Work-out yield
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
	 * Calculate the bond Yield01 from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond Yield01 from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond Yield01 from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond Yield01 from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond par ASW from Work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond par ASW from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond par ASW from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond par ASW from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Work-out Yield
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYield Work-out Yield input
	 * 
	 * @return Calculated bond convexity from Work-out yield
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond convexity from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Yield to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to maturity input
	 * 
	 * @return Calculated bond convexity from yield to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYTM (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Yield to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYield Yield to exercise input
	 * 
	 * @return Calculated bond convexity from yield to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromYield (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYield)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond yield to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the yield to Work-out cannot be calculated
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
	 * Calculate the bond yield to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond yield to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the yield to maturity cannot be calculated
	 */

	public abstract double calcYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond yield to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the yield to maturity cannot be calculated
	 */

	public abstract double calcYTMFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond yield to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the yield to exercise cannot be calculated
	 */

	public abstract org.drip.param.valuation.WorkoutInfo calcExerciseYieldFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice);

	/**
	 * Calculate the bond z spread to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond z spread to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to Work-out cannot be calculated
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
	 * Calculate the bond z spread to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond z spread to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to maturity cannot be calculated
	 */

	public abstract double calcZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond z spread to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond z spread to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted spread to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond z spread to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted spread to Work-out cannot be calculated
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
	 * Calculate the bond Option Adjusted spread to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Option Adjusted spread to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted spread to maturity cannot be calculated
	 */

	public abstract double calcOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted spread to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Option Adjusted spread to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseOASFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond Basis to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis to Work-out cannot be calculated
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
	 * Calculate the Bond Basis to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond Basis to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis to maturity cannot be calculated
	 */

	public abstract double calcBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond Basis to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis to exercise cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Yield Spread to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread to Work-out cannot be calculated
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
	 * Calculate the Yield Spread to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Yield Spread to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread to maturity cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Yield Spread to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond credit basis to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis to Work-out cannot be calculated
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
	 * Calculate the bond credit basis to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond credit basis to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis to maturity cannot be calculated
	 */

	public abstract double calcCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond credit basis to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis to exercise cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond PECS to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the PECS to Work-out cannot be calculated
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
	 * Calculate the Bond PECS to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond PECS to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the PECS to maturity cannot be calculated
	 */

	public abstract double calcPECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated Bond PECS to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the PECS to exercise cannot be calculated
	 */

	public abstract double calcExercisePECSFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond spread to treasury to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury to Work-out cannot be calculated
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
	 * Calculate the bond spread to treasury to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond spread to treasury to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury to maturity cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond spread to treasury to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury to exercise cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond G spread to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the G spread to Work-out cannot be calculated
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
	 * Calculate the bond G spread to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond G spread to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the G spread to maturity cannot be calculated
	 */

	public abstract double calcGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G spread to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond G spread to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the G spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond I spread to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the I spread to Work-out cannot be calculated
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
	 * Calculate the bond I spread to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond I spread to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the I spread to maturity cannot be calculated
	 */

	public abstract double calcISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I spread to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond I spread to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the I spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Discount Margin to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin to Work-out cannot be calculated
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
	 * Calculate the bond Discount Margin to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Discount Margin to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin to maturity cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Discount Margin to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin to exercise cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond duration to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the duration to Work-out cannot be calculated
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
	 * Calculate the bond duration to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond duration to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the duration to maturity cannot be calculated
	 */

	public abstract double calcDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond duration to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the duration to exercise cannot be calculated
	 */

	public abstract double calcExerciseDurationFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield01 to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Yield01 to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 to Work-out cannot be calculated
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
	 * Calculate the Bond Yield01 to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Yield01 to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the duration to Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond Yield01 to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 to exercise cannot be calculated
	 */

	public abstract double calcExerciseYield01FromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond par ASW to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW to Work-out cannot be calculated
	 */

	public abstract double calcParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond par ASW to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW to maturity cannot be calculated
	 */

	public abstract double calcParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond par ASW to exercise from price
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW to exercise cannot be calculated
	 */

	public abstract double calcExerciseParASWFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity to Work-out from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond convexity to Work-out from price
	 * 
	 * @throws java.lang.Exception Thrown if the convexity to Work-out cannot be calculated
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
	 * Calculate the bond convexity to maturity from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond convexity to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the convexity to maturity cannot be calculated
	 */

	public abstract double calcConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity to exercise from price
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPrice Price input
	 * 
	 * @return Calculated bond convexity to maturity from price
	 * 
	 * @throws java.lang.Exception Thrown if the convexity to exercise cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromPrice (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPrice)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated bond price from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated bond price from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated bond price from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Bond Basis from Z Spread to Work-out
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
	 * Calculate the Bond Basis from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated Bond Basis from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated Bond Basis from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Yield Spread from Z Spread to Work-out
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
	 * Calculate the Yield Spread from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated Yield Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated Yield Spread from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated bond yield from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
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
	 * Calculate the bond yield from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated bond yield from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated bond yield from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Option Adjusted Spread from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Bond OAS from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond OAS from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated bond OAS from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond OAS from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated bond OAS from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExerciseOASFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated bond credit basis from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
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
	 * Calculate the bond credit basis from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated bond credit basis from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated bond credit basis from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread) throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Bond PECS from Z Spread to Work-out
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
	 * Calculate the Bond PECS from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated Bond PECS from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated Bond PECS from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated spread to treasury from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
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
	 * Calculate the bond spread to treasury from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated spread to treasury from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated spread to treasury from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated G Spread from Z Spread to Work-out
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
	 * Calculate the bond G Spread from Z Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Maturity input
	 * 
	 * @return Calculated G Spread from Z Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Z Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to Exercise input
	 * 
	 * @return Calculated G Spread from Z Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated I Spread from Z Spread to Work-out
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
	 * Calculate the bond I Spread from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated I Spread from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated I Spread from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Discount Margin from Z Spread to Work-out
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
	 * Calculate the bond Discount Margin from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated Discount Margin from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated Discount Margin from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated duration from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the bond duration from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated duration from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated duration from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated Yield01 from Z Spread to Work-out
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
	 * Calculate the bond Yield01 from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated Yield01 from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated Yield01 from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated par ASW from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated par ASW from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated par ASW from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Z Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblZSpread Z Spread to Work-out input
	 * 
	 * @return Calculated convexity from Z Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from Z Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to maturity input
	 * 
	 * @return Calculated convexity from Z Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Z Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblZSpread Z Spread to exercise input
	 * 
	 * @return Calculated convexity from Z Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromZSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated bond price from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from Option Adjusted Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Maturity input
	 * 
	 * @return Calculated bond price from Option Adjusted Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Option Adjusted Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Exercise input
	 * 
	 * @return Calculated bond price from Option Adjusted Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond z spread to Work-out from OAS
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Bond OAS
	 * 
	 * @return Calculated bond z spread to Work-out from OAS
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to Work-out cannot be calculated
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
	 * Calculate the bond z spread to maturity from OAS
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Bond OAS input
	 * 
	 * @return Calculated bond z spread to maturity from OAS
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to maturity cannot be calculated
	 */

	public abstract double calcZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond z spread to exercise from OAS
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Bond OAS Input
	 * 
	 * @return Calculated bond z spread to exercise from OAS
	 * 
	 * @throws java.lang.Exception Thrown if the z spread to exercise cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated Bond Basis from Option Adjusted Spread to Work-out
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
	 * Calculate the Bond Basis from Option Adjusted Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Maturity input
	 * 
	 * @return Calculated Bond Basis from Option Adjusted Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Option Adjusted Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Exercise input
	 * 
	 * @return Calculated Bond Basis from Option Adjusted Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated Yield Spread from Option Adjusted Spread to Work-out
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
	 * Calculate the Yield Spread from Option Adjusted Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Maturity input
	 * 
	 * @return Calculated Yield Spread from Option Adjusted Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Option Adjusted Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Exercise input
	 * 
	 * @return Calculated Yield Spread from Option Adjusted Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS OAS to Work-out input
	 * 
	 * @return Calculated bond yield from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
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
	 * Calculate the bond yield from Option Adjusted Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Maturity input
	 * 
	 * @return Calculated bond yield from Option Adjusted Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Option Adjusted Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Exercise input
	 * 
	 * @return Calculated bond yield from Option Adjusted Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated bond credit basis from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
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
	 * Calculate the bond credit basis from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated bond credit basis from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated bond credit basis from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated Bond PECS from Option Adjusted Spread to Work-out
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
	 * Calculate the Bond PECS from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated Bond PECS from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated Bond PECS from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated spread to treasury from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
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
	 * Calculate the bond spread to treasury from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated spread to treasury from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated spread to treasury from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated G Spread from Option Adjusted Spread to Work-out
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
	 * Calculate the bond G Spread from Option Adjusted Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Maturity input
	 * 
	 * @return Calculated G Spread from Option Adjusted Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Option Adjusted Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to Exercise input
	 * 
	 * @return Calculated G Spread from Option Adjusted Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated I Spread from Option Adjusted Spread to Work-out
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
	 * Calculate the bond I Spread from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated I Spread from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated I Spread from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated Discount Margin from Option Adjusted Spread to Work-out
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
	 * Calculate the bond Discount Margin from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated Discount Margin from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated Discount Margin from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated duration from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the bond duration from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated duration from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated duration from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated Yield01 from Option Adjusted Spread to Work-out
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
	 * Calculate the bond Yield01 from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated Yield01 from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated Yield01 from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated par ASW from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated par ASW from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated par ASW from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Option Adjusted Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblOAS Option Adjusted Spread to Work-out input
	 * 
	 * @return Calculated convexity from Option Adjusted Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from Option Adjusted Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to maturity input
	 * 
	 * @return Calculated convexity from Option Adjusted Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Option Adjusted Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblOAS Option Adjusted Spread to exercise input
	 * 
	 * @return Calculated convexity from Option Adjusted Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromOAS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblOAS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated bond price from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from Bond Basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Maturity input
	 * 
	 * @return Calculated bond price from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Bond Basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Exercise input
	 * 
	 * @return Calculated bond price from Bond Basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Bond Basis to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to work-out input
	 * 
	 * @return Calculated Z Spread from Bond Basis to work-out
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
	 * Calculate the bond Z Spread from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Z Spread from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Z Spread from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Bond Basis to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from Bond Basis to work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated bond yield from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
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
	 * Calculate the bond yield from Bond Basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Maturity input
	 * 
	 * @return Calculated bond yield from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Bond Basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Exercise input
	 * 
	 * @return Calculated bond yield from Bond Basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield Spread from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated bond Yield Spread from Bond Basis to Work-out
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
	 * Calculate the bond Yield Spread from Bond Basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Maturity input
	 * 
	 * @return Calculated bond Yield Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield Spread from Bond Basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Exercise input
	 * 
	 * @return Calculated bond Yield Spread from Bond Basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated bond credit basis from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
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
	 * Calculate the bond credit basis from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated bond credit basis from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated bond credit basis from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated Bond PECS from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
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
	 * Calculate the Bond PECS from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Bond PECS from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Bond PECS from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated spread to treasury from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
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
	 * Calculate the bond spread to treasury from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated spread to treasury from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated spread to treasury from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated G Spread from Bond Basis to Work-out
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
	 * Calculate the bond G Spread from Bond Basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Maturity input
	 * 
	 * @return Calculated G Spread from Bond Basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Bond Basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to Exercise input
	 * 
	 * @return Calculated G Spread from Bond Basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated I Spread from Bond Basis to Work-out
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
	 * Calculate the bond I Spread from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated I Spread from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated I Spread from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated Discount Margin from Bond Basis to Work-out
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
	 * Calculate the bond Discount Margin from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Discount Margin from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Discount Margin from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated Duration from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the bond duration from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated duration from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated duration from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated Yield01 from Bond Basis to Work-out
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
	 * Calculate the bond Yield01 from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Yield01 from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Yield01 from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated par ASW from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Par ASW from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated par ASW from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Convexity from Bond Basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblBondBasis Bond Basis to Work-out input
	 * 
	 * @return Calculated convexity from Bond Basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the Bond Convexity from Bond Basis to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to maturity input
	 * 
	 * @return Calculated Convexity from Bond Basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Bond Basis to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblBondBasis Bond Basis to exercise input
	 * 
	 * @return Calculated Convexity from Bond Basis to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromBondBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblBondBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated bond price from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from Yield Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Maturity input
	 * 
	 * @return Calculated bond price from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Yield Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Exercise input
	 * 
	 * @return Calculated bond price from Yield Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Yield Spread to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread YieldSpread to work-out input
	 * 
	 * @return Calculated Z Spread from Yield Spread to work-out
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
	 * Calculate the bond Z Spread from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Z Spread from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Z Spread from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Yield Spread to work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from Yield Spread to work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated bond yield from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
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
	 * Calculate the bond yield from Yield Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Maturity input
	 * 
	 * @return Calculated bond yield from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Yield Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Exercise input
	 * 
	 * @return Calculated bond yield from Yield Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated Bond Basis from Yield Spread to Work-out
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
	 * Calculate the Bond Basis from Yield Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Maturity input
	 * 
	 * @return Calculated Bond Basis from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Yield Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Exercise input
	 * 
	 * @return Calculated Bond Basis from Yield Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated bond credit basis from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
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
	 * Calculate the bond credit basis from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated bond credit basis from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond credit basis from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated bond credit basis from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the credit basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated Bond PECS from Yield Spread to Work-out
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
	 * Calculate the Bond PECS from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Bond PECS from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Bond PECS from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated spread to treasury from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
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
	 * Calculate the bond spread to treasury from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated spread to treasury from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond spread to treasury from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated spread to treasury from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the spread to treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated G Spread from Yield Spread to Work-out
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
	 * Calculate the bond G Spread from Yield Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Maturity input
	 * 
	 * @return Calculated G Spread from Yield Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Yield Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to Exercise input
	 * 
	 * @return Calculated G Spread from Bond Basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated I Spread from Yield Spread to Work-out
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
	 * Calculate the bond I Spread from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated I Spread from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated I Spread from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated Discount Margin from Yield Spread to Work-out
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
	 * Calculate the bond Discount Margin from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Discount Margin from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Discount Margin from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated Duration from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the Bond Duration from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated duration from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Duration from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated duration from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated Yield01 from Yield Spread to Work-out
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
	 * Calculate the Bond Yield01 from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Yield01 from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield01 from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Yield01 from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated par ASW from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Par ASW from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated par ASW from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Convexity from Yield Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblYieldSpread Yield Spread to Work-out input
	 * 
	 * @return Calculated convexity from Yield Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the Bond Convexity from Yield Spread to maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to maturity input
	 * 
	 * @return Calculated Convexity from Yield Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Yield Spread to exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblYieldSpread Yield Spread to exercise input
	 * 
	 * @return Calculated Convexity from Yield Spread to exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromYieldSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblYieldSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated price from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated price from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated price from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated yield from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
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
	 * Calculate the bond yield from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated yield from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated yield from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Z Spread from credit basis to Work-out
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
	 * Calculate the bond Z Spread from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Z Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Z Spread from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Bond Basis from credit basis to Work-out
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
	 * Calculate the Bond Basis from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Bond Basis from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Bond Basis from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Yield Spread from credit basis to Work-out
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
	 * Calculate the Yield Spread from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Yield Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Yield Spread from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Spread to Treasury from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
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
	 * Calculate the bond Spread to Treasury from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Spread to Treasury from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Spread to Treasury from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated G Spread from credit basis to Work-out
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
	 * Calculate the bond G Spread from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated G Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated G Spread from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated I Spread from credit basis to Work-out
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
	 * Calculate the bond I Spread from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated I Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated I Spread from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Discount Margin from credit basis to Work-out
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
	 * Calculate the bond Discount Margin from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Discount Margin from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Discount Margin from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated PECS from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated PECS from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated PECS from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Duration from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Duration from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Duration from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated Yield01 from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated Yield01 from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated Yield01 from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated par ASW from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated par ASW from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated par ASW from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from credit basis to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblCreditBasis credit basis to Work-out input
	 * 
	 * @return Calculated convexity from credit basis to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Maturity input
	 * 
	 * @return Calculated convexity from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblCreditBasis credit basis to Exercise input
	 * 
	 * @return Calculated convexity from credit basis to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromCreditBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblCreditBasis)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Price from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Price from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
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
	 * Calculate the Bond Price from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Price from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcPriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Price from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Price from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public abstract double calcExercisePriceFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Yield from PECS to Work-out
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
	 * Calculate the Bond Yield from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Yield from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Yield from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Z Spread from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Z Spread from PECS to Work-out
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
	 * Calculate the Bond Z Spread from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Z Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Z Spread from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Z Spread from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Option Adjusted Spread from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Bond Basis from PECS to Work-out
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
	 * Calculate the Bond Basis from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Bond Basis from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Bond Basis from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Yield Spread from PECS to Work-out
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
	 * Calculate the Yield Spread from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Yield Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Yield Spread from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Spread to Treasury from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Spread to Treasury from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
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
	 * Calculate the Bond Spread to Treasury from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Spread to Treasury from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Spread to Treasury from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Spread to Treasury from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond G Spread from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated G Spread from PECS to Work-out
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
	 * Calculate the Bond G Spread from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated G Spread from credit basis to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond G Spread from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated G Spread from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond I Spread from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated I Spread from PECS to Work-out
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
	 * Calculate the Bond I Spread from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated I Spread from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond I Spread from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated I Spread from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Discount Margin from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Discount Margin from PECS to Work-out
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
	 * Calculate the Bond Discount Margin from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Discount Margin from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Discount Margin from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Discount Margin from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Credit Basis from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Credit Basis from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Credit Basis from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Credit Basis from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Credit Basis from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Credit Basis from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Duration from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Duration from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Duration from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Duration from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Duration from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Duration from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield01 from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Yield01 from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield01 from PECS to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Yield01 from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Yield01 from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Yield01 from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Par ASW from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Par ASW from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Par ASW from PECS to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Par ASW from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Convexity from PECS to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblPECS PECS to Work-out input
	 * 
	 * @return Calculated Convexity from PECS to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Convexity from credit basis to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Maturity input
	 * 
	 * @return Calculated Convexity from PECS to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Convexity from credit basis to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblPECS PECS to Exercise input
	 * 
	 * @return Calculated Convexity from PECS to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromPECS (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblPECS)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated price from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated price from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated price from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated yield from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated yield from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated yield from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Z Spread from the spread treasury benchmark to Work-out
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
	 * Calculate the bond Z Spread from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Z Spread from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Z Spread from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Bond Basis from the spread treasury benchmark to Work-out
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
	 * Calculate the Bond Basis from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Bond Basis from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;
	/**
	 * Calculate the Bond Basis from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Bond Basis from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Yield Spread from the spread treasury benchmark to Work-out
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
	 * Calculate the Yield Spread from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Yield Spread from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Yield Spread from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Credit Basis from the spread treasury benchmark to Work-out
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
	 * Calculate the bond Credit Basis from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Credit Basis from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Credit Basis from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated PECS from the spread treasury benchmark to Work-out
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
	 * Calculate the Bond PECS from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated PECS from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated PECS from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated G Spread from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated G Spread from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated G Spread from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated I Spread from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated I Spread from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated I Spread from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Discount Margin from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Discount Margin from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Discount Margin from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated duration from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
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
	 * Calculate the bond duration from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated duration from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond duration from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated duration from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated Yield01 from the spread treasury benchmark to Work-out
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
	 * Calculate the bond Yield01 from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated Yield01 from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated Yield01 from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated par ASW from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated par ASW from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond par ASW from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated par ASW from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from spread treasury benchmark to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblTSYSpread The spread treasury benchmark to Work-out input
	 * 
	 * @return Calculated convexity from the spread treasury benchmark to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from spread treasury benchmark to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Maturity input
	 * 
	 * @return Calculated convexity from the spread treasury benchmark to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from spread treasury benchmark to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblTSYSpread The spread treasury benchmark to Exercise input
	 * 
	 * @return Calculated convexity from the spread treasury benchmark to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromTSYSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblTSYSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated price from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated price from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated price from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated yield from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated yield from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated yield from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated Z Spread from the G Spread to Work-out
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
	 * Calculate the bond Z Spread from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated Z Spread from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated Z Spread from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated Bond Basis from the G Spread to Work-out
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
	 * Calculate the Bond Basis from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated Bond Basis from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated Bond Basis from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The G Spread to Work-out input
	 * 
	 * @return Calculated Yield Spread from the G Spread to Work-out
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
	 * Calculate the Yield Spread from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated Yield Spread from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated Yield Spread from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Credit Basis from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
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
	 * Calculate the bond Credit Basis from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Credit Basis from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Credit Basis from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The PECS to Work-out input
	 * 
	 * @return Calculated PECS from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
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
	 * Calculate the Bond PECS from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Maturity input
	 * 
	 * @return Calculated PECS from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The G Spread to Exercise input
	 * 
	 * @return Calculated PECS from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Spread to Treasury from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Spread to Treasury from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Spread to Treasury from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated I Spread from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated I Spread from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated I Spread from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Discount Margin from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Discount Margin from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Discount Margin from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Duration from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
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
	 * Calculate the bond Duration from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Duration from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Duration from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Yield01 from the G Spread to Work-out
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
	 * Calculate the bond Yield01 from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Yield01 from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Yield01 from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated Par ASW from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated Par ASW from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated Par ASW from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from G Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblGSpread The Credit Basis to Work-out input
	 * 
	 * @return Calculated convexity from the G Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from G Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Maturity input
	 * 
	 * @return Calculated convexity from the G Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from G Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblGSpread The Credit Basis to Exercise input
	 * 
	 * @return Calculated convexity from the G Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromGSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblGSpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated price from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated price from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated price from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated yield from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated yield from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated yield from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Z Spread from the I Spread to Work-out
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
	 * Calculate the bond Z Spread from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Z Spread from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Z Spread from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Bond Basis from the I Spread to Work-out
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
	 * Calculate the Bond Basis from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Bond Basis from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Bond Basis from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Yield Spread from the I Spread to Work-out
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
	 * Calculate the Yield Spread from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Yield Spread from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Yield Spread from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Credit Basis from the I Spread to Work-out
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
	 * Calculate the bond Credit Basis from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Credit Basis from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Credit Basis from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated PECS from the I Spread to Work-out
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
	 * Calculate the Bond PECS from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated PECS from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated PECS from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Spread to Treasury from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Spread to Treasury from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Spread to Treasury from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated G Spread from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated G Spread from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated G Spread from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Duration from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
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
	 * Calculate the bond Duration from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Duration from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Duration from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Yield01 from the I Spread to Work-out
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
	 * Calculate the bond Yield01 from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Yield01 from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Yield01 from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated Par ASW from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated Par ASW from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated Par ASW from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from I Spread to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblISpread The I Spread to Work-out input
	 * 
	 * @return Calculated convexity from the I Spread to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from I Spread to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Maturity input
	 * 
	 * @return Calculated convexity from the I Spread to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from I Spread to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblISpread The I Spread to Exercise input
	 * 
	 * @return Calculated convexity from the I Spread to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromISpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblISpread)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated price from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
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
	 * Calculate the bond price from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated price from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated price from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated yield from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated yield from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated yield from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Z Spread from the Discount Margin to Work-out
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
	 * Calculate the bond Z Spread from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Z Spread from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Z Spread from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
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
	 * Calculate the bond Option Adjusted Spread from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Bond Basis from the Discount Margin to Work-out
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
	 * Calculate the Bond Basis from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Bond Basis from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Bond Basis from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Yield Spread from the Discount Margin to Work-out
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
	 * Calculate the Yield Spread from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Yield Spread from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Yield Spread from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Credit Basis from the Discount Margin to Work-out
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
	 * Calculate the bond Credit Basis from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Credit Basis from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Credit Basis from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated PECS from the Discount Margin to Work-out
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
	 * Calculate the Bond PECS from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated PECS from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated PECS from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Spread to Treasury from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Spread to Treasury from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Spread to Treasury from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated G Spread from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated G Spread from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated G Spread from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Duration from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
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
	 * Calculate the bond Duration from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Duration from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Duration from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Yield01 from the Discount Margin to Work-out
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
	 * Calculate the bond Yield01 from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Yield01 from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Yield01 from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated Par ASW from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated Par ASW from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Par ASW from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated Par ASW from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Par ASW cannot be calculated
	 */

	public abstract double calcExerciseParASWFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Discount Margin to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblDiscountMargin The Discount Margin to Work-out input
	 * 
	 * @return Calculated convexity from the Discount Margin to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
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
	 * Calculate the bond convexity from Discount Margin to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Maturity input
	 * 
	 * @return Calculated convexity from the Discount Margin to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Discount Margin to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblDiscountMargin The Discount Margin to Exercise input
	 * 
	 * @return Calculated convexity from the Discount Margin to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromDiscountMargin (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblDiscountMargin)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated price from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated price from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcPriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond price from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated price from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the price cannot be calculated
	 */

	public abstract double calcExercisePriceFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated yield from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated yield from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond yield from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated yield from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the yield cannot be calculated
	 */

	public abstract double calcExerciseYieldFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Z Spread from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Z Spread from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Z Spread from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Z Spread from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Z Spread cannot be calculated
	 */

	public abstract double calcExerciseZSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Option Adjusted Spread from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Option Adjusted Spread from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Option Adjusted Spread from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Option Adjusted Spread from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Option Adjusted Spread cannot be calculated
	 */

	public abstract double calcExerciseOASFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Bond Basis from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Bond Basis from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond Basis from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Bond Basis from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Bond Basis cannot be calculated
	 */

	public abstract double calcExerciseBondBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Yield Spread from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Yield Spread from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Yield Spread from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Yield Spread from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield Spread cannot be calculated
	 */

	public abstract double calcExerciseYieldSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Credit Basis from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Credit Basis from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Credit Basis from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Credit Basis from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Credit Basis cannot be calculated
	 */

	public abstract double calcExerciseCreditBasisFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated PECS from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated PECS from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcPECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the Bond PECS from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated PECS from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the PECS cannot be calculated
	 */

	public abstract double calcExercisePECSFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury Benchmark from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Spread to Treasury Benchmark from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury Benchmark cannot be calculated
	 */

	public abstract double calcTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury Benchmark from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Spread to Treasury Benchmark from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury Benchmark cannot be calculated
	 */

	public abstract double calcTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Spread to Treasury Benchmark from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Spread to Treasury Benchmark from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Spread to Treasury Benchmark cannot be calculated
	 */

	public abstract double calcExerciseTSYSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated G Spread from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated G Spread from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond G Spread from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated G Spread from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the G Spread cannot be calculated
	 */

	public abstract double calcExerciseGSpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated I Spread from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated I Spread from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond I Spread from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated I Spread from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the I Spread cannot be calculated
	 */

	public abstract double calcExerciseISpreadFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Discount Margin from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Discount Margin from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Discount Margin from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Discount Margin from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Discount Margin cannot be calculated
	 */

	public abstract double calcExerciseDiscountMarginFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Duration from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Duration from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Duration from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Duration from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Duration cannot be calculated
	 */

	public abstract double calcExerciseDurationFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated Yield01 from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated Yield01 from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond Yield01 from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated Yield01 from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the Yield01 cannot be calculated
	 */

	public abstract double calcExerciseYield01FromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Par ASW to Work-out
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblWorkoutDate JulianDate Work-out
	 * @param dblWorkoutFactor Work-out factor
	 * @param dblParASW The Par ASW to Work-out input
	 * 
	 * @return Calculated convexity from the Par ASW to Work-out
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblWorkoutDate,
		final double dblWorkoutFactor,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Par ASW to Maturity
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Maturity input
	 * 
	 * @return Calculated convexity from the Par ASW to Maturity
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
		throws java.lang.Exception;

	/**
	 * Calculate the bond convexity from Par ASW to Exercise
	 * 
	 * @param valParams ValuationParams
	 * @param mktParams Bond market parameters
	 * @param quotingParams Bond Quoting parameters
	 * @param dblParASW The Par ASW to Exercise input
	 * 
	 * @return Calculated convexity from the Par ASW to Exercise
	 * 
	 * @throws java.lang.Exception Thrown if the convexity cannot be calculated
	 */

	public abstract double calcExerciseConvexityFromParASW (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblParASW)
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
