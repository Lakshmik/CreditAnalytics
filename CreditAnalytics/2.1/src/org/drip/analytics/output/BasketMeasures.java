
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
 * This class has the place holder for the analytical basket measures, optionally across scenarios. Contains
 * 		measure maps for unadjusted base IR/credit curves, flat delta/gamma bump measure maps for IR/credit
 * 		bump curves, component/tenor bump double maps for IR/credit curves, flat/component recovery bumped
 * 		measure maps for recovery bumped credit curves
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BasketMeasures extends org.drip.service.stream.Serializer {
	/**
	 * Basket output calculation time
	 */

	public double _dblCalcTime = java.lang.Double.NaN;

	/**
	 * Map of the base measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mBase = null;

	/**
	 * Map of the parallel IR delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatIRDelta = null;

	/**
	 * Map of the parallel IR gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatIRGamma = null;

	/**
	 * Map of the parallel RR delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatRRDelta = null;

	/**
	 * Map of the parallel RR gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatRRGamma = null;

	/**
	 * Map of the parallel credit delta measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatCreditDelta = null;

	/**
	 * Map of the parallel credit gamma measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> _mFlatCreditGamma = null;

	/**
	 * Map of the component IR delta measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmIRDelta =
		null;

	/**
	 * Map of the component IR gamma measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmIRGamma =
		null;

	/**
	 * Map of the component credit delta measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmCreditDelta
		= null;

	/**
	 * Map of the component credit gamma measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmCreditGamma
		= null;

	/**
	 * Map of the component RR delta measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmRRDelta =
		null;

	/**
	 * Map of the component RR gamma measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmRRGamma =
		null;

	/**
	 * Triple Map of the component, IR tenor, measure, and delta value
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		java.lang.Double>>> _mmmIRTenorDelta = null;

	/**
	 * Triple Map of the component, IR tenor, measure, and gamma value
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		java.lang.Double>>> _mmmIRTenorGamma = null;

	/**
	 * Triple Map of the component, credit tenor, measure, and delta value
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		java.lang.Double>>> _mmmCreditTenorDelta = null;

	/**
	 * Triple Map of the component, credit tenor, measure, and gamma value
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		java.lang.Double>>> _mmmCreditTenorGamma = null;

	/**
	 * Map of the custom scenario measure map
	 */

	public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> _mmCustom =
		null;

	/**
	 * Empty constructor - all members initialized to NaN or null
	 */

	public BasketMeasures() {
	}

	/**
	 * BasketOutput de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if BasketOutput cannot be properly de-serialized
	 */

	public BasketMeasures (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("BasketOutput de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("BasketOutput de-serializer: Empty state");

		java.lang.String strSerializedBasketOutput = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedBasketOutput || strSerializedBasketOutput.isEmpty())
			throw new java.lang.Exception ("BasketOutput de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedBasketOutput, getFieldDelimiter());

		if (null == astrField || 20 > astrField.length)
			throw new java.lang.Exception ("BasketOutput de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("BasketOutput de-serializer: Cannot locate calc time");

		_dblCalcTime = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[2]))
			_mBase = null;
		else
			_mBase = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[2],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[3] || astrField[3].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[3]))
			_mFlatRRDelta = null;
		else
			_mFlatRRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[3],
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[4] || astrField[4].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[4]))
			_mFlatRRGamma = null;
		else
			_mFlatRRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo2DSDMap (astrField[4],
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
			_mmIRDelta = null;
		else
			_mmIRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[9],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[10] || astrField[10].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[10]))
			_mmIRGamma = null;
		else
			_mmIRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[10],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[11] || astrField[11].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[11]))
			_mmCreditDelta = null;
		else
			_mmCreditDelta = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[11],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[12] || astrField[12].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[12]))
			_mmCreditGamma = null;
		else
			_mmCreditGamma = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[12],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[13] || astrField[13].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[13]))
			_mmRRDelta = null;
		else
			_mmRRDelta = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[13],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[14] || astrField[14].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[14]))
			_mmRRGamma = null;
		else
			_mmRRGamma = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[14],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[15] || astrField[15].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[15]))
			_mmmIRTenorDelta = null;
		else
			_mmmIRTenorDelta = org.drip.analytics.support.GenericUtil.FlatStringTo4DSDMap (astrField[15],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[16] || astrField[16].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[16]))
			_mmmIRTenorGamma = null;
		else
			_mmmIRTenorGamma = org.drip.analytics.support.GenericUtil.FlatStringTo4DSDMap (astrField[16],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[17] || astrField[17].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[17]))
			_mmmCreditTenorDelta = null;
		else
			_mmmCreditTenorDelta = org.drip.analytics.support.GenericUtil.FlatStringTo4DSDMap (astrField[17],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[18] || astrField[18].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[18]))
			_mmmCreditTenorGamma = null;
		else
			_mmmCreditTenorGamma = org.drip.analytics.support.GenericUtil.FlatStringTo4DSDMap (astrField[18],
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter(), true, NULL_SER_STRING);

		if (null == astrField[19] || astrField[19].isEmpty() || NULL_SER_STRING.equalsIgnoreCase
			(astrField[19]))
			_mmCustom = null;
		else
			_mmCustom = org.drip.analytics.support.GenericUtil.FlatStringTo3DSDMap (astrField[19],
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

		if (null == _mFlatRRDelta || 0 == _mFlatRRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatRRDelta,
				getCollectionKeyValueDelimiter(), getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mFlatRRGamma || 0 == _mFlatRRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.TwoDSDMapToFlatString (_mFlatRRGamma,
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

		if (null == _mmIRDelta || 0 == _mmIRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmIRDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmIRGamma || 0 == _mmIRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmIRGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmCreditDelta || 0 == _mmCreditDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmCreditDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmCreditGamma || 0 == _mmCreditGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmCreditGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmRRDelta || 0 == _mmRRDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmRRDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmRRGamma || 0 == _mmRRGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.ThreeDSDMapToFlatString (_mmRRGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmmIRTenorDelta || 0 == _mmmIRTenorDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.FourDSDMapToFlatString (_mmmIRTenorDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmmIRTenorGamma || 0 == _mmmIRTenorGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.FourDSDMapToFlatString (_mmmIRTenorGamma,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmmCreditTenorDelta || 0 == _mmmCreditTenorDelta.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.FourDSDMapToFlatString (_mmmCreditTenorDelta,
				getCollectionMultiLevelKeyDelimiter(), getCollectionKeyValueDelimiter(),
					getCollectionRecordDelimiter()) + getFieldDelimiter());

		if (null == _mmmCreditTenorGamma || 0 == _mmmCreditTenorGamma.size())
			sb.append (NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (org.drip.analytics.support.GenericUtil.FourDSDMapToFlatString (_mmmCreditTenorGamma,
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
			return new BasketMeasures (ab);
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

	private static final void Set4DMapValues (
		final java.util.Map<java.lang.String,
		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>> map4D,
		final double dblPV,
		final double dblFairPremium)
	{
		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> mapARGComp1Y = new
			java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>();

		Set3DMapValues (mapARGComp1Y, dblPV, dblFairPremium);

		map4D.put ("ARG", mapARGComp1Y);

		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> mapBRAComp1Y = new
			java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>();

		Set3DMapValues (mapBRAComp1Y, dblPV, dblFairPremium);

		map4D.put ("BRA", mapBRAComp1Y);
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		BasketMeasures bo = new BasketMeasures();

		bo._dblCalcTime = 433.7;

		Set2DMapValues (bo._mBase = new java.util.HashMap<java.lang.String, java.lang.Double>(), 0.3601,
			537.500);

		Set2DMapValues (bo._mFlatRRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			0.0015, 0.020);

		Set2DMapValues (bo._mFlatRRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			0.0006, 0.003);

		Set2DMapValues (bo._mFlatIRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0007, 0.006);

		Set2DMapValues (bo._mFlatIRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0001, 0.001);

		Set2DMapValues (bo._mFlatCreditDelta = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0118, 1.023);

		Set2DMapValues (bo._mFlatCreditGamma = new java.util.HashMap<java.lang.String, java.lang.Double>(),
			-0.0004, 0.014);

		Set3DMapValues (bo._mmIRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmIRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmCreditDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmCreditGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmRRDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set3DMapValues (bo._mmRRGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmIRTenorDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmIRTenorGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmCreditTenorDelta = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		Set4DMapValues (bo._mmmCreditTenorGamma = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>(),
				-0.00003751, 0.000002262);

		SetCustom3DMapValues ("CSW10PC", bo._mmCustom = new java.util.HashMap<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>(), -0.00003468, 0.000000952);

		byte abBO[] = bo.serialize();

		System.out.println (new java.lang.String (abBO));

		BasketMeasures boDeser = new BasketMeasures (abBO);

		System.out.println ("\n" + new java.lang.String (boDeser.serialize()));
	}
}
