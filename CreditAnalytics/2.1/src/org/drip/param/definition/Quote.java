
package org.drip.param.definition;

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
 * 	This interface contains the stubs corresponding to a product quote. It contains the quote value, quote
 *   instant for the different quote sides (bid/ask/mid).
 *   
 * @author Lakshmi Krishnamurthy
 */

public abstract class Quote extends org.drip.service.stream.Serializer {
	/**
	 * Get the quote value for the given side
	 * 
	 * @param strSide bid/ask/mid
	 * 
	 * @return Quote Value
	 */

	public abstract double getQuote (
		final java.lang.String strSide);

	/**
	 * Get the time of the quote
	 * 
	 * @param strSide bid/ask/mid
	 * 
	 * @return DateTime
	 */

	public abstract org.drip.analytics.date.DateTime getQuoteTime (
		final java.lang.String strSide);

	/**
	 * Set the quote for the specified side
	 * 
	 * @param strSide bid/ask/mid
	 * @param dblQuote Quote value
	 * 
	 * @return Success (true) or failure (false)
	 */

	public abstract boolean setSide (
		final java.lang.String strSide,
		final double dblQuote);
}
