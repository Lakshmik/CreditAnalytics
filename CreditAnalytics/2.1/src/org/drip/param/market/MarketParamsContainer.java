
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
 * This class serves as the place holder for the comprehensive suite of the market set of curves for the
 * 		given date. Contains treasury quote map, fixings map, IR scenario curve map, credit curve scenario
 * 		set map, and component quote map.
 *
 * @author Lakshmi Krishnamurthy
 */

public class MarketParamsContainer extends org.drip.param.definition.MarketParams {
	private static final int BASE = 0;
	private static final int BUMP_UP = 1;
	private static final int BUMP_DN = 2;
	private static final int RR_BUMP_UP = 4;
	private static final int RR_BUMP_DN = 8;

	private java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> _mapCQTSY = null;
	private java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> _mmFixings = null;

	private java.util.Map<java.lang.String, org.drip.param.definition.RatesScenarioCurve> _mapIRCSC = new
		java.util.HashMap<java.lang.String, org.drip.param.definition.RatesScenarioCurve>();

	private java.util.Map<java.lang.String, org.drip.param.definition.CreditScenarioCurve> _mapCCSC = new
		java.util.HashMap<java.lang.String, org.drip.param.definition.CreditScenarioCurve>();

	private java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> _mapCQComp = new
		java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentQuote>();

	private java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> _mapScenBMP = new
		java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

	private java.util.Map<java.lang.String, org.drip.param.definition.ComponentMarketParams> _mapScenCMP =
		new java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentMarketParams>();

	private java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> getDCSet (
		final int iBumpType)
	{
		if (null == _mapIRCSC.entrySet()) return null;

		java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> mapDC = new
			java.util.HashMap<java.lang.String, org.drip.analytics.definition.DiscountCurve>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.RatesScenarioCurve> meDCSG :
			_mapIRCSC.entrySet()) {
			if (null != meDCSG.getKey() && null != meDCSG.getValue()) {
				if (BASE == iBumpType)
					mapDC.put (meDCSG.getKey(), meDCSG.getValue().getDCBase());
				else if (BUMP_UP == iBumpType)
					mapDC.put (meDCSG.getKey(), meDCSG.getValue().getDCBumpUp());
				else if (BUMP_DN == iBumpType)
					mapDC.put (meDCSG.getKey(), meDCSG.getValue().getDCBumpDn());
			}
		}

