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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collection;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private Double[] minRisk;
	private Double[] maxGrowth;
	
	public Tables(PortfolioView view) { 
		this.view = view;
	}
	
	public Component getHistoryTable() {
		Object[] header = InvestDate.addDateHeader(view.getShortNames());
		Object[][] data = InvestMatrix.transpose(view.getPortfolioHistory());
		
		for(Object[] o : data) {
			o[0] = InvestDate.dateWithDash(((Double) o[0]).intValue());
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
		return new JScrollPane(table);
	}
	
	public Component getMarketTable() {
		Collection<Market> set = view.getMarketSet();
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
			header[x] = m.getName();
			for(Map.Entry<String, Stock> e : m.getMarketMap().entrySet()) {
				Stock s = e.getValue();
				data[y][x] = s.getName();
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

		return new JScrollPane(table);
	}

	public Component getOptimizationTable() {
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
		minRisk = CalcModels.optimizeLowRisk(coV);
		maxGrowth = CalcModels.optimizeHighGrowth(coV, 
					CalcModels.portfolioExpectedValue(histories));
		Double[] personal = getPersonal();
		
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
		
		data[0][5] = "Lambda";
		data[1][5] = view.getCurrentPortfolio().getLambda();
		
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
		
		JPanel ip = new JPanel(new BorderLayout());
		ip.add(new JScrollPane(table), BorderLayout.CENTER);
		ip.add(new LambdaSlider(view), BorderLayout.SOUTH);
		return ip;
	}
	
	public Double[] getPersonal() {
		return CalcModels.personalPortfolio(minRisk, maxGrowth, view.getCurrentPortfolio().getLambda());
	}

	public Component getHomeTable() {
		Portfolio portfolio = view.getCurrentPortfolio();
		
		String[] omxIds = view.getStockIds();
		String[] stocks = view.getStockNames();
		String[] shortName = view.getShortNames();
		Integer[] nbrOf = portfolio.getShareDistribution();
		Double[][] history = view.getPortfolioHistory(1);
		
		int height = Math.max(stocks.length, 6); //8);
		int width = 7;
		
		Object[] header = {"omxId", "name", "short name", "nbrOf", "last value", "total", ""};
		Object[][] data = new Object[height][width];
		
		for (Object[] o : data) {
			Arrays.fill(o, "");
		}
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = omxIds[i];
			data[i][1] = stocks[i];
			data[i][2] = shortName[i];
			data[i][3] = nbrOf[i];
			data[i][4] = history[i+1][0];
			data[i][5] = nbrOf[i]*history[i+1][0];
			data[i][6] = "";
		}
		
//		Double portvariance = CalcModels.portfolioVariance(view.getPortfolioDistribution(), CalcModels.covariance(view.getPortfolioHistory()));
//		double value = view.portfolioValue();
		
		data[0][6] = "currency";
		data[1][6] = portfolio.getCurrency();
		data[2][6] = "lambda";
		data[3][6] = portfolio.getLambda();
		data[4][6] = "liquid";
		data[5][6] = portfolio.getLiquidAsset();
//		data[6][6] = "VaR";
//		data[7][6] = value != 0 ? CalcModels.valueAtRisk(value, portvariance, 0.05, 0.019) : 0;
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 1) {
				column.setPreferredWidth(90);
			} else {
				column.setPreferredWidth(75);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		
		JPanel ip = new JPanel(new BorderLayout());
		ip.add(new JScrollPane(table), BorderLayout.CENTER);
		ip.add(new HomeCommandPanel(view), BorderLayout.SOUTH);
		return ip;
	}
}
