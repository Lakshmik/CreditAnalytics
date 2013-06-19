
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
 * This class implements the solver for a system of linear equations given by A * x = B, where A is the
 * 	matrix, x the set of variables, and B is the result to be solved for.
 *
 * @author Lakshmi Krishnamurthy
 */

public class LinearSystemSolver {

	/**
	 * Regularize (i.e., convert the diagonal entries of the given cell to non-zero using suitable linear
	 * 	transformations)
	 * 
	 * @param aadblA In/Out Matrix to be regularized
	 * @param adblSolution In/out RHS
	 * @param iInnerRow Matrix Cell Row that needs to be regularized
	 * @param iOuter Matrix Cell Column that needs to be regularized
	 * 
	 * @return TRUE => Matrix has been successfully regularized
	 */

	public static final boolean RegulariseRow (
		final double[][] aadblA,
		final double[] adblSolution,
		final int iInnerRow,
		final int iOuter)
	{
		double dblInnerScaler = aadblA[iInnerRow][iOuter];

		if (0. != dblInnerScaler) return true;

		int iSize = aadblA.length;
		int iProxyRow = iSize - 1;

		while (0. == aadblA[iProxyRow][iOuter] && iProxyRow >= 0) --iProxyRow;

		if (iProxyRow < 0) return false;

		adblSolution[iInnerRow] += adblSolution[iProxyRow];

		for (int i = 0; i < iSize; ++i)
			aadblA[iInnerRow][i] += aadblA[iProxyRow][i];

		return 0. != aadblA[iInnerRow][iOuter];
	}

	/**
	 * Check to see if the matrix is diagonally dominant.
	 * 
	 * @param aadblA Input Matrix
	 * @param bCheckForStrongDominance TRUE => Fail if the matrix is not strongly diagonally dominant.
	 * 
	 * @return TRUE => Strongly or weakly Diagonally Dominant
	 */

