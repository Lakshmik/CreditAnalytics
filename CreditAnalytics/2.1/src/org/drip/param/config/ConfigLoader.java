
package org.drip.param.config;

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
 * This class implements the configuration initialization functionality. It parses the XML configuration file
 * 		and extracts the information, tag pairs – such as holiday sets for different locations, logger
 * 		location, analytics server connection strings, database server connection strings.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ConfigLoader {
	private static final int GetIntTagValue (
		final org.w3c.dom.Element eTag,
		final java.lang.String strTag)
		throws java.lang.Exception {
		if (null == eTag || null == strTag || null == eTag.getElementsByTagName (strTag))
			throw new java.lang.Exception ("Cannot get int value for <" + strTag + ">");

		org.w3c.dom.NodeList nl = eTag.getElementsByTagName (strTag);

		if (null == nl.item (0) || !(nl.item (0) instanceof org.w3c.dom.Element))
			throw new java.lang.Exception ("Cannot get int value for <" + strTag + ">");

		org.w3c.dom.Element elem = (org.w3c.dom.Element) nl.item (0);

		if (null == elem || null == elem.getChildNodes() || null == elem.getChildNodes().item (0) ||
			!(elem.getChildNodes().item (0) instanceof org.w3c.dom.Node))
			throw new java.lang.Exception ("Cannot get int value for <" + strTag + ">");

		org.w3c.dom.Node node = elem.getChildNodes().item (0);

		if (null == node || null == node.getNodeValue())
			throw new java.lang.Exception ("Cannot get int value for <" + strTag + ">");

		return new java.lang.Integer (node.getNodeValue()).intValue();
	}

	private static final boolean GetBooleanTagValue (
		final org.w3c.dom.Element eTag,
		final java.lang.String strTag)
		throws java.lang.Exception
	{
		if (null == eTag || null == strTag || null == eTag.getElementsByTagName (strTag))
			throw new java.lang.Exception ("Cannot get bool value for <" + strTag + ">");

		org.w3c.dom.NodeList nl = eTag.getElementsByTagName (strTag);

		if (null == nl.item (0) || !(nl.item (0) instanceof org.w3c.dom.Element))
			throw new java.lang.Exception ("Cannot get bool value for <" + strTag + ">");

		org.w3c.dom.Element elem = (org.w3c.dom.Element) nl.item (0);

		if (null == elem || null == elem.getChildNodes() || null == elem.getChildNodes().item (0) ||
			!(elem.getChildNodes().item (0) instanceof org.w3c.dom.Node))
			throw new java.lang.Exception ("Cannot get bool value for <" + strTag + ">");

		org.w3c.dom.Node node = elem.getChildNodes().item (0);

		if (null == node || null == node.getNodeValue())
			throw new java.lang.Exception ("Cannot get bool value for <" + strTag + ">");

		return new java.lang.Boolean (node.getNodeValue()).booleanValue();
	}

	private static final java.lang.String GetStringTagValue (
		final org.w3c.dom.Element eTag,
		final java.lang.String strTag)
	{
		if (null == eTag || null == strTag || null == eTag.getElementsByTagName (strTag)) return null;

		org.w3c.dom.NodeList nl = eTag.getElementsByTagName (strTag);

		if (null == nl.item (0) || !(nl.item (0) instanceof org.w3c.dom.Element)) return null;

		org.w3c.dom.Element elem = (org.w3c.dom.Element) nl.item (0);

		if (null == elem || null == elem.getChildNodes() || null == elem.getChildNodes().item (0) ||
			!(elem.getChildNodes().item (0) instanceof org.w3c.dom.Node))
			return null;

		org.w3c.dom.Node node = elem.getChildNodes().item (0);

		if (null == node || null == node.getNodeValue()) return null;

		return node.getNodeValue();
	}

	private static final int[] GetIntArrayTagValue (
		final org.w3c.dom.Element eTag,
		final java.lang.String strTag)
	{
		if (null == eTag || null == strTag || null == eTag.getElementsByTagName (strTag)) return null;

		org.w3c.dom.NodeList nl = eTag.getElementsByTagName (strTag);

		if (!(nl.item (0) instanceof org.w3c.dom.Element)) return null;

		org.w3c.dom.Element elem = (org.w3c.dom.Element) nl.item (0);

		if (null == elem || null == elem.getChildNodes() || null == elem.getChildNodes().item (0) ||
			!(elem.getChildNodes().item (0) instanceof org.w3c.dom.Node))
			return null;

		java.lang.String strValue = elem.getChildNodes().item (0).getNodeValue();

		if (null == strValue || strValue.isEmpty()) return null;

		java.lang.String[] astrValue = strValue.split (",");

		int[] ai = new int[astrValue.length];

		for (int i = 0; i < astrValue.length; ++i)
			ai[i] = new java.lang.Integer (astrValue[i]).intValue();

		return ai;
	}

	private static final org.w3c.dom.Document GetNormalizedXMLDoc (
		final java.lang.String strXMLFile)
	{
		if (null == strXMLFile || strXMLFile.isEmpty()) return null;

		org.w3c.dom.Document doc = null;

		try {
			doc = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse (new
				java.io.File (strXMLFile));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (null == doc || null == doc.getDocumentElement()) return null;

		doc.getDocumentElement().normalize();

		return doc;
	}

	/**
	 * Creates a LocHolidays object from the XML Document and the Location Tag
	 * 
	 * @param doc XML Document
	 * @param strLoc Location Tag
	 * 
	 * @return LocHolidays
	 */

	public static org.drip.analytics.holiday.Locale GetLocHolidays (
		final org.w3c.dom.Document doc,
		final java.lang.String strLoc)
	{
		if (null == doc || null == strLoc) return null;

		org.w3c.dom.NodeList nlLoc = doc.getElementsByTagName (strLoc);

		if (null == nlLoc || null == nlLoc.item (0) || org.w3c.dom.Node.ELEMENT_NODE != nlLoc.item
			(0).getNodeType())
			return null;

		org.drip.analytics.holiday.Locale locHols = new org.drip.analytics.holiday.Locale();

		org.w3c.dom.Element e = (org.w3c.dom.Element) nlLoc.item (0);

		org.w3c.dom.NodeList nlHols = e.getElementsByTagName ("Weekend");

		if (null != nlHols && null != nlHols.item (0) && org.w3c.dom.Node.ELEMENT_NODE == nlHols.item
			(0).getNodeType())
			locHols.addWeekend (GetIntArrayTagValue ((org.w3c.dom.Element) nlHols.item (0), "DaysInWeek"));

		if (null != (nlHols = e.getElementsByTagName ("FixedHoliday"))) {
			for (int j = 0; j < nlHols.getLength(); ++j) {
				if (null == nlHols.item (j) || org.w3c.dom.Node.ELEMENT_NODE != nlHols.item
					(j).getNodeType())
					continue;

				org.w3c.dom.Element elemHol = (org.w3c.dom.Element) nlHols.item (j);

				if (null != elemHol) {
					try {
						locHols.addFixedHoliday (GetIntTagValue (elemHol, "Date"), GetIntTagValue (elemHol,
							"Month"), "");
					} catch (java.lang.Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}	

		if (null != (nlHols = e.getElementsByTagName ("FloatingHoliday"))) {
			for (int j = 0; j < nlHols.getLength(); ++j) {
				if (null == nlHols.item (j) || org.w3c.dom.Node.ELEMENT_NODE != nlHols.item
					(j).getNodeType())
					continue;

				org.w3c.dom.Element elemHol = (org.w3c.dom.Element) nlHols.item (j);

				if (null != elemHol) {
					try {
						locHols.addFloatingHoliday (GetIntTagValue (elemHol, "WeekInMonth"), GetIntTagValue
							(elemHol, "WeekDay"), GetIntTagValue (elemHol, "Month"), GetBooleanTagValue
								(elemHol, "FromFront"), "");
					} catch (java.lang.Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		return locHols;
	}

	/**
	 * Gets the logger location from the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return String representing the logger location's full path
	 */

	public static java.lang.String GetLoggerLocation (
		final java.lang.String strConfigFile)
	{
		org.w3c.dom.Document doc = GetNormalizedXMLDoc (strConfigFile);

		if (null == doc) return null;

		org.w3c.dom.NodeList nlLogger = doc.getElementsByTagName ("logger");

		if (null == nlLogger || null == nlLogger.item (0) || org.w3c.dom.Node.ELEMENT_NODE !=
			nlLogger.item (0).getNodeType())
			return null;

		return GetStringTagValue ((org.w3c.dom.Element) nlLogger.item (0), "Location");
	}

	/**
	 * Connects to the analytics server from the connection parameters set in the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return java.net.Socket
	 */

	public static java.net.Socket ConnectToAnalServer (
		final java.lang.String strConfigFile)
	{
		org.w3c.dom.Document doc = GetNormalizedXMLDoc (strConfigFile);

		if (null == doc) return null;

		org.w3c.dom.NodeList nlLogger = doc.getElementsByTagName ("analserver");

		if (null == nlLogger || null == nlLogger.item (0) || org.w3c.dom.Node.ELEMENT_NODE !=
			nlLogger.item (0).getNodeType())
			return null;

		try {
			return new java.net.Socket (GetStringTagValue ((org.w3c.dom.Element) nlLogger.item (0),
				"host"), GetIntTagValue ((org.w3c.dom.Element) nlLogger.item (0), "port"));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Initialize the analytics server from the connection parameters set in the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return java.net.ServerSocket
	 */

	public static java.net.ServerSocket InitAnalServer (
		final java.lang.String strConfigFile)
	{
		org.w3c.dom.Document doc = GetNormalizedXMLDoc (strConfigFile);

		if (null == doc) return null;

		org.w3c.dom.NodeList nlLogger = doc.getElementsByTagName ("analserver");

		if (null == nlLogger || null == nlLogger.item (0) || org.w3c.dom.Node.ELEMENT_NODE !=
			nlLogger.item (0).getNodeType())
			return null;

		try {
			return new java.net.ServerSocket (GetIntTagValue ((org.w3c.dom.Element) nlLogger.item (0),
				"port"), GetIntTagValue ((org.w3c.dom.Element) nlLogger.item (0), "maxconn"));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads the map of the holiday calendars from the entries set in the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return Map of the holiday calendars
	 */

	public static java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale>
		LoadHolidayCalendars (
			final java.lang.String strConfigFile)
		{
		org.w3c.dom.Document doc = GetNormalizedXMLDoc (strConfigFile);

		if (null == doc) return null;

		org.drip.analytics.holiday.Locale lhNYB = GetLocHolidays (doc, "NYB");

		if (null == lhNYB) return null;

		java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> mapHols = new
			java.util.HashMap<java.lang.String, org.drip.analytics.holiday.Locale>();

		mapHols.put ("NYB", lhNYB);

		return mapHols;
	}

	/**
	 * Initialize the Oracle database from the connection parameters set in the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return Connection Statement object
	 */

	public static java.sql.Statement OracleInit (
		final java.lang.String strConfigFile)
	{
		org.w3c.dom.Document doc = GetNormalizedXMLDoc (strConfigFile);

		if (null == doc) return null;

		org.w3c.dom.NodeList nlDBConn = doc.getElementsByTagName ("dbconn");

		if (null == nlDBConn || null == nlDBConn.item (0) || org.w3c.dom.Node.ELEMENT_NODE !=
			nlDBConn.item (0).getNodeType())
			return null;

		org.w3c.dom.Element elemDBConn = (org.w3c.dom.Element) nlDBConn.item (0);

		try {
			java.lang.Class.forName ("oracle.jdbc.driver.OracleDriver");

			java.lang.String strURL = "jdbc:oracle:thin:@//" + GetStringTagValue (elemDBConn, "host") + ":" +
				GetStringTagValue (elemDBConn, "port") + "/" + GetStringTagValue (elemDBConn, "dbname");

			// java.lang.String strURL = "jdbc:oracle:thin:@//localhost:1521/XE";

			System.out.println ("URL: " + strURL);

			java.sql.Connection conn = java.sql.DriverManager.getConnection (strURL, "hr", "hr");

			System.out.println ("Conn: " + conn);

			conn.setAutoCommit (false);

			return conn.createStatement();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads the map of the holiday calendars from the database settings set in the XML Configuration file
	 * 
	 * @param strConfigFile XML Configuration file
	 * 
	 * @return Map of the holiday calendars
	 */

	public static final java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale>
		LoadHolidayCalendarsFromDB (
			final java.lang.String strConfigFile)
	{
		java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> mapHols = new
			java.util.HashMap<java.lang.String, org.drip.analytics.holiday.Locale>();

		java.sql.Statement stmt = OracleInit (strConfigFile);

		if (null == stmt) return null;

		long lStart = System.nanoTime();

		try {
			java.sql.ResultSet rs = stmt.executeQuery ("SELECT Location, Holiday FROM Holidays");

			while (null != rs && rs.next()) {
				java.lang.String strLocation = rs.getString ("Location");

				java.util.Date dtSQLHoliday = rs.getDate ("Holiday");

				if (null != dtSQLHoliday) {
					org.drip.analytics.holiday.Locale lh = mapHols.get (strLocation);

					if (null == lh) lh = new org.drip.analytics.holiday.Locale();

					lh.addStaticHoliday (org.drip.analytics.date.JulianDate.CreateFromYMD
						(org.drip.analytics.support.GenericUtil.GetYear (dtSQLHoliday),
							org.drip.analytics.support.GenericUtil.GetMonth (dtSQLHoliday),
								org.drip.analytics.support.GenericUtil.GetYear (dtSQLHoliday)), "");

					mapHols.put (strLocation, lh);
				}
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		int[] aiWeekend = new int[2];
		aiWeekend[1] = org.drip.analytics.date.JulianDate.SUNDAY;
		aiWeekend[0] = org.drip.analytics.date.JulianDate.SATURDAY;

		for (java.util.Map.Entry<java.lang.String, org.drip.analytics.holiday.Locale> me :
			mapHols.entrySet())
			me.getValue().addWeekend (aiWeekend);

		System.out.println ("Loading hols from DB took " + (System.nanoTime() - lStart) * 1.e-06 +
			" m-sec\n");

		return mapHols;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		java.lang.String strConfigFile = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		java.util.Map<java.lang.String, org.drip.analytics.holiday.Locale> mapHols =
			LoadHolidayCalendars (strConfigFile);

		for (java.util.Map.Entry<java.lang.String, org.drip.analytics.holiday.Locale> me :
			mapHols.entrySet())
			System.out.println (me.getKey() + "=" + me.getValue());

		System.out.println ("Logger: " + GetLoggerLocation (strConfigFile));
	}
}
