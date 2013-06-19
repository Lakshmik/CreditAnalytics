
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
 * ExecutionControl implements the core fixed point search execution control and customization functionality.
 * 
 * ExecutionControl is used for a) calculating the absolute tolerance, and b) determining whether the OF has
 * 	reached the goal.
 * 
 * ExecutionControl determines the execution termination using its ExecutionControlParams instance. 
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExecutionControl {
	private org.drip.math.solver1D.ExecutionControlParams _ecp = null;

	protected org.drip.math.function.AbstractUnivariate _of = null;

	/**
	 * ExecutionControl constructor
	 * 
	 * @param of Objective Function
	 * @param ecp Execution Control Parameters
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public ExecutionControl (
		final org.drip.math.function.AbstractUnivariate of,
		final org.drip.math.solver1D.ExecutionControlParams ecp)
		throws java.lang.Exception
	{
		if (null == (_of = of))
			throw new java.lang.Exception ("ExecutionControl constructor: Invalid inputs");

		if (null == (_ecp = ecp)) _ecp = new org.drip.math.solver1D.ExecutionControlParams();
	}

	/**
	 * Retrieve the Number of Iterations
	 * 
	 * @return Number of solver iterations
	 */

	public int getNumIterations()
	{
		return _ecp.getNumIterations();
	}

	/**
	 * Calculate the absolute OF tolerance using the initial OF value
	 * 
	 * @param dblOFInitial Initial OF Value
	 * 
	 * @return The absolute OF Tolerance
	 * 
	 * @throws java.lang.Exception Thrown if absolute tolerance cannot be calculated
	 */

	public double calcAbsoluteOFTolerance (
		final double dblOFInitial)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblOFInitial))
			throw new java.lang.Exception ("ExecutionControl::calcAbsoluteOFTolerance => Invalid inputs!");

		double dblAbsoluteTolerance = java.lang.Math.abs (dblOFInitial) * _ecp.getOFGoalToleranceFactor();

		if (!org.drip.math.common.NumberUtil.IsValid (dblAbsoluteTolerance) || dblAbsoluteTolerance <
			_ecp.getAbsoluteOFToleranceFallback())
			dblAbsoluteTolerance = _ecp.getAbsoluteOFToleranceFallback();

		return dblAbsoluteTolerance;
	}

	/**
	 * Calculate the absolute variate convergence amount using the initial variate
	 * 
	 * @param dblInitialVariate Initial Variate
	 * 
	 * @return The Absolute Variate Convergence Amount
	 * 
	 * @throws java.lang.Exception Thrown if Absolute Variate Convergence Amount cannot be calculated
	 */

	public double calcAbsoluteVariateConvergence (
		final double dblInitialVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblInitialVariate))
			throw new java.lang.Exception
				("ExecutionControl::calcAbsoluteVariateConvergence => Invalid inputs!");

		double dblAbsoluteConvergence = java.lang.Math.abs (dblInitialVariate) *
			_ecp.getVariateConvergenceFactor();

		if (!org.drip.math.common.NumberUtil.IsValid (dblAbsoluteConvergence) || dblAbsoluteConvergence <
			_ecp.getAbsoluteVariateConvergenceFallback())
			dblAbsoluteConvergence = _ecp.getAbsoluteVariateConvergenceFallback();

		return dblAbsoluteConvergence;
	}

	/**
	 * Checks to see if the OF has reached the goal
	 * 
	 * @param dblAbsoluteTolerance Absolute Tolerance
	 * @param dblOF OF Value
	 * @param dblOFGoal OF Goal
	 * 
	 * @return TRUE => If the OF has reached the goal
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public boolean hasOFReachedGoal (
		final double dblAbsoluteTolerance,
		final double dblOF,
		final double dblOFGoal)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblAbsoluteTolerance) ||
			!org.drip.math.common.NumberUtil.IsValid (dblOF) || !org.drip.math.common.NumberUtil.IsValid
				(dblOFGoal))
			throw new java.lang.Exception ("ExecutionControl::hasOFReachedGoal => Invalid inputs!");

		return dblAbsoluteTolerance > java.lang.Math.abs (dblOF - dblOFGoal);
	}

	/**
	 * Indicates if the variate convergence check has been turned on
	 * 
	 * @return TRUE => Variate convergence check has been turned on
	 */

	public boolean isVariateConvergenceCheckEnabled()
	{
		return _ecp.isVariateConvergenceCheckEnabled();
	}
}
