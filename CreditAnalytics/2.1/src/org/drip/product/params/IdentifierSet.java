
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
 * This class contains the component's identifier parameters - ISIN, CUSIP, ID, and ticker.
 *
 * @author Lakshmi Krishnamurthy
 */

public class IdentifierSet extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * ISIN
	 */

	public java.lang.String _strISIN = "";

	/**
	 * CUSIP
	 */

	public java.lang.String _strCUSIP = "";

	/**
	 * component ID
	 */

	public java.lang.String _strID = "";

	/**
	 * Ticker
	 */

	public java.lang.String _strTicker = "";

	/**
	 * Constructs the IdentifierSet from ISIN, CUSIP, ID, and ticker.
	 * 
	 * @param strISIN ISIN
	 * @param strCUSIP CUSIP
	 * @param strID component ID
	 * @param strTicker Ticker
	 */

	public IdentifierSet (
		final java.lang.String strISIN,
		final java.lang.String strCUSIP,
		final java.lang.String strID,
		final java.lang.String strTicker)
	{
		_strISIN = strISIN;
		_strCUSIP = strCUSIP;
		_strID = strID;
		_strTicker = strTicker;
	}

	/**
	 * IdentifierSet de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if IdentifierSet cannot be properly de-serialized
	 */

	public IdentifierSet (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("IdentifierSet de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("IdentifierSet de-serializer: Empty state");

		java.lang.String strSerializedIdentifierSet = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedIdentifierSet || strSerializedIdentifierSet.isEmpty())
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedIdentifierSet, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("IdentifierSet de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot locate ISIN");

		_strISIN = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot locate CUSIP");

		_strCUSIP = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot locate component ID");

		_strID = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot locate ticker");

		_strTicker = astrField[4];

		if (!validate())
			throw new java.lang.Exception ("IdentifierSet de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if ((null == _strISIN || _strISIN.isEmpty()) && (null == _strCUSIP || _strCUSIP.isEmpty()))
			return false;

		if (null == _strID || _strID.isEmpty()) {
			if (null == (_strID = _strISIN) || _strID.isEmpty()) _strID = _strCUSIP;
		}

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strISIN +
			getFieldDelimiter() + _strCUSIP + getFieldDelimiter() + _strID + getFieldDelimiter() +
				_strTicker);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new IdentifierSet (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		IdentifierSet bip = new IdentifierSet ("ABC", "DEF", "GHI", "JKL");

		byte[] abBIP = bip.serialize();

		System.out.println (new java.lang.String (abBIP));

		IdentifierSet bipDeser = new IdentifierSet (abBIP);

		System.out.println (new java.lang.String (bipDeser.serialize()));
	}
}
