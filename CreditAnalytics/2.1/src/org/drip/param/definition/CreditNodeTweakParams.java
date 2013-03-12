
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
 * This class contains the place holder for the credit curve scenario tweak parameters, for a given measure,
 * 		for either a specific curve node, or the entire curve (flat).
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditNodeTweakParams extends NodeTweakParams {
	/**
	 * Tweak Parameter Type of Quote
	 */

	public static final java.lang.String CREDIT_TWEAK_NODE_PARAM_QUOTE = "Quote";

	/**
	 * Tweak Parameter Type of Recovery
	 */

	public static final java.lang.String CREDIT_TWEAK_NODE_PARAM_RECOVERY = "Recovery";

	/**
	 * Tweak Measure Type of Quote
	 */

	public static final java.lang.String CREDIT_TWEAK_NODE_MEASURE_QUOTE = "Quote";

	/**
	 * Tweak Measure Type of Hazard
	 */

	public static final java.lang.String CREDIT_TWEAK_NODE_MEASURE_HAZARD = "Hazard";

	/**
	 * Tweak Parameter Type
	 */

	public java.lang.String _strTweakParamType = "";

	/**
	 * Tweak Measure Type
	 */

	public java.lang.String _strTweakMeasureType = "";

	/**
	 * Flag indicating if the calibration occurs over a single node
	 */

	public boolean _bSingleNodeCalib = false;

	/**
	 * CreditNodeTweakParams constructor
	 * 
	 * @param strTweakParamType Node Tweak Parameter Type
	 * @param strTweakMeasureType Node Tweak Measure Type
	 * @param iTweakNode Node to be tweaked - Set to NODE_FLAT_TWEAK for flat curve tweak
	 * @param bIsTweakProportional True => Tweak is proportional, False => parallel
	 * @param dblTweakAmount Amount to be tweaked - proportional tweaks are represented as percent, parallel
	 * 			tweaks are absolute numbers
	 * @param bSingleNodeCalib Flat Calibration using a single node?
	 */

	public CreditNodeTweakParams (
		final java.lang.String strTweakParamType,
		final java.lang.String strTweakMeasureType,
		final int iTweakNode,
		final boolean bIsTweakProportional,
		final double dblTweakAmount,
		final boolean bSingleNodeCalib)
		throws java.lang.Exception
	{
		super (iTweakNode, bIsTweakProportional, dblTweakAmount);

		if (null == (_strTweakParamType = strTweakParamType) ||
			!CREDIT_TWEAK_NODE_PARAM_QUOTE.equalsIgnoreCase (_strTweakParamType) ||
				!CREDIT_TWEAK_NODE_PARAM_QUOTE.equalsIgnoreCase (_strTweakParamType))
			throw new java.lang.Exception ("Invalid Tweak Parameter Type!");

		if (null == (_strTweakMeasureType = strTweakMeasureType) ||
			!CREDIT_TWEAK_NODE_PARAM_QUOTE.equalsIgnoreCase (_strTweakMeasureType) ||
				!CREDIT_TWEAK_NODE_PARAM_QUOTE.equalsIgnoreCase (_strTweakMeasureType))
			throw new java.lang.Exception ("Invalid Tweak Measure Type!");

		_bSingleNodeCalib = bSingleNodeCalib;
	}

	/**
	 * CreditNodeTweakParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CreditNodeTweakParams cannot be properly de-serialized
	 */

	public CreditNodeTweakParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CreditNodeTweakParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CreditNodeTweakParams de-serializer: Empty state");

		java.lang.String strSerializedCreditNodeTweakParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCreditNodeTweakParams || strSerializedCreditNodeTweakParams.isEmpty())
			throw new java.lang.Exception ("CreditNodeTweakParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCreditNodeTweakParams, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("CreditNodeTweakParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("ProductCouponPeriodCurveMeasures de-serializer: Cannot locate Tweak Parameter Type");

		_strTweakParamType = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("ProductCouponPeriodCurveMeasures de-serializer: Cannot locate Tweak Measure Type");

		_strTweakMeasureType = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("ProductCouponPeriodCurveMeasures de-serializer: Cannot locate Tweak Measure Type");

		_bSingleNodeCalib = new java.lang.Boolean (astrField[3]);
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "@";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _strTweakParamType +
			getFieldDelimiter() + _strTweakMeasureType + getFieldDelimiter() + _bSingleNodeCalib);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CreditNodeTweakParams cntp = new CreditNodeTweakParams ("Quote", "Quote", NODE_FLAT_TWEAK, false,
			0.1, false);

		byte[] abCNTP = cntp.serialize();

		System.out.println (new java.lang.String (abCNTP));

		CreditNodeTweakParams cntpDeser = new CreditNodeTweakParams (abCNTP);

		System.out.println (new java.lang.String (cntpDeser.serialize()));
	}
}
