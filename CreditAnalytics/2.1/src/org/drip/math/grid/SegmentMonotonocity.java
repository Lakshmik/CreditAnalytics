
package org.drip.math.grid;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * This class contains the monotonicity details related to the given segment.
 *
 * @author Lakshmi Krishnamurthy
 */

public class SegmentMonotonocity {

	/**
	 * MONOTONIC
	 */

	public static final int MONOTONIC = 2;

	/**
	 * NON-MONOTONIC
	 */

	public static final int NON_MONOTONIC = 4;

	/**
	 * NON MONOTONE - MINIMA
	 */

	public static final int MINIMA = 5;

	/**
	 * NON MONOTONE - MAXIMA
	 */

	public static final int MAXIMA = 6;

	/**
	 * NON MONOTONE - INFLECTION
	 */

	public static final int INFLECTION = 7;

	private int _iMonotoneType = -1;

	/**
	 * SegmentMonotonocity constructor
	 * 
	 * @param iMonotoneType One of the valid monotone types
	 * 
	 * @throws java.lang.Exception Thrown if the input monotone type is invalid
	 */

	public SegmentMonotonocity (
		final int iMonotoneType)
		throws java.lang.Exception
	{
		if (org.drip.math.grid.SegmentMonotonocity.MONOTONIC != (_iMonotoneType = iMonotoneType) &&
			org.drip.math.grid.SegmentMonotonocity.NON_MONOTONIC != _iMonotoneType &&
				org.drip.math.grid.SegmentMonotonocity.MINIMA != _iMonotoneType &&
					org.drip.math.grid.SegmentMonotonocity.MAXIMA != _iMonotoneType &&
						org.drip.math.grid.SegmentMonotonocity.INFLECTION != _iMonotoneType)
			throw new java.lang.Exception ("SegmentMonotonocity ctr: Unknown monotone type " +
				_iMonotoneType);
	}

	/**
	 * Retrieve the Monotone Type
	 * 
	 * @return The Monotone Type
	 */

	public int type()
	{
		return _iMonotoneType;
	}

	@Override public java.lang.String toString()
	{
		if (org.drip.math.grid.SegmentMonotonocity.NON_MONOTONIC == _iMonotoneType) return "NON_MONOTIC";

		if (org.drip.math.grid.SegmentMonotonocity.MONOTONIC == _iMonotoneType) return "MONOTONIC";

		if (org.drip.math.grid.SegmentMonotonocity.MINIMA == _iMonotoneType) return "MINIMA";

		if (org.drip.math.grid.SegmentMonotonocity.MAXIMA == _iMonotoneType) return "MAXIMA";

		return "INFLECTION";
	}
}
