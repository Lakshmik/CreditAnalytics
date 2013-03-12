
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
 * This class contains the suite of helper functions for creating FX Forward Contract.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FXForwardBuilder {

	/**
	 * Creates the FXForward object from Currency Pair, effective date, and maturity.
	 * 
	 * @param ccyPair Currency Pair
	 * @param dtEffective JulianDate effective
	 * @param dtMaturity Maturity
	 * 
	 * @return FXForward object
	 */

	public static final org.drip.product.definition.FXForward CreateFXForward (
		final org.drip.product.params.CurrencyPair ccyPair,
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity)
	{
		if (null == dtEffective || null == dtMaturity) {
			System.out.println ("Null dates into FXForwardBuilder.CreateFXForward!");

			return null;
		}

		try {
			org.drip.product.definition.FXForward fxfwd = new org.drip.product.fx.FXForwardContract (ccyPair,
				dtEffective, dtMaturity);

			fxfwd.setPrimaryCode ("FXFWD." + dtMaturity + "." + ccyPair.getCode());

			return fxfwd;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the FXForward object from Currency Pair, effective date, and tenor.
	 * 
	 * @param ccyPair Currency Pair
	 * @param dtEffective JulianDate effective
	 * @param strTenor String tenor
	 * 
	 * @return FXForward object
	 */

	public static final org.drip.product.definition.FXForward CreateFXForward (
		final org.drip.product.params.CurrencyPair ccyPair,
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty()) {
			System.out.println ("Null dates into FXForwardBuilder.CreateFXForward!");

			return null;
		}

		try {
			org.drip.product.definition.FXForward fxfwd = new org.drip.product.fx.FXForwardContract (ccyPair,
				dtEffective, dtEffective.addTenor (strTenor));

			fxfwd.setPrimaryCode ("FXFWD." + strTenor + "." + ccyPair.getCode());

			return fxfwd;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a FXForward Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return FXForward Instance
	 */

	public static final org.drip.product.definition.FXForward FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.fx.FXForwardContract (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
