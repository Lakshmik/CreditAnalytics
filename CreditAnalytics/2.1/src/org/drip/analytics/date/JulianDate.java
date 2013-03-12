
package org.drip.analytics.date;

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
 * Class provides a comprehensive representation of Julian date and date manipulation functionality
 * 
 * @author Lakshmi Krishnamurthy
 */

public class JulianDate implements java.lang.Comparable<JulianDate> {
	private static final boolean s_bLog = false;

	private static double HALFSECOND = 0.5;
  	private static int JGREG = 15 + 31 * (10 + 12 * 1582);

	/**
	 * LEFT_INCLUDE includes the start date in the Feb29 check
	 */

	public static final int LEFT_INCLUDE = 1;

	/**
	 * RIGHT_INCLUDE includes the end date in the Feb29 check
	 */

	public static final int RIGHT_INCLUDE = 2;

	/**
	 * Days of the week - Monday
	 */

	public static final int MONDAY = 0;

	/**
	 * Days of the week - Tuesday
	 */

	public static final int TUESDAY = 1;

	/**
	 * Days of the week - Wednesday
	 */

	public static final int WEDNESDAY = 2;

	/**
	 * Days of the week - Thursday
	 */

	public static final int THURSDAY = 3;

	/**
	 * Days of the week - Friday
	 */

	public static final int FRIDAY = 4;

	/**
	 * Days of the week - Saturday
	 */

	public static final int SATURDAY = 5;

	/**
	 * Days of the week - Sunday
	 */

	public static final int SUNDAY = 6;

	/**
	 * Integer Month - January
	 */

	public static final int JANUARY = 1;

	/**
	 * Integer Month - February
	 */

	public static final int FEBRUARY = 2;

	/**
	 * Integer Month - March
	 */

	public static final int MARCH = 3;

	/**
	 * Integer Month - April
	 */

	public static final int APRIL = 4;

	/**
	 * Integer Month - May
	 */

	public static final int MAY = 5;

	/**
	 * Integer Month - June
	 */

	public static final int JUNE = 6;

	/**
	 * Integer Month - July
	 */

	public static final int JULY = 7;

	/**
	 * Integer Month - August
	 */

	public static final int AUGUST = 8;

	/**
	 * Integer Month - September
	 */

	public static final int SEPTEMBER = 9;

	/**
	 * Integer Month - October
	 */

	public static final int OCTOBER = 10;

	/**
	 * Integer Month - November
	 */

	public static final int NOVEMBER = 11;

	/**
	 * Integer Month - December
	 */

	public static final int DECEMBER = 12;

	private double _dblJulian = java.lang.Double.NaN;

	/**
	 * Converts YMD to a Julian double.
	 * 
	 * @param iYear Year
	 * @param iMonth Month
	 * @param iDay Day
	 * 
	 * @return double representing the Julian date
	 */

	public static double toJulian (
		final int iYear,
		final int iMonth,
		final int iDay) 
	{
		int iJulianYear = iYear;
		int iJulianMonth = iMonth;

		if (iYear < 0) ++iJulianYear;

		if (iMonth > 2)
			++iJulianMonth;
		else {
			--iJulianYear;
			iJulianMonth += 13;
		}

		double dblJulian = (java.lang.Math.floor (365.25 * iJulianYear) + java.lang.Math.floor (30.6001 *
			iJulianMonth) + iDay + 1720995.0);

		if (iDay + 31 * (iMonth + 12 * iYear) >= JGREG) {
     			int iJA = (int)(0.01 * iJulianYear);
     			dblJulian += 2 - iJA + (0.25 * iJA);
   		}

   		return java.lang.Math.floor (dblJulian);
	}

	/**
	 * Creates a MM/DD/YYYY string from the input Julian double
	 * 
	 * @param dblJulianIn double representing Julian date
	 * 
	 * @return MM/DD/YYYY date string
	 */

