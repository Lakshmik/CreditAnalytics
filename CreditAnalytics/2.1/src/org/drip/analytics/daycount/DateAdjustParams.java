
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
 * This class contains the parameters needed for adjusting dates – holiday calendar and adjustment type.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DateAdjustParams extends org.drip.service.stream.Serializer {
	/**
	 * The Roll mode
	 */

	public int _iRollMode = 0;

	/**
	 * Roll calendar
	 */

	public java.lang.String _strCalendar = "";

	/**
	 * Creates a DateAdjustParams class from the roll mode and the calendar
	 * 
	 * @param iRollMode Roll Mode
	 * @param strCalendar Calendar
	 */

	public DateAdjustParams (
		final int iRollMode,
		final java.lang.String strCalendar)
	{
		_iRollMode = iRollMode;
		_strCalendar = strCalendar;
	}

	/**
	 * De-serialization of DateAdjustParams from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize DateAdjustParams
	 */

	public DateAdjustParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("DateAdjustParams de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("DateAdjustParams de-serializer: Empty state");

		java.lang.String strDAP = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strDAP || strDAP.isEmpty())
			throw new java.lang.Exception ("DateAdjustParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strDAP,
			getFieldDelimiter());

		if (null == astrField || 3 > astrField.length)
			throw new java.lang.Exception ("DateAdjustParams de-serialize: Invalid number of fields");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("DateAdjustParams de-serializer: Cannot locate roll mode");

		_iRollMode = new java.lang.Integer (astrField[1]).intValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("DateAdjustParams de-serializer: Cannot locate calendar");

		_strCalendar = astrField[2];
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "!";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "@";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _iRollMode +
			getFieldDelimiter() + _strCalendar);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new DateAdjustParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Rolls the given date
	 * 
	 * @param dblDate Input date
	 * 
	 * @return New JulianDate double
	 * 
	 * @throws java.lang.Exception Thrown if the date cannot be rolled
	 */

	public double Roll (
		final double dblDate)
		throws java.lang.Exception
	{
		return Convention.RollDate (dblDate, _iRollMode, _strCalendar);
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		DateAdjustParams dap = new DateAdjustParams (0, "CZK");

		byte[] abDAP = dap.serialize();

		System.out.println ("Input: " + new java.lang.String (abDAP));

		DateAdjustParams dapDeser = new DateAdjustParams (abDAP);

		System.out.println ("Output: " + new java.lang.String (dapDeser.serialize()));
	}
}
