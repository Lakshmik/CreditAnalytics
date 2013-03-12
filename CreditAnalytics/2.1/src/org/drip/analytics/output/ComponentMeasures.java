
package org.drip.analytics.output;

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
 * This class serves as a place holder for analytical single component output measures, optionally across
 * 		scenarios. Contains measure maps for unadjusted base IR/credit curves, flat delta/gamma bump measure
 * 		maps for IR/credit bump curves, tenor bump double maps for IR/credit curves, flat/recovery bumped
 * 		measure maps for recovery bumped credit curves
 *
 * @author Lakshmi Krishnamurthy
 */

public class ComponentMeasures extends org.drip.service.stream.Serializer {
	/**
	 * Calculation Time
	 */

	public double _dblCalcTime = java.lang.Double.NaN;

	/**
	 * Map of the base measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mBase = null;

	/**
	 * Map of the parallel RR delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mRRDelta = null;

	/**
	 * Map of the parallel RR gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mRRGamma = null;

	/**
	 * Map of the parallel IR delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatIRDelta = null;

	/**
	 * Map of the parallel IR gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatIRGamma = null;

	/**
	 * Map of the parallel credit delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatCreditDelta = null;

	/**
	 * Map of the parallel credit gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatCreditGamma = null;

	/**
	 * Map of the tenor IR delta measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmTenorIRDelta
		= null;

	/**
	 * Map of the tenor IR gamma measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmTenorIRGamma
		= null;

	/**
	 * Map of the tenor credit delta measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
		_mmTenorCreditDelta = null;

	/**
	 * Map of the tenor credit gamma measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
		_mmTenorCreditGamma = null;

	/**
	 * Map of the custom scenario measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmCustom =
		null;

	/**
	 * Empty constructor - all members initialized to NaN or null
	 */

	public ComponentMeasures()
	{
	}

	/**
	 * ComponentOutput de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if ComponentOutput cannot be properly de-serialized
	 */

	public ComponentMeasures (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("ComponentOutput de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("ComponentOutput de-serializer: Empty state");

		java.lang.String strSerializedComponentOutput = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedComponentOutput || strSerializedComponentOutput.isEmpty())
			throw new java.lang.Exception ("ComponentOutput de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedComponentOutput, getFieldDelimiter());

		if (null == astrField || 14 > astrField.length)
			throw new java.lang.Exception ("ComponentOutput de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("ComponentOutput de-serializer: Cannot locate calc time");

		_dblCalcTime = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[2]))
			_mBase = null;
		else
			_mBase = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[2],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[3] || astrField[3].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[3]))
			_mRRDelta = null;
		else
			_mRRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[3],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[4] || astrField[4].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
				(astrField[4]))
			_mRRGamma = null;
		else
			_mRRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[4],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[5] || astrField[5].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[5]))
			_mFlatIRDelta = null;
		else
			_mFlatIRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[5],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[6] || astrField[6].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
				(astrField[6]))
			_mFlatIRGamma = null;
		else
			_mFlatIRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[6],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[7] || astrField[7].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[7]))
			_mFlatCreditDelta = null;
		else
			_mFlatCreditDelta = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[7],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[8] || astrField[8].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[8]))
			_mFlatCreditGamma = null;
		else
			_mFlatCreditGamma = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[8],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[9] || astrField[9].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[9]))
			_mmTenorIRDelta = null;
		else
			_mmTenorIRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[9],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[10] || astrField[10].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[10]))
			_mmTenorIRGamma = null;
		else
			_mmTenorIRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[10],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[11] || astrField[11].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[11]))
			_mmTenorCreditDelta = null;
		else
			_mmTenorCreditDelta = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[11],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[12] || astrField[12].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[12]))
			_mmTenorCreditGamma = null;
		else
			_mmTenorCreditGamma = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[12],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[13] || astrField[13].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[13]))
			_mmCustom = null;
		else
			_mmCustom = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[13],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (VERSION + getFieldDelimiter() + _dblCalcTime + getFieldDelimiter());

		if (null == _mBase || 0 == _mBase.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mBase,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mRRDelta || 0 == _mRRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mRRDelta,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mRRGamma || 0 == _mRRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mRRGamma,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mFlatIRDelta || 0 == _mFlatIRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatIRDelta,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mFlatIRGamma || 0 == _mFlatIRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatIRGamma,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mFlatCreditDelta || 0 == _mFlatCreditDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatCreditDelta,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mFlatCreditGamma || 0 == _mFlatCreditGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatCreditGamma,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmTenorIRDelta || 0 == _mmTenorIRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmTenorIRDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmTenorIRGamma || 0 == _mmTenorIRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmTenorIRGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmTenorCreditDelta || 0 == _mmTenorCreditDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmTenorCreditDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmTenorCreditGamma || 0 == _mmTenorCreditGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmTenorCreditGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmCustom || 0 == _mmCustom.size())
			sb.append (NULL_SER_STRING);
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmCustom,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new ComponentMeasures (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final void Set2DMapValues (
		final java.util.Map<java.lang.String, java.lang.Double> map2D,
		final double dblPV,
		final double dblFairPremium)
	{
		map2D.put ("PV", dblPV);

		map2D.put ("FairPremium", dblFairPremium);
	}

	private static final void Set3DMapValues (
		final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3D,
		final double dblPV,
		final double dblFairPremium)
	{
		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario1Y = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario1Y, dblPV, dblFairPremium);

		map3D.put ("1Y", mapIRScenario1Y);

		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario2Y = new
				java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario2Y, dblPV, dblFairPremium);

		map3D.put ("2Y", mapIRScenario2Y);
	}

	private static final void SetCustom3DMapValues (
		final java.lang.String strCustomSetName,
		final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3D,
		final double dblPV,
		final double dblFairPremium)
	{
		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario1Y = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario1Y, dblPV, dblFairPremium);

		map3D.put ("1Y", mapIRScenario1Y);

		java.util.Map<java.lang.String, java.lang.Double> mapIRScenario2Y = new
				java.util.HashMap<java.lang.String, java.lang.Double>();

		Set2DMapValues (mapIRScenario2Y, dblPV, dblFairPremium);

		map3D.put ("2Y", mapIRScenario2Y);
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		ComponentMeasures co = new ComponentMeasures();

		co._dblCalcTime = 433.7;

		Set2DMapValues (co._mBase = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.3601,
			537.500);

		Set2DMapValues (co._mRRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.0015,
			0.020);

		Set2DMapValues (co._mRRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.0006,
			0.003);

		Set2DMapValues (co._mFlatIRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0007, 0.006);

		Set2DMapValues (co._mFlatIRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0001, 0.001);

		Set2DMapValues (co._mFlatCreditDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0118, 1.023);

		Set2DMapValues (co._mFlatCreditGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0004, 0.014);

		Set3DMapValues (co._mmTenorIRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (co._mmTenorIRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00007501, 0.000004524);

		Set3DMapValues (co._mmTenorCreditDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00000867, 0.000000238);

		Set3DMapValues (co._mmTenorCreditGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00001734, 0.000000476);

		SetCustom3DMapValues ("CSW10PC", co._mmCustom = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003468, 0.000000952);

		byte abCO[] = co.serialize();

		System.out.println (new java.lang.String (abCO));

		ComponentMeasures coDeser = new ComponentMeasures (abCO);

		System.out.println (new java.lang.String (coDeser.serialize()));
	}
}
