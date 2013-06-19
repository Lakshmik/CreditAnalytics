
package org.drip.regression.curveJacobian;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * This sample implements the regression analysis set for the Cash product related Sensitivity Jacobians.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CashJacobianRegressorSet implements org.drip.regression.core.RegressorSet {
	private java.lang.String _strRegressionScenario =
		"org.drip.analytics.definition.CashDiscountCurve.CompPVDFJacobian";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet() {
		return _setRegressors;
	}

	@Override public boolean setupRegressors() {
		try {
			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CashJacobian",
				_strRegressionScenario) {
				org.drip.analytics.date.JulianDate dtStart = null;
				org.drip.math.calculus.WengertJacobian wjPVDF = null;
				org.drip.math.calculus.WengertJacobian aWJComp[] = null;
				org.drip.analytics.definition.DiscountCurve dcCash = null;
				org.drip.product.definition.CalibratableComponent aCompCalib[] = null;

				@Override public boolean preRegression() {
					int NUM_CASH_INSTR = 7;
					double adblDate[] = new double[NUM_CASH_INSTR];
					double adblRate[] = new double[NUM_CASH_INSTR];
					double adblCompCalibValue[] = new double[NUM_CASH_INSTR];
					aWJComp = new org.drip.math.calculus.WengertJacobian[NUM_CASH_INSTR];
					java.lang.String astrCalibMeasure[] = new java.lang.String[NUM_CASH_INSTR];
					aCompCalib = new org.drip.product.definition.CalibratableComponent[NUM_CASH_INSTR];

					if (null == (dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6)))
						return false;

					adblDate[0] = dtStart.addDays (3).getJulian(); // ON

					adblDate[1] = dtStart.addDays (4).getJulian(); // 1D (TN)

					adblDate[2] = dtStart.addDays (9).getJulian(); // 1W

					adblDate[3] = dtStart.addDays (16).getJulian(); // 2W

					adblDate[4] = dtStart.addDays (32).getJulian(); // 1M

					adblDate[5] = dtStart.addDays (62).getJulian(); // 2M

					adblDate[6] = dtStart.addDays (92).getJulian(); // 3M

					adblCompCalibValue[0] = .0013;
					adblCompCalibValue[1] = .0017;
					adblCompCalibValue[2] = .0017;
					adblCompCalibValue[3] = .0018;
					adblCompCalibValue[4] = .0020;
					adblCompCalibValue[5] = .0023;
					adblCompCalibValue[6] = .0026;

					for (int i = 0; i < NUM_CASH_INSTR; ++i) {
						adblRate[i] = 0.01;
						astrCalibMeasure[i] = "Rate";

						try {
							aCompCalib[i] = org.drip.product.creator.CashBuilder.CreateCash (dtStart.addDays
								(2), new org.drip.analytics.date.JulianDate (adblDate[i]), "USD");
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return null != (dcCash =
						org.drip.param.creator.RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
								aCompCalib, adblCompCalibValue, astrCalibMeasure, null));
				}

				@Override public boolean execRegression() {
					for (int i = 0; i < aCompCalib.length; ++i) {
						try {
							if (null == (aWJComp[i] = aCompCalib[i].calcPVDFMicroJack (new
								org.drip.param.valuation.ValuationParams (dtStart, dtStart, "USD"), null,
									org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
										(dcCash, null, null, null, null, null, null), null)))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return null != (wjPVDF = dcCash.compPVDFJacobian (dtStart));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd) {
					for (int i = 0; i < aCompCalib.length; ++i) {
						if (!rnvd.set ("PVDFMicroJack_" + aCompCalib[i].getComponentName(),
							aWJComp[i].displayString()))
							return false;
					}

					return rnvd.set ("CompPVDFJacobian", wjPVDF.displayString());
				}
			});
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override public java.lang.String getSetName() {
		return _strRegressionScenario;
	}
}
