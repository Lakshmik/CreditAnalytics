
package org.drip.service.sample;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2011 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditLib, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.creditlib.org
 * 
 * CreditLib is a free, full featured, fixed income credit analytics library, developed with a special focus
 * 		towards the needs of the bonds and credit products community.
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
 * A fairly comprehensive sample API class demo'ing the usage of the FI functions
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FIFull {
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

	private static final org.drip.param.product.FactorSchedule MakeFSPrincipal() {
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.util.date.JulianDate dtEOSStart = org.drip.util.date.JulianDate.Today().addDays (2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
		}

		try {
			return org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final org.drip.param.product.FactorSchedule MakeFSCoupon() {
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.util.date.JulianDate dtEOSStart = org.drip.util.date.JulianDate.Today().addDays (2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
		}

		try {
			return org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
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

	public static final org.drip.product.credit.Bond CreateCustomBond (final java.lang.String strName, final
		int iBondType) {
		boolean bEOSOn = false;
		boolean bEOSAmerican = false;
		org.drip.product.credit.Bond bond = null;
		org.drip.param.product.EmbeddedOptionSchedule eosPut = null;
		org.drip.param.product.EmbeddedOptionSchedule eosCall = null;

		if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFloater (strName, "USD", "DRIPRI", 0.01,
				"30/360", org.drip.util.date.JulianDate.CreateFromYMD (2008, 9, 21),
					org.drip.util.date.JulianDate.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed (strName, "USD", 0.05, "30/360",
				org.drip.util.date.JulianDate.CreateFromYMD (2008, 9, 21),
					org.drip.util.date.JulianDate.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF == iBondType) {
			final int NUM_CF_ENTRIES = 30;
			double[] adblCouponAmount = new double[NUM_CF_ENTRIES];
			double[] adblPrincipalAmount = new double[NUM_CF_ENTRIES];
			org.drip.util.date.JulianDate[] adt = new org.drip.util.date.JulianDate[NUM_CF_ENTRIES];

			org.drip.util.date.JulianDate dtEffective = org.drip.util.date.JulianDate.CreateFromYMD (2008, 9,
				20);

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

			org.drip.util.date.JulianDate dtEOSStart = org.drip.util.date.JulianDate.Today().addDays (2);

			for (int i = 0; i < 5; ++i) {
				adblPutFactor[i] = 0.9;
				adblCallFactor[i] = 1.0;

				adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
			}

			if (bEOSAmerican) {
				eosCall = org.drip.param.product.EmbeddedOptionSchedule.fromAmerican
					(org.drip.util.date.JulianDate.Today().getJulian() + 1, adblDate, adblCallFactor, false,
						30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

				eosPut = org.drip.param.product.EmbeddedOptionSchedule.fromAmerican
					(org.drip.util.date.JulianDate.Today().getJulian(), adblDate, adblPutFactor, true, 30,
						false, java.lang.Double.NaN, "", java.lang.Double.NaN);
			} else {
				try {
					eosCall = new org.drip.param.product.EmbeddedOptionSchedule (adblDate, adblCallFactor,
						false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

					eosPut = new org.drip.param.product.EmbeddedOptionSchedule (adblDate, adblPutFactor,
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

	public static final void CalenderAPISample() {
		java.util.Set<java.lang.String> setLoc = org.drip.service.api.FI.GetHolLocations();

		org.drip.util.date.JulianDate[] adtHols = org.drip.service.api.FI.GetHolsInYear ("USD,GBP", 2011);

		org.drip.util.date.JulianDate[] adtWeekDayHols = org.drip.service.api.FI.GetWeekDayHolsInYear
			("USD,GBP", 2011);

		org.drip.util.date.JulianDate[] adtWeekendHols = org.drip.service.api.FI.GetWeekendHolsInYear
			("USD,GBP", 2011);

		int[] aiWkendDays = org.drip.service.api.FI.GetWeekendDays ("USD,GBP");

		boolean bIsHoliday = false;

		try {
			bIsHoliday = org.drip.service.api.FI.IsHoliday ("USD,GBP",
				org.drip.util.date.JulianDate.CreateFromYMD (2011, 12, 28));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("CalendarAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.List<java.lang.Double> lsHols = org.drip.analytics.daycount.DayCountBasis.HolidaySet
			(org.drip.util.date.JulianDate.Today().getJulian(),
				org.drip.util.date.JulianDate.Today().addYears (1).getJulian(), "USD,GBP");

		if (s_bPrintHolLoc) {
			System.out.println ("Num Hol Locations: " + setLoc.size());

			for (java.lang.String strLoc : setLoc)
				System.out.println (strLoc);
		}

		if (s_bPrintHolsInYear) {
			System.out.println (org.drip.util.date.JulianDate.CreateFromYMD (2011, 12, 28) +
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
					System.out.println (org.drip.util.date.JulianDate.getDayChars (aiWkendDays[i]));
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
					System.out.println (new org.drip.util.date.JulianDate (dblDate).toOracleDate());
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

	public static final void DayCountAPISample() {
		java.lang.String strDCList = org.drip.service.api.FI.GetAvailableDC();

		double dblYF = java.lang.Double.NaN;

		try {
			dblYF = org.drip.service.api.FI.YearFraction (org.drip.util.date.JulianDate.CreateFromYMD (2011,
				1, 14), org.drip.util.date.JulianDate.CreateFromYMD (2011, 2, 14), "Act/360", false, "USD");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("DayCountAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.util.date.JulianDate dtAdjusted = org.drip.service.api.FI.Adjust
			(org.drip.util.date.JulianDate.CreateFromYMD (2011, 1, 16), "USD", 0);

		org.drip.util.date.JulianDate dtRoll = org.drip.service.api.FI.RollDate
			(org.drip.util.date.JulianDate.CreateFromYMD (2011, 1, 16), "USD",
				org.drip.analytics.daycount.DayCountBasis.DR_PREV);

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

	public static final void DiscountCurveAPISample() {
		org.drip.util.date.JulianDate dt1 = org.drip.util.date.JulianDate.CreateFromYMD (2011, 12, 16);

		org.drip.util.date.JulianDate dt2 = org.drip.util.date.JulianDate.CreateFromYMD (2012, 1, 17);

		java.util.Set<java.lang.String> setstrIRCurves = org.drip.service.api.FI.GetEODIRCurveNames (dt1);

		org.drip.analytics.curve.DiscountCurve dc = org.drip.service.api.FI.LoadEODFullIRCurve ("EUR", dt1);

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve> mapDC =
			org.drip.service.api.FI.LoadEODFullIRCurves ("EUR", dt1, dt2);

		org.drip.analytics.curve.DiscountCurve dcCash = org.drip.service.api.FI.LoadEODIRCashCurve ("EUR",
			dt1);

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve> mapCashDC =
			org.drip.service.api.FI.LoadEODIRCashCurves ("EUR", dt1, dt2);

		org.drip.analytics.curve.DiscountCurve dcEDF = org.drip.service.api.FI.LoadEODEDFCurve ("EUR", dt1);

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve> mapEDFDC =
			org.drip.service.api.FI.LoadEODEDFCurves ("EUR", dt1, dt2);

		org.drip.analytics.curve.DiscountCurve dcIRS = org.drip.service.api.FI.LoadEODIRSwapCurve ("EUR",
			dt1);

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve> mapIRSDC =
			org.drip.service.api.FI.LoadEODIRSwapCurves ("EUR", dt1, dt2);

		double[] adblDF = new double[5];
		double[] adblDate = new double[5];
		double[] adblRate = new double[5];

		org.drip.util.date.JulianDate dtStart = org.drip.util.date.JulianDate.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblDF[i] = 1. - 2 * (i + 1) * (adblRate[i] = 0.05);
		}

		org.drip.analytics.curve.DiscountCurve dcFromDF = org.drip.analytics.curve.DiscountCurve.BuildFromDF
			(dtStart, "EUR", adblDate, adblDF);

		org.drip.analytics.curve.DiscountCurve dcFromRate = org.drip.analytics.curve.DiscountCurve.CreateDC
			(dtStart, "EUR", adblDate, adblRate);

		org.drip.analytics.curve.DiscountCurve dcFromFlatRate =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (dtStart, "DKK", 0.04);

		java.util.Set<java.lang.String> setstrTSYCurves = org.drip.service.api.FI.GetEODTSYCurveNames (dt1);

		org.drip.analytics.curve.DiscountCurve dcTSY = org.drip.service.api.FI.LoadEODTSYCurve ("USD", dt1);

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve> mapTSYDC =
			org.drip.service.api.FI.LoadEODTSYCurves ("USD", dt1, dt2);

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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblQuotes = dc.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCC = dc.getCalibComponents();

			for (int i = 0; i < aCC.length; ++i)
				System.out.println (aCC[i].getPrimaryCode() + " => " + adblQuotes[i]);
		}

		if (s_bPrintEODIRFullCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve>
				meDC : mapDC.entrySet()) {
				org.drip.util.date.JulianDate dt = meDC.getKey();

				org.drip.analytics.curve.DiscountCurve dcEOD = meDC.getValue();

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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblCashQuotes = dcCash.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCCCash = dcCash.getCalibComponents();

			for (int i = 0; i < aCCCash.length; ++i)
				System.out.println (aCCCash[i].getPrimaryCode() + " => " + (int) (10000. *
					adblCashQuotes[i]));
		}

		if (s_bPrintEODIRCashCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve>
				meCashDC : mapCashDC.entrySet()) {
				org.drip.util.date.JulianDate dt = meCashDC.getKey();

				org.drip.analytics.curve.DiscountCurve dcEOD = meCashDC.getValue();

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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblEDFQuotes = dcEDF.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCCEDF = dcEDF.getCalibComponents();

			for (int i = 0; i < aCCEDF.length; ++i)
				System.out.println (aCCEDF[i].getPrimaryCode() + " => " + (int) (10000. * adblEDFQuotes[i]));
		}

		if (s_bPrintEODIREDFCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve>
				meEDFDC : mapEDFDC.entrySet()) {
				org.drip.util.date.JulianDate dt = meEDFDC.getKey();

				org.drip.analytics.curve.DiscountCurve dcEOD = meEDFDC.getValue();

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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblIRSQuotes = dcIRS.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCCIRS = dcIRS.getCalibComponents();

			for (int i = 0; i < aCCIRS.length; ++i)
				System.out.println (aCCIRS[i].getPrimaryCode() + " => " + (int) (10000. * adblIRSQuotes[i]));
		}

		if (s_bPrintEODIRSwapCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve>
				meIRSDC : mapIRSDC.entrySet()) {
				org.drip.util.date.JulianDate dt = meIRSDC.getKey();

				org.drip.analytics.curve.DiscountCurve dcEOD = meIRSDC.getValue();

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblTSYQuotes = dcTSY.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCompTSY = dcTSY.getCalibComponents();

			for (int i = 0; i < aCompTSY.length; ++i)
				System.out.println (aCompTSY[i].getPrimaryCode() + " => " + (int) (10000. *
					adblTSYQuotes[i]));
		}

		if (s_bPrintEODTSYCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.DiscountCurve>
				meTSYDC : mapTSYDC.entrySet()) {
				org.drip.util.date.JulianDate dt = meTSYDC.getKey();

				org.drip.analytics.curve.DiscountCurve dcTSYEOD = meTSYDC.getValue();

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

	public static final void CreditCurveAPISample() {
		org.drip.analytics.curve.CreditCurve ccFlatHazard =
			org.drip.analytics.curve.CreditCurve.FromFlatHazard
				(org.drip.util.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4);

		double[] adblDate = new double[5];
		double[] adblSurvival = new double[5];

		org.drip.util.date.JulianDate dtStart = org.drip.util.date.JulianDate.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblSurvival[i] = 1. - 0.1 * (i + 1);
		}

		org.drip.analytics.curve.CreditCurve ccFromSurvival =
			org.drip.analytics.curve.CreditCurve.FromSurvival (dtStart.getJulian(), "CC", adblDate,
				adblSurvival, 0.4);

		java.util.Set<java.lang.String> setstrCDSCurves = org.drip.service.api.FI.GetEODCDSCurveNames
			(org.drip.util.date.JulianDate.CreateFromYMD (2011, 7, 21));

		org.drip.analytics.curve.CreditCurve ccEOD = org.drip.service.api.FI.LoadEODCDSCreditCurve ("813796",
			"USD", org.drip.util.date.JulianDate.CreateFromYMD (2011, 7, 21));

		java.util.Map <org.drip.util.date.JulianDate, org.drip.analytics.curve.CreditCurve> mapCC =
			org.drip.service.api.FI.LoadEODCDSCreditCurves ("813796", "USD",
				org.drip.util.date.JulianDate.CreateFromYMD (2011, 7, 14),
					org.drip.util.date.JulianDate.CreateFromYMD (2011, 7, 21));

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

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
			org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today().addYears (10);

			try {
				System.out.println ("CCFromEOD[" + dt.toString() + "]; Survival=" + ccEOD.getSurvival ("10Y") +
					"; Hazard=" + ccEOD.calcHazard ("10Y"));
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
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			double[] adblCDSQuotes = ccEOD.getCompQuotes();

			org.drip.product.common.CalibratableComponent[] aCompCDS = ccEOD.getCalibComponents();

			for (int i = 0; i < aCompCDS.length; ++i)
				System.out.println (aCompCDS[i].getPrimaryCode() + " => " + (int) (adblCDSQuotes[i]));
		}

		if (s_bPrintEODCDSCurves) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, org.drip.analytics.curve.CreditCurve>
				meCC : mapCC.entrySet()) {
				org.drip.util.date.JulianDate dt = meCC.getKey();

				org.drip.analytics.curve.CreditCurve ccCOB = meCC.getValue();

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

	public static final void CDSAPISample() {
		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
			(org.drip.util.date.JulianDate.Today(), "USD", 0.05);

		org.drip.analytics.curve.CreditCurve cc = org.drip.analytics.curve.CreditCurve.FromFlatHazard
			(org.drip.util.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4);

		org.drip.product.credit.CreditDefaultSwap cds = org.drip.product.credit.CreditDefaultSwap.CreateSNAC
			(org.drip.util.date.JulianDate.Today(), "5Y", 0.1, "CC");

		if (s_bCDSCouponCFDisplay) {
			org.drip.param.valuation.ValuationParams valParams =
				org.drip.param.valuation.ValuationParams.CreateValParams
					(org.drip.util.date.JulianDate.Today(), 0, "",
						org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL);

			org.drip.param.pricer.PricerParams pricerParams =
				org.drip.param.pricer.PricerParams.MakeStdPricerParams();

			System.out.println ("Acc Start       Acc End     Pay Date      Cpn DCF    Pay01    Surv01");

			System.out.println ("---------      ---------    ---------    --------- --------- --------");

			for (org.drip.analytics.period.ProductCouponPeriodCurveMeasures p : cds.getCouponFlow (valParams,
				pricerParams, org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
					(org.drip.util.date.JulianDate.Today(), "USD", 0.05),
						org.drip.analytics.curve.CreditCurve.FromFlatHazard
							(org.drip.util.date.JulianDate.Today().getJulian(), "CC", 0.02, 0.4))) {
				try {
					System.out.println (org.drip.util.date.JulianDate.fromJulian (p.getAccrualStartDate()) +
						"    " + org.drip.util.date.JulianDate.fromJulian (p.getAccrualEndDate()) + "    " +
							org.drip.util.date.JulianDate.fromJulian (p.getPayDate()) + "    " +
								org.drip.util.common.FIGen.FormatSpreadSimple (p.getCouponDCF(), 1, 4, 1.) +
									"    " + org.drip.util.common.FIGen.FormatSpreadSimple (dc.getDF
										(p.getPayDate()), 1, 4, 1.) + "    " +
											org.drip.util.common.FIGen.FormatSpreadSimple (cc.getSurvival
												(p.getPayDate()), 1, 4, 1.));
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

			for (org.drip.analytics.period.ProductLossPeriodCurveMeasures dp : cds.getLossFlow
				(org.drip.param.valuation.ValuationParams.CreateValParams 
					(org.drip.util.date.JulianDate.Today(), 0, "USD",
						org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL),
							org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
								org.drip.param.market.ComponentMarketParams.MakeCreditCMP (dc, cc)))
				System.out.println (org.drip.util.date.JulianDate.fromJulian (dp.getStartDate()) + "    " +
					org.drip.util.date.JulianDate.fromJulian (dp.getEndDate()) + "    " +
						org.drip.util.date.JulianDate.fromJulian (dp.getPayDate()) + "    " +
							org.drip.util.common.FIGen.FormatSpreadSimple (dp.getCouponDCF(), 1, 4, 1.) +
								"    " + org.drip.util.common.FIGen.FormatSpreadSimple
									(dp.getEffectiveNotional(), 1, 0, 1.) + "    " +
										org.drip.util.common.FIGen.FormatSpreadSimple
											(dp.getEffectiveRecovery(), 1, 2, 1.) + "    " +
												org.drip.util.common.FIGen.FormatSpreadSimple
													(dp.getEffectiveDF(), 1, 4, 1.)  + "    " +
														org.drip.util.common.FIGen.FormatSpreadSimple
															(dp.getStartSurvival(), 1, 4, 1.) + "    " +
																org.drip.util.common.FIGen.FormatSpreadSimple
																	(dp.getEndSurvival(), 1, 4, 1.));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond API
	 */

	public static final void BondAPISample() {
		java.util.Set<java.lang.String> setstrTickers = org.drip.service.api.FI.GetAvailableTickers();

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.FI.GetISINsForTicker ("DB");

		// java.lang.String strISIN = "219350AQ8"; // EOS
		java.lang.String strISIN = "008686AA5"; // Amortizer
		org.drip.param.valuation.QuotingParams quotingParams = null;
		boolean bInFirstPeriod = true;
		boolean bInLastPeriod = true;
		double dblGTMFromPrice = java.lang.Double.NaN;
		double dblITMFromPrice = java.lang.Double.NaN;
		double dblYTMFromPrice = java.lang.Double.NaN;
		double dblZTMFromPrice = java.lang.Double.NaN;
		double dblTSYTMFromPrice = java.lang.Double.NaN;
		double dblYieldFromPrice = java.lang.Double.NaN;
		double dblBondCreditPrice = java.lang.Double.NaN;
		double dblParASWFromPrice = java.lang.Double.NaN;
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
		double dblCreditBasisFromTSYSpread = java.lang.Double.NaN;

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

		org.drip.product.credit.Bond bond = org.drip.service.api.FI.GetBond (strISIN);

		org.drip.util.date.JulianDate dtToday = org.drip.util.date.JulianDate.Today();

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateValParams (dtToday, 0, "",
				org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL);

		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
			(org.drip.util.date.JulianDate.Today(), "USD", 0.03);

		org.drip.analytics.curve.DiscountCurve dcTSY =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.04);

		org.drip.analytics.curve.CreditCurve cc = org.drip.analytics.curve.CreditCurve.FromFlatHazard
			(dtToday.getJulian(), "CC", 0.02, 0.);

		org.drip.param.valuation.WorkoutInfo wi = org.drip.service.api.FI.BondWorkoutInfoFromPrice (strISIN,
			dtToday, dc, 1.);

		try {
			dblYieldFromPrice = org.drip.service.api.FI.BondYieldFromPrice (strISIN, dtToday, dc, 1.);

			dblYTMFromPrice = org.drip.service.api.FI.BondYTMFromPrice (strISIN, valParams, dc, 1.,
				quotingParams);

			dblZSpreadFromPrice = org.drip.service.api.FI.BondZSpreadFromPrice (strISIN, dtToday, dc, 1.);

			dblZTMFromPrice = org.drip.service.api.FI.BondZTMFromPrice (strISIN, valParams, dc, 1.,
				quotingParams);

			dblISpreadFromPrice = org.drip.service.api.FI.BondISpreadFromPrice (strISIN, dtToday, dc, 1.);

			dblITMFromPrice = org.drip.service.api.FI.BondITMFromPrice (strISIN, valParams, dc, 1.,
				quotingParams);

			dblTSYSpreadFromPrice = org.drip.service.api.FI.BondTSYSpreadFromPrice (strISIN, dtToday, dc,
				dcTSY, 1.);

			dblTSYTMFromPrice = org.drip.service.api.FI.BondTSYTMFromPrice (strISIN, valParams, dc, dcTSY,
				1., quotingParams);

			dblGSpreadFromPrice = org.drip.service.api.FI.BondGSpreadFromPrice (strISIN, dtToday, dc, dcTSY,
				1.);

			dblGTMFromPrice = org.drip.service.api.FI.BondGTMFromPrice (strISIN, valParams, dc, dcTSY, 1.,
				quotingParams);

			dblParASWFromPrice = org.drip.service.api.FI.BondParASWFromPrice (strISIN, dtToday, dc, 1.);

			dblParASWTMFromPrice = org.drip.service.api.FI.BondParASWTMFromPrice (strISIN, valParams, dc, 1.,
				quotingParams);

			dblCreditBasisFromPrice = org.drip.service.api.FI.BondCreditBasisFromPrice (strISIN, dtToday, dc,
				cc, 1.);

			dblCreditBasisTMFromPrice = org.drip.service.api.FI.BondCreditBasisTMFromPrice (strISIN,
				valParams, dc, cc, 1., quotingParams);

			dblPriceFromTSYSpread = org.drip.service.api.FI.BondPriceFromTSYSpread (strISIN, dtToday, dc,
				dcTSY, 0.0271);

			dblYieldFromTSYSpread = org.drip.service.api.FI.BondYieldFromTSYSpread (strISIN, dtToday, dcTSY,
				0.0271);

			dblZSpreadFromTSYSpread = org.drip.service.api.FI.BondZSpreadFromTSYSpread (strISIN, dtToday, dc,
				dcTSY, 0.0271);

			dblISpreadFromTSYSpread = org.drip.service.api.FI.BondISpreadFromTSYSpread (strISIN, dtToday, dc,
				dcTSY, 0.0271);

			dblGSpreadFromTSYSpread = org.drip.service.api.FI.BondGSpreadFromTSYSpread (strISIN, dtToday, dc,
				dcTSY, 0.0271);

			dblParASWFromTSYSpread = org.drip.service.api.FI.BondParASWFromTSYSpread (strISIN, dtToday, dc,
				dcTSY, 0.0271);

			dblCreditBasisFromTSYSpread = org.drip.service.api.FI.BondCreditBasisFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, cc, 0.0271);

			dblBondCreditPrice = org.drip.service.api.FI.BondCreditPrice (strISIN, valParams, dc, cc,
				quotingParams);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.util.date.JulianDate dtPreviousCoupon = org.drip.service.api.FI.PreviousCouponDate (strISIN,
			dtToday);

		org.drip.util.date.JulianDate dtCurrentCoupon = org.drip.service.api.FI.NextCouponDate (strISIN,
			dtToday);

		org.drip.util.date.JulianDate dtNextCoupon = org.drip.service.api.FI.NextCouponDate (strISIN,
			dtToday);

		org.drip.util.date.JulianDate dtEffective = org.drip.service.api.FI.EffectiveDate (strISIN);

		org.drip.util.date.JulianDate dtMaturity = org.drip.service.api.FI.MaturityDate (strISIN);

		try {
			bInFirstPeriod = org.drip.service.api.FI.InFirstPeriod (strISIN, valParams);

			bInLastPeriod = org.drip.service.api.FI.InLastPeriod (strISIN, valParams);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) return;

			e.printStackTrace();
		}

		org.drip.param.valuation.NextExerciseInfo nei = org.drip.service.api.FI.NextExerciseInfo (strISIN,
			dtToday);

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

			for (org.drip.analytics.period.ProductCouponPeriodCurveMeasures p : bond.getCouponFlow
				(valParams, pricerParams, org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (dtToday,
					"USD", 0.05), org.drip.analytics.curve.CreditCurve.FromFlatHazard (dtToday.getJulian(),
						"CC", 0.02, 0.4))) {
				try {
					System.out.println (org.drip.util.date.JulianDate.fromJulian (p.getAccrualStartDate()) +
						"    " + org.drip.util.date.JulianDate.fromJulian (p.getAccrualEndDate()) + "    " +
							org.drip.util.date.JulianDate.fromJulian (p.getPayDate()) + "    " +
								org.drip.util.common.FIGen.FormatSpreadSimple (p.getCouponDCF(), 1, 4, 1.) +
									"    " + org.drip.util.common.FIGen.FormatSpreadSimple (dc.getDF
										(p.getPayDate()), 1, 4, 1.) + "    " +
											org.drip.util.common.FIGen.FormatSpreadSimple (cc.getSurvival
												(p.getPayDate()), 1, 4, 1.));
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
	
			for (org.drip.analytics.period.ProductLossPeriodCurveMeasures dp : bond.getLossFlow (valParams,
				org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
					org.drip.param.market.ComponentMarketParams.MakeCreditCMP (dc, cc)))
				System.out.println (org.drip.util.date.JulianDate.fromJulian (dp.getStartDate()) + "    " +
					org.drip.util.date.JulianDate.fromJulian (dp.getEndDate()) + "    " +
						org.drip.util.date.JulianDate.fromJulian (dp.getPayDate()) + "    " +
							org.drip.util.common.FIGen.FormatSpreadSimple (dp.getCouponDCF(), 1, 4, 1.) +
								"    " + org.drip.util.common.FIGen.FormatSpreadSimple
									(dp.getEffectiveNotional(), 1, 0, 1.) + "    " +
										org.drip.util.common.FIGen.FormatSpreadSimple
											(dp.getEffectiveRecovery(), 1, 2, 1.) + "    " +
												org.drip.util.common.FIGen.FormatSpreadSimple
													(dp.getEffectiveDF(), 1, 4, 1.)  + "    " +
														org.drip.util.common.FIGen.FormatSpreadSimple
															(dp.getStartSurvival(), 1, 4, 1.) + "    " +
																org.drip.util.common.FIGen.FormatSpreadSimple
																	(dp.getEndSurvival(), 1, 4, 1.));
		}

		if (s_bBondAnalDisplay) {
			try {
				System.out.println (strISIN + "    " + bond.getTicker() + " " +
					org.drip.util.common.FIGen.FormatPrice (bond.getCoupon
						(org.drip.util.date.JulianDate.Today().getJulian())) + " " + bond.getMaturityDate());

				System.out.println ("Work-out date From Price: " + new org.drip.util.date.JulianDate
					(wi._dblDate));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Work-out factor From Price: " + wi._dblExerciseFactor);

			System.out.println ("Work-out Yield From Price: " + org.drip.util.common.FIGen.FormatPrice
				(wi._dblYield));

			System.out.println ("Work-out Type for Price: " +
				org.drip.util.internal.FIUtil.WorkoutTypeToString (wi._iWOType));

			System.out.println ("Yield From Price: " + org.drip.util.common.FIGen.FormatPrice
				(dblYieldFromPrice) + " / " + org.drip.util.common.FIGen.FormatPrice (dblYTMFromPrice));

			System.out.println ("Z Spread From Price: " + (int) (10000. * dblZSpreadFromPrice) + " / " +
				(int) (10000. * dblZTMFromPrice));

			System.out.println ("I Spread From Price: " + (int) (10000. * dblISpreadFromPrice) + " / " +
				(int) (10000. * dblITMFromPrice));

			System.out.println ("TSY Spread From Price: " + (int) (10000. * dblTSYSpreadFromPrice) + " / " +
				(int) (10000. * dblTSYTMFromPrice));

			System.out.println ("G Spread From Price: " + (int) (10000. * dblGSpreadFromPrice) + " / " +
				(int) (10000. * dblGTMFromPrice));

			System.out.println ("Par ASW From Price: " + (int) dblParASWFromPrice + " / " + (int)
				dblParASWTMFromPrice);

			System.out.println ("Credit Basis From Price: " + (int) (10000. * dblCreditBasisFromPrice) +
				" / " + (int) (10000. * dblCreditBasisTMFromPrice));

			System.out.println ("Price From TSY Spread: " + org.drip.util.common.FIGen.FormatPrice
				(dblPriceFromTSYSpread));

			System.out.println ("Yield From TSY Spread: " + org.drip.util.common.FIGen.FormatPrice
				(dblYieldFromTSYSpread));

			System.out.println ("Z Spread From TSY Spread: " + (int) (10000. * dblZSpreadFromTSYSpread));

			System.out.println ("I Spread From TSY Spread: " + (int) (10000. * dblISpreadFromTSYSpread));

			System.out.println ("G Spread From TSY Spread: " + (int) (10000. * dblGSpreadFromTSYSpread));

			System.out.println ("Par ASW From TSY Spread: " + (int) dblParASWFromTSYSpread);

			System.out.println ("Credit Basis From TSY Spread: " + (int) (10000. *
				dblCreditBasisFromTSYSpread));

			System.out.println ("Credit Risky Price: " + org.drip.util.common.FIGen.FormatPrice
				(dblBondCreditPrice));

			System.out.println ("Valuation Date: " + org.drip.util.date.JulianDate.Today());

			System.out.println ("Effective Date: " + dtEffective);

			System.out.println ("Maturity Date: " + dtMaturity);

			System.out.println ("Is Val Date in the first period? " + bInFirstPeriod);

			System.out.println ("Is Val Date in the last period? " + bInLastPeriod);

			System.out.println ("Previous Coupon Date: " + dtPreviousCoupon);

			System.out.println ("Current Coupon Date: " + dtCurrentCoupon);

			System.out.println ("Next Coupon Date: " + dtNextCoupon);

			try {
				System.out.println ("Next Exercise Date: " + new org.drip.util.date.JulianDate
					(nei._dblDate));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Next Exercise Factor: " + nei._dblExerciseFactor);

			System.out.println ("Next Exercise Type: " + org.drip.util.internal.FIUtil.WorkoutTypeToString
				(nei._iWOType));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the custom bond API
	 */

	public static final void CustomBondAPISample() {
		org.drip.product.credit.Bond[] aBond = new org.drip.product.credit.Bond[3];

		if (null == (aBond[0] = org.drip.service.api.FI.GetBond ("CustomFixed")))
			org.drip.service.api.FI.PutBond ("CustomFixed", aBond[0] = CreateCustomBond ("CustomFixed",
				org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED));

		if (null == (aBond[1] = org.drip.service.api.FI.GetBond ("CustomFRN")))
			org.drip.service.api.FI.PutBond ("CustomFRN", aBond[1] = CreateCustomBond ("CustomFRN",
				org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER));

		if (null == (aBond[2] = org.drip.service.api.FI.GetBond ("CustomBondFromCF")))
			org.drip.service.api.FI.PutBond ("CustomBondFromCF", aBond[2] = CreateCustomBond
				("CustomBondFromCF", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF));

		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
			(org.drip.util.date.JulianDate.Today(), "USD", 0.04);

		org.drip.analytics.curve.DiscountCurve dcTSY =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (org.drip.util.date.JulianDate.Today(),
				"USD", 0.03);

		org.drip.analytics.curve.CreditCurve cc = org.drip.analytics.curve.CreditCurve.FromFlatHazard
			(org.drip.util.date.JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		for (int i = 0; i < aBond.length; ++i) {
			if (s_bCustomBondCouponCFDisplay) {
				System.out.println
					("\nAcc Start     Acc End     Pay Date      Cpn DCF       Pay01       Surv01");

				System.out.println
					("---------    ---------    ---------    ---------    ---------    --------");

				for (org.drip.analytics.period.Period p : aBond[i].getCouponPeriod()) {
					try {
						System.out.println (org.drip.util.date.JulianDate.fromJulian
							(p.getAccrualStartDate()) + "    " + org.drip.util.date.JulianDate.fromJulian
								(p.getAccrualEndDate()) + "    " + org.drip.util.date.JulianDate.fromJulian
									(p.getPayDate()) + "    " + org.drip.util.common.FIGen.FormatSpreadSimple
										(p.getCouponDCF(), 1, 4, 1.) + "    " +
											org.drip.util.common.FIGen.FormatSpreadSimple (dc.getDF
												(p.getPayDate()), 1, 4, 1.) + "    " +
													org.drip.util.common.FIGen.FormatSpreadSimple
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

			org.drip.param.market.ComponentMarketParams cmp = new org.drip.param.market.ComponentMarketParams
				(dc, dcTSY, dcTSY, cc, null, null, org.drip.util.common.FIGen.CreateFixingsObject (aBond[i],
					org.drip.util.date.JulianDate.Today(), 0.04));

			if (s_bCustomBondAnalDisplay) {
				try {
					System.out.println ("\nPrice From Yield: " + org.drip.util.common.FIGen.FormatPrice
						(aBond[i].calcPriceFromYield (org.drip.param.valuation.ValuationParams.CreateValParams
							(org.drip.util.date.JulianDate.Today(), 0, "",
								org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null, 0.)));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CustomAPISample failed.");

						return;
					}

					e.printStackTrace();
				}

				org.drip.param.valuation.WorkoutInfo wi = aBond[i].calcExerciseYieldFromPrice
					(org.drip.param.valuation.ValuationParams.CreateValParams
						(org.drip.util.date.JulianDate.Today(), 0, "",
							org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null, 1.);

				System.out.println ("Workout Date: " + org.drip.util.date.JulianDate.fromJulian
					(wi._dblDate));

				System.out.println ("Workout Factor: " + wi._dblExerciseFactor);

				System.out.println ("Workout Yield: " + org.drip.util.common.FIGen.FormatPrice
					(wi._dblYield));

				try {
					System.out.println ("Workout Yield From Price: " + org.drip.util.common.FIGen.FormatPrice
						(aBond[i].calcYieldFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("Z Spread From Price: " + org.drip.util.common.FIGen.FormatSpread
						(aBond[i].calcZSpreadFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("TSY Spread From Price: " + org.drip.util.common.FIGen.FormatSpread
						(aBond[i].calcTSYSpreadFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("ASW Spread From Price: " + (int) aBond[i].calcParASWFromPrice
						(org.drip.param.valuation.ValuationParams.CreateValParams
							(org.drip.util.date.JulianDate.Today(), 0, "USD",
								org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null, wi._dblDate,
									wi._dblExerciseFactor, 1.));

					System.out.println ("Credit Basis From Price: " + org.drip.util.common.FIGen.FormatSpread
						(aBond[i].calcCreditBasisFromPrice
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										wi._dblDate, wi._dblExerciseFactor, 1.)));

					System.out.println ("Price From TSY Spread: " + org.drip.util.common.FIGen.FormatPrice
						(aBond[i].calcPriceFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										0.0188)));

					System.out.println ("Yield From TSY Spread: " + org.drip.util.common.FIGen.FormatPrice
						(aBond[i].calcYieldFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, 0.0188)));

					System.out.println ("Par ASW From TSY Spread: " + (int) aBond[i].calcParASWFromTSYSpread
						(org.drip.param.valuation.ValuationParams.CreateValParams
							(org.drip.util.date.JulianDate.Today(), 0, "USD",
								org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null, 0.0188));

					System.out.println ("Credit Basis From TSY Spread: " +
						org.drip.util.common.FIGen.FormatSpread (aBond[i].calcCreditBasisFromTSYSpread
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
										0.0188)));

					System.out.println ("Theoretical Price: " + org.drip.util.common.FIGen.FormatPrice
						(aBond[i].calcPriceFromCreditBasis
							(org.drip.param.valuation.ValuationParams.CreateValParams
								(org.drip.util.date.JulianDate.Today(), 0, "USD",
									org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
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

	public static final void BondTickerAPISample() {
		int iNumBonds = 0;
		java.lang.String strTicker = "BK";

		org.drip.util.date.JulianDate dtToday = org.drip.util.date.JulianDate.Today();

		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
			(dtToday, "USD", 0.05);

		org.drip.analytics.curve.DiscountCurve dcTSY =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.04);

		org.drip.analytics.curve.CreditCurve cc = org.drip.analytics.curve.CreditCurve.FromFlatHazard
			(dtToday.getJulian(), "CC", 0.02, 0.4);

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.FI.GetISINsForTicker (strTicker);

		if (s_bTickerAnalDisplay) {
			System.out.println
				("Dumping: ISIN, Bond, Yield, Z Spread, TSY Spread, Credit Basis, Credit Price");

			System.out.println
				("----------------------------------------------------------------------------");
		}

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.credit.Bond bond = org.drip.service.api.FI.GetBond (strISIN);

			if (null != bond && !bond.hasVariableCoupon() && !bond.hasBeenExercised() && !bond.hasDefaulted()
				&& bond.getMaturityDate().getJulian() > dtToday.getJulian()) {
				try {
					double dblYieldFromPrice = org.drip.service.api.FI.BondYieldFromPrice (strISIN, dtToday,
						dc, 1.);

					double dblZSpreadFromPrice = org.drip.service.api.FI.BondZSpreadFromPrice (strISIN,
						dtToday, dc, 1.);

					double dblTSYSpreadFromPrice = org.drip.service.api.FI.BondTSYSpreadFromPrice (strISIN,
						dtToday, dc, dcTSY, 1.);

					double dblCreditBasisFromPrice = org.drip.service.api.FI.BondCreditBasisFromPrice
						(strISIN, dtToday, dc, cc, 1.);

					double dblBondCreditPrice = org.drip.service.api.FI.BondCreditPrice (strISIN, dtToday,
						dc, cc);

					++iNumBonds;

					if (s_bTickerAnalDisplay)
						System.out.println (strISIN + "    " + bond.getTicker() + " " +
							org.drip.util.common.FIGen.FormatPrice (bond.getCoupon
								(org.drip.util.date.JulianDate.Today().getJulian())) + " " +
									bond.getMaturityDate() + "    " + org.drip.util.common.FIGen.FormatPrice
										(dblYieldFromPrice) + "    " +
											org.drip.util.common.FIGen.FormatSpread (dblZSpreadFromPrice) +
												"    " + org.drip.util.common.FIGen.FormatSpread
													(dblTSYSpreadFromPrice) + "    " +
														org.drip.util.common.FIGen.FormatSpread
															(dblCreditBasisFromPrice) + "    " +
																org.drip.util.common.FIGen.FormatPrice
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
			org.drip.product.credit.Bond bond = org.drip.service.api.FI.GetBond (strISIN);

			try {
				double dblOutstandingAmount = org.drip.service.api.FI.GetBondDoubleField (strISIN,
					"OutstandingAmount");

				if (s_bTickerNotionalDisplay)
					System.out.println (strISIN + "    " + bond.getTicker() + " " +
						org.drip.util.common.FIGen.FormatPrice (bond.getCoupon
							(org.drip.util.date.JulianDate.Today().getJulian())) + " " +
								bond.getMaturityDate() + "    " + org.drip.util.common.FIGen.FormatPrice
									(dblOutstandingAmount, 10, 0, 1.));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondTickerAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		org.drip.util.date.JulianDate[] adtAscending = new org.drip.util.date.JulianDate[5];

		adtAscending[0] = org.drip.util.date.JulianDate.Today().addYears (3);

		adtAscending[1] = org.drip.util.date.JulianDate.Today().addYears (5);

		adtAscending[2] = org.drip.util.date.JulianDate.Today().addYears (10);

		adtAscending[3] = org.drip.util.date.JulianDate.Today().addYears (30);

		adtAscending[4] = org.drip.util.date.JulianDate.Today().addYears (60);

		java.util.Map<org.drip.util.date.JulianDate, java.lang.Double> mapOutstandingNotional =
			org.drip.service.api.FI.GetIssuerAggregateOutstandingNotional
				(org.drip.util.date.JulianDate.Today(), strTicker, adtAscending);

		if (s_bCumulativeTickerNotionalDisplay) {
			for (java.util.Map.Entry<org.drip.util.date.JulianDate, java.lang.Double> me :
				mapOutstandingNotional.entrySet())
				System.out.println ("[" + org.drip.util.date.JulianDate.Today() + "=>" + me.getKey() + "] = "
					+ me.getValue());
		}
	}

	/**
	 * Sample demonstrating the retrieval of the bond's static fields
	 */

	public static final void BondStaticAPISample() {
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

		java.lang.String strAccrualDC = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"AccrualDC");

		java.lang.String strBBG_ID = org.drip.service.api.FI.GetBondStringField (strBondISIN, "BBG_ID");

		java.lang.String strBBGParent = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"BBGParent");

		java.lang.String strBBGUniqueID = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"BBGUniqueID");

		java.lang.String strCalculationType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CalculationType");

		java.lang.String strCDRCountryCode = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CDRCountryCode");

		java.lang.String strCDRSettleCode = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CDRSettleCode");

		java.lang.String strCollateralType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CollateralType");

		java.lang.String strCountryOfDomicile = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CountryOfDomicile");

		java.lang.String strCountryOfGuarantor = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CountryOfGuarantor");

		java.lang.String strCountryOfIncorporation = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CountryOfIncorporation");

		java.lang.String strCouponCurrency = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CouponCurrency");

		java.lang.String strCouponDC = org.drip.service.api.FI.GetBondStringField (strBondISIN, "CouponDC");

		java.lang.String strCouponType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CouponType");

		java.lang.String strCreditCurve = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"CreditCurve");

		java.lang.String strCUSIP = org.drip.service.api.FI.GetBondStringField (strBondISIN, "CUSIP");

		java.lang.String strDescription = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"Description");

		java.lang.String strEDSFCurve = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"EDSFCurve");

		java.lang.String strExchangeCode = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"ExchangeCode");

		java.lang.String strFitch = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Fitch");

		java.lang.String strFloatCouponConvention = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"FloatCouponConvention");

		java.lang.String strIndustryGroup = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IndustryGroup");

		java.lang.String strIndustrySector = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IndustrySector");

		java.lang.String strIndustrySubgroup = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IndustrySubgroup");

		java.lang.String strIRCurve = org.drip.service.api.FI.GetBondStringField (strBondISIN, "IRCurve");

		java.lang.String strISIN = org.drip.service.api.FI.GetBondStringField (strBondISIN, "ISIN");

		java.lang.String strIssuer = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Issuer");

		java.lang.String strIssuerCategory = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IssuerCategory");

		java.lang.String strIssuerCountry = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IssuerCountry");

		java.lang.String strIssuerCountryCode = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IssuerCountryCode");

		java.lang.String strIssuerIndustry = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"IssuerIndustry");

		java.lang.String strLeadManager = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"LeadManager");

		java.lang.String strLongCompanyName = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"LongCompanyName");

		java.lang.String strMarketIssueType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"MarketIssueType");

		java.lang.String strMaturityType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"MaturityType");

		java.lang.String strMoody = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Moody");

		java.lang.String strName = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Name");

		java.lang.String strRateIndex = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"RateIndex");

		java.lang.String strRedemptionCurrency = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"RedemptionCurrency");

		java.lang.String strSecurityType = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"SecurityType");

		java.lang.String strSeniorSub = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"SeniorSub");

		java.lang.String strSeries = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Series");

		java.lang.String strSnP = org.drip.service.api.FI.GetBondStringField (strBondISIN, "SnP");

		java.lang.String strShortName = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"ShortName");

		java.lang.String strTicker = org.drip.service.api.FI.GetBondStringField (strBondISIN, "Ticker");

		java.lang.String strTradeCurrency = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"TradeCurrency");

		java.lang.String strTreasuryCurve = org.drip.service.api.FI.GetBondStringField (strBondISIN,
			"TreasuryCurve");

		try {
			bIsBearer = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Bearer");

			bIsCallable = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Callable");

			bDefaulted = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Defaulted");

			bExercised = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Exercised");

			bIsFloater = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Floater");

			bIsPerpetual = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Perpetual");

			bIsPrivatePlacement = org.drip.service.api.FI.GetBondBooleanField (strBondISIN,
				"PrivatePlacement");

			bIsPutable = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Putable");

			bIsRegistered = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Registered");

			bIsReverseConvertible = org.drip.service.api.FI.GetBondBooleanField (strBondISIN,
				"ReverseConvertible");

			bIsSinkable = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "Sinkable");

			bIsStructuredNote = org.drip.service.api.FI.GetBondBooleanField (strBondISIN,
				"StructuredNote");

			bIsTradeStatus = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "TradeStatus");

			bIsUnitTraded = org.drip.service.api.FI.GetBondBooleanField (strBondISIN, "UnitTraded");

			dblCoupon = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "Coupon");

			dblCurrentCoupon = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "CurrentCoupon");

			dblFloatSpread = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "FloatSpread");

			dblIssueAmount = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "IssueAmount");

			dblIssuePrice = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "IssuePrice");

			dblMinimumIncrement = org.drip.service.api.FI.GetBondDoubleField (strBondISIN,
				"MinimumIncrement");

			dblMinimumPiece = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "MinimumPiece");

			dblOutstandingAmount = org.drip.service.api.FI.GetBondDoubleField (strBondISIN,
				"OutstandingAmount");

			dblParAmount = org.drip.service.api.FI.GetBondDoubleField (strBondISIN, "ParAmount");

			dblRedemptionValue = org.drip.service.api.FI.GetBondDoubleField (strBondISIN,
				"RedemptionValue");

			iCouponFrequency = org.drip.service.api.FI.GetBondIntegerField (strBondISIN, "Frequency");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondStaticAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.util.date.JulianDate dtAccrualStart = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"AccrualStartDate");

		org.drip.util.date.JulianDate dtAnnounce = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"AnnounceDate");

		org.drip.util.date.JulianDate dtFirstCoupon = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"FirstCouponDate");

		org.drip.util.date.JulianDate dtFirstSettle = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"FirstSettleDate");

		org.drip.util.date.JulianDate dtFinalMaturity = org.drip.service.api.FI.GetBondDateField
			(strBondISIN, "FinalMaturity");

		org.drip.util.date.JulianDate dtIssue = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"IssueDate");

		org.drip.util.date.JulianDate dtMaturity = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"Maturity");

		org.drip.util.date.JulianDate dtNextCoupon = org.drip.service.api.FI.GetBondDateField (strBondISIN,
			"NextCouponDate");

		org.drip.util.date.JulianDate dtPenultimateCoupon = org.drip.service.api.FI.GetBondDateField
			(strBondISIN, "PenultimateCouponDate");

		org.drip.util.date.JulianDate dtPreviousCoupon = org.drip.service.api.FI.GetBondDateField
			(strBondISIN, "PreviousCouponDate");

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

			System.out.println ("Coupon: " + org.drip.util.common.FIGen.FormatPrice (dblCoupon));

			System.out.println ("CurrentCoupon: " + org.drip.util.common.FIGen.FormatPrice
				(dblCurrentCoupon));

			System.out.println ("FloatSpread: " + org.drip.util.common.FIGen.FormatSpread (dblFloatSpread));

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

	public static void BondCDSCurveCalibration() {
		double dblCreditPrice = java.lang.Double.NaN;

		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate
			(org.drip.util.date.JulianDate.Today(), "DKK", 0.04);

		org.drip.analytics.curve.CreditCurve cc = org.drip.analytics.curve.CreditCurve.FromFlatHazard
			(org.drip.util.date.JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		org.drip.product.credit.Bond bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed
			("CCCalibBond", "DKK", 0.05, "30/360", org.drip.util.date.JulianDate.CreateFromYMD (2008, 9, 21),
				org.drip.util.date.JulianDate.CreateFromYMD (2023, 9, 20), null, null);

		org.drip.param.market.ComponentMarketParams cmp = new org.drip.param.market.ComponentMarketParams
			(dc, null, null, cc, null, null, org.drip.util.common.FIGen.CreateFixingsObject (bond,
				org.drip.util.date.JulianDate.Today(), 0.04));

		try {
			dblCreditPrice = bond.calcPriceFromCreditBasis
				(org.drip.param.valuation.ValuationParams.CreateValParams
					(org.drip.util.date.JulianDate.Today(), 0, "USD",
						org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), cmp, null,
							bond.getMaturityDate().getJulian(), 1., 0.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.product.credit.CreditDefaultSwap cds = org.drip.product.credit.CreditDefaultSwap.CreateCDS
			(org.drip.util.date.JulianDate.Today(), org.drip.util.date.JulianDate.Today().addTenor ("5Y"),
				0.1, "DKK", 0.40, "CC", "DKK");

		if (s_bCDSBondCreditCurve) System.out.println ("Credit Price: " + dblCreditPrice);

		org.drip.product.common.CalibratableComponent[] aCalibInst = new
			org.drip.product.common.CalibratableComponent[2];
		java.lang.String[] astrCalibMeasure = new java.lang.String[2];
		double[] adblQuotes = new double[2];
		aCalibInst[0] = cds;
		aCalibInst[1] = bond;
		astrCalibMeasure[0] = "FairPremium";
		astrCalibMeasure[1] = "FairCleanPV";
		adblQuotes[0] = 100.;
		adblQuotes[1] = 100. * dblCreditPrice;
		org.drip.analytics.curve.CreditCurve ccCalib = null;

		try {
			org.drip.curve.calibration.CreditCurveScenarioGenerator ccsg = new
				org.drip.curve.calibration.CreditCurveScenarioGenerator (aCalibInst);

			ccCalib = ccsg.createCC ("CC", org.drip.param.valuation.ValuationParams.CreateValParams
				(org.drip.util.date.JulianDate.Today(), 0, "USD",
					org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), dc, null, null, adblQuotes, 0.40,
						astrCalibMeasure, null, new org.drip.param.valuation.QuotingParams ("30/360", 2,
							true, null, "USD", false), false);

			if (s_bCDSBondCreditCurve)
				System.out.println ("Surv (2021, 1, 14): " + ccCalib.getSurvival
					(org.drip.util.date.JulianDate.CreateFromYMD (2021, 1, 14)));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		double[] adblCDSQuotes = ccCalib.getCompQuotes();

		org.drip.product.common.CalibratableComponent[] aCompCDS = ccCalib.getCalibComponents();

		if (s_bCDSBondCreditCurve) {
			for (int i = 0; i < aCompCDS.length; ++i)
				System.out.println (aCompCDS[i].getPrimaryCode() + " => " + (int) (adblCDSQuotes[i]));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond basket API
	 */

	public static final void BasketBondAPISample() {
		org.drip.util.date.JulianDate dtToday = org.drip.util.date.JulianDate.Today();

		org.drip.product.credit.BasketBond bb = org.drip.service.api.FI.MakeBondBasket ("SLMA_ETF", new
			java.lang.String[] {"US78490FVJ55", "US78490FWD76", "US78490FVL02", "US78442FAZ18",
				"US78490FTL30"}, new double[] {1., 2., 3., 4., 5.}, dtToday, 100.);

		org.drip.param.market.BasketMarketParams bmp = new org.drip.param.market.BasketMarketParams();

		bmp.addDC ("USD", org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.04));

		java.util.Map<java.lang.String, java.lang.Double> mapResult = null;

		try {
			mapResult = bb.value (org.drip.param.valuation.ValuationParams.CreateValParams (dtToday, 0,
				"USD", org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL), new
					org.drip.param.pricer.PricerParams (7, false, false,
						org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_FULL_COUPON), bmp, null);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BasketBondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bBasketBond) {
			System.out.println ("Fair Def Free Clean Price: " + mapResult.get ("FairDefFreePrice"));

			System.out.println ("Fair YTM: " + mapResult.get ("FairYTM"));

			System.out.println ("Fair GSpread: " + mapResult.get ("FairGSpread"));

			System.out.println ("Fair ZSpread: " + mapResult.get ("FairZSpread"));

			System.out.println ("Fair ISpread: " + mapResult.get ("FairISpread"));

			System.out.println ("Fair DV01: " + mapResult.get ("FairDV01"));

			System.out.println ("Accrued: " + mapResult.get ("Accrued"));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the FX API
	 */

	public static final void FXAPISample() {
		org.drip.param.product.CurrencyPair cp = null;

		try {
			cp = new org.drip.param.product.CurrencyPair ("EUR", "USD", "USD", 10000.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.Random rand = new java.util.Random();

		org.drip.analytics.curve.DiscountCurve dcUSD =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (org.drip.util.date.JulianDate.Today(),
				"USD", 0.05);

		org.drip.analytics.curve.DiscountCurve dcEUR =
			org.drip.analytics.curve.DiscountCurve.CreateFromFlatRate (org.drip.util.date.JulianDate.Today(),
				"EUR", 0.04);

		double dblFXSpot = 1.40;
		double dblFXFwdMarket = 1.40;
		double[] adblNodes = new double[5];
		double[] adblFXFwd = new double[5];
		boolean[] abIsPIP = new boolean[5];
		double dblFXFwd = java.lang.Double.NaN;
		double dblFXFwdPIP = java.lang.Double.NaN;
		double dblDCEURBasis = java.lang.Double.NaN;
		double dblDCUSDBasis = java.lang.Double.NaN;
		org.drip.analytics.curve.FXCurve fxCurve = null;

		for (int i = 0; i < 5; ++i) {
			abIsPIP[i] = false;
			adblFXFwd[i] = dblFXSpot - (i + 1) * 0.01 * rand.nextDouble();

			adblNodes[i] = org.drip.util.date.JulianDate.Today().addYears (i + 1).getJulian();

			if (s_bFXFwd)
				System.out.println ("Input " + cp.getCode() + "[" + (i + 1) + "] = " +
					org.drip.util.common.FIGen.FormatSpreadSimple (adblFXFwd[i]));
		}

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateValParams (org.drip.util.date.JulianDate.Today(),
				0, "USD", org.drip.analytics.daycount.DayCountBasis.DR_ACTUAL);

		org.drip.product.fx.FXForward fxfwd = org.drip.product.fx.FXForward.CreateFXForward (cp,
			org.drip.util.date.JulianDate.Today(), "1Y");

		try {
			dblFXFwd = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, false);

			dblFXFwdPIP = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, true);

			dblDCEURBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, false);

			dblDCUSDBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, true);

			fxCurve = new org.drip.analytics.curve.FXCurve (cp, org.drip.util.date.JulianDate.Today(),
				dblFXSpot, adblNodes, adblFXFwd, abIsPIP);
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
		org.drip.analytics.curve.FXBasis fxEURBasisCurve = null;
		org.drip.analytics.curve.FXBasis fxUSDBasisCurve = null;

		try {
			fxUSDBasisCurve = new org.drip.analytics.curve.FXBasis (cp,
				org.drip.util.date.JulianDate.Today(), dblFXSpot, adblNodes, adblFullUSDBasis, false);

			adblFXFwdFromUSDBasis = fxUSDBasisCurve.getFullFXFwd (valParams, dcEUR, dcUSD, true, false);

			fxEURBasisCurve = new org.drip.analytics.curve.FXBasis (cp,
				org.drip.util.date.JulianDate.Today(), dblFXSpot, adblNodes, adblFullEURBasis, false);

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

			System.out.println (cp.getCode() + "[1Y](pip)= " + org.drip.util.common.FIGen.FormatSpreadSimple
				(dblFXFwdPIP));

			System.out.println ("EUR Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.util.common.FIGen.FormatSpreadSimple (dblDCEURBasis));

			System.out.println ("USD Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.util.common.FIGen.FormatSpreadSimple (dblDCUSDBasis));

			for (int i = 0; i < adblFullUSDBasis.length; ++i) {
				System.out.println ("FullUSDBasis[" + (i + 1) + "Y]=" +
					org.drip.util.common.FIGen.FormatSpread (adblFullUSDBasis[i]));

				System.out.println ("FullEURBasis[" + (i + 1) + "Y]=" +
					org.drip.util.common.FIGen.FormatSpread (adblFullEURBasis[i]));
			}

			for (int i = 0; i < adblBootstrappedUSDBasis.length; ++i) {
				System.out.println ("Bootstrapped USDBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.util.common.FIGen.FormatSpread (adblBootstrappedUSDBasis[i]));

				System.out.println ("Bootstrapped EURBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.util.common.FIGen.FormatSpread (adblBootstrappedEURBasis[i]));
			}

			for (int i = 0; i < adblFXFwdFromUSDBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped USD Basis: " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.util.common.FIGen.FormatSpreadSimple (adblFXFwdFromUSDBasis[i]));

			for (int i = 0; i < adblFXFwdFromEURBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped EUR Basis: " + cp.getCode() + "[" + (i + 1) +
					"Y]=" + org.drip.util.common.FIGen.FormatSpreadSimple (adblFXFwdFromEURBasis[i]));
		}
	}

	public static final void main (final java.lang.String astrArgs[]) {
		// java.lang.String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		java.lang.String strConfig = "";

		boolean bFIInit = org.drip.service.api.FI.Init (strConfig);

		if (!bFIInit) System.out.println ("Cannot fully init FI!");

		CalenderAPISample();

		DayCountAPISample();

		DiscountCurveAPISample();

		CreditCurveAPISample();

		CDSAPISample();

		if (bFIInit) BondAPISample();

		CustomBondAPISample();

		if (bFIInit) {
			BondTickerAPISample();

			BondStaticAPISample();
		}

		BondCDSCurveCalibration();

		FXAPISample();

		BasketBondAPISample();
	}
}
