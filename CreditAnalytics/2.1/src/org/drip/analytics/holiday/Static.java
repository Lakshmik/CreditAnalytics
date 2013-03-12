
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
 * This class a full date as a specific holiday
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Static extends Base {
	private double _dblDate = java.lang.Double.NaN;

	/**
	 * Creates a static holiday from the date string and the description
	 * 
	 * @param strDate Date string
	 * @param strDescription Description
	 * 
	 * @return StaticHoliday instance
	 */

	public static final Static CreateFromDateDescription (
		final java.lang.String strDate,
		final java.lang.String strDescription)
	{
		org.drip.analytics.date.JulianDate dtHol = org.drip.analytics.date.JulianDate.CreateFromDDMMMYYYY
			(strDate);

		if (null == dtHol) return null;

		try {
			return new Static (dtHol, strDescription);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Constructs a static holiday from the date and the description
	 * 
	 * @param dt Date
	 * @param strDescription Description
	 */

	public Static (
		final org.drip.analytics.date.JulianDate dt,
		final java.lang.String strDescription)
		throws java.lang.Exception
	{
		super (strDescription);

		if (null == dt) throw new java.lang.Exception ("Null date into Static Holiday");

		_dblDate = dt.getJulian();
	}

	/**
	 * De-serialization of StaticHoliday from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize StaticHoliday
	 */

	public Static (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("StaticHoliday de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("StaticHoliday de-serializer: Empty state");

		java.lang.String strFH = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strFH || strFH.isEmpty())
			throw new java.lang.Exception ("StaticHoliday de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strFH,
			getFieldDelimiter());

		if (null == astrField || 2 > astrField.length)
			throw new java.lang.Exception ("StaticHoliday de-serialize: Invalid number of fields");

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("StaticHoliday de-serializer: Cannot locate static holiday date");

		_dblDate = new java.lang.Double (astrField[1]).doubleValue();
	}

	@Override public double getDateInYear (
		final int iYear,
		final boolean bAdjusted)
	{
		return _dblDate;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "^";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _dblDate);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new Static (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Static sh = Static.CreateFromDateDescription ("12-JUN-2020", "Are you kidding me?");

		byte[] abSH = sh.serialize();

		System.out.println ("Input: " + new java.lang.String (abSH));

		Static shDeser = new Static (abSH);

		System.out.println ("Output: " + new java.lang.String (shDeser.serialize()));
	}
}
