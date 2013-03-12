
package org.drip.product.params;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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
 *  This class implements the creation and the static details of the all the NA, EU, SovX, EMEA, and ASIA
 *  	standardized CDS indicies.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class CDXIdentifier extends org.drip.service.stream.Serializer {
	public int _iSeries = 0;
	public int _iVersion = 0;
	public java.lang.String _strIndex = "";
	public java.lang.String _strTenor = "";

	/**
	 * Creates the CDX Identifier from the CDX Code
	 * 
	 * @param strCode The CDX Code
	 * 
	 * @return CDXIdentifier output
	 */

	public static final CDXIdentifier CreateCDXIdentifierFromCode (
		final java.lang.String strCode)
	{
		if (null == strCode || strCode.isEmpty()) return null;

		java.lang.String[] astrFields = strCode.split (".");

		if (null == astrFields || 4 > astrFields.length) return null;

		try {
			return new CDXIdentifier (new java.lang.Integer (astrFields[astrFields.length - 2]), new
				java.lang.Integer (astrFields[astrFields.length - 1]), astrFields[astrFields.length - 3],
					astrFields[astrFields.length - 4]);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the CDX identifier from the CDX index, series, tenor, and the version
	 * 
	 * @param iSeries CDX Series
	 * @param iVersion CDX Version
	 * @param strIndex CDX Index
	 * @param strTenor CDX Tenor
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public CDXIdentifier (
		final int iSeries,
		final int iVersion,
		final java.lang.String strIndex,
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == (_strIndex = strIndex) || _strIndex.isEmpty() || null == (_strTenor = strTenor) ||
			_strTenor.isEmpty())
			throw new java.lang.Exception ("Invalid parameters into CDXIdentifier!");

		_iSeries = iSeries;
		_iVersion = iVersion;
	}

	/**
	 * CDXIdentifier de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CDXIdentifier cannot be properly de-serialized
	 */

	public CDXIdentifier (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Empty state");

		java.lang.String strSerializedCDXIdentifier = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCDXIdentifier || strSerializedCDXIdentifier.isEmpty())
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCDXIdentifier, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Cannot locate Series");

		_iSeries = new java.lang.Integer (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Cannot locate Version");

		_iVersion = new java.lang.Integer (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Cannot locate Index");

		_strIndex = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("CDXIdentifier de-serializer: Cannot locate Tenor");

		_strTenor = astrField[4];
	}

	/**
	 * Returns the CDX code string composed off of the index, tenor, series, and the version
	 * 
	 * @return The CDX Code string
	 */

	public java.lang.String getCode()
	{
		return _strIndex + "." + _strTenor + "." + _iSeries + "." + _iVersion;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _iSeries +
			getFieldDelimiter() + _iVersion + getFieldDelimiter() + _strIndex + getFieldDelimiter() +
				_strTenor);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CDXIdentifier (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CDXIdentifier cdxID = new CDXIdentifier (17, 1, "CDX.NA.IG", "5Y");

		byte[] abCDXID = cdxID.serialize();

		System.out.println (new java.lang.String (abCDXID));

		CDXIdentifier abCDXIDDeser = new CDXIdentifier (abCDXID);

		System.out.println (new java.lang.String (abCDXIDDeser.serialize()));
	}
}
