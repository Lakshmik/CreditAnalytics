
package org.drip.analytics.daycount;

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
 * This interface holds the DCC name and its year-fraction and the days accrued stubs.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface DCFCalculator {

	/**
	 * Retrieves the base calculation type corresponding to the DCF Calculator
	 * 
	 * @return Name of the base calculation type
	 */

	public abstract java.lang.String getBaseCalculationType();

	/**
	 * Retrieves the full set of alternate names corresponding to the DCF Calculator
	 * 
	 * @return Array of alternate names
	 */

	public abstract java.lang.String[] getAlternateNames();

	/**
	 * Calculates the accrual fraction in years between 2 given days
	 * 
	 * @param dblStart Start Date
	 * @param dblEnd End Date
	 * @param bApplyEOMAdj Apply end-of-month adjustment (true)
	 * @param dblMaturity Maturity Date
	 * @param actactParams ActActParams
	 * @param strCalendar Holiday Calendar
	 * 
	 * @return Accrual Fraction in years
	 * 
	 * @throws java.lang.Exception Thrown if the accrual fraction cannot be calculated
	 */

	public abstract double yearFraction (
		final double dblStart,
		final double dblEnd,
		final boolean bApplyEOMAdj,
		final double dblMaturity,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception;

	/**
	 * Calculates the number of days accrued between the two given days
	 * 
	 * @param dblStart Start Date
	 * @param dblEnd End Date
	 * @param bApplyEOMAdj Apply end-of-month adjustment (true)
	 * @param dblMaturity Maturity Date
	 * @param actactParams ActActParams
	 * @param strCalendar Holiday Calendar
	 * 
	 * @return Accrual Fraction in years
	 * 
	 * @throws java.lang.Exception Thrown if the accrual fraction cannot be calculated
	 */

	public abstract int daysAccrued (
		final double dblStart,
		final double dblEnd,
		final boolean bApplyEOMAdj,
		final double dblMaturity,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception;
}
