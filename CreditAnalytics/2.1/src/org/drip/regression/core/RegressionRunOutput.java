
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
 * This class contains the output of the regression activity.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RegressionRunOutput {
	/**
	 * Execution time for the Regression Module
	 */

	public long _lExecTime = 0L;

	/**
	 * Completion Status for the Regression Module
	 */

	public boolean _bStatus = false;

	/**
	 * Completion Status for the Regression Module
	 */

	public java.lang.String _strRegressionScenarioName = "";

	/**
	 * Completion Time for the Regression Module
	 */

	public java.util.Date _dtCompletion = null;

	private org.drip.regression.core.RegressionRunDetail _rnvd = new
		org.drip.regression.core.RegressionRunDetail();

	/**
	 * Empty Regression Run Output Constructor
	 * 
	 * @param strRegressionScenarioName Regression Scenario Name
	 */

	public RegressionRunOutput (
		final java.lang.String strRegressionScenarioName)
		throws java.lang.Exception
	{
		if (null == (_strRegressionScenarioName = strRegressionScenarioName) ||
			_strRegressionScenarioName.isEmpty())
			throw new java.lang.Exception ("Invalid Regression Scenario Name!");
	}

	/**
	 * Sets the termination status for the regression output
	 * 
	 * @param bSuccess TRUE => Regression Run succeeded
	 * 
	 * @return TRUE => Termination status successfully set
	 */

	public boolean setTerminationStatus (
		final boolean bSuccess)
	{
		_dtCompletion = new java.util.Date();

		_bStatus = bSuccess;
		return true;
	}

	/**
	 * Retrieve the regression details object
	 * 
	 * @return The regression details object
	 */

	public org.drip.regression.core.RegressionRunDetail getRegressionDetail()
	{
		return _rnvd;
	}

	/**
	 * Print the contents of the regression output
	 * 
	 * @param bDetailed Display detailed output
	 * 
	 * @return String representing the Regression output
	 */

	public java.lang.String displayString (
		final boolean bDetailed)
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append ("\n");

		sb.append ("\t\t" + _strRegressionScenarioName + ".Success=").append (_bStatus).append ("\n");

		java.util.Map<java.lang.String, java.lang.String> mapDetails = _rnvd.getFieldMap();

		if (null != mapDetails && 0 != mapDetails.size() && null != mapDetails.entrySet()) {
			for (java.util.Map.Entry<java.lang.String, java.lang.String> me : mapDetails.entrySet()) {
				if (null != me && null != me.getKey() && !me.getKey().isEmpty() && null != me.getValue() &&
					!me.getValue().isEmpty())
					sb.append ("\t\t" + _strRegressionScenarioName + "." + me.getKey() + "=").append
						(me.getValue()).append ("\n");
			}
		}

		sb.append ("\t\t" + _strRegressionScenarioName + ".FinishTime=").append (_dtCompletion).append
			("\n");

		sb.append ("\t\t" + _strRegressionScenarioName + ".ExecTime=").append (_lExecTime);

		return sb.toString();
	}
}
