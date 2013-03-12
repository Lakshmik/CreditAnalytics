
package org.drip.analytics.definition;

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
 * This abstract class exposes the FXBasis curve representing term structure of FX basis. Basis can be full
 * 		or bootstrapped.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class FXBasisCurve extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {

	/**
	 * Returns the currency pair instance
	 * 
	 * @return CurrencyPair object instance
	 */

	public abstract org.drip.product.params.CurrencyPair getCurrencyPair();

	/**
	 * Returns the Spot Date
	 * 
	 * @return Spot Date
	 */

	public abstract org.drip.analytics.date.JulianDate getSpotDate();

	/**
	 * Gets the FX Spot
	 * 
	 * @return FX Spot
	 */

	public abstract double getFXSpot();

	/**
	 * Returns if the inputs are for bootstrapped FX basis
	 * 
	 * @return True if the inputs are for bootstrapped FX basis
	 */

	public abstract boolean IsBasisBootstrapped();

	/**
	 * Returns the array of full FX Forwards
	 * 
	 * @param valParam ValuationParams
	 * @param dcNum Discount Curve Numerator
	 * @param dcDenom Discount Curve Numerator
	 * @param bBasisOnDenom True if the basis is on the denominator
	 * @param bFwdAsPIP True if the FX Forwards are to represented as PIP
	 * 
	 * @return Array of FXForward
	 */

	public abstract double[] getFullFXFwd (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom,
		final boolean bFwdAsPIP);
}
