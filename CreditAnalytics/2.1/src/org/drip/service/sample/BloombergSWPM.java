
package org.drip.service.sample;

import java.util.HashMap;
import java.util.Map;

import org.drip.analytics.creator.DiscountCurveBuilder;
import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.DiscountCurve;
import org.drip.math.common.FormatUtil;
import org.drip.param.creator.RatesScenarioCurveBuilder;
import org.drip.product.creator.CashBuilder;
import org.drip.product.creator.EDFutureBuilder;
import org.drip.product.creator.IRSBuilder;
import org.drip.product.definition.CalibratableComponent;
import org.drip.service.api.CreditAnalytics;

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
 * Demo of reproduction of the calculations in Bloomberg's SWPM screen
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BloombergSWPM {
	private static DiscountCurve BuildBBGRatesCurve (
		final JulianDate dtStart,
		final String[] astrCashTenor,
		final double[] adblCashRate,
		final double[] adblEDFRate,
		final String[] astrIRSTenor,
		final double[] adblIRSRate,
		final String strCurrency)
		throws Exception
	{
		int iNumDCInstruments = astrCashTenor.length + adblEDFRate.length + adblIRSRate.length;
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

			aCompCalib[i] = CashBuilder.CreateCash (dtStart, new JulianDate (adblDate[i] =
				dtStart.addTenor (astrCashTenor[i]).getJulian()), strCurrency);

			// System.out.println ("CASH = " + aCompCalib[i].getMaturityDate());
		}

		// EDF Calibration

		CalibratableComponent[] aEDF = EDFutureBuilder.GenerateEDPack (dtStart, adblEDFRate.length, "USD");

		for (int i = 0; i < adblEDFRate.length; ++i) {
			aCompCalib[astrCashTenor.length + i] = aEDF[i];
			astrCalibMeasure[astrCashTenor.length + i] = "Rate";
			adblRate[astrCashTenor.length + i] = java.lang.Double.NaN;
			adblCompCalibValue[astrCashTenor.length + i] = adblEDFRate[i];

			// System.out.println ("EDF = " + aCompCalib[astrCashTenor.length + i].getMaturityDate());
		}

		// IRS Calibration

		for (int i = 0; i < astrIRSTenor.length; ++i) {
			astrCalibMeasure[i + adblEDFRate.length + astrCashTenor.length] = "Rate";
			adblRate[i + adblEDFRate.length + astrCashTenor.length] = java.lang.Double.NaN;
			adblCompCalibValue[i + adblEDFRate.length + astrCashTenor.length] = adblIRSRate[i];

			aCompCalib[i + adblEDFRate.length + astrCashTenor.length] = IRSBuilder.CreateIRS (dtStart.addDays (2), new
				JulianDate (adblDate[i + astrCashTenor.length] = dtStart.addTenor
					(astrIRSTenor[i]).getJulian()), 0., strCurrency, strIndex, strCurrency);

			// System.out.println ("IRS = " + aCompCalib[i + adblEDFRate.length + astrCashTenor.length].getMaturityDate());
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

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, strCurrency,
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD, aCompCalib, adblCompCalibValue, astrCalibMeasure, mmFixings);

		/*
		 * Check: Re-calculate the input rates
		 */

		for (int i = 0; i < aCompCalib.length; ++i) {
			double dblYearFract = (aCompCalib[i].getMaturityDate().getJulian() - dc.getStartDate().getJulian()) / 365.25;

			double dblZero = dc.calcImpliedRate (aCompCalib[i].getMaturityDate().getJulian());

			double dblZeroDF = java.lang.Math.exp (-1. * dblZero * dblYearFract);

			System.out.println (aCompCalib[i].getPrimaryCode() + " | " + aCompCalib[i].getMaturityDate() + " | " +
				FormatUtil.FormatDouble (dblZero, 1, 6, 100.) + " | " + FormatUtil.FormatDouble (dblZeroDF, 1, 6, 1.));
		}

		return dc;
	}

	public static void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		JulianDate dtStart = JulianDate.CreateFromYMD (2013, JulianDate.JUNE, 13);

		String[] astrCashTenor = new String[] {"3M"};
		double[] adblCashRate = new double[] {0.0027325};
		/* double[] adblEDFRate = new double[] {0.00275, 0.00280, 0.00290, 0.00305, 0.00324, 0.00339, 0.00353, 0.00411, 0.00484,
			0.00571, 0.00678, 0.00805, 0.00951, 0.01112, 0.01292, 0.01477}; */
		double[] adblEDFRate = new double[] {0.00275, 0.00280};
		/* String[] astrIRSTenor = new String[] {    "4Y",      "5Y",      "6Y",      "7Y",      "8Y",      "9Y",     "10Y",
			    "11Y",     "12Y",     "15Y",     "20Y",     "25Y",     "30Y",     "40Y",     "50Y"};
		double[] adblIRSRate = new double[] {0.0102874, 0.0133407, 0.0161406, 0.0186062, 0.0207300, 0.0225449, 0.0241363,
			0.0255210, 0.0267012, 0.0292385, 0.0313285, 0.0323277, 0.0328821, 0.0330859, 0.0328771}; */
		String[] astrIRSTenor = new String[] {};
		double[] adblIRSRate = new double[] {};

		BuildBBGRatesCurve (dtStart, astrCashTenor, adblCashRate, adblEDFRate, astrIRSTenor, adblIRSRate, "USD");
	}
}
