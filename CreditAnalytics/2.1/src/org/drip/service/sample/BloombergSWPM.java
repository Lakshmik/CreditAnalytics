
package org.drip.service.sample;

import java.util.HashMap;
import java.util.Map;

import org.drip.analytics.creator.DiscountCurveBuilder;
import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.definition.DiscountCurve;
import org.drip.analytics.period.Period;
import org.drip.math.common.FormatUtil;
import org.drip.param.creator.ComponentMarketParamsBuilder;
import org.drip.param.creator.RatesScenarioCurveBuilder;
import org.drip.param.definition.ComponentMarketParams;
import org.drip.param.valuation.ValuationParams;
import org.drip.product.creator.CashBuilder;
import org.drip.product.creator.EDFutureBuilder;
import org.drip.product.creator.IRSBuilder;
import org.drip.product.definition.CalibratableComponent;
import org.drip.product.definition.RatesComponent;
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
	private static final String FIELD_SEPARATOR = "    ";

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
				JulianDate (adblDate[i + astrCashTenor.length] = dtStart.addDays (2).addTenor
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

			System.out.println (aCompCalib[i].getPrimaryCode() + " | " + FormatUtil.FormatDouble (dblZero, 1, 6, 100.)
				+ " | " + FormatUtil.FormatDouble (dblZeroDF, 1, 6, 1.));
		}

		return dc;
	}

	public static void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		JulianDate dtStart = JulianDate.CreateFromYMD (2013, JulianDate.JUNE, 19);

		/*
		 * This part is best modeled by Curve #47 in the SWPM "Curves" tab
		 */

		String[] astrCashTenor = new String[] {"3M"};
		double[] adblCashRate = new double[] {0.0027175};
		double[] adblEDFRate = new double[] {0.00277, 0.00309};
		String[] astrIRSTenor = new String[] {   "1Y",      "2Y",      "3Y",      "4Y",      "5Y",      "6Y",      "7Y",
				 "8Y",      "9Y",     "10Y",     "12Y",     "15Y",     "20Y",     "30Y"};
		double[] adblIRSRate = new double[] {0.003177, 0.0045574, 0.0073411, 0.0109921, 0.0145993, 0.0175048, 0.0198985,
			0.0219095, 0.0236188, 0.0250914, 0.0274766, 0.0297526, 0.0315100, 0.0327862};

		DiscountCurve dc = BuildBBGRatesCurve (dtStart, astrCashTenor, adblCashRate, adblEDFRate, astrIRSTenor, adblIRSRate, "USD");

		ComponentMarketParams cmp = ComponentMarketParamsBuilder.MakeDiscountCMP (dc);

		ValuationParams valParams = ValuationParams.CreateValParams (dtStart.addDays (2), 0, "", Convention.DR_ACTUAL);

		RatesComponent swap = IRSBuilder.CreateIRS (
			dtStart.addDays (2),
			dtStart.addDays (2).addTenor ("5Y"),
			0.01478734,
			"USD",
			"USD-LIBOR-6M",
			"USD");

		Map<String, Double> mIndexFixings = new HashMap<String, Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0042);

		Map<JulianDate, Map<String, Double>> mmFixings = new HashMap<JulianDate, Map<String, Double>>();

		mmFixings.put (dtStart.addDays (2), mIndexFixings);

		cmp.setFixings (mmFixings);

		Map<String, Double> mapSwapCalc = swap.value (valParams, null, cmp, null);

		System.out.println ("PV: " + mapSwapCalc.get ("CleanPV"));

		System.out.println ("DV01: " + mapSwapCalc.get ("DirtyDV01"));

		System.out.println ("\nCashflow\n--------");

		for (Period p : swap.getCouponPeriod())
			System.out.println (
				JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
				FormatUtil.FormatDouble (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
				FormatUtil.FormatDouble (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR
			);
	}
}
