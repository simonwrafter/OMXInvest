package stock; 

public class StockValues {
	
	public static double expectedValue(double[] history) {
		double[] lH = logHist(history);
		double sum = 0;
		
		for (int i=0; i < 500; i++) {
			sum += lH[i];
		}
		return 0.5 * sum;
	}
	
	public static double expV(double[] lH) {
		double sum = 0;
		for (int i=0; i < 500; i++) {
			sum += lH[i];
		}
		return 0.5 * sum;
	}
	
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
	
	public static double variance(double[] history) {
		double[] lH = logHist(history);
		double expVal = expV(lH);
		double sum = 0;
		for (int i=0; i<500; i++) {
			sum += square(lH[i] - expVal/250);
		}
		return 250/499 * sum;
	}
	
	private static double square(double a) {
		return a*a;
	}
}
