
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
 * This class implements the elastic coefficients for the segment using cubic polynomial basis splines.
 * 
 * 			A + B*x + C*x^2 + D*x^3
 * 
 *		where is the normalized ordinate mapped as
 * 
 * 			x => (x - x_i-1) / (x_i - x_i-1)
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasisCubicPolynomial extends org.drip.math.spline.ElasticCoefficients {

	/*
	 * Number of Wengert Variables: 4 (_dblA, _dblB, _dblC, _dblD)
	 */

	private static final int NUM_COEFFICIENTS = 4;
	private static final boolean s_bBlog = false;

	private double _dblA = java.lang.Double.NaN;
	private double _dblB = java.lang.Double.NaN;
	private double _dblC = java.lang.Double.NaN;
	private double _dblD = java.lang.Double.NaN;
	private org.drip.math.spline.InelasticOrdinates _io = null;
	private org.drip.math.algodiff.WengertJacobian _wjMicro = null; 

	/**
	 * BasisCubicPolynomial constructor
	 * 
	 * @param io Inelastic Ordinates
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public BasisCubicPolynomial (
		final org.drip.math.spline.InelasticOrdinates io)
		throws java.lang.Exception
	{
		if (null == (_io = io))
			throw new java.lang.Exception ("BasisCubicPolynomial constructor: Invalid inputs!");
	}

	@Override public int numCoefficients()
	{
		return NUM_COEFFICIENTS;
	}

	@Override public int numParameters()
	{
		return NUM_COEFFICIENTS;
	}

	@Override public boolean calibrate (
		final double dblLeftValue,
		final double[] adblLeftLocalDeriv,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftValue) || null == adblLeftLocalDeriv || 2 >
			adblLeftLocalDeriv.length || !org.drip.math.common.NumberUtil.IsValid (adblLeftLocalDeriv) ||
				!org.drip.math.common.NumberUtil.IsValid (dblRightValue))
			return false;

		_dblA = dblLeftValue;
		_dblB = adblLeftLocalDeriv[0];
		_dblC = 0.5 * adblLeftLocalDeriv[1];
		_dblD = dblRightValue - dblLeftValue - adblLeftLocalDeriv[0] - 0.5 * adblLeftLocalDeriv[1];

		try {
			if (s_bBlog) {
				System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Left: [Calc = " +
					calcValue (_io.getLeft()) + " | In = " + dblLeftValue + "]");

				System.out.println ("\tLeft Slope: " + adblLeftLocalDeriv[0] + "; Right Node: " +
					_io.getRight() + "; Right Value: " + calcValue (_io.getRight()));

				System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Right: [Calc = " +
					calcValue (_io.getRight()) + " | In = " + dblRightValue + "]");
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override public boolean calibrate (
		final ElasticCoefficients ecPrev,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblRightValue)) return false;

		try {
			if (null == ecPrev) {
				_dblD = dblRightValue - _dblA - _dblB - _dblC;

				if (s_bBlog) {
					System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Left: [Calc = "
						+ calcValue (_io.getLeft()) + "]");

					System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Right: [Calc = "
						+ calcValue (_io.getRight()) + " | In = " + dblRightValue + "]");
				}

				return true;
			}

			try {
				if (!calibrate (ecPrev.calcValue (_io.getLeft()), new double[] {ecPrev.calcOrderedDerivative
					(_io.getLeft(), 1, false), ecPrev.calcOrderedDerivative (_io.getLeft(), 2, false)},
						dblRightValue))
					return false;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			if (s_bBlog) {
				System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Left: [Calc = " +
					calcValue (_io.getLeft()) + "]");

				System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Right: [Calc = " +
					calcValue (_io.getRight()) + " | In = " + dblRightValue + "]");
			}

			return true;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcJacobian()
	{
		if (null != _wjMicro) return _wjMicro;

		try {
			_wjMicro = new org.drip.math.algodiff.WengertJacobian (NUM_COEFFICIENTS, NUM_COEFFICIENTS);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (!_wjMicro.setWengert (0, _dblA) || !_wjMicro.setWengert (1, _dblB) || !_wjMicro.setWengert (2,
			_dblC) || !_wjMicro.setWengert (3, _dblD))
			return null;

		/*
		 * dA/dVLeft, dA/dVRight, dA/dVFirst, dA/dVSecond
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (0, 0, 1.) ||
			!_wjMicro.accumulatePartialFirstDerivative (0, 1, 0.) ||
				!_wjMicro.accumulatePartialFirstDerivative (0, 2, 0.) ||
					!_wjMicro.accumulatePartialFirstDerivative (0, 3, 0.))
			return null;

		/*
		 * dB/dVLeft, dB/dVRight, dB/dVFirst, dB/dVSecond
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (1, 0, 0.) ||
			!_wjMicro.accumulatePartialFirstDerivative (1, 1, 0.) ||
				!_wjMicro.accumulatePartialFirstDerivative (1, 2, 1.) ||
					!_wjMicro.accumulatePartialFirstDerivative (1, 3, 0.))
			return null;

		/*
		 * dC/dVLeft, dC/dVRight, dC/dVFirst, dC/dVSecond
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (2, 0, 0.) ||
			!_wjMicro.accumulatePartialFirstDerivative (2, 1, 0.) ||
				!_wjMicro.accumulatePartialFirstDerivative (2, 2, 0.) ||
					!_wjMicro.accumulatePartialFirstDerivative (2, 3, 0.5))
			return null;

		/*
		 * dD/dVLeft, dD/dVRight, dD/dVFirst, dD/dVSecond
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (3, 0, -1.) ||
			!_wjMicro.accumulatePartialFirstDerivative (3, 1, 1.) ||
				!_wjMicro.accumulatePartialFirstDerivative (3, 2, -1.) ||
					!_wjMicro.accumulatePartialFirstDerivative (3, 3, -0.5))
			return null;

		return _wjMicro;
	}

	@Override public double getIndexedCoefficient (
		final int iIndex,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (4 <= iIndex)
			throw new java.lang.Exception
				("CubicPolynomialBasis.getIndexedCoefficient => Invalid index for the co-efficient!");

		if (bLocal) {
			if (3 == iIndex) return _dblD;

			if (2 == iIndex) return _dblC;

			if (1 == iIndex) return _dblB;

			return _dblA;
		}

		double dblLeft = _io.getLeft();

		double dblWidth = _io.getSpan();

		if (3 == iIndex) return _dblD / dblWidth / dblWidth / dblWidth;

		if (2 == iIndex)
			return (_dblC / dblWidth / dblWidth) - (3. * _dblD * dblLeft / dblWidth / dblWidth / dblWidth);

		if (1 == iIndex)
			return (_dblB / dblWidth) - (2. * _dblC * dblLeft / dblWidth / dblWidth) + (3. * _dblD * dblLeft
				* dblLeft / dblWidth / dblWidth / dblWidth);

		return _dblA - (_dblB * dblLeft / dblWidth) + (_dblC * dblLeft * dblLeft / dblWidth / dblWidth) -
			(_dblD * dblLeft * dblLeft * dblLeft / dblWidth / dblWidth / dblWidth);
	}

	@Override public double calcOrderedDerivative (
		final double dblPoint,
		final int iOrder,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (4 <= iOrder || !_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("CubicPolynomialBasis.calcOrderedDerivative: Invalid inputs!");

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		if (bLocal) {
			if (3 == iOrder) return _dblD;

			if (2 == iOrder) return _dblC + 3. * _dblD * dblNormalizedPoint;

			if (1 == iOrder)
				return _dblB + 2. * _dblC * dblNormalizedPoint + 3. * _dblD * dblNormalizedPoint *
					dblNormalizedPoint;

			return _dblA + _dblB * dblNormalizedPoint + _dblC * dblNormalizedPoint * dblNormalizedPoint +
				_dblD * dblNormalizedPoint * dblNormalizedPoint * dblNormalizedPoint;
		}

		double dblWidth = _io.getSpan();

		if (3 == iOrder) return _dblD / dblWidth / dblWidth / dblWidth;

		if (2 == iOrder) return (_dblC + 3. * _dblD * dblNormalizedPoint) / dblWidth / dblWidth;

		if (1 == iOrder)
			return (_dblB + 2. * _dblC * dblNormalizedPoint + 3. * _dblD * dblNormalizedPoint *
				dblNormalizedPoint) / dblWidth;

		return _dblA + _dblB * dblNormalizedPoint + _dblC * dblNormalizedPoint * dblNormalizedPoint + _dblD *
			dblNormalizedPoint * dblNormalizedPoint * dblNormalizedPoint;
	}

	@Override public double calcValue (
		final double dblPoint)
		throws java.lang.Exception
	{
		if (!_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("CubicPolynomialBasis.calcValue => Invalid input!");

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		return _dblA + _dblB * dblNormalizedPoint + _dblC * dblNormalizedPoint * dblNormalizedPoint + _dblD *
			dblNormalizedPoint * dblNormalizedPoint * dblNormalizedPoint;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcValueJacobian (
		final double dblPoint)
	{
		if (!_io.isInSegment (dblPoint)) return null;

		try {
			double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

			double dblDVDA = 1.;
			double dblDVDB = dblNormalizedPoint;
			double dblDVDC = dblNormalizedPoint * dblNormalizedPoint;
			double dblDVDD = dblNormalizedPoint * dblNormalizedPoint * dblNormalizedPoint;

			org.drip.math.algodiff.WengertJacobian wj = (null == _wjMicro) ? calcJacobian() : _wjMicro;

			double dblDADLeftNode = wj.getFirstDerivative (0, 0);

			double dblDBDLeftNode = wj.getFirstDerivative (1, 0);

			double dblDCDLeftNode = wj.getFirstDerivative (2, 0);

			double dblDDDLeftNode = wj.getFirstDerivative (3, 0);

			double dblDADRightNode = wj.getFirstDerivative (0, 1);

			double dblDBDRightNode = wj.getFirstDerivative (1, 1);

			double dblDCDRightNode = wj.getFirstDerivative (2, 1);

			double dblDDDRightNode = wj.getFirstDerivative (3, 1);

			double dblDADLeftFirst = wj.getFirstDerivative (0, 2);

			double dblDBDLeftFirst = wj.getFirstDerivative (1, 2);

			double dblDCDLeftFirst = wj.getFirstDerivative (2, 2);

			double dblDDDLeftFirst = wj.getFirstDerivative (3, 2);

			double dblDADLeftSecond = wj.getFirstDerivative (0, 3);

			double dblDBDLeftSecond = wj.getFirstDerivative (1, 3);

			double dblDCDLeftSecond = wj.getFirstDerivative (2, 3);

			double dblDDDLeftSecond = wj.getFirstDerivative (3, 3);

			org.drip.math.algodiff.WengertJacobian wjValue = new org.drip.math.algodiff.WengertJacobian (1,
				NUM_COEFFICIENTS);

			if (!wjValue.setWengert (0, calcValue (dblPoint))) return null;

			/*
			 * dV/dVLeftNode
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0, 0, dblDVDA * dblDADLeftNode + dblDVDB *
				dblDBDLeftNode + dblDVDC * dblDCDLeftNode + dblDVDD * dblDDDLeftNode))
				return null;

			/*
			 * dV/dVRightNode
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0, 1, dblDVDA * dblDADRightNode + dblDVDB *
				dblDBDRightNode + dblDVDC * dblDCDRightNode + dblDVDD * dblDDDRightNode))
				return null;

			/*
			 * dV/dVLeftFirst
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0, 2, dblDVDA * dblDADLeftFirst + dblDVDB *
				dblDBDLeftFirst + dblDVDC * dblDCDLeftFirst + dblDVDD * dblDDDLeftFirst))
				return null;

			/*
			 * dV/dVLeftSecond
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0, 3, dblDVDA * dblDADLeftSecond + dblDVDB *
				dblDBDLeftSecond + dblDVDC * dblDCDLeftSecond + dblDVDD * dblDDDLeftSecond))
				return null;

			return wjValue;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.lang.String displayString()
	{
		int iNumPartitions = 5;
		double dblValue = java.lang.Double.NaN;

		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		double dblStepWidth = _io.getSpan() / iNumPartitions;

		for (int i = 0; i <= iNumPartitions; ++i) {
			double dblPoint = _io.getLeft() + i * dblStepWidth;

			try {
				dblValue = calcValue (dblPoint);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			sb.append ("\t\t\t" + dblPoint + " = " + dblValue + "\n");
		}

		return sb.toString();
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.math.spline.InelasticOrdinates io1 = new org.drip.math.spline.InelasticOrdinates (1., 5.);

		org.drip.math.spline.ElasticCoefficients ec1 = new org.drip.math.spline.BasisCubicPolynomial (io1);

		org.drip.math.algodiff.WengertJacobian wj1 = ec1.calibrateJacobian (1., 0., 7.);

		System.out.println ("EC1 Jacobian: " + wj1.displayString());

		System.out.println ("EC1 Head: " + ec1.calcJacobian().displayString());

		org.drip.math.spline.InelasticOrdinates io2 = new org.drip.math.spline.InelasticOrdinates (5., 9.);

		org.drip.math.spline.ElasticCoefficients ec2 = new org.drip.math.spline.BasisCubicPolynomial (io2);

		org.drip.math.algodiff.WengertJacobian wj2 = ec2.calibrateJacobian (ec1, 13.);

		System.out.println ("EC2 Jacobian: " + wj2.displayString());

		System.out.println ("EC2 Regular Jacobian: " + ec2.calcJacobian().displayString());

		System.out.println ("\tBase A: " + ec2.getIndexedCoefficient (3, true));

		System.out.println ("\tBase B: " + ec2.getIndexedCoefficient (2, true));

		System.out.println ("\tBase C: " + ec2.getIndexedCoefficient (1, true));

		System.out.println ("\tBase D: " + ec2.getIndexedCoefficient (0, true));

		ec2.calibrate (ec1, 14.);

		System.out.println ("\tBumped A: " + ec2.getIndexedCoefficient (3, true));

		System.out.println ("\tBumped B: " + ec2.getIndexedCoefficient (2, true));

		System.out.println ("\tBumped C: " + ec2.getIndexedCoefficient (1, true));

		System.out.println ("\tBumped D: " + ec2.getIndexedCoefficient (0, true));

		double dblX = 7.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + ec2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + ec2.calcValueJacobian
			(dblX).displayString());
	}
}
