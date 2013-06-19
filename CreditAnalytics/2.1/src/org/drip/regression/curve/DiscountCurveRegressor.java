
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
 * This sample implements the regression set analysis for the Discount Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DiscountCurveRegressor implements org.drip.regression.core.RegressorSet {
	private java.lang.String _strCurrency = "";
	private org.drip.analytics.date.JulianDate _dtStart = null;
	private org.drip.analytics.definition.DiscountCurve _dc = null;
	private org.drip.analytics.definition.DiscountCurve _dcFromFlatRate = null;
	private java.lang.String _strRegressionScenario = "org.drip.analytics.curve.DiscountCurve";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	/**
	 * Do Nothing DiscountCurveRegressor constructor
	 */

	public DiscountCurveRegressor()
	{
	}

	/*
	 * Discount Curve Regressor set setup
	 */

	@Override public boolean setupRegressors()
	{
		/*
		 * Testing creation of the Discount Curve from rates instruments - implements the pre-regression, the
		 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
		 */

		try {
			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor
				("CreateFromRatesInstruments", _strRegressionScenario)
			{
				private static final int NUM_DC_INSTR = 30;

				private double _adblCompCalibValue[] = new double[NUM_DC_INSTR];
				private java.lang.String _astrCalibMeasure[] = new java.lang.String[NUM_DC_INSTR];
				private org.drip.product.definition.CalibratableComponent _aCompCalib[] = new
					org.drip.product.definition.CalibratableComponent[NUM_DC_INSTR];

				java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
					java.lang.Double>> _mmFixings = new java.util.HashMap<org.drip.analytics.date.JulianDate,
						java.util.Map<java.lang.String, java.lang.Double>>();

				@Override public boolean preRegression()
				{
					_strCurrency = "CHF";
					double adblDate[] = new double[NUM_DC_INSTR];
					double adblRate[] = new double[NUM_DC_INSTR];

					if (null == (_dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2010,
						org.drip.analytics.date.JulianDate.MAY, 12)))
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
						_astrCalibMeasure[i] = "Rate";
						adblRate[i] = java.lang.Double.NaN;

						try {
							_aCompCalib[i] = org.drip.product.creator.CashBuilder.CreateCash
								(_dtStart.addDays (2), new org.drip.analytics.date.JulianDate (adblDate[i]),
									_strCurrency);
						} catch (java.lang.Exception e) {
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
					org.drip.analytics.date.JulianDate dtEDFStart = _dtStart;

					org.drip.product.definition.CalibratableComponent[] aEDF =
						org.drip.product.creator.EDFutureBuilder.GenerateEDPack (_dtStart, 8, _strCurrency);

					for (int i = 0; i < 8; ++i) {
						_aCompCalib[i + 7] = aEDF[i];
						_astrCalibMeasure[i + 7] = "Rate";
						adblRate[i + 7] = java.lang.Double.NaN;

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
						_astrCalibMeasure[i + 15] = "Rate";
						adblRate[i + 15] = java.lang.Double.NaN;

						try {
							_aCompCalib[i + 15] = org.drip.product.creator.IRSBuilder.CreateIRS
								(_dtStart.addDays (2), new org.drip.analytics.date.JulianDate (adblDate[i +
								    15]), 0., _strCurrency, _strCurrency + "-LIBOR-6M", _strCurrency);
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					java.util.Map<java.lang.String, java.lang.Double> mIndexFixings = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					mIndexFixings.put (_strCurrency + "-LIBOR-6M", 0.0042);

					_mmFixings.put (_dtStart.addDays (2), mIndexFixings);

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_dc =
						org.drip.param.creator.RatesScenarioCurveBuilder.CreateDiscountCurve (_dtStart,
							_strCurrency,
								org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_POLYNOMIAL_SPLINE_DF,
						_aCompCalib, _adblCompCalibValue, _astrCalibMeasure, _mmFixings));
				}
			});

			/*
			 * Testing creation of the Discount Curve from a flat rate - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CreateFromFlatRate",
				_strRegressionScenario)
			{
				@Override public boolean execRegression()
				{
					return null != (_dcFromFlatRate =
						org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate (_dtStart,
							_strCurrency, 0.04));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					final int NUM_DC_INSTRUMENTS = 5;
					double adblDate[] = new double[NUM_DC_INSTRUMENTS];
					double adblDiscountFactorFlatRate[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblDiscountFactorFlatRate[i] =
								_dcFromFlatRate.getDF (adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;

							rnvd.set ("DiscountFactor[" + new org.drip.analytics.date.JulianDate
								(adblDate[i]) + "]", "" + adblDiscountFactorFlatRate[i]);
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("BuildFromDF",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private org.drip.analytics.definition.DiscountCurve _dcFromDF = null;
				private double _adblDiscountFactorFlatRate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (_adblDiscountFactorFlatRate[i] =
								_dc.getDF (_adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_dcFromDF = org.drip.analytics.creator.DiscountCurveBuilder.BuildFromDF
						(_dtStart, _strCurrency, _adblDate, _adblDiscountFactorFlatRate,
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							double dblDiscountFactorDFCurve = _dcFromDF.getDF (_adblDate[i]);

							rnvd.set ("DiscountFactorFlatRate[" + new org.drip.analytics.date.JulianDate
								(_adblDate[i]) + "]", org.drip.math.common.FormatUtil.FormatDouble
									(_adblDiscountFactorFlatRate[i], 1, 3, 1));

							rnvd.set ("DiscountFactorDFCurve[" + new org.drip.analytics.date.JulianDate
								(_adblDate[i]) + "]", org.drip.math.common.FormatUtil.FormatDouble
									(dblDiscountFactorDFCurve, 1, 3, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (dblDiscountFactorDFCurve,
								_adblDiscountFactorFlatRate[i]))
								return false;
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CreateDC",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblRate[] = new double[NUM_DC_INSTRUMENTS];
				private org.drip.analytics.definition.DiscountCurve _dcFromRates = null;

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (_adblRate[i] = _dc.calcImpliedRate
								(_adblDate[i] = _dtStart.addYears (i + 1).getJulian())))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_dcFromRates = org.drip.analytics.creator.DiscountCurveBuilder.CreateDC
						(_dtStart, _strCurrency, _adblDate, _adblRate,
							org.drip.analytics.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							double dblStartDate = _dtStart.getJulian();

							if (0 != i) dblStartDate = _adblDate[i - 1];

							double dblRate = _dcFromRates.calcImpliedRate (dblStartDate, _adblDate[i]);

							rnvd.set ("DiscountFactorOriginalDC[" + new org.drip.analytics.date.JulianDate
								(_adblDate[i]) + "]", org.drip.math.common.FormatUtil.FormatDouble
									(_adblRate[i], 1, 3, 1));

							rnvd.set ("DiscountFactoRateImplied[" + new org.drip.analytics.date.JulianDate
								(_adblDate[i]) + "]", org.drip.math.common.FormatUtil.FormatDouble (dblRate,
									1, 3, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (dblRate, _adblRate[i]))
								return false;
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CompAndQuotes",
				_strRegressionScenario)
			{
				private double[] _adblQuote = null;
				private org.drip.product.definition.CalibratableComponent[] _aCalibComp = null;

				@Override public boolean execRegression()
				{
					return null != (_aCalibComp = _dc.getCalibComponents()) && 0 == _aCalibComp.length &&
						null == (_adblQuote = _dc.getCompQuotes()) && 0 == _adblQuote.length &&
							_aCalibComp.length != _adblQuote.length;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < _aCalibComp.length; ++i) {
						java.lang.String strCalibCompCode = _aCalibComp[i].getPrimaryCode();

						try {
							double dblQuote = _dc.getQuote (strCalibCompCode);

							org.drip.analytics.date.JulianDate dt = _dc.getNodeDate (i);

							rnvd.set ("CompQuote" + "_" + strCalibCompCode + "{" + dt + "}",
								org.drip.math.common.FormatUtil.FormatDouble (dblQuote, 1, 4, 1));

							rnvd.set ("NodeQuote" + "_" + strCalibCompCode + "{" + dt + "}",
								org.drip.math.common.FormatUtil.FormatDouble (_adblQuote[i], 1, 4, 1));
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("ParallelShiftedCurve",
				_strRegressionScenario)
			{
				private org.drip.analytics.definition.DiscountCurve _dcShifted = null;

				@Override public boolean execRegression()
				{
					return null != (_dcShifted = (org.drip.analytics.definition.DiscountCurve)
						_dc.createParallelShiftedCurve (0.0004));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					org.drip.product.definition.CalibratableComponent[] aCalibComp =
						_dc.getCalibComponents();

					for (int i = 0; i < aCalibComp.length; ++i) {
						java.lang.String strCalibCompCode = aCalibComp[i].getPrimaryCode();

						try {
							double dblQuote = _dc.getQuote (strCalibCompCode);

							double dblQuoteShifted = _dcShifted.getQuote (strCalibCompCode);

							rnvd.set ("BaseCurve" + "_" + strCalibCompCode,
								org.drip.math.common.FormatUtil.FormatDouble (dblQuote, 1, 4, 1));

							rnvd.set ("ParallelShiftedCurve" + "_" + strCalibCompCode,
								org.drip.math.common.FormatUtil.FormatDouble (dblQuoteShifted, 1, 4, 1));
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("RateShiftedCurve",
				_strRegressionScenario)
			{
				private org.drip.analytics.definition.DiscountCurve _dcShifted = null;

				@Override public boolean execRegression()
				{
					return null != (_dcShifted = (org.drip.analytics.definition.DiscountCurve)
						_dcFromFlatRate.createParallelShiftedCurve (0.0004));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					final int NUM_DC_INSTRUMENTS = 5;
					double adblDate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateShifted[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblRate[i] =
								_dcFromFlatRate.calcImpliedRate (adblDate[i] = _dtStart.addYears (i +
									1).getJulian())))
								return false;

							if (!org.drip.math.common.NumberUtil.IsValid (adblRateShifted[i] =
								_dcShifted.calcImpliedRate (adblDate[i] = _dtStart.addYears (i +
									1).getJulian())))
								return false;

							org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate
								(adblDate[i]);

							rnvd.set ("RateBase[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
								(adblRate[i], 1, 4, 1));

							rnvd.set ("RateShifted[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
								(adblRateShifted[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (adblRate[i] + 0.0004,
								adblRateShifted[i]))
								return false;
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("BasisRateShiftedCurve",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private org.drip.analytics.definition.DiscountCurve _dcBasisShifted = null;
				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblBasis[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblBasis[i] = (i + 1) * 0.0001;

						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_dcBasisShifted = _dcFromFlatRate.createBasisRateShiftedCurve (_adblDate,
						_adblBasis));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateShifted[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblRate[i] =
								_dcFromFlatRate.calcImpliedRate (_adblDate[i])))
								return false;

							if (!org.drip.math.common.NumberUtil.IsValid (adblRateShifted[i] =
								_dcBasisShifted.calcImpliedRate (_adblDate[i])))
								return false;

							org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate
								(_adblDate[i]);

							rnvd.set ("RateBasisEmpty[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblRate[i], 1, 4, 1));

							rnvd.set ("RateBasisShifted[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblRateShifted[i], 1, 4, 1));
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CreateTweakedCurve",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private org.drip.param.definition.NodeTweakParams _ntp = null;
				private org.drip.analytics.definition.DiscountCurve _dcNTP = null;

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					try {
						_ntp = new org.drip.param.definition.NodeTweakParams (0, false, 0.0005);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_dcNTP = (org.drip.analytics.definition.DiscountCurve)
						_dcFromFlatRate.createTweakedCurve (_ntp));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double adblRate[] = new double[NUM_DC_INSTRUMENTS];
					double adblRateNTP[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblRate[i] =
								_dcFromFlatRate.calcImpliedRate (_adblDate[i])))
								return false;

							if (!org.drip.math.common.NumberUtil.IsValid (adblRateNTP[i] =
								_dcNTP.calcImpliedRate (_adblDate[i])))
								return false;

							org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate
								(_adblDate[i]);

							rnvd.set ("RateUnTweaked[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblRate[i], 1, 4, 1));

							rnvd.set ("RateTweaked[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
								(adblRateNTP[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (adblRate[i] + 0.0005,
								adblRateNTP[i]))
								return false;
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("EffectiveDF",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblDiscountFactor[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (_adblDiscountFactor[i] =
								_dc.getEffectiveDF ((i + 1) + "Y", (i + 2) + "Y")))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							rnvd.set ("Effective[" + new org.drip.analytics.date.JulianDate (_adblDate[i]) +
								"]", org.drip.math.common.FormatUtil.FormatDouble (_adblDiscountFactor[i], 1,
									4, 1));
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CalcImpliedRate",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblImpliedRate[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i)
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (_adblImpliedRate[i] =
								_dc.calcImpliedRate ((i + 1) + "Y", (i + 2) + "Y")))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
					}

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							rnvd.set ("ImpliedRate[" + new org.drip.analytics.date.JulianDate (_adblDate[i])
								+ "]", org.drip.math.common.FormatUtil.FormatDouble (_adblImpliedRate[i], 1,
									4, 1));
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}
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

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet()
	{
		return _setRegressors;
	}

	@Override public java.lang.String getSetName()
	{
		return _strRegressionScenario;
	}
}
