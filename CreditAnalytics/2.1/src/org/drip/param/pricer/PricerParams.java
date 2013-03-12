
package org.drip.param.pricer;

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
 * Class contains the pricer parameters - the discrete unit size, calibration mode on/off, survival to
 * 	pay/end date, and the discretization scheme
 *
 * @author Lakshmi Krishnamurthy
 */

public class PricerParams extends org.drip.service.stream.Serializer {
	/*
	 * Loss period Grid discretization scheme
	 */

	/**
	 * Minimum number of days per unit
	 */

	public static final int PERIOD_DAY_STEPS_MINIMUM = 7;

	/**
	 * Discretization as a sequence of day steps
	 */

	public static final int PERIOD_DISCRETIZATION_DAY_STEP = 1;

	/**
	 * Discretization as a sequence of time space divided periods
	 */

	public static final int PERIOD_DISCRETIZATION_PERIOD_STEP = 2;

	/**
	 * No discretization at all - just the full coupon period
	 */

	public static final int PERIOD_DISCRETIZATION_FULL_COUPON = 3;

	/**
	 * Discretization Unit Size
	 */

	public int _iUnitSize = 7;

	/**
	 * (Optional) Calibration Params
	 */

	public org.drip.param.definition.CalibrationParams _calibParams = null;

	/**
	 * Survival to Pay Date (True) or Period End Date (false)
	 */

	public boolean _bSurvToPayDate = false;

	/**
	 * Discretization Scheme In Use
	 */

	public int _iDiscretizationScheme = PERIOD_DISCRETIZATION_DAY_STEP;

	/**
	 * Creates the standard pricer parameters object instance
	 * 
	 * @return PricerParams object instance
	 */

	public static final PricerParams MakeStdPricerParams()
	{
		return new PricerParams (7, null, false, PERIOD_DISCRETIZATION_DAY_STEP);
	}

	/**
	 * Creates the pricer parameters from the discrete unit size, calibration mode on/off, survival to
	 * 	pay/end date, and the discretization scheme
	 * 
	 * @param iUnitSize Discretization Unit Size
	 * @param calibParams Optional Calibration Params
	 * @param bSurvToPayDate Survival to Pay Date (True) or Period End Date (false)
	 * @param iDiscretizationScheme Discretization Scheme In Use
	 */

	public PricerParams (
		final int iUnitSize,
		final org.drip.param.definition.CalibrationParams calibParams,
		final boolean bSurvToPayDate,
		final int iDiscretizationScheme)
	{
		_iUnitSize = iUnitSize;
		_calibParams = calibParams;
		_bSurvToPayDate = bSurvToPayDate;
		_iDiscretizationScheme = iDiscretizationScheme;
	}

	/**
	 * PricerParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if PricerParams cannot be properly de-serialized
	 */

	public PricerParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("PricerParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("PricerParams de-serializer: Empty state");

		java.lang.String strSerializedCreditCurve = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCreditCurve || strSerializedCreditCurve.isEmpty())
			throw new java.lang.Exception ("PricerParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCreditCurve, getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("PricerParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("PricerParams de-serializer: Cannot locate unit size");

		_iUnitSize = new java.lang.Integer (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("PricerParams de-serializer: Cannot locate calib params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_calibParams = null;
		else
			_calibParams = new org.drip.param.definition.CalibrationParams (astrField[2].getBytes());

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("PricerParams de-serializer: Cannot locate survival to pay date flag");

		_bSurvToPayDate = new java.lang.Boolean (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("PricerParams de-serializer: Cannot locate discretization scheme");

		_iDiscretizationScheme = new java.lang.Integer (astrField[4]);
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "!";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "&";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _iUnitSize +
			getFieldDelimiter());

		if (null == _calibParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_calibParams.serialize()) + getFieldDelimiter());

		sb.append (_bSurvToPayDate + getFieldDelimiter() + _iDiscretizationScheme + getFieldDelimiter());

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new PricerParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		PricerParams p = new PricerParams (7, new org.drip.param.definition.CalibrationParams ("Price",
			1, new org.drip.param.valuation.WorkoutInfo
				(org.drip.analytics.date.JulianDate.Today().getJulian(), 0.04, 1.,
					org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY)), false, 1);

		byte[] abPricer = p.serialize();

		System.out.println (new java.lang.String (abPricer));

		PricerParams pDeser = new PricerParams (abPricer);

		System.out.println (new java.lang.String (pDeser.serialize()));
	}
}
