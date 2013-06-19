
package org.drip.math.spline;

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
 * This class implements the basis set and spline builder for the following typoes of splines:
 * 	- Exponential basis tension splines
 * 	- Hyperbolic basis tension splines
 * 	- Polynomial basis tension splines
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasisBuilder {

	/**
	 * This function implements the elastic coefficients for the segment using tension exponential basis
	 * 	splines inside - [0,...,1) - Globally [x_0,...,x_1). The segment equation is
	 * 
	 * 		y = A + B * x + C * exp (Tension * x / (x_i - x_i-1)) + D * exp (-Tension * x / (x_i - x_i-1))
	 * 
	 *	where is the normalized ordinate mapped as
	 * 
	 * 		x => (x - x_i-1) / (x_i - x_i-1)
	 * 
	 * @param dblX0 Left Ordinate
	 * @param dblX1 Right Ordinate
	 * @param dblTension The Segment Tension
	 * 
	 * @return Instance of Ck
	 */

	public static final org.drip.math.spline.Ck CreateExponentialTensionBasis (
		final double dblX0,
		final double dblX1,
		final double dblTension)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU = new
				org.drip.math.function.AbstractUnivariate[4];

			aAU[0] = new org.drip.math.function.Polynomial (0);

			aAU[1] = new org.drip.math.function.Polynomial (1);

			aAU[2] = new org.drip.math.function.ExponentialTension (java.lang.Math.E, dblTension);

			aAU[3] = new org.drip.math.function.ExponentialTension (java.lang.Math.E, -dblTension);

			return new org.drip.math.spline.Ck (dblX0, dblX1, aAU, 2);

			/* return new org.drip.math.spline.Ck (dblX0, dblX1, new org.drip.math.function.AbstractUnivariate[]
				{new org.drip.math.function.Polynomial (0), new org.drip.math.function.Polynomial (1), new
					org.drip.math.function.ExponentialTension (java.lang.Math.E, dblTension), new
						org.drip.math.function.ExponentialTension (java.lang.Math.E, dblTension)}, 2); */
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This function implements the elastic coefficients for the segment using tension hyperbolic basis
	 * 	splines inside - [0,...,1) - Globally [x_0,...,x_1). The segment equation is
	 * 
	 * 		y = A + B * x + C * sinh (Tension * x / (x_i - x_i-1)) + D * cosh (Tension * x / (x_i - x_i-1))
	 * 
	 *	where is the normalized ordinate mapped as
	 * 
	 * 		x => (x - x_i-1) / (x_i - x_i-1)
	 * 
	 * @param dblX0 Left Ordinate
	 * @param dblX1 Right Ordinate
	 * @param dblTension The Segment Tension
	 * 
	 * @return Instance of Ck
	 */

	public static final org.drip.math.spline.Ck CreateHyperbolicTensionBasis (
		final double dblX0,
		final double dblX1,
		final double dblTension)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU = new
				org.drip.math.function.AbstractUnivariate[4];

			aAU[0] = new org.drip.math.function.Polynomial (0);

			aAU[1] = new org.drip.math.function.Polynomial (1);

			aAU[2] = new org.drip.math.function.HyperbolicTension
				(org.drip.math.function.HyperbolicTension.COSH, dblTension);

			aAU[3] = new org.drip.math.function.HyperbolicTension
				(org.drip.math.function.HyperbolicTension.SINH, dblTension);

			return new org.drip.math.spline.Ck (dblX0, dblX1, aAU, 2);

			/* return new org.drip.math.spline.Ck (dblX0, dblX1, new org.drip.math.function.AbstractUnivariate[]
				{new org.drip.math.function.Polynomial (0), new org.drip.math.function.Polynomial (1), new
					org.drip.math.function.HyperbolicTension (org.drip.math.function.HyperbolicTension.COSH,
					 	dblTension), new org.drip.math.function.HyperbolicTension
					 		(org.drip.math.function.HyperbolicTension.SINH, dblTension)}, 2); */
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This class implements the elastic coefficients for the segment using polynomial basis splines inside -
	 * 		[0,...,1) - Globally [x_0,...,x_1):
	 * 
	 * 			y = Tension (A_i*x^i) i = 0,...,n (0 and n inclusive)
	 * 
	 *		where is the normalized ordinate mapped as
	 * 
	 * 			x => (x - x_i-1) / (x_i - x_i-1)
	 * 
	 * @param dblX0 Left Ordinate
	 * @param dblX1 Right Ordinate
	 * @param iNumBasis Number of Basis Functions
	 * @param iK Continuity Criterion in C_k
	 * 
	 * @return Instance of Ck
	 */

	public static final org.drip.math.spline.Ck CreatePolynomialBasis (
		final double dblX0,
		final double dblX1,
		final int iNumBasis,
		final int iK)
	{
		if (0 == iNumBasis) return null;

		org.drip.math.function.AbstractUnivariate[] aAU = new
			org.drip.math.function.AbstractUnivariate[iNumBasis];

		try {
			for (int i = 0; i < iNumBasis; ++i)
				aAU[i] = new org.drip.math.function.Polynomial (i);

			return new org.drip.math.spline.Ck (dblX0, dblX1, aAU, iK);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct BernsteinPolynomialBasis from the Bernstein polynomial basis function set
	 * 
	 * @param dblX0 Left Ordinate
	 * @param dblX1 Right Ordinate
	 * @param iNumBasis Number of Basis Functions
	 * @param iK Continuity Criterion in C_k
	 * 
	 * @return Instance of Ck
	 */

	public static final org.drip.math.spline.Ck CreateBernsteinPolynomialBasis (
		final double dblX0,
		final double dblX1,
		final int iNumBasis,
		final int iK)
	{
		if (0 == iNumBasis) return null;

		org.drip.math.function.AbstractUnivariate[] aAU = new
			org.drip.math.function.AbstractUnivariate[iNumBasis];

		try {
			for (int i = 0; i < iNumBasis; ++i)
				aAU[i] = new org.drip.math.function.BernsteinPolynomial (i, iNumBasis - 1 - i);

			return new org.drip.math.spline.Ck (dblX0, dblX1, aAU, iK);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		int iK = 1;
		int iNumBasis = 3;
		org.drip.math.grid.Segment seg1 = null;
		org.drip.math.grid.Segment seg2 = null;

		if ("Exponential".equalsIgnoreCase (astrArgs[0])) {
			seg1 = org.drip.math.spline.BasisBuilder.CreateExponentialTensionBasis (1., 5., 1.);

			seg2 = org.drip.math.spline.BasisBuilder.CreateExponentialTensionBasis (5., 9., 1.);
		} else if ("Hyperbolic".equalsIgnoreCase (astrArgs[0])) {
			seg1 = org.drip.math.spline.BasisBuilder.CreateHyperbolicTensionBasis (1., 5., 1.);

			seg2 = org.drip.math.spline.BasisBuilder.CreateHyperbolicTensionBasis (5., 9., 1.);
		} else if ("Polynomial".equalsIgnoreCase (astrArgs[0])) {
			seg1 = org.drip.math.spline.BasisBuilder.CreatePolynomialBasis (1., 5., iNumBasis, iK);

			seg2 = org.drip.math.spline.BasisBuilder.CreatePolynomialBasis (5., 9., iNumBasis, iK);
		} else if ("BernsteinPolynomial".equalsIgnoreCase (astrArgs[0])) {
			seg1 = org.drip.math.spline.BasisBuilder.CreateBernsteinPolynomialBasis (1., 5., iNumBasis, iK);

			seg2 = org.drip.math.spline.BasisBuilder.CreateBernsteinPolynomialBasis (5., 9., iNumBasis, iK);
		}

		org.drip.math.calculus.WengertJacobian wj1 = seg1.calibrateJacobian (1., 0., 7.);

		System.out.println ("Segment 1 Jacobian: " + wj1.displayString());

		System.out.println ("Segment 1 Head: " + seg1.calcJacobian().displayString());

		System.out.println ("Segment 1 Monotone Type: " + seg1.monotoneType());

		org.drip.math.calculus.WengertJacobian wj2 = seg2.calibrateJacobian (seg1, 13.);

		System.out.println ("Segment 2 Jacobian: " + wj2.displayString());

		System.out.println ("Segment 2 Regular Jacobian: " + seg2.calcJacobian().displayString());

		System.out.println ("Segment 2 Monotone Type: " + seg2.monotoneType());

		seg2.calibrate (seg1, 14.);

		double dblX = 7.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + seg2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + seg2.calcValueJacobian
			(dblX).displayString());
	}
}
