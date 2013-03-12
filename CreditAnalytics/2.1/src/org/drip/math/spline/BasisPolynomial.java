
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
 * This class implements the elastic coefficients for the segment using the following polynomial basis
 * 		spline:
 * 
 * 			A + B*x
 * 
 *		where is the normalized ordinate mapped as
 * 
 * 			x => (x - x_i-1) / (x_i - x_i-1)
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasisPolynomial extends org.drip.math.spline.ElasticCoefficients {

	/*
	 * Number of Coefficients: 2 (_dblA, _dblB)
	 */

	private static final int NUM_COEFFICIENTS = 2;
	private double _dblA = java.lang.Double.NaN;
	private double _dblB = java.lang.Double.NaN;
	private org.drip.math.spline.InelasticOrdinates _io = null;
	private org.drip.math.algodiff.WengertJacobian _wjMicro = null; 

	/**
	 * BasisPolynomial constructor
	 * 
	 * @param io Inelastic Ordinates
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public BasisPolynomial (
		final org.drip.math.spline.InelasticOrdinates io)
		throws java.lang.Exception
	{
		if (null == (_io = io))
			throw new java.lang.Exception ("BasisPolynomial constructor: Invalid inputs!");
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
		final double[] adblLeftDeriv,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftValue) ||
			!org.drip.math.common.NumberUtil.IsValid (dblRightValue))
			return false;

		_dblA = dblLeftValue;
		_dblB = dblRightValue - dblLeftValue;
		return true;
	}

	@Override public boolean calibrate (
		final org.drip.math.spline.ElasticCoefficients ecPrev,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblRightValue)) return false;

		if (null == ecPrev) {
			_dblB = dblRightValue - _dblA;
			return true;
		}

		try {
			return calibrate (ecPrev.calcValue (_io.getLeft()), null, dblRightValue);
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

		if (!_wjMicro.setWengert (0, _dblA) || !_wjMicro.setWengert (1, _dblB)) return null;

		/*
		 * dA/dVLeft, dA/dVRight
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (0,
			org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX, 1.) ||
				!_wjMicro.accumulatePartialFirstDerivative (0,
					org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX, 0.))
			return null;

		/*
		 * dB/dVLeft, dB/dVRight
		 */

		if (!_wjMicro.accumulatePartialFirstDerivative (1,
			org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX, -1.) ||
				!_wjMicro.accumulatePartialFirstDerivative (1,
					org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX, 1.))
			return null;

		return _wjMicro;
	}

	@Override public double getIndexedCoefficient (
		final int iIndex,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (2 <= iIndex)
			throw new java.lang.Exception
				("BasisPolynomial.getIndexedCoefficient => Invalid index for the co-efficient!");

		if (bLocal) {
			if (1 == iIndex) return _dblB;

			return _dblA;
		}

		double dblLeft = _io.getLeft();

		double dblWidth = _io.getSpan();

		double dblBGlobal = _dblB / dblWidth;

		if (1 == iIndex) return dblBGlobal;

		return _dblA - dblBGlobal * dblLeft;
	}

	@Override public double calcOrderedDerivative (
		final double dblPoint,
		final int iOrder,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (!_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("BasisPolynomial.calcOrderedDerivative: Invalid inputs!");

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		if (bLocal) {
			if (0 == iOrder) return _dblA + _dblB * dblNormalizedPoint;

			if (1 == iOrder) return _dblB;

			return 0.;
		}

		double dblWidth = _io.getSpan();

		if (0 == iOrder) return _dblA + _dblB * dblNormalizedPoint;

		if (1 == iOrder) return _dblB / dblWidth;

		return 0.;
	}

	@Override public double calcValue (
		final double dblPoint)
		throws java.lang.Exception
	{
		if (!_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("BasisPolynomial.calcValue => Invalid input!");

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		return _dblA + _dblB * dblNormalizedPoint;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcValueJacobian (
		final double dblPoint)
	{
		if (!_io.isInSegment (dblPoint)) return null;

		try {
			double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

			double dblDVDA = 1.;
			double dblDVDB = dblNormalizedPoint;

			org.drip.math.algodiff.WengertJacobian wj = (null == _wjMicro) ? calcJacobian() : _wjMicro;

			double dblDADLeftNode = wj.getFirstDerivative (0,
				org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX);

			double dblDBDLeftNode = wj.getFirstDerivative (1,
				org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX);

			double dblDADRightNode = wj.getFirstDerivative (0,
				org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX);

			double dblDBDRightNode = wj.getFirstDerivative (1,
				org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX);

			org.drip.math.algodiff.WengertJacobian wjValue = new org.drip.math.algodiff.WengertJacobian (1,
				NUM_COEFFICIENTS);

			if (!wjValue.setWengert (0, calcValue (dblPoint))) return null;

			/*
			 * dOPValue/dLeftValue
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0,
				org.drip.math.spline.ElasticCoefficients.LEFT_NODE_VALUE_PARAMETER_INDEX, dblDVDA *
					dblDADLeftNode + dblDVDB * dblDBDLeftNode))
				return null;

			/*
			 * dOPValue/dRightValue
			 */

			if (!wjValue.accumulatePartialFirstDerivative (0,
				org.drip.math.spline.ElasticCoefficients.RIGHT_NODE_VALUE_PARAMETER_INDEX, dblDVDA *
					dblDADRightNode + dblDVDB * dblDBDRightNode))
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

		org.drip.math.spline.ElasticCoefficients ec1 = new org.drip.math.spline.BasisPolynomial (io1);

		org.drip.math.algodiff.WengertJacobian wj1 = ec1.calibrateJacobian (1., 0., 7.);

		System.out.println ("EC1 Jacobian: " + wj1.displayString());

		System.out.println ("EC1 Head: " + ec1.calcJacobian().displayString());

		org.drip.math.spline.InelasticOrdinates io2 = new org.drip.math.spline.InelasticOrdinates (5., 9.);

		org.drip.math.spline.ElasticCoefficients ec2 = new org.drip.math.spline.BasisPolynomial (io2);

		org.drip.math.algodiff.WengertJacobian wj2 = ec2.calibrateJacobian (ec1, 13.);

		System.out.println ("EC2 Jacobian: " + wj2.displayString());

		System.out.println ("EC2 Regular Jacobian: " + ec2.calcJacobian().displayString());

		System.out.println ("\tBase A: " + ec2.getIndexedCoefficient (1, true));

		System.out.println ("\tBase B: " + ec2.getIndexedCoefficient (0, true));

		ec2.calibrate (ec1, 14.);

		System.out.println ("\tBumped A: " + ec2.getIndexedCoefficient (1, true));

		System.out.println ("\tBumped B: " + ec2.getIndexedCoefficient (0, true));

		double dblX = 7.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + ec2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + ec2.calcValueJacobian
			(dblX).displayString());
	}
}
