
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
 * Class contains component treasury benchmark parameters - the treasury benchmark set, and the names of the
 * 	treasury and the EDF IR curves.
 *
 * @author Lakshmi Krishnamurthy
 */

public class TreasuryBenchmark extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Treasury Benchmark Set
	 */

	public TsyBmkSet _tsyBmkSet = null;

	/**
	 * Treasury IR Curve Name
	 */

	public java.lang.String _strIRTSY = "";

	/**
	 * EDSF IR Curve Name
	 */

	public java.lang.String _strIREDSF = "";

	/**
	 * Creates the TreasuryBenchmark object from the treasury benchmark set, and the names of the
	 * 	treasury and the EDF IR curves.
	 * 
	 * @param tsyBmkSet Treasury Benchmark Set
	 * @param strIRTSY Treasury IR Curve Name
	 * @param strIREDSF EDSF IR Curve Name
	 */

	public TreasuryBenchmark (
		final TsyBmkSet tsyBmkSet,
		final java.lang.String strIRTSY,
		final java.lang.String strIREDSF)
	{
		_strIRTSY = strIRTSY;
		_strIREDSF = strIREDSF;
		_tsyBmkSet = tsyBmkSet;
	}

	/**
	 * TreasuryBenchmark de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if TreasuryBenchmark cannot be properly de-serialized
	 */

	public TreasuryBenchmark (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Empty state");

		java.lang.String strSerializedTreasuryBenchmark = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedTreasuryBenchmark || strSerializedTreasuryBenchmark.isEmpty())
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedTreasuryBenchmark, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Cannot locate tsy params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_tsyBmkSet = null;
		else
			_tsyBmkSet = new TsyBmkSet (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception
				("TreasuryBenchmark de-serializer: Cannot locate IR TSY curve name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strIRTSY = "";
		else
			_strIRTSY = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception
				("TreasuryBenchmark de-serializer: Cannot locate IR EDSF curve name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strIREDSF = "";
		else
			_strIREDSF = astrField[3];

		if (!validate())
			throw new java.lang.Exception ("TreasuryBenchmark de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if ((null == _strIRTSY || _strIRTSY.isEmpty()) || (null == _strIREDSF || _strIREDSF.isEmpty()))
			return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null != _tsyBmkSet)
			sb.append (new java.lang.String (_tsyBmkSet.serialize()) + getFieldDelimiter());
		else
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());

		if (null == _strIRTSY || _strIRTSY.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIRTSY + getFieldDelimiter());

		if (null == _strIREDSF || _strIREDSF.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIREDSF);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new TreasuryBenchmark (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		TreasuryBenchmark btp = new TreasuryBenchmark (new TsyBmkSet ("ABC", new java.lang.String[] {"DEF",
			"GHI", "JKL"}), "ABCTSY", "ABDEDSF");

		byte[] abBTP = btp.serialize();

		System.out.println (new java.lang.String (abBTP));

		TreasuryBenchmark btpDeser = new TreasuryBenchmark (abBTP);

		System.out.println (new java.lang.String (btpDeser.serialize()));
	}
}