	public static final boolean IsDiagonallyDominant (
		final double[][] aadblA,
		final boolean bCheckForStrongDominance)
	{
		if (null == aadblA) return false;

		int iSize = aadblA.length;

		if (0 == iSize || null == aadblA[0] || iSize != aadblA[0].length) return false;

		for (int i = 0; i < iSize; ++i) {
			double dblAbsoluteDiagonalEntry = java.lang.Math.abs (aadblA[i][i]);

			for (int j = 0; j < iSize; ++j) {
				if (i != j) {
					if ((bCheckForStrongDominance && dblAbsoluteDiagonalEntry <= java.lang.Math.abs
						(aadblA[i][j])) || (!bCheckForStrongDominance && dblAbsoluteDiagonalEntry <
							java.lang.Math.abs (aadblA[i][j])))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * Pivots the matrix A (Refer to wikipedia to find out what "pivot a matrix" means ;))
	 * 
	 * @param aadblA Input Matrix
	 * @param adblB Input RHS
	 * 
	 * @return The pivoted input matrix and the re-jigged input RHS
	 */

	public static final double[] Pivot (
		final double[][] aadblA,
		final double[] adblB)
	{
		if (null == aadblA || null == adblB) return null;

		int iSize = aadblA.length;
		double[] adblSolution = new double[iSize];

		if (0 == iSize || null == aadblA[0] || iSize != aadblA[0].length || iSize != adblB.length)
			return null;

		for (int i = 0; i < iSize; ++i)
			adblSolution[i] = adblB[i];

		for (int iOuter = 0; iOuter < iSize - 1; ++iOuter) {
			for (int iInnerRow = iOuter + 1; iInnerRow < iSize; ++iInnerRow) {
				if (!RegulariseRow (aadblA, adblSolution, iInnerRow, iOuter) || !RegulariseRow (aadblA,
					adblSolution, iOuter, iOuter))
					return null;
			}
		}

		return adblSolution;
	}

	/**
	 * Solve the Linear System using Gaussian Elimination from the Set of Values in the Array
	 * 
	 * @param aadblAIn Input Matrix
	 * @param adblB The Array of Values to be calibrated to
	 * 
	 * @return The Linear System Solution for the Coefficients
	 */

	public static final org.drip.math.linearalgebra.LinearizationOutput SolveUsingGaussianElimination (
		final double[][] aadblAIn,
		final double[] adblB)
	{
		if (null == aadblAIn || null == adblB) return null;

		int iSize = aadblAIn.length;
		double[][] aadblA = new double[iSize][iSize];

		if (0 == iSize || null == aadblAIn[0] || iSize != aadblAIn[0].length) return null;

		if (adblB.length != iSize) return null;

		for (int i = 0; i < iSize; ++i) {
			for (int j = 0; j < iSize; ++j)
				aadblA[i][j] = aadblAIn[i][j];
		}

		double[] adblSolution = Pivot (aadblA, adblB);

		if (null == adblSolution || adblSolution.length != iSize) return null;

		for (int iOuter = 0; iOuter < iSize - 1; ++iOuter) {
			for (int iInnerRow = iOuter + 1; iInnerRow < iSize; ++iInnerRow) {
				double dblEliminationRatio = aadblA[iOuter][iOuter] / aadblA[iInnerRow][iOuter];
				adblSolution[iInnerRow] = adblSolution[iInnerRow] * dblEliminationRatio -
					adblSolution[iOuter];

				for (int iInnerColumn = iOuter; iInnerColumn < iSize; ++iInnerColumn)
					aadblA[iInnerRow][iInnerColumn] = aadblA[iInnerRow][iInnerColumn] * dblEliminationRatio -
						aadblA[iOuter][iInnerColumn];
			}
		}

		for (int i = iSize - 1; i >= 0; --i) {
			for (int j = iSize - 1; j > i; --j)
				adblSolution[i] -= adblSolution[j] * aadblA[i][j];

			adblSolution[i] /= aadblA[i][i];
		}

		try {
			return new LinearizationOutput (adblSolution, aadblA, "GaussianElimination");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Solve the Linear System using the Gauss-Seidel algorithm from the Set of Values in the Array
	 * 
	 * @param aadblAIn Input Matrix
	 * @param adblB The Array of Values to be calibrated to
	 * 
	 * @return The Linear System Solution for the Coefficients
	 */

	public static final org.drip.math.linearalgebra.LinearizationOutput SolveUsingGaussSeidel (
		final double[][] aadblAIn,
		final double[] adblB)
	{
		if (null == aadblAIn || null == adblB) return null;

		int NUM_SIM = 5;
		int iSize = aadblAIn.length;
		double[] adblSolution = new double[iSize];
		double[][] aadblA = new double[iSize][iSize];

		if (0 == iSize || null == aadblAIn[0] || iSize != aadblAIn[0].length || iSize != adblB.length)
			return null;

		for (int i = 0; i < iSize; ++i) {
			for (int j = 0; j < iSize; ++j)
				aadblA[i][j] = aadblAIn[i][j];
		}

		double[] adblRHS = Pivot (aadblA, adblB);

		if (null == adblRHS || iSize != adblRHS.length ||
			!org.drip.math.linearalgebra.LinearSystemSolver.IsDiagonallyDominant (aadblA, true))
			return null;

		for (int i = 0; i < iSize; ++i)
			adblSolution[i] = 0.;

		for (int k = 0; k < NUM_SIM; ++k) {
			for (int i = 0; i < iSize; ++i) {
				adblSolution[i] = adblRHS[i];

				for (int j = 0; j < iSize; ++j) {
					if (j != i) adblSolution[i] -= aadblA[i][j] * adblSolution[j];
				}

				adblSolution[i] /= aadblA[i][i];
			}
		}

		try {
			return new LinearizationOutput (adblSolution, aadblA, "GaussianSeidel");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Solve the Linear System using by Inverting the coefficient Matrix
	 * 
	 * @param aadblAIn Input Matrix
	 * @param adblB The Array of Values to be calibrated to
	 * @param strInversionMethod The Inversion Method
	 * 
	 * @return The Linear System Solution for the Coefficients
	 */

	public static final org.drip.math.linearalgebra.LinearizationOutput SolveUsingMatrixInversion (
		final double[][] aadblAIn,
		final double[] adblB,
		final java.lang.String strInversionMethod)
	{
		if (null == aadblAIn || null == adblB) return null;

		int iSize = aadblAIn.length;
		double[] adblSolution = new double[iSize];

		if (0 == iSize || null == aadblAIn[0] || iSize != aadblAIn[0].length) return null;

		if (adblB.length != iSize) return null;

		double[][] aadblAInv = org.drip.math.linearalgebra.Matrix.Invert (aadblAIn, strInversionMethod);

		if (null == aadblAInv || 0 == aadblAInv.length || 0 == aadblAInv[0].length) return null;

		double[][] aadblProduct = org.drip.math.linearalgebra.Matrix.Product (aadblAInv, adblB);

		if (null == aadblProduct || 0 == aadblProduct.length || 0 == aadblProduct[0].length) return null;

		for (int i = 0; i < aadblProduct.length; ++i)
			adblSolution[i] = aadblProduct[i][i];

		try {
			return new LinearizationOutput (adblSolution, aadblAInv, strInversionMethod);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArg)
		throws java.lang.Exception
	{
		/* double[][] aadblA = new double[][] {{1., 2., 3.}, {2., 3., 4.}, {3., 5., 8.}};
		double[] adblB = new double[] {63., 19., 121.};
		double[][] aadblA = new double[][] {{0., 2., 0.}, {5., 0., 0.}, {0., 0., 1.}};
		double[] adblB = new double[] {14., 5., 22. };
		double[][] aadblA = new double[][] {{5., 1., 1.}, {1., 5., 1.}, {1., 1., 5.}};
		double[] adblB = new double[] {12., 17., 22.}; */
		double[][] aadblA = new double[][] {{1., 0., 0., 0.}, {1., 1., 2.72, 0.37}, {0., 0., 3.19, 0.37},
			{0., 0., 1., 0.43}};
		double[] adblB = new double[] {1., 1., 7., 0.};

		org.drip.math.linearalgebra.LinearizationOutput lssGaussianElimination =
			org.drip.math.linearalgebra.LinearSystemSolver.SolveUsingGaussianElimination (aadblA, adblB);

		for (int i = 0; i < lssGaussianElimination.getTransformedRHS().length; ++i)
			System.out.println ("GaussianElimination[" + i + "] = " +
				org.drip.math.common.FormatUtil.FormatDouble (lssGaussianElimination.getTransformedRHS()[i],
					0, 2, 1.));

		org.drip.math.linearalgebra.LinearizationOutput lssMatrixInversion =
			org.drip.math.linearalgebra.LinearSystemSolver.SolveUsingMatrixInversion (aadblA, adblB,
				"GaussianElimination");

		for (int i = 0; i < lssMatrixInversion.getTransformedRHS().length; ++i)
			System.out.println ("MatrixInversion[" + i + "] = " +
				org.drip.math.common.FormatUtil.FormatDouble (lssMatrixInversion.getTransformedRHS()[i], 0,
					2, 1.));

		/* org.drip.math.linearalgebra.LinearSystemSolution lssGaussSeidel =
			org.drip.math.linearalgebra.LinearSystemSolver.SolveUsingGaussSeidel (aadblA, adblB);

		for (int i = 0; i < lssGaussSeidel.getSolution().length; ++i)
			System.out.println ("GaussSeidel[" + i + "] = " + org.drip.math.common.FormatUtil.FormatDouble
				(lssGaussSeidel.getSolution()[i], 0, 2, 1.)); */
	}
}
