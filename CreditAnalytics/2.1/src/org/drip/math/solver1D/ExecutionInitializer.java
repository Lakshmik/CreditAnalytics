
package org.drip.math.solver1D;

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
 * ExecutionInitializer implements the initialization execution and customization functionality.
 * 
 * ExecutionInitializer performs two types of variate initialization:
 * 	- Bracketing initialization: This brackets the fixed point using the bracketing algorithm described in
 * 		http://www.credit-trader.org. If successful, a pair of variate/OF coordinate nodes that bracket the
 * 		fixed point are generated. These brackets are eventually used by routines that iteratively determine
 * 		the fixed point. Bracketing initialization is controlled by the parameters in
 * 		BracketingControlParams.
 * 	- Convergence Zone initialization: This generates a variate that lies within the convergence zone for the
 * 		iterative determination of the fixed point using the Newton's method. Convergence Zone Determination
 * 		is controlled by the parameters in ConvergenceControlParams.
 *
 * ExecutionInitializer behavior can be customized/optimized through several of the initialization heuristics
 * 	techniques implemented in the InitializationHeuristics class. 
 * 
 * @author Lakshmi Krishnamurthy
 */

public class ExecutionInitializer {
	class StartingVariateOF {
		public double _dblOF = java.lang.Double.NaN;
		public double _dblVariate = java.lang.Double.NaN;

		public StartingVariateOF (
			final double dblVariate,
			final double dblOF)
			throws java.lang.Exception
		{
			if (!org.drip.math.common.NumberUtil.IsValid (_dblOF = dblOF) ||
				!org.drip.math.common.NumberUtil.IsValid (_dblVariate = dblVariate))
				throw new java.lang.Exception ("StartingVariateOF constructor: Invalid inputs!");
		}
	}

	private org.drip.math.algodiff.ObjectiveFunction _of = null;
	private org.drip.math.solver1D.ConvergenceControlParams _ccp = null;

	private java.util.SortedMap<java.lang.Double, java.lang.Double> _mapOFMap = new
		java.util.TreeMap<java.lang.Double, java.lang.Double>();

	private double evaluateOF (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (_mapOFMap.containsKey (dblVariate)) return _mapOFMap.get (dblVariate);

		double dblOF = _of.evaluate (dblVariate);

		if (org.drip.math.common.NumberUtil.IsValid (dblOF)) _mapOFMap.put (dblVariate, dblOF);

		return dblOF;
	}

	private StartingVariateOF validateVariate (
		final double dblVariate,
		final org.drip.math.solver1D.BracketingOutput bop)
	{
		double dblOF = java.lang.Double.NaN;

		try {
			dblOF = evaluateOF (dblVariate);
		} catch (java.lang.Exception e) {
			dblOF = java.lang.Double.NaN;
		}

		if (!bop.incrOFCalcs() || !org.drip.math.common.NumberUtil.IsValid (dblOF)) return null;

		_mapOFMap.put (dblVariate, dblOF);

		try {
			return new StartingVariateOF (dblVariate, dblOF);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private StartingVariateOF initializeBracketingVariate (
		final int iNumExpansions,
		final double dblBracketVariateStart,
		final double dblBracketWidthStart,
		final double dblBracketWidthExpansionFactor,
		final org.drip.math.solver1D.BracketingOutput bop)
	{
		StartingVariateOF sv = validateVariate (dblBracketVariateStart, bop);

		if (null != sv) return sv;

		double dblVariate = dblBracketVariateStart;
		int iNumExpansionsCurrent = iNumExpansions;
		double dblBracketWidth = dblBracketWidthStart;
		double dblBracketLeft = dblVariate - dblBracketWidth;
		double dblBracketRight = dblVariate + dblBracketWidth;

		while (0 <= iNumExpansionsCurrent--) {
			if (null != (sv = validateVariate (dblBracketLeft, bop))) return sv;

			if (null != (sv = validateVariate (dblBracketRight, bop))) return sv;

			dblBracketWidth *= dblBracketWidthExpansionFactor;
			dblBracketLeft = dblVariate - dblBracketWidth;
			dblBracketRight = dblVariate + dblBracketWidth;
		}

		return null;
	}

	private boolean bracketingDone (
		final double dblVariateLeft,
		final double dblVariateRight,
		final double dblOFLeft,
		final double dblOFRight,
		final double dblOFGoal,
		final org.drip.math.solver1D.BracketingOutput bop)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblOFLeft) || !org.drip.math.common.NumberUtil.IsValid
			(dblOFRight))
			return false;

