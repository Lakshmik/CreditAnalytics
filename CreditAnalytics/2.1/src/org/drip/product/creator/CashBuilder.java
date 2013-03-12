
package org.drip.product.creator;

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
 * This class contains the suite of helper functions for creating the simple Rates Cash Product.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CashBuilder {

	/**
	 * Creates a cash product from effective date, tenor, IR curve name, and code.
	 * 
	 * @param dtEffective JulianDate specifying the effective date
	 * @param strTenor String tenor
	 * @param strIR IR curve name
	 * @param strCode Product Code
	 * 
	 * @return Cash Object
	 */

	public static final org.drip.product.definition.RatesComponent CreateCash (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final java.lang.String strIR,
		final java.lang.String strCode)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty() || null == strIR ||
			strIR.isEmpty()) {
			System.out.println ("Invalid CashBuilder.CreateCash params!");

			return null;
		}

		try {
			org.drip.product.definition.RatesComponent cash = new org.drip.product.rates.CashComponent (dtEffective,
				dtEffective.addTenor (strTenor), strIR);

			cash.setPrimaryCode (strCode + "." + strTenor + "." + strIR);

			return cash;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a cash product from effective and maturity dates, and the IR cuve
	 * 
	 * @param dtEffective Effective date
	 * @param dtMaturity Maturity
	 * @param strIR IR Curve name
	 * 
	 * @return Cash product
	 */

	public static final org.drip.product.definition.RatesComponent CreateCash (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final java.lang.String strIR)
	{
		if (null == dtMaturity) {
			System.out.println ("Invalid CashBuilder.CreateCash params!");

			return null;
		}

		try {
			org.drip.product.definition.RatesComponent cash = new org.drip.product.rates.CashComponent (dtEffective,
				dtMaturity, strIR);

			cash.setPrimaryCode ("CD." + dtMaturity + "." + strIR);

			return cash;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the cash product from the effective date, tenor, and the IR curve name.
	 * 
	 * @param dtEffective JulianDate Effective
	 * @param strTenor String tenor
	 * @param strIR IR Curve Name
	 * 
	 * @return Cash object
	 */

	public static final org.drip.product.definition.RatesComponent CreateCash (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final java.lang.String strIR)
	{
		return CreateCash (dtEffective, strTenor, strIR, "CD");
	}

	/**
	 * Create a Cash Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return Cash Instance
	 */

	public static final org.drip.product.definition.RatesComponent FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.rates.CashComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
