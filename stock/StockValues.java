package stock; 

import Jama.Matrix;

public class StockValues {
	
	public static double expectedValue(double[] history) {
		double[] lH = logHist(history);
		double sum = 0;
		
		for (int i=0; i < 500; i++) {
			sum += lH[i];
		}
		return 0.5 * sum;
	}
	
	public static double logExpectedValue(double[] lH) {
		double sum = 0;
		for (int i=0; i < 500; i++) {
			sum += lH[i];
		}
		return 0.5 * sum;
	}
	
	public static double variance(double[] history) {
		double[] lH = logHist(history);
		double expVal = logExpectedValue(lH);
		double sum = 0;
		for (int i=0; i<500; i++) {
			sum += square(lH[i] - expVal/250);
		}
		return 250/499 * sum;
	}
	
	public static double[][] covariance(double[][] histories) {
		double[][] prodMatrix = new double[histories.length][histories.length];
		
		for (int i=0; i < histories.length; i++) {
			double[] lHi = logHist(histories[i]);
			double expVali = logExpectedValue(lHi);
			
			for (int j=0; j < histories.length; j++) {
				double[] lHj = logHist(histories[j]);
				double expValj = logExpectedValue(lHj);
				
				double sum = 0;
				for (int k=0; k<500; k++) {
					sum += (lHi[k] - expVali/250) * (lHj[k] - expValj/250);
				}
				
				prodMatrix[i][j] = 250/499 * sum;
			}
		}
		return prodMatrix;
	}
	
	public static double portfolioVariance(double[] proportions, double[][] covariance) {
		double result = 0;
		for (int i=0; i<proportions.length; i++) {
			for (int j=0; j<proportions.length; j++) {
				result += proportions[i] * covariance[i][j] * proportions[j];
			}
		}
		return result;
	}
	
	public static double valueAtRisk(double value, double portVariance, double rho, double years) {
		return value * portVariance * rho * Math.sqrt(years);
	}
	
	public static double[] optimizeLowRisk(double[][] coVariance) {
		double[] result = new double[coVariance.length];
		
		Matrix C = new Matrix(coVariance);
		C = C.inverse();
		double[][] coV = C.getArrayCopy();
		
		double sum = 0;
		for (int i=0; i<coV.length; i++) {
			for (int j=0; j<coV.length; j++) {
				sum += coV[i][j];
			}
		}
		for (int i=0; i<coV.length; i++) {
			double tmp = 0;
			for (int j=0; j<coV.length; j++) {
				tmp += coV[i][j];
			}
			result[i] = tmp / sum;
		}
		return result;
	}
	
	public static double[] optimizeHighGrowth(double[][] coVariance, double[] expValues) {
		int bound = expValues.length;
		double tmp;
		double[] arrTmp = new double[bound];
		double[] result = new double[bound];
		
		Matrix C = new Matrix(coVariance);
		C = C.inverse();
		double[][] coV = C.getArrayCopy();
		
		double sum = 0;
		for (int i=0; i<bound; i++) {
			for (int j=0; j<bound; j++) {
				sum += coV[i][j];
			}
		}
		
		for (int i=0; i<bound; i++) {
			tmp = 0;
			for (int j=0; j<bound; j++) {
				tmp += coV[i][j];
			}
			arrTmp[i] = tmp;
		}
		
		tmp = 0;
		for (int i=0; i<bound; i++) {
			tmp += arrTmp[i] * expValues[i];
		}
		tmp = (tmp - 1) / sum;
		
		for (int i=0; i<bound; i++) {
			arrTmp[i] = expValues[i] - tmp;
		}
		
		for (int i=0; i<bound; i++) {
			tmp = 0;
			for (int j=0; j<bound; j++) {
				tmp += coV[i][j] * arrTmp[j];
			}
			result[i] = tmp / sum;
		}
		return result;
	}
	
	public static double[] personalPortfolio(double[] low, double[] high, double lambda) {
		int bound = low.length;
		double[] result = new double[bound];
		
		for (int i=0; i<bound; i++) {
			result[i] = lambda * high[i] + (1-lambda) * low[i];
		}
		
		return result;
	}
	
	//--privates
	
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
	
	private static double square(double a) {
		return a*a;
	}
}
