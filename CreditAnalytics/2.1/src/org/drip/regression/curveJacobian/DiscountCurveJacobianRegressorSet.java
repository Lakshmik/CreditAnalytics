
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
 * This sample implements the regression analysis set for the Discount Curve related Sensitivity Jacobians.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DiscountCurveJacobianRegressorSet implements org.drip.regression.core.RegressorSet {
	private java.lang.String _strRegressionScenario =
		"org.drip.analytics.definition.IRSDiscountCurve.CompPVDFJacobian";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet() {
		return _setRegressors;
	}

	@Override public boolean setupRegressors() {
		try {
			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("DiscountCurveJacobian",
				_strRegressionScenario) {
				org.drip.analytics.date.JulianDate dtStart = null;
				org.drip.math.calculus.WengertJacobian wjPVDF = null;
				org.drip.math.calculus.WengertJacobian aWJComp[] = null;
				org.drip.analytics.definition.DiscountCurve dcIRS = null;
				org.drip.product.definition.CalibratableComponent aCompCalib[] = null;
				java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
					java.lang.Double>> mmFixings = null;

				@Override public boolean preRegression() {
					int NUM_DC_INSTR = 15;
					double adblDate[] = new double[NUM_DC_INSTR];
					double adblRate[] = new double[NUM_DC_INSTR];
					double adblCompCalibValue[] = new double[NUM_DC_INSTR];
					aWJComp = new org.drip.math.calculus.WengertJacobian[NUM_DC_INSTR];
					java.lang.String astrCalibMeasure[] = new java.lang.String[NUM_DC_INSTR];
					aCompCalib = new org.drip.product.definition.CalibratableComponent[NUM_DC_INSTR];

					if (null == (dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6)))
						return false;

					adblDate[0] = dtStart.addDays ((int)(365.25 * 1 + 2)).getJulian(); // 4Y

					adblDate[1] = dtStart.addDays ((int)(365.25 * 2 + 2)).getJulian(); // 5Y

					adblDate[2] = dtStart.addDays ((int)(365.25 * 3 + 2)).getJulian(); // 6Y

					adblDate[3] = dtStart.addDays ((int)(365.25 * 7 + 2)).getJulian(); // 7Y

					adblDate[4] = dtStart.addDays ((int)(365.25 * 8 + 2)).getJulian(); // 8Y

					adblDate[5] = dtStart.addDays ((int)(365.25 * 9 + 2)).getJulian(); // 9Y

					adblDate[6] = dtStart.addDays ((int)(365.25 * 10 + 2)).getJulian(); // 10Y

					adblDate[7] = dtStart.addDays ((int)(365.25 * 11 + 2)).getJulian(); // 11Y

					adblDate[8] = dtStart.addDays ((int)(365.25 * 12 + 2)).getJulian(); // 12Y

					adblDate[9] = dtStart.addDays ((int)(365.25 * 15 + 2)).getJulian(); // 15Y

					adblDate[10] = dtStart.addDays ((int)(365.25 * 20 + 2)).getJulian(); // 20Y

					adblDate[11] = dtStart.addDays ((int)(365.25 * 25 + 2)).getJulian(); // 25Y

					adblDate[12] = dtStart.addDays ((int)(365.25 * 30 + 2)).getJulian(); // 30Y

					adblDate[13] = dtStart.addDays ((int)(365.25 * 40 + 2)).getJulian(); // 40Y

					adblDate[14] = dtStart.addDays ((int)(365.25 * 50 + 2)).getJulian(); // 50Y

					adblCompCalibValue[0] = .0166;
					adblCompCalibValue[1] = .0206;
					adblCompCalibValue[2] = .0241;
					adblCompCalibValue[3] = .0269;
					adblCompCalibValue[4] = .0292;
					adblCompCalibValue[5] = .0311;
					adblCompCalibValue[6] = .0326;
					adblCompCalibValue[7] = .0340;
					adblCompCalibValue[8] = .0351;
					adblCompCalibValue[9] = .0375;
					adblCompCalibValue[10] = .0393;
					adblCompCalibValue[11] = .0402;
					adblCompCalibValue[12] = .0407;
					adblCompCalibValue[13] = .0409;
					adblCompCalibValue[14] = .0409;

					for (int i = 0; i < NUM_DC_INSTR; ++i) {
						adblRate[i] = 0.01;
						astrCalibMeasure[i] = "Rate";

						try {
							aCompCalib[i] = org.drip.product.creator.IRSBuilder.CreateIRS (dtStart.addDays
								(2), new org.drip.analytics.date.JulianDate (adblDate[i]), 0., "USD",
									"USD-LIBOR-6M", "USD");
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					mIndexFixings.put ("USD-LIBOR-6M", 0.0042);

					(mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
						java.util.Map<java.lang.String, java.lang.Double>>()).put (dtStart.addDays (2),
							mIndexFixings);

					return null != (dcIRS =
						org.drip.param.creator.RatesScenarioCurveBuilder.CreateDiscountCurve (dtStart, "USD",
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
								aCompCalib, adblCompCalibValue, astrCalibMeasure, mmFixings));
				}

				@Override public boolean execRegression() {
					for (int i = 0; i < aCompCalib.length; ++i) {
						try {
							if (null == (aWJComp[i] = aCompCalib[i].calcPVDFMicroJack (new
								org.drip.param.valuation.ValuationParams (dtStart, dtStart, "USD"), null,
									org.drip.param.creator.ComponentMarketParamsBuilder.CreateComponentMarketParams
										(dcIRS, null, null, null, null, null, mmFixings), null)))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return null != (wjPVDF = dcIRS.compPVDFJacobian (dtStart));
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
