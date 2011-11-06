package util;

public class Matrix {
	public static Object[][] transpose(Object[][] matrix) {
		Object[][] result = new Object[matrix[0].length][matrix.length];
		for (int i=0; i < matrix.length; i++) {
			for (int j=0; j < matrix[0].length; j++) {
				result[j][i] = matrix[i][j];
			}
		}
		return result;
	}
}
