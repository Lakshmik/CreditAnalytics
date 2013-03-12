
package org.drip.param.definition;

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
 * This abstract class serves as the place holder for the different Rates scenario curves. It exposes
 * 		scenario generator object that wraps the calibration instruments - the base curve, bumped up/down
 * 			curves, and tenor mapped up/down curves.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class RatesScenarioCurve {

	/**
	 * Base Discount Curve
	 */

	public static final int DC_BASE = 0;

	/**
	 * Discount Curve Parallel Bump Up
	 */

	public static final int DC_FLAT_UP = 1;

	/**
	 * Discount Curve Parallel Bump Down
	 */

	public static final int DC_FLAT_DN = 2;

	/**
	 * Discount Curve Tenor Bump Up
	 */

	public static final int DC_TENOR_UP = 4;

	/**
	 * Discount Curve Tenor Bump Down
	 */

	public static final int DC_TENOR_DN = 8;

	/**
	 * Generates the set of discount curves from the scenario specified, and the instrument quotes
	 * 
	 * @param valParams Valuation Parameters
	 * @param dcTSY The Treasury Discount Curve
	 * @param dcEDSF The EDSF Discount Curve
	 * @param adblQuotes Matched array of the calibration instrument quotes
	 * @param dblBump Amount of bump to be applied
	 * @param astrCalibMeasure Matched array of the calibration instrument measures
	 * @param mmFixings Double map of date/rate index and fixings
	 * @param quotingParams Quoting Parameters
	 * @param iDCMode One of the values in the DC_ enum listed above.
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean cookScenarioDC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblBump,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final int iDCMode);

	/**
	 * Cooks a custom discount curve according to the desired tweak parameters
	 * 
	 * @param strCurveName Scenario Discount Curve Name
	 * @param strCustomName Custom Scenario Name
	 * @param valParams Valuation Parameters
	 * @param dcTSY TSY Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param adblQuotes Double array of input quotes
	 * @param astrCalibMeasure Array of calibration measures
	 * @param mmFixings Date/Index fixings
	 * @param quotingParams Calibration quoting parameters
	 * @param ntpTSY Node Tweak Parameters for the TSY Discount Curve
	 * @param ntpEDSF Node Tweak Parameters for the EDSF Discount Curve
	 * @param ntpDC Node Tweak Parameters for the Base Discount Curve
	 * 
	 * @return Creates a custom discount curve
	 */

	public abstract boolean cookCustomDC (
		final java.lang.String strCurveName,
		final java.lang.String strCustomName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.definition.NodeTweakParams ntpTSY,
		final org.drip.param.definition.NodeTweakParams ntpEDSF,
		final org.drip.param.definition.NodeTweakParams ntpDC);

	/**
	 * Return the base Discount Curve
	 * 
	 * @return The base Discount Curve
	 */

	public abstract org.drip.analytics.definition.DiscountCurve getDCBase();

	/**
	 * Return the Bump Up Discount Curve
	 * 
	 * @return The Bump Up Discount Curve
	 */

	public abstract org.drip.analytics.definition.DiscountCurve getDCBumpUp();

	/**
	 * Return the Bump Down Discount Curve
	 * 
	 * @return The Bump Down Discount Curve
	 */

	public abstract org.drip.analytics.definition.DiscountCurve getDCBumpDn();

	/**
	 * Return the map of the tenor Bump Up Discount Curve
	 * 
	 * @return The map of the tenor Bump Up Discount Curve
	 */

	public abstract java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve>
		getTenorDCBumpUp();

	/**
	 * Return the map of the tenor Bump Down Discount Curve
	 * 
	 * @return The map of the tenor Bump Down Discount Curve
	 */

	public abstract java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve>
		getTenorDCBumpDn();
}
