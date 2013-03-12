
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
 * Contains the treasury benchmark set - the primary treasury benchmark, and an array of secondary treasury
 * 	benchmarks
 *
 * @author Lakshmi Krishnamurthy
 */

public class TsyBmkSet extends org.drip.service.stream.Serializer {
	private java.lang.String _strBmkPrimary = "";
	private java.lang.String[] _astrSecBmk = null;

	/**
	 * Constructs the treasury benchmark set from the primary treasury benchmark, and an array of secondary
	 * 	treasury benchmarks
	 * 
	 * @param strBmkPrimary Primary Treasury Benchmark
	 * @param astrSecBmk Array of Secondary Treasury Benchmarks
	 */

	public TsyBmkSet (
		final java.lang.String strBmkPrimary,
		final java.lang.String[] astrSecBmk)
	{
		_astrSecBmk = astrSecBmk;
		_strBmkPrimary = strBmkPrimary;
	}

	/**
	 * TsyBmkSet de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if TsyBmkSet cannot be properly de-serialized
	 */

	public TsyBmkSet (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Empty state");

		java.lang.String strSerializedTsyBmkSet = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedTsyBmkSet || strSerializedTsyBmkSet.isEmpty())
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedTsyBmkSet,
			getFieldDelimiter());

		if (null == astrField || 3 > astrField.length)
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Cannot locate Primary Tsy Bmk");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_strBmkPrimary = "";
		else
			_strBmkPrimary = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("TsyBmkSet de-serializer: Cannot locate CUSIP");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_astrSecBmk = null;
		else {
			_astrSecBmk = org.drip.analytics.support.GenericUtil.Split (astrField[2],
				getCollectionRecordDelimiter());

			if (null == _astrSecBmk || 0 == _astrSecBmk.length) {
				for (int i = 0; i < _astrSecBmk.length; ++i) {
					if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (_astrSecBmk[i]))
						_astrSecBmk[i] = null;
				}
			}
		}
	}

	/**
	 * Returns the Primary Treasury Benchmark
	 * 
	 * @return Primary Treasury Benchmark
	 */

	public java.lang.String getPrimaryBmk()
	{
		return _strBmkPrimary;
	}

	/**
	 * Returns an Array of Secondary Treasury Benchmarks
	 * 
	 * @return  Array of Secondary Treasury Benchmarks
	 */

	public java.lang.String[] getSecBmk()
	{
		return _astrSecBmk;
	}

	@Override public java.lang.String getCollectionRecordDelimiter()
	{
		return "!";
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

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strBmkPrimary +
			getFieldDelimiter());

		if (null == _astrSecBmk || 0 == _astrSecBmk.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbSB = new java.lang.StringBuffer();

			for (int i = 0; i < _astrSecBmk.length; ++i) {
				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbSB.append (getCollectionRecordDelimiter());

				if (null != _astrSecBmk[i] && !_astrSecBmk[i].isEmpty())
					sbSB.append (_astrSecBmk[i]);
				else
					sbSB.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			}

			if (!sbSB.toString().isEmpty())
				sb.append (sbSB.toString());
			else
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new TsyBmkSet (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		TsyBmkSet tss = new TsyBmkSet ("ABC", new java.lang.String[] {"DEF", "GHI", "JKL"});

		byte[] abTSS = tss.serialize();

		System.out.println (new java.lang.String (abTSS));

		TsyBmkSet tssDeser = new TsyBmkSet (abTSS);

		System.out.println (new java.lang.String (tssDeser.serialize()));
	}
}
