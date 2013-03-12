
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
 * 
 * This class serves as a holder for the period dates: period start/end, period accrual start/end, pay, and
 * 	full period day count fraction.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Period extends org.drip.service.stream.Serializer {
	protected double _dblDCF = java.lang.Double.NaN;
	protected double _dblEnd = java.lang.Double.NaN;
	protected double _dblPay = java.lang.Double.NaN;
	protected double _dblStart = java.lang.Double.NaN;
	protected double _dblAccrualEnd = java.lang.Double.NaN;
	protected double _dblAccrualStart = java.lang.Double.NaN;

	/**
	 * Constructs a period object instance from the corresponding date parameters
	 * 
	 * @param dblStart Period Start Date
	 * @param dblEnd Period End Date
	 * @param dblAccrualStart Period Accrual Start Date
	 * @param dblAccrualEnd Period Accrual End Date
	 * @param dblPay Period Pay Date
	 * @param dblDCF Period Day count fraction
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public Period (
		final double dblStart,
		final double dblEnd,
		final double dblAccrualStart,
		final double dblAccrualEnd,
		final double dblPay,
		final double dblDCF)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStart) || !org.drip.math.common.NumberUtil.IsValid
			(dblEnd) || !org.drip.math.common.NumberUtil.IsValid (dblAccrualStart) ||
				!org.drip.math.common.NumberUtil.IsValid (dblAccrualEnd) ||
					!org.drip.math.common.NumberUtil.IsValid (dblDCF) || dblStart > dblEnd || dblAccrualStart
						> dblAccrualEnd) {
			System.out.println (org.drip.analytics.date.JulianDate.fromJulian (dblStart) + "=>" +
				org.drip.analytics.date.JulianDate.fromJulian (dblEnd));

			throw new java.lang.Exception ("Invalid parameters into FixedPeriod ctr");
		}

		_dblDCF = dblDCF;
		_dblEnd = dblEnd;
		_dblPay = dblPay;
		_dblStart = dblStart;
		_dblAccrualEnd = dblAccrualEnd;
		_dblAccrualStart = dblAccrualStart;
	}

	/**
	 * De-serialization of Period from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize
	 */

	public Period (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Period de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("Period de-serializer: Empty state");

		java.lang.String strPeriod = strRawString.substring (0, strRawString.indexOf
			(super.getObjectTrailer()));

		if (null == strPeriod || strPeriod.isEmpty())
			throw new java.lang.Exception ("Period de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strPeriod,
			super.getFieldDelimiter());

		if (null == astrField || 7 > astrField.length)
			throw new java.lang.Exception ("Period de-serialize: Invalid number of fields");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("Period de-serializer: Cannot locate start date");

		_dblStart = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("Period de-serializer: Cannot locate end date");

		_dblEnd = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("ActActDCParams de-serializer: Cannot locate accrual start date");

		_dblAccrualStart = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("ActActDCParams de-serializer: Cannot locate accrual end date");

		_dblAccrualEnd = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("Period de-serializer: Cannot locate pay date");

		_dblPay = new java.lang.Double (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			throw new java.lang.Exception ("Period de-serializer: Cannot locate DCF");

		_dblDCF = new java.lang.Double (astrField[6]);
	}

	/**
	 * Returns the period Start Date
	 * 
	 * @return Period Start Date
	 */

	public double getStartDate()
	{
		return _dblStart;
	}

	/**
	 * Returns the period End Date
	 * 
	 * @return Period End Date
	 */

	public double getEndDate()
	{
		return _dblEnd;
	}

	/**
	 * Returns the period Accrual Start Date
	 * 
	 * @return Period Accrual Start Date
	 */

	public double getAccrualStartDate()
	{
		return _dblAccrualStart;
	}

	/**
	 * Set the period Accrual Start Date
	 * 
	 * @param dblAccrualStart Period Accrual Start Date
	 */

	public boolean setAccrualStartDate (
		final double dblAccrualStart)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblAccrualStart)) return false;

		_dblAccrualStart = dblAccrualStart;
		return true;
	}

	/**
	 * Returns the period Accrual End Date
	 * 
	 * @return Period Accrual End Date
	 */

	public double getAccrualEndDate()
	{
		return _dblAccrualEnd;
	}

	/**
	 * Returns the period Reset Date
	 * 
	 * @return Period Reset Date
	 */

	public double getResetDate()
	{
		return _dblStart;
	}

	/**
	 * Returns the period Pay Date
	 * 
	 * @return Period Pay Date
	 */

	public double getPayDate()
	{
		return _dblPay;
	}

	/**
	 * Set the period Pay Date
	 * 
	 * @param dblPay Period Pay Date
	 */

	public boolean setPayDate (
		final double dblPay)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblPay)) return false;

		_dblPay = dblPay;
		return true;
	}

	/**
	 * Get the period Accrual Day Count Fraction to an accrual end date
	 * 
	 * @param dblAccrualEnd Accrual End Date
	 * 
	 * @exception Throws if inputs are invalid, or if the date does not lie within the period
	 */

	public double getAccrualDCF (
		final double dblAccrualEnd)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblAccrualEnd))
			throw new java.lang.Exception ("Accrual end is NaN!");

		if (_dblAccrualStart > dblAccrualEnd || dblAccrualEnd > _dblAccrualEnd)
			throw new java.lang.Exception ("Invalid in-period accrual date!");

		return (dblAccrualEnd - _dblAccrualStart) * _dblDCF / (_dblAccrualEnd - _dblAccrualStart);
	}

	/**
	 * Gets the coupon DCF
	 * 
	 * @return The coupon DCF
	 */

	public double getCouponDCF()
	{
		return _dblDCF;
	}
	
	/**
	 * Checks whether the supplied date is inside the period specified
	 * 
	 * @param dblDate Date input
	 * 
	 * @return True indicates the specified date is inside the period
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public boolean contains (final double dblDate) throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("Input date is NaN!");

		if (_dblStart > dblDate || dblDate > _dblEnd) return false;

		return true;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "!";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "@";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + super.getFieldDelimiter() + _dblStart +
			super.getFieldDelimiter() + _dblEnd + super.getFieldDelimiter() + _dblAccrualStart +
				super.getFieldDelimiter() + _dblAccrualEnd + super.getFieldDelimiter() + _dblPay +
					super.getFieldDelimiter() + _dblDCF);

		return sb.append (super.getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new Period (ab);
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

		Period period = new Period (dblStart, dblStart + 180, dblStart, dblStart + 180, dblStart + 180, 0.5);

		byte[] abPeriod = period.serialize();

		System.out.println ("Input: " + new java.lang.String (abPeriod));

		Period periodDeser = new Period (abPeriod);

		System.out.println ("Output: " + new java.lang.String (periodDeser.serialize()));
	}
}
