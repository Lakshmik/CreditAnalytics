
package org.drip.product.params;

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
 * Class the generates the component coupon periods from flexible inputs
 *
 * @author Lakshmi Krishnamurthy
 */

public class PeriodGenerator extends PeriodSet {
	private static final boolean m_bBlog = false;

	private boolean _bApplyAccEOMAdj = true;
	private java.lang.String _strCalendar = "";
	private boolean _bPeriodsFromForward = false;
	private double _dblFirstCouponDate = java.lang.Double.NaN;
	private double _dblInterestAccrualStart = java.lang.Double.NaN;
	private org.drip.analytics.daycount.DateAdjustParams _dapPay = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapReset = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapMaturity = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapEffective = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapPeriodEnd = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapAccrualEnd = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapPeriodStart = null;
	private org.drip.analytics.daycount.DateAdjustParams _dapAccrualStart = null;

	/**
	 * Generates the coupon periods from the date rules and the date adjustment rules for the different
	 * 	period dates
	 * 
	 * @param dblMaturity Maturity Date
	 * @param dblEffective Effective Date
	 * @param dblFinalMaturity Final Maturity Date
	 * @param dblFirstCouponDate First Coupon Date
	 * @param dblInterestAccrualStart Interest Accrual Start Date
	 * @param iFreq Coupon Frequency
	 * @param strCouponDC Coupon day count convention
	 * @param strAccrualDC Accrual day count convention
	 * @param dapPay Pay Date Adjustment Parameters
	 * @param dapReset Reset Date Adjustment Parameters
	 * @param dapMaturity Maturity Date Adjustment Parameters
	 * @param dapEffective Effective Date Adjustment Parameters
	 * @param dapPeriodEnd Period End Date Adjustment Parameters
	 * @param dapAccrualEnd Accrual Date Adjustment Parameters
	 * @param dapPeriodStart Period Start Date Adjustment Parameters
	 * @param dapAccrualStart Accrual Start  Date Adjustment Parameters
	 * @param strMaturityType Maturity Type
	 * @param bPeriodsFromForward Generate Periods forward (True) or Backward (False)
	 * @param strCalendar Optional Holiday Calendar for accrual calculations
	 */

	public PeriodGenerator (
		final double dblMaturity,
		final double dblEffective,
		final double dblFinalMaturity,
		final double dblFirstCouponDate,
		final double dblInterestAccrualStart,
		final int iFreq,
		final java.lang.String strCouponDC,
		final java.lang.String strAccrualDC,
		final org.drip.analytics.daycount.DateAdjustParams dapPay,
		final org.drip.analytics.daycount.DateAdjustParams dapReset,
		final org.drip.analytics.daycount.DateAdjustParams dapMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapEffective,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodStart,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualStart,
		final java.lang.String strMaturityType,
		final boolean bPeriodsFromForward,
		final java.lang.String strCalendar)
	{
		super (dblEffective, strCouponDC, iFreq, null);

		_dapPay = dapPay;
		_dapReset = dapReset;
		_dapMaturity = dapMaturity;
		_dblMaturity = dblMaturity;
		_strCalendar = strCalendar;
		_strAccrualDC = strAccrualDC;
		_dapEffective = dapEffective;
		_dapPeriodEnd = dapPeriodEnd;
		_dapAccrualEnd = dapAccrualEnd;
		_dapPeriodStart = dapPeriodStart;
		_strMaturityType = strMaturityType;
		_dapAccrualStart = dapAccrualStart;
		_dblFinalMaturity = dblFinalMaturity;
		_dblFirstCouponDate = dblFirstCouponDate;
		_bPeriodsFromForward = bPeriodsFromForward;
		_dblInterestAccrualStart = dblInterestAccrualStart;

		if (strCouponDC.toUpperCase().contains ("EOM")) _bApplyCpnEOMAdj = false;

		int iCouponDCIndex = strCouponDC.indexOf (" NON");

		if (-1 != iCouponDCIndex)
			_strCouponDC = strCouponDC.substring (0, iCouponDCIndex);
		else
			_strCouponDC = strCouponDC;

		if (strAccrualDC.toUpperCase().contains ("EOM")) _bApplyAccEOMAdj = false;

		int iAccrualDCIndex = strAccrualDC.indexOf (" NON");

		if (-1 != iAccrualDCIndex)
			_strAccrualDC = strAccrualDC.substring (0, iAccrualDCIndex);
		else
			_strAccrualDC = strAccrualDC;
	}

