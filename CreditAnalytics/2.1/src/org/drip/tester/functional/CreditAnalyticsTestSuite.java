
package org.drip.tester.functional;

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
 * Comprehensive sample API class demo'ing the usage of the FI functions
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalyticsTestSuite {
	private static final boolean s_bSupressErrMsg = false;

	/*
	 * Holiday Calendar Demo OP
	 */

	private static final boolean s_bPrintHolLoc = false;
	private static final boolean s_bPrintHolsInYear = false;
	private static final boolean s_bPrintWeekDayHolsInYear = false;
	private static final boolean s_bPrintWeekendHolsInYear = false;
	private static final boolean s_bPrintWeekendDays = false;
	private static final boolean s_bPrintHolidaySet = false;

	/*
	 * DayCount Demo OP
	 */

	private static final boolean s_bPrintDayCountList = false;
	private static final boolean s_bPrintDayCountTest = false;

	/*
	 * Discount Curve Demo OP
	 */

	private static final boolean s_bPrintEODIRCurveNames = false;
	private static final boolean s_bPrintEODIRFullCurve = false;
	private static final boolean s_bPrintEODIRFullCurves = false;
	private static final boolean s_bPrintEODIRCashCurve = false;
	private static final boolean s_bPrintEODIRCashCurves = false;
	private static final boolean s_bPrintEODIREDFCurve = false;
	private static final boolean s_bPrintEODIREDFCurves = false;
	private static final boolean s_bPrintEODIRSwapCurve = false;
	private static final boolean s_bPrintEODIRSwapCurves = false;
	private static final boolean s_bDCFromDF = false;
	private static final boolean s_bDCFromRate = false;
	private static final boolean s_bDCFromFlatRate = false;
	private static final boolean s_bPrintEODTSYCurveNames = false;
	private static final boolean s_bPrintEODTSYCurve = false;
	private static final boolean s_bPrintEODTSYCurves = false;

	/*
	 * Credit Curve Demo OP
	 */

	private static final boolean s_bCCFromFlatHazard = false;
	private static final boolean s_bCCFromSurvival = false;
	private static final boolean s_bPrintEODCDSCurveNames = false;
	private static final boolean s_bPrintEODCDSCurve = false;
	private static final boolean s_bPrintEODCDSQuotes = false;
	private static final boolean s_bPrintEODCDSCurves = false;

	/*
	 * CDS Demo OP
	 */

	private static final boolean s_bCDSCouponCFDisplay = false;
	private static final boolean s_bCDSLossCFDisplay = false;

	/*
	 * Bond Demo OP
	 */

	private static final boolean s_bAvailableTickers = false;
	private static final boolean s_bISINForTicker = false;
	private static final boolean s_bBondCouponCFDisplay = false;
	private static final boolean s_bBondLossCFDisplay = false;
	private static final boolean s_bBondAnalDisplay = false;

	/*
	 * Custom Bond Tests
	 */

	private static final boolean s_bCustomBondAnalDisplay = false;
	private static final boolean s_bCustomBondCouponCFDisplay = false;

	/*
	 * Bond Ticker Demo OP
	 */

	private static final boolean s_bTickerAnalDisplay = false;
	private static final boolean s_bTickerNotionalDisplay = false;
	private static final boolean s_bCumulativeTickerNotionalDisplay = false;

	/*
	 * Bond EOD Measures
	 */

	private static final boolean s_bBondEODMeasuresFromPrice = false;
	private static final boolean s_bBondEODMeasuresFromTSYSpread = false;
	private static final boolean s_bBondEODMeasuresFromYield = false;

	/*
	 * CDS EOD Measures
	 */

	private static final boolean s_bEODCDSMeasures = false;

	/*
	 * Bond Static Demo OP
	 */

	private static final boolean s_bStaticDisplay = false;

	/*
	 * Credit Curve calibration from Bond and CDS quotes Demo OP
	 */

	private static final boolean s_bCDSBondCreditCurve = false;

	/*
	 * FX Forward OP
	 */

	private static final boolean s_bFXFwd = false;

	/*
	 * Bond Basket OP
	 */

	private static final boolean s_bBasketBond = false;

	/*
	 * CDS Basket/CDX/iTRAXX OP
	 */

	private static final boolean s_bBasketCDS = false;
	private static final boolean s_bNamedCDXMap = false;
	private static final boolean s_bStandardCDXNames = false;
	private static final boolean s_bOnTheRun = false;
	private static final boolean s_bCDXSeries = false;

	private static final org.drip.product.params.FactorSchedule MakeFSPrincipal()
	{
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.analytics.date.JulianDate dtEOSStart = org.drip.analytics.date.JulianDate.Today().addDays
			(2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
		}

		try {
			return org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final org.drip.product.params.FactorSchedule MakeFSCoupon()
	{
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.analytics.date.JulianDate dtEOSStart = org.drip.analytics.date.JulianDate.Today().addDays
			(2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
		}

		try {
			return org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a custom named bond from the bond type and parameters
	 * 
	 * @param strName String representing the bond name
	 * @param iBondType Integer representing the bond type (fixed/floating/custom)
	 * 
	 * @return The created Bond
	 */

	public static final org.drip.product.definition.Bond CreateCustomBond (
		final java.lang.String strName,
		final int iBondType)
	{
		boolean bEOSOn = false;
		boolean bEOSAmerican = false;
		org.drip.product.credit.BondComponent bond = null;
		org.drip.product.params.EmbeddedOptionSchedule eosPut = null;
		org.drip.product.params.EmbeddedOptionSchedule eosCall = null;

		if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFloater (strName, "USD", "DRIPRI", 0.01,
				2, "30/360", org.drip.analytics.date.JulianDate.CreateFromYMD (2012, 3, 21),
					org.drip.analytics.date.JulianDate.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed (strName, "USD", 0.05, 2, "30/360",
				org.drip.analytics.date.JulianDate.CreateFromYMD (2012, 3, 21),
					org.drip.analytics.date.JulianDate.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF == iBondType) {
			final int NUM_CF_ENTRIES = 30;
			double[] adblCouponAmount = new double[NUM_CF_ENTRIES];
			double[] adblPrincipalAmount = new double[NUM_CF_ENTRIES];
			org.drip.analytics.date.JulianDate[] adt = new
				org.drip.analytics.date.JulianDate[NUM_CF_ENTRIES];

			org.drip.analytics.date.JulianDate dtEffective = org.drip.analytics.date.JulianDate.CreateFromYMD
				(2008, 9, 20);

			for (int i = 0; i < NUM_CF_ENTRIES; ++i) {
				adt[i] = dtEffective.addMonths (6 * (i + 1));

				adblCouponAmount[i] = 0.025;
				adblPrincipalAmount[i] = 1.0;
			}

			bond = org.drip.product.creator.BondBuilder.CreateBondFromCF (strName, dtEffective, "USD",
				"30/360", 2, adt, adblCouponAmount, adblPrincipalAmount, false);
		}

		if (bEOSOn) {
			double[] adblDate = new double[5];
			double[] adblPutFactor = new double[5];
			double[] adblCallFactor = new double[5];

			org.drip.analytics.date.JulianDate dtEOSStart =
				org.drip.analytics.date.JulianDate.Today().addDays (2);

			for (int i = 0; i < 5; ++i) {
				adblPutFactor[i] = 0.9;
				adblCallFactor[i] = 1.0;

				adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
			}

			if (bEOSAmerican) {
				eosCall = org.drip.product.params.EmbeddedOptionSchedule.fromAmerican
					(org.drip.analytics.date.JulianDate.Today().getJulian() + 1, adblDate, adblCallFactor,
						false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

				eosPut = org.drip.product.params.EmbeddedOptionSchedule.fromAmerican
					(org.drip.analytics.date.JulianDate.Today().getJulian(), adblDate, adblPutFactor, true,
						30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);
			} else {
				try {
					eosCall = new org.drip.product.params.EmbeddedOptionSchedule (adblDate, adblCallFactor,
						false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

					eosPut = new org.drip.product.params.EmbeddedOptionSchedule (adblDate, adblPutFactor,
						true, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CreateCustomBond failed.");

						return null;
					}

					e.printStackTrace();
				}
			}

			bond.setEmbeddedCallSchedule (eosCall);

			bond.setEmbeddedPutSchedule (eosPut);
		}

		return bond;
	}

	/**
	 * Sample demonstrating the calendar API
	 */

	public static final void CalenderAPISample()
	{
		java.util.Set<java.lang.String> setLoc = org.drip.service.api.CreditAnalytics.GetHolLocations();

		org.drip.analytics.date.JulianDate[] adtHols = org.drip.service.api.CreditAnalytics.GetHolsInYear
			("USD,GBP", 2011);

		org.drip.analytics.date.JulianDate[] adtWeekDayHols =
			org.drip.service.api.CreditAnalytics.GetWeekDayHolsInYear ("USD,GBP", 2011);

		org.drip.analytics.date.JulianDate[] adtWeekendHols =
			org.drip.service.api.CreditAnalytics.GetWeekendHolsInYear ("USD,GBP", 2011);

		int[] aiWkendDays = org.drip.service.api.CreditAnalytics.GetWeekendDays ("USD,GBP");

		boolean bIsHoliday = false;

		try {
			bIsHoliday = org.drip.service.api.CreditAnalytics.IsHoliday ("USD,GBP",
				org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 12, 28));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("CalendarAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.List<java.lang.Double> lsHols = org.drip.analytics.daycount.Convention.HolidaySet
			(org.drip.analytics.date.JulianDate.Today().getJulian(),
				org.drip.analytics.date.JulianDate.Today().addYears (1).getJulian(), "USD,GBP");

		if (s_bPrintHolLoc) {
			System.out.println ("Num Hol Locations: " + setLoc.size());

			for (java.lang.String strLoc : setLoc)
				System.out.println (strLoc);
		}

		if (s_bPrintHolsInYear) {
			System.out.println (org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 12, 28) +
				" is a USD,GBP holiday? " + bIsHoliday);

			System.out.println ("USD,GBP has " + adtHols.length + " hols");

			for (int i = 0; i < adtHols.length; ++i)
				System.out.println (adtHols[i]);
		}

		if (s_bPrintWeekDayHolsInYear) {
			System.out.println ("USD,GBP has " + adtWeekDayHols.length + " week day hols");

			for (int i = 0; i < adtWeekDayHols.length; ++i)
				System.out.println (adtWeekDayHols[i]);
		}

		if (s_bPrintWeekendHolsInYear) {
			System.out.println ("USD,GBP has " + adtWeekendHols.length + " weekend hols");

			for (int i = 0; i < adtWeekendHols.length; ++i)
				System.out.println (adtWeekendHols[i]);
		}

		if (s_bPrintWeekendDays) {
			System.out.println ("USD,GBP has " + aiWkendDays.length + " weekend days");

			for (int i = 0; i < aiWkendDays.length; ++i) {
				try {
					System.out.println (org.drip.analytics.date.JulianDate.getDayChars (aiWkendDays[i]));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CalendarAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintHolidaySet) {
			for (double dblDate : lsHols) {
				try {
					System.out.println (new org.drip.analytics.date.JulianDate (dblDate).toOracleDate());
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CalendarAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample API demonstrating the day count functionality
	 */

	public static final void DayCountAPISample()
	{
		java.lang.String strDCList = org.drip.service.api.CreditAnalytics.GetAvailableDC();

		double dblYF = java.lang.Double.NaN;

		try {
			dblYF = org.drip.service.api.CreditAnalytics.YearFraction
				(org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 1, 14),
					org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 2, 14), "Act/360", false, "USD");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("DayCountAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtAdjusted = org.drip.service.api.CreditAnalytics.Adjust
			(org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 1, 16), "USD", 0);

		org.drip.analytics.date.JulianDate dtRoll = org.drip.service.api.CreditAnalytics.RollDate
			(org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 1, 16), "USD",
				org.drip.analytics.daycount.Convention.DR_PREV);

		if (s_bPrintDayCountList)
			System.out.println (strDCList);

		if (s_bPrintDayCountTest) {
			System.out.println ("YearFract: " + dblYF);

			System.out.println ("Adjusted: " + dtAdjusted);

			System.out.println ("Rolled: " + dtRoll);
		}
	}

	/**
	 * Sample API demonstrating the creation/usage of discount curve
	 */

	public static final void DiscountCurveAPISample()
	{
		org.drip.analytics.date.JulianDate dt1 = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 12,
			16);

		org.drip.analytics.date.JulianDate dt2 = org.drip.analytics.date.JulianDate.CreateFromYMD (2012, 1,
			17);

		java.util.Set<java.lang.String> setstrIRCurves =
			org.drip.service.api.CreditAnalytics.GetEODIRCurveNames (dt1);

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.service.api.CreditAnalytics.LoadEODFullIRCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= org.drip.service.api.CreditAnalytics.LoadEODFullIRCurves ("EUR", dt1, dt2);

		org.drip.analytics.definition.DiscountCurve dcCash =
			org.drip.service.api.CreditAnalytics.LoadEODIRCashCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve>
			mapCashDC = org.drip.service.api.CreditAnalytics.LoadEODIRCashCurves ("EUR", dt1, dt2);

		org.drip.analytics.definition.DiscountCurve dcEDF =
			org.drip.service.api.CreditAnalytics.LoadEODEDFCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve>
			mapEDFDC = org.drip.service.api.CreditAnalytics.LoadEODEDFCurves ("EUR", dt1, dt2);

		org.drip.analytics.definition.DiscountCurve dcIRS =
			org.drip.service.api.CreditAnalytics.LoadEODIRSwapCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve>
			mapIRSDC = org.drip.service.api.CreditAnalytics.LoadEODIRSwapCurves ("EUR", dt1, dt2);

		double[] adblDF = new double[5];
		double[] adblDate = new double[5];
		double[] adblRate = new double[5];

		org.drip.analytics.date.JulianDate dtStart = org.drip.analytics.date.JulianDate.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblDF[i] = 1. - 2 * (i + 1) * (adblRate[i] = 0.05);
		}

		org.drip.analytics.definition.DiscountCurve dcFromDF =
			org.drip.analytics.creator.DiscountCurveBuilder.BuildFromDF (dtStart, "EUR", adblDate, adblDF,
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.definition.DiscountCurve dcFromRate =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateDC (dtStart, "EUR", adblDate, adblRate,
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.definition.DiscountCurve dcFromFlatRate =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtStart, "DKK", 0.04);

		java.util.Set<java.lang.String> setstrTSYCurves =
			org.drip.service.api.CreditAnalytics.GetEODTSYCurveNames (dt1);

		org.drip.analytics.definition.DiscountCurve dcTSY =
			org.drip.service.api.CreditAnalytics.LoadEODTSYCurve ("USD", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve>
			mapTSYDC = org.drip.service.api.CreditAnalytics.LoadEODTSYCurves ("USD", dt1, dt2);

		if (s_bPrintEODIRCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrIRCurves.size() + " IR Curves. They are:");

				for (java.lang.String strIRCurveName : setstrIRCurves)
					System.out.println (strIRCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODTSYCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrTSYCurves.size() + " IR Curves. They are:");

				for (java.lang.String strTSYCurveName : setstrTSYCurves)
					System.out.println (strTSYCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODIRFullCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dc.getDF
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblQuotes = dc.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCC = dc.getCalibComponents();

			for (int i = 0; i < aCC.length; ++i)
				System.out.println (aCC[i].getPrimaryCode() + " => " + adblQuotes[i]);
		}

		if (s_bPrintEODIRFullCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve> meDC : mapDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meDC.getKey();

				org.drip.analytics.definition.DiscountCurve dcEOD = meDC.getValue();

				try {
					System.out.println (dt + "[IRS.3Y] => " + dcEOD.getQuote ("IRS.3Y"));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIRCashCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcCash.getDF
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblCashQuotes = dcCash.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCCCash = dcCash.getCalibComponents();

			for (int i = 0; i < aCCCash.length; ++i)
				System.out.println (aCCCash[i].getPrimaryCode() + " => " + (int) (10000. *
					adblCashQuotes[i]));
		}

		if (s_bPrintEODIRCashCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve> meCashDC : mapCashDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meCashDC.getKey();

				org.drip.analytics.definition.DiscountCurve dcEOD = meCashDC.getValue();

				try {
					System.out.println (dt + "[3M] => " + (int) (10000. * dcEOD.getQuote ("3M")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIREDFCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcEDF.getDF
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblEDFQuotes = dcEDF.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCCEDF = dcEDF.getCalibComponents();

			for (int i = 0; i < aCCEDF.length; ++i)
				System.out.println (aCCEDF[i].getPrimaryCode() + " => " + (int) (10000. * adblEDFQuotes[i]));
		}

		if (s_bPrintEODIREDFCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve> meEDFDC : mapEDFDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meEDFDC.getKey();

				org.drip.analytics.definition.DiscountCurve dcEOD = meEDFDC.getValue();

				try {
					System.out.println (dt + "[EDZ3] => " + (int) (10000. * dcEOD.getQuote ("EDZ3")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIRSwapCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcIRS.getDF
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblIRSQuotes = dcIRS.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCCIRS = dcIRS.getCalibComponents();

			for (int i = 0; i < aCCIRS.length; ++i)
				System.out.println (aCCIRS[i].getPrimaryCode() + " => " + (int) (10000. * adblIRSQuotes[i]));
		}

		if (s_bPrintEODIRSwapCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve> meIRSDC : mapIRSDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meIRSDC.getKey();

				org.drip.analytics.definition.DiscountCurve dcEOD = meIRSDC.getValue();

				try {
					System.out.println (dt + "[IRS.40Y bp] => " + (int) (dcEOD.getQuote ("IRS.40Y") *
						10000.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bDCFromDF) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("DCFromDF[" + dt.toString() + "]; DF=" + dcFromDF.getDF (dt) + "; Rate="
					+ dcFromDF.calcImpliedRate ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bDCFromRate) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("DCFromRate[" + dt.toString() + "]; DF=" + dcFromRate.getDF (dt) +
					"; Rate=" + dcFromRate.calcImpliedRate ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bDCFromFlatRate) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("DCFromFlatRate[" + dt.toString() + "]; DF=" + dcFromFlatRate.getDF (dt)
					+ "; Rate=" + dcFromFlatRate.calcImpliedRate ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODTSYCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcTSY.getDF
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblTSYQuotes = dcTSY.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCompTSY = dcTSY.getCalibComponents();

			for (int i = 0; i < aCompTSY.length; ++i)
				System.out.println (aCompTSY[i].getPrimaryCode() + " => " + (int) (10000. *
					adblTSYQuotes[i]));
		}

		if (s_bPrintEODTSYCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve> meTSYDC : mapTSYDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meTSYDC.getKey();

				org.drip.analytics.definition.DiscountCurve dcTSYEOD = meTSYDC.getValue();

				try {
					System.out.println (dt + "[5Y] => " + (int) (10000. * dcTSYEOD.getQuote ("5Y")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample API demonstrating the creation/usage of the credit curve API
	 */

	public static final void CreditCurveAPISample()
	{
		org.drip.analytics.definition.CreditCurve ccFlatHazard =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4);

		double[] adblDate = new double[5];
		double[] adblSurvival = new double[5];

		org.drip.analytics.date.JulianDate dtStart = org.drip.analytics.date.JulianDate.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblSurvival[i] = 1. - 0.1 * (i + 1);
		}

		org.drip.analytics.definition.CreditCurve ccFromSurvival =
			org.drip.analytics.creator.CreditCurveBuilder.FromSurvival (dtStart.getJulian(), "CC", adblDate,
				adblSurvival, 0.4);

		java.util.Set<java.lang.String> setstrCDSCurves =
			org.drip.service.api.CreditAnalytics.GetEODCDSCurveNames
				(org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 7, 21));

		org.drip.analytics.definition.CreditCurve ccEOD =
			org.drip.service.api.CreditAnalytics.LoadEODCDSCreditCurve ("813796", "USD",
				org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 7, 21));

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.CreditCurve> mapCC =
			org.drip.service.api.CreditAnalytics.LoadEODCDSCreditCurves ("813796", "USD",
				org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 7, 14),
					org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 7, 21));

		if (s_bPrintEODCDSCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrCDSCurves.size() + " CDS Curves. They are:");

				for (java.lang.String strCDSCurveName : setstrCDSCurves)
					System.out.println (strCDSCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bCCFromFlatHazard) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("CCFromFlatHazard[" + dt.toString() + "]; Survival=" +
					ccFlatHazard.getSurvival ("10Y") + "; Hazard=" + ccFlatHazard.calcHazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bCCFromSurvival) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("CCFromSurvival[" + dt.toString() + "]; Survival=" +
					ccFromSurvival.getSurvival ("10Y") + "; Hazard=" + ccFromSurvival.calcHazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODCDSCurve) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("CCFromEOD[" + dt.toString() + "]; Survival=" + ccEOD.getSurvival ("10Y")
					+ "; Hazard=" + ccEOD.calcHazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODCDSQuotes) {
			try {
				System.out.println ("Surv (2021, 1, 14): " + ccEOD.getSurvival
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblCDSQuotes = ccEOD.getCompQuotes();

			org.drip.product.definition.CalibratableComponent[] aCompCDS = ccEOD.getCalibComponents();

			for (int i = 0; i < aCompCDS.length; ++i)
				System.out.println (aCompCDS[i].getPrimaryCode() + " => " + (int) (adblCDSQuotes[i]));
		}

		if (s_bPrintEODCDSCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.CreditCurve> meCC : mapCC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meCC.getKey();

				org.drip.analytics.definition.CreditCurve ccCOB = meCC.getValue();

				try {
					System.out.println (dt + "[CDS.5Y] => " + (int) (ccCOB.getQuote ("CDS.5Y")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CreditCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the CDS API
	 */

	public static final void CDSAPISample()
	{
		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "USD", 0.05);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4);

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateSNAC
			(org.drip.analytics.date.JulianDate.Today(), "5Y", 0.1, "CC");

		if (s_bCDSCouponCFDisplay) {
			org.drip.param.valuation.ValuationParams valParams =
				org.drip.param.valuation.ValuationParams.CreateValParams
					(org.drip.analytics.date.JulianDate.Today(), 0, "",
						org.drip.analytics.daycount.Convention.DR_ACTUAL);

			org.drip.param.pricer.PricerParams pricerParams =
				org.drip.param.pricer.PricerParams.MakeStdPricerParams();

			System.out.println ("Acc Start       Acc End     Pay Date      Cpn DCF    Pay01    Surv01");

			System.out.println ("---------      ---------    ---------    --------- --------- --------");

			for (org.drip.analytics.period.CouponPeriodCurveFactors p : cds.getCouponFlow (valParams,
				pricerParams, org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP
					(org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
						(org.drip.analytics.date.JulianDate.Today(), "USD", 0.05),
							org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
								(org.drip.analytics.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4))))
			{
				try {
					System.out.println (org.drip.analytics.date.JulianDate.fromJulian
						(p.getAccrualStartDate()) + "    " + org.drip.analytics.date.JulianDate.fromJulian
							(p.getAccrualEndDate()) + "    " + org.drip.analytics.date.JulianDate.fromJulian
								(p.getPayDate()) + "    " +
									org.drip.analytics.support.GenericUtil.FormatDouble
										(p.getCouponDCF(), 1, 4, 1.) + "    " +
											org.drip.analytics.support.GenericUtil.FormatDouble
												(dc.getDF (p.getPayDate()), 1, 4, 1.) + "    " +
													org.drip.analytics.support.GenericUtil.FormatDouble
														(cc.getSurvival (p.getPayDate()), 1, 4, 1.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CDSAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bCDSLossCFDisplay) {
			System.out.println
				("Loss Start     Loss End      Pay Date      Cpn    Notl     Rec    EffDF    StartSurv  EndSurv");

			System.out.println
				("----------     --------      --------      ---    ----     ---    -----    ---------  -------");

			for (org.drip.analytics.period.LossPeriodCurveFactors dp : cds.getLossFlow
				(org.drip.param.valuation.ValuationParams.CreateValParams 
					(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
						org.drip.analytics.daycount.Convention.DR_ACTUAL),
							org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
							org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc)))
				System.out.println (org.drip.analytics.date.JulianDate.fromJulian (dp.getStartDate()) +
					"    " + org.drip.analytics.date.JulianDate.fromJulian (dp.getEndDate()) + "    " +
						org.drip.analytics.date.JulianDate.fromJulian (dp.getPayDate()) + "    " +
							org.drip.analytics.support.GenericUtil.FormatDouble (dp.getCouponDCF(), 1,
								4, 1.) + "    " + org.drip.analytics.support.GenericUtil.FormatDouble
									(dp.getEffectiveNotional(), 1, 0, 1.) + "    " +
										org.drip.analytics.support.GenericUtil.FormatDouble
											(dp.getEffectiveRecovery(), 1, 2, 1.) + "    " +
												org.drip.analytics.support.GenericUtil.FormatDouble
													(dp.getEffectiveDF(), 1, 4, 1.)  + "    " +
														org.drip.analytics.support.GenericUtil.FormatDouble
					(dp.getStartSurvival(), 1, 4, 1.) + "    " +
						org.drip.analytics.support.GenericUtil.FormatDouble (dp.getEndSurvival(), 1, 4,
							1.));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond API
	 */

	public static final void BondAPISample()
	{
		java.util.Set<java.lang.String> setstrTickers =
			org.drip.service.api.CreditAnalytics.GetAvailableTickers();

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.CreditAnalytics.GetISINsForTicker
			("DB");

		// java.lang.String strISIN = "XS0145044193";
		java.lang.String strISIN = "US78490FPP89"; // EOS
		// java.lang.String strISIN = "US760677FD19"; // Amortizer
		org.drip.param.valuation.QuotingParams quotingParams = null;
		boolean bInFirstPeriod = true;
		boolean bInLastPeriod = true;
		double dblGTMFromPrice = java.lang.Double.NaN;
		double dblITMFromPrice = java.lang.Double.NaN;
		double dblYTMFromPrice = java.lang.Double.NaN;
		double dblZTMFromPrice = java.lang.Double.NaN;
		double dblPECSFromPrice = java.lang.Double.NaN;
		double dblTSYTMFromPrice = java.lang.Double.NaN;
		double dblYieldFromPrice = java.lang.Double.NaN;
		double dblBondCreditPrice = java.lang.Double.NaN;
		double dblParASWFromPrice = java.lang.Double.NaN;
		double dblPECSTMFromPrice = java.lang.Double.NaN;
		double dblGSpreadFromPrice = java.lang.Double.NaN;
		double dblISpreadFromPrice = java.lang.Double.NaN;
		double dblZSpreadFromPrice = java.lang.Double.NaN;
		double dblParASWTMFromPrice = java.lang.Double.NaN;
		double dblPriceFromTSYSpread = java.lang.Double.NaN;
		double dblTSYSpreadFromPrice = java.lang.Double.NaN;
		double dblYieldFromTSYSpread = java.lang.Double.NaN;
		double dblParASWFromTSYSpread = java.lang.Double.NaN;
		double dblCreditBasisFromPrice = java.lang.Double.NaN;
		double dblGSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblISpreadFromTSYSpread = java.lang.Double.NaN;
		double dblZSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblCreditBasisTMFromPrice = java.lang.Double.NaN;
		double dblDiscountMarginFromPrice = java.lang.Double.NaN;
		double dblCreditBasisFromTSYSpread = java.lang.Double.NaN;
		double dblDiscountMarginTMFromPrice = java.lang.Double.NaN;
		double dblDiscountMarginFromTSYSpread = java.lang.Double.NaN;

		try {
			quotingParams = new org.drip.param.valuation.QuotingParams ("30/360", 2, true, null, "USD",
				false);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.JulianDate.Today();

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateValParams (dtToday, 0, "",
				org.drip.analytics.daycount.Convention.DR_ACTUAL);

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "USD", 0.03);

		org.drip.analytics.definition.DiscountCurve dcTSY =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard (dtToday.getJulian(), "CC", 0.02,
				0.);

		org.drip.param.valuation.WorkoutInfo wi =
			org.drip.service.api.CreditAnalytics.BondWorkoutInfoFromPrice (strISIN, dtToday, dc, 1.);

		try {
			dblYieldFromPrice = org.drip.service.api.CreditAnalytics.BondYieldFromPrice (strISIN, dtToday,
				dc, 1.);

			dblYTMFromPrice = org.drip.service.api.CreditAnalytics.BondYTMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondZSpreadFromPrice (strISIN,
				dtToday, dc, 1.);

			dblZTMFromPrice = org.drip.service.api.CreditAnalytics.BondZTMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblISpreadFromPrice = org.drip.service.api.CreditAnalytics.BondISpreadFromPrice (strISIN,
				dtToday, dc, 1.);

			dblITMFromPrice = org.drip.service.api.CreditAnalytics.BondITMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblDiscountMarginFromPrice = org.drip.service.api.CreditAnalytics.BondDiscountMarginFromPrice
				(strISIN, dtToday, dc, 1.);

			dblDiscountMarginTMFromPrice = org.drip.service.api.CreditAnalytics.BondDiscountMarginTMFromPrice
				(strISIN, valParams, dc, 1., quotingParams);

			dblTSYSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondTSYSpreadFromPrice (strISIN,
				dtToday, dc, dcTSY, 1.);

			dblTSYTMFromPrice = org.drip.service.api.CreditAnalytics.BondTSYTMFromPrice (strISIN, valParams,
				dc, dcTSY, 1., quotingParams);

			dblGSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondGSpreadFromPrice (strISIN,
				dtToday, dc, dcTSY, 1.);

			dblGTMFromPrice = org.drip.service.api.CreditAnalytics.BondGTMFromPrice (strISIN, valParams, dc,
				dcTSY, 1., quotingParams);

			dblParASWFromPrice = org.drip.service.api.CreditAnalytics.BondParASWFromPrice (strISIN, dtToday,
				dc, 1.);

			dblParASWTMFromPrice = org.drip.service.api.CreditAnalytics.BondParASWTMFromPrice (strISIN,
				valParams, dc, 1., quotingParams);

			dblCreditBasisFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisFromPrice (strISIN,
				dtToday, dc, cc, 1.);

			dblCreditBasisTMFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisTMFromPrice
				(strISIN, valParams, dc, cc, 1., quotingParams);

			dblPECSFromPrice = org.drip.service.api.CreditAnalytics.BondPECSFromPrice (strISIN, dtToday, dc,
				cc, 1.);

			dblPECSTMFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisTMFromPrice (strISIN,
				valParams, dc, cc, 1., quotingParams);

			dblPriceFromTSYSpread = org.drip.service.api.CreditAnalytics.BondPriceFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblYieldFromTSYSpread = org.drip.service.api.CreditAnalytics.BondYieldFromTSYSpread (strISIN,
				dtToday, dcTSY, 0.0271);

			dblZSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondZSpreadFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblISpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondISpreadFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblDiscountMarginFromTSYSpread = org.drip.service.api.CreditAnalytics.BondISpreadFromTSYSpread
				(strISIN, dtToday, dc, dcTSY, 0.0271);

			dblGSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondGSpreadFromTSYSpread (strISIN,
				dtToday, dc,  dcTSY, 0.0271);

			dblParASWFromTSYSpread = org.drip.service.api.CreditAnalytics.BondParASWFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblCreditBasisFromTSYSpread = org.drip.service.api.CreditAnalytics.BondCreditBasisFromTSYSpread
				(strISIN, dtToday, dc, dcTSY, cc, 0.0271);

			dblBondCreditPrice = org.drip.service.api.CreditAnalytics.BondCreditPrice (strISIN, valParams,
				dc, cc, quotingParams);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtPreviousCoupon =
			org.drip.service.api.CreditAnalytics.PreviousCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtCurrentCoupon =
			org.drip.service.api.CreditAnalytics.NextCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtNextCoupon =
			org.drip.service.api.CreditAnalytics.NextCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtEffective = org.drip.service.api.CreditAnalytics.EffectiveDate
			(strISIN);

		org.drip.analytics.date.JulianDate dtMaturity = org.drip.service.api.CreditAnalytics.MaturityDate
			(strISIN);

		try {
			bInFirstPeriod = org.drip.service.api.CreditAnalytics.InFirstPeriod (strISIN,
				valParams._dblValue);

			bInLastPeriod = org.drip.service.api.CreditAnalytics.InLastPeriod (strISIN, valParams._dblValue);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) return;

			e.printStackTrace();
		}

		org.drip.analytics.output.ExerciseInfo nei = org.drip.service.api.CreditAnalytics.NextExerciseInfo
			(strISIN, dtToday);

		if (s_bAvailableTickers) {
			for (java.lang.String strTicker : setstrTickers)
				System.out.println (strTicker);
		}

		if (s_bISINForTicker) {
			for (java.lang.String strBondISIN : lsstrISIN)
				System.out.println (strBondISIN);
		}

		if (s_bBondCouponCFDisplay) {
			org.drip.param.pricer.PricerParams pricerParams =
				org.drip.param.pricer.PricerParams.MakeStdPricerParams();

			System.out.println ("Acc Start       Acc End     Pay Date      Cpn DCF    Pay01    Surv01");

			System.out.println ("---------      ---------    ---------    --------- --------- --------");

			for (org.drip.analytics.period.CouponPeriodCurveFactors p : bond.getCouponFlow
				(valParams, pricerParams, org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP
					(org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD",
						0.05), org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
							(dtToday.getJulian(), "CC", 0.02, 0.4)))) {
				try {
					System.out.println (org.drip.analytics.date.JulianDate.fromJulian
						(p.getAccrualStartDate()) + "    " + org.drip.analytics.date.JulianDate.fromJulian
							(p.getAccrualEndDate()) + "    " + org.drip.analytics.date.JulianDate.fromJulian
								(p.getPayDate()) + "    " +
									org.drip.analytics.support.GenericUtil.FormatDouble
										(p.getCouponDCF(), 1, 4, 1.) + "    " +
											org.drip.analytics.support.GenericUtil.FormatDouble
												(dc.getDF (p.getPayDate()), 1, 4, 1.) + "    " +
													org.drip.analytics.support.GenericUtil.FormatDouble
														(cc.getSurvival (p.getPayDate()), 1, 4, 1.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("BondAPISample failed.");

						return;
					}
	
					e.printStackTrace();
				}
			}
		}

		if (s_bBondLossCFDisplay) {
			System.out.println
				("Loss Start     Loss End      Pay Date      Cpn    Notl     Rec    EffDF    StartSurv  EndSurv");

			System.out.println
				("----------     --------      --------      ---    ----     ---    -----    ---------  -------");
	
			for (org.drip.analytics.period.LossPeriodCurveFactors dp : bond.getLossFlow (valParams,
				org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
				org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc)))
				System.out.println (org.drip.analytics.date.JulianDate.fromJulian (dp.getStartDate()) +
					"    " + org.drip.analytics.date.JulianDate.fromJulian (dp.getEndDate()) + "    " +
						org.drip.analytics.date.JulianDate.fromJulian (dp.getPayDate()) + "    " +
							org.drip.analytics.support.GenericUtil.FormatDouble (dp.getCouponDCF(), 1,
								4, 1.) + "    " + org.drip.analytics.support.GenericUtil.FormatDouble
									(dp.getEffectiveNotional(), 1, 0, 1.) + "    " +
										org.drip.analytics.support.GenericUtil.FormatDouble
											(dp.getEffectiveRecovery(), 1, 2, 1.) + "    " +
												org.drip.analytics.support.GenericUtil.FormatDouble
													(dp.getEffectiveDF(), 1, 4, 1.)  + "    " +
														org.drip.analytics.support.GenericUtil.FormatDouble
					(dp.getStartSurvival(), 1, 4, 1.) + "    " +
						org.drip.analytics.support.GenericUtil.FormatDouble (dp.getEndSurvival(), 1, 4,
							1.));
		}

		if (s_bBondAnalDisplay) {
			try {
				System.out.println (strISIN + "    " + bond.getTicker() + " " +
					org.drip.analytics.support.GenericUtil.FormatPrice (bond.getCoupon
						(org.drip.analytics.date.JulianDate.Today().getJulian(),
							org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc))) +
								" " + bond.getMaturityDate());

				System.out.println ("Work-out date From Price: " + new org.drip.analytics.date.JulianDate
					(wi._dblDate));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Work-out factor From Price: " + wi._dblExerciseFactor);

			System.out.println ("Work-out Yield From Price: " +
				org.drip.analytics.support.GenericUtil.FormatPrice (wi._dblYield));

			System.out.println ("Work-out Type for Price: " +
				org.drip.analytics.support.AnalyticsHelper.WorkoutTypeToString (wi._iWOType));

			System.out.println ("Yield From Price: " + org.drip.analytics.support.GenericUtil.FormatPrice
				(dblYieldFromPrice) + " / " + org.drip.analytics.support.GenericUtil.FormatPrice
					(dblYTMFromPrice));

			System.out.println ("Z Spread From Price: " + (int) (10000. * dblZSpreadFromPrice) + " / " +
				(int) (10000. * dblZTMFromPrice));

			System.out.println ("I Spread From Price: " + (int) (10000. * dblISpreadFromPrice) + " / " +
				(int) (10000. * dblITMFromPrice));

			System.out.println ("Discount Margin From Price: " + (int) (10000. * dblDiscountMarginFromPrice)
				+ " / " + (int) (10000. * dblDiscountMarginTMFromPrice));

			System.out.println ("TSY Spread From Price: " + (int) (10000. * dblTSYSpreadFromPrice) + " / " +
				(int) (10000. * dblTSYTMFromPrice));

			System.out.println ("G Spread From Price: " + (int) (10000. * dblGSpreadFromPrice) + " / " +
				(int) (10000. * dblGTMFromPrice));

			System.out.println ("Par ASW From Price: " + (int) dblParASWFromPrice + " / " + (int)
				dblParASWTMFromPrice);

			System.out.println ("Credit Basis From Price: " + (int) (10000. * dblCreditBasisFromPrice) +
				" / " + (int) (10000. * dblCreditBasisTMFromPrice));

			System.out.println ("PECS From Price: " + (int) (10000. * dblPECSFromPrice) + " / " + (int)
				(10000. * dblPECSTMFromPrice));

			System.out.println ("Price From TSY Spread: " +
				org.drip.analytics.support.GenericUtil.FormatPrice (dblPriceFromTSYSpread));

			System.out.println ("Yield From TSY Spread: " +
				org.drip.analytics.support.GenericUtil.FormatPrice (dblYieldFromTSYSpread));

			System.out.println ("Z Spread From TSY Spread: " + (int) (10000. * dblZSpreadFromTSYSpread));

			System.out.println ("I Spread From TSY Spread: " + (int) (10000. * dblISpreadFromTSYSpread));

			System.out.println ("Discount Margin From TSY Spread: " + (int) (10000. *
				dblDiscountMarginFromTSYSpread));

			System.out.println ("G Spread From TSY Spread: " + (int) (10000. * dblGSpreadFromTSYSpread));

			System.out.println ("Par ASW From TSY Spread: " + (int) dblParASWFromTSYSpread);

			System.out.println ("Credit Basis From TSY Spread: " + (int) (10000. *
				dblCreditBasisFromTSYSpread));

			System.out.println ("Credit Risky Price: " + org.drip.analytics.support.GenericUtil.FormatPrice
				(dblBondCreditPrice));

			System.out.println ("Valuation Date: " + org.drip.analytics.date.JulianDate.Today());

			System.out.println ("Effective Date: " + dtEffective);

			System.out.println ("Maturity Date: " + dtMaturity);

			System.out.println ("Is Val Date in the first period? " + bInFirstPeriod);

			System.out.println ("Is Val Date in the last period? " + bInLastPeriod);

			System.out.println ("Previous Coupon Date: " + dtPreviousCoupon);

			System.out.println ("Current Coupon Date: " + dtCurrentCoupon);

			System.out.println ("Next Coupon Date: " + dtNextCoupon);

			try {
				System.out.println ("Next Exercise Date: " + new org.drip.analytics.date.JulianDate
					(nei._dblDate));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Next Exercise Factor: " + nei._dblExerciseFactor);

			System.out.println ("Next Exercise Type: " +
				org.drip.analytics.support.AnalyticsHelper.WorkoutTypeToString (nei._iWOType));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the custom bond API
	 */

	public static final void CustomBondAPISample()
	{
		org.drip.product.definition.Bond[] aBond = new org.drip.product.definition.Bond[3];

		if (null == (aBond[0] = org.drip.service.api.CreditAnalytics.GetBond ("CustomFixed")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomFixed", aBond[0] = CreateCustomBond
				("CustomFixed", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED));

		if (null == (aBond[1] = org.drip.service.api.CreditAnalytics.GetBond ("CustomFRN")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomFRN", aBond[1] = CreateCustomBond
				("CustomFRN", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER));

		if (null == (aBond[2] = org.drip.service.api.CreditAnalytics.GetBond ("CustomBondFromCF")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomBondFromCF", aBond[2] = CreateCustomBond
				("CustomBondFromCF", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF));

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "USD", 0.04);

		org.drip.analytics.definition.DiscountCurve dcTSY =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "USD", 0.03);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		for (int i = 0; i < aBond.length; ++i) {
			if (s_bCustomBondCouponCFDisplay) {
				System.out.println
					("\nAcc Start     Acc End     Pay Date      Cpn DCF       Pay01       Surv01");

				System.out.println
					("---------    ---------    ---------    ---------    ---------    --------");

				for (org.drip.analytics.period.Period p : aBond[i].getCouponPeriod()) {
					try {
						System.out.println (org.drip.analytics.date.JulianDate.fromJulian
							(p.getAccrualStartDate()) + "    " +
								org.drip.analytics.date.JulianDate.fromJulian (p.getAccrualEndDate()) +
									"    " + org.drip.analytics.date.JulianDate.fromJulian (p.getPayDate()) +
										"    " + org.drip.analytics.support.GenericUtil.FormatDouble
											(p.getCouponDCF(), 1, 4, 1.) + "    " +
												org.drip.analytics.support.GenericUtil.FormatDouble
													(dc.getDF (p.getPayDate()), 1, 4, 1.) + "    " +
														org.drip.analytics.support.GenericUtil.FormatDouble
							(cc.getSurvival (p.getPayDate()), 1, 4, 1.));
					} catch (java.lang.Exception e) {
						if (s_bSupressErrMsg) {
							System.out.println ("CustomAPISample failed.");

							return;
						}

						e.printStackTrace();
					}
				}
			}

			org.drip.param.definition.ComponentMarketParams cmp =
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
					dcTSY, cc, null, null, org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject
						(aBond[i], org.drip.analytics.date.JulianDate.Today(), 0.04));

			if (s_bCustomBondAnalDisplay) {
				try {
					System.out.println ("\nPrice From Yield: " +
						org.drip.analytics.support.GenericUtil.FormatPrice (aBond[i].calcPriceFromYield
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, 0.)));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CustomAPISample failed.");

						return;
					}

					e.printStackTrace();
				}

				org.drip.param.valuation.WorkoutInfo wi = aBond[i].calcExerciseYieldFromPrice
					(org.drip.param.valuation.ValuationParams.CreateValParams
						(org.drip.analytics.date.JulianDate.Today(), 0, "",
							org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, 1.);

				System.out.println ("Workout Date: " + org.drip.analytics.date.JulianDate.fromJulian
					(wi._dblDate));

				System.out.println ("Workout Factor: " + wi._dblExerciseFactor);

				System.out.println ("Workout Yield: " + org.drip.analytics.support.GenericUtil.FormatPrice
					(wi._dblYield));

				try {
					System.out.println ("Workout Yield From Price: " +
						org.drip.analytics.support.GenericUtil.FormatPrice (aBond[i].calcYieldFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					try {
						System.out.println ("Z Spread From Price: " +
							org.drip.analytics.support.GenericUtil.FormatSpread
								(aBond[i].calcZSpreadFromPrice
									(org.drip.param.valuation.ValuationParams.CreateValParams
										(org.drip.analytics.date.JulianDate.Today(), 0, "",
											org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
												wi._dblDate, wi._dblExerciseFactor, 1.)));
					} catch (java.lang.Exception e) {
						e.printStackTrace();
					}

					System.out.println ("TSY Spread From Price: " +
						org.drip.analytics.support.GenericUtil.FormatSpread (aBond[i].calcTSYSpreadFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("ASW Spread From Price: " + (int) aBond[i].calcParASWFromPrice
						(org.drip.param.valuation.ValuationParams.CreateValParams
							(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
								org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, wi._dblDate,
									wi._dblExerciseFactor, 1.));

					System.out.println ("Credit Basis From Price: " +
						org.drip.analytics.support.GenericUtil.FormatSpread
							(aBond[i].calcCreditBasisFromPrice
								(org.drip.param.valuation.ValuationParams.CreateValParams
									(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
										org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
											wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("PECS From Price: " +
						org.drip.analytics.support.GenericUtil.FormatSpread
							(aBond[i].calcPECSFromPrice
								(org.drip.param.valuation.ValuationParams.CreateValParams
									(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
										org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
											wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("Price From TSY Spread: " +
						org.drip.analytics.support.GenericUtil.FormatPrice (aBond[i].calcPriceFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, 0.0188)));

					System.out.println ("Yield From TSY Spread: " +
						org.drip.analytics.support.GenericUtil.FormatPrice (aBond[i].calcYieldFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, 0.0188)));

					System.out.println ("Par ASW From TSY Spread: " + (int) aBond[i].calcParASWFromTSYSpread
						(org.drip.param.valuation.ValuationParams.CreateValParams
							(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
								org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, 0.0188));

					System.out.println ("Credit Basis From TSY Spread: " +
						org.drip.analytics.support.GenericUtil.FormatSpread
							(aBond[i].calcCreditBasisFromTSYSpread
								(org.drip.param.valuation.ValuationParams.CreateValParams
									(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
										org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
											0.0188)));

					System.out.println ("PECS From TSY Spread: " +
						org.drip.analytics.support.GenericUtil.FormatSpread (aBond[i].calcPECSFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null, 0.0188)));

					System.out.println ("Theoretical Price: " +
						org.drip.analytics.support.GenericUtil.FormatPrice (aBond[i].calcPriceFromCreditBasis
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 0.)));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CustomAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample demonstrating the calculation of analytics for the set of bonds associated with the ticker
	 */

	public static final void BondTickerAPISample()
	{
		int iNumBonds = 0;
		java.lang.String strTicker = "GE";

		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.JulianDate.Today();

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.05);

		org.drip.analytics.definition.DiscountCurve dcTSY =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard (dtToday.getJulian(), "CC", 0.02,
				0.4);

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.CreditAnalytics.GetISINsForTicker
			(strTicker);

		if (s_bTickerAnalDisplay) {
			System.out.println
				("Dumping: ISIN, Bond, FIX/FLT, Yield, Z Spread, Disc Margin, TSY Spread, Credit Basis, PECS, Credit Price");

			System.out.println
				("--------------------------------------------------------------------------------------------------------");
		}

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

			if (null != bond && !bond.hasVariableCoupon() && !bond.hasBeenExercised() && !bond.hasDefaulted()
				&& bond.getMaturityDate().getJulian() > dtToday.getJulian()) {
				try {
					++iNumBonds;
					double dblPECSFromPrice = java.lang.Double.NaN;
					double dblZSpreadFromPrice = java.lang.Double.NaN;
					double dblCreditBasisFromPrice = java.lang.Double.NaN;

					double dblYieldFromPrice = org.drip.service.api.CreditAnalytics.BondYieldFromPrice
						(strISIN, dtToday, dc, 1.);

					if (!org.drip.service.api.CreditAnalytics.IsBondFloater (strISIN))
						dblZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondZSpreadFromPrice
							(strISIN, dtToday, dc, 1.);

					double dblDiscountMarginFromPrice =
						org.drip.service.api.CreditAnalytics.BondDiscountMarginFromPrice (strISIN, dtToday,
							dc, 1.);

					double dblTSYSpreadFromPrice =
						org.drip.service.api.CreditAnalytics.BondTSYSpreadFromPrice (strISIN, dtToday, dc,
							dcTSY, 1.);

					try {
						dblCreditBasisFromPrice =
							org.drip.service.api.CreditAnalytics.BondCreditBasisFromPrice (strISIN, dtToday,
								dc, cc, 1.);
					} catch (java.lang.Exception e) {
					}

					try {
						dblPECSFromPrice = org.drip.service.api.CreditAnalytics.BondPECSFromPrice (strISIN,
							dtToday, dc, cc, 1.);
					} catch (java.lang.Exception e) {
					}

					double dblBondCreditPrice = org.drip.service.api.CreditAnalytics.BondCreditPrice
						(strISIN, dtToday, dc, cc);

					if (s_bTickerAnalDisplay)
						System.out.println (strISIN + "    " + bond.getTicker() + " " +
							org.drip.analytics.support.GenericUtil.FormatPrice (bond.getCoupon
								(org.drip.analytics.date.JulianDate.Today().getJulian(),
									org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP (dc,
										cc))) + " " + bond.getMaturityDate() + "    " + (bond.isFloater() ?
											"FLOAT" : "FIXED") + "     " +
												org.drip.analytics.support.GenericUtil.FormatPrice
													(dblYieldFromPrice) + "    " +
														org.drip.analytics.support.GenericUtil.FormatSpread
														(dblZSpreadFromPrice) + "    " +
															org.drip.analytics.support.GenericUtil.FormatSpread
							(dblDiscountMarginFromPrice) + "    " +
								org.drip.analytics.support.GenericUtil.FormatSpread (dblTSYSpreadFromPrice) +
									"    " + org.drip.analytics.support.GenericUtil.FormatSpread
										(dblCreditBasisFromPrice) + "    " + (dblPECSFromPrice) + "    " +
											org.drip.analytics.support.GenericUtil.FormatPrice
												(dblBondCreditPrice));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("BondTickerAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		System.out.println ("Processed " + iNumBonds + " " + strTicker + " bonds!");

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

			try {
				double dblOutstandingAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField
					(strISIN, "OutstandingAmount");

				if (s_bTickerNotionalDisplay)
					System.out.println (strISIN + "    " + bond.getTicker() + " " +
						org.drip.analytics.support.GenericUtil.FormatPrice (bond.getCoupon
							(org.drip.analytics.date.JulianDate.Today().getJulian(),
								org.drip.param.creator.ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc)))
									+ " " + bond.getMaturityDate() + "    " +
										org.drip.analytics.support.GenericUtil.FormatPrice
											(dblOutstandingAmount, 10, 0, 1.));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondTickerAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		org.drip.analytics.date.JulianDate[] adtAscending = new org.drip.analytics.date.JulianDate[5];

		adtAscending[0] = org.drip.analytics.date.JulianDate.Today().addYears (3);

		adtAscending[1] = org.drip.analytics.date.JulianDate.Today().addYears (5);

		adtAscending[2] = org.drip.analytics.date.JulianDate.Today().addYears (10);

		adtAscending[3] = org.drip.analytics.date.JulianDate.Today().addYears (30);

		adtAscending[4] = org.drip.analytics.date.JulianDate.Today().addYears (60);

		java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> mapOutstandingNotional =
			org.drip.service.api.CreditAnalytics.GetIssuerAggregateOutstandingNotional
				(org.drip.analytics.date.JulianDate.Today(), strTicker, adtAscending);

		if (s_bCumulativeTickerNotionalDisplay) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.lang.Double> me :
				mapOutstandingNotional.entrySet())
				System.out.println ("[" + org.drip.analytics.date.JulianDate.Today() + "=>" + me.getKey() +
					"] = " + me.getValue());
		}
	}

	/**
	 * Sample demonstrating the calculation of the bond's EOD measures from price
	 */

	public static final void BondEODMeasuresAPISample()
	{
		java.lang.String strISIN = "008686AA5"; // Amortizer
		double dblEODPrice = 1.0;
		double dblEODConvexityFromPrice = java.lang.Double.NaN;
		double dblEODCreditBasisFromPrice = java.lang.Double.NaN;
		double dblEODPECSFromPrice = java.lang.Double.NaN;
		double dblEODDiscountMarginFromPrice = java.lang.Double.NaN;
		double dblEODDurationFromPrice = java.lang.Double.NaN;
		double dblEODGSpreadFromPrice = java.lang.Double.NaN;
		double dblEODISpreadFromPrice = java.lang.Double.NaN;
		double dblEODOASFromPrice = java.lang.Double.NaN;
		double dblEODParASWFromPrice = java.lang.Double.NaN;
		double dblEODTSYSpreadFromPrice = java.lang.Double.NaN;
		double dblEODYieldFromPrice = java.lang.Double.NaN;
		double dblEODZSpreadFromPrice = java.lang.Double.NaN;

		org.drip.analytics.date.JulianDate dtEOD = org.drip.analytics.date.JulianDate.CreateFromYMD (2011,
			12, 16);

		System.out.println ("Price measures for " + org.drip.service.api.CreditAnalytics.GetBondStringField
			(strISIN, "Description"));

		try {
			dblEODConvexityFromPrice = org.drip.service.api.CreditAnalytics.BondEODConvexityFromPrice
				(strISIN, dtEOD, dblEODPrice);

			try {
				dblEODCreditBasisFromPrice = org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			try {
				dblEODPECSFromPrice = org.drip.service.api.CreditAnalytics.BondEODPECSFromPrice (strISIN,
					dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODDiscountMarginFromPrice =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromPrice (strISIN, dtEOD,
					dblEODPrice);

			dblEODDurationFromPrice = org.drip.service.api.CreditAnalytics.BondEODDurationFromPrice (strISIN,
				dtEOD, dblEODPrice);

			try {
				dblEODGSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODISpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODISpreadFromPrice (strISIN,
				dtEOD, dblEODPrice);

			dblEODOASFromPrice = org.drip.service.api.CreditAnalytics.BondEODOASFromPrice (strISIN, dtEOD,
				dblEODPrice);

			dblEODParASWFromPrice = org.drip.service.api.CreditAnalytics.BondEODParASWFromPrice (strISIN,
				dtEOD, dblEODPrice);

			try {
				dblEODTSYSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODTSYSpreadFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODYieldFromPrice = org.drip.service.api.CreditAnalytics.BondEODYieldFromPrice (strISIN,
				org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 12, 16), dblEODPrice);

			dblEODZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromPrice (strISIN,
				dtEOD, dblEODPrice);

			if (s_bBondEODMeasuresFromPrice) {
				System.out.println ("EOD Convexity From Price: " + dblEODConvexityFromPrice);

				System.out.println ("EOD Credit Basis From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODCreditBasisFromPrice));

				System.out.println ("EOD PECS From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODPECSFromPrice));

				System.out.println ("EOD Discount Margin From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODDiscountMarginFromPrice));

				System.out.println ("EOD Duration From Price: " + dblEODDurationFromPrice);

				System.out.println ("EOD G Spread From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODGSpreadFromPrice));

				System.out.println ("EOD I Spread From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODISpreadFromPrice));

				System.out.println ("EOD OAS From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODOASFromPrice));

				System.out.println ("EOD Par ASW From Price: " + (int) dblEODParASWFromPrice);

				System.out.println ("EOD TSY Spread From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODTSYSpreadFromPrice));

				System.out.println ("EOD Yield From Price: " +
					org.drip.analytics.support.GenericUtil.FormatPrice (dblEODYieldFromPrice));

				System.out.println ("EOD Z Spread From Price: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODZSpreadFromPrice));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double dblEODYield = 0.0782;
		double dblEODConvexityFromYield = java.lang.Double.NaN;
		double dblEODCreditBasisFromYield = java.lang.Double.NaN;
		double dblEODPECSFromYield = java.lang.Double.NaN;
		double dblEODDiscountMarginFromYield = java.lang.Double.NaN;
		double dblEODDurationFromYield = java.lang.Double.NaN;
		double dblEODGSpreadFromYield = java.lang.Double.NaN;
		double dblEODISpreadFromYield = java.lang.Double.NaN;
		double dblEODOASFromYield = java.lang.Double.NaN;
		double dblEODParASWFromYield = java.lang.Double.NaN;
		double dblEODPriceFromYield = java.lang.Double.NaN;
		double dblEODTSYSpreadFromYield = java.lang.Double.NaN;
		double dblEODZSpreadFromYield = java.lang.Double.NaN;

		System.out.println ("\nYield measures for " + org.drip.service.api.CreditAnalytics.GetBondStringField
			(strISIN, "Description"));

		try {
			dblEODConvexityFromYield = org.drip.service.api.CreditAnalytics.BondEODConvexityFromYield
				(strISIN, dtEOD, dblEODYield);

			try {
				dblEODCreditBasisFromYield = org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			try {
				dblEODPECSFromYield = org.drip.service.api.CreditAnalytics.BondEODPECSFromYield (strISIN,
					dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODDiscountMarginFromYield =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromYield (strISIN, dtEOD,
					dblEODYield);

			dblEODDurationFromYield = org.drip.service.api.CreditAnalytics.BondEODDurationFromYield (strISIN,
				dtEOD, dblEODYield);

			try {
				dblEODGSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODISpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODISpreadFromYield (strISIN,
				dtEOD, dblEODYield);

			dblEODOASFromYield = org.drip.service.api.CreditAnalytics.BondEODOASFromYield (strISIN, dtEOD,
				dblEODYield);

			dblEODParASWFromYield = org.drip.service.api.CreditAnalytics.BondEODParASWFromYield (strISIN,
				dtEOD, dblEODYield);

			dblEODPriceFromYield = org.drip.service.api.CreditAnalytics.BondEODPriceFromYield (strISIN,
				dtEOD, dblEODYield);

			try {
				dblEODTSYSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODTSYSpreadFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODZSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromYield (strISIN,
				dtEOD, dblEODYield);

			if (s_bBondEODMeasuresFromYield) {
				System.out.println ("EOD Convexity From Yield: " + dblEODConvexityFromYield);

				System.out.println ("EOD Credit Basis From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODCreditBasisFromYield));

				System.out.println ("EOD PECS From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODPECSFromYield));

				System.out.println ("EOD Discount Margin From Yield: " +
						org.drip.analytics.support.GenericUtil.FormatSpread (dblEODDiscountMarginFromYield));

				System.out.println ("EOD Duration From Yield: " + dblEODDurationFromYield);

				System.out.println ("EOD G Spread From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODGSpreadFromYield));

				System.out.println ("EOD I Spread From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODISpreadFromYield));

				System.out.println ("EOD OAS From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODOASFromYield));

				System.out.println ("EOD Par ASW From Yield: " + (int) dblEODParASWFromYield);

				System.out.println ("EOD Price From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatPrice (dblEODPriceFromYield));

				System.out.println ("EOD TSY Spread From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODTSYSpreadFromYield));

				System.out.println ("EOD Z Spread From Yield: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODZSpreadFromYield));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double dblEODTSYSpread = 0.0618;
		double dblEODConvexityFromTSYSpread = java.lang.Double.NaN;
		double dblEODCreditBasisFromTSYSpread = java.lang.Double.NaN;
		double dblEODPECSFromTSYSpread = java.lang.Double.NaN;
		double dblEODDiscountMarginFromTSYSpread = java.lang.Double.NaN;
		double dblEODDurationFromTSYSpread = java.lang.Double.NaN;
		double dblEODGSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblEODISpreadFromTSYSpread = java.lang.Double.NaN;
		double dblEODOASFromTSYSpread = java.lang.Double.NaN;
		double dblEODParASWFromTSYSpread = java.lang.Double.NaN;
		double dblEODPriceFromTSYSpread = java.lang.Double.NaN;
		double dblEODYieldFromTSYSpread = java.lang.Double.NaN;
		double dblEODZSpreadFromTSYSpread = java.lang.Double.NaN;

		System.out.println ("\nTSY Spread measures for " +
			org.drip.service.api.CreditAnalytics.GetBondStringField (strISIN, "Description"));

		try {
			dblEODConvexityFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODConvexityFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODCreditBasisFromTSYSpread =
					org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromTSYSpread (strISIN, dtEOD,
						dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			try {
				dblEODPECSFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODPECSFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODDiscountMarginFromTSYSpread =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromTSYSpread (strISIN, dtEOD,
					dblEODTSYSpread);

			dblEODDurationFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODDurationFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODGSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODISpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODISpreadFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			dblEODOASFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODOASFromTSYSpread (strISIN,
				dtEOD, dblEODTSYSpread);

			dblEODParASWFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODParASWFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			dblEODPriceFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODPriceFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODYieldFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODYieldFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODZSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			if (s_bBondEODMeasuresFromTSYSpread) {
				System.out.println ("EOD Convexity From TSY Spread: " + dblEODConvexityFromTSYSpread);

				System.out.println ("EOD Credit Basis From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODCreditBasisFromTSYSpread));

				System.out.println ("EOD PECS From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODPECSFromTSYSpread));

				System.out.println ("EOD Discount Margin From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODDiscountMarginFromTSYSpread));

				System.out.println ("EOD Duration From TSY Spread: " + dblEODDurationFromTSYSpread);

				System.out.println ("EOD G Spread From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODGSpreadFromTSYSpread));

				System.out.println ("EOD I Spread From TSYSpread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODISpreadFromTSYSpread));

				System.out.println ("EOD OAS From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODOASFromTSYSpread));

				System.out.println ("EOD Par ASW From TSY Spread: " + (int) dblEODParASWFromTSYSpread);

				System.out.println ("EOD Price From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatPrice (dblEODPriceFromTSYSpread));

				System.out.println ("EOD Yield From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODYieldFromTSYSpread));

				System.out.println ("EOD Z Spread From TSY Spread: " +
					org.drip.analytics.support.GenericUtil.FormatSpread (dblEODZSpreadFromTSYSpread));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}
	}

	/**
	 * Sample demonstrating the calculation of the CDS EOD measures from price
	 */

	public static final void CDSEODMeasuresAPISample()
	{
		org.drip.analytics.date.JulianDate dtEOD = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 7,
			21);

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateSNAC
			(org.drip.analytics.date.JulianDate.Today(), "5Y", 0.1, "813796");

		java.util.Map<java.lang.String, java.lang.Double> mapEODCDSMeasures =
			org.drip.service.api.CreditAnalytics.GetEODCDSMeasures (cds, dtEOD);

		if (s_bEODCDSMeasures) {
			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapEODCDSMeasures.entrySet())
				System.out.println (me.getKey() + " => " + me.getValue());
		}
	}

	/**
	 * Sample demonstrating the retrieval of the bond's static fields
	 */

	public static final void BondStaticAPISample()
	{
		java.lang.String strBondISIN = "US001383CA43";
		boolean bIsBearer = true;
		boolean bIsCallable = true;
		boolean bDefaulted = true;
		boolean bExercised = true;
		boolean bIsFloater = true;
		boolean bIsPerpetual = true;
		boolean bIsPrivatePlacement = false;
		boolean bIsPutable = true;
		boolean bIsRegistered = false;
		boolean bIsReverseConvertible = true;
		boolean bIsSinkable = true;
		boolean bIsStructuredNote = true;
		boolean bIsTradeStatus = false;
		boolean bIsUnitTraded = false;
		double dblCoupon = java.lang.Double.NaN;
		double dblCurrentCoupon = java.lang.Double.NaN;
		double dblFloatSpread = java.lang.Double.NaN;
		double dblIssueAmount = java.lang.Double.NaN;
		double dblIssuePrice = java.lang.Double.NaN;
		double dblMinimumIncrement = java.lang.Double.NaN;
		double dblMinimumPiece = java.lang.Double.NaN;
		double dblOutstandingAmount = java.lang.Double.NaN;
		double dblParAmount = java.lang.Double.NaN;
		double dblRedemptionValue = java.lang.Double.NaN;
		int iCouponFrequency = 1;

		java.lang.String strAccrualDC = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"AccrualDC");

		java.lang.String strBBG_ID = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"BBG_ID");

		java.lang.String strBBGParent = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"BBGParent");

		java.lang.String strBBGUniqueID = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "BBGUniqueID");

		java.lang.String strCalculationType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CalculationType");

		java.lang.String strCDRCountryCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CDRCountryCode");

		java.lang.String strCDRSettleCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CDRSettleCode");

		java.lang.String strCollateralType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CollateralType");

		java.lang.String strCountryOfDomicile = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfDomicile");

		java.lang.String strCountryOfGuarantor = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfGuarantor");

		java.lang.String strCountryOfIncorporation = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfIncorporation");

		java.lang.String strCouponCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CouponCurrency");

		java.lang.String strCouponDC = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"CouponDC");

		java.lang.String strCouponType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CouponType");

		java.lang.String strCreditCurve = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CreditCurve");

		java.lang.String strCUSIP = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"CUSIP");

		java.lang.String strDescription = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "Description");

		java.lang.String strEDSFCurve = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"EDSFCurve");

		java.lang.String strExchangeCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "ExchangeCode");

		java.lang.String strFitch = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Fitch");

		java.lang.String strFloatCouponConvention = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "FloatCouponConvention");

		java.lang.String strIndustryGroup = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustryGroup");

		java.lang.String strIndustrySector = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustrySector");

		java.lang.String strIndustrySubgroup = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustrySubgroup");

		java.lang.String strIRCurve = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"IRCurve");

		java.lang.String strISIN = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"ISIN");

		java.lang.String strIssuer = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Issuer");

		java.lang.String strIssuerCategory = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCategory");

		java.lang.String strIssuerCountry = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCountry");

		java.lang.String strIssuerCountryCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCountryCode");

		java.lang.String strIssuerIndustry = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerIndustry");

		java.lang.String strLeadManager = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "LeadManager");

		java.lang.String strLongCompanyName = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "LongCompanyName");

		java.lang.String strMarketIssueType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "MarketIssueType");

		java.lang.String strMaturityType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "MaturityType");

		java.lang.String strMoody = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Moody");

		java.lang.String strName = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Name");

		java.lang.String strRateIndex = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"RateIndex");

		java.lang.String strRedemptionCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "RedemptionCurrency");

		java.lang.String strSecurityType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "SecurityType");

		java.lang.String strSeniorSub = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"SeniorSub");

		java.lang.String strSeries = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Series");

		java.lang.String strSnP = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"SnP");

		java.lang.String strShortName = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"ShortName");

		java.lang.String strTicker = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Ticker");

		java.lang.String strTradeCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "TradeCurrency");

		java.lang.String strTreasuryCurve = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "TreasuryCurve");

		try {
			bIsBearer = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Bearer");

			bIsCallable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Callable");

			bDefaulted = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Defaulted");

			bExercised = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Exercised");

			bIsFloater = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Floater");

			bIsPerpetual = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"Perpetual");

			bIsPrivatePlacement = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"PrivatePlacement");

			bIsPutable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Putable");

			bIsRegistered = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"Registered");

			bIsReverseConvertible = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"ReverseConvertible");

			bIsSinkable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Sinkable");

			bIsStructuredNote = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"StructuredNote");

			bIsTradeStatus = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"TradeStatus");

			bIsUnitTraded = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"UnitTraded");

			dblCoupon = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN, "Coupon");

			dblCurrentCoupon = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"CurrentCoupon");

			dblFloatSpread = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"FloatSpread");

			dblIssueAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"IssueAmount");

			dblIssuePrice = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"IssuePrice");

			dblMinimumIncrement = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"MinimumIncrement");

			dblMinimumPiece = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"MinimumPiece");

			dblOutstandingAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"OutstandingAmount");

			dblParAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"ParAmount");

			dblRedemptionValue = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"RedemptionValue");

			iCouponFrequency = org.drip.service.api.CreditAnalytics.GetBondIntegerField (strBondISIN,
				"Frequency");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondStaticAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtAccrualStart =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "AccrualStartDate");

		org.drip.analytics.date.JulianDate dtAnnounce = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "AnnounceDate");

		org.drip.analytics.date.JulianDate dtFirstCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FirstCouponDate");

		org.drip.analytics.date.JulianDate dtFirstSettle =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FirstSettleDate");

		org.drip.analytics.date.JulianDate dtFinalMaturity =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FinalMaturity");

		org.drip.analytics.date.JulianDate dtIssue = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "IssueDate");

		org.drip.analytics.date.JulianDate dtMaturity = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "Maturity");

		org.drip.analytics.date.JulianDate dtNextCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "NextCouponDate");

		org.drip.analytics.date.JulianDate dtPenultimateCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "PenultimateCouponDate");

		org.drip.analytics.date.JulianDate dtPreviousCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "PreviousCouponDate");

		if (s_bStaticDisplay) {
			System.out.println ("AccrualDC: " + strAccrualDC);

			System.out.println ("BBG_ID: " + strBBG_ID);

			System.out.println ("BBGParent: " + strBBGParent);

			System.out.println ("BBGUniqueID: " + strBBGUniqueID);

			System.out.println ("CalculationType: " + strCalculationType);

			System.out.println ("CDRCountryCode: " + strCDRCountryCode);

			System.out.println ("CDRSettleCode: " + strCDRSettleCode);

			System.out.println ("CollateralType: " + strCollateralType);

			System.out.println ("CountryOfDomicile: " + strCountryOfDomicile);

			System.out.println ("CountryOfGuarantor: " + strCountryOfGuarantor);

			System.out.println ("CountryOfIncorporation: " + strCountryOfIncorporation);

			System.out.println ("CouponCurrency: " + strCouponCurrency);

			System.out.println ("CouponDC: " + strCouponDC);

			System.out.println ("CouponType: " + strCouponType);

			System.out.println ("CreditCurve: " + strCreditCurve);

			System.out.println ("CUSIP: " + strCUSIP);

			System.out.println ("Description: " + strDescription);

			System.out.println ("EDSFCurve: " + strEDSFCurve);

			System.out.println ("ExchangeCode: " + strExchangeCode);

			System.out.println ("Fitch: " + strFitch);

			System.out.println ("FloatCouponConvention: " + strFloatCouponConvention);

			System.out.println ("IndustryGroup: " + strIndustryGroup);

			System.out.println ("IndustrySector: " + strIndustrySector);

			System.out.println ("IndustrySubgroup: " + strIndustrySubgroup);

			System.out.println ("IRCurve: " + strIRCurve);

			System.out.println ("ISIN: " + strISIN);

			System.out.println ("Issuer: " + strIssuer);

			System.out.println ("IssuerCategory: " + strIssuerCategory);

			System.out.println ("IssuerCountry: " + strIssuerCountry);

			System.out.println ("IssuerCountryCode: " + strIssuerCountryCode);

			System.out.println ("IssuerIndustry: " + strIssuerIndustry);

			System.out.println ("LeadManager: " + strLeadManager);

			System.out.println ("LongCompanyName: " + strLongCompanyName);

			System.out.println ("MarketIssueType: " + strMarketIssueType);

			System.out.println ("MaturityType: " + strMaturityType);

			System.out.println ("Moody: " + strMoody);

			System.out.println ("Name: " + strName);

			System.out.println ("RateIndex: " + strRateIndex);

			System.out.println ("RedemptionCurrency: " + strRedemptionCurrency);

			System.out.println ("SecurityType: " + strSecurityType);

			System.out.println ("Series: " + strSeries);

			System.out.println ("SeniorSub: " + strSeniorSub);

			System.out.println ("ShortName: " + strShortName);

			System.out.println ("SnP: " + strSnP);

			System.out.println ("Ticker: " + strTicker);

			System.out.println ("TradeCurrency: " + strTradeCurrency);

			System.out.println ("TreasuryCurve: " + strTreasuryCurve);

			System.out.println ("IsBearer: " + bIsBearer);

			System.out.println ("IsCallable: " + bIsCallable);

			System.out.println ("IsDefaulted: " + bDefaulted);

			System.out.println ("IsExercised: " + bExercised);

			System.out.println ("IsFloater: " + bIsFloater);

			System.out.println ("IsPrivatePlacement: " + bIsPrivatePlacement);

			System.out.println ("IsPerpetual: " + bIsPerpetual);

			System.out.println ("IsPutable: " + bIsPutable);

			System.out.println ("IsRegistered: " + bIsRegistered);

			System.out.println ("IsReverseConvertible: " + bIsReverseConvertible);

			System.out.println ("IsSinkable: " + bIsSinkable);

			System.out.println ("IsStructuredNote: " + bIsStructuredNote);

			System.out.println ("IsTradeStatus: " + bIsTradeStatus);

			System.out.println ("IsUnitTraded: " + bIsUnitTraded);

			System.out.println ("Coupon: " + org.drip.analytics.support.GenericUtil.FormatPrice (dblCoupon));

			System.out.println ("CurrentCoupon: " + org.drip.analytics.support.GenericUtil.FormatPrice
				(dblCurrentCoupon));

			System.out.println ("FloatSpread: " + org.drip.analytics.support.GenericUtil.FormatSpread
				(dblFloatSpread));

			System.out.println ("IssueAmount: " + dblIssueAmount);

			System.out.println ("IssuePrice: " + dblIssuePrice);

			System.out.println ("MinimumIncrement: " + dblMinimumIncrement);

			System.out.println ("MinimumPiece: " + dblMinimumPiece);

			System.out.println ("OutstandingAmount: " + dblOutstandingAmount);

			System.out.println ("ParAmount: " + dblParAmount);

			System.out.println ("RedemptionValue: " + dblRedemptionValue);

			System.out.println ("Coupon Freq: " + iCouponFrequency);

			System.out.println ("AccrualStart: " + dtAccrualStart);

			System.out.println ("Announce: " + dtAnnounce);

			System.out.println ("FinalMaturity: " + dtFinalMaturity);

			System.out.println ("FirstCoupon: " + dtFirstCoupon);

			System.out.println ("FirstSettle: " + dtFirstSettle);

			System.out.println ("Issue: " + dtIssue);

			System.out.println ("Maturity: " + dtMaturity);

			System.out.println ("NextCoupon: " + dtNextCoupon);

			System.out.println ("PenultimateCoupon: " + dtPenultimateCoupon);

			System.out.println ("PrevCoupon: " + dtPreviousCoupon);
		}
	}

	/**
	 * API demonstrating how to calibrate a CDS curve from CDS and bond quotes
	 */

	public static void BondCDSCurveCalibration()
	{
		double dblCreditPrice = java.lang.Double.NaN;

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "DKK", 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		org.drip.product.credit.BondComponent bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed
			("CCCalibBond", "DKK", 0.05, 2, "30/360", org.drip.analytics.date.JulianDate.CreateFromYMD (2008,
				9, 21), org.drip.analytics.date.JulianDate.CreateFromYMD (2023, 9, 20), null, null);

		org.drip.param.definition.ComponentMarketParams cmp =
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond,
					org.drip.analytics.date.JulianDate.Today(), 0.04));

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateValParams
				(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
					org.drip.analytics.daycount.Convention.DR_ACTUAL);
		try {
			dblCreditPrice = bond.calcPriceFromCreditBasis (valParams, cmp, null,
				bond.getMaturityDate().getJulian(), 1., 0.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateCDS
			(org.drip.analytics.date.JulianDate.Today(), org.drip.analytics.date.JulianDate.Today().addTenor
				("5Y"), 0.1, "DKK", 0.40, "CC", "DKK", true);

		if (s_bCDSBondCreditCurve) System.out.println ("Credit Price: " + dblCreditPrice);

		org.drip.product.definition.CalibratableComponent[] aCalibInst = new
			org.drip.product.definition.CalibratableComponent[2];
		java.lang.String[] astrCalibMeasure = new java.lang.String[2];
		double[] adblQuotes = new double[2];
		aCalibInst[0] = cds;
		aCalibInst[1] = bond;
		astrCalibMeasure[0] = "FairPremium";
		astrCalibMeasure[1] = "FairPrice";
		adblQuotes[0] = 100.;
		adblQuotes[1] = dblCreditPrice;
		org.drip.analytics.definition.CreditCurve ccCalib = null;

		try {
			org.drip.analytics.calibration.CreditCurveScenarioGenerator ccsg = new
				org.drip.analytics.calibration.CreditCurveScenarioGenerator (aCalibInst);

			ccCalib = ccsg.createCC ("CC", valParams, dc, null, null, adblQuotes, 0.40, astrCalibMeasure,
				null, new org.drip.param.valuation.QuotingParams ("30/360", 2, true, null, "USD", false),
					false);

			if (s_bCDSBondCreditCurve)
				System.out.println ("Surv (2021, 1, 14): " + ccCalib.getSurvival
					(org.drip.analytics.date.JulianDate.CreateFromYMD (2021, 1, 14)));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bCDSBondCreditCurve) {
			try {
				System.out.println (cds.getPrimaryCode() + " => " + cds.calcMeasureValue (valParams,
					org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
						org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc,
							null, null, ccCalib, null, null,
								org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond,
									org.drip.analytics.date.JulianDate.Today(), 0.04)), null,
										"FairPremium"));

				System.out.println (bond.getPrimaryCode() + " => " + bond.calcPriceFromCreditBasis
					(valParams,
						org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc,
							null, null, ccCalib, null, null,
								org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond,
									org.drip.analytics.date.JulianDate.Today(), 0.04)), null,
										bond.getMaturityDate().getJulian(), 1., 0.));
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond basket API
	 */

	public static final void BasketBondAPISample()
	{
		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.JulianDate.Today();

		org.drip.product.definition.BasketProduct bb = org.drip.service.api.CreditAnalytics.MakeBondBasket
			("SLMA_ETF", new java.lang.String[] {"US78490FVJ55", "US78490FWD76", "US78490FVL02",
				"US78442FAZ18", "US78490FTL30"}, new double[] {1., 2., 3., 4., 5.}, dtToday, 100.);

		org.drip.param.definition.BasketMarketParams bmp =
			org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams();

		bmp.addDC ("USD", org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD",
			0.04));

		java.util.Map<java.lang.String, java.lang.Double> mapResult = null;

		try {
			mapResult = bb.value (org.drip.param.valuation.ValuationParams.CreateValParams (dtToday, 0,
				"USD", org.drip.analytics.daycount.Convention.DR_ACTUAL), new
					org.drip.param.pricer.PricerParams (7, null, false,
						org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_FULL_COUPON), bmp, null);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BasketBondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bBasketBond) {
			System.out.println ("Fair Clean Price: " + org.drip.analytics.support.GenericUtil.FormatPrice
				(mapResult.get ("FairCleanPV")));

			System.out.println ("Fair Yield: " + org.drip.analytics.support.GenericUtil.FormatPrice
				(mapResult.get ("Yield")));

			System.out.println ("Fair GSpread: " + org.drip.analytics.support.GenericUtil.FormatSpread
				(mapResult.get ("FairGSpread")));

			System.out.println ("Fair ZSpread: " + org.drip.analytics.support.GenericUtil.FormatSpread
				(mapResult.get ("FairZSpread")));

			System.out.println ("Fair ISpread: " + org.drip.analytics.support.GenericUtil.FormatSpread
				(mapResult.get ("FairISpread")));

			System.out.println ("Fair DV01: " + mapResult.get ("FairDV01"));

			System.out.println ("Accrued: " + mapResult.get ("Accrued"));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the FX API
	 */

	public static final void FXAPISample()
	{
		org.drip.product.params.CurrencyPair cp = null;

		try {
			cp = new org.drip.product.params.CurrencyPair ("EUR", "USD", "USD", 10000.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.Random rand = new java.util.Random();

		org.drip.analytics.definition.DiscountCurve dcUSD =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "USD", 0.05);

		org.drip.analytics.definition.DiscountCurve dcEUR =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.JulianDate.Today(), "EUR", 0.04);

		double dblFXSpot = 1.40;
		double dblFXFwdMarket = 1.40;
		double[] adblNodes = new double[5];
		double[] adblFXFwd = new double[5];
		boolean[] abIsPIP = new boolean[5];
		double dblFXFwd = java.lang.Double.NaN;
		double dblFXFwdPIP = java.lang.Double.NaN;
		double dblDCEURBasis = java.lang.Double.NaN;
		double dblDCUSDBasis = java.lang.Double.NaN;
		org.drip.analytics.curve.DerivedFXForward fxCurve = null;

		for (int i = 0; i < 5; ++i) {
			abIsPIP[i] = false;
			adblFXFwd[i] = dblFXSpot - (i + 1) * 0.01 * rand.nextDouble();

			adblNodes[i] = org.drip.analytics.date.JulianDate.Today().addYears (i + 1).getJulian();

			if (s_bFXFwd)
				System.out.println ("Input " + cp.getCode() + "[" + (i + 1) + "] = " +
					org.drip.analytics.support.GenericUtil.FormatDouble (adblFXFwd[i]));
		}

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateValParams
				(org.drip.analytics.date.JulianDate.Today(), 0, "USD",
					org.drip.analytics.daycount.Convention.DR_ACTUAL);

		org.drip.product.definition.FXForward fxfwd =
			org.drip.product.creator.FXForwardBuilder.CreateFXForward (cp,
				org.drip.analytics.date.JulianDate.Today(), "1Y");

		try {
			dblFXFwd = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, false);

			dblFXFwdPIP = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, true);

			dblDCEURBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, false);

			dblDCUSDBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, true);

			fxCurve = new org.drip.analytics.curve.DerivedFXForward (cp,
				org.drip.analytics.date.JulianDate.Today(), dblFXSpot, adblNodes, adblFXFwd, abIsPIP);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double[] adblFullUSDBasis = fxCurve.getFullBasis (valParams, dcEUR, dcUSD, true);

		double[] adblFullEURBasis = fxCurve.getFullBasis (valParams, dcEUR, dcUSD, false);

		double[] adblBootstrappedUSDBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, true);

		double[] adblBootstrappedEURBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, false);

		double[] adblFXFwdFromUSDBasis = null;
		double[] adblFXFwdFromEURBasis = null;
		org.drip.analytics.definition.FXBasisCurve fxEURBasisCurve = null;
		org.drip.analytics.definition.FXBasisCurve fxUSDBasisCurve = null;

		try {
			fxUSDBasisCurve = org.drip.analytics.creator.FXBasisCurveBuilder.CreateFXBasisCurve (cp,
				org.drip.analytics.date.JulianDate.Today(), dblFXSpot, adblNodes, adblFullUSDBasis, false);

			adblFXFwdFromUSDBasis = fxUSDBasisCurve.getFullFXFwd (valParams, dcEUR, dcUSD, true, false);

			fxEURBasisCurve = org.drip.analytics.creator.FXBasisCurveBuilder.CreateFXBasisCurve (cp,
				org.drip.analytics.date.JulianDate.Today(), dblFXSpot, adblNodes, adblFullEURBasis, false);

			adblFXFwdFromEURBasis = fxEURBasisCurve.getFullFXFwd (valParams, dcEUR, dcUSD, false, false);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bFXFwd) {
			System.out.println (cp.getCode() + "[1Y]= " + dblFXFwd);

			System.out.println (cp.getCode() + "[1Y](pip)= " +
				org.drip.analytics.support.GenericUtil.FormatDouble (dblFXFwdPIP));

			System.out.println ("EUR Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.analytics.support.GenericUtil.FormatDouble (dblDCEURBasis));

			System.out.println ("USD Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.analytics.support.GenericUtil.FormatDouble (dblDCUSDBasis));

			for (int i = 0; i < adblFullUSDBasis.length; ++i) {
				System.out.println ("FullUSDBasis[" + (i + 1) + "Y]=" +
					org.drip.analytics.support.GenericUtil.FormatSpread (adblFullUSDBasis[i]));

				System.out.println ("FullEURBasis[" + (i + 1) + "Y]=" +
					org.drip.analytics.support.GenericUtil.FormatSpread (adblFullEURBasis[i]));
			}

			for (int i = 0; i < adblBootstrappedUSDBasis.length; ++i) {
				System.out.println ("Bootstrapped USDBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.analytics.support.GenericUtil.FormatSpread
						(adblBootstrappedUSDBasis[i]));

				System.out.println ("Bootstrapped EURBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.analytics.support.GenericUtil.FormatSpread
						(adblBootstrappedEURBasis[i]));
			}

			for (int i = 0; i < adblFXFwdFromUSDBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped USD Basis: " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.analytics.support.GenericUtil.FormatDouble
						(adblFXFwdFromUSDBasis[i]));

			for (int i = 0; i < adblFXFwdFromEURBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped EUR Basis: " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.analytics.support.GenericUtil.FormatDouble
						(adblFXFwdFromEURBasis[i]));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the CDX API
	 */

	public static final void BasketCDSAPISample()
	{
		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.JulianDate.Today();

		org.drip.product.definition.BasketProduct bpCDX = org.drip.service.api.CreditAnalytics.MakeCDX
			("CDX.NA.IG", 17, "5Y");

		org.drip.product.definition.BasketProduct bpCDXOTR = org.drip.service.api.CreditAnalytics.MakeCDX
			("CDX.NA.IG", dtToday, "5Y");

		java.util.Set<java.lang.String> setstrCDXNames =
			org.drip.service.env.StandardCDXManager.GetCDXNames();

		java.util.Map<java.lang.String, java.lang.String> mapCDXDescr =
			org.drip.service.env.StandardCDXManager.GetCDXDescriptions();

		org.drip.product.definition.BasketProduct bpPresetOTR =
			org.drip.service.env.StandardCDXManager.GetOnTheRun ("CDX.EM", dtToday.subtractTenor ("1Y"),
				"5Y");

		org.drip.product.definition.BasketProduct bpPreLoadedOTR =
			org.drip.service.env.StandardCDXManager.GetOnTheRun ("ITRAXX.ENERGY", dtToday.subtractTenor
				("7Y"), "5Y");

		java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Integer> mapCDXSeries =
			org.drip.service.env.StandardCDXManager.GetCDXSeriesMap ("ITRAXX.ENERGY");

		if (s_bBasketCDS) {
			System.out.println (bpCDX.getName() + ": " + bpCDX.getEffectiveDate() + "=>" +
				bpCDX.getMaturityDate());

			System.out.println (bpCDXOTR.getName() + ": " + bpCDXOTR.getEffectiveDate() + "=>" +
				bpCDXOTR.getMaturityDate());
		}

		if (s_bStandardCDXNames) {
			int i = 0;

			for (java.lang.String strCDX : setstrCDXNames)
				System.out.println ("CDX[" + i++ + "]: " + strCDX);
		}

		if (s_bNamedCDXMap) {
			for (java.util.Map.Entry<java.lang.String, java.lang.String> meCDXDescr : mapCDXDescr.entrySet())
				System.out.println ("[" + meCDXDescr.getKey() + "]: " + meCDXDescr.getValue());
		}

		if (s_bOnTheRun) {
			System.out.println (bpPresetOTR.getName() + ": " + bpPresetOTR.getEffectiveDate() + "=>" +
				bpPresetOTR.getMaturityDate());

			System.out.println (bpPreLoadedOTR.getName() + ": " + bpPreLoadedOTR.getEffectiveDate() + "=>" +
				bpPreLoadedOTR.getMaturityDate());
		}

		if (s_bCDXSeries) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.lang.Integer> me :
				mapCDXSeries.entrySet())
				System.out.println ("ITRAXX.ENERGY[" + me.getValue() + "]: " + me.getKey());
		}
	}

	public static final void main (
		final java.lang.String astrArgs[])
	{
		java.lang.String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		// java.lang.String strConfig = "";

		if (null != astrArgs) {
			if (1 == astrArgs.length)
				strConfig = astrArgs[0];
			else {
				if (2 <= astrArgs.length) {
					if (astrArgs[0].equalsIgnoreCase ("-config") || astrArgs[0].equalsIgnoreCase ("-cfg") ||
						astrArgs[0].equalsIgnoreCase ("-conf"))
						strConfig = astrArgs[1];
				}
			}
		}

		boolean bFIInit = org.drip.service.api.CreditAnalytics.Init (strConfig);

		if (!bFIInit) System.out.println ("Cannot fully init FI!");

		CalenderAPISample();

		DayCountAPISample();

		if (bFIInit) {
			DiscountCurveAPISample();

			CreditCurveAPISample();
		}

		CDSAPISample();

		if (bFIInit) BondAPISample();

		CustomBondAPISample();

		if (bFIInit) {
			BondTickerAPISample();

			BondEODMeasuresAPISample();

			CDSEODMeasuresAPISample();

			BondStaticAPISample();
		}

		BondCDSCurveCalibration();

		FXAPISample();

		if (bFIInit) BasketBondAPISample();

		BasketCDSAPISample();
	}
}