		if (((dblOFLeft - dblOFGoal) * (dblOFRight - dblOFGoal)) > 0.) return false;

		double dblOF = java.lang.Double.NaN;
		double dblOFPrev = java.lang.Double.NaN;
		double dblVariate = java.lang.Double.NaN;
		double dblVariatePrev = java.lang.Double.NaN;

		for (java.util.Map.Entry<java.lang.Double, java.lang.Double> me : _mapOFMap.entrySet()) {
			dblVariate = me.getKey();

			dblOF = me.getValue();

			if (org.drip.math.common.NumberUtil.IsValid (dblVariatePrev) &&
				org.drip.math.common.NumberUtil.IsValid (dblOFPrev) && (((dblOF - dblOFGoal) * (dblOFPrev -
					dblOFGoal)) < 0.)) {
				try {
					bop.done (dblVariatePrev, dblVariate, dblOFPrev, dblOF,
						org.drip.math.solver1D.VariateIteratorPrimitive.Bisection (dblVariatePrev,
							dblVariate));
				} catch (java.lang.Exception e) {
				}

				return true;
			}

			dblOFPrev = dblOF;
			dblVariatePrev = dblVariate;
		}

		try {
			bop.done (dblVariateLeft, dblVariateRight, dblOFLeft, dblOFRight,
				org.drip.math.solver1D.VariateIteratorPrimitive.Bisection (dblVariateLeft, dblVariateRight));
		} catch (java.lang.Exception e) {
		}

