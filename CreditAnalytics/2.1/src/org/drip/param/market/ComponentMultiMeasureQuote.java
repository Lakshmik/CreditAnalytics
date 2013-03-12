
package org.drip.param.market;

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
 * This class holds the different types of quotes for a given component. It contains a single market
 *  field/quote pair, but multiple alternate named quotes (to accommodate quotes on different measures for
 *   the component). 
 *   
 * @author Lakshmi Krishnamurthy
 */

public class ComponentMultiMeasureQuote extends org.drip.param.definition.ComponentQuote {
	private java.lang.String _strMarketQuoteField = "";
	private org.drip.param.definition.Quote _mktQuote = null;

	private java.util.Map<java.lang.String, org.drip.param.definition.Quote> _mapQuotes = new
		java.util.HashMap<java.lang.String, org.drip.param.definition.Quote>();

	/**
	 * Constructs an empty component quote from the component
	 */

	public ComponentMultiMeasureQuote ()
	{
	}

	/**
	 * ComponentQuote de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if ComponentQuote cannot be properly de-serialized
	 */

	public ComponentMultiMeasureQuote (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("ComponentQuote de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("ComponentQuote de-serializer: Empty state");

		java.lang.String strSerializedComponentQuote = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedComponentQuote || strSerializedComponentQuote.isEmpty())
			throw new java.lang.Exception ("ComponentQuote de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedComponentQuote, getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("ComponentQuote de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("ComponentQuote de-serializer: Cannot locate market quote");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_mktQuote = new MultiSidedQuote (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("ComponentQuote de-serializer: Cannot locate market quote field");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strMarketQuoteField = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("ComponentQuote de-serializer: Cannot locate map of quotes");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3])) {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[3],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty()) continue;

					java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split
						(astrRecord[i], getCollectionKeyValueDelimiter());
				
					if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
						astrKVPair[0].isEmpty() || null == astrKVPair[1] || astrKVPair[1].isEmpty() ||
							NULL_SER_STRING.equals (astrKVPair[0]) || NULL_SER_STRING.equals (astrKVPair[1]))
						continue;

					_mapQuotes.put (astrKVPair[0], new org.drip.param.market.MultiSidedQuote
						(astrKVPair[1].getBytes()));
				}
			}
		}
	}

	@Override public void addQuote (
		final java.lang.String strQuoteField,
		final org.drip.param.definition.Quote q,
		final boolean bIsMarketQuote)
	{
		_mapQuotes.put (strQuoteField, q);

		if (bIsMarketQuote) {
			_mktQuote = q;
			_strMarketQuoteField = strQuoteField;
		}
	}

	@Override public boolean setMarketQuote (
		final java.lang.String strMarketQuoteField,
		final org.drip.param.definition.Quote q)
	{
		if (null == strMarketQuoteField || strMarketQuoteField.isEmpty() || null == q) return false;

		_strMarketQuoteField = strMarketQuoteField;
		_mktQuote = q;
		return true;
	}

	@Override public boolean removeMarketQuote()
	{
		_mktQuote = null;
		_strMarketQuoteField = "";
		return true;
	}

	@Override public org.drip.param.definition.Quote getQuote (
		final java.lang.String strQuoteField)
	{
		if (null == strQuoteField || strQuoteField.isEmpty()) return null;

		return _mapQuotes.get (strQuoteField);
	}

	@Override public org.drip.param.definition.Quote getMarketQuote()
	{
		return _mktQuote;
	}

	@Override public java.lang.String getMarketQuoteField()
	{
		return _strMarketQuoteField;
	}

	@Override public boolean removeQuote (final java.lang.String strQuoteField)
	{
		if (null == strQuoteField || strQuoteField.isEmpty()) return false;

		_mapQuotes.remove (strQuoteField);

		if (!_strMarketQuoteField.equalsIgnoreCase (strQuoteField)) return true;

		removeMarketQuote();

		return true;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "&";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "@";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _mktQuote)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_mktQuote.serialize()) + getFieldDelimiter());

		if (null == _strMarketQuoteField || _strMarketQuoteField.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strMarketQuoteField + getFieldDelimiter());

		if (null == _mapQuotes || 0 == _mapQuotes.size())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbQMap = new java.lang.StringBuffer();

			for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.Quote> me :
				_mapQuotes.entrySet()) {
				if (null == me) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbQMap.append (getCollectionRecordDelimiter());

				sbQMap.append (me.getKey() + getCollectionKeyValueDelimiter() + new java.lang.String
					(me.getValue().serialize()));
			}

			if (sbQMap.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbQMap.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ComponentMultiMeasureQuote (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		ComponentMultiMeasureQuote cq = new ComponentMultiMeasureQuote();

		cq.addQuote ("Price", new MultiSidedQuote ("ASK", 103.), false);

		cq.setMarketQuote ("SpreadToTsyBmk", new MultiSidedQuote ("MID", 210.));

		byte[] abCQ = cq.serialize();

		System.out.println (new java.lang.String (abCQ));

		ComponentMultiMeasureQuote cqDeser = (ComponentMultiMeasureQuote) cq.deserialize (abCQ);

		System.out.println (new java.lang.String (cqDeser.serialize()));
	}
}
