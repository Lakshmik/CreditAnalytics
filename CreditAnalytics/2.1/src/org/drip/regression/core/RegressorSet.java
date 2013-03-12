
package org.drip.regression.core;

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
 * This interface provides the Regression set stubs.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface RegressorSet {
	/**
	 * Set up the list of Regressors in the set
	 * 
	 * @return TRUE if set up is successful
	 */

	public abstract boolean setupRegressors();

	/**
	 * Retrieve the list of regressors
	 * 
	 * @return List of regressors
	 */

	public abstract java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet();

	/**
	 * Retrieve the Regression Set Name
	 * 
	 * @return Regression Set Name
	 */

	public abstract java.lang.String getSetName();
}
