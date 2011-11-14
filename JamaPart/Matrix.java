/* Copyright Notice
 * This software is a cooperative product of The MathWorks and the National
 * Institute of Standards and Technology (NIST) which has been released to the
 * public domain. Neither The MathWorks nor NIST assumes any responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. 
 */

/*
 * Simon Wrafter <simon.wrafter@gmail.com>
 * As part of OMXInvest, this class has been minimised and stripped of unused code.
 */

package JamaPart;

public class Matrix {

	private Double[][] A;
	private int m, n;

	public Matrix (int m, int n) {
		this.m = m;
		this.n = n;
		A = new Double[m][n];
	}

	public Matrix (Double[][] A) {
		m = A.length;
		n = A[0].length;
		for (int i = 0; i < m; i++) {
			if (A[i].length != n) {
				throw new IllegalArgumentException("All rows must have the same length.");
			}
		}
		this.A = A;
	}

	public Double[][] getArray () {
		return A;
	}

	public Double[][] getArrayCopy () {
		Double[][] C = new Double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j];
			}
		}
		return C;
	}

	public int getRowDimension () {
		return m;
	}

	public int getColumnDimension () {
		return n;
	}

	public Matrix getMatrix (int[] r, int j0, int j1) {
		Matrix X = new Matrix(r.length,j1-j0+1);
		Double[][] B = X.getArray();
		try {
			for (int i = 0; i < r.length; i++) {
				for (int j = j0; j <= j1; j++) {
					B[i][j-j0] = A[r[i]][j];
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}

	public Matrix inverse () {
		return new LUDecomposition(this).solve(identity(m,m));
	}

	public static Matrix identity (int m, int n) {
		Matrix A = new Matrix(m,n);
		Double[][] X = A.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X[i][j] = (i == j ? 1.0 : 0.0);
			}
		}
		return A;
	}
}
