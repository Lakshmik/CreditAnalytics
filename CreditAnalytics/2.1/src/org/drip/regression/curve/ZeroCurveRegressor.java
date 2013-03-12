
package org.drip.regression.curve;

/*
 * Generic imports
 */

import java.util.*;

/*
 * Regression Suite imports
 */

import org.drip.regression.core.*;

/*
 * Credit Analytics imports
 */

import org.drip.analytics.creator.*;
import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.*;
import org.drip.analytics.period.Period;

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

public class ZeroCurveRegressor implements RegressorSet {
	private ZeroCurve _zc = null;
	private String _strRegressionScenario = "org.drip.analytics.curve.ZeroCurve";

	private List<UnitRegressor> _setRegressors = new ArrayList<UnitRegressor>();

	/**
	 * ZeroCurveRegressor constructor - Creates the base zero curve and initializes the regression objects
	 */

	public ZeroCurveRegressor() {
	}

	/*
	 * Setting up of the zero curve regressor set
	 */

	@Override public boolean setupRegressors() {
		/*
		 * Zero Curve Creation unit regressor - implements the pre-regression, the post-regression, and the
		 * 	actual regression functionality of the UnitRegressorExecutor class.
		 */

		try {
			_setRegressors.add (new UnitRegressionExecutor ("CreateZeroCurveFromPeriods",
				_strRegressionScenario) {
				private static final double s_dblZSpread = 0.01;

				private DiscountCurve _dc = null;
				private JulianDate _dtStart = null;
				private JulianDate _dtPeriodStart = null;

				private List<Period> _lsPeriod = new ArrayList<Period>();

				@Override public boolean preRegression() {
					if (null == (_dtStart = JulianDate.CreateFromYMD (2010, JulianDate.MAY, 12)))
						return false;

					if (null == (_dtPeriodStart = JulianDate.CreateFromYMD (2008, JulianDate.SEPTEMBER, 25)))
						return false;

					final int NUM_DC_NODES = 5;
					final int NUM_PERIOD_NODES  = 40;
					double adblDate[] = new double[NUM_DC_NODES];
					double adblRate[] = new double[NUM_DC_NODES];

					for (int i = 0; i < NUM_DC_NODES; ++i) {
						adblDate[i] = _dtStart.addYears (2 * i + 1).getJulian();

						adblRate[i] = 0.05 + 0.001 * (NUM_DC_NODES - i);
					}

					if (null == (_dc = DiscountCurveBuilder.CreateDC (_dtStart, "CHF", adblDate, adblRate,
						org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD)))
						return false;

					for (int i = 0; i < NUM_PERIOD_NODES; ++i) {
						double dblStart = _dtPeriodStart.getJulian();

						JulianDate dtEnd = _dtPeriodStart.addMonths (6);

						double dblEnd = dtEnd.getJulian();

						try {
							_lsPeriod.add (new Period (dblStart, dblEnd, dblStart, dblEnd, dblEnd, 0.5));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}

						_dtPeriodStart = dtEnd;
					}

					return true;
				}

				@Override public boolean execRegression() {
					try {
						if (null == (_zc = ZeroCurveBuilder.CreateZeroCurve (_lsPeriod, _dtPeriodStart.getJulian(),
							_dtStart.addDays (2).getJulian(), _dc, null, s_dblZSpread)))
							return false;
					} catch (Exception e) {
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

			_setRegressors.add (new UnitRegressionExecutor ("getZeroDF", _strRegressionScenario) {
				private static final int NUM_DF_NODES = 30;

				private double _adblDate[] = new double[NUM_DF_NODES];
				private double _adblDiscFactor[] = new double[NUM_DF_NODES];

				@Override public boolean preRegression() {
					JulianDate dtStart = JulianDate.CreateFromYMD (2008, JulianDate.SEPTEMBER, 25);

					for (int i = 0; i < NUM_DF_NODES; ++i)
						_adblDate[i] = dtStart.addMonths (6 * i + 6).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							_adblDiscFactor[i] = _zc.getDF (_adblDate[i]);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							rnvd.set ("ZeroDF[" + new JulianDate (_adblDate[i]) + "]", "" +
								_adblDiscFactor[i]);
					} catch (Exception e) {
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

			_setRegressors.add (new UnitRegressionExecutor ("getZeroRate", _strRegressionScenario) {
				private static final int NUM_DF_NODES = 30;

				private double _adblDate[] = new double[NUM_DF_NODES];
				private double _adblRate[] = new double[NUM_DF_NODES];

				@Override public boolean preRegression() {
					JulianDate dtStart = JulianDate.CreateFromYMD (2008, JulianDate.SEPTEMBER, 25);

					for (int i = 0; i < NUM_DF_NODES; ++i)
						_adblDate[i] = dtStart.addMonths (6 * i + 6).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							_adblRate[i] = _zc.getZeroRate (_adblDate[i]);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					try {
						for (int i = 0; i < NUM_DF_NODES; ++i)
							rnvd.set ("ZeroRate[" + new JulianDate (_adblDate[i]) + "]", "" + _adblRate[i]);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@Override public List<UnitRegressor> getRegressorSet() {
		return _setRegressors;
	}

	@Override public String getSetName() {
		return _strRegressionScenario;
	}
}
