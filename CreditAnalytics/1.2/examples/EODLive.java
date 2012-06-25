
package org.drip.service.sample;

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
 * Comprehensive sample class demo'ing the usage of the EOD and Live Curve FI API functions
 * 
 * @author Lakshmi Krishnamurthy
 */

public class EODLive {
	public static final boolean s_bDumpPriceMeasures = false;
	public static final boolean s_bDumpTSYSpreadMeasures = false;
	public static final boolean s_bDumpYieldMeasures = false;

	public static final void BondEODSample() {
		org.drip.util.date.JulianDate dtEOD = org.drip.util.date.JulianDate.CreateFromYMD (2012, 1, 13);

		// java.lang.String strISIN = "219350AQ8"; // EOS
		java.lang.String strISIN = "US44180Y2046"; // Plain old fixed coupon

		java.util.Map<java.lang.String, java.lang.Double> mapPriceMeasures =
			org.drip.service.api.FI.BondEODMeasuresFromPrice (strISIN, dtEOD, 1.);

		java.util.Map<java.lang.String, java.lang.Double> mapTSYSpreadMeasures =
			org.drip.service.api.FI.BondEODMeasuresFromTSYSpread (strISIN, dtEOD, 0.0486);

		java.util.Map<java.lang.String, java.lang.Double> mapYieldMeasures =
			org.drip.service.api.FI.BondEODMeasuresFromYield (strISIN, dtEOD, 0.0749);

		if (s_bDumpPriceMeasures) {
			System.out.println ("\n--------------\nPrice Measures\n--------------");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapPriceMeasures.entrySet())
				System.out.println (me.getKey() + "=" + me.getValue());
		}

		if (s_bDumpTSYSpreadMeasures) {
			System.out.println ("\n---------------\nSpread Measures\n---------------");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
				mapTSYSpreadMeasures.entrySet())
				System.out.println (me.getKey() + "=" + me.getValue());
		}

		if (s_bDumpYieldMeasures) {
			System.out.println ("\n--------------\nYield Measures\n--------------");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
				mapYieldMeasures.entrySet())
				System.out.println (me.getKey() + "=" + me.getValue());
		}
	}

	public static final void main (final java.lang.String astrArgs[]) {
		java.lang.String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		// java.lang.String strConfig = "";

		if (null != astrArgs) {
			if (1 == astrArgs.length)
				strConfig = astrArgs[0];
			else {
				if (2 <= astrArgs.length) {
					if (astrArgs[0].equalsIgnoreCase ("-config") || astrArgs[0].equalsIgnoreCase ("-cfg") ||
						astrArgs[0].equalsIgnoreCase ("-conf"))
						strConfig = astrArgs[1];
				}
			}
		}

		boolean bFIInit = org.drip.service.api.FI.Init (strConfig);

		if (!bFIInit) System.out.println ("Cannot fully init FI!");

		BondEODSample();
	}
}
