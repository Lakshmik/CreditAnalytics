
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
 * This class implements the Act/364 day count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DCAct_364 implements org.drip.analytics.daycount.DCFCalculator {

	/**
	 * Empty DCAct_364 constructor
	 */

	public DCAct_364()
	{
	}

	@Override public java.lang.String getBaseCalculationType()
	{
		return "DCAct_364";
	}

	@Override public java.lang.String[] getAlternateNames() {
		return new java.lang.String[] {"Act/364", "DCAct_364"};
	}

	@Override public double yearFraction (
		final double dblStart,
		final double dblEnd,
		final boolean bApplyEOMAdj,
		final double dblMaturity,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA (dblStart, dblEnd, dblMaturity, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DCAct_364.yearFraction: Cannot create DateEOMAdjustment!");

		return (org.drip.analytics.date.JulianDate.DaysRemaining (dblStart) /
			(org.drip.analytics.date.JulianDate.IsLeapYear (dblStart) ? 366. : 364.)) +
				(org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd) /
					(org.drip.analytics.date.JulianDate.IsLeapYear (dblEnd) ? 366. : 364.) + dm._iD2Adj -
						dm._iD1Adj) + org.drip.analytics.date.JulianDate.Year (dblEnd) -
							org.drip.analytics.date.JulianDate.Year (dblStart) - 1;
	}

	@Override public int daysAccrued (
		final double dblStart,
		final double dblEnd,
		final boolean bApplyEOMAdj,
		final double dblMaturity,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA (dblStart, dblEnd, dblMaturity, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DCAct_364.daysAccrued: Cannot create DateEOMAdjustment!");

		int iDaysOC = 364;

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblStart))
			iDaysOC = (org.drip.analytics.date.JulianDate.ContainsFeb29 (dblStart, dblEnd,
				org.drip.analytics.date.JulianDate.RIGHT_INCLUDE) ? 364 : 366);

		return (org.drip.analytics.date.JulianDate.DaysRemaining (dblStart) +
			org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd) + dm._iD2Adj - dm._iD1Adj) +
				org.drip.analytics.date.JulianDate.Year (dblEnd) -
					org.drip.analytics.date.JulianDate.Year (dblStart) - iDaysOC;
	}
}