	public static java.lang.String fromJulian (
		final double dblJulianIn)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblJulianIn)) return null;

		int iJA = (int) (dblJulianIn + HALFSECOND / 86400.0);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		int iDay = iJB - iJD - (int) (30.6001 * iJE);
   		int iMonth = iJE - 1;
		int iYear = iJC - 4715;

		if (iMonth > 12) iMonth -= 12;

		if (iMonth > 2) --iYear;

		if (iYear <= 0) --iYear;

		return org.drip.analytics.support.GenericUtil.PrePad (iMonth) + "/" +
			org.drip.analytics.support.GenericUtil.PrePad (iDay) + "/" + iYear;
	}

	/**
	 * Returns the year corresponding to the Julian double
	 * 
	 * @param dblJulianIn double representing the Julian date
	 * 
	 * @return integer representing the month
	 * 
	 * @throws java.lang.Exception thrown if the input date in invalid
	 */

	public static int Year (
		final double dblJulianIn)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblJulianIn))
			throw new java.lang.Exception ("JulianDate.Year got NaN input!");

		int iJA = (int) (dblJulianIn + HALFSECOND / 86400.0);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		int iMonth = iJE - 1;
		int iYear = iJC - 4715;

		if (iMonth > 12) iMonth -= 12;

		if (iMonth > 2) --iYear;

		if (iYear <= 0) --iYear;

		return iYear;
	}

	/**
	 * Return the month given the date represented by the Julian double.
	 * 
	 * @param dblJulianIn double representing the Julian date
	 * 
	 * @return integer representing the month
	 * 
	 * @throws java.lang.Exception thrown if input date is invalid
	 */

	public static int Month (
		final double dblJulianIn)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblJulianIn))
			throw new java.lang.Exception ("JulianDate.Month got NaN input!");

		int iJA = (int) (dblJulianIn + HALFSECOND / 86400.0);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iMonth = (int) ((iJB - iJD) / 30.6001) - 1;

		if (iMonth > 12) iMonth -= 12;

		return iMonth;
	}

	/**
	 * Returns the day corresponding to the Julain double
	 *  
	 * @param dblJulianIn double representing the Julian date
	 * 
	 * @return integer representing the year
	 * 
	 * @throws java.lang.Exception thrown if input date is invalid
	 */

	public static int Day (
		final double dblJulianIn)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblJulianIn))
			throw new java.lang.Exception ("JulianDate.Month got NaN input!");

		int iJA = (int) (dblJulianIn + HALFSECOND / 86400.0);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		return iJB - iJD - (int) (30.6001 * iJE);
	}

	/**
	 * Numbers of days elapsed in the year represented by the given Julian date
	 * 
	 * @param dblDate Double representing the Julian date
	 * 
	 * @return Double representing the number of days in the current year
	 * 
	 * @throws java.lang.Exception Thrown if the input date is invalid
	 */

	public static final int DaysElapsed (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("JulianDate.DaysElapsed got NaN input!");

		return (int) (dblDate - toJulian (Year (dblDate), JANUARY, 1));
	}

	/**
	 * Returns the number of days remaining in the year represented by the given Julian year
	 * 
	 * @param dblDate Double representing the Julian date
	 * 
	 * @return Double representing the number of days remaining
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final int DaysRemaining (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("JulianDate.DaysRemaining got NaN input!");

		return (int) (toJulian (Year (dblDate), DECEMBER, 31) - dblDate);
	}

	/**
	 * Indicates if the year in the given Julian date is a leap year
	 * 
	 * @param dblDate Double representing the input Julian date
	 * 
	 * @return True indicates leap year
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final boolean IsLeapYear (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("JulianDate.IsLeapYear got NaN input!");

		return 0 == (Year (dblDate) % 4);
	}

	/**
	 * Indicates whether there is at least one leap day between 2 given Julian dates
	 *  
	 * @param dblStart Double representing the starting Julian date
	 * @param dblEnd Double representing the ending Julian date
	 * @param iIncludeSide INCLUDE_LEFT or INCLUDE_RIGHT indicating whether the starting date, the ending
	 * 	date, or both dates are to be included
	 *  
	 * @return True indicates there is at least one Feb29 between the dates
	 * 
	 * @throws java.lang.Exception If inputs are invalid
	 */

	public static final boolean ContainsFeb29 (
		final double dblStart,
		final double dblEnd,
		final int iIncludeSide)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblEnd))
			throw new java.lang.Exception ("JulianDate.ContainsFeb29 got NaN input!");

		if (dblStart >= dblEnd) return false;

		double dblLeft = dblStart;
		double dblRight = dblEnd;

		if (0 == (iIncludeSide & LEFT_INCLUDE)) ++dblLeft;

		if (0 == (iIncludeSide & RIGHT_INCLUDE)) --dblRight;

		for (double dblDate = dblLeft; dblDate <= dblRight; ++dblDate) {
			if (FEBRUARY == Month (dblDate) && 29 == Day (dblDate)) return true;
		}

		return false;
	}

	/**
	 * Calculates how many leap days exist between the 2 given Julian days
	 * 
	 * @param dblStart Double representing the starting Julian date
	 * @param dblEnd Double representing the ending Julian date
	 * @param iIncludeSide INCLUDE_LEFT or INCLUDE_RIGHT indicating whether the starting date, the ending
	 * 	date, or both dates are to be included
	 * 
	 * @return Integer representing the number of leap days
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public static final int NumFeb29 (
		final double dblStart,
		final double dblEnd,
		final int iIncludeSide)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblEnd))
			throw new java.lang.Exception ("JulianDate.NumFeb29 got NaN input!");

		int iNumFeb29 = 0;
		boolean bLoop = true;
		double dblDate = dblStart;

		while (bLoop) {
			double dblEndDate = dblDate + 365;

			if (dblEndDate > dblEnd) {
				bLoop = false;
				dblEndDate = dblEnd;
			}

			if (ContainsFeb29 (dblDate, dblEndDate, iIncludeSide)) ++iNumFeb29;

			dblDate = dblEndDate;
		}

		return iNumFeb29;
	}

	/**
	 * Returns the english word corresponding to the input integer month
	 *  
	 * @param iMonth Integer representing the month
	 * 
	 * @return String of the English word
	 * 
	 * @throws java.lang.Exception Thrown if the input month is invalid
	 */

	public static java.lang.String getMonthChar (
		final int iMonth)
		throws java.lang.Exception
	{
		if (JANUARY == iMonth) return "January";

		if (FEBRUARY == iMonth) return "February";

		if (MARCH == iMonth) return "March";

		if (APRIL == iMonth) return "April";

		if (MAY == iMonth) return "May";

		if (JUNE == iMonth) return "June";

		if (JULY == iMonth) return "July";

		if (AUGUST == iMonth) return "August";

		if (SEPTEMBER == iMonth) return "September";

		if (OCTOBER == iMonth) return "October";

		if (NOVEMBER == iMonth) return "November";

		if (DECEMBER == iMonth) return "December";

		throw new java.lang.Exception ("Invalid month number " + iMonth);
	}

	/**
	 * Returns the Oracle DB trigram corresponding to the input integer month
	 * 
	 * @param iMonth Integer representing the month
	 * 
	 * @return String representing the Oracle DB trigram
	 * 
	 * @throws java.lang.Exception thrown if the input month is invalid
	 */

	public static java.lang.String getMonthOracleChar (
		final int iMonth) throws java.lang.Exception
	{
		if (JANUARY == iMonth) return "JAN";

		if (FEBRUARY == iMonth) return "FEB";

		if (MARCH == iMonth) return "MAR";

		if (APRIL == iMonth) return "APR";

		if (MAY == iMonth) return "MAY";

		if (JUNE == iMonth) return "JUN";

		if (JULY == iMonth) return "JUL";

		if (AUGUST == iMonth) return "AUG";

		if (SEPTEMBER == iMonth) return "SEP";

		if (OCTOBER == iMonth) return "OCT";

		if (NOVEMBER == iMonth) return "NOV";

		if (DECEMBER == iMonth) return "DEC";

		throw new java.lang.Exception ("Invalid month number " + iMonth);
	}

	/**
	 * Converts the month trigram/word to the corresponding month integer
	 * 
	 * @param strMonth Month trigram or english word
	 * 
	 * @return Integer representing the month
	 * 
	 * @throws java.lang.Exception Thrown on invalid input month
	 */

	public static int MonthFromMonthChars (
		final java.lang.String strMonth)
		throws java.lang.Exception
	{
		if (null == strMonth) throw new java.lang.Exception ("Null month!");

		if (strMonth.equalsIgnoreCase ("JAN") || strMonth.equalsIgnoreCase ("JANUARY")) return JANUARY;

		if (strMonth.equalsIgnoreCase ("FEB") || strMonth.equalsIgnoreCase ("FEBRUARY")) return FEBRUARY;

		if (strMonth.equalsIgnoreCase ("MAR") || strMonth.equalsIgnoreCase ("MARCH")) return MARCH;

		if (strMonth.equalsIgnoreCase ("APR") || strMonth.equalsIgnoreCase ("APRIL")) return APRIL;

		if (strMonth.equalsIgnoreCase ("MAY")) return MAY;

		if (strMonth.equalsIgnoreCase ("JUN") || strMonth.equalsIgnoreCase ("JUNE")) return JUNE;

		if (strMonth.equalsIgnoreCase ("JUL") || strMonth.equalsIgnoreCase ("JULY")) return JULY;

		if (strMonth.equalsIgnoreCase ("AUG") || strMonth.equalsIgnoreCase ("AUGUST")) return AUGUST;

		if (strMonth.equalsIgnoreCase ("SEP") || strMonth.equalsIgnoreCase ("SEPTEMBER") ||
			strMonth.equalsIgnoreCase ("SEPT"))
			return SEPTEMBER;

		if (strMonth.equalsIgnoreCase ("OCT") || strMonth.equalsIgnoreCase ("OCTOBER")) return OCTOBER;

		if (strMonth.equalsIgnoreCase ("NOV") || strMonth.equalsIgnoreCase ("NOVEMBER")) return NOVEMBER;

		if (strMonth.equalsIgnoreCase ("DEC") || strMonth.equalsIgnoreCase ("DECEMBER")) return DECEMBER;

		throw new java.lang.Exception ("Invalid month " + strMonth);
	}

	/**
	 * Gets the english word for day corresponding to the input integer
	 * 
	 * @param iDay Integer representing the day
	 * 
	 * @return String representing the English word for the day
	 * 
	 * @throws java.lang.Exception Thrown if the input day is invalid
	 */

	public static java.lang.String getDayChars (
		final int iDay)
		throws java.lang.Exception
	{
		if (MONDAY == iDay) return "Monday";

		if (TUESDAY == iDay) return "Tuesday";

		if (WEDNESDAY == iDay) return "Wednesday";

		if (THURSDAY == iDay) return "Thursday";

		if (FRIDAY == iDay) return "Friday";

		if (SATURDAY == iDay) return "Saturday";

		if (SUNDAY == iDay) return "Sunday";

		throw new java.lang.Exception ("Invalid WeekDay number " + iDay);
	}

	/**
	 * Gets the maximum number of days in the given month and year
	 * 
	 * @param iMonth Integer representing the month
	 * @param iYear Integer representing the year
	 * 
	 * @return Integer representing the maximum days
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static int DaysInMonth (
		final int iMonth,
		final int iYear)
		throws java.lang.Exception
	{
		if (JANUARY == iMonth) return 31;

		if (FEBRUARY == iMonth) {
			if (0 == (iYear % 4)) return 29;

			return 28;
		}

		if (MARCH == iMonth) return 31;

		if (APRIL == iMonth) return 30;

		if (MAY == iMonth) return 31;

		if (JUNE == iMonth) return 30;

		if (JULY == iMonth) return 31;

		if (AUGUST == iMonth) return 31;

		if (SEPTEMBER == iMonth) return 30;

		if (OCTOBER == iMonth) return 31;

		if (NOVEMBER == iMonth) return 30;

		if (DECEMBER == iMonth) return 31;

		throw new java.lang.Exception ("Invalid Month: " + iMonth);
	}

	/**
	 * Indicates if the given Julian double corresponds to an end of month day
	 * 
	 * @param dblDate Double representing the Julain date
	 * 
	 * @return True indicates EOM is true
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final boolean IsEOM (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("JulianDate.IsEOM got NaN input!");

		return Day (dblDate) == DaysInMonth (Month (dblDate), Year (dblDate)) ? true : false;
	}

	/**
	 * Returns a Julian Date corresponding to today
	 *  
	 * @return JulianDate corresponding to today
	 */

	public static final JulianDate Today()
	{
		java.util.Date dtNow = new java.util.Date();

		try {
			return CreateFromYMD (org.drip.analytics.support.GenericUtil.GetYear (dtNow),
				org.drip.analytics.support.GenericUtil.GetMonth (dtNow),
					org.drip.analytics.support.GenericUtil.GetDate (dtNow));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a JulianDate from year, month, and date
	 *  
	 * @param iYear Integer year
	 * @param iMonth Integer month
	 * @param iDay Integer day
	 * 
	 * @return Julian Date corresponding to the specified year, month, and day
	 */

	public static final JulianDate CreateFromYMD (
		final int iYear,
		final int iMonth,
		final int iDay)
	{
		try {
			return new JulianDate (toJulian (iYear, iMonth, iDay));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a JulianDate from a string containing date in the DDMMYYYY format
	 * 
	 * @param strDate String containing date in the DDMMYYYY format
	 * 
	 * @return JulianDate
	 */

	public static final JulianDate CreateFromDDMMMYYYY (
		final java.lang.String strDate)
	{
		if (null == strDate || strDate.isEmpty()) return null;

		java.lang.String[] astrParts = strDate.split ("-");

		if (3 != astrParts.length) return null;

		try {
			int iDay = new java.lang.Integer (astrParts[0]);

			int iYear = new java.lang.Integer (astrParts[2]);

			return CreateFromYMD (iYear, MonthFromMonthChars (astrParts[1]), iDay);
		} catch (java.lang.Exception e) {
			if (s_bLog) e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create JulianDate from a double Julian
	 * 
	 * @param dblJulian Double representing the JulianDate
	 * 
	 * @throws java.lang.Exception Thrown if the input date is invalid
	 */

	public JulianDate (
		final double dblJulian)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblJulian))
			throw new java.lang.Exception ("JulianDate ctr got NaN input!");

		_dblJulian = dblJulian;
	}

	/**
	 * Returns the double Julian
	 * 
	 * @return The double Julian
	 */

	public double getJulian()
	{
		return _dblJulian;
	}

	/**
	 * Add the given number of days and returns a new JulianDate
	 * 
	 * @param iDays Integer representing the number of days to be added
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate addDays (
		final int iDays)
	{
		try {
			return new JulianDate (_dblJulian + iDays);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Subtracts the given number of days and returns a new JulianDate
	 * 
	 * @param iDays Integer representing the number of days to be subtracted
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate subtractDays (
		final int iDays)
	{
		try {
			return new JulianDate (_dblJulian - iDays);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Add the given number of business days and returns a new JulianDate
	 * 
	 * @param iDays Integer representing the number of days to be subtracted
	 * 
	 * @param strCalendarSet String representing the calendar set containing the business days
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate addBusDays (
		final int iDays,
		final java.lang.String strCalendarSet)
	{
		int iNumDaysToAdd = iDays;
		double dblAdjusted = _dblJulian;

		try {
			while (0 != iNumDaysToAdd--) {
				++dblAdjusted;

				while (org.drip.analytics.daycount.Convention.IsHoliday (dblAdjusted, strCalendarSet))
					++dblAdjusted;
			}

			return new JulianDate (dblAdjusted);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Subtract the given number of business days and returns a new JulianDate
	 * 
	 * @param iDays Integer representing the number of days to be subtracted
	 * 
	 * @param strCalendarSet String representing the calendar set containing the business days
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate subtractBusDays (
		final int iDays,
		final java.lang.String strCalendarSet)
	{
		int iNumDaysToAdd = iDays;
		double dblAdjusted = _dblJulian;

		try {
			while (0 != iNumDaysToAdd--) {
				--dblAdjusted;

				while (org.drip.analytics.daycount.Convention.IsHoliday (dblAdjusted, strCalendarSet))
					--dblAdjusted;
			}

			return new JulianDate (dblAdjusted);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Add the given number of years and returns a new JulianDate
	 * 
	 * @param iNumYears Integer representing the number of years to be added
	 *  
	 * @return The new JulianDate
	 */

	public JulianDate addYears (
		final int iNumYears)
	{
		int iJA = (int) (_dblJulian + HALFSECOND / 86400.);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		int iDay = iJB - iJD - (int) (30.6001 * iJE);
   		int iMonth = iJE - 1;
		int iYear = iJC - 4715;

		if (iMonth > 12) iMonth -= 12;

		if (iMonth > 2) --iYear;

		if (iYear <= 0) --iYear;

		try {
			return CreateFromYMD (iYear + iNumYears, iMonth, iDay);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Add the given number of months and returns a new JulianDate
	 * 
	 * @param iNumMonths Integer representing the number of months to be added
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate addMonths (
		final int iNumMonths)
	{
		int iJA = (int) (_dblJulian + HALFSECOND / 86400.);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680.0 + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		int iDay = iJB - iJD - (int) (30.6001 * iJE);
   		int iMonth = iJE - 1;
		int iYear = iJC - 4715;

		if (iMonth > 12) iMonth -= 12;

		if (iMonth > 2) --iYear;

		if (iYear <= 0) --iYear;

		if (12 < (iMonth += iNumMonths)) {
			while (12 < iMonth) {
				++iYear;
				iMonth -= 12;
			}
		} else if (0 >= iMonth) {
			--iYear;
			iMonth += 12;
		}

		try {
			while (iDay > DaysInMonth (iMonth, iYear))
				--iDay;

			return CreateFromYMD (iYear, iMonth, iDay);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generates the First EDSF start date from this JulianDate
	 * 
	 * @param iNumRollMonths Integer representing number of months to roll
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate getFirstEDFStartDate (
		final int iNumRollMonths)
	{
		int iJA = (int) (_dblJulian + HALFSECOND / 86400.);

		if (iJA >= JGREG) {
			int iJAlpha = (int) (((iJA - 1867216) - 0.25) / 36524.25);
			iJA = iJA + 1 + iJAlpha - iJAlpha / 4;
		}

		int iJB = iJA + 1524;
		int iJC = (int) (6680. + ((iJB - 2439870) - 122.1) / 365.25);
		int iJD = 365 * iJC + iJC / 4;
		int iJE = (int) ((iJB - iJD) / 30.6001);
   		int iDay = iJB - iJD - (int) (30.6001 * iJE);
   		int iMonth = iJE - 1;
		int iYear = iJC - 4715;

		if (iMonth > 12) iMonth -= 12;

		if (iMonth > 2) --iYear;

		if (iYear <= 0) --iYear;

		if (20 <= iDay) {
			if (12 < ++iMonth) {
				++iYear;
				iMonth -= 12;
			}
		}

		while (0 != iMonth % iNumRollMonths) ++iMonth;

		try {
			return CreateFromYMD (iYear, iMonth, 20);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Adds the tenor to the JulianDate to create a new date
	 * 
	 * @param strTenor String representing the tenor to add
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate addTenor (
		final java.lang.String strTenor)
	{
		if (null == strTenor || strTenor.isEmpty()) return null;

		char chTenor = strTenor.charAt (strTenor.length() - 1);

		int iTimeUnit = -1;

		try {
			iTimeUnit = (int) new java.lang.Double (strTenor.substring (0, strTenor.length() -
				1)).doubleValue();
		} catch (java.lang.Exception e) {
			System.out.println ("Bad time unit " + iTimeUnit + " in tenor " + strTenor);

			return null;
		}

		if ('d' == chTenor || 'D' == chTenor) return addDays (iTimeUnit);

		if ('w' == chTenor || 'W' == chTenor) return addDays (iTimeUnit * 7);

		if ('m' == chTenor || 'M' == chTenor) return addMonths (iTimeUnit);

		if ('y' == chTenor || 'Y' == chTenor) return addYears (iTimeUnit);

		System.out.println ("Unknown tenor format " + strTenor);

		return null;
	}

	/**
	 * Subtracts the tenor to the JulianDate to create a new date
	 * 
	 * @param strTenor String representing the tenor to add
	 * 
	 * @return The new JulianDate
	 */

	public JulianDate subtractTenor (
		final java.lang.String strTenor)
	{
		if (null == strTenor || strTenor.isEmpty()) return null;

		char chTenor = strTenor.charAt (strTenor.length() - 1);

		int iTimeUnit = -1;

		try {
			iTimeUnit = (int) new java.lang.Double (strTenor.substring (0, strTenor.length() -
				1)).doubleValue();
		} catch (java.lang.Exception e) {
			System.out.println ("Bad time unit " + iTimeUnit + " in tenor " + strTenor);

			return null;
		}

		if ('d' == chTenor || 'D' == chTenor) return addDays (-iTimeUnit);

		if ('w' == chTenor || 'W' == chTenor) return addDays (-iTimeUnit * 7);

		if ('m' == chTenor || 'M' == chTenor) return addMonths (-iTimeUnit);

		if ('y' == chTenor || 'Y' == chTenor) return addYears (-iTimeUnit);

		System.out.println ("Unknown tenor format " + strTenor);

		return null;
	}

	/**
	 * Difference in days between the current and the input date
	 * 
	 * @param dt JulianDate representing the input date
	 * 
	 * @return Integer representing the difference in days
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public int daysDiff (
		final JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("JulianDate.daysDiff got NaN date input!");

		return (int) (_dblJulian - dt.getJulian());
	}

	/**
	 * Returns a trigram representation of date
	 * 
	 * @return String representing the trigram representation of date
	 */

	public java.lang.String toOracleDate()
	{
		try {
			return Day (_dblJulian) + "-" + getMonthOracleChar (Month (_dblJulian)) + "-" + Year
				(_dblJulian);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public boolean equals (
		final java.lang.Object o)
	{
		if (!(o instanceof JulianDate)) return false;

		return (int) _dblJulian == (int) ((JulianDate) o)._dblJulian;
	}

	@Override public int hashCode()
	{
		long lBits = java.lang.Double.doubleToLongBits ((int) _dblJulian);

		return (int) (lBits ^ (lBits >>> 32));
	}

	@Override public java.lang.String toString()
	{
		return fromJulian (_dblJulian);
	}

	@Override public int compareTo (
		final JulianDate dtOther)
	{
		if ((int) _dblJulian > (int) (dtOther._dblJulian)) return 1;

		if ((int) _dblJulian < (int) (dtOther._dblJulian)) return -1;

		return 0;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
	{
		System.out.println (JulianDate.Today());
	}
}
