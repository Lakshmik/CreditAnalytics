
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
 * BracketingOutput carries the results of the bracketing initialization.
 * 
 * 	In addition to the fields of ExecutionInitializationOutput, BracketingOutput holds the left/right bracket
 *  	variates and the corresponding values for the objective function.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BracketingOutput extends org.drip.math.solver1D.ExecutionInitializationOutput {
	private double _dblOFLeft = java.lang.Double.NaN;
	private double _dblOFRight = java.lang.Double.NaN;
	private double _dblVariateLeft = java.lang.Double.NaN;
	private double _dblVariateRight = java.lang.Double.NaN;

	/**
	 * Default BracketingOutput constructor: Initializes the output
	 */

	public BracketingOutput()
	{
		super();
	}

	/**
	 * Return the left Variate
	 * 
	 * @return Left Variate
	 */

	public double getVariateLeft()
	{
		return _dblVariateLeft;
	}

	/**
	 * Return the Right Variate
	 * 
	 * @return Right Variate
	 */

	public double getVariateRight()
	{
		return _dblVariateRight;
	}

	/**
	 * Return the left OF
	 * 
	 * @return Left OF
	 */

	public double getOFLeft()
	{
		return _dblOFLeft;
	}

	/**
	 * Return the Right OF
	 * 
	 * @return Right OF
	 */

	public double getOFRight()
	{
		return _dblOFRight;
	}

	/**
	 * Set the brackets in the output object
	 * 
	 * @param dblVariateLeft Left Variate
	 * @param dblVariateRight Right Variate
	 * @param dblOFLeft Left OF
	 * @param dblOFRight Right OF
	 * @param dblStartingVariate Starting Variate
	 * 
	 * @return TRUE => Successfully set
	 */

	public boolean done (
		final double dblVariateLeft,
		final double dblVariateRight,
		final double dblOFLeft,
		final double dblOFRight,
		final double dblStartingVariate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblVariateLeft = dblVariateLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblVariateRight = dblVariateRight) ||
				!org.drip.math.common.NumberUtil.IsValid (_dblOFLeft = dblOFLeft) ||
					!org.drip.math.common.NumberUtil.IsValid (_dblOFRight = dblOFRight) ||
						!setStartingVariate (dblStartingVariate))
			return false;

		return done();
	}

	/**
	 * Makes a ConvergenceOutput for the Open Method from the bracketing output
	 * 
	 * @return The ConvergenceOutput object
	 */

	public org.drip.math.solver1D.ConvergenceOutput makeConvergenceVariate()
	{
		org.drip.math.solver1D.ConvergenceOutput cop = null;

		try {
			cop = new org.drip.math.solver1D.ConvergenceOutput (this);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return cop.done (getStartingVariate()) ? cop : null;
	}

	@Override public java.lang.String displayString()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (super.displayString());

		sb.append ("\n\t\tLeft Bracket: " + getVariateLeft());

		sb.append ("\n\t\tRight Bracket: " + getVariateRight());

		sb.append ("\n\t\tLeft OF: " + getOFLeft());

		sb.append ("\n\t\tRight OF: " + getOFRight());

		return sb.toString();
	}
}
