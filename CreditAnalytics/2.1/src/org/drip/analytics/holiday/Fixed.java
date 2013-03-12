
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
 * This class contains the fixed holiday’s date and month. Will be generated with an optional adjustment for
 * 		weekends in a given year.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Fixed extends Base {
	private int _iDay = 0;
	private int _iMonth = 0;
	private Weekend _wkend = null;

	/**
	 * Constructs the object from the day, month, weekend, and description
	 * 
	 * @param iDay Day
	 * @param iMonth Month
	 * @param wkend Weekend Object
	 * @param strDescription Description
	 */

	public Fixed (
		final int iDay,
		final int iMonth,
		final Weekend wkend,
		final java.lang.String strDescription)
	{
		super (strDescription);

		_iDay = iDay;
		_wkend = wkend;
		_iMonth = iMonth;
	}

	/**
	 * De-serialization of FixedHoliday from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize FixedHoliday
	 */

	public Fixed (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FixedHoliday de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FixedHoliday de-serializer: Empty state");

		java.lang.String strFH = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strFH || strFH.isEmpty())
			throw new java.lang.Exception ("FixedHoliday de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strFH,
			getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("FixedHoliday de-serialize: Invalid number of fields");

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FixedHoliday de-serializer: Cannot locate day");

		_iDay = new java.lang.Integer (astrField[1]).intValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FixedHoliday de-serializer: Cannot locate month");

		_iMonth = new java.lang.Integer (astrField[2]).intValue();

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("FixedHoliday de-serializer: Cannot locate wkend");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[3]))
			_wkend = null;
		else
			_wkend = new Weekend (astrField[3].getBytes());
	}

	@Override public double getDateInYear (final int iYear, final boolean bAdjust) {
		double dblDate = java.lang.Double.NaN;

		try {
			dblDate = org.drip.analytics.date.JulianDate.CreateFromYMD (iYear, _iMonth, _iDay).getJulian();

			if (bAdjust) return Base.RollHoliday (dblDate, true, _wkend);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return dblDate;
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

		if (null != _wkend)
			sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _iDay +
				getFieldDelimiter() + _iMonth + getFieldDelimiter() + _wkend.serialize());
		else
			sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _iDay +
				getFieldDelimiter() + _iMonth + getFieldDelimiter() +
					org.drip.service.stream.Serializer.NULL_SER_STRING);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new Fixed (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Fixed fh = new Fixed (1, 3, null, "MLK Holiday");

		byte[] abFH = fh.serialize();

		System.out.println ("Input: " + new java.lang.String (abFH));

		Fixed fhDeser = new Fixed (abFH);

		System.out.println ("Output: " + new java.lang.String (fhDeser.serialize()));
	}
}
