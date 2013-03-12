
package org.drip.product.params;

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
 * Contains the component's floating rate parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FloaterSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Rate Index
	 */

	public java.lang.String _strRateIndex = "";

	/**
	 * Floating Day Count
	 */

	public java.lang.String _strFloatDayCount = "";

	/**
	 * Floating Spread
	 */

	public double _dblFloatSpread = java.lang.Double.NaN;

	/**
	 * Current Coupon
	 */

	public double _dblCurrentCoupon = java.lang.Double.NaN;

	/**
	 * Constructs the FloaterSetting from rate index, floating day count, float spread, and current coupon
	 * 
	 * @param strRateIndex Rate Index
	 * @param strFloatDayCount Floating Day Count
	 * @param dblFloatSpread Floating Spread
	 * @param dblCurrentCoupon Current Coupon
	 */

	public FloaterSetting (
		final java.lang.String strRateIndex,
		final java.lang.String strFloatDayCount,
		final double dblFloatSpread,
		final double dblCurrentCoupon)
	{
		_strRateIndex = strRateIndex;
		_dblFloatSpread = dblFloatSpread;
		_strFloatDayCount = strFloatDayCount;
		_dblCurrentCoupon = dblCurrentCoupon;
	}

	/**
	 * FloaterSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FloaterSetting cannot be properly de-serialized
	 */

	public FloaterSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FloaterSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FloaterSetting de-serializer: Empty state");

		java.lang.String strSerializedFloaterSetting = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFloaterSetting || strSerializedFloaterSetting.isEmpty())
			throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedFloaterSetting, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("FloaterSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot locate rate index");

		_strRateIndex = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot locate float spread");

		_dblFloatSpread = new java.lang.Double (astrField[2]).doubleValue();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot locate current coupon");

		_dblCurrentCoupon = new java.lang.Double (astrField[3]).doubleValue();

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot locate float day count");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_strFloatDayCount = "";
		else
			_strFloatDayCount = astrField[4];

		if (!validate()) throw new java.lang.Exception ("FloaterSetting de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblFloatSpread) &&
			!org.drip.math.common.NumberUtil.IsValid (_dblCurrentCoupon))
			return false;

		if (!org.drip.math.common.NumberUtil.IsValid (_dblCurrentCoupon) && (null == _strRateIndex ||
			_strRateIndex.isEmpty()))
			return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strRateIndex +
			getFieldDelimiter() + _dblFloatSpread + getFieldDelimiter() + _dblCurrentCoupon +
				getFieldDelimiter());

		if (null == _strFloatDayCount || _strFloatDayCount.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (_strFloatDayCount);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new FloaterSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		FloaterSetting bfp = new FloaterSetting ("ABCD", "HAHA", 1., 0.);

		byte[] abBFP = bfp.serialize();

		System.out.println (new java.lang.String (abBFP));

		FloaterSetting bfpDeser = new FloaterSetting (abBFP);

		System.out.println (new java.lang.String (bfpDeser.serialize()));
	}
}
