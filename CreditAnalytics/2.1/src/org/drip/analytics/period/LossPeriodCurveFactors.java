
package org.drip.analytics.period;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * This class is an implementation of the period class enhanced by the following period measures: start/end
 * 		survival probabilities, period effective notional/recovery/discount factor
 *
 * @author Lakshmi Krishnamurthy
 */

public class LossPeriodCurveFactors extends Period {
	protected double _dblStartSurvival = java.lang.Double.NaN;
	protected double _dblEndSurvival = java.lang.Double.NaN;
	protected double _dblEffectiveNotional = java.lang.Double.NaN;
	protected double _dblEffectiveRecovery = java.lang.Double.NaN;
	protected double _dblEffectiveDF = java.lang.Double.NaN;

	/**
	 * Creates an instance of the LossPeriodCurveFactors class using the period's dates and curves to
	 * 		generate the curve measures
	 * 
	 * @param dblStart Period Start Date
	 * @param dblEnd Period End Date
	 * @param dblEffectiveDCF Period's effective day count fraction
	 * @param dblEffectiveNotional Period's effective notional
	 * @param dblEffectiveRecovery Period's effective recovery
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param iDefaultLag Default Pay Lag
	 * 
	 * @return LossPeriodCurveFactors instance
	 */

	public static final LossPeriodCurveFactors MakeDefaultPeriod (
		final double dblStart,
		final double dblEnd,
		final double dblEffectiveDCF,
		final double dblEffectiveNotional,
		final double dblEffectiveRecovery,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final int iDefaultLag)
	{
		if (java.lang.Double.isNaN (dblStart) || java.lang.Double.isNaN (dblEnd) || java.lang.Double.isNaN
			(dblEffectiveDCF) || java.lang.Double.isNaN (dblEffectiveNotional) || java.lang.Double.isNaN
				(dblEffectiveRecovery) || null == dc || null == cc)
			return null;

		try {
			return new LossPeriodCurveFactors (dblStart, dblEnd, dblStart, dblEnd, dblStart + iDefaultLag,
				dblEffectiveDCF, cc.getSurvival (dblStart), cc.getSurvival (dblEnd), dblEffectiveNotional,
					dblEffectiveRecovery, dc.getEffectiveDF (dblStart + iDefaultLag, dblEnd + iDefaultLag));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a LossPeriodCurveFactors instance from the period dates and the curve measures
	 * 
	 * @param dblStart Period Start Date
	 * @param dblEnd Period End Date
	 * @param dblEffectiveDCF Period effective day count fraction
	 * @param dblEffectiveNotional Period effective notional
	 * @param dc Discount Curve
	 * @param cc Credit Curve
	 * @param iDefaultLag Default Pay Lag
	 * 
	 * @return LossPeriodCurveFactors instance
	 */

	public static final LossPeriodCurveFactors MakeDefaultPeriod (
		final double dblStart,
		final double dblEnd,
		final double dblEffectiveDCF,
		final double dblEffectiveNotional,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.CreditCurve cc,
		final int iDefaultLag)
	{
		if (java.lang.Double.isNaN (dblStart) || java.lang.Double.isNaN (dblEnd) || java.lang.Double.isNaN
			(dblEffectiveDCF) || java.lang.Double.isNaN (dblEffectiveNotional) || null == dc || null == cc)
			return null;

		try {
			return new LossPeriodCurveFactors (dblStart, dblEnd, dblStart, dblEnd, dblStart + iDefaultLag,
				dblEffectiveDCF, cc.getSurvival (dblStart), cc.getSurvival (dblEnd), dblEffectiveNotional,
					cc.getEffectiveRecovery (dblStart + iDefaultLag, dblEnd + iDefaultLag), dc.getEffectiveDF
						(dblStart + iDefaultLag, dblEnd + iDefaultLag));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Elaborate LossPeriodCurveFactors constructor
	 * 
	 * @param dblStart Start Date
	 * @param dblEnd End Date
	 * @param dblAccrualStart Accrual Start Date
	 * @param dblAccrualEnd Accrual End Date
	 * @param dblPay Pay Date
	 * @param dblEffectiveDCF Effective period DCF
	 * @param dblStartSurvival Period Start Survival
	 * @param dblEndSurvival Period End Survival
	 * @param dblEffectiveNotional Period Effective Notional
	 * @param dblEffectiveRecovery Period Effective Recovery
	 * @param dblEffectiveDF Period Effective Discount Factor
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public LossPeriodCurveFactors (
		final double dblStart,
		final double dblEnd,
		final double dblAccrualStart,
		final double dblAccrualEnd,
		final double dblPay,
		final double dblEffectiveDCF,
		final double dblStartSurvival,
		final double dblEndSurvival,
		final double dblEffectiveNotional,
		final double dblEffectiveRecovery,
		final double dblEffectiveDF)
		throws java.lang.Exception
	{
		super (dblStart, dblEnd, dblStart, dblEnd, dblPay, dblEffectiveDCF);

		if (java.lang.Double.isNaN (_dblStartSurvival = dblStartSurvival) || java.lang.Double.isNaN
			(_dblEndSurvival = dblEndSurvival) || java.lang.Double.isNaN (_dblEffectiveNotional =
				dblEffectiveNotional) || java.lang.Double.isNaN (_dblEffectiveRecovery =
					dblEffectiveRecovery) || java.lang.Double.isNaN (_dblEffectiveDF = dblEffectiveDF))
			throw new java.lang.Exception ("Invalid params into LossPeriod ctr");
	}

	/**
	 * De-serialization of LossPeriodCurveFactors from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize LossPeriodCurveFactors
	 */

	public LossPeriodCurveFactors (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);

		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("LossPeriodCurveFactors de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("LossPeriodCurveFactors de-serializer: Empty state");

		java.lang.String strCP = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strCP || strCP.isEmpty())
			throw new java.lang.Exception ("LossPeriodCurveFactors de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strCP,
			getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("LossPeriodCurveFactors de-serialize: Invalid number of fields");

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception
				("LossPeriodCurveFactors de-serializer: Cannot locate start survival");

		_dblStartSurvival = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception
				("LossPeriodCurveFactors de-serializer: Cannot locate end survival");

		_dblEndSurvival = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception
				("LossPeriodCurveFactors de-serializer: Cannot locate effective notional");

		_dblEffectiveNotional = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception
				("LossPeriodCurveFactors de-serializer: Cannot locate effective recovery");

		_dblEffectiveRecovery = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception
				("LossPeriodCurveFactors de-serializer: Cannot locate effective DF");

		_dblEffectiveDF = new java.lang.Double (astrField[5]);
	}

	/**
	 * Survival at the period beginning
	 * 
	 * @return Survival at the period beginning
	 */

	public double getStartSurvival()
	{
		return _dblStartSurvival;
	}

	/**
	 * Survival at the period end
	 * 
	 * @return Survival at the period end
	 */

	public double getEndSurvival()
	{
		return _dblEndSurvival;
	}

	/**
	 * Gets the period's effective notional
	 * 
	 * @return Period's effective notional
	 */

	public double getEffectiveNotional()
	{
		return _dblEffectiveNotional;
	}

	/**
	 * Gets the period's effective recovery
	 * 
	 * @return Period's effective recovery
	 */

	public double getEffectiveRecovery()
	{
		return _dblEffectiveRecovery;
	}

	/**
	 * Gets the period's effective discount factor
	 * 
	 * @return Period's effective discount factor
	 */

	public double getEffectiveDF()
	{
		return _dblEffectiveDF;
	}

	/**
	 * Gets the period's accrual day count factor
	 * 
	 * @return Period's accrual day count factor
	 */

	public double getAccrualDCF()
	{
		return _dblDCF;
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

		sb.append (new java.lang.String (super.serialize()) + getFieldDelimiter() + _dblStartSurvival +
			getFieldDelimiter() + _dblEndSurvival + getFieldDelimiter() + _dblEffectiveNotional +
				getFieldDelimiter() + _dblEffectiveRecovery + getFieldDelimiter() + _dblEffectiveDF);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}
	
	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		LossPeriodCurveFactors plpcm = new LossPeriodCurveFactors (dblStart, dblStart + 180, dblStart,
			dblStart + 180, dblStart + 180, 0.5, 0.98, 0.94, 1000000., 0.36, 0.96);

		byte[] abPLPCM = plpcm.serialize();

		System.out.println ("Input: " + new java.lang.String (abPLPCM));

		LossPeriodCurveFactors plpcmDeser = new LossPeriodCurveFactors (abPLPCM);

		System.out.println ("Output: " + new java.lang.String (plpcmDeser.serialize()));
	}
}
