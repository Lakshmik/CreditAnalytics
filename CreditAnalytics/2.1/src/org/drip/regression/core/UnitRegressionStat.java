
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
 * This class provides the statistics functionality for the Unit Regressor.
 *
 * @author Lakshmi Krishnamurthy
 */

public class UnitRegressionStat {
	private long _lExecTimeMean = 0L;
	private long _lExecTimeMaximum = 0L;
	private long _lExecTimeMinimum = 0L;
	private long _lExecTimeVariance = 0L;
	private long _lInitializationDelay = 0L;
	private java.util.List<java.lang.Long> _llExecTime = null;

	/**
	 * Empty Constructor
	 */

	public UnitRegressionStat()
	{
		_llExecTime = new java.util.ArrayList<java.lang.Long>();
	}

	/**
	 * Adds another run execution time
	 * 
	 * @param lExecTime Execution Time
	 * 
	 * @return TRUE => Executed time run successfully added
	 */

	public boolean addExecTime (
		final long lExecTime)
	{
		_llExecTime.add (lExecTime);

		return true;
	}

	/**
	 * Generate the statistics across all the execution times generated
	 * 
	 * @return TRUE => Statistics successfully generated
	 */

	public boolean generateStat()
	{
		boolean bFirstRun = true;

		int iNumRuns = _llExecTime.size();

		if (0 == iNumRuns) return false;

		for (long lExecTime : _llExecTime) {
			if (bFirstRun) {
				_lExecTimeMaximum = lExecTime;
				_lExecTimeMinimum = lExecTime;
				_lInitializationDelay = lExecTime;
			} else {
				_lExecTimeMean += lExecTime;

				if (_lExecTimeMaximum < lExecTime) _lExecTimeMaximum = lExecTime;

				if (_lExecTimeMinimum > lExecTime) _lExecTimeMinimum = lExecTime;
			}

			bFirstRun = false;
		}

		_lExecTimeMean /= (iNumRuns - 1);
		bFirstRun = true;

		for (long lExecTime : _llExecTime) {
			if (!bFirstRun)
				_lExecTimeVariance += (lExecTime - _lExecTimeMean) * (lExecTime - _lExecTimeMean);

			bFirstRun = false;
		}

		_lExecTimeVariance = (long) java.lang.Math.sqrt (_lExecTimeVariance / (iNumRuns - 1));

		_lInitializationDelay -= _lExecTimeMean;
		return true;
	}

	/**
	 * Gets the number of runs for the statistics
	 * 
	 * @return Number of runs
	 */

	public int getRuns()
	{
		return _llExecTime.size();
	}

	/**
	 * Gets the Mean in the execution time
	 * 
	 * @return Execution Time Mean
	 */

	public long getMean()
	{
		return _lExecTimeMean;
	}

	/**
	 * Gets the Minimum in the execution time
	 * 
	 * @return Execution Time Minimum
	 */

	public long getMin()
	{
		return _lExecTimeMinimum;
	}

	/**
	 * Gets the Maximum in the execution time
	 * 
	 * @return Execution Time Maximum
	 */

	public long getMax()
	{
		return _lExecTimeMaximum;
	}

	/**
	 * Gets the variance in the execution time
	 * 
	 * @return Execution Time Variance
	 */

	public long getVariance()
	{
		return _lExecTimeVariance;
	}

	/**
	 * Gets the delay when the regressor is invoked for the first time
	 * 
	 * @return Initialization Delay
	 */

	public long getInitializationDelay()
	{
		return _lInitializationDelay;
	}

	/**
	 * Returns the string version of the statistics
	 * 
	 * @param strRegressionUnit Name the unit for which the regression run was done
	 * 
	 * @return String holding the content of the unit regression statistics
	 */

	public java.lang.String displayString (
		final java.lang.String strRegressionUnit)
	{
		if (null == strRegressionUnit || strRegressionUnit.isEmpty()) return null;

		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append ("\t" + strRegressionUnit + ".Stat.NumRuns=" + _llExecTime.size() + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.ExecTimeMean=" + _lExecTimeMean + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.ExecTimeMaximum=" + _lExecTimeMaximum + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.ExecTimeMinimum=" + _lExecTimeMinimum + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.ExecTimeVariance=" + _lExecTimeVariance + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.InitializationDelay=" + _lInitializationDelay + "\n");

		sb.append ("\t" + strRegressionUnit + ".Stat.ExecTimeList=" + _llExecTime.toString() + "\n");

		return sb.toString();
	}
}
