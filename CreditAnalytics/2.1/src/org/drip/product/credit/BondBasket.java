
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
 * 	Class implements the bond basket product contract details. Contains the basket name, basket notional,
 * 		component bonds, and their weights.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondBasket extends org.drip.product.definition.BasketProduct {
	private double _dblNotional = 100.;
	private java.lang.String _strName = "";
	private double[] _adblNormWeights = null;
	private org.drip.product.definition.Bond[] _aBond = null;
	private org.drip.analytics.date.JulianDate _dtEffective = null;

	private final java.util.Map<java.lang.String, java.lang.Double> SetMapAttr (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.BasketMarketParams bmp,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		java.util.Map<java.lang.String, java.lang.Double> mapResult = new java.util.TreeMap<java.lang.String,
			java.lang.Double>();

		for (int i = 0; i < _aBond.length; ++i) {
			java.util.Map<java.lang.String, java.lang.Double> mapBondCalc = _aBond[i].value (valParams,
				pricerParams, bmp.getComponentMarketParams (_aBond[i]), quotingParams);

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> meBond : mapBondCalc.entrySet()) {
				java.lang.Double dblValue = mapResult.get (meBond.getKey());

				if (null == dblValue)
					mapResult.put (meBond.getKey(), meBond.getValue());
				else
					mapResult.put (meBond.getKey(), meBond.getValue() + _adblNormWeights[i] * dblValue);
			}
		}

		return mapResult;
	}

	/**
	 * BondBasket de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if BondBasket cannot be properly de-serialized
	 */

	public BondBasket (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("BondBasket de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("BondBasket de-serializer: Empty state");

		java.lang.String strSerializedBasketBond = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedBasketBond || strSerializedBasketBond.isEmpty())
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedBasketBond,
			getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("BondBasket de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate name");

		_strName = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate component bonds");

		java.lang.String[] astrBondRecord = org.drip.analytics.support.GenericUtil.Split (astrField[3],
			getCollectionRecordDelimiter());

		if (null == astrBondRecord || 0 == astrBondRecord.length)
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate component bonds");

		_aBond = new org.drip.product.definition.Bond[astrBondRecord.length];

		for (int i = 0; i < astrBondRecord.length; ++i) {
			if (null == astrBondRecord[i] || astrBondRecord[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrBondRecord[i]))
				throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate bond #" + i);

			_aBond[i] = new org.drip.product.credit.BondComponent (astrBondRecord[i].getBytes());
		}

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate component weights");

		java.lang.String[] astrWeightRecord = org.drip.analytics.support.GenericUtil.Split (astrField[4],
			getCollectionRecordDelimiter());

		if (null == astrWeightRecord || 0 == astrWeightRecord.length)
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate component weights");

		_adblNormWeights = new double[astrWeightRecord.length];

		for (int i = 0; i < astrWeightRecord.length; ++i) {
			if (null == astrWeightRecord[i] || astrWeightRecord[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrWeightRecord[i]))
				throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate weight #" + i);

			_adblNormWeights[i] = new java.lang.Double (astrWeightRecord[i]);
		}

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("BondBasket de-serializer: Cannot locate effective date");

		_dtEffective = new org.drip.analytics.date.JulianDate (new java.lang.Double (astrField[5]));
	}

	/**
	 * BondBasket constructor
	 * 
	 * @param strName BondBasket Name
	 * @param aBond Component bonds
	 * @param adblWeights Component Bond weights
	 * @param dtEffective Effective date
	 * @param dblNotional Basket Notional
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public BondBasket (
		final java.lang.String strName,
		final org.drip.product.definition.Bond[] aBond,
		final double[] adblWeights,
		final org.drip.analytics.date.JulianDate dtEffective,
		final double dblNotional)
		throws java.lang.Exception
	{
		if (null == strName || strName.isEmpty() || null == aBond || 0 == aBond.length || null == adblWeights
			|| 0 == adblWeights.length || aBond.length != adblWeights.length || null == dtEffective)
			throw new java.lang.Exception ("Invalid inputs into BasketBond!");

		_aBond = aBond;
		_strName = strName;
		_dtEffective = dtEffective;
		_dblNotional = dblNotional;
		double dblCumulativeWeight = 0.;
		_adblNormWeights = new double[adblWeights.length];

		for (int i = 0; i < adblWeights.length; ++i) {
			if (!org.drip.math.common.NumberUtil.IsValid (adblWeights[i]))
				throw new java.lang.Exception ("Invalid BondBasket weights!");

			dblCumulativeWeight += adblWeights[i];
		}

		if (0. == dblCumulativeWeight) throw new java.lang.Exception ("Invalid BondBasket weights!");

		for (int i = 0; i < adblWeights.length; ++i)
			_adblNormWeights[i] = adblWeights[i] / dblCumulativeWeight;
	}

	@Override public java.lang.String getName()
	{
		return _strName;
	}

	@Override public int getNumberofComponents()
	{
		return _aBond.length;
	}

	@Override public double getInitialNotional()
	{
		return _dblNotional;
	}

	@Override public org.drip.analytics.date.JulianDate getEffectiveDate()
	{
		return _dtEffective;
	}

	@Override public org.drip.analytics.date.JulianDate getMaturityDate()
	{
		org.drip.analytics.date.JulianDate dtMaturity = _dtEffective;

		for (int i = 0; i < _aBond.length; ++i) {
			org.drip.analytics.date.JulianDate dtBondMaturity = _aBond[i].getMaturityDate();

			if (null == dtBondMaturity) continue;

			if (dtMaturity.getJulian() < dtBondMaturity.getJulian()) dtMaturity = dtBondMaturity;
		}

		return dtMaturity;
	}


	@Override public org.drip.analytics.date.JulianDate getFirstCouponDate()
	{
		org.drip.analytics.date.JulianDate dtFirstCoupon = _aBond[0].getFirstCouponDate();

		for (org.drip.product.definition.Bond bond : _aBond) {
			if (dtFirstCoupon.getJulian() > bond.getFirstCouponDate().getJulian())
				dtFirstCoupon = bond.getFirstCouponDate();
		}

		return dtFirstCoupon;
	}

	@Override public java.util.List<org.drip.analytics.period.CouponPeriod> getCouponPeriod()
	{
		return null;
	}

	@Override public java.util.Set<java.lang.String> getComponentIRCurveNames()
	{
		java.util.Set<java.lang.String> sIR = new java.util.HashSet<java.lang.String>();

		for (int i = 0; i < _aBond.length; ++i)
			sIR.add (_aBond[i].getIRCurveName());

		return sIR;
	}

	@Override public java.util.Set<java.lang.String> getComponentCreditCurveNames()
	{
		java.util.Set<java.lang.String> sCC = new java.util.HashSet<java.lang.String>();

		for (int i = 0; i < _aBond.length; ++i)
			sCC.add (_aBond[i].getCreditCurveName());

		return sCC;
	}


	@Override public double getNotional (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("BondBasket.getNotional: Invalid input date!");

		double dblNotional = 0.;

		for (int i = 0; i < _aBond.length; ++i)
			dblNotional += _aBond[i].getNotional (dblDate) * _adblNormWeights[i];

		return dblNotional;
	}

	@Override public double getNotional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate1) || !org.drip.math.common.NumberUtil.IsValid
			(dblDate2))
			throw new java.lang.Exception ("BondBasket.getNotional: Invalid input dates!");

		double dblNotional = 0.;

		for (int i = 0; i < _aBond.length; ++i)
			dblNotional += _aBond[i].getNotional (dblDate1, dblDate2) * _adblNormWeights[i];

		return dblNotional;
	}

	@Override public double getCoupon (
		final double dblDate,
		final org.drip.param.definition.BasketMarketParams bmp)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("BondBasket.getCoupon: Invalid input date!");

		double dblNotional = 0.;

		for (int i = 0; i < _aBond.length; ++i) {
			org.drip.param.definition.ComponentMarketParams cmp = null;

			if (null != bmp) cmp = bmp.getComponentMarketParams (_aBond[i]);

			dblNotional += _aBond[i].getCoupon (dblDate, cmp) * _adblNormWeights[i];
		}

		return dblNotional;
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.BasketMarketParams bmp,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		long lStart = System.nanoTime();

		java.util.Map<java.lang.String, java.lang.Double> mapResult = SetMapAttr (valParams, pricerParams,
			bmp, quotingParams);

		if (null == mapResult) return null;

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getCollectionRecordDelimiter()
	{
		return "(";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return ")";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _dblNotional +
			getFieldDelimiter());

		if (null == _strName || _strName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strName + getFieldDelimiter());

		if (null == _aBond || 0 == _aBond.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbBond = new java.lang.StringBuffer();

			for (org.drip.product.definition.Bond bond : _aBond) {
				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbBond.append (getCollectionRecordDelimiter());

				sbBond.append (new java.lang.String (bond.serialize()));
			}

			if (sbBond.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbBond.toString() + getFieldDelimiter());
		}

		if (null == _adblNormWeights || 0 == _adblNormWeights.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbNormWeights = new java.lang.StringBuffer();

			for (double dblNormWeights : _adblNormWeights) {
				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbNormWeights.append (getCollectionRecordDelimiter());

				sbNormWeights.append (dblNormWeights);
			}

			if (sbNormWeights.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbNormWeights.toString() + getFieldDelimiter());
		}

		if (null == _dtEffective)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else
			sb.append (_dtEffective.getJulian());

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new BondBasket (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.support.Logger.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblPutDate = new double[3];
		double[] adblCallDate = new double[3];
		double[] adblPutFactor = new double[3];
		double[] adblCallFactor = new double[3];
		double[] adblCouponFactor = new double[3];
		double[] adblNotionalFactor = new double[3];
		adblPutFactor[0] = 0.80;
		adblPutFactor[1] = 0.90;
		adblPutFactor[2] = 1.00;
		adblCallFactor[0] = 1.20;
		adblCallFactor[1] = 1.10;
		adblCallFactor[2] = 1.00;
		adblPutDate[0] = dblStart + 30.;
		adblPutDate[1] = dblStart + 396.;
		adblPutDate[2] = dblStart + 761.;
		adblCallDate[0] = dblStart + 1126.;
		adblCallDate[1] = dblStart + 1492.;
		adblCallDate[2] = dblStart + 1857.;

		for (int i = 0; i < 3; ++i) {
			adblCouponFactor[i] = 1 - 0.1 * i;
			adblNotionalFactor[i] = 1 - 0.05 * i;
			adblDate[i] = dblStart + 365. * (i + 1);
		}

		java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
			java.util.HashMap<java.lang.String, java.lang.Double>();

		mIndexFixings.put ("USD-LIBOR-6M", 0.0402);

		java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String, java.lang.Double>>
			mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
				java.util.Map<java.lang.String, java.lang.Double>>();

		mmFixings.put (org.drip.analytics.date.JulianDate.Today().addDays (2), mIndexFixings);

		org.drip.product.params.PeriodGenerator bpgp = new
			org.drip.product.params.PeriodGenerator (dblStart + 3653., dblStart, dblStart + 3653.,
				dblStart + 182., dblStart, 2, "30/360", "30/360", null, null, null, null, null, null, null,
					null, "IGNORE", false, "USD");

		if (!bpgp.validate()) {
			System.out.println ("Cannot validate BPGP!");

			System.exit (125);
		}

		BondComponent bond = new BondComponent();

		if (!bond.setTreasuryBenchmark (new org.drip.product.params.TreasuryBenchmark (new
			org.drip.product.params.TsyBmkSet ("USD5YON", new java.lang.String[] {"USD3YON", "USD7YON"}),
				"USDTSY", "USDEDSF"))) {
			System.out.println ("Cannot initialize bond TSY params!");

			System.exit (126);
		}

		if (!bond.setCouponSetting (new org.drip.product.params.CouponSetting
			(org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblCouponFactor),
				"FLOATER", 0.01, java.lang.Double.NaN, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Coupon params!");

			System.exit (127);
		}

		if (!bond.setNotionalSetting (new org.drip.product.params.NotionalSetting
			(org.drip.product.params.FactorSchedule.CreateFromDateFactorArray (adblDate, adblNotionalFactor),
				1., org.drip.product.params.NotionalSetting.PERIOD_AMORT_AT_START, false))) {
			System.out.println ("Cannot initialize bond Notional params!");

			System.exit (128);
		}

		if (!bond.setFloaterSetting (new org.drip.product.params.FloaterSetting ("USD-LIBOR-6M", "30/360",
			0.01, java.lang.Double.NaN))) {
			System.out.println ("Cannot initialize bond Floater params!");

			System.exit (129);
		}

		if (!bond.setFixings (mmFixings)) {
			System.out.println ("Cannot initialize bond Fixings!");

			System.exit (130);
		}

		if (!bond.setCurrencySet (new org.drip.product.params.CurrencySet ("USD", "USD", "USD"))) {
			System.out.println ("Cannot initialize bond currency params!");

			System.exit (131);
		}

		if (!bond.setIdentifierSet (new org.drip.product.params.IdentifierSet ("US07942381EZ",
			"07942381E", "IBM-US07942381EZ", "IBM"))) {
			System.out.println ("Cannot initialize bond Identifier params!");

			System.exit (132);
		}

		if (!bond.setMarketConvention (new org.drip.product.params.QuoteConvention (new
			org.drip.param.valuation.QuotingParams ("30/360", 2, true, null, "DKK", false), "REGULAR",
				dblStart + 2, 1., 3, "USD", org.drip.analytics.daycount.Convention.DR_FOLL))) {
			System.out.println ("Cannot initialize bond Valuation params!");

			System.exit (133);
		}

		if (!bond.setRatesSetting (new org.drip.product.params.RatesSetting ("USD", "USD", "USD", "USD"))) {
			System.out.println ("Cannot initialize Bond Rates Valuation params!");

			System.exit (153);
		}

		if (!bond.setCreditSetting (new org.drip.product.params.CreditSetting (30, java.lang.Double.NaN,
			true, "IBMSUB", false))) {
			System.out.println ("Cannot initialize bond Credit Valuation params!");

			System.exit (134);
		}

		if (!bond.setTerminationSetting (new org.drip.product.params.TerminationSetting (false, false,
			false))) {
			System.out.println ("Cannot initialize bond CFTE params!");

			System.exit (135);
		}

		if (!bond.setPeriodSet (bpgp)) {
			System.out.println ("Cannot initialize bond Period Generation params!");

			System.exit (136);
		}

		bond.setEmbeddedPutSchedule (org.drip.product.params.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblPutDate, adblPutFactor, true, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		bond.setEmbeddedCallSchedule (org.drip.product.params.EmbeddedOptionSchedule.fromAmerican (dblStart,
			adblCallDate, adblCallFactor, false, 30, false, java.lang.Double.NaN, "CRAP",
				java.lang.Double.NaN));

		BondBasket bb = new BondBasket ("BASKETBOND", new org.drip.product.definition.Bond[] {bond, bond},
			new double[] {0.7, 1.3}, org.drip.analytics.date.JulianDate.Today(), 1.);

		byte[] abBB = bb.serialize();

		System.out.println (new java.lang.String (abBB));

		BondBasket bbDeser = new BondBasket (abBB);

		System.out.println (new java.lang.String (bbDeser.serialize()));
	}
}
