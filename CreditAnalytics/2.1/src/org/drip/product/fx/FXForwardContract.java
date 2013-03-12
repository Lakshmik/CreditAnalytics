
package org.drip.product.fx;

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
 * Class contains the FX forward product contract details - the effective date, the maturity date, the
 *  currency pair and the product code.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class FXForwardContract extends org.drip.product.definition.FXForward {
	private static final boolean s_bLog = false;

	private java.lang.String _strCode = "";
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private org.drip.product.params.CurrencyPair _ccyPair = null;

	/**
	 * Create an FXForward Contract from the currency pair, the effective and the maturity dates
	 * 
	 * @param ccyPair Currency Pair
	 * @param dtEffective Effective Date
	 * @param dtMaturity Maturity Date
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public FXForwardContract (
		final org.drip.product.params.CurrencyPair ccyPair,
		final org.drip.analytics.date.JulianDate dtEffective,
		final org.drip.analytics.date.JulianDate dtMaturity)
		throws java.lang.Exception
	{
		if (null == ccyPair || null == dtEffective || null == dtMaturity || dtEffective.getJulian() >=
			dtMaturity.getJulian())
			throw new java.lang.Exception ("Invalid params into FXForward ctr");

		_ccyPair = ccyPair;

		_dblMaturity = dtMaturity.getJulian();

		_dblEffective = dtEffective.getJulian();
	}

	/**
	 * FXForward de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FXForward cannot be properly de-serialized
	 */

	public FXForwardContract (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FXForward de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FXForward de-serializer: Empty state");

		java.lang.String strSerializedFXForward = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFXForward || strSerializedFXForward.isEmpty())
			throw new java.lang.Exception ("FXForward de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strSerializedFXForward,
			getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("FXForward de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("CurrencyPair de-serializer: Cannot locate FXForward Code");

		_strCode = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FXForward de-serializer: Cannot locate Effective Date");

		_dblEffective = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("FXForward de-serializer: Cannot locate Maturity Date");

		_dblMaturity = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("FXForward de-serializer: Cannot locate Currency Pair");

		_ccyPair = new org.drip.product.params.CurrencyPair (astrField[4].getBytes());
	}

	@Override public java.lang.String getPrimaryCode()
	{
		return _strCode;
	}

	@Override public void setPrimaryCode (
		final java.lang.String strCode)
	{
		_strCode = strCode;
	}

	@Override public java.lang.String[] getSecondaryCode()
	{
		java.lang.String strPrimaryCode = getPrimaryCode();

		int iNumTokens = 0;
		java.lang.String astrCodeTokens[] = new java.lang.String[2];

		java.util.StringTokenizer stCodeTokens = new java.util.StringTokenizer (strPrimaryCode, ".");

		while (stCodeTokens.hasMoreTokens())
			astrCodeTokens[iNumTokens++] = stCodeTokens.nextToken();

		System.out.println (astrCodeTokens[0]);

		return new java.lang.String[] {astrCodeTokens[0]};
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

	@Override public org.drip.product.params.CurrencyPair getCcyPair()
	{
		return _ccyPair;
	}

	@Override public double implyFXForward (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final double dblFXSpot,
		final boolean bFwdAsPIP)
		throws java.lang.Exception
	{
		if (null == valParams || null == dcNum || null == dcDenom || !org.drip.math.common.NumberUtil.IsValid
			(dblFXSpot))
			throw new java.lang.Exception ("Invalid params into FXForward.implyFXForward");

		double dblFXFwd = dblFXSpot * dcDenom.getDF (_dblMaturity) * dcNum.getDF (valParams._dblCashPay) /
			dcNum.getDF (_dblMaturity) / dcDenom.getDF (valParams._dblCashPay);

		if (!bFwdAsPIP) return dblFXFwd;
		
		return (dblFXFwd - dblFXSpot) * _ccyPair.getPIPFactor();
	}

	@Override public double calcDCBasis (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final double dblFXSpot,
		final double dblMarketFXFwdPrice,
		final boolean bBasisOnDenom)
		throws java.lang.Exception
	{
		if (null == valParams || null == dcNum || null == dcDenom || !org.drip.math.common.NumberUtil.IsValid
			(dblFXSpot))
			throw new java.lang.Exception ("Invalid params into FXForward.calcDCBasis");

		return new FXBasisCalibrator (this).calibrateDCBasisFromFwdPriceNR (valParams, dcNum, dcDenom,
			dblFXSpot, dblMarketFXFwdPrice, bBasisOnDenom);
	}

	@Override public java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final double dblFXSpot)
	{
		if (null == valParams || null == dcNum || null == dcDenom || !org.drip.math.common.NumberUtil.IsValid
			(dblFXSpot))
			return null;

		java.util.Map<java.lang.String, java.lang.Double> mapRes = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		try {
			mapRes.put ("FXFWD", implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, false));

			mapRes.put ("FXOutright", implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, false));

			mapRes.put ("Outright", implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, false));

			mapRes.put ("FXFWDPIP", implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, true));

			mapRes.put ("PIP", implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, true));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return mapRes;
	}

	/**
	 * @author Lakshmi Krishnamurthy
	 *
	 * Calibrator for FXBasis - either bootstrapped or cumulative
	 */

	public class FXBasisCalibrator {
		private FXForwardContract _fxfwd = null;

		// DC Basis Calibration Stochastic Control

		private int _iNumIterations = 100;
		private double _dblBasisIncr = 0.0001;
		private double _dblBasisDiffTol = 0.0001;

		private final double calcFXFwd (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.analytics.definition.DiscountCurve dcNum,
			final org.drip.analytics.definition.DiscountCurve dcDenom,
			final double dblFXSpot,
			final double dblBump,
			final boolean bBasisOnDenom)
			throws java.lang.Exception {
			if (bBasisOnDenom)
				return _fxfwd.implyFXForward (valParams, dcNum, (org.drip.analytics.definition.DiscountCurve)
					dcDenom.createParallelShiftedCurve (dblBump), dblFXSpot, false);

			return _fxfwd.implyFXForward (valParams, (org.drip.analytics.definition.DiscountCurve)
				dcNum.createParallelShiftedCurve (dblBump), dcDenom, dblFXSpot, false);
		}

		/**
		 * Constructor: Constructs the basis calibrator from the FXForward parent
		 * 
		 * @param fxfwd FXForward parent
		 * 
		 * @throws java.lang.Exception Thrown if parent is invalid
		 */

		public FXBasisCalibrator (
			final FXForwardContract fxfwd)
			throws java.lang.Exception
		{
			if (null == (_fxfwd = fxfwd))
				throw new java.lang.Exception ("null fxfwd into FXBasisCalibrator");
		}

		/**
		 * Calibrates the discount curve basis from FXForward using Newton-Raphson methodology
		 * 
		 * @param valParams ValuationParams
		 * @param dcNum Discount Curve for the Numerator
		 * @param dcDenom Discount Curve for the Denominator
		 * @param dblFXSpot FXSpot value
		 * @param dblMarketFXFwdPrice FXForward market value
		 * @param bBasisOnDenom True - Basis is set on the denominator
		 * 
		 * @return Calibrated DC basis
		 * 
		 * @throws java.lang.Exception Thrown if cannot calibrate
		 */

		public double calibrateDCBasisFromFwdPriceNR (
			final org.drip.param.valuation.ValuationParams valParams,
			final org.drip.analytics.definition.DiscountCurve dcNum,
			final org.drip.analytics.definition.DiscountCurve dcDenom,
			final double dblFXSpot,
			final double dblMarketFXFwdPrice,
			final boolean bBasisOnDenom)
			throws java.lang.Exception
		{
			if (null == valParams || null == dcNum || null == dcDenom ||
				!org.drip.math.common.NumberUtil.IsValid (dblMarketFXFwdPrice) ||
					!org.drip.math.common.NumberUtil.IsValid (dblFXSpot))
				throw new java.lang.Exception ("calibrateDCBasisFromFwdPriceNR - bad inputs");

			double dblFXFwdBase = _fxfwd.implyFXForward (valParams, dcNum, dcDenom, dblFXSpot, false);

			if (!org.drip.math.common.NumberUtil.IsValid (dblFXFwdBase))
				throw new java.lang.Exception ("Cannot imply FX Fwd Base!");

			double dblFXFwdBumped = calcFXFwd (valParams, dcNum, dcDenom, dblFXSpot, _dblBasisIncr,
				bBasisOnDenom);

			if (!org.drip.math.common.NumberUtil.IsValid (dblFXFwdBumped))
				throw new java.lang.Exception ("Cannot imply FX Fwd for " + _dblBasisIncr + " shift!");

			double dblDBasisDFXFwd = _dblBasisIncr / (dblFXFwdBumped - dblFXFwdBase);

			if (!org.drip.math.common.NumberUtil.IsValid (dblDBasisDFXFwd))
				throw new java.lang.Exception ("Cannot calculate Fwd/Basis Slope for 0 basis!");

			double dblBasisPrev = 0.;
			double dblBasis = dblDBasisDFXFwd * (dblMarketFXFwdPrice - dblFXFwdBase);

			if (!org.drip.math.common.NumberUtil.IsValid (dblBasis))
				throw new java.lang.Exception ("Got " + dblBasis + " for FlatSpread for " +
					_fxfwd.getPrimaryCode() + " and price " + dblFXFwdBase);

			while (_dblBasisDiffTol < java.lang.Math.abs (dblBasis - dblBasisPrev)) {
				if (0 == --_iNumIterations)
					throw new java.lang.Exception ("Cannot calib Basis for " + _fxfwd.getPrimaryCode() +
						" and price " + dblMarketFXFwdPrice + " within limit!");

				if (!org.drip.math.common.NumberUtil.IsValid (dblFXFwdBase = calcFXFwd (valParams, dcNum,
					dcDenom, dblFXSpot, dblBasisPrev = dblBasis, bBasisOnDenom)))
					throw new java.lang.Exception ("Cannot imply FX Fwd for " + dblBasis + " shift!");

				if (!org.drip.math.common.NumberUtil.IsValid (dblFXFwdBumped = calcFXFwd (valParams, dcNum,
					dcDenom, dblFXSpot, dblBasis + _dblBasisIncr, bBasisOnDenom)))
					throw new java.lang.Exception ("Cannot imply FX Fwd for " + (dblBasis + _dblBasisIncr) +
						" shift!");

				if (!org.drip.math.common.NumberUtil.IsValid (dblDBasisDFXFwd = _dblBasisIncr /
					(dblFXFwdBumped - dblFXFwdBase)))
					throw new java.lang.Exception ("Cannot calculate Fwd/Basis Slope for " + (dblBasis +
						_dblBasisIncr) + " basis!");

				if (s_bLog) System.out.println ("\tFXFwd[" + dblBasis + "]=" + dblFXFwdBase);

				dblBasis = dblBasisPrev + dblDBasisDFXFwd * (dblMarketFXFwdPrice - dblFXFwdBase);

				if (!org.drip.math.common.NumberUtil.IsValid (dblBasis))
					throw new java.lang.Exception ("Got " + dblBasis + " for FlatSpread for " +
						_fxfwd.getPrimaryCode() + " and price " + dblFXFwdBase);
			}

			return dblBasis;
		}
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strCode +
			getFieldDelimiter() + _dblEffective + getFieldDelimiter() + _dblMaturity + getFieldDelimiter() +
				new java.lang.String (_ccyPair.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new FXForwardContract (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.product.definition.FXForward fxFwd = new FXForwardContract (new
			org.drip.product.params.CurrencyPair ("USD", "INR", "INR", 1.),
				org.drip.analytics.date.JulianDate.Today(),
					org.drip.analytics.date.JulianDate.Today().addTenor ("18M"));

		byte[] abFXFwd = fxFwd.serialize();

		System.out.println (new java.lang.String (abFXFwd));

		org.drip.product.definition.FXForward fxFwdDeser = (org.drip.product.definition.FXForward)
			fxFwd.deserialize (abFXFwd);

		System.out.println (new java.lang.String (fxFwdDeser.serialize()));
	}
}
