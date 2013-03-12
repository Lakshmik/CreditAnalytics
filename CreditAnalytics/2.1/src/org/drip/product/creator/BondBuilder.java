
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
 * This class contains the suite of helper functions for creating user defined bonds, optionally with custom
 * 	cash flows and embedded option schedules (European or American).
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondBuilder {
	/**
	 * Custom Bond Type Simple Fixed
	 */

	public static final int BOND_TYPE_SIMPLE_FIXED = 0;

	/**
	 * Custom Bond Type Simple Floater
	 */

	public static final int BOND_TYPE_SIMPLE_FLOATER = 1;

	/**
	 * Custom Bond Type Simple From Cash flows
	 */

	public static final int BOND_TYPE_SIMPLE_FROM_CF = 2;

	private static final boolean s_bLog = false;

	/**
	 * Creates the full generic bond object from the complete set of parameters
	 * 
	 * @param tsyParams Bond Treasury Parameters
	 * @param idParams Bond Identifier Parameters
	 * @param cpnParams Bond Coupon Parameters
	 * @param ccyParams Bond Currency Parameters
	 * @param fltParams Bond Floater Parameters
	 * @param irValParams Bond IR Valuation Parameters
	 * @param crValParams Bond Credit Valuation Parameters
	 * @param cfteParams Bond Cash-flow Termination Event Parameters
	 * @param periodParams Bond Period Generation Parameters
	 * @param notlParams Bond Notional Parameters
	 * 
	 * @return The Bond object
	 */

	public static final org.drip.product.credit.BondComponent CreateBondFromParams (
		final org.drip.product.params.TreasuryBenchmark tsyParams,
		final org.drip.product.params.IdentifierSet idParams,
		final org.drip.product.params.CouponSetting cpnParams,
		final org.drip.product.params.CurrencySet ccyParams,
		final org.drip.product.params.FloaterSetting fltParams,
		final org.drip.product.params.QuoteConvention mktConv,
		final org.drip.product.params.RatesSetting irValParams,
		final org.drip.product.params.CreditSetting crValParams,
		final org.drip.product.params.TerminationSetting cfteParams,
		final org.drip.product.params.PeriodSet periodParams,
		final org.drip.product.params.NotionalSetting notlParams)
	{
		if (null == tsyParams || !tsyParams.validate() || null == idParams || !idParams.validate() || null ==
			cpnParams || !cpnParams.validate() || null == ccyParams || !ccyParams.validate() || (null !=
				fltParams && !fltParams.validate()) || null == mktConv || !mktConv.validate() || null ==
					irValParams || !irValParams.validate() || null == crValParams || !crValParams.validate()
						|| null == cfteParams || !cfteParams.validate() || null == periodParams ||
							!periodParams.validate() || null == notlParams || !notlParams.validate())
			return null;

		org.drip.product.credit.BondComponent bond = new org.drip.product.credit.BondComponent();

		bond.setTreasuryBenchmark (tsyParams);

		bond.setIdentifierSet (idParams);

		bond.setCouponSetting (cpnParams);

		bond.setCurrencySet (ccyParams);

		bond.setFloaterSetting (fltParams);

		bond.setMarketConvention (mktConv);

		bond.setRatesSetting (irValParams);

		bond.setCreditSetting (crValParams);

		bond.setTerminationSetting (cfteParams);

		bond.setPeriodSet (periodParams);

		bond.setNotionalSetting (notlParams);

		return bond;
	}

	/**
	 * Creates a simple fixed bond from parameters
	 * 
	 * @param strName Bond Name
	 * @param strCurrency Bond Currency
	 * @param dblCoupon Bond Fixed Coupon
	 * @param iFreq Coupon Frequency
	 * @param strDayCount Bond Coupon Day count convention
	 * @param dtEffective Effective Date
	 * @param dtMaturity Maturity Date
	 * @param fsPrincipalOutstanding Outstanding Principal schedule
	 * @param fsCoupon Bond Coupon Schedule
	 * 
	 * @return The Bond Object
	 */

	public static final org.drip.product.credit.BondComponent CreateSimpleFixed (
		final java.lang.String strName,
		final java.lang.String strCurrency,
		final double dblCoupon,
		final int iFreq,
		final java.lang.String strDayCount,
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final org.drip.product.params.FactorSchedule fsPrincipalOutstanding,
		final org.drip.product.params.FactorSchedule fsCoupon)
	{
		if (null == strName || strName.isEmpty() || null == strCurrency || strCurrency.isEmpty() || null ==
			dtEffective || null == dtMaturity || !org.drip.math.common.NumberUtil.IsValid (dblCoupon))
			return null;

		return BondBuilder.CreateBondFromParams (new org.drip.product.params.TreasuryBenchmark (null,
			strCurrency + "TSY", strCurrency + "EDSF"), new org.drip.product.params.IdentifierSet
				(strName, strName, strName, strCurrency + "TSY"), new
					org.drip.product.params.CouponSetting (fsCoupon, "", dblCoupon, java.lang.Double.NaN,
						java.lang.Double.NaN), new org.drip.product.params.CurrencySet (strCurrency +
							"TSY", strCurrency + "TSY", strCurrency + "TSY"), null, new
								org.drip.product.params.QuoteConvention (null, "",
									dtEffective.getJulian(), 100., 3, strCurrency,
										org.drip.analytics.daycount.Convention.DR_MOD_FOLL), new
											org.drip.product.params.RatesSetting
												(strCurrency, strCurrency, strCurrency, strCurrency), new
													org.drip.product.params.CreditSetting
														(30, java.lang.Double.NaN, true, "", true), new
															org.drip.product.params.TerminationSetting
																(false, false, false), new
																	org.drip.product.params.PeriodGenerator
																		(dtMaturity.getJulian(),
																			dtEffective.getJulian(),
																				java.lang.Double.NaN,
																					java.lang.Double.NaN,
																						dtEffective.getJulian(),
				iFreq, strDayCount, strDayCount, null, null, null, null, null, null, null, null, "", false,
					strCurrency), new org.drip.product.params.NotionalSetting (fsPrincipalOutstanding,
						100., org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_START, false));
	}

	/**
	 * Creates a simple floating rate bond
	 * 
	 * @param strName Bond Name
	 * @param strCurrency Bond Currency
	 * @param strRateIndex Floating Rate Index
	 * @param dblSpread Bond Floater Spread
	 * @param iFreq Coupon Frequency
	 * @param strDayCount Coupon Day Count Convention
	 * @param dtEffective Effective date
	 * @param dtMaturity Maturity Date
	 * @param fsPrincipalOutstanding Outstanding Principal Schedule
	 * @param fsCoupon Coupon Schedule
	 * 
	 * @return The Bond object
	 */

	public static final org.drip.product.credit.BondComponent CreateSimpleFloater (
		final java.lang.String strName,
		final java.lang.String strCurrency,
		final java.lang.String strRateIndex,
		final double dblSpread,
		final int iFreq,
		final java.lang.String strDayCount,
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final org.drip.product.params.FactorSchedule fsPrincipalOutstanding,
		final org.drip.product.params.FactorSchedule fsCoupon)
	{
		if (null == strName || strName.isEmpty() || null == strCurrency || strCurrency.isEmpty() || null ==
			dtEffective || null == dtMaturity || !org.drip.math.common.NumberUtil.IsValid (dblSpread))
			return null;

		return BondBuilder.CreateBondFromParams (new org.drip.product.params.TreasuryBenchmark (null,
			strCurrency + "TSY", strCurrency + "EDSF"), new org.drip.product.params.IdentifierSet (strName,
				strName, strName, strCurrency + "TSY"), new org.drip.product.params.CouponSetting (fsCoupon,
					"", dblSpread, java.lang.Double.NaN, java.lang.Double.NaN), new
						org.drip.product.params.CurrencySet (strCurrency + "TSY", strCurrency + "TSY",
							strCurrency + "TSY"), new org.drip.product.params.FloaterSetting (strRateIndex,
								"", dblSpread, java.lang.Double.NaN), new
									org.drip.product.params.QuoteConvention (null, "",
										dtEffective.getJulian(), 100., 3, strCurrency,
											org.drip.analytics.daycount.Convention.DR_MOD_FOLL), new
												org.drip.product.params.RatesSetting (strCurrency,
													strCurrency, strCurrency, strCurrency), new
														org.drip.product.params.CreditSetting (30,
															java.lang.Double.NaN, true, "", true), new
																org.drip.product.params.TerminationSetting
																	(false, false, false), new
																		org.drip.product.params.PeriodGenerator
			(dtMaturity.getJulian(), dtEffective.getJulian(), java.lang.Double.NaN, java.lang.Double.NaN,
				dtEffective.getJulian(), iFreq, strDayCount, strDayCount, null, null, null, null, null, null,
					null, null, "", false, strCurrency), new org.drip.product.params.NotionalSetting
						(fsPrincipalOutstanding, 100.,
							org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_START, false));
	}

	/**
	 * Creates a bond from custom/user-defined cash flows and coupon conventions
	 * 
	 * @param strName Bond Name
	 * @param dtEffective Effective Date
	 * @param strCurrency Bond Currency
	 * @param strDC Coupon Day Count Convention
	 * @param iFreq Coupon Frequency
	 * @param adt Array of dates
	 * @param adblCouponAmount Matching array of coupon amounts
	 * @param adblPrincipal Matching array of principal amounts
	 * @param bIsPrincipalPayDown Flag indicating whether principal is pay down or outstanding
	 * 
	 * @return The Bond object
	 */

	public static final org.drip.product.credit.BondComponent CreateBondFromCF (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strCurrency,
		final java.lang.String strDC,
		final int iFreq,
		final org.drip.analytics.date.JulianDate[] adt,
		final double[] adblCouponAmount,
		final double[] adblPrincipal,
		final boolean bIsPrincipalPayDown)
	{
		if (null == adt || 0 == adt.length || null == adblCouponAmount || 0 == adblCouponAmount.length ||
			null == adblPrincipal || 0 == adblPrincipal.length || adt.length != adblCouponAmount.length ||
				adt.length != adblPrincipal.length || null == dtEffective)
			return null;

		java.util.List<org.drip.analytics.period.Period> lPeriods = new
			java.util.ArrayList<org.drip.analytics.period.Period>();

		double dblTotalPrincipal = 0.;
		double[] adblDates = new double[adt.length];
		double[] adblNormalizedPrincipal = new double[adt.length];
		double[] adblCurrentCumulativePayDown = new double[adt.length];

		double dblPeriodStart = dtEffective.getJulian();

		if (bIsPrincipalPayDown) {
			for (int i = 0; i < adt.length; ++i) {
				if (0 == i)
					adblCurrentCumulativePayDown[i] = adblPrincipal[i];
				else
					adblCurrentCumulativePayDown[i] = adblCurrentCumulativePayDown[i - 1] + adblPrincipal[i];

				dblTotalPrincipal += adblPrincipal[i];
			}
		}

		for (int i = 0; i < adt.length; ++i) {
			if (null == adt[i]) {
				System.out.println ("Bad date in BondBuilder.CreateBondFromCF; index " + i);

				return null;
			}

			adblDates[i] = adt[i].getJulian();

			if (bIsPrincipalPayDown) {
				if (0. == dblTotalPrincipal)
					adblNormalizedPrincipal[i] = 1.;
				else
					adblNormalizedPrincipal[i] = 1. - (adblCurrentCumulativePayDown[i] / dblTotalPrincipal);

				if (s_bLog)
					System.out.println (adblPrincipal[i] + " | " + adblCurrentCumulativePayDown[i] + " | " +
						adblNormalizedPrincipal[i]);
			} else {
				adblNormalizedPrincipal[i] = adblPrincipal[i] / adblPrincipal[0];

				if (s_bLog) System.out.println (adblNormalizedPrincipal[i]);
			}

			try {
				if (bIsPrincipalPayDown) {
					double dblOutstandingPrincipal = 1.;

					if (0. != dblTotalPrincipal)
						dblOutstandingPrincipal = dblTotalPrincipal - adblCurrentCumulativePayDown[i];

					lPeriods.add (new org.drip.analytics.period.Period (dblPeriodStart, adblDates[i],
						dblPeriodStart, adblDates[i], adblDates[i], adblCouponAmount[i] /
							dblOutstandingPrincipal));
				} else
					lPeriods.add (new org.drip.analytics.period.Period (dblPeriodStart, adblDates[i],
						dblPeriodStart, adblDates[i], adblDates[i], adblCouponAmount[i] / adblPrincipal[i]));
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			dblPeriodStart = adblDates[i];
		}

		org.drip.product.params.PeriodSet bfpgp = new
			org.drip.product.params.PeriodSet (dtEffective.getJulian(), strDC, iFreq,
				lPeriods);

		if (!bfpgp.validate()) {
			System.out.println ("Could not validate bfpgp!");

			return null;
		}

		return BondBuilder.CreateBondFromParams (new org.drip.product.params.TreasuryBenchmark (null,
			strCurrency + "TSY", strCurrency + "EDSF"), new org.drip.product.params.IdentifierSet (strName,
				strName, strName, strCurrency + "TSY"), new org.drip.product.params.CouponSetting (null, "",
					1., java.lang.Double.NaN, java.lang.Double.NaN), new org.drip.product.params.CurrencySet
						(strCurrency + "TSY", strCurrency + "TSY", strCurrency + "TSY"), null, new
							org.drip.product.params.QuoteConvention (null, "", dtEffective.getJulian(), 100.,
								3, strCurrency, org.drip.analytics.daycount.Convention.DR_MOD_FOLL), new
									org.drip.product.params.RatesSetting (strCurrency, strCurrency,
										strCurrency, strCurrency), new org.drip.product.params.CreditSetting
											(30, java.lang.Double.NaN, true, "", true), new
												org.drip.product.params.TerminationSetting (false, false,
													false), bfpgp, new
														org.drip.product.params.NotionalSetting
															(org.drip.product.params.FactorSchedule.CreateFromDateFactorArray
			(adblDates, adblNormalizedPrincipal), 100.,
				org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_START, false));
	}

	/**
	 * Create a Bond Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return Bond Instance
	 */

	public static final org.drip.product.definition.Bond FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.credit.BondComponent (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
