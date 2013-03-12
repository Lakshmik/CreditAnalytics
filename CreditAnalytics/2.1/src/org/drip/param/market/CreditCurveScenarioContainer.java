
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
 * This class contains the place holder for the bump parameters and the curves for the different credit curve
 * 		scenarios. Contains the spread and the recovery bumps, and the credit curve scenario generator object
 * 		that wraps the calibration instruments. It also contains the base credit curve, spread bumped up/down
 * 		credit curves, recovery bumped up/down credit curves, and the tenor mapped up/down credit curves.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditCurveScenarioContainer extends org.drip.param.definition.CreditScenarioCurve {
	private static final boolean s_bBlog = false;

	private double _dblCouponBump = 0.0001;
	private double _dblRecoveryBump = 0.01;
	private org.drip.analytics.definition.CreditCurve _ccBase = null;
	private org.drip.analytics.definition.CreditCurve _ccBumpUp = null;
	private org.drip.analytics.definition.CreditCurve _ccBumpDn = null;
	private org.drip.analytics.definition.CreditCurve _ccRecoveryUp = null;
	private org.drip.analytics.definition.CreditCurve _ccRecoveryDn = null;
	private org.drip.analytics.calibration.CreditCurveScenarioGenerator _ccsg = null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> _mapCustomCC = null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> _mapTenorCCBumpUp =
		null;
	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> _mapTenorCCBumpDn =
		null;

	/**
	 * Constructs CreditCurveScenarioContainer from the array of calibration instruments, the coupon bump
	 * 		parameter, and the recovery bump parameter
	 * 
	 * @param aCalibInst Array of calibration instruments
	 * @param dblCouponBump Coupon Bump
	 * @param dblRecoveryBump Recovery Bump
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public CreditCurveScenarioContainer (
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double dblCouponBump,
		final double dblRecoveryBump)
		throws java.lang.Exception
	{
		if (null == aCalibInst || 0 == aCalibInst.length || !org.drip.math.common.NumberUtil.IsValid
			(_dblCouponBump = dblCouponBump) || !org.drip.math.common.NumberUtil.IsValid (_dblRecoveryBump =
				dblRecoveryBump) || null == (_ccsg = new
					org.drip.analytics.calibration.CreditCurveScenarioGenerator (aCalibInst)))
			throw new java.lang.Exception ("Invalid Calib instruments into CCSC ctr");
	}

	@Override public boolean cookScenarioCC (
		final java.lang.String strName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblRecovery,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat,
		final int iCCScenario)
	{
		if (null == _ccsg || null == dc || null == adblQuotes || 0 == adblQuotes.length ||
			!org.drip.math.common.NumberUtil.IsValid (dblRecovery) || null == astrCalibMeasure || 0 ==
				astrCalibMeasure.length || astrCalibMeasure.length != adblQuotes.length) {
			if (s_bBlog) System.out.println ("Bad CCSC.cookScenarioCC Input params!");

			return false;
		}

		if (null == (_ccBase = _ccsg.createCC (strName, valParams, dc, dcTSY, dcEDSF, adblQuotes,
			dblRecovery, astrCalibMeasure, mmFixings, quotingParams, bFlat))) {
			if (s_bBlog) System.out.println ("Bad ccBase Cook!");

			return false;
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_FLAT_UP & iCCScenario)) {
			if (null == (_ccBumpUp = _ccsg.createCC (strName, valParams, dc, dcTSY, dcEDSF,
				org.drip.analytics.support.AnalyticsHelper.BumpQuotes (adblQuotes, _dblCouponBump, false),
					dblRecovery, astrCalibMeasure, mmFixings, quotingParams, bFlat))) {
				if (s_bBlog) System.out.println ("Bad ccBumpUp Cook!");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_FLAT_DN & iCCScenario)) {
			if (null == (_ccBumpDn = _ccsg.createCC (strName, valParams, dc, dcTSY, dcEDSF,
				org.drip.analytics.support.AnalyticsHelper.BumpQuotes (adblQuotes, -_dblCouponBump,
					false), dblRecovery, astrCalibMeasure, mmFixings, quotingParams, bFlat))) {
				if (s_bBlog) System.out.println ("Bad ccBumpDn Cook!");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_TENOR_UP & iCCScenario)) {
			if (null == (_mapTenorCCBumpUp = _ccsg.createTenorCCMap (strName, valParams, dc, dcTSY, dcEDSF,
				adblQuotes, _dblCouponBump, dblRecovery, astrCalibMeasure, mmFixings, quotingParams, bFlat)))
			{
				if (s_bBlog) System.out.println ("Bad ccPartialUp Cook!");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_TENOR_DN & iCCScenario)) {
			if (null == (_mapTenorCCBumpDn = _ccsg.createTenorCCMap (strName, valParams, dc, dcTSY, dcEDSF,
				adblQuotes, -_dblCouponBump, dblRecovery, astrCalibMeasure, mmFixings, quotingParams,
					bFlat))) {
				if (s_bBlog) System.out.println ("Bad ccPartialDn Cook!");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_RR_FLAT_UP & iCCScenario)) {
			if (null == (_ccRecoveryUp = _ccsg.createCC (strName, valParams, dc, dcTSY, dcEDSF, adblQuotes,
				dblRecovery + _dblRecoveryBump, astrCalibMeasure, mmFixings, quotingParams, bFlat))) {
				if (s_bBlog) System.out.println ("Bad ccRRUp Cook!");

				return false;
			}
		}

		if (0 != (org.drip.param.definition.CreditScenarioCurve.CC_RR_FLAT_DN & iCCScenario)) {
			if (null == (_ccRecoveryDn = _ccsg.createCC (strName, valParams, dc, dcTSY, dcEDSF, adblQuotes,
				dblRecovery - _dblRecoveryBump, astrCalibMeasure, mmFixings, quotingParams, bFlat))) {
				if (s_bBlog) System.out.println ("Bad ccRRDn Cook!");

				return false;
			}
		}

		return true;
	}

	@Override public boolean cookCustomCC (
		final java.lang.String strName,
		final java.lang.String strCustomName,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final double[] adblQuotes,
		final double dblRecovery,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final boolean bFlat,
		final org.drip.param.definition.NodeTweakParams ntpDC,
		final org.drip.param.definition.NodeTweakParams ntpTSY,
		final org.drip.param.definition.NodeTweakParams ntpEDSF,
		final org.drip.param.definition.NodeTweakParams ntpCC)
	{
		if (null == strCustomName || strCustomName.isEmpty() || null == _ccsg || null == dc || null ==
			adblQuotes || 0 == adblQuotes.length || !org.drip.math.common.NumberUtil.IsValid (dblRecovery) ||
				null == astrCalibMeasure || 0 == astrCalibMeasure.length || astrCalibMeasure.length !=
					adblQuotes.length || (null == ntpDC && null == ntpTSY && null == ntpEDSF && null ==
						ntpCC)) {
			if (s_bBlog) System.out.println ("Bad CCSC.cookCustomCC Input params!");

			return false;
		}

		org.drip.analytics.definition.DiscountCurve dcAdj = (org.drip.analytics.definition.DiscountCurve)
			dc.createTweakedCurve (ntpDC);

		org.drip.analytics.definition.DiscountCurve dcTSYAdj = (org.drip.analytics.definition.DiscountCurve)
			dcTSY.createTweakedCurve (ntpTSY);

		org.drip.analytics.definition.DiscountCurve dcEDSFAdj = (org.drip.analytics.definition.DiscountCurve)
			dcEDSF.createTweakedCurve (ntpEDSF);

		org.drip.analytics.definition.CreditCurve ccBaseCustom = _ccsg.createCC (strName, valParams, null ==
			dcAdj ? dc : dcAdj, null == dcTSYAdj ? dcTSY : dcTSYAdj, null == dcEDSFAdj ? dcEDSF : dcEDSFAdj,
				adblQuotes, dblRecovery, astrCalibMeasure, mmFixings, quotingParams, bFlat);

		if (null == ccBaseCustom) {
			if (s_bBlog) System.out.println ("Cannot create ccBaseCustom!");

			return false;
		}

		if (null == _mapCustomCC)
			_mapCustomCC = new java.util.HashMap<java.lang.String,
				org.drip.analytics.definition.CreditCurve>();

		org.drip.analytics.definition.CreditCurve ccCustom = (org.drip.analytics.definition.CreditCurve)
			ccBaseCustom.createTweakedCurve (ntpCC);

		if (null == ccCustom)
			_mapCustomCC.put (strCustomName, ccBaseCustom);
		else
			_mapCustomCC.put (strCustomName, ccCustom);

		return true;
	}

	@Override public org.drip.analytics.definition.CreditCurve getCCBase()
	{
		return _ccBase;
	}

	@Override public org.drip.analytics.definition.CreditCurve getCCBumpUp()
	{
		return _ccBumpUp;
	}

	@Override public org.drip.analytics.definition.CreditCurve getCCBumpDn()
	{
		return _ccBumpDn;
	}

	@Override public org.drip.analytics.definition.CreditCurve getCCRecoveryUp()
	{
		return _ccRecoveryUp;
	}

	@Override public org.drip.analytics.definition.CreditCurve getCCRecoveryDn()
	{
		return _ccRecoveryDn;
	}

	@Override public java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getTenorCCBumpUp()
	{
		return _mapTenorCCBumpUp;
	}

	@Override public java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getTenorCCBumpDn()
	{
		return _mapTenorCCBumpDn;
	}
}
