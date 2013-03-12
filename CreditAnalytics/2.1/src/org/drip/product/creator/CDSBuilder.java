
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
 * This class contains the suite of helper functions for creating credit default swaps. It creates
 * 		standard EU, NA, ASIA contracts, CDS with amortization schedules, and CDS from product codes/tenors.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSBuilder {
	/**
	 * Creates the credit default swap from the effective/maturity dates, coupon, IR curve name, and
	 * 	component credit valuation parameters.
	 * 
	 * @param dtEffective JulianDate effective
	 * @param dtMaturity JulianDate maturity
	 * @param dblCoupon Coupon
	 * @param strIR IR Curve name
	 * @param crValParams CompCRValParams
	 * @param strCalendar Optional Holiday Calendar for accrual calculation
	 * @param bAdjustDates Roll using the FWD mode for the period end dates and the pay dates
	 * 
	 * @return CreditDefaultSwap product
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateCDS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final java.lang.String strIR,
		final org.drip.product.params.CreditSetting crValParams,
		final java.lang.String strCalendar,
		final boolean bAdjustDates)
	{
		if (null == dtEffective || null == dtMaturity || null == strIR || strIR.isEmpty() || null ==
			crValParams || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid CDS ctr params!");

			return null;
		}

		try {
			org.drip.analytics.daycount.DateAdjustParams dap = null;

			if (bAdjustDates)
				dap = new org.drip.analytics.daycount.DateAdjustParams
					(org.drip.analytics.daycount.Convention.DR_FOLL, strCalendar);

			org.drip.product.definition.CreditDefaultSwap cds = new org.drip.product.credit.CDSComponent
				(dtEffective.getJulian(), dtMaturity.getJulian(), dblCoupon, 4, "Act/360", "Act/360", "",
					false, null, null, null, dap, dap, dap, dap, null, null, 100., strIR, crValParams,
						strCalendar);

			cds.setPrimaryCode ("CDS." + dtMaturity.toString() + "." + crValParams._strCC);

			return cds;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the credit default swap from the effective/maturity dates, coupon, IR curve name, and
	 * 	credit curve.
	 * 
	 * @param dtEffective JulianDate effective
	 * @param dtMaturity JulianDate maturity
	 * @param dblCoupon Coupon
	 * @param strIR IR Curve name
	 * @param dblRecovery Recovery Rate
	 * @param strCC Credit curve name
	 * @param strCalendar Optional Holiday Calendar for accrual calculation
	 * @param bAdjustDates Roll using the FWD mode for the period end dates and the pay dates
	 * 
	 * @return CreditDefaultSwap product
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateCDS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final java.lang.String strIR,
		final double dblRecovery,
		final java.lang.String strCC,
		final java.lang.String strCalendar,
		final boolean bAdjustDates)
	{
		if (null == dtEffective || null == dtMaturity || null == strIR || strIR.isEmpty() || null == strCC ||
			strCC.isEmpty() || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid CDS ctr params!");

			return null;
		}

		org.drip.product.params.CreditSetting crValParams = new
			org.drip.product.params.CreditSetting (30, dblRecovery, true, strCC, true);

		if (!crValParams.validate()) {
			System.out.println ("Invalid validation of crValParams!");

			return null;
		}

		return CreateCDS (dtEffective, dtMaturity, dblCoupon, strIR, crValParams, strCalendar, bAdjustDates);
	}

	/**
	 * Creates an SNAC style CDS contract with full first stub
	 * 
	 * @param dtEffective CDS Effective date
	 * @param strTenor CDS Tenor
	 * @param dblCoupon SNAC strike coupon
	 * @param strCC Credit Curve name
	 * 
	 * @return CDS instance object
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateSNAC (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strCC)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty()) return null;

		org.drip.analytics.date.JulianDate dtFirstCoupon = dtEffective.getFirstEDFStartDate (3);

		if (null == dtFirstCoupon) return null;

		org.drip.product.definition.CreditDefaultSwap cds = CreateCDS (dtFirstCoupon.subtractTenor ("3M"),
			dtFirstCoupon.addTenor (strTenor), dblCoupon, "USD", 0.40, strCC, "USD", true);

		cds.setPrimaryCode ("CDS." + strTenor + "." + strCC);

		return cds;
	}

	/**
	 * Creates an Standard EU CDS contract with full first stub
	 * 
	 * @param dtEffective CDS Effective date
	 * @param strTenor CDS Tenor
	 * @param dblCoupon Strike coupon
	 * @param strCC Credit Curve name
	 * 
	 * @return CDS instance object
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateSEUC (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strCC)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty()) return null;

		org.drip.analytics.date.JulianDate dtFirstCoupon = dtEffective.getFirstEDFStartDate (3);

		if (null == dtFirstCoupon) return null;

		org.drip.product.definition.CreditDefaultSwap cds = CreateCDS (dtFirstCoupon.subtractTenor ("3M"),
			dtFirstCoupon.addTenor (strTenor), dblCoupon, "EUR", 0.40, strCC, "EUR", true);

		cds.setPrimaryCode ("CDS." + strTenor + "." + strCC);

		return cds;
	}

	/**
	 * Creates an Standard Asia Pacific CDS contract with full first stub
	 * 
	 * @param dtEffective CDS Effective date
	 * @param strTenor CDS Tenor
	 * @param dblCoupon Strike coupon
	 * @param strCC Credit Curve name
	 * 
	 * @return CDS instance object
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateSAPC (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strCC)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty()) return null;

		org.drip.analytics.date.JulianDate dtFirstCoupon = dtEffective.getFirstEDFStartDate (3);

		if (null == dtFirstCoupon) return null;

		org.drip.product.definition.CreditDefaultSwap cds = CreateCDS (dtFirstCoupon.subtractTenor ("3M"),
			dtFirstCoupon.addTenor (strTenor), dblCoupon, "HKD", 0.40, strCC, "HKD", true);

		cds.setPrimaryCode ("CDS." + strTenor + "." + strCC);

		return cds;
	}

	/**
	 * Creates an Standard Emerging Market CDS contract with full first stub
	 * 
	 * @param dtEffective CDS Effective date
	 * @param strTenor CDS Tenor
	 * @param dblCoupon Strike coupon
	 * @param strCC Credit Curve name
	 * 
	 * @return CDS instance object
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateSTEM (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strCC,
		final java.lang.String strLocation)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty()) return null;

		org.drip.analytics.date.JulianDate dtFirstCoupon = dtEffective.getFirstEDFStartDate (3);

		if (null == dtFirstCoupon) return null;

		org.drip.product.definition.CreditDefaultSwap cds = CreateCDS (dtFirstCoupon.subtractTenor ("3M"),
			dtFirstCoupon.addTenor (strTenor), dblCoupon, strLocation, 0.25, strCC, strLocation, true);

		cds.setPrimaryCode ("CDS." + strTenor + "." + strCC);

		return cds;
	}

	/**
	 * Creates the credit default swap from the effective date, tenor, coupon, IR curve name, and
	 * 	component credit valuation parameters.
	 * 
	 * @param dtEffective JulianDate effective
	 * @param strTenor String tenor
	 * @param dblCoupon Coupon
	 * @param strIR IR Curve name
	 * @param crValParams CompCRValParams
	 * @param strCalendar Optional Holiday Calendar for accrual calculation
	 * 
	 * @return CreditDefaultSwap product
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateCDS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strIR,
		final org.drip.product.params.CreditSetting crValParams,
		final java.lang.String strCalendar)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty() || null == strIR || strIR.isEmpty()
			|| null == crValParams || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid CDS ctr params!");

			return null;
		}

		try {
			org.drip.product.definition.CreditDefaultSwap cds = new org.drip.product.credit.CDSComponent
				(dtEffective.getJulian(), dtEffective.addTenor (strTenor).getJulian(), dblCoupon, 4,
					"30/360", "30/360", "", true, null, null, null, null, null, null, null, null, null, 100.,
						strIR, crValParams, strCalendar);

			cds.setPrimaryCode ("CDS." + strTenor + "." + crValParams._strCC);

			return cds;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the credit default swap from the effective/maturity dates, coupon, IR curve name, and
	 * 	credit curve.
	 * 
	 * @param dtEffective JulianDate effective
	 * @param strTenor String tenor
	 * @param dblCoupon Coupon
	 * @param strIR IR Curve name
	 * @param strCC Credit curve name
	 * @param strCalendar Optional Holiday Calendar for accrual calculation
	 * 
	 * @return CreditDefaultSwap product
	 */

	public static final org.drip.product.definition.CreditDefaultSwap CreateCDS (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final double dblCoupon,
		final java.lang.String strIR,
		final java.lang.String strCC,
		final java.lang.String strCalendar)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty() || null == strIR || strIR.isEmpty()
			|| null == strCC || strCC.isEmpty() || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)) {
			System.out.println ("Invalid CDS ctr params!");

			return null;
		}

		org.drip.product.params.CreditSetting crValParams = new org.drip.product.params.CreditSetting (30,
			java.lang.Double.NaN, true, strCC, true);

		if (!crValParams.validate()) {
			System.out.println ("Invalid validation of crValParams!");

			return null;
		}

		return CreateCDS (dtEffective, strTenor, dblCoupon, strIR, crValParams, strCalendar);
	}

	/**
	 * Create a CDS Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return CDS Instance
	 */

	public static final org.drip.product.definition.CreditDefaultSwap FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.credit.CDSComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
