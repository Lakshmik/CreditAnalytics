
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
 * This class contains the spline segment in-elastic fields - in this case the start/end ranges.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Inelastics implements java.lang.Comparable<Inelastics> {
	private double _dblLeft = java.lang.Double.NaN;
	private double _dblRight = java.lang.Double.NaN;

	/**
	 * Inelastics constructor
	 * 
	 * @param dblLeft Spline Left Ordinate
	 * @param dblRight Spline Right Ordinate
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public Inelastics (
		final double dblLeft,
		final double dblRight)
		throws java.lang.Exception {
		if (!org.drip.math.common.NumberUtil.IsValid (_dblLeft = dblLeft) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblRight = dblRight) || _dblLeft >= _dblRight) {
			System.out.println (_dblLeft + "=>" + _dblRight);

			throw new java.lang.Exception ("Inelastics ctr: Invalid inputs!");
		}
	}

	/**
	 * Retrieve the left ordinate
	 * 
	 * @return Left Ordinate
	 */

	public double getLeft() {
		return _dblLeft;
	}

	/**
	 * Retrieve the right Ordinate
	 * 
	 * @return Right Ordinate
	 */

	public double getRight() {
		return _dblRight;
	}

	/**
	 * Finds out if the point is inside the segment - left/right is inclusive.
	 * 
	 * @param dblPoint Point
	 * 
	 * @return TRUE => Point is inside the segment
	 */

	public boolean isInSegment (
		final double dblPoint)
	{
		return org.drip.math.common.NumberUtil.IsValid (dblPoint) && _dblLeft <= dblPoint && _dblRight >=
			dblPoint;
	}

	/**
	 * Gets the normalizing width of the ordinate
	 * 
	 * @return Width
	 */

	public double getSpan()
	{
		return _dblRight - _dblLeft;
	}

	/**
	 * Transforms the point to the normalized domain ordinate
	 * 
	 * @param dblPoint Point
	 * 
	 * @return Ordinate normalized to domain dimensions
	 * 
	 * @throws java.lang.Exception Thrown if the input is invalid
	 */

	public double calcNormalizedOrdinate (
		final double dblPoint)
		throws java.lang.Exception
	{
		if (!isInSegment (dblPoint))
			throw new java.lang.Exception ("Inelastics.calcNormalizedOrdinate: Invalid inputs!");

		return (dblPoint - _dblLeft) / (_dblRight - _dblLeft);
	}

	@Override public int hashCode()
	{
		long lBits = java.lang.Double.doubleToLongBits ((int) _dblLeft);

		return (int) (lBits ^ (lBits >>> 32));
	}

	@Override public int compareTo (
		final org.drip.math.grid.Inelastics ieOther)
	{
		if (_dblLeft > ieOther._dblLeft) return 1;

		if (_dblLeft < ieOther._dblLeft) return -1;

		return 0;
	}
}
