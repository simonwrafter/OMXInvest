/*
 * Copyright © 2011, Simon Wrafter <simon.wrafter@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package util; 

import java.util.Arrays;

import JamaPart.Matrix;

public class CalcModels {
	
	public static Double[] logHist(Double[] history) {
		int limit=history.length-1;
		while (history[limit] == null)
			limit--;
		Double[] result = new Double[limit];
		for (int i=0; i < limit; i++) {
			if (Double.isNaN(history[i]) || Double.isNaN(history[i+1]))
				result[i] = result[i-1];
			else
				result[i] = Math.log(history[i]/history[i+1]);
		}
		return result;
	}
	
	public static Double expectedValue(Double[] history) {
		return logExpectedValue(logHist(history));
	}
	
	public static Double logExpectedValue(Double[] logHistory) {
		double sum = 0;
		for (int i=0; i < logHistory.length; i++)
			sum += logHistory[i];
		return (250.0 / (logHistory.length+1)) * sum;
	}
	
	public static Double[] portfolioExpectedValue(Double[][] histories) {
		Double[] result = new Double[histories.length-1];
		for (int i=1; i<histories.length; i++)
			result[i-1] = expectedValue(histories[i]);
		return result;
	}
	
	public static Double variance(Double[] history) {
		Double[] lH = logHist(history);
		double expVal = logExpectedValue(lH);
		double sum = 0;
		for (int i=lH.length-1; i >= 0; i--)
			sum += square(lH[i] - expVal/250);
		return 250.0/499 * sum;
	}
	
	public static Double[][] covariance(Double[][] histories) {
		Double[][] prodMatrix = new Double[histories.length-1][histories.length-1];
		for (int i=1; i < histories.length; i++) {
			Double[] lHi = logHist(histories[i]);
			double expVali = logExpectedValue(lHi);
			for (int j=1; j < histories.length; j++) {
				Double[] lHj = logHist(histories[j]);
				double expValj = logExpectedValue(lHj);
				double sum = 0;
				int days = Math.min(lHi.length-1, lHj.length-1);
				for (int k=days; k >= 0; k--)
					sum += (lHi[k] - expVali/250) * (lHj[k] - expValj/250);
				prodMatrix[i-1][j-1] = 250.0/days * sum;
			}
		}
		return prodMatrix;
	}
	
	public static Double portfolioVariance(Double[] proportions, Double[][] covariance) {
		double result = 0;
		for (int i=0; i<proportions.length; i++)
			for (int j=0; j<proportions.length; j++)
				result += proportions[i] * covariance[i][j] * proportions[j];
		return result;
	}
	
	public static Double[] optimizeLowRisk(Double[][] coVariance) {
		Double[] result = new Double[coVariance.length];
		Arrays.fill(result, new Double(0.0));
		Double[][] coV = new Matrix(coVariance).inverse().getArrayCopy();
		for (int i=0; i<coV.length; i++)
			for (int j=0; j<coV.length; j++)
				result[i] += coV[i][j];
		double sum = 0;
		for (int i=0; i<result.length; i++)
			sum += result[i];
		for (int i=0; i<result.length; i++)
			result[i] /= sum;
		return result;
	}
	
	public static Double[] optimizeHighGrowth(Double[][] coVariance, Double[] expValues) {
		int bound = expValues.length;
		double prod = 0, sum = 0;
		Double[] arrTmp = new Double[bound];
		Double[] result = new Double[bound];
		Arrays.fill(arrTmp, new Double(0.0));
		Arrays.fill(result, new Double(0.0));
		Double[][] coV = new Matrix(coVariance).inverse().getArrayCopy();
		for (int i=0; i<bound; i++)
			for (int j=0; j<coV.length; j++)
				arrTmp[i] += coV[j][i];
		for (int i=0; i<bound; i++) {
			prod += arrTmp[i] * expValues[i];
			sum += arrTmp[i];
		}
		for (int i=0; i<bound; i++)
			arrTmp[i] = expValues[i] - (prod-1)/sum;
		for (int i=0; i<bound; i++)
			for (int j=0; j<coV.length; j++)
				result[i] += (coV[i][j] * arrTmp[j]);
		return result;
	}
	
	public static Double[] personalPortfolio(Double[] low, Double[] high, Double lambda) {
		int bound = low.length;
		Double[] result = new Double[bound];
		for (int i=0; i<bound; i++) {
			result[i] = lambda * high[i] + (1.0 - lambda) * low[i];
		}
		return result;
	}
	
	private static double square(double a) {
		return a*a;
	}
}
