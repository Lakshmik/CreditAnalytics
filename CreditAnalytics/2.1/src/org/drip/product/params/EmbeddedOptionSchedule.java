
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
 * This class is a place holder for the embedded option schedule for the component. Contains the schedule of
 * 	exercise dates and factors, the exercise notice period, and the option is to call or put. Further, if the
 * 	option is of the type fix-to-float on exercise, contains the post-exercise floater index and floating
 * 	spread. If the exercise is not discrete (American option), the exercise dates/factors are discretized
 * 	according to a pre-specified discretization grid.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class EmbeddedOptionSchedule extends org.drip.service.stream.Serializer {
	public static final int CALL_NOTICE_PERIOD_DEFAULT = 30;

	private boolean _bIsPut = false;
	private double _adblDate[] = null;
	private double _adblFactor[] = null;
	private java.lang.String _strFloatIndex = "";
	private boolean _bFixToFloatOnExercise = false;
	private int _iNoticePeriod = CALL_NOTICE_PERIOD_DEFAULT;
	private double _dblFixToFloatSpread = java.lang.Double.NaN;
	private double _dblFixToFloatExerciseDate = java.lang.Double.NaN;

	/**
	 * Creates the EOS from the dates/factors string arrays
	 * 
	 * @param strDates String representing the date array
	 * @param strFactors String representing the factor array
	 * @param iNoticePeriod Exercise Notice Period
	 * @param bIsPut True (Put), False (Call)
	 * @param bIsDiscrete True (Discrete), False (Continuous)
	 * @param dblScheduleStart Schedule start Date
	 * @param bFixToFloatOnExercise True => component becomes a floater on call
	 * @param dblFixToFloatExerciseDate Date at which the fix to float conversion happens
	 * @param strFloatIndex Floater Rate Index
	 * @param dblFixToFloatSpread Floater Spread
	 * 
	 * @return EOS object
	 */

	public static final EmbeddedOptionSchedule CreateFromDateFactorSet (
		final java.lang.String strDates,
		final java.lang.String strFactors,
		final int iNoticePeriod,
		final boolean bIsPut,
		final boolean bIsDiscrete,
		final double dblScheduleStart,
		final boolean bFixToFloatOnExercise,
		final double dblFixToFloatExerciseDate,
		final java.lang.String strFloatIndex,
		final double dblFixToFloatSpread)
	{
		if (null == strDates || strDates.isEmpty() || null == strFactors || strFactors.isEmpty() ||
			!org.drip.math.common.NumberUtil.IsValid (dblScheduleStart))
			return null;

		if (bIsDiscrete) {
			try {
				return new EmbeddedOptionSchedule
					(org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
						java.util.StringTokenizer (strDates, ";")),
							org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
								java.util.StringTokenizer (strFactors, ";")), bIsPut, iNoticePeriod,
									bFixToFloatOnExercise, dblFixToFloatExerciseDate, strFloatIndex,
										dblFixToFloatSpread);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		return fromAmerican (dblScheduleStart,
			org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
				java.util.StringTokenizer (strDates, ";")),
					org.drip.analytics.support.GenericUtil.MakeDoubleArrayFromStringTokenizer (new
						java.util.StringTokenizer (strFactors, ";")), bIsPut, iNoticePeriod,
							bFixToFloatOnExercise, dblFixToFloatExerciseDate, strFloatIndex,
								dblFixToFloatSpread);
	}

	/**
	 * Creates the discretized American EOS schedule from the array of dates and factors
	 * 
	 * @param dblValDate Valuation Date - date to which the component is assumed to not have been exercised
	 * @param adblDate Array of dates
	 * @param adblFactor Matched Array of Factors
	 * @param bIsPut True (Put), False (Call)
	 * @param iNoticePeriod Exercise Notice Period
	 * @param bFixToFloatOnExercise True => component becomes a floater on call
	 * @param dblFixToFloatExerciseDate Date at which the fix to float conversion happens
	 * @param strFloatIndex Floater Rate Index
	 * @param dblFixToFloatSpread Floater Spread
	 * 
	 * @return Discretized EOS
	 */

	public static final EmbeddedOptionSchedule fromAmerican (
		final double dblValDate,
		final double adblDate[],
		final double adblFactor[],
		final boolean bIsPut,
		final int iNoticePeriod,
		final boolean bFixToFloatOnExercise,
		final double dblFixToFloatExerciseDate,
		final java.lang.String strFloatIndex,
		final double dblFixToFloatSpread)
	{
		if (null == adblDate || adblDate.length == 0 || null == adblFactor || adblFactor.length == 0 ||
			adblDate.length != adblFactor.length)
			return null;

		int i = 0;
		int iCallDiscretization = 30;
		double dblScheduleStart = dblValDate;

		if (dblValDate < adblDate[0]) dblScheduleStart = adblDate[0];

		java.util.ArrayList<java.lang.Double> ldblCallDates = new java.util.ArrayList<java.lang.Double>();

		java.util.ArrayList<java.lang.Double> ldblCallFactors = new java.util.ArrayList<java.lang.Double>();

		for (; i < adblDate.length; ++i) {
			double dblCallDate = dblScheduleStart;

			if (0 != i) dblCallDate = adblDate[i - 1];

			while (dblCallDate <= adblDate[i]) {
				ldblCallDates.add (dblCallDate);

				ldblCallFactors.add (adblFactor[i]);

				dblCallDate += iCallDiscretization;
			}
		}

		double[] adblEOSDate = new double[ldblCallDates.size()];

		i = 0;

		for (double dblCallDate : ldblCallDates)
			adblEOSDate[i++] = dblCallDate;

		double[] adblEOSFactor = new double[ldblCallFactors.size()];

		i = 0;

		for (double dblCallFactor : ldblCallFactors)
			adblEOSFactor[i++] = dblCallFactor;

		try {
			return new EmbeddedOptionSchedule (adblEOSDate, adblEOSFactor, bIsPut, iNoticePeriod,
				bFixToFloatOnExercise, dblFixToFloatExerciseDate, strFloatIndex, dblFixToFloatSpread);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Constructs the EOS from the array of dates and factors
	 * 
	 * @param adblDate Array of dates
	 * @param adblFactor Matched Array of Factors
	 * @param bIsPut True (Put), False (Call)
	 * @param iNoticePeriod Exercise Notice Period
	 * @param bFixToFloatOnExercise True => component becomes a floater on call
	 * @param dblFixToFloatExerciseDate Date at which the fix to float conversion happens
	 * @param strFloatIndex Floater Rate Index
	 * @param dblFixToFloatSpread Floater Spread
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public EmbeddedOptionSchedule (
		final double[] adblDate,
		final double[] adblFactor,
		final boolean bIsPut,
		final int iNoticePeriod,
		final boolean bFixToFloatOnExercise,
		final double dblFixToFloatExerciseDate,
		final java.lang.String strFloatIndex,
		final double dblFixToFloatSpread)
		throws java.lang.Exception
	{
		if (null == adblDate || null == adblFactor || adblDate.length != adblFactor.length)
			throw new java.lang.Exception ("Invalid EOS ctr params");

		_adblDate = new double[adblDate.length];
		_adblFactor = new double[adblFactor.length];

		for (int i = 0; i < _adblDate.length; ++i)
			_adblDate[i] = adblDate[i];

		for (int i = 0; i < _adblFactor.length; ++i)
			_adblFactor[i] = adblFactor[i];

		_bIsPut = bIsPut;
		_iNoticePeriod = iNoticePeriod;
		_strFloatIndex = strFloatIndex;
		_dblFixToFloatSpread = dblFixToFloatSpread;
		_bFixToFloatOnExercise = bFixToFloatOnExercise;
		_dblFixToFloatExerciseDate = dblFixToFloatExerciseDate;
	}

	/**
	 * Constructs a Deep Copy EOS from another EOS
	 * 
	 * @param eosOther The Other EOS
	 */

	public EmbeddedOptionSchedule (
		final EmbeddedOptionSchedule eosOther)
	{
		_adblDate = new double[eosOther._adblDate.length];
		_adblFactor = new double[eosOther._adblFactor.length];

		for (int i = 0; i < _adblDate.length; ++i)
			_adblDate[i] = eosOther._adblDate[i];

		for (int i = 0; i < _adblFactor.length; ++i)
			_adblFactor[i] = eosOther._adblFactor[i];

		_bIsPut = eosOther._bIsPut;
		_iNoticePeriod = eosOther._iNoticePeriod;
		_strFloatIndex = eosOther._strFloatIndex;
		_dblFixToFloatSpread = eosOther._dblFixToFloatSpread;
		_bFixToFloatOnExercise = eosOther._bFixToFloatOnExercise;
		_dblFixToFloatExerciseDate = eosOther._dblFixToFloatExerciseDate;
	}

	/**
	 * EmbeddedOptionSchedule de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if EmbeddedOptionSchedule cannot be properly de-serialized
	 */

	public EmbeddedOptionSchedule (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Empty state");

		java.lang.String strSerializedEmbeddedOptionSchedule = strRawString.substring (0,
			strRawString.indexOf (getObjectTrailer()));

		if (null == strSerializedEmbeddedOptionSchedule || strSerializedEmbeddedOptionSchedule.isEmpty())
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedEmbeddedOptionSchedule, getFieldDelimiter());

		if (null == astrField || 8 > astrField.length)
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot locate notice period");

		_iNoticePeriod = new java.lang.Integer (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Cannot locate Put flag");

		_bIsPut = new java.lang.Boolean (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot locate fix-to-float-on-exercise flag");

		_bFixToFloatOnExercise = new java.lang.Boolean (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot locate fix-to-float-on-exercise flag");

		_dblFixToFloatSpread = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot locate fix-to-float exercise date");

		_dblFixToFloatExerciseDate = new java.lang.Double (astrField[5]).doubleValue();

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot locate float index");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			_strFloatIndex = "";
		else
			_strFloatIndex = astrField[6];

		if (null == astrField[7] || astrField[7].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			throw new java.lang.Exception ("EmbeddedOptionSchedule de-serializer: Cannot decode state");

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblFactor = new java.util.ArrayList<java.lang.Double>();

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblFactor,
			astrField[7], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot decode hazard state");

		if (0 == lsdblDate.size() || 0 == lsdblFactor.size() || lsdblDate.size() != lsdblFactor.size())
			throw new java.lang.Exception
				("EmbeddedOptionSchedule de-serializer: Cannot decode hazard state");

		_adblDate = new double[lsdblDate.size()];

		_adblFactor = new double[lsdblFactor.size()];

		for (int i = 0; i < _adblFactor.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			_adblFactor[i] = lsdblFactor.get (i);
		}
	}

	/**
	 * Whether the component is putable or callable
	 * 
	 * @return True (Put), False (Call)
	 */

	public boolean isPut()
	{
		return _bIsPut;
	}

	/**
	 * Gets the array of dates
	 * 
	 * @return The array of dates
	 */

	public double[] getDates()
	{
		return _adblDate;
	}

	/**
	 * Gets the array of factors
	 * 
	 * @return The array of factors
	 */

	public double[] getFactors()
	{
		return _adblFactor;
	}

	/**
	 * Gets the specific indexed factor
	 * 
	 * @param iIndex Factor index
	 * 
	 * @return Factor corresponding to the index
	 */

	public double getFactor (final int iIndex)
	{
		return _adblFactor[iIndex];
	}

	/**
	 * Retrieves the exercise notice period
	 * 
	 * @return Minimum Exercise Notice Period in Days
	 */

	public int getExerciseNoticePeriod()
	{
		return _iNoticePeriod;
	}

	/**
	 * Returns whether the component is fix to float on exercise
	 * 
	 * @return True (component becomes a floater on call), False (component does not change)
	 */

	public boolean isFixToFloatOnExercise()
	{
		return _bFixToFloatOnExercise;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _iNoticePeriod +
			getFieldDelimiter() + _bIsPut + getFieldDelimiter() + _bFixToFloatOnExercise +
				getFieldDelimiter() + _dblFixToFloatSpread + getFieldDelimiter() + _dblFixToFloatExerciseDate
					+ getFieldDelimiter());

		if (null == _strFloatIndex || _strFloatIndex.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strFloatIndex + getFieldDelimiter());

		if (null == _adblDate || 0 == _adblDate.length || null == _adblFactor || 0 == _adblFactor.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			java.lang.StringBuffer sbEOS = new java.lang.StringBuffer();

			for (int i = 0; i < _adblDate.length; ++i) {
				if (0 != i) sbEOS.append (getCollectionRecordDelimiter());

				sbEOS.append (_adblDate[i] + getCollectionKeyValueDelimiter() + _adblFactor[i]);
			}

			if (sbEOS.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbEOS.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new EmbeddedOptionSchedule (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblScheduleStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblFactor = new double[3];
		adblFactor[0] = 1.20;
		adblFactor[1] = 1.10;
		adblFactor[2] = 1.00;
		adblDate[0] = dblScheduleStart + 30.;
		adblDate[1] = dblScheduleStart + 396.;
		adblDate[2] = dblScheduleStart + 761.;

		EmbeddedOptionSchedule eos = EmbeddedOptionSchedule.fromAmerican (dblScheduleStart, adblDate,
			adblFactor, true, 30, false, java.lang.Double.NaN, "CRAP", java.lang.Double.NaN);

		byte[] abEOS = eos.serialize();

		System.out.println (new java.lang.String (abEOS));

		EmbeddedOptionSchedule eosDeser = new EmbeddedOptionSchedule (abEOS);

		System.out.println (new java.lang.String (eosDeser.serialize()));
	}
}
