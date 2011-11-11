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

package userInterface;

import java.awt.Component;
import java.util.SortedSet;
import java.util.Arrays;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import stock.Market;
import stock.Stock;
import stock.Portfolio;
import util.InvestDate;
import util.InvestMatrix;
import util.CalcModels;;

public class Tables {
	private PortfolioView view;
	
	public Tables(PortfolioView view) { 
		this.view = view;
	}
	
	public Component getHistoryTable() {
		Object[] header = InvestDate.addDateHeader(view.getShortNames());
		Object[][] data = InvestMatrix.transpose(view.getPortfolioHistory());
		
		for(Object[] o : data) {
			o[0] = InvestDate.makeDateString(((Double) o[0]).intValue());
			for(int i=1;i<o.length;i++) {
				o[i] = String.format("%.02f", o[i]);
			}
		}
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(90);
			} else {
				column.setPreferredWidth(75);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		return table;
	}
	
	public Component getMarketTable() {
		SortedSet<Market> set = view.getMarketSet();
		int x = set.size()*3;
		int y = 0;
		for(Market m : set) {
			y = y < m.getMarketMap().size() ? m.getMarketMap().size() : y; 
		}
		Object[] header = new Object[x];
		Object[][] data = new Object[y][x];
		
		Arrays.fill(header, "");
		for (Object[] o : data)
			Arrays.fill(o, "");
		
		x=0;
		for (Market m : set) {
			y=0;
			header[x] = m.getListName();
			for(Map.Entry<String, Stock> e : m.getMarketMap().entrySet()) {
				Stock s = e.getValue();
				data[y][x] = s.getFullName();
				data[y][x+1] = s.getShortName();
				data[y][x+2] = s.getOmxId();
				y++;
			}
			x += 3;
		}
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i%3 == 0) {
				column.setPreferredWidth(175);
			} else {
				column.setPreferredWidth(75);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		return table;
	}

	public Component getOptimizationTable() {
		Portfolio portfolio = view.getCurrentPortfolio();
		String[] stocks = view.getShortNames();
		int width = 6;
		int height = Math.max(stocks.length, 5);
		
		Object[] header = {"name", "min risk", "personal", "max growth", "", ""};//new Object[width];
		Object[][] data = new Object[height][width];
		
		//Arrays.fill(header, "");
		for (Object[] o : data) {
			Arrays.fill(o, "");
		}
		
		Double portfolioValue = view.portfolioValue();
		Double portfolioLiquid = view.getPortfolioLiquid();
		Double[][] histories = view.getPortfolioHistory();
		Double[][] coV = CalcModels.covariance(histories);
		Double[] minRisk = CalcModels.optimizeLowRisk(coV);
		Double[] maxGrowth = CalcModels.optimizeHighGrowth(coV, 
					CalcModels.portfolioExpectedValue(histories));
		Double[] personal = CalcModels.personalPortfolio(minRisk, maxGrowth, portfolio.getLambda());
		Double variance = CalcModels.portfolioVariance(personal, coV);
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = stocks[i];
			
			data[i][1] = new Integer((int) Math.round(minRisk[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][2] = new Integer((int) Math.round(personal[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][3] = new Integer((int) Math.round(maxGrowth[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			
//			data[i][1] = minRisk[i];
//			data[i][2] = personal[i];
//			data[i][3] = maxGrowth[i];
			data[i][4] = "";
			data[i][5] = "";
		}
		
		data[2][5] = "VaR";
		data[3][5] = CalcModels.valueAtRisk(portfolioValue, variance, 1, .1);
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(75);
			} else {
				column.setPreferredWidth(80);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		return table;
	}

	public Component getHomeTable() {
		Portfolio portfolio = view.getCurrentPortfolio();
		
		String[] stocks = view.getStockNames();
		String[] shortName = view.getShortNames();
		Integer[] nbrOf = portfolio.getShareDistribution();
		Double[][] history = view.getPortfolioHistory(1);
		
		int height = Math.max(stocks.length, 5);
		int width = 6;
		
		Object[] header = {"stocks", "short name", "nbrOf", "last value", "total", ""};
		Object[][] data = new Object[height][width];
		
		for (Object[] o : data) {
			Arrays.fill(o, "");
		}
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = stocks[i];
			data[i][1] = shortName[i];
			data[i][2] = nbrOf[i];
			data[i][3] = history[i+1][0];
			data[i][4] = nbrOf[i]*history[i+1][0];
			data[i][5] = "";
		}
		
		data[0][5] = "lambda";
		data[1][5] = portfolio.getLambda();
		data[2][5] = "liquid";
		data[3][5] = portfolio.getLiquidAsset();
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(90);
			} else {
				column.setPreferredWidth(75);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		return table;
	}
	
}
