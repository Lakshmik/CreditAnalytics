
package org.drip.product.creator;

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
 * This class contains the entire set of static parameters for the bond product. In particular, it contains
 * 		the bond identifier parameters (ISIN, CUSIP, BBG ID, name short name), the issuer level parameters
 * 		(Ticker, category, industry, issue type, issuer country, issuer country code, collateral type,
 * 		description, security type, unique Bloomberg ID, long company name, issuer name, SPN or the credit
 * 		curve string), issue parameters (issue amount, issue price, outstanding amount, minimum piece,
 * 		minimum increment, par amount, lead manager, exchange code, country of incorporation, country of
 * 		guarantor, country of domicile, industry sector, industry group, industry sub-group, senior/sub),
 * 		coupon parameters (coupon rate, coupon frequency, coupon type, day count), maturity parameters
 * 		(maturity date, maturity type, final maturity, redemption value), date parameters (announce, first
 * 		settle, first coupon, interest accrual start, next coupon, previous coupon, penultimate coupon, and
 * 		issue dates), embedded option parameters (callable, putable, has been exercised), currency parameters
 * 		(trade, coupon, and redemption currencies), floater parameters (floater flag, floating coupon
 * 		convention, current coupon, rate index, spread), trade status, ratings (S & P, Moody, and Fitch), and
 * 		whether the bond is private placement, is registered, is a bearer bond, is reverse convertible, is a
 * 		structured note, can be unit traded, is perpetual or has defaulted.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondRefDataBuilder extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable {
	private static final boolean m_bBlog = false;
	private static final boolean m_bDisplayWarnings = true;

	/**
	 * ISIN
	 */

	public java.lang.String _strISIN = "";

	/**
	 * CUSIP
	 */

	public java.lang.String _strCUSIP = "";

	/**
	 * Bloomberg ID
	 */

	public java.lang.String _strBBGID = "";

	/**
	 * Issuer Category
	 */

	public java.lang.String _strIssuerCategory = "";

	/**
	 * Ticker
	 */

	public java.lang.String _strTicker = "";

	/**
	 * Series
	 */

	public java.lang.String _strSeries = "";

	/**
	 * Name
	 */

	public java.lang.String _strName = "";

	/**
	 * Short Name
	 */

	public java.lang.String _strShortName = "";

	/**
	 * Issuer Industry
	 */

	public java.lang.String _strIssuerIndustry = "";

	/**
	 * Coupon Type
	 */

	public java.lang.String _strCouponType = "";

	/**
	 * Maturity Type
	 */

	public java.lang.String _strMaturityType = "";

	/**
	 * Calculation Type
	 */

	public java.lang.String _strCalculationType = "";

	/**
	 * Day Count Code
	 */

	public java.lang.String _strDayCountCode = "";

	/**
	 * Market Issue Type
	 */

	public java.lang.String _strMarketIssueType = "";

	/**
	 * Issue Country Code
	 */

	public java.lang.String _strIssueCountryCode = "";

	/**
	 * Issue Country
	 */

	public java.lang.String _strIssueCountry = "";

	/**
	 * Collateral Type
	 */

	public java.lang.String _strCollateralType = "";

	/**
	 * Issue Amount
	 */

	public double _dblIssueAmount = java.lang.Double.NaN;

	/**
	 * Outstanding Amount
	 */

	public double _dblOutstandingAmount = java.lang.Double.NaN;

	/**
	 * Minimum Piece
	 */

	public double _dblMinimumPiece = java.lang.Double.NaN;

	/**
	 * Minimum Increment
	 */

	public double _dblMinimumIncrement = java.lang.Double.NaN;

	/**
	 * Par Amount
	 */

	public double _dblParAmount = java.lang.Double.NaN;

	/**
	 * Lead Manager
	 */

	public java.lang.String _strLeadManager = "";

	/**
	 * Exchange Code
	 */

	public java.lang.String _strExchangeCode = "";

	/**
	 * Redemption Value
	 */

	public double _dblRedemptionValue = java.lang.Double.NaN;

	/**
	 * Announce Date
	 */

	public org.drip.analytics.date.JulianDate _dtAnnounce = null;

	/**
	 * First Settle Date
	 */

	public org.drip.analytics.date.JulianDate _dtFirstSettle = null;

	/**
	 * First Coupon Date
	 */

	public org.drip.analytics.date.JulianDate _dtFirstCoupon = null;

	/**
	 * Interest Accrual Start Date
	 */

	public org.drip.analytics.date.JulianDate _dtInterestAccrualStart = null;

	/**
	 * Issue Date
	 */

	public org.drip.analytics.date.JulianDate _dtIssue = null;

	/**
	 * Next Coupon Date
	 */

	public org.drip.analytics.date.JulianDate _dtNextCouponDate = null;

	/**
	 * Callable flag
	 */

	public boolean _bIsCallable = false;

	/**
	 * Putable flag
	 */

	public boolean _bIsPutable = false;

	/**
	 * Sinkable flag
	 */

	public boolean _bIsSinkable = false;

	/**
	 * Bloomberg Parent
	 */

	public java.lang.String _strBBGParent = "";

	/**
	 * Country of Incorporation
	 */

	public java.lang.String _strCountryOfIncorporation = "";

	/**
	 * Industry Sector
	 */

	public java.lang.String _strIndustrySector = "";

	/**
	 * Industry Group
	 */

	public java.lang.String _strIndustryGroup = "";

	/**
	 * Industry Sub Group
	 */

	public java.lang.String _strIndustrySubgroup = "";

	/**
	 * Country of Guarantor
	 */

	public java.lang.String _strCountryOfGuarantor = "";

	/**
	 * Country of Domicile
	 */

	public java.lang.String _strCountryOfDomicile = "";

	/**
	 * Description
	 */

	public java.lang.String _strDescription = "";

	/**
	 * Security Type
	 */

	public java.lang.String _strSecurityType = "";

	/**
	 * Previous Coupon Date
	 */

	public org.drip.analytics.date.JulianDate _dtPrevCouponDate = null;

	/**
	 * Unique Bloomberg ID
	 */

	public java.lang.String _strBBGUniqueID = "";

	/**
	 * Long Company Name
	 */

	public java.lang.String _strLongCompanyName = "";

	/**
	 * Flag indicating Structured Note
	 */

	public boolean _bIsStructuredNote = false;

	/**
	 * Flag indicating whether unit traded
	 */

	public boolean _bIsUnitTraded = false;

	/**
	 * Flag indicating is reverse convertible
	 */

	public boolean _bIsReversibleConvertible = false;

	/**
	 * Redemption Currency
	 */

	public java.lang.String _strRedemptionCurrency = "";

	/**
	 * Coupon Currency
	 */

	public java.lang.String _strCouponCurrency = "";

	/**
	 * Trade Currency
	 */

	public java.lang.String _strTradeCurrency = "";

	/**
	 * Is this a Bearer Bond
	 */

	public boolean _bIsBearer = false;

	/**
	 * Is this registered
	 */

	public boolean _bIsRegistered = false;

	/**
	 * Has this been called
	 */

	public boolean _bHasBeenCalled = false;

	/**
	 * Issuer Name
	 */

	public java.lang.String _strIssuer = "";

	/**
	 * Penultimate Coupon Date
	 */

	public org.drip.analytics.date.JulianDate _dtPenultimateCouponDate = null;

	/**
	 * Float Coupon Convention
	 */

	public java.lang.String _strFloatCouponConvention = "";

	/**
	 * Current Coupon
	 */

	public double _dblCurrentCoupon = java.lang.Double.NaN;

	/**
	 * Is this bond a floater
	 */

	public boolean _bIsFloater = false;

	/**
	 * Trade Status
	 */

	public boolean _bTradeStatus = false;

	/**
	 * CDR Country Code
	 */

	public java.lang.String _strCDRCountryCode = "";

	/**
	 * CDR Settle Code
	 */

	public java.lang.String _strCDRSettleCode = "";

	/**
	 * Final Maturity Date
	 */

	public org.drip.analytics.date.JulianDate _dtFinalMaturity = null;

	/**
	 * Is this a private placement
	 */

	public boolean _bIsPrivatePlacement = false;

	/**
	 * Is this bond perpetual
	 */

	public boolean _bIsPerpetual = false;

	/**
	 * Has this bond defaulted
	 */

	public boolean _bIsDefaulted = false;

	/**
	 * Spread over the floater index for this bond
	 */

	public double _dblFloatSpread = java.lang.Double.NaN;

	/**
	 * Floating rate index
	 */

	public java.lang.String _strRateIndex = "";

	/**
	 * Moody's Rating
	 */

	public java.lang.String _strMoody = "";

	/**
	 * S&P rating
	 */

	public java.lang.String _strSnP = "";

	/**
	 * Fitch Rating
	 */

	public java.lang.String _strFitch = "";

	/**
	 * Senior or Sub-ordinate
	 */

	public java.lang.String _strSnrSub = "";

	/**
	 * Issuer SPN
	 */

	public java.lang.String _strIssuerSPN = "";

	/**
	 * Issue Price
	 */

	public double _dblIssuePrice = java.lang.Double.NaN;

	/**
	 * Coupon
	 */

	public double _dblCoupon = java.lang.Double.NaN;

	/**
	 * Maturity
	 */

	public org.drip.analytics.date.JulianDate _dtMaturity = null;

	private org.drip.analytics.date.JulianDate reconcileStartDate()
	{
		if (null != _dtInterestAccrualStart) return _dtInterestAccrualStart;

		if (null != _dtFirstCoupon) return _dtFirstCoupon;

		if (null != _dtIssue) return _dtIssue;

		if (null != _dtFirstSettle) return _dtFirstSettle;

		return _dtAnnounce;
	}

	/**
	 * Creates BondRefDataBuilder object from java ResultSet SQL
	 * 
	 * @param rs SQL ResultSet
	 * 
	 * @return BondRefDataBuilder object
	 */

	public static final BondRefDataBuilder CreateFromResultSet (
		final java.sql.ResultSet rs)
	{
		try {
			BondRefDataBuilder brdb = new BondRefDataBuilder();

			if (null == (brdb._strISIN = rs.getString ("ISIN"))) return null;

			if (null == (brdb._strCUSIP = rs.getString ("CUSIP"))) return null;

			brdb._strBBGID = rs.getString ("BBG_ID");

			brdb._strIssuerCategory = rs.getString ("IssuerCategory");

			brdb._strTicker = rs.getString ("Ticker");

			if (!org.drip.math.common.NumberUtil.IsValid (brdb._dblCoupon = rs.getDouble ("Coupon")))
				return null;

			brdb._dtMaturity = org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
				("Maturity"));

			brdb._strSeries = rs.getString ("Series");

			brdb._strName = rs.getString ("Name");

			brdb._strShortName = rs.getString ("ShortName");

			brdb._strIssuerIndustry = rs.getString ("IssuerIndustry");

			brdb._strCouponType = rs.getString ("CouponType");

			brdb._strMaturityType = rs.getString ("MaturityType");

			brdb._strCalculationType = rs.getString ("CalculationType");

			brdb._strDayCountCode = rs.getString ("DayCountConv");

			brdb._strMarketIssueType = rs.getString ("MarketIssueType");

			brdb._strIssueCountryCode = rs.getString ("IssueCountryCode");

			brdb._strIssueCountry = rs.getString ("IssueCountry");

			brdb._strCollateralType = rs.getString ("CollateralType");

			brdb._dblIssueAmount = rs.getDouble ("IssueAmount");

			brdb._dblOutstandingAmount = rs.getDouble ("OutstandingAmount");

			brdb._dblMinimumPiece = rs.getDouble ("MinimumPiece");

			brdb._dblMinimumIncrement = rs.getDouble ("MinimumIncrement");

			brdb._dblParAmount = rs.getDouble ("ParAmount");

			brdb._strLeadManager = rs.getString ("LeadManager");

			brdb._strExchangeCode = rs.getString ("ExchangeCode");

			brdb._dblRedemptionValue = rs.getDouble ("RedemptionValue");

			brdb._dtNextCouponDate = org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry
				(rs.getDate ("NextCouponDate"));

			if (null == (brdb._dtAnnounce =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
					("AnnounceDate"))))
				return null;

			if (null == (brdb._dtFirstSettle =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
					("FirstSettleDate"))))
				return null;

			if (null == (brdb._dtFirstCoupon =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
					("FirstCouponDate"))))
				return null;

			if (null == (brdb._dtInterestAccrualStart =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
					("AccrualStartDate"))))
				return null;

			if (null == (brdb._dtIssue = org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry
				(rs.getDate ("IssueDate"))))
				return null;

			brdb._bIsCallable = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("IsCallable"));

			brdb._bIsPutable = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("IsPutable"));

			brdb._bIsSinkable = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("IsSinkable"));

			brdb._strBBGParent = rs.getString ("BBGParent");

			brdb._strCountryOfIncorporation = rs.getString ("CountryOfIncorporation");

			brdb._strIndustrySector = rs.getString ("IndustrySector");

			brdb._strIndustryGroup = rs.getString ("IndustryGroup");

			brdb._strIndustrySubgroup = rs.getString ("IndustrySubgroup");

			brdb._strCountryOfGuarantor = rs.getString ("CountryOfGuarantor");

			brdb._strCountryOfDomicile = rs.getString ("CountryOfDomicile");

			brdb._strDescription = rs.getString ("Description");

			brdb._strSecurityType = rs.getString ("SecurityType");

			brdb._dtPrevCouponDate = org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry
				(rs.getDate ("PrevCouponDate"));

			brdb._strBBGUniqueID = rs.getString ("BBUniqueID");

			brdb._strLongCompanyName = rs.getString ("LongCompanyName");

			brdb._strRedemptionCurrency = rs.getString ("RedemptionCurrency");

			if (null == brdb._strRedemptionCurrency || brdb._strRedemptionCurrency.isEmpty()) return null;

			brdb._strCouponCurrency = rs.getString ("CouponCurrency");

			if (null == brdb._strCouponCurrency || brdb._strCouponCurrency.isEmpty()) return null;

			brdb._bIsStructuredNote = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString
				(rs.getString ("StructuredNote"));

			brdb._bIsUnitTraded = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("UnitTraded"));

			brdb._bIsReversibleConvertible = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString
				(rs.getString ("ReverseConvertible"));

			brdb._strTradeCurrency = rs.getString ("TradeCurrency");

			if (null == brdb._strTradeCurrency || brdb._strTradeCurrency.isEmpty()) return null;

			brdb._bIsBearer = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("Bearer"));

			brdb._bIsRegistered = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("Registered"));

			brdb._bHasBeenCalled = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString
				(rs.getString ("Called"));

			brdb._strIssuer = rs.getString ("Issuer");

			brdb._dtPenultimateCouponDate =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry (rs.getDate
					("PenultimateCouponDate"));

			brdb._strFloatCouponConvention = rs.getString ("FloatCouponConvention");

			brdb._dblCurrentCoupon = rs.getDouble ("CurrentCoupon");

			brdb._bIsFloater = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("Floater"));

			brdb._bTradeStatus = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("TradeStatus"));

			brdb._strCDRCountryCode = rs.getString ("CDRCountryCode");

			brdb._strCDRSettleCode = rs.getString ("CDRSettleCode");

			brdb._strFloatCouponConvention = rs.getString ("FloatCouponConvention");

			brdb._dtFinalMaturity = org.drip.analytics.support.AnalyticsHelper.MakeJulianFromRSEntry
				(rs.getDate ("FinalMaturity"));

			brdb._bIsPrivatePlacement = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString
				(rs.getString ("PrivatePlacement"));

			brdb._bIsPerpetual = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("Perpetual"));

			brdb._bIsDefaulted = org.drip.analytics.support.GenericUtil.ParseFromUnitaryString (rs.getString
				("Defaulted"));

			brdb._dblFloatSpread = rs.getDouble ("FloatSpread");

			brdb._strRateIndex = rs.getString ("RateIndex");

			brdb._strMoody = rs.getString ("Moody");

			brdb._strSnP = rs.getString ("SnP");

			brdb._strFitch = rs.getString ("Fitch");

			brdb._strSnrSub = rs.getString ("SnrSub");

			brdb._strIssuerSPN = rs.getString ("SPN");

			brdb._dblIssuePrice = rs.getDouble ("IssuePrice");

			return brdb;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Empty BondRefDataBuilder ctr - uninitialized members
	 */

	public BondRefDataBuilder()
	{
	}

	/**
	 * BondRefDataBuilder de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if BondRefDataBuilder cannot be properly de-serialized
	 */

	public BondRefDataBuilder (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Empty state");

		java.lang.String strSerializedBondRefDataBuilder = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedBondRefDataBuilder || strSerializedBondRefDataBuilder.isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedBondRefDataBuilder, getFieldDelimiter());

		if (null == astrField || 76 > astrField.length)
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate ISIN");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_strISIN = "";
		else
			_strISIN = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate CUSIP");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_strCUSIP = "";
		else
			_strCUSIP = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate BBG ID");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strBBGID = "";
		else
			_strBBGID = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issuer category");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_strIssuerCategory = "";
		else
			_strIssuerCategory = astrField[4];

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate ticker");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			_strTicker = "";
		else
			_strTicker = astrField[5];

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Series");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			_strSeries = "";
		else
			_strSeries = astrField[6];

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issue name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			_strName = "";
		else
			_strName = astrField[7];

		if (null == astrField[8] || astrField[8].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate short name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			_strShortName = "";
		else
			_strShortName = astrField[8];

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issuer industry");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			_strIssuerIndustry = "";
		else
			_strIssuerIndustry = astrField[9];

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate coupon type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[10]))
			_strCouponType = "";
		else
			_strCouponType = astrField[10];

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate maturity type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[11]))
			_strMaturityType = "";
		else
			_strMaturityType = astrField[11];

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate calculation type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[12]))
			_strCalculationType = "";
		else
			_strCalculationType = astrField[12];

		if (null == astrField[13] || astrField[13].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate DayCount Code");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[13]))
			_strDayCountCode = "";
		else
			_strDayCountCode = astrField[13];

		if (null == astrField[14] || astrField[14].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate market issue type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[14]))
			_strMarketIssueType = "";
		else
			_strMarketIssueType = astrField[14];

		if (null == astrField[15] || astrField[15].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issue country code");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[15]))
			_strIssueCountryCode = "";
		else
			_strIssueCountryCode = astrField[15];

		if (null == astrField[16] || astrField[16].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issue country");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[16]))
			_strIssueCountry = "";
		else
			_strIssueCountry = astrField[16];

		if (null == astrField[17] || astrField[17].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issue collateral type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[17]))
			_strCollateralType = "";
		else
			_strCollateralType = astrField[17];

		if (null == astrField[18] || astrField[18].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[18]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issue amount");

		_dblIssueAmount = new java.lang.Double (astrField[18]);

		if (null == astrField[19] || astrField[19].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[19]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate outstanding amount");

		_dblOutstandingAmount = new java.lang.Double (astrField[19]);

		if (null == astrField[20] || astrField[20].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[20]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate minimum piece");

		_dblMinimumPiece = new java.lang.Double (astrField[20]);

		if (null == astrField[21] || astrField[21].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[21]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate minimum increment");

		_dblMinimumIncrement = new java.lang.Double (astrField[21]);

		if (null == astrField[22] || astrField[22].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[22]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate par amount");

		_dblParAmount = new java.lang.Double (astrField[22]);

		if (null == astrField[23] || astrField[23].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issue lead manager");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[23]))
			_strLeadManager = "";
		else
			_strLeadManager = astrField[23];

		if (null == astrField[24] || astrField[24].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate issue Exchange Code");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[24]))
			_strExchangeCode = "";
		else
			_strExchangeCode = astrField[24];

		if (null == astrField[25] || astrField[25].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[25]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate redemption value");

		_dblRedemptionValue = new java.lang.Double (astrField[25]);

		if (null == astrField[26] || astrField[26].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate announce date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[26]))
			_dtAnnounce = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[26]));
		else
			_dtAnnounce = null;

		if (null == astrField[27] || astrField[27].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate first settle date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[27]))
			_dtFirstSettle = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[27]));
		else
			_dtFirstSettle = null;

		if (null == astrField[28] || astrField[28].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate first coupon date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[28]))
			_dtFirstCoupon = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[28]));
		else
			_dtFirstCoupon = null;

		if (null == astrField[29] || astrField[29].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate interest accrual start date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[29]))
			_dtInterestAccrualStart = new org.drip.analytics.date.JulianDate (new java.lang.Double
				(astrField[29]));
		else
			_dtInterestAccrualStart = null;

		if (null == astrField[30] || astrField[30].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issue date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[30]))
			_dtIssue = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[30]));
		else
			_dtIssue = null;

		if (null == astrField[31] || astrField[31].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate next coupon date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[31]))
			_dtNextCouponDate = new org.drip.analytics.date.JulianDate (new java.lang.Double
				(astrField[31]));
		else
			_dtNextCouponDate = null;

		if (null == astrField[32] || astrField[32].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[32]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate callable flag");

		_bIsCallable = new java.lang.Boolean (astrField[32]);

		if (null == astrField[33] || astrField[33].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[33]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate putable flag");

		_bIsPutable = new java.lang.Boolean (astrField[33]);

		if (null == astrField[34] || astrField[34].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[34]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate sinkable flag");

		_bIsSinkable = new java.lang.Boolean (astrField[34]);

		if (null == astrField[35] || astrField[35].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate BBG Parent");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[35]))
			_strBBGParent = "";
		else
			_strBBGParent = astrField[35];

		if (null == astrField[36] || astrField[36].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Country Of Incorporation");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[36]))
			_strCountryOfIncorporation = "";
		else
			_strCountryOfIncorporation = astrField[36];

		if (null == astrField[37] || astrField[37].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate industry sector");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[37]))
			_strIndustrySector = "";
		else
			_strIndustrySector = astrField[37];

		if (null == astrField[38] || astrField[38].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Industry Group");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[38]))
			_strIndustryGroup = "";
		else
			_strIndustryGroup = astrField[38];

		if (null == astrField[39] || astrField[39].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Industry Subgroup");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[39]))
			_strIndustrySubgroup = "";
		else
			_strIndustrySubgroup = astrField[39];

		if (null == astrField[40] || astrField[40].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Country Of Guarantor");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[40]))
			_strCountryOfGuarantor = "";
		else
			_strCountryOfGuarantor = astrField[40];

		if (null == astrField[41] || astrField[41].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Country Of Domicile");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[41]))
			_strCountryOfDomicile = "";
		else
			_strCountryOfDomicile = astrField[41];

		if (null == astrField[42] || astrField[42].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Description");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[42]))
			_strDescription = "";
		else
			_strDescription = astrField[42];

		if (null == astrField[43] || astrField[43].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Security Type");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[43]))
			_strSecurityType = "";
		else
			_strSecurityType = astrField[43];

		if (null == astrField[44] || astrField[44].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate prev coupon date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[44]))
			_dtPrevCouponDate = new org.drip.analytics.date.JulianDate (new java.lang.Double
				(astrField[44]).doubleValue());
		else
			_dtPrevCouponDate = null;

		if (null == astrField[45] || astrField[45].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate BBG Unique ID");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[45]))
			_strBBGUniqueID = "";
		else
			_strBBGUniqueID = astrField[45];

		if (null == astrField[46] || astrField[46].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Long Company Name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[46]))
			_strLongCompanyName = "";
		else
			_strLongCompanyName = astrField[46];

		if (null == astrField[47] || astrField[47].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[47]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate structured note flag");

		_bIsStructuredNote = new java.lang.Boolean (astrField[47]);

		if (null == astrField[48] || astrField[48].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[48]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate unit traded flag");

		_bIsUnitTraded = new java.lang.Boolean (astrField[48]);

		if (null == astrField[49] || astrField[49].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[49]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Reversible Convertible flag");

		_bIsReversibleConvertible = new java.lang.Boolean (astrField[49]);

		if (null == astrField[50] || astrField[50].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate redemption ccy");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[50]))
			_strRedemptionCurrency = "";
		else
			_strRedemptionCurrency = astrField[50];

		if (null == astrField[51] || astrField[51].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate coupon ccy");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[51]))
			_strCouponCurrency = "";
		else
			_strCouponCurrency = astrField[51];

		if (null == astrField[52] || astrField[52].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate trade ccy");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[52]))
			_strTradeCurrency = "";
		else
			_strTradeCurrency = astrField[52];

		if (null == astrField[53] || astrField[53].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[53]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate bearer flag");

		_bIsBearer = new java.lang.Boolean (astrField[53]);

		if (null == astrField[54] || astrField[54].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[54]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate registered flag");

		_bIsRegistered = new java.lang.Boolean (astrField[54]);

		if (null == astrField[55] || astrField[55].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[55]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Has Been Called flag");

		_bHasBeenCalled = new java.lang.Boolean (astrField[55]);

		if (null == astrField[56] || astrField[56].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issuer");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[56]))
			_strIssuer = "";
		else
			_strIssuer = astrField[56];

		if (null == astrField[57] || astrField[57].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate penultimate coupon date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[57]))
			_dtPenultimateCouponDate = new org.drip.analytics.date.JulianDate (new java.lang.Double
				(astrField[57]));
		else
			_dtPenultimateCouponDate = null;

		if (null == astrField[58] || astrField[58].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate float coupon convention");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[58]))
			_strFloatCouponConvention = "";
		else
			_strFloatCouponConvention = astrField[58];

		if (null == astrField[59] || astrField[59].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[59]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate current coupon");

		_dblCurrentCoupon = new java.lang.Double (astrField[59]);

		if (null == astrField[60] || astrField[60].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[60]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate floater flag");

		_bIsFloater = new java.lang.Boolean (astrField[60]);

		if (null == astrField[61] || astrField[61].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[61]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate trade status flag");

		_bTradeStatus = new java.lang.Boolean (astrField[61]);

		if (null == astrField[62] || astrField[62].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate CDR Country Code");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[62]))
			_strCDRCountryCode = "";
		else
			_strCDRCountryCode = astrField[62];

		if (null == astrField[63] || astrField[63].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate CDR Settle Code");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[63]))
			_strCDRSettleCode = "";
		else
			_strCDRSettleCode = astrField[63];

		if (null == astrField[64] || astrField[64].isEmpty())
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate final maturity date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[64]))
			_dtFinalMaturity = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[64]));
		else
			_dtFinalMaturity = null;

		if (null == astrField[65] || astrField[65].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[65]))
			throw new java.lang.Exception
				("BondRefDataBuilder de-serializer: Cannot locate Private Placement Flag");

		_bIsPrivatePlacement = new java.lang.Boolean (astrField[65]);

		if (null == astrField[66] || astrField[66].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[66]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Perpetual Flag");

		_bIsPerpetual = new java.lang.Boolean (astrField[66]);

		if (null == astrField[67] || astrField[67].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[67]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Defaulted Flag");

		_bIsDefaulted = new java.lang.Boolean (astrField[67]);

		if (null == astrField[68] || astrField[68].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[68]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate float spread");

		_dblFloatSpread = new java.lang.Double (astrField[68]);

		if (null == astrField[69] || astrField[69].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Rate Index");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[69]))
			_strRateIndex = "";
		else
			_strRateIndex = astrField[69];

		if (null == astrField[70] || astrField[70].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Moody's");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[70]))
			_strMoody = "";
		else
			_strMoody = astrField[70];

		if (null == astrField[71] || astrField[71].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate SnP");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[71]))
			_strSnP = "";
		else
			_strSnP = astrField[71];

		if (null == astrField[72] || astrField[72].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Fitch");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[72]))
			_strFitch = "";
		else
			_strFitch = astrField[72];

		if (null == astrField[73] || astrField[73].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate Snr/Sub");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[73]))
			_strSnrSub = "";
		else
			_strSnrSub = astrField[73];

		if (null == astrField[74] || astrField[74].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issuer SPN");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[74]))
			_strIssuerSPN = "";
		else
			_strIssuerSPN = astrField[74];

		if (null == astrField[75] || astrField[75].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[75]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate issue price");

		_dblIssuePrice = new java.lang.Double (astrField[75]);

		if (null == astrField[76] || astrField[76].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[76]))
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate coupon");

		_dblCoupon = new java.lang.Double (astrField[76]);

		if (null == astrField[77] || astrField[77].isEmpty())
			throw new java.lang.Exception ("BondRefDataBuilder de-serializer: Cannot locate maturity date");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[77]))
			_dtMaturity = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[77]));
		else
			_dtMaturity = null;
	}

	/**
	 * Sets the ISIN
	 * 
	 * @param strISIN ISIN
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setISIN (
		final java.lang.String strISIN)
	{
		if (null == strISIN || strISIN.isEmpty()) return false;

		_strISIN = strISIN;
		return true;
	}

	/**
	 * Sets the CUSIP
	 * 
	 * @param strCUSIP CUSIP
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCUSIP (
		final java.lang.String strCUSIP)
	{
		if (null == strCUSIP || strCUSIP.isEmpty()) return false;

		_strCUSIP = strCUSIP;
		return true;
	}

	/**
	 * Sets the Bloomberg ID
	 * 
	 * @param strBBGID Bloomberg ID String
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setBBGID (
		final java.lang.String strBBGID)
	{
		if (null == (_strBBGID = strBBGID)) _strBBGID = "";

		return true;
	}

	/**
	 * Sets the Issuer Category
	 * 
	 * @param strIssuerCategory Issuer Category
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssuerCategory (
		final java.lang.String strIssuerCategory)
	{
		if (null == (_strIssuerCategory = strIssuerCategory)) _strIssuerCategory = "";

		return true;
	}

	/**
	 * Sets the Issuer Ticker
	 * 
	 * @param strTicker Ticker
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setTicker (
		final java.lang.String strTicker)
	{
		if (null == (_strTicker = strTicker)) _strTicker = "";

		return true;
	}

	/**
	 * Sets the Issuer Series
	 * 
	 * @param strSeries series
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setSeries (
		final java.lang.String strSeries)
	{
		if (null == (_strSeries = strSeries)) _strSeries = "";

		return true;
	}

	/**
	 * Sets the Issuer Name
	 * 
	 * @param strName Name
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setName (
		final java.lang.String strName)
	{
		if (null == (_strName = strName)) _strName = "";

		return true;
	}

	/**
	 * Sets the Issuer Short Name
	 * 
	 * @param strShortName Short Name
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setShortName (
		final java.lang.String strShortName)
	{
		if (null == (_strShortName = strShortName)) _strShortName = "";

		return true;
	}

	/**
	 * Sets the Issuer Industry
	 * 
	 * @param strIssuerIndustry Issuer Industry
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssuerIndustry (
		final java.lang.String strIssuerIndustry)
	{
		if (null == (_strIssuerIndustry = strIssuerIndustry)) _strIssuerIndustry = "";

		return true;
	}

	/**
	 * Sets the Coupon Type
	 * 
	 * @param strCouponType Coupon Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCouponType (
		final java.lang.String strCouponType)
	{
		if (null == (_strCouponType = strCouponType)) _strCouponType = "";

		return true;
	}

	/**
	 * Sets the Maturity Type
	 * 
	 * @param strMaturityType Maturity Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMaturityType (
		final java.lang.String strMaturityType)
	{
		if (null == (_strMaturityType = strMaturityType)) _strMaturityType = "";

		return true;
	}

	/**
	 * Sets the Calculation Type
	 * 
	 * @param strCalculationType Calculation Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCalculationType (
		final java.lang.String strCalculationType)
	{
		if (null == (_strCalculationType = strCalculationType)) _strCalculationType = "";

		return true;
	}

	/**
	 * Sets the Day Count Code
	 * 
	 * @param strDayCountCode Day Count Code
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setDayCountCode (
		final java.lang.String strDayCountCode)
	{
		_strDayCountCode = "Unknown DC";

		try {
			_strDayCountCode = org.drip.analytics.support.AnalyticsHelper.ParseFromBBGDCCode
				(strDayCountCode);
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad dayCount " + strDayCountCode + " for ISIN " +
					_strISIN);

			return false;
		}

		return true;
	}

	/**
	 * Sets the Market Issue Type
	 * 
	 * @param strMarketIssueType Market Issue Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMarketIssueType (
		final java.lang.String strMarketIssueType)
	{
		if (null == (_strMarketIssueType = strMarketIssueType)) _strMarketIssueType = "";

		return true;
	}

	/**
	 * Sets the Issue Country Code
	 * 
	 * @param strIssueCountryCode Issue Country Code
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssueCountryCode (
		final java.lang.String strIssueCountryCode)
	{
		if (null == (_strIssueCountryCode = strIssueCountryCode)) _strIssueCountryCode = "";

		return true;
	}

	/**
	 * Sets the Issue Country
	 * 
	 * @param strIssueCountry Issue Country
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssueCountry (
		final java.lang.String strIssueCountry)
	{
		if (null == (_strIssueCountry = strIssueCountry)) _strIssueCountry = "";

		return true;
	}

	/**
	 * Sets the Collateral Type
	 * 
	 * @param strCollateralType Collateral Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCollateralType (
		final java.lang.String strCollateralType)
	{
		if (null == (_strCollateralType = strCollateralType)) _strCollateralType = "";

		return true;
	}

	/**
	 * Sets the Issue Amount
	 * 
	 * @param strIssueAmount Issue Amount
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssueAmount (
		final java.lang.String strIssueAmount)
	{
		try {
			_dblIssueAmount = new java.lang.Double (strIssueAmount.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Issue Amount " + strIssueAmount + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Outstanding Amount
	 * 
	 * @param strOutstandingAmount Outstanding Amount
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setOutstandingAmount (
		final java.lang.String strOutstandingAmount)
	{
		try {
			_dblOutstandingAmount = new java.lang.Double (strOutstandingAmount.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Outstanding Amount " + strOutstandingAmount + " for ISIN " +
					_strISIN);
		}

		return false;
	}

	/**
	 * Sets the Minimum Piece
	 * 
	 * @param strMinimumPiece Minimum Piece
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMinimumPiece (
		final java.lang.String strMinimumPiece)
	{
		try {
			_dblMinimumPiece = new java.lang.Double (strMinimumPiece.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Minimum Piece " + strMinimumPiece + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Minimum Increment
	 * 
	 * @param strMinimumIncrement Minimum Increment
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMinimumIncrement (
		final java.lang.String strMinimumIncrement)
	{
		try {
			_dblMinimumIncrement = new java.lang.Double (strMinimumIncrement.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Minimum Increment " + strMinimumIncrement + " for ISIN " +
					_strISIN);
		}

		return false;
	}

	/**
	 * Sets the Par Amount
	 * 
	 * @param strParAmount Par Amount
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setParAmount (
		final java.lang.String strParAmount)
	{
		try {
			_dblParAmount = new java.lang.Double (strParAmount.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Par Amount " + strParAmount + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Lead Manager
	 * 
	 * @param strLeadManager Lead Manager
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setLeadManager (
		final java.lang.String strLeadManager)
	{
		if (null == (_strLeadManager = strLeadManager)) _strLeadManager = "";

		return true;
	}

	/**
	 * Sets the Exchange Code
	 * 
	 * @param strExchangeCode Exchange Code
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setExchangeCode (
		final java.lang.String strExchangeCode)
	{
		if (null == (_strExchangeCode = strExchangeCode)) _strExchangeCode = "";

		return true;
	}

	/**
	 * Sets the Redemption Value
	 * 
	 * @param strRedemptionValue Redemption Value
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setRedemptionValue (
		final java.lang.String strRedemptionValue)
	{
		try {
			_dblRedemptionValue = new java.lang.Double (strRedemptionValue.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Redemption Value " + strRedemptionValue + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Announce
	 * 
	 * @param strAnnounce Announce
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setAnnounce (
		final java.lang.String strAnnounce)
	{
		try {
			_dtAnnounce = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strAnnounce.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Announce " + strAnnounce + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the First Settle
	 * 
	 * @param strFirstSettle First Settle
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFirstSettle (
		final java.lang.String strFirstSettle)
	{
		try {
			_dtFirstSettle = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strFirstSettle.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad First Settle " + strFirstSettle + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the First Coupon
	 * 
	 * @param strFirstCoupon First Coupon
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFirstCoupon (
		final java.lang.String strFirstCoupon)
	{
		try {
			_dtFirstCoupon = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strFirstCoupon.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad First Coupon " + strFirstCoupon + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Interest Accrual Start Date
	 * 
	 * @param strInterestAccrualStart Interest Accrual Start Date
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setInterestAccrualStart (
		final java.lang.String strInterestAccrualStart)
	{
		try {
			_dtInterestAccrualStart = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strInterestAccrualStart.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Announce " + strInterestAccrualStart + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Issue Date
	 * 
	 * @param strIssue Issue Date
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssue (
		final java.lang.String strIssue)
	{
		try {
			_dtIssue = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strIssue.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Issue " + strIssue + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Next Coupon Date
	 * 
	 * @param strNextCouponDate Next Coupon Date
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setNextCouponDate (
		final java.lang.String strNextCouponDate)
	{
		try {
			_dtNextCouponDate = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strNextCouponDate.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Next Coupon Date " + strNextCouponDate + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets whether is Callable
	 * 
	 * @param strCallable Callable?
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsCallable (
		final java.lang.String strCallable)
	{
		if (null == strCallable) _bIsCallable = false;

		if ("1".equalsIgnoreCase (strCallable))
			_bIsCallable = true;
		else
			_bIsCallable = false;

		return true;
	}

	/**
	 * Sets whether is Putable
	 * 
	 * @param strPutable Putable?
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsPutable (
		final java.lang.String strPutable)
	{
		if (null == strPutable) _bIsPutable = false;

		if ("1".equalsIgnoreCase (strPutable))
			_bIsPutable = true;
		else
			_bIsPutable = false;

		return true;
	}

	/**
	 * Sets whether is Sinkable
	 * 
	 * @param strSinkable Sinkable?
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsSinkable (
		final java.lang.String strSinkable)
	{
		if (null == strSinkable) _bIsSinkable = false;

		if ("1".equalsIgnoreCase (strSinkable))
			_bIsSinkable = true;
		else
			_bIsSinkable = false;

		return true;
	}

	/**
	 * Sets the Bloomberg Parent
	 * 
	 * @param strBBGParent Bloomberg Parent?
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setBBGParent (
		final java.lang.String strBBGParent)
	{
		if (null == (_strBBGParent = strBBGParent)) _strBBGParent = "";

		return true;
	}

	/**
	 * Sets the Country Of Incorporation
	 * 
	 * @param strCountryOfIncorporation Country Of Incorporation
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCountryOfIncorporation (
		final java.lang.String strCountryOfIncorporation)
	{
		if (null == (_strCountryOfIncorporation = strCountryOfIncorporation))
			_strCountryOfIncorporation = "";

		return true;
	}

	/**
	 * Sets the Industry Sector
	 * 
	 * @param strIndustrySector Industry Sector
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIndustrySector (
		final java.lang.String strIndustrySector)
	{
		if (null == (_strIndustrySector = strIndustrySector)) _strIndustrySector = "";

		return true;
	}

	/**
	 * Sets the Industry Group
	 * 
	 * @param strIndustryGroup Industry Group
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIndustryGroup (
		final java.lang.String strIndustryGroup)
	{
		if (null == (_strIndustryGroup = strIndustryGroup)) _strIndustryGroup = "";

		return true;
	}

	/**
	 * Sets the Industry Subgroup
	 * 
	 * @param strIndustrySubgroup Industry Subgroup
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIndustrySubgroup (
		final java.lang.String strIndustrySubgroup)
	{
		if (null == (_strIndustrySubgroup = strIndustrySubgroup)) _strIndustrySubgroup = "";

		return true;
	}

	/**
	 * Sets the Country Of Guarantor
	 * 
	 * @param strCountryOfGuarantor Country Of Guarantor
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCountryOfGuarantor (
		final java.lang.String strCountryOfGuarantor)
	{
		if (null == (_strCountryOfGuarantor = strCountryOfGuarantor)) _strCountryOfGuarantor = "";

		return true;
	}

	/**
	 * Sets the Country Of Domicile
	 * 
	 * @param strCountryOfDomicile Country Of Domicile
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCountryOfDomicile (
		final java.lang.String strCountryOfDomicile)
	{
		if (null == (_strCountryOfDomicile = strCountryOfDomicile)) _strCountryOfDomicile = "";

		return true;
	}

	/**
	 * Sets the Description
	 * 
	 * @param strDescription Description
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setDescription (
		final java.lang.String strDescription)
	{
		if (null == (_strDescription = strDescription)) _strDescription = "";

		return true;
	}

	/**
	 * Sets the Security Type
	 * 
	 * @param strSecurityType Security Type
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setSecurityType (
		final java.lang.String strSecurityType)
	{
		if (null == (_strSecurityType = strSecurityType)) _strSecurityType = "";

		return true;
	}

	/**
	 * Sets the Previous Coupon Date
	 * 
	 * @param strPrevCouponDate Previous Coupon Date
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setPrevCouponDate (
		final java.lang.String strPrevCouponDate)
	{
		try {
			_dtPrevCouponDate = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strPrevCouponDate.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Prev Coupon Date " + strPrevCouponDate + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Unique Bloomberg ID
	 * 
	 * @param strBBGUniqueID BBGUniqueID
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setBBGUniqueID (
		final java.lang.String strBBGUniqueID)
	{
		if (null == (_strBBGUniqueID = strBBGUniqueID)) _strBBGUniqueID = "";

		return true;
	}

	/**
	 * Sets the Long Company Name
	 * 
	 * @param strLongCompanyName Long Company Name
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setLongCompanyName (
		final java.lang.String strLongCompanyName)
	{
		if (null == (_strLongCompanyName = strLongCompanyName)) _strLongCompanyName = "";

		return true;
	}

	/**
	 * Sets the Flag indicating Structured Note
	 * 
	 * @param strIsStructuredNote Flag indicating Structured Note
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsStructuredNote (
		final java.lang.String strIsStructuredNote)
	{
		if (null == strIsStructuredNote) _bIsStructuredNote = false;

		if ("1".equalsIgnoreCase (strIsStructuredNote))
			_bIsStructuredNote = true;
		else
			_bIsStructuredNote = false;

		return true;
	}

	/**
	 * Sets the Flag indicating Unit Traded
	 * 
	 * @param strIsUnitTraded Flag indicating Unit Traded
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsUnitTraded (
		final java.lang.String strIsUnitTraded)
	{
		if (null == strIsUnitTraded) _bIsUnitTraded = false;

		if ("1".equalsIgnoreCase (strIsUnitTraded))
			_bIsUnitTraded = true;
		else
			_bIsUnitTraded = false;

		return true;
	}

	/**
	 * Sets the Flag indicating Reverse Convertible
	 * 
	 * @param strIsReversibleConvertible Flag indicating Reverse Convertible
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsReversibleConvertible (
		final java.lang.String strIsReversibleConvertible)
	{
		if (null == strIsReversibleConvertible) _bIsReversibleConvertible = false;

		if ("1".equalsIgnoreCase (strIsReversibleConvertible))
			_bIsReversibleConvertible = true;
		else
			_bIsReversibleConvertible = false;

		return true;
	}

	/**
	 * Sets the Redemption Currency
	 * 
	 * @param strRedemptionCurrency Redemption Currency
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setRedemptionCurrency (
		final java.lang.String strRedemptionCurrency)
	{
		if (null == (_strRedemptionCurrency = strRedemptionCurrency)) return false;

		return true;
	}

	/**
	 * Sets the Coupon Currency
	 * 
	 * @param strCouponCurrency Coupon Currency
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCouponCurrency (
		final java.lang.String strCouponCurrency)
	{
		if (null == (_strCouponCurrency = strCouponCurrency)) return false;

		return true;
	}

	/**
	 * Sets the Trade Currency
	 * 
	 * @param strTradeCurrency Trade Currency
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setTradeCurrency (
		final java.lang.String strTradeCurrency)
	{
		if (null == (_strTradeCurrency = strTradeCurrency)) return false;

		return true;
	}

	/**
	 * Sets the Flag indicating Bearer Bond
	 * 
	 * @param strIsBearer Flag indicating Bearer Bond
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsBearer (
		final java.lang.String strIsBearer)
	{
		if (null == strIsBearer) _bIsBearer = false;

		if ("1".equalsIgnoreCase (strIsBearer))
			_bIsBearer = true;
		else
			_bIsBearer = false;

		return true;
	}

	/**
	 * Sets the Flag Registered
	 * 
	 * @param strIsRegistered Flag indicating Is Registered
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsRegistered (
		final java.lang.String strIsRegistered)
	{
		if (null == strIsRegistered) _bIsRegistered = false;

		if ("1".equalsIgnoreCase (strIsRegistered))
			_bIsRegistered = true;
		else
			_bIsRegistered = false;

		return true;
	}

	/**
	 * Sets the Flag indicating If bond has been called
	 * 
	 * @param strHasBeenCalled Flag indicating If bond has been called
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setHasBeenCalled (
		final java.lang.String strHasBeenCalled)
	{
		if (null == strHasBeenCalled) _bHasBeenCalled = false;

		if ("1".equalsIgnoreCase (strHasBeenCalled))
			_bHasBeenCalled = true;
		else
			_bHasBeenCalled = false;

		return true;
	}

	/**
	 * Sets the Issuer
	 * 
	 * @param strIssuer Issuer Name
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssuer (
		final java.lang.String strIssuer)
	{
		if (null == (_strIssuer = strIssuer)) _strIssuer = "";

		return true;
	}

	/**
	 * Sets the setPenultimateCouponDate
	 * 
	 * @param strPenultimateCouponDate setPenultimateCouponDate
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setPenultimateCouponDate (
		final java.lang.String strPenultimateCouponDate)
	{
		try {
			_dtPenultimateCouponDate =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
					(strPenultimateCouponDate.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Penultimate Coupon Date " + strPenultimateCouponDate + " for ISIN "
					+ _strISIN);
		}

		return false;
	}

	/**
	 * Set the Float Coupon Convention
	 * 
	 * @param strFloatCouponConvention Float Coupon Convention
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFloatCouponConvention (
		final java.lang.String strFloatCouponConvention)
	{
		if (null == (_strFloatCouponConvention = strFloatCouponConvention)) _strFloatCouponConvention = "";

		return true;
	}

	/**
	 * Set the Current Coupon
	 * 
	 * @param strCurrentCoupon Current Coupon
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCurrentCoupon (
		final java.lang.String strCurrentCoupon)
	{
		if (null == strCurrentCoupon || strCurrentCoupon.isEmpty() || "null".equalsIgnoreCase
			(strCurrentCoupon))
			_dblCurrentCoupon = 0.;
		else {
			try {
				_dblCurrentCoupon = new java.lang.Double (strCurrentCoupon.trim()).doubleValue();

				return true;
			} catch (java.lang.Exception e) {
				if (m_bBlog)
					System.out.println ("Bad Current Coupon " + strCurrentCoupon + " for ISIN " + _strISIN);
			}
		}

		return false;
	}

	/**
	 * Sets the Floater Flag
	 * 
	 * @param strIsFloater Flag indicating Is Floater
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsFloater (
		final java.lang.String strIsFloater)
	{
		if (null == strIsFloater) _bIsFloater = false;

		if ("1".equalsIgnoreCase (strIsFloater))
			_bIsFloater = true;
		else
			_bIsFloater = false;

		return true;
	}

	/**
	 * Set Trade Status
	 * 
	 * @param strTradeStatus Trade Status
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setTradeStatus (
		final java.lang.String strTradeStatus)
	{
		if (null == strTradeStatus) _bTradeStatus = false;

		if ("1".equalsIgnoreCase (strTradeStatus))
			_bTradeStatus = true;
		else
			_bTradeStatus = false;

		return true;
	}

	/**
	 * Set the CDR Country Code
	 * 
	 * @param strCDRCountryCode CDR Country Code
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCDRCountryCode (
		final java.lang.String strCDRCountryCode)
	{
		if (null == (_strCDRCountryCode = strCDRCountryCode)) _strCDRCountryCode = "";

		return true;
	}

	/**
	 * Set the CDR Settle Code
	 * 
	 * @param strCDRSettleCode CDR Settle Code
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCDRSettleCode (
		final java.lang.String strCDRSettleCode)
	{
		if (null == (_strCDRSettleCode = strCDRSettleCode)) _strCDRSettleCode = "";

		return true;
	}

	/**
	 * Set the Final Maturity
	 * 
	 * @param strFinalMaturity Final Maturity
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFinalMaturity (
		final java.lang.String strFinalMaturity)
	{
		try {
			_dtFinalMaturity = org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
				(strFinalMaturity.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog)
				System.out.println ("Bad Final Maturity " + strFinalMaturity + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Private Placement Flag
	 * 
	 * @param strIsPrivatePlacement Flag indicating Is Private Placement
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsPrivatePlacement (
		final java.lang.String strIsPrivatePlacement)
	{
		if (null == strIsPrivatePlacement) _bIsPrivatePlacement = false;

		if ("1".equalsIgnoreCase (strIsPrivatePlacement))
			_bIsPrivatePlacement = true;
		else
			_bIsPrivatePlacement = false;

		return true;
	}

	/**
	 * Sets the Perpetual Flag
	 * 
	 * @param strIsPerpetual Flag indicating Is Perpetual
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsPerpetual (
		final java.lang.String strIsPerpetual)
	{
		if (null == strIsPerpetual) _bIsPerpetual = false;

		if ("1".equalsIgnoreCase (strIsPerpetual))
			_bIsPerpetual = true;
		else
			_bIsPerpetual = false;

		return true;
	}

	/**
	 * Sets the Defaulted Flag
	 * 
	 * @param strIsDefaulted Flag indicating Is Defaulted
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIsDefaulted (
		final java.lang.String strIsDefaulted)
	{
		if (null == strIsDefaulted) _bIsDefaulted = false;

		if ("1".equalsIgnoreCase (strIsDefaulted))
			_bIsDefaulted = true;
		else
			_bIsDefaulted = false;

		return true;
	}

	/**
	 * Sets the Float Spread
	 * 
	 * @param strFloatSpread Float Spread
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFloatSpread (
		final java.lang.String strFloatSpread)
	{
		try {
			_dblFloatSpread = new java.lang.Double (strFloatSpread.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Float Spread " + strFloatSpread + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the Rate Index
	 * 
	 * @param strRateIndex Rate Index
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setRateIndex (
		final java.lang.String strRateIndex)
	{
		if (null == (_strRateIndex = strRateIndex)) _strRateIndex = "";

		return true;
	}

	/**
	 * Sets the Moodys Rating
	 * 
	 * @param strMoody Moodys Rating
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMoody (
		final java.lang.String strMoody)
	{
		if (null == (_strMoody = strMoody)) _strMoody = "";

		return true;
	}

	/**
	 * Sets the S&P Rating
	 * 
	 * @param strSnP S&P Rating
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setSnP (
		final java.lang.String strSnP)
	{
		if (null == (_strSnP = strSnP)) _strSnP = "";

		return true;
	}

	/**
	 * Sets the Fitch Rating
	 * 
	 * @param strFitch Fitch Rating
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setFitch (
		final java.lang.String strFitch)
	{
		if (null == (_strFitch = strFitch)) _strFitch = "";

		return true;
	}

	/**
	 * Sets Senior or Sub-ordinate
	 * 
	 * @param strSnrSub Senior or Sub-ordinate
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setSnrSub (
		final java.lang.String strSnrSub)
	{
		if (null == (_strSnrSub = strSnrSub)) _strSnrSub = "";

		return true;
	}

	/**
	 * Sets Issuer SPN
	 * 
	 * @param strIssuerSPN Issuer SPN
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssuerSPN (
		final java.lang.String strIssuerSPN)
	{
		if (null == (_strIssuerSPN = strIssuerSPN)) _strIssuerSPN = "";

		return true;
	}

	/**
	 * Sets Issue Price
	 * 
	 * @param strIssuePrice Issue Price
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setIssuePrice (
		final java.lang.String strIssuePrice)
	{
		try {
			_dblIssuePrice = new java.lang.Double (strIssuePrice.trim()).doubleValue();

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Issue Price " + strIssuePrice + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the coupon
	 * 
	 * @param strCoupon Coupon
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setCoupon (
		final java.lang.String strCoupon)
	{
		if (null == strCoupon || strCoupon.isEmpty() || "null".equalsIgnoreCase (strCoupon)) _dblCoupon = 0.;

		try {
			_dblCoupon = new java.lang.Double (strCoupon.trim());

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad coupon " + strCoupon + " for ISIN " + _strISIN);
		}

		return false;
	}

	/**
	 * Sets the maturity
	 * 
	 * @param strMaturity maturity
	 * 
	 * @return True (success), false (failure)
	 */

	public boolean setMaturity (
		final java.lang.String strMaturity)
	{
		try {
			if (null == (_dtMaturity =
				org.drip.analytics.support.AnalyticsHelper.MakeJulianDateFromBBGDate
					(strMaturity.trim())))
				return false;

			return true;
		} catch (java.lang.Exception e) {
			if (m_bBlog) System.out.println ("Bad Maturity " + strMaturity + " for ISIN " + _strISIN);
		}

		return false;
	}

	@Override public boolean validate()
	{
		if (null == _strISIN || _strISIN.isEmpty() || null == _strCUSIP || _strCUSIP.isEmpty()) {
			if (m_bDisplayWarnings)
				System.out.println ("Check ISIN[" + _strISIN + "] or CUSIP[" + _strCUSIP + "]");

			return false;
		}

		if (null == _dtInterestAccrualStart) {
			if (null == (_dtInterestAccrualStart = reconcileStartDate())) {
				if (m_bDisplayWarnings)
					System.out.println ("All possible date init candidates are null for ISIN " + _strISIN);

				return false;
			}
		}

		if (null == _dtFirstCoupon) _dtFirstCoupon = reconcileStartDate();

		if (null == _dtIssue) _dtIssue = reconcileStartDate();

		if (null == _dtFirstSettle) _dtFirstSettle = reconcileStartDate();

		if (null == _dtAnnounce) _dtAnnounce = reconcileStartDate();

		return true;
	}

	/**
	 * Creates an SQL Insert string for the given object
	 * 
	 * @return SQL Insert string
	 */

	public java.lang.String makeSQLInsert()
	{
		java.lang.StringBuilder sb = new java.lang.StringBuilder();

		sb.append ("insert into BondRefData values(");

		sb.append ("'").append (_strISIN).append ("', ");

		sb.append ("'").append (_strCUSIP).append ("', ");

		sb.append ("'").append (_strBBGID).append ("', ");

		sb.append ("'").append (_strIssuerCategory).append ("', ");

		sb.append ("'").append (_strTicker).append ("', ");

		sb.append ("'").append (_strSeries).append ("', ");

		sb.append ("'").append (_strName).append ("', ");

		sb.append ("'").append (_strShortName).append ("', ");

		sb.append ("'").append (_strIssuerIndustry).append ("', ");

		sb.append ("'").append (_strCouponType).append ("', ");

		sb.append ("'").append (_strMaturityType).append ("', ");

		sb.append ("'").append (_strCalculationType).append ("', ");

		sb.append ("'").append (_strDayCountCode).append ("', ");

		sb.append ("'").append (_strMarketIssueType).append ("', ");

		sb.append ("'").append (_strIssueCountryCode).append ("', ");

		sb.append ("'").append (_strIssueCountry).append ("', ");

		sb.append ("'").append (_strCollateralType).append ("', ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblIssueAmount))
			sb.append ("null, ");
		else
			sb.append (_dblIssueAmount).append (", ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblOutstandingAmount))
			sb.append ("null, ");
		else
			sb.append (_dblOutstandingAmount).append (", ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblMinimumPiece))
			sb.append ("null, ");
		else
			sb.append (_dblMinimumPiece).append (", ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblMinimumIncrement))
			sb.append ("null, ");
		else
			sb.append (_dblMinimumIncrement).append (", ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblParAmount))
			sb.append ("null, ");
		else
			sb.append (_dblParAmount).append (", ");

		sb.append ("'").append (_strLeadManager).append ("', ");

		sb.append ("'").append (_strExchangeCode).append ("', ");

		sb.append (_dblRedemptionValue).append (", ");

		sb.append ("'").append (_dtAnnounce.toOracleDate()).append ("', ");

		sb.append ("'").append (_dtFirstSettle.toOracleDate()).append ("', ");

		sb.append ("'").append (_dtFirstCoupon.toOracleDate()).append ("', ");

		sb.append ("'").append (_dtInterestAccrualStart.toOracleDate()).append ("', ");

		sb.append ("'").append (_dtIssue.toOracleDate()).append ("', ");

		if (null == _dtNextCouponDate)
			sb.append ("null, ");
		else
			sb.append ("'").append (_dtNextCouponDate.toOracleDate()).append ("', ");

		sb.append ("'").append (_bIsCallable ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsPutable ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsSinkable ? 1 : 0).append ("', ");

		sb.append ("'").append (_strBBGParent).append ("', "); // Done

		sb.append ("'").append (_strCountryOfIncorporation).append ("', ");

		sb.append ("'").append (_strIndustrySector).append ("', ");

		sb.append ("'").append (_strIndustryGroup).append ("', ");

		sb.append ("'").append (_strIndustrySubgroup).append ("', ");

		sb.append ("'").append (_strCountryOfGuarantor).append ("', ");

		sb.append ("'").append (_strCountryOfDomicile).append ("', ");

		sb.append ("'").append (_strDescription).append ("', ");

		sb.append ("'").append (_strSecurityType).append ("', ");

		if (null == _dtPrevCouponDate)
			sb.append ("null, ");
		else
			sb.append ("'").append (_dtPrevCouponDate.toOracleDate()).append ("', ");

		sb.append ("'").append (_strBBGUniqueID).append ("', ");

		sb.append ("'").append (_strLongCompanyName).append ("', ");

		sb.append ("'").append (_strRedemptionCurrency).append ("', ");

		sb.append ("'").append (_strCouponCurrency).append ("', ");

		sb.append ("'").append (_bIsStructuredNote ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsUnitTraded ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsReversibleConvertible ? 1 : 0).append ("', ");

		sb.append ("'").append (_strTradeCurrency).append ("', ");

		sb.append ("'").append (_bIsBearer ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsRegistered ? 1 : 0).append ("', ");

		sb.append ("'").append (_bHasBeenCalled ? 1 : 0).append ("', ");

		sb.append ("'").append (_strIssuer).append ("', ");

		if (null == _dtPenultimateCouponDate)
			sb.append ("null, ");
		else
			sb.append ("'").append (_dtPenultimateCouponDate.toOracleDate()).append ("', ");

		sb.append ("'").append (_strFloatCouponConvention).append ("', ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblCurrentCoupon))
			sb.append ("null, ");
		else
			sb.append (_dblCurrentCoupon).append (", ");

		sb.append ("'").append (_bIsFloater ? 1 : 0).append ("', ");

		sb.append ("'").append (_bTradeStatus ? 1 : 0).append ("', ");

		sb.append ("'").append (_strCDRCountryCode).append ("', ");

		sb.append ("'").append (_strCDRSettleCode).append ("', ");

		if (null == _dtFinalMaturity)
			sb.append ("null, ");
		else
			sb.append ("'").append (_dtFinalMaturity.toOracleDate()).append ("', ");

		sb.append ("'").append (_bIsPrivatePlacement ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsPerpetual ? 1 : 0).append ("', ");

		sb.append ("'").append (_bIsDefaulted ? 1 : 0).append ("', ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblFloatSpread))
			sb.append ("null, ");
		else
			sb.append (_dblFloatSpread).append (", ");

		sb.append ("'").append (_strRateIndex).append ("', ");

		sb.append ("'").append (_strMoody).append ("', ");

		sb.append ("'").append (_strSnP).append ("', ");

		sb.append ("'").append (_strFitch).append ("', ");

		sb.append ("'").append (_strSnrSub).append ("', ");

		sb.append ("'").append (_strIssuerSPN).append ("', ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblIssuePrice))
			sb.append ("null, ");
		else
			sb.append (_dblIssuePrice).append (", ");

		if (!org.drip.math.common.NumberUtil.IsValid (_dblCoupon))
			sb.append ("null, ");
		else
			sb.append (_dblCoupon).append (", ");

		if (null == _dtMaturity)
			sb.append ("null");
		else
			sb.append ("'").append (_dtMaturity.toOracleDate()).append ("'");

		return sb.append (")").toString();
	}

	/**
	 * Creates an SQL Delete string for the given object
	 * 
	 * @return SQL Delete string
	 */

	public java.lang.String makeSQLDelete()
	{
		java.lang.StringBuilder sb = new java.lang.StringBuilder();

		sb.append ("delete from BondRefData where ISIN = '").append (_strISIN).append
			("' or CUSIP = '").append (_strCUSIP).append ("'");

		return sb.toString();
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _strISIN || _strISIN.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strISIN + getFieldDelimiter());

		if (null == _strCUSIP || _strCUSIP.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCUSIP + getFieldDelimiter());

		if (null == _strBBGID || _strBBGID.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strBBGID + getFieldDelimiter());

		if (null == _strIssuerCategory || _strIssuerCategory.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssuerCategory + getFieldDelimiter());

		if (null == _strTicker || _strTicker.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strTicker + getFieldDelimiter());

		if (null == _strSeries || _strSeries.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strSeries + getFieldDelimiter());

		if (null == _strName || _strName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strName + getFieldDelimiter());

		if (null == _strShortName || _strShortName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strShortName + getFieldDelimiter());

		if (null == _strIssuerIndustry || _strIssuerIndustry.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssuerIndustry + getFieldDelimiter());

		if (null == _strCouponType || _strCouponType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCouponType + getFieldDelimiter());

		if (null == _strMaturityType || _strMaturityType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strMaturityType + getFieldDelimiter());

		if (null == _strCalculationType || _strCalculationType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCalculationType + getFieldDelimiter());

		if (null == _strDayCountCode || _strDayCountCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strDayCountCode + getFieldDelimiter());

		if (null == _strMarketIssueType || _strMarketIssueType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strMarketIssueType + getFieldDelimiter());

		if (null == _strIssueCountryCode || _strIssueCountryCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssueCountryCode + getFieldDelimiter());

		if (null == _strIssueCountry || _strIssueCountry.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssueCountry + getFieldDelimiter());

		if (null == _strCollateralType || _strCollateralType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCollateralType + getFieldDelimiter());

		sb.append (_dblIssueAmount + getFieldDelimiter() + _dblOutstandingAmount + getFieldDelimiter() +
			_dblMinimumPiece + getFieldDelimiter() + _dblMinimumIncrement + getFieldDelimiter() +
				_dblParAmount + getFieldDelimiter());

		if (null == _strLeadManager || _strLeadManager.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strLeadManager + getFieldDelimiter());

		if (null == _strExchangeCode || _strExchangeCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strExchangeCode + getFieldDelimiter());

		sb.append (_dblRedemptionValue + getFieldDelimiter());

		if (null == _dtAnnounce)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtAnnounce.getJulian() + getFieldDelimiter());

		if (null == _dtFirstSettle)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtFirstSettle.getJulian() + getFieldDelimiter());

		if (null == _dtFirstCoupon)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtFirstCoupon.getJulian() + getFieldDelimiter());

		if (null == _dtInterestAccrualStart)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtInterestAccrualStart.getJulian() + getFieldDelimiter());

		if (null == _dtIssue)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtIssue.getJulian() + getFieldDelimiter());

		if (null == _dtNextCouponDate)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtNextCouponDate.getJulian() + getFieldDelimiter());

		sb.append (_bIsCallable + getFieldDelimiter());

		sb.append (_bIsPutable + getFieldDelimiter());

		sb.append (_bIsSinkable + getFieldDelimiter());

		if (null == _strBBGParent || _strBBGParent.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strBBGParent + getFieldDelimiter());

		if (null == _strCountryOfIncorporation || _strCountryOfIncorporation.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCountryOfIncorporation + getFieldDelimiter());

		if (null == _strIndustrySector || _strIndustrySector.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIndustrySector + getFieldDelimiter());

		if (null == _strIndustryGroup || _strIndustryGroup.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIndustryGroup + getFieldDelimiter());

		if (null == _strIndustrySubgroup || _strIndustrySubgroup.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIndustrySubgroup + getFieldDelimiter());

		if (null == _strCountryOfGuarantor || _strCountryOfGuarantor.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCountryOfGuarantor + getFieldDelimiter());

		if (null == _strCountryOfDomicile || _strCountryOfDomicile.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCountryOfDomicile + getFieldDelimiter());

		if (null == _strDescription || _strDescription.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strDescription + getFieldDelimiter());

		if (null == _strSecurityType || _strSecurityType.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strSecurityType + getFieldDelimiter());

		if (null == _dtPrevCouponDate)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtPrevCouponDate.getJulian() + getFieldDelimiter());

		if (null == _strBBGUniqueID || _strBBGUniqueID.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strBBGUniqueID + getFieldDelimiter());

		if (null == _strLongCompanyName || _strLongCompanyName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strLongCompanyName + getFieldDelimiter());

		sb.append (_bIsStructuredNote + getFieldDelimiter());

		sb.append (_bIsUnitTraded + getFieldDelimiter());

		sb.append (_bIsReversibleConvertible + getFieldDelimiter());

		if (null == _strRedemptionCurrency || _strRedemptionCurrency.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strRedemptionCurrency + getFieldDelimiter());

		if (null == _strCouponCurrency || _strCouponCurrency.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCouponCurrency + getFieldDelimiter());

		if (null == _strTradeCurrency || _strTradeCurrency.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strTradeCurrency + getFieldDelimiter());

		sb.append (_bIsBearer + getFieldDelimiter());

		sb.append (_bIsRegistered + getFieldDelimiter());

		sb.append (_bHasBeenCalled + getFieldDelimiter());

		if (null == _strIssuer || _strIssuer.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssuer + getFieldDelimiter());

		if (null == _dtPenultimateCouponDate)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtPenultimateCouponDate.getJulian() + getFieldDelimiter());

		if (null == _strFloatCouponConvention || _strFloatCouponConvention.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strFloatCouponConvention + getFieldDelimiter());

		sb.append (_dblCurrentCoupon + getFieldDelimiter());

		sb.append (_bIsFloater + getFieldDelimiter());

		sb.append (_bTradeStatus + getFieldDelimiter());

		if (null == _strCDRCountryCode || _strCDRCountryCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCDRCountryCode + getFieldDelimiter());

		if (null == _strCDRSettleCode || _strCDRSettleCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCDRSettleCode + getFieldDelimiter());

		if (null == _dtFinalMaturity)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_dtFinalMaturity.getJulian() + getFieldDelimiter());

		sb.append (_bIsPrivatePlacement + getFieldDelimiter());

		sb.append (_bIsPerpetual + getFieldDelimiter());

		sb.append (_bIsDefaulted + getFieldDelimiter());

		sb.append (_dblFloatSpread + getFieldDelimiter());

		if (null == _strRateIndex || _strRateIndex.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strRateIndex + getFieldDelimiter());

		if (null == _strMoody || _strMoody.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strMoody + getFieldDelimiter());

		if (null == _strSnP || _strSnP.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strSnP + getFieldDelimiter());

		if (null == _strFitch || _strFitch.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strFitch + getFieldDelimiter());

		if (null == _strSnrSub || _strSnrSub.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strSnrSub + getFieldDelimiter());

		if (null == _strIssuerSPN || _strIssuerSPN.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIssuerSPN + getFieldDelimiter());

		sb.append (_dblIssuePrice + getFieldDelimiter());

		sb.append (_dblCoupon + getFieldDelimiter());

		if (null == _dtMaturity)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (_dtMaturity.getJulian());

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new BondRefDataBuilder (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		BondRefDataBuilder brdb = new BondRefDataBuilder();

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

		brdb._dtAnnounce = org.drip.analytics.date.JulianDate.Today();

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

		System.out.println (new java.lang.String (abBRDB));

		BondRefDataBuilder brdbDeser = new BondRefDataBuilder (abBRDB);

		System.out.println (new java.lang.String (brdbDeser.serialize()));
	}
}