		return true;
	}

	private boolean isInConvergenceZone (
		final double dblConvergenceZoneVariate,
		final double dblOFGoal,
		final org.drip.math.solver1D.ConvergenceOutput cop)
		throws java.lang.Exception
	{
		if (!cop.incrOFCalcs())
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot increment OF in the output");

		double dblOFValue = evaluateOF (dblConvergenceZoneVariate) - dblOFGoal;

		if (!org.drip.math.common.NumberUtil.IsValid (dblOFValue))
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot evaluate OF for variate " +
					dblConvergenceZoneVariate);

		if (!cop.incrOFDerivCalcs())
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot increment OF deriv count in the output");

		org.drip.math.algodiff.Differential diff1D = _of.calcDerivative (dblConvergenceZoneVariate, 1);

		if (null == diff1D)
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot evaluate OF first deriv for variate " +
					dblConvergenceZoneVariate);

		if (!cop.incrOFDerivCalcs() && !cop.incrOFDerivCalcs())
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot increment OF deriv in the output");

		org.drip.math.algodiff.Differential diff2D = _of.calcDerivative (dblConvergenceZoneVariate, 2);

		if (null == diff2D)
			throw new java.lang.Exception
				("ExecutionInitializer::isInConvergenceZone => Cannot evaluate OF second deriv for variate "
					+ dblConvergenceZoneVariate);

		return java.lang.Math.abs (dblOFValue * diff2D.calcSlope (false)) < (diff1D.calcSlope (false) *
			diff1D.calcSlope (false) * _ccp.getConvergenceZoneEdgeLimit());
	}

	private boolean leftOFValidityEdgeReached (
		final double dblVariateLeft,
		final double dblOFLeft,
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		return !org.drip.math.common.NumberUtil.IsValid (dblOFLeft) || (null != ih &&
			org.drip.math.common.NumberUtil.IsValid (ih.getBracketFloor()) && dblVariateLeft <
				ih.getBracketFloor());
	}

	private boolean rightOFValidityEdgeReached (
		final double dblVariateRight,
		final double dblOFRight,
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		return !org.drip.math.common.NumberUtil.IsValid (dblOFRight) || (null != ih &&
			org.drip.math.common.NumberUtil.IsValid (ih.getBracketCeiling()) && dblVariateRight >
				ih.getBracketCeiling());
	}

	private double getStartingBracketVariate (
		final org.drip.math.solver1D.BracketingControlParams bcp,
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		if (null != ih && org.drip.math.common.NumberUtil.IsValid (ih.getStartingBracketMid()))
			return ih.getStartingBracketMid();

		if (null != ih && org.drip.math.common.NumberUtil.IsValid (ih.getStartingBracketLeft()) &&
			org.drip.math.common.NumberUtil.IsValid (ih.getStartingBracketRight()))
			return 0.5 * (ih.getStartingBracketLeft() + ih.getStartingBracketRight());

		return bcp.getVariateStart();
	}

	private double getStartingBracketWidth (
		final org.drip.math.solver1D.BracketingControlParams bcp,
		final org.drip.math.solver1D.InitializationHeuristics ih)
	{
		if (null != ih) {
			double dblBracketStartLeft = ih.getStartingBracketLeft();

			double dblBracketStartRight = ih.getStartingBracketRight();

			if (org.drip.math.common.NumberUtil.IsValid (dblBracketStartLeft) &&
				org.drip.math.common.NumberUtil.IsValid (dblBracketStartRight) && dblBracketStartRight >
					dblBracketStartLeft)
				return dblBracketStartRight - dblBracketStartLeft;
		}

		return bcp.getStartingBracketWidth();
	}

	/**
	 * ExecutionInitializer constructor
	 * 
	 * @param of Objective Function
	 * @param ccp Convergence Control Parameters
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public ExecutionInitializer (
		final org.drip.math.algodiff.ObjectiveFunction of,
		final org.drip.math.solver1D.ConvergenceControlParams ccp)
		throws java.lang.Exception
	{
		if (null == (_of = of))
			throw new java.lang.Exception ("ExecutionInitializer constructor: Invalid inputs");

		if (null == (_ccp = ccp)) _ccp = new org.drip.math.solver1D.ConvergenceControlParams();
	}

	/**
	 * Sets up the bracket to be used for the eventual search kick-off
	 * 
	 * @param ih Optional InitializationHeuristics instance
	 * @param dblOFGoal The OF Goal
	 * 
	 * @return The Bracketing Output
	 */

	public org.drip.math.solver1D.BracketingOutput initializeBracket (
		final org.drip.math.solver1D.InitializationHeuristics ih,
		final double dblOFGoal)
	{
		org.drip.math.solver1D.BracketingControlParams bcp = (null != ih && null != ih.getCustomBCP()) ?
			ih.getCustomBCP() : new org.drip.math.solver1D.BracketingControlParams();

		int iNumExpansions = bcp.getNumExpansions();

		org.drip.math.solver1D.BracketingOutput bop = new org.drip.math.solver1D.BracketingOutput();

		StartingVariateOF sv = initializeBracketingVariate (iNumExpansions, getStartingBracketVariate (bcp,
			ih), getStartingBracketWidth (bcp, ih), bcp.getBracketWidthExpansionFactor(), bop);

		if (null == sv) return bop;

		double dblOFLeft = sv._dblOF;
		double dblOFRight = sv._dblOF;
		double dblPreviousOFLeft = sv._dblOF;
		double dblPreviousOFRight = sv._dblOF;
		double dblVariateLeft = sv._dblVariate;
		double dblVariateRight = sv._dblVariate;
		boolean bLeftOFValidityEdgeReached = false;
		boolean bRightOFValidityEdgeReached = false;
		double dblPreviousVariateLeft = sv._dblVariate;
		double dblPreviousVariateRight = sv._dblVariate;

		double dblBracketWidth = bcp.getStartingBracketWidth();

		while (0 <= iNumExpansions--) {
			if (!bop.incrIterations()) return null;

			if (bLeftOFValidityEdgeReached && bRightOFValidityEdgeReached) return bop;

			if (!bLeftOFValidityEdgeReached) {
				dblPreviousVariateLeft = dblVariateLeft;
				dblVariateLeft -= dblBracketWidth;
				dblPreviousOFLeft = dblOFLeft;

				try {
					if (bracketingDone (dblVariateLeft, dblVariateRight, dblOFLeft = evaluateOF
						(dblVariateLeft), dblOFRight, dblOFGoal, bop) && bop.incrOFCalcs())
						return bop;
				} catch (java.lang.Exception e) {
					dblOFLeft = java.lang.Double.NaN;
				}

				if (bLeftOFValidityEdgeReached = leftOFValidityEdgeReached (dblVariateLeft, dblOFLeft, ih)) {
					dblOFLeft = dblPreviousOFLeft;
					dblVariateLeft = dblPreviousVariateLeft;
				}
			}

			if (!bRightOFValidityEdgeReached) {
				dblPreviousVariateRight = dblVariateRight;
				dblVariateRight += dblBracketWidth;
				dblPreviousOFRight = dblOFRight;

				try {
					if (bracketingDone (dblVariateLeft, dblVariateRight, dblOFLeft, dblOFRight = evaluateOF
						(dblVariateRight), dblOFGoal, bop) && bop.incrOFCalcs())
						return bop;
				} catch (java.lang.Exception e) {
					dblOFRight = java.lang.Double.NaN;
				}

				if (bRightOFValidityEdgeReached = rightOFValidityEdgeReached (dblVariateRight, dblOFRight,
					ih)) {
					dblOFRight = dblPreviousOFRight;
					dblVariateRight = dblPreviousVariateRight;
				}
			}

			if (bracketingDone (dblVariateLeft, dblVariateRight, dblOFLeft, dblOFRight, dblOFGoal, bop))
				return bop;

			dblBracketWidth *= bcp.getBracketWidthExpansionFactor();
		}

		return null;
	}

	/**
	 * Initialize the starting variate to within the fixed point convergence zone
	 * 
	 * @param ih Optional InitializationHeuristics instance
	 * @param dblOFGoal The OF Goal
	 * 
	 * @return The Convergence Zone Output
	 */

	public org.drip.math.solver1D.ConvergenceOutput initializeVariate (
		final org.drip.math.solver1D.InitializationHeuristics ih,
		final double dblOFGoal)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblOFGoal)) return null;

		org.drip.math.solver1D.ConvergenceOutput cop = new org.drip.math.solver1D.ConvergenceOutput();

		org.drip.math.solver1D.BracketingOutput bop = initializeBracket (ih, dblOFGoal);

		if (null != bop && bop.done()) return bop.makeConvergenceVariate();

		double dblConvergenceZoneVariate = _ccp.getConvergenceZoneVariateBegin();

		int iFixedPointConvergenceIterations = _ccp.getFixedPointConvergenceIterations();

		while (0 != iFixedPointConvergenceIterations--) {
			if (!cop.incrIterations()) return cop;

			try {
				if (isInConvergenceZone (dblConvergenceZoneVariate, dblOFGoal, cop)) {
					cop.done (dblConvergenceZoneVariate);

					return cop;
				}
			} catch (java.lang.Exception e) {
				// e.printStackTrace();
			}

			try {
				if (isInConvergenceZone (-1. * dblConvergenceZoneVariate, dblOFGoal, cop)) {
					cop.done (-1. * dblConvergenceZoneVariate);

					return cop;
				}
			} catch (java.lang.Exception e) {
				// e.printStackTrace();
			}

			dblConvergenceZoneVariate *= _ccp.getConvergenceZoneVariateBumpFactor();
		}

		return null;
	}

	/**
	 * Initializes the starting bracket within the specified boundary
	 * 
	 * @param ih Initialization Heuristics containing the hard search edges
	 * @param dblOFGoal The OF Goal
	 * 
	 * @return Results of the Verification
	 */

	public org.drip.math.solver1D.BracketingOutput verifyHardSearchEdges (
		final org.drip.math.solver1D.InitializationHeuristics ih,
		final double dblOFGoal)
	{
		if (null == ih || !org.drip.math.common.NumberUtil.IsValid (ih.getSearchStartLeft()) ||
			!org.drip.math.common.NumberUtil.IsValid (ih.getSearchStartRight()) ||
				!org.drip.math.common.NumberUtil.IsValid (dblOFGoal))
			return null;

		try {
			org.drip.math.solver1D.BracketingOutput bop = new org.drip.math.solver1D.BracketingOutput();

			if (bracketingDone (ih.getSearchStartLeft(), ih.getSearchStartRight(), evaluateOF
				(ih.getSearchStartLeft()), evaluateOF (ih.getSearchStartRight()), dblOFGoal, bop) &&
					bop.incrOFCalcs())
				return bop;
		} catch (java.lang.Exception e) {
		}

		return null;
	}
}