	/**
	 * PeriodGenerator de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if PeriodGenerator cannot be properly de-serialized
	 */

	public PeriodGenerator (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);
	}

	@Override public boolean validate()
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblEffective) ||
			!org.drip.math.common.NumberUtil.IsValid (_dblMaturity))
			return false;

		if (null == _dapPay)
			_dapPay = new org.drip.analytics.daycount.DateAdjustParams
				(org.drip.analytics.daycount.Convention.DR_FOLL, "USD");

		if (m_bBlog)
			System.out.println ("Starting " + org.drip.analytics.date.JulianDate.fromJulian (_dblEffective) +
				"->" + org.drip.analytics.date.JulianDate.fromJulian (_dblMaturity) + " with freq " + _iFreq
					+ " ...");

		if (0 == _iFreq) {
			if (null == (_lPeriods = org.drip.analytics.period.CouponPeriod.GetSinglePeriod (_dblEffective,
				_dblMaturity, _strCalendar)))
				return false;
		} else {
			if (_bPeriodsFromForward) {
				if (null == (_lPeriods = org.drip.analytics.period.CouponPeriod.GeneratePeriodsForward (
						_dblEffective, // Effective
-						_dblMaturity, // Maturity
						_dapEffective, // Effective DAP
						_dapMaturity, // Maturity DAP
						_dapPeriodStart, // Period Start DAP
						_dapPeriodEnd, // Period End DAP
						_dapAccrualStart, // Accrual Start DAP
						_dapAccrualEnd, // Accrual End DAP
						_dapPay, // Pay DAP
						_dapReset, // Reset DAP
						_iFreq, // Coupon Freq
						_strCouponDC, // Coupon Day Count
						_bApplyCpnEOMAdj,
						_strAccrualDC, // Accrual Day Count
						_bApplyAccEOMAdj,
						_strCalendar))
						|| 0 == _lPeriods.size())
						return false;
			} else {
				if (null == (_lPeriods = org.drip.analytics.period.CouponPeriod.GeneratePeriodsBackward (
					_dblEffective, // Effective
					_dblMaturity, // Maturity
					_dapEffective, // Effective DAP
					_dapMaturity, // Maturity DAP
					_dapPeriodStart, // Period Start DAP
					_dapPeriodEnd, // Period End DAP
					_dapAccrualStart, // Accrual Start DAP
					_dapAccrualEnd, // Accrual End DAP
					_dapPay, // Pay DAP
					_dapReset, // Reset DAP
					_iFreq, // Coupon Freq
					_strCouponDC, // Coupon Day Count
					_bApplyCpnEOMAdj,
					_strAccrualDC, // Accrual Day Count
					_bApplyAccEOMAdj,
					false, // Full First Coupon Period?
					true, // Merge the first 2 Periods - create a long stub?
					_strCalendar))
					|| 0 == _lPeriods.size())
					return false;
			}
		}

		if (org.drip.math.common.NumberUtil.IsValid (_dblFirstCouponDate))
			_lPeriods.get (0).setPayDate (_dblFirstCouponDate);

		if (org.drip.math.common.NumberUtil.IsValid (_dblInterestAccrualStart))
			_lPeriods.get (0).setAccrualStartDate (_dblInterestAccrualStart);

		return true;
	}

	@Override public java.util.List<org.drip.analytics.period.Period> getPeriods()
	{
		return _lPeriods;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.support.Logger.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		double dblEffective = org.drip.analytics.date.JulianDate.Today().getJulian();

		PeriodGenerator bpp = new PeriodGenerator (dblEffective + 3653., dblEffective, dblEffective + 3653.,
			dblEffective + 182., dblEffective, 2, "30/360", "30/360", null, null, null, null, null, null,
				null, null, "IGNORE", false, "USD");

		if (!bpp.validate()) {
			System.out.println ("Cannot validate BPP!");

			System.exit (47);
		}

		byte[] abBPP = bpp.serialize();

		System.out.println (new java.lang.String (abBPP));

		PeriodGenerator bppDeser = new PeriodGenerator (abBPP);

		System.out.println (new java.lang.String (bppDeser.serialize()));
	}
}
