
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
 * This class implements the various ways of constructing, de-serializing, and building the Basket Market
 * 		Parameters.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasketMarketParamsBuilder {

	/**
	 * Constructs the BasketMarketParams object from the map of discount curve, the map of credit curve, and
	 * 	a double map of date/rate index and fixings.
	 * 
	 * @param mapDC Map of discount curve
	 * @param mapCC Map of Credit curve
	 * @param mapCQComp Map of component quotes
	 * @param mmFixings Double map of date/rate index and fixings
	 */

	public static final org.drip.param.definition.BasketMarketParams CreateBasketMarketParams (
		final java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> mapDC,
		final java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> mapCC,
		final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mapCQComp,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings)
	{
		try {
			return new org.drip.param.market.BasketMarketParamSet (mapDC, mapCC, mapCQComp, mmFixings);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Constructs the empty BasketMarketParams object.
	 * 
	 * @return Instance of the Basket Market Params interface
	 */

	public static final org.drip.param.definition.BasketMarketParams CreateBasketMarketParams()
	{
		try {
			return new org.drip.param.market.BasketMarketParamSet (null, null, null, null);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a Basket Market Parameter Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return Basket Market Parameter Instance
	 */

	public static final org.drip.param.definition.BasketMarketParams FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.param.market.BasketMarketParamSet (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
