
package org.drip.param.valuation;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * QuotingParams holds the parameters needed to interpret the input quotes
 * 
 * @author Lakshmi Krishnamurthy
 */

public class QuotingParams extends org.drip.service.stream.Serializer {
	/**
	 * Yield Quoting day count
	 */

	public java.lang.String _strYieldDC = "";

	/**
	 * Yield Quoting Frequency
	 */

	public int _iYieldFrequency = 0;

	/**
	 * Yield Apply EOM Adjustment?
	 */

	public boolean _bYieldApplyEOMAdj = false;

	/**
	 * Yield Act Act DC Params
	 */

	public org.drip.analytics.daycount.ActActDCParams _aapYield = null;

	/**
	 * Yield Calendar
	 */

	public java.lang.String _strYieldCalendar = "";

	/**
	 * Is Spread Quoted
	 */

	public boolean _bSpreadQuoted = false;

	/**
	 * QuotingParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if QuotingParams cannot be properly de-serialized
	 */

	public QuotingParams (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("QuotingParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("QuotingParams de-serializer: Empty state");

		java.lang.String strSerializedQuotingParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedQuotingParams || strSerializedQuotingParams.isEmpty())
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedQuotingParams, getFieldDelimiter());

		if (null == astrField || 7 > astrField.length)
			throw new java.lang.Exception ("QuotingParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate yield DC");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			_strYieldDC = "";
		else
			_strYieldDC = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate Yield Frequency");

		_iYieldFrequency = new java.lang.Integer (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate spread quote flag");

		_bSpreadQuoted = new java.lang.Boolean (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate yield DC");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[4]))
			_strYieldCalendar = "";
		else
			_strYieldCalendar = astrField[4];

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("QuotingParams de-serializer: Cannot locate apply EOM flag");

		_bYieldApplyEOMAdj = new java.lang.Boolean (astrField[5]).booleanValue();

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception
				("QuotingParams de-serializer: Cannot locate optional yield ActAct Params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[6]))
			_aapYield = null;
		else
			_aapYield = new org.drip.analytics.daycount.ActActDCParams (astrField[6].getBytes());
	}

	/**
	 * Constructs QuotingParams from the Day Count and the Frequency parameters
	 * 
	 * @param strDC Quoting Day Count
	 * @param iFrequency Quoting Frequency
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public QuotingParams (
		final java.lang.String strDC,
		final int iFrequency,
		final boolean bApplyEOMAdj,
		final org.drip.analytics.daycount.ActActDCParams aap,
		final java.lang.String strCalendar,
		final boolean bSpreadQuoted)
		throws java.lang.Exception
	{
		if (null == strDC || strDC.isEmpty() || 0 == iFrequency)
			throw new java.lang.Exception ("Invalid quoting params!");

		_aapYield = aap;
		_strYieldDC = strDC;
		_iYieldFrequency = iFrequency;
		_bSpreadQuoted = bSpreadQuoted;
		_strYieldCalendar = strCalendar;
		_bYieldApplyEOMAdj = bApplyEOMAdj;
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

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _strYieldDC || _strYieldDC.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strYieldDC + getFieldDelimiter());

		sb.append (_iYieldFrequency + getFieldDelimiter() + _bSpreadQuoted + getFieldDelimiter());

		if (null == _strYieldCalendar || _strYieldCalendar.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strYieldCalendar + getFieldDelimiter());

		sb.append (_bYieldApplyEOMAdj + getFieldDelimiter());

		if (null == _aapYield)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_aapYield.serialize()) + getFieldDelimiter());

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new QuotingParams (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		QuotingParams qp = new QuotingParams ("30/360", 2, true, null, "DKK", false);

		byte[] abQP = qp.serialize();

		System.out.println (new java.lang.String (abQP));

		QuotingParams qpDeser = new QuotingParams (abQP);

		System.out.println (new java.lang.String (qpDeser.serialize()));
	}
}
