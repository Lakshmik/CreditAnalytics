
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
 * This class implements the FXBasis curve representing term structure of FX basis. Basis can be full or
 * 		bootstrapped.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DerivedFXBasis extends org.drip.analytics.definition.FXBasisCurve {
	private double[] _adblDate = null;
	private double[] _adblFXBasis = null;
	private boolean _bIsFXBasisBootstrapped = false;
	private double _dblFXSpot = java.lang.Double.NaN;
	private double _dblSpotDate = java.lang.Double.NaN;
	private org.drip.product.params.CurrencyPair _cp = null;

	/**
	 * Constructs an FXBasis instance from the currency pair, FX Spot, and FX basis parameters
	 * 
	 * @param cp Currency Pair
	 * @param dtSpot Spot Date
	 * @param dblFXSpot FX Spot
	 * @param adblDate Array of dates
	 * @param adblFXBasis Array of FX Basis
	 * @param bIsFXBasisBootstrapped True if the inputs are for bootstrapped FX basis
	 * 
	 * @throws java.lang.Exception Thrown if the FXBasis instance cannot be created
	 */

	public DerivedFXBasis (
		final org.drip.product.params.CurrencyPair cp,
		final org.drip.analytics.date.JulianDate dtSpot,
		final double dblFXSpot,
		final double[] adblDate,
		final double[] adblFXBasis,
		final boolean bIsFXBasisBootstrapped)
		throws java.lang.Exception
	{
		if (null == cp || null == dtSpot || !org.drip.math.common.NumberUtil.IsValid (dblFXSpot) || null ==
			adblDate || 0 == adblDate.length || null == adblFXBasis || 0 == adblFXBasis.length ||
				adblDate.length != adblFXBasis.length)
			throw new java.lang.Exception ("Invalid params into FXBasis ctr!");

		_dblSpotDate = dtSpot.getJulian();

		_cp = cp;
		_dblFXSpot = dblFXSpot;
		_adblDate = new double[adblDate.length];
		_adblFXBasis = new double[adblFXBasis.length];
		_bIsFXBasisBootstrapped = bIsFXBasisBootstrapped;

		for (int i = 0; i < adblFXBasis.length; ++i) {
			if (!org.drip.math.common.NumberUtil.IsValid (adblDate[i]) || adblDate[i] <= _dblSpotDate)
				throw new java.lang.Exception ("Invalid params into FXCurve ctr: Node date " +
					org.drip.analytics.date.JulianDate.fromJulian (adblDate[i]) + " before spot " + dtSpot);

			_adblDate[i] = adblDate[i];
			_adblFXBasis[i] = adblFXBasis[i];
		}
	}

	/**
	 * FXBasis de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FXBasis cannot be properly de-serialized
	 */

	public DerivedFXBasis (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FXBasis de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FXBasis de-serializer: Empty state");

		java.lang.String strFXBasis = strRawString.substring (0, strRawString.indexOf (getObjectTrailer()));

		if (null == strFXBasis || strFXBasis.isEmpty())
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split (strFXBasis,
			getFieldDelimiter());

		if (null == astrField || 6 > astrField.length)
			throw new java.lang.Exception ("FXBasis de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot locate spot date");

		_dblSpotDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot locate spot FX");

		_dblFXSpot = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot locate boot strap flag");

		_bIsFXBasisBootstrapped = new java.lang.Boolean (astrField[3]).booleanValue();

		java.util.List<java.lang.Double> lsdblDate = new java.util.ArrayList<java.lang.Double>();

		java.util.List<java.lang.Double> lsdblBasis = new java.util.ArrayList<java.lang.Double>();

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot decode state");

		if (!org.drip.analytics.support.GenericUtil.KeyValueListFromStringArray (lsdblDate, lsdblBasis,
			astrField[4], getCollectionRecordDelimiter(), getCollectionKeyValueDelimiter()))
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot decode state");

		if (0 == lsdblDate.size() || 0 == lsdblBasis.size() || lsdblDate.size() != lsdblBasis.size())
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot decode state");

		_adblDate = new double[lsdblDate.size()];

		_adblFXBasis = new double[lsdblBasis.size()];

		for (int i = 0; i < _adblDate.length; ++i) {
			_adblDate[i] = lsdblDate.get (i);

			_adblFXBasis[i] = lsdblBasis.get (i);
		}

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[5]))
			throw new java.lang.Exception ("FXBasis de-serializer: Cannot locate currency pair");

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

	@Override public boolean IsBasisBootstrapped()
	{
		return _bIsFXBasisBootstrapped;
	}

	@Override public double[] getFullFXFwd (
		final org.drip.param.valuation.ValuationParams valParam,
		final org.drip.analytics.definition.DiscountCurve dcNum,
		final org.drip.analytics.definition.DiscountCurve dcDenom,
		final boolean bBasisOnDenom,
		final boolean bFwdAsPIP)
	{
		org.drip.analytics.definition.DiscountCurve dcBasisAdj = null;
		double[] adblFXFwd = new double[_adblFXBasis.length];

		if (bBasisOnDenom)
			dcBasisAdj = dcDenom;
		else
			dcBasisAdj = dcNum;

		for (int i = 0; i < _adblFXBasis.length; ++i) {
			try {
				org.drip.product.definition.FXForward fxfwd =
					org.drip.product.creator.FXForwardBuilder.CreateFXForward (_cp, new
						org.drip.analytics.date.JulianDate (_dblSpotDate), new
							org.drip.analytics.date.JulianDate (_adblDate[i]));

				if (null == fxfwd) {
					System.out.println ("Cannot make fxfwd at maturity " +
						org.drip.analytics.date.JulianDate.fromJulian (_adblDate[i]));

					return null;
				}

				if (bBasisOnDenom) {
					if (_bIsFXBasisBootstrapped)
						dcBasisAdj.bumpNodeValue (i, _adblFXBasis[i]);
					else
						dcBasisAdj = (org.drip.analytics.definition.DiscountCurve)
							dcDenom.createParallelShiftedCurve (_adblFXBasis[i]);

					if (null == dcBasisAdj) {
						System.out.println ("Cannot create bootstrapped/full denom curve at node " +
							org.drip.analytics.date.JulianDate.fromJulian (_adblDate[i]));

						return null;
					}

					adblFXFwd[i] = fxfwd.implyFXForward (valParam, dcNum, dcBasisAdj, _dblFXSpot,
						bFwdAsPIP);
				} else {
					if (_bIsFXBasisBootstrapped)
						dcBasisAdj.bumpNodeValue (i, _adblFXBasis[i]);
					else
						dcBasisAdj = (org.drip.analytics.definition.DiscountCurve)
							dcNum.createParallelShiftedCurve (_adblFXBasis[i]);

					if (null == dcBasisAdj) {
						System.out.println ("Cannot create bootstrapped/full num curve at node " +
							org.drip.analytics.date.JulianDate.fromJulian (_adblDate[i]));

						return null;
					}

					adblFXFwd[i] = fxfwd.implyFXForward (valParam, dcBasisAdj, dcDenom, _dblFXSpot,
						bFwdAsPIP);
				}
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return adblFXFwd;
	}

	@Override public boolean setNodeValue (
		final int iIndex,
		final double dblValue)
	{
		if (_adblFXBasis.length <= iIndex) return false;

		_adblFXBasis[iIndex] = dblValue;
		return true;
	}

	@Override public boolean bumpNodeValue (
		final int iIndex,
		final double dblValue)
	{
		if (_adblFXBasis.length <= iIndex) return false;

		_adblFXBasis[iIndex] += dblValue;
		return true;
	}

	@Override public boolean setFlatValue (
		final double dblValue)
	{
		for (int i = 0; i < _adblFXBasis.length; ++i)
			_adblFXBasis[i] = dblValue;

		return true;
	}

	@Override public java.lang.String displayString()
	{
		try {
			java.lang.StringBuffer sb = new java.lang.StringBuffer();

			for (int i = 0; i < _adblDate.length; ++i) {
				if (0 != i) sb.append (" | ");

				sb.append (new org.drip.analytics.date.JulianDate (_adblDate[i]) + "=" + _adblFXBasis[i]);
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

			sb.append (_adblDate[i] + ":" + _adblFXBasis[i]);
		}

		return sb.toString();
	}

	@Override public org.drip.analytics.definition.Curve createParallelShiftedCurve (
		final double dblShift)
	{
		double[] adblFXBasisBumped = new double[_adblFXBasis.length];

		for (int i = 0; i < _adblFXBasis.length; ++i)
			adblFXBasisBumped[i] = _adblFXBasis[i] + dblShift;

		try {
			return new DerivedFXBasis (_cp, new org.drip.analytics.date.JulianDate (_dblSpotDate),
				_dblFXSpot, _adblDate, adblFXBasisBumped, _bIsFXBasisBootstrapped);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.definition.Curve createTweakedCurve (
		final org.drip.param.definition.NodeTweakParams ntp)
	{
		if (null == ntp) return null;

		double[] adblFXBasisBumped = org.drip.analytics.support.AnalyticsHelper.BumpNTPNode (_adblFXBasis,
			ntp);

		if (null == adblFXBasisBumped || 0 == adblFXBasisBumped.length) return null;

		try {
			return new DerivedFXBasis (_cp, new org.drip.analytics.date.JulianDate (_dblSpotDate),
				_dblFXSpot, _adblDate, adblFXBasisBumped, _bIsFXBasisBootstrapped);
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
			getFieldDelimiter() + _dblFXSpot + getFieldDelimiter() + _bIsFXBasisBootstrapped +
				getFieldDelimiter());

		for (int i = 0; i < _adblDate.length; ++i) {
			if (0 != i) sb.append (getCollectionRecordDelimiter());

			sb.append (_adblDate[i] + getCollectionKeyValueDelimiter() + _adblFXBasis[i]);
		}

		sb.append (getFieldDelimiter() + new java.lang.String (_cp.serialize()));

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab)
	{
		try {
			return new DerivedFXBasis (ab);
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
		double[] adblFXBasis = new double[3];

		for (int i = 0; i < 3; ++i) {
			adblDate[i] = dblStart + 365. * (i + 1);
			adblFXBasis[i] = 0.02 * (i + 1);
		}

		DerivedFXBasis fxBasis = new DerivedFXBasis (new org.drip.product.params.CurrencyPair
			("USD", "INR", "INR", 1.), org.drip.analytics.date.JulianDate.Today(), 53.51, adblDate,
				adblFXBasis, true);

		byte[] abFXBasis = fxBasis.serialize();

		System.out.println ("Input: " + new java.lang.String (abFXBasis));

		DerivedFXBasis fxBasisDeser = new DerivedFXBasis (abFXBasis);

		System.out.println ("Output: " + new java.lang.String (fxBasisDeser.serialize()));
	}
}
