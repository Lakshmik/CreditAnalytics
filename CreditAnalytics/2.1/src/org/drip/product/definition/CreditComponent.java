
package org.drip.product.definition;

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
 *  Base abstract class that extends CalibratableComponent on top of which all credit components are
 *  	implemented.
 *  
 * @author Lakshmi Krishnamurthy
 */

public abstract class CreditComponent extends org.drip.product.definition.CalibratableComponent {
	/**
	 * Gets the coupon flow for the credit component
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mktParams Component Market Params
	 * 
	 * @return List of ProductCouponPeriodCurveMeasures
	 */

	public abstract java.util.List<org.drip.analytics.period.CouponPeriodCurveFactors> getCouponFlow (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams);

	/**
	 * Generates the loss flow for the credit component based on the pricer parameters
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mktParams ComponentMarketParams
	 * 
	 * @return List of ProductLossPeriodCurveMeasures
	 */

	public abstract java.util.List<org.drip.analytics.period.LossPeriodCurveFactors> getLossFlow (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams);

	/**
	 * Get the recovery of the credit component for the given date
	 * 
	 * @param dblDate Double JulianDate
	 * @param cc Credit Curve
	 * 
	 * @return Recovery
	 * 
	 * @throws java.lang.Exception Thrown if recovery cannot be calculated
	 */

	public abstract double getRecovery (
		final double dblDate,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception;

	/**
	 * Get the time-weighted recovery of the credit component between the given dates
	 * 
	 * @param dblDate1 Double JulianDate
	 * @param dblDate2 Double JulianDate
	 * @param cc Credit Curve
	 * 
	 * @return Recovery
	 * 
	 * @throws java.lang.Exception Thrown if recovery cannot be calculated
	 */

	public abstract double getRecovery (
		final double dblDate1,
		final double dblDate2,
		final org.drip.analytics.definition.CreditCurve cc)
		throws java.lang.Exception;

	/**
	 * Get the credit component's Credit Valuation Parameters
	 * 
	 * @return CompCRValParams
	 */

	public abstract org.drip.product.params.CreditSetting getCRValParams();
}
