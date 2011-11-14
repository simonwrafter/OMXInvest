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

public class LUDecomposition {

	private Double[][] LU;
	private int m, n, pivsign; 
	private int[] piv;

	public LUDecomposition (Matrix matrix) {
		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.
		LU = matrix.getArrayCopy();
		m = matrix.getRowDimension();
		n = matrix.getColumnDimension();
		piv = new int[m];
		for (int i = 0; i < m; i++) {
			piv[i] = i;
		}
		pivsign = 1;
		Double[] LUrowi;
		Double[] LUcolj = new Double[m];

		// Outer loop.
		for (int j = 0; j < n; j++) {

			// Make a copy of the j-th column to localize references.
			for (int i = 0; i < m; i++) {
				LUcolj[i] = LU[i][j];
			}

			// Apply previous transformations.
			for (int i = 0; i < m; i++) {
				LUrowi = LU[i];

				// Most of the time is spent in the following dot product.
				int kmax = Math.min(i,j);
				double s = 0.0;
				for (int k = 0; k < kmax; k++) {
					s += LUrowi[k]*LUcolj[k];
				}
				LUrowi[j] = LUcolj[i] -= s;
			}

			// Find pivot and exchange if necessary.
			int p = j;
			for (int i = j+1; i < m; i++) {
				if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
					p = i;
				}
			}
			if (p != j) {
				for (int k = 0; k < n; k++) {
					double t = LU[p][k]; LU[p][k] = LU[j][k]; LU[j][k] = t;
				}
				int k = piv[p]; piv[p] = piv[j]; piv[j] = k;
				pivsign = -pivsign;
			}

			// Compute multipliers.
			if (j < m & LU[j][j] != 0.0) {
				for (int i = j+1; i < m; i++) {
					LU[i][j] /= LU[j][j];
				}
			}
		}
	}

	public boolean isNonsingular () {
		for (int j = 0; j < n; j++) {
			if (LU[j][j] == 0)
				return false;
		}
		return true;
	}

	public Matrix solve (Matrix B) {
		if (B.getRowDimension() != m) {
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		}
		if (!this.isNonsingular()) {
			throw new RuntimeException("Matrix is singular.");
		}

		// Copy right hand side with pivoting
		int nx = B.getColumnDimension();
		Matrix Xmat = B.getMatrix(piv,0,nx-1);
		Double[][] X = Xmat.getArray();

		// Solve L*Y = B(piv,:)
		for (int k = 0; k < n; k++) {
			for (int i = k+1; i < n; i++) {
				for (int j = 0; j < nx; j++) {
					X[i][j] -= X[k][j]*LU[i][k];
				}
			}
		}
		// Solve U*X = Y;
		for (int k = n-1; k >= 0; k--) {
			for (int j = 0; j < nx; j++) {
				X[k][j] /= LU[k][k];
			}
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < nx; j++) {
					X[i][j] -= X[k][j]*LU[i][k];
				}
			}
		}
		return Xmat;
	}
}
