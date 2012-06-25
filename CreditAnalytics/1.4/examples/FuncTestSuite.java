
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

public class FuncTestSuite {
	/*
	 * Test Mode
	 */

	public static final int TM_BASE = 0;
	public static final int TM_IR_UP01 = 1;
	public static final int TM_IR_DN01 = 2;
	public static final int TM_IR_SET_UP01 = 4;
	public static final int TM_IR_SET_DN01 = 8;
	public static final int TM_IR_TENOR_UP01 = 16;
	public static final int TM_IR_TENOR_DN01 = 32;
	public static final int TM_CC_UP01 = 64;
	public static final int TM_CC_DN01 = 128;
	public static final int TM_CC_SET_UP01 = 256;
	public static final int TM_CC_SET_DN01 = 512;
	public static final int TM_CC_TENOR_UP01 = 1024;
	public static final int TM_CC_TENOR_DN01 = 2048;
	public static final int TM_RR_UP01 = 4096;
	public static final int TM_RR_DN01 = 8192;
	public static final int TM_RR_SET_UP01 = 16384;
	public static final int TM_RR_SET_DN01 = 32768;
	public static final int TM_TSY_UP01 = 65536;
	public static final int TM_TSY_DN01 = 131072;
	public static final int TM_TSY_SET_UP01 = 262144;
	public static final int TM_TSY_SET_DN01 = 524288;
	public static final int TM_TSY_TENOR_UP01 = 1048576;
	public static final int TM_TSY_TENOR_DN01 = 2097152;

	/*
	 * Test Detail
	 */

	public static final int TD_SUCCESS_FAILURE = 0;
	public static final int TD_BRIEF = 1;
	public static final int TD_DETAILED = 2;

	/*
	 * Test Product
	 */

	public static final int TP_NONE = 0;
	public static final int TP_CASH = 1;
	public static final int TP_EDF = 2;
	public static final int TP_IRS = 4;
	public static final int TP_CDS = 8;
	public static final int TP_FIXED_BOND = 16;
	public static final int TP_CDX = 32;
	public static final int TP_BASKET_BOND = 64;

