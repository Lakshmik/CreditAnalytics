
package org.drip.service.sample;

/*
 * Generic imports
 */

import java.util.*;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.product.definition.*;

/*
 * Credit Analytics API imports
 */

import org.drip.service.api.CreditAnalytics;
import org.drip.service.env.StandardCDXManager;

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
 * Demo of the CDS basket API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSBasketAPI {

	/**
	 * Sample demonstrating the creation/usage of the CDX API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BasketCDSAPISample()
	{
		JulianDate dtToday = JulianDate.CreateFromYMD (2013, JulianDate.MAY, 10);

		/*
		 * Construct the CDX.NA.IG 5Y Series 17 index by name and series
		 */

		BasketProduct bpCDX = CreditAnalytics.MakeCDX ("CDX.NA.IG", 17, "5Y");

		/*
		 * Construct the on-the-run CDX.NA.IG 5Y Series index
		 */

		BasketProduct bpCDXOTR = CreditAnalytics.MakeCDX ("CDX.NA.IG", dtToday, "5Y");

		/*
		 * List of all the built-in CDX names
		 */

		Set<String> setstrCDXNames = StandardCDXManager.GetCDXNames();

		/*
		 * Descriptions of all the built-in CDX names
		 */

		Map<String, String> mapCDXDescr = StandardCDXManager.GetCDXDescriptions();

		/*
		 * Construct the on-the run CDX.EM 5Y corresponding to T - 1Y
		 */

		BasketProduct bpPresetOTR = StandardCDXManager.GetOnTheRun ("CDX.EM", dtToday.subtractTenor ("1Y"), "5Y");

		/*
		 * Construct the on-the run ITRAXX.ENERGY 5Y corresponding to T - 7Y
		 */

		BasketProduct bpPreLoadedOTR = StandardCDXManager.GetOnTheRun ("ITRAXX.ENERGY", dtToday.subtractTenor
			("7Y"), "5Y");

		/*
		 * Retrieve the full set of date/index series set for ITRAXX.ENERGY
		 */

		Map<JulianDate, Integer> mapCDXSeries = StandardCDXManager.GetCDXSeriesMap ("ITRAXX.ENERGY");

		System.out.println (bpCDX.getName() + ": " + bpCDX.getEffectiveDate() + "=>" + bpCDX.getMaturityDate());

		System.out.println (bpCDXOTR.getName() + ": " + bpCDXOTR.getEffectiveDate() + "=>" + bpCDXOTR.getMaturityDate());

		int i = 0;

		for (String strCDX : setstrCDXNames)
			System.out.println ("CDX[" + i++ + "]: " + strCDX);

		for (Map.Entry<String, String> meCDXDescr : mapCDXDescr.entrySet())
			System.out.println ("[" + meCDXDescr.getKey() + "]: " + meCDXDescr.getValue());

		System.out.println (bpPresetOTR.getName() + ": " + bpPresetOTR.getEffectiveDate() + "=>" + bpPresetOTR.getMaturityDate());

		System.out.println (bpPreLoadedOTR.getName() + ": " + bpPreLoadedOTR.getEffectiveDate() + "=>" + bpPreLoadedOTR.getMaturityDate());

		for (Map.Entry<JulianDate, Integer> me : mapCDXSeries.entrySet())
			System.out.println ("ITRAXX.ENERGY[" + me.getValue() + "]: " + me.getKey());
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		BasketCDSAPISample();
	}
}
