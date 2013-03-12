
package org.drip.service.env;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 * Container that holds the EOD and CDS/credit curve information on a per-issuer basis.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSManager {
	private static final boolean s_bBlog = false;
	private static final boolean s_bLocalTS = false;
	private static final boolean s_bCalcFlatSpread = false;
	private static final boolean s_bLoadStaticCurves = false;

	/**
	 * Retrieves all the credit curves for a given date
	 * 
	 * @param stmt SQL Statement object representing the executable query
	 * @param dtEOD EOD date
	 * 
	 * @return Set of curve strings
	 */

	public static final java.util.Set<java.lang.String> GetCreditCurves (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == stmt || null == dtEOD) return null;

		try {
			java.util.Set<java.lang.String> setCreditCurves = new java.util.HashSet<java.lang.String>();

			java.sql.ResultSet rsCreditCurves = stmt.executeQuery
				("select distinct SPN from CR_EOD where EOD = '" + dtEOD.toOracleDate() + "' order by SPN");

			while (rsCreditCurves.next())
				setCreditCurves.add (rsCreditCurves.getString ("SPN"));

			return setCreditCurves;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Saves the EOD CDS measures for a credit curve in a given EOD
	 * 
	 * @param stmt SQL Statement representing the executable query
	 * @param mpc MarketParamContainer with all the closing discount/credit curves
	 * @param strSPN Credit curve ID string
	 * @param dtEOD EOD date
	 * @param strCurrency Discount curve string
	 * 
	 * @return Success (true), failure (false)
	 */

	public static final boolean SaveSPNEOD (
		final java.sql.Statement stmt,
		final org.drip.param.definition.MarketParams mpc,
		final java.lang.String strSPN,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency)
	{
		if (null == stmt || null == dtEOD || null == strSPN || strSPN.isEmpty() || null == mpc || null ==
			mpc.getCCSG() || null == mpc.getCCSG().get (strSPN))
			return false;

		org.drip.param.definition.CreditScenarioCurve ccsg = mpc.getCCSG().get (strSPN);

		if (null == ccsg.getCCBase() || null == ccsg.getCCBase().getCalibComponents()) return false;

		org.drip.analytics.definition.CreditCurve cc = ccsg.getCCBase();

		org.drip.product.definition.CreditDefaultSwap[] aCDS =
			(org.drip.product.definition.CreditDefaultSwap[]) cc.getCalibComponents();

		org.drip.param.pricer.PricerParams pricerParams = new org.drip.param.pricer.PricerParams (7, null,
			false, org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_DAY_STEP);

		org.drip.param.valuation.ValuationParams valParams = null;

		try {
			valParams = new org.drip.param.valuation.ValuationParams (dtEOD, dtEOD.addBusDays (3,
				strCurrency), strCurrency);

			stmt.executeQuery ("delete from CDSHist where SPN = '" + strSPN + "' and EOD = '" +
				dtEOD.toOracleDate() + "'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		java.lang.StringBuilder sbSQL = new java.lang.StringBuilder();

		java.lang.StringBuilder sbDV01 = new java.lang.StringBuilder();

		java.lang.StringBuilder sbParSpread = new java.lang.StringBuilder();

		java.lang.StringBuilder sbUpfront100 = new java.lang.StringBuilder();

		java.lang.StringBuilder sbUpfront500 = new java.lang.StringBuilder();

		java.lang.StringBuilder sbFlatSpread100 = new java.lang.StringBuilder();

		java.lang.StringBuilder sbFlatSpread500 = new java.lang.StringBuilder();

		sbSQL.append ("insert into CDSHist values('").append (strSPN).append ("', '").append
			(dtEOD.toOracleDate()).append ("', '").append (strCurrency).append ("', 'ParSpread', ");

		long lStart = System.nanoTime();

		for (int i = 0; i < aCDS.length; ++i) {
			java.util.Map<java.lang.String, java.lang.Double> mapCalc = aCDS[1].value (valParams,
				pricerParams, org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
					(mpc.getScenCMP (aCDS[i], "Base").getDiscountCurve(), null, null, cc, null, null, null),
						null);

			if (null == mapCalc) {
				System.out.println ("CDS Calc for " + strSPN + " and " + aCDS[1].getComponentName() +
					"failed");

				continue;
			}

			double dblDV01 = mapCalc.get ("CleanDV01");

			double dblFairPremium = mapCalc.get ("FairPremium");

			double dblUpfront100 = mapCalc.get ("LossPV") - dblDV01 * 100.;

			double dblUpfront500 = mapCalc.get ("LossPV") - dblDV01 * 500.;

			if (s_bBlog) {
				System.out.println ("\nDV01[" + i + "]=" + dblDV01);

				System.out.println ("Upfront100[" + i + "]=" + dblUpfront100);

				System.out.println ("Upfront500[" + i + "]=" + dblUpfront500);

				System.out.println ("dblFairPremium[" + i + "]=" + dblFairPremium);
			}

			if (java.lang.Double.isNaN (dblFairPremium))
				sbParSpread.append ("null, ");
			else
				sbParSpread.append (dblFairPremium).append (", ");

			if (java.lang.Double.isNaN (dblUpfront100))
				sbUpfront100.append ("null, ");
			else
				sbUpfront100.append (dblUpfront100).append (", ");

			if (java.lang.Double.isNaN (dblUpfront500))
				sbUpfront500.append ("null, ");
			else
				sbUpfront500.append (dblUpfront500).append (", ");

			if (java.lang.Double.isNaN (dblDV01))
				sbDV01.append ("null");
			else
				sbDV01.append (dblDV01);

			if (i != aCDS.length - 1) sbDV01.append (", ");
		}

		if (s_bLocalTS)
			System.out.println ("Par Spreads in " + (System.nanoTime() - lStart) * 1.e-06 + " msec");

		for (int i = 0; i < aCDS.length; ++i) {
			double dblOldCoupon = java.lang.Double.NaN;
			double dblFlatSpread100 = java.lang.Double.NaN;

			try {
				dblOldCoupon = aCDS[i].resetCoupon (0.01);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}

			if (s_bCalcFlatSpread) {
				try {
					dblFlatSpread100 = aCDS[i].calibFlatSpread (valParams, pricerParams,
						org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
							(mpc.getScenCMP (aCDS[i], "Base").getDiscountCurve(), null, null, cc, null, null,
								null), null);
				} catch (java.lang.Exception e) {
					System.out.println ("SPN: " + strSPN + "; Flat spread 500 calc problem!" +
						e.getMessage());

					if (s_bBlog) e.printStackTrace();
				}
			}

			if (s_bBlog) System.out.println ("FlatSpread100[" + i + "]=" + dblFlatSpread100);

			if (java.lang.Double.isNaN (dblFlatSpread100))
				sbFlatSpread100.append ("null, ");
			else
				sbFlatSpread100.append (dblFlatSpread100).append (", ");

			if (s_bLocalTS)
				System.out.println ("FlatSpread100[" + i + "]=" + (System.nanoTime() - lStart) * 1.e-06 +
					" msec");

			try {
				aCDS[i].resetCoupon (dblOldCoupon);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		if (s_bLocalTS) {
			System.out.println ("Flat Spread 100 in " + (System.nanoTime() - lStart) * 1.e-06 + " msec");

			lStart = System.nanoTime();
		}

		for (int i = 0; i < aCDS.length; ++i) {
			double dblOldCoupon = java.lang.Double.NaN;
			double dblFlatSpread500 = java.lang.Double.NaN;

			try {
				dblOldCoupon = aCDS[i].resetCoupon (0.05);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}

			if (s_bCalcFlatSpread) {
				try {
					dblFlatSpread500 = aCDS[i].calibFlatSpread (valParams, pricerParams, 
						org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
							(mpc.getScenCMP (aCDS[i], "Base").getDiscountCurve(), null, null, cc, null, null,
								null), null);
				} catch (java.lang.Exception e) {
					System.out.println ("SPN: " + strSPN + "; Flat spread 500 calc problem!" +
						e.getMessage());

					if (s_bBlog) e.printStackTrace();
				}
			}

			if (s_bBlog) System.out.println ("FlatSpread500[" + i + "]=" + dblFlatSpread500);

			if (java.lang.Double.isNaN (dblFlatSpread500))
				sbFlatSpread500.append ("null, ");
			else
				sbFlatSpread500.append (dblFlatSpread500).append (", ");

			if (s_bLocalTS)
				System.out.println ("FlatSpread500[" + i + "]=" + (System.nanoTime() - lStart) * 1.e-06 +
					" msec");

			try {
				aCDS[i].resetCoupon (dblOldCoupon);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		if (s_bLocalTS)
			System.out.println ("Flat Spread 500 in " + (System.nanoTime() - lStart) * 1.e-06 + " msec");

		sbSQL.append (sbParSpread.toString()).append (sbUpfront100.toString()).append
			(sbUpfront500.toString()).append (sbFlatSpread100.toString()).append
				(sbFlatSpread500.toString()).append (sbDV01.toString()).append (")");

		if (s_bBlog) System.out.println (sbSQL.toString());

		try {
			stmt.executeQuery (sbSQL.toString());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	/**
	 * Saves the EOD measures corresponding to all the credit curves for a given EOD and currency
	 * 
	 * @param mpc MarketParamContainer containing the closing discount/credit curves
	 * @param stmt SQL Statement representing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency String representing the discount curve
	 * 
	 * @return Success (true), failure (false)
	 */

	public static final boolean SaveCREOD (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency)
	{
		if (null == mpc || null == mpc.getCCSG() || null == mpc.getCCSG().entrySet()) return false;

		boolean bAllSPNSuccess = true;

		for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.CreditScenarioCurve> meCCSG
			: mpc.getCCSG().entrySet()) {
			if (null == meCCSG.getKey()) continue;

			if (!SaveSPNEOD (stmt, mpc, meCCSG.getKey(), dtEOD, strCurrency)) bAllSPNSuccess = false;
		}

		return bAllSPNSuccess;
	}

	/**
	 * Saves the EOD CDS measures for a given curve and a EOD using the USD curve
	 * 
	 * @param mpc MarketParamContainer with all the closing credit curves
	 * @param stmt SQL Statement representing the executable query
	 * @param strSPN Credit curve ID string
	 * @param dtEOD EOD date
	 * 
	 * @return Success (true), failure (false)
	 */

	public static final boolean SaveSPNCalibMeasures (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final java.lang.String strSPN,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (!EODCurves.BuildCREOD (mpc, stmt, dtEOD, strSPN, "USD")) return false;

		long lStart = System.nanoTime();

		boolean bSPNSuccess = SaveSPNEOD (stmt, mpc, strSPN, dtEOD, "USD");

		System.out.println ("Calcs done in " + (System.nanoTime() - lStart) * 1.e-06 + " msec");

		return bSPNSuccess;
	}

	/**
	 * Saves the EOD measures corresponding to all the credit curves for a given EOD using the USD curve
	 * 
	 * @param stmt SQL Statement representing the executable query
	 * @param dtEOD EOD date
	 * 
	 * @return Success (true), failure (false)
	 */

	public static final boolean SaveCreditCalibMeasures (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		int iNumCurves = 0;

		System.out.println ("\nStart the loading for " + dtEOD.toString() + " ...");

		long lStart = System.nanoTime();

		org.drip.param.definition.MarketParams mpc =
			org.drip.param.creator.MarketParamsBuilder.CreateMarketParams();

		if (!RatesManager.LoadFullIRCurves (mpc, stmt, dtEOD)) return false;

		if (!LoadFullCreditCurves (mpc, stmt, dtEOD)) return false;

		java.util.Set<java.lang.String> setCC = GetCreditCurves (stmt, dtEOD);

		if (null == setCC || 0 == setCC.size()) return false;

		for (java.lang.String strCredit : setCC) {
			SaveSPNEOD (stmt, mpc, strCredit, dtEOD, "USD");

			if (0 == (++iNumCurves % 100))
				System.out.println ("\n" + iNumCurves + " curves done in " + (System.nanoTime() - lStart) *
					1.e-09 + " sec\n");
		}

		System.out.println ("\nLoading " + iNumCurves + " curves for " + dtEOD.toString() + " took " +
			((System.nanoTime() - lStart) * 1.e-09 / 60.) + " min\n");

		return false;
	}

	/**
	 * Saves the EOD measures corresponding to all the credit curves between a pair of EODs using the USD
	 *  curve
	 * 
	 * @param stmt SQL Statement representing the executable query
	 * @param dtEODStart Start EOD
	 * @param dtEODFinish Finish EOD
	 * 
	 * @return Success (true), failure (false)
	 * 
	 * @throws java.lang.Exception Thrown on invalid inputs
	 */

	public static final boolean CalcAndLoadCDSClosingMeasures (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEODStart,
		final org.drip.analytics.date.JulianDate dtEODFinish)
		throws java.lang.Exception
	{
		java.util.List<org.drip.analytics.date.JulianDate> lsEOD = new
			java.util.ArrayList<org.drip.analytics.date.JulianDate>();

		try {
			java.sql.ResultSet rsEOD = stmt.executeQuery
				("select distinct EOD from CR_EOD where EOD >= '" + dtEODStart.toOracleDate() +
					"' and EOD <= '" + dtEODFinish.toOracleDate() + "' order by EOD asc");

			while (rsEOD.next())
				lsEOD.add (org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rsEOD.getDate
					("EOD")));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		if (0 == lsEOD.size()) return false;

		for (org.drip.analytics.date.JulianDate dtEOD : lsEOD)
			SaveCreditCalibMeasures (stmt, dtEOD);

		return true;
	}

	/**
	 * Load the complete set of credit curves for a given EOD
	 * 
	 * @param mpc org.drip.param.definition.MarketParams containing the discount/credit curve for the EOD
	 * @param stmt SQL Statement representing the executable query
	 * @param dtEOD EOD date
	 * 
	 * @return Success (true), failure (false)
	 */

	public static final boolean LoadFullCreditCurves (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD)
	{
		if (null == mpc || null == stmt || null == dtEOD) {
			System.out.println ("Invalid params into CDSManager.LoadFullCreditCurves!");

			return false;
		}

		int iNumCurvesCooked = 0;
		int iNumCurvesFailed = 0;

		java.util.Set<java.lang.String> setCreditCurves = GetCreditCurves (stmt, dtEOD);

		if (null == setCreditCurves || 0 == setCreditCurves.size()) return false;

		for (java.lang.String strCC : setCreditCurves) {
			if (s_bLoadStaticCurves) {
				if (!StaticBACurves.setCC (mpc, dtEOD, strCC, "USD", 0.01, 125., 0.4))
					++iNumCurvesFailed;
				else
					++iNumCurvesCooked;
			} else {
				if (!EODCurves.BuildCREOD (mpc, stmt, dtEOD, strCC, "USD"))
					++iNumCurvesFailed;
				else
					++iNumCurvesCooked;
			}
		}

		System.out.println ("Num cooked: " + iNumCurvesCooked + "; Num failed: " + iNumCurvesFailed);

		return true;
	}
}
