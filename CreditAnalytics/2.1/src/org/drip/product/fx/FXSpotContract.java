
package org.drip.product.fx;

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
 *  Class contains the FX spot contract parameters - the spot date and the currency pair.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class FXSpotContract extends org.drip.product.definition.FXSpot {
	private double _dblSpotDate = java.lang.Double.NaN;
	private org.drip.product.params.CurrencyPair _ccyPair = null;

	/**
	 * Constructor: Creates the FX spot object from the spot date and the currency pair.
	 * 
	 * @param dtSpot Spot date
	 * @param ccyPair CurrencyPair
	 * 
	 * @throws java.lang.Exception Thrown on invalid parameters
	 */

	public FXSpotContract (
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.params.CurrencyPair ccyPair)
		throws java.lang.Exception
	{
		if (null == dtSpot || null == ccyPair)
			throw new java.lang.Exception ("Invalid params into FXSpot ctr!");

		_ccyPair = ccyPair;

		_dblSpotDate = dtSpot.getJulian();
	}

	/**
	 * FXSpot de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FXSpot cannot be properly de-serialized
	 */

	public FXSpotContract (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FXSpot de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FXSpot de-serializer: Empty state");

		java.lang.String strSerializedFXSpot = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFXSpot || strSerializedFXSpot.isEmpty())
			throw new java.lang.Exception ("FXSpot de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedFXSpot, getFieldDelimiter());

		if (null == astrField || 3 > astrField.length)
			throw new java.lang.Exception ("FXSpot de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FXSpot de-serializer: Cannot locate Spot Date");

		_dblSpotDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FXSpot de-serializer: Cannot locate Currency Pair");

		_ccyPair = new org.drip.product.params.CurrencyPair (astrField[2].getBytes());
	}

	@Override public double getSpotDate()
	{
		return _dblSpotDate;
	}

	@Override public org.drip.product.params.CurrencyPair getCcyPair()
	{
		return _ccyPair;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _dblSpotDate +
			getFieldDelimiter() + new java.lang.String (_ccyPair.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new FXSpotContract (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		FXSpotContract fxSpot = new FXSpotContract (org.drip.analytics.date.JulianDate.Today(), new
			org.drip.product.params.CurrencyPair ("USD", "INR", "INR", 1.));

		byte[] abFXSpot = fxSpot.serialize();

		System.out.println (new java.lang.String (abFXSpot));

		FXSpotContract fxSpotDeser = new FXSpotContract (abFXSpot);

		System.out.println (new java.lang.String (fxSpotDeser.serialize()));
	}
}
