
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
 * This class implements the elastic coefficients for the segment using tension hyperbolic basis splines.
 * 		The segment equation is
 * 
 * 			A * sinh (sigma * x) + B * cosh (sigma * x) + C * x + D
 * 
 *		where is the normalized ordinate mapped as
 * 
 * 			x => (x - x_i-1) / (x_i - x_i-1)
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasisHyperbolicTension extends org.drip.math.spline.ElasticCoefficients {

	/*
	 * Number of Wengert Variables: 4 (_dblA, _dblB, _dblC, _dblD)
	 */

	private static final int NUM_WENGERTS = 4;

	/*
	 * Number of Parameters: 3 (dblLeftValue, dblLeftSlope, dblRightValue)
	 */

	private static final int NUM_PARAMETERS = 3;
	private static final boolean s_bBlog = false;

	private double _dblA = java.lang.Double.NaN;
	private double _dblB = java.lang.Double.NaN;
	private double _dblC = java.lang.Double.NaN;
	private double _dblD = java.lang.Double.NaN;
	private double _dblSigma = java.lang.Double.NaN;
	private org.drip.math.spline.InelasticOrdinates _io = null;
	private org.drip.math.algodiff.WengertJacobian _wjMicro = null; 

	public BasisHyperbolicTension (
		final org.drip.math.spline.InelasticOrdinates io,
		final org.drip.math.spline.BasisSplineElasticParams bfp)
		throws java.lang.Exception
	{
		if (null == (_io = io) || null == bfp)
			throw new java.lang.Exception ("BasisHyperbolicTension ctr: Invalid inputs!");

		if (bfp.containsParameter ("DenormalizedTension"))
			_dblSigma = bfp.getParamValue ("DenormalizedTension") * _io.getSpan();
		else if (bfp.containsParameter ("Tension"))
			_dblSigma = bfp.getParamValue ("Tension");
		else if (bfp.containsParameter ("NormalizedTension"))
			_dblSigma = bfp.getParamValue ("NormalizedTension");
		else if (bfp.containsParameter ("LocalizedTension"))
			_dblSigma = bfp.getParamValue ("LocalizedTension");
	}

	@Override public int numCoefficients()
	{
		return NUM_WENGERTS;
	}

	@Override public int numParameters()
	{
		return NUM_PARAMETERS;
	}

	@Override public boolean calibrate (
		final double dblLeftValue,
		final double[] adblLeftDeriv,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftValue) || null == adblLeftDeriv || 2 >
			adblLeftDeriv.length || !org.drip.math.common.NumberUtil.IsValid (adblLeftDeriv[0]) ||
				!org.drip.math.common.NumberUtil.IsValid (dblRightValue))
			return false;

		double dblLocalizedLeftSlope = adblLeftDeriv[0] * _io.getSpan();

		_dblA = (dblRightValue - dblLeftValue - dblLocalizedLeftSlope) / (java.lang.Math.sinh (_dblSigma) -
			_dblSigma);

		_dblB = 0.;
		_dblC = dblLocalizedLeftSlope - _dblSigma * _dblA;
		_dblD = dblLeftValue;

		try {
			if (s_bBlog) {
				System.out.println ("\t{" + _io.getLeft() + " | " + _io.getRight() + "}: Left: [Calc = " +
					calcValue (_io.getLeft()) + " | In = " + dblLeftValue + "]");

				System.out.println ("\tLeft Slope: " + adblLeftDeriv[0] + "; Right Node: " + _io.getRight() +
					"; Right Value: " + calcValue (_io.getRight()));

				System.out.println ("\tBegin{" + _io.getLeft() + " | " + _io.getRight() +
					"}: Right: [Calc = " + calcValue (_io.getRight()) + " | In = " + dblRightValue + "]");
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
				double dblLeftValue = _dblB + _dblD;
				double dblLocalizedLeftSlope = _dblSigma * _dblA + _dblC;

				_dblA = (dblRightValue - dblLeftValue - dblLocalizedLeftSlope) / (java.lang.Math.sinh
					(_dblSigma) - _dblSigma);

				if (s_bBlog)
					System.out.println ("\tFirst{" + _io.getLeft() + " | " + _io.getRight() +
						"}: Right: [Calc = " + calcValue (_io.getRight()) + " | In = " + dblRightValue +
							"]");

				return true;
			}

			double dblWidth = _io.getSpan();

			double dblLeftValue = ecPrev.calcValue (_io.getLeft());

			double dblLocalizedLeftSlope = ecPrev.calcOrderedDerivative (_io.getLeft(), 1, false) * dblWidth;

			double dblLocalizedLeftConvexity = ecPrev.calcOrderedDerivative (_io.getLeft(), 2, false) *
				dblWidth * dblWidth;

			_dblA = (_dblSigma * _dblSigma * (dblRightValue - dblLeftValue - dblLocalizedLeftSlope) +
				dblLocalizedLeftConvexity * (1. - java.lang.Math.cosh (_dblSigma))) / (_dblSigma * _dblSigma
					* (java.lang.Math.sinh (_dblSigma) - _dblSigma));

			_dblB = dblLocalizedLeftConvexity / _dblSigma / _dblSigma;
			_dblC = dblLocalizedLeftSlope - _dblSigma * _dblA;
			_dblD = dblLeftValue - _dblB;

			if (s_bBlog)
				System.out.println ("\tLater{" + _io.getLeft() + " | " + _io.getRight() +
					"}: Right: [Calc = " + calcValue (_io.getRight()) + " | In = " + dblRightValue + "]");

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
			/*
			 * Number of Parameters: 3 ALWAYS (dblLeftValue, dblLeftSlope, dblRightValue)
			 */

			_wjMicro = new org.drip.math.algodiff.WengertJacobian (NUM_WENGERTS, NUM_PARAMETERS);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (!_wjMicro.setWengert (0, _dblA) || !_wjMicro.setWengert (1, _dblB)|| !_wjMicro.setWengert (2,
			_dblC) || !_wjMicro.setWengert (3, _dblD))
			return null;

		double dblAlpha = 1. / (java.lang.Math.sinh (_dblSigma) - _dblSigma);

		if (!_wjMicro.accumulatePartialFirstDerivative (0, 0, -dblAlpha) ||
			!_wjMicro.accumulatePartialFirstDerivative (0, 1, -dblAlpha) ||
				!_wjMicro.accumulatePartialFirstDerivative (0, 2, dblAlpha))
			return null;

		if (!_wjMicro.accumulatePartialFirstDerivative (1, 0, 0.) ||
			!_wjMicro.accumulatePartialFirstDerivative (1, 1, 0.) ||
				!_wjMicro.accumulatePartialFirstDerivative (1, 2, 0.))
			return null;

		if (!_wjMicro.accumulatePartialFirstDerivative (2, 0, dblAlpha * _dblSigma) ||
			!_wjMicro.accumulatePartialFirstDerivative (2, 1, 1. + dblAlpha * _dblSigma) ||
				!_wjMicro.accumulatePartialFirstDerivative (2, 2, -dblAlpha * _dblSigma))
			return null;

		if (!_wjMicro.accumulatePartialFirstDerivative (3, 0, 1.) ||
			!_wjMicro.accumulatePartialFirstDerivative (3, 1, 0.) ||
				!_wjMicro.accumulatePartialFirstDerivative (3, 2, 0.))
			return null;

		return _wjMicro;
	}

	@Override public double getIndexedCoefficient (
		final int iIndex,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (!bLocal)
			throw new java.lang.Exception
				("BasisExponentialTension.getIndexedCoefficient => Cannot get non-local co-efficients!");

		if (4 <= iIndex)
			throw new java.lang.Exception
				("BasisExponentialTension.getIndexedCoefficient => Invalid index for the co-efficient!");

		if (3 == iIndex) return _dblA;

		if (2 == iIndex) return _dblB;

		if (1 == iIndex) return _dblC;

		return _dblD;
	}

	@Override public double calcOrderedDerivative (
		final double dblPoint,
		final int iOrder,
		final boolean bLocal)
		throws java.lang.Exception
	{
		if (4 <= iOrder || !_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("BasisHyperbolicTension.calcOrderedDerivative: Invalid inputs!");

		double dblWidth = _io.getSpan();

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		if (3 == iOrder) {
			double dblDerivative = _dblSigma * _dblSigma * _dblSigma * (_dblA * java.lang.Math.cosh
				(_dblSigma * dblNormalizedPoint) + _dblB * java.lang.Math.sinh (_dblSigma *
					dblNormalizedPoint));

			return bLocal ? dblDerivative : dblDerivative / dblWidth / dblWidth / dblWidth;
		}

		if (2 == iOrder) {
			double dblDerivative = _dblSigma * _dblSigma * (_dblA * java.lang.Math.sinh (_dblSigma *
				dblNormalizedPoint) + _dblB * java.lang.Math.cosh (_dblSigma * dblNormalizedPoint));

			return bLocal ? dblDerivative : dblDerivative / dblWidth / dblWidth;
		}

		if (1 == iOrder) {
			double dblDerivative = _dblSigma * (_dblA * java.lang.Math.cosh (_dblSigma * dblNormalizedPoint)
				+ _dblB * java.lang.Math.sinh (_dblSigma * dblNormalizedPoint)) + _dblC;

			return bLocal ? dblDerivative : dblDerivative / dblWidth;
		}

		return _dblA * java.lang.Math.sinh (_dblSigma * dblNormalizedPoint) + _dblB * java.lang.Math.cosh
			(_dblSigma * dblNormalizedPoint) + _dblC * dblNormalizedPoint + _dblD;
	}

	@Override public double calcValue (
		final double dblPoint)
		throws java.lang.Exception
	{
		if (!_io.isInSegment (dblPoint))
			throw new java.lang.Exception ("BasisHyperbolicTension.calcValue => Invalid input!");

		double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

		return _dblA * java.lang.Math.sinh (_dblSigma * dblNormalizedPoint) + _dblB * java.lang.Math.cosh
			(_dblSigma * dblNormalizedPoint) + _dblC * dblNormalizedPoint + _dblD;
	}

	@Override public org.drip.math.algodiff.WengertJacobian calcValueJacobian (
		final double dblPoint)
	{
		if (!_io.isInSegment (dblPoint)) return null;

		try {
			double dblNormalizedPoint = _io.calcNormalizedOrdinate (dblPoint);

			/*
			 * Number of Parameters: 3 ALWAYS (dblLeftValue, dblLeftSlope, dblRightValue)
			 */

			double dblDVDA = java.lang.Math.sinh (_dblSigma * dblNormalizedPoint);

			double dblDVDB = java.lang.Math.cosh (_dblSigma * dblNormalizedPoint);

			double dblDVDC = dblNormalizedPoint;
			double dblDVDD = 1.;

			org.drip.math.algodiff.WengertJacobian wj = (null == _wjMicro) ? calcJacobian() : _wjMicro;

			double dblDADLeftNode = wj.getFirstDerivative (0, 0);

			double dblDBDLeftNode = wj.getFirstDerivative (1, 0);

			double dblDCDLeftNode = wj.getFirstDerivative (2, 0);

			double dblDDDLeftNode = wj.getFirstDerivative (3, 0);

			double dblDADLeftSlope = wj.getFirstDerivative (0, 1);

			double dblDBDLeftSlope = wj.getFirstDerivative (1, 1);

			double dblDCDLeftSlope = wj.getFirstDerivative (2, 1);

			double dblDDDLeftSlope = wj.getFirstDerivative (3, 1);

			double dblDADRightNode = wj.getFirstDerivative (0, 2);

			double dblDBDRightNode = wj.getFirstDerivative (1, 2);

			double dblDCDRightNode = wj.getFirstDerivative (2, 2);

			double dblDDDRightNode = wj.getFirstDerivative (3, 2);

			org.drip.math.algodiff.WengertJacobian wjValue = new org.drip.math.algodiff.WengertJacobian (1,
				NUM_PARAMETERS);

			if (!wjValue.setWengert (0, calcValue (dblPoint))) return null;

			if (!wjValue.accumulatePartialFirstDerivative (0, 0, dblDVDA * dblDADLeftNode + dblDVDB *
				dblDBDLeftNode + dblDVDC * dblDCDLeftNode + dblDVDD * dblDDDLeftNode))
				return null;

			if (!wjValue.accumulatePartialFirstDerivative (0, 1, dblDVDA * dblDADLeftSlope + dblDVDB *
				dblDBDLeftSlope + dblDVDC * dblDCDLeftSlope + dblDVDD * dblDDDLeftSlope))
				return null;

			if (!wjValue.accumulatePartialFirstDerivative (0, 2, dblDVDA * dblDADRightNode + dblDVDB *
				dblDBDRightNode + dblDVDC * dblDCDRightNode + dblDVDD * dblDDDRightNode))
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
			double dblPoint = _io.getLeft() + dblStepWidth;

			try {
				dblValue = calcValue (dblPoint);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			sb.append (dblValue + " | ");
		}

		return sb.toString();
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.math.spline.BasisSplineElasticParams bfp = new
			org.drip.math.spline.BasisSplineElasticParams();

		if (!bfp.addParam ("Tension", 10.)) {
			System.out.println ("Cannot specify tension!");

			System.exit (304);
		}

		org.drip.math.spline.InelasticOrdinates io1 = new org.drip.math.spline.InelasticOrdinates (1., 5.);

		org.drip.math.spline.ElasticCoefficients ec1 = new org.drip.math.spline.BasisHyperbolicTension (io1,
			bfp);

		org.drip.math.algodiff.WengertJacobian wj1 = ec1.calibrateJacobian (1., 0., 4.);

		System.out.println ("EC1 Jacobian: " + wj1.displayString());

		System.out.println ("EC1 Head: " + ec1.calcJacobian().displayString());

		org.drip.math.spline.InelasticOrdinates io2 = new org.drip.math.spline.InelasticOrdinates (5., 9.);

		org.drip.math.spline.ElasticCoefficients ec2 = new org.drip.math.spline.BasisHyperbolicTension (io2,
			bfp);

		org.drip.math.algodiff.WengertJacobian wj2 = ec2.calibrateJacobian (ec1, 7.);

		System.out.println ("EC2 Jacobian: " + wj2.displayString());

		System.out.println ("EC2 Regular Jacobian: " + ec2.calcJacobian().displayString());

		double dblBaseA = ec2.getIndexedCoefficient (3, true);

		double dblBaseB = ec2.getIndexedCoefficient (2, true);

		double dblBaseC = ec2.getIndexedCoefficient (1, true);

		double dblBaseD = ec2.getIndexedCoefficient (0, true);

		ec2.calibrate (ec1, 8.);

		double dblBumpedA = ec2.getIndexedCoefficient (3, true);

		double dblBumpedB = ec2.getIndexedCoefficient (2, true);

		double dblBumpedC = ec2.getIndexedCoefficient (1, true);

		double dblBumpedD = ec2.getIndexedCoefficient (0, true);

		System.out.println ("\tDiff A: " + (dblBumpedA - dblBaseA));

		System.out.println ("\tDiff B: " + (dblBumpedB - dblBaseB));

		System.out.println ("\tDiff C: " + (dblBumpedC - dblBaseC));

		System.out.println ("\tDiff D: " + (dblBumpedD - dblBaseD));

		double dblX = 5.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + ec2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + ec2.calcValueJacobian
			(dblX).displayString());
	}
}
