
package org.drip.math.grid;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * This abstract class contains the basis spline segment in-elastic ordinates. Interpolating segment spline
 *  functions and their coefficients are implemented/calibrated in the overriding spline classes.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class Segment extends org.drip.math.grid.Inelastics {

	/**
	 * LEFT NODE VALUE PARAMETER INDEX
	 */

	public static final int LEFT_NODE_VALUE_PARAMETER_INDEX = 0;

	/**
	 * RIGHT NODE VALUE PARAMETER INDEX
	 */

	public static final int RIGHT_NODE_VALUE_PARAMETER_INDEX = 1;

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

	protected Segment (
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception {
		super (dblLeft, dblRight);
	}

	protected abstract boolean isMonotone();

	/**
	 * Y from X
	 * 
	 * @param dblPoint X
	 * 
	 * @return Y
	 * 
	 * @throws java.lang.Exception Thrown if Y Cannot be computed.
	 */

	public abstract double y (
		final double dblPoint)
		throws java.lang.Exception;

	/**
	 * d^nY/dx^n from X
	 * 
	 * @param dblPoint X
	 * @param iOrder Order of the Derivative
	 * 
	 * @return d^nY/dx^n
	 * 
	 * @throws java.lang.Exception Thrown if d^nY/dx^n Cannot be computed.
	 */

	public abstract double derivative (
		final double dblX,
		final int iOrder)
		throws java.lang.Exception;


	/**
	 * Retrieve the number of Basis Functions
	 * 
	 * @return The Number of Basis Functions
	 */

	public abstract int numBasis();

	/**
	 * Retrieve the Number of Parameters
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
	 * Calibrate the coefficients from the prior Segment and the right node value
	 * 
	 * @param segmentPrev Prior Segment
	 * @param dblRightValue Right End Node Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public abstract boolean calibrate (
		final Segment segPrev,
		final double dblRightValue);

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

	public double calcValue (
		final double dblPoint)
		throws java.lang.Exception
	{
		return y (calcNormalizedOrdinate (dblPoint));
	}

	/**
	 * Calculate the Jacobian for the segment
	 * 
	 * @return The Jacobian
	 */

	public abstract org.drip.math.calculus.WengertJacobian calcJacobian();

	/**
	 * Calculate the Jacobian of the interpolated value at the given input point
	 * 
	 * @param dblPoint Input point
	 * 
	 * @return Value Jacobian
	 */

	public abstract org.drip.math.calculus.WengertJacobian calcValueJacobian (
		final double dblPoint);

	/**
	 * Calculate the Jacobian of the computed value to the segment elastics at the specified point
	 * 
	 * @param dblPoint Input Point
	 * 
	 * @return Value Jacobian
	 */

	public abstract org.drip.math.calculus.WengertJacobian calcValueElasticJacobian (
		final double dblPoint);

	/**
	 * Indicate whether the given segment is monotone. If monotone, may optionally indicate the nature of
	 * 	the extrema contained inside (maxima/minima/infection).
	 *  
	 * @return The monotone Type
	 */

	public org.drip.math.grid.SegmentMonotonocity monotoneType()
	{
		if (isMonotone()) {
			try {
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.MONOTONIC);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		org.drip.math.function.AbstractUnivariate ofDeriv = new org.drip.math.function.AbstractUnivariate
			(null) {
			@Override public double evaluate (
				final double dblX)
				throws java.lang.Exception
			{
				return derivative (dblX, 1);
			}

			@Override public org.drip.math.calculus.Differential calcDifferential (
				final double dblX,
				final double dblOFBase,
				final int iOrder)
			{
				try {
					double dblVariateInfinitesimal = _dc.getVariateInfinitesimal (dblX);

					return new org.drip.math.calculus.Differential (dblVariateInfinitesimal, derivative
						(dblX, iOrder) * dblVariateInfinitesimal);
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};

		try {
			org.drip.math.solver1D.FixedPointFinderOutput fpop = new
				org.drip.math.solver1D.FixedPointFinderBrent (0., ofDeriv).findRoot
					(org.drip.math.solver1D.InitializationHeuristics.FromHardSearchEdges (0., 1.));

			if (null == fpop || !fpop.containsRoot())
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.MONOTONIC);

			double dblExtremum = fpop.getRoot();

			if (!org.drip.math.common.NumberUtil.IsValid (dblExtremum) || dblExtremum <= 0. || dblExtremum >=
				1.)
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.MONOTONIC);

			double dbl2ndDeriv = derivative (dblExtremum, 2);

			if (0. > dbl2ndDeriv)
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.MAXIMA);

			if (0. < dbl2ndDeriv)
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.MINIMA);

			if (0. == dbl2ndDeriv)
				return new org.drip.math.grid.SegmentMonotonocity
					(org.drip.math.grid.SegmentMonotonocity.INFLECTION);

			return new org.drip.math.grid.SegmentMonotonocity
				(org.drip.math.grid.SegmentMonotonocity.NON_MONOTONIC);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		try {
			return new org.drip.math.grid.SegmentMonotonocity
				(org.drip.math.grid.SegmentMonotonocity.MONOTONIC);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

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

	public org.drip.math.calculus.WengertJacobian calibrateJacobian (
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

	public org.drip.math.calculus.WengertJacobian calibrateJacobian (
		final double dblLeftValue,
		final double dblLeftSlope,
		final double dblRightValue)
	{
		if (!calibrate (dblLeftValue, dblLeftSlope, dblRightValue)) return null;

		return calcJacobian();
	}

	/**
	 * Calibrate the base coefficients and their Jacobians
	 * 
	 * @param segmentPrev Prior Segment
	 * @param dblRightValue Right End Node Value
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian calibrateJacobian (
		final Segment segmentPrev,
		final double dblRightValue)
	{
		if (!calibrate (segmentPrev, dblRightValue)) return null;

		return calcJacobian();
	}

	/**
	 * Display the string representation for diagnostic purposes
	 * 
	 * @return The string representation
	 */

	public abstract java.lang.String displayString();
}
