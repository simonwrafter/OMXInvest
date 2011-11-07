package stock;

import java.io.*;
import java.net.*;
import java.util.*;

public class Investments {
	private SortedSet<Portfolio> portfolios;
	private SortedSet<Market> markets;
	private Portfolio defaultPortfolio;
	private Portfolio currentPortfolio;

	public Investments() throws MalformedURLException, IOException {
		portfolios = new TreeSet<Portfolio>();
		markets = new TreeSet<Market>();

		for (String s : MarketData.arrayMarkets) {
			for (String t : MarketData.arrayCapital) {
				markets.add(new Market(s, t));
			}
		}

		buildDefaultPortfolio();
		portfolios.add(defaultPortfolio);
	}
	
	public SortedSet<Market> getMarketSet() {
		return markets;
	}

	public Portfolio getDefaultPortfolio() {
		return defaultPortfolio;
	}

	public Portfolio setDefaultPortfolio(Portfolio portfolio) {
		Portfolio p = defaultPortfolio;
		defaultPortfolio = portfolio;
		return p;
	} 

	public Portfolio getCurrentPortfolio() {
		return currentPortfolio;
	}

	public void setCurrentPortfolio(Portfolio portfolio) {
		this.currentPortfolio = portfolio;
	}
	
	public void addStockToPortfolio(String omxId) throws MalformedURLException, IOException {
		currentPortfolio.add(omxId);
		rebuildHistory(omxId);
	}

	public void updateHistory() throws MalformedURLException, IOException {
		for (String s : currentPortfolio.getStocksInPortfolio()) {
			rebuildHistory(s);
		}
	}
	
	public void updateHistory(String omxId) throws MalformedURLException, IOException {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				m.updateHistory(omxId);
				break;
			}
		}
	}

	public void rebuildHistory() throws MalformedURLException, IOException {
		for (String s : currentPortfolio.getStocksInPortfolio()) {
			rebuildHistory(s);
		}
	}
	
	public void rebuildHistory(String omxId) throws MalformedURLException, IOException {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				m.rebuildHistory(omxId);
				break;
			}
		}
	}
	
	public Object[][] getHistory(int historyType) {
		return getHistory(historyType, 500);
	}
	
	public Object[][] getHistory(int historyType, int nbrOfDays) {
		Object[][] result = new Object[currentPortfolio.size()+1][];

		int i=1;
		for (String s : currentPortfolio.getStocksInPortfolio()) { 
			for (Market m : markets) {
				if (m.contains(s)) {
					result[0] = m.getStock(s).getHistory(nbrOfDays)[0];
					result[i++] = m.getStock(s).getHistory(nbrOfDays)[historyType];
					break;
				}
			}
		}
		return result;
	}
	
	private Portfolio buildDefaultPortfolio() throws MalformedURLException, IOException {
		Portfolio result = new Portfolio("default");
		defaultPortfolio = currentPortfolio = result;
		addStockToPortfolio("SSE3966");
		addStockToPortfolio("SSE18634");
		addStockToPortfolio("SSE402");
		addStockToPortfolio("SSE3524");
		addStockToPortfolio("SSE101");
		return result;
	}
}
