
package org.drip.math.spline;

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
 * This class implements basis functional parameter set. It is typically set at per segment basis.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BasisSplineElasticParams {
	private java.util.Map<java.lang.String, java.lang.Double> _mapParams = new
		java.util.HashMap<java.lang.String, java.lang.Double>();

	/**
	 * Empty BasisFunctionalParams constructor
	 */

	public BasisSplineElasticParams()
	{
	}

	/**
	 * Adds a named parameter
	 * 
	 * @param strParamName Parameter Name
	 * @param dblParamValue Parameter Value
	 * 
	 * @return TRUE => Successfully added the parameter
	 */

	public boolean addParam (
		final java.lang.String strParamName,
		final double dblParamValue)
	{
		if (null == strParamName || strParamName.isEmpty() || !org.drip.math.common.NumberUtil.IsValid
			(dblParamValue))
			return false;

		_mapParams.put (strParamName, dblParamValue);

		return true;
	}

	/**
	 * Retrieves the value for the named parameter
	 * 
	 * @param strParamName Parameter Name
	 * 
	 * @return Parameter Value
	 * 
	 * @throws java.lang.Exception Thrown if the name is invalid
	 */

	public double getParamValue (
		final java.lang.String strParamName)
		throws java.lang.Exception
	{
		if (null == strParamName || strParamName.isEmpty() || !_mapParams.containsKey (strParamName))
			throw new java.lang.Exception
				("BasisFunctionalParams::getParamValue => Invalid parameter name!");

		return _mapParams.get (strParamName);
	}

	/**
	 * Indicates if the specified named parameter is available
	 * 
	 * @param strParamName Parameter Name
	 * 
	 * @return TRUE => Parameter present
	 */

	public boolean containsParameter (
		final java.lang.String strParamName)
	{
		if (null == strParamName || strParamName.isEmpty()) return false;

		return _mapParams.containsKey (strParamName);
	}
}
