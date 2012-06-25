
package org.drip.service.sample;

import org.drip.service.api.FI;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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

/**
 * Demo of the bond static API Sample
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondStaticAPISample {
	/**
	 * Sample demonstrating the retrieval of the bond's static fields
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BondStaticAPI() throws Exception {
		String strBondISIN = "US001383CA43";

		System.out.println ("AccrualDC: " + FI.GetBondStringField (strBondISIN, "AccrualDC"));

		System.out.println ("BBG_ID: " + FI.GetBondStringField (strBondISIN, "BBG_ID"));

		System.out.println ("BBGParent: " + FI.GetBondStringField (strBondISIN, "BBGParent"));

		System.out.println ("BBGUniqueID: " + FI.GetBondStringField (strBondISIN, "BBGUniqueID"));

		System.out.println ("CalculationType: " + FI.GetBondStringField (strBondISIN, "CalculationType"));

		System.out.println ("CDRCountryCode: " + FI.GetBondStringField (strBondISIN, "CDRCountryCode"));

		System.out.println ("CDRSettleCode: " + FI.GetBondStringField (strBondISIN, "CDRSettleCode"));

		System.out.println ("CollateralType: " + FI.GetBondStringField (strBondISIN, "CollateralType"));

		System.out.println ("CountryOfDomicile: " + FI.GetBondStringField (strBondISIN, "CountryOfDomicile"));

		System.out.println ("CountryOfGuarantor: " + FI.GetBondStringField (strBondISIN, "CountryOfGuarantor"));

		System.out.println ("CountryOfIncorporation: " + FI.GetBondStringField (strBondISIN, "CountryOfIncorporation"));

		System.out.println ("CouponCurrency: " + FI.GetBondStringField (strBondISIN, "CouponCurrency"));

		System.out.println ("CouponDC: " + FI.GetBondStringField (strBondISIN, "CouponDC"));

		System.out.println ("CouponType: " + FI.GetBondStringField (strBondISIN, "CouponType"));

		System.out.println ("CreditCurve: " + FI.GetBondStringField (strBondISIN, "CreditCurve"));

		System.out.println ("CUSIP: " + FI.GetBondStringField (strBondISIN, "CUSIP"));

		System.out.println ("Description: " + FI.GetBondStringField (strBondISIN, "Description"));

		System.out.println ("EDSFCurve: " + FI.GetBondStringField (strBondISIN, "EDSFCurve"));

		System.out.println ("ExchangeCode: " + FI.GetBondStringField (strBondISIN, "ExchangeCode"));

		System.out.println ("Fitch: " + FI.GetBondStringField (strBondISIN, "Fitch"));

		System.out.println ("FloatCouponConvention: " + FI.GetBondStringField (strBondISIN, "FloatCouponConvention"));

		System.out.println ("IndustryGroup: " + FI.GetBondStringField (strBondISIN, "IndustryGroup"));

		System.out.println ("IndustrySector: " + FI.GetBondStringField (strBondISIN, "IndustrySector"));

		System.out.println ("IndustrySubgroup: " + FI.GetBondStringField (strBondISIN, "IndustrySubgroup"));

		System.out.println ("IRCurve: " + FI.GetBondStringField (strBondISIN, "IRCurve"));

		System.out.println ("ISIN: " + FI.GetBondStringField (strBondISIN, "ISIN"));

		System.out.println ("Issuer: " + FI.GetBondStringField (strBondISIN, "Issuer"));

		System.out.println ("IssuerCategory: " + FI.GetBondStringField (strBondISIN, "IssuerCategory"));

		System.out.println ("IssuerCountry: " + FI.GetBondStringField (strBondISIN, "IssuerCountry"));

		System.out.println ("IssuerCountryCode: " + FI.GetBondStringField (strBondISIN, "IssuerCountryCode"));

		System.out.println ("IssuerIndustry: " + FI.GetBondStringField (strBondISIN, "IssuerIndustry"));

		System.out.println ("LeadManager: " + FI.GetBondStringField (strBondISIN, "LeadManager"));

		System.out.println ("LongCompanyName: " + FI.GetBondStringField (strBondISIN, "LongCompanyName"));

		System.out.println ("MarketIssueType: " + FI.GetBondStringField (strBondISIN, "MarketIssueType"));

		System.out.println ("MaturityType: " + FI.GetBondStringField (strBondISIN, "MaturityType"));

		System.out.println ("Moody: " + FI.GetBondStringField (strBondISIN, "Moody"));

		System.out.println ("Name: " + FI.GetBondStringField (strBondISIN, "Name"));

		System.out.println ("RateIndex: " + FI.GetBondStringField (strBondISIN, "RateIndex"));

		System.out.println ("RedemptionCurrency: " + FI.GetBondStringField (strBondISIN, "RedemptionCurrency"));

		System.out.println ("SecurityType: " + FI.GetBondStringField (strBondISIN, "SecurityType"));

		System.out.println ("Series: " + FI.GetBondStringField (strBondISIN, "Series"));

		System.out.println ("SeniorSub: " + FI.GetBondStringField (strBondISIN, "SeniorSub"));

		System.out.println ("ShortName: " + FI.GetBondStringField (strBondISIN, "ShortName"));

		System.out.println ("SnP: " + FI.GetBondStringField (strBondISIN, "SnP"));

		System.out.println ("Ticker: " + FI.GetBondStringField (strBondISIN, "Ticker"));

		System.out.println ("TradeCurrency: " + FI.GetBondStringField (strBondISIN, "TradeCurrency"));

		System.out.println ("TreasuryCurve: " + FI.GetBondStringField (strBondISIN, "TreasuryCurve"));

		System.out.println ("IsBearer: " + FI.GetBondBooleanField (strBondISIN, "Bearer"));

		System.out.println ("IsCallable: " + FI.GetBondBooleanField (strBondISIN, "Callable"));

		System.out.println ("IsDefaulted: " + FI.GetBondBooleanField (strBondISIN, "Defaulted"));

		System.out.println ("IsExercised: " + FI.GetBondBooleanField (strBondISIN, "Exercised"));

		System.out.println ("IsFloater: " + FI.GetBondBooleanField (strBondISIN, "Floater"));

		System.out.println ("IsPrivatePlacement: " + FI.GetBondBooleanField (strBondISIN, "PrivatePlacement"));

		System.out.println ("IsPerpetual: " + FI.GetBondBooleanField (strBondISIN, "Perpetual"));

		System.out.println ("IsPutable: " + FI.GetBondBooleanField (strBondISIN, "Putable"));

		System.out.println ("IsRegistered: " + FI.GetBondBooleanField (strBondISIN, "Registered"));

		System.out.println ("IsReverseConvertible: " + FI.GetBondBooleanField (strBondISIN, "ReverseConvertible"));

		System.out.println ("IsSinkable: " + FI.GetBondBooleanField (strBondISIN, "Sinkable"));

		System.out.println ("IsStructuredNote: " + FI.GetBondBooleanField (strBondISIN, "StructuredNote"));

		System.out.println ("IsTradeStatus: " + FI.GetBondBooleanField (strBondISIN, "TradeStatus"));

		System.out.println ("IsUnitTraded: " + FI.GetBondBooleanField (strBondISIN, "UnitTraded"));

		if (!FI.GetBondBooleanField (strBondISIN, "Floater"))
			System.out.println ("Coupon: " + FI.GetBondDoubleField (strBondISIN, "Coupon"));

		System.out.println ("CurrentCoupon: " + FI.GetBondDoubleField (strBondISIN, "CurrentCoupon"));

		System.out.println ("FloatSpread: " + FI.GetBondDoubleField (strBondISIN, "FloatSpread"));

		System.out.println ("IssueAmount: " + FI.GetBondDoubleField (strBondISIN, "IssueAmount"));

		System.out.println ("IssuePrice: " + FI.GetBondDoubleField (strBondISIN, "IssuePrice"));

		System.out.println ("MinimumIncrement: " + FI.GetBondDoubleField (strBondISIN, "MinimumIncrement"));

		System.out.println ("MinimumPiece: " + FI.GetBondDoubleField (strBondISIN, "MinimumPiece"));

		System.out.println ("OutstandingAmount: " + FI.GetBondDoubleField (strBondISIN, "OutstandingAmount"));

		System.out.println ("ParAmount: " + FI.GetBondDoubleField (strBondISIN, "ParAmount"));

		System.out.println ("RedemptionValue: " + FI.GetBondDoubleField (strBondISIN, "RedemptionValue"));

		System.out.println ("CouponFrequency: " + FI.GetBondIntegerField (strBondISIN, "Frequency"));

		System.out.println ("AccrualStartDate: " + FI.GetBondDateField (strBondISIN, "AccrualStartDate"));

		System.out.println ("AnnounceDate: " + FI.GetBondDateField (strBondISIN, "AnnounceDate"));

		System.out.println ("FinalMaturity: " + FI.GetBondDateField (strBondISIN, "FinalMaturity"));

		System.out.println ("FirstCoupon: " + FI.GetBondDateField (strBondISIN, "FirstCouponDate"));

		System.out.println ("FirstSettle: " + FI.GetBondDateField (strBondISIN, "FirstSettleDate"));

		System.out.println ("Issue: " + FI.GetBondDateField (strBondISIN, "IssueDate"));

		System.out.println ("Maturity: " + FI.GetBondDateField (strBondISIN, "Maturity"));

		System.out.println ("NextCoupon: " + FI.GetBondDateField (strBondISIN, "NextCouponDate"));

		System.out.println ("PenultimateCoupon: " + FI.GetBondDateField (strBondISIN, "PenultimateCouponDate"));

		System.out.println ("PrevCoupon: " + FI.GetBondDateField (strBondISIN, "PreviousCouponDate"));
	}

	public static final void main (final String astrArgs[]) throws Exception {
		// String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		String strConfig = "";

		if (!FI.Init (strConfig)) {
			System.out.println ("Cannot fully init FI!");

			System.exit (304);
		}

		BondStaticAPI();
	}
}
