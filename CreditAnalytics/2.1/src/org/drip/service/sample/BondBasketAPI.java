
package org.drip.service.sample;

/*
 * Generic Imports
 */

import java.util.Map;

/*
 * Credit Product Imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.param.definition.*;
import org.drip.param.pricer.PricerParams;
import org.drip.param.valuation.ValuationParams;
import org.drip.product.definition.*;

/*
 * Credit Analytics API Imports
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.service.api.CreditAnalytics;

/*
 * DRIP Math Support
 */

import org.drip.math.common.FormatUtil;

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
 * Demo of the bond basket API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondBasketAPI {
	/**
	 * Sample demonstrating the creation/usage of the bond basket API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BasketBondAPISample()
		throws Exception
	{
		JulianDate dtToday = JulianDate.Today();

		String[] astrBasketISIN = new String[] {"US78490FVJ55", "US78490FWD76", "US78490FVL02", "US78442FAZ18", "US78490FTL30"};
		double[] adblBasketWeights = new double[] {1., 2., 3., 4., 5.};

		/*
		 * Creates the basket from the ISIN and the weight array
		 */

		BasketProduct bb = CreditAnalytics.MakeBondBasket ("SLMA_ETF", astrBasketISIN, adblBasketWeights, dtToday, 100.);

		/*
		 * Create the basket market parameters and add the named discount curve and the credit curves to it.
		 */

		BasketMarketParams bmp = BasketMarketParamsBuilder.CreateBasketMarketParams();

		bmp.addDC ("USD", DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.04));

		bmp.addDC ("USDTSY", DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.03));

		ValuationParams valParams = ValuationParams.CreateValParams (dtToday, 0, "USD", Convention.DR_ACTUAL);

		PricerParams pricerParams = new PricerParams (7, null, false, PricerParams.PERIOD_DISCRETIZATION_FULL_COUPON);

		/*
		 * Generate the bond basket measures from the valuation, the pricer, and the market parameters
		 */

		Map<String, Double> mapResult = bb.value (valParams, pricerParams, bmp, null);

		System.out.println ("Fair Clean Price: " + FormatUtil.FormatDouble (mapResult.get ("FairCleanPV"), 2, 3, 100.));

		System.out.println ("Fair Yield: " + FormatUtil.FormatDouble (mapResult.get ("Yield"), 2, 3, 100.));

		System.out.println ("Fair GSpread: " + FormatUtil.FormatDouble (mapResult.get ("FairGSpread"), 1, 3, 100.));

		System.out.println ("Fair ZSpread: " + FormatUtil.FormatDouble (mapResult.get ("FairZSpread"), 1, 3, 100.));

		System.out.println ("Fair ISpread: " + FormatUtil.FormatDouble (mapResult.get ("FairISpread"), 1, 3, 100.));

		System.out.println ("Fair DV01: " + mapResult.get ("FairDV01"));

		System.out.println ("Accrued: " + mapResult.get ("Accrued"));
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		if (!CreditAnalytics.Init (strConfig)) {
			System.out.println ("Cannot fully init FI!");

			System.exit (301);
		}

		BasketBondAPISample();
	}
}
