
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
 * This class contains the floating holiday’s month, day in week, and week in month. Will be generated with
 * 		an optional adjustment for weekends in a given year.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Variable extends Base {
	private int _iMonth = 0;
	private int _iWeekDay = 0;
	private int _iWeekInMonth = 0;
	private boolean _bFromFront = true;
	private Weekend _wkend = null;

	/**
	 * Constructs the object from the week, day, month, from front/back, week end, and description
	 * 
	 * @param iWeekInMonth Week of the Month
	 * @param iWeekDay Day of the Week
	 * @param iMonth Month
	 * @param bFromFront From Front (true), Back (false)
	 * @param wkend Weekend
	 * @param strDescription Description
	 */

	public Variable (
		final int iWeekInMonth,
		final int iWeekDay,
		final int iMonth,
		final boolean bFromFront,
		final Weekend wkend,
		final java.lang.String strDescription)
	{
		super (strDescription);

		_iMonth = iMonth;
		_iWeekDay = iWeekDay;
		_wkend = wkend;
		_bFromFront = bFromFront;
		_iWeekInMonth = iWeekInMonth;
	}

	/**
	 * De-serialization of FloatingHoliday from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize FloatingHoliday
	 */

	public Variable (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FloatingHoliday de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Empty state");

		java.lang.String strFH = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strFH || strFH.isEmpty())
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strFH,
			getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("FloatingHoliday de-serialize: Invalid number of fields");

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate month");

		_iMonth = new java.lang.Integer (astrField[1]).intValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate week day");

		_iWeekDay = new java.lang.Integer (astrField[2]).intValue();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate week in month");

		_iWeekInMonth = new java.lang.Integer (astrField[3]).intValue();

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate from front flag");

		_bFromFront = new java.lang.Boolean (astrField[4]).booleanValue();

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception ("FloatingHoliday de-serializer: Cannot locate wkend");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[5]))
			_wkend = null;
		else
			_wkend = new Weekend (astrField[5].getBytes());
	}

	@Override public double getDateInYear (
		final int iYear,
		final boolean bAdjustForWeekend)
	{
		double dblDate = java.lang.Double.NaN;

		try {
			if (_bFromFront)
				dblDate = (org.drip.analytics.date.JulianDate.CreateFromYMD (iYear, _iMonth,
					_iWeekDay)).getJulian() + (7 * (_iWeekInMonth - 1));
			else {
				dblDate = (org.drip.analytics.date.JulianDate.CreateFromYMD (iYear, _iMonth,
					org.drip.analytics.date.JulianDate.DaysInMonth (_iMonth, iYear))).getJulian() - (7 *
						(_iWeekInMonth - 1));

				while (_iWeekDay != (dblDate % 7)) --dblDate;
			}

			if (bAdjustForWeekend) return Base.RollHoliday (dblDate, true, _wkend);
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
			sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _iMonth +
				getFieldDelimiter() + _iWeekDay + getFieldDelimiter() + _iWeekInMonth + getFieldDelimiter() +
					_bFromFront + getFieldDelimiter() + _wkend.serialize());
		else
			sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _iMonth +
				getFieldDelimiter() + _iWeekDay + getFieldDelimiter() + _iWeekInMonth + getFieldDelimiter() +
					_bFromFront + getFieldDelimiter() + org.drip.service.stream.Serializer.NULL_SER_STRING);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new Variable (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Variable fh = new Variable (1, 3, 4, false, null, "3 Jan Holiday");

		byte[] abFH = fh.serialize();

		System.out.println ("Input: " + new java.lang.String (abFH));

		Variable fhDeser = new Variable (abFH);

		System.out.println ("Output: " + new java.lang.String (fhDeser.serialize()));
	}
}
