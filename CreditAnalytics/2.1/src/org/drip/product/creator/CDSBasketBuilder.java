
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
 * This class contains the suite of helper functions for creating the CDS Basket Product from different
 * 		kinds of inputs.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSBasketBuilder {

	/**
	 * Creates the named CDX from effective, maturity, coupon, IR curve name, credit curve name set, and
	 * 		their weights.
	 * 
	 * @param dtEffective JulianDate Effective
	 * @param dtMaturity JulianDate Maturity
	 * @param dblCoupon Coupon
	 * @param strIR IR curve name
	 * @param astrCC credit curve name
	 * @param adblWeight Credit Component Weights
	 * @param strName CDX name
	 * 
	 * @return BasketDefaultSwap
	 */

	public static final org.drip.product.definition.BasketProduct MakeCDX (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final java.lang.String strIR,
		final java.lang.String[] astrCC,
		final double[] adblWeight,
		final java.lang.String strName)
	{
		if (null == dtEffective || null == dtMaturity || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)
			|| null == strIR || strIR.isEmpty() || null == strName || strName.isEmpty() || null == astrCC ||
				0 == astrCC.length || null == adblWeight || 0 == adblWeight.length || adblWeight.length !=
					astrCC.length)
			return null;

		org.drip.product.definition.CreditDefaultSwap aCDS[] = new
			org.drip.product.definition.CreditDefaultSwap[astrCC.length];

		for (int i = 0; i < astrCC.length; ++i) {
			try {
				aCDS[i] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtMaturity, dblCoupon,
					strIR, 0.40, astrCC[i], strIR, true);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		try {
			return new org.drip.product.credit.CDSBasket (dtEffective, dtMaturity, dblCoupon, aCDS,
				adblWeight, strName);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the named CDX from effective, maturity, coupon, IR curve name, credit curve name set.
	 * 
	 * @param dtEffective JulianDate Effective
	 * @param dtMaturity JulianDate Maturity
	 * @param dblCoupon Coupon
	 * @param strIR IR curve name
	 * @param astrCC credit curve name
	 * @param strName CDX name
	 * 
	 * @return BasketDefaultSwap
	 */

	public static final org.drip.product.definition.BasketProduct MakeCDX (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final java.lang.String strIR,
		final java.lang.String[] astrCC,
		final java.lang.String strName)
	{
		if (null == dtEffective || null == dtMaturity || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)
			|| null == strIR || strIR.isEmpty() || null == strName || strName.isEmpty() || null == astrCC ||
				0 == astrCC.length)
			return null;

		org.drip.product.definition.CreditDefaultSwap aCDS[] = new
			org.drip.product.definition.CreditDefaultSwap[astrCC.length];

		for (int i = 0; i < astrCC.length; ++i) {
			try {
				aCDS[i] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtMaturity, dblCoupon,
					strIR, 0.40, astrCC[i], strIR, true);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		try {
			double adblWeight[] = new double[aCDS.length];

			for (int i = 0; i < aCDS.length; ++i)
				adblWeight[i] = 1.;

			return new org.drip.product.credit.CDSBasket (dtEffective, dtMaturity, dblCoupon, aCDS,
				adblWeight, strName);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the basket default swap from effective, maturity, and an array of the credit components.
	 * 
	 * @param dtEffective JulianDate Effective
	 * @param dtMaturity JulianDate Maturity
	 * @param aComp Array of the credit components
	 * 
	 * @return BasketDefaultSwap object
	 */

	public static final org.drip.product.definition.BasketProduct MakeBasketDefaultSwap (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final org.drip.product.definition.Component aComp[])
	{
		try {
			double adblWeight[] = new double[aComp.length];

			for (int i = 0; i < aComp.length; ++i)
				adblWeight[i] = 1.;

			return new org.drip.product.credit.CDSBasket (dtEffective, dtMaturity, 0.0001, aComp, adblWeight, "");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a CDSBasket Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return CDSBasket Instance
	 */

	public static final org.drip.product.definition.BasketProduct FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.credit.CDSBasket (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
