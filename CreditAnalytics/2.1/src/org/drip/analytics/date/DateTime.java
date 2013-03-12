
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
 * This class provides the representation of the instantiation-time date and time objects
 * 
 * @author Lakshmi Krishnamurthy
 */

public class DateTime extends org.drip.service.stream.Serializer {
	private long _lTime = 0L;
	private double _dblDate = java.lang.Double.NaN;

	/**
	 * Default constructor initializes the time and date to the current time and current date.
	 */

	public DateTime()
		throws java.lang.Exception
	{
		_lTime = System.nanoTime();

		java.util.Date dtNow = new java.util.Date();

		_dblDate = JulianDate.toJulian (org.drip.analytics.support.GenericUtil.GetYear (dtNow),
			org.drip.analytics.support.GenericUtil.GetMonth (dtNow),
				org.drip.analytics.support.GenericUtil.GetDate (dtNow));
	}

	/**
	 * Constructs DateTime from separate date and time inputs 
	 * 
	 * @param dblDate date
	 * @param lTime time
	 * 
	 * @throws java.lang.Exception thrown on invalid inputs
	 */

	public DateTime (
		final double dblDate,
		final long lTime)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Invalid DateTime ctr params!");

		_lTime = lTime;
		_dblDate = dblDate;
	}

	/**
	 * DateTime de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if DateTime cannot be properly de-serialized
	 */

	public DateTime (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("DateTime de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("DateTime de-serializer: Empty state");

		java.lang.String strSerializedDateTime = strRawString.substring (0, strRawString.indexOf
			(super.getObjectTrailer()));

		if (null == strSerializedDateTime || strSerializedDateTime.isEmpty())
			throw new java.lang.Exception ("DateTime de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedDateTime,
			super.getFieldDelimiter());

		if (null == astrField || 3 > astrField.length)
			throw new java.lang.Exception ("DateTime de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			throw new java.lang.Exception ("DateTime de-serializer: Cannot locate long time");

		_lTime = new java.lang.Long (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
			throw new java.lang.Exception ("DateTime de-serializer: Cannot locate date");

		_dblDate = new java.lang.Double (astrField[2]);
	}

	/**
	 * Retrieves the Date
	 * 
	 * @return date
	 */

	public double getDate()
	{
		return _dblDate;
	}

	/**
	 * Retrieves the time
	 * 
	 * @return time
	 */

	public long getTime()
	{
		return _lTime;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + super.getFieldDelimiter() + _lTime +
			super.getFieldDelimiter() + _dblDate);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new DateTime (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		DateTime dt = new DateTime();

		byte[] abDT = dt.serialize();

		System.out.println (new java.lang.String (abDT));

		DateTime dtDeser = new DateTime (abDT);

		System.out.println (new java.lang.String (dtDeser.serialize()));
	}
}
