
package org.drip.param.creator;

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
 * This class implements the various ways of constructing, de-serializing, and building the Rates Scenario
 * 		Curves Container.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RatesScenarioCurveBuilder {

	/**
	 * Creates an RatesScenarioCurve Instance from the currency and the array of the calibration
	 * 	instruments
	 * 
	 * @param strCurrency Currency
	 * @param strBootstrapMode Bootstrap Mode - one of the choices in DiscountCurveBuilder.BOOTSTRAP_MODE_xxx
	 * @param aCalibInst Array of the calibration instruments
	 * 
	 * @return The RatesScenarioCurve instance
	 */

	public static final org.drip.param.definition.RatesScenarioCurve FromIRCSG (
		final java.lang.String strCurrency,
		final java.lang.String strBootstrapMode,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst)
	{
		if (null == strCurrency || strCurrency.isEmpty() || null == aCalibInst || 0 == aCalibInst.length) {
			System.out.println ("Invalid ccy/calib comp in RatesScenarioCurveBuilder.FromIRCSG");

			return null;
		}

		try {
			return new org.drip.param.market.RatesCurveScenarioContainer (new
				org.drip.analytics.calibration.RatesCurveScenarioGenerator (strCurrency, strBootstrapMode,
					aCalibInst));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates Discount Curve from the Rates Calibration Instruments
	 * 
	 * @param dt Valuation Date
	 * @param strCurrency Currency
	 * @param strBootstrapMode Bootstrap Mode - one of the choices in DiscountCurveBuilder.BOOTSTRAP_MODE_xxx
	 * @param aCalibInst Input Rates Calibration Instruments
	 * @param adblQuotes Input Calibration Quotes
	 * @param astrCalibMeasure Input Calibration Measures
	 * @param mmFixings (Optional) Input Fixings
	 * 
	 * @return The Calibrated Discount Curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve CreateDiscountCurve (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strCurrency,
		final java.lang.String strBootstrapMode,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblQuotes,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings)
	{
		org.drip.param.definition.RatesScenarioCurve irsg = FromIRCSG (strCurrency, strBootstrapMode, aCalibInst);

		if (null == irsg || !irsg.cookScenarioDC (org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), null, null, adblQuotes, 0.,
				astrCalibMeasure, mmFixings, null, org.drip.param.definition.RatesScenarioCurve.DC_BASE))
			return null;

		return irsg.getDCBase();
	}
}
