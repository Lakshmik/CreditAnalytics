
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
 * Contains the coupon type, schedule, and the coupon amount for the component.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CouponSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Coupon schedule
	 */

	public FactorSchedule _fsCoupon = null;

	/**
	 * Coupon Type
	 */

	public java.lang.String _strCouponType = "";

	/**
	 * Coupon Amount
	 */

	public double _dblCoupon = java.lang.Double.NaN;

	/**
	 * Coupon Floor
	 */

	public double _dblCouponFloor = java.lang.Double.NaN;

	/**
	 * Coupon Ceiling
	 */

	public double _dblCouponCeiling = java.lang.Double.NaN;

	/**
	 * Constructs the CouponSetting from the coupon schedule, coupon type, and the coupon amount
	 * 
	 * @param fsCoupon Coupon schedule
	 * @param strCouponType Coupon Type
	 * @param dblCoupon Coupon Amount
	 * @param dblCouponCeiling Coupon Ceiling Amount
	 * @param dblCouponFloor Coupon Floor Amount
	 */

	public CouponSetting (
		final FactorSchedule fsCoupon,
		final java.lang.String strCouponType,
		final double dblCoupon,
		final double dblCouponCeiling,
		final double dblCouponFloor)
	{
		_fsCoupon = fsCoupon;
		_dblCoupon = dblCoupon;
		_strCouponType = strCouponType;
		_dblCouponFloor = dblCouponFloor;
		_dblCouponCeiling = dblCouponCeiling;
	}

	/**
	 * CouponSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CouponSetting cannot be properly de-serialized
	 */

	public CouponSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CouponSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CouponSetting de-serializer: Empty state");

		java.lang.String strSerializedCouponSetting = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedCouponSetting || strSerializedCouponSetting.isEmpty())
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCouponSetting, getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("CouponSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate perpetual flag");

		_fsCoupon = new FactorSchedule (astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate coupon");

		_dblCoupon = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate coupon type");

		_strCouponType = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate coupon");

		_dblCouponCeiling = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("CouponSetting de-serializer: Cannot locate coupon");

		_dblCouponFloor = new java.lang.Double (astrField[5]);

		if (!validate()) throw new java.lang.Exception ("CouponSetting de-serializer: Cannot validate!");
	}

	/**
	 * Trims the component coupon if it falls outside the (optionally) specified coupon window. Note that
	 * 		trimming the coupon ceiling takes precedence over hiking the coupon floor.
	 * 
	 * @param dblCoupon Input Coupon
	 * @param dblDate Input Date representing the period that the coupon belongs to
	 * 
	 * @return The "trimmed" coupon
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public double processCouponWindow (
		final double dblCoupon,
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblCoupon) || !org.drip.math.common.NumberUtil.IsValid
			(dblDate))
			throw new java.lang.Exception ("Invalid params into CouponSetting.processCouponWindow");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblCouponCeiling) &&
			!org.drip.math.common.NumberUtil.IsValid (_dblCouponFloor))
			return dblCoupon;

		if (!!org.drip.math.common.NumberUtil.IsValid (_dblCouponCeiling) && dblCoupon > _dblCouponCeiling)
			return _dblCouponCeiling;

		if (!!org.drip.math.common.NumberUtil.IsValid (_dblCouponFloor) && dblCoupon < _dblCouponFloor)
			return _dblCouponFloor;

		return dblCoupon;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + new java.lang.String
			(_fsCoupon.serialize()) + getFieldDelimiter() + _dblCoupon + getFieldDelimiter() +
				_strCouponType + getFieldDelimiter() + _dblCouponCeiling + getFieldDelimiter() +
					_dblCouponFloor);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CouponSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public boolean validate()
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblCoupon)) return false;

		if (null == _fsCoupon) _fsCoupon = FactorSchedule.CreateBulletSchedule();

		if (org.drip.math.common.NumberUtil.IsValid (_dblCouponCeiling) &&
			org.drip.math.common.NumberUtil.IsValid (_dblCouponFloor) && _dblCouponCeiling < _dblCouponFloor)
			return false;

		return true;
	}

	public static final void main (
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

		FactorSchedule fs = FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);

		CouponSetting bcp = new CouponSetting (fs, "FULL", 0.05, java.lang.Double.NaN, java.lang.Double.NaN);

		byte[] abBCP = bcp.serialize();

		System.out.println (new java.lang.String (abBCP));

		CouponSetting bcpDeser = new CouponSetting (abBCP);

		System.out.println (new java.lang.String (bcpDeser.serialize()));
	}
}
