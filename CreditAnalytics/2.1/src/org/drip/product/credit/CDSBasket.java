
package org.drip.product.credit;

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
 * 	Class implements the basket default swap product contract details. Contains effective date, maturity
 * 		date, coupon, coupon day count, coupon frequency, basket components, basket notional, loss pay lag,
 * 		and optionally the outstanding notional schedule and the flat basket recovery.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CDSBasket extends org.drip.product.definition.BasketProduct {
	private double _dblCoupon = 0.0001;
	private double _dblNotional = 100.;
	private double[] _adblWeight = null;
	private java.lang.String _strName = "";
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private org.drip.product.definition.Component[] _aComp = null;
	private org.drip.product.params.FactorSchedule _notlSchedule = null;
	private java.util.List<org.drip.analytics.period.CouponPeriod> _lPeriods = null;

	/**
	 * Constructs a CDS Basket from the components and their weights
	 * 
	 * @param dtEffective Effective
	 * @param dtMaturity Maturity
	 * @param dblCoupon Coupon
	 * @param aComp Array of components
	 * @param adblWeight Weights of the components
	 * @param strName Name of the basket
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public CDSBasket (
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity,
		final double dblCoupon,
		final org.drip.product.definition.Component[] aComp,
		final double[] adblWeight,
		final java.lang.String strName)
		throws java.lang.Exception
	{
		if (null == dtEffective || null == dtMaturity || !org.drip.math.common.NumberUtil.IsValid (dblCoupon)
			|| null == aComp || 0 == aComp.length || null == adblWeight || 0 == adblWeight.length ||
				aComp.length != adblWeight.length || null == strName || strName.isEmpty())
			throw new java.lang.Exception ("Invalid inputs to BasketDefaultSwap ctr!");

		_strName = strName;
		_dblCoupon = dblCoupon;
		double dblCumulativeWeight = 0.;
		_adblWeight = new double[adblWeight.length];
		_aComp = new org.drip.product.definition.Component[aComp.length];

		_dblEffective = dtEffective.getJulian();

		_dblMaturity = dtMaturity.getJulian();

		_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();

		for (int i = 0; i < aComp.length; ++i)
			dblCumulativeWeight += _adblWeight[i];

		for (int i = 0; i < aComp.length; ++i) {
			if (null == (_aComp[i] = aComp[i]))
				throw new java.lang.Exception ("Invalid Basket Swap component!");

			_adblWeight[i] = adblWeight[i] / dblCumulativeWeight;
		}
	}

	/**
	 * BasketDefaultSwap de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if BasketDefaultSwap cannot be properly de-serialized
	 */

	public CDSBasket (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CDSBasket de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CDSBasket de-serializer: Empty state");

		java.lang.String strSerializedBasketDefaultSwap = strRawString.substring (0,
			strRawString.indexOf (getObjectTrailer()));

		if (null == strSerializedBasketDefaultSwap || strSerializedBasketDefaultSwap.isEmpty())
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedBasketDefaultSwap, getFieldDelimiter());

		if (null == astrField || 10 > astrField.length)
			throw new java.lang.Exception ("CDSBasket de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate coupon");

		_dblCoupon = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate Name");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_strName = "";
		else
			_strName = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate maturity date");

		_dblMaturity = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate effective date");

		_dblEffective = new java.lang.Double (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[6]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate component array");

		java.lang.String[] astrCDS = org.drip.analytics.support.GenericUtil.Split (astrField[6],
			getCollectionRecordDelimiter());

		if (null == astrCDS || 0 == astrCDS.length)
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate component array");

		_aComp = new CDSComponent[astrCDS.length];

		for (int i = 0; i < astrCDS.length; ++i) {
			if (null == astrCDS[i] || astrCDS[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrCDS[i]))
				throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate component #" + i);

			_aComp[i] = new CDSComponent (astrCDS[i].getBytes());
		}

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate notional schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[7]))
			_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();
		else
			_notlSchedule = new org.drip.product.params.FactorSchedule (astrField[7].getBytes());

		if (null == astrField[8] || astrField[8].isEmpty())
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate periods");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[8]))
			_lPeriods = null;
		else {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[8],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					if (null == _lPeriods)
						_lPeriods = new java.util.ArrayList<org.drip.analytics.period.CouponPeriod>();

					_lPeriods.add (new org.drip.analytics.period.CouponPeriod (astrRecord[i].getBytes()));
				}
			}
		}

		if (null == astrField[9] || astrField[9].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[9]))
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate component weights");

		java.lang.String[] astrWeights = org.drip.analytics.support.GenericUtil.Split (astrField[9],
			getCollectionRecordDelimiter());

		if (null == astrWeights || 0 == astrWeights.length)
			throw new java.lang.Exception ("CDSBasket de-serializer: Cannot locate component weights");

		_adblWeight = new double[astrWeights.length];

		for (int i = 0; i < astrWeights.length; ++i) {
			if (null == astrWeights[i] || astrWeights[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrWeights[i]))
				throw new java.lang.Exception
					("CDSBasket de-serializer: Cannot locate weight for component #" + i);

			_adblWeight[i] = new java.lang.Double (astrWeights[i]);
		}
	}

	@Override public java.lang.String getName()
	{
		return _strName;
	}

	@Override public double getNotional (
		final double dblDate)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("CDSBasket.getNotional got NaN");

		return _notlSchedule.getFactor (dblDate);
	}

	@Override public double getNotional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate1) ||
			!org.drip.math.common.NumberUtil.IsValid (dblDate2))
			throw new java.lang.Exception ("BasketDefaultSwap.getNotional got NaN");

		return _notlSchedule.getFactor (dblDate1, dblDate2);
	}

	@Override public double getInitialNotional()
	{
		return _dblNotional;
	}

	@Override public double getCoupon (
		final double dblDate,
		final org.drip.param.definition.BasketMarketParams bmp)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("BasketDefaultSwap.getCoupon got NaN");

		return _dblCoupon;
	}

	@Override public org.drip.analytics.date.JulianDate getEffectiveDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblEffective);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getMaturityDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblMaturity);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getFirstCouponDate()
	{
		org.drip.analytics.date.JulianDate dtFirstCoupon = _aComp[0].getFirstCouponDate();

		for (org.drip.product.definition.Component comp : _aComp) {
			if (dtFirstCoupon.getJulian() > comp.getFirstCouponDate().getJulian())
				dtFirstCoupon = comp.getFirstCouponDate();
		}

		return dtFirstCoupon;
	}

	@Override public java.util.List<org.drip.analytics.period.CouponPeriod> getCouponPeriod()
	{
		return _lPeriods;
	}

	@Override public int getNumberofComponents()
	{
		return _aComp.length;
	}

	@Override public java.util.Set<java.lang.String> getComponentIRCurveNames()
	{
		java.util.Set<java.lang.String> sIR = new java.util.HashSet<java.lang.String>();

		for (int i = 0; i < _aComp.length; ++i)
			sIR.add (_aComp[i].getIRCurveName());

		return sIR;
	}

	@Override public java.util.Set<java.lang.String> getComponentCreditCurveNames()
	{
		java.util.Set<java.lang.String> sCC = new java.util.HashSet<java.lang.String>();

		for (int i = 0; i < _aComp.length; ++i)
			sCC.add (_aComp[i].getCreditCurveName());

		return sCC;
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.BasketMarketParams bmp,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		double dblSumNotional  = 0.;

		long lStart = System.nanoTime();

		java.util.Map<java.lang.String, java.lang.Double> mapResult = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		for (int i = 0; i < _aComp.length; ++i) {
			try {
				dblSumNotional += _aComp[i].getInitialNotional() * _aComp[i].getNotional
					(valParams._dblValue);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			java.util.Map<java.lang.String, java.lang.Double> mapCompCalc = _aComp[i].value (valParams,
				pricerParams, bmp.getComponentMarketParams (_aComp[i]), quotingParams);

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> meComp : mapCompCalc.entrySet()) {
				java.lang.Double dblValue = mapResult.get (meComp.getKey());

				if (null == dblValue)
					mapResult.put (meComp.getKey(), meComp.getValue());
				else
					mapResult.put (meComp.getKey(), meComp.getValue() + dblValue);
			}
		}

		mapResult.put ("Price", 100. * (1  + (mapResult.get ("PV") / dblSumNotional)));

		mapResult.put ("FairPremium", mapResult.get ("LossPV") / mapResult.get ("DV01"));

		mapResult.put ("CleanPrice", 100. * (1  + (mapResult.get ("CleanPV") / dblSumNotional)));

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getCollectionRecordDelimiter()
	{
		return "@";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return ":";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _dblCoupon +
			getFieldDelimiter() + _dblNotional + getFieldDelimiter());

		if (null == _strName || _strName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strName + getFieldDelimiter());

		sb.append (_dblMaturity + getFieldDelimiter() + _dblEffective + getFieldDelimiter());

		if (null == _aComp || 0 == _aComp.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbCDS = new java.lang.StringBuffer();

			for (org.drip.product.definition.Component comp : _aComp) {
				if (null == comp || !(comp instanceof org.drip.product.definition.CreditDefaultSwap))
					continue;

				org.drip.product.definition.CreditDefaultSwap cds =
					(org.drip.product.definition.CreditDefaultSwap) comp;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbCDS.append (getCollectionRecordDelimiter());

				sbCDS.append (new java.lang.String (cds.serialize()));
			}

			if (sbCDS.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbCDS.toString() + getFieldDelimiter());
		}

		if (null == _notlSchedule)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlSchedule.serialize()) + getFieldDelimiter());

		if (null == _lPeriods)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbPeriods = new java.lang.StringBuffer();

			for (org.drip.analytics.period.Period p : _lPeriods) {
				if (null == p) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbPeriods.append (getCollectionRecordDelimiter());

				sbPeriods.append (new java.lang.String (p.serialize()));
			}

			if (sbPeriods.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbPeriods.toString() + getFieldDelimiter());
		}

		if (null == _adblWeight || 0 == _adblWeight.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbWeights = new java.lang.StringBuffer();

			for (double dblWeight : _adblWeight) {
				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbWeights.append (getCollectionRecordDelimiter());

				sbWeights.append (dblWeight);
			}

			if (sbWeights.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbWeights.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new CDSBasket (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.date.JulianDate dtEffective = org.drip.analytics.date.JulianDate.Today();

		org.drip.product.definition.CreditDefaultSwap aCDS[] = new
			org.drip.product.definition.CreditDefaultSwap[6];

		aCDS[0] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "CHN", "USD", true);

		aCDS[1] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "IND", "USD", true);

		aCDS[2] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "INDO", "USD", true);

		aCDS[3] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "PAK", "USD", true);

		aCDS[4] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "BNG", "USD", true);

		aCDS[5] = org.drip.product.creator.CDSBuilder.CreateCDS (dtEffective, dtEffective.addYears (5), 0.01,
			"USD", 0.40, "JPN", "USD", true);

		org.drip.product.definition.BasketProduct bds = new CDSBasket (dtEffective, dtEffective.addYears (5),
			0.01, aCDS, new double[] {1., 2., 3., 4., 5., 6.}, "SOVX");

		byte[] abBDS = bds.serialize();

		System.out.println (new java.lang.String (abBDS));

		org.drip.product.definition.BasketProduct bdsDeser = (org.drip.product.definition.BasketProduct)
			bds.deserialize (abBDS);

		System.out.println (new java.lang.String (bdsDeser.serialize()));
	}
}
