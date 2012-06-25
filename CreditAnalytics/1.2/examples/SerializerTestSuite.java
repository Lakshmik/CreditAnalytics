
package org.drip.tester.product;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.CreditAnalytics.org
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

public class SerializerTestSuite {
	private static final boolean s_bTrace = true;

	private static final void Set2DMapValues (final java.util.Map<java.lang.String, java.lang.Double> map2D,
		final double dblPV, final double dblFairPremium) {
		map2D.put ("PV", dblPV);

		map2D.put ("FairPremium", dblFairPremium);
	}

	private static final void Set3DMapValues (final java.util.Map<java.lang.String,
		java.util.Map<java.lang.String, java.lang.Double>> map3D, final double dblPV, final double
			dblFairPremium) {
		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario1Y = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario1Y, dblPV, dblFairPremium);

		map3D.put ("1Y", mapIRScenario1Y);

		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario2Y = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario2Y, dblPV, dblFairPremium);

		map3D.put ("2Y", mapIRScenario2Y);
	}

	private static final void SetCustom3DMapValues (final java.lang.String strCustomSetName, final
		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3D, final
			double dblPV, final double dblFairPremium) {
		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario1Y = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario1Y, dblPV, dblFairPremium);

		map3D.put ("1Y", mapIRScenario1Y);

		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario2Y = new
				java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario2Y, dblPV, dblFairPremium);

		map3D.put ("2Y", mapIRScenario2Y);
	}

	private static final void Set4DMapValues (final java.util.Map<java.lang.String,
		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>> map4D, final
			double dblPV, final double dblFairPremium) {
		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> mapARGComp1Y = new
			java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>();

		Set3DMapValues (mapARGComp1Y, dblPV, dblFairPremium);

		map4D.put ("ARG", mapARGComp1Y);

		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> mapBRAComp1Y = new
			java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>();

		Set3DMapValues (mapBRAComp1Y, dblPV, dblFairPremium);

		map4D.put ("BRA", mapBRAComp1Y);
	}

	private static final void Verify (final byte[] abSer, final org.drip.analytics.core.Serializer sDeser,
		final java.lang.String strDRIPObj) {
		if (null == sDeser || null == abSer || 0 == abSer.length) {
			System.out.println (strDRIPObj + " serialization FAILED!");

			System.exit (138);
		}

		byte[] abDeser = sDeser.serialize();

		if (null == abDeser || 0 == abDeser.length) {
			System.out.println (strDRIPObj + " serialization FAILED!");

			System.exit (139);
		}

		java.lang.String strSer = new java.lang.String (abSer);

		java.lang.String strDeser = new java.lang.String (abDeser);

		if (s_bTrace) System.out.println (strSer + "\n" + strDeser);

		if (!strSer.trim().equalsIgnoreCase (strDeser.trim())) {
			System.out.println (strDRIPObj + " serialization FAILED!");

			System.exit (140);
		}

		System.out.println (strDRIPObj + " serialization OK.\n----------------------\n\n");
	}

	public static final void main (final java.lang.String[] astrArgs) throws java.lang.Exception {
		org.drip.util.internal.Logger.Init ("c:\\Lakshmi\\java\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.DayCountBasis.Init ("c:\\Lakshmi\\java\\BondAnal\\Config.xml");

		org.drip.util.date.JulianDate dtToday = org.drip.util.date.JulianDate.Today();

		double dblStart = dtToday.getJulian();

		double[] adblDCDate = new double[3];
		double[] adblDCRate = new double[3];
		double[] adblHazardDate = new double[3];
		double[] adblHazardRate = new double[3];
		double[] adblRecoveryDate = new double[3];
		double[] adblRecoveryRate = new double[3];
		double[] adblFXBasisDate = new double[3];
		double[] adblFXBasis = new double[3];
		double[] adblFXFwdDate = new double[3];
		double[] adblFXFwd = new double[3];
		boolean[] abIsPIP = new boolean[3];

		for (int i = 0; i < 3; ++i) {
			adblFXFwdDate[i] = dblStart + 365. * (i + 1);
			adblFXFwd[i] = 53.51 + i;
			abIsPIP[i] = false;
		}

		for (int i = 0; i < 3; ++i) {
			adblDCDate[i] = dblStart + 365. * (i + 1);
			adblDCRate[i] = 0.01 * (i + 1);
			adblHazardDate[i] = dblStart + 365. * (i + 1);
			adblHazardRate[i] = 0.01 * (i + 1);
			adblRecoveryDate[i] = dblStart + 365. * (i + 1);
			adblRecoveryRate[i] = 0.40;
			adblFXBasisDate[i] = dblStart + 365. * (i + 1);
			adblFXBasis[i] = 0.02 * (i + 1);
		}

		org.drip.param.product.CurrencyPair cp = new org.drip.param.product.CurrencyPair ("USD", "INR",
			"INR", 1.);

		byte[] abCP = cp.serialize();

		Verify (abCP, new org.drip.param.product.CurrencyPair (abCP), "CurrencyPair");

		org.drip.analytics.curve.DiscountCurve dc = org.drip.analytics.curve.DiscountCurve.CreateDC (dtToday,
			"ABC", adblDCDate, adblDCRate);

		byte[] abDC = dc.serialize();

		Verify (abDC, new org.drip.analytics.curve.DiscountCurve (abDC), "DiscountCurve");

		org.drip.analytics.curve.CreditCurve cc = new org.drip.analytics.curve.CreditCurve (dblStart, "XXS",
			adblHazardRate, adblHazardDate, adblRecoveryRate, adblRecoveryDate);

		byte[] abCC = cc.serialize();

		Verify (abCC, new org.drip.analytics.curve.CreditCurve (abCC), "CreditCurve");

		byte[] abFxBasis = new org.drip.analytics.curve.FXBasis (cp, dtToday, 53.51, adblFXBasisDate,
			adblFXBasis, true).serialize();

		Verify (abFxBasis, new org.drip.analytics.curve.FXBasis (abFxBasis), "FXBasis");

		byte[] abFxCurve = new org.drip.analytics.curve.FXCurve (cp, dtToday, 53.51, adblFXFwdDate,
			adblFXFwd, abIsPIP).serialize();

		Verify (abFxCurve, new org.drip.analytics.curve.FXCurve (abFxCurve), "FXCurve");

		byte[] abAAP = new org.drip.analytics.daycount.ActActDCParams (2, dblStart, dblStart +
			180.).serialize();

		Verify (abAAP, new org.drip.analytics.daycount.ActActDCParams (abAAP), "ActActDCParams");

		byte[] abDAP = new org.drip.analytics.daycount.DateAdjustParams (0, "CZK").serialize();

		Verify (abDAP, new org.drip.analytics.daycount.DateAdjustParams (abDAP), "DateAdjustParams");

		byte[] abFixH = new org.drip.analytics.daycount.FixedHoliday (1, 3, null, "MLK Holiday").serialize();

		Verify (abFixH, new org.drip.analytics.daycount.FixedHoliday (abFixH), "FixedHoliday");

		byte[] abFltH = new org.drip.analytics.daycount.FloatingHoliday (1, 3, 4, false, null,
			"3 Jan Holiday").serialize();

		Verify (abFltH, new org.drip.analytics.daycount.FloatingHoliday (abFltH), "FloatingHoliday");

		byte[] abSH = org.drip.analytics.daycount.StaticHoliday.CreateFromDateDescription ("12-JUN-2020",
			"Are you kidding me?").serialize();

		Verify (abSH, new org.drip.analytics.daycount.StaticHoliday (abSH), "StaticHoliday");

		byte[] abWH = org.drip.analytics.daycount.WeekendHoliday.StandardWeekend().serialize();

		Verify (abWH, new org.drip.analytics.daycount.WeekendHoliday (abWH), "WeekendHoliday");

		byte[] abPeriod = new org.drip.analytics.period.Period (dblStart, dblStart + 180, dblStart, dblStart
			+ 180, dblStart + 180, 0.5).serialize();

		Verify (abPeriod, new org.drip.analytics.period.Period (abPeriod), "Period");

		byte[] abCouponPeriod = new org.drip.analytics.period.CouponPeriod (dblStart, dblStart + 180,
			dblStart, dblStart + 180, dblStart + 180, dblStart + 180, 2, "30/360", true, "30/360", true,
				dblStart + 1825, "GBP").serialize();

		Verify (abCouponPeriod, new org.drip.analytics.period.CouponPeriod (abCouponPeriod), "CouponPeriod");

		byte[] abPCPCM = new org.drip.analytics.period.ProductCouponPeriodCurveMeasures (dblStart, dblStart +
			180., dblStart, dblStart + 180., dblStart + 180., 0.5, 0.05, 1000000., 100000., 0.97,
				0.98).serialize();

		Verify (abPCPCM, new org.drip.analytics.period.ProductCouponPeriodCurveMeasures (abPCPCM),
			"ProductCouponPeriodCurveMeasures");

		byte[] abPLPCM = new org.drip.analytics.period.ProductLossPeriodCurveMeasures (dblStart, dblStart + 180,
			dblStart, dblStart + 180, dblStart + 180, 0.5, 0.98, 0.94, 1000000., 0.36, 0.96).serialize();

		Verify (abPLPCM, new org.drip.analytics.period.ProductCouponPeriodCurveMeasures (abPLPCM),
			"ProductLossPeriodCurveMeasures");

		byte[] abCash = org.drip.product.rates.Cash.CreateCash (dtToday, "1Y", "AUD").serialize();

		Verify (abCash, new org.drip.product.rates.Cash (abCash), "Cash");

		byte[] abEDF = org.drip.product.rates.EDFuture.CreateEDF (dtToday, "1Y", "GBP").serialize();

		Verify (abEDF, new org.drip.product.rates.EDFuture (abEDF), "EDFuture");

		byte[] abIRS = org.drip.product.rates.InterestRateSwap.CreateIRS (dtToday, "4Y", 0.03, "JPY",
			"JPY-LIBOR", "JPY").serialize();

		Verify (abIRS, new org.drip.product.rates.InterestRateSwap (abIRS), "InterestRateSwap");

		org.drip.product.quote.Quote q = new org.drip.product.quote.Quote ("ASK", 103.);

		Verify (q.serialize(), new org.drip.product.quote.Quote (q.serialize()), "Quote");

		org.drip.product.quote.ComponentQuote cq = new org.drip.product.quote.ComponentQuote
			((org.drip.product.common.Component) null);

		cq.addQuote ("Price", q, false);

		cq.setMarketQuote ("SpreadToTsyBmk", new org.drip.product.quote.Quote ("MID", 210.));

		byte[] abCQ = cq.serialize();

		Verify (abCQ, new org.drip.product.quote.ComponentQuote (abCQ), "ComponentQuote");

		byte[] abFxFwd = org.drip.product.fx.FXForward.CreateFXForward (cp, dtToday, "18M").serialize();

		Verify (abFxFwd, new org.drip.product.fx.FXForward (abFxFwd), "FXForward");

		byte[] abFxSpot = new org.drip.product.fx.FXSpot (dtToday, cp).serialize();

		Verify (abFxSpot, new org.drip.product.fx.FXSpot (abFxSpot), "FXSpot");

		byte[] abCDS = org.drip.product.credit.CreditDefaultSwap.CreateSNAC (dtToday, "5Y", 0.01,
			"IBM").serialize();

		Verify (abCDS, new org.drip.product.credit.CreditDefaultSwap (abCDS), "CreditDefaultSwap");

		org.drip.calc.output.ComponentOutput co = new org.drip.calc.output.ComponentOutput();

		co._dblCalcTime = 433.7;

		Set2DMapValues (co._mBase = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.3601,
			537.500);

		Set2DMapValues (co._mRRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.0015,
			0.020);

		Set2DMapValues (co._mRRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.0006,
			0.003);

		Set2DMapValues (co._mFlatIRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0007, 0.006);

		Set2DMapValues (co._mFlatIRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0001, 0.001);

		Set2DMapValues (co._mFlatCreditDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0118, 1.023);

		Set2DMapValues (co._mFlatCreditGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0004, 0.014);

		Set3DMapValues (co._mmTenorIRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (co._mmTenorIRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00007501, 0.000004524);

		Set3DMapValues (co._mmTenorCreditDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00000867, 0.000000238);

		Set3DMapValues (co._mmTenorCreditGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00001734, 0.000000476);

		SetCustom3DMapValues ("CSW10PC", co._mmCustom = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003468, 0.000000952);

		byte abCO[] = co.serialize();

		Verify (abCO, new org.drip.calc.output.ComponentOutput (abCO), "ComponentOutput");

		byte[] abBDS = ((org.drip.product.credit.BasketDefaultSwap)
			org.drip.product.credit.BasketDefaultSwap.MakeCDX (dtToday, dtToday.addYears (5), 0.01, "USD",
				new java.lang.String[] {"CHN", "IND", "INDO", "PAK", "BNG", "JPN"},
					"CDX_ASIA_SOV")).serialize();

		Verify (abBDS, new org.drip.product.credit.BasketDefaultSwap (abBDS), "BasketDefaultSwap");

		org.drip.calc.output.BasketOutput bo = new org.drip.calc.output.BasketOutput();

		bo._dblCalcTime = 433.7;

		Set2DMapValues (bo._mBase = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.3601,
			537.500);

		Set2DMapValues (bo._mFlatRRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			0.0015, 0.020);

		Set2DMapValues (bo._mFlatRRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			0.0006, 0.003);

		Set2DMapValues (bo._mFlatIRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0007, 0.006);

		Set2DMapValues (bo._mFlatIRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0001, 0.001);

		Set2DMapValues (bo._mFlatCreditDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0118, 1.023);

		Set2DMapValues (bo._mFlatCreditGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0004, 0.014);

		Set3DMapValues (bo._mmIRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmIRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmCreditDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmCreditGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmRRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmRRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmIRTenorDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmIRTenorGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmCreditTenorDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmCreditTenorGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		SetCustom3DMapValues ("CSW10PC", bo._mmCustom = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003468, 0.000000952);

		byte abBO[] = bo.serialize();

		Verify (abBO, new org.drip.calc.output.BasketOutput (abBO), "BasketOutput");

		double[] adblNotionalDate = new double[3];
		double[] adblPutDate = new double[3];
		double[] adblCallDate = new double[3];
		double[] adblPutFactor = new double[3];
		double[] adblCallFactor = new double[3];
		double[] adblCouponFactor = new double[3];
		double[] adblNotionalFactor = new double[3];
		adblPutFactor[0] = 0.80;
		adblPutFactor[1] = 0.90;
		adblPutFactor[2] = 1.00;
		adblCallFactor[0] = 1.20;
		adblCallFactor[1] = 1.10;
		adblCallFactor[2] = 1.00;
		adblPutDate[0] = dblStart + 30.;
		adblPutDate[1] = dblStart + 396.;
		adblPutDate[2] = dblStart + 761.;
		adblCallDate[0] = dblStart + 1126.;
		adblCallDate[1] = dblStart + 1492.;
		adblCallDate[2] = dblStart + 1857.;

		for (int i = 0; i < 3; ++i) {
			adblCouponFactor[i] = 1 - 0.1 * i;
			adblNotionalFactor[i] = 1 - 0.05 * i;
			adblNotionalDate[i] = dblStart + 365. * (i + 1);
		}

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0402);

		java.util.Map<org.drip.util.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.util.date.JulianDate, java.util.Map<java.lang.String,
				java.lang.Double>>();

		mmFixings.put (org.drip.util.date.JulianDate.Today().addDays (2), mIndexFixings);

		org.drip.param.product.BondPeriodGenerationParams bpgp = new
			org.drip.param.product.BondPeriodGenerationParams (dblStart + 3653., dblStart, dblStart + 3653.,
				dblStart + 182., dblStart, 2, "30/360", "30/360", null, null, null, null, null, null, null,
					null, "IGNORE", false, "USD");

		if (!bpgp.validate()) {
			System.out.println ("Cannot validate BPGP!");

			System.exit (125);
		}

		byte[] abBPGP = bpgp.serialize();

		Verify (abBPGP, new org.drip.param.product.BondPeriodGenerationParams (abBPGP),
			"BondPeriodGenerationParams");

		org.drip.product.credit.Bond bond = new org.drip.product.credit.Bond();

		org.drip.param.product.TsyBmkSet tbs = new org.drip.param.product.TsyBmkSet ("USD5YON", new
			java.lang.String[] {"USD3YON", "USD7YON"});

		byte[] abTBS = tbs.serialize();

		Verify (abTBS, new org.drip.param.product.TsyBmkSet (abTBS), "TsyBmkSet");

		org.drip.param.product.BondTSYParams btp = new org.drip.param.product.BondTSYParams (tbs, "USDTSY",
			"USDEDSF");

		byte[] abBTP = btp.serialize();

		Verify (abBTP, new org.drip.param.product.BondTSYParams (abBTP), "BondTSYParams");

		if (!bond.setTSYParams (btp)) {
			System.out.println ("Cannot initialize bond TSY params!");

			System.exit (126);
		}

		org.drip.param.product.BondCouponParams bcp = new org.drip.param.product.BondCouponParams
			(org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblNotionalDate,
				adblCouponFactor), "FLOATER", 0.01, java.lang.Double.NaN, java.lang.Double.NaN);

		byte[] abBCP = bcp.serialize();

		Verify (abBCP, new org.drip.param.product.BondCouponParams (abBCP), "BondCouponParams");

		if (!bond.setCouponParams (new org.drip.param.product.BondCouponParams
			(org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblNotionalDate,
				adblCouponFactor), "FLOATER", 0.01, java.lang.Double.NaN, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Coupon params!");

			System.exit (127);
		}

		org.drip.param.product.FactorSchedule fs =
			org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblNotionalDate,
				adblNotionalFactor);

		byte[] abFS = fs.serialize();

		Verify (abFS, new org.drip.param.product.FactorSchedule (abFS), "FactorSchedule");

		org.drip.param.product.BondNotionalParams bnp = new org.drip.param.product.BondNotionalParams (fs,
			1., org.drip.param.product.BondNotionalParams.PERIOD_AMORT_AT_START, false);

		byte[] abBNP = bnp.serialize();

		Verify (abBNP, new org.drip.param.product.BondNotionalParams (abBNP), "BondNotionalParams");

		if (!bond.setNotionalParams (bnp)) {
			System.out.println ("Cannot initialize bond Notional params!");

			System.exit (128);
		}

		org.drip.param.product.BondFloaterParams bflp = new org.drip.param.product.BondFloaterParams
			("USD-LIBOR-6M", "30/360", 0.01, java.lang.Double.NaN);

		byte[] abBFlP = bflp.serialize();

		Verify (abBFlP, new org.drip.param.product.BondFloaterParams (abBFlP), "BondFloaterParams");

		if (!bond.setFloaterParams (bflp)) {
			System.out.println ("Cannot initialize bond Floater params!");

			System.exit (129);
		}

		if (!bond.setFixings (mmFixings)) {
			System.out.println ("Cannot initialize bond Fixings!");

			System.exit (130);
		}

		org.drip.param.product.BondCurrencyParams ccyParams = new org.drip.param.product.BondCurrencyParams
			("USD", "USD", "USD");

		byte[] abCcyParams = ccyParams.serialize();

		Verify (abCcyParams, new org.drip.param.product.BondCurrencyParams (abCcyParams),
			"BondCurrencyParams");

		if (!bond.setCurrencyParams (ccyParams)) {
			System.out.println ("Cannot initialize bond currency params!");

			System.exit (131);
		}

		org.drip.param.product.BondIdentifierParams bip = new org.drip.param.product.BondIdentifierParams
			("US07942381EZ", "07942381E", "IBM-US07942381EZ", "IBM");

		byte[] abBIP = bip.serialize();

		Verify (abBIP, new org.drip.param.product.BondIdentifierParams (abBIP), "BondIdentifierParams");

		if (!bond.setIdentifierParams (bip)) {
			System.out.println ("Cannot initialize bond Identifier params!");

			System.exit (132);
		}

		org.drip.param.product.BondIRValuationParams birvp = new org.drip.param.product.BondIRValuationParams
			("USD", "30/360", "REGULAR", dblStart + 2, 1., 3, "USD",
				org.drip.analytics.daycount.DayCountBasis.DR_FOLL);

		byte[] abBIRVP = birvp.serialize();

		Verify (abBIRVP, new org.drip.param.product.BondIRValuationParams (abBIRVP),
			"BondIRValuationParams");

		if (!bond.setIRValuationParams (birvp)) {
			System.out.println ("Cannot initialize bond IR Valuation params!");

			System.exit (133);
		}

		org.drip.param.product.CompCRValParams crValParams = new org.drip.param.product.CompCRValParams (30,
			java.lang.Double.NaN, true, "IBMSUB", false);

		byte[] abCRVP = crValParams.serialize();

		Verify (abCRVP, new org.drip.param.product.CompCRValParams (abCRVP), "CompCRValParams");

		if (!bond.setCRValuationParams (crValParams)) {
			System.out.println ("Cannot initialize bond Credit Valuation params!");

			System.exit (134);
		}

		org.drip.param.product.BondCFTerminationEvent cfte = new
			org.drip.param.product.BondCFTerminationEvent (false, false, false);

		byte[] abCFTE = cfte.serialize();

		Verify (abCFTE, new org.drip.param.product.BondCFTerminationEvent (abCFTE),
			"BondCFTerminationEvent");

		if (!bond.setCFTEParams (cfte)) {
			System.out.println ("Cannot initialize bond CFTE params!");

			System.exit (135);
		}

		if (!bond.setPeriodGenParams (bpgp)) {
			System.out.println ("Cannot initialize bond Period Generation params!");

			System.exit (136);
		}

		if (!bond.setQuotingParams (new org.drip.param.valuation.QuotingParams ("30/360", 2, true, null,
			"USD", false))) {
			System.out.println ("Cannot initialize bond Quoting params!");

			System.exit (137);
		}

		org.drip.param.product.EmbeddedOptionSchedule eosPut =
			org.drip.param.product.EmbeddedOptionSchedule.fromAmerican (dblStart, adblPutDate, adblPutFactor,
				true, 30, false, java.lang.Double.NaN, "CRAP", java.lang.Double.NaN);

		byte[] abEOS = eosPut.serialize();

		Verify (abEOS, new org.drip.param.product.EmbeddedOptionSchedule (abEOS), "EmbeddedOptionSchedule");

		bond.setEmbeddedPutSchedule (eosPut);

		bond.setEmbeddedCallSchedule (org.drip.param.product.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblCallDate, adblCallFactor, false, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		byte[] abBond = bond.serialize();

		Verify (abBond, new org.drip.product.credit.Bond (abBond), "Bond");

		org.drip.calc.output.BondOutput bondOp = new org.drip.calc.output.BondOutput();

		bondOp._dblCalcTime = 1.;
		bondOp._dblBidPrice = 2.;
		bondOp._dblAskPrice = 3.;
		bondOp._dblBidYield = 4.;
		bondOp._dblAskYield = 5.;
		bondOp._dblBidZSpread = 6.;
		bondOp._dblAskZSpread = 7.;
		bondOp._dblBidGSpread = 8.;
		bondOp._dblAskGSpread = 9.;
		bondOp._dblBidISpread = 10.;
		bondOp._dblAskISpread = 11.;
		bondOp._dblBidTSYSpread = 12.;
		bondOp._dblAskTSYSpread = 13.;
		bondOp._dblBidCreditBasis = 14.;
		bondOp._dblAskCreditBasis = 15.;
		bondOp._dblBidWorkoutDate = 16.;
		bondOp._dblAskWorkoutDate = 17.;
		bondOp._dblBidExerciseFactor = 18.;
		bondOp._dblAskExerciseFactor = 19.;
		bondOp._dblBidAssetSwapSpread = 20.;
		bondOp._dblAskAssetSwapSpread = 21.;

		byte[] abBondOP = bondOp.serialize();

		Verify (abBondOP, new org.drip.calc.output.BondOutput (abBondOP), "BondOutput");

		double[] adblRateTSY = new double[3];
		double[] adblRateEDSF = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblRateTSY[i] = 0.01 * (i + 1);
			adblRateEDSF[i] = 0.0125 * (i + 1);
		}

		org.drip.analytics.curve.DiscountCurve dcTSY = org.drip.analytics.curve.DiscountCurve.CreateDC
			(org.drip.util.date.JulianDate.Today(), "ABCTSY", adblDCDate, adblRateTSY);

		org.drip.analytics.curve.DiscountCurve dcEDSF = org.drip.analytics.curve.DiscountCurve.CreateDC
			(org.drip.util.date.JulianDate.Today(), "ABCEDSF", adblDCDate, adblRateEDSF);

		java.util.Map<java.lang.String, org.drip.product.quote.ComponentQuote> mapTSYQuotes = new
			java.util.HashMap<java.lang.String, org.drip.product.quote.ComponentQuote>();

		mapTSYQuotes.put ("TSY2ON", cq);

		mmFixings.put (org.drip.util.date.JulianDate.Today().addDays (2), mIndexFixings);

		byte[] abCMP = new org.drip.param.market.ComponentMarketParams (dc, dcTSY, dcEDSF, cc, cq,
			mapTSYQuotes, mmFixings).serialize();

		Verify (abCMP, new org.drip.param.market.ComponentMarketParams (abCMP), "ComponentMarketParams");

		java.util.Map<java.lang.String, org.drip.analytics.curve.DiscountCurve> mapDC = new
			java.util.HashMap<java.lang.String, org.drip.analytics.curve.DiscountCurve>();

		mapDC.put ("ABC", dc);

		mapDC.put ("ABCTSY", dcTSY);

		mapDC.put ("ABCEDSF", dcEDSF);

		java.util.Map<java.lang.String, org.drip.analytics.curve.CreditCurve> mapCC = new
			java.util.HashMap<java.lang.String, org.drip.analytics.curve.CreditCurve>();

		mapCC.put ("ABCSOV", cc);

		byte[] abBMP = new org.drip.param.market.BasketMarketParams (mapDC, mapCC, mapTSYQuotes,
			mmFixings).serialize();

		Verify (abBMP, new org.drip.param.market.BasketMarketParams (abBMP), "BasketMarketParams");

		byte[] abNTP = new org.drip.param.market.NodeTweakParams
			(org.drip.param.market.NodeTweakParams.NODE_FLAT_TWEAK, false, 0.1).serialize();

		Verify (abNTP, new org.drip.param.market.NodeTweakParams (abNTP), "NodeTweakParams");

		byte[] abPricer = new org.drip.param.pricer.PricerParams (7, new
			org.drip.param.pricer.CalibrationParams ("KOOL", 1, new org.drip.param.valuation.WorkoutInfo
				(org.drip.util.date.JulianDate.Today().getJulian(), 0.04, 1.,
					org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY)), false, 1).serialize();

		Verify (abPricer, new org.drip.param.pricer.PricerParams (abPricer), "PricerParams");

		java.util.List<org.drip.analytics.period.Period> lPeriods = new
			java.util.ArrayList<org.drip.analytics.period.Period>();

		int i = 5;

		while (0 != i--) {
			lPeriods.add (new org.drip.analytics.period.Period (dblStart, dblStart + 180, dblStart, dblStart
				+ 180, dblStart + 180, 0.5));

			dblStart += 180.;
		}

		org.drip.param.product.BondFixedPeriodGenerationParams bfpgp = new
			org.drip.param.product.BondFixedPeriodGenerationParams (1., "Act/360", 2, lPeriods);

		bfpgp.validate();

		byte[] abBFPGP = bfpgp.serialize();

		Verify (abBFPGP, new org.drip.param.product.BondFixedPeriodGenerationParams (abBFPGP),
			"BondFixedPeriodGenerationParams");

		byte[] abCSP = new org.drip.param.valuation.CashSettleParams (2, "DKK", 3).serialize();

		Verify (abCSP, new org.drip.param.valuation.CashSettleParams (abCSP), "CashSettleParams");

		byte[] abQP = new org.drip.param.valuation.QuotingParams ("30/360", 2, true, null, "DKK",
			false).serialize();

		Verify (abQP, new org.drip.param.valuation.QuotingParams (abQP), "QuotingParams");

		byte[] abVP = org.drip.param.valuation.ValuationParams.CreateValParams (dtToday, 2, "DKK",
			3).serialize();

		Verify (abVP, new org.drip.param.valuation.ValuationParams (abVP), "ValuationParams");

		byte[] abNEI = new org.drip.param.valuation.NextExerciseInfo (dblStart, 1.,
			org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY).serialize();

		Verify (abNEI, new org.drip.param.valuation.NextExerciseInfo (abNEI), "NextExerciseInfo");

		byte[] abWI = new org.drip.param.valuation.WorkoutInfo (dblStart, 0.06, 1.,
			org.drip.param.valuation.WorkoutInfo.WO_TYPE_MATURITY).serialize();

		Verify (abWI, new org.drip.param.valuation.WorkoutInfo (abWI), "WorkoutInfo");

		org.drip.product.creator.BondProductBuilder bpb = new org.drip.product.creator.BondProductBuilder();

		bpb._strISIN = "US734427FC";
		bpb._strCUSIP = "734427F";
		bpb._strTicker = "BSI";
		bpb._dblCoupon = 0.06;

		bpb._dtMaturity = org.drip.util.date.JulianDate.Today().addYears (20);

		bpb._iCouponFreq = 2;
		bpb._strCouponType = "FULL";
		bpb._strMaturityType = "FULL";
		bpb._strCalculationType = "REGULAR";
		bpb._strDayCountCode = "30/360";
		bpb._dblRedemptionValue = 1.;

		bpb._dtAnnounce = org.drip.util.date.JulianDate.Today();

		bpb._dtFirstSettle = bpb._dtAnnounce;
		bpb._dtFirstCoupon = bpb._dtAnnounce;
		bpb._dtInterestAccrualStart = bpb._dtAnnounce;
		bpb._dtIssue = bpb._dtAnnounce;
		bpb._bIsCallable = false;
		bpb._bIsPutable = false;
		bpb._bIsSinkable = false;
		bpb._strRedemptionCurrency = "USD";
		bpb._strCouponCurrency = "USD";
		bpb._strTradeCurrency = "USD";
		bpb._bHasBeenCalled = false;
		bpb._strFloatCouponConvention = "30/360";
		bpb._dblCurrentCoupon = 0.06;
		bpb._bIsFloater = false;
		bpb._dtFinalMaturity = bpb._dtMaturity;
		bpb._bIsPerpetual = false;
		bpb._bIsDefaulted = false;
		bpb._dblFloatSpread = java.lang.Double.NaN;
		bpb._strRateIndex = "USD-LIBOR-6M";
		bpb._strIssuerSPN = "BSI_SNR";

		byte[] abBPB = bpb.serialize();

		Verify (abBPB, new org.drip.product.creator.BondProductBuilder (abBPB), "BondProductBuilder");

		org.drip.product.creator.BondRefDataBuilder brdb = new org.drip.product.creator.BondRefDataBuilder();

		brdb._strISIN = "US3451683DF";
		brdb._strCUSIP = "3451683D";
		brdb._strBBGID = "1286BB45";
		brdb._strIssuerCategory = "Construction";
		brdb._strTicker = "BSI";
		brdb._strSeries = "RegS";
		brdb._strName = "Broken Systems International";
		brdb._strShortName = "Broken Systems";
		brdb._strIssuerIndustry = "Architecture & Engineering";
		brdb._strCouponType = "REGULAR";
		brdb._strMaturityType = "BULLET";
		brdb._strCalculationType = "NORMAL";
		brdb._strDayCountCode = "30/360";
		brdb._strMarketIssueType = "Primary Annual Series A";
		brdb._strIssueCountryCode = "USA";
		brdb._strIssueCountry = "United States of America";
		brdb._strCollateralType = "Equipment";
		brdb._dblIssueAmount = 1000000000.;
		brdb._dblOutstandingAmount = 800000000.;
		brdb._dblMinimumPiece = 1000.;
		brdb._dblMinimumIncrement = 1000.;
		brdb._dblParAmount = 100.;
		brdb._strLeadManager = "LEHMANN";
		brdb._strExchangeCode = "NYSE";
		brdb._dblRedemptionValue = 1.;

		brdb._dtAnnounce = org.drip.util.date.JulianDate.Today();

		brdb._dtFirstSettle = null;
		brdb._dtFirstCoupon = brdb._dtAnnounce;
		brdb._dtInterestAccrualStart = brdb._dtAnnounce;
		brdb._dtIssue = brdb._dtAnnounce;
		brdb._dtNextCouponDate = brdb._dtAnnounce;
		brdb._bIsCallable = false;
		brdb._bIsPutable = false;
		brdb._bIsSinkable = false;
		brdb._strBBGParent = "ADI";
		brdb._strCountryOfIncorporation = "United States of America";
		brdb._strIndustrySector = "ArchConstr";
		brdb._strIndustryGroup = "Software";
		brdb._strIndustrySubgroup = "CAD";
		brdb._strCountryOfGuarantor = "USA";
		brdb._strCountryOfDomicile = "USA";
		brdb._strDescription = "BSI Senior Series 6 pc coupon annual issue";
		brdb._strSecurityType = "BULLET";
		brdb._dtPrevCouponDate = brdb._dtAnnounce;
		brdb._strBBGUniqueID = "BSI374562IID";
		brdb._strLongCompanyName = "Broken System International Inc.";
		brdb._bIsStructuredNote = false;
		brdb._bIsUnitTraded = false;
		brdb._bIsReversibleConvertible = false;
		brdb._strRedemptionCurrency = "USD";
		brdb._strCouponCurrency = "USD";
		brdb._strTradeCurrency = "USD";
		brdb._bIsBearer = false;
		brdb._bIsRegistered = true;
		brdb._bHasBeenCalled = false;
		brdb._strIssuer = "Bentley Systems";
		brdb._dtPenultimateCouponDate = brdb._dtAnnounce;
		brdb._strFloatCouponConvention = "30/360";
		brdb._dblCurrentCoupon = 0.06;
		brdb._bIsFloater = true;
		brdb._bTradeStatus = true;
		brdb._strCDRCountryCode = "US";
		brdb._strCDRSettleCode = "US";
		brdb._bIsPrivatePlacement = false;
		brdb._bIsPerpetual = false;
		brdb._bIsDefaulted = false;
		brdb._dblFloatSpread = 0.01;
		brdb._strRateIndex = "USD-LIBOR-6M";
		brdb._strMoody = "A";
		brdb._strSnP = "A";
		brdb._strFitch = "A";
		brdb._strSnrSub = "Senior";
		brdb._strIssuerSPN = "374528";
		brdb._dblIssuePrice = 93.75;
		brdb._dblCoupon = 0.01;

		brdb._dtMaturity = brdb._dtAnnounce.addYears (10);

		brdb._dtFinalMaturity = brdb._dtMaturity;

		byte[] abBRDB = brdb.serialize();

		Verify (abBRDB, new org.drip.product.creator.BondRefDataBuilder (abBRDB), "BondRefDataBuilder");

		org.drip.product.credit.BasketBond bb = new org.drip.product.credit.BasketBond ("BASKETBOND", new
			org.drip.product.credit.Bond[] {bond, bond}, new double[] {0.7, 1.3},
				org.drip.util.date.JulianDate.Today(), 1.);

		byte[] abBB = bb.serialize();

		Verify (abBB, new org.drip.product.credit.BasketBond (abBB), "BasketBond");

		org.drip.calc.output.BondCouponMeasures bcm = new org.drip.calc.output.BondCouponMeasures (1., 2.,
			3.);

		byte[] abBCM = bcm.serialize();

		Verify (abBCM, new org.drip.calc.output.BondCouponMeasures (abBCM), "BondCouponMeasures");

		org.drip.calc.output.BondWorkoutMeasures bwm = new org.drip.calc.output.BondWorkoutMeasures (null,
			bcm, 4., 5., 6., 7., 8., 9., 10., 11., 12., 13., 14., 15., 16.);

		byte[] abBWM = bwm.serialize();

		Verify (abBWM, new org.drip.calc.output.BondWorkoutMeasures (abBWM), "BondWorkoutMeasures");
	}
}
