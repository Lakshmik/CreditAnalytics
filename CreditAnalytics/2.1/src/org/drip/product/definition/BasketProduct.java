
package org.drip.product.definition;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 *  This abstract class extends BasketMarketParamRef. Provides methods for getting the basket’s components,
 *  	notional, coupon, effective date, maturity date, coupon amount, and list of coupon periods.
 *  
 * @author Lakshmi Krishnamurthy
 */

public abstract class BasketProduct extends org.drip.service.stream.Serializer implements
	org.drip.product.definition.BasketMarketParamRef {
	protected double getMeasure (
		final java.lang.String strMeasure,
		final java.util.Map<java.lang.String, java.lang.Double> mapCalc)
		throws java.lang.Exception
	{
		if (null == strMeasure || strMeasure.isEmpty() || null == mapCalc || null == mapCalc.entrySet())
			throw new java.lang.Exception ("Invalid params into BasketProduct.getMeasure!");

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
			if (null != me && null != me.getKey() && me.getKey().equals (strMeasure)) return me.getValue();
		}

		throw new java.lang.Exception (strMeasure + " not a valid measure!");
	}

	/**
	 * Returns the basket name
	 * 
	 * @return Name of the basket product
	 */

	public abstract java.lang.String getName();

	/**
	 * Returns the number of components in the basket
	 * 
	 * @return Number of components in the basket product
	 */

	public abstract int getNumberofComponents();

	/**
	 * Returns the initial notional of the basket product
	 * 
	 * @return Initial notional of the basket product
	 */

	public abstract double getInitialNotional();

	/**
	 * Retrieves the notional at the given date
	 * 
	 * @param dblDate Double JulianDate
	 * 
	 * @return Notional
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public abstract double getNotional (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Retrieves the time-weighted notional between 2 given dates
	 * 
	 * @param dblDate1 Double JulianDate first
	 * @param dblDate2 Double JulianDate second
	 * 
	 *; @return Notional
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public abstract double getNotional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception;

	/**
	 * Retrieves the basket product's coupon amount at the given date
	 * 
	 * @param dblDate Double JulianDate
	 * @param bmp Basket Market Parameters
	 * 
	 * @return Coupon Amount
	 * 
	 * @throws java.lang.Exception Thrown if coupon cannot be calculated
	 */

	public abstract double getCoupon (
		final double dblDate,
		final org.drip.param.definition.BasketMarketParams bmp)
		throws java.lang.Exception;

	/**
	 * Returns the effective date of the basket product
	 * 
	 * @return Effective date of the basket product
	 */

	public abstract org.drip.analytics.date.JulianDate getEffectiveDate();

	/**
	 * Returns the maturity date of the basket product
	 * 
	 * @return Maturity date of the basket product
	 */

	public abstract org.drip.analytics.date.JulianDate getMaturityDate();

	/**
	 * Gets the basket product's coupon periods
	 * 
	 * @return List of CouponPeriods
	 */

	public abstract java.util.List<org.drip.analytics.period.CouponPeriod> getCouponPeriod();

	/**
	 * Gets the first coupon date
	 * 
	 * @return First Coupon Date
	 */

	public abstract org.drip.analytics.date.JulianDate getFirstCouponDate();

	/**
	 * Generates a full list of the basket product measures for the full input set of market parameters
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param bmp BasketMarketParams
	 * @param quotingParams Quoting Parameters
	 * 
	 * @return Map of measure name and value
	 */

	public abstract java.util.Map<java.lang.String, java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.BasketMarketParams bmp,
		final org.drip.param.valuation.QuotingParams quotingParams);

	/**
	 * Calculates the value of the given basket product measure
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param bmp BasketMarketParams
	 * @param quotingParams Quoting Parameters
	 * @param strMeasure Measure String
	 * 
	 * @return Double measure value
	 * 
	 * @throws java.lang.Exception Thrown if the measure cannot be calculated
	 */

	public double calcMeasureValue (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.BasketMarketParams bmp,
		final org.drip.param.valuation.QuotingParams quotingParams,
		final java.lang.String strMeasure)
		throws java.lang.Exception
	{
		return getMeasure (strMeasure, value (valParams, pricerParams, bmp, quotingParams));
	}

	/**
	 * Generates a full list of the basket product measures for the set of scenario market parameters present
	 * 	in the org.drip.param.definition.MarketParams
	 * 
	 * @param valParams ValuationParams
	 * @param pricerParams PricerParams
	 * @param mpc org.drip.param.definition.MarketParams
	 * @param quotingParams Quoting Parameters
	 * 
	 * @return BasketOutput object
	 */

	public org.drip.analytics.output.BasketMeasures calcMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.MarketParams mpc,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == mpc.getScenBMP (this, "Base")) return null;

		long lStart = System.nanoTime();

		org.drip.analytics.output.BasketMeasures bkop = new org.drip.analytics.output.BasketMeasures();

		if (null == (bkop._mBase = value (valParams, pricerParams, mpc.getScenBMP (this, "Base"),
			quotingParams)))
			return null;

		if (null != mpc.getScenBMP (this, "FlatCreditBumpUp")) {
			java.util.Map<java.lang.String, java.lang.Double> mapCreditBumpUp = value (valParams,
				pricerParams, mpc.getScenBMP (this, "FlatCreditBumpUp"), quotingParams);

			if (null != mapCreditBumpUp && null != mapCreditBumpUp.entrySet()) {
				bkop._mFlatCreditDelta = new java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCreditBumpUp.entrySet())
				{
					if (null == me || null == me.getKey()) continue;

					bkop._mFlatCreditDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				if (null != mpc.getScenBMP (this, "FlatCreditBumpDn")) {
					java.util.Map<java.lang.String, java.lang.Double> mapCreditBumpDn = value (valParams,
						pricerParams, mpc.getScenBMP (this, "FlatCreditBumpDn"), quotingParams);

					if (null != mapCreditBumpDn && null != mapCreditBumpDn.entrySet()) {
						bkop._mFlatCreditGamma = new java.util.HashMap<java.lang.String, java.lang.Double>();

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							mapCreditBumpDn.entrySet()) {
							if (null == me || null == me.getKey()) continue;

							bkop._mFlatCreditGamma.put (me.getKey(), me.getValue() + mapCreditBumpUp.get
								(me.getKey()) - bkop._mBase.get (me.getKey()));
						}
					}
				}
			}
		}

		if (null != mpc.getScenBMP (this, "FlatIRBumpUp")) {
			java.util.Map<java.lang.String, java.lang.Double> mapIRBumpUp = value (valParams, pricerParams,
				mpc.getScenBMP (this, "FlatIRBumpUp"), quotingParams);

			if (null != mapIRBumpUp && null != mapIRBumpUp.entrySet()) {
				bkop._mFlatIRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapIRBumpUp.entrySet()) {
					if (null == me || null == me.getKey()) continue;

					bkop._mFlatIRDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				if (null != mpc.getScenBMP (this, "FlatIRBumpDn")) {
					java.util.Map<java.lang.String, java.lang.Double> mapIRBumpDn = value (valParams,
						pricerParams, mpc.getScenBMP (this, "FlatIRBumpDn"), quotingParams);

					if (null != mapIRBumpDn && null != mapIRBumpDn.entrySet()) {
						bkop._mFlatIRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>();

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							mapIRBumpDn.entrySet()) {
							if (null == me || null == me.getKey()) continue;

							bkop._mFlatIRGamma.put (me.getKey(), me.getValue() + mapIRBumpUp.get
								(me.getKey()) - bkop._mBase.get (me.getKey()));
						}
					}
				}
			}
		}

		if (null != mpc.getScenBMP (this, "FlatRRBumpUp")) {
			java.util.Map<java.lang.String, java.lang.Double> mapRRBumpUp = value (valParams, pricerParams,
				mpc.getScenBMP (this, "FlatRRBumpUp"), quotingParams);

			if (null != mapRRBumpUp && null != mapRRBumpUp.entrySet()) {
				bkop._mFlatRRDelta = new java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapRRBumpUp.entrySet()) {
					if (null == me || null == me.getKey()) continue;

					bkop._mFlatRRDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				if (null != mpc.getScenBMP (this, "FlatRRBumpDn")) {
					java.util.Map<java.lang.String, java.lang.Double> mapRRBumpDn = value (valParams,
						pricerParams, mpc.getScenBMP (this, "FlatRRBumpDn"), quotingParams);

					if (null != mapRRBumpDn && null != mapRRBumpDn.entrySet()) {
						bkop._mFlatRRGamma = new java.util.HashMap<java.lang.String, java.lang.Double>();

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							mapRRBumpDn.entrySet()) {
							if (null == me || null == me.getKey()) continue;

							bkop._mFlatRRGamma.put (me.getKey(), me.getValue() + mapRRBumpUp.get
								(me.getKey()) - bkop._mBase.get (me.getKey()));
						}
					}
				}
			}
		}

		if (null != mpc.getIRBumpBMP (this, true) && null != mpc.getIRBumpBMP (this, true).entrySet()) {
			bkop._mmIRDelta = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
				java.lang.Double>>();

			for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams> meBMP :
				mpc.getIRBumpBMP (this, true).entrySet()) {
				java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams, pricerParams,
					meBMP.getValue(), quotingParams);

				if (null == mapCalc || null == mapCalc.entrySet()) continue;

				java.util.Map<java.lang.String, java.lang.Double> mapIRDelta = new
					java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
					if (null == me || null == me.getKey()) continue;

					mapIRDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				bkop._mmIRDelta.put (meBMP.getKey(), mapIRDelta);
			}

			if (null != mpc.getIRBumpBMP (this, false) && null != mpc.getIRBumpBMP (this, false).entrySet())
			{
				bkop._mmIRGamma = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					java.lang.Double>>();

				for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams> meBMP :
					mpc.getIRBumpBMP (this, true).entrySet()) {
					java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
						pricerParams, meBMP.getValue(), quotingParams);

					if (null == mapCalc || null == mapCalc.entrySet()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapIRGamma = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
						if (null == me || null == me.getKey()) continue;

						mapIRGamma.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()) +
							bkop._mmIRDelta.get (meBMP.getKey()).get (me.getKey()));
					}

					bkop._mmIRGamma.put (meBMP.getKey(), mapIRGamma);
				}
			}
		}

		if (null != mpc.getCreditBumpBMP (this, true) && null != mpc.getCreditBumpBMP (this,
			true).entrySet()) {
			bkop._mmCreditDelta = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
				java.lang.Double>>();

			for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams> meBMP :
				mpc.getCreditBumpBMP (this, true).entrySet()) {
				java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams, pricerParams,
					meBMP.getValue(), quotingParams);

				if (null == mapCalc || null == mapCalc.entrySet()) continue;

				java.util.Map<java.lang.String, java.lang.Double> mapCreditDelta = new
					java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
					if (null == me || null == me.getKey()) continue;

					mapCreditDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				bkop._mmCreditDelta.put (meBMP.getKey(), mapCreditDelta);
			}

			if (null != mpc.getCreditBumpBMP (this, false) && null != mpc.getCreditBumpBMP (this,
				false).entrySet()) {
				bkop._mmCreditGamma = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					java.lang.Double>>();

				for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
					meBMP : mpc.getCreditBumpBMP (this, true).entrySet()) {
					java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
						pricerParams, meBMP.getValue(), quotingParams);

					if (null == mapCalc || null == mapCalc.entrySet()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapCreditGamma = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
						if (null == me || null == me.getKey()) continue;

						mapCreditGamma.put (me.getKey(), me.getValue() + bkop._mmCreditDelta.get
							(meBMP.getKey()).get (me.getKey()) - bkop._mBase.get (me.getKey()));
					}

					bkop._mmCreditGamma.put (meBMP.getKey(), mapCreditGamma);
				}
			}
		}

		if (null != mpc.getRecoveryBumpBMP (this, true) && null != mpc.getRecoveryBumpBMP (this,
			true).entrySet()) {
			bkop._mmRRDelta = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
				java.lang.Double>>();

			for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams> meBMP :
				mpc.getRecoveryBumpBMP (this, true).entrySet()) {
				java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams, pricerParams,
					meBMP.getValue(), quotingParams);

				if (null == mapCalc || null == mapCalc.entrySet()) continue;

				java.util.Map<java.lang.String, java.lang.Double> mapRRDelta = new
					java.util.HashMap<java.lang.String, java.lang.Double>();

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
					if (null == me || null == me.getKey()) continue;

					mapRRDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
				}

				bkop._mmRRDelta.put (meBMP.getKey(), mapRRDelta);
			}

			if (null != mpc.getRecoveryBumpBMP (this, false) && null != mpc.getRecoveryBumpBMP (this,
				false).entrySet()) {
				bkop._mmRRGamma = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					java.lang.Double>>();

				for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
					meBMP : mpc.getRecoveryBumpBMP (this, true).entrySet()) {
					java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
						pricerParams, meBMP.getValue(), quotingParams);

					if (null == mapCalc || null == mapCalc.entrySet()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapRRGamma = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
						if (null == me || null == me.getKey()) continue;

						mapRRGamma.put (me.getKey(), me.getValue() + bkop._mmRRDelta.get (meBMP.getKey()).get
							(me.getKey()) - bkop._mBase.get (me.getKey()));
					}

					bkop._mmRRGamma.put (meBMP.getKey(), mapRRGamma);
				}
			}
		}

		if (null != mpc.getIRTenorBumpBMP (this, true) && null != mpc.getIRTenorBumpBMP (this,
			true).entrySet()) {
			bkop._mmmIRTenorDelta = new java.util.HashMap<java.lang.String,
				java.util.Map<java.lang.String, java.util.Map<java.lang.String,
					java.lang.Double>>>();

			for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
				org.drip.param.definition.BasketMarketParams>> meIRTenorBMP : mpc.getIRTenorBumpBMP (this,
					true).entrySet()) {
				java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					mmTenorDelta = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>>();

				if (null == meIRTenorBMP || null == meIRTenorBMP.getValue() || null ==
					meIRTenorBMP.getValue().entrySet())
					continue;

				for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
					meTenorBMP : meIRTenorBMP.getValue().entrySet()) {
					java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
						pricerParams, meTenorBMP.getValue(), quotingParams);

					if (null == mapCalc || null == mapCalc.entrySet()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapDelta = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
						if (null == me || null == me.getKey()) continue;

						mapDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
					}

					mmTenorDelta.put (meTenorBMP.getKey(), mapDelta);
				}

				bkop._mmmIRTenorDelta.put (meIRTenorBMP.getKey(), mmTenorDelta);
			}

			if (null != mpc.getIRTenorBumpBMP (this, false) && null != mpc.getIRTenorBumpBMP (this, false)) {
				bkop._mmmIRTenorGamma = new java.util.HashMap<java.lang.String,
					java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>();

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					org.drip.param.definition.BasketMarketParams>> meIRTenorBMP : mpc.getIRTenorBumpBMP
						(this, false).entrySet()) {
					java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
						mmTenorGamma = new java.util.HashMap<java.lang.String,
							java.util.Map<java.lang.String, java.lang.Double>>();

					if (null == meIRTenorBMP || null == meIRTenorBMP.getValue() || null ==
						meIRTenorBMP.getValue().entrySet())
						continue;

					for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
						meTenorBMP : meIRTenorBMP.getValue().entrySet()) {
						java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
							pricerParams, meTenorBMP.getValue(), quotingParams);

						if (null == mapCalc || null == mapCalc.entrySet()) continue;

						java.util.Map<java.lang.String, java.lang.Double> mapGamma = new
							java.util.HashMap<java.lang.String, java.lang.Double>();

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet())
						{
							if (null == me || null == me.getKey()) continue;

							mapGamma.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()) +
								bkop._mmmIRTenorDelta.get (meIRTenorBMP.getKey()).get
									(meTenorBMP.getKey()).get (me.getKey()));
						}

						mmTenorGamma.put (meTenorBMP.getKey(), mapGamma);
					}

					bkop._mmmIRTenorGamma.put (meIRTenorBMP.getKey(), mmTenorGamma);
				}
			}
		}

		if (null != mpc.getCreditTenorBumpBMP (this, true) && null != mpc.getCreditTenorBumpBMP (this,
			true).entrySet()) {
			bkop._mmmCreditTenorDelta = new java.util.HashMap<java.lang.String,
				java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>();

			for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
				org.drip.param.definition.BasketMarketParams>> meCreditTenorBMP : mpc.getCreditTenorBumpBMP
					(this, true).entrySet()) {
				if (null == meCreditTenorBMP || null == meCreditTenorBMP.getValue() || null ==
					meCreditTenorBMP.getValue().entrySet())
					continue;

				java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
					mmTenorDelta = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
						java.lang.Double>>();

				for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
					meTenorBMP : meCreditTenorBMP.getValue().entrySet()) {
					if (null == meTenorBMP || null == meTenorBMP.getValue()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
						pricerParams, meTenorBMP.getValue(), quotingParams);

					if (null == mapCalc || null == mapCalc.entrySet()) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapDelta = new
						java.util.HashMap<java.lang.String, java.lang.Double>();

					for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapCalc.entrySet()) {
						if (null == me || null == me.getKey()) continue;

						mapDelta.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()));
					}

					mmTenorDelta.put (meTenorBMP.getKey(), mapDelta);
				}

				bkop._mmmCreditTenorDelta.put (meCreditTenorBMP.getKey(), mmTenorDelta);
			}

			if (null != mpc.getCreditTenorBumpBMP (this, false) && null != mpc.getCreditTenorBumpBMP (this,
				false).entrySet()) {
				bkop._mmmCreditTenorGamma = new java.util.HashMap<java.lang.String,
					java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>();

				for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
					org.drip.param.definition.BasketMarketParams>> meCreditTenorBMP :
						mpc.getCreditTenorBumpBMP (this, false).entrySet()) {
					java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
						mmTenorGamma = new java.util.HashMap<java.lang.String,
							java.util.Map<java.lang.String, java.lang.Double>>();

					if (null == meCreditTenorBMP || null == meCreditTenorBMP.getValue() || null ==
						meCreditTenorBMP.getValue().entrySet())
						continue;

					for (java.util.Map.Entry<java.lang.String, org.drip.param.definition.BasketMarketParams>
						meTenorBMP : meCreditTenorBMP.getValue().entrySet()) {
						if (null == meTenorBMP || null == meTenorBMP.getValue()) continue;

						java.util.Map<java.lang.String, java.lang.Double> mapCalc = value (valParams,
							pricerParams, meTenorBMP.getValue(), quotingParams);

						if (null == mapCalc || null == mapCalc.entrySet()) continue;

						java.util.Map<java.lang.String, java.lang.Double> mapGamma = new
							java.util.HashMap<java.lang.String, java.lang.Double>();

						for (java.util.Map.Entry<java.lang.String, java.lang.Double> me :
							mapCalc.entrySet()) {
							if (null == me || null == me.getKey()) continue;

							mapGamma.put (me.getKey(), me.getValue() - bkop._mBase.get (me.getKey()) +
								bkop._mmmCreditTenorDelta.get (meCreditTenorBMP.getKey()).get
									(meTenorBMP.getKey()).get (me.getKey()));
						}

						mmTenorGamma.put (meTenorBMP.getKey(), mapGamma);
					}

					bkop._mmmCreditTenorGamma.put (meCreditTenorBMP.getKey(), mmTenorGamma);
				}
			}
		}

		bkop._dblCalcTime = (System.nanoTime() - lStart) * 1.e-09;

		return bkop;
	}

	public java.util.Map<java.lang.String, java.lang.Double> calcCustomScenarioMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.MarketParams mpc,
		final java.lang.String strCustomScenName,
		final org.drip.param.valuation.QuotingParams quotingParams,
		java.util.Map<java.lang.String, java.lang.Double> mapBase)
	{
		if (null == valParams || null == mpc) return null;

		if (null == mapBase && null == mpc.getScenBMP (this, "Base")) return null;

		if (null == mapBase) {
			org.drip.param.definition.BasketMarketParams bmp = mpc.getScenBMP (this, "Base");

			if (null == bmp || null == (mapBase = value (valParams, pricerParams, bmp, quotingParams)))
				return null;
		}

		org.drip.param.definition.BasketMarketParams bmpScen = mpc.getScenBMP (this, strCustomScenName);

		if (null == bmpScen) return null;

		java.util.Map<java.lang.String, java.lang.Double> mapScenMeasures = value (valParams, pricerParams,
			bmpScen, quotingParams);

		if (null == mapScenMeasures || null != mapScenMeasures.entrySet()) return null;

		java.util.Map<java.lang.String, java.lang.Double> mapOP = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapScenMeasures.entrySet()) {
			if (null == me || null == me.getKey()) continue;

			mapOP.put (me.getKey(), me.getValue() - mapBase.get (me.getKey()));
		}

		return mapOP;
	}
}
