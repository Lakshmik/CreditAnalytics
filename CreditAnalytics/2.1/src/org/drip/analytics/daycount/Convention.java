
package org.drip.analytics.daycount;

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
 * This class contains flags that indicate where the holidays are loaded from, as well as the holiday types
 * 		and load rules
 *
 * @author Lakshmi Krishnamurthy
 */

public class Convention {
	/**
	 * Date Roll Actual
	 */

	public static final int DR_ACTUAL = 0;

	/**
	 * Date Roll Following
	 */

	public static final int DR_FOLL = 1;

	/**
	 * Date Roll Modified Following
	 */

	public static final int DR_MOD_FOLL = 2;

	/**
	 * Date Roll Previous
	 */

	public static final int DR_PREV = 4;

	/**
	 * Date Roll Modified Previous
	 */

	public static final int DR_MOD_PREV = 8;

	/**
	 * Week Day Holiday
	 */

	public static final int WEEKDAY_HOLS = 1;

	/**
	 * Week End Holiday
	 */

	public static final int WEEKEND_HOLS = 2;

	private static final int INIT_FROM_HOLS_DB = 1;
	private static final int INIT_FROM_HOLS_XML = 2;
	private static final int INIT_FROM_HOLS_SOURCE = 4;

	private static int s_iInitHols = INIT_FROM_HOLS_SOURCE;
	private static java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> s_mapLocHols = null;

	private static java.util.Map<java.lang.String, org.drip.analytics.daycount.DCFCalculator> s_mapDCCalc =
		new java.util.HashMap<java.lang.String, org.drip.analytics.daycount.DCFCalculator>();

	private static final boolean UpdateDCCalcMap (
		final org.drip.analytics.daycount.DCFCalculator dcfCalc)
	{
		for (java.lang.String strDC : dcfCalc.getAlternateNames())
			s_mapDCCalc.put (strDC, dcfCalc);

		return true;
	}

