
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
 * This class contains the suite of helper functions for creating the Interest Rate Swap Product from
 * 		different kinds of inputs.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IRSBuilder {

	/**
	 * Creates an IRS product from effective/maturity dates, coupon, and IR curve name/rate index
	 * 
	 * @param dtEffective JulianDate effective
	 * @param dtMaturity JulianDate maturity
	 * @param dblCoupon Double coupon
	 * @param strIR IR curve name
	 * @param strFloatingRateIndex Floater Index
	 * @param strCalendar Optional Holiday Calendar for coupon accrual
	 * 
	 * @return IRS product
	 */

	public static final org.drip.product.definition.RatesComponent CreateIRS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final java.lang.String strIR,
		final java.lang.String strFloatingRateIndex,
		final java.lang.String strCalendar)
	{
		if (null == dtEffective || null == dtMaturity || null == strIR || strIR.isEmpty() ||
			!org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid IRS ctr params!");

			return null;
		}

		try {
			org.drip.product.definition.RatesComponent irs = new org.drip.product.rates.IRSComponent
				(dtEffective.getJulian(), dtMaturity.getJulian(), dblCoupon, 2, "30/360", "30/360",
					strFloatingRateIndex, false, null, null, null, null, null, null, null, null, null, 100.,
						strIR, strCalendar);

			irs.setPrimaryCode ("IRS." + dtMaturity.toString() + "." + strIR);

			return irs;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an IRS product from effective date, tenor, coupon, and IR curve name/rate index
	 * 
	 * @param dtEffective JulianDate effective
	 * @param strTenor String tenor
	 * @param dblCoupon Double coupon
	 * @param strIR IR curve name
	 * @param strFloatingRateIndex Floater Index
	 * @param strCalendar Optional Holiday Calendar for coupon accrual
	 * 
	 * @return IRS product
	 */

	public static final org.drip.product.definition.RatesComponent CreateIRS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strIR,
		final java.lang.String strFloatingRateIndex,
		final java.lang.String strCalendar)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty() || null == strIR || strIR.isEmpty()
			|| !org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid IRS ctr params!");

			return null;
		}

		try {
			org.drip.product.definition.RatesComponent irs = new org.drip.product.rates.IRSComponent
				(dtEffective.getJulian(), dtEffective.addTenor (strTenor).getJulian(), dblCoupon, 2,
					"30/360", "30/360", strFloatingRateIndex, false, null, null, null, null, null, null,
						null, null, null, 100., strIR, strCalendar);

			irs.setPrimaryCode ("IRS." + strTenor + "." + strIR);

			return irs;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a IRS Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return IRS Instance
	 */

	public static final org.drip.product.definition.RatesComponent FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.rates.IRSComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
