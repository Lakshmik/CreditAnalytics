
package org.drip.analytics.definition;

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
 * This class contains the baseline abstract zero curve holder object. In addition to the discount
 * 		functionality, it provides the functionality to calculate the zero rate.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class ZeroCurve extends org.drip.analytics.definition.DiscountCurve {
	/**
	 * Retrieve the zero rate corresponding to the given date
	 * 
	 * @param dblDate Date for which the zero rate is requested
	 * 
	 * @return Zero Rate
	 * 
	 * @throws java.lang.Exception Thrown if the date is not represented in the map
	 */

	public abstract double getZeroRate (
		final double dblDate)
		throws java.lang.Exception;

	@Override public boolean initializeCalibrationRun (
		final double dblLeftSlope)
	{
		return true;
	}
}
