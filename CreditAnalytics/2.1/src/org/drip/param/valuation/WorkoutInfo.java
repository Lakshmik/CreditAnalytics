
package org.drip.param.valuation;

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
 * This class is a place-holder for the work-out parameters. It contains the date, the factor, the type, and
 * 		the yield of the work-out.
 *
 * @author Lakshmi Krishnamurthy
 */

public class WorkoutInfo extends org.drip.service.stream.Serializer {
	/**
	 * Work out type Call
	 */

	public static final int WO_TYPE_CALL = 1;

	/**
	 * Work out type Put
	 */

	public static final int WO_TYPE_PUT = 2;

	/**
	 * Work out type Maturity
	 */

	public static final int WO_TYPE_MATURITY = 3;

	/**
	 * Work-out Date
	 */

	public double _dblDate = java.lang.Double.NaN;

	/**
	 * Work-out Yield
	 */

	public double _dblYield = java.lang.Double.NaN;

	/**
	 * Work-out Factor
	 */

	public double _dblExerciseFactor = java.lang.Double.NaN;

	/**
	 * Work out Type
	 */

	public int _iWOType = WO_TYPE_MATURITY;

	/**
	 * Constructor: Constructs the class from the work-out date, yield, exercise factor, and type
	 * 
	 * @param dblDate Work-out Date
	 * @param dblYield Work-out Yield
	 * @param dblExerciseFactor Work-out Factor
	 * @param iWOType Work out Type
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public WorkoutInfo (
		final double dblDate,
		final double dblYield,
		final double dblExerciseFactor,
		final int iWOType)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblDate = dblDate) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblYield = dblYield) ||
				!org.drip.math.common.NumberUtil.IsValid (_dblExerciseFactor = dblExerciseFactor))
			throw new java.lang.Exception ("One of wkout dat/yld/ex factor came out NaN!");

		_iWOType= iWOType;
	}

	/**
	 * WorkoutInfo de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if WorkoutInfo cannot be properly de-serialized
	 */

	public WorkoutInfo (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Empty state");

		java.lang.String strSerializedWorkoutInfo = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedWorkoutInfo || strSerializedWorkoutInfo.isEmpty())
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedWorkoutInfo, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Cannot locate workout date");

		_dblDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Cannot locate workout yield");

		_dblYield = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Cannot locate exercise factor");

		_dblExerciseFactor = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("WorkoutInfo de-serializer: Cannot locate work out type");

		_iWOType = new java.lang.Integer (astrField[4]);
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (VERSION + getFieldDelimiter() + _dblDate + getFieldDelimiter() + _dblYield +
			getFieldDelimiter() + _dblExerciseFactor + getFieldDelimiter() + _iWOType);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new WorkoutInfo (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		WorkoutInfo wi = new WorkoutInfo (org.drip.analytics.date.JulianDate.Today().getJulian(), 0.06, 1.,
			WO_TYPE_MATURITY);

		byte[] abWI = wi.serialize();

		System.out.println (new java.lang.String (abWI));

		WorkoutInfo wiDeser = new WorkoutInfo (abWI);

		System.out.println (new java.lang.String (wiDeser.serialize()));
	}
}
