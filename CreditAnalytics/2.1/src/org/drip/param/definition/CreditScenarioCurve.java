
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
 * This abstract class exposes the bump parameters and the curves for the different credit curve scenarios -
 * 		the spread and the recovery bumps, and the credit curve scenario generator object
 * 		that wraps the calibration instruments. It also exposes the base credit curve, spread bumped up/down
 * 		credit curves, recovery bumped up/down credit curves, and the tenor mapped up/down credit curves.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class CreditScenarioCurve {

	/**
	 * CC Scenario Base
	 */

	public static final int CC_BASE = 0;

	/**
	 * CC Scenario Parallel Up
	 */

	public static final int CC_FLAT_UP = 1;

	/**
	 * CC Scenario Parallel Down
	 */

	public static final int CC_FLAT_DN = 2;

	/**
	 * CC Scenario Tenor Up
	 */

	public static final int CC_TENOR_UP = 4;

	/**
	 * CC Scenario Tenor Down
	 */

	public static final int CC_TENOR_DN = 8;

	/**
	 * CC Scenario Recovery Parallel Up
	 */

	public static final int CC_RR_FLAT_UP = 16;

	/**
	 * CC Scenario Recovery Parallel Down
	 */

	public static final int CC_RR_FLAT_DN = 32;

	/**
	 * Cooks and saves the credit curves corresponding to the scenario specified
	 * 
	 * @param strName Credit Curve Name
	 * @param valParams ValuationParams
	 * @param dc Base Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param adblQuotes Matched array of Quotes
	 * @param dblRecovery Curve Recovery
	 * @param astrCalibMeasure Matched array of Calibration measures
	 * @param mmFixings Double map of date/rate index and fixings
	 * @param quotingParams Quoting Parameters
	 * @param bFlat Whether the calibration is to a flat curve
	 * @param iCCScenario One of the values in the CC_ enum listed above. 
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean cookScenarioCC (
		final java.lang.String strName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblRecovery,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat,
		final int iCCScenario);

	/**
	 * Cook the credit curve according to the desired tweak parameters
	 * 
	 * @param strName Scenario Credit Curve Name
	 * @param strCustomName Scenario Name
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY TSY Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param adblQuotes Double array of input quotes
	 * @param dblRecovery Recovery Rate
	 * @param astrCalibMeasure Array of calibration measures
	 * @param mmFixings Date/Index fixings
	 * @param quotingParams Calibration quoting parameters
	 * @param bFlat Whether the calibration is flat
	 * @param ntpDC Node Tweak Parameters for the Base Discount Curve
	 * @param ntpTSY Node Tweak Parameters for the TSY Discount Curve
	 * @param ntpEDSF Node Tweak Parameters for the EDSF Discount Curve
	 * @param ntpCC Node Tweak Parameters for the Credit Curve
	 * 
	 * @return True => Credit Curve successfully created
	 */

	public abstract boolean cookCustomCC (
		final java.lang.String strName,
		final java.lang.String strCustomName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblRecovery,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat,
		final org.drip.param.definition.NodeTweakParams ntpDC,
		final org.drip.param.definition.NodeTweakParams ntpTSY,
		final org.drip.param.definition.NodeTweakParams ntpEDSF,
		final org.drip.param.definition.NodeTweakParams ntpCC);

	/**
	 * Return the base credit curve
	 * 
	 * @return The base credit curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCCBase();

	/**
	 * Return the bump up credit curve
	 * 
	 * @return The Bumped up credit curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCCBumpUp();

	/**
	 * Return the bump down credit curve
	 * 
	 * @return The Bumped down credit curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCCBumpDn();

	/**
	 * Return the recovery bump up credit curve
	 * 
	 * @return The Recovery Bumped up credit curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCCRecoveryUp();

	/**
	 * Return the recovery bump down credit curve
	 * 
	 * @return The Recovery Bumped Down credit curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCCRecoveryDn();

	/**
	 * Return the tenor bump up credit curve map
	 * 
	 * @return The Tenor Bumped up credit curve Map
	 */

	public abstract java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getTenorCCBumpUp();

	/**
	 * Return the tenor bump down credit curve map
	 * 
	 * @return The Tenor Bumped Down credit curve Map
	 */

	public abstract java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getTenorCCBumpDn();
}
