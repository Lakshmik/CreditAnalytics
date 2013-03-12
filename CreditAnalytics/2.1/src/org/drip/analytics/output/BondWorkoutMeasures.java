
package org.drip.analytics.output;

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
 * This class encapsulates the parsimonius but complete set of measures generated out of a full bond
 * 		analytics run to a given work-out.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BondWorkoutMeasures extends org.drip.service.stream.Serializer {
	/**
	 * Clean Credit Risky Bond Coupon Measures
	 */

	public BondCouponMeasures _bcmCreditRiskyClean = null;

	/**
	 * Dirty Credit Risky Bond Coupon Measures
	 */

	public BondCouponMeasures _bcmCreditRiskyDirty = null;

	/**
	 * Clean Credit Risk-less Bond Coupon Measures
	 */

	public BondCouponMeasures _bcmCreditRisklessClean = null;

	/**
	 * Dirty Credit Risk-less Bond Coupon Measures
	 */

	public BondCouponMeasures _bcmCreditRisklessDirty = null;

	/**
	 * Credit Risky Par PV
	 */

	public double _dblCreditRiskyParPV = java.lang.Double.NaN;

	/**
	 * Credit Risk-less Par PV
	 */

	public double _dblCreditRisklessParPV = java.lang.Double.NaN;

	/**
	 * Credit Risky Principal PV
	 */

	public double _dblCreditRiskyPrincipalPV = java.lang.Double.NaN;

	/**
	 * Credit Risk-less Principal PV
	 */

	public double _dblCreditRisklessPrincipalPV = java.lang.Double.NaN;

	/**
	 * Recovery PV
	 */

	public double _dblRecoveryPV = java.lang.Double.NaN;

	/**
	 * Expected Recovery
	 */

	public double _dblExpectedRecovery = java.lang.Double.NaN;

	/**
	 * Default Exposure - Same as PV on instantaneous default
	 */

	public double _dblDefaultExposure = java.lang.Double.NaN;

	/**
	 * Default Exposure without recovery - Same as PV on instantaneous default without recovery
	 */

	public double _dblDefaultExposureNoRec = java.lang.Double.NaN;

	/**
	 * Loss On Instantaneous Default
	 */

	public double _dblLossOnInstantaneousDefault = java.lang.Double.NaN;

	/**
	 * Accrued 01
	 */

	public double _dblAccrued01 = java.lang.Double.NaN;

	/**
	 * First Coupon Rate
	 */

	public double _dblFirstCouponRate = java.lang.Double.NaN;

	/**
	 * First Index Rate
	 */

	public double _dblFirstIndexRate = java.lang.Double.NaN;

	/**
	 * BondWorkoutMeasures constructor
	 * 
	 * @param bcmCreditRiskyDirty Dirty credit risky BondMeasuresCoupon
	 * @param bcmCreditRisklessDirty Dirty credit risk-less BondMeasuresCoupon
	 * @param dblCreditRiskyParPV Credit risky Par PV
	 * @param dblCreditRisklessParPV Credit risk-less par PV
	 * @param dblCreditRiskyPrincipalPV Credit Risky Principal PV
	 * @param dblCreditRisklessPrincipalPV Credit Risk-less Principal PV
	 * @param dblRecoveryPV Recovery PV
	 * @param dblExpectedRecovery Expected Recovery
	 * @param dblDefaultExposure PV on instantaneous default
	 * @param dblDefaultExposureNoRec PV on instantaneous default with zero recovery
	 * @param dblLossOnInstantaneousDefault Loss On Instantaneous Default
	 * @param dblAccrued01 Accrued01
	 * @param dblFirstCouponRate First Coupon Rate
	 * @param dblFirstIndexRate First Index Rate
	 * @param dblCashPayDF Cash Pay Discount Factor
	 * 
	 * @throws java.lang.Exception
	 */

	public BondWorkoutMeasures (
		final BondCouponMeasures bcmCreditRiskyDirty,
		final BondCouponMeasures bcmCreditRisklessDirty,
		final double dblCreditRiskyParPV,
		final double dblCreditRisklessParPV,
		final double dblCreditRiskyPrincipalPV,
		final double dblCreditRisklessPrincipalPV,
		final double dblRecoveryPV,
		final double dblExpectedRecovery,
		final double dblDefaultExposure,
		final double dblDefaultExposureNoRec,
		final double dblLossOnInstantaneousDefault,
		final double dblAccrued01,
		final double dblFirstCouponRate,
		final double dblFirstIndexRate,
		final double dblCashPayDF)
		throws java.lang.Exception
	{
		if (null == bcmCreditRisklessDirty || !org.drip.math.common.NumberUtil.IsValid
			(dblCreditRisklessParPV) || !org.drip.math.common.NumberUtil.IsValid
				(dblCreditRisklessPrincipalPV) || !org.drip.math.common.NumberUtil.IsValid (dblAccrued01) ||
					!org.drip.math.common.NumberUtil.IsValid (dblFirstCouponRate))
			throw new java.lang.Exception ("Invalid inputs into BondWorkoutMeasures constructor!");

		_dblAccrued01 = dblAccrued01;
		_dblRecoveryPV = dblRecoveryPV;
		_dblFirstIndexRate = dblFirstIndexRate;
		_dblFirstCouponRate = dblFirstCouponRate;
		_dblDefaultExposure = dblDefaultExposure;
		_dblExpectedRecovery = dblExpectedRecovery;
		_bcmCreditRiskyDirty = bcmCreditRiskyDirty;
		_dblCreditRiskyParPV = dblCreditRiskyParPV;
		_bcmCreditRisklessDirty = bcmCreditRisklessDirty;
		_dblCreditRisklessParPV = dblCreditRisklessParPV;
		_dblDefaultExposureNoRec = dblDefaultExposureNoRec;
		_dblCreditRiskyPrincipalPV = dblCreditRiskyPrincipalPV;
		_dblCreditRisklessPrincipalPV = dblCreditRisklessPrincipalPV;
		_dblLossOnInstantaneousDefault = dblLossOnInstantaneousDefault;

		if (!(_bcmCreditRisklessClean = new BondCouponMeasures (_bcmCreditRisklessDirty._dblDV01,
			_bcmCreditRisklessDirty._dblIndexCouponPV, _bcmCreditRisklessDirty._dblCouponPV,
				_bcmCreditRisklessDirty._dblPV)).adjustForSettlement (dblCashPayDF))
			throw new java.lang.Exception ("Cannot successfully set up BCM CreditRisklessClean");

		if (!_bcmCreditRisklessClean.adjustForAccrual (_dblAccrued01, _dblFirstCouponRate, dblFirstIndexRate,
			false))
			throw new java.lang.Exception ("Cannot successfully set up BCM CreditRisklessClean");

		if (null != _bcmCreditRiskyDirty && ((!(_bcmCreditRiskyClean = new BondCouponMeasures
			(_bcmCreditRiskyDirty._dblDV01, _bcmCreditRiskyDirty._dblIndexCouponPV,
				_bcmCreditRiskyDirty._dblCouponPV, _bcmCreditRiskyDirty._dblPV)).adjustForSettlement
					(dblCashPayDF)) || !_bcmCreditRiskyClean.adjustForAccrual (_dblAccrued01,
						_dblFirstCouponRate, _dblFirstCouponRate, false)))
			throw new java.lang.Exception ("Cannot successfully set up BCM CreditRiskyClean");
	}

	/**
	 * BondWorkoutMeasures de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if BondWorkoutMeasures cannot be properly de-serialized
	 */

	public BondWorkoutMeasures (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("BondWorkoutMeasures de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("BondWorkoutMeasures de-serializer: Empty state");

		java.lang.String strSerializedBondWorkoutMeasures = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedBondWorkoutMeasures || strSerializedBondWorkoutMeasures.isEmpty())
			throw new java.lang.Exception ("BondWorkoutMeasures de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedBondWorkoutMeasures, getFieldDelimiter());

		if (null == astrField || 17 > astrField.length)
			throw new java.lang.Exception ("BondWorkoutMeasures de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty())
			throw new java.lang.Exception
				("BondWorkoutMeasures de-serializer: Cannot locate credit risky clean BCM");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[1]))
			_bcmCreditRiskyClean = null;
		else
			_bcmCreditRiskyClean = new org.drip.analytics.output.BondCouponMeasures
				(astrField[1].getBytes());

		if (null == astrField[2] || astrField[2].isEmpty())
			throw new java.lang.Exception
				("BondWorkoutMeasures de-serializer: Cannot locate credit risky dirty BCM");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[2]))
			_bcmCreditRiskyDirty = null;
		else
			_bcmCreditRiskyDirty = new org.drip.analytics.output.BondCouponMeasures
				(astrField[2].getBytes());

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception
				("BondWorkoutMeasures de-serializer: Cannot locate credit riskless clean BCM");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[3]))
			_bcmCreditRisklessClean = null;
		else
			_bcmCreditRisklessClean = new org.drip.analytics.output.BondCouponMeasures
				(astrField[3].getBytes());

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception
				("BondWorkoutMeasures de-serializer: Cannot locate credit riskless dirty BCM");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equals (astrField[4]))
			_bcmCreditRisklessDirty = null;
		else
			_bcmCreditRisklessDirty = new org.drip.analytics.output.BondCouponMeasures
				(astrField[4].getBytes());

		if (null == astrField[5] || astrField[5].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate credit risky Par PV");

		_dblCreditRiskyParPV = new java.lang.Double (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate credit riskless Par PV");

		_dblCreditRisklessParPV = new java.lang.Double (astrField[6]);

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate credit risky Principal PV");

		_dblCreditRiskyPrincipalPV = new java.lang.Double (astrField[7]);

		if (null == astrField[8] || astrField[8].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate credit riskless Principal PV");

		_dblCreditRisklessPrincipalPV = new java.lang.Double (astrField[8]);

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception ("BondCouponMeasures de-serializer: Cannot locate recovery PV");

		_dblRecoveryPV = new java.lang.Double (astrField[9]);

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate expected recovery");

		_dblExpectedRecovery = new java.lang.Double (astrField[10]);

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception ("BondCouponMeasures de-serializer: Cannot locate accrued 01");

		_dblAccrued01 = new java.lang.Double (astrField[11]);

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate first coupon rate");

		_dblFirstCouponRate = new java.lang.Double (astrField[12]);

		if (null == astrField[13] || astrField[13].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate first index rate");

		_dblFirstIndexRate = new java.lang.Double (astrField[13]);

		if (null == astrField[14] || astrField[14].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate default exposure");

		_dblDefaultExposure = new java.lang.Double (astrField[14]);

		if (null == astrField[15] || astrField[15].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate default exposure without recovery");

		_dblDefaultExposureNoRec = new java.lang.Double (astrField[15]);

		if (null == astrField[16] || astrField[16].isEmpty())
			throw new java.lang.Exception
				("BondCouponMeasures de-serializer: Cannot locate loss on instantenous default");

		_dblLossOnInstantaneousDefault = new java.lang.Double (astrField[16]);
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "@";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "!";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _bcmCreditRiskyClean)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_bcmCreditRiskyClean.serialize()) + getFieldDelimiter());

		if (null == _bcmCreditRiskyDirty)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_bcmCreditRiskyDirty.serialize()) + getFieldDelimiter());

		if (null == _bcmCreditRisklessClean)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_bcmCreditRisklessClean.serialize()) + getFieldDelimiter());

		if (null == _bcmCreditRisklessDirty)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_bcmCreditRisklessDirty.serialize()) + getFieldDelimiter());

		sb.append (_dblCreditRiskyParPV + getFieldDelimiter() + _dblCreditRisklessParPV + getFieldDelimiter()
			+ _dblCreditRiskyPrincipalPV + getFieldDelimiter() + _dblCreditRisklessPrincipalPV +
				getFieldDelimiter() + _dblRecoveryPV + getFieldDelimiter() + _dblExpectedRecovery +
					getFieldDelimiter() + _dblAccrued01 + getFieldDelimiter() + _dblFirstCouponRate +
						getFieldDelimiter() + _dblFirstIndexRate + getFieldDelimiter() + _dblDefaultExposure
							+ getFieldDelimiter() + _dblDefaultExposureNoRec + getFieldDelimiter() +
								_dblLossOnInstantaneousDefault);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	/**
	 * Returns the state as a measure map
	 * 
	 * @param strPrefix Measure name prefix
	 * 
	 * @return Map of the measures
	 */

	public java.util.Map<java.lang.String, java.lang.Double> toMap (
		final java.lang.String strPrefix)
	{
		java.util.Map<java.lang.String, java.lang.Double> mapMeasures = new
			java.util.TreeMap<java.lang.String, java.lang.Double>();

		mapMeasures.put (strPrefix + "Accrued", _dblAccrued01 * _dblFirstCouponRate);

		mapMeasures.put (strPrefix + "Accrued01", _dblAccrued01);

		mapMeasures.put (strPrefix + "CleanCouponPV", _bcmCreditRisklessClean._dblCouponPV);

		mapMeasures.put (strPrefix + "CleanDV01", _bcmCreditRisklessClean._dblDV01);

		mapMeasures.put (strPrefix + "CleanIndexCouponPV", _bcmCreditRisklessClean._dblIndexCouponPV);

		mapMeasures.put (strPrefix + "CleanPrice", _bcmCreditRisklessClean._dblPV);

		mapMeasures.put (strPrefix + "CleanPV", _bcmCreditRisklessClean._dblPV);

		mapMeasures.put (strPrefix + "CreditRisklessParPV", _dblCreditRisklessParPV);

		mapMeasures.put (strPrefix + "CreditRisklessPrincipalPV", _dblCreditRisklessPrincipalPV);

		mapMeasures.put (strPrefix + "CreditRiskyParPV", _dblCreditRiskyParPV);

		mapMeasures.put (strPrefix + "CreditRiskyPrincipalPV", _dblCreditRiskyPrincipalPV);

		mapMeasures.put (strPrefix + "DefaultExposure", _dblDefaultExposure);

		mapMeasures.put (strPrefix + "DefaultExposureNoRec", _dblDefaultExposureNoRec);

		mapMeasures.put (strPrefix + "DirtyCouponPV", _bcmCreditRisklessDirty._dblCouponPV);

		mapMeasures.put (strPrefix + "DirtyDV01", _bcmCreditRisklessDirty._dblDV01);

		mapMeasures.put (strPrefix + "DirtyIndexCouponPV", _bcmCreditRisklessDirty._dblIndexCouponPV);

		mapMeasures.put (strPrefix + "DirtyPrice", _bcmCreditRisklessDirty._dblPV);

		mapMeasures.put (strPrefix + "DirtyPV", _bcmCreditRisklessDirty._dblPV);

		mapMeasures.put (strPrefix + "DV01", _bcmCreditRisklessClean._dblDV01);

		mapMeasures.put (strPrefix + "ExpectedRecovery", _dblExpectedRecovery);

		mapMeasures.put (strPrefix + "FirstCouponRate", _dblFirstCouponRate);

		mapMeasures.put (strPrefix + "FirstIndexRate", _dblFirstIndexRate);

		mapMeasures.put (strPrefix + "LossOnInstantaneousDefault", _dblLossOnInstantaneousDefault);

		mapMeasures.put (strPrefix + "ParPV", _dblCreditRisklessParPV);

		mapMeasures.put (strPrefix + "PrincipalPV", _dblCreditRisklessPrincipalPV);

		mapMeasures.put (strPrefix + "PV", _bcmCreditRisklessClean._dblPV);

		mapMeasures.put (strPrefix + "RecoveryPV", _dblRecoveryPV);

		org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, _bcmCreditRisklessDirty.toMap
			(strPrefix + "RisklessDirty"));

		org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, _bcmCreditRisklessClean.toMap
			(strPrefix + "RisklessClean"));

		if (null != _bcmCreditRiskyDirty) {
			mapMeasures.put (strPrefix + "CleanCouponPV", _bcmCreditRiskyClean._dblCouponPV);

			mapMeasures.put (strPrefix + "CleanDV01", _bcmCreditRiskyClean._dblDV01);

			mapMeasures.put (strPrefix + "CleanIndexCouponPV", _bcmCreditRiskyClean._dblIndexCouponPV);

			mapMeasures.put (strPrefix + "CleanPrice", _bcmCreditRiskyClean._dblPV);

			mapMeasures.put (strPrefix + "CleanPV", _bcmCreditRiskyClean._dblPV);

			mapMeasures.put (strPrefix + "DirtyCouponPV", _bcmCreditRiskyDirty._dblCouponPV);

			mapMeasures.put (strPrefix + "DirtyDV01", _bcmCreditRiskyDirty._dblDV01);

			mapMeasures.put (strPrefix + "DirtyIndexCouponPV", _bcmCreditRiskyDirty._dblIndexCouponPV);

			mapMeasures.put (strPrefix + "DirtyPrice", _bcmCreditRiskyDirty._dblPV);

			mapMeasures.put (strPrefix + "DirtyPV", _bcmCreditRiskyDirty._dblPV);

			mapMeasures.put (strPrefix + "DV01", _bcmCreditRiskyClean._dblDV01);

			mapMeasures.put (strPrefix + "ParPV", _dblCreditRiskyParPV);

			mapMeasures.put (strPrefix + "PrincipalPV", _dblCreditRiskyPrincipalPV);

			mapMeasures.put (strPrefix + "PV", _bcmCreditRiskyClean._dblPV);

			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, _bcmCreditRiskyDirty.toMap
				(strPrefix + "RiskyDirty"));

			org.drip.analytics.support.GenericUtil.MergeWithMain (mapMeasures, _bcmCreditRiskyClean.toMap
				(strPrefix + "RiskyClean"));
		}

		return mapMeasures;
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new BondWorkoutMeasures (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		BondCouponMeasures bcm = new BondCouponMeasures (1., 2., 3., 4.);

		BondWorkoutMeasures bwm = new BondWorkoutMeasures (null, bcm, 4., 5., 6., 7., 8., 9., 10., 11., 12.,
			13., 14., 15., 16.);

		byte[] abBWM = bwm.serialize();

		System.out.println (new java.lang.String (abBWM));

		BondWorkoutMeasures bwmDeser = new BondWorkoutMeasures (abBWM);

		System.out.println ("\n" + new java.lang.String (bwmDeser.serialize()));
	}
}
