
package org.drip.analytics.period;

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
 * This class extends the period class with a few day-count specific parameters such as: frequency, reset
 * 		date, and accrual day-count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CouponPeriod extends Period {
	private static final boolean s_bLog = false;

	private int _iFreq = 2;
	private boolean _bApplyAccEOMAdj = false;
	private java.lang.String _strCalendar = "";
	private double _dblReset = java.lang.Double.NaN;
	private java.lang.String _strAccrualDC = "30/360";
	private double _dblMaturity = java.lang.Double.NaN;

	private static final double DAPAdjust (
		final double dblDate,
		final org.drip.analytics.daycount.DateAdjustParams dap)
	{
		if (null == dap) return dblDate;

		try {
			return dap.Roll (dblDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return dblDate;
	}

	/** Generates the period list backward starting from the end.
	 * 
	 * @param dblEffective Effective date
	 * @param dblMaturity Maturity date
	 * @param dapEffective Effective date Date Adjust Parameters
	 * @param dapMaturity Maturity date Date Adjust Parameters
	 * @param dapPeriodStart Period Start date Date Adjust Parameters
	 * @param dapPeriodEnd Period End date Date Adjust Parameters
	 * @param dapAccrualStart Accrual Start date Date Adjust Parameters
	 * @param dapAccrualEnd Accrual End date Date Adjust Parameters
	 * @param dapPay Pay date Date Adjust Parameters
	 * @param dapReset Reset date Date Adjust Parameters
	 * @param iFreq Frequency
	 * @param strCouponDC Coupon day count
	 * @param bApplyCpnEOMAdj Apply end-of-month adjustment to the coupon periods
	 * @param strAccrualDC Accrual day count
	 * @param bApplyAccEOMAdj Apply end-of-month adjustment to the accrual periods
	 * @param bFullStub True - generates full first stub
	 * @param strCalendar Optional Holiday Calendar for accrual
	 * 
	 * @return List of coupon Periods
	 */

	public static final java.util.List<Period> GeneratePeriodsBackward (
		final double dblEffective,
		final double dblMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapEffective,
		final org.drip.analytics.daycount.DateAdjustParams dapMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodStart,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualStart,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapPay,
		final org.drip.analytics.daycount.DateAdjustParams dapReset,
		final int iFreq,
		final java.lang.String strCouponDC,
		final boolean bApplyCpnEOMAdj,
		final java.lang.String strAccrualDC,
		final boolean bApplyAccEOMAdj,
		final boolean bFullStub,
		final java.lang.String strCalendar)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblEffective) ||
			!org.drip.math.common.NumberUtil.IsValid (dblMaturity) || dblEffective >= dblMaturity || 0 ==
				iFreq)
			return null;

		boolean bFinalPeriod = true;
		boolean bGenerationDone = false;
		double dblPeriodEndDate = dblMaturity;
		java.lang.String strTenor = (12 / iFreq) + "M";
		double dblPeriodStartDate = java.lang.Double.NaN;

		try {
			dblPeriodStartDate = new org.drip.analytics.date.JulianDate (dblPeriodEndDate).subtractTenor
				(strTenor).getJulian();
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		java.util.List<Period> lsPeriods = new java.util.ArrayList<Period>();

		while (!bGenerationDone) {
			if (dblPeriodStartDate <= dblEffective) {
				if (!bFullStub) dblPeriodStartDate = dblEffective;

				bGenerationDone = true;
			}

			try {
				if (bFinalPeriod) {
					lsPeriods.add (0, new CouponPeriod (DAPAdjust (dblPeriodStartDate, dapPeriodStart),
						dblPeriodEndDate, DAPAdjust (dblPeriodStartDate, dapAccrualStart), dblPeriodEndDate,
							DAPAdjust (dblPeriodEndDate, dapPay), DAPAdjust (dblPeriodStartDate, dapReset),
								iFreq, strCouponDC, bApplyCpnEOMAdj, strAccrualDC, bApplyAccEOMAdj,
									dblMaturity, strCalendar));

					bFinalPeriod = false;
				} else
					lsPeriods.add (0, new CouponPeriod (DAPAdjust (dblPeriodStartDate, dapPeriodStart),
						DAPAdjust (dblPeriodEndDate, dapPeriodEnd), DAPAdjust (dblPeriodStartDate,
							dapAccrualStart), DAPAdjust (dblPeriodEndDate, dapAccrualEnd), DAPAdjust
								(dblPeriodEndDate, dapPay), DAPAdjust (dblPeriodStartDate, dapReset), iFreq,
									strCouponDC, bApplyCpnEOMAdj, strAccrualDC, bApplyAccEOMAdj, dblMaturity,
										strCalendar));
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			dblPeriodEndDate = dblPeriodStartDate;

			try {
				dblPeriodStartDate = new org.drip.analytics.date.JulianDate (dblPeriodEndDate).subtractTenor
					(strTenor).getJulian();
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return lsPeriods;
	}

	/**
	 * Generates the period list forward starting from the start.
	 * 
	 * @param dblEffective Effective date
	 * @param dblMaturity Maturity date
	 * @param dapEffective Effective date Date Adjust Parameters
	 * @param dapMaturity Maturity date Date Adjust Parameters
	 * @param dapPeriodStart Period Start date Date Adjust Parameters
	 * @param dapPeriodEnd Period End date Date Adjust Parameters
	 * @param dapAccrualStart Accrual Start date Date Adjust Parameters
	 * @param dapAccrualEnd Accrual End date Date Adjust Parameters
	 * @param dapPay Pay date Date Adjust Parameters
	 * @param dapReset Reset date Date Adjust Parameters
	 * @param iFreq Frequency
	 * @param strCouponDC Coupon day count
	 * @param bApplyCpnEOMAdj Apply end-of-month adjustment to the coupon periods
	 * @param strAccrualDC Accrual day count
	 * @param bApplyAccEOMAdj Apply end-of-month adjustment to the accrual periods
	 * @param strCalendar Optional Holiday Calendar for accrual
	 * 
	 * @return List of coupon Periods
	 */

	public static final java.util.List<Period> GeneratePeriodsForward (
		final double dblEffective,
		final double dblMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapEffective,
		final org.drip.analytics.daycount.DateAdjustParams dapMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodStart,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualStart,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapPay,
		final org.drip.analytics.daycount.DateAdjustParams dapReset,
		final int iFreq,
		final java.lang.String strCouponDC,
		final boolean bApplyCpnEOMAdj,
		final java.lang.String strAccrualDC,
		final boolean bApplyAccEOMAdj,
		final java.lang.String strCalendar)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblEffective) ||
			!org.drip.math.common.NumberUtil.IsValid (dblMaturity) || dblEffective >= dblMaturity || 0 ==
				iFreq)
			return null;

		boolean bFinalPeriod = false;
		double dblPeriodDays = 365.25 / iFreq;
		double dblPeriodStartDate = dblEffective;
		double dblPeriodEndDate = dblPeriodStartDate + dblPeriodDays;

		java.util.List<Period> lsPeriods = new java.util.ArrayList<Period>();

		while (!bFinalPeriod) {
			if (dblPeriodEndDate >= dblMaturity) {
				bFinalPeriod = true;
				dblPeriodEndDate = dblMaturity;
			}

			try {
				if (!bFinalPeriod)
					lsPeriods.add (0, new CouponPeriod (DAPAdjust (dblPeriodStartDate, dapPeriodStart),
						DAPAdjust (dblPeriodEndDate, dapPeriodEnd), DAPAdjust (dblPeriodStartDate,
							dapAccrualStart), DAPAdjust (dblPeriodEndDate, dapAccrualEnd), DAPAdjust
								(dblPeriodEndDate, dapPay), DAPAdjust (dblPeriodStartDate, dapReset), iFreq,
									strCouponDC, bApplyCpnEOMAdj, strAccrualDC, bApplyAccEOMAdj, dblMaturity,
										strCalendar));
				else
					lsPeriods.add (0, new CouponPeriod (DAPAdjust (dblPeriodStartDate, dapPeriodStart),
						dblPeriodEndDate, DAPAdjust (dblPeriodStartDate, dapAccrualStart), dblPeriodEndDate,
							DAPAdjust (dblPeriodEndDate, dapPay), DAPAdjust (dblPeriodStartDate, dapReset),
								iFreq, strCouponDC, bApplyCpnEOMAdj, strAccrualDC, bApplyAccEOMAdj,
									dblMaturity, strCalendar));
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			dblPeriodEndDate = dblPeriodStartDate;
			dblPeriodStartDate = dblPeriodEndDate - dblPeriodDays;
		}

		return lsPeriods;
	}

	/**
	 * Generates a single coupon period between the effective and the maturity dates
	 * 
	 * @param dblEffective Effective date
	 * @param dblMaturity Maturity date
	 * @param strCalendar Optional Holiday Calendar for accrual
	 * 
	 * @return List containing the single coupon period
	 */

	public static final java.util.List<Period> GetSinglePeriod (
		final double dblEffective,
		final double dblMaturity,
		final java.lang.String strCalendar)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblEffective) ||
			!org.drip.math.common.NumberUtil.IsValid (dblMaturity) || dblEffective >= dblMaturity)
			return null;

		java.util.List<Period> lsPeriods = new java.util.ArrayList<Period>();

		try {
			lsPeriods.add (0, new CouponPeriod (dblEffective, dblMaturity, dblEffective, dblMaturity,
				dblMaturity, dblEffective, 2, "30/360", true, "30/360", true, dblMaturity, strCalendar));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return lsPeriods;
	}

	/**
	 * Constructs a CouponPeriod instance from the specified dates
	 * 
	 * @param dblStart Period Start Date
	 * @param dblEnd Period End Date
	 * @param dblAccrualStart Period Accrual Start Date
	 * @param dblAccrualEnd Period Accrual End Date
	 * @param dblPay Period Pay Date
	 * @param dblReset Period Reset Date
	 * @param iFreq Frequency
	 * @param strCouponDC Coupon day count
	 * @param bApplyCpnEOMAdj Apply end-of-month adjustment to the coupon periods
	 * @param strAccrualDC Accrual Day count
	 * @param bApplyAccEOMAdj Apply end-of-month adjustment to the accrual periods
	 * @param dblMaturity Maturity date
	 * @param strCalendar Holiday Calendar
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public CouponPeriod (
		final double dblStart,
		final double dblEnd,
		final double dblAccrualStart,
		final double dblAccrualEnd,
		final double dblPay,
		final double dblReset,
		final int iFreq,
		final java.lang.String strCouponDC,
		final boolean bApplyCpnEOMAdj,
		final java.lang.String strAccrualDC,
		final boolean bApplyAccEOMAdj,
		final double dblMaturity,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		/* super (dblStart, dblEnd, dblAccrualStart, dblAccrualEnd, dblPay,
			org.drip.analytics.daycount.Convention.YearFraction (dblAccrualStart, dblAccrualEnd,
				strCouponDC, bApplyCpnEOMAdj, dblMaturity, new org.drip.analytics.daycount.ActActDCParams
					(iFreq, dblAccrualStart, dblAccrualEnd), strCalendar)); */

		super (dblStart, dblEnd, dblAccrualStart, dblAccrualEnd, dblPay, 1. / iFreq);

		if (s_bLog)
			System.out.println (org.drip.analytics.date.JulianDate.fromJulian (dblStart) + "=>" +
				org.drip.analytics.date.JulianDate.fromJulian (dblEnd) + " | " +
					org.drip.analytics.date.JulianDate.fromJulian (dblPay));

		_iFreq = iFreq;
		_dblReset = dblReset;
		_dblMaturity= dblMaturity;
		_strCalendar = strCalendar;
		_strAccrualDC = strAccrualDC;
		_bApplyAccEOMAdj = bApplyAccEOMAdj;
	}

	/**
	 * De-serialization of CouponPeriod from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize CouponPeriod
	 */

	public CouponPeriod (
		final byte[] ab)
		throws java.lang.Exception
	{
		super (ab);
	}

	@Override public double getResetDate()
	{
		return _dblReset;
	}

	@Override public double getAccrualDCF (
		final double dblAccrualEnd)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblAccrualEnd))
			throw new java.lang.Exception ("Accrual end is NaN!");

		if (_dblAccrualStart > dblAccrualEnd && dblAccrualEnd > _dblAccrualEnd)
			throw new java.lang.Exception ("Invalid in-period accrual date!");

		org.drip.analytics.daycount.ActActDCParams aap = new org.drip.analytics.daycount.ActActDCParams
			(_iFreq, _dblAccrualStart, _dblAccrualEnd);

		double dblCurrentDCF = org.drip.analytics.daycount.Convention.YearFraction (_dblAccrualStart,
			dblAccrualEnd, _strAccrualDC, _bApplyAccEOMAdj, _dblMaturity, aap, _strCalendar);

		double dblPeriodDCF = org.drip.analytics.daycount.Convention.YearFraction (_dblAccrualStart,
			_dblAccrualEnd, _strAccrualDC, _bApplyAccEOMAdj, _dblMaturity, aap, _strCalendar);

		return dblCurrentDCF * _dblDCF / dblPeriodDCF;
	}
	
	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		double dblStart = org.drip.analytics.date.JulianDate.Today().getJulian();

		CouponPeriod cpnPeriod = new CouponPeriod (dblStart, dblStart + 180, dblStart, dblStart + 180,
			dblStart + 180, dblStart + 180, 2, "30/360", true, "30/360", true, dblStart + 1825, "GBP");

		byte[] abCouponPeriod = cpnPeriod.serialize();

		System.out.println ("Input: " + new java.lang.String (abCouponPeriod));

		CouponPeriod cpnPeriodDeser = new CouponPeriod (abCouponPeriod);

		System.out.println ("Output: " + new java.lang.String (cpnPeriodDeser.serialize()));
	}
}
