
package org.drip.service.sample;

import java.util.*;

import org.drip.service.api.FI;
import org.drip.util.date.JulianDate;
import org.drip.analytics.curve.CreditCurve;
import org.drip.product.credit.CreditDefaultSwap;
import org.drip.product.common.CalibratableComponent;

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
 * Comprehensive sample class demo'ing the usage of the EOD and Live CDS Curve API functions
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSLiveAndEODAPISample {
	/**
	 * Sample API demonstrating the creation/usage of the credit curve API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void CreditCurveEODAPISample() throws Exception {
		JulianDate dt = JulianDate.CreateFromYMD (2011, 7, 21);

		/*
		 * Retrieves all the CDS curves available for the given EOD
		 */

		Set<String> setstrCDSCurves = FI.GetEODCDSCurveNames (dt);

		for (String strCDSCurveName : setstrCDSCurves)
			System.out.println (strCDSCurveName);

		/*
		 * Retrieves the calibrated credit curve from the CDS instruments for the given CDS curve name,
		 * 		IR curve name, and EOD. Also shows the 10Y survival probability and hazard rate.
		 */

		CreditCurve ccEOD = FI.LoadEODCDSCreditCurve ("813796", "USD", dt);

		JulianDate dt10Y = JulianDate.Today().addYears (10);

		System.out.println ("CCFromEOD[" + dt10Y.toString() + "]; Survival=" + ccEOD.getSurvival ("10Y") +
			"; Hazard=" + ccEOD.calcHazard ("10Y"));

		/*
		 * Displays the CDS quotes used to construct the closing credit curve
		 */

		double[] adblCDSQuotes = ccEOD.getCompQuotes();

		CalibratableComponent[] aCompCDS = ccEOD.getCalibComponents();

		for (int i = 0; i < aCompCDS.length; ++i)
			System.out.println (aCompCDS[i].getPrimaryCode() + " => " + (int) (adblCDSQuotes[i]));

		/*
		 * Loads all available credit curves for the given curve ID built from CDS instruments between 2 dates
		 */

		Map<JulianDate, CreditCurve> mapCC = FI.LoadEODCDSCreditCurves ("813796", "USD", JulianDate.CreateFromYMD (2011, 7, 14), dt);

		/*
		 * Displays their 5Y CDS quote
		 */

		for (Map.Entry<JulianDate, CreditCurve> meCC : mapCC.entrySet()) {
			JulianDate dtME = meCC.getKey();

			CreditCurve ccCOB = meCC.getValue();

			System.out.println (dtME + "[CDS.5Y] => " + (int) (ccCOB.getQuote ("CDS.5Y")));
		}
	}

	/**
	 * Sample demonstrating the calculation of the CDS EOD measures from price
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void CDSEODMeasuresAPISample() {
		JulianDate dtEOD = JulianDate.CreateFromYMD (2011, 7, 21); // EOD

		/*
		 * Create a spot starting CDS based off of a specific credit curve
		 */

		CreditDefaultSwap cds = CreditDefaultSwap.CreateSNAC (JulianDate.Today(), "5Y", 0.1, "813796");

		/*
		 * Calculate the EOD CDS measures
		 */

		Map<String, Double> mapEODCDSMeasures = FI.GetEODCDSMeasures (cds, dtEOD);

		/*
		 * Display the EOD CDS measures
		 */

		for (Map.Entry<String, Double> me : mapEODCDSMeasures.entrySet())
			System.out.println (me.getKey() + " => " + me.getValue());
	}

	public static final void main (final String astrArgs[]) throws Exception {
		// String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		String strConfig = "";

		if (!FI.Init (strConfig)) {
			System.out.println ("Cannot fully init FI!");

			System.exit (305);
		}

		CreditCurveEODAPISample();

		CDSEODMeasuresAPISample();
	}
}
