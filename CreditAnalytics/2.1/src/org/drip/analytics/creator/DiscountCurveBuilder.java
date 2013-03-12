
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
 * This class contains the baseline discount curve builder object. It contains static functions that build
 * 		bootstrapped and other types of discount curve from differing types of inputs.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DiscountCurveBuilder {
	/**
	 * Constant Forward Bootstrap mode
	 */

	public static final java.lang.String BOOTSTRAP_MODE_CONSTANT_FORWARD = "ConstantForward";

	/**
	 * Linear Forward Bootstrap mode
	 */

	public static final java.lang.String BOOTSTRAP_MODE_LINEAR_FORWARD = "LinearForward";

	/**
	 * Cubic Forward Bootstrap mode
	 */

	public static final java.lang.String BOOTSTRAP_MODE_CUBIC_FORWARD = "CubicForward";

	/**
	 * Hyperbolic Tension Spline Forward Bootstrap mode
	 */

	public static final java.lang.String BOOTSTRAP_MODE_HYPERBOLIC_TENSION_FORWARD =
		"HyperbolicTensionForward";

	/**
	 * Polynomial Spline DF Bootstrap mode
	 */

	public static final java.lang.String BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF = "PolynomialSplineDF";

	/**
	 * Builds a Discount Curve from an array of discount factors
	 * 
	 * @param dtStart Start Date
	 * @param strCurrency Currency
	 * @param adblDate Array of dates
	 * @param adblDF array of discount factors
	 * @param strBootstrapMode Mode of the bootstrapping to be done: "ConstantForward", "LinearForward",
	 * 			"QuadraticForward", or "CubicForward". Defaults to "ConstantForward".
	 * 
	 * @return Discount Curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve BuildFromDF (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double adblDate[],
		final double adblDF[],
		final java.lang.String strBootstrapMode)
	{
		if (null == adblDate || 0 == adblDate.length || null == adblDF || adblDate.length != adblDF.length ||
			null == dtStart || null == strCurrency || strCurrency.isEmpty())
			return null;

		double dblDFBegin = 1.;
		double[] adblRate = new double[adblDate.length];

		double dblPeriodBegin = dtStart.getJulian();

		for (int i = 0; i < adblDate.length; ++i) {
			if (adblDate[i] <= dblPeriodBegin) return null;

			adblRate[i] = 365.25 / (adblDate[i] - dblPeriodBegin) * java.lang.Math.log (dblDFBegin /
				adblDF[i]);

			dblDFBegin = adblDF[i];
			dblPeriodBegin = adblDate[i];
		}

		try {
			if (null == strBootstrapMode)
				return new org.drip.analytics.curve.ConstantForwardRate (dtStart, strCurrency,
					adblDate, adblRate);

			if (BOOTSTRAP_MODE_LINEAR_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.LinearForwardRate (dtStart, strCurrency, adblDate,
					adblRate);

			if (BOOTSTRAP_MODE_CUBIC_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.CubicForwardRate (dtStart, strCurrency, adblDate,
					adblRate);

			if (BOOTSTRAP_MODE_HYPERBOLIC_TENSION_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.HyperbolicTensionForwardRate (dtStart, strCurrency,
					adblDate, adblRate);

			if (BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.PolynomialSplineDF (dtStart, strCurrency, adblDate,
					adblRate);

			return new org.drip.analytics.curve.ConstantForwardRate (dtStart, strCurrency, adblDate,
				adblRate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a discount curve from the flat rate
	 * 
	 * @param dtStart Start Date
	 * @param strCurrency Currency
	 * @param dblRate Date
	 * 
	 * @return Discount Curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve CreateFromFlatRate (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double dblRate)
	{
		if (null == dtStart || !org.drip.math.common.NumberUtil.IsValid (dblRate)) return null;

		try {
			return new org.drip.analytics.curve.ConstantForwardRate (dtStart, strCurrency, new double[]
				{dtStart.getJulian()}, new double[] {dblRate});
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a discount curve from an array of dates/rates
	 * 
	 * @param dtStart Start Date
	 * @param strCurrency Currency
	 * @param adblDate array of dates
	 * @param adblRate array of rates
	 * @param strBootstrapMode Mode of the bootstrapping to be done: "ConstantForward", "LinearForward",
	 * 			"QuadraticForward", or "CubicForward". Defaults to "ConstantForward".
	 * 
	 * @return Creates the discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve CreateDC (
		final org.drip.analytics.date.JulianDate dtStart,
		final java.lang.String strCurrency,
		final double[] adblDate,
		final double[] adblRate,
		final java.lang.String strBootstrapMode)
	{
		try {
			if (null == strBootstrapMode)
				return new org.drip.analytics.curve.ConstantForwardRate (dtStart, strCurrency, adblDate,
					adblRate);

			if (BOOTSTRAP_MODE_LINEAR_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.LinearForwardRate (dtStart, strCurrency, adblDate,
					adblRate);

			if (BOOTSTRAP_MODE_CUBIC_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.CubicForwardRate (dtStart, strCurrency, adblDate,
					adblRate);

			if (BOOTSTRAP_MODE_HYPERBOLIC_TENSION_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.HyperbolicTensionForwardRate (dtStart, strCurrency,
					adblDate, adblRate);

			if (BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.PolynomialSplineDF (dtStart, strCurrency, adblDate,
					adblRate);

			return new org.drip.analytics.curve.ConstantForwardRate (dtStart, strCurrency, adblDate,
				adblRate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a discount curve instance from the byte array
	 * 
	 * @param ab Byte Array
	 * @param strBootstrapMode Mode of the bootstrapping to be done: "ConstantForward", "LinearForward",
	 * 			"QuadraticForward", or "CubicForward". Defaults to "ConstantForward".
	 * 
	 * @return Discount Curve Instance
	 */

	public static final org.drip.analytics.definition.DiscountCurve FromByteArray (
		final byte[] ab,
		final java.lang.String strBootstrapMode)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			if (null == strBootstrapMode) return new org.drip.analytics.curve.ConstantForwardRate (ab);

			if (BOOTSTRAP_MODE_LINEAR_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.LinearForwardRate (ab);

			if (BOOTSTRAP_MODE_CUBIC_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.CubicForwardRate (ab);

			if (BOOTSTRAP_MODE_HYPERBOLIC_TENSION_FORWARD.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.HyperbolicTensionForwardRate (ab);

			if (BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF.equalsIgnoreCase (strBootstrapMode))
				return new org.drip.analytics.curve.PolynomialSplineDF (ab);

			return new org.drip.analytics.curve.ConstantForwardRate (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
