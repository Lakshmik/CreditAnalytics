
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
 * FixedPointFinderNewton customizes the FixedPointFinder for Open (Newton's) fixed point finder
 * 	functionality.
 * 
 * FixedPointFinderNewton applies the following customization:
 * 	- Initializes the fixed point finder by computing a starting variate in the convergence zone
 * 	- Iterating the next search variate using the Newton's method.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FixedPointFinderNewton extends org.drip.math.solver1D.FixedPointFinder {
	private org.drip.math.solver1D.ExecutionInitializer _ei = null;

	private double calcVariateOFSlope (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("FixedPointFinderNewton::calcVariateOFSlope => Invalid input!");

		org.drip.math.calculus.Differential diff = _of.calcDifferential (dblVariate, 1);

		if (null == diff)
			throw new java.lang.Exception
				("FixedPointFinderNewton::calcVariateTargetSlope => Cannot evaluate Derivative for variate "
					+ dblVariate);

		return diff.calcSlope (false);
	}

	@Override protected boolean iterateVariate (
		final org.drip.math.solver1D.IteratedVariate vi,
		final org.drip.math.solver1D.FixedPointFinderOutput rfop)
	{
		if (null == vi || null == rfop) return false;

		double dblVariate = vi.getVariate();

		try {
			double dblVariateNext = dblVariate - calcVariateOFSlope (dblVariate) * vi.getOF();

			return vi.setVariate (dblVariateNext) && vi.setOF (_of.evaluate (dblVariateNext)) &&
				rfop.incrOFDerivCalcs() && rfop.incrOFCalcs();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override protected org.drip.math.solver1D.ExecutionInitializationOutput initializeVariateZone (
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		return _ei.initializeBracket (ih, _dblOFGoal);
	}

	/**
	 * FixedPointFinderNewton constructor
	 * 
	 * @param dblOFGoal OF Goal
	 * @param of Objective Function
	 * 
	 * @throws java.lang.Exception Propogated from underneath
	 */

	public FixedPointFinderNewton (
		final double dblOFGoal,
		final org.drip.math.function.AbstractUnivariate of)
		throws java.lang.Exception
	{
		super (dblOFGoal, of, null);

		_ei = new org.drip.math.solver1D.ExecutionInitializer (_of, null);
	}
}
