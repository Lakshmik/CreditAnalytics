
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
 * VariateIterationSelectorParams implements the control parameters for the compound variate selector scheme
 * 	used in Brent's method.
 * 
 * Brent's method uses the following fields in VariateIterationSelectorParams to generate the next variate:
 * 	- The Variate Primitive that is regarded as the "fast" method
 * 	- The Variate Primitive that is regarded as the "robust" method
 * 	- The relative variate shift that determines when the "robust" method is to be invoked over the "fast"
 * 	- The lower bound on the variate shift between iterations that serves as the fall-back to the "robust"
 *
 * @author Lakshmi Krishnamurthy
 */

public class VariateIterationSelectorParams {
	private int _iFastIteratorPrimitive = -1;
	private int _iRobustIteratorPrimitive = -1;
	private double _dblRelativeVariateShift = java.lang.Double.NaN;
	private double _dblVariateShiftLowerBound = java.lang.Double.NaN;

	/**
	 * Default VariateIterationSelectorParams constructor
	 */

	public VariateIterationSelectorParams()
	{
		_dblRelativeVariateShift = 0.5;
		_dblVariateShiftLowerBound = 0.01;
		_iRobustIteratorPrimitive = org.drip.math.solver1D.VariateIteratorPrimitive.BISECTION;
		_iFastIteratorPrimitive =
			org.drip.math.solver1D.VariateIteratorPrimitive.INVERSE_QUADRATIC_INTERPOLATION;
	}

	/**
	 * VariateIterationSelectorParams constructor
	 * 
	 * @param dblRelativeVariateShift Relative Variate Shift
	 * @param dblVariateShiftLowerBound Variant Shift Lower Bound
	 * @param iFastIteratorPrimitive Fast Iterator Primitive
	 * @param iRobustIteratorPrimitive Robust Iterator Primitive
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public VariateIterationSelectorParams (
		final double dblRelativeVariateShift,
		final double dblVariateShiftLowerBound,
		final int iFastIteratorPrimitive,
		final int iRobustIteratorPrimitive)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblRelativeVariateShift = dblRelativeVariateShift) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblVariateShiftLowerBound =
				dblVariateShiftLowerBound))
			throw new java.lang.Exception ("VariateIterationSelectorParams constructor: Invalid inputs!");
	}

	/**
	 * Retrieve the relative variate Shift
	 * 
	 * @return Relative variate Shift
	 */

	public double getRelativeVariateShift()
	{
		return _dblRelativeVariateShift;
	}

	/**
	 * Retrieve the Variate Shift lower bound
	 * 
	 * @return Variate Shift lower bound
	 */

	public double getVariateShiftLowerBound()
	{
		return _dblVariateShiftLowerBound;
	}

	/**
	 * Retrieve the variate iterator primitive meant for speed
	 * 
	 * @return variate iterator primitive meant for speed
	 */

	public int getFastVariateIteratorPrimitive()
	{
		return _iFastIteratorPrimitive;
	}

	/**
	 * Retrieve the variate iterator primitive meant for robustness
	 * 
	 * @return variate iterator primitive meant for robustness
	 */

	public int getRobustVariateIteratorPrimitive()
	{
		return _iRobustIteratorPrimitive;
	}
}
