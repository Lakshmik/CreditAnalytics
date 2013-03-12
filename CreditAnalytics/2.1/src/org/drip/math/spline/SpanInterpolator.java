
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
 * This class implements the basis spline interpolator. It calibrates across spline segments.
 *
 * @author Lakshmi Krishnamurthy
 */

public class SpanInterpolator extends org.drip.math.algodiff.ObjectiveFunction {
	private static final double X_MULT = 1.;

	/**
	 * Linear Polynomial Spline
	 */

	public static final java.lang.String BASIS_SPLINE_LINEAR_POLYNOMIAL = "LinearPolynomial";

	/**
	 * Cubic Polynomial Spline
	 */

	public static final java.lang.String BASIS_SPLINE_CUBIC_POLYNOMIAL = "CubicPolynomial";

	/**
	 * Quartic Polynomial Spline
	 */

	public static final java.lang.String BASIS_SPLINE_QUARTIC_POLYNOMIAL = "QuarticPolynomial";

	/**
	 * Hyperbolic Tension Spline
	 */

	public static final java.lang.String BASIS_SPLINE_HYPERBOLIC_TENSION = "HyperbolicTension";

	/**
	 * Exponential Tension Spline
	 */

	public static final java.lang.String BASIS_SPLINE_EXPONENTIAL_TENSION = "ExponentialTension";

	/**
	 * Calibration Mode: Natural Boundary Condition
	 */

	public static final java.lang.String SPLINE_BOUNDARY_MODE_NATURAL = "Natural";

	/**
	 * Calibration Mode: Financial Boundary Condition
	 */

	public static final java.lang.String SPLINE_BOUNDARY_MODE_FINANCIAL = "Financial";

	/**
	 * Span Set Up Mode: Set Up ITEP
	 */

	public static final int SET_ITEP = 1;

	/**
	 * Span Set Up Mode: Calibrate SPAN
	 */

	public static final int CALIBRATE_SPAN = 2;

	/**
	 * Span Set Up Mode: Calibrate Jacobian
	 */

	public static final int CALIBRATE_JACOBIAN = 4;

	private InterpolatorTargetEvalParams _itep = null;
	private org.drip.math.algodiff.WengertJacobian _wjSpan = null;
	private org.drip.math.spline.BasisSplineSegment[] _aCSS = null;

	/**
	 * Creates a SpanInterpolator over the specified X and Y input array points, using the specified basis
	 * 		splines.
	 * 
	 * @param adblX Input X array points
	 * @param adblY Input Y array points
	 * @param strBasisSpline Basis Spline
	 * @param bsep Basis Spline Elastic Parameters
	 * @param strCalibrationMode Calibration Mode
	 * @param iSetupMode Setup Mode
	 * 
	 * @return SpanInterpolator instance
	 */

