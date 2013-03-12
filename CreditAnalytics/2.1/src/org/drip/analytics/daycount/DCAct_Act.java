
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
 * This class implements the Act/Act day count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DCAct_Act implements org.drip.analytics.daycount.DCFCalculator {

	/**
	 * Empty DCAct_Act constructor
	 */

	public DCAct_Act()
	{
	}

	@Override public java.lang.String getBaseCalculationType()
	{
		return "DCAct_Act";
	}

	@Override public java.lang.String[] getAlternateNames() {
		return new java.lang.String[] {"Actual/Actual", "Actual/Actual ICMA", "Act/Act", "Act/Act ICMA",
			"ISMA-99", "Act/Act ISMA", "DCAct_Act"};
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
		double dblDCF = 0.;
		int iNumLeapDays = 0;
		int iNumNonLeapDays = 0;

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblStart))
			iNumLeapDays += org.drip.analytics.date.JulianDate.DaysRemaining (dblStart);
		else
			iNumNonLeapDays += org.drip.analytics.date.JulianDate.DaysRemaining (dblStart);

		int iYearStart = org.drip.analytics.date.JulianDate.Year (dblStart);

		int iYearEnd = org.drip.analytics.date.JulianDate.Year (dblEnd);

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblEnd))
			iNumLeapDays += org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd);
		else
			iNumNonLeapDays += org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd);

		if (iYearEnd == iYearStart) {
			if (!org.drip.analytics.date.JulianDate.IsLeapYear (dblStart))
				return 1. * (iNumNonLeapDays - 365) / 365.;

			return 1. * (iNumLeapDays - (org.drip.analytics.date.JulianDate.ContainsFeb29 (dblStart,
				dblEnd, org.drip.analytics.date.JulianDate.RIGHT_INCLUDE) ? 365 : 365)) / 366.;
		}

		for (int iYear = iYearStart + 1; iYear < iYearEnd; ++iYear) {
			double dblYear = org.drip.analytics.date.JulianDate.toJulian (iYear,
				org.drip.analytics.date.JulianDate.JANUARY, 1);

			if (org.drip.analytics.date.JulianDate.IsLeapYear (dblYear))
				iNumLeapDays += 366;
			else
				iNumNonLeapDays += 365;
		}

		return dblDCF + (((double) (iNumLeapDays)) / 366.) + (((double) (iNumNonLeapDays)) / 365.);
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
		int iDaysOC = 0;
		int iNumLeapDays = 0;
		int iNumNonLeapDays = 0;

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblStart))
			iNumLeapDays += org.drip.analytics.date.JulianDate.DaysRemaining (dblStart);
		else
			iNumNonLeapDays += org.drip.analytics.date.JulianDate.DaysRemaining (dblStart);

		int iYearStart = org.drip.analytics.date.JulianDate.Year (dblStart);

		int iYearEnd = org.drip.analytics.date.JulianDate.Year (dblEnd);

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblEnd))
			iNumLeapDays += org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd);
		else
			iNumNonLeapDays += org.drip.analytics.date.JulianDate.DaysElapsed (dblEnd);

		if (!org.drip.analytics.date.JulianDate.IsLeapYear (dblStart))
			iDaysOC -= 365;
		else
			iDaysOC -= (org.drip.analytics.date.JulianDate.ContainsFeb29 (dblStart, dblEnd,
				org.drip.analytics.date.JulianDate.RIGHT_INCLUDE) ? 365 : 366);

		if (iYearEnd == iYearStart) return iDaysOC;

		for (int iYear = iYearStart + 1; iYear < iYearEnd; ++iYear) {
			double dblYear = org.drip.analytics.date.JulianDate.toJulian (iYear,
				org.drip.analytics.date.JulianDate.JANUARY, 1);

			if (org.drip.analytics.date.JulianDate.IsLeapYear (dblYear))
				iNumLeapDays += 366;
			else
				iNumNonLeapDays += 365;
		}

		return iDaysOC + iNumLeapDays + iNumNonLeapDays;
	}
}
