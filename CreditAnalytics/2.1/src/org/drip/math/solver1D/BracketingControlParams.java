
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
 * BracketingControlParams implements the control parameters for bracketing solutions.
 * 
 * 	BracketingControlParams provides the following parameters:
 * 		- The starting variate from which the search for bracketing begins
 * 		- The initial width for the brackets
 * 		- The factor by which the width expands with each iterative search
 * 		- The number of such iterations.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BracketingControlParams {
	/*
	 * Bracket Determination Parameters
	 */

	private int _iNumExpansions = 0;
	private double _dblVariateStart = java.lang.Double.NaN;
	private double _dblBracketStartingWidth = java.lang.Double.NaN;
	private double _dblBracketWidthExpansionFactor = java.lang.Double.NaN;

	/**
	 * Default BracketingControlParams constructor
	 */

	public BracketingControlParams()
	{
		_dblVariateStart = 0.;
		_iNumExpansions = 100;
		_dblBracketStartingWidth = 1.e-06;
		_dblBracketWidthExpansionFactor = 2.;
	}

	/**
	 * BracketingControlParams constructor
	 * 
	 * @param iNumExpansions Number of bracket expansions to determine the bracket
	 * @param dblVariateStart Variate start for the bracket determination
	 * @param dblBracketStartingWidth Base Bracket Width
	 * @param dblBracketWidthExpansionFactor Bracket width expansion factor
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public BracketingControlParams (
		final int iNumExpansions,
		final double dblVariateStart,
		final double dblBracketStartingWidth,
		final double dblBracketWidthExpansionFactor)
		throws java.lang.Exception
	{
		if (0 >= (_iNumExpansions = iNumExpansions) || !org.drip.math.common.NumberUtil.IsValid
			(_dblVariateStart = dblVariateStart) || !org.drip.math.common.NumberUtil.IsValid
				(_dblBracketStartingWidth = dblBracketStartingWidth) ||
					!org.drip.math.common.NumberUtil.IsValid (_dblBracketWidthExpansionFactor =
						dblBracketWidthExpansionFactor))
			throw new java.lang.Exception ("BracketingControlParams constructor: Invalid inputs!");
	}

	/**
	 * Return the number of expansions
	 * 
	 * @return Number of expansions
	 */

	public int getNumExpansions()
	{
		return _iNumExpansions;
	}

	/**
	 * Return the starting point of bracketing determination
	 * 
	 * @return Starting point of bracketing determination
	 */

	public double getVariateStart()
	{
		return _dblVariateStart;
	}

	/**
	 * Return the initial bracket width
	 * 
	 * @return Initial bracket width
	 */

	public double getStartingBracketWidth()
	{
		return _dblBracketStartingWidth;
	}

	/**
	 * Return the bracket width expansion factor
	 * 
	 * @return Bracket width expansion factor
	 */

	public double getBracketWidthExpansionFactor()
	{
		return _dblBracketWidthExpansionFactor;
	}
}
