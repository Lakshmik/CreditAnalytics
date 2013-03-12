
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
 * This interface contains the elastic co-efficients of the spline segment.
 * 
 * @author Lakshmi Krishnamurthy
 */

public abstract class ElasticCoefficients {

	/**
	 * LEFT NODE VALUE PARAMETER INDEX
	 */

	static final int LEFT_NODE_VALUE_PARAMETER_INDEX = 0;

	/**
	 * RIGHT NODE VALUE PARAMETER INDEX
	 */

	static final int RIGHT_NODE_VALUE_PARAMETER_INDEX = 1;

	private double[] derivArrayFromSlope (
		final double dblSlope)
	{
		int iNumDerivs = numParameters() - 2;

		if (0 >= iNumDerivs) return null;

		double[] adblDeriv = new double[iNumDerivs];

		for (int i = 0; i < iNumDerivs; ++i)
			adblDeriv[i] = (0 == i) ? dblSlope : 0.;

		return adblDeriv;
	}

	/**
	 * Retrieves the number of coefficients
	 * 
	 * @return The Number of coefficients
	 */

	public abstract int numCoefficients();

	/**
	 * Retrieves the Number of Parameters
	 * 
	 * @return The Number of Parameters
	 */

	public abstract int numParameters();

	/**
	 * Calibrate the coefficients from the boundary values and the left derivatives set
	 * 
	 * @param dblLeftValue Left Value
	 * @param adblLeftLocalDeriv Array of Left Local Derivatives
	 * @param dblRightValue Right Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public abstract boolean calibrate (
		final double dblLeftValue,
		final double[] adblLeftLocalDeriv,
		final double dblRightValue);

	/**
	 * Calibrate the coefficients from the boundary values and the left slope
	 * 
	 * @param dblLeftValue Left Value
	 * @param dblLeftSlope Left Slope
	 * @param dblRightValue Right Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public boolean calibrate (
		final double dblLeftValue,
		final double dblLeftSlope,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftSlope)) return false;

		return calibrate (dblLeftValue, derivArrayFromSlope (dblLeftSlope), dblRightValue);
	}

	/**
	 * Calibrate the base coefficients and their Jacobians
	 * 
	 * @param dblLeftValue Left Value
	 * @param adblLeftDeriv Array of Left Derivatives
	 * @param dblRightValue Right Value
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.algodiff.WengertJacobian calibrateJacobian (
		final double dblLeftValue,
		final double[] adblLeftDeriv,
		final double dblRightValue)
	{
		if (!calibrate (dblLeftValue, adblLeftDeriv, dblRightValue)) return null;

		return calcJacobian();
	}

	/**
	 * Calibrate the base coefficients and their Jacobians
	 * 
	 * @param dblLeftValue Left Value
	 * @param dblLeftSlope Left Slope
	 * @param dblRightValue Right Value
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.algodiff.WengertJacobian calibrateJacobian (
		final double dblLeftValue,
		final double dblLeftSlope,
		final double dblRightValue)
	{
		if (!calibrate (dblLeftValue, dblLeftSlope, dblRightValue)) return null;

		return calcJacobian();
	}

	/**
	 * Calibrate the coefficients from the prior B Spline Segment and the right node value
	 * 
	 * @param ecPrev B Prior Elastic Coefficients
	 * @param dblRightValue Right End Node Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public abstract boolean calibrate (
		final ElasticCoefficients ecPrev,
		final double dblRightValue);

	/**
	 * Calculate the Jacobian for the segment
	 * 
	 * @return The Jacobian
	 */

	public abstract org.drip.math.algodiff.WengertJacobian calcJacobian();

	/**
	 * Calibrate the base coefficients and their Jacobians
	 * 
	 * @param ecPrev B Prior Elastic Coefficients
	 * @param dblRightValue Right End Node Value
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.algodiff.WengertJacobian calibrateJacobian (
		final ElasticCoefficients ecPrev,
		final double dblRightValue)
	{
		if (!calibrate (ecPrev, dblRightValue)) return null;

		return calcJacobian();
	}

	/**
	 * Retrieve the coefficient corresponding to a the given index
	 * 
	 * @param iIndex Coefficient Index
	 * @param bLocal TRUE => Get the localized transform of the coefficient; FALSE => Get the untransformed
	 * 
	 * @throws Thrown if the index is over-run
	 * 
	 * @return Retrieve the indexed coefficient
	 */

	public abstract double getIndexedCoefficient (
		final int iIndex,
		final boolean bLocal)
		throws java.lang.Exception;

	/**
	 * Calculate the ordered derivative for the node
	 * 
	 * @param dblPoint Point at which the derivatives are to be calculated
	 * @param iOrder Derivative Order
	 * @param bLocal TRUE => Get the localized transform of the derivative; FALSE => Get the untransformed
	 * 
	 * @throws Thrown if the Order is over-run
	 * 
	 * @return Retrieve the ordered derivative
	 */

	public abstract double calcOrderedDerivative (
		final double dblPoint,
		final int iOrder,
		final boolean bLocal)
		throws java.lang.Exception;

	/**
	 * Calculate the interpolated value at the given input point
	 * 
	 * @param dblPoint Input point
	 * 
	 * @return Interpolated output
	 * 
	 * @throws java.lang.Exception Thrown if the interpolation did not succeed
	 */

	public abstract double calcValue (
		final double dblPoint)
		throws java.lang.Exception;

	/**
	 * Calculate the Jacobian of the interpolated value at the given input point
	 * 
	 * @param dblPoint Input point
	 * 
	 * @return Value Jacobian
	 */

	public abstract org.drip.math.algodiff.WengertJacobian calcValueJacobian (
		final double dblPoint);

	/**
	 * Displays a string representation of the spanning state
	 * 
	 * @return The String Representation
	 */

	public abstract java.lang.String displayString();
}
