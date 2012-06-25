
package org.drip.service.sample;

import org.drip.util.date.JulianDate;
import org.drip.analytics.curve.DiscountCurve;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.CreditAnalytics.org
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
 * Demo of the Rates Analytics API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RatesAnalyticsAPISample {
	/**
	 * Sample API demonstrating the creation/usage of discount curve
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void DiscountCurveAPISample() throws Exception {
		JulianDate dtStart = JulianDate.Today();

		double[] adblDF = new double[5];
		double[] adblDate = new double[5];
		double[] adblRate = new double[5];

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblDF[i] = 1. - 2 * (i + 1) * 0.05;
			adblRate[i] = 0.05;
		}

		/*
		 * Build the discount curve from an array of dates and discount factors
		 */

		DiscountCurve dcFromDF = DiscountCurve.BuildFromDF (dtStart, "EUR", adblDate, adblDF);

		JulianDate dt = dtStart.addYears (10);

		System.out.println ("DCFromDF[" + dt.toString() + "]; DF=" + dcFromDF.getDF (dt) + "; Rate=" +
			dcFromDF.calcImpliedRate ("10Y"));

		/*
		 * Build the discount curve from an array of dates and forward rates
		 */

		DiscountCurve dcFromRate = DiscountCurve.CreateDC (dtStart, "EUR", adblDate, adblRate);

		System.out.println ("DCFromRate[" + dt.toString() + "]; DF=" + dcFromRate.getDF (dt) + "; Rate=" +
			dcFromRate.calcImpliedRate ("10Y"));

		/*
		 * Build the discount curve from a flat rate
		 */

		DiscountCurve dcFromFlatRate = DiscountCurve.CreateFromFlatRate (dtStart, "DKK", 0.04);

		System.out.println ("DCFromFlatRate[" + dt.toString() + "]; DF=" + dcFromFlatRate.getDF (dt) +
			"; Rate=" + dcFromFlatRate.calcImpliedRate ("10Y"));
	}

	public static final void main (final String astrArgs[]) throws Exception {
		// String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		String strConfig = "";

		org.drip.service.api.FI.Init (strConfig);

		DiscountCurveAPISample();
	}
}
