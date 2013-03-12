
package org.drip.service.api;

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
 *  This class exposes all the CreditAnalytics API to clients – this class is the main functional interface.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalytics {
	private static java.sql.Statement s_stmt = null;

	private static final java.util.Map<java.lang.String, org.drip.product.definition.Bond> _mapBondCache =
		new java.util.HashMap<java.lang.String, org.drip.product.definition.Bond>();

	private static final java.util.Map<java.lang.String, org.drip.product.creator.BondRefDataBuilder>
		_mapBondRefDataCache = new java.util.HashMap<java.lang.String,
			org.drip.product.creator.BondRefDataBuilder>();

	private static final org.drip.product.definition.Bond GetBondFromCache (
		final java.lang.String strBondId)
	{
		return _mapBondCache.get (strBondId);
	}

	private static final org.drip.product.creator.BondRefDataBuilder GetBondRefDataFromCache (
		final java.lang.String strBondId)
	{
		return _mapBondRefDataCache.get (strBondId);
	}

	private static final org.drip.analytics.date.JulianDate[] GetLocationSetTypedHolidaysInYear (
		final java.lang.String strLocationSet,
		final int iYear,
		final int iHolType)
	{
		if (null == strLocationSet) return null;

		java.util.List<org.drip.analytics.date.JulianDate> ldtHols = new
			java.util.ArrayList<org.drip.analytics.date.JulianDate>();

		try {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.JulianDate.CreateFromYMD (iYear,
				1, 1);

			while (iYear + 1 != org.drip.analytics.date.JulianDate.Year (dt.getJulian())) {
				if (org.drip.analytics.daycount.Convention.IsHoliday (dt.getJulian(), strLocationSet,
					iHolType))
					ldtHols.add (dt);

				dt = dt.addDays (1);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (0 == ldtHols.size()) return null;

		org.drip.analytics.date.JulianDate[] adtHols = new
			org.drip.analytics.date.JulianDate[ldtHols.size()];

		int i = 0;

		for (org.drip.analytics.date.JulianDate dtHol : ldtHols)
			adtHols[i++] = dtHol;

		return adtHols;
	}

	/**
	 * Initializes the CreditAnalytics DRIP library.
	 * 
	 * @param strConfig Input configuration file
	 *  
	 * @return Success (true) or failure (false).
	 */

	public static final boolean Init (
		final java.lang.String strConfig)
	{
		if (null == (s_stmt = org.drip.service.env.EnvManager.InitEnv (strConfig))) return false;

		return true;
	}

	/**
	 * Retrieves the set of holiday locations
	 * 
	 * @return The set of holiday location strings
	 */

	public static final java.util.Set<java.lang.String> GetHolLocations()
	{
		return org.drip.analytics.daycount.Convention.GetHolLocations();
	}

	/**
	 * Retrieves all the available day counts
	 * 
	 * @return The set of the available day count string.
	 */

	public static final java.lang.String GetAvailableDC()
	{
		return org.drip.analytics.daycount.Convention.GetAvailableDC();
	}

	/**
	 * Gets the week day holidays for the calendar set in a given year
	 * 
	 * @param strLocationSet Comma delimited string of the holiday set
	 * @param iYear Integer year
	 * 
	 * @return Set of JulianDates representing the holidays
	 */

	public static final org.drip.analytics.date.JulianDate[] GetWeekDayHolsInYear (
		final java.lang.String strLocationSet,
		final int iYear)
	{
		return GetLocationSetTypedHolidaysInYear (strLocationSet, iYear,
			org.drip.analytics.daycount.Convention.WEEKDAY_HOLS);
	}

	/**
	 * Gets the week end holidays for the calendar set in a given year
	 * 
	 * @param strLocationSet Comma delimited string of the holiday set
	 * @param iYear Integer year
	 * 
	 * @return Set of JulianDates representing the holidays
	 */

	public static final org.drip.analytics.date.JulianDate[] GetWeekendHolsInYear (
		final java.lang.String strLocationSet,
		final int iYear)
	{
		return GetLocationSetTypedHolidaysInYear (strLocationSet, iYear,
			org.drip.analytics.daycount.Convention.WEEKEND_HOLS);
	}

	/**
	 * Gets all the holidays for the calendar set in a given year
	 * 
	 * @param strLocationSet Comma delimited string of the holiday set
	 * @param iYear Integer year
	 * 
	 * @return Set of JulianDates representing the holidays
	 */

	public static final org.drip.analytics.date.JulianDate[] GetHolsInYear (
		final java.lang.String strLocationSet,
		final int iYear)
	{
		return GetLocationSetTypedHolidaysInYear (strLocationSet, iYear,
			org.drip.analytics.daycount.Convention.WEEKEND_HOLS |
				org.drip.analytics.daycount.Convention.WEEKDAY_HOLS);
	}

	/**
	 * Gets the week end days corresponding to the holiday set
	 * 
	 * @param strLocationSet Comma delimited string of the holiday set
	 * 
	 * @return Integer array of the week end days
	 */

	public static final int[] GetWeekendDays (
		final java.lang.String strLocationSet)
	{
		return org.drip.analytics.daycount.Convention.GetWeekendDays (strLocationSet);
	}

	/**
	 * Indicates whether the given date is a holiday in the calendar set.
	 * 
	 * @param strLocationSet Comma delimited string of the holiday set
	 * @param dt JulainDate to be checked
	 * 
	 * @return Yes (true), No (false)
	 * 
	 * @throws java.lang.Exception Thrownif error/exception is encountered
	 */

	public static final boolean IsHoliday (
		final java.lang.String strLocationSet,
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		return org.drip.analytics.daycount.Convention.IsHoliday (dt.getJulian(), strLocationSet);
	}

	/**
	 * Computes the year fraction between two JulianDates according the given day count
	 * 
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * @param strDayCount String representing the day count
	 * @param bApplyEOMAdj Whether the end of month adjustment is to be applied
	 * 
	 * @return Day count fraction in years
	 * 
	 * @throws java.lang.Exception Thrown if any exception is encountered
	 */

	public static final double YearFraction (
		final org.drip.analytics.date.JulianDate dtStart,
		final org.drip.analytics.date.JulianDate dtEnd,
		final java.lang.String strDayCount,
		final boolean bApplyEOMAdj,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == dtStart || null == dtEnd)
			throw new java.lang.Exception ("Null date into CreditAnalytics.YearFraction");

		return org.drip.analytics.daycount.Convention.YearFraction (dtStart.getJulian(),
			dtEnd.getJulian(), strDayCount, bApplyEOMAdj, java.lang.Double.NaN, null, strCalendar);
	}

	/**
	 * Computes the year fraction between two JulianDates according the given day count
	 * 
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * @param strDayCount String representing the day count
	 * 
	 * @return Day count fraction in years
	 * 
	 * @throws java.lang.Exception Thrown if any exception is encountered
	 */

	public static final double YearFraction (
		final org.drip.analytics.date.JulianDate dtStart,
		final org.drip.analytics.date.JulianDate dtEnd,
		final java.lang.String strDayCount,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == dtStart || null == dtEnd)
			throw new java.lang.Exception ("Null date into CreditAnalytics.YearFraction");

		return org.drip.analytics.daycount.Convention.YearFraction (dtStart.getJulian(),
			dtEnd.getJulian(), strDayCount, true, java.lang.Double.NaN, null, strCalendar);
	}

	/**
	 * Adjusts the given date according to the calendar set and the adjustment mode
	 * 
	 * @param dt JulianDate input
	 * @param strCalendar Comma delimited calendar set
	 * @param iAdjustMode Adjustment mode specified in org.drip.analytics.daycount.DayCountBasis.Adjust
	 * 
	 * @return The adjusted JulianDate
	 */

	public static final org.drip.analytics.date.JulianDate Adjust (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strCalendar,
		final int iAdjustMode)
	{
		if (null == dt) return null;

		try {
			double dblAdjust = org.drip.analytics.daycount.Convention.Adjust (dt.getJulian(), strCalendar,
				iAdjustMode);

			if (java.lang.Double.isNaN (dblAdjust)) return null;

			return new org.drip.analytics.date.JulianDate (dblAdjust);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Rolls the given date according to the calendar set and the roll mode
	 * 
	 * @param dt JulianDate input
	 * @param strCalendarSet Comma delimited calendar set
	 * @param iRollMode Roll type specified in org.drip.analytics.daycount.DayCountBasis.RollDate
	 * 
	 * @return The rolled JulianDate
	 */

	public static final org.drip.analytics.date.JulianDate RollDate (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strCalendarSet,
		final int iRollMode)
	{
		if (null == dt) return null;

		try {
			double dblAdjust = org.drip.analytics.daycount.Convention.RollDate (dt.getJulian(), iRollMode,
				strCalendarSet);

			if (java.lang.Double.isNaN (dblAdjust)) return null;

			return new org.drip.analytics.date.JulianDate (dblAdjust);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the on-the-run treasury set string for the given date
	 * 
	 * @param dt Valuation date
	 * 
	 * @return The set of on-the-run treasury identifier strings
	 */

	public static final java.util.Map<java.lang.String, java.lang.String> GetOnTheRunTSYSet (
		final org.drip.analytics.date.JulianDate dt)
	{
		return null;
	}

	/**
	 * Gets the set of on-the-run treasury yields for a given EOD
	 * 
	 * @param dtEOD JulainDate EOD
	 * 
	 * @return The set of on-the-run treasury identifiers and their yields
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> GetEODOnTheRunTSYSetYield (
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		return null;
	}

	/**
	 * Gets the set of on-the-run treasury yields for a set of dates
	 * 
	 * @param dtStart Start JulianDate
	 * @param dtEnd End JulianDate
	 * 
	 * @return Double map containing the set of on-the-run treasury identifiers and yields for a set of dates
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> GetOnTheRunTSYSetYield (
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		return null;
	}

	/**
	 * Constructs the calibration component from the specified component code for the specified date
	 * 
	 * @param dt JulianDate input
	 * @param strCode String representing the component code
	 *
	 * @return The constructed component
	 */

	public static final org.drip.product.definition.Component MakeInstrumentFromCode (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strCode)
	{
		return null;
	}

	/**
	 * Constructs an array of calibration components for the specified component type and number for the
	 *  specified date
	 * 
	 * @param dt JulianDate input
	 * @param iNumInstr Number of components to be constructed
	 * @param strType String representing the component type
	 *
	 * @return The constructed component array
	 */

	public static final org.drip.product.definition.Component[] MakeStdInstrumentSet (
		final org.drip.analytics.date.JulianDate dt,
		final int iNumInstr,
		final java.lang.String strType)
	{
		return null;
	}

	/**
	 * Retrieves the names of all the IR curves corresponding to the given date
	 * 
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return The set of the curve names
	 */

	public static final java.util.Set<java.lang.String> GetEODIRCurveNames (
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == s_stmt || null == dtEOD) return null;

		return org.drip.service.env.RatesManager.GetAvailableEODIRCurveNames (s_stmt, dtEOD);
	}

	/**
	 * Loads the live IR curve
	 * 
	 * @param strName Name of the curve to be loaded
	 * 
	 * @return Current discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadLiveFullIRCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing IR curve
	 * 
	 * @param strName Name of the curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODFullIRCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == s_stmt || null == dtEOD || null == strName || strName.isEmpty()) return null;

		return org.drip.service.env.EODCurves.LoadEODIR (s_stmt, dtEOD, strName, "swap", strName);
	}

	/**
	 * Loads the set of discount curves between two dates
	 * 
	 * @param strName Name of the curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the discount curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.DiscountCurve> LoadEODFullIRCurves (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == dtStart || null == dtEnd || dtStart.getJulian() >
			dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.DiscountCurve dc = LoadEODFullIRCurve (strName, dt);

			if (null != dc) mapDC.put (dt, dc);

			dt = dt.addDays (1);
		}

		return mapDC;
	}

	/**
	 * Loads the live IR cash curve
	 * 
	 * @param strName Name of the cash curve to be loaded
	 * 
	 * @return Current cash discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadLiveIRCashCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing IR cash curve
	 * 
	 * @param strName Name of the cash curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing cash discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODIRCashCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == strName || strName.isEmpty() || null == s_stmt) return null;

		org.drip.param.definition.RatesScenarioCurve ircsc =
			org.drip.service.env.EODCurves.BuildEODIRCurveOfCode (null, s_stmt, dtEOD, strName, "M", "swap",
				strName);

		if (null == ircsc) return null;

		return ircsc.getDCBase();
	}

	/**
	 * Loads the set of cash discount curves between two dates
	 * 
	 * @param strName Name of the cash curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the cash discount curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.DiscountCurve> LoadEODIRCashCurves (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == dtStart || null == dtEnd || dtStart.getJulian() >
			dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.DiscountCurve dc = LoadEODIRCashCurve (strName, dt);

			if (null != dc) mapDC.put (dt, dc);

			dt = dt.addDays (1);
		}

		return mapDC;
	}

	/**
	 * Loads the live IR EDF curve
	 * 
	 * @param strName Name of the EDF curve to be loaded
	 * 
	 * @return Current EDF discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadLiveEDFCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing IR EDF curve
	 * 
	 * @param strName Name of the EDF curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing EDF discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODEDFCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == strName || strName.isEmpty() || null == s_stmt) return null;

		org.drip.param.definition.RatesScenarioCurve ircsc =
			org.drip.service.env.EODCurves.BuildEODIRCurveOfCode (null, s_stmt, dtEOD, strName, "E", "swap",
				strName);

		if (null == ircsc) return null;

		return ircsc.getDCBase();
	}

	/**
	 * Loads the set of EDF discount curves between two dates
	 * 
	 * @param strName Name of the EDF curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the EDF discount curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.DiscountCurve>	LoadEODEDFCurves (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == dtStart || null == dtEnd || dtStart.getJulian() >
			dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.DiscountCurve dc = LoadEODEDFCurve (strName, dt);

			if (null != dc) mapDC.put (dt, dc);

			dt = dt.addDays (1);
		}

		return mapDC;
	}

	/**
	 * Loads the live IR swap curve
	 * 
	 * @param strName Name of the swap curve to be loaded
	 * 
	 * @return Current swap discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadLiveIRSwapCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing IR swap curve
	 * 
	 * @param strName Name of the swap curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing swap discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODIRSwapCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == strName || strName.isEmpty() || null == s_stmt) return null;

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put (strName + "-LIBOR-6M", 0.0042);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (dtEOD.addDays (2), mIndexFixings);

		org.drip.param.definition.RatesScenarioCurve ircsc =
			org.drip.service.env.EODCurves.BuildEODIRCurveOfCode (mmFixings, s_stmt, dtEOD, strName, "S",
				"swap", strName);

		if (null == ircsc) return null;

		return ircsc.getDCBase();
	}

	/**
	 * Loads the set of swap discount curves between two dates
	 * 
	 * @param strName Name of the swap curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the swap discount curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.DiscountCurve> LoadEODIRSwapCurves (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == dtStart || null == dtEnd || dtStart.getJulian() >
			dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.DiscountCurve dc = LoadEODIRSwapCurve (strName, dt);

			if (null != dc) mapDC.put (dt, dc);

			dt = dt.addDays (1);
		}

		return mapDC;
	}

	/**
	 * Gets the set of treasury curves available for a given date
	 * 
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return The set of treasury curves
	 */

	public static final java.util.Set<java.lang.String> GetEODTSYCurveNames (
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == s_stmt) return null;

		return org.drip.service.env.RatesManager.GetIRCurves (s_stmt, dtEOD, "government");
	}

	/**
	 * Loads the live TSY curve
	 * 
	 * @param strName Name of the TSY curve to be loaded
	 * 
	 * @return Current TSY discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadLiveTSYCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing TSY curve
	 * 
	 * @param strName Name of the TSY curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing TSY discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODTSYCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == s_stmt) return null;

		return org.drip.service.env.EODCurves.LoadEODIR (s_stmt, dtEOD, strName, "government", strName);
	}

	/**
	 * Loads the set of TSY discount curves between two dates
	 * 
	 * @param strName Name of the swap curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the TSY discount curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.DiscountCurve> LoadEODTSYCurves (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == dtStart || null == dtEnd || dtStart.getJulian() >
			dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.DiscountCurve> mapDC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.DiscountCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.DiscountCurve dc = LoadEODTSYCurve (strName, dt);

			if (null != dc) mapDC.put (dt, dc);

			dt = dt.addDays (1);
		}

		return mapDC;
	}

	/**
	 * Gets the set of CDS curves available for a given date
	 * 
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return The set of CDS curves
	 */

	public static final java.util.Set<java.lang.String> GetEODCDSCurveNames (
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == s_stmt) return null;

		return org.drip.service.env.CDSManager.GetCreditCurves (s_stmt, dtEOD);
	}

	/**
	 * Loads the live CDS credit curve
	 * 
	 * @param strName Name of the CDS curve to be loaded
	 * 
	 * @return Current CDS credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadLiveCDSCreditCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing CDS curve
	 * 
	 * @param strName Name of the CDS curve to be loaded
	 * @param strCurrency Currency string
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing CDS credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadEODCDSCreditCurve (
		final java.lang.String strName,
		final java.lang.String strCurrency,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == strName || strName.isEmpty() || null == strCurrency || strCurrency.isEmpty() || null ==
			dtEOD || null == s_stmt)
			return null;

		org.drip.analytics.definition.DiscountCurve dc = LoadEODFullIRCurve (strCurrency, dtEOD);

		if (null == dc) return null;

		org.drip.param.definition.CreditScenarioCurve ccsg =
			org.drip.service.env.EODCurves.BuildEODCreditCurve (s_stmt, dtEOD, dc, strName, strCurrency);

		if (null == ccsg) return null;

		return ccsg.getCCBase();
	}

	/**
	 * Loads the set of CDS credit curves between two dates
	 * 
	 * @param strName Name of the CDS curve to be loaded
	 * @param strCurrency Currency string
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the CDS credit curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.CreditCurve> LoadEODCDSCreditCurves (
			final java.lang.String strName,
			final java.lang.String strCurrency,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == strName|| strName.isEmpty() || null == strCurrency || strCurrency.isEmpty() || null ==
			dtStart || null == dtEnd || dtStart.getJulian() > dtEnd.getJulian() || null == s_stmt)
			return null;

		org.drip.analytics.date.JulianDate dt = null;

		try {
			dt = new org.drip.analytics.date.JulianDate (dtStart.getJulian());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.Map<org.drip.analytics.date.JulianDate, org.drip.analytics.definition.CreditCurve> mapCC
			= new java.util.TreeMap<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.CreditCurve>();

		while (dt.getJulian() <= dtEnd.getJulian()) {
			org.drip.analytics.definition.CreditCurve cc = LoadEODCDSCreditCurve (strName, strCurrency, dt);

			if (null != cc) mapCC.put (dt, cc);

			dt = dt.addDays (1);
		}

		return mapCC;
	}

	/**
	 * Loads the live bond credit curve
	 * 
	 * @param strName Name of the bond curve to be loaded
	 * 
	 * @return Current bond credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadLiveBondCreditCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing bond credit curve
	 * 
	 * @param strName Name of the bond curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing bond credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadEODBondCreditCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == s_stmt) return null;

		return null;
	}

	/**
	 * Loads the set of bond credit curves between two dates
	 * 
	 * @param strName Name of the bond curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the bond credit curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.CreditCurve> LoadEODBondCreditCurve (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == dtStart || null == dtEnd || null == strName || strName.isEmpty() || null == s_stmt)
			return null;

		return null;
	}

	/**
	 * Loads the live credit curve
	 * 
	 * @param strName Name of the credit curve to be loaded
	 * 
	 * @return Current credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadLiveFullCreditCurve (
		final java.lang.String strName)
	{
		return null;
	}

	/**
	 * Loads the closing credit curve
	 * 
	 * @param strName Name of the credit curve to be loaded
	 * @param dtEOD JulianDate EOD
	 * 
	 * @return Closing credit curve
	 */

	public static final org.drip.analytics.definition.CreditCurve LoadEODFullCreditCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == dtEOD || null == strName || strName.isEmpty() || null == s_stmt) return null;

		return null;
	}

	/**
	 * Loads the set of credit curves between two dates
	 * 
	 * @param strName Name of the credit curve to be loaded
	 * @param dtStart JulianDate start
	 * @param dtEnd JulianDate end
	 * 
	 * @return Map containing the dates and the credit curves
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.definition.CreditCurve> LoadEODFullCreditCurve (
			final java.lang.String strName,
			final org.drip.analytics.date.JulianDate dtStart,
			final org.drip.analytics.date.JulianDate dtEnd)
	{
		if (null == dtStart || null == dtEnd || null == strName || strName.isEmpty() || null == s_stmt)
			return null;

		return null;
	}

	/**
	 * Calculates the EOD Measures for the CDS using the closing discount and the credit curves
	 * 
	 * @param cds Input Credit Default Swap
	 * @param dtEOD EOD
	 * 
	 * @return Map of the EOD CDS measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> GetEODCDSMeasures (
		final org.drip.product.definition.CreditDefaultSwap cds,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == cds || null == dtEOD) return null;

		java.lang.String strIR = cds.getIRCurveName();

		java.lang.String strCC = cds.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty()) return null;

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		if (null == dcEOD || null == ccEOD) return null;

		return cds.value (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
			org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD, null,
					null, ccEOD, null, null, null), null);
	}

	/**
	 * Calculate the CDS measures from live discount and credit curves
	 * 
	 * @param cds
	 * 
	 * @return Map of live measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> GetLiveCDSMeasures (
		final org.drip.product.definition.CreditDefaultSwap cds)
	{
		if (null == cds) return null;

		return null;
	}

	/**
	 * Constructs/retrieves the bond object from a given bond ID and date
	 * 
	 * @param strBondId Bond ID string
	 * @param dt JulianDate
	 * 
	 * @return Bond object
	 */

	public static final org.drip.product.definition.Bond GetBond (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		org.drip.product.definition.Bond bond = GetBondFromCache (strBondId);

		if (null != bond) return bond;

		if (null == s_stmt) return null;

		if (null != dt)
			bond = org.drip.service.env.BondManager.LoadFromBondId (null, s_stmt, strBondId, dt.getJulian());
		else
			bond = org.drip.service.env.BondManager.LoadFromBondId (null, s_stmt, strBondId,
				org.drip.analytics.date.JulianDate.Today().getJulian());

		_mapBondCache.put (strBondId, bond);

		return bond;
	}

	/**
	 * Gets the bond from its ID
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return The bond object
	 */

	public static final org.drip.product.definition.Bond GetBond (
		final java.lang.String strBondId)
	{
		return GetBond (strBondId, null);
	}

	/**
	 * Maps the bond to an ID and adds it to the cache
	 * 
	 * @param strBondId Bond ID
	 * @param bond Bond object to be mapped/added
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean PutBond (
		final java.lang.String strBondId,
		final org.drip.product.definition.Bond bond)
	{
		if (null == bond || null == strBondId || strBondId.isEmpty()) return false;

		_mapBondCache.put (strBondId, bond);

		return true;
	}

	/**
	 * Removes the bond ID from the cache
	 * 
	 * @param strBondId Bond ID to be removed
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean RemoveBond (
		final java.lang.String strBondId)
	{
		if (null == strBondId || strBondId.isEmpty()) return false;

		_mapBondCache.remove (strBondId);

		return true;
	}

	/**
	 * Retrieves the bond's reference data
	 * 
	 * @param strBondId Bond ID whose reference data is to be retrieved
	 * 
	 * @return BondRefDataBuilder object
	 */

	public static final org.drip.product.creator.BondRefDataBuilder GetBondRefData (
		final java.lang.String strBondId)
	{
		org.drip.product.creator.BondRefDataBuilder brdb = GetBondRefDataFromCache (strBondId);

		if (null != brdb) return brdb;

		if (null == s_stmt) return null;

		_mapBondRefDataCache.put (strBondId, brdb = org.drip.service.env.BondManager.LoadBondRefData (s_stmt,
			strBondId));

		return brdb;
	}

	/**
	 * Retrieves the bond's call option schedule from the given date
	 * 
	 * @param strBondId Bond ID
	 * @param dt JulianDate from which the schedule starts
	 * 
	 * @return EmbeddedOptionSchedule object
	 */

	public static final org.drip.product.params.EmbeddedOptionSchedule GetBondCallEOS (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == GetBond (strBondId, dt)) return null;

		return GetBond (strBondId).getEmbeddedCallSchedule();
	}

	/**
	 * Retrieves the bond's call option schedule
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return EmbeddedOptionSchedule object
	 */

	public static final org.drip.product.params.EmbeddedOptionSchedule GetBondCallEOS (
		final java.lang.String strBondId)
	{
		return GetBondCallEOS (strBondId, null);
	}

	/**
	 * Retrieves the bond's put option schedule from the given date
	 * 
	 * @param strBondId Bond ID
	 * @param dt JulianDate from which the schedule starts
	 * 
	 * @return EmbeddedOptionSchedule object
	 */

	public static final org.drip.product.params.EmbeddedOptionSchedule GetBondPutEOS (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == GetBond (strBondId, dt)) return null;

		return GetBond (strBondId).getEmbeddedPutSchedule();
	}

	/**
	 * Retrieves the bond's put option schedule
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return EmbeddedOptionSchedule object
	 */

	public static final org.drip.product.params.EmbeddedOptionSchedule GetBondPutEOS (
		final java.lang.String strBondId)
	{
		return GetBondPutEOS (strBondId, null);
	}

	/**
	 * Retrieves all the available issuer tickers
	 * 
	 * @return Set of available issuer tickers
	 */

	public static final java.util.Set<java.lang.String> GetAvailableTickers()
	{
		if (null == s_stmt) return null;

		return org.drip.service.env.BondManager.GetAvailableTickers (s_stmt);
	}

	/**
	 * Creates the fixings object for the given bond and fix coupon, based off of the period represented by
	 *  the specified date
	 *  
	 * @param bond Bond object
	 * @param dtValue Valuation date (Date representing the period whose fixing is set)
	 * @param dblFix Fix coupon
	 * 
	 * @return The fixings object map
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> CreateFixingsObject (
			final org.drip.product.credit.BondComponent bond,
			final org.drip.analytics.date.JulianDate dtValue, final double dblFix)
	{
		return org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond, dtValue, dblFix);
	}

	/**
	 * Retrieves the ISINs for the specified issuer ticker
	 * 
	 * @param strTicker The issuer ticker
	 * 
	 * @return The set of tickers
	 */

	public static final java.util.List<java.lang.String> GetISINsForTicker (
		final java.lang.String strTicker)
	{
		if (null == s_stmt || null == strTicker || strTicker.isEmpty()) return null;

		return org.drip.service.env.BondManager.GetISINsForTicker (s_stmt, strTicker);
	}

	/**
	 * Is this floating rate bond
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return Bond is a floater
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final boolean IsBondFloater (
		final java.lang.String strBondId)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			throw new java.lang.Exception ("Invalid input into CreditAnalytics.IsBondFloater");

		return GetBond (strBondId).isFloater();
	}

	/**
	 * Calculates the bond work-out details from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Quoting Parameters
	 * 
	 * @return The work-out info instance
	 */

	public static final org.drip.param.valuation.WorkoutInfo BondWorkoutInfoFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			return null;

		return GetBond (strBondId).calcExerciseYieldFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond work-out details from price (Simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return The work-out info instance
	 */

	public static final org.drip.param.valuation.WorkoutInfo BondWorkoutInfoFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice)
	{
		return BondWorkoutInfoFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond yield from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondYieldFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondYieldFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.param.valuation.WorkoutInfo wi = GetBond (strBondId).calcExerciseYieldFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Cannot calc wi for bond " + strBondId + " priced at " +
				dblPrice + " on " + new org.drip.analytics.date.JulianDate (valParams._dblValue));

		return wi._dblYield;
	}

	/**
	 * Calculates the bond YTM from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondYTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondYTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcYieldFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond yield from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondYieldFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice) throws java.lang.Exception
	{
		return BondYieldFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond Z Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondZSpreadFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseZSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond Z Spread to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Z Spread To Maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondZTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcZSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond Z Spread from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice) throws java.lang.Exception
	{
		return BondZSpreadFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond OAS from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondOASFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondOASFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseOASFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond OAS to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Params
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double OAS To Maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondOASTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondOASTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcOASFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond OAS from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondOASFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondOASFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond I Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondISpreadFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseISpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond I Spread to Maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double I Spread To Maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondITMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondITMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcISpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond I Spread from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondISpreadFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond Discount Margin from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondDiscountMarginFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseDiscountMarginFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond Discount Margin to Maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Discount Margin To Maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondDiscountMarginTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcDiscountMarginFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
					null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond Discount Margin from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondDiscountMarginFromPrice (strBondId,
			org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0, "",
				org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond spread to treasury from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double treasury spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondTSYSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondTSYSpreadFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseTSYSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond spread over treasury to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double treasury spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondTSYTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondTSYTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcTSYSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond spread to treasury from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double treasury spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondTSYSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondTSYSpreadFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblPrice, null);
	}

	/**
	 * Calculates the bond G spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double G spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondGSpreadFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseGSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond G spread to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double G spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondGTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcGSpreadFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond G Spread from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondGSpreadFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblPrice, null);
	}

	/**
	 * Calculates the bond par asset swap Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double par asset swap Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondASWFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseParASWFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond par asset swap Spread to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double par asset swap Spread to maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondASWTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcParASWFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond Par ASW Spread from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblPrice Price
	 * 
	 * @return Double Par ASW Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondParASWFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblPrice, null);
	}

	/**
	 * Calculates the bond credit basis from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double bond credit basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondCreditBasisFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExerciseCreditBasisFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond credit basis to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double bond credit basis to maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondCreditBasisTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcCreditBasisFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the bond credit basis from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * 
	 * @return Double bond credit basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondCreditBasisFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, cc, dblPrice, null);
	}

	/**
	 * Calculates the Bond PECS from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Bond PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondPECSFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcExercisePECSFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the Bond PECS to maturity from price
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Bond PECS to maturity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSTMFromPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondPECSTMFromPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcPECSFromPrice (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblPrice);
	}

	/**
	 * Calculates the Bond PECS from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblPrice Price
	 * 
	 * @return Double Bond PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondPECSFromPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, cc, dblPrice, null);
	}

	/**
	 * Calculates the bond price from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPriceFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondYieldFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcPriceFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the bond price from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPriceFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondPriceFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread, null);
	}

	/**
	 * Calculates the bond yield from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondYieldFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondYieldFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcYieldFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), dblTSYSpread);
	}

	/**
	 * Calculates the bond yield from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondYieldFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondYieldFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dcTSY, dcTSY, dblTSYSpread);
	}

	/**
	 * Calculates the bond Z spread from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Z spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondZSpreadFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcZSpreadFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the bond Z spread from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double Z spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondZSpreadFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread, null);
	}

	/**
	 * Calculates the bond OAS from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondOASFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondOASFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcOASFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the bond OAS from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondOASFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondOASFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread, null);
	}

	/**
	 * Calculates the bond I spread from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double I spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondISpreadFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcISpreadFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), dblTSYSpread);
	}

	/**
	 * Calculates the bond I spread from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double I spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondISpreadFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread);
	}

	/**
	 * Calculates the bond Discount Margin from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception
				("Bad inputs into CreditAnalytics.BondDiscountMarginFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcDiscountMarginFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), dblTSYSpread);
	}

	/**
	 * Calculates the bond Discount Margin from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondISpreadFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread);
	}

	/**
	 * Calculates the bond G spread from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double G spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondGSpreadFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcGSpreadFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), dblTSYSpread);
	}

	/**
	 * Calculates the bond G spread from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double G spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondGSpreadFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread);
	}

	/**
	 * Calculates the bond par asset swap spread from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double par asset swap spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondParASWFromTSYSpread");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcParASWFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the bond par asset swap spread from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double par asset swap spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondParASWFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblTSYSpread, null);
	}

	/**
	 * Calculates the bond credit basis from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param cc Credit Curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double credit basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == cc || null == strBondId ||
			strBondId.isEmpty() || null == GetBond (strBondId))
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondCreditBasisFromTSYSpread");

		return GetBond (strBondId).calcCreditBasisFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, cc, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the bond credit basis from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param cc Credit Curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double credit basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondCreditBasisFromTSYSpread (strBondId,
			org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0, "",
				org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, cc, dblTSYSpread, null);
	}

	/**
	 * Calculates the Bond PECS from spread to a treasury benchmark
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param cc Credit Curve
	 * @param dblTSYSpread Spread to treasury
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblTSYSpread,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == cc || null == strBondId ||
			strBondId.isEmpty() || null == GetBond (strBondId))
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondPECSFromTSYSpread");

		return GetBond (strBondId).calcCreditBasisFromTSYSpread (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, cc, null, null, null), quotingParams, dblTSYSpread);
	}

	/**
	 * Calculates the Bond PECS from spread to a treasury benchmark (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY Treasury discount curve
	 * @param cc Credit Curve
	 * @param dblTSYSpread Spread to treasury
	 * 
	 * @return Double PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondPECSFromTSYSpread (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, cc, dblTSYSpread,
				null);
	}

	/**
	 * Calculates the bond price from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPriceFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondPriceFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcPriceFromYield (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblYield);
	}

	/**
	 * Calculates the bond price from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPriceFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondPriceFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblYield, null);
	}

	/**
	 * Calculates the bond Z spread from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Z spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondZSpreadFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcZSpreadFromYield (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblYield);
	}

	/**
	 * Calculates the bond Z spread from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double Z spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondZSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondZSpreadFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblYield, null);
	}

	/**
	 * Calculates the bond I spread from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double I spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondISpreadFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcISpreadFromYTM (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), dblYield);
	}

	/**
	 * Calculates the bond I spread from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double I spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondISpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondISpreadFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblYield, null);
	}

	/**
	 * Calculates the bond Discount Margin from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondDiscountMarginFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcISpreadFromYTM (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), dblYield);
	}

	/**
	 * Calculates the bond Discount Margin from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondDiscountMarginFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondDiscountMarginFromYield (strBondId,
			org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0, "",
				org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblYield, null);
	}

	/**
	 * Calculates the bond G spread from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dcTSY TSY Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double G spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == dcTSY || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondGSpreadFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcGSpreadFromYTM (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
				dcTSY, null, null, null, null), dblYield);
	}

	/**
	 * Calculates the bond G spread from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dcTSY TSY Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double G spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondGSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondGSpreadFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dcTSY, dblYield, null);
	}

	/**
	 * Calculates the bond par ASW from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondParASWFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcParASWFromYield (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				null, null, null, null), quotingParams, dblYield);
	}

	/**
	 * Calculates the bond Par ASW from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param dblYield YTM
	 * 
	 * @return Double Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondParASWFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondParASWFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt,
			0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, dblYield, null);
	}

	/**
	 * Calculates the bond Credit Basis from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondCreditBasisFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcCreditBasisFromYield (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblYield);
	}

	/**
	 * Calculates the bond Credit Basis from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblYield YTM
	 * 
	 * @return Double Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondCreditBasisFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondCreditBasisFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams
			(dt, 0, "", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, cc, dblYield, null);
	}

	/**
	 * Calculates the Bond PECS from yield
	 * 
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblYield YTM
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Double PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromYield (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblYield,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondPECSFromYield");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcPECSFromYield (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, dblYield);
	}

	/**
	 * Calculates the Bond PECS from yield (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param dblYield YTM
	 * 
	 * @return Double PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondPECSFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondPECSFromYield (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, cc, dblYield, null);
	}

	/**
	 * Computes the bond's theoretical price from discount curve and the credit curve
	 *  
	 * @param strBondId Bond ID
	 * @param valParams Valuation Parameters
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param quotingParams Bond Quoting parameters
	 * 
	 * @return Price
	 * 
	 * @throws java.lang.Exception Thrown if exception encountered during calculation
	 */

	public static final double BondCreditPrice (
		final java.lang.String strBondId,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		if (null == valParams || null == dc || null == cc || null == strBondId || strBondId.isEmpty())
			throw new java.lang.Exception ("Bad inputs into CreditAnalytics.BondCreditPrice");

		if (null == GetBond (strBondId))
			throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		return GetBond (strBondId).calcPriceFromCreditBasis (valParams,
			org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null,
				cc, null, null, null), quotingParams, 0.);
	}

	/**
	 * Computes the bond's theoretical price from discount curve and the credit curve (simplified version)
	 *  
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * 
	 * @return Price
	 * 
	 * @throws java.lang.Exception Thrown if exception encountered during calculation
	 */

	public static final double BondCreditPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception
	{
		return BondCreditPrice (strBondId, org.drip.param.valuation.ValuationParams.CreateValParams (dt, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), dc, cc, null);
	}

	/**
	 * Returns the effective date for the specified bond
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return Effective
	 */

	public static final org.drip.analytics.date.JulianDate EffectiveDate (
		final java.lang.String strBondId)
	{
		if (null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId)) return null;

		return GetBond (strBondId).getEffectiveDate();
	}

	/**
	 * Returns the maturity date for the specified bond
	 * 
	 * @param strBondId Bond ID
	 * 
	 * @return Maturity
	 */

	public static final org.drip.analytics.date.JulianDate MaturityDate (
		final java.lang.String strBondId)
	{
		if (null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId)) return null;

		return GetBond (strBondId).getMaturityDate();
	}

	/**
	 * Returns the coupon date for the period prior to the specified date for the specified bond
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * 
	 * @return The previous coupon date
	 */

	public static final org.drip.analytics.date.JulianDate PreviousCouponDate (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt || null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			return null;

		try {
			return GetBond (strBondId).calcPreviousCouponDate (dt);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the coupon date for the coupon period current to the specified date for the specified bond
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * 
	 * @return The current coupon date
	 */

	public static final org.drip.analytics.date.JulianDate CurrentCouponDate (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt || null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			return null;

		try {
			return GetBond (strBondId).calcCurrentCouponDate (dt);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the coupon date for the period subsequent to the specified date for the specified bond
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * 
	 * @return The next coupon date
	 */

	public static final org.drip.analytics.date.JulianDate NextCouponDate (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt || null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			return null;

		try {
			return GetBond (strBondId).calcNextCouponDate (dt);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the next valid exercise info (post notice period adjustments) subsequent to the specified date
	 * 
	 * @param strBondId Bond ID
	 * @param dt Valuation Date
	 * 
	 * @return Next Exercise Info
	 */

	public static final org.drip.analytics.output.ExerciseInfo NextExerciseInfo (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt || null == strBondId || strBondId.isEmpty() || null == GetBond (strBondId))
			return null;

		return GetBond (strBondId).calcNextValidExerciseInfo (dt);
	}

	/**
	 * Indicates whether the specified date exists in the first coupon period
	 * 
	 * @param strBondId Bond ID
	 * @param dblDate Valuation Date
	 * 
	 * @return True => Input date belongs to the first period
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public static final boolean InFirstPeriod (
		final java.lang.String strBondId,
		final double dblDate)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate) || null == strBondId || strBondId.isEmpty() || null == GetBond
			(strBondId))
			throw new java.lang.Exception ("Invalid inputs into CreditAnalytics.InFirstPeriod");

		return GetBond (strBondId).inFirstCouponPeriod (dblDate);
	}

	/**
	 * Indicates whether the specified date exists in the last coupon period
	 * 
	 * @param strBondId Bond ID
	 * @param dblDate Valuation Date
	 * 
	 * @return True => Input date belongs to the last period
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public static final boolean InLastPeriod (
		final java.lang.String strBondId,
		final double dblDate)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblDate) || null == strBondId || strBondId.isEmpty() || null == GetBond
			(strBondId))
			throw new java.lang.Exception ("Invalid inputs into CreditAnalytics.InFirstPeriod");

		return GetBond (strBondId).inLastCouponPeriod (dblDate);
	}

	/**
	 * Calculates the EOD bond Convexity from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODConvexityFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODConvexityFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcConvexityFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond Credit Basis from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODCreditBasisFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODCreditBasisFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.calcCreditBasisFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, ccEOD, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD Bond PECS from Price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODPECSFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODPECSFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.calcPECSFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, ccEOD, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond Duration from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDurationFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDurationFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcDurationFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond G Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODGSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODGSpreadFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strTSY = bond.getTreasuryCurveName();

		if (null == strTSY || strTSY.isEmpty())
			throw new java.lang.Exception ("Cannot locate TSY Curve for bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcTSY = LoadEODTSYCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcGSpreadFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				dcTSY, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond I Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODISpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODISpreadFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcISpreadFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond Discount Margin from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDiscountMarginFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDiscountMarginFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcDiscountMarginFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond OAS from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODOASFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODOASFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcOASFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond par ASW from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODParASWFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODParASWFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcParASWFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond TSY Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD TSY Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODTSYSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODTSYSpreadFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcTSYSpreadFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblPrice);
	}

	/**
	 * Calculates the EOD bond yield from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODYieldFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODYieldFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.param.valuation.WorkoutInfo wi = bond.calcExerciseYieldFromPrice
			(org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD, null,
					null, null, null, null, null), null, dblPrice);

		if (null == wi)
			throw new java.lang.Exception ("Cannot calc wi for bond " + strBondId + " priced at " + dblPrice
				+ " on " + dtEOD);

		return wi._dblYield;
	}

	/**
	 * Calculates the EOD bond Z Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Double EOD Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODZSpreadFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			throw new java.lang.Exception ("CreditAnalytics.BondEODZSpreadFromPrice: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcZSpreadFromPrice (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblPrice);
	}

	/**
	 * Calculates the Live bond Convexity from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveConvexityFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODConvexityFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond Credit Basis from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveCreditBasisFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODCreditBasisFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live Bond PECS from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLivePECSFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODPECSFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond Duration from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveDurationFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODDurationFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond G Spread from price (simplified version)
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODGSpreadFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODGSpreadFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond I Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveISpreadFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODISpreadFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond OAS from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Live EOD OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveOASFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODOASFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond par ASW from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveParASWFromPrice (final java.lang.String strBondId, final double
		dblPrice) throws java.lang.Exception {
		return BondEODParASWFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond TSY Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live TSY Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveTSYSpreadFromPrice (
		final java.lang.String strBondId, 
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODTSYSpreadFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond yield from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveYieldFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODYieldFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the Live bond Z Spread from price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Double Live Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveZSpreadFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
		throws java.lang.Exception
	{
		return BondEODZSpreadFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Calculates the EOD bond Convexity from yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODConvexityFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODConvexityFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcConvexityFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond Credit Basis from yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODCreditBasisFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODCreditBasisFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.calcCreditBasisFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, ccEOD, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD Bond PECS from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODPECSFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODPECSFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.calcPECSFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, ccEOD, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond Duration from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDurationFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDurationFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcDurationFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond G Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODGSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODGSpreadFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strTSY = bond.getTreasuryCurveName();

		if (null == strTSY || strTSY.isEmpty())
			throw new java.lang.Exception ("Cannot locate TSY Curve for bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcTSY = LoadEODTSYCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcGSpreadFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				dcTSY, null, null, null, null, null), dblYield);
	}

	/**
	 * Calculates the EOD bond I Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODISpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODISpreadFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcISpreadFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), dblYield);
	}

	/**
	 * Calculates the EOD bond Discount Margin from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDiscountMarginFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDiscountMarginFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcDiscountMarginFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, null, null, null, null, null), dblYield);
	}

	/**
	 * Calculates the EOD bond OAS from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODOASFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODOASFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcOASFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond par ASW from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODParASWFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODParASWFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcParASWFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond Price from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODPriceFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODPriceFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcPriceFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond TSY Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD TSY Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODTSYSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODTSYSpreadFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcTSYSpreadFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblYield);
	}

	/**
	 * Calculates the EOD bond Z Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Double EOD Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODZSpreadFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			throw new java.lang.Exception ("CreditAnalytics.BondEODZSpreadFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		return bond.calcZSpreadFromYield (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, null, null, null, null, null), null, dblYield);
	}

	/**
	 * Calculates the Live bond Convexity from yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveConvexityFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODConvexityFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond Credit Basis from yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveCreditBasisFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODCreditBasisFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live Bond PECS from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLivePECSFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODPECSFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond Duration from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveDurationFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODDurationFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond G Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveGSpreadFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODGSpreadFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond I Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveISpreadFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODISpreadFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond OAS from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveOASFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODOASFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond par ASW from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveParASWFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODConvexityFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond Price from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLivePriceFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODPriceFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond TSY Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live TSY Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveTSYSpreadFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODTSYSpreadFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the Live bond Z Spread from Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Double Live Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveZSpreadFromYield (
		final java.lang.String strBondId,
		final double dblYield)
		throws java.lang.Exception
	{
		return BondEODZSpreadFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Calculates the EOD bond Convexity from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODConvexityFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODConvexityFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcConvexityFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Credit Basis from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODCreditBasisFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODCreditBasisFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcCreditBasisFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, ccEOD, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt,
					dtEOD, strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD Bond PECS from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODPECSFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODPECSFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		java.lang.String strCC = bond.getCreditCurveName();

		if (null == strIR || strIR.isEmpty() || null == strCC || strCC.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR or Credit Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.CreditCurve ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcPECSFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, ccEOD, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt,
					dtEOD, strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Duration from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDurationFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDurationFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcDurationFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond G Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODGSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODGSpreadFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (bond.getIRCurveName(), dtEOD);

		java.lang.String strTSY = bond.getTreasuryCurveName();

		if (null == strTSY || strTSY.isEmpty())
			throw new java.lang.Exception ("Cannot locate TSY Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcTSY = LoadEODTSYCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcGSpreadFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, dcTSY, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt,
					dtEOD, strIR), null), dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond I Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODISpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODISpreadFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcISpreadFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Discount Margin from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Discount Margin
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODDiscountMarginFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODDiscountMarginFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcDiscountMarginFromTSYSpread
			(org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD, null,
					dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD, strIR),
						null), dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond OAS from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODOASFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODOASFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcOASFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD,
			strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
				null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD, strIR),
					null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond par ASW from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODParASWFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODParASWFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcParASWFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Price from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODPriceFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODPriceFromYield: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond for ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcPriceFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Yield from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODYieldFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODYieldFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null != strIR && !strIR.isEmpty()) dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcYieldFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), dblTSYSpread);
	}

	/**
	 * Calculates the EOD bond Z Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double EOD Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondEODZSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			throw new java.lang.Exception ("CreditAnalytics.BondEODZSpreadFromTSYSpread: Bad inputs");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot locate bond with ID " + strBondId);

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty())
			throw new java.lang.Exception ("Cannot locate IR Curve for bond with ID " + strBondId);

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		java.lang.String strEDSF = bond.getEDSFCurveName();

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		return bond.calcZSpreadFromTSYSpread (org.drip.param.valuation.ValuationParams.CreateStdValParams
			(dtEOD, strIR), org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
				(dcEOD, null, dcEDSF, null, null, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
					strIR), null), null, dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Convexity from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Convexity
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveConvexityFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODConvexityFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Credit Basis from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Credit Basis
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveCreditBasisFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODCreditBasisFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live Bond PECS from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live PECS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLivePECSFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODPECSFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Duration from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Duration
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveDurationFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODDurationFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond G Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Live EOD G Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveGSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODZSpreadFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond I Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live I Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveISpreadFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODISpreadFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond OAS from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live OAS
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveOASFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODOASFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(), dblTSYSpread);
	}

	/**
	 * Calculates the Live bond par ASW from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Par ASW
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveParASWFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODParASWFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Price from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Price
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLivePriceFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODPriceFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Yield from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Yield
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveYieldFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODYieldFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Calculates the Live bond Z Spread from TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Double Live Z Spread
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public static final double BondLiveZSpreadFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
		throws java.lang.Exception
	{
		return BondEODZSpreadFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Get the full set of the Bond's EOD Measures From Clean Price
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblPrice Price
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondEODMeasuresFromPrice (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblPrice)
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblPrice))
			return null;

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) return null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty()) return null;

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		if (null == dcEOD) return null;

		org.drip.param.definition.ComponentQuote cq =
			org.drip.param.creator.ComponentQuoteBuilder.CreateComponentQuote();

		try {
			// cq.addQuote ("Price", org.drip.product.quote.Quote ("mid", dblPrice), true);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.lang.String strCC = bond.getCreditCurveName();

		java.lang.String strTSY = bond.getTreasuryCurveName();

		java.lang.String strEDSF = bond.getEDSFCurveName();

		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;
		org.drip.analytics.definition.CreditCurve ccEOD = null;

		if (null == strTSY || strTSY.isEmpty()) dcTSY = LoadEODTSYCurve (strTSY, dtEOD);

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		if (null == strCC || strCC.isEmpty()) ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.value (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
			org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
					dcTSY, dcEDSF, ccEOD, cq, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
						strIR), null), null);
	}

	/**
	 * Get the full set of the Bond's Live Measures From Clean Price
	 * 
	 * @param strBondId Bond ID
	 * @param dblPrice Price
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondLiveMeasuresFromPrice (
		final java.lang.String strBondId,
		final double dblPrice)
	{
		return BondEODMeasuresFromPrice (strBondId, org.drip.analytics.date.JulianDate.Today(), dblPrice);
	}

	/**
	 * Get the full set of the Bond's EOD Measures From the TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondEODMeasuresFromTSYSpread (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblTSYSpread)
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN
			(dblTSYSpread))
			return null;

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) return null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty()) return null;

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		if (null == dcEOD) return null;

		org.drip.param.definition.ComponentQuote cq =
			org.drip.param.creator.ComponentQuoteBuilder.CreateComponentQuote();

		try {
			cq.addQuote ("TSYSpread", org.drip.param.creator.QuoteBuilder.CreateQuote ("mid", dblTSYSpread),
				true);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.lang.String strCC = bond.getCreditCurveName();

		java.lang.String strTSY = bond.getTreasuryCurveName();

		java.lang.String strEDSF = bond.getEDSFCurveName();

		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;
		org.drip.analytics.definition.CreditCurve ccEOD = null;

		if (null == strTSY || strTSY.isEmpty()) dcTSY = LoadEODTSYCurve (strTSY, dtEOD);

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		if (null == strCC || strCC.isEmpty()) ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.value (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
			org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
					dcTSY, dcEDSF, ccEOD, cq, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
						strIR), null), null);
	}

	/**
	 * Get the full set of the Bond's Live Measures From TSY Spread
	 * 
	 * @param strBondId Bond ID
	 * @param dblTSYSpread TSY Spread
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondLiveMeasuresFromTSYSpread (
		final java.lang.String strBondId,
		final double dblTSYSpread)
	{
		return BondEODMeasuresFromTSYSpread (strBondId, org.drip.analytics.date.JulianDate.Today(),
			dblTSYSpread);
	}

	/**
	 * Get the full set of the Bond's EOD Measures From the Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dtEOD EOD
	 * @param dblYield Yield
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondEODMeasuresFromYield (
		final java.lang.String strBondId,
		final org.drip.analytics.date.JulianDate dtEOD,
		final double dblYield)
	{
		if (null == strBondId || strBondId.isEmpty() || null == dtEOD || java.lang.Double.isNaN (dblYield))
			return null;

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) return null;

		java.lang.String strIR = bond.getIRCurveName();

		if (null == strIR || strIR.isEmpty()) return null;

		org.drip.analytics.definition.DiscountCurve dcEOD = LoadEODFullIRCurve (strIR, dtEOD);

		if (null == dcEOD) return null;

		org.drip.param.definition.ComponentQuote cq =
			org.drip.param.creator.ComponentQuoteBuilder.CreateComponentQuote();

		try {
			cq.addQuote ("Yield", org.drip.param.creator.QuoteBuilder.CreateQuote ("mid", dblYield), true);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.lang.String strCC = bond.getCreditCurveName();

		java.lang.String strTSY = bond.getTreasuryCurveName();

		java.lang.String strEDSF = bond.getEDSFCurveName();

		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;
		org.drip.analytics.definition.CreditCurve ccEOD = null;

		if (null == strTSY || strTSY.isEmpty()) dcTSY = LoadEODTSYCurve (strTSY, dtEOD);

		if (null == strEDSF || strEDSF.isEmpty()) dcEDSF = LoadEODEDFCurve (strEDSF, dtEOD);

		if (null == strCC || strCC.isEmpty()) ccEOD = LoadEODCDSCreditCurve (strCC, strIR, dtEOD);

		return bond.value (org.drip.param.valuation.ValuationParams.CreateStdValParams (dtEOD, strIR),
			org.drip.param.pricer.PricerParams.MakeStdPricerParams(),
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dcEOD,
					dcTSY, dcEDSF, ccEOD, cq, org.drip.service.env.EODCurves.GetTSYQuotes (s_stmt, dtEOD,
						strIR), null), null);
	}

	/**
	 * Get the full set of the Bond's Live Measures From Yield
	 * 
	 * @param strBondId Bond ID
	 * @param dblYield Yield
	 * 
	 * @return Map representing the bond measures
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> BondLiveMeasuresFromYield (
		final java.lang.String strBondId,
		final double dblYield)
	{
		return BondEODMeasuresFromYield (strBondId, org.drip.analytics.date.JulianDate.Today(), dblYield);
	}

	/**
	 * Retrieves the named boolean field for the given bond
	 * 
	 * @param strBondId Bond ID
	 * @param strField Field Name
	 * 
	 * @return Boolean field value
	 * 
	 * @throws java.lang.Exception Thrown if exception encountered during calculation
	 */

	public static final boolean GetBondBooleanField (
		final java.lang.String strBondId,
		final java.lang.String strField)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) 
			throw new java.lang.Exception ("Bad inputs into GetBondBooleanField");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot get Bond " + strBondId);

		if ("Callable".equalsIgnoreCase (strField)) return bond.isCallable();

		if ("Defaulted".equalsIgnoreCase (strField)) return bond.hasDefaulted();

		if ("Exercised".equalsIgnoreCase (strField)) return bond.hasBeenExercised();

		if ("Floater".equalsIgnoreCase (strField)) return bond.isFloater();

		if ("Perpetual".equalsIgnoreCase (strField)) return bond.isPerpetual();

		if ("Putable".equalsIgnoreCase (strField)) return bond.isPutable();

		if ("Sinkable".equalsIgnoreCase (strField)) return bond.isSinkable();

		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty())
			throw new java.lang.Exception ("Bad inputs into GetBondBooleanField");

		org.drip.product.creator.BondRefDataBuilder brdb = GetBondRefData (strBondId);

		if (null == brdb) throw new java.lang.Exception ("Bad inputs into GetBondBooleanField");

		if ("Bearer".equalsIgnoreCase (strField)) return brdb._bIsBearer;

		if ("PrivatePlacement".equalsIgnoreCase (strField)) return brdb._bIsPrivatePlacement;

		if ("Registered".equalsIgnoreCase (strField)) return brdb._bIsRegistered;

		if ("ReverseConvertible".equalsIgnoreCase (strField)) return brdb._bIsReversibleConvertible;

		if ("StructuredNote".equalsIgnoreCase (strField)) return brdb._bIsStructuredNote;

		if ("TradeStatus".equalsIgnoreCase (strField)) return brdb._bTradeStatus;

		if ("UnitTraded".equalsIgnoreCase (strField)) return brdb._bIsUnitTraded;

		throw new java.lang.Exception ("Cannot find field " + strField + " for Bond " + strBondId);
	}

	/**
	 * Retrieves the named integer field for the given bond
	 * 
	 * @param strBondId Bond ID
	 * @param strField Field Name
	 * 
	 * @return Integer field value
	 * 
	 * @throws java.lang.Exception Thrown if exception encountered during calculation
	 */

	public static final int GetBondIntegerField (
		final java.lang.String strBondId,
		final java.lang.String strField)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) 
			throw new java.lang.Exception ("Bad inputs into GetBondBooleanField");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot get Bond " + strBondId);

		if ("Frequency".equalsIgnoreCase (strField)) return bond.getCouponFreq();

		throw new java.lang.Exception ("Cannot find field " + strField + " for Bond " + strBondId);
	}

	/**
	 * Retrieves the named double field for the given bond
	 * 
	 * @param strBondId Bond ID
	 * @param strField Field Name
	 * 
	 * @return Double field value
	 * 
	 * @throws java.lang.Exception Thrown if exception encountered during calculation
	 */

	public static final double GetBondDoubleField (
		final java.lang.String strBondId,
		final java.lang.String strField)
		throws java.lang.Exception
	{
		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) 
			throw new java.lang.Exception ("Bad inputs into GetBondBooleanField");

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) throw new java.lang.Exception ("Cannot get Bond " + strBondId);

		if ("Coupon".equalsIgnoreCase (strField))
			return bond.getCoupon (org.drip.analytics.date.JulianDate.Today().getJulian(), null);

		if ("CurrentCoupon".equalsIgnoreCase (strField)) return bond.getCurrentCoupon();

		if ("FloatSpread".equalsIgnoreCase (strField)) return bond.getFloatSpread();

		if ("RedemptionValue".equalsIgnoreCase (strField)) return bond.getRedemptionValue();

		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty())
			throw new java.lang.Exception ("Bad inputs into GetBondDoubleField");

		org.drip.product.creator.BondRefDataBuilder brdb = GetBondRefData (strBondId);

		if (null == brdb) throw new java.lang.Exception ("Bad inputs into GetBondDoubleField");

		if ("IssueAmount".equalsIgnoreCase (strField)) return brdb._dblIssueAmount;

		if ("IssuePrice".equalsIgnoreCase (strField)) return brdb._dblIssuePrice;

		if ("MinimumIncrement".equalsIgnoreCase (strField)) return brdb._dblMinimumIncrement;

		if ("MinimumPiece".equalsIgnoreCase (strField)) return brdb._dblMinimumPiece;

		if ("OutstandingAmount".equalsIgnoreCase (strField)) return brdb._dblOutstandingAmount;

		if ("ParAmount".equalsIgnoreCase (strField)) return brdb._dblParAmount;

		throw new java.lang.Exception ("Cannot find field " + strField + " for Bond " + strBondId);
	}

	/**
	 * Retrieves the named string field for the given bond
	 * 
	 * @param strBondId Bond ID
	 * @param strField Field Name
	 * 
	 * @return String field value
	 */

	public static final java.lang.String GetBondStringField (
		final java.lang.String strBondId,
		final java.lang.String strField)
	{
		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) return null;

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) return null;

		if ("AccrualDC".equalsIgnoreCase (strField)) return bond.getAccrualDC();

		if ("CalculationType".equalsIgnoreCase (strField)) return bond.getCalculationType();

		if ("CouponCurrency".equalsIgnoreCase (strField)) return bond.getCouponCurrency();

		if ("CouponType".equalsIgnoreCase (strField)) return bond.getCouponType();

		if ("CreditCurve".equalsIgnoreCase (strField)) return bond.getCreditCurveName();

		if ("CUSIP".equalsIgnoreCase (strField)) return bond.getCUSIP();

		if ("CouponDC".equalsIgnoreCase (strField)) return bond.getCouponDC();

		if ("EDSFCurve".equalsIgnoreCase (strField)) return bond.getEDSFCurveName();

		if ("FloatCouponConvention".equalsIgnoreCase (strField)) return bond.getFloatCouponConvention();

		if ("IRCurve".equalsIgnoreCase (strField)) return bond.getIRCurveName();

		if ("ISIN".equalsIgnoreCase (strField)) return bond.getISIN();

		if ("MaturityType".equalsIgnoreCase (strField)) return bond.getMaturityType();

		if ("RateIndex".equalsIgnoreCase (strField)) return bond.getRateIndex();

		if ("RedemptionCurrency".equalsIgnoreCase (strField)) return bond.getRedemptionCurrency();

		if ("Ticker".equalsIgnoreCase (strField)) return bond.getTicker();

		if ("TradeCurrency".equalsIgnoreCase (strField)) return bond.getTradeCurrency();

		if ("TreasuryCurve".equalsIgnoreCase (strField)) return bond.getTreasuryCurveName();

		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) return null;

		org.drip.product.creator.BondRefDataBuilder brdb = GetBondRefData (strBondId);

		if (null == brdb) return null;

		if ("BBG_ID".equalsIgnoreCase (strField)) return brdb._strBBGID;

		if ("BBGParent".equalsIgnoreCase (strField)) return brdb._strBBGParent;

		if ("BBGUniqueID".equalsIgnoreCase (strField)) return brdb._strBBGUniqueID;

		if ("CDRCountryCode".equalsIgnoreCase (strField)) return brdb._strCDRCountryCode;

		if ("CDRSettleCode".equalsIgnoreCase (strField)) return brdb._strCDRSettleCode;

		if ("CollateralType".equalsIgnoreCase (strField)) return brdb._strCollateralType;

		if ("CountryOfDomicile".equalsIgnoreCase (strField)) return brdb._strCountryOfDomicile;

		if ("CountryOfGuarantor".equalsIgnoreCase (strField)) return brdb._strCountryOfGuarantor;

		if ("CountryOfIncorporation".equalsIgnoreCase (strField)) return brdb._strCountryOfIncorporation;

		if ("Description".equalsIgnoreCase (strField)) return brdb._strDescription;

		if ("ExchangeCode".equalsIgnoreCase (strField)) return brdb._strExchangeCode;

		if ("Fitch".equalsIgnoreCase (strField)) return brdb._strFitch;

		if ("IndustryGroup".equalsIgnoreCase (strField)) return brdb._strIndustryGroup;

		if ("IndustrySector".equalsIgnoreCase (strField)) return brdb._strIndustrySector;

		if ("IndustrySubgroup".equalsIgnoreCase (strField)) return brdb._strIndustrySubgroup;

		if ("Issuer".equalsIgnoreCase (strField)) return brdb._strIssuer;

		if ("IssuerCountry".equalsIgnoreCase (strField)) return brdb._strIssueCountry;

		if ("IssuerCountryCode".equalsIgnoreCase (strField)) return brdb._strIssueCountryCode;

		if ("LeadManager".equalsIgnoreCase (strField)) return brdb._strLeadManager;

		if ("LongCompanyName".equalsIgnoreCase (strField)) return brdb._strLongCompanyName;

		if ("MarketIssueType".equalsIgnoreCase (strField)) return brdb._strMarketIssueType;

		if ("Moody".equalsIgnoreCase (strField)) return brdb._strMoody;

		if ("Name".equalsIgnoreCase (strField)) return brdb._strName;

		if ("SecurityType".equalsIgnoreCase (strField)) return brdb._strSecurityType;

		if ("Series".equalsIgnoreCase (strField)) return brdb._strSeries;

		if ("SnP".equalsIgnoreCase (strField)) return brdb._strSnP;

		if ("SeniorSub".equalsIgnoreCase (strField)) return brdb._strSnrSub;

		if ("ShortName".equalsIgnoreCase (strField)) return brdb._strShortName;

		return null;
	}

	/**
	 * Retrieves the named date field for the given bond
	 * 
	 * @param strBondId Bond ID
	 * @param strField Field Name
	 * 
	 * @return Date field value
	 */

	public static final org.drip.analytics.date.JulianDate GetBondDateField (
		final java.lang.String strBondId,
		final java.lang.String strField)
	{
		if (null == strBondId || strBondId.isEmpty() || null == strField || strField.isEmpty()) return null;

		org.drip.product.definition.Bond bond = GetBond (strBondId);

		if (null == bond) return null;

		if ("FinalMaturity".equalsIgnoreCase (strField)) return bond.getFinalMaturity();

		if ("Maturity".equalsIgnoreCase (strField)) return bond.getMaturityDate();

		org.drip.product.creator.BondRefDataBuilder brdb = GetBondRefData (strBondId);

		if (null == brdb) return null;

		if ("AccrualStartDate".equalsIgnoreCase (strField)) return brdb._dtInterestAccrualStart;

		if ("AnnounceDate".equalsIgnoreCase (strField)) return brdb._dtAnnounce;

		if ("FirstCouponDate".equalsIgnoreCase (strField)) return brdb._dtFirstCoupon;

		if ("FirstSettleDate".equalsIgnoreCase (strField)) return brdb._dtFirstSettle;

		if ("IssueDate".equalsIgnoreCase (strField)) return brdb._dtIssue;

		if ("NextCouponDate".equalsIgnoreCase (strField)) return brdb._dtNextCouponDate;

		if ("PenultimateCouponDate".equalsIgnoreCase (strField)) return brdb._dtPrevCouponDate;

		if ("PreviousCouponDate".equalsIgnoreCase (strField)) return brdb._dtPrevCouponDate;

		return null;
	}

	/**
	 * Gets the outstanding issuer cumulative notional aggregated by the specified ascending maturity buckets
	 * 
	 * @param dtToday JulianDate representing the valuation date
	 * @param strTicker Issuer ticker
	 * @param adtAscending Array of ascending dates
	 * 
	 * @return Map of dates and cumulative aggregate notional
	 */

	public static final java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double>
		GetIssuerAggregateOutstandingNotional (
			final org.drip.analytics.date.JulianDate dtToday,
			final java.lang.String strTicker,
			final org.drip.analytics.date.JulianDate[] adtAscending)
	{
		if (null == strTicker || strTicker.isEmpty() || null == adtAscending || 0 == adtAscending.length ||
			null == s_stmt)
			return null;

		java.util.List<java.lang.String> lsstrISIN = GetISINsForTicker (strTicker);

		if (null == lsstrISIN || 0 == lsstrISIN.size()) return null;

		java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> mapOutstandingNotionals = new
			java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

		for (int i = 0; i < adtAscending.length; ++i)
			mapOutstandingNotionals.put (adtAscending[i], 0.);

		org.drip.analytics.date.JulianDate dtCurrent = dtToday;

		if (null == dtToday) dtCurrent = org.drip.analytics.date.JulianDate.Today();

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.definition.Bond bond = CreditAnalytics.GetBond (strISIN);

			if (null != bond && !bond.hasVariableCoupon() && !bond.hasBeenExercised() && !bond.hasDefaulted()
				&& bond.getMaturityDate().getJulian() > dtCurrent.getJulian()) {
				try {
					double dblOutstandingAmount = GetBondDoubleField (strISIN, "OutstandingAmount");

					for (org.drip.analytics.date.JulianDate dt : mapOutstandingNotionals.keySet()) {
						if (bond.getMaturityDate().getJulian() <= dt.getJulian()) {
							double dblCumulativeNotional = mapOutstandingNotionals.get (dt);

							mapOutstandingNotionals.put (dt, dblCumulativeNotional + dblOutstandingAmount);
						}
					}
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			}
		}

		return mapOutstandingNotionals;
	}

	/**
	 * @param strName Name of the bond basket
	 * @param astrBondId Array of bondIdentifiers (CUSIP/ISIN/etc) for the basket components
	 * @param adblWeights Basket component weights
	 * @param dtEffective Basket Effective Date
	 * @param dblNotional Basket Notional
	 * 
	 * @return The bond basket object
	 */

	public static final org.drip.product.definition.BasketProduct MakeBondBasket (
		final java.lang.String strName,
		final java.lang.String[] astrBondId,
		final double[] adblWeights,
		final org.drip.analytics.date.JulianDate dtEffective,
		final double dblNotional)
	{
		if (null == strName || strName.isEmpty() || null == astrBondId || 0 == astrBondId.length || null ==
			adblWeights || 0 == adblWeights.length || astrBondId.length != adblWeights.length || null ==
				dtEffective)
			return null;

		org.drip.product.definition.Bond[] aBond = new org.drip.product.definition.Bond[astrBondId.length];

		for (int i = 0; i < aBond.length; ++i) {
			if (null == astrBondId[i] || astrBondId[i].isEmpty() || null == (aBond[i] = GetBond
				(astrBondId[i]))) {
				System.out.println ("Basket component " + astrBondId[i] + " failed to create!");

				return null;
			}
		}

		try {
			return org.drip.product.creator.BondBasketBuilder.CreateBondBasket (strName, aBond, adblWeights,
				dtEffective, dblNotional);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Makes an on-the-run CDX product for the given index, the date, and the tenor
	 * 
	 * @param strIndex Index
	 * @param dt Current Date
	 * @param strTenor Tenor
	 * 
	 * @return BasketProduct representing the on-the-run CDX
	 */

	public static final org.drip.product.definition.BasketProduct MakeCDX (
		final java.lang.String strIndex,
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strTenor)
	{
		return org.drip.service.env.StandardCDXManager.GetOnTheRun (strIndex, dt, strTenor);
	}

	/**
	 * Makes an on-the-run CDX product for the given index, the series, and the tenor
	 * 
	 * @param strIndex Index
	 * @param iSeries Series
	 * @param strTenor Tenor
	 * 
	 * @return BasketProduct representing the on-the-run CDX
	 */

	public static final org.drip.product.definition.BasketProduct MakeCDX (
		final java.lang.String strIndex,
		final int iSeries,
		final java.lang.String strTenor)
	{
		return org.drip.service.env.StandardCDXManager.MakeStandardCDX (strIndex, iSeries, strTenor);
	}
}
