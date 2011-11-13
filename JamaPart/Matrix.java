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
