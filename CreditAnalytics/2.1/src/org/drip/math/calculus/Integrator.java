
package org.drip.math.calculus;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * Integral implements several routines for integrating the objective function.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Integrator {
	private final static int NUM_QUAD = 10000;

	/**
	 * Compute the function's integral within the specified limits using the LinearQuadrature technique.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double LinearQuadrature (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::LinearQuadrature => Invalid Inputs");

		double dblWidth = (dblRight - dblLeft) / NUM_QUAD;
		double dblX = dblLeft + dblWidth;
		double dblAUArea = 0.;

		while (dblX <= dblRight) {
			double dblY = au.evaluate (dblX - 0.5 * dblWidth);

			if (!org.drip.math.common.NumberUtil.IsValid (dblLeft))
				throw new java.lang.Exception
					("Integrator::LinearQuadrature => Cannot calculate an intermediate Y");

			dblAUArea += dblY * dblWidth;
			dblX += dblWidth;
		}

		return dblAUArea;
	}

	/**
	 * Compute the function's integral within the specified limits using the Mid-point rule.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double MidPoint (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::MidPoint => Invalid Inputs");

		double dblYMid = au.evaluate (0.5 * (dblLeft + dblRight));

		if (!org.drip.math.common.NumberUtil.IsValid (dblYMid))
			throw new java.lang.Exception ("Integrator::MidPoint => Cannot calculate Y at " + 0.5 * (dblLeft
				+ dblRight));

		return (dblRight - dblLeft) * dblYMid;
	}

	/**
	 * Compute the function's integral within the specified limits using the Trapezoidal rule.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double Trapezoidal (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::Trapezoidal => Invalid Inputs");

		double dblYLeft = au.evaluate (dblLeft);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYLeft))
			throw new java.lang.Exception ("Integrator::Trapezoidal => Cannot calculate Y at " + dblLeft);

		double dblYRight = au.evaluate (dblRight);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYLeft))
			throw new java.lang.Exception ("Integrator::Trapezoidal => Cannot calculate Y at " + dblRight);

		return 0.5 * (dblRight - dblLeft) * (dblYLeft + dblYRight);
	}

	/**
	 * Compute the function's integral within the specified limits using the Simpson rule.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double Simpson (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::Simpson => Invalid Inputs");

		double dblYLeft = au.evaluate (dblLeft);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYLeft))
			throw new java.lang.Exception ("Integrator::Simpson => Cannot calculate Y at " + dblLeft);

		double dblXMid = 0.5 * (dblLeft + dblRight);

		double dblYMid = au.evaluate (dblXMid);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYMid))
			throw new java.lang.Exception ("Integrator::Simpson => Cannot calculate Y at " + dblXMid);

		double dblYRight = au.evaluate (dblRight);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYRight))
			throw new java.lang.Exception ("Integrator::Simpson => Cannot calculate Y at " + dblRight);

		return (dblRight - dblLeft) / 6. * (dblYLeft + 4. * dblYMid + dblYRight);
	}

	/**
	 * Compute the function's integral within the specified limits using the Simpson 3/8 rule.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double Simpson38 (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::Simpson38 => Invalid Inputs");

		double dblY0 = au.evaluate (dblLeft);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY0))
			throw new java.lang.Exception ("Integrator::Simpson38 => Cannot calculate Y at " + dblLeft);

		double dblX1 = (2. * dblLeft + dblRight) / 3.;

		double dblY1 = au.evaluate (dblX1);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY1))
			throw new java.lang.Exception ("Integrator::Simpson38 => Cannot calculate Y at " + dblX1);

		double dblX2 = (dblLeft + 2. * dblRight) / 3.;

		double dblY2 = au.evaluate (dblX2);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY2))
			throw new java.lang.Exception ("Integrator::Simpson38 => Cannot calculate Y at " + dblX2);

		double dblY3 = au.evaluate (dblRight);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY3))
			throw new java.lang.Exception ("Integrator::Simpson38 => Cannot calculate Y at " + dblRight);

		return (dblRight - dblLeft) * (0.125 * dblY0 + 0.375 * dblY1 + 0.375 * dblY2 + 0.125 * dblY3);
	}

	/**
	 * Compute the function's integral within the specified limits using the Boole rule.
	 * 
	 * @param au Univariate Function
	 * @param dblLeft Left Variate
	 * @param dblRight Right Variate
	 * 
	 * @return The Integral
	 * 
	 * @throws java.lang.Exception Thrown if the error cannot be computed
	 */

	public static final double Boole (
		final org.drip.math.function.AbstractUnivariate au,
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception
	{
		if (null == au || !org.drip.math.common.NumberUtil.IsValid (dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRight))
			throw new java.lang.Exception ("Integrator::Boole => Invalid Inputs");

		double dblY0 = au.evaluate (dblLeft);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY0))
			throw new java.lang.Exception ("Integrator::Boole => Cannot calculate Y at " + dblLeft);

		double dblX1 = 0.25 * dblLeft + 0.75 * dblRight;

		double dblY1 = au.evaluate (dblX1);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY1))
			throw new java.lang.Exception ("Integrator::Boole => Cannot calculate Y at " + dblX1);

		double dblX2 = 0.5 * (dblLeft + dblRight);

		double dblY2 = au.evaluate (dblX2);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY2))
			throw new java.lang.Exception ("Integrator::Boole => Cannot calculate Y at " + dblX2);

		double dblX3 = 0.75 * dblLeft + 0.25 * dblRight;

		double dblY3 = au.evaluate (dblX3);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY3))
			throw new java.lang.Exception ("Integrator::Boole => Cannot calculate Y at " + dblX3);

		double dblY4 = au.evaluate (dblRight);

		if (!org.drip.math.common.NumberUtil.IsValid (dblY4))
			throw new java.lang.Exception ("Integrator::Boole => Cannot calculate Y at " + dblRight);

		return (dblRight - dblLeft) / 90 * (7 * dblY0 + 32 * dblY1 + 12 * dblY2 + 32 * dblY3 + 7 * dblY4);
	}

	public static void main (
		final java.lang.String astrArgs[])
		throws java.lang.Exception
	{
		// org.drip.math.function.AbstractUnivariate au = new org.drip.math.function.Polynomial (10);

		org.drip.math.function.AbstractUnivariate au = new org.drip.math.function.ExponentialTension
			(java.lang.Math.E, 1.);

		System.out.println ("Linear = " + Integrator.LinearQuadrature (au, 0., 1.));

		System.out.println ("MidPoint = " + Integrator.MidPoint (au, 0., 1.));

		System.out.println ("Trapezoidal = " + Integrator.Trapezoidal (au, 0., 1.));

		System.out.println ("Simpson = " + Integrator.Simpson (au, 0., 1.));

		System.out.println ("Simpson38 = " + Integrator.Simpson38 (au, 0., 1.));

		System.out.println ("Boole = " + Integrator.Boole (au, 0., 1.));
	}
}
