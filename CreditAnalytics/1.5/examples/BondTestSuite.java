
package org.drip.tester.product;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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

public class BondTestSuite {
	public static final void RunFullBondTests (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final double dblBidPrice, final double dblAskPrice) throws
			java.lang.Exception {
		long lTestStart = System.nanoTime();

		int iNumProcessed = org.drip.service.env.BondManager.CalcFullBondAnalytics (mpc, dt, dblBidPrice,
			dblAskPrice);

		long lNanos = System.nanoTime() - lTestStart;

		System.out.println ("Run on " + iNumProcessed + " bonds took " + lNanos * 1.e-09 + " sec - " +
			+ (lNanos / iNumProcessed) * 1.e-06 + " msec per bond\n");
	}

	public static final void RunFullMarketBondTests (final org.drip.param.market.MarketParamsContainer mpc,
		final org.drip.util.date.JulianDate dt) throws java.lang.Exception {
		long lTestStart = System.nanoTime();

		int iNumProcessed = org.drip.service.env.BondManager.FullBondMarketAnalytics (mpc, dt);

		long lNanos = System.nanoTime() - lTestStart;

		System.out.println ("Run on " + iNumProcessed + " bonds took " + lNanos * 1.e-09 + " sec - "
			+ (lNanos / iNumProcessed) * 1.e-06 + " msec per bond\n");
	}

	public static void main (java.lang.String astrArgs[]) throws java.lang.Exception {
		if (1 > astrArgs.length || null == astrArgs[0] || astrArgs[0].isEmpty()) {
			System.out.println
				("Usage: org.drip.tester.product.BondTestSuite -<<eodcdsmeasures | eodcdssetmeasures | eodbondsetmeasures>>");

			System.exit (3212);
		}

		java.sql.Statement stmt = org.drip.service.env.EnvManager.InitEnv
			("c:\\Lakshmi\\java\\BondAnal\\Config.xml");

		org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.CreateFromYMD (2011, 1, 3);

		org.drip.util.date.JulianDate dtEnd = org.drip.util.date.JulianDate.CreateFromYMD (2011, 6, 30);

		if ("-eodcdssetmeasures".equalsIgnoreCase (astrArgs[0])) {
			org.drip.service.env.CDSManager.CalcAndLoadCDSClosingMeasures (stmt, dt, dtEnd);

			return;
		}

		if ("-eodcdsmeasures".equalsIgnoreCase (astrArgs[0])) {
			org.drip.service.env.CDSManager.SaveCreditCalibMeasures (stmt, dt);

			return;
		}

		System.out.println ("Time: " + (new java.util.Date()).toString());

		org.drip.param.market.MarketParamsContainer mpc = org.drip.service.env.EnvManager.PopulateMPC (stmt,
			dt);

		if ("-eodbondsetmeasures".equalsIgnoreCase (astrArgs[0])) {
			org.drip.service.env.BondManager.CalcAndLoadBondClosingMeasures (mpc, stmt, dt, dtEnd);

			return;
		}

		java.lang.String strBondSetID = "UBS";
		double dblMidPriceMark = 100.;
		double dblBidPriceStart = dblMidPriceMark - 0.;
		double dblBidPriceFinish = dblMidPriceMark + 0.5;
		double dblBidPriceIncrement = 10.;
		java.lang.String strCommand = "TickerAnalFromPrice";

		if (0 < astrArgs.length) {
			strCommand = astrArgs[0];

			if (1 < astrArgs.length) {
				strBondSetID = astrArgs[1];

				if (2 < astrArgs.length) {
					dblBidPriceStart = java.lang.Double.valueOf (astrArgs[2]).doubleValue();

					if (3 < astrArgs.length) {
						dblBidPriceFinish = java.lang.Double.valueOf (astrArgs[3]).doubleValue();

						if (4 < astrArgs.length)
							dblBidPriceIncrement = java.lang.Double.valueOf (astrArgs[4]).doubleValue();
					}
				}
			}
		}

		if (strCommand.startsWith ("MarketBondAnalFromPrice")) {
			org.drip.service.env.BondManager.LoadMidBondMarks (dt, stmt);

			RunFullMarketBondTests (mpc, dt);

			return;
		}

		if (strCommand.startsWith ("MarketTickerAnalFromPrice")) {
			org.drip.service.env.BondManager.LoadMidBondMarks (dt, stmt);

			org.drip.service.env.BondManager.CalcMarketMeasuresForTicker (strBondSetID, mpc, dt);

			return;
		}

		if (strCommand.startsWith ("SaveCREOD")) {
			org.drip.service.env.CDSManager.SaveCreditCalibMeasures (stmt, dt);

			return;
		}

		if (strCommand.startsWith ("SaveSPNEOD")) {
			org.drip.service.env.CDSManager.SaveSPNCalibMeasures (mpc, stmt, "1623583", dt);

			return;
		}

		System.out.println ("Time 2: " + (new java.util.Date()).toString());

		System.out.println ("Number of bonds committed is " +
			org.drip.service.env.BondManager.CommitBondsToMem (mpc, stmt));

		System.out.println ("Time 3: " + (new java.util.Date()).toString());

		for (double dblBidPrice = dblBidPriceStart; dblBidPrice < dblBidPriceFinish; dblBidPrice +=
			dblBidPriceIncrement) {
			if (strCommand.startsWith ("BondAnalFromPrice")) {
				if ("CompleteSet".equalsIgnoreCase (strBondSetID))
					RunFullBondTests (mpc, dt, dblBidPrice, dblBidPrice + 1.);
				else {
					long lTestStart = System.nanoTime();

					org.drip.service.env.BondManager.CalcBondAnalyticsFromPrice (strBondSetID, mpc, dt,
						dblBidPrice, dblBidPrice + 1.);

					System.out.println ("Run on " + strBondSetID + " took " + (System.nanoTime() -
						lTestStart) * 1.e-06 + " milli-sec\n");
				}
			} else if (strCommand.startsWith ("TickerAnalFromPrice"))
				org.drip.service.env.BondManager.CalcMeasuresForTicker (strBondSetID, mpc, dt, dblBidPrice,
					dblBidPrice + 1.);
			else {
				System.out.println ("Unknown command!");

				System.exit (36);
			}
		}

		System.out.println ("Time: " + (new java.util.Date()).toString());
	}
}
