
package org.drip.math.distribution;

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
 * This class implements the univariate normal distribution. It implements incremental, cumulative, and
 *  inverse cumulative distribution densities.
 *
 * @author Lakshmi Krishnamurthy
 */

public class UnivariateNormal extends org.drip.math.distribution.Univariate {
	/*
	 * Constants for the Cumulative Normal
	 */

	private static final double _CUMULATIVE_P =   0.231641900;
	private static final double _CUMULATIVE_B1 =  0.319381530;
	private static final double _CUMULATIVE_B2 = -0.356563782;
	private static final double _CUMULATIVE_B3 =  1.781477937;
	private static final double _CUMULATIVE_B4 = -1.821255978;
	private static final double _CUMULATIVE_B5 =  1.330274429;
	private static final double _CUMULATIVE_C2 =  0.398942300;

	/*
	 * Constants for the Inverse Cumulative Normal
	 */

	private static final double _INV_CUMULATIVE_PLOW  = 0.02425;
	private static final double _INV_CUMULATIVE_PHIGH = 1. - _INV_CUMULATIVE_PLOW;

	private static final double[] _INV_CUMULATIVE_A = new double[] {
		-3.969683028665376e+01,
		 2.209460984245205e+02,
		-2.759285104469687e+02,
		 1.383577518672690e+02,
		-3.066479806614716e+01,
		 2.506628277459239e+00
	};

	private static final double[] _INV_CUMULATIVE_B = new double[] {
		-5.447609879822406e+01,
		 1.615858368580409e+02,
		-1.556989798598866e+02,
		 6.680131188771972e+01,
		-1.328068155288572e+01
	};

	private static final double[] _INV_CUMULATIVE_C = new double[] {
		-7.784894002430293e-03,
		-3.223964580411365e-01,
		-2.400758277161838e+00,
		-2.549732539343734e+00,
		 4.374664141464968e+00,
		 2.938163982698783e+00
	};

	private static final double[] _INV_CUMULATIVE_D = new double[] {
		7.784695709041462e-03,
		3.224671290700398e-01,
		2.445134137142996e+00,
		3.754408661907416e+00
	};

	private double _dblMean = java.lang.Double.NaN;
	private double _dblVariance = java.lang.Double.NaN;

	private double cumulativeCanonical (
		final double dblXCanonical)
	{
		if (6. < dblXCanonical) return 1.;

		if (-6. > dblXCanonical) return 0.;

		double dblT = 1. / (1. + java.lang.Math.abs (dblXCanonical) * _CUMULATIVE_P);

		double dblCumulative = _CUMULATIVE_C2 * java.lang.Math.exp (- dblXCanonical * dblXCanonical / 2.) *
			((((_CUMULATIVE_B5 * dblT + _CUMULATIVE_B4) * dblT + _CUMULATIVE_B3) * dblT + _CUMULATIVE_B2) *
				dblT + _CUMULATIVE_B1) * dblT;

		return (0. > dblXCanonical) ? dblCumulative : 1. - dblCumulative;
	}

	private double invCumulativeCanonical (
		final double dblXCanonical)
	{
	    if (dblXCanonical < _INV_CUMULATIVE_PLOW) {
            double dblQ = java.lang.Math.sqrt (-2. * java.lang.Math.log (dblXCanonical));

            return (((((_INV_CUMULATIVE_C[0] * dblQ + _INV_CUMULATIVE_C[1]) * dblQ + _INV_CUMULATIVE_C[2]) *
            	dblQ + _INV_CUMULATIVE_C[3]) * dblQ + _INV_CUMULATIVE_C[4]) * dblQ + _INV_CUMULATIVE_C[5]) /
            		((((_INV_CUMULATIVE_D[0] * dblQ + _INV_CUMULATIVE_D[1]) * dblQ + _INV_CUMULATIVE_D[2]) *
            			dblQ + _INV_CUMULATIVE_D[3]) * dblQ + 1.);
	    }

	    if (_INV_CUMULATIVE_PHIGH < dblXCanonical) {
            double dblQ = java.lang.Math.sqrt (-2. * java.lang.Math.log (1. - dblXCanonical));

            return -(((((_INV_CUMULATIVE_C[0] * dblQ + _INV_CUMULATIVE_C[1]) * dblQ + _INV_CUMULATIVE_C[2]) *
            	dblQ + _INV_CUMULATIVE_C[3]) * dblQ + _INV_CUMULATIVE_C[4]) * dblQ + _INV_CUMULATIVE_C[5]) /
	                ((((_INV_CUMULATIVE_D[0] * dblQ + _INV_CUMULATIVE_D[1]) * dblQ + _INV_CUMULATIVE_D[2]) *
	                	dblQ + _INV_CUMULATIVE_D[3]) * dblQ + 1.);
	    }

	    double dblQ = dblXCanonical - 0.5;
	    double dblR = dblQ * dblQ;
	    return (((((_INV_CUMULATIVE_A[0] * dblR + _INV_CUMULATIVE_A[1]) * dblR + _INV_CUMULATIVE_A[2]) * dblR
	    	+ _INV_CUMULATIVE_A[3]) * dblR + _INV_CUMULATIVE_A[4]) * dblR + _INV_CUMULATIVE_A[5]) * dblQ /
	    		(((((_INV_CUMULATIVE_B[0] * dblR + _INV_CUMULATIVE_B[1]) * dblR + _INV_CUMULATIVE_B[2]) *
	    			dblR + _INV_CUMULATIVE_B[3]) * dblR + _INV_CUMULATIVE_B[4]) * dblR + 1);
	}

	public static final org.drip.math.distribution.Univariate GenerateStandardNormal() {
		try {
			return new UnivariateNormal (0., 1.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public UnivariateNormal (
		final double dblMean,
		final double dblVariance)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (_dblMean = dblMean) || java.lang.Double.isNaN (_dblVariance =
			dblVariance) || 0. >= _dblVariance)
			throw new java.lang.Exception ("UnivariateNormal constructor: Invalid inputs");
	}

	@Override public double cumulative (
		final double dblX)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblX))
			throw new java.lang.Exception ("UnivariateNormal.cumulative => Invalid inputs");

		return cumulativeCanonical ((dblX - _dblMean) / java.lang.Math.sqrt (_dblVariance));
	}

	@Override public double incremental (
		final double dblXLeft,
		final double dblXRight)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblXLeft) || java.lang.Double.isNaN (dblXRight) || dblXLeft > dblXRight)
			throw new java.lang.Exception ("UnivariateNormal.incremental => Invalid inputs");

		return cumulative (dblXRight) - cumulative (dblXLeft);
	}

	@Override public double invCumulative (
		final double dblX)
		throws java.lang.Exception
	{
		if (java.lang.Double.isNaN (dblX))
			throw new java.lang.Exception ("UnivariateNormal.invCumulative => Invalid inputs");

	    return invCumulativeCanonical (dblX) * java.lang.Math.sqrt (_dblVariance) + _dblMean;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		UnivariateNormal nd = new UnivariateNormal (10., 10.);

		double dblX = 10.;

		double dblCumulative = nd.cumulative (dblX);

		System.out.println ("Cumulative: " + dblCumulative);

		System.out.println ("Incremental: " + nd.incremental (9., 11.));

		System.out.println ("Inverse Cumulative: " + nd.invCumulative (dblCumulative));
	}
}
