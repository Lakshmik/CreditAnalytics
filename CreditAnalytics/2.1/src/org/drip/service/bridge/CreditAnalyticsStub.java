
package org.drip.service.bridge;

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
 *  This class receives the requests from the analytics client, and invokes the CreditAnalytics
 *  	functionality, and sends the client the results.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalyticsStub {
	private static void sendMessage (
		final java.io.ObjectOutputStream out,
		final java.lang.String strMsg)
		throws java.lang.Exception
	{
		out.writeObject (strMsg);

		out.flush();
	}

	private static final void run (
		final java.sql.Statement stmt,
		final org.drip.param.definition.MarketParams mpc,
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		java.net.ServerSocket sockAS = org.drip.param.config.ConfigLoader.InitAnalServer
			("c:\\Lakshmi\\BondAnal\\Config.xml");

		java.net.Socket sockAC = sockAS.accept();

		System.out.println ("Connection from: " + sockAC.getInetAddress().getHostName());

		java.io.ObjectOutputStream out = new java.io.ObjectOutputStream (sockAC.getOutputStream());

		java.io.ObjectInputStream in = new java.io.ObjectInputStream (sockAC.getInputStream());

		java.lang.String strCommand = (java.lang.String) in.readObject();

		if (strCommand.startsWith ("BondAnalFromPrice")) {
			sendMessage (out, "InProgress");

			int iNumRecord = 0;
			java.lang.String astrFields[] = new java.lang.String[4];

			java.util.StringTokenizer st = new java.util.StringTokenizer (strCommand, ";");

			while (st.hasMoreTokens())
				astrFields[iNumRecord++] = st.nextToken();

			(st = new java.util.StringTokenizer (astrFields[1], "=")).nextToken();

			java.lang.String strISIN = st.nextToken();

			(st = new java.util.StringTokenizer (astrFields[2], "=")).nextToken();

			double dblBidPrice = (new java.lang.Double (st.nextToken())).doubleValue();

			(st = new java.util.StringTokenizer (astrFields[3], "=")).nextToken();

			double dblAskPrice = (new java.lang.Double (st.nextToken())).doubleValue();

			System.out.println ("ISIN: " + strISIN + "; Bid: " + dblBidPrice + "; Ask: " + dblAskPrice);

			if ("CompleteSet".equalsIgnoreCase (strISIN)) {
				org.drip.tester.functional.BondTestSuite.RunFullBondTests (mpc, dt, dblBidPrice,
					dblAskPrice);

				sendMessage (out, "Completed");
			} else {
				long lTestStart = System.nanoTime();

				java.util.Map<java.lang.String, org.drip.analytics.output.BondRVMeasures> mapBMRV =
					org.drip.service.env.BondManager.CalcBondAnalyticsFromPrice (strISIN, mpc, dt,
						dblBidPrice, dblBidPrice + 1.);

				System.out.println ("Run on " + strISIN + " took " + (System.nanoTime() - lTestStart) *
					1.e-06 + " milli-sec\n");

				if (null == mapBMRV || 2 != mapBMRV.size())
					sendMessage (out, "Bad Analytics for " + strISIN + "[" + strCommand + "]");
				else {
					java.lang.String strAskRVCalcOP = "";
					java.lang.String strBidRVCalcOP = "";

					org.drip.analytics.output.BondRVMeasures bmRVAsk = mapBMRV.get ("ASK");

					if (null != bmRVAsk) strAskRVCalcOP = new java.lang.String (bmRVAsk.serialize());

					org.drip.analytics.output.BondRVMeasures bmRVBid = mapBMRV.get ("BID");

					if (null != bmRVBid) strBidRVCalcOP = new java.lang.String (bmRVBid.serialize());

					sendMessage (out, strAskRVCalcOP + "?" + strBidRVCalcOP);
				}
			}
		} else
			sendMessage (out, "Unknown Command: " + strCommand);

		sockAS.close();
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		java.sql.Statement stmt = org.drip.service.env.EnvManager.InitEnv
			("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 5,
			20);

		org.drip.param.definition.MarketParams mpc = org.drip.service.env.EnvManager.PopulateMPC (stmt,
			dt);

		org.drip.service.env.BondManager.CommitBondsToMem (mpc, stmt);

		System.out.println ("Ready ...");

		while (true)
			run (stmt, mpc, dt);
	}
}
