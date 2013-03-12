
package org.drip.regression.curve;

/*
 * Other imports
 */

import java.util.*;

/*
 * Regression Suite Imports
 */

import org.drip.regression.core.*;

/*
 * Credit Analytics imports 
 */

import org.drip.analytics.creator.*;
import org.drip.analytics.date.JulianDate;
import org.drip.analytics.definition.*;
import org.drip.analytics.support.GenericUtil;
import org.drip.param.creator.*;
import org.drip.param.definition.*;
import org.drip.product.creator.*;
import org.drip.product.definition.*;
import org.drip.math.common.NumberUtil;

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
 * This sample implements the regression set for the Credit Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditCurveRegressor implements RegressorSet {
	private CreditCurve _cc = null;
	private String _strCurrency = "";
	private DiscountCurve _dc = null;
	private JulianDate _dtStart = null;
	private String _strRegressionScenario = "org.drip.analytics.curve.CreditCurve";

	private List<UnitRegressor> _setRegressors = new ArrayList<UnitRegressor>();

	/**
	 * Do Nothing CreditCurveRegressor constructor.
	 */

	public CreditCurveRegressor() {
	}

	/*
	 * Set up the unit functional regressors for the credit curve regression set
	 */
	
	@Override public boolean setupRegressors() {
		try {
			/*
			 * Testing creation of the Credit Curve from SNAC instruments - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CreateSNAC", _strRegressionScenario) {
				private double[] _adblQuotes = new double[5];
				private String[] _astrCalibMeasure = new String[5];
				private CreditDefaultSwap[] _aCDS = new CreditDefaultSwap[5];

				@Override public boolean preRegression() {
					_strCurrency = "CHF";

					if (null == (_dtStart = JulianDate.CreateFromYMD (2010, JulianDate.MAY, 12)))
						return false;

					if (null == (_dc = DiscountCurveBuilder.CreateFromFlatRate (_dtStart, _strCurrency, 0.04)))
						return false;

					for (int i = 0; i < 5; ++i) {
						_adblQuotes[i] = 50. * (i + 1);
						_astrCalibMeasure[i] = "FairPremium";

						if (null == (_aCDS[i] = CDSBuilder.CreateSNAC (_dtStart, (i + 1) + "Y", 0.01, "CORP")))
							return false;
			 		}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_cc = CreditScenarioCurveBuilder.CreateCreditCurve ("CORP", _dtStart,
						_aCDS, _dc, _adblQuotes, _astrCalibMeasure, 0.4, false)))
						return false;

					return true;
				}
			});

			/*
			 * Testing creation of the Credit Curve from flat hazard - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FromFlatHazard", _strRegressionScenario) {
				private CreditCurve _ccFromFlatHazard = null;

				@Override public boolean execRegression() {
					if (null == (_ccFromFlatHazard = CreditCurveBuilder.FromFlatHazard (_dtStart.getJulian(),
						"CORP", 0.02, 0.4)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					final int NUM_DC_INSTRUMENTS = 5;
					double adblHazard[] = new double[NUM_DC_INSTRUMENTS];
					JulianDate adt[] = new JulianDate[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (Double.isNaN (adblHazard[i] = _ccFromFlatHazard.calcHazard (_dtStart, (adt[i]
								= _dtStart.addYears (i + 1)))))
								return false;

							rnvd.set ("HazardRateFromHazardCurve[" + adt[i] + "]", GenericUtil.FormatDouble
								(adblHazard[i], 1, 4, 1));

							if (!NumberUtil.WithinTolerance (adblHazard[i], 0.02)) return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Credit Curve from flat survival - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FromSurvival", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private CreditCurve _ccFromSurvival = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblSurvival[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

						_adblSurvival[i] = 1. - (i + 1) * 0.1;
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_ccFromSurvival = CreditCurveBuilder.FromSurvival (_dtStart.getJulian(), "CORP",
						_adblDate, _adblSurvival, 0.4)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double adblSurvivalCalc[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (Double.isNaN (adblSurvivalCalc[i] = _ccFromSurvival.getSurvival
								(_adblDate[i])))
								return false;

							JulianDate dt = new JulianDate (_adblDate[i]);

							rnvd.set ("SurvivalFromOriginal[" + dt + "]", GenericUtil.FormatDouble
								(_adblSurvival[i], 1, 4, 1));

							rnvd.set ("SurvivalFromSurvival[" + dt + "]", GenericUtil.FormatDouble
								(adblSurvivalCalc[i], 1, 4, 1));

							if (!NumberUtil.WithinTolerance (adblSurvivalCalc[i], _adblSurvival[i]))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Credit Curve from hazard nodes - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FromHazard", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private CreditCurve _ccFromHazard = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblHazard[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

						_adblHazard[i] = 0.01 * (1. - (i + 1) * 0.1);
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_ccFromHazard = CreditCurveBuilder.CreateCreditCurve (_dtStart, "CORP", _adblDate,
						_adblHazard, 0.4)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					JulianDate dt1 = _dtStart;
					double adblHazardCalc[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (Double.isNaN (adblHazardCalc[i] = _ccFromHazard.calcHazard (dt1, dt1.addYears
								(1))))
								return false;

							JulianDate dt2 = dt1.addYears (1);

							rnvd.set ("HazardFromOriginal[" + dt1 + "-" + dt2 + "]", GenericUtil.FormatDouble
								(_adblHazard[i], 1, 4, 1));

							rnvd.set ("HazardFromHazard[" + dt1 + "-" + dt2 + "]", GenericUtil.FormatDouble
								(adblHazardCalc[i], 1, 4, 1));

							if (!NumberUtil.WithinTolerance (adblHazardCalc[i], _adblHazard[i]))
								return false;

							dt1 = dt1.addYears (1);
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing extraction of the credit curve components and quotes - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CompAndQuotes", _strRegressionScenario) {
				private double[] _adblQuotes = null;
				private CalibratableComponent[] _aCalibComp = null;

				@Override public boolean execRegression() {
					if (null == (_adblQuotes = _cc.getCompQuotes()) || 0 == _adblQuotes.length) return false;

					if (null == (_aCalibComp = _cc.getCalibComponents()) || 0 == _aCalibComp.length ||
						_aCalibComp.length != _adblQuotes.length)
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < _adblQuotes.length; ++i) {
						JulianDate dt = _cc.getNodeDate (i);

						String strCode = _aCalibComp[i].getPrimaryCode();

						if (null == dt || null == strCode || strCode.isEmpty()) return false;

						try {
							double dblCompQuote = _cc.getQuote (strCode);

							rnvd.set ("CompQuote" + "_" + strCode + "[" + dt + "]", GenericUtil.FormatDouble
								(dblCompQuote, 1, 4, 1));

							rnvd.set ("NodeQuote" + "_" + strCode + "[" + dt + "]", GenericUtil.FormatDouble
								(_adblQuotes[i], 1, 4, 1));

							if (!NumberUtil.WithinTolerance (dblCompQuote, _adblQuotes[i])) return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of a parallel hazard shifted Credit Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("ParallelHazardShiftedCurve",
				_strRegressionScenario) {
				private CreditCurve _ccParallelShifted = null;

				@Override public boolean execRegression() {
					if (null == (_ccParallelShifted = _cc.createParallelHazardShiftedCurve (0.0005)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double[] adblQuotes = _cc.getCompQuotes();

					JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						JulianDate dt = _cc.getNodeDate (i);

						double dblBaseHazard = Double.NaN;
						double dblShiftedHazard = Double.NaN;

						try {
							if (Double.isNaN (dblShiftedHazard = _ccParallelShifted.calcHazard (dt1, dt)) ||
								Double.isNaN (dblBaseHazard = _cc.calcHazard (dt1, dt)))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("BaseCurveHazard[" + dt1 + "-" + dt + "]", GenericUtil.FormatDouble
							(dblBaseHazard, 1, 4, 1));

						rnvd.set ("ParallelShiftedCurveHazard[" + dt1 + "-" + dt + "]",
							GenericUtil.FormatDouble (dblShiftedHazard, 1, 4, 1));

						dt = dt1;

						if (!NumberUtil.WithinTolerance (dblBaseHazard + 0.0005, dblShiftedHazard))
							return false;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the parallel quote shifted credit curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("ParallelQuoteShiftedCurve",
				_strRegressionScenario) {
				private CreditCurve _ccParallelShifted = null;

				@Override public boolean execRegression() {
					if (null == (_ccParallelShifted = (CreditCurve) _cc.createParallelShiftedCurve (5.)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double[] adblQuotes = _cc.getCompQuotes();

					double[] adblQuotesShifted = _ccParallelShifted.getCompQuotes();

					JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						JulianDate dt = _cc.getNodeDate (i);

						rnvd.set ("BaseCurveQuote[" + dt + "]", GenericUtil.FormatDouble (adblQuotes[i], 1, 5, 1));

						rnvd.set ("ParallelShiftedCurveQuote[" + dt + "]", GenericUtil.FormatDouble
							(adblQuotesShifted[i], 1, 5, 1));

						dt = dt1;

						if (!NumberUtil.WithinTolerance (adblQuotes[i] + 5., adblQuotesShifted[i]))
							return false;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the node tweaked Credit Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("NodeTweakedCurve", _strRegressionScenario) {
				private static final int TWEAKED_NODE = 0;

				private CreditCurve _ccTweakedCurve = null;
				private CreditNodeTweakParams _cntp = null;

				@Override public boolean preRegression() {
					try {
						_cntp = new CreditNodeTweakParams
							(CreditNodeTweakParams.CREDIT_TWEAK_NODE_PARAM_QUOTE,
								CreditNodeTweakParams.CREDIT_TWEAK_NODE_MEASURE_QUOTE, TWEAKED_NODE, true,
									0.1, false);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_ccTweakedCurve = (CreditCurve) _cc.createTweakedCurve (_cntp)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double[] adblQuotes = _cc.getCompQuotes();

					JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						JulianDate dt = _cc.getNodeDate (i);

						double dblBaseHazard = Double.NaN;
						double dblShiftedHazard = Double.NaN;

						try {
							if (Double.isNaN (dblShiftedHazard = _ccTweakedCurve.calcHazard (dt1, dt)) ||
								Double.isNaN (dblBaseHazard = _cc.calcHazard (dt1, dt)))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("UntweakedHazard[" + dt + "]", GenericUtil.FormatDouble (dblBaseHazard, 1, 5, 1));

						rnvd.set ("TweakedHazard[" + dt + "]", GenericUtil.FormatDouble (dblShiftedHazard, 1, 5, 1));

						dt = dt1;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Credit Curve from flat/quoted spread - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FlatCurve", _strRegressionScenario) {
				private CreditCurve _ccFlatCurve = null;

				@Override public boolean execRegression() {
					if (null == (_ccFlatCurve = _cc.createFlatCurve (90., false, 0.35))) return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					final int NUM_DC_INSTRUMENTS = 5;

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						JulianDate dt = _dtStart.addYears (i + 1);

						double dblHazard = java.lang.Double.NaN;

						try {
							if (Double.isNaN (dblHazard = _ccFlatCurve.calcHazard (dt))) return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("FlatHazard[" + dt + "]", GenericUtil.FormatDouble (dblHazard, 1, 5, 1));
					}

					return true;
				}
			});

			/*
			 * Testing setting/removing specific default dates - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("SpecificDefault", _strRegressionScenario) {
				private double _dblSpecificDefault = java.lang.Double.NaN;

				@Override public boolean preRegression() {
					if (Double.isNaN (_dblSpecificDefault = _dtStart.addYears (2).getJulian())) return false;

					return true;
				}

				@Override public boolean execRegression() {
					return _cc.setSpecificDefault (_dblSpecificDefault);
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double dblSurvivalProb = Double.NaN;

					JulianDate dtSurvival = _dtStart.addYears (3);

					double dblSurvivalDate = dtSurvival.getJulian();

					try {
						if (Double.isNaN (dblSurvivalProb = _cc.getSurvival (dblSurvivalDate))) return false;
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					rnvd.set ("SpecificDefaultSetSurvival[" + dtSurvival + "]", "" + dblSurvivalProb);

					if (!_cc.unsetSpecificDefault()) return false;

					try {
						if (Double.isNaN (dblSurvivalProb = _cc.getSurvival (dblSurvivalDate))) return false;
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					rnvd.set ("SpecificDefaultUnsetSurvival[" + dtSurvival + "]", "" + dblSurvivalProb);

					return true;
				}
			});

			/*
			 * Testing calculation of effective survival between2 dates - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("EffectiveSurvival", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblSurvival[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (Double.isNaN (_adblSurvival[i] = _cc.getEffectiveSurvival ((i + 1) + "Y", (i + 2) + "Y")))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							rnvd.set ("EffectiveSurvival[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (_adblSurvival[i], 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing calculation of effective recovery between2 dates - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("EffectiveRecovery", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblEffectiveRecovery[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean execRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (Double.isNaN (_adblEffectiveRecovery[i] = _cc.getEffectiveRecovery ((i + 1) +
								"Y", (i + 2) + "Y")))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							rnvd.set ("EffectiveRecovery[" + (i + 1) + "Y-" + (i + 2) + "Y]",
								GenericUtil.FormatDouble (_adblEffectiveRecovery[i], 1, 4, 1));

							rnvd.set ("CurveRecovery[" + (i + 1) + "Y-" + (i + 2) + "Y]",
								GenericUtil.FormatDouble (_cc.getRecovery ((i + 1) + "Y"), 1, 4, 1) + "-" +
									GenericUtil.FormatDouble (_cc.getRecovery ((i + 2) + "Y"), 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
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
