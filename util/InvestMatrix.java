package util;

public class InvestMatrix {
	public static Object[][] transpose(Object[][] matrix) {
		Object[][] result = new Object[matrix[0].length][matrix.length];
		for (int i=0; i < matrix.length; i++) {
			for (int j=0; j < matrix[0].length; j++) {
				result[j][i] = matrix[i][j];
			}
		}
		return result;
	}

	public static double[][] toDoublePrimitiv(Double[][] original) {
		double[][] result = new double[original.length][original[0].length];
		
		for (int i=0; i<original.length; i++)
			for (int j=0; j<original[i].length; j++)
				result[i][j] = original[i][j].doubleValue();
		
		return result;
	}

	public static Double[][] toDoubleObject(double[][] original) {
		Double[][] result = new Double[original.length][original[0].length];
		
		for (int i=0; i<original.length; i++)
			for (int j=0; j<original[i].length; j++)
				result[i][j] = original[i][j];
		
		return result;
	}
}
