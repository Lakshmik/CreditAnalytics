
package org.drip.service.sample;

/*
 * General imports
 */

import java.util.*;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.definition.*;
import org.drip.analytics.period.Period;
import org.drip.math.common.FormatUtil;
import org.drip.param.definition.*;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;
import org.drip.product.params.EmbeddedOptionSchedule;

/*
 * Credit Analytics API imports
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.product.credit.BondComponent;
import org.drip.service.api.CreditAnalytics;

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
 * Simple Bond Sample demonstrating the construction and usage of bond functionality
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BloombergYAS {
	private static final String FIELD_SEPARATOR = "    ";

	private static DiscountCurve BuildRatesCurveFromInstruments (
		final JulianDate dtStart,
		final String[] astrCashTenor,
		final double[] adblCashRate,
		final String[] astrIRSTenor,
		final double[] adblIRSRate,
		final String strCurrency)
		throws Exception
	{
		int iNumDCInstruments = astrCashTenor.length + adblIRSRate.length;
		double adblDate[] = new double[iNumDCInstruments];
		double adblRate[] = new double[iNumDCInstruments];
		String astrCalibMeasure[] = new String[iNumDCInstruments];
		double adblCompCalibValue[] = new double[iNumDCInstruments];
		CalibratableComponent aCompCalib[] = new CalibratableComponent[iNumDCInstruments];
		String strIndex = strCurrency + "-LIBOR-6M";

		// Cash Calibration

		for (int i = 0; i < astrCashTenor.length; ++i) {
			astrCalibMeasure[i] = "Rate";
			adblRate[i] = java.lang.Double.NaN;
			adblCompCalibValue[i] = adblCashRate[i];

			aCompCalib[i] = CashBuilder.CreateCash (dtStart.addDays (2), new JulianDate (adblDate[i] =
				dtStart.addTenor (astrCashTenor[i]).getJulian()), strCurrency);
		}

		// IRS Calibration

		for (int i = 0; i < astrIRSTenor.length; ++i) {
			astrCalibMeasure[i + astrCashTenor.length] = "Rate";
			adblRate[i + astrCashTenor.length] = java.lang.Double.NaN;
			adblCompCalibValue[i + astrCashTenor.length] = adblIRSRate[i];

			aCompCalib[i + astrCashTenor.length] = IRSBuilder.CreateIRS (dtStart.addDays (2), new
				JulianDate (adblDate[i + astrCashTenor.length] = dtStart.addTenor
					(astrIRSTenor[i]).getJulian()), 0., strCurrency, strIndex, strCurrency);
		}

		/*
		 * Create the sample (in this case dummy) IRS index rate fixings object
		 */

		Map<String, Double> mIndexFixings = new HashMap<String, Double>();

		mIndexFixings.put (strIndex, 0.0042);

		Map<JulianDate, Map<String, Double>> mmFixings = new HashMap<JulianDate, Map<String, Double>>();

		mmFixings.put (dtStart.addDays (2), mIndexFixings);

		/*
		 * Build the IR curve from the components, their calibration measures, and their calibration quotes.
		 */

		return RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, strCurrency,
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD, aCompCalib, adblCompCalibValue, astrCalibMeasure, mmFixings);
	}

	public static final void BondPricerSample()
		throws Exception
	{
		JulianDate dtCurve = JulianDate.CreateFromYMD (2013, 6, 20);

		JulianDate dtSettle = JulianDate.CreateFromYMD (2013, 6, 24);

		double dblNotional = 1000000.;
		String[] astrCashTenor = new String[] {};
		double[] adblCashRate = new double[] {};
		String[] astrIRSTenor = new String[] {   "1Y",    "2Y",    "3Y",    "4Y",    "5Y",    "6Y",    "7Y",
			   "8Y",    "9Y",   "10Y",   "11Y",   "12Y",   "15Y",   "20Y",   "25Y",   "30Y",   "40Y",   "50Y"};
		double[] adblIRSRate = new double[]  {0.00317, 0.00422, 0.00645, 0.00953, 0.01274, 0.01563, 0.01819,
			0.02037, 0.02223, 0.02386, 0.02526, 0.02643, 0.02890, 0.03091, 0.03184, 0.03236, 0.03259, 0.03238};

		DiscountCurve dc = BuildRatesCurveFromInstruments (dtCurve, astrCashTenor, adblCashRate, astrIRSTenor,
			adblIRSRate, "USD");

		BondComponent bond = BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				"TEST",		// Name
				"USD",			// Currency
				0.06125,			// Bond Coupon
				2, 				// Frequency
				"30/360",		// Day Count
				JulianDate.CreateFromYMD (2013, 5, 24), // Effective
				JulianDate.CreateFromYMD (2018, 6, 1),	// Maturity
				null,		// Principal Schedule
				null);

		/* double[] adblDate = new double[] {
			JulianDate.CreateFromYMD (2016, 3, 1).getJulian(),
			JulianDate.CreateFromYMD (2017, 3, 1).getJulian(),
			JulianDate.CreateFromYMD (2018, 3, 1).getJulian(),
			JulianDate.CreateFromYMD (2019, 3, 1).getJulian(),
			JulianDate.CreateFromYMD (2020, 3, 1).getJulian()
		};

		double[] adblFactor = new double[] {1.045, 1.03, 1.015, 1., 1.};

		EmbeddedOptionSchedule eos = new EmbeddedOptionSchedule (adblDate, adblFactor, false, 30, false, Double.NaN, "", Double.NaN);

		bond.setEmbeddedCallSchedule (eos); */

		ComponentMarketParams cmp = ComponentMarketParamsBuilder.MakeDiscountCMP (dc);

		ValuationParams valParams = ValuationParams.CreateValParams (dtSettle, 0, "", Convention.DR_ACTUAL);

		double dblPrice = 0.988;

		double dblAccrued = bond.calcAccrued (valParams._dblValue, cmp);

		WorkoutInfo wi = bond.calcExerciseYieldFromPrice (valParams, cmp, null, dblPrice);

		double dblISpread = bond.calcISpreadFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblZSpread = bond.calcZSpreadFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblASW = bond.calcASWFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblModDur = bond.calcModifiedDurationFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblMacDur = bond.calcMacaulayDurationFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblYield01 = bond.calcYield01FromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		double dblConvexity = bond.calcConvexityFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, dblPrice);

		System.out.println ("Price          : " + org.drip.math.common.FormatUtil.FormatDouble (dblPrice, 1, 3, 100.));

		System.out.println ("Yield          : " + org.drip.math.common.FormatUtil.FormatDouble (wi._dblYield, 1, 3, 100.));

		System.out.println ("Workout Date   : " + new org.drip.analytics.date.JulianDate (wi._dblDate));

		System.out.println ("Workout Factor : " + org.drip.math.common.FormatUtil.FormatDouble (wi._dblExerciseFactor, 1, 2, 100.));

		System.out.println ("\n--SPREAD AND YIELD CALCULATIONS--\n");

		System.out.println ("I Spread : " + org.drip.math.common.FormatUtil.FormatDouble (dblISpread, 1, 0, 10000.));

		System.out.println ("Z Spread : " + org.drip.math.common.FormatUtil.FormatDouble (dblZSpread, 1, 0, 10000.));

		System.out.println ("ASW      : " + org.drip.math.common.FormatUtil.FormatDouble (dblASW, 1, 0, 10000.));

		System.out.println ("\n--RISK--\n");

		System.out.println ("Modified Duration : " + org.drip.math.common.FormatUtil.FormatDouble (dblModDur, 1, 2, 10000.));

		System.out.println ("Macaulay Duration : " + org.drip.math.common.FormatUtil.FormatDouble (dblMacDur, 1, 2, 1.));

		System.out.println ("Risk              : " + org.drip.math.common.FormatUtil.FormatDouble (dblYield01 * 10000., 1, 2, 1.));

		System.out.println ("Convexity         : " + org.drip.math.common.FormatUtil.FormatDouble (dblConvexity, 1, 2, 1000000.));

		System.out.println ("DV01              : " + org.drip.math.common.FormatUtil.FormatDouble (dblYield01 * dblNotional, 1, 0, 1.));

		System.out.println ("\n--INVOICE--\n");

		System.out.println ("Face      : " + org.drip.math.common.FormatUtil.FormatDouble (dblNotional, 1, 0, 1.));

		System.out.println ("Principal : " + org.drip.math.common.FormatUtil.FormatDouble (dblPrice * dblNotional, 1, 2, 1.));

		System.out.println ("Accrued   : " + org.drip.math.common.FormatUtil.FormatDouble (dblAccrued * dblNotional, 1, 2, 1.));

		System.out.println ("Total     : " + org.drip.math.common.FormatUtil.FormatDouble ((dblPrice + dblAccrued) * dblNotional, 1, 2, 1.));

		System.out.println ("\nCashflow\n--------");

		for (Period p : bond.getCouponPeriod())
			System.out.println (
				JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
				FormatUtil.FormatDouble (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
				FormatUtil.FormatDouble (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR
			);
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		BondPricerSample();
	}
}
