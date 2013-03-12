
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
 * 	This class implements the Quote interface, and contains the details corresponding to a product quote. It
 * 		contains the quote value, quote instant for the different quote sides (bid/ask/mid).
 *   
 * @author Lakshmi Krishnamurthy
 */

public class MultiSidedQuote extends org.drip.param.definition.Quote {
	protected class SidedQuote extends org.drip.service.stream.Serializer {
		private double _dblQuote = java.lang.Double.NaN;
		private org.drip.analytics.date.DateTime _dt = null;

		public SidedQuote (
			final double dblQuote)
			throws java.lang.Exception
		{
			if (!org.drip.math.common.NumberUtil.IsValid (_dblQuote = dblQuote))
				throw new java.lang.Exception ("Quote:SidedQuote cannot take NaN!");

			_dt = new org.drip.analytics.date.DateTime();
		}

		public SidedQuote (
			final byte[] ab)
			throws java.lang.Exception
		{
			if (null == ab || 0 == ab.length)
				throw new java.lang.Exception ("SidedQuote de-serialize: Invalid byte stream input");

			java.lang.String strRawString = new java.lang.String (ab);

			if (null == strRawString || strRawString.isEmpty())
				throw new java.lang.Exception ("SidedQuote de-serializer: Empty state");

			java.lang.String strSidedQuote = strRawString.substring (0, strRawString.indexOf
				(getObjectTrailer()));

			if (null == strSidedQuote || strSidedQuote.isEmpty())
				throw new java.lang.Exception ("SidedQuote de-serializer: Cannot locate state");

			java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSidedQuote,
				getFieldDelimiter());

			if (null == astrField || 3 > astrField.length)
				throw new java.lang.Exception ("SidedQuote de-serialize: Invalid number of fields");

			if (null == astrField[1] || astrField[1].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
				throw new java.lang.Exception ("SidedQuote de-serializer: Cannot locate DateTime");

			_dt = new org.drip.analytics.date.DateTime (astrField[1].getBytes());

			if (null == astrField[2] || astrField[2].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
				throw new java.lang.Exception ("SidedQuote de-serializer: Cannot locate Quote");

			_dblQuote = new java.lang.Double (astrField[2]);
		}

		public double getQuote()
		{
			return _dblQuote;
		}

		public org.drip.analytics.date.DateTime getQuoteTime()
		{
			return _dt;
		}

		public boolean setQuote (
			final double dblQuote)
		{
			if (!org.drip.math.common.NumberUtil.IsValid (dblQuote)) return false;

			_dblQuote = dblQuote;
			return true;
		}

		@Override public java.lang.String getFieldDelimiter()
		{
			return "%";
		}

		@Override public java.lang.String getObjectTrailer()
		{
			return "!";
		}

		@Override public byte[] serialize()
		{
			java.lang.StringBuffer sb = new java.lang.StringBuffer();

			sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + new
				java.lang.String (_dt.serialize()) + getFieldDelimiter() + _dblQuote);

			return sb.append (getObjectTrailer()).toString().getBytes();
		}

		@Override public org.drip.service.stream.Serializer deserialize (
			final byte[] ab) {
			try {
				return new SidedQuote (ab);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			return null;
		}
	};

	private java.util.Map<java.lang.String, SidedQuote> _mapSidedQuote = new
		java.util.HashMap<java.lang.String, SidedQuote>();

	/**
	 * Constructor: Constructs a Quote object from the quote value and the side string.
	 * 
	 * @param strSide bid/ask/mid
	 * @param dblQuote Quote Value
	 * 
	 * @throws java.lang.Exception Thrown on invalid inputs
	 */

	public MultiSidedQuote (
		final java.lang.String strSide,
		final double dblQuote)
		throws java.lang.Exception
	{
		if (null == strSide || strSide.isEmpty() || !org.drip.math.common.NumberUtil.IsValid (dblQuote))
			throw new java.lang.Exception ("Quote invalid Quote/Side!");

		_mapSidedQuote.put (strSide, new SidedQuote (dblQuote));
	}

	/**
	 * Quote de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if Quote cannot be properly de-serialized
	 */

	public MultiSidedQuote (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Quote de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("Quote de-serializer: Empty state");

		java.lang.String strSerializedQuote = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedQuote || strSerializedQuote.isEmpty())
			throw new java.lang.Exception ("Quote de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedQuote,
			getFieldDelimiter());

		if (null == astrField || 2 > astrField.length)
			throw new java.lang.Exception ("Quote de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]).doubleValue();

		if (null != astrField[1] && !astrField[1].isEmpty() &&
			!org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1])) {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[1],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				if (null == _mapSidedQuote)
					_mapSidedQuote = new java.util.HashMap<java.lang.String, SidedQuote>();

				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty()) continue;

					java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split
						(astrRecord[i], getCollectionKeyValueDelimiter());
					
					if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
						astrKVPair[0].isEmpty() || null == astrKVPair[1] || astrKVPair[1].isEmpty())
						continue;

					_mapSidedQuote.put (astrKVPair[0], new SidedQuote (astrKVPair[1].getBytes()));
				}
			}
		}
	}

	@Override public double getQuote (
		final java.lang.String strSide)
	{
		if (null == strSide || strSide.isEmpty()) return java.lang.Double.NaN;

		return _mapSidedQuote.get (strSide).getQuote();
	}

	@Override public org.drip.analytics.date.DateTime getQuoteTime (
		final java.lang.String strSide)
	{
		if (null == strSide || strSide.isEmpty()) return null;

		return _mapSidedQuote.get (strSide).getQuoteTime();
	}

	@Override public boolean setSide (
		final java.lang.String strSide,
		final double dblQuote)
	{
		if (null != strSide && !strSide.isEmpty() && !org.drip.math.common.NumberUtil.IsValid (dblQuote))
			return false;

		try {
			_mapSidedQuote.put (strSide, new SidedQuote (dblQuote));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "^";
	}

	@Override public java.lang.String getCollectionKeyValueDelimiter()
	{
		return "-";
	}

	@Override public java.lang.String getCollectionRecordDelimiter()
	{
		return "+";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _mapSidedQuote || 0 == _mapSidedQuote.size())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbSQMap = new java.lang.StringBuffer();

			for (java.util.Map.Entry<java.lang.String, SidedQuote> me : _mapSidedQuote.entrySet()) {
				if (null == me) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbSQMap.append (getCollectionRecordDelimiter());

				sbSQMap.append (me.getKey() + getCollectionKeyValueDelimiter() + new java.lang.String
					(me.getValue().serialize()));
			}

			if (sbSQMap.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbSQMap.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new MultiSidedQuote (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.param.definition.Quote q = new MultiSidedQuote ("ASK", 103.);

		byte[] abQuote = q.serialize();

		System.out.println (new java.lang.String (abQuote));

		org.drip.param.definition.Quote qDeser = (org.drip.param.definition.Quote) q.deserialize (abQuote);

		System.out.println (new java.lang.String (qDeser.serialize()));
	}
}
