
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
 * This class contains the component's trade, the coupon, and the redemption currencies.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CurrencySet extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Trade Currency
	 */

	public java.lang.String _strTradeCurrency = "";

	/**
	 * Coupon Currency
	 */

	public java.lang.String _strCouponCurrency = "";

	/**
	 * Redemption Currency
	 */

	public java.lang.String _strRedemptionCurrency = "";

	/**
	 * Constructs the CurrencySet object from the trade, the coupon, and the redemption currencies.
	 * 
	 * @param strTradeCurrency Trade Currency
	 * @param strCouponCurrency Coupon Currency
	 * @param strRedemptionCurrency Redemption Currency
	 */

	public CurrencySet (
		final java.lang.String strTradeCurrency,
		final java.lang.String strCouponCurrency,
		final java.lang.String strRedemptionCurrency)
	{
		_strTradeCurrency = strTradeCurrency;
		_strCouponCurrency = strCouponCurrency;
		_strRedemptionCurrency = strRedemptionCurrency;
	}

	/**
	 * CurrencySet de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CurrencySet cannot be properly de-serialized
	 */

	public CurrencySet (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CurrencySet de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CurrencySet de-serializer: Empty state");

		java.lang.String strSerializedCurrencySet = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCurrencySet || strSerializedCurrencySet.isEmpty())
			throw new java.lang.Exception ("CurrencySet de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCurrencySet, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("CurrencySet de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("CurrencySet de-serializer: Cannot locate Trade Currency");

		_strTradeCurrency = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("CurrencySet de-serializer: Cannot locate Coupon Currency");

		_strCouponCurrency = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("CurrencySet de-serializer: Cannot locate Redemption Currency");

		_strRedemptionCurrency = astrField[3];

		if (!validate()) throw new java.lang.Exception ("CurrencySet de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if (null == _strTradeCurrency || _strTradeCurrency.isEmpty() || null == _strCouponCurrency ||
			_strCouponCurrency.isEmpty() || null == _strRedemptionCurrency ||
				_strRedemptionCurrency.isEmpty())
			return false;

		if (!_strRedemptionCurrency.equalsIgnoreCase (_strCouponCurrency) ||
			!_strRedemptionCurrency.equalsIgnoreCase (_strTradeCurrency))
			return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strTradeCurrency +
			getFieldDelimiter() + _strCouponCurrency + getFieldDelimiter() + _strRedemptionCurrency);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CurrencySet (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CurrencySet bcp = new CurrencySet ("ABC", "DEF", "GHI");

		byte[] abBCP = bcp.serialize();

		System.out.println (new java.lang.String (abBCP));

		CurrencySet bcpDeser = new CurrencySet (abBCP);

		System.out.println (new java.lang.String (bcpDeser.serialize()));
	}
}
