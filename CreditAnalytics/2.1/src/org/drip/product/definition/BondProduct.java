
package org.drip.product.definition;

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
 * This interface implements the product static data behind bonds of all kinds. Bond static data is
 * 		captured in a set of 11 container classes – BondTSYParams, BondCouponParams, BondNotionalParams,
 * 		BondFloaterParams, BondCurrencyParams, BondIdentifierParams, ComponentValuationParams,
 * 		ComponentRatesValuationParams, ComponentCreditValuationParams, ComponentTerminationEvent,
 *  	BondFixedPeriodParams, and one EmbeddedOptionSchedule object instance each for the call and the put
 *  	objects. Each of these parameter sets can be set separately.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface BondProduct {
	/**
	 * Sets the bond treasury benchmark
	 * 
	 * @param tsyBmk Bond treasury benchmark
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setTreasuryBenchmark (
		final org.drip.product.params.TreasuryBenchmark tsyBmk
	);

	/**
	 * Retrieves the bond treasury benchmark
	 * 
	 * @return Bond treasury benchmark
	 */

	public abstract org.drip.product.params.TreasuryBenchmark getTreasuryBenchmark();

	/**
	 * Sets the bond identifier set
	 * 
	 * @param idSet Bond identifier set
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setIdentifierSet (
		final org.drip.product.params.IdentifierSet idSet
	);

	/**
	 * Retrieves the bond identifier set
	 * 
	 * @return Bond identifier set
	 */

	public abstract org.drip.product.params.IdentifierSet getIdentifierSet();

	/**
	 * Sets the bond coupon setting
	 * 
	 * @param cpnSetting Bond coupon setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setCouponSetting (
		final org.drip.product.params.CouponSetting cpnSetting
	);

	/**
	 * Retrieves the bond coupon setting
	 * 
	 * @return Bond Coupon setting
	 */

	public abstract org.drip.product.params.CouponSetting getCouponSetting();

	/**
	 * Sets the bond currency set
	 * 
	 * @param ccySet Bond currency set
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setCurrencySet (
		final org.drip.product.params.CurrencySet ccySet
	);

	/**
	 * Retrieves the bond currency set
	 * 
	 * @return Bond Currency Set
	 */

	public abstract org.drip.product.params.CurrencySet getCurrencyParams();

	/**
	 * Sets the bond floater setting
	 * 
	 * @param fltSetting Bond floater setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setFloaterSetting (
		final org.drip.product.params.FloaterSetting fltSetting
	);

	/**
	 * Retrieves the bond floater setting
	 * 
	 * @return Bond Floater setting
	 */

	public abstract org.drip.product.params.FloaterSetting getFloaterSetting();

	/**
	 * Sets the bond fixings
	 * 
	 * @param mmFixings Bond fixings
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setFixings (
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixings
	);

	/**
	 * Retrieves the bond fixings
	 * 
	 * @return Bond fixings
	 */

	public abstract java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
		java.lang.Double>> getFixings();

	/**
	 * Sets the Bond's Market Convention
	 * 
	 * @param mktConv Bond's Market Convention
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setMarketConvention (
		final org.drip.product.params.QuoteConvention mktConv
	);

	/**
	 * Retrieves the Bond's Market Convention
	 * 
	 * @return Bond's Market Convention
	 */

	public abstract org.drip.product.params.QuoteConvention getMarketConvention();

	/**
	 * Sets the Bond Rates Setting
	 * 
	 * @param ratesSetting Bond Rates Setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setRatesSetting (
		final org.drip.product.params.RatesSetting ratesSetting
	);

	/**
	 * Retrieves the Bond Rates Setting
	 * 
	 * @return Bond Rates Setting
	 */

	public abstract org.drip.product.params.RatesSetting setRatesSetting();

	/**
	 * Sets the bond Credit Setting
	 * 
	 * @param creditSetting Bond credit Setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setCreditSetting (
		final org.drip.product.params.CreditSetting creditSetting
	);

	/**
	 * Retrieves the bond credit Setting
	 * 
	 * @return Bond credit Setting
	 */

	public abstract org.drip.product.params.CreditSetting getCreditSetting();

	/**
	 * Sets the bond termination setting
	 * 
	 * @param termSetting Bond termination setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setTerminationSetting (
		final org.drip.product.params.TerminationSetting termSetting
	);

	/**
	 * Retrieves the bond termination setting
	 * 
	 * @return Bond termination setting
	 */

	public abstract org.drip.product.params.TerminationSetting getTerminationSetting();

	/**
	 * Sets the bond Period Set
	 * 
	 * @param periodSet Bond Period Set
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setPeriodSet (
		final org.drip.product.params.PeriodSet periodSet
	);

	/**
	 * Retrieves the bond period Set
	 * 
	 * @return Bond period Set
	 */

	public abstract org.drip.product.params.PeriodSet getPeriodSet();

	/**
	 * Sets the bond notional Setting
	 * 
	 * @param notlSetting Bond Notional Setting
	 * 
	 * @return True if succeeded
	 */

	public abstract boolean setNotionalSetting (
		final org.drip.product.params.NotionalSetting notlSetting
	);

	/**
	 * Retrieves the bond notional Setting
	 * 
	 * @return Bond notional Setting
	 */

	public abstract org.drip.product.params.NotionalSetting getNotionalSetting();

	/**
	 * Sets the bond's embedded call schedule
	 * 
	 * @param eos Bond's embedded call schedule
	 */

	public abstract void setEmbeddedCallSchedule (
		final org.drip.product.params.EmbeddedOptionSchedule eos
	);

	/**
	 * Retrieves the bond embedded call schedule parameters
	 * 
	 * @return Bond embedded call schedule parameters
	 */

	public abstract org.drip.product.params.EmbeddedOptionSchedule getEmbeddedCallSchedule();

	/**
	 * Sets the bond's embedded put schedule
	 * 
	 * @param eos Bond's embedded put schedule
	 */

	public abstract void setEmbeddedPutSchedule (
		final org.drip.product.params.EmbeddedOptionSchedule eos
	);

	/**
	 * Retrieves the bond embedded put schedule parameters
	 * 
	 * @return Bond embedded put schedule parameters
	 */

	public abstract org.drip.product.params.EmbeddedOptionSchedule getEmbeddedPutSchedule();
}
