
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
 * This base abstract class implements the pricing, the valuation, and the RV analytics functionality for the
 * 		CDS product.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class CreditDefaultSwap extends org.drip.product.definition.CreditComponent {
	/**
	 * Resets the CDS's coupon
	 * 
	 * @param dblCoupon The new Coupon
	 * 
	 * @return The old Coupon
	 * 
	 * @throws java.lang.Exception Thrown if the coupon cannot be reset
	 */

	public abstract double resetCoupon (
		final double dblCoupon)
		throws java.lang.Exception;

	/**
	 * Calibrates the CDS's flat spread from the calculated up-front points
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mktParams ComponentMarketParams
	 * 
	 * @return Calibrated flat spread
	 * 
	 * @throws java.lang.Exception Thrown if cannot calibrate
	 */

	public abstract double calibFlatSpread (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
		throws java.lang.Exception;
}
