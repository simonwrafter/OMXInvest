package stock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class Investments {
	private SortedSet<Portfolio> portfolios;
	private SortedSet<Market> markets;
	
	public Investments() throws MalformedURLException, IOException {
		portfolios = new TreeSet<Portfolio>();
		markets = new TreeSet<Market>();
		
		portfolios.add(new Portfolio("default"));
		
		for (String s : MarketData.arrayMarkets)
			for (String t : MarketData.arrayCapital)
				markets.add(new Market(s, t));
	}
	
	public Portfolio getDefaultPortfolio() {
		return portfolios.first();
	}
	
	public void updateHistory(Portfolio portfolio) throws MalformedURLException, IOException {
		for (String s : portfolio.stocksInPortfolio()) {
			for (Market m : markets) {
				if (m.contains(s)) {
					m.updateHistory(s);
					break;
				}
			}
		}
	}
	
	public void rebuildHistory(Portfolio portfolio) throws MalformedURLException, IOException {
		for (String s : portfolio.stocksInPortfolio()) {
			for (Market m : markets) {
				if (m.contains(s)) {
					m.rebuildHistory(s);
					break;
				}
			}
		}
	}
	
	public double[][] getHistory(Portfolio portfolio) {
		double[][] result = new double[portfolio.size()][];
		int i=0;
		
		for (String s : portfolio.stocksInPortfolio()) {
			for (Market m : markets) {
				if (m.contains(s)) {
					result[i++] = m.getStock(s).getHistory()[4];
					break;
				}
			}
		}
		return result;
	}
}
