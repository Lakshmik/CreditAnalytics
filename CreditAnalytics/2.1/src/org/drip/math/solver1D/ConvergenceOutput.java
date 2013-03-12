
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
 * ConvergenceOutput extends the ExecutionInitializationOutput by retaining the starting variate that
 * 	results from the convergence zone search.
 * 
 * ConvergenceOutput does not add any new field to ExecutionInitializationOutput.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ConvergenceOutput extends org.drip.math.solver1D.ExecutionInitializationOutput {
	/**
	 * Default ConvergenceOutput constructor: Initializes the output object
	 */

	public ConvergenceOutput()
	{
		super();
	}

	/**
	 * Initializes off of an existing EIOP
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 * 
	 * @param eiop EIOP
	 */

	public ConvergenceOutput (
		final org.drip.math.solver1D.ExecutionInitializationOutput eiop)
		throws java.lang.Exception
	{
		super (eiop);
	}

	/**
	 * Indicate that the initialization is completed
	 * 
	 * @param dblStartingVariate Starting Variate
	 * 
	 * @return TRUE => Initialization successfully completed
	 */

	public boolean done (
		final double dblStartingVariate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblStartingVariate)) return false;

		return setStartingVariate (dblStartingVariate) && done();
	}
}
