
package org.drip.math.calculus;

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
 * This class contains the Jacobian of the given set of Wengert variables to the set of parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public class WengertJacobian {
	private double[] _adblWengert = null;
	private double[][] _aadblDWengertDParameter = null;

	/**
	 * WengertJacobian constructor
	 * 
	 * @param iNumWengerts Number of Wengert variables
	 * @param iNumParameters Number of Parameters
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public WengertJacobian (
		final int iNumWengerts,
		final int iNumParameters)
		throws java.lang.Exception
	{
		if (0 >= iNumWengerts || 0 >= iNumParameters)
			throw new java.lang.Exception ("WengertJacobian constructor: Invalid inputs");

		_adblWengert = new double[iNumWengerts];
		_aadblDWengertDParameter = new double[iNumWengerts][iNumParameters];

		for (int iWengertIndex = 0; iWengertIndex < _aadblDWengertDParameter.length; ++iWengertIndex) {
			for (int iParameterIndex = 0; iParameterIndex < _aadblDWengertDParameter[0].length;
				++iParameterIndex)
				_aadblDWengertDParameter[iWengertIndex][iParameterIndex] = 0.;
		}
	}

	/**
	 * Retrieve the number of Wengert Variables
	 * 
	 * @return Number of Wengert Variables
	 */

	public int numWengerts()
	{
		return null == _adblWengert ? 0 : _adblWengert.length;
	}

	/**
	 * Retrieve the number of Parameters
	 * 
	 * @return Number of Parameters
	 */

	public int numParameters()
	{
		return (null == _aadblDWengertDParameter || null == _aadblDWengertDParameter[0]) ? 0 :
			_aadblDWengertDParameter[0].length;
	}

	/**
	 * Set the Value for the Wengert variable
	 * 
	 * @param iWengertIndex Wengert Variable Index 
	 * @param dblWengert The Value for the Wengert Variable
	 * 
	 * @return TRUE => Successfully set
	 */

	public boolean setWengert (
		final int iWengertIndex,
		final double dblWengert)
	{
		if (0 > iWengertIndex || iWengertIndex >= _adblWengert.length ||
			!org.drip.math.common.NumberUtil.IsValid (dblWengert))
			return false;

		_adblWengert[iWengertIndex] = dblWengert;
		return true;
	}

	/**
	 * Get the Value for the Wengert Variable
	 * 
	 * @param iWengertIndex Wengert Variable Index 
	 * 
	 * @return The Value for the Wengert variable
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public double getWengert (
		final int iWengertIndex)
		throws java.lang.Exception
	{
		if (0 > iWengertIndex || iWengertIndex >= _adblWengert.length)
			throw new java.lang.Exception ("Invalid Wengert Variable Index!");

		return _adblWengert[iWengertIndex];
	}

	/**
	 * Accumulate {D(Wengert)}/{D(Parameter)}
	 * 
	 * @param iWengertIndex Wengert Variable Index
	 * @param iParameterIndex Parameter Index
	 * @param dblDWengertDParameter The incremental {D(Wengert)}/{D(Parameter)}
	 * 
	 * @return TRUE => Successfully set
	 */

	public boolean accumulatePartialFirstDerivative (
		final int iWengertIndex,
		final int iParameterIndex,
		final double dblDWengertDParameter)
	{
		if (0 > iParameterIndex || iParameterIndex >= _aadblDWengertDParameter[0].length || 0 > iWengertIndex
			|| iWengertIndex >= _adblWengert.length || !org.drip.math.common.NumberUtil.IsValid
				(dblDWengertDParameter))
			return false;

		_aadblDWengertDParameter[iWengertIndex][iParameterIndex] += dblDWengertDParameter;
		return true;
	}

	/**
	 * Retrieve {D(Wengert)}/{D(Parameter)} for the Wengert and the parameter identified by their indices
	 * 
	 * @param iWengertIndex Wengert Variable Index
	 * @param iParameterIndex Parameter Index
	 * 
	 * @return {D(Wengert)}/{D(Parameter)}
	 */

	public double getFirstDerivative (
		final int iWengertIndex,
		final int iParameterIndex)
	{
		return _aadblDWengertDParameter[iWengertIndex][iParameterIndex];
	}

	/**
	 * Accumulate and merge partial entries from the other CurveWengertJacobian
	 * 
	 * @param wjOther CurveWengertJacobian to be accumulated and merged
	 * 
	 * @return TRUE => Successfully accumulated and merged
	 */

	public boolean cumulativeMerge (
		final org.drip.math.calculus.WengertJacobian wjOther)
	{
		if (null == wjOther) return false;

		for (int iWengertIndex = 0; iWengertIndex < _aadblDWengertDParameter.length; ++iWengertIndex) {
			for (int iParameterIndex = 0; iParameterIndex < _aadblDWengertDParameter[0].length;
				++iParameterIndex)
				_aadblDWengertDParameter[iWengertIndex][iParameterIndex] += wjOther.getFirstDerivative
					(iWengertIndex, iParameterIndex);
		}

		return true;
	}

	/**
	 * Scale the partial entries
	 * 
	 * @param dblScale Factor by which the partials are to be scaled by
	 * 
	 * @return TRUE => Scaling down successful
	 */

	public boolean scale (
		final double dblScale)
	{
		if (0 >= dblScale) return false;

		for (int iWengertIndex = 0; iWengertIndex < _aadblDWengertDParameter.length; ++iWengertIndex) {
			for (int iParameterIndex = 0; iParameterIndex < _aadblDWengertDParameter[0].length;
				++iParameterIndex)
				_aadblDWengertDParameter[iWengertIndex][iParameterIndex] *= dblScale;
		}

		return true;
	}

	/**
	 * Stringifies the contents of WengertJacobian
	 * 
	 * @return Stringified WengertJacobian
	 */

	public java.lang.String displayString()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		for (int iWengertIndex = 0; iWengertIndex < _aadblDWengertDParameter.length; ++iWengertIndex) {
			java.lang.StringBuffer sbDWengertDParameter = new java.lang.StringBuffer();

			sbDWengertDParameter.append ("Wengert{" + iWengertIndex + "} => [");

			for (int iParameterIndex = 0; iParameterIndex < _aadblDWengertDParameter[0].length;
				++iParameterIndex) {
				if (0 != iParameterIndex) sbDWengertDParameter.append (", ");

				sbDWengertDParameter.append (org.drip.math.common.FormatUtil.FormatDouble
					(_aadblDWengertDParameter[iWengertIndex][iParameterIndex], 1, 4, 1.));
			}

			sb.append (sbDWengertDParameter).append ("]\n");
		}

		return sb.toString();
	}
}
