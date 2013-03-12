
package org.drip.math.spline;

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
 * This class contains the basis spline segment fields - the in-elastic ordinates, and the elastic
 * 		calibratables. Interpolating segments function are implemented in the corresponding spline classes.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BasisSplineSegment {
	private org.drip.math.spline.InelasticOrdinates _io = null;
	private org.drip.math.spline.ElasticCoefficients _ec = null;

	/**
	 * BSplineSegment constructor
	 * 
	 * @param io Inelastic Ordinates
	 * @param ec Elastic Coefficients
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public BasisSplineSegment (
		final org.drip.math.spline.InelasticOrdinates io,
		final org.drip.math.spline.ElasticCoefficients ec)
		throws java.lang.Exception {
		if (null == (_io = io) || null == (_ec = ec))
			throw new java.lang.Exception ("BSplineSegment ctr: Invalid inputs!");
	}

	/**
	 * Retrieve the segment in-elastics
	 * 
	 * @return Segment in-elastics
	 */

	public org.drip.math.spline.InelasticOrdinates getInelastics() {
		return _io;
	}

	/**
	 * Retrieve the segment elastics
	 * 
	 * @return Segment elastics
	 */

	public org.drip.math.spline.ElasticCoefficients getElastics() {
		return _ec;
	}

	/**
	 * Display the string representation for diagnostic purposes
	 * 
	 * @return The string representation
	 */

	public java.lang.String displayString()
	{
		org.drip.math.spline.InelasticOrdinates io = getInelastics();

		org.drip.math.spline.ElasticCoefficients ec = getElastics();

		try {
			return "[" + new org.drip.analytics.date.JulianDate (io.getLeft()) + "->" + new
				org.drip.analytics.date.JulianDate (io.getRight()) + "] A=" +
					org.drip.analytics.support.GenericUtil.FormatPrice (ec.getIndexedCoefficient (3, true),
						4, 4, 1.) + ", B=" + org.drip.analytics.support.GenericUtil.FormatPrice
							(ec.getIndexedCoefficient (2, true), 4, 4, 1.) + ", C=" +
								org.drip.analytics.support.GenericUtil.FormatPrice (ec.getIndexedCoefficient
									(1, true), 4, 4, 1.) + ", D=" +
										org.drip.analytics.support.GenericUtil.FormatPrice
											(ec.getIndexedCoefficient (0, true), 4, 4, 1.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
