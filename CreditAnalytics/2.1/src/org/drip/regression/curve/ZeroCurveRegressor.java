
package org.drip.regression.curve;

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
 * This sample implements the regression analysis set for the Zero Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ZeroCurveRegressor implements org.drip.regression.core.RegressorSet {
	private org.drip.analytics.definition.ZeroCurve _zc = null;
	private java.lang.String _strRegressionScenario = "org.drip.analytics.curve.ZeroCurve";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	/**
	 * ZeroCurveRegressor constructor - Creates the base zero curve and initializes the regression objects
	 */

	public ZeroCurveRegressor()
	{
	}

	/*
	 * Setting up of the zero curve regressor set
	 */

	@Override public boolean setupRegressors()
	{
		/*
		 * Zero Curve Creation unit regressor - implements the pre-regression, the post-regression, and the
		 * 	actual regression functionality of the UnitRegressorExecutor class.
		 */

		try {
			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor
				("CreateZeroCurveFromPeriods", _strRegressionScenario)
			{
				private static final double s_dblZSpread = 0.01;

				private org.drip.analytics.date.JulianDate _dtStart = null;
				private org.drip.analytics.definition.DiscountCurve _dc = null;
				private org.drip.analytics.date.JulianDate _dtPeriodStart = null;

				private java.util.List<org.drip.analytics.period.Period> _lsPeriod = new
					java.util.ArrayList<org.drip.analytics.period.Period>();

				@Override public boolean preRegression()
				{
					if (null == (_dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2010,
						org.drip.analytics.date.JulianDate.MAY, 12)))
						return false;

					if (null == (_dtPeriodStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2008,
						org.drip.analytics.date.JulianDate.SEPTEMBER, 25)))
						return false;

					final int NUM_DC_NODES = 5;
					final int NUM_PERIOD_NODES  = 40;
					double adblDate[] = new double[NUM_DC_NODES];
					double adblRate[] = new double[NUM_DC_NODES];

					for (int i = 0; i < NUM_DC_NODES; ++i) {
						adblDate[i] = _dtStart.addYears (2 * i + 1).getJulian();

						adblRate[i] = 0.05 + 0.001 * (NUM_DC_NODES - i);
					}

					if (null == (_dc = org.drip.analytics.creator.DiscountCurveBuilder.CreateDC (_dtStart,
						"CHF", adblDate, adblRate,
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD)))
						return false;

					for (int i = 0; i < NUM_PERIOD_NODES; ++i) {
						double dblStart = _dtPeriodStart.getJulian();

						org.drip.analytics.date.JulianDate dtEnd = _dtPeriodStart.addMonths (6);

						double dblEnd = dtEnd.getJulian();

						try {
							_lsPeriod.add (new org.drip.analytics.period.Period (dblStart, dblEnd, dblStart,
								dblEnd, dblEnd, 0.5));
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}

						_dtPeriodStart = dtEnd;
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					try {
						if (null == (_zc = org.drip.analytics.creator.ZeroCurveBuilder.CreateZeroCurve (2,
							"30/360", _dc.getCurrency(), true, _lsPeriod, _dtPeriodStart.getJulian(),
								_dtStart.addDays (2).getJulian(), _dc, null, s_dblZSpread)))
							return false;
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}
			});

			/*
			 * Get Zero Discount Factor unit regressor - implements the pre-regression, the post-regression,
			 *	and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("getZeroDF",
				_strRegressionScenario)
			{
				private static final int NUM_DF_NODES = 30;

				private double _adblDate[] = new double[NUM_DF_NODES];
				private double _adblDiscFactor[] = new double[NUM_DF_NODES];

				@Override public boolean preRegression()
				{
					org.drip.analytics.date.JulianDate dtStart =
						org.drip.analytics.date.JulianDate.CreateFromYMD (2008,
							org.drip.analytics.date.JulianDate.SEPTEMBER, 25);

					for (int i = 0; i < NUM_DF_NODES; ++i)
						_adblDate[i] = dtStart.addMonths (6 * i + 6).getJulian();

					return true;
				}

				@Override public boolean execRegression()
				{
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							_adblDiscFactor[i] = _zc.getDF (_adblDate[i]);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							rnvd.set ("ZeroDF[" + new org.drip.analytics.date.JulianDate (_adblDate[i]) +
								"]", "" + _adblDiscFactor[i]);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}
			});

			/*
			 * Get Zero Rate unit regressor - implements the pre-regression, the post-regression, and the
			 * 	actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("getZeroRate",
				_strRegressionScenario)
			{
				private static final int NUM_DF_NODES = 30;

				private double _adblDate[] = new double[NUM_DF_NODES];
				private double _adblRate[] = new double[NUM_DF_NODES];

				@Override public boolean preRegression()
				{
					org.drip.analytics.date.JulianDate dtStart =
						org.drip.analytics.date.JulianDate.CreateFromYMD (2008,
							org.drip.analytics.date.JulianDate.SEPTEMBER, 25);

					for (int i = 0; i < NUM_DF_NODES; ++i)
						_adblDate[i] = dtStart.addMonths (6 * i + 6).getJulian();

					return true;
				}

				@Override public boolean execRegression()
				{
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							_adblRate[i] = _zc.getZeroRate (_adblDate[i]);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							rnvd.set ("ZeroRate[" + new org.drip.analytics.date.JulianDate (_adblDate[i]) +
								"]", "" + _adblRate[i]);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}
			});
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet() {
		return _setRegressors;
	}

	@Override public java.lang.String getSetName() {
		return _strRegressionScenario;
	}
}
