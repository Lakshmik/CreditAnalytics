
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
 * This class contains the notional schedule and the amount.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NotionalSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Period amortization proxies to the period start factor
	 */

	public static final int PERIOD_AMORT_AT_START = 1;

	/**
	 * Period amortization proxies to the period end factor
	 */

	public static final int PERIOD_AMORT_AT_END = 2;

	/**
	 * Period amortization proxies to the period effective factor
	 */

	public static final int PERIOD_AMORT_EFFECTIVE = 3;

	/**
	 * Notional Amount
	 */

	public double _dblNotional = java.lang.Double.NaN;

	/**
	 * Is the price quoted off of component's issue notional factor
	 */

	public boolean _bPriceOffOriginalNotional = false;

	/**
	 * Amortization Mode - Indicates which amortization node serves as the period's amortization proxy
	 */

	public int _iPeriodAmortizationMode = PERIOD_AMORT_AT_START;

	/**
	 * Notional Schedule
	 */

	public FactorSchedule _fsPrincipalOutstanding = null;

	/**
	 * Constructs the NotionalSetting from the notional schedule and the amount.
	 * 
	 * @param fsPrincipalOutstanding Notional Schedule
	 * @param dblNotional Notional Amount
	 * @param iPeriodAmortizationMode Period Amortization Proxy Mode
	 * @param bPriceOffOriginalNotional Indicates whether the price is based off of the original notional
	 */

	public NotionalSetting (
		final FactorSchedule fsPrincipalOutstanding,
		final double dblNotional,
		final int iPeriodAmortizationMode,
		final boolean bPriceOffOriginalNotional)
	{
		_dblNotional = dblNotional;
		_fsPrincipalOutstanding = fsPrincipalOutstanding;
		_iPeriodAmortizationMode = iPeriodAmortizationMode;
		_bPriceOffOriginalNotional = bPriceOffOriginalNotional;
	}

	/**
	 * NotionalSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if NotionalSetting cannot be properly de-serialized
	 */

	public NotionalSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("NotionalSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("NotionalSetting de-serializer: Empty state");

		java.lang.String strSerializedNotionalSetting = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedNotionalSetting || strSerializedNotionalSetting.isEmpty())
			throw new java.lang.Exception ("NotionalSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedNotionalSetting, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("NotionalSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception
				("NotionalSetting de-serializer: Cannot locate principal schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_fsPrincipalOutstanding = FactorSchedule.CreateBulletSchedule();
		else
			_fsPrincipalOutstanding = new FactorSchedule (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("NotionalSetting de-serializer: Cannot locate principal notional");

		_dblNotional = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("NotionalSetting de-serializer: Cannot locate Price Off Original Notional flag");

		_bPriceOffOriginalNotional = new java.lang.Boolean (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("NotionalSetting de-serializer: Cannot locate Period amortization proxy mode");

		_iPeriodAmortizationMode = new java.lang.Integer (astrField[4]);

		if (!validate()) throw new java.lang.Exception ("NotionalSetting de-serializer: Cannot validate!");
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		if (null == _fsPrincipalOutstanding)
			sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() +
				org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter() + _dblNotional +
					getFieldDelimiter() + _bPriceOffOriginalNotional + getFieldDelimiter() +
						_iPeriodAmortizationMode);
		else
			sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + new
				java.lang.String (_fsPrincipalOutstanding.serialize()) + getFieldDelimiter() + _dblNotional +
					getFieldDelimiter() + _bPriceOffOriginalNotional + getFieldDelimiter() +
						_iPeriodAmortizationMode);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public boolean validate()
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblNotional)) return false;

		if (null == _fsPrincipalOutstanding) _fsPrincipalOutstanding = FactorSchedule.CreateBulletSchedule();

		return true;
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new NotionalSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		NotionalSetting bnp = new NotionalSetting (null, 1., PERIOD_AMORT_AT_START, false);

		byte[] abBNP = bnp.serialize();

		System.out.println (new java.lang.String (abBNP));

		NotionalSetting bnpDeser = new NotionalSetting (abBNP);

		System.out.println (new java.lang.String (bnpDeser.serialize()));
	}
}