		return mapDC;
	}

	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> getCCSet (
		final int iBumpType)
	{
		if (null == _mapCCSC.entrySet()) return null;

		java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> mapCC = new
			java.util.HashMap<java.lang.String, org.drip.analytics.definition.CreditCurve>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.CreditScenarioCurve> meCCSG :
			_mapCCSC.entrySet()) {
			if (null != meCCSG.getKey() && null != meCCSG.getValue()) {
				if (BASE == iBumpType)
					mapCC.put (meCCSG.getKey(), meCCSG.getValue().getCCBase());
				else if (BUMP_UP == iBumpType)
					mapCC.put (meCCSG.getKey(), meCCSG.getValue().getCCBumpUp());
				else if (BUMP_DN == iBumpType)
					mapCC.put (meCCSG.getKey(), meCCSG.getValue().getCCBumpDn());
				else if (RR_BUMP_UP == iBumpType)
					mapCC.put (meCCSG.getKey(), meCCSG.getValue().getCCRecoveryUp());
				else if (RR_BUMP_DN == iBumpType)
					mapCC.put (meCCSG.getKey(), meCCSG.getValue().getCCRecoveryDn());
			}
		}

		return mapCC;
	}

	private java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve>
		getSpecificIRFlatBumpDCSet (
			final java.lang.String strIRCurve,
			final boolean bBumpUp)
	{
		if (null == strIRCurve || strIRCurve.isEmpty() || null == _mapIRCSC.get (strIRCurve)) return null;

		java.util.Map<java.lang.String, org.drip.analytics.definition.DiscountCurve> mapDC = getDCSet (BASE);

		if (null == mapDC) return null;

		mapDC.put (strIRCurve, bBumpUp ? _mapIRCSC.get (strIRCurve).getDCBumpUp() : _mapIRCSC.get
			(strIRCurve).getDCBumpDn());

		return mapDC;
	}

	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getSpecificCreditFlatBumpCCSet (
			final java.lang.String strCreditCurve,
			final boolean bBumpUp)
	{
		if (null == strCreditCurve || strCreditCurve.isEmpty() || null == _mapCCSC.get (strCreditCurve))
			return null;

		java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> mapCC = getCCSet (BASE);

		if (null == mapCC || null == _mapCCSC.get (strCreditCurve)) return null;

		mapCC.put (strCreditCurve, bBumpUp ? _mapCCSC.get (strCreditCurve).getCCBumpUp() : _mapCCSC.get
			(strCreditCurve).getCCBumpDn());

		return mapCC;
	}

	private java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve>
		getSpecificCreditFlatBumpRRSet (
			final java.lang.String strCreditCurve,
			final boolean bBumpUp)
	{
		if (null == strCreditCurve || strCreditCurve.isEmpty() || null == _mapCCSC.get (strCreditCurve))
			return null;

		java.util.Map<java.lang.String, org.drip.analytics.definition.CreditCurve> mapCC = getCCSet (BASE);

		if (null == mapCC || null == _mapCCSC.get (strCreditCurve)) return null;

		mapCC.put (strCreditCurve, bBumpUp ? _mapCCSC.get (strCreditCurve).getCCRecoveryUp() : _mapCCSC.get
			(strCreditCurve).getCCRecoveryDn());

		return mapCC;
	}

	/**
	 * Constructs an empty MarketParamsContainer instance
	 */

	public MarketParamsContainer()
	{
	}

	@Override public boolean addScenDC (
		final java.lang.String strName,
		final org.drip.param.definition.RatesScenarioCurve irsg)
	{
		if (null != strName && !strName.isEmpty() && null != irsg) {
			_mapIRCSC.put (strName, irsg);

			return true;
		}

		return false;
	}

	@Override public boolean removeScenDC (
		final java.lang.String strName)
	{
		if (null != strName && !strName.isEmpty()) {
			_mapIRCSC.remove (strName);

			return true;
		}

		return false;
	}

	@Override public boolean addScenCC (
		final java.lang.String strName,
		final org.drip.param.definition.CreditScenarioCurve ccsg)
	{
		if (null != strName && !strName.isEmpty() && null != ccsg) {
			_mapCCSC.put (strName, ccsg);

			return true;
		}

		return false;
	}

	@Override public boolean removeScenCC (
		final java.lang.String strName)
	{
		if (null != strName && !strName.isEmpty()) {
			_mapCCSC.remove (strName);

			return true;
		}

		return false;
	}

	@Override public boolean addTSYQuote (
		final java.lang.String strBenchmark,
		final org.drip.param.definition.ComponentQuote cqTSY)
	{
		if (null == strBenchmark || strBenchmark.isEmpty() || null == cqTSY) return false;

		if (null == _mapCQTSY)
			_mapCQTSY = new java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentQuote>();

		_mapCQTSY.put (strBenchmark, cqTSY);

		return true;
	}

	@Override public boolean removeTSYQuote (
		final java.lang.String strBenchmark)
	{
		if (null == strBenchmark || strBenchmark.isEmpty()) return false;

		if (null == _mapCQTSY) return true;

		_mapCQTSY.remove (strBenchmark);

		return true;
	}

	@Override public boolean setTSYQuotes (
		final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mapCQTSY)
	{
		_mapCQTSY = mapCQTSY;
		return true;
	}

	@Override public org.drip.param.definition.ComponentQuote getTSYQuote (
		final java.lang.String strBenchmark)
	{
		if (null == _mapCQTSY || null == strBenchmark || strBenchmark.isEmpty()) return null;

		return _mapCQTSY.get (strBenchmark);
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> getTSYQuotes()
	{
		return _mapCQTSY;
	}

	@Override public boolean addFixings (
		final org.drip.analytics.date.JulianDate dtFix,
		final java.lang.String strIndex,
		final double dblFixing)
	{
		if (null == dtFix || null == strIndex || strIndex.isEmpty() ||
			!org.drip.math.common.NumberUtil.IsValid (dblFixing))
			return false;

		if (null == _mmFixings)
			_mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = _mmFixings.get (dtFix);

		if (null == mIndexFixings)
			mIndexFixings = new java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put (strIndex, dblFixing);

		_mmFixings.put (dtFix, mIndexFixings);

		return true;
	}

	@Override public boolean removeFixings (
		final org.drip.analytics.date.JulianDate dtFix,
		final java.lang.String strIndex)
	{
		if (null == dtFix || null == strIndex || strIndex.isEmpty()) return false;

		if (null == _mmFixings) return true;

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = _mmFixings.get (dtFix);

		if (null == mIndexFixings) return true;

		mIndexFixings.remove (strIndex);

		_mmFixings.put (dtFix, mIndexFixings);

		return true;
	}

	@Override public java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getFixings()
	{
		return _mmFixings;
	}

	@Override public boolean addCompQuote (
		final java.lang.String strCompID,
		final org.drip.param.definition.ComponentQuote cqComp)
	{
		if (null == strCompID || strCompID.isEmpty() || null == cqComp) return false;

		if (null == _mapCQComp)
			_mapCQComp = new java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentQuote>();

		_mapCQComp.put (strCompID, cqComp);

		return true;
	}

	@Override public boolean removeCompQuote (
		final java.lang.String strCompID)
	{
		if (null == strCompID || strCompID.isEmpty()) return false;

		if (null == _mapCQComp) return true;

		_mapCQComp.remove (strCompID);

		return true;
	}

	@Override public boolean addCompQuotes (
		final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mCompQuotes)
	{
		_mapCQComp = mCompQuotes;
		return true;
	}

	@Override public org.drip.param.definition.ComponentQuote getCompQuote (
		final java.lang.String strCompID)
	{
		if (null == _mapCQComp || null == strCompID || strCompID.isEmpty()) return null;

		return _mapCQComp.get (strCompID);
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote>
		getCompQuotes()
	{
		return _mapCQComp;
	}

	@Override public boolean addScenBMP (
		final java.lang.String strScenarioName,
		final org.drip.param.definition.BasketMarketParams bmp)
	{
		if (null == strScenarioName || strScenarioName.isEmpty() || null == bmp) return false;

		_mapScenBMP.put (strScenarioName, bmp);

		return true;
	}

	@Override public org.drip.param.definition.BasketMarketParams getScenBMP (
		final java.lang.String strScenarioName)
	{
		if (null == strScenarioName || strScenarioName.isEmpty()) return null;

		return _mapScenBMP.get (strScenarioName);
	}

	@Override public boolean addScenCMP (
		final java.lang.String strScenarioName,
		final org.drip.param.definition.ComponentMarketParams cmp)
	{
		if (null == strScenarioName || strScenarioName.isEmpty() || null == cmp) return false;

		_mapScenCMP.put (strScenarioName, cmp);

		return true;
	}

	@Override public org.drip.param.definition.ComponentMarketParams getScenCMP (
		final java.lang.String strScenarioName)
	{
		if (null == strScenarioName || strScenarioName.isEmpty()) return null;

		return _mapScenCMP.get (strScenarioName);
	}

	@Override public org.drip.param.definition.ComponentMarketParams getScenCMP (
		final org.drip.product.definition.Component comp,
		final java.lang.String strScen)
	{
		if (null == comp || null == strScen || strScen.isEmpty()) return null;

		org.drip.analytics.definition.CreditCurve cc = null;
		org.drip.analytics.definition.DiscountCurve dc = null;
		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null != comp.getIRCurveName() && null != _mapIRCSC.get (comp.getIRCurveName()))
			dc = _mapIRCSC.get (comp.getIRCurveName()).getDCBase();

		if (null != comp.getTreasuryCurveName() && null != _mapIRCSC.get (comp.getTreasuryCurveName()))
			dcTSY = _mapIRCSC.get (comp.getTreasuryCurveName()).getDCBase();

		if (null != comp.getEDSFCurveName() && null != _mapIRCSC.get (comp.getEDSFCurveName()))
			dcEDSF = _mapIRCSC.get (comp.getEDSFCurveName()).getDCBase();

		if (null != comp.getCreditCurveName() && null != _mapCCSC.get (comp.getCreditCurveName()))
			cc = _mapCCSC.get (comp.getCreditCurveName()).getCCBase();

		if ("FlatIRBumpUp".equalsIgnoreCase (strScen) && null != comp.getIRCurveName() && null !=
			_mapIRCSC.get (comp.getIRCurveName()))
			dc = _mapIRCSC.get (comp.getIRCurveName()).getDCBumpUp();

		if ("FlatIRBumpDn".equalsIgnoreCase (strScen) && null != comp.getIRCurveName() && null !=
			_mapIRCSC.get (comp.getIRCurveName()))
			dc = _mapIRCSC.get (comp.getIRCurveName()).getDCBumpDn();

		if ("FlatCreditBumpUp".equalsIgnoreCase (strScen) && null != comp.getCreditCurveName() && null !=
			_mapCCSC.get (comp.getCreditCurveName()))
			cc = _mapCCSC.get (comp.getCreditCurveName()).getCCBumpUp();

		if ("FlatCreditBumpDn".equalsIgnoreCase (strScen) && null != comp.getCreditCurveName() && null !=
			_mapCCSC.get (comp.getCreditCurveName()))
			cc = _mapCCSC.get (comp.getCreditCurveName()).getCCBumpDn();

		return org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, dcTSY,
			dcEDSF, cc, _mapCQComp.get (comp.getComponentName()), _mapCQTSY, _mmFixings);
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.ComponentMarketParams>
		getIRTenorCMP (
			final org.drip.product.definition.Component comp,
			final boolean bBumpUp)
	{
		if (null == comp || null == comp.getIRCurveName() || null == _mapIRCSC.get (comp.getIRCurveName()))
			return null;

		if (bBumpUp && (null == _mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpUp() || null ==
			_mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpUp().entrySet()))
			return null;

		if (!bBumpUp && (null == _mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpDn() || null ==
			_mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpDn().entrySet()))
			return null;

		org.drip.analytics.definition.CreditCurve cc = null;
		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null != comp.getTreasuryCurveName() && null != _mapIRCSC.get (comp.getTreasuryCurveName()))
			dcTSY = _mapIRCSC.get (comp.getTreasuryCurveName()).getDCBase();

		if (null != comp.getEDSFCurveName() && null != _mapIRCSC.get (comp.getEDSFCurveName()))
			dcEDSF = _mapIRCSC.get (comp.getEDSFCurveName()).getDCBase();

		if (null != comp.getCreditCurveName() && null != _mapCCSC.get (comp.getCreditCurveName()))
			cc = _mapCCSC.get (comp.getCreditCurveName()).getCCBase();

		java.util.Map<java.lang.String, org.drip.param.definition.ComponentMarketParams> mapCMP = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentMarketParams>();

		if (bBumpUp) {
			if (null == _mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpUp() || null == _mapIRCSC.get
				(comp.getIRCurveName()).getTenorDCBumpUp().entrySet())
				return null;

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.DiscountCurve> meDC :
				_mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpUp().entrySet()) {
				if (null == meDC || null == meDC.getKey() || meDC.getKey().isEmpty()) continue;

				mapCMP.put (meDC.getKey(),
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
						(meDC.getValue(), dcTSY, dcEDSF, cc, _mapCQComp.get (comp.getComponentName()),
							_mapCQTSY, _mmFixings));
			}
		} else {
			if (null == _mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpDn() || null == _mapIRCSC.get
				(comp.getIRCurveName()).getTenorDCBumpDn().entrySet())
				return null;

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.DiscountCurve> meDC :
				_mapIRCSC.get (comp.getIRCurveName()).getTenorDCBumpDn().entrySet()) {
				if (null == meDC || null == meDC.getKey() || meDC.getKey().isEmpty()) continue;

				mapCMP.put (meDC.getKey(),
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
						(meDC.getValue(), dcTSY, dcEDSF, cc, _mapCQComp.get (comp.getComponentName()),
							_mapCQTSY, _mmFixings));
			}
		}

		return mapCMP;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.ComponentMarketParams>
		getCreditTenorCMP (
			final org.drip.product.definition.Component comp,
			final boolean bBumpUp)
	{
		if (null == comp || null == comp.getCreditCurveName() || null == _mapCCSC.get
			(comp.getCreditCurveName()))
			return null;

		if (bBumpUp && (null == _mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpUp() || null ==
			_mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpUp().entrySet()))
			return null;

		if (!bBumpUp && (null == _mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpDn() || null ==
			_mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpDn().entrySet()))
			return null;

		org.drip.analytics.definition.DiscountCurve dc = null;
		org.drip.analytics.definition.DiscountCurve dcTSY = null;
		org.drip.analytics.definition.DiscountCurve dcEDSF = null;

		if (null != comp.getIRCurveName() && null != _mapIRCSC.get (comp.getIRCurveName()))
			dc = _mapIRCSC.get (comp.getIRCurveName()).getDCBase();

		if (null != comp.getTreasuryCurveName() && null != _mapIRCSC.get (comp.getTreasuryCurveName()))
			dcTSY = _mapIRCSC.get (comp.getTreasuryCurveName()).getDCBase();

		if (null != comp.getEDSFCurveName() && null != _mapIRCSC.get (comp.getEDSFCurveName()))
			dcEDSF = _mapIRCSC.get (comp.getEDSFCurveName()).getDCBase();

		java.util.Map<java.lang.String, org.drip.param.definition.ComponentMarketParams> mapCMP = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.ComponentMarketParams>();

		if (bBumpUp) {
			if (null == _mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpUp() || null == _mapCCSC.get
				(comp.getCreditCurveName()).getTenorCCBumpUp().entrySet())
				return null;

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.CreditCurve> meCC :
				_mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpUp().entrySet()) {
				if (null == meCC || null == meCC.getKey() || meCC.getKey().isEmpty()) continue;

				mapCMP.put (meCC.getKey(),
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc,
						dcTSY, dcEDSF, meCC.getValue(), _mapCQComp.get (comp.getComponentName()), _mapCQTSY,
							_mmFixings));
			}
		} else {
			if (null == _mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpDn() || null == _mapCCSC.get
				(comp.getCreditCurveName()).getTenorCCBumpDn().entrySet())
				return null;

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.CreditCurve> meCC :
				_mapCCSC.get (comp.getCreditCurveName()).getTenorCCBumpDn().entrySet()) {
				if (null == meCC || null == meCC.getKey() || meCC.getKey().isEmpty()) continue;

				mapCMP.put (meCC.getKey(),
					org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams (dc,
						dcTSY, dcEDSF, meCC.getValue(), _mapCQComp.get (comp.getComponentName()), _mapCQTSY,
							_mmFixings));
			}
		}

		return mapCMP;
	}

	@Override public org.drip.param.definition.BasketMarketParams getScenBMP (
		final org.drip.product.definition.BasketProduct bp,
		final java.lang.String strScen)
	{
		if (null == strScen) return null;

		if ("Base".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BASE), getCCSet (BASE), null, _mmFixings);

		if ("FlatIRBumpUp".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BUMP_UP), getCCSet (BASE), null, _mmFixings);

		if ("FlatIRBumpDn".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BUMP_DN), getCCSet (BASE), null, _mmFixings);

		if ("FlatCreditBumpUp".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BASE), getCCSet (BUMP_UP), null, _mmFixings);

		if ("FlatCreditBumpDn".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BASE), getCCSet (BUMP_DN), null, _mmFixings);

		if ("FlatRRBumpUp".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BASE), getCCSet (RR_BUMP_UP), null, _mmFixings);

		if ("FlatRRBumpDn".equalsIgnoreCase (strScen))
			return org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
				(BASE), getCCSet (RR_BUMP_DN), null, _mmFixings);

		return null;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams>
		getIRBumpBMP (
			final org.drip.product.definition.BasketProduct bp,
			final boolean bBump)
	{
		java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> mapBMP = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.RatesScenarioCurve> meDCSG :
			_mapIRCSC.entrySet()) {
			if (null != meDCSG && null != meDCSG.getKey())
				mapBMP.put (meDCSG.getKey(),
					org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams
						(getSpecificIRFlatBumpDCSet (meDCSG.getKey(), bBump), getCCSet (BASE), null,
							_mmFixings));
		}

		return mapBMP;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams>
		getCreditBumpBMP (
			final org.drip.product.definition.BasketProduct bp,
			final boolean bBump)
	{
		java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> mapBMP = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.CreditScenarioCurve> meCCSG :
			_mapCCSC.entrySet()) {
			if (null != meCCSG && null != meCCSG.getKey())
				mapBMP.put (meCCSG.getKey(),
					org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
						(BASE), getSpecificCreditFlatBumpCCSet (meCCSG.getKey(), bBump), null, _mmFixings));
		}

		return mapBMP;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams>
		getRecoveryBumpBMP (
			final org.drip.product.definition.BasketProduct bp,
			final boolean bBump)
	{
		java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> mapBMP = new
			java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.CreditScenarioCurve> meCCSG :
			_mapCCSC.entrySet()) {
			if (null != meCCSG && null != meCCSG.getKey())
				mapBMP.put (meCCSG.getKey(),
					org.drip.param.creator.BasketMarketParamsBuilder.CreateBasketMarketParams (getDCSet
						(BASE), getSpecificCreditFlatBumpRRSet (meCCSG.getKey(), bBump), null, _mmFixings));
		}

		return mapBMP;
	}

	@Override public java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		org.drip.param.definition.BasketMarketParams>>
			getIRTenorBumpBMP (
				final org.drip.product.definition.BasketProduct bp,
				final boolean bBump)
	{
		if (null == bp) return null;

		java.util.Map<java.lang.String, java.util.Map<java.lang.String,
			org.drip.param.definition.BasketMarketParams>> mmIRTenorBMP = new
				java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					org.drip.param.definition.BasketMarketParams>>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.RatesScenarioCurve> meDCSG :
			_mapIRCSC.entrySet()) {
			if (null == meDCSG) continue;

			if (bBump && (null == meDCSG.getValue() || null == meDCSG.getValue().getTenorDCBumpUp() || null
				== meDCSG.getValue().getTenorDCBumpUp().entrySet()))
				return null;

			java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> mapTenorBMP = new
				java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.DiscountCurve> meDC :
				(bBump ? meDCSG.getValue().getTenorDCBumpUp().entrySet() :
					meDCSG.getValue().getTenorDCBumpDn().entrySet())) {
				if (null == meDC || null == meDCSG.getKey() || meDCSG.getKey().isEmpty()) continue;

				org.drip.param.definition.BasketMarketParams bmp = getScenBMP (bp, "Base");

				if (null != bmp) {
					bmp.addDC (meDCSG.getKey(), meDC.getValue());

					mapTenorBMP.put (meDC.getKey(), bmp);
				}
			}

			mmIRTenorBMP.put (meDCSG.getKey(), mapTenorBMP);
		}

		return mmIRTenorBMP;
	}

	@Override public java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		org.drip.param.definition.BasketMarketParams>>
			getCreditTenorBumpBMP (
				final org.drip.product.definition.BasketProduct bp,
				final boolean bBump)
	{
		if (null == bp) return null;

		java.util.Map<java.lang.String, java.util.Map<java.lang.String,
			org.drip.param.definition.BasketMarketParams>> mmCreditTenorBMP = new
				java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					org.drip.param.definition.BasketMarketParams>>();

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.CreditScenarioCurve> meCCSG :
			_mapCCSC.entrySet()) {
			if (null == meCCSG) continue;

			if (bBump && (null == meCCSG.getValue() || null == meCCSG.getValue().getTenorCCBumpUp() || null
				== meCCSG.getValue().getTenorCCBumpUp().entrySet()))
				return null;

			java.util.Map<java.lang.String, org.drip.param.definition.BasketMarketParams> mapTenorBMP = new
				java.util.HashMap<java.lang.String, org.drip.param.definition.BasketMarketParams>();

			for (java.util.Map.Entry<java.lang.String, org.drip.analytics.definition.CreditCurve> meCC :
				(bBump ? meCCSG.getValue().getTenorCCBumpUp().entrySet() :
					meCCSG.getValue().getTenorCCBumpDn().entrySet())) {
				if (null == meCC || null == meCCSG.getKey() || meCCSG.getKey().isEmpty()) continue;

				org.drip.param.definition.BasketMarketParams bmp = getScenBMP (bp, "Base");

				if (null != bmp) {
					bmp.addCC (meCCSG.getKey(), meCC.getValue());

					mapTenorBMP.put (meCC.getKey(), bmp);
				}
			}

			mmCreditTenorBMP.put (meCCSG.getKey(), mapTenorBMP);
		}

		return mmCreditTenorBMP;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.RatesScenarioCurve> getIRSG()
	{
		return _mapIRCSC;
	}

	@Override public java.util.Map<java.lang.String, org.drip.param.definition.CreditScenarioCurve> getCCSG()
	{
		return _mapCCSC;
	}
}
