
package org.drip.service.sample;

/* 
 * Generic imports
 */

import java.util.*;

/* 
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;

/* 
 * Credit Analytics API imports
 */

import org.drip.service.api.CreditAnalytics;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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
 * Day-count and Calendar API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class DayCountAndCalendarAPI {
	/**
	 * Sample demonstrating the calendar API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void CalenderAPISample()
		throws Exception
	{
		/*
		 * Get all the holiday locations in CreditAnalytics
		 */

		Set<String> setLoc = CreditAnalytics.GetHolLocations();

		System.out.println ("Num Hol Locations: " + setLoc.size());

		for (String strLoc : setLoc)
			System.out.println (strLoc);

		/*
		 * Get all the holidays in the year according the calendar set
		 */

		JulianDate[] adtHols = CreditAnalytics.GetHolsInYear ("USD,GBP", 2011);

		System.out.println ("USD,GBP has " + adtHols.length + " hols");

		for (int i = 0; i < adtHols.length; ++i)
			System.out.println (adtHols[i]);

		/*
		 * Get all the week day holidays in the year according the calendar set
		 */

		JulianDate[] adtWeekDayHols = CreditAnalytics.GetWeekDayHolsInYear ("USD,GBP", 2011);

		System.out.println ("USD,GBP has " + adtWeekDayHols.length + " week day hols");

		for (int i = 0; i < adtWeekDayHols.length; ++i)
			System.out.println (adtWeekDayHols[i]);

		/*
		 * Get all the weekend holidays in the year according the calendar set
		 */

		JulianDate[] adtWeekendHols = CreditAnalytics.GetWeekendHolsInYear ("USD,GBP", 2011);

		System.out.println ("USD,GBP has " + adtWeekendHols.length + " weekend hols");

		for (int i = 0; i < adtWeekendHols.length; ++i)
			System.out.println (adtWeekendHols[i]);

		/*
		 * Indicate which days correspond to the weekend for the given calendar set
		 */

		int[] aiWkendDays = CreditAnalytics.GetWeekendDays ("USD,GBP");

		for (int i = 0; i < aiWkendDays.length; ++i)
			System.out.println (JulianDate.getDayChars (aiWkendDays[i]));

		System.out.println ("USD,GBP has " + aiWkendDays.length + " weekend days");

		/*
		 * Check if the given day is a holiday
		 */

		boolean bIsHoliday = CreditAnalytics.IsHoliday ("USD,GBP", JulianDate.CreateFromYMD (2011, 12, 28));

		System.out.println (JulianDate.CreateFromYMD (2011, 12, 28) + " is a USD,GBP holiday? " + bIsHoliday);

		JulianDate dtToday = JulianDate.Today();

		/*
		 * List all the holidays between the specified days according to the calendar set
		 */

		List<Double> lsHols = Convention.HolidaySet
			(dtToday.getJulian(), dtToday.addYears (1).getJulian(), "USD,GBP");

		for (double dblDate : lsHols)
			System.out.println (new JulianDate (dblDate).toOracleDate());
	}

	/**
	 * Sample API demonstrating the day count functionality
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void DayCountAPISample()
		throws Exception
	{
		/*
		 * List available day count
		 */

		/* String strDCList = CreditAnalytics.GetAvailableDC();

		System.out.println (strDCList); */

		/*
		 * Calculate year fraction between 2 dates according to semi-annual, Act/360, and USD calendar
		 */

		double dblYF = CreditAnalytics.YearFraction (JulianDate.CreateFromYMD (2013, 5, 31),
			JulianDate.CreateFromYMD (2013, 6, 13), "30/360", false, "USD");

		/*
		 * Adjust the date FORWARD according to the USD calendar
		 */

		// JulianDate dtAdjusted = CreditAnalytics.Adjust (JulianDate.CreateFromYMD (2011, 1, 16), "USD", 0);

		/*
		 * Roll to the PREVIOUS date according to the USD calendar
		 */

		/* JulianDate dtRoll = CreditAnalytics.RollDate
			(JulianDate.CreateFromYMD (2011, 1, 16), "USD", Convention.DR_PREV);

		System.out.println ("YearFract: " + dblYF + "; Adjusted: " + dtAdjusted + "; Rolled: " + dtRoll); */

		System.out.println ("YearFract: " + dblYF);
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		// CalenderAPISample();

		DayCountAPISample();
	}
}
