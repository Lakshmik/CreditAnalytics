
package org.drip.math.linearalgebra;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * This class implements Matrix functionality, e.g., Matrix inversion.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Matrix {
	private static final boolean DiagonalizeRow (
		final int iQ,
		final double[][] aadblZ2XJack,
		final double[][] aadblZ2YJack)
	{
		if (0. != aadblZ2XJack[iQ][iQ]) return true;

		int iSize = aadblZ2XJack.length;
		int iP = iSize - 1;

		while (0. == aadblZ2XJack[iP][iQ] && iP >= 0) --iP;

		if (0 > iP) return false;

		for (int j = 0; j < iSize; ++j)
			aadblZ2XJack[iQ][j] += aadblZ2XJack[iP][j];

		aadblZ2YJack[iQ][iP] += 1.;
		return true;
	}

	/**
	 * Compute the Product of an input matrix and a column
	 * 
	 * @param aadblA Matrix A
	 * @param adblB Array B
	 * 
	 * @return The Product
	 */

	public static final double[][] Product (
		final double[][] aadblA,
		final double[] adblB)
	{
		if (null == aadblA || null == adblB) return null;

		int iNumACol = aadblA[0].length;
		int iNumProductCol = adblB.length;
		int iNumProductRow = aadblA.length;
		double[][] aadblProduct = new double[iNumProductRow][iNumProductCol];

		if (0 == iNumACol || iNumACol != adblB.length || 0 == iNumProductRow || 0 == iNumProductCol)
			return null;

		for (int iRow = 0; iRow < iNumProductRow; ++iRow) {
			for (int iCol = 0; iCol < iNumProductCol; ++iCol) {
				aadblProduct[iRow][iCol] = 0.;

				for (int i = 0; i < iNumACol; ++i) {
					if (!org.drip.math.common.NumberUtil.IsValid (aadblA[iRow][i]) ||
						!org.drip.math.common.NumberUtil.IsValid (adblB[i]))
						return null;

					aadblProduct[iRow][iCol] += aadblA[iRow][i] * adblB[i];
				}
			}
		}

		return aadblProduct;
	}

	/**
	 * Compute the Product of an input column and a matrix
	 * 
	 * @param adblA Column A
	 * @param aadblB Matrix B
	 * 
	 * @return The Product
	 */

	public static final double[][] Product (
		final double[] adblA,
		final double[][] aadblB)
	{
		if (null == adblA || null == aadblB) return null;

		int iNumACol = adblA.length;
		int iNumProductCol = aadblB.length;
		double[][] aadblProduct = new double[iNumACol][iNumProductCol];

		if (0 == iNumACol || iNumACol != aadblB.length || 0 == iNumProductCol) return null;

		for (int iRow = 0; iRow < iNumACol; ++iRow) {
			for (int iCol = 0; iCol < iNumProductCol; ++iCol) {
				aadblProduct[iRow][iCol] = 0.;

				for (int i = 0; i < iNumACol; ++i) {
					if (!org.drip.math.common.NumberUtil.IsValid (adblA[iRow]) ||
						!org.drip.math.common.NumberUtil.IsValid (aadblB[i][iCol]))
						return null;

					aadblProduct[iRow][iCol] += adblA[iRow] * aadblB[i][iCol];
				}
			}
		}

		return aadblProduct;
	}

	/**
	 * Compute the Product of the input matrices
	 * 
	 * @param aadblA Matrix A
	 * @param aadblB Matrix B
	 * 
	 * @return The Product
	 */

	public static final double[][] Product (
		final double[][] aadblA,
		final double[][] aadblB)
	{
		if (null == aadblA || null == aadblB) return null;

		int iNumACol = aadblA[0].length;
		int iNumProductRow = aadblA.length;
		int iNumProductCol = aadblB.length;
		double[][] aadblProduct = new double[iNumProductRow][iNumProductCol];

		if (0 == iNumACol || iNumACol != aadblB.length || 0 == iNumProductRow || 0 == iNumProductCol)
			return null;

		for (int iRow = 0; iRow < iNumProductRow; ++iRow) {
			for (int iCol = 0; iCol < iNumProductCol; ++iCol) {
				aadblProduct[iRow][iCol] = 0.;

				for (int i = 0; i < iNumACol; ++i) {
					if (!org.drip.math.common.NumberUtil.IsValid (aadblA[iRow][i]) ||
						!org.drip.math.common.NumberUtil.IsValid (aadblB[i][iCol]))
						return null;

					aadblProduct[iRow][iCol] += aadblA[iRow][i] * aadblB[i][iCol];
				}
			}
		}

		return aadblProduct;
	}

	/**
	 * Invert a 2D Matrix using Cramer's Rule
	 * 
	 * @param aadblA Input 2D Matrix
	 * 
	 * @return The Inverted Matrix
	 */

	public static final double[][] Invert2DMatrixUsingCramerRule (
		final double[][] aadblA)
	{
		if (null == aadblA || 2 != aadblA.length || 2 != aadblA[0].length) return null;

		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 2; ++j) {
				if (!org.drip.math.common.NumberUtil.IsValid (aadblA[i][j])) return null;
			}
		}

		double dblScale = aadblA[0][0] * aadblA[1][1] - aadblA[0][1] * aadblA[1][0];

		if (0. == dblScale) return null;

		return new double[][] {{aadblA[1][1] / dblScale, -1. * aadblA[0][1] / dblScale}, {-1. * aadblA[1][0]
			/ dblScale, aadblA[0][0] / dblScale}};
	}

	/**
	 * Invert the input matrix using Gaussian Elimination
	 * 
	 * @param aadblA Input Matrix
	 * 
	 * @return The Inverted Matrix
	 */

	public static final double[][] InvertUsingGaussianElimination (
		final double[][] aadblASource)
	{
		int iSize = aadblASource.length;
		double[][] aadblAInv = new double[iSize][iSize];

		for (int i = 0; i < iSize; ++i) {
			for (int j = 0; j < iSize; ++j)
				aadblAInv[i][j] = i == j ? 1. : 0.;
		}

		for (int iDiagonal = 0; iDiagonal < iSize; ++iDiagonal) {
			for (int iEntryCol = 0; iEntryCol < iSize; ++iEntryCol) {
				if (iDiagonal == iEntryCol || 0. == aadblASource[iDiagonal][iEntryCol]) continue;

				double dblWorkingRowFactor = aadblASource[iDiagonal][iDiagonal] /
					aadblASource[iDiagonal][iEntryCol];

				for (int iWorkingRow = 0; iWorkingRow < iSize; ++iWorkingRow) {
					aadblAInv[iWorkingRow][iEntryCol] *= dblWorkingRowFactor;
					aadblASource[iWorkingRow][iEntryCol] *= dblWorkingRowFactor;
					aadblAInv[iWorkingRow][iEntryCol] -= aadblAInv[iWorkingRow][iDiagonal];
					aadblASource[iWorkingRow][iEntryCol] -= aadblASource[iWorkingRow][iDiagonal];
				}
			}
		}

		for (int iDiagonal = 0; iDiagonal < iSize; ++iDiagonal) {
			double dblDiagScaleDown = aadblASource[iDiagonal][iDiagonal];

			for (int iEntryRow = 0; iEntryRow < iSize; ++iEntryRow) {
				aadblAInv[iEntryRow][iDiagonal] = aadblAInv[iEntryRow][iDiagonal] / dblDiagScaleDown;
				aadblASource[iEntryRow][iDiagonal] = aadblASource[iEntryRow][iDiagonal] / dblDiagScaleDown;
			}
		}

		return aadblAInv;
	}

	/**
	 * Invert the input matrix using the specified Method
	 * 
	 * @param aadblA Input Matrix
	 * @param strMethod The Inversion Method
	 * 
	 * @return The Inverted Matrix
	 */

	public static final double[][] Invert (
		final double[][] aadblA,
		final java.lang.String strMethod)
	{
		if (null == aadblA) return null;

		int iSize = aadblA.length;
		double[][] aadblAInv = null;
		double[][] aadblASource = new double[iSize][iSize];
		double[][] aadblZ2YJack = new double[iSize][iSize];

		if (0 == iSize || iSize != aadblA[0].length) return null;

		for (int i = 0; i < iSize; ++i) {
			for (int j = 0; j < iSize; ++j) {
				if (!org.drip.math.common.NumberUtil.IsValid (aadblASource[i][j] = aadblA[i][j]))
					return null;

				aadblZ2YJack[i][j] = i == j ? 1. : 0.;
			}
		}

		for (int i = 0; i < iSize; ++i) {
			if (0. == aadblASource[i][i] && !DiagonalizeRow (i, aadblASource, aadblZ2YJack)) return null;
		}

		if (null == strMethod || strMethod.isEmpty() || strMethod.equalsIgnoreCase ("GaussianElimination"))
			aadblAInv = InvertUsingGaussianElimination (aadblASource);

		if (null == aadblAInv || iSize != aadblAInv.length || iSize != aadblAInv[0].length) return null;

		return Product (aadblAInv, aadblZ2YJack);
	}

	public static final void main (
		final java.lang.String[] astrArg)
	{
		double[][] aadblA = new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9.01}};

		double[][] aadblAInv = InvertUsingGaussianElimination (aadblA);

		org.drip.math.common.NumberUtil.Print2DArray ("AINV", aadblAInv, false);

		double[][] aadblProduct = Product (aadblA, aadblAInv);

		org.drip.math.common.NumberUtil.Print2DArray ("PROD", aadblProduct, false);
	}
}
