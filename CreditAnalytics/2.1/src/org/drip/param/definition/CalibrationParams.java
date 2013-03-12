
package org.drip.param.definition;

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
 * Class contains the calibration parameters - the measure to be calibrated, and the type/nature of the
 * 		calibration to be performed
 *
 * @author Lakshmi Krishnamurthy
 */

public class CalibrationParams extends org.drip.service.stream.Serializer {
	/**
	 * Calibration Measure
	 */

	public java.lang.String _strMeasure = "";

	/**
	 * Calibration Type
	 */

	public int _iType = 0;

	/**
	 * (Optional) Calibration Workout Info
	 */

	public org.drip.param.valuation.WorkoutInfo _wi = null;

	/**
	 * Creates a standard calibration parameter instance around the price measure and base type
	 * 
	 * @return CalibrationParams instance
	 */

	public static final CalibrationParams MakeStdCalibParams()
	{
		try {
			return new CalibrationParams ("Price", 0, null);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * CalibrationParams constructor
	 * 
	 * @param strMeasure Measure to be calibrated
	 * @param iType Calibration Type
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public CalibrationParams (
		final java.lang.String strMeasure,
		final int iType,
		final org.drip.param.valuation.WorkoutInfo wi)
		throws java.lang.Exception
	{
		if (null == (_strMeasure = strMeasure) || _strMeasure.isEmpty())
			throw new java.lang.Exception ("Invalid Inputs into CalibrationParams!");

		_wi = wi;
		_iType = iType;
	}

	/**
	 * CalibrationParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CalibrationParams cannot be properly de-serialized
	 */

	public CalibrationParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CalibrationParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CalibrationParams de-serializer: Empty state");

		java.lang.String strSerializedCalibrationParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCalibrationParams || strSerializedCalibrationParams.isEmpty())
			throw new java.lang.Exception ("CalibrationParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCalibrationParams, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("CalibrationParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]).doubleValue();

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CalibrationParams de-serializer: Cannot locate calib measure");

		_strMeasure = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CalibrationParams de-serializer: Cannot locate calib type");

		_iType = new java.lang.Integer (astrField[2]).intValue();

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("CalibrationParams de-serializer: Cannot locate work-out info");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_wi = null;
		else
			_wi = new org.drip.param.valuation.WorkoutInfo (astrField[3].getBytes());
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "[";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "]";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strMeasure +
			getFieldDelimiter() + _iType + getFieldDelimiter());

		if (null == _wi)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_wi.serialize()) + getFieldDelimiter());

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CalibrationParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CalibrationParams cp = new CalibrationParams ("Price", 1, new org.drip.param.valuation.WorkoutInfo
			(org.drip.analytics.date.JulianDate.Today().getJulian(), 0.06, 1.,
				org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY));

		byte[] abCP = cp.serialize();

		System.out.println (new java.lang.String (abCP));

		CalibrationParams cpDeser = new CalibrationParams (abCP);

		System.out.println (new java.lang.String (cpDeser.serialize()));
	}
}
