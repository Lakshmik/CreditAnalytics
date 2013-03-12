
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
 * Contains the Component Market Convention Parameters - the quote convention, the calculation type, the
 * 		first settle date, and the redemption amount.
 *
 * @author Lakshmi Krishnamurthy
 */

public class QuoteConvention extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	/**
	 * Calculation Type
	 */

	public java.lang.String _strCalculationType = "";

	/**
	 * First Settle Date
	 */

	public double _dblFirstSettle = java.lang.Double.NaN;

	/**
	 * Redemption Value
	 */

	public double _dblRedemptionValue = java.lang.Double.NaN;

	/**
	 * Quoting Parameters
	 */

	public org.drip.param.valuation.QuotingParams _quotingParams = null;

	/**
	 * Cash Settle parameters
	 */

	public org.drip.param.valuation.CashSettleParams _settleParams = null;

	/**
	 * Constructs the Market Convention object from the quoting convention, the calculation type, the
	 * 		first settle date, and the redemption value.
	 * 
	 * @param quotingParams Quoting Params
	 * @param strCalculationType Calculation Type
	 * @param dblFirstSettle First Settle Date
	 * @param dblRedemptionValue Redemption Value
	 * @param iSettleLag Settle Lag
	 * @param strSettleCalendar Settlement Calendar
	 * @param iSettleAdjustMode Is Settle date business adjusted
	 */

	public QuoteConvention (
		final org.drip.param.valuation.QuotingParams quotingParams,
		final java.lang.String strCalculationType,
		final double dblFirstSettle,
		final double dblRedemptionValue,
		final int iSettleLag,
		final java.lang.String strSettleCalendar,
		final int iSettleAdjustMode)
	{
		_quotingParams = quotingParams;
		_dblFirstSettle = dblFirstSettle;
		_strCalculationType = strCalculationType;
		_dblRedemptionValue = dblRedemptionValue;
		
		_settleParams = new org.drip.param.valuation.CashSettleParams (iSettleLag, strSettleCalendar,
			iSettleAdjustMode);
	}

	/**
	 * Market Convention de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if Market Convention cannot be properly de-serialized
	 */

	public QuoteConvention (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("Market Convention de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty()) 
			throw new java.lang.Exception ("Market Convention de-serializer: Empty state");

		java.lang.String strSerializedMarketConvention = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedMarketConvention || strSerializedMarketConvention.isEmpty())
			throw new java.lang.Exception ("Market Convention de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedMarketConvention, getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("Market Convention de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("MarketConvention de-serializer: Cannot locate first settle date");

		_dblFirstSettle = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("MarketConvention de-serializer: Cannot locate Calculation Type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strCalculationType = "";
		else
			_strCalculationType = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("MarketConvention de-serializer: Cannot locate first redemption value");

		_dblRedemptionValue = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception
				("MarketConvention de-serializer: Cannot locate Quoting Convention");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_quotingParams = null;
		else
			_quotingParams = new org.drip.param.valuation.QuotingParams (astrField[4].getBytes());

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception
				("MarketConvention de-serializer: Cannot locate settle params value");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			_settleParams = null;
		else
			_settleParams = new org.drip.param.valuation.CashSettleParams (astrField[5].getBytes());

		if (!validate())
			throw new java.lang.Exception ("MarketConvention de-serializer: Cannot validate!");
	}

	public double getSettleDate (
		final org.drip.param.valuation.ValuationParams valParams)
		throws java.lang.Exception
	{
		if (null == valParams)
			throw new java.lang.Exception
				("Invalid val Params into ComponentValuationParams::getSettleDate!");

		return _settleParams.cashSettleDate (valParams._dblValue);
	}

	@Override public boolean validate()
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblFirstSettle) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblRedemptionValue))
			return false;

		return true;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		sb.append (_dblFirstSettle + getFieldDelimiter());

		if (null == _strCalculationType || _strCalculationType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCalculationType + getFieldDelimiter());

		sb.append (_dblRedemptionValue + getFieldDelimiter());

		if (null == _quotingParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_quotingParams.serialize()) + getFieldDelimiter());

		if (null == _settleParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (new java.lang.String (_settleParams.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new QuoteConvention (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		QuoteConvention bivp = new QuoteConvention (new org.drip.param.valuation.QuotingParams ("30/360", 2,
			true, null, "DKK", false), "GHI", 1., 2., 3, "JKL", 4);

		byte[] abBIVP = bivp.serialize();

		System.out.println (new java.lang.String (abBIVP));

		QuoteConvention bivpDeser = new QuoteConvention (abBIVP);

		System.out.println (new java.lang.String (bivpDeser.serialize()));
	}
}
