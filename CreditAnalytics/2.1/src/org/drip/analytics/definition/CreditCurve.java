
package org.drip.analytics.definition;

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
 * This class contains the baseline abstract credit curve holder object. It provides the stub functionality
 * 		for accessing the hazard rates, the calibration instruments, the calibration measures, calibration
 * 		quotes, and parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class CreditCurve extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {

	/**
	 * Sets the calibration inputs for the CreditCurve
	 * 
	 * @param valParam ValuationParams
	 * @param bFlat Flat calibration desired (True)
	 * @param dc Base Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param pricerParam PricerParams
	 * @param aCalibInst Array of calibration instruments
	 * @param adblCalibQuote Array of calibration quotes
	 * @param astrCalibMeasure Array of calibration measures
	 * @param mmFixing Fixings object
	 * @param quotingParams Quoting Parameters
	 */

	public abstract void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final boolean bFlat,
		final org.drip.analytics.definition.DiscountCurve dc,
		final org.drip.analytics.definition.DiscountCurve dcTSY,
		final org.drip.analytics.definition.DiscountCurve dcEDSF,
		final org.drip.param.pricer.PricerParams pricerParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.util.Map<java.lang.String,
			java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams);

	/**
	 * Creates a parallel shifted hazard curve
	 * 
	 * @param dblShift Shift amount
	 * 
	 * @return New CreditCurve instance
	 */

	public abstract CreditCurve createParallelHazardShiftedCurve (
		final double dblShift);

	/**
	 * Creates a flat hazard curve from the inputs
	 * 
	 * @param dblFlatNodeValue Flat hazard node value
	 * @param bSingleNode Uses a single node for Calibration (True)
	 * @param dblRecovery (Optional) Recovery to be used in creation of the flat curve
	 * 
	 * @return New CreditCurve instance
	 */

	public abstract CreditCurve createFlatCurve (
		final double dblFlatNodeValue,
		final boolean bSingleNode,
		final double dblRecovery);

	/**
	 * Sets the Specific Default Date
	 * 
	 * @param dblSpecificDefaultDate Date of Specific Default
	 * 
	 * @return TRUE if successful
	 */

	public abstract boolean setSpecificDefault (
		final double dblSpecificDefaultDate);

	/**
	 * Removes the Specific Default Date
	 * 
	 * @return TRUE if successful
	 */

	public abstract boolean unsetSpecificDefault();

	/**
	 * Calculates the survival to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getSurvival (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Calculates the survival to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getSurvival (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception;

	/**
	 * Calculates the survival to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getSurvival (
		final java.lang.String strTenor)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted survival between a pair of 2 dates
	 * 
	 * @param dblDate1 First Date
	 * @param dblDate2 Second Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getEffectiveSurvival (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted survival between a pair of 2 dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getEffectiveSurvival (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted survival between a pair of 2 tenors
	 * 
	 * @param strTenor1 First tenor
	 * @param strTenor2 Second tenor
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getEffectiveSurvival (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception;

	/**
	 * Calculates the hazard rate between a pair of forward dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public abstract double calcHazard (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception;

	/**
	 * Calculates the hazard rate to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public abstract double calcHazard (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception;

	/**
	 * Calculates the hazard rate to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public abstract double calcHazard (
		final java.lang.String strTenor)
		throws java.lang.Exception;

	/**
	 * Calculates the recovery rate to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public abstract double getRecovery (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Calculates the recovery rate to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public abstract double getRecovery (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception;

	/**
	 * Calculates the recovery rate to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public abstract double getRecovery (
		final java.lang.String strTenor)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted recovery between a pair of dates
	 * 
	 * @param dblDate1 First Date
	 * @param dblDate2 Second Date
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public abstract double getEffectiveRecovery (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted recovery between a pair of dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public abstract double getEffectiveRecovery (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception;

	/**
	 * Calculates the time-weighted recovery between a pair of tenors
	 * 
	 * @param strTenor1 First Tenor
	 * @param strTenor2 Second Tenor
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public abstract double getEffectiveRecovery (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception;
}
