
package org.drip.analytics.creator;

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
 * This class contains the baseline FX Forward curve builder object. It contains static functions that build
 * 		FX Forward curves from the different inputs.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FXForwardCurveBuilder {
	/**
	 * Creates an FXForwardCurve from the CurrencyPair, FX Spot, and the FX Forward parameters
	 * 
	 * @param cp CurrencyPair
	 * @param dtSpot Spot Date
	 * @param dblFXSpot FX Spot Rate
	 * @param adblDate Array of dates
	 * @param adblFXFwd Array of FX Forwards
	 * @param abIsPIP Array of PIP indicators
	 * 
	 * @throws FXForwardCurve Instance
	 */

	public static final org.drip.analytics.definition.FXForwardCurve CreateFXForwardCurve (
			final org.drip.product.params.CurrencyPair cp,
			final org.drip.analytics.date.JulianDate dtSpot,
			final double dblFXSpot,
			final double[] adblDate,
			final double[] adblFXFwd,
			final boolean[] abIsPIP)
	{
		try {
			return new org.drip.analytics.curve.DerivedFXForward (cp, dtSpot, dblFXSpot, adblDate,
				adblFXFwd, abIsPIP);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates the FXForwardCurve from the given byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return FXForwardCurve instance
	 */

	public static final org.drip.analytics.definition.FXForwardCurve FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.analytics.curve.DerivedFXForward (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
