package userInterface;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import stock.*;
import util.*;

public class Tables {
	private PortfolioView view;
	
	public Tables(PortfolioView view) { 
		this.view = view;
	}
	
	public Component getHistoryTable() {
		Object[] header = InvestDate.addDateHeader(view.getCurrentPortfolio().getStocksInPortfolio());
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
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		return table;
	}

	public Component getOptimizationTable() {
		Portfolio portfolio = view.getCurrentPortfolio();
		String[] stocks = portfolio.getStocksInPortfolio();
		int width = 6;
		int height = stocks.length;
		
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
		
		for (int i=0; i<height; i++) {
			data[i][0] = stocks[i];
			data[i][1] = new Integer((int) Math.round(minRisk[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][2] = new Integer((int) Math.round(personal[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][3] = new Integer((int) Math.round(maxGrowth[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return table;
	}

	public Component getHomeTable() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
