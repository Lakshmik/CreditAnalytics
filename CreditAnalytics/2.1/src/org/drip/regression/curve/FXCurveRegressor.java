
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
 * Credit Analytics Imports
 */

import org.drip.analytics.creator.*;
import org.drip.analytics.definition.*;
import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.support.GenericUtil;
import org.drip.param.valuation.*;
import org.drip.product.creator.*;
import org.drip.product.definition.*;
import org.drip.product.params.*;

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
 * This sample implements the regression analysis set for the FX Curve.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FXCurveRegressor implements RegressorSet {
	private FXForwardCurve _fxForwardCurve = null;
	private FXForward _fxfwd = null;
	private String _strRegressionScenario = "org.drip.analytics.curve.FXCurve";

	private List<UnitRegressor> _setRegressors = new ArrayList<UnitRegressor>();

	/**
	 * Do nothing FXCurveRegressor constructor
	 */

	public FXCurveRegressor() {
	}

	/*
	 * FX Curve Regressor set setup
	 */

	@Override public boolean setupRegressors() {
		/*
		 * FXBasis and FXCurve Creation unit regressor - implements the pre-regression, the post-regression,
		 * 	and the actual regression functionality of the UnitRegressorExecutor class.
		 */

		try {
			_setRegressors.add (new UnitRegressionExecutor ("FXBasisAndFXCurveCreation",
				_strRegressionScenario) {
				private static final int NUM_FX_NODES = 5;

				private CurrencyPair _cp = null;
				private double _dblFXSpot = 1.25;
				double[] _adblFXFwd = new double[NUM_FX_NODES];
				double[] _adblNodes = new double[NUM_FX_NODES];
				boolean[] _abIsPIP = new boolean[NUM_FX_NODES];

				@Override public boolean preRegression() {
					try {
						_cp = new CurrencyPair ("EUR", "USD", "USD", 10000.);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					Random rand = new Random();

					JulianDate dtToday = JulianDate.Today();

					for (int i = 0; i < NUM_FX_NODES; ++i) {
						_abIsPIP[i] = false;

						_adblFXFwd[i] = _dblFXSpot - (i + 1) * 0.01 * rand.nextDouble();

						_adblNodes[i] = dtToday.addYears (i + 1).getJulian();
					}

					return true;
				}

				@Override public boolean execRegression() {
					JulianDate dtToday = JulianDate.Today();

					if (null == (_fxfwd = FXForwardBuilder.CreateFXForward (_cp, dtToday, "18M"))) return false;

					try {
						if (null == (_fxForwardCurve = FXForwardCurveBuilder.CreateFXForwardCurve (_cp,
							dtToday, _dblFXSpot, _adblNodes, _adblFXFwd, _abIsPIP)))
							return false;
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}
			});

			/*
			 * FX Forward Test unit regressor - implements the pre-regression, the post-regression, and the
			 * 	actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FXFwdTest", _strRegressionScenario) {
				private DiscountCurve _dcEUR = null;
				private DiscountCurve _dcUSD = null;
				private double _dblFXFwd = Double.NaN;
				private double _dblFXFwdPIP = Double.NaN;
				private ValuationParams _valParams = null;

				@Override public boolean preRegression() {
					JulianDate dtToday = JulianDate.Today();

					if (null == (_dcUSD = DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.05)))
						return false;

					if (null == (_dcEUR = DiscountCurveBuilder.CreateFromFlatRate (dtToday, "EUR", 0.04)))
						return false;

					if (null == (_valParams = ValuationParams.CreateValParams (dtToday, 0, "USD",
						Convention.DR_ACTUAL)))
						return false;

					return true;
				}

				@Override public boolean execRegression() {
					try {
						_dblFXFwd = _fxfwd.implyFXForward (_valParams, _dcEUR, _dcUSD, 1.4, false);

						_dblFXFwdPIP = _fxfwd.implyFXForward (_valParams, _dcEUR, _dcUSD, 1.4, true);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					rnvd.set ("FXFwd", GenericUtil.FormatDouble (_dblFXFwd, 1, 4, 1));

					rnvd.set ("FXFwdPIP", GenericUtil.FormatDouble (_dblFXFwdPIP, 1, 1, 1));

					return true;
				}
			});

			/*
			 * FXBasis Test unit regressor - implements the pre-regression, the post-regression, and the
			 * 	actual regression functionality of the UnitRegressorExecutor class.
			 */

			_setRegressors.add (new UnitRegressionExecutor ("FXBasisTest", _strRegressionScenario) {
				private static final int NUM_FX_NODES = 5;

				private double _dblFXSpot = 1.26;
				private DiscountCurve _dcEUR = null;
				private DiscountCurve _dcUSD = null;
				private double _dblFXFwdMarket = 1.26;
				private FXBasisCurve _fxEURBasisCurve = null;
				private FXBasisCurve _fxUSDBasisCurve = null;
				private double[] _adblFullEURBasis = null;
				private double[] _adblFullUSDBasis = null;
				private ValuationParams _valParams = null;
				private double _dblDCEURBasis = Double.NaN;
				private double _dblDCUSDBasis = Double.NaN;
				private double[] _adblFXFwdFromEURBasis = null;
				private double[] _adblFXFwdFromUSDBasis = null;
				private double[] _adblBootstrappedEURBasis = null;
				private double[] _adblBootstrappedUSDBasis = null;
				private double[] _adblNodes = new double[NUM_FX_NODES];

				@Override public boolean preRegression() {
					JulianDate dtToday = JulianDate.Today();

					if (null == (_dcUSD = DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", 0.05)))
						return false;

					if (null == (_dcEUR = DiscountCurveBuilder.CreateFromFlatRate (dtToday, "EUR", 0.04)))
						return false;

					if (null == (_valParams = ValuationParams.CreateValParams (dtToday, 0, "USD",
						Convention.DR_ACTUAL)))
						return false;

						for (int i = 0; i < NUM_FX_NODES; ++i)
							_adblNodes[i] = dtToday.addYears (i + 1).getJulian();

					return true;
				}

				@Override public boolean execRegression() {
					try {
						_dblDCEURBasis = _fxfwd.calcDCBasis (_valParams, _dcEUR, _dcUSD, _dblFXSpot,
							_dblFXFwdMarket, false);

						_dblDCUSDBasis = _fxfwd.calcDCBasis (_valParams, _dcEUR, _dcUSD, _dblFXSpot,
							_dblFXFwdMarket, true);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					if (null == (_adblFullUSDBasis = _fxForwardCurve.getFullBasis (_valParams, _dcEUR, _dcUSD,
						true)) || 0 == _adblFullUSDBasis.length)
						return false;

					if (null == (_adblFullEURBasis = _fxForwardCurve.getFullBasis (_valParams, _dcEUR, _dcUSD,
						false)) || 0 == _adblFullEURBasis.length)
						return false;

					if (null == (_adblBootstrappedUSDBasis = _fxForwardCurve.bootstrapBasis (_valParams, _dcEUR,
						_dcUSD, true)) || 0 == _adblBootstrappedUSDBasis.length)
						return false;

					if (null == (_adblBootstrappedEURBasis = _fxForwardCurve.bootstrapBasis (_valParams, _dcEUR,
						_dcUSD, false)) || 0 == _adblBootstrappedEURBasis.length)
						return false;

					try {
						JulianDate dtToday = JulianDate.Today();

						CurrencyPair cp = new CurrencyPair ("EUR", "USD", "USD", 10000.);

						_fxUSDBasisCurve = FXBasisCurveBuilder.CreateFXBasisCurve (cp, dtToday, _dblFXSpot, _adblNodes,
							_adblFullUSDBasis, false);

						_fxEURBasisCurve = FXBasisCurveBuilder.CreateFXBasisCurve (cp, dtToday, _dblFXSpot, _adblNodes,
							_adblFullEURBasis, false);
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}

					if (null == (_adblFXFwdFromUSDBasis = _fxUSDBasisCurve.getFullFXFwd (_valParams, _dcEUR,
						_dcUSD, true, false)) || 0 == _adblFXFwdFromUSDBasis.length)
						return false;

					if (null == (_adblFXFwdFromEURBasis = _fxEURBasisCurve.getFullFXFwd (_valParams, _dcEUR,
						_dcUSD, false, false)) || 0 == _adblFXFwdFromEURBasis.length)
						return false;

					return true;
				}

				@Override public boolean postRegression (final RegressionRunDetail rnvd) {
					rnvd.set ("EURBasis", GenericUtil.FormatDouble (_dblDCEURBasis, 1, 2, 10000.));

					rnvd.set ("USDBasis", GenericUtil.FormatDouble (_dblDCUSDBasis, 1, 2, 10000.));

					for (int i = 0; i < _adblFullUSDBasis.length; ++i) {
						rnvd.set ("FullUSDBasis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
							(_adblFullUSDBasis[i], 1, 4, 10000.));

						rnvd.set ("FullEURBasis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
								(_adblFullEURBasis[i], 1, 4, 10000.));

						rnvd.set ("BootstrapUSDBasis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
							(_adblBootstrappedUSDBasis[i], 1, 0, 10000.));

						rnvd.set ("BootstrapEURBasis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
							(_adblBootstrappedEURBasis[i], 1, 0, 10000.));

						rnvd.set ("FXFwd from USD Basis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
							(_adblFXFwdFromUSDBasis[i], 1, 4, 1.));

						rnvd.set ("FXFwd from EUR Basis{" + (i + 1) + "Y}", GenericUtil.FormatDouble
							(_adblFXFwdFromEURBasis[i], 1, 4, 1.));

						if (!org.drip.math.common.NumberUtil.WithinTolerance (_adblFullUSDBasis[i],
							-_adblFullEURBasis[i]))
							return false;

						if (!org.drip.math.common.NumberUtil.WithinTolerance (_adblBootstrappedUSDBasis[i],
							-_adblBootstrappedEURBasis[i]))
							return false;

						if (!org.drip.math.common.NumberUtil.WithinTolerance (_adblFXFwdFromUSDBasis[i],
							_adblFXFwdFromEURBasis[i]))
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
