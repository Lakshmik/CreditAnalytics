
package org.drip.param.definition;

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
 * This abstract class extends the BaketMarketParamsRef for a specific scenario. It provides acess to maps
 * 	holding named discount curves, named credit curves, named treasury quote, named component quote, and
 * 	fixings object.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class BasketMarketParams extends org.drip.service.stream.Serializer {

	/**
	 * Adds a named discount curve
	 * 
	 * @param strName Name
	 * @param dc Discount Curve
	 * 
	 * @return Success (true) or Failure (false)
	 */

	public abstract boolean addDC (
		final java.lang.String strName,
		final org.drip.analytics.definition.DiscountCurve dc);

	/**
	 * Adds a named credit curve
	 * 
	 * @param strName Name
	 * @param cc Credit Curve
	 * 
	 * @return Success (true) or Failure (false)
	 */

	public abstract boolean addCC (
		final java.lang.String strName,
		final org.drip.analytics.definition.CreditCurve cc);

	/**
	 * Retrieves a named discount curve
	 * 
	 * @param strName Name
	 * 
	 * @return Discount Curve
	 */

	public abstract org.drip.analytics.definition.DiscountCurve getDC (
		final java.lang.String strName);

	/**
	 * Retrieves a named credit curve
	 * 
	 * @param strName Name
	 * 
	 * @return Credit Curve
	 */

	public abstract org.drip.analytics.definition.CreditCurve getCC (
		final java.lang.String strName);

	/**
	 * Retrieves the basket component's market parameters
	 * 
	 * @param compRef The component's ComponentMarketParamRef
	 * 
	 * @return The ComponentMarketParam object
	 */

	public abstract org.drip.param.definition.ComponentMarketParams getComponentMarketParams (
		final org.drip.product.definition.ComponentMarketParamRef compRef);
}
