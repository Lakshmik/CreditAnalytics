
package org.drip.analytics.creator;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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
 * This class contains the baseline credit curve builder object. It contains static functions that build
 * 		bootstrapped and other types of credit curve from differing types of inputs.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditCurveBuilder {

	/**
	 * Creates a CreditCurve instance from a single node hazard rate
	 * 
	 * @param dblStartDate Curve epoch date
	 * @param strName Credit Curve Name
	 * @param dblHazardRate Curve hazard rate
	 * @param dblRecovery Curve recovery
	 * 
	 * @return CreditCurve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve FromFlatHazard (
		final double dblStartDate,
		final java.lang.String strName,
		final double dblHazardRate,
		final double dblRecovery)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStartDate) ||
			!org.drip.math.common.NumberUtil.IsValid (dblHazardRate) ||
				!org.drip.math.common.NumberUtil.IsValid (dblRecovery))
			return null;

		double[] adblHazard = new double[1];
		double[] adblRecovery = new double[1];
		double[] adblHazardDate = new double[1];
		double[] adblRecoveryDate = new double[1];
		adblHazard[0] = dblHazardRate;
		adblRecovery[0] = dblRecovery;
		adblHazardDate[0] = dblStartDate;
		adblRecoveryDate[0] = dblStartDate;

		try {
			return new org.drip.analytics.curve.ConstantForwardHazard (dblStartDate, strName, adblHazard,
				adblHazardDate, adblRecovery, adblRecoveryDate, java.lang.Double.NaN);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a CreditCurve instance from the input array of survival probabilities
	 * 
	 * @param dblStartDate Start Date
	 * @param strName Credit Curve Name
	 * @param adblSurvivalDate Array of dates
	 * @param adblSurvivalProbability Array of survival probabilities
	 * @param dblRecovery Recovery
	 * 
	 * @return CreditCurve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve FromSurvival (
		final double dblStartDate,
		final java.lang.String strName,
		final double[] adblSurvivalDate,
		final double[] adblSurvivalProbability,
		final double dblRecovery)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblRecovery)) return null;

		try {
			double dblSurvivalBegin = 1.;
			double dblPeriodBegin = dblStartDate;
			double[] adblHazard = new double[adblSurvivalProbability.length];
			double[] adblRecovery = new double[1];
			double[] adblRecoveryDate = new double[1];
			adblRecovery[0] = dblRecovery;
			adblRecoveryDate[0] = dblStartDate;

			for (int i = 0; i < adblSurvivalProbability.length; ++i) {
				if (!org.drip.math.common.NumberUtil.IsValid (adblSurvivalProbability[i]) ||
					adblSurvivalDate[i] <= dblPeriodBegin || dblSurvivalBegin <= adblSurvivalProbability[i])
					return null;

				adblHazard[i] = 365.25 / (adblSurvivalDate[i] - dblPeriodBegin) * java.lang.Math.log
					(dblSurvivalBegin / adblSurvivalProbability[i]);

				dblPeriodBegin = adblSurvivalDate[i];
				dblSurvivalBegin = adblSurvivalProbability[i];
			}

			return new org.drip.analytics.curve.ConstantForwardHazard (dblStartDate, strName, adblHazard,
				adblSurvivalDate, adblRecovery, adblRecoveryDate, java.lang.Double.NaN);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an instance of the CreditCurve object from a solitary hazard rate node
	 * 
	 * @param dblStartDate The Curve epoch date
	 * @param strName Credit Curve Name
	 * @param dblHazardRate The solo hazard rate
	 * @param dblHazardDate Date
	 * @param dblRecovery Recovery
	 * 
	 * @return CreditCurve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve FromHazardNode (
		final double dblStartDate,
		final java.lang.String strName,
		final double dblHazardRate,
		final double dblHazardDate,
		final double dblRecovery)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStartDate) ||
			!org.drip.math.common.NumberUtil.IsValid (dblHazardRate) ||
				!org.drip.math.common.NumberUtil.IsValid (dblHazardDate) ||
					!org.drip.math.common.NumberUtil.IsValid (dblRecovery)) {
			System.out.println ("Invalid inputs into CreditCurve.FromHazardNode");

			return null;
		}

		double[] adblHazard = new double[1];
		double[] adblRecovery = new double[1];
		double[] adblHazardDate = new double[1];
		double[] adblRecoveryDate = new double[1];
		adblHazard[0] = dblHazardRate;
		adblRecovery[0] = dblRecovery;
		adblHazardDate[0] = dblHazardDate;
		adblRecoveryDate[0] = dblStartDate;

		try {
			return new org.drip.analytics.curve.ConstantForwardHazard (dblStartDate, strName, adblHazard,
				adblHazardDate, adblRecovery, adblRecoveryDate, java.lang.Double.NaN);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a credit curve from an array of dates and hazard rates
	 * 
	 * @param dtStart Curve epoch date
	 * @param strName Credit Curve Name
	 * @param adblDate Array of dates
	 * @param adblHazardRate Array of hazard rates
	 * @param dblRecovery Recovery
	 * 
	 * @return CreditCurve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve CreateCreditCurve (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strName,
		final double[] adblDate,
		final double[] adblHazardRate,
		final double dblRecovery)
	{
		if (null == dtStart || null == adblHazardRate || null == adblDate || adblHazardRate.length !=
			adblDate.length || !org.drip.math.common.NumberUtil.IsValid (dblRecovery)) {
			System.out.println ("Invalid Credit curve ctr params!");

			return null;
		}

		try {
			double[] adblRecovery = new double[1];
			double[] adblRecoveryDate = new double[1];
			adblRecovery[0] = dblRecovery;

			adblRecoveryDate[0] = dtStart.getJulian();

			return new org.drip.analytics.curve.ConstantForwardHazard (dtStart.getJulian(), strName,
				adblHazardRate, adblDate, adblRecovery, adblRecoveryDate, java.lang.Double.NaN);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the credit curve from the given byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return The credit curve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.analytics.curve.ConstantForwardHazard (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a credit curve from hazard rate and recovery rate term structures
	 * 
	 * @param dblStart Curve Epoch date
	 * @param strName Credit Curve Name
	 * @param adblHazardRate Matched array of hazard rates
	 * @param adblHazardDate Matched array of hazard dates
	 * @param adblRecoveryRate Matched array of recovery rates
	 * @param adblRecoveryDate Matched array of recovery dates
	 * @param dblSpecificDefaultDate (Optional) Specific Default Date
	 * 
	 * @return CreditCurve instance
	 */

	public static final org.drip.analytics.definition.CreditCurve CreateCreditCurve (
		final double dblStart,
		final java.lang.String strName,
		final double adblHazardRate[],
		final double adblHazardDate[],
		final double[] adblRecoveryRate,
		final double[] adblRecoveryDate,
		final double dblSpecificDefaultDate)
	{
		try {
			return new org.drip.analytics.curve.ConstantForwardHazard (dblStart, strName, adblHazardRate,
				adblHazardDate, adblRecoveryRate, adblRecoveryDate, dblSpecificDefaultDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
