
package org.drip.math.solver1D;

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
 * FixedPointFinderBracketing customizes the FixedPointFinder for bracketing based fixed point finder
 * 	functionality.
 * 
 * FixedPointFinderBracketing applies the following customization:
 * 	- Initializes the fixed point finder by computing the starting brackets
 * 	- Iterating the next search variate using one of the specified variate iterator primitives.
 * 
 * By default, FixedPointFinderBracketing does not do compound iterations of the variate using any schemes -
 * 	that is done by classes that extend it.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FixedPointFinderBracketing extends org.drip.math.solver1D.FixedPointFinder {
	protected org.drip.math.solver1D.IteratedBracket _ib = null;
	private org.drip.math.solver1D.ExecutionInitializer _ei = null;

	protected int _iIteratorPrimitive = -1;

	protected final double calcNextVariate (
		final double dblCurrentVariate,
		final double dblContraVariate,
		final double dblCurrentOF,
		final double dblContraPointOF,
		final int iIteratorPrimitive,
		final org.drip.math.solver1D.FixedPointFinderOutput rfop)
		throws java.lang.Exception
	{
		if (org.drip.math.solver1D.VariateIteratorPrimitive.BISECTION == iIteratorPrimitive)
			return org.drip.math.solver1D.VariateIteratorPrimitive.Bisection (dblCurrentVariate,
				dblContraVariate);

		if (org.drip.math.solver1D.VariateIteratorPrimitive.FALSE_POSITION == iIteratorPrimitive)
			return org.drip.math.solver1D.VariateIteratorPrimitive.FalsePosition (dblCurrentVariate,
				dblContraVariate, dblCurrentOF, dblContraPointOF);

		double dblIntermediateVariate = org.drip.math.solver1D.VariateIteratorPrimitive.Bisection
			(dblCurrentVariate, dblContraVariate);

		if (!rfop.incrOFCalcs())
			throw new java.lang.Exception
				("FixedPointFinderBracketing::calcNextVariate => Cannot increment rfop!");

		if (org.drip.math.solver1D.VariateIteratorPrimitive.QUADRATIC_INTERPOLATION == iIteratorPrimitive)
			return org.drip.math.solver1D.VariateIteratorPrimitive.QuadraticInterpolation (dblCurrentVariate,
				dblIntermediateVariate, dblContraVariate, dblCurrentOF, _of.evaluate
					(dblIntermediateVariate), dblContraPointOF);

		if (org.drip.math.solver1D.VariateIteratorPrimitive.INVERSE_QUADRATIC_INTERPOLATION ==
			iIteratorPrimitive)
			return org.drip.math.solver1D.VariateIteratorPrimitive.InverseQuadraticInterpolation
				(dblCurrentVariate, dblIntermediateVariate, dblContraVariate, dblCurrentOF, _of.evaluate
					(dblIntermediateVariate), dblContraPointOF);

		if (org.drip.math.solver1D.VariateIteratorPrimitive.RIDDER == iIteratorPrimitive)
			return org.drip.math.solver1D.VariateIteratorPrimitive.Ridder (dblCurrentVariate,
				dblIntermediateVariate, dblContraVariate, dblCurrentOF, _of.evaluate
					(dblIntermediateVariate), dblContraPointOF);

		throw new java.lang.Exception
			("FixedPointFinderBracketing.calcNextVariate => Unknown Iterator Primitive");
	}

	protected double iterateCompoundVariate (
		final double dblCurrentVariate,
		final double dblContraVariate,
		final double dblCurrentOF,
		final double dblContraPointOF,
		final org.drip.math.solver1D.FixedPointFinderOutput rfop)
		throws java.lang.Exception
	{
		return calcNextVariate (dblCurrentVariate, dblContraVariate, dblCurrentOF, dblContraPointOF,
			_iIteratorPrimitive, rfop);
	}

	@Override protected boolean iterateVariate (
		final org.drip.math.solver1D.IteratedVariate iv,
		final org.drip.math.solver1D.FixedPointFinderOutput rfop)
	{
		if (null == iv || null == rfop) return false;

		double dblContraRoot = java.lang.Double.NaN;
		double dblContraRootOF = java.lang.Double.NaN;

		double dblOF = iv.getOF();

		double dblOFLeft = _ib.getOFLeft();

		double dblOFRight = _ib.getOFRight();

		double dblVariate = iv.getVariate();

		double dblVariateLeft = _ib.getVariateLeft();

		double dblVariateRight = _ib.getVariateRight();

		if (((dblOFLeft - _dblOFGoal) * (dblOF - _dblOFGoal)) > 0.) {
			if (!_ib.setOFLeft (dblOF) || !_ib.setVariateLeft (dblVariate)) return false;

			dblContraRootOF = dblOFRight;
			dblContraRoot = dblVariateRight;
		} else if (((dblOFRight - _dblOFGoal) * (dblOF - _dblOFGoal)) > 0.) {
			if (!_ib.setOFRight (dblOF) || !_ib.setVariateRight (dblVariate)) return false;

			dblContraRootOF = dblOFLeft;
			dblContraRoot = dblVariateLeft;
		}

		try {
			dblVariate = iterateCompoundVariate (dblVariate, dblContraRoot, dblOF, dblContraRootOF, rfop);

			return iv.setVariate (dblVariate) && iv.setOF (_of.evaluate (dblVariate)) && rfop.incrOFCalcs();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override protected org.drip.math.solver1D.ExecutionInitializationOutput initializeVariateZone (
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		org.drip.math.solver1D.BracketingOutput bop = null;

		if (null != ih && org.drip.math.solver1D.InitializationHeuristics.SEARCH_HARD_BRACKETS ==
			ih.getDeterminant())
			bop = _ei.verifyHardSearchEdges (ih, _dblOFGoal);
		else
			bop = _ei.initializeBracket (ih, _dblOFGoal);

		if (null == bop || !bop.isDone()) return null;

		try {
			_ib = new org.drip.math.solver1D.IteratedBracket (bop);

			return bop;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * FixedPointFinderBracketing constructor
	 * 
	 * @param dblOFGoal OF Goal
	 * @param of Objective Function
	 * @param ec Execution Control
	 * @param iIteratorPrimitive Iterator Primitive
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public FixedPointFinderBracketing (
		final double dblOFGoal,
		final org.drip.math.function.AbstractUnivariate of,
		final org.drip.math.solver1D.ExecutionControl ec,
		final int iIteratorPrimitive)
		throws java.lang.Exception
	{
		super (dblOFGoal, of, ec);

		if (org.drip.math.solver1D.VariateIteratorPrimitive.BISECTION != (_iIteratorPrimitive =
			iIteratorPrimitive) && org.drip.math.solver1D.VariateIteratorPrimitive.FALSE_POSITION !=
				_iIteratorPrimitive &&
					org.drip.math.solver1D.VariateIteratorPrimitive.QUADRATIC_INTERPOLATION !=
						_iIteratorPrimitive &&
							org.drip.math.solver1D.VariateIteratorPrimitive.INVERSE_QUADRATIC_INTERPOLATION
								!= _iIteratorPrimitive &&
									org.drip.math.solver1D.VariateIteratorPrimitive.RIDDER !=
										_iIteratorPrimitive)
			throw new java.lang.Exception ("FixedPointFinderBracketing constructor: Invalid inputs!");

		_ei = new org.drip.math.solver1D.ExecutionInitializer (_of, null);
	}
}
