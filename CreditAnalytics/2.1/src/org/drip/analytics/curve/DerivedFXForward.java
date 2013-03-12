
package org.drip.analytics.curve;

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
 * This class contains the term structure of dates/times and FX forwards (PIP/outright), and Spot FX info for
 * 		the given currency pair.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DerivedFXForward extends org.drip.analytics.definition.FXForwardCurve {
	private boolean[] _abIsPIP = null;
	private double[] _adblDate = null;
	private double[] _adblFXFwd = null;
	private double _dblFXSpot = java.lang.Double.NaN;
	private double _dblSpotDate = java.lang.Double.NaN;
	private org.drip.product.params.CurrencyPair _cp = null;

	private double calcNodeBasis (
		final int iNode,
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom)
		throws java.lang.Exception
	{
		if (iNode >= _abIsPIP.length || null == valParam || null == dcNum || null == dcDenom)
			throw new java.lang.Exception ("Bad inputs into FXCurve.calcNodeBasis!");

		double dblFXFwd = _adblFXFwd[iNode];

		if (_abIsPIP[iNode]) dblFXFwd = _dblFXSpot + (_adblFXFwd[iNode] / _cp.getPIPFactor());

		org.drip.product.definition.FXForward fxfwd =
			org.drip.product.creator.FXForwardBuilder.CreateFXForward (_cp, new
				org.drip.analytics.date.JulianDate (_dblSpotDate), new org.drip.analytics.date.JulianDate
					(_adblDate[iNode]));

		if (null == fxfwd)
			throw new java.lang.Exception ("Cannot make fxfwd at maturity " +
				org.drip.analytics.date.JulianDate.fromJulian (_adblDate[iNode]));

		return fxfwd.calcDCBasis (valParam, dcNum, dcDenom, _dblFXSpot, dblFXFwd, bBasisOnDenom);
	}

	/**
	 * Creates an FXCurve from the CurrencyPair, FX Spot, and the FX Forward parameters
	 * 
	 * @param cp CurrencyPair
	 * @param dtSpot Spot Date
	 * @param dblFXSpot FX Spot Rate
	 * @param adblDate Array of dates
	 * @param adblFXFwd Array of FX Forwards
	 * @param abIsPIP Array of PIP indicators
	 * 
	 * @throws java.lang.Exception Creates the FXCurve instance
	 */

	public DerivedFXForward (
		final org.drip.product.params.CurrencyPair cp,
		final org.drip.analytics.date.JulianDate dtSpot,
		final double dblFXSpot,
		final double[] adblDate,
		final double[] adblFXFwd,
		final boolean[] abIsPIP)
		throws java.lang.Exception
	{
		if (null == cp || null == dtSpot || !org.drip.math.common.NumberUtil.IsValid (dblFXSpot) || null ==
			adblDate || 0 == adblDate.length || null == adblFXFwd || 0 == adblFXFwd.length || null == abIsPIP
				|| 0 == abIsPIP.length || adblDate.length != adblFXFwd.length || adblDate.length !=
					abIsPIP.length)
			throw new java.lang.Exception ("Invalid params into FXCurve ctr!");

		_dblSpotDate = dtSpot.getJulian();

		_cp = cp;
		_dblFXSpot = dblFXSpot;
		_abIsPIP = new boolean[abIsPIP.length];
		_adblDate = new double[adblDate.length];
		_adblFXFwd = new double[adblFXFwd.length];

		for (int i = 0; i < abIsPIP.length; ++i) {
			if (!org.drip.math.common.NumberUtil.IsValid (adblDate[i]) || adblDate[i] <= _dblSpotDate)
				throw new java.lang.Exception ("Invalid params into FXCurve ctr: Node date " +
					org.drip.analytics.date.JulianDate.fromJulian (adblDate[i]) + " before spot " + dtSpot);

			_abIsPIP[i] = abIsPIP[i];
			_adblDate[i] = adblDate[i];
			_adblFXFwd[i] = adblFXFwd[i];
		}
	}

	/**
	 * FXCurve de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FXCurve cannot be properly de-serialized
	 */

	public DerivedFXForward (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FXCurve de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FXCurve de-serializer: Empty state");

		java.lang.String strFXCurve = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strFXCurve || strFXCurve.isEmpty())
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strFXCurve,
			getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("FXCurve de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot locate spot date");

		_dblSpotDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot locate FX spot");

		_dblFXSpot = new java.lang.Double (astrField[2]);

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblBasis = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblBasis,
			astrField[3], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode state");

		if (0 == lsdblDate.size() || 0 == lsdblBasis.size() || lsdblDate.size() != lsdblBasis.size())
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode state");

		_adblDate = new double[lsdblDate.size()];

		_adblFXFwd = new double[lsdblBasis.size()];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			_adblFXFwd[i] = lsdblBasis.get (i);
		}

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode state");

		java.util.List<java.lang.Boolean> lsb = new java.util.ArrayList<java.lang.Boolean>();

		if (!org.drip.analytics.support.GenericUtil.BooleanListFromString (lsb, astrField[4],
			getCollectionRecordDelimiter()))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode state");

		_abIsPIP = new boolean[lsb.size()];

		for (int i = 0; i < _abIsPIP.length; ++i)
			_abIsPIP[i] = lsb.get (i);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("FXCurve de-serializer: Cannot decode currency pair");

		_cp = new org.drip.product.params.CurrencyPair (astrField[5].getBytes());
	}

	@Override public boolean initializeCalibrationRun (
		final double dblLeftSlope)
	{
		return true;
	}

	@Override public int numCalibNodes()
	{
		return _adblDate.length;
	}

	@Override public org.drip.product.params.CurrencyPair getCurrencyPair()
	{
		return _cp;
	}

	@Override public org.drip.analytics.date.JulianDate getSpotDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblSpotDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public double getFXSpot()
	{
		return _dblFXSpot;
	}

	@Override public double[] getFullBasis (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom)
	{
		double[] adblBasis = new double[_abIsPIP.length];

		for (int i = 0; i < _abIsPIP.length; ++i) {
			try {
				adblBasis[i] = calcNodeBasis (i, valParam, dcNum, dcDenom, bBasisOnDenom);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return adblBasis;
	}

	@Override public double[] bootstrapBasis (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom)
	{
		org.drip.analytics.definition.DiscountCurve dcBasis = null;
		double[] adblBasis = new double[_abIsPIP.length];

		try {
			if (bBasisOnDenom)
				dcBasis = (org.drip.analytics.definition.DiscountCurve) dcDenom.createParallelShiftedCurve
					(0.);
			else
				dcBasis = (org.drip.analytics.definition.DiscountCurve) dcNum.createParallelShiftedCurve
					(0.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == dcBasis) return null;

		for (int i = 0; i < _abIsPIP.length; ++i) {
			try {
				if (bBasisOnDenom)
					adblBasis[i] = calcNodeBasis (i, valParam, dcNum, dcBasis, true);
				else
					adblBasis[i] = calcNodeBasis (i, valParam, dcBasis, dcDenom, false);

				dcBasis.bumpNodeValue (i, adblBasis[i]);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return adblBasis;
	}

	@Override public org.drip.analytics.definition.DiscountCurve bootstrapBasisDC (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom)
	{
		org.drip.analytics.definition.DiscountCurve dcBasis = null;

		try {
			if (bBasisOnDenom)
				dcBasis = (org.drip.analytics.definition.DiscountCurve) dcDenom.createParallelShiftedCurve
					(0.);
			else
				dcBasis = (org.drip.analytics.definition.DiscountCurve) dcNum.createParallelShiftedCurve
					(0.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == dcBasis) return null;

		for (int i = 0; i < _abIsPIP.length; ++i) {
			double dblBasis = java.lang.Double.NaN;

			try {
				if (bBasisOnDenom)
					dblBasis = calcNodeBasis (i, valParam, dcNum, dcBasis, true);
				else
					dblBasis = calcNodeBasis (i, valParam, dcBasis, dcDenom, false);

				dcBasis.bumpNodeValue (i, dblBasis);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return dcBasis;
	}

	public double calcImpliedRate (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final double dblDate,
		final boolean bBasisOnDenom)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("FXForwardCurve.calcImpliedRate: Invalid input date!");

		org.drip.analytics.definition.DiscountCurve dcImplied = bootstrapBasisDC (valParam, dcNum, dcDenom,
			bBasisOnDenom);

		if (null == dcImplied)
			throw new java.lang.Exception ("FXForwardCurve.calcImpliedRate: Cannot imply basis DC!");

		return dcImplied.calcImpliedRate (dblDate);
	}

	@Override public double[] calcImpliedNodeRates (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom)
	{
		double[] adblImpliedNodeRate = new double[_abIsPIP.length];

		for (int i = 0; i < _abIsPIP.length; ++i) {
			try {
				double dblBaseImpliedRate = java.lang.Double.NaN;

				if (bBasisOnDenom)
					dblBaseImpliedRate = dcNum.calcImpliedRate (_adblDate[i]);
				else
					dblBaseImpliedRate = dcDenom.calcImpliedRate (_adblDate[i]);

				adblImpliedNodeRate[i] = dblBaseImpliedRate + calcNodeBasis (i,	valParam, dcNum, dcDenom,
					bBasisOnDenom);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return adblImpliedNodeRate;
	}

	@Override public boolean setNodeValue (
		final int iIndex,
		final double dblValue)
	{
		if (_adblFXFwd.length <= iIndex) return false;

		_adblFXFwd[iIndex] = dblValue;
		return true;
	}

	@Override public boolean bumpNodeValue (
		final int iIndex,
		final double dblValue)
	{
		if (_adblFXFwd.length <= iIndex) return false;

		_adblFXFwd[iIndex] += dblValue;
		return true;
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		for (int i = 0; i < _adblFXFwd.length; ++i)
			_adblFXFwd[i] = dblValue;

		return true;
	}

	@Override public java.lang.String displayString()
	{
		try {
			java.lang.StringBuffer sb = new java.lang.StringBuffer();

			for (int i = 0; i < _adblDate.length; ++i) {
				if (0 != i) sb.append (" | ");

				sb.append (new org.drip.analytics.date.JulianDate (_adblDate[i]) + "=" + _adblFXFwd[i] + "/"
					+ _abIsPIP[i]);
			}

			return sb.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public double[] getCompQuotes()
	{
		return null;
	}

	@Override public java.lang.String[] getCompMeasures()
	{
		return null;
	}

	@Override public double getQuote (
		final java.lang.String strInstr)
		throws java.lang.Exception
	{
		return java.lang.Double.NaN;
	}

	@Override public org.drip.analytics.date.JulianDate getNodeDate (
		final int iIndex)
	{
		try {
			return new org.drip.analytics.date.JulianDate (_adblDate[iIndex]);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.product.definition.CalibratableComponent[] getCalibComponents()
	{
		return null;
	}

	@Override public java.lang.String getName()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append ("FXFWDBASIS[" + _cp.getCode() + "]=");

		for (int i = 0; i < _adblDate.length; ++i) {
			if (0 != i) sb.append (";");

			sb.append (_adblDate[i] + ":" + _adblFXFwd[i]);
		}

		return sb.toString();
	}

	@Override public org.drip.analytics.definition.Curve createParallelShiftedCurve (
		final double dblShift)
	{
		double[] adblFXForwardBumped = new double[_adblFXFwd.length];

		for (int i = 0; i < _adblFXFwd.length; ++i)
			adblFXForwardBumped[i] = _adblFXFwd[i] + dblShift;

		try {
			return new DerivedFXForward (_cp, new org.drip.analytics.date.JulianDate (_dblSpotDate),
				_dblFXSpot, _adblDate, adblFXForwardBumped, _abIsPIP);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.definition.Curve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp)
	{
		if (null == ntp) return null;

		double[] adblFXBasisBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode (_adblFXFwd,
			ntp);

		if (null == adblFXBasisBumped || 0 == adblFXBasisBumped.length) return null;

		try {
			return new DerivedFXForward (_cp, new org.drip.analytics.date.JulianDate (_dblSpotDate),
				_dblFXSpot, _adblDate, adblFXBasisBumped, _abIsPIP);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getStartDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblSpotDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _dblSpotDate +
			getFieldDelimiter() + _dblFXSpot + getFieldDelimiter());

		for (int i = 0; i < _adblDate.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_adblDate[i] + getCollectionKeyValueDelimiter() + _adblFXFwd[i]);
		}

		sb.append (getFieldDelimiter());

		for (int i = 0; i < _abIsPIP.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_abIsPIP[i]);
		}

		sb.append (getFieldDelimiter() + new java.lang.String (_cp.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new DerivedFXForward (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public boolean buildInterpolator()
	{
		return false;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		double[] adblDate = new double[3];
		double[] adblFXFwd = new double[3];
		boolean[] abIsPIP = new boolean[3];

		for (int i = 0; i < 3; ++i) {
			adblDate[i] = dblStart + 365. * (i + 1);
			adblFXFwd[i] = 53.51 + i;
			abIsPIP[i] = false;
		}

		DerivedFXForward fxCurve = new DerivedFXForward (new org.drip.product.params.CurrencyPair
			("USD", "INR", "INR", 1.), org.drip.analytics.date.JulianDate.Today(), 53.51, adblDate,
				adblFXFwd, abIsPIP);

		byte[] abFXCurve = fxCurve.serialize();

		System.out.println ("Input: " + new java.lang.String (abFXCurve));

		DerivedFXForward fxCurveDeser = new DerivedFXForward (abFXCurve);

		System.out.println ("Output: " + new java.lang.String (fxCurveDeser.serialize()));
	}
}
