
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
 * This sample implements the regression analysis set for the EDF product related Sensitivity Jacobians.
 *
 * @author Lakshmi Krishnamurthy
 */

public class EDFJacobianRegressorSet implements org.drip.regression.core.RegressorSet {
	private java.lang.String _strRegressionScenario =
		"org.drip.analytics.definition.EDFDiscountCurve.CompPVDFJacobian";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet() {
		return _setRegressors;
	}

	@Override public boolean setupRegressors() {
		try {
			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("EDFJacobian",
				_strRegressionScenario) {
				org.drip.analytics.date.JulianDate dtStart = null;
				org.drip.math.algodiff.WengertJacobian wjPVDF = null;
				org.drip.math.algodiff.WengertJacobian aWJComp[] = null;
				org.drip.analytics.definition.DiscountCurve dcEDF = null;
				org.drip.product.definition.CalibratableComponent aCompCalib[] = null;

				@Override public boolean preRegression() {
					int NUM_DC_INSTR = 8;
					double adblDate[] = new double[NUM_DC_INSTR];
					double adblRate[] = new double[NUM_DC_INSTR];
					double adblCompCalibValue[] = new double[NUM_DC_INSTR];
					aWJComp = new org.drip.math.algodiff.WengertJacobian[NUM_DC_INSTR];
					java.lang.String astrCalibMeasure[] = new java.lang.String[NUM_DC_INSTR];
					aCompCalib = new org.drip.product.definition.CalibratableComponent[NUM_DC_INSTR];

					dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2011, 4, 6);

					adblCompCalibValue[0] = .0027;
					adblCompCalibValue[1] = .0032;
					adblCompCalibValue[2] = .0041;
					adblCompCalibValue[3] = .0054;
					adblCompCalibValue[4] = .0077;
					adblCompCalibValue[5] = .0104;
					adblCompCalibValue[6] = .0134;
					adblCompCalibValue[7] = .0160;
					org.drip.analytics.date.JulianDate dtEDFStart = dtStart;

					org.drip.product.definition.CalibratableComponent[] aEDF =
						org.drip.product.creator.EDFutureBuilder.GenerateEDPack (dtStart, 8, "USD");

					for (int i = 0; i < NUM_DC_INSTR; ++i) {
						adblRate[i] = 0.01;
						aCompCalib[i] = aEDF[i];
						astrCalibMeasure[i] = "Rate";

						adblDate[i] = dtEDFStart.addDays ((i + 1) * 91).getJulian();
					}

					return null != (dcEDF =
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
										(dcEDF, null, null, null, null, null, null), null)))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return null != (wjPVDF = dcEDF.compPVDFJacobian (dtStart));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd) {
					for (int i = 0; i < aCompCalib.length; ++i) {
						if (!rnvd.set ("PVDFMicroJack_" + aCompCalib[i].getComponentName(),
							aWJComp[i].displayString()))
							return false;
					}

					return rnvd.set ("CompPVDFJacobian", "" + wjPVDF.displayString());
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
