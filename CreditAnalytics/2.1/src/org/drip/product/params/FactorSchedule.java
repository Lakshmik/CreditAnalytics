
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
 * Contains array of dates and factors
 *
 * @author Lakshmi Krishnamurthy
 */

public class FactorSchedule extends org.drip.service.stream.Serializer {
	private double _adblDate[] = null;
	private double _adblFactor[] = null;

	/**
	 * Creates the factor schedule from a matched string array of dates and factors
	 * 
	 * @param strDates String array of dates
	 * @param strFactors String array of Factors
	 * 
	 * @return FactorSchedule object
	 */

	public static final FactorSchedule CreateFromDateFactorSet (
		final java.lang.String strDates,
		final java.lang.String strFactors)
	{
		if (null == strDates || strDates.isEmpty() || null == strFactors || strFactors.isEmpty())
			return null;

		try {
			return new FactorSchedule
				(org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
					java.util.StringTokenizer (strDates, ";")),
						org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
							java.util.StringTokenizer (strFactors, ";")));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the factor schedule from a matched array of dates and factors
	 * 
	 * @param adblDate Array of dates
	 * @param adblFactor Array of Factors
	 * 
	 * @return FactorSchedule object
	 */

	public static final FactorSchedule CreateFromDateFactorArray (
		final double[] adblDate,
		final double[] adblFactor)
	{
		try {
			return new FactorSchedule (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the factor schedule from a matched array of dates and factor deltas
	 * 
	 * @param adblDate Array of dates
	 * @param adblFactorDelta Array of Factor Deltas
	 * 
	 * @return FactorSchedule object
	 */

	public static final FactorSchedule CreateFromDateFactorDeltaArray (
		final double[] adblDate,
		final double[] adblFactorDelta)
	{
		if (null == adblDate || 0 == adblDate.length || null == adblFactorDelta || 0 ==
			adblFactorDelta.length || adblDate.length != adblFactorDelta.length)
			return null;

		double[] adblFactor = new double[adblFactorDelta.length];

		int i = 0;
		adblFactor[0] = 1.;

		for (double dblFactorDelta : adblFactorDelta) {
			if (i < adblFactorDelta.length - 1) adblFactor[i + 1] = adblFactor[i] - dblFactorDelta;

			++i;
		}

		try {
			return new FactorSchedule (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates factor schedule of flat unit notional
	 * 
	 * @return FactorSchedule object
	 */

	public static final FactorSchedule CreateBulletSchedule()
	{
		double[] adblDate = new double[1];
		double[] adblFactor = new double[1];
		adblFactor[0] = 1.;

		adblDate[0] = org.drip.analytics.date.JulianDate.CreateFromYMD (1900, 1, 1).getJulian();

		try {
			return new FactorSchedule (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private FactorSchedule (
		final double[] adblDate,
		final double[] adblFactor)
		throws java.lang.Exception
	{
		if (null == adblDate || 0 == adblDate.length || null == adblFactor || 0 == adblFactor.length ||
			adblDate.length != adblFactor.length)
			throw new java.lang.Exception ("Invalid FactorSchedule ctr params");

		_adblDate = new double[adblDate.length];
		_adblFactor = new double[adblFactor.length];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = adblDate[i];
			_adblFactor[i] = adblFactor[i];
		}
	}

	/**
	 * FactorSchedule de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FactorSchedule cannot be properly de-serialized
	 */

	public FactorSchedule (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FactorSchedule de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FactorSchedule de-serializer: Empty state");

		java.lang.String strSerializedFactorSchedule = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFactorSchedule || strSerializedFactorSchedule.isEmpty())
			throw new java.lang.Exception ("FactorSchedule de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedFactorSchedule, getFieldDelimiter());

		if (null == astrField || 2 > astrField.length)
			throw new java.lang.Exception ("FactorSchedule de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FactorSchedule de-serializer: Cannot decode state");

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblFactor = new java.util.ArrayList<java.lang.Double>();

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblFactor,
			astrField[1], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("FactorSchedule de-serializer: Cannot decode hazard state");

		if (0 == lsdblDate.size() || 0 == lsdblFactor.size() || lsdblDate.size() != lsdblFactor.size())
			throw new java.lang.Exception ("FactorSchedule de-serializer: Cannot decode hazard state");

		_adblDate = new double[lsdblDate.size()];

		_adblFactor = new double[lsdblFactor.size()];

		for (int i = 0; i < _adblFactor.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			_adblFactor[i] = lsdblFactor.get (i);
		}
	}

	/**
	 * Retrieves the notional factor for a given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Notional factor 
	 * 
	 * @throws java.lang.Exception Thrown if the notional cannot be computed
	 */

	public double getFactor (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("FactorSchedule.getFactor input NaN");

		if (dblDate <= _adblDate[0]) return _adblFactor[0];

		for (int i = 1; i < _adblDate.length; ++i) {
			if (dblDate > _adblDate[i - 1] && dblDate <= _adblDate[i]) return _adblFactor[i];
		}

		return _adblFactor[_adblDate.length - 1];
	}

	/**
	 * Retrieves the index that corresponds to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Index 
	 * 
	 * @throws java.lang.Exception Thrown if the index cannot be computed
	 */

	public int getIndex (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("FactorSchedule.getIndex input/state invalid");

		if (dblDate <= _adblDate[0]) return 0;

		for (int i = 1; i < _adblDate.length; ++i) {
			if (dblDate <= _adblDate[i]) return i;
		}

		return _adblDate.length - 1;
	}

	/**
	 * Retrieves the time-weighted notional factor between 2 dates
	 * 
	 * @param dblStartDate Start Date
	 * @param dblEndDate End Date
	 * 
	 * @return Notional factor 
	 * 
	 * @throws java.lang.Exception Thrown if the notional cannot be computed
	 */

	public double getFactor (
		final double dblStartDate,
		final double dblEndDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStartDate) ||
			!org.drip.math.common.NumberUtil.IsValid (dblEndDate))
			throw new java.lang.Exception ("Invalid inputs into FactorSchedule.getFactor");

		int iEndIndex = getIndex (dblEndDate);

		int iStartIndex = getIndex (dblStartDate);

		if (iStartIndex == iEndIndex) return _adblFactor[iStartIndex];

		double dblWeightedFactor = _adblFactor[iStartIndex] * (_adblDate[iStartIndex] - dblStartDate);

		for (int i = iStartIndex + 1; i <= iEndIndex; ++i)
			dblWeightedFactor += _adblFactor[i] * (_adblDate[i] - _adblDate[i - 1]);

		return (dblWeightedFactor + _adblFactor[iEndIndex] * (dblEndDate - _adblDate[iEndIndex])) /
			(dblEndDate - dblStartDate);
	}

	/**
	 * Retrieves the array of dates
	 * 
	 * @return Double array of JulianDate
	 */

	public double[] getDates()
	{
		return _adblDate;
	}

	/**
	 * Retrieves the array of notional factors
	 * 
	 * @return Double array of notional factors
	 */

	public double[] getFactors()
	{
		return _adblFactor;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "`";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "~";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _adblDate || 0 == _adblDate.length || null == _adblFactor || 0 == _adblFactor.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			for (int i = 0; i < _adblDate.length; ++i) {
				if (0 != i) sb.append (getCollectionRecordDelimiter());

				sb.append (_adblDate[i] + getCollectionKeyValueDelimiter() + _adblFactor[i]);
			}
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new FactorSchedule (ab);
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
		double[] adblFactor = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblDate[i] = dblStart + 365. * (i + 1);
			adblFactor[i] = 1 - 0.1 * i;
		}

		FactorSchedule fs = CreateFromDateFactorArray (adblDate, adblFactor);

		byte[] abFS = fs.serialize();

		System.out.println (new java.lang.String (abFS));

		FactorSchedule fsDeser = new FactorSchedule (abFS);

		System.out.println (new java.lang.String (fsDeser.serialize()));
	}
}
