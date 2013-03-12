
package org.drip.regression.curve;

/*
 * Other imports
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
import org.drip.analytics.date.*;
import org.drip.analytics.definition.*;
import org.drip.analytics.support.*;
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
 * This sample implements the regression set analysis for the Discount Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DiscountCurveRegressor implements RegressorSet {
	private String _strCurrency = "";
	private DiscountCurve _dc = null;
	private JulianDate _dtStart = null;
	private DiscountCurve _dcFromFlatRate = null;
	private String _strRegressionScenario = "org.drip.analytics.curve.DiscountCurve";

	private List<UnitRegressor> _setRegressors = new ArrayList<UnitRegressor>();

	/**
	 * Do Nothing DiscountCurveRegressor constructor
	 */

	public DiscountCurveRegressor() {
	}

	/*
	 * Discount Curve Regressor set setup
	 */

	@Override public boolean setupRegressors() {
		/*
		 * Testing creation of the Discount Curve from rates instruments - implements the pre-regression, the
		 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
		 */

		try {
			_setRegressors.add (new UnitRegressionExecutor ("CreateFromRatesInstruments",
				_strRegressionScenario) {
				private static final int NUM_DC_INSTR = 30;

				private String _astrCalibMeasure[] = new String[NUM_DC_INSTR];
				private double _adblCompCalibValue[] = new double[NUM_DC_INSTR];
				private CalibratableComponent _aCompCalib[] = new CalibratableComponent[NUM_DC_INSTR];

				Map<JulianDate, Map<String, Double>> _mmFixings = new HashMap<JulianDate, Map<String,
					Double>>();

				@Override public boolean preRegression() {
					_strCurrency = "CHF";
					double adblDate[] = new double[NUM_DC_INSTR];
					double adblRate[] = new double[NUM_DC_INSTR];

					if (null == (_dtStart = JulianDate.CreateFromYMD (2010, JulianDate.MAY, 12)))
						return false;

					adblDate[0] = _dtStart.addDays (3).getJulian(); // ON

					adblDate[1] = _dtStart.addDays (4).getJulian(); // 1D (TN)

					adblDate[2] = _dtStart.addDays (9).getJulian(); // 1W

					adblDate[3] = _dtStart.addDays (16).getJulian(); // 2W

					adblDate[4] = _dtStart.addDays (32).getJulian(); // 1M

					adblDate[5] = _dtStart.addDays (62).getJulian(); // 2M

					adblDate[6] = _dtStart.addDays (92).getJulian(); // 3M

					_adblCompCalibValue[0] = .0013;
					_adblCompCalibValue[1] = .0017;
					_adblCompCalibValue[2] = .0017;
					_adblCompCalibValue[3] = .0018;
					_adblCompCalibValue[4] = .0020;
					_adblCompCalibValue[5] = .0023;
					_adblCompCalibValue[6] = .0026;

					for (int i = 0; i < 7; ++i) {
						adblRate[i] = Double.NaN;
						_astrCalibMeasure[i] = "Rate";

						try {
							_aCompCalib[i] = CashBuilder.CreateCash (_dtStart.addDays (2), new JulianDate
								(adblDate[i]), _strCurrency);
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					_adblCompCalibValue[7] = .0027;
					_adblCompCalibValue[8] = .0032;
					_adblCompCalibValue[9] = .0041;
					_adblCompCalibValue[10] = .0054;
					_adblCompCalibValue[11] = .0077;
					_adblCompCalibValue[12] = .0104;
					_adblCompCalibValue[13] = .0134;
					_adblCompCalibValue[14] = .0160;
					JulianDate dtEDFStart = _dtStart;

					CalibratableComponent[] aEDF = EDFutureBuilder.GenerateEDPack (_dtStart, 8, _strCurrency);

					for (int i = 0; i < 8; ++i) {
						adblRate[i + 7] = Double.NaN;
						_aCompCalib[i + 7] = aEDF[i];
						_astrCalibMeasure[i + 7] = "Rate";

						adblDate[i + 7] = dtEDFStart.addDays ((i + 1) * 91).getJulian();
					}

					adblDate[15] = _dtStart.addDays ((int)(365.25 * 4 + 2)).getJulian(); // 4Y

					adblDate[16] = _dtStart.addDays ((int)(365.25 * 5 + 2)).getJulian(); // 5Y

					adblDate[17] = _dtStart.addDays ((int)(365.25 * 6 + 2)).getJulian(); // 6Y

					adblDate[18] = _dtStart.addDays ((int)(365.25 * 7 + 2)).getJulian(); // 7Y

					adblDate[19] = _dtStart.addDays ((int)(365.25 * 8 + 2)).getJulian(); // 8Y

					adblDate[20] = _dtStart.addDays ((int)(365.25 * 9 + 2)).getJulian(); // 9Y

					adblDate[21] = _dtStart.addDays ((int)(365.25 * 10 + 2)).getJulian(); // 10Y

					adblDate[22] = _dtStart.addDays ((int)(365.25 * 11 + 2)).getJulian(); // 11Y

					adblDate[23] = _dtStart.addDays ((int)(365.25 * 12 + 2)).getJulian(); // 12Y

					adblDate[24] = _dtStart.addDays ((int)(365.25 * 15 + 2)).getJulian(); // 15Y

					adblDate[25] = _dtStart.addDays ((int)(365.25 * 20 + 2)).getJulian(); // 20Y

					adblDate[26] = _dtStart.addDays ((int)(365.25 * 25 + 2)).getJulian(); // 25Y

					adblDate[27] = _dtStart.addDays ((int)(365.25 * 30 + 2)).getJulian(); // 30Y

					adblDate[28] = _dtStart.addDays ((int)(365.25 * 40 + 2)).getJulian(); // 40Y

					adblDate[29] = _dtStart.addDays ((int)(365.25 * 50 + 2)).getJulian(); // 50Y

					_adblCompCalibValue[15] = .0166;
					_adblCompCalibValue[16] = .0206;
					_adblCompCalibValue[17] = .0241;
					_adblCompCalibValue[18] = .0269;
					_adblCompCalibValue[19] = .0292;
					_adblCompCalibValue[20] = .0311;
					_adblCompCalibValue[21] = .0326;
					_adblCompCalibValue[22] = .0340;
					_adblCompCalibValue[23] = .0351;
					_adblCompCalibValue[24] = .0375;
					_adblCompCalibValue[25] = .0393;
					_adblCompCalibValue[26] = .0402;
					_adblCompCalibValue[27] = .0407;
					_adblCompCalibValue[28] = .0409;
					_adblCompCalibValue[29] = .0409;

					for (int i = 0; i < 15; ++i) {
						adblRate[i + 15] = Double.NaN;
						_astrCalibMeasure[i + 15] = "Rate";

						try {
							_aCompCalib[i + 15] = IRSBuilder.CreateIRS (_dtStart.addDays (2), new
								JulianDate (adblDate[i + 15]), 0., _strCurrency, _strCurrency + "-LIBOR-6M",
									_strCurrency);
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					Map<String, Double> mIndexFixings = new HashMap<String, Double>();

					mIndexFixings.put (_strCurrency + "-LIBOR-6M", 0.0042);

					_mmFixings.put (_dtStart.addDays (2), mIndexFixings);

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_dc = RatesScenarioCurveBuilder.CreateDiscountCurve (_dtStart, _strCurrency,
						org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
							_aCompCalib, _adblCompCalibValue, _astrCalibMeasure, _mmFixings)))
						return false;

					return true;
				}
			});

			/*
			 * Testing creation of the Discount Curve from a flat rate - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CreateFromFlatRate", _strRegressionScenario) {
				@Override public boolean execRegression() {
					if (null == (_dcFromFlatRate = DiscountCurveBuilder.CreateFromFlatRate (_dtStart, _strCurrency,
						0.04)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					final int NUM_DC_INSTRUMENTS = 5;
					double adblDate[] = new double[NUM_DC_INSTRUMENTS];
					double adblDiscountFactorFlatRate[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (adblDiscountFactorFlatRate[i] = _dcFromFlatRate.getDF
								(adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;

							rnvd.set ("DiscountFactor[" + new JulianDate (adblDate[i]) + "]", "" +
								adblDiscountFactorFlatRate[i]);
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Discount Curve from discount factors - implements the pre-regression,
			 * 	the post-regression, and the actual regression functionality of the UnitRegressorExecutor
			 * 		class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("BuildFromDF", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private DiscountCurve _dcFromDF = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblDiscountFactorFlatRate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (_adblDiscountFactorFlatRate[i] = _dc.getDF (_adblDate[i]
								= _dtStart.addYears (i + 1).getJulian())))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_dcFromDF = DiscountCurveBuilder.BuildFromDF (_dtStart, _strCurrency, _adblDate,
						_adblDiscountFactorFlatRate,
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							double dblDiscountFactorDFCurve = _dcFromDF.getDF (_adblDate[i]);

							rnvd.set ("DiscountFactorFlatRate[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (_adblDiscountFactorFlatRate[i], 1, 3, 1));

							rnvd.set ("DiscountFactorDFCurve[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (dblDiscountFactorDFCurve, 1, 3, 1));

							if (!NumberUtil.WithinTolerance (dblDiscountFactorDFCurve,
								_adblDiscountFactorFlatRate[i]))
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
			 * Testing creation of the Discount Curve from rates nodes - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CreateDC", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private DiscountCurve _dcFromRates = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblRate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (_adblRate[i] = _dc.calcImpliedRate (_adblDate[i] =
								_dtStart.addYears (i + 1).getJulian())))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_dcFromRates = DiscountCurveBuilder.CreateDC (_dtStart, _strCurrency,
						_adblDate, _adblRate,
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							double dblStartDate = _dtStart.getJulian();

							if (0 != i) dblStartDate = _adblDate[i - 1];

							double dblRate = _dcFromRates.calcImpliedRate (dblStartDate, _adblDate[i]);

							rnvd.set ("DiscountFactorOriginalDC[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (_adblRate[i], 1, 3, 1));

							rnvd.set ("DiscountFactoRateImplied[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (dblRate, 1, 3, 1));

							if (!NumberUtil.WithinTolerance (dblRate, _adblRate[i])) return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing the extraction of the components and quotes - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CompAndQuotes", _strRegressionScenario) {
				private double[] _adblQuote = null;
				private CalibratableComponent[] _aCalibComp = null;

				@Override public boolean execRegression() {
					if (null == (_aCalibComp = _dc.getCalibComponents()) || 0 == _aCalibComp.length)
						return false;

					if (null == (_adblQuote = _dc.getCompQuotes()) || 0 == _adblQuote.length ||
						_aCalibComp.length != _adblQuote.length)
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					for (int i = 0; i < _aCalibComp.length; ++i) {
						String strCalibCompCode = _aCalibComp[i].getPrimaryCode();

						try {
							double dblQuote = _dc.getQuote (strCalibCompCode);

							JulianDate dt = _dc.getNodeDate (i);

							rnvd.set ("CompQuote" + "_" + strCalibCompCode + "{" + dt + "}",
								GenericUtil.FormatDouble (dblQuote, 1, 4, 1));

							rnvd.set ("NodeQuote" + "_" + strCalibCompCode + "{" + dt + "}",
								GenericUtil.FormatDouble (_adblQuote[i], 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Parallel-shifted Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("ParallelShiftedCurve", _strRegressionScenario) {
				private DiscountCurve _dcShifted = null;

				@Override public boolean execRegression() {
					if (null == (_dcShifted = (DiscountCurve) _dc.createParallelShiftedCurve (0.0004))) return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					CalibratableComponent[] aCalibComp = _dc.getCalibComponents();

					for (int i = 0; i < aCalibComp.length; ++i) {
						String strCalibCompCode = aCalibComp[i].getPrimaryCode();

						try {
							double dblQuote = _dc.getQuote (strCalibCompCode);

							double dblQuoteShifted = _dcShifted.getQuote (strCalibCompCode);

							rnvd.set ("BaseCurve" + "_" + strCalibCompCode, GenericUtil.FormatDouble
								(dblQuote, 1, 4, 1));

							rnvd.set ("ParallelShiftedCurve" + "_" + strCalibCompCode,
								GenericUtil.FormatDouble (dblQuoteShifted, 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Rate-shifted Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("RateShiftedCurve", _strRegressionScenario) {
				private DiscountCurve _dcShifted = null;

				@Override public boolean execRegression() {
					if (null == (_dcShifted = (DiscountCurve) _dcFromFlatRate.createParallelShiftedCurve (0.0004)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					final int NUM_DC_INSTRUMENTS = 5;
					double adblDate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateShifted[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (adblRate[i] = _dcFromFlatRate.calcImpliedRate
								(adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;

							if (!NumberUtil.IsValid (adblRateShifted[i] = _dcShifted.calcImpliedRate
								(adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;

							JulianDate dt = new JulianDate (adblDate[i]);

							rnvd.set ("RateBase[" + dt + "]", GenericUtil.FormatDouble (adblRate[i], 1, 4,
								1));

							rnvd.set ("RateShifted[" + dt + "]", GenericUtil.FormatDouble
								(adblRateShifted[i], 1, 4, 1));

							if (!NumberUtil.WithinTolerance (adblRate[i] + 0.0004, adblRateShifted[i]))
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
			 * Testing creation of the basis rate-shifted Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("BasisRateShiftedCurve", _strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private DiscountCurve _dcBasisShifted = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblBasis[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblBasis[i] = (i + 1) * 0.0001;

						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_dcBasisShifted = _dcFromFlatRate.createBasisRateShiftedCurve (_adblDate,
						_adblBasis)))
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateShifted[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (adblRate[i] = _dcFromFlatRate.calcImpliedRate
								(_adblDate[i])))
								return false;

							if (!NumberUtil.IsValid (adblRateShifted[i] = _dcBasisShifted.calcImpliedRate
								(_adblDate[i])))
								return false;

							JulianDate dt = new JulianDate (_adblDate[i]);

							rnvd.set ("RateBasisEmpty[" + dt + "]", GenericUtil.FormatDouble (adblRate[i], 1,
								4, 1));

							rnvd.set ("RateBasisShifted[" + dt + "]", GenericUtil.FormatDouble
								(adblRateShifted[i], 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Tweaked Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CreateTweakedCurve", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private DiscountCurve _dcNTP = null;
				private NodeTweakParams _ntp = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					try {
						_ntp = new NodeTweakParams (0, false, 0.0005);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean execRegression() {
					if (null == (_dcNTP = (DiscountCurve) _dcFromFlatRate.createTweakedCurve (_ntp))) return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateNTP[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (adblRate[i] = _dcFromFlatRate.calcImpliedRate
								(_adblDate[i])))
								return false;

							if (!NumberUtil.IsValid (adblRateNTP[i] = _dcNTP.calcImpliedRate (_adblDate[i])))
								return false;

							JulianDate dt = new JulianDate (_adblDate[i]);

							rnvd.set ("RateUnTweaked[" + dt + "]", GenericUtil.FormatDouble (adblRate[i], 1,
								4, 1));

							rnvd.set ("RateTweaked[" + dt + "]", GenericUtil.FormatDouble (adblRateNTP[i], 1,
								4, 1));

							if (!NumberUtil.WithinTolerance (adblRate[i] + 0.0005, adblRateNTP[i]))
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
			 * Testing calc of Effective DF for the Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("EffectiveDF", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblDiscountFactor[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (_adblDiscountFactor[i] = _dc.getEffectiveDF ((i + 1) +
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
							rnvd.set ("Effective[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (_adblDiscountFactor[i], 1, 4, 1));
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}
			});

			/*
			 * Testing calc of Effective Rate for the Discount Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("CalcImpliedRate", _strRegressionScenario) {
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblImpliedRate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!NumberUtil.IsValid (_adblImpliedRate[i] = _dc.calcImpliedRate ((i + 1) +
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
							rnvd.set ("ImpliedRate[" + new JulianDate (_adblDate[i]) + "]",
								GenericUtil.FormatDouble (_adblImpliedRate[i], 1, 4, 1));
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
