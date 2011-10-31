package stock;

public class StockValues {
	
	private static double[] logHist(double[] history) {
		double[] result = new double[500];
		result[0] = 1;
		for (int i=1; i < 500; i++) {
			if (Double.isNaN(history[i])) {
				result[i] = result[i-1];
			} else {
				result[i] = Math.log(history[i]/result[i-1]);
			}
		}
		return result;
	}
	
	public static double expectedValue(double[] history) {
		double[] lH = logHist(history);
		double sum = 0;
		
		for (int i=0; i < 500; i++) {
			sum += lH[i];
		}
		return 0.5 * sum;
	}
}
