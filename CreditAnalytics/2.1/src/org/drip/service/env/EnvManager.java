
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
 * EnvManager sets the environment/connection parameters, and populates the market parameters for the given
 *  EOD
 * 
 * @author Lakshmi Krishnamurthy
 */

public class EnvManager {
	private static final boolean s_bBuildCC = true;

	/**
	 * Initializes the logger, the database connections, the day count parameters, and day count objects
	 * 
	 * @param strConfig String representing the full path of the configuration file
	 * 
	 * @return SQL Statement representing the initialized object.
	 */

	public static final java.sql.Statement InitEnv (
		final java.lang.String strConfig)
	{
		org.drip.analytics.support.Logger.Init (strConfig);

		org.drip.analytics.daycount.Convention.Init (strConfig);

		if (!org.drip.service.env.StandardCDXManager.InitStandardCDXSeries()) {
			System.out.println ("Cannot Initialize standard CDX indices!");

			return null;
		}

		java.sql.Statement stmt = org.drip.param.config.ConfigLoader.OracleInit (strConfig);

		System.out.println ("Stmt: " + stmt);

		return stmt;
	}

	/**
	 * Populates the MarketParams with the closing discount curves, closing credit curves, and other
	 *  market objects for the given EOD
	 *  
	 * @param stmt SQL Statement representing the executable query
	 * @param dt EOD
	 * 
	 * @return The MarkertParamsContainer
	 */

	public static final org.drip.param.definition.MarketParams PopulateMPC (
		final java.sql.Statement stmt,
		final org.drip.analytics.date.JulianDate dt)
	{
		org.drip.param.definition.MarketParams mpc =
			org.drip.param.creator.MarketParamsBuilder.CreateMarketParams();

		if (!RatesManager.LoadFullIRCurves (mpc, stmt, dt)) return null;

		if (s_bBuildCC && !CDSManager.LoadFullCreditCurves (mpc, stmt, dt)) return null;

		return mpc;
	}
}
