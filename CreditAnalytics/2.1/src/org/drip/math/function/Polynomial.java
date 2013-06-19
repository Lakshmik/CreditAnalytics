
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
 * This class provides the evaluation of the n-th order Polynomial and its derivatives for a specified
 * 	variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Polynomial extends org.drip.math.function.AbstractUnivariate {
	private int _iDegree = -1;

	/**
	 * Polynomial constructor
	 * 
	 * @param iDegree Degree of the Polynomial
	 * 
	 * @throws java.lang.Exception Thrown if the input is invalid
	 */

	public Polynomial (
		final int iDegree)
		throws java.lang.Exception
	{
		super (null);

		if (0 > (_iDegree = iDegree)) throw new java.lang.Exception ("Polynomial ctr: Invalid Inputs");
	}

	@Override public double evaluate (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("Polynomial::evaluate => Invalid Inputs");

		return java.lang.Math.pow (dblVariate, _iDegree);
	}

	@Override public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate) || 0 > iOrder)
			throw new java.lang.Exception ("Polynomial::calcDerivative => Invalid Inputs");

		return iOrder > _iDegree ? 0. : java.lang.Math.pow (dblVariate, _iDegree - iOrder) *
			org.drip.math.common.NumberUtil.NPK (_iDegree, _iDegree - iOrder);
	}

	/**
	 * Retrieve the degree of the polynomial
	 * 
	 * @return Degree of the polynomial
	 */

	public double getDegree()
	{
		 return _iDegree;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		Polynomial poly = new Polynomial (4);

		System.out.println ("Poly[0.0] = " + poly.evaluate (0.0));

		System.out.println ("Poly[0.5] = " + poly.evaluate (0.5));

		System.out.println ("Poly[1.0] = " + poly.evaluate (1.0));

		System.out.println ("Deriv[0.0] = " + poly.calcDerivative (0.0, 3));

		System.out.println ("Deriv[0.5] = " + poly.calcDerivative (0.5, 3));

		System.out.println ("Deriv[1.0] = " + poly.calcDerivative (1.0, 3));
	}
}
