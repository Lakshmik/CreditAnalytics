
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
 * This class provides the control and frame-work functionality for the General Purpose Regression Suite.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RegressionEngine {
	/**
	 * Regression outputs decomposed at individual Module Units 
	 */

	public static final int REGRESSION_DETAIL_MODULE_UNIT_DECOMPOSED = 1;

	/**
	 * Regression outputs rolled up to Module Units
	 */

	public static final int REGRESSION_DETAIL_MODULE_UNIT_AGGREGATED = 2;

	/**
	 * Regression outputs rolled up to Modules 
	 */

	public static final int REGRESSION_DETAIL_MODULE_AGGREGATED = 4;

	/**
	 * Regression Output: Statistics
	 */

	public static final int REGRESSION_DETAIL_STATS = 8;

	private int _iNumRuns = 0;
	private int _iRegressionDetail = REGRESSION_DETAIL_MODULE_UNIT_DECOMPOSED;

	protected java.util.Set<org.drip.regression.core.RegressorSet> _setRS = new
		java.util.HashSet<org.drip.regression.core.RegressorSet>();

	protected java.util.Map<java.lang.String, org.drip.regression.core.UnitRegressionStat> _mapURS = new
		java.util.TreeMap<java.lang.String, org.drip.regression.core.UnitRegressionStat>();

	private boolean executeRegressionSet (
		final org.drip.regression.core.RegressorSet rs)
	{
		if (null == rs || null == rs.getSetName() || rs.getSetName().isEmpty()) return false;

		java.util.List<org.drip.regression.core.UnitRegressor> lsRegressor = rs.getRegressorSet();

		if (null == lsRegressor || 0 == lsRegressor.size()) {
			System.out.println ("Cannot get the " + rs.getSetName() + " scenarios!");

			return false;
		}

		long lModuleTime = 0;
		int iNumRegressionsSucceeded = 0;
		boolean bRegressionDetail = 0 != (REGRESSION_DETAIL_MODULE_UNIT_DECOMPOSED & _iRegressionDetail) ?
			true : false;

		System.out.println ("\t" + rs.getSetName() + " starts at " + new java.util.Date());

		for (org.drip.regression.core.UnitRegressor r : lsRegressor) {
			if (null == r) continue;

			org.drip.regression.core.RegressionRunOutput ro = r.regress();

			if (null != ro && ro._bStatus) {
				++iNumRegressionsSucceeded;
				lModuleTime += ro._lExecTime;

				java.lang.String strScenarioQualifiedRegressor = rs.getSetName() + "." + r.getName();

				org.drip.regression.core.UnitRegressionStat urs = _mapURS.get
					(strScenarioQualifiedRegressor);

				if (null == urs) urs = new org.drip.regression.core.UnitRegressionStat();

				urs.addExecTime (ro._lExecTime);

				_mapURS.put (strScenarioQualifiedRegressor, urs);

				if (0 != (REGRESSION_DETAIL_MODULE_UNIT_DECOMPOSED & _iRegressionDetail) ||
					0 != (REGRESSION_DETAIL_MODULE_UNIT_AGGREGATED & _iRegressionDetail))
					System.out.println (ro.displayString (bRegressionDetail));
			}
		}

		System.out.println ("\t" + rs.getSetName() + "=> " + lModuleTime+ " (mu-s): " +
			iNumRegressionsSucceeded + " / " + lsRegressor.size() + " succeeded.");

		System.out.println ("\t" + rs.getSetName() + " ends at " + new java.util.Date() + "\n");

		return true;
	}

	/**
	 * Initializes the Regression Engine
	 * 
	 * @param iNumRuns Number of runs to be initialized with
	 * @param iRegressionDetail Detail of the regression run
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	protected RegressionEngine (
		final int iNumRuns,
		final int iRegressionDetail)
		throws java.lang.Exception
	{
		if (0 >= (_iNumRuns = iNumRuns))
			throw new java.lang.Exception ("Invalid inputs into the RegressionEngine constructor!");

		_iRegressionDetail = iRegressionDetail;
	}

	/**
	 * Adds the regressor set to the framework
	 * 
	 * @param rs Regressor Set
	 * 
	 * @return TRUE => Regressor Set successfully added
	 */

	protected final boolean addRegressorSet (
		final org.drip.regression.core.RegressorSet rs)
	{
		if (null == rs || !rs.setupRegressors()) return false;

		_setRS.add (rs);

		return true;
	}

	/**
	 * One-time initialization of the regression engine environment
	 * 
	 * @return TRUE => Regression Environment initialized successfully
	 */

	public boolean initRegressionEnv()
	{
		return true;
	}

	/**
	 * Launches the Regression Engine and executed the regression sets
	 * 
	 * @return TRUE => Launch Successful
	 */

	protected final boolean launch()
	{
		if (0 == _setRS.size() || !initRegressionEnv()) return false;

		boolean bLaunchSuccessful = true;

		for (int i = 0; i < _iNumRuns; ++i) {
			for (org.drip.regression.core.RegressorSet rs : _setRS)
				bLaunchSuccessful = executeRegressionSet (rs);
		}

		if (0 != _mapURS.size() && null != _mapURS.entrySet()) {
			for (java.util.Map.Entry<java.lang.String, org.drip.regression.core.UnitRegressionStat>
				meURS : _mapURS.entrySet()) {
				if (null == meURS || null == meURS.getKey() || meURS.getKey().isEmpty()) continue;

				org.drip.regression.core.UnitRegressionStat urs = meURS.getValue();

				if (null == urs || !urs.generateStat()) continue;

				if (0 != (REGRESSION_DETAIL_STATS & _iRegressionDetail)) {
					System.out.println ("\n--------\nStats for " + meURS.getKey() + "\n--------");

					System.out.println (urs.displayString (meURS.getKey()));
				}
			}
		}

		return bLaunchSuccessful;
	}
}
