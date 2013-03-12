
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
 *  Container that exposes the functionality to create the set of closing IR and credit curves for a given
 *   EOD.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class EODCurves {
	private static final boolean s_bBlog = false;
	private static final boolean s_bStatBlog = false;

	private static final int s_iIRCalibMode = 0;
	private static final int s_iCreditCalibMode = 0;
	private static final boolean s_bUpdateBareTSYCQNames = true;

	private static final org.drip.product.definition.CalibratableComponent CreateIRComp (
		final org.drip.analytics.date.JulianDate dtEffective,
		final java.lang.String strTenor,
		final java.lang.String strCurrency,
		final java.lang.String strInstrCode,
		final boolean bTSY)
	{
		if (null == dtEffective || null == strTenor || strTenor.isEmpty() || null == strCurrency ||
			strCurrency.isEmpty() || null == strInstrCode || strInstrCode.isEmpty())
			return null;

		try {
			if (bTSY)
				return org.drip.product.creator.CashBuilder.CreateCash (dtEffective.addDays (2), strTenor,
					strCurrency, "TSY");

			if ("M".equalsIgnoreCase (strInstrCode))
				return org.drip.product.creator.CashBuilder.CreateCash (dtEffective.addDays (2), strTenor,
					strCurrency);

			if ("S".equalsIgnoreCase (strInstrCode))
				return org.drip.product.creator.IRSBuilder.CreateIRS (dtEffective.addDays (2), strTenor, 0.,
					strCurrency, strCurrency + "-LIBOR-6M", strCurrency);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final boolean AddCalibComp (
		final java.util.List<java.lang.Double> alCalibValue,
		final java.util.List<org.drip.product.definition.CalibratableComponent> alCalibComp,
		final java.sql.ResultSet rsCurvePoints,
		final java.lang.String strCurveID,
		final java.lang.String strInstrCode,
		final java.lang.String strCurrency,
		final org.drip.analytics.date.JulianDate dtEOD,
		final boolean bTSY)
	{
		if (null == rsCurvePoints || null == alCalibValue || null == alCalibComp || null == strCurveID ||
			strCurveID.isEmpty() || null == strCurrency || strCurrency.isEmpty() || null == dtEOD) {
			if (s_bBlog) System.out.println ("Bad inputs into EODCurves.AddCalibComp!");

			return false;
		}

		java.lang.String strCPType = null;

		try {
			strCPType = rsCurvePoints.getString ("IR" + strCurveID + "_TYPE");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		if (null == strCPType || strCPType.isEmpty()) {
			if (s_bBlog)
				System.out.println ("Valid CP does not exist for " + strCurrency + "/" + strCurveID + "/" +
					strInstrCode);

			return false;
		}

		if (null != strInstrCode && !strInstrCode.isEmpty() && !strCPType.equalsIgnoreCase (strInstrCode)) {
			if (s_bBlog) 
				System.out.println ("No instr for " + strCurrency + "/" + strCurveID + "/" + strInstrCode);

			return false;
		}

		double dblCP = java.lang.Double.NaN;

		try {
			dblCP = rsCurvePoints.getDouble ("IR" + strCurveID);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		if (java.lang.Double.isNaN (dblCP)) {
			if (s_bBlog)
				System.out.println ("NaN curve points for " + strCurrency + "/" + strCurveID + "/" +
					strInstrCode);

			return false;
		}

		org.drip.product.definition.CalibratableComponent calibComp = CreateIRComp (dtEOD, strCurveID,
			strCurrency, strCPType, bTSY);

		if (null == calibComp) {
			if (s_bBlog) 
				System.out.println ("Can't make calib comp for " + strCurrency + "/" + strCurveID + "/" +
					strInstrCode);

			return false;
		}

		alCalibValue.add (dblCP);

		alCalibComp.add (calibComp);

		return true;
	}

	private static final boolean AddTSYQuoteToMPC (
		final org.drip.param.definition.MarketParams mpc,
		final java.lang.String strTsyBmk,
		final double dblTSYQuote)
	{
		if (null == mpc) return false;

		org.drip.param.definition.ComponentQuote cqTSY =
			org.drip.param.creator.ComponentQuoteBuilder.CreateComponentQuote();

		try {
			cqTSY.addQuote ("Yield", org.drip.param.creator.QuoteBuilder.CreateQuote ("mid", dblTSYQuote),
				true);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		mpc.addTSYQuote (strTsyBmk, cqTSY);

		return true;
	}

	private static final boolean AddTSYCQToMap (
		final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mapTSYCQ,
		final java.lang.String strTsyBmk,
		final double dblTSYQuote)
	{
		if (null == mapTSYCQ || java.lang.Double.isNaN (dblTSYQuote)) return false;

		org.drip.param.definition.ComponentQuote cqTSY =
			org.drip.param.creator.ComponentQuoteBuilder.CreateComponentQuote();

		try {
			cqTSY.addQuote ("Yield", org.drip.param.creator.QuoteBuilder.CreateQuote ("mid", dblTSYQuote),
				true);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		mapTSYCQ.put (strTsyBmk, cqTSY);

		return true;
	}

	/**
	 * Builds the credit curve's CreditScenarioCurve for the given EOD and currency from the
	 *  corresponding marks
	 * 
	 * @param stmt SQL Statement representing executable query
	 * @param dtEOD EOD Date
	 * @param dc Discount Curve
	 * @param strSPN Credit Curve ID string
	 * @param strCurrency Discount Curve currency
	 * 
	 * @return The CreditScenarioCurve object
	 */

	public static final org.drip.param.definition.CreditScenarioCurve BuildEODCreditCurve (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final org.drip.analytics.definition.DiscountCurve dc,
		final java.lang.String strSPN,
		final java.lang.String strCurrency)
	{
		if (null == dc || null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty() ||
			null == strSPN || strSPN.isEmpty())
			return null;

		double[] adblQuotes = new double[20];
		java.sql.ResultSet rsCDSPoints = null;
		double dblRecovery = java.lang.Double.NaN;
		java.lang.String[] astrCalibMeasure = new java.lang.String[20];
		org.drip.param.definition.CreditScenarioCurve ccsc = null;
		org.drip.product.definition.CalibratableComponent[] aCDS = new
			org.drip.product.definition.CreditDefaultSwap[20];

		try {
			rsCDSPoints = stmt.executeQuery ("select * from CR_EOD where EOD = '" + dtEOD.toOracleDate() +
				"' and SPN = '" + strSPN + "'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == rsCDSPoints) return null;

		try {
			while (rsCDSPoints.next()) {
				org.drip.analytics.date.JulianDate dtEffective = dtEOD.getFirstEDFStartDate (3);

				dblRecovery = rsCDSPoints.getDouble ("RecRate");

				astrCalibMeasure[0] = "FairPremium";
				astrCalibMeasure[1] = "FairPremium";
				astrCalibMeasure[2] = "FairPremium";
				astrCalibMeasure[3] = "FairPremium";
				astrCalibMeasure[4] = "FairPremium";
				astrCalibMeasure[5] = "FairPremium";
				astrCalibMeasure[6] = "FairPremium";
				astrCalibMeasure[7] = "FairPremium";
				astrCalibMeasure[8] = "FairPremium";
				astrCalibMeasure[9] = "FairPremium";
				astrCalibMeasure[10] = "FairPremium";
				astrCalibMeasure[11] = "FairPremium";
				astrCalibMeasure[12] = "FairPremium";
				astrCalibMeasure[13] = "FairPremium";
				astrCalibMeasure[14] = "FairPremium";
				astrCalibMeasure[15] = "FairPremium";
				astrCalibMeasure[16] = "FairPremium";
				astrCalibMeasure[17] = "FairPremium";
				astrCalibMeasure[18] = "FairPremium";
				astrCalibMeasure[19] = "FairPremium";

				adblQuotes[0] = rsCDSPoints.getDouble ("CR3M");

				adblQuotes[1] = rsCDSPoints.getDouble ("CR6M");

				adblQuotes[2] = rsCDSPoints.getDouble ("CR9M");

				adblQuotes[3] = rsCDSPoints.getDouble ("CR1Y");

				adblQuotes[4] = rsCDSPoints.getDouble ("CR18M");

				adblQuotes[5] = rsCDSPoints.getDouble ("CR2Y");

				adblQuotes[6] = rsCDSPoints.getDouble ("CR3Y");

				adblQuotes[7] = rsCDSPoints.getDouble ("CR4Y");

				adblQuotes[8] = rsCDSPoints.getDouble ("CR5Y");

				adblQuotes[9] = rsCDSPoints.getDouble ("CR6Y");

				adblQuotes[10] = rsCDSPoints.getDouble ("CR7Y");

				adblQuotes[11] = rsCDSPoints.getDouble ("CR8Y");

				adblQuotes[12] = rsCDSPoints.getDouble ("CR9Y");

				adblQuotes[13] = rsCDSPoints.getDouble ("CR10Y");

				adblQuotes[14] = rsCDSPoints.getDouble ("CR11Y");

				adblQuotes[15] = rsCDSPoints.getDouble ("CR12Y");

				adblQuotes[16] = rsCDSPoints.getDouble ("CR15Y");

				adblQuotes[17] = rsCDSPoints.getDouble ("CR20Y");

				adblQuotes[18] = rsCDSPoints.getDouble ("CR30Y");

				adblQuotes[19] = rsCDSPoints.getDouble ("CR40Y");

				aCDS[0] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "3M", 0.1, strSPN);

				aCDS[1] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "6M", 0.1, strSPN);

				aCDS[2] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "9M", 0.1, strSPN);

				aCDS[3] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "1Y", 0.1, strSPN);

				aCDS[4] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "18M", 0.1, strSPN);

				aCDS[5] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "2Y", 0.1, strSPN);

				aCDS[6] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "3Y", 0.1, strSPN);

				aCDS[7] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "4Y", 0.1, strSPN);

				aCDS[8] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "5Y", 0.1, strSPN);

				aCDS[9] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "6Y", 0.1, strSPN);

				aCDS[10] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "7Y", 0.1, strSPN);

				aCDS[11] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "8Y", 0.1, strSPN);

				aCDS[12] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "9Y", 0.1, strSPN);

				aCDS[13] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "10Y", 0.1, strSPN);

				aCDS[14] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "11Y", 0.1, strSPN);

				aCDS[15] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "12Y", 0.1, strSPN);

				aCDS[16] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "15Y", 0.1, strSPN);

				aCDS[17] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "20Y", 0.1, strSPN);

				aCDS[18] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "30Y", 0.1, strSPN);

				aCDS[19] = org.drip.product.creator.CDSBuilder.CreateSNAC (dtEffective, "40Y", 0.1, strSPN);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		try {
			if (!(ccsc = org.drip.param.creator.CreditScenarioCurveBuilder.CreateCCSC (aCDS)).cookScenarioCC
				(strSPN, new org.drip.param.valuation.ValuationParams (dtEOD, dtEOD.addBusDays (3,
					strCurrency), "USD"), dc, null, null, adblQuotes, dblRecovery, astrCalibMeasure, null,
						null, false, s_iCreditCalibMode)) {
				System.out.println ("CC[" + strSPN + "] failed to cook");

				return null;
			}

			System.out.println ("CC[" + strSPN + "] cooked successfully");
		} catch (java.lang.Exception e) {
			java.lang.StringBuilder sb = new java.lang.StringBuilder();

			sb.append ("Recovery=").append (dblRecovery).append (";");

			for (int i = 0; i < aCDS.length; ++i)
				sb.append (" " + aCDS[i].getMaturityDate().toString()).append ("=").append
					(adblQuotes[i]).append (";");

			System.out.println (strSPN + " failed: " + sb.toString());

			System.out.println (e.getMessage());

			return null;
		}

		return ccsc;
	}

	/**
	 * Adds the TSY quotes to the specified MPC
	 * 
	 * @param mpc MPC
	 * @param stmt SQL statement object to retrieve the MPC quotes from
	 * @param dtEOD EOD
	 * @param strCurrency Currency
	 * 
	 * @return true if TSY quotes successfully retrieved and added
	 */

	public static final boolean AddTSYQuotesToMPC (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency)
	{
		if (null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty()) {
			System.out.println ("Bad inputs into EODCurves.AddTSYQuotesToMPC!");

			return false;
		}

		java.sql.ResultSet rsCurvePoints = null;

		try {
			rsCurvePoints = stmt.executeQuery ("select * from IR_EOD where EOD = '" + dtEOD.toOracleDate() +
				"' and Currency = '" + strCurrency + "' and Type = 'government'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		if (null == rsCurvePoints) return false;

		try {
			while (rsCurvePoints.next()) {
				if (!AddTSYQuoteToMPC (mpc, strCurrency + "1DON", rsCurvePoints.getDouble ("IR1D")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "1MON", rsCurvePoints.getDouble ("IR1M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "2MON", rsCurvePoints.getDouble ("IR2M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "3MON", rsCurvePoints.getDouble ("IR3M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "4MON", rsCurvePoints.getDouble ("IR4M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "5MON", rsCurvePoints.getDouble ("IR5M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "6MON", rsCurvePoints.getDouble ("IR6M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "9MON", rsCurvePoints.getDouble ("IR9M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "1YON", rsCurvePoints.getDouble ("IR1Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "18MON", rsCurvePoints.getDouble ("IR18M")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "2YON", rsCurvePoints.getDouble ("IR2Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "3YON", rsCurvePoints.getDouble ("IR3Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "4YON", rsCurvePoints.getDouble ("IR4Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "5YON", rsCurvePoints.getDouble ("IR5Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "6YON", rsCurvePoints.getDouble ("IR6Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "7YON", rsCurvePoints.getDouble ("IR7Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "8YON", rsCurvePoints.getDouble ("IR8Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "9YON", rsCurvePoints.getDouble ("IR9Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "10YON", rsCurvePoints.getDouble ("IR10Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "12YON", rsCurvePoints.getDouble ("IR12Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "15YON", rsCurvePoints.getDouble ("IR15Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "20YON", rsCurvePoints.getDouble ("IR20Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "25YON", rsCurvePoints.getDouble ("IR25Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "30YON", rsCurvePoints.getDouble ("IR30Y")))
					return false;

				if (!AddTSYQuoteToMPC (mpc, strCurrency + "40YON", rsCurvePoints.getDouble ("IR40Y")))
					return false;

				return true;
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Retrieves the treasury quotes for the specified EOD and currency
	 * 
	 * @param stmt SQL Statement
	 * @param dtEOD EOD
	 * @param strCurrency Currency
	 * 
	 * @return Map of the treasury component quotes
	 */

	public static final java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote>
		GetTSYQuotes (
			final java.sql.Statement stmt,
			final org.drip.analytics.date.JulianDate dtEOD,
			final java.lang.String strCurrency)
	{
		if (null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty()) {
			System.out.println ("Bad inputs into EODCurves.GetTSYQuotes!");

			return null;
		}

		java.sql.ResultSet rsCurvePoints = null;

		try {
			rsCurvePoints = stmt.executeQuery ("select * from IR_EOD where EOD = '" + dtEOD.toOracleDate() +
				"' and Currency = '" + strCurrency + "' and Type = 'government'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == rsCurvePoints) return null;

		java.util.Map<java.lang.String, org.drip.param.definition.ComponentQuote> mapTSYCQ = new
			java.util.TreeMap<java.lang.String, org.drip.param.definition.ComponentQuote>();

		try {
			while (rsCurvePoints.next()) {
				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "1DON", 0.01 * rsCurvePoints.getDouble ("IR1D")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "1MON", 0.01 * rsCurvePoints.getDouble ("IR1M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "2MON", 0.01 * rsCurvePoints.getDouble ("IR2M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "3MON", 0.01 * rsCurvePoints.getDouble ("IR3M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "4MON", 0.01 * rsCurvePoints.getDouble ("IR4M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "5MON", 0.01 * rsCurvePoints.getDouble ("IR5M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "6MON", 0.01 * rsCurvePoints.getDouble ("IR6M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "9MON", 0.01 * rsCurvePoints.getDouble ("IR9M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "1YON", 0.01 * rsCurvePoints.getDouble ("IR1Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "18MON", 0.01 * rsCurvePoints.getDouble
					("IR18M")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "2YON", 0.01 * rsCurvePoints.getDouble ("IR2Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "3YON", 0.01 * rsCurvePoints.getDouble ("IR3Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "4YON", 0.01 * rsCurvePoints.getDouble ("IR4Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "5YON", 0.01 * rsCurvePoints.getDouble ("IR5Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "6YON", 0.01 * rsCurvePoints.getDouble ("IR6Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "7YON", 0.01 * rsCurvePoints.getDouble ("IR7Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "8YON", 0.01 * rsCurvePoints.getDouble ("IR8Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "9YON", 0.01 * rsCurvePoints.getDouble ("IR9Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "10YON", 0.01 * rsCurvePoints.getDouble
					("IR10Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "12YON", 0.01 * rsCurvePoints.getDouble
					("IR12Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "15YON", 0.01 * rsCurvePoints.getDouble
					("IR15Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "20YON", 0.01 * rsCurvePoints.getDouble
					("IR20Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "25YON", 0.01 * rsCurvePoints.getDouble
					("IR25Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "30YON", 0.01 * rsCurvePoints.getDouble
					("IR30Y")))
					return null;

				if (!AddTSYCQToMap (mapTSYCQ, strCurrency + "40YON", 0.01 * rsCurvePoints.getDouble
					("IR40Y")))
					return null;

				if (s_bUpdateBareTSYCQNames) {
					if (!AddTSYCQToMap (mapTSYCQ, "1DON", rsCurvePoints.getDouble ("IR1D"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "1MON", rsCurvePoints.getDouble ("IR1M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "2MON", rsCurvePoints.getDouble ("IR2M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "3MON", rsCurvePoints.getDouble ("IR3M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "4MON", rsCurvePoints.getDouble ("IR4M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "5MON", rsCurvePoints.getDouble ("IR5M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "6MON", rsCurvePoints.getDouble ("IR6M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "9MON", rsCurvePoints.getDouble ("IR9M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "1YON", rsCurvePoints.getDouble ("IR1Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "18MON", rsCurvePoints.getDouble ("IR18M"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "2YON", rsCurvePoints.getDouble ("IR2Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "3YON", rsCurvePoints.getDouble ("IR3Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "4YON", rsCurvePoints.getDouble ("IR4Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "5YON", rsCurvePoints.getDouble ("IR5Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "6YON", rsCurvePoints.getDouble ("IR6Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "7YON", rsCurvePoints.getDouble ("IR7Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "8YON", rsCurvePoints.getDouble ("IR8Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "9YON", rsCurvePoints.getDouble ("IR9Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "10YON", rsCurvePoints.getDouble ("IR10Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "12YON", rsCurvePoints.getDouble ("IR12Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "15YON", rsCurvePoints.getDouble ("IR15Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "20YON", rsCurvePoints.getDouble ("IR20Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "25YON", rsCurvePoints.getDouble ("IR25Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "30YON", rsCurvePoints.getDouble ("IR30Y"))) return null;

					if (!AddTSYCQToMap (mapTSYCQ, "40YON", rsCurvePoints.getDouble ("IR40Y"))) return null;
				}

				if (s_bBlog) {
					for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.ComponentQuote>
						meTSYCQ : mapTSYCQ.entrySet()) {
						if (null != meTSYCQ)
							System.out.println (meTSYCQ.getKey() + "=" + meTSYCQ.getValue().getQuote
								("Yield").getQuote ("mid"));
					}
				}

				return mapTSYCQ;
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Builds the closing IRCurveScenarioContainer for the specific discount curve instruments given the EOD
	 *  and the currency
	 *  
	 * @param mmFixings The fixings object
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * @param strInstrCode String representing the specific instrument code (cash/EDF/IRS)
	 * @param strInstrSetType String representing the instrument set (treasury/rates)
	 * @param strCurveName String representing the curve name
	 * 
	 * @return The IRCurveScenarioContainer object
	 */

	public static final org.drip.param.definition.RatesScenarioCurve BuildEODIRCurveOfCode (
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency,
		final java.lang.String strInstrCode,
		final java.lang.String strInstrSetType,
		final java.lang.String strCurveName)
	{
		if (null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty() || null ==
			strInstrCode || strInstrCode.isEmpty()  || null == strInstrSetType || strInstrSetType.isEmpty()
				|| null == strCurveName || strCurveName.isEmpty()) {
			System.out.println ("Bad inputs into EODCurves.BuildEODIRCurveOfCode!");

			return null;
		}

		int i = 0;
		java.sql.ResultSet rsCurvePoints = null;
		org.drip.param.definition.RatesScenarioCurve ircsc = null;

		boolean bTSY = strInstrSetType.equalsIgnoreCase ("government");

		java.util.List<java.lang.Double> alCalibValue = new java.util.ArrayList<java.lang.Double>();

		java.util.List<org.drip.product.definition.CalibratableComponent> alCalibComp = new
			java.util.ArrayList<org.drip.product.definition.CalibratableComponent>();

		try {
			rsCurvePoints = stmt.executeQuery ("select * from IR_EOD where EOD = '" + dtEOD.toOracleDate() +
				"' and Currency = '" + strCurrency + "' and Type = '" + strInstrSetType + "'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == rsCurvePoints) return null;

		try {
			while (rsCurvePoints.next()) {
				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1D", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "2M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "3M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "4M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "5M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "6M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "9M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "18M", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "2Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "3Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "4Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "5Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "6Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "7Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "8Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "9Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "10Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "12Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "15Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "20Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "25Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "30Y", strInstrCode, strCurrency,
					dtEOD, bTSY);

				AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "40Y", strInstrCode, strCurrency,
					dtEOD, bTSY);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (0 == alCalibValue.size()) return null;

		double[] adblCompCalibValue = new double[alCalibValue.size()];

		java.lang.String[] astrCalibMeasure = new java.lang.String[alCalibValue.size()];

		org.drip.product.definition.CalibratableComponent[] aCompCalib = new
			org.drip.product.definition.CalibratableComponent[alCalibValue.size()];

		for (double dblCompCalibValue : alCalibValue) {
			astrCalibMeasure[i] = "Rate";
			adblCompCalibValue[i++] = dblCompCalibValue;
		}

		i = 0;

		for (org.drip.product.definition.CalibratableComponent comp : alCalibComp)
			aCompCalib[i++] = comp;

		try {
			(ircsc = org.drip.param.creator.RatesScenarioCurveBuilder.FromIRCSG (strCurveName,
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
					aCompCalib)).cookScenarioDC (new org.drip.param.valuation.ValuationParams (dtEOD,
						dtEOD.addBusDays (3, strCurrency), "USD"), null, null, adblCompCalibValue, 0.0001,
							astrCalibMeasure, mmFixings, null, s_iIRCalibMode);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return ircsc;
	}

	/**
	 * Builds the closing IRCurveScenarioContainer for the specific discount curve instrument set type
	 * 	(treasury or rates instruments), given the EOD and the currency
	 *  
	 * @param mmFixings The fixings object
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * @param strInstrSetType String representing the instrument set (treasury/rates)
	 * @param strCurveName String representing the curve name
	 * 
	 * @return The IRCurveScenarioContainer object
	 */

	public static final org.drip.param.definition.RatesScenarioCurve BuildEODIRCurve (
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency,
		final java.lang.String strInstrSetType,
		final java.lang.String strCurveName)
	{
		if (null == mmFixings || null == stmt || null == dtEOD || null == strCurrency ||
			strCurrency.isEmpty() || null == strInstrSetType || strInstrSetType.isEmpty() || null ==
				strCurveName || strCurveName.isEmpty())
			return null;

		int i = 0;
		java.sql.ResultSet rsCurvePoints = null;
		org.drip.param.definition.RatesScenarioCurve ircsc = null;

		boolean bTSY = strInstrSetType.equalsIgnoreCase ("government");

		java.util.List<java.lang.Double> alCalibValue = new java.util.ArrayList<java.lang.Double>();

		java.util.List<org.drip.product.definition.CalibratableComponent> alCalibComp = new
			java.util.ArrayList<org.drip.product.definition.CalibratableComponent>();

		try {
			rsCurvePoints = stmt.executeQuery ("select * from IR_EOD where EOD = '" + dtEOD.toOracleDate() +
				"' and Currency = '" + strCurrency + "' and Type = '" + strInstrSetType + "'");
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == rsCurvePoints) return null;

		try {
			while (rsCurvePoints.next()) {
				java.lang.String[] astrCalibMeasure = new java.lang.String[25];
				astrCalibMeasure[0] = "Rate";
				astrCalibMeasure[1] = "Rate";
				astrCalibMeasure[2] = "Rate";
				astrCalibMeasure[3] = "Rate";
				astrCalibMeasure[4] = "Rate";
				astrCalibMeasure[5] = "Rate";
				astrCalibMeasure[6] = "Rate";
				astrCalibMeasure[7] = "Rate";
				astrCalibMeasure[8] = "Rate";
				astrCalibMeasure[9] = "Rate";
				astrCalibMeasure[10] = "Rate";
				astrCalibMeasure[11] = "Rate";
				astrCalibMeasure[12] = "Rate";
				astrCalibMeasure[13] = "Rate";
				astrCalibMeasure[14] = "Rate";
				astrCalibMeasure[15] = "Rate";
				astrCalibMeasure[16] = "Rate";
				astrCalibMeasure[17] = "Rate";
				astrCalibMeasure[18] = "Rate";
				astrCalibMeasure[19] = "Rate";
				astrCalibMeasure[20] = "Rate";
				astrCalibMeasure[21] = "Rate";
				astrCalibMeasure[22] = "Rate";
				astrCalibMeasure[23] = "Rate";
				astrCalibMeasure[24] = "Rate";

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1D", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "2M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "3M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "4M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "5M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "6M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "9M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "1Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "18M", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "2Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "3Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "4Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "5Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "6Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "7Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "8Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "9Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "10Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "12Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "15Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "20Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "25Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "30Y", null, strCurrency, dtEOD,
					bTSY))
					return null;

				if (!AddCalibComp (alCalibValue, alCalibComp, rsCurvePoints, "40Y", null, strCurrency, dtEOD,
					bTSY))
					return null;
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (0 == alCalibValue.size()) return null;

		double[] adblCompCalibValue = new double[alCalibValue.size()];

		java.lang.String[] astrCalibMeasure = new java.lang.String[alCalibValue.size()];

		org.drip.product.definition.CalibratableComponent[] aCompCalib = new
			org.drip.product.definition.CalibratableComponent[alCalibValue.size()];

		for (double dblCompCalibValue : alCalibValue) {
			astrCalibMeasure[i] = "Rate";
			adblCompCalibValue[i++] = dblCompCalibValue;
		}

		i = 0;

		for (org.drip.product.definition.CalibratableComponent comp : alCalibComp)
			aCompCalib[i++] = comp;

		try {
			(ircsc = org.drip.param.creator.RatesScenarioCurveBuilder.FromIRCSG (strCurveName,
				org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
					aCompCalib)).cookScenarioDC (new org.drip.param.valuation.ValuationParams (dtEOD,
						dtEOD.addBusDays (3, strCurrency), "USD"), null, null, adblCompCalibValue, 0.0001,
							astrCalibMeasure, mmFixings, null, s_iIRCalibMode);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return ircsc;
	}

	/**
	 * Creates the named base IR curve based on the set of instruments and their types for a given EOD
	 * 
	 * @param stmt SQL Statement representing the executable query
	 * @param dtEOD EOD Date
	 * @param strCurrency String representing the currency
	 * @param strInstrType Instrument type string (G for treasury and S for rates)
	 * @param strCurveName Name of the discount curve
	 * 
	 * @return The discount curve
	 */

	public static final org.drip.analytics.definition.DiscountCurve LoadEODIR (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency,
		final java.lang.String strInstrType,
		final java.lang.String strCurveName)
	{
		if (null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty() || null ==
			strInstrType || strInstrType.isEmpty() || null == strCurveName || strCurveName.isEmpty())
			return null;

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put (strCurrency + "-LIBOR-6M", 0.0042);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (dtEOD.addDays (2), mIndexFixings);

		org.drip.param.definition.RatesScenarioCurve ircsg = BuildEODIRCurve (mmFixings, stmt, dtEOD,
			strCurrency, strInstrType, strCurveName);

		if (null == ircsg || null == ircsg.getDCBase()) return null;

		return ircsg.getDCBase();
	}

	/**
	 * Builds the closing IRCurveScenarioContainer for the specific discount curve instrument set type
	 * 	(treasury or rates instruments), the given EOD, and the currency, and loads it to the input MPC
	 * 
	 * @param mpc org.drip.param.definition.MarketParams to be loaded into
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * @param strInstrType String representing the instrument set (treasury/rates)
	 * @param strCurveName String representing the curve name
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean LoadEODIRToMPC (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency,
		final java.lang.String strInstrType,
		final java.lang.String strCurveName)
	{
		if (null == mpc || null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty() ||
			null == strInstrType || strInstrType.isEmpty() || null == strCurveName || strCurveName.isEmpty())
			return false;

		long lStart = System.nanoTime();

		org.drip.param.definition.RatesScenarioCurve ircsg = BuildEODIRCurve (mpc.getFixings(), stmt, dtEOD,
			strCurrency, strInstrType, strCurveName);

		if (null == ircsg) return false;

		if (!mpc.addScenDC (strCurveName, ircsg)) return false;

		System.out.println ("DC[" + strCurveName + "] Cooked in: " + (System.nanoTime() - lStart) * 1.e-09 +
			" sec");

		return true;
	}

	/**
	 * Builds the closing IRCurveScenarioContainer for the specific discount curve instrument set (cash or
	 *  EDF or swaps), the EOD, and the currency, and loads it to the input MPC
	 * 
	 * @param mpc org.drip.param.definition.MarketParams to be loaded into
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * @param strInstrCode String representing the instrument set (cash/EDF/swaps)
	 * @param strInstrType String representing the instrument set (treasury/rates)
	 * @param strCurveName String representing the curve name
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean LoadEODIROfCodeToMPC (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency,
		final java.lang.String strInstrCode,
		final java.lang.String strInstrType,
		final java.lang.String strCurveName)
	{
		if (null == mpc || null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty() ||
			null == strInstrCode || strInstrCode.isEmpty() || null == strCurveName || strCurveName.isEmpty()
				|| null == strInstrCode || strInstrCode.isEmpty()) {
			if (s_bBlog) System.out.println ("Bad inputs into EODCurves.LoadEODIROfCodeToMPC!");

			return false;
		}

		long lStart = System.nanoTime();

		org.drip.param.definition.RatesScenarioCurve ircsg = BuildEODIRCurveOfCode (mpc.getFixings(), stmt,
			dtEOD, strCurrency, strInstrCode, strInstrType, strCurveName);

		if (null == ircsg) {
			if (s_bBlog) System.out.println ("Cannot create IRCSG for " + strCurveName + "!");

			return false;
		}

		if (!mpc.addScenDC (strCurveName, ircsg)) {
			if (s_bBlog) System.out.println ("Cannot cook " + strCurveName + "!");

			return false;
		}

		System.out.println ("DC[" + strCurveName + "] Cooked in: " + (System.nanoTime() - lStart) *
			1.e-09 + " sec");

		return true;
	}

	/**
	 * Builds the complete set of treasury EOD curves for the given currency, and loads them to the MPC
	 * 
	 * @param mpc org.drip.param.definition.MarketParams to be loaded into
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean BuildTSYEODCurve (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency)
	{
		if (null == mpc || null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty())
			return false;

		return LoadEODIRToMPC (mpc, stmt, dtEOD, strCurrency, "government", strCurrency + "TSY");
	}

	/**
	 * Builds the complete set of rates EOD curves for the given currency, and loads them to the MPC
	 * 
	 * @param mpc org.drip.param.definition.MarketParams to be loaded into
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strCurrency Currency string
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static final boolean BuildIREODCurve (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strCurrency)
	{
		if (null == mpc || null == stmt || null == dtEOD || null == strCurrency || strCurrency.isEmpty())
			return false;

		mpc.addFixings (dtEOD.addDays (2), strCurrency + "-LIBOR-6M", 0.0042);

		if (!LoadEODIRToMPC (mpc, stmt, dtEOD, strCurrency, "swap", strCurrency)) {
			if (s_bStatBlog) System.out.println ("Cannot load Full DC for " + strCurrency);

			return false;
		}

		if (!LoadEODIROfCodeToMPC (mpc, stmt, dtEOD, strCurrency, "M", "swap", strCurrency + "EDSF")) {
			if (s_bStatBlog) System.out.println ("Cannot load MM DC for " + strCurrency);

			return false;
		}

		if (!LoadEODIROfCodeToMPC (mpc, stmt, dtEOD, strCurrency, "S", "swap", strCurrency + "SWAP")) {
			if (s_bStatBlog) System.out.println ("Cannot load Swap DC for " + strCurrency);

			return false;
		}

		if (!AddTSYQuotesToMPC (mpc, stmt, dtEOD, strCurrency)) {
			if (s_bStatBlog) System.out.println ("Cannot add TSY quotes for " + strCurrency);

			return false;
		}

		return true;
	}

	/**
	 * Builds the EOD credit curve, and loads it to the MPC
	 * 
	 * @param mpc org.drip.param.definition.MarketParams to be loaded into
	 * @param stmt SQL Statement containing the executable query
	 * @param dtEOD EOD date
	 * @param strSPN Credit Curve ID string
	 * @param strCurrency Currency string
	 * 
	 * @return Success (true) or failure (false)
	 */

	public static boolean BuildCREOD (
		final org.drip.param.definition.MarketParams mpc,
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dtEOD,
		final java.lang.String strSPN,
		final java.lang.String strCurrency)
	{
		if (null == mpc || null == mpc.getIRSG() || null == stmt || null == dtEOD || null == strCurrency ||
			strCurrency.isEmpty() || null == strSPN || strSPN.isEmpty() || null == mpc.getIRSG().get
				(strCurrency) || null == mpc.getIRSG().get (strCurrency).getDCBase())
			return false;

		org.drip.param.definition.CreditScenarioCurve ccsg = BuildEODCreditCurve (stmt, dtEOD,
			mpc.getIRSG().get (strCurrency).getDCBase(), strSPN, strCurrency);

		if (null == ccsg) return false;

		if (!mpc.addScenCC (strSPN, ccsg)) return false;

		return true;
	}
}
