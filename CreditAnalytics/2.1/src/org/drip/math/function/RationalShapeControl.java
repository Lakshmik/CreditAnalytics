
package org.drip.math.function;

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
 * This class implements the deterministic rational shape control functionality on top of interpolating basis
 *  splines inside - [0,...,1) - Globally [x_0,...,x_1):
 * 
 * 			y = 1 / [1 + lambda * x * (1-x)]
 * 
 *		where is the normalized ordinate mapped as
 * 
 * 			x => (x - x_i-1) / (x_i - x_i-1)
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RationalShapeControl extends org.drip.math.function.AbstractUnivariate {
	private double _dblLambda = java.lang.Double.NaN;

	/**
	 * RationalShapeControl constructor
	 * 
	 * @param ep Segment Elastic Parameters
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public RationalShapeControl (
		final org.drip.math.grid.ElasticParams ep)
		throws java.lang.Exception
	{
		super (null);

		if (ep.containsParameter ("Tension"))
			_dblLambda = ep.getParamValue ("Tension");
		else if (ep.containsParameter ("NormalizedTension"))
			_dblLambda = ep.getParamValue ("NormalizedTension");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblLambda))
			throw new java.lang.Exception ("RationalShapeControl ctr: Invalid tension");
	}

	@Override public double evaluate (
		final double dblX)
		throws java.lang.Exception
	{
		return 1. / (1. + _dblLambda * dblX * (1. - dblX));
	}

	@Override public double calcDerivative (
		final double dblX,
		final int iOrder)
		throws java.lang.Exception
	{
		if (3 <= iOrder)
			throw new java.lang.Exception
				("RationalShapeControl::calcDerivative => Invalid derivative order");

		double dblD2BetaDX2 = -2. * _dblLambda;
		double dblDBetaDX = _dblLambda * (1. - 2. * dblX);
		double dblBeta = 1. + _dblLambda * dblX * (1. - dblX);
		return 1 == iOrder ? -1. * dblDBetaDX / (dblBeta * dblBeta) : (2. * dblDBetaDX - dblBeta *
			dblD2BetaDX2) / (dblBeta * dblBeta * dblBeta);
	}

	/**
	 * Retrieve the shape control coefficient
	 * 
	 * @return Shape control coefficient
	 */

	public double getShapeControlCoefficient()
	{
		return _dblLambda;
	}
}
