
package org.drip.product.params;

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
 *  This class implements the parameters used to create the standard CDX - coupon, components, and currency.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class StandardCDXParams {
	public int _iNumComponents = 0;
	public java.lang.String _strCurrency = "";
	public double _dblCoupon = java.lang.Double.NaN;

	/**
	 * Create the Standard CDX Parameters object using the components, the currency, and the coupon
	 * 
	 * @param iNumComponents CDX Components
	 * @param strCurrency CDX Currency
	 * @param dblCoupon CDX Coupon
	 * 
	 * @throws java.lang.Exception
	 */

	public StandardCDXParams (
		final int iNumComponents,
		final java.lang.String strCurrency,
		final double dblCoupon)
		throws java.lang.Exception
	{
		if (null == (_strCurrency = strCurrency) || _strCurrency.isEmpty() ||
			!org.drip.math.common.NumberUtil.IsValid (_dblCoupon = dblCoupon))
			throw new java.lang.Exception ("Invalid parameters into StandardCDXParams!");

		_iNumComponents = iNumComponents;
	}
}
