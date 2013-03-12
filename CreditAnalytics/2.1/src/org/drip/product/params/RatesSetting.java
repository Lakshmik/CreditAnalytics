
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
 * Component Rates Valuation Parameters contains the interest rates related valuation parameters - the
 * 		discount curves to be used for discounting the coupon, the redemption, the principal, and the settle
 * 			cash flows.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RatesSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Trade Currency Discount Curve Name
	 */

	public java.lang.String _strTradeDiscountCurve = "";

	/**
	 * Coupon Cash flow Discount Curve Name
	 */

	public java.lang.String _strCouponDiscountCurve = "";

	/**
	 * Principal Cash flow Discount Curve Name
	 */

	public java.lang.String _strPrincipalDiscountCurve = "";

	/**
	 * Redemption Cash flow Discount Curve Name
	 */

	public java.lang.String _strRedemptionDiscountCurve = "";

	/**
	 * RatesSetting constructor
	 * 
	 * @param strTradeDiscountCurve Trade Cash flow Discount Curve
	 * @param strCouponDiscountCurve Coupon Cash flow Discount Curve
	 * @param strPrincipalDiscountCurve Principal Cash flow Discount Curve
	 * @param strRedemptionDiscountCurve Redemption Cash flow Discount Curve
	 */

	public RatesSetting (
		final java.lang.String strTradeDiscountCurve,
		final java.lang.String strCouponDiscountCurve,
		final java.lang.String strPrincipalDiscountCurve,
		final java.lang.String strRedemptionDiscountCurve)
	{
		_strTradeDiscountCurve = strTradeDiscountCurve;
		_strCouponDiscountCurve = strCouponDiscountCurve;
		_strPrincipalDiscountCurve = strPrincipalDiscountCurve;
		_strRedemptionDiscountCurve = strRedemptionDiscountCurve;
	}

	/**
	 * RatesSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if RatesSetting cannot be properly de-serialized
	 */

	public RatesSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("RatesSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("RatesSetting de-serializer: Empty state");

		java.lang.String strSerializedRatesSetting = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedRatesSetting ||
			strSerializedRatesSetting.isEmpty())
			throw new java.lang.Exception
				("RatesSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedRatesSetting, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception
				("RatesSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("RatesSetting de-serializer: Cannot locate Trade Currency");

		_strTradeDiscountCurve = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("RatesSetting de-serializer: Cannot locate Coupon Currency");

		_strCouponDiscountCurve = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("RatesSetting de-serializer: Cannot locate Principal Currency");

		_strPrincipalDiscountCurve = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("RatesSetting de-serializer: Cannot locate Redemption Currency");

		_strRedemptionDiscountCurve = astrField[4];

		if (!validate()) throw new java.lang.Exception ("RatesSetting de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if (null == _strTradeDiscountCurve || _strTradeDiscountCurve.isEmpty() || null ==
			_strCouponDiscountCurve || _strCouponDiscountCurve.isEmpty() || null ==
				_strPrincipalDiscountCurve || _strPrincipalDiscountCurve.isEmpty() || null ==
					_strRedemptionDiscountCurve || _strRedemptionDiscountCurve.isEmpty())
			return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strTradeDiscountCurve
			+ getFieldDelimiter() + _strCouponDiscountCurve + getFieldDelimiter() +
				_strPrincipalDiscountCurve + getFieldDelimiter() + _strRedemptionDiscountCurve);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new RatesSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		RatesSetting crvp = new RatesSetting ("USD", "USD", "USD", "USD");

		byte[] abCRVP = crvp.serialize();

		System.out.println (new java.lang.String (abCRVP));

		RatesSetting crvpDeser = new RatesSetting (abCRVP);

		System.out.println (new java.lang.String (crvpDeser.serialize()));
	}
}
