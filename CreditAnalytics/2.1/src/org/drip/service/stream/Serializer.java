
package org.drip.service.stream;

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
 * This interface defines the core object serializer methods – serialization into and
 * 		de-serialization out of byte arrays, as well as the object version.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class Serializer {
	private static final java.lang.String OBJECT_TRAILER = "$";
	private static final java.lang.String FIELD_DELIMITER = "|";
	private static final java.lang.String COLLECTION_RECORD_DELIMITER = ";";
	private static final java.lang.String COLLECTION_KEY_VALUE_DELIMITER = "=";
	private static final java.lang.String COLLECTION_MULTI_LEVEL_KEY_DELIMITER = "_";

	/**
	 * Null serialized string
	 */

	public static final java.lang.String NULL_SER_STRING = "<<null>>";

	/**
	 * Serialization Version - ALWAYS prepend this on all derived classes
	 */

	public static final double VERSION = 1.6;

	/**
	 * Returns the Collection Multi-level Key Delimiter String
	 * 
	 * @return Collection Multi-level Key Delimiter String
	 */

	public java.lang.String getCollectionMultiLevelKeyDelimiter()
	{
		return COLLECTION_MULTI_LEVEL_KEY_DELIMITER;
	}

	/**
	 * Returns the Collection Key Value Delimiter String
	 * 
	 * @return Collection Key Value Delimiter String
	 */

	public java.lang.String getCollectionKeyValueDelimiter()
	{
		return COLLECTION_KEY_VALUE_DELIMITER;
	}

	/**
	 * Returns the Collection Record Delimiter String
	 * 
	 * @return Collection Record Delimiter String
	 */

	public java.lang.String getCollectionRecordDelimiter()
	{
		return COLLECTION_RECORD_DELIMITER;
	}

	/**
	 * Returns the Field Delimiter String
	 * 
	 * @return Field Delimiter String
	 */

	public java.lang.String getFieldDelimiter()
	{
		return FIELD_DELIMITER;
	}

	/**
	 * Returns the Object Trailer String
	 * 
	 * @return Object Trailer String
	 */

	public java.lang.String getObjectTrailer()
	{
		return OBJECT_TRAILER;
	}

	/**
	 * Serialize into a byte array.
	 *  
	 * @return Byte Array
	 */

	public abstract byte[] serialize();

	/**
	 * De-serialize from a byte array.
	 *  
	 * @return De-serialized object instance represented by Serializer
	 */

	public abstract org.drip.service.stream.Serializer deserialize (
		final byte[] ab);
}
