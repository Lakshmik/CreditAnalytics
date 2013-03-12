
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
 * Holds the left and the right weekend days
 *
 * @author Lakshmi Krishnamurthy
 */

public class Weekend extends org.drip.service.stream.Serializer {
	private int[] _aiDays = null;

	/**
	 * Creates a Weekend with SATURDAY and SUNDAY
	 * 
	 * @return Weekend object
	 */

	public static final Weekend StandardWeekend()
	{
		int[] aiWeekend = new int[] {org.drip.analytics.date.JulianDate.SUNDAY,
			org.drip.analytics.date.JulianDate.SATURDAY};

		return new Weekend (aiWeekend);
	}

	/**
	 * De-serialization of WeekendHoliday from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize WeekendHoliday
	 */

	public Weekend (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("WeekendHoliday de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("WeekendHoliday de-serializer: Empty state");

		java.lang.String strWH = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strWH || strWH.isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (strWH))
			throw new java.lang.Exception ("WeekendHoliday de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strWH,
			getFieldDelimiter());

		if (null == astrField || 2 > astrField.length)
			throw new java.lang.Exception ("WeekendHoliday de-serialize: Invalid number of fields");

		// double dblVersion = new java.lang.Double (astrField[0]);

		java.util.List<java.lang.Integer> lsi = new java.util.ArrayList<java.lang.Integer>();

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]) ||
			!org.drip.analytics.support.GenericUtil.IntegerListFromString (lsi, astrField[1],
				getCollectionRecordDelimiter()))
			throw new java.lang.Exception ("WeekendHoliday de-serializer: Cannot decode state");

		_aiDays = new int[lsi.size()];

		for (int i = 0; i < _aiDays.length; ++i)
			_aiDays[i] = lsi.get (i);
	}

	/**
	 * Creates the weekend instance object from the array of the weekend days
	 * 
	 * @param aiDays Array of the weekend days
	 */

	public Weekend (
		final int[] aiDays)
	{
		if (null == aiDays || 0 == aiDays.length) return;

		_aiDays = new int[aiDays.length];

		for (int i = 0; i < aiDays.length; ++i)
			_aiDays[i] = aiDays[i];
	}

	/**
	 * Retrieves the weekend days
	 * 
	 * @return Array of the weekend days
	 */

	public int[] getDays()
	{
		return _aiDays;
	}

	/**
	 * Is the given date a left weekend day
	 * 
	 * @param dblDate Date
	 * 
	 * @return True (Left weekend day)
	 */

	public boolean isLeftWeekend (
		final double dblDate)
	{
		if (null == _aiDays || 0 == _aiDays.length) return false;

		if (_aiDays[0] == (dblDate % 7)) return true;

		return false;
	}

	/**
	 * Is the given date a right weekend day
	 * 
	 * @param dblDate Date
	 * 
	 * @return True (Right weekend day)
	 */

	public boolean isRightWeekend (
		final double dblDate)
	{
		if (null == _aiDays || 1 >= _aiDays.length) return false;

		if (_aiDays[1] == (dblDate % 7)) return true;

		return false;
	}

	/**
	 * Is the given date a weekend day
	 * 
	 * @param dblDate Date
	 * 
	 * @return True (Weekend day)
	 */

	public boolean isWeekend (
		final double dblDate)
	{
		return isLeftWeekend (dblDate) || isRightWeekend (dblDate);
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		for (int i = 0; i < _aiDays.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_aiDays[i]);
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new Weekend (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Weekend wh = Weekend.StandardWeekend();

		byte[] abWH = wh.serialize();

		System.out.println ("Input: " + new java.lang.String (abWH));

		Weekend whDeser = new Weekend (abWH);

		System.out.println ("Output: " + new java.lang.String (whDeser.serialize()));
	}
}
