
package org.drip.math.sample;

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
 * This sample contains a sample illustration of usage of the Root Finder Library.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RootFinderSample {
	/*
	 * Sample illustrating the Invocation of the Newton-Raphson Open Method
	 */

	private static final void InvokeNewton (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderNewton (0., of).findRoot();

			System.out.println ("--------\nNEWTON START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nNEWTON FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Bisection Bracketing Method
	 */

	private static final void InvokeBisection (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBracketing (0., of, null,
					org.drip.math.solver1D.VariateIteratorPrimitive.BISECTION).findRoot();

			System.out.println ("--------\nBISECTION START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nBISECTION FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the False Position Method
	 */

	private static final void InvokeFalsePosition (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBracketing (0., of, null,
					org.drip.math.solver1D.VariateIteratorPrimitive.FALSE_POSITION).findRoot();

			System.out.println ("--------\nFALSE POSITION START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nFALSE POSITION FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Quadratic Interpolation Bracketing Method
	 */

	private static final void InvokeQuadraticInterpolation (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBracketing (0., of, null,
					org.drip.math.solver1D.VariateIteratorPrimitive.QUADRATIC_INTERPOLATION).findRoot();

			System.out.println ("--------\nQUADRATIC INTERPOLATION START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nQUADRATIC INTERPOLATION FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Inverse Quadratic Interpolation Bracketing Method
	 */

	private static final void InvokeInverseQuadraticInterpolation (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBracketing (0., of, null,
					org.drip.math.solver1D.VariateIteratorPrimitive.INVERSE_QUADRATIC_INTERPOLATION).findRoot();

			System.out.println ("--------\nINVERSE QUADRATIC INTERPOLATION START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nINVERSE QUADRATIC INTERPOLATION FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Ridder Bracketing Method
	 */

	private static final void InvokeRidder (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBracketing (0., of, null,
					org.drip.math.solver1D.VariateIteratorPrimitive.RIDDER).findRoot();

			System.out.println ("--------\nRIDDER START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nRIDDER FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Brent's Bracketing Method
	 */

	private static final void InvokeBrent (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBrent (0., of).findRoot();

			System.out.println ("--------\nBRENT START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nBRENT FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sample illustrating the Invocation of the Zheng's Bracketing Method
	 */

	private static final void InvokeZheng (
		final org.drip.math.function.AbstractUnivariate of)
	{
		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderZheng (0., of).findRoot();

			System.out.println ("--------\nZHENG START\n-------");

			if (null != fpop && fpop.containsRoot()) {
				System.out.println ("Root: " + org.drip.math.common.FormatUtil.FormatDouble (fpop.getRoot(),
					1, 4, 1.));

				System.out.println (fpop.displayString());
			} else
				System.out.println ("Root searched failed!");

			System.out.println ("--------\nZHENG FINISH\n-------\n");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	public static final void main (
		final java.lang.String[] astrArgs)
	{
		/*
		 * Define and implement the objective function
		 */

		org.drip.math.function.AbstractUnivariate of = new org.drip.math.function.AbstractUnivariate (null) {
			@Override public double evaluate (
				final double dblVariate)
				throws java.lang.Exception
			{
				if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
					throw new java.lang.Exception ("RootFinderSample.evalTarget => Invalid variate!");

				return java.lang.Math.cos (dblVariate) - dblVariate * dblVariate * dblVariate;

				/* return dblVariate * dblVariate * dblVariate - 3. * dblVariate * dblVariate + 2. *
					dblVariate;

				return dblVariate * dblVariate * dblVariate + 4. * dblVariate + 4.;

				return 32. * dblVariate * dblVariate * dblVariate * dblVariate * dblVariate * dblVariate
					- 48. * dblVariate * dblVariate * dblVariate * dblVariate + 18. * dblVariate *
						dblVariate - 1.;

				return 1. + 3. * dblVariate - 2. * java.lang.Math.sin (dblVariate); */
			}

			@Override public org.drip.math.calculus.Differential calcDifferential (
				final double dblVariate,
				final double dblOFBase,
				final int iOrder)
			{
				if (!org.drip.math.common.NumberUtil.IsValid (dblVariate) || 0 >= iOrder || 2 < iOrder)
					return null;

				double dblVariateInfinitesimal = java.lang.Double.NaN;

				try {
					dblVariateInfinitesimal = _dc.getVariateInfinitesimal (dblVariate);
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}

				if (1 != iOrder) {
					try {
						return new org.drip.math.calculus.Differential (dblVariateInfinitesimal, (-1. *
						 	java.lang.Math.cos (dblVariate) - 6. * dblVariate) * dblVariateInfinitesimal);

						/* return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (6. *
							dblVariate - 6.) * dblVariateInfinitesimal);

						return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (6. *
						 	dblVariate) * dblVariateInfinitesimal);

						return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (960. *
							dblVariate * dblVariate * dblVariate * dblVariate - 576. * dblVariate *
								dblVariate + 36.) * dblVariateInfinitesimal);

						return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (2. *
							java.lang.Math.sin (dblVariate)) * dblVariateInfinitesimal); */
					} catch (java.lang.Exception e) {
						e.printStackTrace();
					}

					return null;
				}

				try {
					return new org.drip.math.calculus.Differential (dblVariateInfinitesimal, (-1. *
					 	java.lang.Math.sin (dblVariate) - 3. * dblVariate * dblVariate) *
					 		dblVariateInfinitesimal);

					/* return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (3. * dblVariate
					 	* dblVariate - 6. * dblVariate + 2.) * dblVariateInfinitesimal);

					return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (3. * dblVariate
					 	* dblVariate + 4.) * dblVariateInfinitesimal);

					return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (192. *
						dblVariate * dblVariate * dblVariate * dblVariate * dblVariate - 192. * dblVariate *
							dblVariate * dblVariate + 36. * dblVariate) * dblVariateInfinitesimal);

					return new org.drip.math.autodiff.Differential (dblVariateInfinitesimal, (3. - 2. *
						java.lang.Math.cos (dblVariate)) * dblVariateInfinitesimal); */
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};

		InvokeNewton (of);

		InvokeBisection (of);

		InvokeFalsePosition (of);

		InvokeQuadraticInterpolation (of);

		InvokeInverseQuadraticInterpolation (of);

		InvokeRidder (of);

		InvokeBrent (of);

		InvokeZheng (of);
	}
}
