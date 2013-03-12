
package org.drip.analytics.period;

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
 * This class is an enhancement of the period class using the following period measures: start/end survival
 * 		probabilities, start/end notionals, and period start/end discount factor
 *
 * @author Lakshmi Krishnamurthy
 */

public class CouponPeriodCurveFactors extends Period {
	protected double _dblEndDF = java.lang.Double.NaN;
	protected double _dblSpread = java.lang.Double.NaN;
	protected double _dblStartDF = java.lang.Double.NaN;
	protected double _dblIndexRate = java.lang.Double.NaN;
	protected double _dblEndNotional = java.lang.Double.NaN;
	protected double _dblEndSurvival = java.lang.Double.NaN;
	protected double _dblStartNotional = java.lang.Double.NaN;
	protected double _dblStartSurvival = java.lang.Double.NaN;
	protected double _dblFullCouponRate = java.lang.Double.NaN;

	/**
	 * Constructs the CouponPeriodCurveFactors class using the corresponding period curve measures.
	 * 
	 * @param dblStart Period Start date
	 * @param dblEnd Period end date
	 * @param dblAccrualStart Period accrual Start date
	 * @param dblAccrualEnd Period Accrual End date
	 * @param dblPay Period Pay date
	 * @param dblDCF Period day count fraction
	 * @param dblFullCouponRate Period Full (i.e., annualized Coupon Rate
	 * @param dblStartNotional Period Start Notional
	 * @param dblEndNotional Period End Notional
	 * @param dblStartDF Period Start discount factor
	 * @param dblEndDF Period End discount factor
	 * @param dblStartSurvival Period Start Survival
	 * @param dblEndSurvival Period End Survival
	 * @param dblSpread Period floater spread (Optional)
	 * @param dblIndexRate Period floating reference rate (Optional)
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public CouponPeriodCurveFactors (
		final double dblStart,
		final double dblEnd,
		final double dblAccrualStart,
		final double dblAccrualEnd,
		final double dblPay,
		final double dblDCF,
		final double dblFullCouponRate,
		final double dblStartNotional,
		final double dblEndNotional,
		final double dblStartDF,
		final double dblEndDF,
		final double dblStartSurvival,
		final double dblEndSurvival,
		final double dblSpread,
		final double dblIndexRate)
		throws java.lang.Exception
	{
		super (dblStart, dblEnd, dblAccrualStart, dblAccrualEnd, dblPay, dblDCF);

		if (!org.drip.math.common.NumberUtil.IsValid (_dblFullCouponRate = dblFullCouponRate) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblStartNotional = dblStartNotional) ||
				!org.drip.math.common.NumberUtil.IsValid (_dblEndNotional = dblEndNotional) ||
					!org.drip.math.common.NumberUtil.IsValid (_dblStartDF = dblStartDF) ||
						!org.drip.math.common.NumberUtil.IsValid (_dblEndDF = dblEndDF) ||
							!org.drip.math.common.NumberUtil.IsValid (_dblStartSurvival = dblStartSurvival)
								|| !org.drip.math.common.NumberUtil.IsValid (_dblEndSurvival =
									dblEndSurvival))
			throw new java.lang.Exception ("Invalid params into CouponPeriodCurveFactors ctr");

		_dblSpread = dblSpread;
		_dblIndexRate = dblIndexRate;
	}

	/**
	 * De-serialization of CouponPeriodCurveFactors from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize CouponPeriodCurveFactors
	 */

	public CouponPeriodCurveFactors (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CouponPeriodCurveFactors de-serializer: Empty state");

		java.lang.String strCP = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strCP || strCP.isEmpty())
			throw new java.lang.Exception ("CouponPeriodCurveFactors de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strCP,
			getFieldDelimiter());

		if (null == astrField || 10 > astrField.length)
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serialize: Invalid number of fields");

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate full coupon rate");

		_dblFullCouponRate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate start notional");

		_dblStartNotional = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate end notional");

		_dblEndNotional = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate start survival");

		_dblStartSurvival = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate end survival");

		_dblEndSurvival = new java.lang.Double (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate start DF");

		_dblStartDF = new java.lang.Double (astrField[6]);

		if (null == astrField[7] || astrField[7].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate end DF");

		_dblEndDF = new java.lang.Double (astrField[7]);

		if (null == astrField[8] || astrField[8].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate floater spread");

		_dblSpread = new java.lang.Double (astrField[8]);

		if (null == astrField[9] || astrField[9].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			throw new java.lang.Exception
				("CouponPeriodCurveFactors de-serializer: Cannot locate index rate");

		_dblIndexRate = new java.lang.Double (astrField[9]);
	}

	/**
	 * Gets the period full coupon rate (annualized quote)
	 * 
	 * @return Period Full Coupon Rate
	 */

	public double getFullCouponRate()
	{
		return _dblFullCouponRate;
	}

	/**
	 * Gets the period spread over the floating index
	 * 
	 * @return Period Spread
	 */

	public double getSpread()
	{
		return _dblSpread;
	}

	/**
	 * Gets the period index rate
	 * 
	 * @return Period Index Reference Rate
	 */

	public double getIndexRate()
	{
		return _dblIndexRate;
	}

	/**
	 * Gets the period start Notional
	 * 
	 * @return Period Start Notional
	 */

	public double getStartNotional()
	{
		return _dblStartNotional;
	}

	/**
	 * Gets the period end Notional
	 * 
	 * @return Period end Notional
	 */

	public double getEndNotional()
	{
		return _dblEndNotional;
	}

	/**
	 * Gets the period end discount factor
	 * 
	 * @return Period end discount factor
	 */

	public double getEndDF()
	{
		return _dblEndDF;
	}

	/**
	 * Gets the period end survival probability
	 * 
	 * @return Period end survival probability
	 */

	public double getEndSurvival()
	{
		return _dblEndSurvival;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "^";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _dblFullCouponRate +
			getFieldDelimiter() + _dblStartNotional + getFieldDelimiter() + _dblEndNotional +
				getFieldDelimiter() + _dblStartSurvival + getFieldDelimiter() + _dblEndSurvival +
					getFieldDelimiter() + _dblStartDF + getFieldDelimiter() + _dblEndDF + getFieldDelimiter()
						+ _dblSpread + getFieldDelimiter() + _dblIndexRate);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}
	
	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		CouponPeriodCurveFactors pcpcm = new CouponPeriodCurveFactors (dblStart, dblStart + 180, dblStart,
			dblStart + 180, dblStart + 180, 0.5, 0.05, 1000000., 100000., 0.986, 0.97, 0.991, 0.98,
				java.lang.Double.NaN, java.lang.Double.NaN);

		byte[] abPCPCM = pcpcm.serialize();

		System.out.println ("Input: " + new java.lang.String (abPCPCM));

		CouponPeriodCurveFactors pcpcmDeser = new CouponPeriodCurveFactors (abPCPCM);

		System.out.println ("Output: " + new java.lang.String (pcpcmDeser.serialize()));
	}
}
