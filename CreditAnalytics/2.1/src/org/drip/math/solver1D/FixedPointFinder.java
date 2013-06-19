
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
 * FixedPointFinder is the base abstract class that is implemented by customized invocations, e.g., Newton's
 * 	method, or any of the bracketing methodologies.
 * 
 * FixedPointFinder invokes the core routine for determining the fixed point from the goal. The
 * 	ExecutionControl determines the execution termination. The initialization heuristics implements
 * 	targeted customization of the search.
 * 
 * FixedPointFinder main flow comprises of the following steps:
 * 	- Initialize the fixed point search zone by determining either a) the brackets, or b) the starting
 * 		variate.
 * 	- Compute the absolute OF tolerance that establishes the attainment of the fixed point.
 * 	- Launch the variate iterator that iterates the variate.
 * 	- Iterate until the desired tolerance has been attained
 * 	- Return the fixed point output.
 * 
 * Fixed point finders that derive from this provide implementations for the following:
 * 	- Variate initialization: They may choose either bracketing initializer, or the convergence initializer -
 * 		functionality is provided for both in this module.
 * 	- Variate Iteration: Variates are iterated using a) any of the standard primitive built-in variate
 * 		iterators (or custom ones), or b) a variate selector scheme for each iteration.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class FixedPointFinder {
	protected double _dblOFGoal = java.lang.Double.NaN;
	protected org.drip.math.solver1D.ExecutionControl _ec = null;
	protected org.drip.math.function.AbstractUnivariate _of = null;

	protected FixedPointFinder (
		final double dblOFGoal,
		final org.drip.math.function.AbstractUnivariate of,
		final org.drip.math.solver1D.ExecutionControl ec)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblOFGoal = dblOFGoal) || null == (_of = of))
			throw new java.lang.Exception ("FixedPointFinder constructor: Invalid inputs");

		_ec = new org.drip.math.solver1D.ExecutionControl (of, null);
	}

	protected abstract boolean iterateVariate (
		final org.drip.math.solver1D.IteratedVariate vi,
		final org.drip.math.solver1D.FixedPointFinderOutput rfop);

	protected abstract org.drip.math.solver1D.ExecutionInitializationOutput initializeVariateZone (
		final org.drip.math.solver1D.InitializationHeuristics ih);

	/**
	 * Invoke the solution 1D root finding sequence
	 * 
	 * @param ih Optional Initialization Heuristics
	 * 
	 * @return Root finder Solution Object for the variate
	 */

	public org.drip.math.solver1D.FixedPointFinderOutput findRoot (
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		org.drip.math.solver1D.FixedPointFinderOutput rfop = null;

		org.drip.math.solver1D.ExecutionInitializationOutput eiop = initializeVariateZone (ih);

		if (null == eiop || !eiop.isDone()) return null;

		try {
			rfop = new org.drip.math.solver1D.FixedPointFinderOutput (eiop);

			if (!rfop.incrOFCalcs()) return rfop;

			double dblOF = _of.evaluate (eiop.getStartingVariate());

			double dblAbsoluteTolerance = _ec.calcAbsoluteOFTolerance (dblOF);

			double dblAbsoluteConvergence = _ec.calcAbsoluteVariateConvergence (eiop.getStartingVariate());

			org.drip.math.solver1D.IteratedVariate iv = new org.drip.math.solver1D.IteratedVariate (eiop,
				dblOF);

			int iNumIterationsPending = _ec.getNumIterations();

			while (!_ec.hasOFReachedGoal (dblAbsoluteTolerance, iv.getOF(), _dblOFGoal)) {
				double dblPrevVariate = iv.getVariate();

				if (!rfop.incrIterations() || 0 == --iNumIterationsPending || !iterateVariate (iv, rfop))
					return rfop;

				if (_ec.isVariateConvergenceCheckEnabled() && (java.lang.Math.abs (dblPrevVariate -
					iv.getVariate()) < dblAbsoluteConvergence))
					break;
			}

			rfop.setRoot (iv.getVariate());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return rfop;
	}

	/**
	 * Invoke the solution 1D root finding sequence
	 * 
	 * @return Root finder Solution Object for the variate
	 */

	public org.drip.math.solver1D.FixedPointFinderOutput findRoot()
	{
		return findRoot (null);
	}
}
