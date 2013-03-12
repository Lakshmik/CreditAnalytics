
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
 * This implementation is the place-holder for the valuation parameters for a given product. Contains the
 * 	valuation and the cash pay/settle dates
 *
 * @author Lakshmi Krishnamurthy
 */

public class ValuationParams extends org.drip.service.stream.Serializer {
	/**
	 * Valuation Date
	 */

	public double _dblValue = java.lang.Double.NaN;

	/**
	 * Cash Pay Date
	 */

	public double _dblCashPay = java.lang.Double.NaN;

	/**
	 * Cash Pay Date Adjustment Calendar
	 */

	public java.lang.String _strCalendar = "";

	/**
	 * Creates the valuation parameters object instance from the valuation date, the cash settle lag, and the
	 * 		settle calendar.
	 * 
	 * @param dtValue Valuation Date
	 * @param iCashSettleLag Cash settle lag
	 * @param strCalendar Calendar Set
	 * 
	 * @return Valuation Parameters instance
	 */

	public static final ValuationParams CreateValParams (
		final org.drip.analytics.date.JulianDate dtValue,
		final int iCashSettleLag,
		final java.lang.String strCalendar,
		final int iAdjustMode)
	{
		if (null == dtValue) return null;

		try {
			return new ValuationParams (dtValue, new org.drip.analytics.date.JulianDate
				(org.drip.analytics.daycount.Convention.Adjust (dtValue.addDays
					(iCashSettleLag).getJulian(), strCalendar, iAdjustMode)), strCalendar);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create the standard T+2B settle parameters for the given valuation date and calendar
	 *  
	 * @param dtValue Valuation Date
	 * @param strCalendar Settle Calendar
	 * 
	 * @return Valuation Parameters instance
	 */

	public static final ValuationParams CreateStdValParams (
		final org.drip.analytics.date.JulianDate dtValue,
		final java.lang.String strCalendar)
	{
		return CreateValParams (dtValue, 2, strCalendar, org.drip.analytics.daycount.Convention.DR_FOLL);
	}

	/**
	 * Create the spot valuation parameters for the given valuation date (uses the T+0 settle)
	 *  
	 * @param dblDate Valuation Date
	 * 
	 * @return Valuation Parameters instance
	 */

	public static final ValuationParams CreateSpotValParams (
		final double dblDate)
	{
		try {
			org.drip.analytics.date.JulianDate dtValue = new org.drip.analytics.date.JulianDate (dblDate);

			return new ValuationParams (dtValue, dtValue, "");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ValuationParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if ValuationParams cannot be properly de-serialized
	 */

	public ValuationParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("ValuationParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("ValuationParams de-serializer: Empty state");

		java.lang.String strSerializedValuationParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedValuationParams || strSerializedValuationParams.isEmpty())
			throw new java.lang.Exception ("ValuationParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedValuationParams, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("ValuationParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("ValuationParams de-serializer: Cannot locate valuation date");

		_dblValue = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("ValuationParams de-serializer: Cannot locate cash pay date");

		_dblCashPay = new java.lang.Double (astrField[2]).doubleValue();

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("ValuationParams de-serializer: Cannot locate Calendar");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[3]))
			_strCalendar = "";
		else
			_strCalendar = astrField[3];
	}

	/**
	 * Constructs ValuationParams from the Valuation Date and the Cash Pay Date parameters
	 * 
	 * @param dtValue Valuation Date
	 * @param dtCashPay Cash Pay Date
	 * @param strCalendar Calendar Set
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public ValuationParams (
		final org.drip.analytics.date.JulianDate dtValue,
		final org.drip.analytics.date.JulianDate dtCashPay,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == dtValue || null ==  dtCashPay)
			throw new java.lang.Exception ("Invalid settle/Cash pay into Val Params!");

		_dblValue = dtValue.getJulian();

		_dblCashPay = dtCashPay.getJulian();

		_strCalendar = strCalendar;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (VERSION + getFieldDelimiter() + _dblValue + getFieldDelimiter() + _dblCashPay +
			getFieldDelimiter() + _strCalendar);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ValuationParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.support.Logger.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		ValuationParams vp = ValuationParams.CreateValParams (org.drip.analytics.date.JulianDate.Today(), 2,
			"DKK", 3);

		byte[] abVP = vp.serialize();

		System.out.println (new java.lang.String (abVP));

		ValuationParams vpDeser = new ValuationParams (abVP);

		System.out.println (new java.lang.String (vpDeser.serialize()));
	}
}
