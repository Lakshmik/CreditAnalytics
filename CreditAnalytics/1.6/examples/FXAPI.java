
package org.drip.service.sample;

/*
 * General imports
 */

import java.util.Random;

/*
 * Credit Product imports
 */

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.definition.*;
import org.drip.analytics.support.GenericUtil;
import org.drip.param.valuation.*;
import org.drip.product.definition.*;
import org.drip.product.params.*;

/*
 * Credit Analytics API
 */

import org.drip.analytics.creator.*;
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
 * Demo of the FX API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FXAPI {
	/**
	 * Sample demonstrating the creation/usage of the FX API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void DisplayFXAPI()
		throws Exception
	{
		/*
		 * Create a currency pair
		 */

		CurrencyPair cp = new CurrencyPair ("EUR", "USD", "USD", 10000.);

		Random rand = new Random();

		DiscountCurve dcUSD = DiscountCurveBuilder.CreateFromFlatRate (JulianDate.Today(), "USD", 0.05);

		DiscountCurve dcEUR = DiscountCurveBuilder.CreateFromFlatRate (JulianDate.Today(), "EUR", 0.04);

		double dblFXSpot = 1.40;
		double dblFXFwdMarket = 1.40;
		double[] adblNodes = new double[5];
		double[] adblFXFwd = new double[5];
		boolean[] abIsPIP = new boolean[5];

		for (int i = 0; i < 5; ++i) {
			abIsPIP[i] = false;

			adblFXFwd[i] = dblFXSpot - (i + 1) * 0.01 * rand.nextDouble();

			adblNodes[i] = JulianDate.Today().addYears (i + 1).getJulian();

			System.out.println (cp.getCode() + "[" + (i + 1) + "]=" + GenericUtil.FormatSpreadSimple (adblFXFwd[i]));
		}

		ValuationParams valParams = ValuationParams.CreateValParams (JulianDate.Today(), 0, "USD", Convention.DR_ACTUAL);

		/*
		 * Create the FX forward instrument
		 */

		FXForward fxfwd = FXForwardBuilder.CreateFXForward (cp, JulianDate.Today(), "1Y");

		/*
		 * Calculate the FX forward outright
		 */

		double dblFXFwd = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, false);

		System.out.println (cp.getCode() + "[1Y]= " + dblFXFwd);

		/*
		 * Calculate the FX forward PIP
		 */

		double dblFXFwdPIP = fxfwd.implyFXForward (valParams, dcEUR, dcUSD, 1.4, true);

		System.out.println (cp.getCode() + "[1Y](pip)= " + GenericUtil.FormatSpreadSimple (dblFXFwdPIP));

		/*
		 * Calculate the DC Basis on the EUR curve
		 */

		double dblDCEURBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, false);

		System.out.println ("EUR Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
			GenericUtil.FormatSpreadSimple (dblDCEURBasis));

		/*
		 * Calculate the DC Basis on the USD curve
		 */

		double dblDCUSDBasis = fxfwd.calcDCBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket, true);

		System.out.println ("USD Basis bp for " + cp.getCode() + "[1Y] = " + dblFXFwdMarket + ": " +
			GenericUtil.FormatSpreadSimple (dblDCUSDBasis));

		/*
		 * Create an FX curve from the spot, and the array of nodes, FX forward, as well as the PIP indicator
		 */

		FXForwardCurve fxCurve = FXForwardCurveBuilder.CreateFXForwardCurve (cp, JulianDate.Today(), dblFXSpot, adblNodes, adblFXFwd, abIsPIP);

		/*
		 * Calculate the array of the USD basis
		 */

		double[] adblFullUSDBasis = fxCurve.getFullBasis (valParams, dcEUR, dcUSD, true);

		for (int i = 0; i < adblFullUSDBasis.length; ++i)
			System.out.println ("FullUSDBasis[" + (i + 1) + "Y]=" + GenericUtil.FormatSpread (adblFullUSDBasis[i]));

		/*
		 * Calculate the array of the EUR basis
		 */

		double[] adblFullEURBasis = fxCurve.getFullBasis (valParams, dcEUR, dcUSD, false);

		for (int i = 0; i < adblFullEURBasis.length; ++i)
			System.out.println ("FullEURBasis[" + (i + 1) + "Y]=" + GenericUtil.FormatSpread (adblFullEURBasis[i]));

		/*
		 * Calculate the array of bootstrapped USD basis
		 */

		double[] adblBootstrappedUSDBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, true);

		for (int i = 0; i < adblBootstrappedUSDBasis.length; ++i)
			System.out.println ("Bootstrapped USDBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) + "Y]=" +
				GenericUtil.FormatSpread (adblBootstrappedUSDBasis[i]));

		/*
		 * Calculate the array of bootstrapped EUR basis
		 */

		double[] adblBootstrappedEURBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, false);

		for (int i = 0; i < adblBootstrappedEURBasis.length; ++i)
			System.out.println ("Bootstrapped EURBasis from FX fwd for " + cp.getCode() + "[" + (i + 1) + "Y]=" +
				GenericUtil.FormatSpread (adblBootstrappedEURBasis[i]));

		/*
		 * Create an USD FX Basis Curve from the spot, and the array of nodes, FX Basis
		 */

		FXBasisCurve fxUSDBasisCurve = FXBasisCurveBuilder.CreateFXBasisCurve (cp, JulianDate.Today(), dblFXSpot,
			adblNodes, adblFullUSDBasis, false);

		/*
		 * Re-calculate the array of FX Forward from USD Basis Curve
		 */

		double[] adblFXFwdFromUSDBasis = fxUSDBasisCurve.getFullFXFwd (valParams, dcEUR, dcUSD, true, false);

		for (int i = 0; i < adblFXFwdFromUSDBasis.length; ++i)
			System.out.println ("FX Fwd from Bootstrapped USD Basis: " + cp.getCode() + "[" + (i + 1) + "Y]=" +
				GenericUtil.FormatSpreadSimple (adblFXFwdFromUSDBasis[i]));

		/*
		 * Create an EUR FX Basis Curve from the spot, and the array of nodes, FX Basis
		 */

		FXBasisCurve fxEURBasisCurve = FXBasisCurveBuilder.CreateFXBasisCurve (cp, JulianDate.Today(), dblFXSpot,
			adblNodes, adblFullEURBasis, false);

		/*
		 * Re-calculate the array of FX Forward from EUR Basis Curve
		 */

		double[] adblFXFwdFromEURBasis = fxEURBasisCurve.getFullFXFwd (valParams, dcEUR, dcUSD, false, false);

		for (int i = 0; i < adblFXFwdFromEURBasis.length; ++i)
			System.out.println ("FX Fwd from Bootstrapped EUR Basis: " + cp.getCode() + "[" + (i + 1) + "Y]=" +
				GenericUtil.FormatSpreadSimple (adblFXFwdFromEURBasis[i]));
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		DisplayFXAPI();
	}
}
