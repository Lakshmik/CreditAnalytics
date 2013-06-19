
package org.drip.regression.curve;

/*
 * Regression Suite imports 
 */

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
 * This sample provides an implementation of the RegressionEngine class.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalyticsRegressionEngine extends org.drip.regression.core.RegressionEngine {
	/**
	 * Initializes the Credit Analytics Regression Engine
	 * 
	 * @param iNumRuns Number of runs to be initialized with
	 * @param iRegressionDetail Detail of the regression run
	 * 
	 * @throws Exception Thrown from the super
	 */

	public CreditAnalyticsRegressionEngine (
		final int iNumRuns,
		final int iRegressionDetail)
		throws java.lang.Exception
	{
		super (iNumRuns, iRegressionDetail);
	}

	/*
	 * Add the implementation specific regression initializer
	 */

	@Override public boolean initRegressionEnv() {
		return super.initRegressionEnv() && org.drip.analytics.support.Logger.Init
			("c:\\Lakshmi\\BondAnal\\Config.xml") && org.drip.analytics.daycount.Convention.Init
				("c:\\Lakshmi\\BondAnal\\Config.xml");
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception {
		CreditAnalyticsRegressionEngine care = new CreditAnalyticsRegressionEngine (10,
			org.drip.regression.core.RegressionEngine.REGRESSION_DETAIL_MODULE_UNIT_DECOMPOSED);

		/*
		 * Add the regressor sets: Refer to the implementation of the corresponding regressors
		 */

		care.addRegressorSet (new CreditCurveRegressor());

		care.addRegressorSet (new DiscountCurveRegressor());

		care.addRegressorSet (new FXCurveRegressor());

		care.addRegressorSet (new ZeroCurveRegressor());

		/*
		 * Launch regression - and that's it!
		 */

		care.launch();
	}
}
