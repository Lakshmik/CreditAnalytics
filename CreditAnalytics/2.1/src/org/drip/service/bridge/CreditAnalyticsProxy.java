
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
 *  Class captures the requests for the Credit Analytics server from the client, formats them, and sends them
 *  	to the Credit Analytics Stub.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalyticsProxy {
	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblBidPriceStart = 70.;
		double dblBidPriceFinish = 130.;
		double dblBidPriceIncrement = 10.;
		java.lang.String strISIN = "US004403AA94";
		java.lang.String strCommand = "BondAnalFromPrice";

		if (0 < astrArgs.length) {
			strCommand = astrArgs[0];

			if (1 < astrArgs.length) {
				strISIN = astrArgs[1];

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

		for (double dblBidPrice = dblBidPriceStart; dblBidPrice <= dblBidPriceFinish; dblBidPrice +=
			dblBidPriceIncrement) {
			java.net.Socket socket = org.drip.param.config.ConfigLoader.ConnectToAnalServer
				("c:\\Lakshmi\\BondAnal\\Config.xml");

			java.io.ObjectOutputStream out = new java.io.ObjectOutputStream (socket.getOutputStream());

			java.io.ObjectInputStream in = new java.io.ObjectInputStream (socket.getInputStream());

			out.writeObject (strCommand + ";Bond=" + strISIN + ";PriceBid=" + dblBidPrice + ";PriceAsk=" +
				(dblBidPrice + 1.));

			out.flush();

			System.out.println ("Sent: " + strCommand);

			java.lang.String strStatus = (java.lang.String) in.readObject();

			System.out.println ("Status: " + strStatus);

			if ("InProgress".equalsIgnoreCase (strStatus)) {
				java.lang.String strResponse = (java.lang.String) in.readObject();

				System.out.println ("Response: " + strResponse);

				if (null != strResponse && !strResponse.isEmpty()) {
					java.lang.String[] astrBMRV = org.drip.analytics.support.GenericUtil.Split (strResponse,
						"?");

					if (null == astrBMRV || 2 != astrBMRV.length) {
						for (java.lang.String strBMRV : astrBMRV) {
							if (null != strBMRV && !strBMRV.isEmpty()) {
								org.drip.analytics.output.BondRVMeasures bmrv = new
									org.drip.analytics.output.BondRVMeasures (strBMRV.getBytes());

								if (null != bmrv) System.out.println (bmrv.toMap (""));
							}
						}
					}
				}
			}
		}
	}
}
