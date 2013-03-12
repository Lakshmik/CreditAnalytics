
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
 * This class provides implementation of the ComponentMarketParamsRef interface. Serves as a place holder for
 * 		the market parameters needed to value the component object – discount curve, treasury curve, EDSF
 * 		curve, credit curve, component quote, treasury quote map, and fixings map.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ComponentMarketParamSet extends org.drip.param.definition.ComponentMarketParams {
	/*
	 * Credit Curve
	 */

	private org.drip.analytics.definition.CreditCurve _cc = null;

	/*
	 * Rates Discount Curve
	 */

	private org.drip.analytics.definition.DiscountCurve _dc = null;

	/*
	 * Treasury Discount Curve
	 */

	private org.drip.analytics.definition.DiscountCurve _dcTSY = null;

	/*
	 * EDSF Discount Curve
	 */

	private org.drip.analytics.definition.DiscountCurve _dcEDSF = null;

	/*
	 * Component Quote
	 */

	private org.drip.param.definition.ComponentQuote _compQuote = null;

	/*
	 * Map of Treasury Benchmark Quotes
	 */

	private java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> _mTSYQuotes = null;

	/*
	 * Double map of date/rate index and fixings
	 */

	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixings = null;

	/**
	 * Creates a CMP with the rates discount curve, the treasury discount curve, the EDSF discount curve, the
	 * 	credit curve, the component quote, the map of treasury benchmark quotes, and the double map of
	 * 	date/rate index and fixings
	 * 
	 * @param dc Rates Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param cc Credit Curve
	 * @param compQuote Component quote
	 * @param mTSYQuotes Map of Treasury Benchmark Quotes
	 * @param mmFixings Double map of date/rate index and fixings
	 */

	public ComponentMarketParamSet (
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.analytics.definition.CreditCurve cc,
		final org.drip.param.definition.ComponentQuote compQuote,
		final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mTSYQuotes,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings)
	{
		_cc = cc;
		_dc = dc;
		_dcTSY = dcTSY;
		_dcEDSF = dcEDSF;
		_compQuote = compQuote;
		_mmFixings = mmFixings;
		_mTSYQuotes = mTSYQuotes;
	}

	/**
	 * ComponentMarketParams de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CreditCurve cannot be properly de-serialized
	 */

	public ComponentMarketParamSet (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Empty state");

		java.lang.String strSerializedComponentMarketParams = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedComponentMarketParams || strSerializedComponentMarketParams.isEmpty())
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedComponentMarketParams, getFieldDelimiter());

		if (null == astrField || 8 > astrField.length)
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception
				("ComponentMarketParams de-serializer: Cannot locate credit curve");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_cc = org.drip.analytics.creator.CreditCurveBuilder.FromByteArray (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception
				("ComponentMarketParams de-serializer: Cannot locate discount curve");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_dc = org.drip.analytics.creator.DiscountCurveBuilder.FromByteArray (astrField[2].getBytes(),
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception
				("ComponentMarketParams de-serializer: Cannot locate TSY discount curve");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_dcTSY = org.drip.analytics.creator.DiscountCurveBuilder.FromByteArray (astrField[3].getBytes(),
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception
				("ComponentMarketParams de-serializer: Cannot locate EDSF discount curve");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_dcEDSF = org.drip.analytics.creator.DiscountCurveBuilder.FromByteArray (astrField[4].getBytes(),
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception
				("ComponentMarketParams de-serializer: Cannot locate component quote");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			_compQuote = new org.drip.param.market.ComponentMultiMeasureQuote (astrField[5].getBytes());

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Cannot locate fixings");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6])) {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[6],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty()) continue;

					java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split
						(astrRecord[i], getCollectionKeyValueDelimiter());
					
					if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
						astrKVPair[0].isEmpty() ||
							org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
								(astrKVPair[0]) || null == astrKVPair[1] || astrKVPair[1].isEmpty() ||
									org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
										(astrKVPair[1]))
						continue;

					java.lang.String[] astrKeySet = org.drip.analytics.support.GenericUtil.Split
						(astrKVPair[0], getCollectionMultiLevelKeyDelimiter());

					if (null == astrKeySet || 2 != astrKeySet.length || null == astrKeySet[0] ||
						astrKeySet[0].isEmpty() ||
							org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
								(astrKeySet[0]) || null == astrKeySet[1] || astrKeySet[1].isEmpty() ||
									org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
										(astrKeySet[1]))
						continue;

					if (null == _mmFixings)
						_mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
							java.util.Map<java.lang.String, java.lang.Double>>();

					java.util.Map<java.lang.String, java.lang.Double> map2D = _mmFixings.get (astrKeySet[0]);

					if (null == map2D) map2D = new java.util.HashMap<java.lang.String, java.lang.Double>();

					map2D.put (astrKeySet[1], new java.lang.Double (astrKVPair[1]));

					_mmFixings.put (new org.drip.analytics.date.JulianDate (new java.lang.Double
						(astrKeySet[0])), map2D);
				}
			}
		}

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("ComponentMarketParams de-serializer: Cannot locate TSY quotes");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7])) {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[7],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split
						(astrRecord[i], getCollectionKeyValueDelimiter());
				
					if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
						astrKVPair[0].isEmpty() ||
							org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
								(astrKVPair[0]) || null == astrKVPair[1] || astrKVPair[1].isEmpty() ||
									org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
										(astrKVPair[1]))
						continue;

					if (null == _mTSYQuotes)
						_mTSYQuotes = new java.util.HashMap<java.lang.String,
							org.drip.param.definition.ComponentQuote>();

					_mTSYQuotes.put (astrKVPair[0], new org.drip.param.market.ComponentMultiMeasureQuote
						(astrKVPair[1].getBytes()));
				}
			}
		}
	}

	@Override public java.lang.String getCollectionKeyValueDelimiter()
	{
		return "]";
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "[";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "~";
	}

	@Override public org.drip.analytics.definition.CreditCurve getCreditCurve()
	{
		return _cc;
	}

	@Override public boolean setCreditCurve (
		final org.drip.analytics.definition.CreditCurve cc)
	{
		if (null == cc) return false;

		_cc = cc;
		return true;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getDiscountCurve()
	{
		return _dc;
	}

	@Override public boolean setDiscountCurve (
		final org.drip.analytics.definition.DiscountCurve dc)
	{
		if (null == dc) return false;

		_dc = dc;
		return true;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getTSYDiscountCurve()
	{
		return _dcTSY;
	}

	@Override public boolean setTSYDiscountCurve (
		final org.drip.analytics.definition.DiscountCurve dcTSY)
	{
		if (null == dcTSY) return false;

		_dcTSY = dcTSY;
		return true;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getEDSFDiscountCurve()
	{
		return _dcEDSF;
	}

	@Override public boolean setEDSFDiscountCurve (
		final org.drip.analytics.definition.DiscountCurve dcEDSF)
	{
		if (null == dcEDSF) return false;

		_dcEDSF = dcEDSF;
		return true;
	}

	@Override public org.drip.param.definition.ComponentQuote getComponentQuote()
	{
		return _compQuote;
	}

	@Override public boolean setComponentQuote (
		final org.drip.param.definition.ComponentQuote compQuote)
	{
		_compQuote = compQuote;
		return true;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote>
		getTSYBenchmarkQuotes()
	{
		return _mTSYQuotes;
	}

	@Override public java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getFixings()
	{
		return _mmFixings;
	}

	@Override public boolean setFixings (
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings)
	{
		_mmFixings = mmFixings;
		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _cc)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_cc.serialize()) + getFieldDelimiter());

		if (null == _dc)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_dc.serialize()) + getFieldDelimiter());

		if (null == _dcTSY)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_dcTSY.serialize()) + getFieldDelimiter());

		if (null == _dcEDSF)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_dcEDSF.serialize()) + getFieldDelimiter());

		if (null == _compQuote)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_compQuote.serialize()) + getFieldDelimiter());

		if (null == _mmFixings || null == _mmFixings.entrySet())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbFixings = new java.lang.StringBuffer();

			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
				java.lang.Double>> meOut : _mmFixings.entrySet()) {
				if (null == meOut || null == meOut.getValue() || null == meOut.getValue().entrySet())
					continue;

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> meIn :
					meOut.getValue().entrySet()) {
					if (null == meIn || null == meIn.getKey() || meIn.getKey().isEmpty()) continue;

					if (bFirstEntry)
						bFirstEntry = false;
					else
						sb.append (getCollectionRecordDelimiter());

					sbFixings.append (meOut.getKey().getJulian() + getCollectionMultiLevelKeyDelimiter() +
						meIn.getKey() + getCollectionKeyValueDelimiter() + meIn.getValue());
				}
			}

			if (sbFixings.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbFixings.toString() + getFieldDelimiter());
		}

		if (null == _mTSYQuotes || 0 == _mTSYQuotes.size())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbMapTSYQuotes = new java.lang.StringBuffer();

			for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.ComponentQuote> me :
				_mTSYQuotes.entrySet()) {
				if (null == me || null == me.getKey() || me.getKey().isEmpty()) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbMapTSYQuotes.append (getCollectionRecordDelimiter());

				sbMapTSYQuotes.append (me.getKey() + getCollectionKeyValueDelimiter() + new java.lang.String
					(me.getValue().serialize()));
			}

			if (!sbMapTSYQuotes.toString().isEmpty()) sb.append (sbMapTSYQuotes);
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ComponentMarketParamSet (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblRate = new double[3];
		double[] adblRateTSY = new double[3];
		double[] adblRateEDSF = new double[3];
		double[] adblHazardRate = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblDate[i] = dblStart + 365. * (i + 1);
			adblRate[i] = 0.015 * (i + 1);
			adblRateTSY[i] = 0.01 * (i + 1);
			adblRateEDSF[i] = 0.0125 * (i + 1);
			adblHazardRate[i] = 0.01 * (i + 1);
		}

		org.drip.analytics.definition.DiscountCurve dc =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateDC
				(org.drip.analytics.date.JulianDate.Today(), "ABC", adblDate, adblRate,
					org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.definition.DiscountCurve dcTSY =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateDC
				(org.drip.analytics.date.JulianDate.Today(), "ABCTSY", adblDate, adblRateTSY,
					org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.definition.DiscountCurve dcEDSF =
			org.drip.analytics.creator.DiscountCurveBuilder.CreateDC
				(org.drip.analytics.date.JulianDate.Today(), "ABCEDSF", adblDate, adblRateEDSF,
					org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.analytics.creator.CreditCurveBuilder.CreateCreditCurve
				(org.drip.analytics.date.JulianDate.Today(), "ABCSOV", adblDate, adblHazardRate, 0.40);

		org.drip.param.market.ComponentMultiMeasureQuote cq = new
			org.drip.param.market.ComponentMultiMeasureQuote();

		cq.addQuote ("Price", new org.drip.param.market.MultiSidedQuote ("ASK", 103.), false);

		cq.setMarketQuote ("SpreadToTsyBmk", new org.drip.param.market.MultiSidedQuote ("MID", 210.));

		java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mapTSYQuotes = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentQuote>();

		mapTSYQuotes.put ("TSY2ON", cq);

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0042);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (org.drip.analytics.date.JulianDate.Today().addDays (2), mIndexFixings);

		ComponentMarketParamSet cmp = new ComponentMarketParamSet (dc, dcTSY, dcEDSF, cc, cq, mapTSYQuotes,
			mmFixings);

		byte[] abCMP = cmp.serialize();

		System.out.println (new java.lang.String (abCMP));

		ComponentMarketParamSet cmpDeser = new ComponentMarketParamSet (abCMP);

		System.out.println (new java.lang.String (cmpDeser.serialize()));
	}
}
