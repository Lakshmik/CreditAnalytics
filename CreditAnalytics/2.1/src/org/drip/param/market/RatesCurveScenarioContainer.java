
package org.drip.param.market;

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
 * This class serves as the place holder for the different IR scenario curves. It contains the IR curve
 * 		scenario generator object that wraps the calibration instruments. It contains the base IR curve,
 * 		spread bumped up/down IR curves, and tenor mapped up/down credit curves.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RatesCurveScenarioContainer extends org.drip.param.definition.RatesScenarioCurve {
	private static final boolean s_bBlog = false;

	private org.drip.analytics.definition.DiscountCurve _dcBase = null;
	private org.drip.analytics.definition.DiscountCurve _dcBumpUp = null;
	private org.drip.analytics.definition.DiscountCurve _dcBumpDn = null;
	private org.drip.analytics.calibration.RatesCurveScenarioGenerator _irsg = null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> _mapCustomDC = null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> _mapDCBumpUp = null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> _mapDCBumpDn = null;

	/**
	 * Constructs an IRCurveScenarioContainer instance from the corresponding IRCurveScenarioGenerator
	 * 
	 * @param irsg IRCurveScenarioGenerator instance
	 * 
	 * @throws java.lang.Exception Thrown if the IRCurveScenarioGenerator instance is invalid
	 */

	public RatesCurveScenarioContainer (
		final org.drip.analytics.calibration.RatesCurveScenarioGenerator irsg)
		throws java.lang.Exception
	{
		if (null == (_irsg = irsg))
			throw new java.lang.Exception ("irsg cannot be bull in IRCurveScenarioGenerator ctr");
	}

	@Override public boolean cookScenarioDC (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblBump,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final int iDCMode)
	{
		if (null == valParams || null == adblQuotes || null == astrCalibMeasure || 0 == adblQuotes.length ||
			0 == astrCalibMeasure.length || adblQuotes.length != astrCalibMeasure.length ||
				!org.drip.math.common.NumberUtil.IsValid (dblBump) || null == _irsg) {
			System.out.println ("Invalid inputs/state in ircsc.cookScenarioDC");

			return false;
		}

		if (null == (_dcBase = _irsg.createIRCurve (valParams, dcTSY, dcEDSF, adblQuotes, 0.,
			astrCalibMeasure, mmFixings, quotingParams))) {
			System.out.println ("Base DC cook problem in ircsc.cookScenarioDC");

			return false;
		}

		if (0 != (org.drip.param.definition.RatesScenarioCurve.DC_FLAT_UP & iDCMode)) {
			if (null == (_dcBumpUp = _irsg.createIRCurve (valParams, dcTSY, dcEDSF, adblQuotes, dblBump,
				astrCalibMeasure, mmFixings, quotingParams))) {
				System.out.println ("Flat bump up DC cook problem in ircsc.cookScenarioDC");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.RatesScenarioCurve.DC_FLAT_DN & iDCMode)) {
			if (null == (_dcBumpDn = _irsg.createIRCurve (valParams, dcTSY, dcEDSF, adblQuotes, -dblBump,
				astrCalibMeasure, mmFixings, quotingParams))) {
				System.out.println ("Flat bump dn DC cook problem in ircsc.cookScenarioDC");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.RatesScenarioCurve.DC_TENOR_UP & iDCMode)) {
			if (null == (_mapDCBumpUp = _irsg.createTenorIRCurveMap (valParams, dcTSY, dcEDSF, adblQuotes,
				dblBump, astrCalibMeasure, mmFixings, quotingParams))) {
				System.out.println ("Partial bump up DC cook problem in ircsc.cookScenarioDC");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.RatesScenarioCurve.DC_TENOR_DN & iDCMode)) {
			if (null == (_mapDCBumpDn = _irsg.createTenorIRCurveMap (valParams, dcTSY, dcEDSF, adblQuotes,
				-dblBump, astrCalibMeasure, mmFixings, quotingParams))) {
				System.out.println ("Partial bump dn DC cook problem in ircsc.cookScenarioDC");

				return false;
			}
		}

		return true;
	}

	@Override public boolean cookCustomDC (
		final java.lang.String strCurveName,
		final java.lang.String strCustomName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final org.drip.param.definition.NodeTweakParams ntpTSY,
		final org.drip.param.definition.NodeTweakParams ntpEDSF,
		final org.drip.param.definition.NodeTweakParams ntpDC)
	{
		if (null == strCustomName || strCustomName.isEmpty() || null == _irsg || null == adblQuotes || 0 ==
			adblQuotes.length || null == astrCalibMeasure || 0 == astrCalibMeasure.length ||
				astrCalibMeasure.length != adblQuotes.length || (null == ntpTSY && null == ntpEDSF && null ==
					ntpDC)) {
			if (s_bBlog) System.out.println ("Bad IRSC.cookCustomDC Input params!");

			return false;
		}

		org.drip.analytics.definition.DiscountCurve dcTSYAdj = (org.drip.analytics.definition.DiscountCurve)
			dcTSY.createTweakedCurve (ntpTSY);

		if (null == dcTSYAdj) dcTSYAdj = dcTSY;

		org.drip.analytics.definition.DiscountCurve dcEDSFAdj = (org.drip.analytics.definition.DiscountCurve)
			dcEDSF.createTweakedCurve (ntpEDSF);

		if (null == dcEDSFAdj) dcEDSFAdj = dcEDSF;

		org.drip.analytics.definition.DiscountCurve dcBaseCustom = _irsg.createIRCurve (valParams, dcTSYAdj,
			dcEDSFAdj, adblQuotes, 0., astrCalibMeasure, mmFixings, quotingParams);

		if (null == dcBaseCustom) {
			if (s_bBlog) System.out.println ("Cannot create IRSC.customBaseDC!");

			return false;
		}

		if (null == _mapCustomDC)
			_mapCustomDC = new java.util.HashMap<java.lang.String,
				org.drip.analytics.definition.DiscountCurve>();

		org.drip.analytics.definition.DiscountCurve dcCustom = (org.drip.analytics.definition.DiscountCurve)
			dcBaseCustom.createTweakedCurve (ntpDC);

		if (null == dcCustom)
			_mapCustomDC.put (strCustomName, dcBaseCustom);
		else
			_mapCustomDC.put (strCustomName, dcCustom);

		return true;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getDCBase()
	{
		return _dcBase;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getDCBumpUp()
	{
		return _dcBumpUp;
	}

	@Override public org.drip.analytics.definition.DiscountCurve getDCBumpDn()
	{
		return _dcBumpDn;
	}

	@Override public java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve>
		getTenorDCBumpUp()
	{
		return _mapDCBumpUp;
	}

	@Override public java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve>
		getTenorDCBumpDn()
	{
		return _mapDCBumpDn;
	}
}
