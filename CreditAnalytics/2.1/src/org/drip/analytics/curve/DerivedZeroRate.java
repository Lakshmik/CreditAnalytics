
package org.drip.analytics.curve;

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
 * This class contains the baseline zero discount curve holder object. It maintains term structure for the
 * 		spot zeroes, the current discount factors, the accrual fractions, and the corresponding dates.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DerivedZeroRate extends org.drip.analytics.definition.ZeroCurve {
	private org.drip.analytics.definition.DiscountCurve _dc = null;

	private java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> _mapDF = new
		java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

	private java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> _mapZeroRate = new
		java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

	private java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> _mapYearFraction = new
		java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

	private void updateMapEntries (
		final double dblDate,
		final int iFreq,
		final java.lang.String strDC,
		final boolean bApplyCpnEOMAdj,
		final java.lang.String strCalendar,
		final double dblZCBump)
		throws java.lang.Exception
	{
		double dblYearFraction = org.drip.analytics.daycount.Convention.YearFraction
			(getStartDate().getJulian(), dblDate, strDC, bApplyCpnEOMAdj, dblDate, null, strCalendar);

		if (!org.drip.math.common.NumberUtil.IsValid (dblYearFraction) || 0. > dblYearFraction) return;

		org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate (dblDate);

		if (0. == dblYearFraction) {
			_mapDF.put (dt, 1.);

			_mapYearFraction.put (dt, 0.);

			_mapZeroRate.put (dt, 0.);

			return;
		}

		double dblBumpedZeroRate = org.drip.analytics.support.AnalyticsHelper.DF2Yield (iFreq, _dc.getDF
			(dblDate), dblYearFraction) + dblZCBump;

		_mapDF.put (dt, org.drip.analytics.support.AnalyticsHelper.Yield2DF (iFreq, dblBumpedZeroRate,
			dblYearFraction));

		_mapYearFraction.put (dt, dblYearFraction);

		_mapZeroRate.put (dt, dblBumpedZeroRate);
	}

	/**
	 * ZeroCurve constructor from period, work-out, settle, and quoting parameters
	 * 
	 * @param lsPeriod List of bond coupon periods
	 * @param dblWorkoutDate Work-out date
	 * @param dblCashPayDate Cash-Pay Date
	 * @param dc Discount Curve
	 * @param quotingParams Quoting Parameters
	 * @param dblZCBump DC Bump
	 * 
	 * @throws java.lang.Exception
	 */

	public DerivedZeroRate (
		final java.util.List<org.drip.analytics.period.Period> lsPeriod,
		final double dblWorkoutDate,
		final double dblCashPayDate,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final double dblZCBump)
		throws java.lang.Exception
	{
		if (null == (_dc = dc) || null == lsPeriod || 0 == lsPeriod.size() ||
			!org.drip.math.common.NumberUtil.IsValid (dblWorkoutDate) ||
				!org.drip.math.common.NumberUtil.IsValid (dblCashPayDate) ||
					!org.drip.math.common.NumberUtil.IsValid (dblZCBump))
			throw new java.lang.Exception ("Invalid date parameters into ZeroCurve!");

		int iFreq = 2;
		boolean bApplyCpnEOMAdj = true;
		java.lang.String strDC = "30/360";
		java.lang.String strCalendar = "";

		if (null != quotingParams) {
			strDC = quotingParams._strYieldDC;
			iFreq = quotingParams._iYieldFrequency;
			strCalendar = quotingParams._strYieldCalendar;
			bApplyCpnEOMAdj = quotingParams._bYieldApplyEOMAdj;
		}

		for (org.drip.analytics.period.Period period : lsPeriod)
			updateMapEntries (period.getPayDate(), iFreq, strDC, bApplyCpnEOMAdj, strCalendar, dblZCBump);

		updateMapEntries (dblWorkoutDate, iFreq, strDC, bApplyCpnEOMAdj, strCalendar, dblZCBump);

		updateMapEntries (dblCashPayDate, iFreq, strDC, bApplyCpnEOMAdj, strCalendar, dblZCBump);
	}

	/**
	 * DerivedZeroCurve de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if DerivedZeroCurve cannot be properly de-serialized
	 */

	public DerivedZeroRate (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("DerivedZeroCurve de-serializer: Invalid input Byte array");

		_dc = org.drip.analytics.creator.DiscountCurveBuilder.FromByteArray (ab,
			org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);
	}

	@Override public int numCalibNodes()
	{
		return _mapDF.size();
	}

	@Override public java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getCalibFixings()
	{
		return _dc.getCalibFixings();
	}

	@Override public double getDF (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("DerivedZeroCurve.getDF got NaN for date");

		if (dblDate <= getStartDate().getJulian()) return 1.;

		java.lang.Double objDF = _mapDF.get (new org.drip.analytics.date.JulianDate (dblDate));

		if (null == objDF)
			throw new java.lang.Exception ("No DF found for date " + new org.drip.analytics.date.JulianDate
				(dblDate));

		return objDF;
	}

	@Override public org.drip.math.algodiff.WengertJacobian getDFJacobian (
		final double dblDate)
	{
		try {
			if (!org.drip.math.common.NumberUtil.IsValid (dblDate) || null == _mapDF.get (new
				org.drip.analytics.date.JulianDate (dblDate)))
				return null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return _dc.getDFJacobian (dblDate);
	}

	@Override public double getZeroRate (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("ZeroCurve.getZeroRate got NaN for date");

		if (dblDate <= getStartDate().getJulian()) return 1.;

		java.lang.Double objZeroRate = _mapZeroRate.get (new org.drip.analytics.date.JulianDate (dblDate));

		if (null == objZeroRate)
			throw new java.lang.Exception ("No Zero Rate found for date " + new
				org.drip.analytics.date.JulianDate (dblDate));

		return objZeroRate;
	}

	@Override public boolean setNodeValue (
		final int iIndex,
		final double dblValue)
	{
		return _dc.setNodeValue (iIndex, dblValue);
	}

	@Override public boolean bumpNodeValue (
		final int iIndex,
		final double dblValue)
	{
		return _dc.bumpNodeValue (iIndex, dblValue);
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		return _dc.setFlatValue (dblValue);
	}

	@Override public  java.lang.String displayString()
	{
		return _dc.displayString();
	}

	@Override public double[] getCompQuotes()
	{
		return _dc.getCompQuotes();
	}


	@Override public java.lang.String[] getCompMeasures()
	{
		return _dc.getCompMeasures();
	}

	@Override public double getQuote (
		final java.lang.String strInstr)
		throws java.lang.Exception {
		return _dc.getQuote (strInstr);
	}

	@Override public  org.drip.analytics.date.JulianDate getNodeDate (
		final int iIndex)
	{
		return _dc.getNodeDate (iIndex);
	}

	@Override public org.drip.product.definition.CalibratableComponent[] getCalibComponents()
	{
		return _dc.getCalibComponents();
	}

	@Override public java.lang.String getName() {
		return _dc.getName();
	}

	@Override public org.drip.analytics.definition.Curve createParallelShiftedCurve (
		final double dblShift) {
		return _dc.createParallelShiftedCurve (dblShift);
	}

	@Override public org.drip.analytics.definition.Curve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp) {
		return _dc.createTweakedCurve (ntp);
	}

	@Override public org.drip.analytics.date.JulianDate getStartDate() {
		return _dc.getStartDate();
	}

	@Override public void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure, final java.util.Map<org.drip.analytics.date.JulianDate,
			java.util.Map<java.lang.String, java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		 _dc.setInstrCalibInputs (valParam, aCalibInst, adblCalibQuote, astrCalibMeasure, mmFixing,
			quotingParams);
	}

	@Override public org.drip.analytics.definition.DiscountCurve createParallelRateShiftedCurve (
		final double dblShift)
	{
		return _dc.createParallelRateShiftedCurve (dblShift);
	}

	@Override public org.drip.analytics.definition.DiscountCurve createBasisRateShiftedCurve (
		final double[] adblDate,
		final double[] adblBasis)
	{
		return _dc.createBasisRateShiftedCurve (adblDate, adblBasis);
	}

	@Override public java.lang.String getCurrency()
	{
		return _dc.getCurrency();
	}

	@Override public double getDF (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		return _dc.getDF (dt);
	}

	@Override public double getDF (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		return _dc.getDF (strTenor);
	}

	@Override public double getEffectiveDF (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		return _dc.getEffectiveDF (dblDate1, dblDate2);
	}

	@Override public double getEffectiveDF (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		return _dc.getEffectiveDF (dt1, dt2);
	}

	@Override public double getEffectiveDF (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		return _dc.getEffectiveDF (strTenor1, strTenor2);
	}

	@Override public double calcImpliedRate (
		final double dblDt1,
		final double dblDt2)
		throws java.lang.Exception
	{
		return _dc.calcImpliedRate (dblDt1, dblDt2);
	}

	@Override public double calcImpliedRate (
		final double dblDate)
		throws java.lang.Exception
	{
		return _dc.calcImpliedRate (dblDate);
	}

	@Override public double calcImpliedRate (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		return _dc.calcImpliedRate (strTenor);
	}

	@Override public double calcImpliedRate (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		return _dc.calcImpliedRate (strTenor1, strTenor2);
	}

	@Override public byte[] serialize()
	{
		return _dc.serialize();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab)
	{
		return _dc.deserialize (ab);
	}

	@Override public boolean buildInterpolator()
	{
		return false;
	}
}
