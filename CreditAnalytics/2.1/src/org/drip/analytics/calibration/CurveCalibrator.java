
package org.drip.analytics.calibration;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 * CurveCalibrator calibrates the discount and credit/hazard curves from the components and their quotes.
 * 
 * CurveCalibrator employs a set of techniques for achieving this calibration.
 * 	- It bootstraps the nodes in sequence to calibrate the curve.
 * 	- In conjunction with splining interpolation techniques, it may also be used to perform dual sweep
 * 		calibration. The inner sweep achieves the calibration of the segment spline parameters, while the
 * 		outer sweep calibrates iteratively for the targeted boundary conditions.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CurveCalibrator {
	class CreditCurveCalibrator extends org.drip.math.algodiff.ObjectiveFunction {
		private int _iInstr = -1;
		private boolean _bFlat = false;
		private java.lang.String _strMeasure = "";
		private double _dblCalibValue = java.lang.Double.NaN;
		private org.drip.product.definition.Component _comp = null;
		private org.drip.analytics.definition.CreditCurve _cc = null;
		private org.drip.analytics.definition.DiscountCurve _dc = null;
		private org.drip.param.pricer.PricerParams _pricerParams = null;
		private org.drip.analytics.definition.DiscountCurve _dcTSY = null;
		private org.drip.param.valuation.ValuationParams _valParams = null;
		private org.drip.analytics.definition.DiscountCurve _dcEDSF = null;
		private org.drip.param.valuation.QuotingParams _quotingParams = null;
		private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> _mmFixings = null;

		public CreditCurveCalibrator (
			org.drip.analytics.definition.CreditCurve cc,
			final org.drip.product.definition.Component comp,
			final int iInstr,
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.analytics.definition.DiscountCurve dc,
			final org.drip.analytics.definition.DiscountCurve dcTSY,
			final org.drip.analytics.definition.DiscountCurve dcEDSF,
			final org.drip.param.pricer.PricerParams pricerParamsIn,
			final java.lang.String strMeasure,
			final double dblCalibValue,
			final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
				java.lang.Double>> mmFixings,
			final org.drip.param.valuation.QuotingParams quotingParams,
			final boolean bFlat)
			throws java.lang.Exception
		{
			super (null);

			_cc = cc;
			_dc = dc;
			_comp = comp;
			_bFlat = bFlat;
			_dcTSY = dcTSY;
			_dcEDSF = dcEDSF;
			_iInstr = iInstr;
			_mmFixings = mmFixings;
			_valParams = valParams;
			_strMeasure = strMeasure;
			_dblCalibValue = dblCalibValue;
			_quotingParams = quotingParams;

			_pricerParams = new org.drip.param.pricer.PricerParams (pricerParamsIn._iUnitSize, new
				org.drip.param.definition.CalibrationParams (strMeasure, 0, null),
					pricerParamsIn._bSurvToPayDate, pricerParamsIn._iDiscretizationScheme);
		}

		public double evaluate (
			final double dblRate)
			throws java.lang.Exception
		{
			if (!SetNode (_cc, _iInstr, _bFlat, dblRate))
				throw new java.lang.Exception ("Cannot set CC = " + dblRate + " for node #" + _iInstr);

			return _dblCalibValue - _comp.calcMeasureValue (_valParams, _pricerParams,
				org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (_dc, _dcTSY,
					_dcEDSF, _cc, null, null, _mmFixings), _quotingParams, _strMeasure);
		}
	}

	private static final boolean SetNode (
		final org.drip.analytics.definition.Curve curve,
		final int iInstr,
		final boolean bFlat,
		final double dblValue)
	{
		if (!bFlat) return curve.setNodeValue (iInstr, dblValue);

		return curve.setFlatValue (dblValue);
	}

	private double calcCalibrationMetric (
		final org.drip.analytics.curve.PolynomialSplineDF dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.product.definition.Component[] aCalibComp,
		final org.drip.param.valuation.ValuationParams valParams,
		final java.lang.String[] astrCalibMeasure,
		final double[] adblCalibValue,
		final double dblBump,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblCalibLeftSlope)
		throws java.lang.Exception
	{
		if (!dc.initializeCalibrationRun (dblCalibLeftSlope))
			throw new java.lang.Exception ("Cannot initialize Calibration Run!");

		double[] adblNodeCalibOP = new double[aCalibComp.length];

		for (int i = 0; i < aCalibComp.length; ++i) {
			if (!org.drip.math.common.NumberUtil.IsValid (adblNodeCalibOP[i] = calibrateIRNode (dc, dcTSY,
				dcEDSF, aCalibComp[i], i, valParams, astrCalibMeasure[i], adblCalibValue[i] + dblBump,
					mmFixings, quotingParams, false, 0 == i ? java.lang.Double.NaN :
						adblNodeCalibOP[i - 1]))) {
				System.out.println ("\t\tCalibration failed for node #" + i);

				throw new java.lang.Exception ("Cannot calibrate node " + i + " for left slope " +
					dblCalibLeftSlope + "!");
			}
		}

		return dc.getCalibrationMetric();
	}

	private double calibrateIRCurve (
		final org.drip.analytics.curve.PolynomialSplineDF dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.product.definition.Component[] aCalibComp,
		final org.drip.param.valuation.ValuationParams valParams,
		final java.lang.String[] astrCalibMeasure,
		final double[] adblCalibValue,
		final double dblBump,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception
	{
		org.drip.math.algodiff.ObjectiveFunction ofIROuter = new org.drip.math.algodiff.ObjectiveFunction
			(null) {
			public double evaluate (
				final double dblShiftedLeftSlope)
				throws java.lang.Exception
			{
				return calcCalibrationMetric (dc, dcTSY, dcEDSF, aCalibComp, valParams, astrCalibMeasure,
					adblCalibValue, dblBump, mmFixings, quotingParams, dblShiftedLeftSlope);
			}
		};

		org.drip.math.solver1D.FixedPointFinderOutput rfop = new org.drip.math.solver1D.FixedPointFinderBrent
			(0., ofIROuter).findRoot();

		if (null == rfop || !rfop.containsRoot())
			throw new java.lang.Exception ("CurveCalibrator.calibrateIRCurve => Cannot get root!");

		return rfop.getRoot();
	}

	/**
	 * Constructs an empty CurveCalibrator
	 */

	public CurveCalibrator()
	{
	}

	public boolean bootstrapHazardRate (
		org.drip.analytics.definition.CreditCurve cc,
		final org.drip.product.definition.Component comp,
		final int iInstr,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.param.pricer.PricerParams pricerParamsIn,
		final java.lang.String strMeasure,
		final double dblCalibValue,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat)
	{
		if (null == cc || null == comp || null == valParams || null == dc || null == pricerParamsIn || null
			== strMeasure || strMeasure.isEmpty() || !org.drip.math.common.NumberUtil.IsValid
				(dblCalibValue)) {
			System.out.println ("Invalid params into CurveCalibrator.bootstrapHazardRate!");

			return false;
		}

		try {
			org.drip.math.solver1D.FixedPointFinderOutput rfop = new
				org.drip.math.solver1D.FixedPointFinderBrent (0., new CreditCurveCalibrator (cc, comp,
					iInstr, valParams, dc, dcTSY, dcEDSF, pricerParamsIn, strMeasure, dblCalibValue,
						mmFixings, quotingParams, bFlat)).findRoot();

			return null != rfop && rfop.containsRoot();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public double calibrateIRNode (
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.product.definition.Component comp,
		final int iInstr,
		final org.drip.param.valuation.ValuationParams valParams,
		final java.lang.String strMeasure,
		final double dblCalibValue,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat,
		final double dblSearchStart)
		throws java.lang.Exception
	{
		if (null == dc || null == comp || null == valParams || null == strMeasure || strMeasure.isEmpty() ||
			!org.drip.math.common.NumberUtil.IsValid (dblCalibValue))
			throw new java.lang.Exception ("CurveCalibrator.calibrateIRNode => Invalid inputs!");

		org.drip.math.algodiff.ObjectiveFunction ofIRNode = new org.drip.math.algodiff.ObjectiveFunction
			(null) {
			public double evaluate (
				final double dblValue)
				throws java.lang.Exception
			{
				if (!SetNode (dc, iInstr, bFlat, dblValue))
					throw new java.lang.Exception ("Cannot set Value = " + dblValue + " for node " + iInstr);

				return dblCalibValue - comp.calcMeasureValue (valParams, null,
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc,
						dcTSY, dcEDSF, null, null, null, mmFixings), quotingParams, strMeasure);
			}
		};

		org.drip.math.solver1D.FixedPointFinderOutput rfop = new
			org.drip.math.solver1D.FixedPointFinderBrent (0., ofIRNode).findRoot();

		if (null == rfop || !rfop.containsRoot())
			throw new java.lang.Exception ("Cannot calibrate IR segment for node #" + iInstr);

		return rfop.getRoot();
	}

	public boolean bootstrapInterestRate (
		final org.drip.analytics.curve.PolynomialSplineDF dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.product.definition.Component[] aCalibComp,
		final org.drip.param.valuation.ValuationParams valParams,
		final java.lang.String[] astrCalibMeasure,
		final double[] adblCalibValue,
		final double dblBump,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat)
	{
		if (null == adblCalibValue || null == aCalibComp || null == astrCalibMeasure || 0 ==
			aCalibComp.length || adblCalibValue.length != aCalibComp.length || adblCalibValue.length !=
				astrCalibMeasure.length)
			return false;

		double dblSearchStart = java.lang.Double.NaN;

		for (int i = 0; i < aCalibComp.length; ++i) {
			try {
				if (!org.drip.math.common.NumberUtil.IsValid (calibrateIRNode (dc, dcTSY, dcEDSF,
					aCalibComp[i], i, valParams, astrCalibMeasure[i], adblCalibValue[i] + dblBump, mmFixings,
						quotingParams, false, dblSearchStart)))
					return false;
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		return true;
	}

	public boolean bootstrapInterestRateSequence (
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.product.definition.Component[] aCalibComp,
		final org.drip.param.valuation.ValuationParams valParams,
		final java.lang.String[] astrCalibMeasure,
		final double[] adblCalibValue,
		final double dblBump,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat)
	{
		if (null == dc || null == adblCalibValue || null == aCalibComp || null == astrCalibMeasure || 0 ==
			aCalibComp.length || adblCalibValue.length != aCalibComp.length || adblCalibValue.length !=
				astrCalibMeasure.length)
			return false;

		if (!(dc instanceof org.drip.analytics.curve.PolynomialSplineDF)) {
			for (int i = 0; i < adblCalibValue.length; ++i) {
				try {
					if (!org.drip.math.common.NumberUtil.IsValid (calibrateIRNode (dc, dcTSY, dcEDSF,
						aCalibComp[i], i, valParams, astrCalibMeasure[i], adblCalibValue[i] + dblBump,
							mmFixings, quotingParams, false, java.lang.Double.NaN)))
						return false;
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return false;
				}
			}

			return true;
		}

		try {
			calibrateIRCurve ((org.drip.analytics.curve.PolynomialSplineDF) dc, dcTSY, dcEDSF, aCalibComp,
				valParams, astrCalibMeasure, adblCalibValue, dblBump, mmFixings, quotingParams);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}
}