	public static final SpanInterpolator CreateSpanInterpolator (
		final double[] adblX,
		final double[] adblY,
		final java.lang.String strBasisSpline,
		final org.drip.math.spline.BasisSplineElasticParams bsep,
		final java.lang.String strCalibrationMode,
		final int iSetupMode)
	{
		if (null == adblX || null == adblY || 0 == adblX.length || adblX.length != adblY.length || null ==
			strBasisSpline || (!BASIS_SPLINE_LINEAR_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
				!BASIS_SPLINE_CUBIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
					!BASIS_SPLINE_QUARTIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
						!BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline) &&
							!BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline)) || null ==
								strCalibrationMode)
			return null;

		org.drip.math.spline.SpanInterpolator csi = null;
		org.drip.math.spline.BasisSplineSegment[] aCSS = new
			org.drip.math.spline.BasisSplineSegment[adblX.length - 1];

		try {
			for (int i = 1; i < adblX.length; ++i) {
				org.drip.math.spline.InelasticOrdinates io = new org.drip.math.spline.InelasticOrdinates
					(X_MULT * adblX[i - 1], X_MULT * adblX [i]);

				if (BASIS_SPLINE_LINEAR_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
					aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
						org.drip.math.spline.BasisPolynomial (io));
				else if (BASIS_SPLINE_CUBIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
					aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
						org.drip.math.spline.BasisCubicPolynomial (io));
				else if (BASIS_SPLINE_QUARTIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
					aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
						org.drip.math.spline.BasisQuarticPolynomial (io));
				else if (BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline))
					aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
						org.drip.math.spline.BasisHyperbolicTension (io, bsep));
				else if (BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline))
					aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
						org.drip.math.spline.BasisExponentialTension (io, bsep));
			}

			csi = new org.drip.math.spline.SpanInterpolator (aCSS);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (!csi.setup (adblY, strCalibrationMode, iSetupMode)) return null;

		return csi;
	}

	/**
	 * Creates a BasisInterpolator from an array of X points and a flat Y point
	 * 
	 * @param adblX X Array
	 * @param dblY Flat Y Input
	 * @param strBasisSpline Basis Spline
	 * @param bsep Basis Spline Elastic Parameters
	 * @param strCalibrationMode Calibration Mode
	 * @param iSetupMode Setup Mode
	 * 
	 * @return Instance of BasisInterpolator
	 */

	public static final SpanInterpolator CreateSpanInterpolator (
		final double[] adblX,
		final double dblY,
		final java.lang.String strBasisSpline,
		final org.drip.math.spline.BasisSplineElasticParams bsep,
		final java.lang.String strCalibrationMode,
		final int iSetupMode)
	{
		if (null == adblX || 0 == adblX.length || !org.drip.math.common.NumberUtil.IsValid (dblY) || null ==
			strCalibrationMode)
			return null;

		double[] adblY = new double[adblX.length];

		for (int i = 0; i < adblX.length; ++i)
			adblY[i] = dblY;

		return org.drip.math.spline.SpanInterpolator.CreateSpanInterpolator (adblX, adblY, strBasisSpline,
			bsep, strCalibrationMode, iSetupMode);
	}

	private SpanInterpolator (
		final org.drip.math.spline.BasisSplineSegment[] aBSS)
		throws java.lang.Exception
	{
		super (null);

		if (null == (_aCSS = aBSS) || 0 == _aCSS.length)
			throw new java.lang.Exception ("SpanInterpolator ctr: Invalid inputs");
	}

	private boolean initStartingSegment (
		final double dblLeftSlope)
	{
		return _aCSS[0].getElastics().calibrate (_itep._adblNodeValues[0], dblLeftSlope,
			_itep._adblNodeValues[1]);
	}

	private boolean calibSegmentFromRightNodeValue (
		final int iSegment,
		final double[] adblCalibValue)
	{
		if (0 == iSegment) return _aCSS[0].getElastics().calibrate (null, adblCalibValue[1]);

		return _aCSS[iSegment].getElastics().calibrate (_aCSS[iSegment - 1].getElastics(),
			adblCalibValue[iSegment + 1]);
	}

	private boolean calibSegmentElastics (
		final int iStartingSegment,
		final double[] adblCalibValue)
	{
		for (int iSegment = iStartingSegment; iSegment < _aCSS.length; ++iSegment) {
			if (!calibSegmentFromRightNodeValue (iSegment, adblCalibValue)) return false;
		}

		return true;
	}

	private boolean setSpanJacobian (
		final int iNodeIndex,
		final org.drip.math.algodiff.WengertJacobian wjSegment)
	{
		if (null == wjSegment) return false;

		int iParameterIndex = 2;

		if (0 == iNodeIndex) iParameterIndex = 0;

		if (!_wjSpan.accumulatePartialFirstDerivative (0, iNodeIndex, wjSegment.getFirstDerivative (0,
			iParameterIndex)))
			return false;

		if (!_wjSpan.accumulatePartialFirstDerivative (1, iNodeIndex, wjSegment.getFirstDerivative (1,
			iParameterIndex)))
			return false;

		if (!_wjSpan.accumulatePartialFirstDerivative (2, iNodeIndex, wjSegment.getFirstDerivative (2,
			iParameterIndex)))
			return false;

		return _wjSpan.accumulatePartialFirstDerivative (3, iNodeIndex, wjSegment.getFirstDerivative (3,
			iParameterIndex));
	}

	private final org.drip.math.algodiff.WengertJacobian setJacobian (
		final int iNodeIndex,
		final org.drip.math.algodiff.WengertJacobian wjSegment)
	{
		if (null == wjSegment) return null;

		org.drip.math.algodiff.WengertJacobian wjSpan = null;

		try {
			wjSpan = new org.drip.math.algodiff.WengertJacobian (1, _aCSS.length + 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i <= _aCSS.length; ++i) {
			if (i == iNodeIndex) {
				if (!wjSpan.accumulatePartialFirstDerivative (0, i, wjSegment.getFirstDerivative (0,
					org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX)) ||
						!wjSpan.accumulatePartialFirstDerivative (0, i + 1, wjSegment.getFirstDerivative (0,
							org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX)))
					return null;
			}
		}

		return wjSpan;
	}

	private boolean setup (
		final double[] adblCalibValue,
		final java.lang.String strCalibrationMode,
		final int iSetupMode)
	{
		if (0 != (SET_ITEP & iSetupMode)) {
			try {
				_itep = new InterpolatorTargetEvalParams (adblCalibValue, strCalibrationMode);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		if (0 != (CALIBRATE_SPAN & iSetupMode)) {
			org.drip.math.solver1D.FixedPointFinderOutput rfopCalib = null;

			/* try {
				rfopCalib = new org.drip.math.solver1D.FixedPointFinderNewton (0., this).findRoot();
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			} */

			if (null == rfopCalib || !rfopCalib.containsRoot()) {
				try {
					rfopCalib = new org.drip.math.solver1D.FixedPointFinderBrent (0., this).findRoot();
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return false;
				}
			}

			if (null == rfopCalib || !org.drip.math.common.NumberUtil.IsValid (rfopCalib.getRoot()))
				return false;
		}

		if (0 != (CALIBRATE_JACOBIAN & iSetupMode)) {
			try {
				if (null == (_wjSpan = new org.drip.math.algodiff.WengertJacobian
					(_aCSS[0].getElastics().numCoefficients(), _aCSS.length + 1)))
					return false;
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}

			org.drip.math.algodiff.WengertJacobian wjHead = _aCSS[0].getElastics().calcJacobian();

			if (!setSpanJacobian (0, wjHead) || !setSpanJacobian (1, wjHead)) return false;

			for (int i = 1; i < _aCSS.length; ++i) {
				if (!setSpanJacobian (i + 1, _aCSS[i].getElastics().calcJacobian())) return false;
			}
		}

		return true;
	}

	/**
	 * SpanInterpolator constructor - Constructs a sequence of basis spline segments
	 * 
	 * @param adblX Array of segment end points
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public SpanInterpolator (
		final double[] adblX,
		final java.lang.String strBasisSpline)
		throws java.lang.Exception
	{
		super (null);

		if ( null == adblX || 0 == adblX.length || null == strBasisSpline ||
			(!BASIS_SPLINE_LINEAR_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
				!BASIS_SPLINE_CUBIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
					!BASIS_SPLINE_QUARTIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
						!BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline) &&
							!BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline)))
			throw new java.lang.Exception ("SpanInterpolator ctr => Invalid inputs!");

		_aCSS = new org.drip.math.spline.BasisSplineSegment[adblX.length - 1];

		org.drip.math.spline.BasisSplineElasticParams bsep = new
			org.drip.math.spline.BasisSplineElasticParams();

		bsep.addParam ("DenormalizedTension", 1.);

		for (int i = 1; i < adblX.length; ++i) {
			org.drip.math.spline.InelasticOrdinates io = new org.drip.math.spline.InelasticOrdinates
				(X_MULT * adblX[i - 1], X_MULT * adblX [i]);

			if (BASIS_SPLINE_LINEAR_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
				_aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
					org.drip.math.spline.BasisPolynomial (io));
			else if (BASIS_SPLINE_CUBIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
				_aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
					org.drip.math.spline.BasisCubicPolynomial (io));
			else if (BASIS_SPLINE_QUARTIC_POLYNOMIAL.equalsIgnoreCase (strBasisSpline))
				_aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
					org.drip.math.spline.BasisQuarticPolynomial (io));
			else if (BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline))
				_aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
					org.drip.math.spline.BasisHyperbolicTension (io, bsep));
			else if (BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline))
				_aCSS[i - 1] = new org.drip.math.spline.BasisSplineSegment (io, new
					org.drip.math.spline.BasisExponentialTension (io, bsep));
		}
	}

	@Override public double evaluate (
		final double dblLeftSlope)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftSlope))
			throw new java.lang.Exception ("SpanInterpolator::evalTarget => Invalid inputs!");

		if (SPLINE_BOUNDARY_MODE_NATURAL.equalsIgnoreCase (_itep._strSolverMode)) {
			if (!initStartingSegment (dblLeftSlope) || !calibSegmentElastics (1, _itep._adblNodeValues))
				throw new java.lang.Exception
					("SpanInterpolator::evalTarget => cannot set segment elastics!");

			return calcTailDerivative (2);
		}

		throw new java.lang.Exception ("SpanInterpolator::evalTarget => Unknown Solver Mode " +
			_itep._strSolverMode + "!");
	}

	/**
	 * Sets the left slope
	 * 
	 * @param dblLeftValue Left most node value
	 * @param dblLeftSlope Left most node slope
	 * @param dblRightValue Left most node value
	 * 
	 * @return TRUE => Left slope successfully set
	 */

	public boolean setLeftNode (
		final double dblLeftValue,
		final double dblLeftSlope,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftValue) ||
			!org.drip.math.common.NumberUtil.IsValid (dblLeftSlope) ||
				!org.drip.math.common.NumberUtil.IsValid (dblRightValue))
			return false;

		return _aCSS[0].getElastics().calibrate (dblLeftValue, dblLeftSlope, dblRightValue);
	}

	/**
	 * Calculates the interpolated value at the given input point
	 * 
	 * @param dblXIn Input point
	 * 
	 * @return Interpolated output
	 * 
	 * @throws java.lang.Exception Thrown if the interpolation did not succeed
	 */

	public double calcValue (
		final double dblXIn)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblXIn))
			throw new java.lang.Exception ("SpanInterpolator:calcValue => Invalid inputs!");

		double dblX = X_MULT * dblXIn;

		if (_aCSS[0].getInelastics().getLeft() > dblX || _aCSS[_aCSS.length - 1].getInelastics().getRight() <
			dblX)
			throw new java.lang.Exception ("SpanInterpolator:calcValue => Input out of range!");

		int iIndex = 0;

		for (int i = 0 ; i < _aCSS.length; ++i) {
			if (_aCSS[i].getInelastics().getLeft() <= dblX && _aCSS[i].getInelastics().getRight() >= dblX) {
				iIndex = i;
				break;
			}
		}

		return _aCSS[iIndex].getElastics().calcValue (dblX);
	}

	/**
	 * Calculates the Jacobian to the inputs at the given input point
	 * 
	 * @param dblXIn Input point
	 * 
	 * @return Jacobian to the inputs
	 */

	public org.drip.math.algodiff.WengertJacobian calcValueJacobian (
		final double dblXIn)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblXIn)) return null;

		double dblX = X_MULT * dblXIn;

		if (_aCSS[0].getInelastics().getLeft() > dblX || _aCSS[_aCSS.length - 1].getInelastics().getRight() <
			dblX)
			return null;

		int iIndex = 0;

		for (int i = 0 ; i < _aCSS.length; ++i) {
			if (_aCSS[i].getInelastics().getLeft() <= dblX && _aCSS[i].getInelastics().getRight() >= dblX) {
				iIndex = i;
				break;
			}
		}

		return setJacobian (iIndex, _aCSS[iIndex].getElastics().calcValueJacobian (dblX));
	}

	/**
	 * Calculate the tail derivative of the requested order for the given node
	 * 
	 * @param iOrder Order of the derivative
	 * 
	 * @return The Tail Derivative
	 * 
	 * @throws java.lang.Exception Thrown if the derivative cannot be calculated
	 */

	public double calcTailDerivative (
		final int iOrder)
		throws java.lang.Exception
	{
		if (iOrder >= 3)
			throw new java.lang.Exception ("SpanInterpolator::calcTailDerivative => Invalid inputs!");

		org.drip.math.spline.BasisSplineSegment css = _aCSS[_aCSS.length - 1];

		return css.getElastics().calcOrderedDerivative (css.getInelastics().getRight(), iOrder, false);
	}

	/**
	 * Reset the given node with the given value
	 * 
	 * @param iNodeIndex Node whose value is set
	 * @param dblNodeValue Node Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public boolean resetNode (
		final int iNodeIndex,
		final double dblNodeValue)
	{
		if (0 == iNodeIndex || 1 == iNodeIndex || _aCSS.length < iNodeIndex ||
			!org.drip.math.common.NumberUtil.IsValid (dblNodeValue))
			return false;

		return _aCSS[iNodeIndex - 1].getElastics().calibrate (_aCSS[iNodeIndex - 2].getElastics(),
			dblNodeValue);
	}

	/**
	 * Displays the full span segment begin and end derivatives
	 * 
	 * @return Derivatives returned as a string
	 */

	public java.lang.String displayDerivatives() {
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		try {
			for (int i = 0; i < _aCSS.length; ++i) {
				org.drip.math.spline.InelasticOrdinates io = _aCSS[i].getInelastics();

				org.drip.math.spline.ElasticCoefficients ec = _aCSS[i].getElastics();

				sb.append ("\n\t\t[Derivative at " + new org.drip.analytics.date.JulianDate (io.getLeft()) +
					"]; 0th=" + org.drip.analytics.support.GenericUtil.FormatPrice (ec.calcOrderedDerivative
						(io.getLeft(), 0, true), 4, 4, 1.) + "; 1st=" +
							org.drip.analytics.support.GenericUtil.FormatPrice (ec.calcOrderedDerivative
								(io.getLeft(), 1, true), 4, 4, 1.) + "; 2nd=" +
									org.drip.analytics.support.GenericUtil.FormatPrice
										(ec.calcOrderedDerivative (io.getLeft(), 2, true), 4, 4, 1.) +
											"; 3rd=" + org.drip.analytics.support.GenericUtil.FormatPrice
												(ec.calcOrderedDerivative (io.getLeft(), 3, true), 4, 4,
													1.));

				sb.append ("\n\t\t\t" + _aCSS[i].displayString());
			}

			return sb.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public class InterpolatorTargetEvalParams {
		private double[] _adblNodeValues = null;
		private java.lang.String _strSolverMode = "";

		/**
		 * InterpolatorTargetEvalParams constructor
		 * 
		 * @param adblNodeValues node truth values
		 * @param strSolverMode Solver Mode - NATURAL | FINANCIAL
		 * 
		 * @throws java.lang.Exception Thrown if the inputs are invalid
		 */

		public InterpolatorTargetEvalParams (
			final double[] adblNodeValues,
			final java.lang.String strSolverMode)
			throws java.lang.Exception
		{
			if (null == (_adblNodeValues = adblNodeValues) || 0 == _adblNodeValues.length || null ==
				(_strSolverMode = strSolverMode) || _strSolverMode.isEmpty())
				throw new java.lang.Exception ("InterpolatorEvalParams ctr: Invalid inputs!");

			if (!SPLINE_BOUNDARY_MODE_NATURAL.equalsIgnoreCase (_strSolverMode) &&
				!SPLINE_BOUNDARY_MODE_FINANCIAL.equalsIgnoreCase (_strSolverMode))
				throw new java.lang.Exception ("InterpolatorEvalParams ctr: Unknown Solver Mode!");
		}
	}

	public static final void main (
		final java.lang.String[] astrArgs)
	{
		double[] adblX = new double[] {1.00, 1.50, 2.00, 3.00, 4.00, 5.00,  6.50,  8.00, 10.00};
		double[] adblY = new double[] {1.00, 1.50, 2.25, 4.25, 6.75, 9.25, 12.25, 14.50, 16.50};

		org.drip.math.spline.BasisSplineElasticParams bsep = new
			org.drip.math.spline.BasisSplineElasticParams();

		if (!bsep.addParam ("DenormalizedTension", 10.)) {
			System.out.println ("Cannot specify tension!");

			System.exit (304);
		}

		org.drip.math.spline.SpanInterpolator bi =
			org.drip.math.spline.SpanInterpolator.CreateSpanInterpolator (adblX, adblY,
				// BASIS_SPLINE_LINEAR_POLYNOMIAL, bsep, SPLINE_BOUNDARY_MODE_NATURAL, SET_ITEP | CALIBRATE_SPAN);
				BASIS_SPLINE_CUBIC_POLYNOMIAL, bsep, SPLINE_BOUNDARY_MODE_NATURAL, SET_ITEP | CALIBRATE_SPAN);
				// BASIS_SPLINE_QUARTIC_POLYNOMIAL, bsep, SPLINE_BOUNDARY_MODE_NATURAL, SET_ITEP | CALIBRATE_SPAN);
				// BASIS_SPLINE_EXPONENTIAL_TENSION, bsep, SPLINE_BOUNDARY_MODE_NATURAL, SET_ITEP | CALIBRATE_SPAN);
				// BASIS_SPLINE_HYPERBOLIC_TENSION, bsep, SPLINE_BOUNDARY_MODE_NATURAL, SET_ITEP | CALIBRATE_SPAN);

		double dblX = 1.;

		while (dblX <= 10.) {
			try {
				System.out.println ("Y[" + dblX + "]=" + bi.calcValue (dblX));

				System.out.println ("Jacobian Y[" + dblX + "]=" + bi.calcValueJacobian
					(dblX).displayString());
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblX += 1.;
		}
	}
}
