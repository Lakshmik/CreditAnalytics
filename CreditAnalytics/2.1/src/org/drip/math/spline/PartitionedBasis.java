
package org.drip.math.spline;

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
 * This class implements the elastic coefficients for the segment using Ck basis splines inside -
 * 	[0,...,1) - Globally [x_0,...,x_1):
 * 
 * 			y = Interpolator (Ck, x) * ShapeControl (x)
 * 
 *		where is the normalized ordinate mapped as
 * 
 * 			x => (x - x_i-1) / (x_i - x_i-1)
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PartitionedBasis extends org.drip.math.spline.Ck {
	private org.drip.math.function.AbstractUnivariate _auShapeControl = null;

	/**
	 * Construct a PartitionedBasis instance from the input parameters
	 * 
	 * @param dblX0 Global Spline Left Ordinate
	 * @param dblX1 Global Spline Right Ordinate
	 * @param iK Continuity Criterion in C_k
	 * @param auShapeControl Shape Controller
	 * 
	 * @return PartitionedBasis instance
	 */

	public static final PartitionedBasis Create (
		final double dblX0,
		final double dblX1,
		final int iK,
		final org.drip.math.function.AbstractUnivariate auShapeControl)
	{
		if (0 > iK || null == auShapeControl) return null;

		org.drip.math.function.AbstractUnivariate[] aAU = new
			org.drip.math.function.AbstractUnivariate[iK + 2];

		try {
			for (int i = 0; i < iK + 2; ++i)
				aAU[i] = new org.drip.math.function.Polynomial (i);

			return new PartitionedBasis (dblX0, dblX1, aAU, iK, auShapeControl);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private PartitionedBasis (
		final double dblX0,
		final double dblX1,
		final org.drip.math.function.AbstractUnivariate[] aAU,
		final int iK,
		final org.drip.math.function.AbstractUnivariate auShapeControl)
		throws java.lang.Exception
	{
		super (dblX0, dblX1, aAU, iK);

		_auShapeControl = auShapeControl;
	}

	@Override public double y (
		final double dblX)
		throws java.lang.Exception
	{
		return super.y (dblX) * _auShapeControl.evaluate (dblX);
	}

	@Override public double derivative (
		final double dblX,
		final int iOrder)
		throws java.lang.Exception
	{
		double dblDerivative = 0.;

		for (int i = 0; i <= iOrder; ++i) {
			double dblInterpDeriv = 0 == i ? super.y (dblX): super.derivative (dblX, i);

			double dblShapeControlDeriv = iOrder == i ? _auShapeControl.evaluate (dblX) :
				_auShapeControl.calcDerivative (dblX, iOrder - i);

			dblDerivative += (org.drip.math.common.NumberUtil.NCK (iOrder,  i) * dblInterpDeriv *
				dblShapeControlDeriv);
		}

		return dblDerivative;
	}

	@Override protected boolean coeffCkMicroJack()
	{
		try {
			double dblDShapeControlDX_0 = _auShapeControl.calcDerivative (0., 1);

			double dblD2ShapeControlDX2_0 = _auShapeControl.calcDerivative (0., 2);

			return accumulateJacobian (0, 0, 1.)     							// dA/dVLeft
				&& accumulateJacobian (0, 1, 0.)     							// dA/dVRight
				&& accumulateJacobian (0, 2, 0.)     							// dA/dVFirst
				&& accumulateJacobian (0, 3, 0.)     							// dA/dVSecond
				&& accumulateJacobian (1, 0, -dblDShapeControlDX_0)     		// dB/dVLeft
				&& accumulateJacobian (1, 1, 0.)     							// dB/dVRight
				&& accumulateJacobian (1, 2, 1.)     							// dB/dVFirst
				&& accumulateJacobian (1, 3, 0.)     							// dB/dVSecond
				&& accumulateJacobian (2, 0, dblDShapeControlDX_0 *
					dblDShapeControlDX_0 - 0.5 * dblD2ShapeControlDX2_0)     	// dC/dVLeft
				&& accumulateJacobian (2, 1, 0.)     							// dC/dVRight
				&& accumulateJacobian (2, 2, -dblDShapeControlDX_0)     		// dC/dVFirst
				&& accumulateJacobian (2, 3, 0.5)    							// dC/dVSecond
				&& accumulateJacobian (3, 0, 0.5 * dblD2ShapeControlDX2_0 +
					dblDShapeControlDX_0 - 1. - dblDShapeControlDX_0 *
						dblDShapeControlDX_0)     								// dD/dVLeft
				&& accumulateJacobian (3, 1, 1.)     							// dD/dVRight
				&& accumulateJacobian (3, 2, dblDShapeControlDX_0 - 1.)     	// dD/dVFirst
				&& accumulateJacobian (3, 3, -0.5);   							// dD/dVSecond
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override public boolean calibrate (
		final double dblY0,
		final double[] adblLeftDeriv,
		final double dblY1)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblY0) || null == adblLeftDeriv || getCk() >
			adblLeftDeriv.length || !org.drip.math.common.NumberUtil.IsValid (adblLeftDeriv) ||
				!org.drip.math.common.NumberUtil.IsValid (dblY1))
			return false;

		try {
			double dblDShapeControlDX_0 = _auShapeControl.calcDerivative (0., 1);

			double dblDInterpDX_0 = adblLeftDeriv[0] - dblY0 * dblDShapeControlDX_0;

			double dblD2InterpDX2_0 = adblLeftDeriv[1] - 2. * dblDInterpDX_0 * dblDShapeControlDX_0 - dblY0 *
				_auShapeControl.calcDerivative (0., 2);

			return super.calibrate (dblY0, new double[] {dblDInterpDX_0, dblD2InterpDX2_0}, dblY1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Retrieve the Shape Control
	 * 
	 * @return The Shape Control
	 */

	public org.drip.math.function.AbstractUnivariate getShapeControl()
	{
		return _auShapeControl;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.math.grid.ElasticParams ep = new org.drip.math.grid.ElasticParams();

		if (!ep.addParam ("Tension", 1.)) {
			System.out.println ("Cannot specify tension!");

			System.exit (304);
		}

		org.drip.math.grid.Segment partSeg1 = org.drip.math.spline.PartitionedBasis.Create (1., 5., 2, new
			org.drip.math.function.RationalShapeControl (ep));

		org.drip.math.calculus.WengertJacobian wj1 = partSeg1.calibrateJacobian (1., 0., 7.);

		System.out.println ("Segment 1 Jacobian: " + wj1.displayString());

		System.out.println ("Segment 1 Head: " + partSeg1.calcJacobian().displayString());

		System.out.println ("Segment 1 Monotone Type: " + partSeg1.monotoneType());

		org.drip.math.grid.Segment partSeg2 = org.drip.math.spline.PartitionedBasis.Create (5., 9., 2, new
			org.drip.math.function.RationalShapeControl (ep));

		org.drip.math.calculus.WengertJacobian wj2 = partSeg2.calibrateJacobian (partSeg1, 13.);

		System.out.println ("Segment 2 Jacobian: " + wj2.displayString());

		System.out.println ("Segment 2 Regular Jacobian: " + partSeg2.calcJacobian().displayString());

		System.out.println ("Segment 2 Monotone Type: " + partSeg2.monotoneType());

		partSeg2.calibrate (partSeg1, 14.);

		double dblX = 7.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + partSeg2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + partSeg2.calcValueJacobian
			(dblX).displayString());
	}
}
