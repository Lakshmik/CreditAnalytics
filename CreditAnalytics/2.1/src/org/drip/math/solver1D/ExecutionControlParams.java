
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
 * ExecutionControlParams holds the parameters needed for controlling the execution of the fixed point
 * 	finder.
 * 
 * ExecutionControlParams fields control the fixed point search in one of the following ways:
 * 	- Number of iterations after which the search is deemed to have failed
 * 	- Relative Objective Function Tolerance Factor which, when reached by the objective function, will
 * 		indicate that the fixed point has been reached
 * 	- Variate Convergence Factor, factor applied to the initial variate to determine the absolute convergence
 * 	- Absolute Tolerance fall-back, which is used to determine that the fixed point has been reached when the
 * 		relative tolerance factor becomes zero
 * 	- Absolute Variate Convergence Fall-back, fall-back used to determine if the variate has converged.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExecutionControlParams {
	private int _iNumIterations = 0;
	private boolean _bIsVariateConvergenceCheckEnabled = false;
	private double _dblOFGoalToleranceFactor = java.lang.Double.NaN;
	private double _dblVariateConvergenceFactor = java.lang.Double.NaN;
	private double _dblAbsoluteOFToleranceFallback = java.lang.Double.NaN;
	private double _dblAbsoluteVariateConvergenceFallback = java.lang.Double.NaN;

	/**
	 * Default Execution Control Parameters constructor
	 */

	public ExecutionControlParams()
	{
		_iNumIterations = 100;
		_dblOFGoalToleranceFactor = 1.0e-07;
		_dblVariateConvergenceFactor = 1.0e-06;
		_dblAbsoluteOFToleranceFallback = 1.0e-07;
		_dblAbsoluteVariateConvergenceFallback = 1.0e-06;
	}

	/**
	 * Execution Control Parameters constructor
	 * 
	 * @param iNumIterations Number of Iterations
	 * @param bIsVariateConvergenceCheckEnabled Flag indicating if the variate convergence check is on
	 * @param dblOFGoalToleranceFactor Tolerance factor for the OF Goal
	 * @param dblVariateConvergenceFactor Variate Convergence Factor
	 * @param dblAbsoluteOFToleranceFallback Absolute Tolerance Fall-back
	 * @param dblAbsoluteVariateConvergenceFallback Absolute Variate Convergence fall-back
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public ExecutionControlParams (
		final int iNumIterations,
		final boolean bIsVariateConvergenceCheckEnabled,
		final double dblOFGoalToleranceFactor,
		final double dblVariateConvergenceFactor,
		final double dblAbsoluteOFToleranceFallback,
		final double dblAbsoluteVariateConvergenceFallback)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblOFGoalToleranceFactor = dblOFGoalToleranceFactor)
			|| !org.drip.math.common.NumberUtil.IsValid (_dblVariateConvergenceFactor =
				dblVariateConvergenceFactor) || !org.drip.math.common.NumberUtil.IsValid
					(_dblAbsoluteOFToleranceFallback = dblAbsoluteOFToleranceFallback) ||
						!org.drip.math.common.NumberUtil.IsValid (_dblAbsoluteVariateConvergenceFallback =
							dblAbsoluteVariateConvergenceFallback) || 0 >= (_iNumIterations =
								iNumIterations))
			throw new java.lang.Exception ("ExecutionControlParams constructor: Invalid inputs");

		_bIsVariateConvergenceCheckEnabled = bIsVariateConvergenceCheckEnabled;
	}

	/**
	 * Return the number of iterations allowed
	 * 
	 * @return Number of iterations
	 */

	public int getNumIterations()
	{
		return _iNumIterations;
	}

	/**
	 * Return the tolerance factor for the OF Goal
	 * 
	 * @return Tolerance factor for the OF Goal
	 */

	public double getOFGoalToleranceFactor()
	{
		return _dblOFGoalToleranceFactor;
	}

	/**
	 * Return the Variate Convergence Factor
	 * 
	 * @return Variate Convergence Factor
	 */

	public double getVariateConvergenceFactor()
	{
		return _dblVariateConvergenceFactor;
	}

	/**
	 * Return the Fall-back absolute tolerance for the OF
	 * 
	 * @return Fall-back absolute tolerance for the OF
	 */

	public double getAbsoluteOFToleranceFallback()
	{
		return _dblAbsoluteOFToleranceFallback;
	}

	/**
	 * Return the fall-back absolute variate convergence
	 * 
	 * @return Fall-back absolute variate convergence
	 */

	public double getAbsoluteVariateConvergenceFallback()
	{
		return _dblAbsoluteVariateConvergenceFallback;
	}

	/**
	 * Indicates if the variate convergence check has been turned on
	 * 
	 * @return TRUE => Variate convergence check has been turned on
	 */

	public boolean isVariateConvergenceCheckEnabled()
	{
		return _bIsVariateConvergenceCheckEnabled;
	}
}
