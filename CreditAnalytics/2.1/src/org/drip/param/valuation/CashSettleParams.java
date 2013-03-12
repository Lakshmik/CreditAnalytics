
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
 * This implementation is the place-holder for the cash settlement parameters for a given product. Contains
 * 		the cash settle lag, the calendar, and the date adjustment mode.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CashSettleParams extends org.drip.service.stream.Serializer {
	/**
	 * Cash Settle Lag
	 */

	public int _iLag = 3;

	/**
	 * Cash Settle Calendar
	 */

	public java.lang.String _strCalendar = "";

	/**
	 * Cash Settle Adjust Mode
	 */

	public int _iAdjustMode = org.drip.analytics.daycount.Convention.DR_FOLL;

	/**
	 * Constructs the CashSettleParams object from the settle lag and the settle calendar objects
	 * 
	 * @param iLag Cash Settle Lag
	 * @param iAdjustMode Settle adjust Mode
	 * @param strCalendar Settlement Calendar
	 */

	public CashSettleParams (
		final int iLag,
		final java.lang.String strCalendar,
		final int iAdjustMode)
	{
		_iLag = iLag;
		_iAdjustMode = iAdjustMode;
		_strCalendar = strCalendar;
	}

	/**
	 * CashSettleParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CashSettleParams cannot be properly de-serialized
	 */

	public CashSettleParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CashSettleParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CashSettleParams de-serializer: Empty state");

		java.lang.String strSerializedCashSettleParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCashSettleParams || strSerializedCashSettleParams.isEmpty())
			throw new java.lang.Exception ("CashSettleParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCashSettleParams, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("CashSettleParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CashSettleParams de-serializer: Cannot locate Lag");

		_iLag = new java.lang.Integer (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("CashSettleParams de-serializer: Cannot locate Calendar");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
			_strCalendar = "";
		else
			_strCalendar = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CashSettleParams de-serializer: Cannot locate Adj mode");

		_iAdjustMode = new java.lang.Integer (astrField[3]);
	}

	/**
	 * Constructs and returns the cash settle date from the valuation date
	 * 
	 * @param dblValue Valuation Date
	 * 
	 * @return Cash settle date
	 */

	public double cashSettleDate (
		final double dblValue)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValue))
			throw new java.lang.Exception ("Invalid input valuation date");

		return org.drip.analytics.daycount.Convention.Adjust (dblValue + _iLag, _strCalendar, _iAdjustMode);
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "~";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "`";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _iLag +
			getFieldDelimiter() + _strCalendar + getFieldDelimiter() + _iAdjustMode);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CashSettleParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CashSettleParams csp = new CashSettleParams (2, "DKK", 3);

		byte[] abCSP = csp.serialize();

		System.out.println (new java.lang.String (abCSP));

		CashSettleParams cspDeser = new CashSettleParams (abCSP);

		System.out.println (new java.lang.String (cspDeser.serialize()));
	}
}
