
package org.drip.analytics.support;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.CreditAnalycredit-tradertics.org
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
 *  The Logger class implements level-set logging, backed by either the screen or a file. Logging always
 * 	includes time-stamps, and happens according to the level requested.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Logger {
	/**
	 * Logger level ERROR
	 */

	public static final int ERROR = 0;

	/**
	 * Logger level WARNING
	 */

	public static final int WARNING = 1;

	/**
	 * Logger level INFO
	 */

	public static final int INFO = 2;

	/**
	 * Logger level DEBUG
	 */

	public static final int DEBUG = 4;

	private static boolean _sbInit = false;
	private static java.io.BufferedWriter _writeLog = null;

	private static final java.lang.String ImprintPreSub (
		final int iLevel,
		final java.lang.String strMsg)
	{
		if (null == strMsg || strMsg.isEmpty()) return "";

		java.lang.StringBuilder sb = new java.lang.StringBuilder();

		sb.append ("[").append ((new java.util.Date()).toString()).append ("|Level:").append (iLevel).append
			("|").append (strMsg).append ("]\n");

		return sb.toString();
	}

	/**
	 * Initializes the logger from a configuration file
	 * 
	 * @param strConfigFile Configuration file containing the logger file location
	 * 
	 * @return boolean indicating whether initialization succeeded
	 */

	public static boolean Init (
			final java.lang.String strConfigFile)
	{
		if (null == strConfigFile || strConfigFile.isEmpty()) return false;

		try {
			_writeLog = new java.io.BufferedWriter (new java.io.FileWriter
				(org.drip.param.config.ConfigLoader.GetLoggerLocation (strConfigFile)));

			return _sbInit = true;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return _sbInit = false;
	}

	/**
	 * Logs a specific message to the level
	 * 
	 * @param iLevel the level of message (ERROR/WARNING/INFO/DEBUG)
	 * @param bHardLog whether the logging is to file/DB (true) or to screen (false)
	 * @param strMsg Message to be logged
	 * 
	 * @return boolean indicating whether logging operation succeeded
	 */

	public static boolean Log (
		final int iLevel,
		final boolean bHardLog,
		final java.lang.String strMsg)
	{
		if (!_sbInit || null == strMsg || strMsg.isEmpty()) return false;

		if (bHardLog) {
			try {
				_writeLog.write (ImprintPreSub (iLevel, strMsg));

				return true;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		System.out.println (ImprintPreSub (iLevel, strMsg));

		return true;
	}
}
