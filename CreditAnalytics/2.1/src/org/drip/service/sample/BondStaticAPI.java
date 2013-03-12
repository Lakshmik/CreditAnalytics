
package org.drip.service.sample;

/*
 * Credit Analytics API Imports
 */

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
 * Demo of the bond static API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondStaticAPI {
	/**
	 * Sample demonstrating the retrieval of the bond's static fields
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void DisplayBondStatic()
		throws Exception
	{
		String strBondISIN = "US001383CA43";

		System.out.println ("AccrualDC: " + CreditAnalytics.GetBondStringField (strBondISIN, "AccrualDC"));

		System.out.println ("BBG_ID: " + CreditAnalytics.GetBondStringField (strBondISIN, "BBG_ID"));

		System.out.println ("BBGParent: " + CreditAnalytics.GetBondStringField (strBondISIN, "BBGParent"));

		System.out.println ("BBGUniqueID: " + CreditAnalytics.GetBondStringField (strBondISIN, "BBGUniqueID"));

		System.out.println ("CalculationType: " + CreditAnalytics.GetBondStringField (strBondISIN, "CalculationType"));

		System.out.println ("CDRCountryCode: " + CreditAnalytics.GetBondStringField (strBondISIN, "CDRCountryCode"));

		System.out.println ("CDRSettleCode: " + CreditAnalytics.GetBondStringField (strBondISIN, "CDRSettleCode"));

		System.out.println ("CollateralType: " + CreditAnalytics.GetBondStringField (strBondISIN, "CollateralType"));

		System.out.println ("CountryOfDomicile: " + CreditAnalytics.GetBondStringField (strBondISIN, "CountryOfDomicile"));

		System.out.println ("CountryOfGuarantor: " + CreditAnalytics.GetBondStringField (strBondISIN, "CountryOfGuarantor"));

		System.out.println ("CountryOfIncorporation: " + CreditAnalytics.GetBondStringField (strBondISIN, "CountryOfIncorporation"));

		System.out.println ("CouponCurrency: " + CreditAnalytics.GetBondStringField (strBondISIN, "CouponCurrency"));

		System.out.println ("CouponDC: " + CreditAnalytics.GetBondStringField (strBondISIN, "CouponDC"));

		System.out.println ("CouponType: " + CreditAnalytics.GetBondStringField (strBondISIN, "CouponType"));

		System.out.println ("CreditCurve: " + CreditAnalytics.GetBondStringField (strBondISIN, "CreditCurve"));

		System.out.println ("CUSIP: " + CreditAnalytics.GetBondStringField (strBondISIN, "CUSIP"));

		System.out.println ("Description: " + CreditAnalytics.GetBondStringField (strBondISIN, "Description"));

		System.out.println ("EDSFCurve: " + CreditAnalytics.GetBondStringField (strBondISIN, "EDSFCurve"));

		System.out.println ("ExchangeCode: " + CreditAnalytics.GetBondStringField (strBondISIN, "ExchangeCode"));

		System.out.println ("Fitch: " + CreditAnalytics.GetBondStringField (strBondISIN, "Fitch"));

		System.out.println ("FloatCouponConvention: " + CreditAnalytics.GetBondStringField (strBondISIN, "FloatCouponConvention"));

		System.out.println ("IndustryGroup: " + CreditAnalytics.GetBondStringField (strBondISIN, "IndustryGroup"));

		System.out.println ("IndustrySector: " + CreditAnalytics.GetBondStringField (strBondISIN, "IndustrySector"));

		System.out.println ("IndustrySubgroup: " + CreditAnalytics.GetBondStringField (strBondISIN, "IndustrySubgroup"));

		System.out.println ("IRCurve: " + CreditAnalytics.GetBondStringField (strBondISIN, "IRCurve"));

		System.out.println ("ISIN: " + CreditAnalytics.GetBondStringField (strBondISIN, "ISIN"));

		System.out.println ("Issuer: " + CreditAnalytics.GetBondStringField (strBondISIN, "Issuer"));

		System.out.println ("IssuerCategory: " + CreditAnalytics.GetBondStringField (strBondISIN, "IssuerCategory"));

		System.out.println ("IssuerCountry: " + CreditAnalytics.GetBondStringField (strBondISIN, "IssuerCountry"));

		System.out.println ("IssuerCountryCode: " + CreditAnalytics.GetBondStringField (strBondISIN, "IssuerCountryCode"));

		System.out.println ("IssuerIndustry: " + CreditAnalytics.GetBondStringField (strBondISIN, "IssuerIndustry"));

		System.out.println ("LeadManager: " + CreditAnalytics.GetBondStringField (strBondISIN, "LeadManager"));

		System.out.println ("LongCompanyName: " + CreditAnalytics.GetBondStringField (strBondISIN, "LongCompanyName"));

		System.out.println ("MarketIssueType: " + CreditAnalytics.GetBondStringField (strBondISIN, "MarketIssueType"));

		System.out.println ("MaturityType: " + CreditAnalytics.GetBondStringField (strBondISIN, "MaturityType"));

		System.out.println ("Moody: " + CreditAnalytics.GetBondStringField (strBondISIN, "Moody"));

		System.out.println ("Name: " + CreditAnalytics.GetBondStringField (strBondISIN, "Name"));

		System.out.println ("RateIndex: " + CreditAnalytics.GetBondStringField (strBondISIN, "RateIndex"));

		System.out.println ("RedemptionCurrency: " + CreditAnalytics.GetBondStringField (strBondISIN, "RedemptionCurrency"));

		System.out.println ("SecurityType: " + CreditAnalytics.GetBondStringField (strBondISIN, "SecurityType"));

		System.out.println ("Series: " + CreditAnalytics.GetBondStringField (strBondISIN, "Series"));

		System.out.println ("SeniorSub: " + CreditAnalytics.GetBondStringField (strBondISIN, "SeniorSub"));

		System.out.println ("ShortName: " + CreditAnalytics.GetBondStringField (strBondISIN, "ShortName"));

		System.out.println ("SnP: " + CreditAnalytics.GetBondStringField (strBondISIN, "SnP"));

		System.out.println ("Ticker: " + CreditAnalytics.GetBondStringField (strBondISIN, "Ticker"));

		System.out.println ("TradeCurrency: " + CreditAnalytics.GetBondStringField (strBondISIN, "TradeCurrency"));

		System.out.println ("TreasuryCurve: " + CreditAnalytics.GetBondStringField (strBondISIN, "TreasuryCurve"));

		System.out.println ("IsBearer: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Bearer"));

		System.out.println ("IsCallable: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Callable"));

		System.out.println ("IsDefaulted: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Defaulted"));

		System.out.println ("IsExercised: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Exercised"));

		System.out.println ("IsFloater: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Floater"));

		System.out.println ("IsPrivatePlacement: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "PrivatePlacement"));

		System.out.println ("IsPerpetual: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Perpetual"));

		System.out.println ("IsPutable: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Putable"));

		System.out.println ("IsRegistered: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Registered"));

		System.out.println ("IsReverseConvertible: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "ReverseConvertible"));

		System.out.println ("IsSinkable: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "Sinkable"));

		System.out.println ("IsStructuredNote: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "StructuredNote"));

		System.out.println ("IsTradeStatus: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "TradeStatus"));

		System.out.println ("IsUnitTraded: " + CreditAnalytics.GetBondBooleanField (strBondISIN, "UnitTraded"));

		if (!CreditAnalytics.GetBondBooleanField (strBondISIN, "Floater"))
			System.out.println ("Coupon: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "Coupon"));

		System.out.println ("CurrentCoupon: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "CurrentCoupon"));

		System.out.println ("FloatSpread: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "FloatSpread"));

		System.out.println ("IssueAmount: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "IssueAmount"));

		System.out.println ("IssuePrice: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "IssuePrice"));

		System.out.println ("MinimumIncrement: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "MinimumIncrement"));

		System.out.println ("MinimumPiece: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "MinimumPiece"));

		System.out.println ("OutstandingAmount: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "OutstandingAmount"));

		System.out.println ("ParAmount: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "ParAmount"));

		System.out.println ("RedemptionValue: " + CreditAnalytics.GetBondDoubleField (strBondISIN, "RedemptionValue"));

		System.out.println ("CouponFrequency: " + CreditAnalytics.GetBondIntegerField (strBondISIN, "Frequency"));

		System.out.println ("AccrualStartDate: " + CreditAnalytics.GetBondDateField (strBondISIN, "AccrualStartDate"));

		System.out.println ("AnnounceDate: " + CreditAnalytics.GetBondDateField (strBondISIN, "AnnounceDate"));

		System.out.println ("FinalMaturity: " + CreditAnalytics.GetBondDateField (strBondISIN, "FinalMaturity"));

		System.out.println ("FirstCoupon: " + CreditAnalytics.GetBondDateField (strBondISIN, "FirstCouponDate"));

		System.out.println ("FirstSettle: " + CreditAnalytics.GetBondDateField (strBondISIN, "FirstSettleDate"));

		System.out.println ("Issue: " + CreditAnalytics.GetBondDateField (strBondISIN, "IssueDate"));

		System.out.println ("Maturity: " + CreditAnalytics.GetBondDateField (strBondISIN, "Maturity"));

		System.out.println ("NextCoupon: " + CreditAnalytics.GetBondDateField (strBondISIN, "NextCouponDate"));

		System.out.println ("PenultimateCoupon: " + CreditAnalytics.GetBondDateField (strBondISIN, "PenultimateCouponDate"));

		System.out.println ("PrevCoupon: " + CreditAnalytics.GetBondDateField (strBondISIN, "PreviousCouponDate"));
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		if (!CreditAnalytics.Init (strConfig)) {
			System.out.println ("Cannot fully init FI!");

			System.exit (304);
		}

		DisplayBondStatic();
	}
}
