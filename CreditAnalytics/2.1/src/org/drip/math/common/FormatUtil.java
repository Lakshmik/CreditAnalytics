
package org.drip.math.common;

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
 *  This class implements formatting utility functions.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FormatUtil {
	/**
	 * Pre-pad a single digit integer with zeros
	 * 
	 * @param i Integer representing the input
	 * 
	 * @return String representing the padded output
	 */

	public static final java.lang.String PrePad (
		final int i)
	{
		if (i > 9) return "" + i;

		return "0" + i;
	}

	/**
	 * Format the double input by multiplying, and then adding left and right adjustments
	 * 
	 * @param dblValue Double representing the input
	 * @param iNumLeft Integer representing the number of left justifying zeros
	 * @param iNumRight Integer representing the number of right justifying zeros
	 * @param dblMultiplier Double representing the multiplier
	 * 
	 * @return String representing the formatted input
	 */

	public static final java.lang.String FormatDouble (
		final double dblValue,
		final int iNumLeft,
		final int iNumRight,
		final double dblMultiplier)
	{
		java.lang.String strFormat = "#";

		for (int i = 0; i < iNumLeft; ++i)
			strFormat += "0";

		if (0 != iNumRight) {
			strFormat += ".";

			for (int i = 0; i < iNumRight; ++i)
				strFormat += "0";
		}

		return new java.text.DecimalFormat (strFormat).format (dblMultiplier * dblValue);
	}
}
