
package org.drip.analytics.definition;

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
 * This class contains the baseline abstract discount curve holder object. It provides the stub functionality
 * 		for accessing the forward rates, the calibration instruments, calibration measures, calibration
 * 		quotes, and parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class DiscountCurve extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {

	/**
	 * Set the calibration inputs
	 * 
	 * @param valParam ValuationParams
	 * @param aCalibInst Array of calibration instruments
	 * @param adblCalibQuote Array of calibration quotes
	 * @param astrCalibMeasure Array of calibration measures
	 * @param mmFixing Fixings map
	 */

	public abstract void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure, final java.util.Map<org.drip.analytics.date.JulianDate,
			java.util.Map<java.lang.String, java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams);

	/**
	 * Create a parallel rate shifted discount curve
	 * 
	 * @param dblShift Parallel shift
	 * 
	 * @return Discount Curve
	 */

	public abstract DiscountCurve createParallelRateShiftedCurve (
		final double dblShift);

	/**
	 * Create a shifted curve from an array of basis shifts
	 * 
	 * @param adblDate Array of dates
	 * @param adblBasis Array of basis
	 * 
	 * @return Discount Curve
	 */

	public abstract DiscountCurve createBasisRateShiftedCurve (
		final double[] adblDate,
		final double[] adblBasis);

	/**
	 * Get the currency
	 * 
	 * @return Currency
	 */

	public abstract java.lang.String getCurrency();

	/**
	 * Calculate the discount factor to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Discount factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double getDF (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Retrieve the Jacobian for the DF to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return The Jacobian
	 */

	public abstract org.drip.math.calculus.WengertJacobian getDFJacobian (
		final double dblDate);

	/**
	 * Compute the time-weighted discount factor between 2 dates
	 * 
	 * @param dblDate1 First Date
	 * @param dblDate2 Second Date
	 * 
	 * @return Discount Factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double getEffectiveDF (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception;

	/**
	 * Compute the time-weighted discount factor between 2 dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Discount Factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double getEffectiveDF (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception;

	/**
	 * Compute the time-weighted discount factor between 2 tenors
	 * 
	 * @param strTenor1 First Date
	 * @param strTenor2 Second Date
	 * 
	 * @return Discount Factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double getEffectiveDF (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception;

	/**
	 * Compute the implied rate between 2 dates
	 * 
	 * @param dblDt1 First Date
	 * @param dblDt2 Second Date
	 * 
	 * @return Implied Rate
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double calcImpliedRate (
		final double dblDt1,
		final double dblDt2)
		throws java.lang.Exception;

	/**
	 * Calculate the implied rate to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Implied rate
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double calcImpliedRate (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Calculate the implied rate to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Implied rate
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public abstract double calcImpliedRate (
		final java.lang.String strTenor)
		throws java.lang.Exception;

	/**
	 * Calculate the implied rate between 2 tenors
	 * 
	 * @param strTenor1 Tenor start
	 * @param strTenor2 Tenor end
	 * 
	 * @return Implied Discount Rate
	 * 
	 * @throws java.lang.Exception
	 */

	public abstract double calcImpliedRate (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception;

	/**
	 * Retrieve the fixings object for calibration using floater instruments
	 * 
	 * @return The fixings object
	 */

	public abstract java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getCalibFixings();

	/**
	 * Calculate the Jacobian of PV at the given date for each component in the calibration set to the DF
	 * 
	 * @param dt Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compPVDFJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		return null == dt ? null : compPVDFJacobian (dt.getJulian());
	}

	/**
	 * Calculate the Jacobian of Component Quote at the given date for each component in the calibration set
	 * 	to the DF
	 * 
	 * @param dblDate Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compQuoteDFJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		org.drip.product.definition.CalibratableComponent[] aCalibComp = getCalibComponents();

		java.lang.String[] astrCalibMeasure = getCompMeasures();

		if (null == aCalibComp || null == astrCalibMeasure || 0 == aCalibComp.length ||
			astrCalibMeasure.length != aCalibComp.length)
			return null;

		int iNumParameters = 0;
		int iNumComponents = aCalibComp.length;
		org.drip.math.calculus.WengertJacobian wjCompQuoteDF = null;

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateSpotValParams (dblDate);

		org.drip.param.definition.ComponentMarketParams mktParams = new
			org.drip.param.market.ComponentMarketParamSet (this, null, null, null, null, null,
				getCalibFixings());

		for (int i = 0; i < iNumComponents; ++i) {
			org.drip.math.calculus.WengertJacobian wjCompQuoteDFMicroJack =
				aCalibComp[i].calcQuoteDFMicroJack (astrCalibMeasure[i], valParams, null, mktParams, null);

			if (null == wjCompQuoteDFMicroJack) return null;

			if (null == wjCompQuoteDF) {
				try {
					wjCompQuoteDF = new org.drip.math.calculus.WengertJacobian (iNumComponents,
						iNumParameters = wjCompQuoteDFMicroJack.numParameters());
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}
			}

			for (int k = 0; k < iNumParameters; ++k) {
				if (!wjCompQuoteDF.accumulatePartialFirstDerivative (i, k,
					wjCompQuoteDFMicroJack.getFirstDerivative (0, k)))
					return null;
			}
		}

		return wjCompQuoteDF;
	}

	/**
	 * Calculate the Jacobian of Component Quote at the given date for each component in the calibration set
	 * 	to the DF
	 * 
	 * @param dt Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compQuoteDFJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		return null == dt ? null : compQuoteDFJacobian (dt.getJulian());
	}

	/**
	 * Calculate the Jacobian of Component Quote at the given date for each component in the calibration set
	 * 	to the Zero Rate
	 * 
	 * @param dblDate Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compQuoteZeroJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		org.drip.math.calculus.WengertJacobian wjCompQuoteDF = compQuoteDFJacobian (dblDate);

		if (null == wjCompQuoteDF) return null;

		org.drip.product.definition.CalibratableComponent[] aCalibComp = getCalibComponents();

		try {
			int iNumComponents = wjCompQuoteDF.numWengerts();

			int iNumParameters = wjCompQuoteDF.numParameters();

			org.drip.math.calculus.WengertJacobian wjCompQuoteZero = new
				org.drip.math.calculus.WengertJacobian (iNumComponents, iNumParameters);

			for (int k = 0; k < iNumParameters; ++k) {
				double dblMaturityK = aCalibComp[k].getMaturityDate().getJulian();

				double dblDDFDZero = -getDF (dblMaturityK) * (dblMaturityK - getStartDate().getJulian()) /
					365.25;

				for (int i = 0; i < iNumComponents; ++i) {
					if (!wjCompQuoteZero.accumulatePartialFirstDerivative (i, k,
						wjCompQuoteDF.getFirstDerivative (i, k) * dblDDFDZero))
						return null;
				}
			}

			return wjCompQuoteZero;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculate the Jacobian of Component Quote at the given date for each component in the calibration set
	 * 	to the Zero Rate
	 * 
	 * @param dt Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compQuoteZeroJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		return null == dt ? null : compQuoteZeroJacobian (dt.getJulian());
	}

	/**
	 * Calculate the Jacobian of Component PV at the given date for each component in the calibration set to
	 * 	the Component Quote
	 * 
	 * @param dblDate Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compPVQuoteJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		org.drip.math.calculus.WengertJacobian wjCompPVDF = compPVDFJacobian (dblDate);

		org.drip.math.calculus.WengertJacobian wjCompQuoteDF = compQuoteDFJacobian (dblDate);

		if (null == wjCompPVDF || null == wjCompQuoteDF) return null;

		int iNumComponents = wjCompPVDF.numWengerts();

		int iNumParameters = wjCompPVDF.numParameters();

		org.drip.math.calculus.WengertJacobian wjCompPVQuote = null;

		try {
			wjCompPVQuote = new org.drip.math.calculus.WengertJacobian (iNumComponents, iNumParameters);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int j = 0; j < iNumComponents; ++j) {
			for (int k = 0; k < iNumParameters; ++k) {
				if (!wjCompPVQuote.accumulatePartialFirstDerivative (j, k, wjCompPVDF.getFirstDerivative (j,
					k) / wjCompQuoteDF.getFirstDerivative (j, k)))
					return null;
			}
		}

		return wjCompPVQuote;
	}

	/**
	 * Calculate the Jacobian of Component PV at the given date for each component in the calibration set to
	 * 	the Component Quote
	 * 
	 * @param dt Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compPVQuoteJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		return null == dt ? null : compPVQuoteJacobian (dt.getJulian());
	}

	/**
	 * Retrieve the Jacobian for the Forward Rate between the given dates
	 * 
	 * @param dblDate1 Date 1
	 * @param dblDate2 Date 2
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getForwardRateJacobian (
		final double dblDate1,
		final double dblDate2)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate1) ||
			!org.drip.math.common.NumberUtil.IsValid (dblDate2) || dblDate1 == dblDate2)
			return null;

		org.drip.math.calculus.WengertJacobian wj1 = getDFJacobian (dblDate1);

		if (null == wj1) return null;

		org.drip.math.calculus.WengertJacobian wj2 = getDFJacobian (dblDate2);

		if (null == wj2) return null;

		int iNumDFNodes = numCalibNodes();

		double dblDF1 = java.lang.Double.NaN;
		double dblDF2 = java.lang.Double.NaN;
		org.drip.math.calculus.WengertJacobian wjForwardRate = null;

		try {
			dblDF1 = getDF (dblDate1);

			dblDF2 = getDF (dblDate2);

			wjForwardRate = new org.drip.math.calculus.WengertJacobian (1, iNumDFNodes);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i < iNumDFNodes; ++i) {
			double dblDForwardDDFi = 365.25 * ((wj1.getFirstDerivative (0, i) / dblDF1) -
				(wj2.getFirstDerivative (0, i) / dblDF2)) / (dblDate2 - dblDate1);

			if (!wjForwardRate.accumulatePartialFirstDerivative (0, i, dblDForwardDDFi)) return null;
		}

		return wjForwardRate;
	}

	/**
	 * Retrieve the Jacobian for the Forward Rate between the given dates
	 * 
	 * @param dt1 Julian Date 1
	 * @param dt2 Julian Date 2
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getForwardRateJacobian (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
	{
		if (null == dt1 || null == dt2) return null;

		return getForwardRateJacobian (dt1.getJulian(), dt2.getJulian());
	}

	/**
	 * Retrieve the Jacobian for the Zero Rate to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getZeroRateJacobian (
		final double dblDate)
	{
		return getForwardRateJacobian (getStartDate().getJulian(), dblDate);
	}

	/**
	 * Retrieve the Jacobian for the Zero Rate to the given date
	 * 
	 * @param dt Julian Date
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getZeroRateJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		return getForwardRateJacobian (getStartDate(), dt);
	}

	/**
	 * Calculate the discount factor to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Discount factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public double getDF (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("DiscountCurve.getDF got null for date");

		return getDF (dt.getJulian());
	}

	/**
	 * Retrieve the Jacobian for the DF for the given date
	 * 
	 * @param dt Date
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getDFJacobian (
		final org.drip.analytics.date.JulianDate dt)
	{
		if (null == dt) return null;

		return getDFJacobian (dt.getJulian());
	}

	/**
	 * Calculate the discount factor to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Discount factor
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public double getDF (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("DiscountCurve.getDF got bad tenor");

		return getDF (getStartDate().addTenor (strTenor));
	}

	/**
	 * Retrieve the Jacobian for the DF for the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian getDFJacobian (
		final java.lang.String strTenor)
	{
		if (null == strTenor || strTenor.isEmpty()) return null;

		try {
			return getDFJacobian (getStartDate().addTenor (strTenor));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculate the Jacobian of PV at the given date for each component in the calibration set to the DF
	 * 
	 * @param dblDate Date for which the Jacobian is needed
	 * 
	 * @return The Jacobian
	 */

	public org.drip.math.calculus.WengertJacobian compPVDFJacobian (
		final double dblDate)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate)) return null;

		org.drip.product.definition.CalibratableComponent[] aCalibComp = getCalibComponents();

		if (null == aCalibComp || 0 == aCalibComp.length) return null;

		int iNumParameters = 0;
		int iNumComponents = aCalibComp.length;
		org.drip.math.calculus.WengertJacobian wjCompPVDF = null;

		org.drip.param.valuation.ValuationParams valParams =
			org.drip.param.valuation.ValuationParams.CreateSpotValParams (dblDate);

		org.drip.param.definition.ComponentMarketParams mktParams = new
			org.drip.param.market.ComponentMarketParamSet (this, null, null, null, null, null,
				getCalibFixings());

		for (int i = 0; i < iNumComponents; ++i) {
			org.drip.math.calculus.WengertJacobian wjCompPVDFMicroJack = aCalibComp[i].calcPVDFMicroJack
				(valParams, null, mktParams, null);

			if (null == wjCompPVDFMicroJack) return null;

			if (null == wjCompPVDF) {
				try {
					wjCompPVDF = new org.drip.math.calculus.WengertJacobian (iNumComponents, iNumParameters =
						wjCompPVDFMicroJack.numParameters());
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}
			}

			for (int k = 0; k < iNumParameters; ++k) {
				if (!wjCompPVDF.accumulatePartialFirstDerivative (i, k,
					wjCompPVDFMicroJack.getFirstDerivative (0, k)))
					return null;
			}
		}

		return wjCompPVDF;
	}

	/**
	 * Compute the LIBOR between 2 dates
	 * 
	 * @param dblDt1 First Date
	 * @param dblDt2 Second Date
	 * 
	 * @return LIBOR
	 * 
	 * @throws java.lang.Exception Thrown if the discount factor cannot be calculated
	 */

	public double calcLIBOR (
		final double dblDt1,
		final double dblDt2)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDt1) || !org.drip.math.common.NumberUtil.IsValid
			(dblDt2))
			throw new java.lang.Exception ("DiscountCurve.calcLIBOR => Invalid input dates");

		double dblStartDate = getStartDate().getJulian();

		if (dblDt1 < dblStartDate || dblDt2 < dblStartDate)
			throw new java.lang.Exception ("DiscountCurve.calcLIBOR => Invalid input dates");

		return 365.25 / (dblDt2 - dblDt1) * java.lang.Math.log (getDF (dblDt1) / getDF (dblDt2));
	}

	/**
	 * Calculate the LIBOR to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return LIBOR
	 * 
	 * @throws java.lang.Exception Thrown if LIBOR cannot be calculated
	 */

	public double calcLIBOR (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("DiscountCurve.calcLIBOR got NaN for date");

		org.drip.analytics.date.JulianDate dtStart = getStartDate().addDays (2);

		java.lang.String strCurrency = getCurrency();

		java.lang.String strIndex = strCurrency + "-LIBOR-6M";

		org.drip.product.definition.RatesComponent irs = org.drip.product.creator.IRSBuilder.CreateIRS
			(dtStart, new org.drip.analytics.date.JulianDate (dblDate), 0., strCurrency, strIndex,
				strCurrency);

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put (strIndex, 0.);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (dtStart, mIndexFixings);

		org.drip.param.market.ComponentMarketParamSet cmp = new org.drip.param.market.ComponentMarketParamSet
			(this, null, null, null, null, null, mmFixings);

		return irs.calcMeasureValue (org.drip.param.valuation.ValuationParams.CreateValParams (dtStart, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), null, cmp, null, "FairPremium");
	}

	/**
	 * Calculate the LIBOR DV01 to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return LIBOR DV01
	 * 
	 * @throws java.lang.Exception Thrown if LIBOR cannot be calculated
	 */

	public double calcLIBORDV01 (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("DiscountCurve.calcLIBORDV01 got NaN for date");

		org.drip.analytics.date.JulianDate dtStart = getStartDate().addDays (2);

		java.lang.String strCurrency = getCurrency();

		java.lang.String strIndex = strCurrency + "-LIBOR-6M";

		org.drip.product.definition.RatesComponent irs = org.drip.product.creator.IRSBuilder.CreateIRS
			(dtStart, new org.drip.analytics.date.JulianDate (dblDate), 0., strCurrency, strIndex,
				strCurrency);

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put (strIndex, 0.);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (dtStart, mIndexFixings);

		org.drip.param.market.ComponentMarketParamSet cmp = new org.drip.param.market.ComponentMarketParamSet
			(this, null, null, null, null, null, mmFixings);

		return irs.calcMeasureValue (org.drip.param.valuation.ValuationParams.CreateValParams (dtStart, 0,
			"", org.drip.analytics.daycount.Convention.DR_ACTUAL), null, cmp, null, "DV01");
	}

	/**
	 * Calculate the LIBOR to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return LIBOR
	 * 
	 * @throws java.lang.Exception Thrown if LIBOR cannot be calculated
	 */

	public double calcLIBOR (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("DiscountCurve.calcLIBOR got empty date");

		org.drip.analytics.date.JulianDate dtStart = getStartDate();

		return calcLIBOR (dtStart.getJulian(), dtStart.addTenor (strTenor).getJulian());
	}

	/**
	 * Calculate LIBOR between 2 tenors
	 * 
	 * @param strTenor1 Tenor start
	 * @param strTenor2 Tenor end
	 * 
	 * @return LIBOR
	 * 
	 * @throws java.lang.Exception
	 */

	public double calcLIBOR (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("DiscountCurve.calcLIBOR got empty date");

		org.drip.analytics.date.JulianDate dtStart = getStartDate();

		return calcLIBOR (dtStart.addTenor (strTenor1).getJulian(), dtStart.addTenor
			(strTenor2).getJulian());
	}

	/**
	 * Estimates the Interpolated calibrated measure value for the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return The Interpolated calibrated measure value
	 * 
	 * @throws java.lang.Exception Thrown if the Interpolated calibrated measure value cannot be computed
	 */

	public double interpMeasure (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("DiscountCurve.interpMeasure => Invalid input");

		org.drip.product.definition.CalibratableComponent[] aCalibComp = getCalibComponents();

		if (null == aCalibComp)
			throw new java.lang.Exception ("DiscountCurve.interpMeasure => Calib Components not available");

		int iNumComponent = aCalibComp.length;
		double[] adblDate = new double[iNumComponent];

		if (0 == iNumComponent)
			throw new java.lang.Exception ("DiscountCurve.interpMeasure => Calib Components not available");

		double[] adblQuote = getCompQuotes();

		if (iNumComponent != adblQuote.length)
			throw new java.lang.Exception ("DiscountCurve.interpMeasure => Calib Quotes not available");

		for (int i = 0; i < iNumComponent; ++i) {
			if (null == aCalibComp[i])
				throw new java.lang.Exception ("DiscountCurve.interpMeasure => Cannot locate a component");

			adblDate[i] = aCalibComp[i].getMaturityDate().getJulian();
		}

		org.drip.math.grid.Span span = org.drip.math.grid.Span.CreateSpanInterpolator (adblDate, adblQuote,
			org.drip.math.grid.Span.BASIS_SPLINE_POLYNOMIAL,
				org.drip.math.grid.Span.SPLINE_BOUNDARY_MODE_NATURAL, 1., 2, 0,
					org.drip.math.grid.Span.SET_ITEP | org.drip.math.grid.Span.CALIBRATE_SPAN);

		if (null == span)
			throw new java.lang.Exception ("DiscountCurve.interpMeasure => Cannot create Interp Span");

		return span.calcValue (dblDate);
	}
}