	private static final boolean SetDCCalc()
	{
		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DC30_360())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DC30_365())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DC30_Act())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DC30E_360())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_360())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_364())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_365())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_365L())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_Act_ISDA())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCAct_Act())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCNL_360())) return false;

		if (!UpdateDCCalcMap (new org.drip.analytics.daycount.DCNL_365())) return false;

		return UpdateDCCalcMap (new org.drip.analytics.daycount.DCNL_Act());
	}

	private static final boolean AddLH (
		final org.drip.analytics.holset.LocationHoliday lh,
		final java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> mapHols)
	{
		if (null == lh || null == mapHols) return false;

		java.lang.String strLocation = lh.getHolidayLoc();

		org.drip.analytics.holiday.Locale locHols = lh.getHolidaySet();

		if (null == locHols || null == strLocation || strLocation.isEmpty()) return false;

		mapHols.put (strLocation, locHols);

		return true;
	}

	private static final java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale>
		SetHolsFromSource()
	{
		java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> mapHols = new
			java.util.HashMap<java.lang.String, org.drip.analytics.holiday.Locale>();

		AddLH (new org.drip.analytics.holset.AEDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ANGHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ARAHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ARFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ARNHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ARPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ARSHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ATSHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.AUDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.AZMHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BAKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BBDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BEFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BGLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BHDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BMDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BRCHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BRLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.BSDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CADHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CAEHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CERHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CFFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CHFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CLFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CLUHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CNYHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.COFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CONHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.COPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CRCHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CYPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.CZKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.DEMHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.DKKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.DOPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.DTFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ECSHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.EEKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.EGPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ESBHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ESPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ESTHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.EUBHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.EURHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.GBPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.GELHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.GFRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.GRDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.HKDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.HRKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.HUFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.IBRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.IDRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.IEPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.IGPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ILSHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.INRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.IPCHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ITLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.JMDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.JPYHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.KPWHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.KRWHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.KWDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.KYDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.KZTHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.LKRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.LTLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.LUFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.LUXHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.LVLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MDLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MIXHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MKDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MXCHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MXNHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MXPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MXVHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.MYRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.NLGHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.NOKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.NZDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PABHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PEFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PENHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PESHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PHPHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PLNHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PLZHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.PTEHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.QEFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.RUBHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.RURHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SARHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SEKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SGDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SITHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SKKHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.SVCHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.TABHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.TGTHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.THBHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.TRLHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.TRYHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.TWDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.UAHHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.USDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.USVHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.UVRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.UYUHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.UYUHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.VACHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.VEBHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.VEFHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.VNDHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.XDRHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.XEUHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ZALHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ZARHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ZUSHoliday(), mapHols);

		AddLH (new org.drip.analytics.holset.ZWDHoliday(), mapHols);

		return mapHols;
	}

	private static final boolean isLocSpecificHoliday (
		final java.lang.String strCalendarSet,
		final double dblDate,
		final int iHolType)
	{
		if (null == strCalendarSet || strCalendarSet.isEmpty() || !org.drip.math.common.NumberUtil.IsValid
			(dblDate))
			return false;

		java.lang.String[] astrCalendars = strCalendarSet.split (",");

		for (java.lang.String strCalendar : astrCalendars) {
			if (null != strCalendar && null != s_mapLocHols.get (strCalendar)) {
				org.drip.analytics.holiday.Locale lh = s_mapLocHols.get (strCalendar);

				if (null == lh) continue;

				if (0 != (WEEKEND_HOLS & iHolType) && null != lh.getWeekendDays() &&
					lh.getWeekendDays().isWeekend (dblDate))
					return true;

				if (null == lh.getHolidays() || 0 == (WEEKDAY_HOLS & iHolType)) continue;

				for (org.drip.analytics.holiday.Base hol : lh.getHolidays()) {
					try {
						if (null != hol && (int) dblDate == (int) hol.getDateInYear
							(org.drip.analytics.date.JulianDate.Year (dblDate), true))
							return true;
					} catch (java.lang.Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return false;
	}

	/**
	 * Initialize the day count basis object from the calendar set
	 * 
	 * @param strCalendarSetLoc The calendar set
	 * 
	 * @return Success (true) Failure (false)
	 */

	public static final boolean Init (
		final java.lang.String strCalendarSetLoc)
	{
		if (!SetDCCalc()) return false;

		if (INIT_FROM_HOLS_SOURCE == s_iInitHols) {
			if (null == (s_mapLocHols = SetHolsFromSource())) return false;

			return true;
		}

		try {
			if (INIT_FROM_HOLS_XML == s_iInitHols)
				s_mapLocHols = org.drip.param.config.ConfigLoader.LoadHolidayCalendars
					(strCalendarSetLoc);
			else if (INIT_FROM_HOLS_DB == s_iInitHols)
				s_mapLocHols = org.drip.param.config.ConfigLoader.LoadHolidayCalendarsFromDB
					(strCalendarSetLoc);

			return true;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (null == s_mapLocHols && null == (s_mapLocHols = SetHolsFromSource()))  return false;

		return false;
	}

	/**
	 * Retrieve the set of holiday locations
	 * 
	 * @return Set of holiday locations
	 */

	public static final java.util.Set<java.lang.String> GetHolLocations()
	{
		return s_mapLocHols.keySet();
	}

	/**
	 * Gets the week end days for the given holiday calendar set
	 * 
	 * @param strCalendarSet Holiday calendar set
	 * 
	 * @return Array of days indicating the week day union
	 */

	public static final int[] GetWeekendDays (
		final java.lang.String strCalendarSet)
	{
		if (null == strCalendarSet || strCalendarSet.isEmpty()) return null;

		java.lang.String[] astrCalendars = strCalendarSet.split (",");

		java.util.Set<java.lang.Integer> si = new java.util.HashSet<java.lang.Integer>();

		for (java.lang.String strCalendar : astrCalendars) {
			if (null != strCalendar && null != s_mapLocHols.get (strCalendar)) {
				org.drip.analytics.holiday.Locale lh = s_mapLocHols.get (strCalendar);

				if (null == lh || null == lh.getWeekendDays() || null == lh.getWeekendDays().getDays())
					continue;

				for (int i : lh.getWeekendDays().getDays())
					si.add (i);
			}
		}

		int j = 0;

		int[] aiWkend = new int[si.size()];

		for (int iHol : si)
			aiWkend[j++] = iHol;

		return aiWkend;
	}

	/**
	 * Gets all the available DRIP day count conventions
	 * 
	 * @return Available DRIP day count conventions
	 */

	public static final java.lang.String GetAvailableDC()
	{
		java.lang.StringBuffer sbDCSet = new java.lang.StringBuffer();

		sbDCSet.append ("30/365;ISMA 30/365;ISDA SWAPS:30/365;ISDA30/365;ISDA 30E/365;");

		sbDCSet.append ("30/360;30U/360;30/360;Bond basis;30/360 US;US MUNI: 30/360;MUNI30/360;ISDA30/360");

		sbDCSet.append ("30E/360;30/360 ICMA;30S/360;Eurobond basis;Eurobond basis (ISDA 2006);Special German;");

		sbDCSet.append ("ISMA 30/360;30E/360 ISDA;Eurobond basis (ISDA 2000);German;German:30/360;Ger:30/360");

		sbDCSet.append ("ISDA SWAPS:30/360;ISDA 30E/360");

		sbDCSet.append ("Actual/Actual;Actual/Actual ICMA;Act/Act ICMA;ISMA-99;Act/Act ISMA");

		sbDCSet.append ("ISMA 30/Act;30/Act;ISMA 30/Act;ISDA SWAPS:30/Act;ISDA30/Act;ISDA 30E/ACT");

		sbDCSet.append ("Actual/Actual ISDA;Actual/Actual;Act/Act;Actual/365;US:WIT Act/Act");

		sbDCSet.append ("Actual/365 Fixed;Act/365 Fixed;A/365 Fixed;A/365F;Act/365F;English;Act/365");

		sbDCSet.append ("Actual/365 JGB;Actual/365 JGB (NL);NL/365");

		sbDCSet.append ("Actual/360 Fixed;Act/360;A/360;French;US:WIB Act/360");

		sbDCSet.append ("Actual/365L;ISMA-Year;Actual/Actual AFB");

		sbDCSet.append ("NL/360");

		sbDCSet.append ("NL/Act");

		sbDCSet.append ("Act/364");

		sbDCSet.append ("BUS252;BUS DAYS252;BUS/252");

		return sbDCSet.toString();
	}
	/**
	 * Calculates the accrual fraction in years between 2 given days for the given day count convention and
	 * 		the other parameters
	 * 
	 * @param dblStart Start Date
	 * @param dblEnd End Date
	 * @param strDayCount Day count convention
	 * @param bApplyEOMAdj Apply end-of-month adjustment (true)
	 * @param dblMaturity Maturity Date
	 * @param actactParams ActActParams
	 * @param strCalendar Holiday Calendar
	 * 
	 * @return Accrual Fraction in years
	 * 
	 * @throws java.lang.Exception Thrown if the accrual fraction cannot be calculated
	 */

	public static final double YearFraction (
		final double dblStart,
		final double dblEnd,
		final java.lang.String strDayCount,
		final boolean bApplyEOMAdj,
		final double dblMaturity,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if ("BUS252".equalsIgnoreCase (strDayCount) || "BUS DAYS252".equalsIgnoreCase (strDayCount) ||
			"BUS/252".equalsIgnoreCase (strDayCount))
			return BusDays (dblStart, dblEnd, strCalendar) / 252.;

		org.drip.analytics.daycount.DCFCalculator dfcCalc = s_mapDCCalc.get (strDayCount);

		if (null != dfcCalc)
			return dfcCalc.yearFraction (dblStart, dblEnd, bApplyEOMAdj, dblMaturity, actactParams,
				strCalendar);

		System.out.println ("Unknown DC: " + strDayCount + "; defaulting to Actual/365.25");

		return (dblEnd - dblStart) / 365.25;
	}

	/**
	 * Rolls the given date in accordance with the roll mode and the calendar set
	 * 
	 * @param dblDate Date to be rolled
	 * @param iRollMode Roll Mode (one of DR_ACT, DR_FOLL, DR_MOD_FOLL, DR_PREV, or DR_MOD_PREV)
	 * @param strCalendarSet Calendar Set to calculate the holidays by
	 * 
	 * @return The rolled date
	 * 
	 * @throws java.lang.Exception Thrown if the date cannot be rolled
	 */

	public static final double RollDate (
		final double dblDate,
		final int iRollMode,
		final java.lang.String strCalendarSet)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Cannot roll a NaN date");

		if (null == strCalendarSet || strCalendarSet.isEmpty() || DR_ACTUAL == iRollMode) return dblDate;

		double dblRolledDate = dblDate;

		if (DR_FOLL == iRollMode) {
			while (IsHoliday (dblRolledDate, strCalendarSet))
				++dblRolledDate;
		} else if (DR_MOD_FOLL == iRollMode) {
			while (IsHoliday (dblRolledDate, strCalendarSet))
				++dblRolledDate;

			if (org.drip.analytics.date.JulianDate.Month (dblDate) !=
				org.drip.analytics.date.JulianDate.Month (dblRolledDate)) {
				while (IsHoliday (dblRolledDate, strCalendarSet))
					--dblRolledDate;
			}
		}

		if (DR_PREV == iRollMode) {
			while (IsHoliday (dblRolledDate, strCalendarSet))
				--dblRolledDate;
		} else if (DR_MOD_PREV == iRollMode) {
			while (IsHoliday (dblRolledDate, strCalendarSet))
				--dblRolledDate;

			if (org.drip.analytics.date.JulianDate.Month (dblDate) !=
				org.drip.analytics.date.JulianDate.Month (dblRolledDate)) {
				while (IsHoliday (dblRolledDate, strCalendarSet))
					++dblRolledDate;
			}
		}

		return dblRolledDate;
	}

	/**
	 * Indicates whether the given date is a holiday in the specified location(s)
	 * 
	 * @param dblDate Date
	 * @param strCalendar Location Calendar set
	 * @param iHolType WEEKDAY_HOLS or WEEKEND_HOLS
	 * 
	 * @return True (it is a holiday) or false
	 * 
	 * @throws java.lang.Exception Thrown if it cannot be evaluated
	 */

	public static final boolean IsHoliday (
		final double dblDate,
		final java.lang.String strCalendar,
		final int iHolType)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Cannot a NaN date for holiday!");

		return isLocSpecificHoliday ((null == strCalendar || strCalendar.isEmpty() || "".equals
			(strCalendar)) ? "USD" : strCalendar, dblDate, iHolType);
	}

	/**
	 * Indicates whether the given date is a holiday in the specified location(s)
	 * 
	 * @param dblDate Date
	 * @param strCalendar Location Calendar set
	 * 
	 * @return True (it is a holiday) or false
	 * 
	 * @throws java.lang.Exception Thrown if it cannot be evaluated
	 */

	public static final boolean IsHoliday (
		final double dblDate,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		return IsHoliday (dblDate, strCalendar, WEEKDAY_HOLS | WEEKEND_HOLS);
	}

	/**
	 * Calculates the number of business days between the start and the end dates
	 * 
	 * @param dblStart Start Date
	 * @param dblFinish End Date
	 * @param strCalendar Holiday Calendar set
	 * 
	 * @return The number of business days
	 * 
	 * @throws java.lang.Exception Thrown if it cannot be evaluated
	 */

	public static final int BusDays (
		final double dblStart,
		final double dblFinish,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblFinish))
			throw new java.lang.Exception ("Cannot a NaN date for a bus day!");

		int iNumBusDays = 0;
		double dblEnd = dblFinish;
		double dblBegin = dblStart;

		if (dblBegin > dblEnd) {
			dblEnd = dblStart;
			dblBegin = dblFinish;
		}

		while (dblBegin != dblEnd) {
			if (!IsHoliday (dblBegin++, strCalendar)) ++iNumBusDays;
		}

		return dblBegin > dblEnd ? -1 * iNumBusDays : iNumBusDays;
	}

	/**
	 * Calculates the set of holidays between the start and the end dates
	 * 
	 * @param dblStart Start Date
	 * @param dblFinish End Date
	 * @param strCalendar Holiday Calendar set
	 * 
	 * @return The set of holidays
	 * 
	 * @throws java.lang.Exception Thrown if it cannot be evaluated
	 */

	public static final java.util.List<java.lang.Double> HolidaySet (
		final double dblStart,
		final double dblFinish,
		final java.lang.String strCalendar)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblFinish))
			return null;

		java.util.List<java.lang.Double> lsHolidays = new java.util.ArrayList<java.lang.Double>();

		double dblEnd = dblFinish;
		double dblBegin = dblStart;

		if (dblBegin > dblEnd) {
			dblEnd = dblStart;
			dblBegin = dblFinish;
		}

		while (dblBegin != dblEnd) {
			try {
				if (IsHoliday (dblBegin++, strCalendar)) lsHolidays.add (dblBegin - 1);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return lsHolidays;
	}

	/**
	 * Calculates the number of holidays between the start and the end dates
	 * 
	 * @param dblStart Start Date
	 * @param dblFinish End Date
	 * @param strCalendar Holiday Calendar set
	 * 
	 * @return The number of holidays
	 * 
	 * @throws java.lang.Exception Thrown if it cannot be evaluated
	 */

	public static final int Holidays (
		final double dblStart,
		final double dblFinish,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblFinish))
			throw new java.lang.Exception ("Cannot have a NaN date for a holiday!");

		int iNumHolidays = 0;
		double dblEnd = dblFinish;
		double dblBegin = dblStart;

		if (dblBegin > dblEnd) {
			dblEnd = dblStart;
			dblBegin = dblFinish;
		}

		while (dblBegin != dblEnd) {
			if (IsHoliday (dblBegin++, strCalendar)) ++iNumHolidays;
		}

		return dblBegin > dblEnd ? -1 * iNumHolidays : iNumHolidays;
	}

	/**
	 * Adjusts the given date in accordance with the adjustment mode and the calendar set
	 * 
	 * @param dblDate Date to be rolled
	 * @param strCalendar Calendar Set to calculate the holidays by
	 * @param iAdjustMode Adjustment Mode (one of DR_ACT, DR_FOLL, DR_MOD_FOLL, DR_PREV, or DR_MOD_PREV
	 * 
	 * @return The adjusted date
	 * 
	 * @throws java.lang.Exception Thrown if the date cannot be adjusted
	 */

	public static final double Adjust (
		final double dblDate,
		final java.lang.String strCalendar,
		final int iAdjustMode)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Cannot adjust a NaN date!");

		double dblDateAdjusted = dblDate;

		while (IsHoliday (dblDateAdjusted, strCalendar)) ++dblDateAdjusted;

		return dblDateAdjusted;
	}

	/**
	 * Adds the specified number of business days and adjusts it according to the calendar set
	 * 
	 * @param dblDate Date to be rolled
	 * @param iNumDays Number of days to add
	 * @param strCalendar Calendar Set to calculate the holidays by
	 * 
	 * @return The adjusted date
	 * 
	 * @throws java.lang.Exception Propogated if exception encountered
	 */

	public static final double AddBusinessDays (
		final double dblDate,
        final int iNumDays,
        final java.lang.String strCalendar)
        throws java.lang.Exception
    {
        return Adjust (dblDate + iNumDays, strCalendar, DR_FOLL);
    }

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Convention.Init ("c:\\DRIP\\CreditProduct\\Config.xml");

		double dblDate = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 5, 5).getJulian();

		org.drip.analytics.holiday.Locale lh = s_mapLocHols.get ("HKD");

		System.out.println (lh.getWeekendDays());

		for (org.drip.analytics.holiday.Base hol : lh.getHolidays()) {
			double dblHoliday = hol.getDateInYear (org.drip.analytics.date.JulianDate.Year (dblDate), true);

			System.out.println (dblHoliday + "=" + org.drip.analytics.date.JulianDate.fromJulian
				(dblHoliday));
		}
	}
}
