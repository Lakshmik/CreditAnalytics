
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
 * This sample implements the regression set for the Credit Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditCurveRegressor implements org.drip.regression.core.RegressorSet {
	private java.lang.String _strCurrency = "";
	private org.drip.analytics.date.JulianDate _dtStart = null;
	private org.drip.analytics.definition.CreditCurve _cc = null;
	private org.drip.analytics.definition.DiscountCurve _dc = null;
	private java.lang.String _strRegressionScenario = "org.drip.analytics.curve.CreditCurve";

	private java.util.List<org.drip.regression.core.UnitRegressor> _setRegressors = new
		java.util.ArrayList<org.drip.regression.core.UnitRegressor>();

	/**
	 * Do Nothing CreditCurveRegressor constructor.
	 */

	public CreditCurveRegressor()
	{
	}

	/*
	 * Set up the unit functional regressors for the credit curve regression set
	 */
	
	@Override public boolean setupRegressors()
	{
		try {
			/*
			 * Testing creation of the Credit Curve from SNAC instruments - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CreateSNAC",
				_strRegressionScenario)
			{
				private double[] _adblQuotes = new double[5];
				private java.lang.String[] _astrCalibMeasure = new java.lang.String[5];
				private org.drip.product.definition.CreditDefaultSwap[] _aCDS = new
					org.drip.product.definition.CreditDefaultSwap[5];

				@Override public boolean preRegression()
				{
					_strCurrency = "CHF";

					if (null == (_dtStart = org.drip.analytics.date.JulianDate.CreateFromYMD (2010,
						org.drip.analytics.date.JulianDate.MAY, 12)))
						return false;

					if (null == (_dc = org.drip.analytics.creator.DiscountCurveBuilder.CreateFromFlatRate
						(_dtStart, _strCurrency, 0.04)))
						return false;

					for (int i = 0; i < 5; ++i) {
						_adblQuotes[i] = 50. * (i + 1);
						_astrCalibMeasure[i] = "FairPremium";

						if (null == (_aCDS[i] = org.drip.product.creator.CDSBuilder.CreateSNAC (_dtStart, (i
							+ 1) + "Y", 0.01, "CORP")))
							return false;
			 		}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_cc = org.drip.param.creator.CreditScenarioCurveBuilder.CreateCreditCurve
						("CORP", _dtStart, _aCDS, _dc, _adblQuotes, _astrCalibMeasure, 0.4, false));
				}
			});

			/*
			 * Testing creation of the Credit Curve from flat hazard - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("FromFlatHazard",
				_strRegressionScenario)
			{
				private org.drip.analytics.definition.CreditCurve _ccFromFlatHazard = null;

				@Override public boolean execRegression()
				{
					return null != (_ccFromFlatHazard =
						org.drip.analytics.creator.CreditCurveBuilder.FromFlatHazard (_dtStart.getJulian(),
							"CORP", 0.02, 0.4));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					final int NUM_DC_INSTRUMENTS = 5;
					double adblHazard[] = new double[NUM_DC_INSTRUMENTS];
					org.drip.analytics.date.JulianDate adt[] = new
						org.drip.analytics.date.JulianDate[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblHazard[i] =
								_ccFromFlatHazard.calcHazard (_dtStart, (adt[i] = _dtStart.addYears (i +
									1)))))
								return false;

							rnvd.set ("HazardRateFromHazardCurve[" + adt[i] + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblHazard[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (adblHazard[i], 0.02))
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
			 * Testing creation of the Credit Curve from flat survival - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("FromSurvival",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblSurvival[] = new double[NUM_DC_INSTRUMENTS];
				private org.drip.analytics.definition.CreditCurve _ccFromSurvival = null;

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

						_adblSurvival[i] = 1. - (i + 1) * 0.1;
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_ccFromSurvival =
						org.drip.analytics.creator.CreditCurveBuilder.FromSurvival (_dtStart.getJulian(),
							"CORP", _adblDate, _adblSurvival, 0.4));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double adblSurvivalCalc[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblSurvivalCalc[i] =
								_ccFromSurvival.getSurvival (_adblDate[i])))
								return false;

							org.drip.analytics.date.JulianDate dt = new org.drip.analytics.date.JulianDate
								(_adblDate[i]);

							rnvd.set ("SurvivalFromOriginal[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (_adblSurvival[i], 1, 4, 1));

							rnvd.set ("SurvivalFromSurvival[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblSurvivalCalc[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (adblSurvivalCalc[i],
								_adblSurvival[i]))
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
			 * Testing creation of the Credit Curve from hazard nodes - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("FromHazard",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblHazard[] = new double[NUM_DC_INSTRUMENTS];
				private org.drip.analytics.definition.CreditCurve _ccFromHazard = null;

				@Override public boolean preRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						_adblDate[i] = _dtStart.addYears (i + 1).getJulian();

						_adblHazard[i] = 0.01 * (1. - (i + 1) * 0.1);
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_ccFromHazard =
						org.drip.analytics.creator.CreditCurveBuilder.CreateCreditCurve (_dtStart, "CORP",
							_adblDate, _adblHazard, 0.4));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					org.drip.analytics.date.JulianDate dt1 = _dtStart;
					double adblHazardCalc[] = new double[NUM_DC_INSTRUMENTS];

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (adblHazardCalc[i] =
								_ccFromHazard.calcHazard (dt1, dt1.addYears (1))))
								return false;

							org.drip.analytics.date.JulianDate dt2 = dt1.addYears (1);

							rnvd.set ("HazardFromOriginal[" + dt1 + "-" + dt2 + "]",
								org.drip.math.common.FormatUtil.FormatDouble (_adblHazard[i], 1, 4, 1));

							rnvd.set ("HazardFromHazard[" + dt1 + "-" + dt2 + "]",
								org.drip.math.common.FormatUtil.FormatDouble (adblHazardCalc[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (adblHazardCalc[i],
								_adblHazard[i]))
								return false;

							dt1 = dt1.addYears (1);
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("CompAndQuotes",
				_strRegressionScenario)
			{
				private double[] _adblQuotes = null;
				private org.drip.product.definition.CalibratableComponent[] _aCalibComp = null;

				@Override public boolean execRegression()
				{
					return null != (_adblQuotes = _cc.getCompQuotes()) && 0 == _adblQuotes.length && null ==
						(_aCalibComp = _cc.getCalibComponents()) && 0 == _aCalibComp.length &&
							_aCalibComp.length != _adblQuotes.length;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					for (int i = 0; i < _adblQuotes.length; ++i) {
						org.drip.analytics.date.JulianDate dt = _cc.getNodeDate (i);

						java.lang.String strCode = _aCalibComp[i].getPrimaryCode();

						if (null == dt || null == strCode || strCode.isEmpty()) return false;

						try {
							double dblCompQuote = _cc.getQuote (strCode);

							rnvd.set ("CompQuote" + "_" + strCode + "[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (dblCompQuote, 1, 4, 1));

							rnvd.set ("NodeQuote" + "_" + strCode + "[" + dt + "]",
								org.drip.math.common.FormatUtil.FormatDouble (_adblQuotes[i], 1, 4, 1));

							if (!org.drip.math.common.NumberUtil.WithinTolerance (dblCompQuote,
								_adblQuotes[i]))
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
			 * Testing creation of a parallel hazard shifted Credit Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor
				("ParallelHazardShiftedCurve", _strRegressionScenario)
			{
				private org.drip.analytics.definition.CreditCurve _ccParallelShifted = null;

				@Override public boolean execRegression()
				{
					if (null == (_ccParallelShifted = _cc.createParallelHazardShiftedCurve (0.0005)))
						return false;

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double[] adblQuotes = _cc.getCompQuotes();

					org.drip.analytics.date.JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						org.drip.analytics.date.JulianDate dt = _cc.getNodeDate (i);

						double dblBaseHazard = java.lang.Double.NaN;
						double dblShiftedHazard = java.lang.Double.NaN;

						try {
							if (!org.drip.math.common.NumberUtil.IsValid (dblShiftedHazard =
								_ccParallelShifted.calcHazard (dt1, dt)) ||
									!org.drip.math.common.NumberUtil.IsValid (dblBaseHazard = _cc.calcHazard
										(dt1, dt)))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("BaseCurveHazard[" + dt1 + "-" + dt + "]",
							org.drip.math.common.FormatUtil.FormatDouble (dblBaseHazard, 1, 4, 1));

						rnvd.set ("ParallelShiftedCurveHazard[" + dt1 + "-" + dt + "]",
							org.drip.math.common.FormatUtil.FormatDouble (dblShiftedHazard, 1, 4, 1));

						dt = dt1;

						if (!org.drip.math.common.NumberUtil.WithinTolerance (dblBaseHazard + 0.0005,
							dblShiftedHazard))
							return false;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the parallel quote shifted credit curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor
				("ParallelQuoteShiftedCurve", _strRegressionScenario)
			{
				private org.drip.analytics.definition.CreditCurve _ccParallelShifted = null;

				@Override public boolean execRegression()
				{
					return null != (_ccParallelShifted = (org.drip.analytics.definition.CreditCurve)
						_cc.createParallelShiftedCurve (5.));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double[] adblQuotes = _cc.getCompQuotes();

					double[] adblQuotesShifted = _ccParallelShifted.getCompQuotes();

					org.drip.analytics.date.JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						org.drip.analytics.date.JulianDate dt = _cc.getNodeDate (i);

						rnvd.set ("BaseCurveQuote[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
							(adblQuotes[i], 1, 5, 1));

						rnvd.set ("ParallelShiftedCurveQuote[" + dt + "]",
							org.drip.math.common.FormatUtil.FormatDouble (adblQuotesShifted[i], 1, 5, 1));

						dt = dt1;

						if (!org.drip.math.common.NumberUtil.WithinTolerance (adblQuotes[i] + 5.,
							adblQuotesShifted[i]))
							return false;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the node tweaked Credit Curve - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("NodeTweakedCurve",
				_strRegressionScenario)
			{
				private static final int TWEAKED_NODE = 0;

				private org.drip.param.definition.CreditNodeTweakParams _cntp = null;
				private org.drip.analytics.definition.CreditCurve _ccTweakedCurve = null;

				@Override public boolean preRegression()
				{
					try {
						_cntp = new org.drip.param.definition.CreditNodeTweakParams
							(org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_PARAM_QUOTE,
								org.drip.param.definition.CreditNodeTweakParams.CREDIT_TWEAK_NODE_MEASURE_QUOTE,
							TWEAKED_NODE, true, 0.1, false);
					} catch (java.lang.Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean execRegression()
				{
					return null != (_ccTweakedCurve = (org.drip.analytics.definition.CreditCurve)
						_cc.createTweakedCurve (_cntp));
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double[] adblQuotes = _cc.getCompQuotes();

					org.drip.analytics.date.JulianDate dt1 = _dtStart;

					for (int i = 0; i < adblQuotes.length; ++i) {
						org.drip.analytics.date.JulianDate dt = _cc.getNodeDate (i);

						double dblBaseHazard = java.lang.Double.NaN;
						double dblShiftedHazard = java.lang.Double.NaN;

						try {
							if (!org.drip.math.common.NumberUtil.IsValid (dblShiftedHazard =
								_ccTweakedCurve.calcHazard (dt1, dt)) ||
									!org.drip.math.common.NumberUtil.IsValid (dblBaseHazard = _cc.calcHazard
										(dt1, dt)))
								return false;
						} catch (Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("UntweakedHazard[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
							(dblBaseHazard, 1, 5, 1));

						rnvd.set ("TweakedHazard[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
							(dblShiftedHazard, 1, 5, 1));

						dt = dt1;
					}

					return true;
				}
			});

			/*
			 * Testing creation of the Credit Curve from flat/quoted spread - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("FlatCurve",
				_strRegressionScenario)
			{
				private org.drip.analytics.definition.CreditCurve _ccFlatCurve = null;

				@Override public boolean execRegression()
				{
					if (null == (_ccFlatCurve = _cc.createFlatCurve (90., false, 0.35))) return false;

					return true;
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					final int NUM_DC_INSTRUMENTS = 5;

					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						org.drip.analytics.date.JulianDate dt = _dtStart.addYears (i + 1);

						double dblHazard = java.lang.Double.NaN;

						try {
							if (!org.drip.math.common.NumberUtil.IsValid (dblHazard = _ccFlatCurve.calcHazard
								(dt)))
								return false;
						} catch (java.lang.Exception e) {
							e.printStackTrace();

							return false;
						}

						rnvd.set ("FlatHazard[" + dt + "]", org.drip.math.common.FormatUtil.FormatDouble
							(dblHazard, 1, 5, 1));
					}

					return true;
				}
			});

			/*
			 * Testing setting/removing specific default dates - implements the pre-regression, the
			 * 	post-regression, and the actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("SpecificDefault",
				_strRegressionScenario)
			{
				private double _dblSpecificDefault = java.lang.Double.NaN;

				@Override public boolean preRegression()
				{
					return !org.drip.math.common.NumberUtil.IsValid (_dblSpecificDefault = _dtStart.addYears
						(2).getJulian());
				}

				@Override public boolean execRegression()
				{
					return _cc.setSpecificDefault (_dblSpecificDefault);
				}

				@Override public boolean postRegression (
					final org.drip.regression.core.RegressionRunDetail rnvd)
				{
					double dblSurvivalProb = java.lang.Double.NaN;

					org.drip.analytics.date.JulianDate dtSurvival = _dtStart.addYears (3);

					double dblSurvivalDate = dtSurvival.getJulian();

					try {
						if (!org.drip.math.common.NumberUtil.IsValid (dblSurvivalProb = _cc.getSurvival
							(dblSurvivalDate)))
							return false;
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					rnvd.set ("SpecificDefaultSetSurvival[" + dtSurvival + "]", "" + dblSurvivalProb);

					if (!_cc.unsetSpecificDefault()) return false;

					try {
						if (!org.drip.math.common.NumberUtil.IsValid (dblSurvivalProb = _cc.getSurvival
							(dblSurvivalDate)))
							return false;
					} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("EffectiveSurvival",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblDate[] = new double[NUM_DC_INSTRUMENTS];
				private double _adblSurvival[] = new double[NUM_DC_INSTRUMENTS];

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
							if (!org.drip.math.common.NumberUtil.IsValid (_adblSurvival[i] =
								_cc.getEffectiveSurvival ((i + 1) + "Y", (i + 2) + "Y")))
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
							rnvd.set ("EffectiveSurvival[" + new org.drip.analytics.date.JulianDate
								(_adblDate[i]) + "]", org.drip.math.common.FormatUtil.FormatDouble
									(_adblSurvival[i], 1, 4, 1));
						} catch (java.lang.Exception e) {
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

			_setRegressors.add (new org.drip.regression.core.UnitRegressionExecutor ("EffectiveRecovery",
				_strRegressionScenario)
			{
				private static final int NUM_DC_INSTRUMENTS = 5;

				private double _adblEffectiveRecovery[] = new double[NUM_DC_INSTRUMENTS];

				@Override public boolean execRegression()
				{
					for (int i = 0; i < NUM_DC_INSTRUMENTS; ++i) {
						try {
							if (!org.drip.math.common.NumberUtil.IsValid (_adblEffectiveRecovery[i] =
								_cc.getEffectiveRecovery ((i + 1) + "Y", (i + 2) + "Y")))
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
							rnvd.set ("EffectiveRecovery[" + (i + 1) + "Y-" + (i + 2) + "Y]",
								org.drip.math.common.FormatUtil.FormatDouble (_adblEffectiveRecovery[i], 1,
									4, 1));

							rnvd.set ("CurveRecovery[" + (i + 1) + "Y-" + (i + 2) + "Y]",
								org.drip.math.common.FormatUtil.FormatDouble (_cc.getRecovery ((i + 1) +
									"Y"), 1, 4, 1) + "-" + org.drip.math.common.FormatUtil.FormatDouble
										(_cc.getRecovery ((i + 2) + "Y"), 1, 4, 1));
						} catch (java.lang.Exception e) {
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

	@Override public java.util.List<org.drip.regression.core.UnitRegressor> getRegressorSet()
	{
		return _setRegressors;
	}

	@Override public java.lang.String getSetName()
	{
		return _strRegressionScenario;
	}
}
