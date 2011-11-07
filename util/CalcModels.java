package util; 

import Jama.Matrix;

public class CalcModels {
	
	public static Double[] logHist(Double[] history) {
		Double[] result = new Double[500];
		result[0] = 499.;
		for (int i=498; i >= 0; i--) {
			if (Double.isNaN(history[i]))
				result[i] = result[i+1];
			else
				result[i] = Math.log(history[i]/result[i+1]);
		}
		return result;
	}
	
	public static Double expectedValue(Double[] history) {
		Double[] lH = logHist(history);
		double sum = 0;
		for (int i=499; i >= 0; i--)
			sum += lH[i];
		return 0.5 * sum;
	}
	
	public static Double logExpectedValue(Double[] lH) {
		double sum = 0;
		for (int i=499; i >= 0; i--)
			sum += lH[i];
		return 0.5 * sum;
	}
	
	public static Double[] portfolioExpectedValue(Double[][] histories) {
		Double[] result = new Double[histories.length];
		for (int i=0; i<histories.length; i++)
			result[i] = expectedValue(histories[i]);
		return result;
	}
	
	public static Double variance(Double[] history) {
		Double[] lH = logHist(history);
		double expVal = logExpectedValue(lH);
		double sum = 0;
		for (int i=499; i >= 0; i--) {
			sum += square(lH[i] - expVal/250);
		}
		return 250/499 * sum;
	}
	
	public static Double[][] covariance(Double[][] histories) {
		Double[][] prodMatrix = new Double[histories.length][histories.length];
		for (int i=0; i < histories.length; i++) {
			Double[] lHi = logHist(histories[i]);
			double expVali = logExpectedValue(lHi);
			for (int j=0; j < histories.length; j++) {
				Double[] lHj = logHist(histories[j]);
				double expValj = logExpectedValue(lHj);
				double sum = 0;
				for (int k=499; k >= 0; k--)
					sum += (lHi[k] - expVali/250) * (lHj[k] - expValj/250);
				prodMatrix[i][j] = 250/499 * sum;
			}
		}
		return prodMatrix;
	}
	
	public static Double portfolioVariance(Double[] proportions, Double[][] covariance) {
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
	
	public static Double[] optimizeLowRisk(Double[][] coVariance) {
		Double[] result = new Double[coVariance.length];
		
		Matrix C = new Matrix(InvestMatrix.toDoublePrimitiv(coVariance));
		C = C.inverse();
		Double[][] coV = InvestMatrix.toDoubleObject(C.getArrayCopy());
		
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
	
	public static Double[] optimizeHighGrowth(Double[][] coVariance, Double[] expValues) {
		int bound = expValues.length;
		double tmp;
		Double[] arrTmp = new Double[bound];
		Double[] result = new Double[bound];
		
		Matrix C = new Matrix(InvestMatrix.toDoublePrimitiv(coVariance));
		C = C.inverse();
		Double[][] coV = InvestMatrix.toDoubleObject(C.getArrayCopy());
		
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
	
	public static Double[] personalPortfolio(Double[] low, Double[] high, Double lambda) {
		int bound = low.length;
		Double[] result = new Double[bound];
		
		for (int i=0; i<bound; i++) {
			result[i] = lambda * high[i] + (1-lambda) * low[i];
		}
		
		return result;
	}
	
	private static double square(double a) {
		return a*a;
	}
}
