
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
 * This class contains the current "liveness" state of the component, and, if inactive, how it entered that
 * 		state.
 *
 * @author Lakshmi Krishnamurthy
 */

public class TerminationSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Is the component Perpetual
	 */

	public boolean _bIsPerpetual = false;

	/**
	 * Has the component Defaulted
	 */

	public boolean _bIsDefaulted = false;

	/**
	 * Has the component Been Exercised
	 */

	public boolean _bHasBeenExercised = false;

	/**
	 * Constructs the TerminationSetting object from the perpetual flag, defaulted flag, and the has
	 * 		been exercised flag.
	 * 
	 * @param bIsPerpetual True (component is perpetual)
	 * @param bIsDefaulted True (component has defaulted)
	 * @param bHasBeenExercised True (component has been exercised)
	 */

	public TerminationSetting (
		final boolean bIsPerpetual,
		final boolean bIsDefaulted,
		final boolean bHasBeenExercised)
	{
		_bIsPerpetual = bIsPerpetual;
		_bIsDefaulted = bIsDefaulted;
		_bHasBeenExercised = bHasBeenExercised;
	}

	/**
	 * TerminationSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if TerminationSetting cannot be properly de-serialized
	 */

	public TerminationSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception
				("TerminationSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("TerminationSetting de-serializer: Empty state");

		java.lang.String strSerializedTerminationSetting = strRawString.substring (0,
			strRawString.indexOf (getObjectTrailer()));

		if (null == strSerializedTerminationSetting ||
			strSerializedTerminationSetting.isEmpty())
			throw new java.lang.Exception ("TerminationSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedTerminationSetting, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception
				("TerminationSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("TerminationSetting de-serializer: Cannot locate perpetual flag");

		_bIsPerpetual = new java.lang.Boolean (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("TerminationSetting de-serializer: Cannot locate defaulted flag");

		_bIsDefaulted = new java.lang.Boolean (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("TerminationSetting de-serializer: Cannot locate exercised flag");

		_bHasBeenExercised = new java.lang.Boolean (astrField[3]);

		if (!validate())
			throw new java.lang.Exception ("TerminationSetting de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _bIsPerpetual +
			getFieldDelimiter() + _bIsDefaulted + getFieldDelimiter() + _bHasBeenExercised);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new TerminationSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		TerminationSetting cfte = new TerminationSetting (true, false, true);

		byte[] abCFTE = cfte.serialize();

		System.out.println (new java.lang.String (abCFTE));

		TerminationSetting cfteDeser = new TerminationSetting (abCFTE);

		System.out.println (new java.lang.String (cfteDeser.serialize()));
	}
}