	private static final org.drip.product.credit.Bond CreateSimpleBond (final java.lang.String strName, final
		org.drip.util.date.JulianDate dtEffective, final org.drip.util.date.JulianDate dtMaturity, final
			double dblCoupon, final java.lang.String strCurrency, final java.lang.String strCC, final
				java.lang.String strTicker) throws java.lang.Exception {
		org.drip.product.credit.Bond simpleBond = new org.drip.product.credit.Bond();

		org.drip.param.product.BondTSYParams tsyParams = new org.drip.param.product.BondTSYParams (null,
			strCurrency + "TSY", strCurrency + "EDSF");

		if (!tsyParams.validate()) {
			System.out.println ("TSY params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setTSYParams (tsyParams);

		org.drip.param.product.BondIdentifierParams idParams = new
			org.drip.param.product.BondIdentifierParams (strName, strName, strName, strTicker);

		if (!idParams.validate()) {
			System.out.println ("ID params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setIdentifierParams (idParams);

		org.drip.param.product.BondCouponParams cpnParams = new org.drip.param.product.BondCouponParams
			(null, "", dblCoupon, java.lang.Double.NaN, java.lang.Double.NaN);

		if (!cpnParams.validate()) {
			System.out.println ("Cpn params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setCouponParams (cpnParams);

		org.drip.param.product.BondCurrencyParams ccyParams = new org.drip.param.product.BondCurrencyParams
			(strCurrency, strCurrency, strCurrency);

		if (!ccyParams.validate()) {
			System.out.println ("CCY params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setCurrencyParams (ccyParams);

		simpleBond.setFloaterParams (null);

		org.drip.param.product.BondIRValuationParams irValParams = new
			org.drip.param.product.BondIRValuationParams (strCurrency, "", "", dtEffective.getJulian(), 1.,
				0, strCurrency, org.drip.analytics.daycount.DayCountBasis.DR_MOD_FOLL);

		if (!irValParams.validate()) {
			System.out.println ("IR Val params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setIRValuationParams (irValParams);

		org.drip.param.product.CompCRValParams crValParams = new org.drip.param.product.CompCRValParams (30,
			0., true, strCC, true);

		if (!crValParams.validate()) {
			System.out.println ("CR Val params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setCRValuationParams (crValParams);

		org.drip.param.product.BondCFTerminationEvent cfteParams = new
			org.drip.param.product.BondCFTerminationEvent (false, false, false);

		if (!cfteParams.validate()) {
			System.out.println ("CFTE params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setCFTEParams (cfteParams);

		org.drip.param.product.BondPeriodGenerationParams periodParams = new
			org.drip.param.product.BondPeriodGenerationParams (dtMaturity.getJulian(),
				dtEffective.getJulian(), java.lang.Double.NaN, java.lang.Double.NaN, java.lang.Double.NaN, 2,
					"30/360", "30/360", null, null, null, null, null, null, null, null, "", false,
						strCurrency);

		if (!periodParams.validate()) {
			System.out.println ("Period Gen params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setPeriodGenParams (periodParams);

		org.drip.param.product.BondNotionalParams notlParams = new org.drip.param.product.BondNotionalParams
			(null, 100., org.drip.param.product.BondNotionalParams.PERIOD_AMORT_AT_START, false);

		if (!notlParams.validate()) {
			System.out.println ("Notional params for " + strName + " could not be validated!");

			return null;
		}

		simpleBond.setNotionalParams (notlParams);

		return simpleBond;
	}

	private static final org.drip.product.credit.Bond CreateTSYBond (final java.lang.String strName, final
		double dblCoupon, final org.drip.util.date.JulianDate dt, int iNumYears) throws java.lang.Exception {
		org.drip.product.credit.Bond bondTSY = new org.drip.product.credit.Bond();

		org.drip.param.product.BondTSYParams tsyParams = new org.drip.param.product.BondTSYParams (null,
			"USDTSY", "USDEDSF");

		if (!tsyParams.validate()) {
			System.out.println ("TSY params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setTSYParams (tsyParams);

		org.drip.param.product.BondIdentifierParams idParams = new
			org.drip.param.product.BondIdentifierParams (strName, strName, strName, "UST");

		if (!idParams.validate()) {
			System.out.println ("ID params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setIdentifierParams (idParams);

		org.drip.param.product.BondCouponParams cpnParams = new org.drip.param.product.BondCouponParams
			(null, "", dblCoupon, java.lang.Double.NaN, java.lang.Double.NaN);

		if (!cpnParams.validate()) {
			System.out.println ("Cpn params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setCouponParams (cpnParams);

		org.drip.param.product.BondCurrencyParams ccyParams = new org.drip.param.product.BondCurrencyParams
			("USDTSY", "USDTSY", "USDTSY");

		if (!ccyParams.validate()) {
			System.out.println ("CCY params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setCurrencyParams (ccyParams);

		bondTSY.setFloaterParams (null);

		org.drip.param.product.BondIRValuationParams irValParams = new
			org.drip.param.product.BondIRValuationParams ("USDTSY", "", "", dt.getJulian(), 1., 3, "USD",
				org.drip.analytics.daycount.DayCountBasis.DR_MOD_FOLL);

		if (!irValParams.validate()) {
			System.out.println ("IR Val params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setIRValuationParams (irValParams);

		org.drip.param.product.CompCRValParams crValParams = new org.drip.param.product.CompCRValParams (30,
			java.lang.Double.NaN, false, "", true);

		if (!crValParams.validate()) {
			System.out.println ("CR Val params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setCRValuationParams (crValParams);

		org.drip.param.product.BondCFTerminationEvent cfteParams = new
			org.drip.param.product.BondCFTerminationEvent (false, false, false);

		if (!cfteParams.validate()) {
			System.out.println ("CFTE params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setCFTEParams (cfteParams);

		org.drip.param.product.BondPeriodGenerationParams periodParams = new
			org.drip.param.product.BondPeriodGenerationParams (dt.addYears (iNumYears).getJulian(),
				dt.getJulian(), java.lang.Double.NaN, dt.getJulian(), dt.getJulian(), 2, "30/360", "30/360",
					null, null, null, null, null, null, null, null, "", false, "USD");

		if (!periodParams.validate()) {
			System.out.println ("Period Gen params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setPeriodGenParams (periodParams);

		org.drip.param.product.BondNotionalParams notlParams = new org.drip.param.product.BondNotionalParams
			(null, 100., org.drip.param.product.BondNotionalParams.PERIOD_AMORT_AT_START, false);

		if (!notlParams.validate()) {
			System.out.println ("Notional params for " + strName + " could not be validated!");

			return null;
		}

		bondTSY.setNotionalParams (notlParams);

		return bondTSY;
	}

	private static final boolean addTSYToMPC (final org.drip.param.market.MarketParamsContainer mpc) {
		try {
			org.drip.product.quote.ComponentQuote cq2YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq2YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.02), true);

			mpc.addTSYQuote ("2YON", cq2YON);

			org.drip.product.quote.ComponentQuote cq3YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq3YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.025), true);

			mpc.addTSYQuote ("3YON", cq3YON);

			org.drip.product.quote.ComponentQuote cq5YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq5YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.03), true);

			mpc.addTSYQuote ("5YON", cq5YON);

			org.drip.product.quote.ComponentQuote cq7YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq7YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.0325), true);

			mpc.addTSYQuote ("7YON", cq7YON);

			org.drip.product.quote.ComponentQuote cq10YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq10YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.0375), true);

			mpc.addTSYQuote ("10YON", cq10YON);

			org.drip.product.quote.ComponentQuote cq30YON = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			cq30YON.addQuote ("Yield", new org.drip.product.quote.Quote ("mid", 0.04), true);

			mpc.addTSYQuote ("30YON", cq30YON);

			org.drip.product.quote.ComponentQuote cqBRA_5_00_21 = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			org.drip.product.quote.Quote qPxBRA_5_00_21 = new org.drip.product.quote.Quote ("bid", 0.900);

			qPxBRA_5_00_21.setSide ("mid", 0.900);

			qPxBRA_5_00_21.setSide ("ask", 0.900);

			cqBRA_5_00_21.addQuote ("Price", qPxBRA_5_00_21, true);

			mpc.addCompQuote ("BRA_5.00_21", cqBRA_5_00_21);

			org.drip.product.quote.ComponentQuote cqTESTCDS = new org.drip.product.quote.ComponentQuote
				((org.drip.product.common.Component) null);

			org.drip.product.quote.Quote qTESTCDS = new org.drip.product.quote.Quote ("mid", 101.);

			cqTESTCDS.addQuote ("CleanPrice", qTESTCDS, true);

			mpc.addCompQuote ("TESTCDS", cqTESTCDS);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	private static final void BuildTSYCurve (final org.drip.util.date.JulianDate dt, final
		org.drip.param.market.MarketParamsContainer mpc, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		System.out.println ("--------\nStarting TSY DC tests\n--------");

		int NUM_TSY_CALIB_INSTR = 6;
		double adblRate[] = new double[NUM_TSY_CALIB_INSTR];
		double adblCompCalibValue[] = new double[NUM_TSY_CALIB_INSTR];
		java.lang.String astrCalibMeasure[] = new java.lang.String[NUM_TSY_CALIB_INSTR];
		org.drip.product.common.CalibratableComponent aCompCalib[] = new
			org.drip.product.common.CalibratableComponent[NUM_TSY_CALIB_INSTR];
		adblCompCalibValue[0] = .0200;
		adblCompCalibValue[1] = .0250;
		adblCompCalibValue[2] = .0300;
		adblCompCalibValue[3] = .0325;
		adblCompCalibValue[4] = .0375;
		adblCompCalibValue[5] = .0400;

		long lStart = System.nanoTime();

		aCompCalib[0] = CreateTSYBond ("USD2YON", 0.02, dt, 2);

		aCompCalib[1] = CreateTSYBond ("USD3YON", 0.025, dt, 3);

		aCompCalib[2] = CreateTSYBond ("USD5YON", 0.03, dt, 5);

		aCompCalib[3] = CreateTSYBond ("USD7YON", 0.0325, dt, 7);

		aCompCalib[4] = CreateTSYBond ("USD10YON", 0.0375, dt, 10);

		aCompCalib[5] = CreateTSYBond ("USD30YON", 0.04, dt, 30);

		for (int i = 0; i < NUM_TSY_CALIB_INSTR; ++i) {
			adblRate[i] = 0.02;
			astrCalibMeasure[i] = "Yield";
		}

		org.drip.param.market.IRCurveScenarioContainer irscUSDTSY =
			org.drip.param.market.IRCurveScenarioContainer.FromIRCSG ("USDTSY", aCompCalib);

		irscUSDTSY.cookScenarioDC (new org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, null,
			adblCompCalibValue, 0.0001, astrCalibMeasure, mpc.getFixings(), null, 15);

		System.out.println ("TSYDC Cook in: " + (System.nanoTime() - lStart) * 1.e-09 + " sec");

		mpc.addScenDC ("USDTSY", irscUSDTSY);

		org.drip.analytics.curve.DiscountCurve dcBaseTSY = mpc.getScenCMP (aCompCalib[0], "Base")._dc;

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Base TSY DC build: " + (null == dcBaseTSY ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Base TSY: " + dcBaseTSY.toString());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("Base TSY DC build: " + (null == dcBaseTSY ? "Failure" : "Success"));

			System.out.println ("\n\n------------------\nTesting Base TSY DC Curve\n--------\n");

			for (int i = 0; i < aCompCalib.length; ++i) {
				System.out.println ("TSYRate[" + i + "] = " + dcBaseTSY.calcImpliedRate
					(aCompCalib[i].getMaturityDate().getJulian()));

				System.out.println (astrCalibMeasure[i] + "[" + i + "] = " + aCompCalib[i].calcMeasureValue
					(new org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
						org.drip.param.market.ComponentMarketParams (dcBaseTSY, null, null, null, null, null,
							mpc.getFixings()), null, astrCalibMeasure[i]));
			}
		}

		if (0 != (TM_TSY_UP01 & iTestMode)) {
			org.drip.analytics.curve.DiscountCurve dcBumpUp = mpc.getScenCMP (aCompCalib[0],
				"FlatIRBumpUp")._dc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bump Up TSY DC build: " + (null == dcBumpUp ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bump Up TSY Curve: " + dcBumpUp.toString());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("Bump Up TSY DC build: " + (null == dcBumpUp ? "Failure" : "Success"));

				System.out.println ("Bump Up TSY DC build: " + dcBumpUp.toString());

				System.out.println
					("\n\n----------\nMeasures for Parallel Bump Up TSY DC Curve\n------\n");

				for (int i = 0; i < aCompCalib.length; ++i)
					System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
						aCompCalib[i].calcMeasureValue (new org.drip.param.valuation.ValuationParams (dt,
							dt, "USD"), null, new org.drip.param.market.ComponentMarketParams (dcBumpUp,
								null, null, null, null, null, mpc.getFixings()), null, astrCalibMeasure[i]));
			}
		}

		if (0 != (TM_TSY_DN01 & iTestMode)) {
			org.drip.analytics.curve.DiscountCurve dcBumpDn = mpc.getScenCMP (aCompCalib[0],
				"FlatIRBumpDn")._dc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bump Dn TSY DC build: " + (null == dcBumpDn ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bump Dn TSY DC: " + dcBumpDn.toString());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("\n\n---------\nMeasures for Parallel Bump Dn TSY Curve\n------\n");

				for (int i = 0; i < aCompCalib.length; ++i)
					System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
						aCompCalib[i].calcMeasureValue (new org.drip.param.valuation.ValuationParams (dt,
							dt, "USD"), null, new org.drip.param.market.ComponentMarketParams (dcBumpDn,
								null, null, null, null, null, mpc.getFixings()), null, astrCalibMeasure[i]));
			}
		}

		if (0 != (TM_TSY_TENOR_UP01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPDCUp =
				mpc.getIRTenorCMP (aCompCalib[0], true);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Up TSY build: " + (null == mapCMPDCUp ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCUp.entrySet())
					System.out.println (me.getKey() + me.getValue()._dc.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCUp.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor TSY Bump Up\n--------\n");

					for (int i = 0; i < aCompCalib.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCompCalib[i].getPrimaryCode() + "] = " + aCompCalib[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
									org.drip.param.market.ComponentMarketParams (me.getValue()._dc, null,
										null, null, null, null, mpc.getFixings()), null,
											astrCalibMeasure[i]));
				}
			}
		}

		if (0 != (TM_TSY_TENOR_DN01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPDCDn =
				mpc.getIRTenorCMP (aCompCalib[0], true);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Dn TSY build: " + (null == mapCMPDCDn ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCDn.entrySet())
					System.out.println (me.getKey() + me.getValue().toString());
			} else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCDn.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor TSY Bump Dn\n--------\n");

					for (int i = 0; i < aCompCalib.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCompCalib[i].getPrimaryCode() + "] = " + aCompCalib[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
									org.drip.param.market.ComponentMarketParams (me.getValue()._dc, null,
										null, null, null, null, mpc.getFixings()), null,
											astrCalibMeasure[i]));
				}
			}
		}
	}

	private static void testDC (org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		System.out.println ("--------\nStarting DC tests\n--------");

		int NUM_DC_INSTR = 30;
		double adblDate[] = new double[NUM_DC_INSTR];
		double adblRate[] = new double[NUM_DC_INSTR];
		double adblCompCalibValue[] = new double[NUM_DC_INSTR];
		java.lang.String astrCalibMeasure[] = new java.lang.String[NUM_DC_INSTR];
		org.drip.product.common.CalibratableComponent aCompCalib[] = new
			org.drip.product.common.CalibratableComponent[NUM_DC_INSTR];

		// First 7 instruments - cash calibration

		adblDate[0] = dt.addDays (3).getJulian(); // ON

		adblDate[1] = dt.addDays (4).getJulian(); // 1D (TN)

		adblDate[2] = dt.addDays (9).getJulian(); // 1W

		adblDate[3] = dt.addDays (16).getJulian(); // 2W

		adblDate[4] = dt.addDays (32).getJulian(); // 1M

		adblDate[5] = dt.addDays (62).getJulian(); // 2M

		adblDate[6] = dt.addDays (92).getJulian(); // 3M

		adblCompCalibValue[0] = .0013;
		adblCompCalibValue[1] = .0017;
		adblCompCalibValue[2] = .0017;
		adblCompCalibValue[3] = .0018;
		adblCompCalibValue[4] = .0020;
		adblCompCalibValue[5] = .0023;
		adblCompCalibValue[6] = .0026;

		for (int i = 0; i < 7; ++i) {
			adblRate[i] = 0.02;
			astrCalibMeasure[i] = "Rate";

			aCompCalib[i] = org.drip.product.rates.Cash.CreateCash (dt.addDays (2), new
				org.drip.util.date.JulianDate (adblDate[i]), "USD");
		}

		// Next 8 instruments - EDF calibration

		org.drip.util.date.JulianDate dtEDFStart = dt;
		adblCompCalibValue[7] = .0027;
		adblCompCalibValue[8] = .0032;
		adblCompCalibValue[9] = .0041;
		adblCompCalibValue[10] = .0054;
		adblCompCalibValue[11] = .0077;
		adblCompCalibValue[12] = .0104;
		adblCompCalibValue[13] = .0134;
		adblCompCalibValue[14] = .0160;

		org.drip.product.rates.EDFuture[] aEDF = org.drip.product.rates.EDFuture.genEDPack (dt, 8, "USD");

		for (int i = 0; i < 8; ++i) {
			adblRate[i + 7] = 0.02;
			aCompCalib[i + 7] = aEDF[i];
			astrCalibMeasure[i + 7] = "Rate";

			adblDate[i + 7] = dtEDFStart.addDays ((i + 1) * 91).getJulian();
		}

		// Final 15 instruments - IRS calibration

		adblDate[15] = dt.addDays ((int)(365.25 * 4 + 2)).getJulian(); // 4Y

		adblDate[16] = dt.addDays ((int)(365.25 * 5 + 2)).getJulian(); // 5Y

		adblDate[17] = dt.addDays ((int)(365.25 * 6 + 2)).getJulian(); // 6Y

		adblDate[18] = dt.addDays ((int)(365.25 * 7 + 2)).getJulian(); // 7Y

		adblDate[19] = dt.addDays ((int)(365.25 * 8 + 2)).getJulian(); // 8Y

		adblDate[20] = dt.addDays ((int)(365.25 * 9 + 2)).getJulian(); // 9Y

		adblDate[21] = dt.addDays ((int)(365.25 * 10 + 2)).getJulian(); // 10Y

		adblDate[22] = dt.addDays ((int)(365.25 * 11 + 2)).getJulian(); // 11Y

		adblDate[23] = dt.addDays ((int)(365.25 * 12 + 2)).getJulian(); // 12Y

		adblDate[24] = dt.addDays ((int)(365.25 * 15 + 2)).getJulian(); // 15Y

		adblDate[25] = dt.addDays ((int)(365.25 * 20 + 2)).getJulian(); // 20Y

		adblDate[26] = dt.addDays ((int)(365.25 * 25 + 2)).getJulian(); // 25Y

		adblDate[27] = dt.addDays ((int)(365.25 * 30 + 2)).getJulian(); // 30Y

		adblDate[28] = dt.addDays ((int)(365.25 * 40 + 2)).getJulian(); // 40Y

		adblDate[29] = dt.addDays ((int)(365.25 * 50 + 2)).getJulian(); // 50Y

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
			adblRate[i + 15] = 0.02;
			adblRate[i + 15] = 0.05;
			astrCalibMeasure[i + 15] = "Rate";

			aCompCalib[i + 15] = org.drip.product.rates.InterestRateSwap.CreateIRS (dt.addDays (2), new
				org.drip.util.date.JulianDate (adblDate[i + 15]), 0., "USD", "USD-LIBOR-6M", "USD");
		}

		mpc.addFixings (dt.addDays (2), "USD-LIBOR-6M", 0.0042);

		long lStart = System.nanoTime();

		org.drip.param.market.IRCurveScenarioContainer irscUSD =
			org.drip.param.market.IRCurveScenarioContainer.FromIRCSG ("USD", aCompCalib);

		irscUSD.cookScenarioDC (new org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, null,
			adblCompCalibValue, 0.0001, astrCalibMeasure, mpc.getFixings(), null, 15);

		System.out.println ("DC Cook in: " + (System.nanoTime() - lStart) * 1.e-09 + " sec");

		mpc.addScenDC ("USD", irscUSD);

		addTSYToMPC (mpc);

		org.drip.analytics.curve.DiscountCurve dcBase = mpc.getScenCMP (aCompCalib[0], "Base")._dc;

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Base DC build: " + (null == dcBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Base: " + dcBase.toString());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("Base DC build: " + (null == dcBase ? "Failure" : "Success"));

			System.out.println ("\n\n------------------\nTesting Base DC Curve\n--------\n");

			for (int i = 0; i < aCompCalib.length; ++i)
				System.out.println (astrCalibMeasure[i] + "[" + i + "] = " + aCompCalib[i].calcMeasureValue
					(new org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
						org.drip.param.market.ComponentMarketParams (dcBase, null, null, null, null, null,
							mpc.getFixings()), null, astrCalibMeasure[i]));
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			org.drip.analytics.curve.DiscountCurve dcBumpUp = mpc.getScenCMP (aCompCalib[0],
				"FlatIRBumpUp")._dc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bump Up DC build: " + (null == dcBumpUp ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Base: " + dcBumpUp.toString());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("Bump Up DC build: " + (null == dcBumpUp ? "Failure" : "Success"));

				System.out.println ("Base: " + dcBumpUp.toString());

				System.out.println ("\n\n----------\nMeasures for Parallel Bump Up DC Curve\n------\n");

				for (int i = 0; i < aCompCalib.length; ++i)
					System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
						aCompCalib[i].calcMeasureValue (new org.drip.param.valuation.ValuationParams (dt,
							dt, "USD"), null, new org.drip.param.market.ComponentMarketParams (dcBumpUp,
								null, null, null, null, null, mpc.getFixings()), null, astrCalibMeasure[i]));
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			org.drip.analytics.curve.DiscountCurve dcBumpDn = mpc.getScenCMP (aCompCalib[0],
				"FlatIRBumpDn")._dc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bump Dn DC build: " + (null == dcBumpDn ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bump Dn DC: " + dcBumpDn.toString());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("\n\n---------\nMeasures for Parallel  Bump Dn IR Curve\n------\n");

				for (int i = 0; i < aCompCalib.length; ++i)
					System.out.println (astrCalibMeasure[i] + "[" + i + "] = " +
						aCompCalib[i].calcMeasureValue (new org.drip.param.valuation.ValuationParams (dt,
							dt, "USD"), null, new org.drip.param.market.ComponentMarketParams (dcBumpDn,
								null, null, null, null, null, mpc.getFixings()), null, astrCalibMeasure[i]));
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPDCUp =
				mpc.getIRTenorCMP (aCompCalib[0], true);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Up DC build: " + (null == mapCMPDCUp ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCUp.entrySet())
					System.out.println (me.getKey() + me.getValue()._dc.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCUp.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor IR Bump Up\n--------\n");

					for (int i = 0; i < aCompCalib.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCompCalib[i].getPrimaryCode() + "] = " + aCompCalib[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
									org.drip.param.market.ComponentMarketParams (me.getValue()._dc, null,
										null, null, null, null, mpc.getFixings()), null,
											astrCalibMeasure[i]));
				}
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPDCDn =
				mpc.getIRTenorCMP (aCompCalib[0], true);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Dn DC build: " + (null == mapCMPDCDn ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCDn.entrySet())
					System.out.println (me.getKey() + me.getValue().toString());
			} else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPDCDn.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor IR Bump Dn\n--------\n");

					for (int i = 0; i < aCompCalib.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCompCalib[i].getPrimaryCode() + "] = " + aCompCalib[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, new
									org.drip.param.market.ComponentMarketParams (me.getValue()._dc, null,
										null, null, null, null, mpc.getFixings()), null,
											astrCalibMeasure[i]));
				}
			}
		}

		BuildTSYCurve (dt, mpc, iTestMode, iTestDetail);
	}

	public static void testCC (org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		System.out.println ("--------\nStarting CC tests\n--------");

		boolean bSNACOn = true;
		double[] adblQuotesARG = new double[5];
		double[] adblQuotesBRA = new double[5];
		java.lang.String[] astrCalibMeasure = new java.lang.String[5];
		org.drip.product.credit.CreditDefaultSwap[] aCDSARG = new
			org.drip.product.credit.CreditDefaultSwap[5];
		org.drip.product.credit.CreditDefaultSwap[] aCDSBRA = new
			org.drip.product.credit.CreditDefaultSwap[5];

		for (int i = 0; i < 5; ++i) {
			adblQuotesARG[i] = 700.;
			adblQuotesBRA[i] = 100.;
			astrCalibMeasure[i] = "FairPremium";

			if (bSNACOn) {
				aCDSARG[i] = org.drip.product.credit.CreditDefaultSwap.CreateSNAC (dt, (i + 1) + "Y", 0.07,
					"ARG");

				aCDSBRA[i] = org.drip.product.credit.CreditDefaultSwap.CreateSNAC (dt, (i + 1) + "Y", 0.01,
					"BRA");
			} else {
				aCDSARG[i] = org.drip.product.credit.CreditDefaultSwap.CreateCDS (dt,
					org.drip.util.date.JulianDate.CreateFromYMD (2012 + i, 6, 20), 0.07, "USD", 0.40, "ARG",
						"USD");

				aCDSBRA[i] = org.drip.product.credit.CreditDefaultSwap.CreateCDS (dt,
					org.drip.util.date.JulianDate.CreateFromYMD (2012 + i, 6, 20), 0.01, "USD", 0.40, "BRA",
						"USD");
			}
		}

		org.drip.param.pricer.PricerParams pricerParams = new org.drip.param.pricer.PricerParams (7, null,
			false, org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_DAY_STEP);

		org.drip.analytics.curve.DiscountCurve dc = mpc.getScenCMP (aCDSBRA[0], "Base")._dc;

		org.drip.param.valuation.ValuationParams valParams = new org.drip.param.valuation.ValuationParams
			(dt, dt, "USD");

		long lStart = System.nanoTime();

		org.drip.param.market.CreditCurveScenarioContainer ccscARG =
			org.drip.param.market.CreditCurveScenarioContainer.CreateCCSC (aCDSARG);

		ccscARG.cookScenarioCC ("ARG", valParams, dc, null, null, adblQuotesARG, 0.40, astrCalibMeasure, null,
			null, false, 63);

		org.drip.param.market.CreditCurveScenarioContainer ccscBRA =
			org.drip.param.market.CreditCurveScenarioContainer.CreateCCSC (aCDSBRA);

		ccscBRA.cookScenarioCC ("BRA", valParams, dc, null, null, adblQuotesBRA, 0.40, astrCalibMeasure, null,
			null, false, 63);

		System.out.println ("All CC Cook in: " + (System.nanoTime() - lStart) * 1.e-09 + " sec");

		mpc.addScenCC ("ARG", ccscARG);

		mpc.addScenCC ("BRA", ccscBRA);

		org.drip.analytics.curve.CreditCurve ccBase = mpc.getScenCMP (aCDSBRA[0], "Base")._cc;

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Base CC build: " + (null == ccBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Base: " + ccBase.toString());
		else if (TD_DETAILED == iTestDetail) {
			for (int i = 0; i < aCDSBRA.length; ++i)
				System.out.println ("Base Fair premium = " + aCDSBRA[i].calcMeasureValue (valParams,
					pricerParams, new org.drip.param.market.ComponentMarketParams (dc, null, null, ccBase,
						null, null, null), null, "FairPremium"));
		}

		if (0 != (TM_CC_UP01 & iTestMode)) {
			org.drip.analytics.curve.CreditCurve ccBumpUp = mpc.getScenCMP (aCDSBRA[0],
				"FlatCreditBumpUp")._cc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CC Up01 build: " + (null == ccBumpUp ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CC Up01: " + ccBumpUp.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (int i = 0; i < aCDSBRA.length; ++i)
					System.out.println ("Up01 Fair premium = " + aCDSBRA[i].calcMeasureValue (valParams,
						pricerParams, new org.drip.param.market.ComponentMarketParams (dc, null, null,
							ccBumpUp, null, null, null), null, "FairPremium"));
			}
		}

		if (0 != (TM_CC_DN01 & iTestMode)) {
			org.drip.analytics.curve.CreditCurve ccBumpDn = mpc.getScenCMP (aCDSBRA[0],
				"FlatCreditBumpDn")._cc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CC Dn01 build: " + (null == ccBumpDn ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CC Dn01: " + ccBumpDn.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (int i = 0; i < aCDSBRA.length; ++i)
					System.out.println ("Dn01 Fair premium = " + aCDSBRA[i].calcMeasureValue (valParams,
						pricerParams, new org.drip.param.market.ComponentMarketParams (dc, null, null,
							ccBumpDn, null, null, null), null, "FairPremium"));
			}
		}

		if (0 != (TM_RR_UP01 & iTestMode)) {
			org.drip.analytics.curve.CreditCurve ccRecoveryUp = mpc.getScenCMP (aCDSBRA[0], "RRBumpUp")._cc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CC RR Up01 build: " + (null == ccRecoveryUp ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CC RR Up01: " + ccRecoveryUp.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (int i = 0; i < aCDSBRA.length; ++i)
					System.out.println ("RR Up01 Fair premium = " + aCDSBRA[i].calcMeasureValue (valParams,
						pricerParams, new org.drip.param.market.ComponentMarketParams (dc, null, null,
							ccRecoveryUp, null, null, null), null, "FairPremium"));
			}
		}

		if (0 != (TM_RR_DN01 & iTestMode)) {
			org.drip.analytics.curve.CreditCurve ccRecoveryDn = mpc.getScenCMP (aCDSBRA[0], "RRBumpDn")._cc;

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CC RR Dn01 build: " + (null == ccRecoveryDn ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CC RR Dn01: " + ccRecoveryDn.toString());
			else if (TD_DETAILED == iTestDetail) {
				for (int i = 0; i < aCDSBRA.length; ++i)
					System.out.println ("RR Dn01 Fair premium = " + aCDSBRA[i].calcMeasureValue (valParams,
						pricerParams, new org.drip.param.market.ComponentMarketParams (dc, null, null,
							ccRecoveryDn, null, null, null), null, "FairPremium"));
			}
		}

		if (0 != (TM_CC_TENOR_UP01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPCCUp =
				mpc.getCreditTenorCMP (aCDSBRA[0], true);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Up CC build: " + (null == mapCMPCCUp ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPCCUp.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			} else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPCCUp.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor CC Bump Up\n--------\n");

					for (int i = 0; i < aCDSBRA.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCDSBRA[i].getPrimaryCode() + "] = " + aCDSBRA[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), pricerParams,
									me.getValue(), null, astrCalibMeasure[i]));
				}
			}
		}

		if (0 != (TM_CC_TENOR_DN01 & iTestMode)) {
			java.util.Map<java.lang.String, org.drip.param.market.ComponentMarketParams> mapCMPCCDn =
				mpc.getCreditTenorCMP (aCDSBRA[0], false);

			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Tenor Bump Dn CC build: " + (null == mapCMPCCDn ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPCCDn.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			} else if (TD_DETAILED == iTestDetail) {
				for (java.util.Map.Entry<java.lang.String, org.drip.param.market.ComponentMarketParams> me :
					mapCMPCCDn.entrySet()) {
					System.out.println ("\n\n------\nMeasures for Tenor CC Bump Dn\n--------\n");

					for (int i = 0; i < aCDSBRA.length; ++i)
						System.out.println ("Tenor: " + me.getKey() + "; " + astrCalibMeasure[i] + "[" +
							aCDSBRA[i].getPrimaryCode() + "] = " + aCDSBRA[i].calcMeasureValue (new
								org.drip.param.valuation.ValuationParams (dt, dt, "USD"), pricerParams,
									me.getValue(), null, astrCalibMeasure[i]));
				}
			}
		}
	}

	private static void testCash (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		org.drip.product.rates.Cash cash = org.drip.product.rates.Cash.CreateCash (dt.addDays (2), dt.addDays
			(10), "USD");

		org.drip.calc.output.ComponentOutput cashOut = cash.calcMeasures (new
			org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, mpc, null);

		System.out.println ("Cash calcs in " + cashOut._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Cash Base test: " + (null == cashOut._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Cash measures generated: " + cashOut._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Base Cash measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : cashOut._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Cash FlatIRDelta test: " + (null == cashOut._mFlatIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Delta Cash measures generated: " +
					cashOut._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Delta Cash measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cashOut._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Cash FlatIRGamma test: " + (null == cashOut._mFlatIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Gamma Cash measures generated: " +
					cashOut._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Gamma Cash measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cashOut._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Cash TenorIRDelta test: " + (null == cashOut._mmTenorIRDelta ? "Failure"
					: "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Cash Tenor IR Delta measures generated for: " +
					cashOut._mmTenorIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Cash Tenor IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cashOut._mmTenorIRDelta.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Cash TenorIRGamma test: " + (null == cashOut._mmTenorIRGamma ? "Failure"
					: "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Cash Tenor IR Gamma measures generated for: " +
					cashOut._mmTenorIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Cash Tenor IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cashOut._mmTenorIRGamma.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}
	}

	private static void testEDF (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		org.drip.product.rates.EDFuture[] aEDF = org.drip.product.rates.EDFuture.genEDPack (dt, 1, "USD");
		
		org.drip.product.rates.EDFuture edf = aEDF[0];

		org.drip.calc.output.ComponentOutput edfOut = edf.calcMeasures (new
			org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, mpc, null);

		System.out.println ("EDF calcs in " + edfOut._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("EDF Base test: " + (null == edfOut._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("EDF measures generated: " + edfOut._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Base EDF DC measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : edfOut._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("EDF FlatIRDelta test: " + (null == edfOut._mFlatIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Delta EDF measures generated: " +
					edfOut._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Delta EDF measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					edfOut._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("EDF FlatIRGamma test: " + (null == edfOut._mFlatIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Gamma EDF measures generated: " +
					edfOut._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Gamma EDF measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					edfOut._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("EDF TenorIRDelta test: " + (null == edfOut._mmTenorIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("EDF Tenor IR Delta measures generated for: " +
					edfOut._mmTenorIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying EDF Tenor IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : edfOut._mmTenorIRDelta.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("EDF TenorIRGamma test: " + (null == edfOut._mmTenorIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("EDF Tenor IR Gamma measures generated for: " +
					edfOut._mmTenorIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying EDF Tenor IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : edfOut._mmTenorIRGamma.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}
	}

	private static void testIRS (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		org.drip.product.rates.InterestRateSwap irs = org.drip.product.rates.InterestRateSwap.CreateIRS
			(dt.addDays (2), dt.addDays ((int)(365.25 * 9 + 2)), 0.04, "USD","USD-LIBOR-6M", "USD");

		org.drip.calc.output.ComponentOutput irsOut = irs.calcMeasures (new
			org.drip.param.valuation.ValuationParams (dt, dt, "USD"), null, mpc, null);

		System.out.println ("IRS calcs in " + irsOut._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("IRS Base test: " + (null == irsOut._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("IRS measures generated: " + irsOut._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Base IRS DC measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : irsOut._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("IRS FlatIRDelta test: " + (null == irsOut._mFlatIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Delta IRS measures generated: " +
					irsOut._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Delta IRS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					irsOut._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("IRS FlatIRGamma test: " + (null == irsOut._mFlatIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Gamma IRS measures generated: " +
					irsOut._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Gamma IRS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					irsOut._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("IRS TenorIRDelta test: " + (null == irsOut._mmTenorIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor IR Delta IRS measures generated for: " +
					irsOut._mmTenorIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor IR Delta IRS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : irsOut._mmTenorIRDelta.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("IRS TenorIRGamma test: " + (null == irsOut._mmTenorIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor IR Gamma IRS measures generated for: " +
					irsOut._mmTenorIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor IR Gamma IRS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : irsOut._mmTenorIRGamma.entrySet())
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
			}
		}
	}

	private static void testIRComponent (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail, final int iTestProduct)
			throws java.lang.Exception {
		System.out.println ("--------\nStarting IR Components tests\n--------");

		if (0 != (TP_CASH & iTestProduct)) testCash (mpc, dt, iTestMode, iTestDetail);

		if (0 != (TP_EDF & iTestProduct)) testEDF (mpc, dt, iTestMode, iTestDetail);

		if (0 != (TP_IRS & iTestProduct)) testIRS (mpc, dt, iTestMode, iTestDetail);
	}

	private static void testCDS (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		org.drip.product.credit.CreditDefaultSwap cds = org.drip.product.credit.CreditDefaultSwap.CreateCDS
			(dt, org.drip.util.date.JulianDate.CreateFromYMD (2015, 6, 20), 0.01, "USD", 0.40, "BRA", "USD");

		cds.setName ("TESTCDS");

		org.drip.calc.output.ComponentOutput cdsOut = cds.calcMeasures (new
			org.drip.param.valuation.ValuationParams (dt, dt, "USD"),
				org.drip.param.pricer.PricerParams.MakeStdPricerParams(), mpc, null);

		System.out.println ("CDS calcs in " + cdsOut._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("CDS Base test: " + (null == cdsOut._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("CDS measures generated: " + cdsOut._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Base CDS IR measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : cdsOut._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatIRDelta test: " + (null == cdsOut._mFlatIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Delta CDS measures generated: " +
					cdsOut._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Delta CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatIRGamma test: " + (null == cdsOut._mFlatIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat IR Gamma CDS measures generated: " +
					cdsOut._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat IR Gamma CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatCreditDelta test: " + (null == cdsOut._mFlatCreditDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat Credit Delta CDS measures generated: " +
					cdsOut._mFlatCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat Credit Delta CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mFlatCreditDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatCreditGamma test: " + (null == cdsOut._mFlatCreditGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat Credit Gamma CDS measures generated: " +
					cdsOut._mFlatCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat Credit Gamma CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mFlatCreditGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatRRDelta test: " + (null == cdsOut._mRRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat RR Delta CDS measures generated: " + cdsOut._mRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat RR Delta CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mRRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS FlatRRGamma test: " + (null == cdsOut._mRRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Flat RR Gamma CDS measures generated: " +
					cdsOut._mRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Flat RR Gamma CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdsOut._mRRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS TenorIRDelta test: " + (null == cdsOut._mmTenorIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor IR Delta CDS measures generated for: " +
					cdsOut._mmTenorIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor IR Delta CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cdsOut._mmTenorIRDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS TenorIRGamma test: " + (null == cdsOut._mmTenorIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor IR Gamma CDS measures generated for: " +
					cdsOut._mmTenorIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor IR Gamma CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cdsOut._mmTenorIRGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS TenorCreditDelta test: " + (null == cdsOut._mmTenorCreditDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor Credit Delta CDS measures generated for: " +
					cdsOut._mmTenorCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor Credit Delta CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cdsOut._mmTenorCreditDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDS TenorCreditGamma test: " + (null == cdsOut._mmTenorCreditGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Tenor Credit Gamma CDS measures generated for: " +
					cdsOut._mmTenorCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println
					("----\nDisplaying Tenor Credit Gamma CDS measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : cdsOut._mmTenorCreditGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}
	}

	private static void testFixedCouponBond (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		double adblAmericanCallDates[] = new double[5];
		double adblAmericanCallFactors[] = new double[5];

		org.drip.product.credit.Bond bond = CreateSimpleBond ("BRA_5.00_21",
			org.drip.util.date.JulianDate.CreateFromYMD (2012, 3, 20),
				org.drip.util.date.JulianDate.CreateFromYMD (2013, 6, 20), 0.05, "USD", "BRA", "BRA");

		for (int i = 0; i < 5; ++i) {
			adblAmericanCallDates[i] = org.drip.util.date.JulianDate.CreateFromYMD (2016 + i, 6,
				20).getJulian();

			adblAmericanCallFactors[i] = 1.;
		}

		bond.setEmbeddedCallSchedule (org.drip.param.product.EmbeddedOptionSchedule.fromAmerican
			(org.drip.util.date.JulianDate.CreateFromYMD (2015, 6, 20).getJulian(), adblAmericanCallDates,
				adblAmericanCallFactors, false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN));

		org.drip.calc.output.ComponentOutput bondOut = bond.calcMeasures (new
			org.drip.param.valuation.ValuationParams (dt, dt, "USD"),
				org.drip.param.pricer.PricerParams.MakeStdPricerParams(), mpc, null);

		System.out.println ("Fixed Cpn Bond calcs in " + bondOut._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Fixed Cpn Bond Base test: " + (null == bondOut._mBase ? "Failure" :
				"Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Fixed Cpn Bond measures generated: " + bondOut._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Fixed Cpn Bond Base measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : bondOut._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatIRDelta test: " + (null == bondOut._mFlatIRDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat IR Delta measures generated: " +
					bondOut._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatIRGamma test: " + (null == bondOut._mFlatIRGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat IR Gamma measures generated: " +
					bondOut._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatCreditDelta test: " + (null ==
					bondOut._mFlatCreditDelta ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat Credit Delta measures generated: " +
					bondOut._mFlatCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat Credit Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mFlatCreditDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatCreditGamma test: " + (null ==
					bondOut._mFlatCreditGamma ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat Credit Gamma measures generated: " +
					bondOut._mFlatCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat Credit Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mFlatCreditGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatRRDelta test: " + (null == bondOut._mRRDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat RR Delta measures generated: " +
					bondOut._mRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat RR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mRRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond FlatRRGamma test: " + (null == bondOut._mRRGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Flat RR Gamma measures generated: " +
					bondOut._mRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Flat RR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bondOut._mRRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond TenorIRDelta test: " + (null == bondOut._mmTenorIRDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Tenor IR Delta measures generated for: " +
					bondOut._mmTenorIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Tenor IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : bondOut._mmTenorIRDelta.entrySet()) {
					System.out.println
						("\t----\n\tDisplaying Fixed Cpn Bond Tenor IR Delta measures\n\t----");

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond TenorIRGamma test: " + (null == bondOut._mmTenorIRGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Tenor IR Gamma measures generated for: " +
					bondOut._mmTenorIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Tenor IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : bondOut._mmTenorIRGamma.entrySet()) {
					System.out.println
						("\t----\n\tDisplaying Fixed Cpn Bond Tenor IR Gamma measures\n\t----");

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond TenorCreditDelta test: " + (null ==
					bondOut._mmTenorCreditDelta ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println
					("Fixed Cpn Bond Tenor Credit Delta measures generated for: " +
						bondOut._mmTenorCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Tenor Credit Delta Fixed Cpn Bond measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : bondOut._mmTenorCreditDelta.entrySet()) {
					System.out.println
						("\t----\n\tDisplaying Fixed Cpn Bond Tenor Credit Delta measures\n\t----");

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Fixed Cpn Bond TenorCreditGamma test: " + (null ==
					bondOut._mmTenorCreditGamma ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Fixed Cpn Bond Tenor Credit Gamma measures generated for: " +
					bondOut._mmTenorCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Fixed Cpn Bond Tenor Credit Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meTenorMeasure : bondOut._mmTenorCreditGamma.entrySet()) {
					System.out.println
						("\t----\n\tDisplaying Fixed Cpn Bond Tenor Credit Gamma measures\n\t----");

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meTenorMeasure.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}
	}

	private static void testCreditComponent (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail, final int iTestProduct)
			throws java.lang.Exception {
		System.out.println ("--------\nStarting Credit Components tests\n--------");

		if (0 != (TP_CDS & iTestProduct)) testCDS (mpc, dt, iTestMode, iTestDetail);

		if (0 != (TP_FIXED_BOND & iTestProduct)) testFixedCouponBond (mpc, dt, iTestMode, iTestDetail);
	}

	private static void testBasketDefaultSwap (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		org.drip.product.credit.BasketProduct cdx = org.drip.product.credit.BasketDefaultSwap.MakeCDX (dt,
			org.drip.util.date.JulianDate.CreateFromYMD (2016, 6, 20), 0.05, "USD", new
				java.lang.String[]{"ARG", "BRA"}, "CDX_Test");

		org.drip.param.valuation.ValuationParams valParams = new org.drip.param.valuation.ValuationParams
			(dt, dt, "USD");

		org.drip.param.pricer.PricerParams pricerParams =
			org.drip.param.pricer.PricerParams.MakeStdPricerParams();

		org.drip.calc.output.BasketOutput cdxOp = cdx.calcMeasures (valParams, pricerParams, mpc, null);

		System.out.println ("CDX calcs in " + cdxOp._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("CDX Base test: " + (null == cdxOp._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("CDX measures generated: " + cdxOp._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying CDX Base measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : cdxOp._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatIRDelta test: " + (null == cdxOp._mFlatIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat IR Delta measures generated: " +
					cdxOp._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatIRGamma test: " + (null == cdxOp._mFlatIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat IR Gamma measures generated: " +
					cdxOp._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatCreditDelta test: " + (null == cdxOp._mFlatCreditDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat Credit Delta measures generated: " +
					cdxOp._mFlatCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat Credit Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatCreditDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatCreditGamma test: " + (null == cdxOp._mFlatCreditGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat Credit Gamma measures generated: " +
					cdxOp._mFlatCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat Credit Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatCreditGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatRRDelta test: " + (null == cdxOp._mFlatRRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat RR Delta measures generated: " +
					cdxOp._mFlatRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat RR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatRRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX FlatRRGamma test: " + (null == cdxOp._mFlatRRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX Flat RR Gamma measures generated: " +
					cdxOp._mFlatRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX Flat RR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					cdxOp._mFlatRRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX IRDelta test: " + (null == cdxOp._mmIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX IRDelta measures generated for curves: " +
					cdxOp._mmIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX IRDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meIR : cdxOp._mmIRDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped IR Curve: " + meIR.getKey());
	
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meIR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX IRGamma test: " + (null == cdxOp._mmIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX IRGamma measures generated for curves: " +
					cdxOp._mmIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX IRGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meIR : cdxOp._mmIRGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped IR Curve: " + meIR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meIR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX CreditDelta test: " + (null == cdxOp._mmCreditDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX CreditDelta measures generated for curves: " +
					cdxOp._mmCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX CreditDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meCredit : cdxOp._mmCreditDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped Credit Curve: " +
						meCredit.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meCredit.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX CreditGamma test: " + (null == cdxOp._mmCreditGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX CreditGamma measures generated for curves: " +
					cdxOp._mmCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX CreditGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meCredit : cdxOp._mmCreditGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped Credit Curve: " +
							meCredit.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meCredit.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_RR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX RRDelta test: " + (null == cdxOp._mmRRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX RRDelta measures generated for curves: " +
					cdxOp._mmRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX RRDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meRR : cdxOp._mmRRDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped RR Curve: " + meRR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meRR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_RR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX RRGamma test: " + (null == cdxOp._mmRRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX RRGamma measures generated for curves: " +
					cdxOp._mmRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX RRGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meRR : cdxOp._mmRRGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped RR Curve: " + meRR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meRR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX IRTenorDelta test: " + (null == cdxOp._mmmIRTenorDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX IRTenorDelta measures generated for curves: " +
					cdxOp._mmmIRTenorDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX IRTenorDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmIRTenor :
						cdxOp._mmmIRTenorDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmIRTenor.getValue().entrySet()) {
						System.out.println ("\tDelta measures shown are for IRCurve=" + mmIRTenor.getKey() +
							" and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX IRTenorGamma test: " + (null == cdxOp._mmmIRTenorGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX IRTenorGamma measures generated for curves: " +
					cdxOp._mmmIRTenorGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX IRTenorGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmIRTenor :
						cdxOp._mmmIRTenorGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmIRTenor.getValue().entrySet()) {
						System.out.println ("\tGamma measures shown are for IRCurve=" + mmIRTenor.getKey() +
							" and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_CC_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX CreditTenorDelta test: " + (null == cdxOp._mmmCreditTenorDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX CreditTenorDelta measures generated for curves: " +
					cdxOp._mmmCreditTenorDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX CreditTenorDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmCreditTenor :
						cdxOp._mmmCreditTenorDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmCreditTenor.getValue().entrySet()) {
						System.out.println ("\tDelta measures shown are for Credit=" + mmCreditTenor.getKey()
							+ " and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_CC_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX CreditTenorGamma test: " + (null == cdxOp._mmmCreditTenorGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("CDX CreditTenorGamma measures generated for curves: " +
					cdxOp._mmmCreditTenorGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying CDX CreditTenorGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmCreditTenor :
						cdxOp._mmmCreditTenorGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmCreditTenor.getValue().entrySet()) {
						System.out.println ("\tGamma measures shown are for Credit=" + mmCreditTenor.getKey()
							+ " and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}
	}

	private static void testBasketBond (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail) throws
			java.lang.Exception {
		double dblStart = dt.getJulian();

		double[] adblDate = new double[3];
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
			adblDate[i] = dblStart + 365. * (i + 1);
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

		org.drip.product.credit.Bond bond = new org.drip.product.credit.Bond();

		if (!bond.setTSYParams (new org.drip.param.product.BondTSYParams (new
			org.drip.param.product.TsyBmkSet ("USD5YON", new java.lang.String[] {"USD3YON", "USD7YON"}),
				"USDTSY", "USDEDSF"))) {
			System.out.println ("Cannot initialize bond TSY params!");

			System.exit (126);
		}

		if (!bond.setCouponParams (new org.drip.param.product.BondCouponParams
			(org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblDate, adblCouponFactor),
				"FLOATER", 0.01, java.lang.Double.NaN, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Coupon params!");

			System.exit (127);
		}

		if (!bond.setNotionalParams (new org.drip.param.product.BondNotionalParams
			(org.drip.param.product.FactorSchedule.CreateFromDateFactorArray (adblDate, adblNotionalFactor),
				1., org.drip.param.product.BondNotionalParams.PERIOD_AMORT_AT_START, false))) {
			System.out.println ("Cannot initialize bond Notional params!");

			System.exit (128);
		}

		if (!bond.setFloaterParams (new org.drip.param.product.BondFloaterParams ("USD-LIBOR-6M", "30/360",
			0.01, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Floater params!");

			System.exit (129);
		}

		if (!bond.setFixings (mmFixings)) {
			System.out.println ("Cannot initialize bond Fixings!");

			System.exit (130);
		}

		if (!bond.setCurrencyParams (new org.drip.param.product.BondCurrencyParams ("USD", "USD", "USD"))) {
			System.out.println ("Cannot initialize bond currency params!");

			System.exit (131);
		}

		if (!bond.setIdentifierParams (new org.drip.param.product.BondIdentifierParams ("US07942381EZ",
			"07942381E", "IBM-US07942381EZ", "IBM"))) {
			System.out.println ("Cannot initialize bond Identifier params!");

			System.exit (132);
		}

		if (!bond.setIRValuationParams (new org.drip.param.product.BondIRValuationParams ("USD", "30/360",
			"REGULAR", dblStart + 2, 1., 3, "USD", org.drip.analytics.daycount.DayCountBasis.DR_FOLL))) {
			System.out.println ("Cannot initialize bond IR Valuation params!");

			System.exit (133);
		}

		if (!bond.setCRValuationParams (new org.drip.param.product.CompCRValParams (30, java.lang.Double.NaN,
			true, "IBMSUB", false))) {
			System.out.println ("Cannot initialize bond Credit Valuation params!");

			System.exit (134);
		}

		if (!bond.setCFTEParams (new org.drip.param.product.BondCFTerminationEvent (false, false, false))) {
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

		bond.setEmbeddedPutSchedule (org.drip.param.product.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblPutDate, adblPutFactor, true, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		bond.setEmbeddedCallSchedule (org.drip.param.product.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblCallDate, adblCallFactor, false, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		org.drip.product.credit.BasketBond bb = new org.drip.product.credit.BasketBond ("BASKETBOND", new
			org.drip.product.credit.Bond[] {bond, bond}, new double[] {0.7, 1.3},
				org.drip.util.date.JulianDate.Today(), 1.);

		org.drip.param.valuation.ValuationParams valParams = new org.drip.param.valuation.ValuationParams
			(dt, dt, "USD");

		org.drip.param.pricer.PricerParams pricerParams =
			org.drip.param.pricer.PricerParams.MakeStdPricerParams();

		org.drip.calc.output.BasketOutput bbOp = bb.calcMeasures (valParams, pricerParams, mpc, null);

		System.out.println ("Bond Basket calcs in " + bbOp._dblCalcTime + " sec");

		if (TD_SUCCESS_FAILURE == iTestDetail)
			System.out.println ("Bond Basket Base test: " + (null == bbOp._mBase ? "Failure" : "Success"));
		else if (TD_BRIEF == iTestDetail)
			System.out.println ("Bond Basket measures generated: " + bbOp._mBase.entrySet());
		else if (TD_DETAILED == iTestDetail) {
			System.out.println ("----\nDisplaying Bond Basket Base measures\n----");

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : bbOp._mBase.entrySet())
				System.out.println (me.getKey() + ": " + me.getValue().toString());
		}

		if (0 != (TM_IR_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatIRDelta test: " + (null == bbOp._mFlatIRDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat IR Delta measures generated: " +
					bbOp._mFlatIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat IR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatIRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatIRGamma test: " + (null == bbOp._mFlatIRGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat IR Gamma measures generated: " +
					bbOp._mFlatIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat IR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatIRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatCreditDelta test: " + (null == bbOp._mFlatCreditDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat Credit Delta measures generated: " +
					bbOp._mFlatCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat Credit Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatCreditDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_CC_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatCreditGamma test: " + (null == bbOp._mFlatCreditGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat Credit Gamma measures generated: " +
					bbOp._mFlatCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat Credit Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatCreditGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_SET_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatRRDelta test: " + (null == bbOp._mFlatRRDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat RR Delta measures generated: " +
					bbOp._mFlatRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat RR Delta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatRRDelta.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_RR_SET_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket FlatRRGamma test: " + (null == bbOp._mFlatRRGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket Flat RR Gamma measures generated: " +
					bbOp._mFlatRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket Flat RR Gamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
					bbOp._mFlatRRGamma.entrySet())
					System.out.println (me.getKey() + ": " + me.getValue().toString());
			}
		}

		if (0 != (TM_IR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket IRDelta test: " + (null == bbOp._mmIRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket IRDelta measures generated for curves: " +
					bbOp._mmIRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket IRDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meIR : bbOp._mmIRDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped IR Curve: " + meIR.getKey());
	
					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meIR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket IRGamma test: " + (null == bbOp._mmIRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket IRGamma measures generated for curves: " +
					bbOp._mmIRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket IRGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meIR : bbOp._mmIRGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped IR Curve: " + meIR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meIR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("CDX CreditDelta test: " + (null == bbOp._mmCreditDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket CreditDelta measures generated for curves: " +
					bbOp._mmCreditDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket CreditDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meCredit : bbOp._mmCreditDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped Credit Curve: " +
						meCredit.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meCredit.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_CC_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket CreditGamma test: " + (null == bbOp._mmCreditGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket CreditGamma measures generated for curves: " +
					bbOp._mmCreditGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket CreditGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meCredit : bbOp._mmCreditGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped Credit Curve: " +
							meCredit.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meCredit.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_RR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket RRDelta test: " + (null == bbOp._mmRRDelta ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket RRDelta measures generated for curves: " +
					bbOp._mmRRDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket RRDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meRR : bbOp._mmRRDelta.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped RR Curve: " + meRR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meRR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_RR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket RRGamma test: " + (null == bbOp._mmRRGamma ? "Failure" :
					"Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket RRGamma measures generated for curves: " +
					bbOp._mmRRGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket RRGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					meRR : bbOp._mmRRGamma.entrySet()) {
					System.out.println ("\tMeasures shown are for bumped RR Curve: " + meRR.getKey());

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
						meRR.getValue().entrySet())
						System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
				}
			}
		}

		if (0 != (TM_IR_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket IRTenorDelta test: " + (null == bbOp._mmmIRTenorDelta ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket IRTenorDelta measures generated for curves: " +
					bbOp._mmmIRTenorDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket IRTenorDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmIRTenor :
						bbOp._mmmIRTenorDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmIRTenor.getValue().entrySet()) {
						System.out.println ("\tDelta measures shown are for IRCurve=" + mmIRTenor.getKey() +
							" and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_IR_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket IRTenorGamma test: " + (null == bbOp._mmmIRTenorGamma ?
					"Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket IRTenorGamma measures generated for curves: " +
					bbOp._mmmIRTenorGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket IRTenorGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmIRTenor :
						bbOp._mmmIRTenorGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmIRTenor.getValue().entrySet()) {
						System.out.println ("\tGamma measures shown are for IRCurve=" + mmIRTenor.getKey() +
							" and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_CC_TENOR_UP01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket CreditTenorDelta test: " + (null ==
					bbOp._mmmCreditTenorDelta ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket CreditTenorDelta measures generated for curves: " +
					bbOp._mmmCreditTenorDelta.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket CreditTenorDelta measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmCreditTenor :
						bbOp._mmmCreditTenorDelta.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmCreditTenor.getValue().entrySet()) {
						System.out.println ("\tDelta measures shown are for Credit=" + mmCreditTenor.getKey()
							+ " and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}

		if (0 != (TM_CC_TENOR_DN01 & iTestMode)) {
			if (TD_SUCCESS_FAILURE == iTestDetail)
				System.out.println ("Bond Basket CreditTenorGamma test: " + (null ==
					bbOp._mmmCreditTenorGamma ? "Failure" : "Success"));
			else if (TD_BRIEF == iTestDetail)
				System.out.println ("Bond Basket CreditTenorGamma measures generated for curves: " +
					bbOp._mmmCreditTenorGamma.entrySet());
			else if (TD_DETAILED == iTestDetail) {
				System.out.println ("----\nDisplaying Bond Basket CreditTenorGamma measures\n----");

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					java.util.Map<java.lang.String, java.lang.Double>>> mmCreditTenor :
						bbOp._mmmCreditTenorGamma.entrySet()) {
					for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>> meTenor : mmCreditTenor.getValue().entrySet()) {
						System.out.println ("\tGamma measures shown are for Credit=" + mmCreditTenor.getKey()
							+ " and Tenor=" + meTenor.getKey());

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							meTenor.getValue().entrySet())
							System.out.println ("\t\t" + me.getKey() + ": " + me.getValue().toString());
					}
				}
			}
		}
	}

	private static void testCreditBasketProduct (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail, final int iTestProduct)
			throws java.lang.Exception {
		System.out.println ("--------\nStarting Basket Credit Products tests\n--------");

		if (0 != (TP_CDX & iTestProduct)) testBasketDefaultSwap (mpc, dt, iTestMode, iTestDetail);
	}

	private static void testBondBasketProduct (final org.drip.param.market.MarketParamsContainer mpc, final
		org.drip.util.date.JulianDate dt, final int iTestMode, final int iTestDetail, final int iTestProduct)
			throws java.lang.Exception {
		System.out.println ("--------\nStarting Basket Bond Products tests\n--------");

		if (0 != (TP_BASKET_BOND & iTestProduct)) testBasketBond (mpc, dt, iTestMode, iTestDetail);
	}

	private static final void AnalSim (final int iTestDetail, final int iTestProduct, final int
		iTestMode, final int iNumSimulations, final long lSleepTime) throws java.lang.Exception {
		org.drip.util.date.JulianDate dt = org.drip.util.date.JulianDate.Today();

		org.drip.param.market.MarketParamsContainer mpc = new org.drip.param.market.MarketParamsContainer();

		for (int i = 0; i < iNumSimulations; ++i) {
			long lTestStart = System.nanoTime();

			testDC (mpc, dt, iTestMode, iTestDetail);

			testIRComponent (mpc, dt, iTestMode, iTestDetail, iTestProduct);

			testCC (mpc, dt, 15, iTestDetail);

			testCreditComponent (mpc, dt, iTestMode, iTestDetail, iTestProduct);

			testCreditBasketProduct (mpc, dt, iTestMode, iTestDetail, iTestProduct);

			testBondBasketProduct (mpc, dt, iTestMode, iTestDetail, iTestProduct);

			System.out.println ("Sim # " + (i + 1) + " took " + (System.nanoTime() - lTestStart) * 1.e-09 +
				" sec\n\n");

			java.lang.Thread.sleep (lSleepTime);
		}
	}

	public static void main (java.lang.String astrArgs[]) throws java.lang.Exception {
		org.drip.util.internal.Logger.Init ("c:\\Lakshmi\\java\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.DayCountBasis.Init ("c:\\Lakshmi\\java\\BondAnal\\Config.xml");

		int iNumRuns = 1;
		long lSleepTime = 1000L;
		int iTestDetail = TD_DETAILED;
		// int iTestProduct = TP_FIXED_BOND;
		// int iTestProduct = TP_FIXED_BOND | TP_CDS | TP_BASKET_BOND;
		int iTestProduct = TP_CASH | TP_EDF | TP_IRS | TP_CDS | TP_FIXED_BOND | TP_CDX | TP_BASKET_BOND;
		// int iTestMode = TM_BASE;
		int iTestMode = TM_IR_UP01 | TM_IR_DN01 | TM_IR_SET_UP01 | TM_IR_SET_DN01 | TM_IR_TENOR_UP01 |
			TM_IR_TENOR_DN01 | TM_CC_UP01 | TM_CC_DN01 | TM_CC_SET_UP01 | TM_CC_SET_DN01 | TM_CC_TENOR_UP01 |
				TM_CC_TENOR_DN01 | TM_RR_UP01 | TM_RR_DN01 | TM_RR_SET_UP01 | TM_RR_SET_DN01 | TM_TSY_UP01 |
					TM_TSY_DN01 | TM_TSY_SET_UP01 | TM_TSY_SET_DN01 | TM_TSY_TENOR_UP01 | TM_TSY_TENOR_DN01;

		if (0 < astrArgs.length) {
			iNumRuns = java.lang.Integer.valueOf (astrArgs[0]).intValue();

			if (1 < astrArgs.length) lSleepTime = java.lang.Long.valueOf (astrArgs[0]).longValue();
		}

		AnalSim (iTestDetail, iTestProduct, iTestMode, iNumRuns, lSleepTime);
	}
}
