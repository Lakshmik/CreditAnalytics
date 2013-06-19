
package org.drip.service.sample;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.definition.*;
import org.drip.analytics.period.*;
import org.drip.analytics.support.*;
import org.drip.param.definition.*;
import org.drip.param.pricer.PricerParams;
import org.drip.param.valuation.*;
import org.drip.product.params.*;
import org.drip.product.definition.*;

/*
 * Credit Analytics API imports
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.service.api.CreditAnalytics;

/*
 * DRIP Math Support
 */

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
 * Demo of the bond analytics API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondAnalyticsAPI {
	private static final String FIELD_SEPARATOR = "    ";

	/*
	 * Sample demonstrating creation of the principal factor schedule from date and factor array
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final FactorSchedule MakeFSPrincipal()
		throws Exception
	{
		double[] adblDate = new double[5];
		double[] adblFactor = new double[] {1., 1.0, 1.0, 1.0, 1.0};
		// double[] adblFactor = new double[] {1., 0.9, 0.8, 0.7, 0.6};

		JulianDate dtEOSStart = JulianDate.Today().addDays (2);

		for (int i = 0; i < 5; ++i)
			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();

		return FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
	}

	/*
	 * Sample demonstrating creation of the coupon factor schedule from date and factor array
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final FactorSchedule MakeFSCoupon()
		throws Exception
	{
		double[] adblDate = new double[5];
		double[] adblFactor = new double[] {1., 1.0, 1.0, 1.0, 1.0};
		// double[] adblFactor = new double[] {1., 0.9, 0.8, 0.7, 0.6};

		JulianDate dtEOSStart = JulianDate.Today().addDays (2);

		for (int i = 0; i < 5; ++i)
			adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();

		return FactorSchedule.CreateFromDateFactorArray (adblDate, adblFactor);
	}

	/**
	 * Creates a custom named bond from the bond type and parameters
	 * 
	 * @param strName String representing the bond name
	 * @param iBondType Integer representing the bond type (fixed/floating/custom)
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 * 
	 * @return The created Bond
	 */

	public static final Bond CreateCustomBond (
		final String strName,
		final int iBondType)
		throws Exception
	{
		BondProduct bond = null;
		boolean bEOSOn = false;
		boolean bEOSAmerican = false;

		if (BondBuilder.BOND_TYPE_SIMPLE_FLOATER == iBondType)
			bond = BondBuilder.CreateSimpleFloater ( // Simple Floating Rate Bond
				strName,		// Name
				"USD",			// Currency
				"DRIPRI",		// Rate Index
				0.01,			// Floating Spread
				2,				// Coupon Frequency
				"30/360",		// Day Count
				JulianDate.CreateFromYMD (2008, 9, 21), // Effective
				JulianDate.CreateFromYMD (2023, 9, 20),	// Maturity
				MakeFSPrincipal(),		// Principal Schedule
				MakeFSCoupon());		// Coupon Schedule
		else if (BondBuilder.BOND_TYPE_SIMPLE_FIXED == iBondType)
			bond = BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				strName,		// Name
				"USD",			// Currency
				0.05,			// Bond Coupon
				2,				// Coupon Frequency
				"30/360",		// Day Count
				JulianDate.CreateFromYMD (2008, 9, 21), // Effective
				JulianDate.CreateFromYMD (2023, 9, 20),	// Maturity
				MakeFSPrincipal(),		// Principal Schedule
				MakeFSCoupon());		// Coupon Schedule
		else if (BondBuilder.BOND_TYPE_SIMPLE_FROM_CF == iBondType) {	// Bond from custom coupon and principal flows
			final int NUM_CF_ENTRIES = 30;
			double[] adblCouponAmount = new double[NUM_CF_ENTRIES];
			double[] adblPrincipalAmount = new double[NUM_CF_ENTRIES];
			JulianDate[] adt = new JulianDate[NUM_CF_ENTRIES];

			JulianDate dtEffective = JulianDate.CreateFromYMD (2008, 9, 20);

			for (int i = 0; i < NUM_CF_ENTRIES; ++i) {
				adt[i] = dtEffective.addMonths (6 * (i + 1));

				adblCouponAmount[i] = 0.025;
				adblPrincipalAmount[i] = 1.0;
			}

			bond = BondBuilder.CreateBondFromCF (
				strName,				// Name
				dtEffective,			// Effective
				"USD",					// Currency
				"30/360",				// Day Count
				2,						// Frequency
				adt,					// Array of dates
				adblCouponAmount,		// Array of coupon amount
				adblPrincipalAmount,	// Array of principal amount
				false);					// Principal is an outstanding notional
		}

		/*
		 * Bonds with options embedded
		 */

		if (bEOSOn) {
			double[] adblDate = new double[5];
			double[] adblPutFactor = new double[5];
			double[] adblCallFactor = new double[5];
			EmbeddedOptionSchedule eosPut = null;
			EmbeddedOptionSchedule eosCall = null;

			JulianDate dtEOSStart = JulianDate.Today().addDays (2);

			for (int i = 0; i < 5; ++i) {
				adblPutFactor[i] = 0.9;
				adblCallFactor[i] = 1.0;

				adblDate[i] = dtEOSStart.addYears (i + 2).getJulian();
			}

			if (bEOSAmerican) {		// Creation of the American call and put schedule
				eosCall = EmbeddedOptionSchedule.fromAmerican (JulianDate.Today().getJulian() + 1, adblDate,
					adblCallFactor, false, 30, false, Double.NaN, "", Double.NaN);

				eosPut = EmbeddedOptionSchedule.fromAmerican (JulianDate.Today().getJulian(), adblDate,
					adblPutFactor, true, 30, false, Double.NaN, "", Double.NaN);
			} else {		// Creation of the European call and put schedule
				eosCall = new EmbeddedOptionSchedule (adblDate, adblCallFactor, false, 30, false, Double.NaN, "", Double.NaN);

				eosPut = new EmbeddedOptionSchedule (adblDate, adblPutFactor, true, 30, false, Double.NaN, "", Double.NaN);
			}

			bond.setEmbeddedCallSchedule (eosCall);

			bond.setEmbeddedPutSchedule (eosPut);
		}

		return (Bond) bond;
	}

	/**
	 * Sample demonstrating the creation/usage of the custom bond API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void CustomBondAPISample()
		throws Exception
	{
		Bond[] aBond = new Bond[3];

		/*
		 * Creates a simple fixed coupon bond and adds it to the FI cache as a named object
		 */

		if (null == (aBond[0] = CreditAnalytics.GetBond ("CustomFixed")))
			CreditAnalytics.PutBond ("CustomFixed", aBond[0] = CreateCustomBond ("CustomFixed", BondBuilder.BOND_TYPE_SIMPLE_FIXED));

		/*
		 * Creates a simple floater and adds it to the FI cache as a named object
		 */

		if (null == (aBond[1] = CreditAnalytics.GetBond ("CustomFRN")))
			CreditAnalytics.PutBond ("CustomFRN", aBond[1] = CreateCustomBond ("CustomFRN", BondBuilder.BOND_TYPE_SIMPLE_FLOATER));

		/*
		 * Creates a custom bond from arbitrary cash flows and adds it to the FI cache as a named object
		 */

		if (null == (aBond[2] = CreditAnalytics.GetBond ("CustomBondFromCF")))
			CreditAnalytics.PutBond ("CustomBondFromCF", aBond[2] = CreateCustomBond ("CustomBondFromCF", BondBuilder.BOND_TYPE_SIMPLE_FROM_CF));

		/*
		 * Base Discount Curve
		 */

		DiscountCurve dc = DiscountCurveBuilder.CreateFromFlatRate (JulianDate.Today(), "USD", 0.04);

		/*
		 * Treasury Discount Curve
		 */

		DiscountCurve dcTSY = DiscountCurveBuilder.CreateFromFlatRate (JulianDate.Today(), "USD", 0.03);

		/*
		 * Credit Curve
		 */

		CreditCurve cc = CreditCurveBuilder.FromFlatHazard (JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		for (int i = 0; i < aBond.length; ++i) {
			System.out.println ("\nAcc Start     Acc End     Pay Date      Cpn DCF       Pay01       Surv01");

			System.out.println ("---------    ---------    ---------    ---------    ---------    --------");

			/*
			 * Generates and displays the coupon period details for the bonds
			 */

			for (Period p : aBond[i].getCouponPeriod())
				System.out.println (
					JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (cc.getSurvival (p.getPayDate()), 1, 4, 1.)
				);

			/*
			 * Create the bond's component market parameters from the market inputs
			 */

			ComponentMarketParams cmp = ComponentMarketParamsBuilder.CreateComponentMarketParams (
				dc,		// Discount curve
				dcTSY,	// TSY Discount Curve
				dcTSY,	// EDSF Discount Curve (proxied to TSY Discount Curve
				cc,		// Credit Curve
				null,	// TSY quotes
				null,	// Bond market quote
				AnalyticsHelper.CreateFixingsObject (aBond[i], JulianDate.Today(), 0.04)	// Fixings
			);

			/*
			 * Construct Valuation Parameters
			 */

			ValuationParams valParams = ValuationParams.CreateValParams (JulianDate.Today(), 0, "", Convention.DR_ACTUAL);

			System.out.println ("\nPrice From Yield: " + FormatUtil.FormatDouble (aBond[i].calcPriceFromYield
				(valParams, cmp, null, 0.), 2, 3, 100.));

			WorkoutInfo wi = aBond[i].calcExerciseYieldFromPrice (valParams, cmp, null, 1.);

			System.out.println ("Workout Date: " + JulianDate.fromJulian (wi._dblDate));

			System.out.println ("Workout Factor: " + wi._dblExerciseFactor);

			System.out.println ("Workout Yield: " + FormatUtil.FormatDouble (wi._dblYield, 2, 3, 100.));

			System.out.println ("Workout Yield From Price: " + FormatUtil.FormatDouble
				(aBond[i].calcYieldFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 2, 3, 100.));

			if (!aBond[i].isFloater()) {
				System.out.println ("Z Spread From Price: " + FormatUtil.FormatDouble
					(aBond[i].calcZSpreadFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));

				System.out.println ("OAS From Price: " + FormatUtil.FormatDouble
					(aBond[i].calcOASFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));
			}

			System.out.println ("I Spread From Price: " + FormatUtil.FormatDouble (aBond[i].calcISpreadFromPrice
				(valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));

			System.out.println ("Discount Margin From Price: " + FormatUtil.FormatDouble (aBond[i].calcDiscountMarginFromPrice
				(valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));

			System.out.println ("TSY Spread From Price: " + FormatUtil.FormatDouble
				(aBond[i].calcTSYSpreadFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));

			System.out.println ("ASW From Price: " + (int) aBond[i].calcASWFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.));

			System.out.println ("Credit Basis From Price: " + FormatUtil.FormatDouble
				(aBond[i].calcCreditBasisFromPrice (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 1.), 1, 3, 100.));

			System.out.println ("Price From TSY Spread: " + FormatUtil.FormatDouble
				(aBond[i].calcPriceFromTSYSpread (valParams, cmp, null, 0.0188), 1, 3, 100.));

			System.out.println ("Yield From TSY Spread: " + FormatUtil.FormatDouble
				(aBond[i].calcYieldFromTSYSpread (valParams, cmp, null, 0.0188), 1, 3, 100.));

			System.out.println ("ASW From TSY Spread: " + (int) aBond[i].calcASWFromTSYSpread (valParams, cmp, null, 0.0188));

			System.out.println ("Credit Basis From TSY Spread: " + FormatUtil.FormatDouble
				(aBond[i].calcCreditBasisFromTSYSpread (valParams, cmp, null, 0.0188), 1, 3, 100.));

			/* System.out.println ("PECS From TSY Spread: " + FIGen.FormatSpread
				(aBond[i].calcPECSFromTSYSpread (valParams, cmp, null, 0.0188))); */

			System.out.println ("Theoretical Price: " + FormatUtil.FormatDouble
				(aBond[i].calcPriceFromCreditBasis (valParams, cmp, null, wi._dblDate, wi._dblExerciseFactor, 0.), 1, 3, 100.));
		}
	}

	/**
	 * API demonstrating how to calibrate a CDS curve from CDS and bond quotes
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static void BondCDSCurveCalibration()
		throws Exception
	{
		/*
		 * Bond calibration instrument
		 */

		Bond bond = BondBuilder.CreateSimpleFixed ("CCCalibBond", "DKK", 0.05, 2, "30/360",
			JulianDate.CreateFromYMD (2008, 9, 21), JulianDate.CreateFromYMD (2023, 9, 20), null, null);

		/*
		 * Discount Curve
		 */

		DiscountCurve dc = DiscountCurveBuilder.CreateFromFlatRate (JulianDate.Today(), "DKK", 0.04);

		/*
		 * Credit Curve
		 */

		CreditCurve cc = CreditCurveBuilder.FromFlatHazard (JulianDate.Today().getJulian(), "CC", 0.01, 0.4);

		/*
		 * Component Market Parameters Container
		 */

		ComponentMarketParams cmp =  ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, cc, null, null, null);

		/*
		 * Valuation Parameters
		 */

		ValuationParams valParams = ValuationParams.CreateValParams (JulianDate.Today(), 0, "USD", Convention.DR_ACTUAL);

		/*
		 * Theoretical Price
		 */

		double dblTheoreticalPrice = bond.calcPriceFromCreditBasis (valParams, cmp, null, bond.getMaturityDate().getJulian(), 1., 0.01);


		System.out.println ("Credit Price From DC and CC: " + dblTheoreticalPrice);

		/*
		 * CDS calibration instrument
		 */

		CreditDefaultSwap cds = CDSBuilder.CreateCDS (JulianDate.Today(), JulianDate.Today().addTenor ("5Y"),
			0.1, "DKK", 0.40, "CC", "DKK", true);

		/*
		 * Set up the calibration instruments
		 */

		CalibratableComponent[] aCalibInst = new CalibratableComponent[] {cds, bond};

		/*
		 * Set up the calibration measures
		 */

		String[] astrCalibMeasure = new String[] {"FairPremium", "FairPrice"};

		/*
		 * Set up the calibration quotes
		 */

		double[] adblQuotes = new double[] {100., dblTheoreticalPrice};

		/*
		 * Setup the curve scenario calibrator/generator and build the credit curve
		 */

		CreditCurve ccCalib = CreditScenarioCurveBuilder.CreateCreditCurve (
				"CC", 					// Name
				JulianDate.Today(), 	// Date
				aCalibInst,				// Calibration instruments
				dc,						// Discount Curve
				adblQuotes,				// Component Quotes
				astrCalibMeasure,		// Calibration Measures
				0.40,					// Recovery
				false);					// Calibration is not flat

		/*
		 * Calculate the survival probability, and recover the input quotes
		 */

		System.out.println ("Surv (2021, 1, 14): " + ccCalib.getSurvival (JulianDate.CreateFromYMD (2021, 1, 14)));

		/*
		 * Calibrated Component Market Parameters Container
		 */

		ComponentMarketParams cmpCalib = ComponentMarketParamsBuilder.CreateComponentMarketParams (dc, null, null, ccCalib, null, null, null);

		/*
		 * Verify the CDS fair premium using the calibrated credit curve
		 */

		System.out.println (cds.getPrimaryCode() + " => " + cds.calcMeasureValue (
			valParams,
			PricerParams.MakeStdPricerParams(),
			cmpCalib,
			null,
			"FairPremium"));

		/*
		 * Verify the Bond fair price using the calibrated credit curve
		 */

		System.out.println (bond.getPrimaryCode() + " => " + bond.calcPriceFromCreditBasis (
			valParams,
			cmpCalib,
			null,
			bond.getMaturityDate().getJulian(),
			1.,
			0.));
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		CustomBondAPISample();

		BondCDSCurveCalibration();
	}
}
