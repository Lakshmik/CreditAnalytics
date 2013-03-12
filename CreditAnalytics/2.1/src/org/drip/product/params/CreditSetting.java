
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
 * CreditSetting contains the credit related valuation parameters - use default pay lag, use
 * 		curve or the component recovery, component recovery, credit curve name, and whether there is accrual
 * 		on default
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditSetting extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Default Pay Lag
	 */

	public int _iDefPayLag = -1;

	/**
	 * Use curve or component recovery
	 */

	public boolean _bUseCurveRec = true;

	/**
	 * Credit Curve Name
	 */

	public java.lang.String _strCC = "";

	/**
	 * Whether accrual gets paid on default
	 */

	public boolean _bAccrOnDefault = false;

	/**
	 * Component recovery
	 */

	public double _dblRecovery = java.lang.Double.NaN;

	/**
	 * Constructs the CreditSetting from the default pay lag, use curve or the component
	 * 		recovery flag, component recovery, credit curve name, and whether there is accrual on default
	 * 
	 * @param iDefPayLag Default Pay Lag
	 * @param dblRecovery Component Recovery
	 * @param bUseCurveRec Use the Curve Recovery (True) or Component Recovery (False)
	 * @param strCC Credit curve name
	 * @param bAccrOnDefault Accrual paid on default (True) 
	 */

	public CreditSetting (
		final int iDefPayLag,
		final double dblRecovery,
		final boolean bUseCurveRec,
		final java.lang.String strCC,
		final boolean bAccrOnDefault)
	{
		_strCC = strCC;
		_iDefPayLag = iDefPayLag;
		_dblRecovery = dblRecovery;
		_bUseCurveRec = bUseCurveRec;
		_bAccrOnDefault = bAccrOnDefault;
	}

	/**
	 * CreditSetting de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if CreditSetting cannot be properly de-serialized
	 */

	public CreditSetting (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception
				("CreditSetting de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CreditSetting de-serializer: Empty state");

		java.lang.String strSerializedCreditSetting = strRawString.substring (0,
			strRawString.indexOf (getObjectTrailer()));

		if (null == strSerializedCreditSetting ||
			strSerializedCreditSetting.isEmpty())
			throw new java.lang.Exception ("CreditSetting de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedCreditSetting, getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("CreditSetting de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CreditSetting de-serializer: Cannot locate Credit Curve Name");

		_strCC = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CreditSetting de-serializer: Cannot locate Default Pay Lag");

		_iDefPayLag = new java.lang.Integer (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("CreditSetting de-serializer: Cannot locate comp recovery");

		_dblRecovery = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("CreditSetting de-serializer: Cannot locate Use curve recpvery flag");

		_bUseCurveRec = new java.lang.Boolean (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("CreditSetting de-serializer: Cannot locate accr on default flag");

		_bAccrOnDefault = new java.lang.Boolean (astrField[5]);

		if (!validate()) throw new java.lang.Exception ("CreditSetting de-serializer: Cannot validate!");
	}

	@Override public boolean validate()
	{
		if (null == _strCC || _strCC.isEmpty()) return true;

		if (!org.drip.math.common.NumberUtil.IsValid (_dblRecovery) && !_bUseCurveRec) return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		if (null == _strCC || _strCC.isEmpty())
			sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() +
				org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strCC +
				getFieldDelimiter());

		sb.append (_iDefPayLag + getFieldDelimiter() + _dblRecovery + getFieldDelimiter() + _bUseCurveRec +
			getFieldDelimiter() + _bAccrOnDefault);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CreditSetting (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		CreditSetting ccvp = new CreditSetting (2, 0.40, true, "JKL", true);

		byte[] abCCVP = ccvp.serialize();

		System.out.println (new java.lang.String (abCCVP));

		CreditSetting ccvpDeser = new CreditSetting (abCCVP);

		System.out.println (new java.lang.String (ccvpDeser.serialize()));
	}
}
