
package org.drip.service.sample;

/*
 * Generic imports
 */

import java.util.*;

/*
 * Credit Products imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.*;
import org.drip.analytics.period.CouponPeriodCurveFactors;
import org.drip.analytics.support.GenericUtil;
import org.drip.param.definition.*;
import org.drip.param.pricer.PricerParams;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;

/*
 * Credit Analytics API imports
 */

import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.service.api.CreditAnalytics;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * Demo of reproduction of the calculations in Bloomberg's CDSW screen
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BloombergCDSW {
	private static final java.lang.String FIELD_SEPARATOR = "   ";

	private static DiscountCurve BuildBBGRatesCurve (
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

		DiscountCurve dc = RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, strCurrency, aCompCalib,
			adblCompCalibValue, astrCalibMeasure, mmFixings);

		/*
		 * Check: Re-calculate the input rates
		 */

		/* ValuationParams valParams = ValuationParams.CreateStdValParams (dtStart, strCurrency);

		ComponentMarketParams cmp = new ComponentMarketParams (dc, null, null, null, null, null, mmFixings);

		for (int i = 0; i < aCompCalib.length; ++i) {
			double dblRate = aCompCalib[i].calcMeasureValue (valParams, null, cmp, null, "Rate");

			System.out.println ("Rate[" + i + "] = " + FIGen.FormatSpreadSimple (dblRate, 1, 4, 100.));
		} */

		return dc;
	}

	private static CreditCurve CreateCreditCurveFromCDS (
		final JulianDate dtStart,
		final double[] adblQuote,
		final String[] astrTenor,
		final DiscountCurve dc,
		final double dblRecovery,
		final String strCCName)
		throws Exception
	{
		String[] astrCalibMeasure = new String[adblQuote.length];
		CreditDefaultSwap[] aCDS = new CreditDefaultSwap[adblQuote.length];

		for (int i = 0; i < astrTenor.length; ++i) {
			aCDS[i] = CDSBuilder.CreateSNAC (dtStart, astrTenor[i], 0.01, strCCName);

			astrCalibMeasure[i] = "FairPremium";
		}

		/*
		 * Build the credit curve from the CDS instruments and the fair premium
		 */

		CreditCurve cc = CreditScenarioCurveBuilder.CreateCreditCurve (strCCName, dtStart, aCDS, dc,
			adblQuote, astrCalibMeasure, dblRecovery, false);

		/*
		 * Check: Re-calculate the input fair premium
		 */

		ValuationParams valParams = ValuationParams.CreateStdValParams (dtStart, dc.getCurrency());

		ComponentMarketParams cmp = ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc);

		PricerParams pricerParams = PricerParams.MakeStdPricerParams();

		for (int i = 0; i < aCDS.length; ++i) {
			double dblFairPremium = aCDS[i].calcMeasureValue (valParams, pricerParams, cmp, null, "FairPremium");

			System.out.println ("\tFairPremium[" + i + "] = " + GenericUtil.FormatSpreadSimple (dblFairPremium, 3, 3, 1.));
		}

		return cc;
	}

	private static CreditDefaultSwap CreateCDS (
		final JulianDate dtStart,
		final String strTenor,
		final double dblCoupon,
		final String strCCName)
	{
		return CDSBuilder.CreateSNAC (dtStart, strTenor, dblCoupon, strCCName);
	}

	private static void PriceCDS (
		final CreditDefaultSwap cds,
		final JulianDate dtVal,
		final DiscountCurve dc,
		final CreditCurve cc,
		final double dblNotional)
		throws Exception 
	{
		/*
		 * This T + 0 Valuation, T + 2B settle
		 */

		// ValuationParams valParams = new ValuationParams (dtVal, // T + 0
			// JulianDate.CreateFromYMD (2012, JulianDate.APRIL, 25), // T + 2B
				// dc.getCurrency());

		/*
		 * This T + 1 Valuation, T + 2B settle
		 */

		ValuationParams valParams = new ValuationParams (dtVal.addDays (1), // T + 1
			JulianDate.CreateFromYMD (2012, JulianDate.APRIL, 25), // T + 2B
				dc.getCurrency());

		ComponentMarketParams cmp = ComponentMarketParamsBuilder.MakeCreditCMP (dc, cc);

		PricerParams pricerParams = PricerParams.MakeStdPricerParams();

		Map<String, Double> mapMeasures = cds.value (valParams, pricerParams, cmp, null);

		System.out.println ("Default Probability at Maturity: " + GenericUtil.FormatSpreadSimple
			(1. - cc.getSurvival (cds.getMaturityDate()), 1, 3, 1.));

		System.out.println ("\nCDS Pricing");

		System.out.println ("\tPrice: " + mapMeasures.get ("CleanPrice"));

		System.out.println ("\tPrincipal: " + (int) (mapMeasures.get ("CleanPV") * dblNotional * 0.01));

		System.out.println ("\tAccrual Days: " + mapMeasures.get ("AccrualDays"));

		System.out.println ("\tAccrued: " + mapMeasures.get ("Accrued01") * dblNotional);

		System.out.println ("\tPts Upf: " + mapMeasures.get ("Upfront"));

		System.out.println ("\nAcc Start       Acc End     Pay Date    Index   Spread   Cpn DCF    Pay01    Surv01");

		System.out.println ("---------      ---------    ---------   ------  ------   -------- --------- --------");

		/*
		 * CDS Coupon Cash Flow
		 */

		for (CouponPeriodCurveFactors p : cds.getCouponFlow (valParams, pricerParams, cmp))
			System.out.println (
				JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
				GenericUtil.FormatSpreadSimple (p.getIndexRate(), 1, 4, 1.) +	FIELD_SEPARATOR +
				GenericUtil.FormatSpreadSimple (p.getSpread(), 1, 4, 1.) + FIELD_SEPARATOR +
				GenericUtil.FormatSpreadSimple (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
				GenericUtil.FormatSpreadSimple (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR +
				GenericUtil.FormatSpreadSimple (cc.getSurvival (p.getPayDate()), 1, 4, 1.)
			);
	}

	public static void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		JulianDate dtStart = JulianDate.CreateFromYMD (2012, JulianDate.APRIL, 20);

		String[] astrCashTenor = new String[] {"1M", "2M", "3M", "6M", "9M", "12M"};
		double[] adblCashRate = new double[] {0.002398, 0.003471, 0.004657, 0.007304, 0.008853, 0.010472};
		String[] astrIRSTenor = new String[] {"2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "12Y", "15Y", "20Y", "25Y", "30Y"};
		double[] adblIRSRate = new double[] {0.005655, 0.006900, 0.008980, 0.011325, 0.013720, 0.015945,
			0.017805, 0.01937, 0.02075, 0.02300, 0.025260, 0.026975, 0.027805, 0.028295};

		DiscountCurve dc = BuildBBGRatesCurve (dtStart, astrCashTenor, adblCashRate, astrIRSTenor,
			adblIRSRate, "USD");

		String[] astrCDSTenor = new String[] {"6M", "1Y", "2Y", "3Y", "4Y", "5Y", "7Y", "10Y"};
		double[] adblCDSParSpread = new double[] {30.367, 37.971, 59.381, 78.013, 100.141, 124.000, 144.735, 157.935};

		CreditCurve cc = CreateCreditCurveFromCDS (dtStart, adblCDSParSpread, astrCDSTenor, dc, 0.4, "CORP");

		CreditDefaultSwap cds = CreateCDS (dtStart, "5Y", 0.01, "KOR");

		PriceCDS (cds, dtStart, dc, cc, 2.6145 * 1.e06);
	}
}
