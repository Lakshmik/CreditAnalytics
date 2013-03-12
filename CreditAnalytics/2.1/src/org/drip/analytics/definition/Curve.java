
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
 * This is the interface defining the core bootstrapping methods – setting/bumping specific nodes, setting
 * 		flat values across all nodes, retrieving specific/collective instrument/node quotes.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface Curve {

	/**
	 * Initialize the Calibration Run with the Left Slope 
	 * 
	 * @param dblLeftSlope Left Slope
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean initializeCalibrationRun (
		final double dblLeftSlope);

	/**
	 * Retrieve the number of calibration nodes 
	 * 
	 * @return Number of calibration nodes
	 */

	public abstract int numCalibNodes();

	/**
	 * Set the Value/Slope at the Node specified by the Index
	 * 
	 * @param iIndex Node Index
	 * @param dblValue Node Value
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean setNodeValue (
		final int iIndex,
		final double dblValue);

	/**
	 * Bump the node value at the node specified the index by the value
	 * 
	 * @param iIndex node index
	 * @param dblValue node bump value
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean bumpNodeValue (
		final int iIndex,
		final double dblValue);

	/**
	 * Set the flat value across all the nodes
	 * 
	 * @param dblValue node value
	 * 
	 * @return Success (true), failure (false)
	 */

	public abstract boolean setFlatValue (
		final double dblValue);

	/**
	 * Get the display String - mostly for informational purposes
	 * 
	 * @return Stringified node=rate pairs
	 */

	public abstract java.lang.String displayString();

	/**
	 * Retrieve all the calibration quotes
	 * 
	 * @return Array of the calibration quotes
	 */

	public abstract double[] getCompQuotes();

	/**
	 * Retrieve all the calibration measures
	 * 
	 * @return Array of the calibration measures
	 */

	public abstract java.lang.String[] getCompMeasures();

	/**
	 * Retrieve the calibration quote of the given instrument
	 * 
	 * @return The calibration quote of the given instrument
	 */

	public abstract double getQuote (
		final java.lang.String strInstr)
		throws java.lang.Exception;

	/**
	 * Get the date at the node specified by the index
	 * 
	 * @param iIndex node index
	 * 
	 * @return Date corresponding to the bootstrap node
	 */

	public abstract org.drip.analytics.date.JulianDate getNodeDate (
		final int iIndex);

	/**
	 * Retrieve all the calibration components
	 * 
	 * @return Array of the calibration components
	 */

	public abstract org.drip.product.definition.CalibratableComponent[] getCalibComponents();

	/**
	 * Gets the curve name
	 * 
	 * @return Name
	 */

	public abstract java.lang.String getName();

	/**
	 * Create a parallel quote shifted  curve
	 * 
	 * @param dblShift Parallel shift
	 * 
	 * @return Curve
	 */

	public abstract Curve createParallelShiftedCurve (
		final double dblShift);

	/**
	 * Create the curve from the tweaked parameters
	 * 
	 * @param ntp Node Tweak Parameters
	 * 
	 * @return New Curve instance
	 */

	public abstract Curve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp);

	/**
	 * Get the epoch date
	 * 
	 * @return Epoch date
	 */

	public abstract org.drip.analytics.date.JulianDate getStartDate();

	/**
	 * Build the interpolator post the curve sweeping build
	 * 
	 * @return TRUE => Build-out successful
	 */

	public boolean buildInterpolator();
}
