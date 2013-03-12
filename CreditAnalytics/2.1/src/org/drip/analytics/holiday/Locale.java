
package org.drip.analytics.holiday;


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
 * This class contains the set of regular holidays and the weekend holidays for a location.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Locale {
	private Weekend _wkend = null;

	private java.util.Set<Base> _setHolidays = new java.util.HashSet<Base>();

	/**
	 * Constructs an empty LocHolidays instance
	 */

	public Locale()
	{
	}

	/**
	 * Adds the array of weekend days
	 * 
	 * @param aiDays Array of weekend days
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addWeekend (
		final int[] aiDays)
	{
		if (null == aiDays || 0 == aiDays.length) return false;

		if (null == (_wkend = new Weekend (aiDays))) return false;

		return true;
	}

	/**
	 * Adds the regular SATURDAY/SUNDAY weekend
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addStandardWeekend()
	{
		_wkend = Weekend.StandardWeekend();

		return true;
	}

	/**
	 * Adds the given date as a static holiday
	 * 
	 * @param dt Date
	 * @param strDescription Description
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addStaticHoliday (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strDescription)
	{
		if (null == dt) return false;

		try {
			_setHolidays.add (new Static (dt, strDescription));

			return true;
		} catch (java.lang.Exception e) {
			System.out.println (e.getMessage());
		}

		return false;
	}

	/**
	 * Adds the given string date as a static holiday
	 * 
	 * @param strDate Date string
	 * @param strDescription Description
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addStaticHoliday (
		final java.lang.String strDate,
		final java.lang.String strDescription)
	{
		if (null == strDate || strDate.isEmpty()) return false;

		org.drip.analytics.date.JulianDate dtStaticHoliday =
			org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY (strDate);

		if (null == dtStaticHoliday) return false;

		return addStaticHoliday (dtStaticHoliday, strDescription);
	}

	/**
	 * Adds a fixed holiday from the day and month
	 * 
	 * @param iDay Day
	 * @param iMonth Month
	 * @param strDescription Description
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addFixedHoliday (
		final int iDay,
		final int iMonth,
		final java.lang.String strDescription)
	{
		_setHolidays.add (new Fixed (iDay, iMonth, _wkend, strDescription));

		return true;
	}

	/**
	 * Adds a floating holiday from the week in month, the day in week, the month, and whether holidays are
	 * 		calculated from front/back.
	 * 
	 * @param iWeekInMonth Week in the Month
	 * @param iWeekDay Day in the week
	 * @param iMonth Month
	 * @param bFromFront Holidays are calculated from the start of the month (True)
	 * @param strDescription Description
	 * 
	 * @return Succeeded (true), failure (false)
	 */

	public boolean addFloatingHoliday (
		final int iWeekInMonth,
		final int iWeekDay,
		final int iMonth,
		final boolean bFromFront,
		final java.lang.String strDescription)
	{
		_setHolidays.add (new Variable (iWeekInMonth, iWeekDay, iMonth, bFromFront, _wkend,
			strDescription));

		return true;
	}

	/**
	 * Returns the weekend
	 * 
	 * @return Weekend
	 */

	public Weekend getWeekendDays()
	{
		return _wkend;
	}

	/**
	 * Returns the set of week day holidays
	 * 
	 * @return Set of hoidays
	 */

	public java.util.Set<Base> getHolidays()
	{
		return _setHolidays;
	}
}
