
package org.drip.math.calculus;

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
 * Differential holds the incremental differentials for the variate and the objective function.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Differential {
	private double _dblDeltaOF = java.lang.Double.NaN;
	private double _dblDeltaVariate = java.lang.Double.NaN;

	/**
	 * Differential constructor
	 * 
	 * @param dblDeltaVariate Delta Variate
	 * @param dblDeltaOF Delta OF
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public Differential (
		final double dblDeltaVariate,
		final double dblDeltaOF)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblDeltaVariate = dblDeltaVariate) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblDeltaOF = dblDeltaOF))
			throw new java.lang.Exception ("Differential constructor: Invalid Inputs!");
	}

	/**
	 * Retrieve the Delta for the OF
	 * 
	 * @return Delta OF
	 */

	public double getDeltaOF()
	{
		return _dblDeltaOF;
	}

	/**
	 * Retrieve the Delta for the variate
	 * 
	 * @return Delta Variate
	 */

	public double getDeltaVariate()
	{
		return _dblDeltaVariate;
	}

	/**
	 * Retrieve the Delta for the variate
	 * 
	 * @param bOFNumerator TRUE => Calculate DOF/DVariate; FALSE => Calculate DVariate/DOF
	 * 
	 * @return Delta Variate
	 */

	public double calcSlope (
		final boolean bOFNumerator)
	{
		if (bOFNumerator) return _dblDeltaOF / _dblDeltaVariate;

		return _dblDeltaVariate / _dblDeltaOF;
	}
}
