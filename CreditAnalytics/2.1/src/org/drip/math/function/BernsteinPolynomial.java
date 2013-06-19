
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
 * This class provides the evaluation of the BernsteinPolynomial and its derivatives for a specified
 * 	variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BernsteinPolynomial extends org.drip.math.function.AbstractUnivariate {
	class FactorPoly extends org.drip.math.function.AbstractUnivariate {
		int _iExponent = -1;

		FactorPoly (
			final int iExponent)
			throws java.lang.Exception
		{
			super (null);

			if (0 > (_iExponent = iExponent))
				throw new java.lang.Exception ("FactorPoly ctr: Invalid Inputs");
		}

		@Override public double evaluate (
			final double dblVariate)
			throws java.lang.Exception
		{
			return java.lang.Math.pow (dblVariate, _iExponent) / org.drip.math.common.NumberUtil.Factorial
				(_iExponent);
		}

		@Override public double calcDerivative (
			final double dblVariate,
			final int iOrder)
			throws java.lang.Exception
		{
			return iOrder > _iExponent ? 0. : java.lang.Math.pow (dblVariate, _iExponent - iOrder) /
				org.drip.math.common.NumberUtil.Factorial (_iExponent - iOrder);
		}
	}

	private FactorPoly _fpBase = null;
	private FactorPoly _fpComplement = null;

	/**
	 * Construct a BernsteinPolynomial instance
	 * 
	 * @param iBaseExponent Base Exponent
	 * @param iComplementExponent Complement Exponent
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public BernsteinPolynomial (
		final int iBaseExponent,
		final int iComplementExponent)
		throws java.lang.Exception
	{
		super (null);

		_fpBase = new FactorPoly (iBaseExponent);

		_fpComplement = new FactorPoly (iComplementExponent);
	}

	@Override public double evaluate (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("BernsteinPolynomial::evaluate => Invalid Input");

		return org.drip.math.common.NumberUtil.Factorial (_fpBase._iExponent + _fpComplement._iExponent) *
			_fpBase.evaluate (dblVariate) * _fpComplement.evaluate (1. - dblVariate);
	}

	@Override public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		double dblDerivative = 0.;

		for (int i = 0; i <= iOrder; ++i)
			dblDerivative += org.drip.math.common.NumberUtil.NCK (iOrder, i) * java.lang.Math.pow (-1.,
				iOrder - i) * _fpBase.calcDerivative (dblVariate, i) * _fpComplement.calcDerivative (1. -
					dblVariate, iOrder - i);

		return org.drip.math.common.NumberUtil.Factorial (_fpBase._iExponent + _fpComplement._iExponent) *
			dblDerivative;
	}

	/**
	 * Retrieve the Base Exponent
	 * 
	 * @return The Base Exponent
	 */

	public int getBaseExponent()
	{
		return _fpBase._iExponent;
	}

	/**
	 * Retrieve the Complement Exponent
	 * 
	 * @return The Complement Exponent
	 */

	public int getComplementExponent()
	{
		return _fpComplement._iExponent;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		BernsteinPolynomial bp = new BernsteinPolynomial (1, 1);

		System.out.println ("BP[0.0] = " + bp.calcDerivative (0.0, 1));

		System.out.println ("BP[0.5] = " + bp.calcDerivative (0.5, 1));

		System.out.println ("BP[1.0] = " + bp.calcDerivative (1.0, 1));
	}
}
