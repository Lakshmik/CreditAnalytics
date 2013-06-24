
package org.drip.service.sample;

/*
 * Generic imports
 */

import java.util.*;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.*;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;

/*
 * Credit Analytics API Import
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.service.api.CreditAnalytics;

/*
 * DRIP Math Support
 */

import org.drip.math.calculus.WengertJacobian;
import org.drip.math.common.FormatUtil;

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
 * Demo of the Rates Analytics API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RatesAnalyticsAPI {
	/**
	 * Sample API demonstrating the creation/usage of discount curve
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void DiscountCurveAPISample()
		throws Exception
	{
		JulianDate dtStart = JulianDate.Today();

		double[] adblDF = new double[5];
		double[] adblDate = new double[5];
		double[] adblRate = new double[5];

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).getJulian();

			adblDF[i] = 1. - 2 * (i + 1) * 0.05;
			adblRate[i] = 0.05;
		}

		/*
		 * Build the discount curve from an array of dates and discount factors
		 */

		DiscountCurve dcFromDF = DiscountCurveBuilder.BuildFromDF (dtStart, "EUR", adblDate, adblDF,
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		JulianDate dt = dtStart.addYears (10);

		System.out.println ("DCFromDF[" + dt.toString() + "]; DF=" + dcFromDF.getDF (dt) + "; Rate=" +
			dcFromDF.calcImpliedRate ("10Y"));

		/*
		 * Build the discount curve from an array of dates and forward rates
		 */

		DiscountCurve dcFromRate = DiscountCurveBuilder.CreateDC (dtStart, "EUR", adblDate, adblRate,
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		System.out.println ("DCFromRate[" + dt.toString() + "]; DF=" + dcFromRate.getDF (dt) + "; Rate=" +
			dcFromRate.calcImpliedRate ("10Y"));

		/*
		 * Build the discount curve from a flat rate
		 */

		DiscountCurve dcFromFlatRate = DiscountCurveBuilder.CreateFromFlatRate (dtStart, "DKK", 0.04);

		System.out.println ("DCFromFlatRate[" + dt.toString() + "]; DF=" + dcFromFlatRate.getDF (dt) +
			"; Rate=" + dcFromFlatRate.calcImpliedRate ("10Y"));
	}

	/**
	 * Sample API demonstrating the creation of the discount curve from the rates input instruments
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static void DiscountCurveFromRatesInstruments()
		throws Exception
	{
		int NUM_DC_INSTR = 30;
		double adblDate[] = new double[NUM_DC_INSTR];
		double adblRate[] = new double[NUM_DC_INSTR];
		String astrCalibMeasure[] = new String[NUM_DC_INSTR];
		double adblCompCalibValue[] = new double[NUM_DC_INSTR];
		CalibratableComponent aCompCalib[] = new CalibratableComponent[NUM_DC_INSTR];

		JulianDate dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6);

		// First 7 instruments - cash calibration

		adblDate[0] = dtStart.addDays (3).getJulian(); // ON

		adblDate[1] = dtStart.addDays (4).getJulian(); // 1D (TN)

		adblDate[2] = dtStart.addDays (9).getJulian(); // 1W

		adblDate[3] = dtStart.addDays (16).getJulian(); // 2W

		adblDate[4] = dtStart.addDays (32).getJulian(); // 1M

		adblDate[5] = dtStart.addDays (62).getJulian(); // 2M

		adblDate[6] = dtStart.addDays (92).getJulian(); // 3M

		/*
		 * Cash Rate Quotes
		 */

		adblCompCalibValue[0] = .0013;
		adblCompCalibValue[1] = .0017;
		adblCompCalibValue[2] = .0017;
		adblCompCalibValue[3] = .0018;
		adblCompCalibValue[4] = .0020;
		adblCompCalibValue[5] = .0023;
		adblCompCalibValue[6] = .0026;

		for (int i = 0; i < 7; ++i) {
			adblRate[i] = 0.01;
			astrCalibMeasure[i] = "Rate";

			aCompCalib[i] = CashBuilder.CreateCash (dtStart.addDays (2), new JulianDate (adblDate[i]), "USD");
		}

		// Next 8 instruments - EDF calibration

		org.drip.analytics.date.JulianDate dtEDFStart = dtStart;
		adblCompCalibValue[7] = .0027;
		adblCompCalibValue[8] = .0032;
		adblCompCalibValue[9] = .0041;
		adblCompCalibValue[10] = .0054;
		adblCompCalibValue[11] = .0077;
		adblCompCalibValue[12] = .0104;
		adblCompCalibValue[13] = .0134;
		adblCompCalibValue[14] = .0160;

		CalibratableComponent[] aEDF = EDFutureBuilder.GenerateEDPack (dtStart, 8, "USD");

		for (int i = 0; i < 8; ++i) {
			aCompCalib[i + 7] = aEDF[i];
			astrCalibMeasure[i + 7] = "Rate";
			adblRate[i + 7] = 0.01;

			adblDate[i + 7] = dtEDFStart.addDays ((i + 1) * 91).getJulian();
		}

		// Final 15 instruments - IRS calibration

		adblDate[15] = dtStart.addDays ((int)(365.25 * 4 + 2)).getJulian(); // 4Y

		adblDate[16] = dtStart.addDays ((int)(365.25 * 5 + 2)).getJulian(); // 5Y

		adblDate[17] = dtStart.addDays ((int)(365.25 * 6 + 2)).getJulian(); // 6Y

		adblDate[18] = dtStart.addDays ((int)(365.25 * 7 + 2)).getJulian(); // 7Y

		adblDate[19] = dtStart.addDays ((int)(365.25 * 8 + 2)).getJulian(); // 8Y

		adblDate[20] = dtStart.addDays ((int)(365.25 * 9 + 2)).getJulian(); // 9Y

		adblDate[21] = dtStart.addDays ((int)(365.25 * 10 + 2)).getJulian(); // 10Y

		adblDate[22] = dtStart.addDays ((int)(365.25 * 11 + 2)).getJulian(); // 11Y

		adblDate[23] = dtStart.addDays ((int)(365.25 * 12 + 2)).getJulian(); // 12Y

		adblDate[24] = dtStart.addDays ((int)(365.25 * 15 + 2)).getJulian(); // 15Y

		adblDate[25] = dtStart.addDays ((int)(365.25 * 20 + 2)).getJulian(); // 20Y

		adblDate[26] = dtStart.addDays ((int)(365.25 * 25 + 2)).getJulian(); // 25Y

		adblDate[27] = dtStart.addDays ((int)(365.25 * 30 + 2)).getJulian(); // 30Y

		adblDate[28] = dtStart.addDays ((int)(365.25 * 40 + 2)).getJulian(); // 40Y

		adblDate[29] = dtStart.addDays ((int)(365.25 * 50 + 2)).getJulian(); // 50Y

		adblCompCalibValue[15] = .0166;
		adblCompCalibValue[16] = .0206;
		adblCompCalibValue[17] = .0241;
		adblCompCalibValue[18] = .0269;
		adblCompCalibValue[19] = .0292;
		adblCompCalibValue[20] = .0311;
		adblCompCalibValue[21] = .0326;
		adblCompCalibValue[22] = .0340;
		adblCompCalibValue[23] = .0351;
		adblCompCalibValue[24] = .0375;
		adblCompCalibValue[25] = .0393;
		adblCompCalibValue[26] = .0402;
		adblCompCalibValue[27] = .0407;
		adblCompCalibValue[28] = .0409;
		adblCompCalibValue[29] = .0409;

		for (int i = 0; i < 15; ++i) {
			astrCalibMeasure[i + 15] = "Rate";
			adblRate[i + 15] = 0.01;

			aCompCalib[i + 15] = IRSBuilder.CreateIRS (dtStart.addDays (2), new JulianDate (adblDate[i + 15]),
				0., "USD", "USD-LIBOR-6M", "USD");
		}

		/*
		 * Create the sample (in this case dummy) IRS index rate fixings object
		 */

		Map<String, Double> mIndexFixings = new HashMap<String, Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0042);

		Map<JulianDate, Map<String, Double>> mmFixings = new HashMap<JulianDate, Map<String, Double>>();

		mmFixings.put (dtStart.addDays (2), mIndexFixings);

		/*
		 * Build the IR curve from the components, their calibration measures, and their calibration quotes.
		 */

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
			aCompCalib, adblCompCalibValue, astrCalibMeasure, mmFixings);

		/*
		 * Re-calculate the component input measure quotes from the calibrated discount curve object
		 */

		for (int i = 0; i < aCompCalib.length; ++i)
			System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
				FormatUtil.FormatDouble (aCompCalib[i].calcMeasureValue (new ValuationParams (dtStart, dtStart, "USD"), null,
					ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, null, null, null, mmFixings),
						null, astrCalibMeasure[i]), 1, 5, 1.) + " | " + FormatUtil.FormatDouble (adblCompCalibValue[i], 1, 5, 1.));

		/* for (int i = 0; i < 100; ++i) {
			org.drip.analytics.date.JulianDate dt = dtStart.addDays (90 * i);

			System.out.println ("DF[" + dt + "] = " + dc.getDF (dt));
		} */

		for (int i = 0; i < aCompCalib.length; ++i) {
			WengertJacobian wjComp = aCompCalib[i].calcPVDFMicroJack
				(new ValuationParams (dtStart, dtStart, "USD"),
				null,
				ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, null, null, null, mmFixings),
				null);

			System.out.println ("PV/DF Micro Jack[" + aCompCalib[i].getComponentName() + "]=" +
				(null == wjComp ? null : wjComp.displayString()));
		}
	}

	public static void DiscountCurveFromCash()
		throws Exception
	{
		int NUM_CASH_INSTR = 7;
		double adblDate[] = new double[NUM_CASH_INSTR];
		double adblRate[] = new double[NUM_CASH_INSTR];
		String astrCalibMeasure[] = new String[NUM_CASH_INSTR];
		double adblCompCalibValue[] = new double[NUM_CASH_INSTR];
		CalibratableComponent aCompCalib[] = new CalibratableComponent[NUM_CASH_INSTR];

		JulianDate dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6);

		// First 7 instruments - cash calibration

		adblDate[0] = dtStart.addDays (3).getJulian(); // ON

		adblDate[1] = dtStart.addDays (4).getJulian(); // 1D (TN)

		adblDate[2] = dtStart.addDays (9).getJulian(); // 1W

		adblDate[3] = dtStart.addDays (16).getJulian(); // 2W

		adblDate[4] = dtStart.addDays (32).getJulian(); // 1M

		adblDate[5] = dtStart.addDays (62).getJulian(); // 2M

		adblDate[6] = dtStart.addDays (92).getJulian(); // 3M

		/*
		 * Cash Rate Quotes
		 */

		adblCompCalibValue[0] = .0013;
		adblCompCalibValue[1] = .0017;
		adblCompCalibValue[2] = .0017;
		adblCompCalibValue[3] = .0018;
		adblCompCalibValue[4] = .0020;
		adblCompCalibValue[5] = .0023;
		adblCompCalibValue[6] = .0026;

		for (int i = 0; i < NUM_CASH_INSTR; ++i) {
			adblRate[i] = 0.01;
			astrCalibMeasure[i] = "Rate";

			aCompCalib[i] = CashBuilder.CreateCash (dtStart.addDays (2), new JulianDate (adblDate[i]), "USD");
		}

		/*
		 * Build the IR curve from the components, their calibration measures, and their calibration quotes.
		 */

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
			DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
			aCompCalib, adblCompCalibValue, astrCalibMeasure, null);

		/*
		 * Re-calculate the component input measure quotes from the calibrated discount curve object
		 */

		for (int i = 0; i < aCompCalib.length; ++i)
			System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
				FormatUtil.FormatDouble (aCompCalib[i].calcMeasureValue (new ValuationParams (dtStart, dtStart, "USD"), null,
					ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, null, null, null, null),
						null, astrCalibMeasure[i]), 1, 5, 1.) + " | " + FormatUtil.FormatDouble (adblCompCalibValue[i], 1, 5, 1.));

		org.drip.math.calculus.WengertJacobian wjPVDF = dc.compPVDFJacobian (dtStart);

		System.out.println ("PV/DF Micro Jack[04/06/11]=" + (null == wjPVDF ? null : wjPVDF.displayString()));
	}

	public static void DiscountCurveFromEDF()
		throws Exception
	{
		int NUM_DC_INSTR = 8;
		double adblDate[] = new double[NUM_DC_INSTR];
		double adblRate[] = new double[NUM_DC_INSTR];
		String astrCalibMeasure[] = new String[NUM_DC_INSTR];
		double adblCompCalibValue[] = new double[NUM_DC_INSTR];
		CalibratableComponent aCompCalib[] = new CalibratableComponent[NUM_DC_INSTR];

		JulianDate dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6);

		// Next 8 instruments - EDF calibration

		org.drip.analytics.date.JulianDate dtEDFStart = dtStart;
		adblCompCalibValue[0] = .0027;
		adblCompCalibValue[1] = .0032;
		adblCompCalibValue[2] = .0041;
		adblCompCalibValue[3] = .0054;
		adblCompCalibValue[4] = .0077;
		adblCompCalibValue[5] = .0104;
		adblCompCalibValue[6] = .0134;
		adblCompCalibValue[7] = .0160;

		CalibratableComponent[] aEDF = EDFutureBuilder.GenerateEDPack (dtStart, 8, "USD");

		for (int i = 0; i < NUM_DC_INSTR; ++i) {
			adblRate[i] = 0.01;
			aCompCalib[i] = aEDF[i];
			astrCalibMeasure[i] = "Rate";

			adblDate[i] = dtEDFStart.addDays ((i + 1) * 91).getJulian();
		}

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
			DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
			aCompCalib, adblCompCalibValue, astrCalibMeasure, null);

		/*
		 * Re-calculate the component input measure quotes from the calibrated discount curve object
		 */

		for (int i = 0; i < aCompCalib.length; ++i)
			System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
				FormatUtil.FormatDouble (aCompCalib[i].calcMeasureValue (new ValuationParams (dtStart, dtStart, "USD"), null,
					ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, null, null, null, null),
						null, astrCalibMeasure[i]), 1, 5, 1.) + " | " + FormatUtil.FormatDouble (adblCompCalibValue[i], 1, 5, 1.));

		org.drip.math.calculus.WengertJacobian wjPVDF = dc.compPVDFJacobian (dtStart);

		System.out.println ("PV/DF Micro Jack[04/06/11]=" + (null == wjPVDF ? null : wjPVDF.displayString()));
	}

	public static void DiscountCurveFromIRS()
		throws Exception
	{
		int NUM_DC_INSTR = 15;
		double adblDate[] = new double[NUM_DC_INSTR];
		double adblRate[] = new double[NUM_DC_INSTR];
		String astrCalibMeasure[] = new String[NUM_DC_INSTR];
		double adblCompCalibValue[] = new double[NUM_DC_INSTR];
		CalibratableComponent aCompCalib[] = new CalibratableComponent[NUM_DC_INSTR];

		JulianDate dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6);

		// Final 15 instruments - IRS calibration

		adblDate[0] = dtStart.addDays ((int)(365.25 * 1 + 2)).getJulian(); // 4Y

		adblDate[1] = dtStart.addDays ((int)(365.25 * 2 + 2)).getJulian(); // 5Y

		adblDate[2] = dtStart.addDays ((int)(365.25 * 3 + 2)).getJulian(); // 6Y

		adblDate[3] = dtStart.addDays ((int)(365.25 * 7 + 2)).getJulian(); // 7Y

		adblDate[4] = dtStart.addDays ((int)(365.25 * 8 + 2)).getJulian(); // 8Y

		adblDate[5] = dtStart.addDays ((int)(365.25 * 9 + 2)).getJulian(); // 9Y

		adblDate[6] = dtStart.addDays ((int)(365.25 * 10 + 2)).getJulian(); // 10Y

		adblDate[7] = dtStart.addDays ((int)(365.25 * 11 + 2)).getJulian(); // 11Y

		adblDate[8] = dtStart.addDays ((int)(365.25 * 12 + 2)).getJulian(); // 12Y

		adblDate[9] = dtStart.addDays ((int)(365.25 * 15 + 2)).getJulian(); // 15Y

		adblDate[10] = dtStart.addDays ((int)(365.25 * 20 + 2)).getJulian(); // 20Y

		adblDate[11] = dtStart.addDays ((int)(365.25 * 25 + 2)).getJulian(); // 25Y

		adblDate[12] = dtStart.addDays ((int)(365.25 * 30 + 2)).getJulian(); // 30Y

		adblDate[13] = dtStart.addDays ((int)(365.25 * 40 + 2)).getJulian(); // 40Y

		adblDate[14] = dtStart.addDays ((int)(365.25 * 50 + 2)).getJulian(); // 50Y

		adblCompCalibValue[0] = .0166;
		adblCompCalibValue[1] = .0206;
		adblCompCalibValue[2] = .0241;
		adblCompCalibValue[3] = .0269;
		adblCompCalibValue[4] = .0292;
		adblCompCalibValue[5] = .0311;
		adblCompCalibValue[6] = .0326;
		adblCompCalibValue[7] = .0340;
		adblCompCalibValue[8] = .0351;
		adblCompCalibValue[9] = .0375;
		adblCompCalibValue[10] = .0393;
		adblCompCalibValue[11] = .0402;
		adblCompCalibValue[12] = .0407;
		adblCompCalibValue[13] = .0409;
		adblCompCalibValue[14] = .0409;

		for (int i = 0; i < NUM_DC_INSTR; ++i) {
			adblRate[i] = 0.01;
			astrCalibMeasure[i] = "Rate";

			aCompCalib[i] = IRSBuilder.CreateIRS (dtStart.addDays (2), new JulianDate (adblDate[i]), 0.,
				"USD", "USD-LIBOR-6M", "USD");
		}

		/*
		 * Create the sample (in this case dummy) IRS index rate fixings object
		 */

		Map<String, Double> mIndexFixings = new HashMap<String, Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0042);

		Map<JulianDate, Map<String, Double>> mmFixings = new HashMap<JulianDate, Map<String, Double>>();

		mmFixings.put (dtStart.addDays (2), mIndexFixings);

		/*
		 * Build the IR curve from the components, their calibration measures, and their calibration quotes.
		 */

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
			aCompCalib, adblCompCalibValue, astrCalibMeasure, mmFixings);

		/*
		 * Re-calculate the component input measure quotes from the calibrated discount curve object
		 */

		for (int i = 0; i < aCompCalib.length; ++i)
			System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
				FormatUtil.FormatDouble (aCompCalib[i].calcMeasureValue (new ValuationParams (dtStart, dtStart, "USD"), null,
					ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, null, null, null, mmFixings),
						null, astrCalibMeasure[i]), 1, 5, 1.) + " | " + FormatUtil.FormatDouble (adblCompCalibValue[i], 1, 5, 1.));

		org.drip.math.calculus.WengertJacobian wjQuoteDF = dc.compQuoteDFJacobian (dtStart);

		System.out.println ("Quote/DF Micro Jack[04/06/11]=" + (null == wjQuoteDF ? null :
			wjQuoteDF.displayString()));

		org.drip.math.calculus.WengertJacobian wjPVDF = dc.compPVDFJacobian (dtStart);

		System.out.println ("PV/Zero Micro Jack[04/06/11]=" + (null == wjPVDF ? null : wjPVDF.displayString()));
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		DiscountCurveFromRatesInstruments();

		// DiscountCurveFromIRS();

		// DiscountCurveAPISample();
	}
}
