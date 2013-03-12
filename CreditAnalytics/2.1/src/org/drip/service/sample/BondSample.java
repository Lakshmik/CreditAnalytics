
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
import org.drip.param.definition.*;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;

/*
 * Credit Analytics API imports
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
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

public class BondSample {
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
		JulianDate dtStart = JulianDate.CreateFromYMD (2012, JulianDate.APRIL, 20);

		String[] astrCashTenor = new String[] {"1M", "2M", "3M", "6M", "9M", "12M"};
		double[] adblCashRate = new double[] {0.002398, 0.003471, 0.004657, 0.007304, 0.008853, 0.010472};
		String[] astrIRSTenor = new String[] {"2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "12Y", "15Y", "20Y", "25Y", "30Y"};
		double[] adblIRSRate = new double[] {0.005655, 0.006900, 0.008980, 0.011325, 0.013720, 0.015945,
			0.017805, 0.01937, 0.02075, 0.02300, 0.025260, 0.026975, 0.027805, 0.028295};

		DiscountCurve dc = BuildRatesCurveFromInstruments (dtStart, astrCashTenor, adblCashRate, astrIRSTenor,
			adblIRSRate, "USD");

		Bond bond = BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				"TEST",		// Name
				"USD",			// Currency
				// 1.,			// Bond Coupon
				0.04500,			// Bond Coupon
				2, 				// Frequency
				"30/360",		// Day Count
				JulianDate.CreateFromYMD (1999, 8, 17), // Effective
				JulianDate.CreateFromYMD (2039, 8, 15),	// Maturity
				null,		// Principal Schedule
				null);

		ComponentMarketParams cmp = ComponentMarketParamsBuilder.CreateComponentMarketParams (
			dc,		// Discount curve
			null,	// TSY Discount Curve
			null,	// EDSF Discount Curve (proxied to TSY Discount Curve
			null,	// Credit Curve
			null,	// TSY quotes
			null,	// Bond market quote
			null	// Fixings - you need this for floaters
		);

		ValuationParams valParams = ValuationParams.CreateValParams (JulianDate.Today().addDays (1), 0, "", Convention.DR_ACTUAL);

		/*
		 * Theoretical Clean Price from the discount curve with zero curve spread applied
		 */

		double dblBasePrice = bond.calcPriceFromBumpedDC (valParams, cmp, bond.getMaturityDate().getJulian(), 1., 0.);

		/*
		 * Theoretical Clean Price using the discount curve plus spread applied
		 */

		double dbSpread = 0.0050; // 50 bp Spread

		double dblSpreadedPrice = bond.calcPriceFromBumpedDC (valParams, cmp, bond.getMaturityDate().getJulian(), 1., dbSpread);

		System.out.println ("Unspreaded theoretical clean price: " + 100. * dblBasePrice);

		System.out.println ("Theoretical clean price with " + (10000 * dbSpread) + " bp spread: " + 100. * dblSpreadedPrice);
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
