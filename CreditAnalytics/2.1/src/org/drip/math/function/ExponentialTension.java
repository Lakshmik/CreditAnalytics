
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
 * This class provides the evaluation of the Exponential Tension Function and its derivatives for a specified
 * 	variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExponentialTension extends org.drip.math.function.AbstractUnivariate {
	private boolean _bIsBaseNatural = false;
	private double _dblBase = java.lang.Double.NaN;
	private double _dblTension = java.lang.Double.NaN;

	/**
	 * ExponentialTension constructor
	 * 
	 * @param dblBase Base of the ExponentialTension Function
	 * @param dblTension Tension of the ExponentialTension Function
	 * 
	 * @throws java.lang.Exception Thrown if the input is invalid
	 */

	public ExponentialTension (
		final double dblBase,
		final double dblTension)
		throws java.lang.Exception
	{
		super (null);

		if (!org.drip.math.common.NumberUtil.IsValid (_dblBase = dblBase) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblTension = dblTension))
			throw new java.lang.Exception ("ExponentialTension ctr: Invalid Inputs");

		_bIsBaseNatural = _dblBase == java.lang.Math.E;
	}

	@Override public double evaluate (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("ExponentialTension::evaluate => Invalid Inputs");

		return _bIsBaseNatural ? java.lang.Math.exp (_dblTension * dblVariate) : java.lang.Math.pow
			(_dblBase, _dblTension * dblVariate);
	}

	@Override public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate) || 0 > iOrder)
			throw new java.lang.Exception ("ExponentialTension::calcDerivative => Invalid Inputs");

		double dblDerivFactor = 1.;

		for (int i = 0; i < iOrder; ++i)
			dblDerivFactor *= _dblTension;

		return _bIsBaseNatural ? dblDerivFactor * java.lang.Math.exp (_dblTension * dblVariate) :
			dblDerivFactor * java.lang.Math.exp (_dblTension * dblVariate);
	}

	/**
	 * Is the base natural?
	 * 
	 * @return TRUE => Base is off of natural logarithm
	 */

	public boolean isBaseNatural()
	{
		return _bIsBaseNatural;
	}

	/**
	 * Retrieve the Base
	 * 
	 * @return The Base
	 */

	public double getBase()
	{
		return _dblBase;
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
		ExponentialTension e = new ExponentialTension (java.lang.Math.E, 2.);

		System.out.println ("E[0.0] = " + e.evaluate (0.0));

		System.out.println ("E[0.5] = " + e.evaluate (0.5));

		System.out.println ("E[1.0] = " + e.evaluate (1.0));

		System.out.println ("EDeriv[0.0] = " + e.calcDerivative (0.0, 2));

		System.out.println ("EDeriv[0.5] = " + e.calcDerivative (0.5, 2));

		System.out.println ("EDeriv[1.0] = " + e.calcDerivative (1.0, 2));
	}
}
