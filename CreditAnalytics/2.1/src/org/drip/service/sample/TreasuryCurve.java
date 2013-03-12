
package org.drip.service.sample;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.*;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;

/*
 * Credit Analytics imports
 */

import org.drip.analytics.creator.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.service.api.CreditAnalytics;

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
 * Demo of construction and usage of the treasury discount curve from government bond inputs
 * 
 * @author Lakshmi Krishnamurthy
 */

public class TreasuryCurve {

	/*
	 * Sample demonstrating creation of simple fixed coupon treasury bond
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final Bond CreateTSYBond (
		final String strName,
		final double dblCoupon,
		final JulianDate dt,
		int iNumYears)
		throws Exception
	{
		return BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				strName,					// Name
				"USDTSY",					// Fictitious Treasury Curve Name
				dblCoupon,					// Bond Coupon
				2, 							// Frequency
				"Act/Act",					// Day Count
				dt, 						// Effective
				dt.addYears (iNumYears),	// Maturity
				null,						// Principal Schedule
				null);
	}

	/*
	 * Sample demonstrating creation of a set of the on-the-run treasury bonds
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final Bond[] CreateOnTheRunTSYBondSet (
		final JulianDate dt,
		final String[] astrTSYBondName,
		final int[] aiMaturityYear,
		final double[] adblCoupon)
		throws Exception
	{
		Bond aTSYBond[] = new Bond[astrTSYBondName.length];

		for (int i = 0; i < astrTSYBondName.length; ++i)
			aTSYBond[i] = CreateTSYBond (astrTSYBondName[i], adblCoupon[i], dt, aiMaturityYear[i]);

		return aTSYBond;
	}

	/*
	 * Sample demonstrating building of the treasury discount curve based off the on-the run instruments and their yields
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final DiscountCurve BuildOnTheRunTSYDiscountCurve (
		final JulianDate dt,
		final Bond[] aTSYBond,
		final double[] adblCalibYield)
		throws Exception
	{
		String astrCalibMeasure[] = new String[aTSYBond.length];

		for (int i = 0; i < aTSYBond.length; ++i)
			astrCalibMeasure[i] = "Yield";

		return RatesScenarioCurveBuilder.CreateDiscountCurve (dt,
			"USDTSY", // Fake curve name to indicate it is a USD TSY curve, not the usual USD curve
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
			aTSYBond,
			adblCalibYield,
			astrCalibMeasure,
			null);
	}

	/*
	 * Sample demonstrating calculation of the yields of the input on the run treasury instruments
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final double[] GetOnTheRunYield (
		final JulianDate dt,
		final DiscountCurve dcTSY,
		final Bond[] aTSYBond)
		throws Exception
	{
		double adblYield[] = new double[aTSYBond.length];

		for (int i = 0; i < aTSYBond.length; ++i) {
			double dblPrice = aTSYBond[i].calcPriceFromBumpedDC (new ValuationParams (JulianDate.Today(), JulianDate.Today(), "USD"),
				ComponentMarketParamsBuilder.MakeDiscountCMP (dcTSY), aTSYBond[i].getMaturityDate().getJulian(), 1., 0.);

			System.out.println ("\tPrice[" + aTSYBond[i].getComponentName() + "]: " + org.drip.math.common.FormatUtil.FormatPrice (dblPrice));

			double dblYield = aTSYBond[i].calcYieldFromPrice (new ValuationParams (JulianDate.Today(), JulianDate.Today(), "USD"),
				ComponentMarketParamsBuilder.MakeDiscountCMP (dcTSY), null, dblPrice);

			System.out.println ("\tYield[" + aTSYBond[i].getComponentName() + "]: " + org.drip.math.common.FormatUtil.FormatDouble (dblYield));
		}

		return adblYield;
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		/*
		 * Define name, maturity, coupon, and the market yield of the input on-the-run treasuries  
		 */

		final String[] astrTSYName = new String[] {"TSY2YON", "TSY3YON", "TSY5YON", "TSY7YON", "TSY10YON", "TSY30YON"};
		final int[] aiMaturityYear = new int[] {2, 3, 5, 7, 10, 30};
		final double[] adblCoupon = new double[] {0.0200, 0.0250, 0.0300, 0.0325, 0.0375, 0.0400};
		final double[] adblCalibYield = new double[] {0.0200, 0.0250, 0.0300, 0.0325, 0.0375, 0.0400};

		/*
		 * Create the on-the-run treasury bonds
		 */

		long lTime = System.nanoTime();

		Bond[] aTSYBond = CreateOnTheRunTSYBondSet (
			JulianDate.Today(),
			astrTSYName,
			aiMaturityYear,
			adblCoupon);

		/*
		 * Create the on-the-run treasury discount curve
		 */

		DiscountCurve dcTSY = BuildOnTheRunTSYDiscountCurve (
			JulianDate.Today(),
			aTSYBond,
			adblCalibYield);

		/*
		 * Compare the implied discount rate and input yields - in general they DO NOT match 
		 */

		for (int i = 0; i < astrTSYName.length; ++i) {
			String strTenor = aiMaturityYear[i] + "Y";

			System.out.println ("DF[" + strTenor + "]: " + dcTSY.calcImpliedRate (strTenor) + "; Yield[" + strTenor + "]: " + adblCalibYield[i]);
		}

		System.out.println ("\n----\n");

		double[] adblYield = GetOnTheRunYield (
			JulianDate.Today(),
			dcTSY,
			aTSYBond);

		/*
		 * Compare the implied and the input yields for the on-the-run's - they DO match 
		 */

		for (int i = 0; i < astrTSYName.length; ++i) {
			String strTenor = aiMaturityYear[i] + "Y";

			System.out.println ("CalcYield[" + strTenor + "]: " + adblYield[i] + "; Input[" + strTenor + "]: " + adblCalibYield[i]);
		}

		/*
		 * Finally calculate the yield of an off-the-run instrument off of the on-the-run yield discount curve 
		 */

		/*
		 * Construct off-the-run
		 */

		int iOffTheRunMaturityYears = 10;

		Bond bondOffTheRun = BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				"USD" + iOffTheRunMaturityYears + "YOFF",
				"USD" + iOffTheRunMaturityYears + "TSY",
				0.0375,
				2,
				"Act/Act",
				JulianDate.Today(),
				JulianDate.Today().addYears (iOffTheRunMaturityYears),	// off-the-run
				null,
				null);

		/*
		 * Calculate yield for off-the-run
		 */

		double dblPrice = bondOffTheRun.calcPriceFromBumpedDC (new ValuationParams (JulianDate.Today(), JulianDate.Today(), "USD"),
			ComponentMarketParamsBuilder.MakeDiscountCMP (dcTSY), bondOffTheRun.getMaturityDate().getJulian(), 1., 0.);

		System.out.println ("\nOff-The-Run Price[" + iOffTheRunMaturityYears + "Y]: " + dblPrice);

		double dblYieldOffTheRun = bondOffTheRun.calcYieldFromPrice (new ValuationParams (JulianDate.Today(), JulianDate.Today(), "USD"),
			ComponentMarketParamsBuilder.MakeDiscountCMP (dcTSY), null, dblPrice);

		System.out.println ("\nOff-The-Run Yield[" + iOffTheRunMaturityYears + "Y]: " + dblYieldOffTheRun);

		System.out.println ("\tTime => " + (System.nanoTime() - lTime) * 1.e-06 + " ms");
	}
}
