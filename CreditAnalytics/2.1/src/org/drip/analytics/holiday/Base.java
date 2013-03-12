
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
 * This class is an abstraction around holiday and description. Abstract function generates an optional
 * 		adjustment for weekends in a given year.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class Base extends org.drip.service.stream.Serializer {
	private java.lang.String _strDescription = "";

	/**
	 * Constructs the holiday instance from the description
	 * 
	 * @param strDescription Holiday Description
	 */

	public Base (
		final java.lang.String strDescription)
	{
		_strDescription = strDescription;
	}

	/**
	 * De-serialization of Holiday from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize Holiday
	 */

	public Base (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Holiday de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("Holiday de-serializer: Empty state");

		java.lang.String strHoliday = strRawString.substring (0, strRawString.indexOf
			(super.getObjectTrailer()));

		if (null == strHoliday || strHoliday.isEmpty())
			throw new java.lang.Exception ("Holiday de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strHoliday,
			super.getFieldDelimiter());

		if (null == astrField || 2 > astrField.length)
			throw new java.lang.Exception ("Holiday de-serialize: Invalid number of fields");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("Holiday de-serializer: Cannot locate description");

		_strDescription = astrField[1];
	}

	/**
	 * Rolls the date to a non-holiday according to the rule specified
	 * 
	 * @param dblDate Date to be rolled
	 * @param bBalkOnYearShift Throw an exception if the year change happens
	 * @param wkend Object representing the weekend days
	 * 
	 * @return The adjusted date
	 * 
	 * @throws java.lang.Exception Thrown if the holiday cannot be rolled
	 */

	public static final double RollHoliday (
		final double dblDate,
		final boolean bBalkOnYearShift,
		final Weekend wkend)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Cannot Roll NaN date!");

		double dblRolledDate = dblDate;

		if (null != wkend && wkend.isLeftWeekend (dblDate)) dblRolledDate = dblDate - 1;

		if (null != wkend && wkend.isRightWeekend (dblDate)) dblRolledDate = dblDate + 1;

		if (bBalkOnYearShift & org.drip.analytics.date.JulianDate.Year (dblDate) !=
			org.drip.analytics.date.JulianDate.Year (dblRolledDate))
			return -1.;

		return dblRolledDate;
	}

	/**
	 * Returns the description
	 * 
	 * @return Description
	 */

	public java.lang.String getDescription()
	{
		return _strDescription;
	}

	/**
	 * Generates the full date specific to the input year
	 * 
	 * @param iYear Input Year
	 * @param bAdjusted Whether adjustment is desired
	 * 
	 * @return The full date
	 */

	public abstract double getDateInYear (
		final int iYear,
		final boolean bAdjusted);

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + super.getFieldDelimiter() + _strDescription);

		return sb.append (super.getObjectTrailer()).toString().getBytes();
	}
}
