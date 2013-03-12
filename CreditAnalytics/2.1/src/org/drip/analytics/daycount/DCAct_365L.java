
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
 * This class implements the Act/365L day count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DCAct_365L implements org.drip.analytics.daycount.DCFCalculator {

	/**
	 * Empty DCAct_365L constructor
	 */

	public DCAct_365L()
	{
	}

	@Override public java.lang.String getBaseCalculationType()
	{
		return "DCAct_365L";
	}

	@Override public java.lang.String[] getAlternateNames() {
		return new java.lang.String[] {"Act/365L", "Actual/365L", "ISMA-Year", "Actual/Actual AFB",
			"DCAct_365L"};
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
		if (null == actactParams)
			throw new java.lang.Exception ("DCAct_365L.yearFraction: Invalid actact Params!");

		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA (dblStart, dblEnd, dblMaturity, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DCAct_365L.yearFraction: Cannot create DateEOMAdjustment!");

		if (1 == actactParams._iFreq) {
			if (org.drip.analytics.date.JulianDate.ContainsFeb29 (dblStart, dblEnd,
				org.drip.analytics.date.JulianDate.RIGHT_INCLUDE))
				return (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj) / 366.;

			return (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj) / 365.;
		}

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblEnd))
			return (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj) / 366.;

		return (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj) / 365.;
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
		if (null == actactParams)
			throw new java.lang.Exception ("DCAct_365L.daysAccrued: Invalid actact Params!");

		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA (dblStart, dblEnd, dblMaturity, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DCAct_365L.daysAccrued: Cannot create DateEOMAdjustment!");

		if (1 == actactParams._iFreq) {
			if (org.drip.analytics.date.JulianDate.ContainsFeb29 (dblStart, dblEnd,
				org.drip.analytics.date.JulianDate.RIGHT_INCLUDE))
				return (int) (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj);

			return (int) (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj);
		}

		if (org.drip.analytics.date.JulianDate.IsLeapYear (dblEnd))
			return (int) (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj);

		return (int) (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj);
	}
}
