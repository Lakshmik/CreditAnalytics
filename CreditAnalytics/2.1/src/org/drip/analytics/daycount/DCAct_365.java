
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
 * This class implements the Act/365 day count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DCAct_365 implements org.drip.analytics.daycount.DCFCalculator {

	/**
	 * Empty DCAct_365 constructor
	 */

	public DCAct_365()
	{
	}

	@Override public java.lang.String getBaseCalculationType()
	{
		return "DCAct_365";
	}

	@Override public java.lang.String[] getAlternateNames() {
		return new java.lang.String[] {"Actual/365 Fixed", "Act/365 Fixed", "A/365 Fixed", "A/365F",
			"English", "Act/365", "DCAct_365"};
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
			throw new java.lang.Exception ("DCAct_365.yearFraction: Cannot create DateEOMAdjustment!");

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
		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA (dblStart, dblEnd, dblMaturity, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DCAct_365.daysAccrued: Cannot create DateEOMAdjustment!");

		return (int) (dblEnd - dblStart + dm._iD2Adj - dm._iD1Adj);
	}
}
