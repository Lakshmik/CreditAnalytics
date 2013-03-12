
package org.drip.analytics.definition;

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
 * This abstract class contains the stub functionality for the term structure of dates/times and FX forwards
 * 		(PIP/outright), and Spot FX info for the given currency pair.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class FXForwardCurve extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {
	/**
	 * Returns the CurrencyPair
	 * 
	 * @return CurrencyPair
	 */

	public abstract org.drip.product.params.CurrencyPair getCurrencyPair();

	/**
	 * 
	 * Returns the Spot Date
	 * 
	 * @return Spot Date
	 */

	public abstract org.drip.analytics.date.JulianDate getSpotDate();

	/**
	 * Returns the FX Spot
	 * 
	 * @return FXSpot
	 */

	public abstract double getFXSpot();

	/**
	 * Calculates the set of full basis given the input discount curves
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Denominator
	 * @param bBasisOnDenom True if the basis is calculated on the denominator discount curve
	 * 
	 * @return Array of the computed basis
	 */

	public abstract double[] getFullBasis (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom);

	/**
	 * Bootstrap the basis to the discount curve inputs
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Denominator
	 * @param bBasisOnDenom True if the basis is calculated on the denominator discount curve
	 * 
	 * @return Array of the computed basis
	 */

	public abstract double[] bootstrapBasis (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom);

	/**
	 * Bootstrap the discount curve from the discount curve inputs
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Denominator
	 * @param bBasisOnDenom True if the basis is calculated on the denominator discount curve
	 * 
	 * @return Array of the computed basis
	 */

	public abstract org.drip.analytics.definition.DiscountCurve bootstrapBasisDC (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom);

	/**
	 * Calculate the rates implied by the discount curve inputs
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Denominator
	 * @param bBasisOnDenom True if the basis is calculated on the denominator discount curve
	 * 
	 * @return Array of the computed implied rates
	 */

	public abstract double[] calcImpliedNodeRates (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom);

	/**
	 * Calculate the rate implied by the discount curve inputs to a specified date
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Denominator
	 * @param dblDate Date to which the implied rate is sought
	 * @param bBasisOnDenom True if the implied rate is calculated on the denominator discount curve
	 * 
	 * @return Implied rate
	 * 
	 * @throws java.lang.Exception Thrown if the implied rate cannot be calculated
	 */

	public abstract double calcImpliedRate (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final double dblDate,
		final boolean bBasisOnDenom)
		throws java.lang.Exception;
}
