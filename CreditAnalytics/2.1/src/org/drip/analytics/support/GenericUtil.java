
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
 *  The GenericUtil class implements generic utility functions used in DRIP modules.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class GenericUtil {
	/**
	 * Prefix the keys in the input map, and return them in a new map
	 * 
	 * @param mapIn Input map
	 * @param strPrefix The prefix
	 * 
	 * @return Map containing the prefixed entries
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> PrefixKeys (
		final java.util.Map<java.lang.String, java.lang.Double> mapIn,
		final java.lang.String strPrefix)
	{
		if (null == mapIn || null == mapIn.entrySet() || null == strPrefix || strPrefix.isEmpty())
			return null;

		java.util.Map<java.lang.String, java.lang.Double> mapOut = new java.util.TreeMap<java.lang.String,
			java.lang.Double>();

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapIn.entrySet()) {
			if (null != me.getKey() && !me.getKey().isEmpty())
				mapOut.put (strPrefix + me.getKey(), me.getValue());
		}

		return mapOut;
	}

	/**
	 * Merge two measure maps
	 * 
	 * @param map1 Measure Map 1
	 * @param map2 Measure Map 2
	 * 
	 * @return The merged measure map
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> MergeMeasureMaps (
		final java.util.Map<java.lang.String, java.lang.Double> map1,
		final java.util.Map<java.lang.String, java.lang.Double> map2)
	{
		if (null == map1 && null == map2) return null;

		if (null == map1 && null != map2) return map2;

		if (null != map1 && null == map2) return map1;

		java.util.Map<java.lang.String, java.lang.Double> mapOut = new java.util.TreeMap<java.lang.String,
			java.lang.Double>();

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : map1.entrySet())
			mapOut.put (me.getKey(), me.getValue());

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : map2.entrySet())
			mapOut.put (me.getKey(), me.getValue());

		return mapOut;
	}

	/**
	 * Merge the secondary map onto the main measure map
	 * 
	 * @param mapMain Main Measure Map
	 * @param mapToAdd Secondary Measure Map to Add
	 * 
	 * @return True => If successfully merged with main
	 */

	public static final boolean MergeWithMain (
		final java.util.Map<java.lang.String, java.lang.Double> mapMain,
		final java.util.Map<java.lang.String, java.lang.Double> mapToAdd)
	{
		if (null == mapMain || null == mapMain.entrySet() || null == mapToAdd || null ==
			mapToAdd.entrySet())
			return false;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapToAdd.entrySet())
			mapMain.put (me.getKey(), me.getValue());

		return true;
	}

	/**
	 * Looks for a match of the file in the input array
	 * 
	 * @param strFieldToMatch Field To Match
	 * @param astrMatchSet Array of fields to compare with
	 * @param bCaseMatch True => Check for Matching case
	 * 
	 * @return True => Match found according to the criteria specified
	 */

	public static final boolean MatchInStringArray (
		final java.lang.String strFieldToMatch,
		final java.lang.String[] astrMatchSet,
		final boolean bCaseMatch)
	{
		if (null == strFieldToMatch || strFieldToMatch.isEmpty() || null == astrMatchSet || 0 ==
			astrMatchSet.length)
			return false;

		for (java.lang.String strMatchSetEntry : astrMatchSet) {
			if (null == strMatchSetEntry || strMatchSetEntry.isEmpty()) continue;

			if (strMatchSetEntry.equals (strFieldToMatch)) return true;

			if (!bCaseMatch && strMatchSetEntry.equalsIgnoreCase (strFieldToMatch)) return true;
		}

		return false;
	}

	/**
	 * Returns the date corresponding to the input java.util.Date
	 * 
	 * @param dt java.util.Date Input
	 * 
	 * @return Date
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final int GetDate (
		final java.util.Date dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("Invalid date in GenericUtil.GetDate");

		java.util.Calendar cal = java.util.Calendar.getInstance();

		cal.setTime (dt);

		return cal.get (java.util.Calendar.DATE);
	}

	/**
	 * Returns the month corresponding to the input java.util.Date. 1 => January, and 12 => December
	 * 
	 * @param dt java.util.Date Input
	 * 
	 * @return Month
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final int GetMonth (
		final java.util.Date dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("Invalid date in GenericUtil.GetMonth");

		java.util.Calendar cal = java.util.Calendar.getInstance();

		cal.setTime (dt);

		return cal.get (java.util.Calendar.MONTH) + 1;
	}

	/**
	 * Returns the year corresponding to the input java.util.Date.
	 * 
	 * @param dt java.util.Date Input
	 * 
	 * @return Year
	 * 
	 * @throws java.lang.Exception Thrown if input date is invalid
	 */

	public static final int GetYear (
		final java.util.Date dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("Invalid date in GenericUtil.GetYear");

		java.util.Calendar cal = java.util.Calendar.getInstance();

		cal.setTime (dt);

		return cal.get (java.util.Calendar.YEAR);
	}

	/**
	 * Formats the given string parameter into an argument
	 * 
	 * @param strArg String Argument
	 * 
	 * @return Parameter from the Argument
	 */

	public static final java.lang.String MakeStringArg (
		final java.lang.String strArg)
	{
		if (null == strArg) return "null";

		if (strArg.isEmpty()) return "\"\"";

		return "\"" + strArg.trim() + "\"";
	}

	/**
	 * Process the Input String to Check for NUll - and return
	 * 
	 * @param strIn Input String
	 * @param bEmptyToNULL TRUE if Empty String needs to be converted to NULL
	 * 
	 * @return The Processed String
	 */

	public static final java.lang.String ProcessInputForNULL (
		final java.lang.String strIn,
		final boolean bEmptyToNULL)
	{
		if (null == strIn) return null;

		if (strIn.isEmpty()) return bEmptyToNULL ? null : "";

		if ("null".equalsIgnoreCase (strIn.trim())) return null;

		if (strIn.trim().toUpperCase().startsWith ("NO")) return null;

		return strIn;
	}

	/**
	 * Parses and splits the input phrase into a string array using the specified delimiter
	 * 
	 * @param strPhrase Phrase input
	 * 
	 * @param strDelim Delimiter
	 * 
	 * @return Array of substrings
	 */

	public static final java.lang.String[] Split (
		final java.lang.String strPhrase,
		final java.lang.String strDelim)
	{
		if (null == strPhrase || strPhrase.isEmpty() || null == strDelim || strDelim.isEmpty()) return null;

		java.util.List<java.lang.String> lsstr = new java.util.ArrayList<java.lang.String>();

		java.util.StringTokenizer stCodeTokens = new java.util.StringTokenizer (strPhrase, strDelim);

		while (stCodeTokens.hasMoreTokens())
			lsstr.add (stCodeTokens.nextToken());

		if (0 == lsstr.size()) return null;

		java.lang.String[] astr = new java.lang.String[lsstr.size()];

		int i = 0;

		for (java.lang.String str : lsstr)
			astr[i++] = str;

		return astr;
	}

	/**
	 * Parses the string and returns the result as a boolean
	 * 
	 * @param strUnitaryBoolean String input
	 * 
	 * @return Boolean output
	 */

	public static final boolean ParseFromUnitaryString (
		final java.lang.String strUnitaryBoolean)
	{
		if (null == strUnitaryBoolean || strUnitaryBoolean.isEmpty() || !"1".equalsIgnoreCase
			(strUnitaryBoolean))
			return false;

		return true;
	}

	/**
	 * Creates an Oracle date trigram from a YYYYMMDD string
	 * 
	 * @param strYYYYMMDD Date string in the YYYYMMDD format.
	 * 
	 * @return Oracle date trigram string
	 */

	public static java.lang.String MakeOracleDateFromYYYYMMDD (
		final java.lang.String strYYYYMMDD)
	{
		if (null == strYYYYMMDD || strYYYYMMDD.isEmpty()) return null;

		try {
			return strYYYYMMDD.substring (6) + "-" + org.drip.analytics.date.JulianDate.getMonthOracleChar
				((new java.lang.Integer (strYYYYMMDD.substring (4, 6))).intValue()) + "-" +
					strYYYYMMDD.substring (0, 4);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an Oracle date trigram from a Bloomberg date string
	 * 
	 * @param strBBGDate Bloomberg date string
	 * 
	 * @return Oracle date trigram string
	 */

	public static java.lang.String MakeOracleDateFromBBGDate (
		final java.lang.String strBBGDate)
	{
		if (null == strBBGDate || strBBGDate.isEmpty()) return null;

		java.util.StringTokenizer st = new java.util.StringTokenizer (strBBGDate, "/");

		try {
			java.lang.String strMonth = org.drip.analytics.date.JulianDate.getMonthOracleChar ((new
				java.lang.Integer (st.nextToken())).intValue());

			if (null == strMonth) return null;

			return st.nextToken() + "-" + strMonth + "-" + st.nextToken();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Makes an array of double from a string tokenizer
	 * 
	 * @param stdbl Tokenizer containing delimited doubles
	 *  
	 * @return Double array
	 */

	public static final double[] MakeDoubleArrayFromStringTokenizer (
		final java.util.StringTokenizer stdbl)
	{
		if (null == stdbl) return null;

		java.util.List<java.lang.Double> lsdbl = new java.util.ArrayList<java.lang.Double>();

		while (stdbl.hasMoreTokens())
			lsdbl.add (new java.lang.Double (stdbl.nextToken()));

		if (0 == lsdbl.size()) return null;

		double[] adbl = new double[lsdbl.size()];

		int i = 0;

		for (double dbl : lsdbl)
			adbl[i++] = dbl;

		return adbl;
	}

	/**
	 * Generates a GUID string
	 * 
	 * @return String representing the GUID
	 */

	public static final java.lang.String GUID() {
	    return java.util.UUID.randomUUID().toString();
	}

	/**
	 * Spits the string array into pairs of key-value doubles and returns them
	 * 
	 * @param lsdblKey [out] List of Keys
	 * @param lsdblValue [out] List of Values
	 * @param strArray [in] String containing KV records
	 * @param strRecordDelim [in] Record Delimiter
	 * @param strKVDelim [in] Key-Value Delimiter
	 * 
	 * @return True if parsing is successful
	 */

	public static final boolean KeyValueListFromStringArray (
		final java.util.List<java.lang.Double> lsdblKey,
		final java.util.List<java.lang.Double> lsdblValue,
		final java.lang.String strArray,
		final java.lang.String strRecordDelim,
		final java.lang.String strKVDelim)
	{
		if (null == strArray || strArray.isEmpty() || null == strRecordDelim || strRecordDelim.isEmpty() ||
			null == strKVDelim || strKVDelim.isEmpty() || null == lsdblKey || null == lsdblValue)
			return false;

		java.lang.String[] astr = org.drip.analytics.support.GenericUtil.Split (strArray, strRecordDelim);

		if (null == astr || 0 == astr.length) return false;

		for (int i = 0; i < astr.length; ++i) {
			if (null == astr[i] || astr[i].isEmpty()) return false;

			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astr[i],
				strKVDelim);

			if (null == astrRecord || 2 != astrRecord.length || null == astrRecord[0] ||
				astrRecord[0].isEmpty() || null == astrRecord[1] || astrRecord[1].isEmpty())
				return false;

			lsdblKey.add (new java.lang.Double (astrRecord[0]).doubleValue());

			lsdblValue.add (new java.lang.Double (astrRecord[1]).doubleValue());
		}

		return true;
	}

	/**
	 * Create a list of integers from a delimited string
	 * 
	 * @param lsi [Output] List of Integers
	 * @param strList Delimited String input
	 * @param strDelim Delimiter
	 * 
	 * @return True if successful
	 */

	public static final boolean IntegerListFromString (
		final java.util.List<java.lang.Integer> lsi,
		final java.lang.String strList, final java.lang.String strDelim)
	{
		if (null == lsi || null == strList || strList.isEmpty() || null == strDelim || strDelim.isEmpty())
			return false;

		java.lang.String[] astr = org.drip.analytics.support.GenericUtil.Split (strList, strDelim);

		if (null == astr || 0 == astr.length) return false;

		for (int i = 0; i < astr.length; ++i) {
			if (null == astr[i] || astr[i].isEmpty()) continue;

			lsi.add (new java.lang.Integer (astr[i]).intValue());
		}

		return true;
	}

	/**
	 * Create a list of booleans from a delimited string
	 * 
	 * @param lsb [Output] List of Booleans
	 * @param strList Delimited String input
	 * @param strDelim Delimiter
	 * 
	 * @return True if successful
	 */

	public static final boolean BooleanListFromString (
		final java.util.List<java.lang.Boolean> lsb,
		final java.lang.String strList,
		final java.lang.String strDelim)
	{
		if (null == lsb || null == strList || strList.isEmpty() || null == strDelim || strDelim.isEmpty())
			return false;

		java.lang.String[] astr = org.drip.analytics.support.GenericUtil.Split (strList, strDelim);

		if (null == astr || 0 == astr.length) return false;

		for (int i = 0; i < astr.length; ++i) {
			if (null == astr[i] || astr[i].isEmpty()) continue;

			lsb.add (new java.lang.Boolean (astr[i]).booleanValue());
		}

		return true;
	}
	
	/**
	 * Flattens an input 2D string/double map into a delimited string array
	 * 
	 * @param map2DSD 2D String/Double map
	 * @param strKVDelimiter Element delimiter
	 * @param strRecordDelimiter Record delimiter
	 * 
	 * @return Flattened map string
	 */

	public static final java.lang.String TwoDSDMapToFlatString (
		final java.util.Map<java.lang.String, java.lang.Double> map2DSD,
		final java.lang.String strKVDelimiter,
		final java.lang.String strRecordDelimiter)
	{
		if (null == map2DSD || 0 == map2DSD.size() || null == map2DSD.entrySet() || null == strKVDelimiter ||
			strKVDelimiter.isEmpty() || null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return "";

		boolean bFirstEntry = true;

		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : map2DSD.entrySet()) {
			if (null == me || null == me.getKey() || me.getKey().isEmpty()) continue;

			if (bFirstEntry)
				bFirstEntry = false;
			else
				sb.append (strRecordDelimiter);

			sb.append (me.getKey() + strKVDelimiter + me.getValue());
		}

		return sb.toString();
	}

	/**
	 * Flattens a 3D SSD map structure onto a string array
	 * 
	 * @param map3DSD 3D SSD map
	 * @param strMultiLevelKeyDelimiter Multi Level KeyDelimiter
	 * @param strKVDelimiter Key-Value Delimiter
	 * @param strRecordDelimiter Record Delimiter
	 * 
	 * @return Flattened String
	 */

	public static final java.lang.String ThreeDSDMapToFlatString (
		final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3DSD,
		final java.lang.String strMultiLevelKeyDelimiter,
		final java.lang.String strKVDelimiter,
		final java.lang.String strRecordDelimiter) {
		if (null == map3DSD || 0 == map3DSD.size() || null == map3DSD.entrySet() || null ==
			strMultiLevelKeyDelimiter || strMultiLevelKeyDelimiter.isEmpty() || null == strKVDelimiter ||
				strKVDelimiter.isEmpty() || null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return null;

		boolean bFirstEntry = true;

		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> meOut :
			map3DSD.entrySet()) {
			if (null == meOut || null == meOut.getValue() || null == meOut.getValue().entrySet()) continue;

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> meIn : meOut.getValue().entrySet())
			{
				if (null == meIn || null == meIn.getKey() || meIn.getKey().isEmpty()) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sb.append (strRecordDelimiter);

				sb.append (meOut.getKey() + strMultiLevelKeyDelimiter + meIn.getKey() + strKVDelimiter +
					meIn.getValue());
			}
		}

		return sb.toString();
	}

	/**
	 * Flattens a 4D SSSD map structure onto a string array
	 * 
	 * @param map4DSD 4D SSSD map
	 * @param strMultiLevelKeyDelimiter Multi Level KeyDelimiter
	 * @param strKVDelimiter Key-Value Delimiter
	 * @param strRecordDelimiter Record Delimiter
	 * 
	 * @return Flattened String
	 */

	public static final java.lang.String FourDSDMapToFlatString (
		final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
			java.lang.Double>>> map4DSD,
		final java.lang.String strMultiLevelKeyDelimiter,
		final java.lang.String strKVDelimiter,
		final java.lang.String strRecordDelimiter)
	{
		if (null == map4DSD || 0 == map4DSD.size() || null == map4DSD.entrySet() || null ==
			strMultiLevelKeyDelimiter || strMultiLevelKeyDelimiter.isEmpty() || null == strKVDelimiter ||
				strKVDelimiter.isEmpty() || null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return null;

		boolean bFirstEntry = true;

		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String,
			java.util.Map<java.lang.String, java.lang.Double>>> meOut : map4DSD.entrySet()) {
			if (null == meOut || null == meOut.getValue() || null == meOut.getValue().entrySet() || null ==
				meOut.getKey() || meOut.getKey().isEmpty())
				continue;

			for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
				meIn : meOut.getValue().entrySet()) {
				if (null == meIn || null == meIn.getValue() || null == meIn.getValue().entrySet() || null ==
					meIn.getKey() || meIn.getKey().isEmpty())
					continue;

				for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : meIn.getValue().entrySet())
				{
					if (null == me || null == me.getKey() || me.getKey().isEmpty()) continue;

					if (bFirstEntry)
						bFirstEntry = false;
					else
						sb.append (strRecordDelimiter);

					sb.append (meOut.getKey() + strMultiLevelKeyDelimiter + meIn.getKey() +
						strMultiLevelKeyDelimiter + me.getKey() + strKVDelimiter + me.getValue());
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Turns an flattened 2D (string, double) string sequence into its corresponding map
	 * 
	 * @param str2DMap Flattened 2D array input
	 * @param strKVDelimiter Key-Value delimiter string
	 * @param strRecordDelimiter Record delimiter string
	 * @param bSkipNullValue Indicates whether NULL Values are to be skipped
	 * @param strNULLString NULL string
	 * 
	 * @return [String, double] map
	 */

	public static final java.util.Map<java.lang.String, java.lang.Double> FlatStringTo2DSDMap (
		final java.lang.String str2DMap,
		final java.lang.String strKVDelimiter,
		final java.lang.String strRecordDelimiter,
		final boolean bSkipNullValue,
		final java.lang.String strNULLString)
	{
		if (null == str2DMap || str2DMap.isEmpty() || null == strNULLString || strNULLString.isEmpty() ||
			strNULLString.equalsIgnoreCase (str2DMap) || null == strKVDelimiter || strKVDelimiter.isEmpty()
				|| null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return null;

		java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (str2DMap,
			strRecordDelimiter);

		if (null == astrRecord || 0 == astrRecord.length) return null;

		java.util.Map<java.lang.String, java.lang.Double> map2D = new java.util.HashMap<java.lang.String,
			java.lang.Double>();

		for (int i = 0; i < astrRecord.length; ++i) {
			if (null == astrRecord[i] || astrRecord[i].isEmpty() || strNULLString.equalsIgnoreCase
				(astrRecord[i]))
				continue;

			java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split (astrRecord[i],
				strKVDelimiter);
			
			if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
				astrKVPair[0].isEmpty() || strNULLString.equalsIgnoreCase (astrKVPair[0]) || (bSkipNullValue
					&& (null == astrKVPair[1] || astrKVPair[1].isEmpty() || strNULLString.equalsIgnoreCase
						(astrKVPair[1]))))
				continue;

			map2D.put (astrKVPair[0], new java.lang.Double (astrKVPair[1]));
		}

		if (0 == map2D.size()) return null;

		return map2D;
	}

	/**
	 * Turns an flattened 3D (string, string, double) string sequence into its corresponding map
	 * 
	 * @param str3DMap Flattened 3D array input
	 * @param strMultiLevelKeyDelimiter Multi-level key delimiter string
	 * @param strKVDelimiter Key-Value delimiter string
	 * @param strRecordDelimiter Record delimiter string
	 * @param bSkipNullValue Indicates whether NULL Values are to be skipped
	 * @param strNULLString NULL string
	 * 
	 * @return [String, [String, double]] map
	 */

	public static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>
		FlatStringTo3DSDMap (
			final java.lang.String str3DMap,
			final java.lang.String strMultiLevelKeyDelimiter,
			final java.lang.String strKVDelimiter,
			final java.lang.String strRecordDelimiter,
			final boolean bSkipNullValue,
			final java.lang.String strNULLString)
	{
		if (null == str3DMap || str3DMap.isEmpty() || null == strNULLString || strNULLString.isEmpty() ||
			strNULLString.equalsIgnoreCase (str3DMap) || null == strKVDelimiter || strKVDelimiter.isEmpty()
				|| null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return null;

		java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (str3DMap,
			strRecordDelimiter);

		if (null == astrRecord || 0 == astrRecord.length) return null;

		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3D = new
			java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>();

		for (int i = 0; i < astrRecord.length; ++i) {
			if (null == astrRecord[i] || astrRecord[i].isEmpty()) continue;

			java.lang.String[] astrKVPair = org.drip.analytics.support.GenericUtil.Split (astrRecord[i],
				strKVDelimiter);
			
			if (null == astrKVPair || 2 != astrKVPair.length || null == astrKVPair[0] ||
				astrKVPair[0].isEmpty() || strNULLString.equalsIgnoreCase (astrKVPair[0]) || (bSkipNullValue
					&& (null == astrKVPair[1] || astrKVPair[1].isEmpty() || strNULLString.equalsIgnoreCase
						(astrKVPair[1]))))
				continue;

			java.lang.String[] astrKeySet = org.drip.analytics.support.GenericUtil.Split (astrKVPair[0],
				strMultiLevelKeyDelimiter);
			
			if (null == astrKeySet || 2 != astrKeySet.length || null == astrKeySet[0] ||
				astrKeySet[0].isEmpty() || strNULLString.equalsIgnoreCase (astrKeySet[0]) || null ==
					astrKeySet[1] || astrKeySet[1].isEmpty() || strNULLString.equalsIgnoreCase
						(astrKeySet[1]))
				continue;

			java.util.Map<java.lang.String, java.lang.Double> map2D = map3D.get (astrKeySet[0]);

			if (null == map2D) map2D = new java.util.HashMap<java.lang.String, java.lang.Double>();

			map2D.put (astrKeySet[1], new java.lang.Double (astrKVPair[1]));

			map3D.put (astrKeySet[0], map2D);
		}

		if (0 == map3D.size()) return null;

		return map3D;
	}

	/**
	 * Turns an flattened 4D (string, string, string, double) string sequence into its corresponding map
	 * 
	 * @param str4DMap Flattened 4D array input
	 * @param strMultiLevelKeyDelimiter Multi-level key delimiter string
	 * @param strKVDelimiter Key-Value delimiter string
	 * @param strRecordDelimiter Record delimiter string
	 * @param bSkipNullValue Indicates whether NULL Values are to be skipped
	 * @param strNULLString NULL string
	 * 
	 * @return [String, [String, [String, double]]] map
	 */

	public static final java.util.Map<java.lang.String, java.util.Map<java.lang.String,
		java.util.Map<java.lang.String, java.lang.Double>>> FlatStringTo4DSDMap (
			final java.lang.String str4DMap,
			final java.lang.String strMultiLevelKeyDelimiter,
			final java.lang.String strKVDelimiter,
			final java.lang.String strRecordDelimiter,
			final boolean bSkipNullValue,
			final java.lang.String strNULLString)
	{
		if (null == str4DMap || str4DMap.isEmpty() || null == strNULLString || strNULLString.isEmpty() ||
			strNULLString.equalsIgnoreCase (str4DMap) || null == strKVDelimiter || strKVDelimiter.isEmpty()
				|| null == strRecordDelimiter || strRecordDelimiter.isEmpty())
			return null;

		java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (str4DMap,
			strRecordDelimiter);

		if (null == astrRecord || 0 == astrRecord.length) return null;

		java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String,
			java.lang.Double>>> map4D = new java.util.HashMap<java.lang.String,
				java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>();

		for (int i = 0; i < astrRecord.length; ++i) {
			if (null == astrRecord[i] || astrRecord[i].isEmpty() || strNULLString.equalsIgnoreCase
				(astrRecord[i]))
				continue;

			java.lang.String[] astrKVPairOut = org.drip.analytics.support.GenericUtil.Split (astrRecord[i],
				strKVDelimiter);
			
			if (null == astrKVPairOut || 2 != astrKVPairOut.length || null == astrKVPairOut[0] ||
				astrKVPairOut[0].isEmpty() || strNULLString.equalsIgnoreCase (astrKVPairOut[0]) ||
					(bSkipNullValue && (null == astrKVPairOut[1] || astrKVPairOut[1].isEmpty() ||
						strNULLString.equalsIgnoreCase (astrKVPairOut[1]))))
				continue;

			java.lang.String[] astrKeySet = org.drip.analytics.support.GenericUtil.Split (astrKVPairOut[0],
				strMultiLevelKeyDelimiter);
			
			if (null == astrKeySet || 3 != astrKeySet.length || null == astrKeySet[0] ||
				astrKeySet[0].isEmpty() || strNULLString.equalsIgnoreCase (astrKeySet[0]) || null ==
					astrKeySet[1] || astrKeySet[1].isEmpty() || strNULLString.equalsIgnoreCase
						(astrKeySet[1]) || null == astrKeySet[2] || astrKeySet[2].isEmpty() ||
							strNULLString.equalsIgnoreCase (astrKeySet[2]))
				continue;

			java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>> map3D =
				map4D.get (astrKeySet[0]);

			if (null == map3D)
				map3D = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String,
					java.lang.Double>>();

			java.util.Map<java.lang.String, java.lang.Double> map2D = map3D.get (astrKeySet[1]);

			if (null == map2D) map2D = new java.util.HashMap<java.lang.String, java.lang.Double>();

			map2D.put (astrKeySet[2], new java.lang.Double (astrKVPairOut[1]));

			map3D.put (astrKeySet[1], map2D);

			map4D.put (astrKeySet[0], map3D);
		}

		if (0 == map4D.size()) return null;

		return map4D;
	}
}
