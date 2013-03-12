
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
 * CurrencyPair class contains the numerator currency, the denominator currency, the quote currency, and the
 * 	PIP Factor
 *
 * @author Lakshmi Krishnamurthy
 */

public class CurrencyPair extends org.drip.service.stream.Serializer {
	private java.lang.String _strNumCcy = "";
	private java.lang.String _strDenomCcy = "";
	private java.lang.String _strQuoteCcy = "";
	private double _dblPIPFactor = java.lang.Double.NaN;

	/**
	 * Constructs the currency pair from the numerator currency, the denominator currency, the quote
	 * 	currency, and the PIP Factor
	 * 
	 * @param strNumCcy Numerator currency
	 * @param strDenomCcy Denominator currency
	 * @param strQuoteCcy Quote Currency
	 * @param dblPIPFactor PIP Factor
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public CurrencyPair (
		final java.lang.String strNumCcy,
		final java.lang.String strDenomCcy,
		final java.lang.String strQuoteCcy,
		final double dblPIPFactor)
		throws java.lang.Exception
	{
		if (null == strNumCcy || strNumCcy.isEmpty() || null == strDenomCcy || strDenomCcy.isEmpty() || null
			== strQuoteCcy || strNumCcy.equalsIgnoreCase (strDenomCcy) || (!strQuoteCcy.equalsIgnoreCase
				(strNumCcy) && !strQuoteCcy.equalsIgnoreCase (strDenomCcy)) ||
					!org.drip.math.common.NumberUtil.IsValid (dblPIPFactor))
			throw new java.lang.Exception ("Invalid parameters into CurrencyPair ctr");

		_strNumCcy = strNumCcy;
		_strDenomCcy = strDenomCcy;
		_strQuoteCcy = strQuoteCcy;
		_dblPIPFactor = dblPIPFactor;
	}

	/**
	 * CurrencyPair de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CurrencyPair cannot be properly de-serialized
	 */

	public CurrencyPair (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CurrencyPair de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CurrencyPair de-serializer: Empty state");

		java.lang.String strSerializedCurrencyPair = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCurrencyPair || strSerializedCurrencyPair.isEmpty())
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCurrencyPair, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("CurrencyPair de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate Num ccy");

		_strNumCcy = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate Denom ccy");

		_strDenomCcy = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate Quote ccy");

		_strQuoteCcy = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate PIP factor");

		_dblPIPFactor = new java.lang.Double (astrField[4]);
	}

	/**
	 * Gets the numerator currency
	 * 
	 * @return Numerator currency
	 */

	public java.lang.String getNumCcy()
	{
		return _strNumCcy;
	}

	/**
	 * Gets the denominator currency
	 * 
	 * @return Denominator currency
	 */

	public java.lang.String getDenomCcy()
	{
		return _strDenomCcy;
	}

	/**
	 * Gets the quote currency
	 * 
	 * @return Quote currency
	 */

	public java.lang.String getQuoteCcy()
	{
		return _strQuoteCcy;
	}

	/**
	 * Gets the currency pair code
	 * 
	 * @return Currency pair code
	 */

	public java.lang.String getCode()
	{
		return _strNumCcy + _strDenomCcy;
	}

	/**
	 * Gets the PIP Factor
	 * 
	 * @return PIP Factor
	 */

	public double getPIPFactor()
	{
		return _dblPIPFactor;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return ":";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "{";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strNumCcy);

		sb.append (getFieldDelimiter() + _strDenomCcy + getFieldDelimiter() + _strQuoteCcy);

		sb.append (getFieldDelimiter() + _dblPIPFactor);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CurrencyPair (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CurrencyPair cp = new CurrencyPair ("USD", "INR", "INR", 1.);

		byte[] abCP = cp.serialize();

		System.out.println ("Input: " + new java.lang.String (abCP));

		System.out.println ("QuoteCcy: " + cp.getQuoteCcy());

		CurrencyPair cpDeser = new CurrencyPair (abCP);

		System.out.println ("Output: " + new java.lang.String (cpDeser.serialize()));

		System.out.println ("QuoteCcy: " + cpDeser.getQuoteCcy());
	}
}
