
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
 * This class provides the evaluation of the Hyperbolic Tension Function and its derivatives for a specified
 * 	variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class HyperbolicTension extends org.drip.math.function.AbstractUnivariate {
	/**
	 * Hyperbolic Tension Function Type - sinh
	 */

	public static final int SINH = 1;

	/**
	 * Hyperbolic Tension Function Type - cosh
	 */

	public static final int COSH = 2;

	private int _iType = -1;
	private double _dblTension = java.lang.Double.NaN;

	/**
	 * HyperbolicTension constructor
	 * 
	 * @param iType Type of the HyperbolicTension Function - SINH/COSH/TANH
	 * @param dblTension Tension of the HyperbolicTension Function
	 * 
	 * @throws java.lang.Exception Thrown if the input is invalid
	 */

	public HyperbolicTension (
		final int iType,
		final double dblTension)
		throws java.lang.Exception
	{
		super (null);

		if ((SINH != (_iType = iType) && COSH != _iType) || !org.drip.math.common.NumberUtil.IsValid
			(_dblTension = dblTension))
			throw new java.lang.Exception ("HyperbolicTension ctr: Invalid Inputs");
	}

	@Override public double evaluate (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("HyperbolicTension::evaluate => Invalid Inputs");

		return SINH == _iType ? java.lang.Math.sinh (_dblTension * dblVariate) : java.lang.Math.cosh
			(_dblTension * dblVariate);
	}

	@Override public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate) || 0 > iOrder)
			throw new java.lang.Exception ("HyperbolicTension::calcDerivative => Invalid Inputs");

		double dblDerivFactor = 1.;

		for (int i = 0; i < iOrder; ++i)
			dblDerivFactor *= _dblTension;

		return (SINH == _iType) ? dblDerivFactor * (1 == iOrder % 2 ? java.lang.Math.cosh (_dblTension *
			dblVariate) : java.lang.Math.sinh (_dblTension * dblVariate)) : dblDerivFactor * (1 == iOrder % 2
				? java.lang.Math.sinh (_dblTension * dblVariate) : java.lang.Math.cosh (_dblTension *
					dblVariate));
	}

	/**
	 * Retrieve the hyperbolic function type
	 * 
	 * @return Hyperbolic function type
	 */

	public int getType()
	{
		return _iType;
	}

	/**
	 * Retrieve the Tension Parameter
	 * 
	 * @return Tension Parameter
	 */

	public double getTension()
	{
		return _dblTension;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		HyperbolicTension e = new HyperbolicTension (SINH, 2.);

		System.out.println ("E[0.0] = " + e.evaluate (0.0));

		System.out.println ("E[0.5] = " + e.evaluate (0.5));

		System.out.println ("E[1.0] = " + e.evaluate (1.0));

		System.out.println ("EDeriv[0.0] = " + e.calcDerivative (0.0, 2));

		System.out.println ("EDeriv[0.5] = " + e.calcDerivative (0.5, 2));

		System.out.println ("EDeriv[1.0] = " + e.calcDerivative (1.0, 2));
	}
}
