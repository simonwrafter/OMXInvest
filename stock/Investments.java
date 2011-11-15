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

package stock;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Investments {
	private SortedSet<Portfolio> portfolios;
	private SortedSet<Market> markets;
	private Portfolio defaultPortfolio;
	private Portfolio currentPortfolio;

	public Investments(JLabel label)
			throws ParserConfigurationException, SAXException, IOException, NamingException {
		portfolios = new TreeSet<Portfolio>();
		markets = new TreeSet<Market>();

		for (String s : MarketData.arrayMarkets) {
			for (String t : MarketData.arrayCapital) {
				label.setText(s + " " + t);
				markets.add(new Market(s, t));
			}
		}
		label.setText("Fetching History");
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
	
	public void addStockToPortfolio(String omxId)
			throws IOException, ParserConfigurationException, SAXException, NamingException {
		if (getCurrency(omxId).equals(currentPortfolio.getCurrency())) {
			rebuildHistory(omxId);
			currentPortfolio.add(omxId);
		} else {
			throw new NamingException(omxId + " does not match portfolio currency");
		}
	}

	private Currency getCurrency(String omxId) {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				return m.getStock(omxId).getCurrency();
			}
		}
		throw new NoSuchElementException(omxId + " is not a known stock.");
	}

	public void removeStockfromPortfolio(String omxId) {
		if (!currentPortfolio.contains(omxId)) {
			throw new NoSuchElementException(omxId + " is not a registered stock.");
		}
		double value = currentPortfolio.remove(omxId);
		currentPortfolio.setLiquidAsset(value * getLastValue(omxId) + currentPortfolio.getLiquidAsset());
	}

	public void updateHistory()
			throws IOException, ParserConfigurationException, SAXException {
		for (String s : currentPortfolio.getStocksInPortfolio()) {
			rebuildHistory(s);
		}
	}
	
	public void updateHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				m.updateHistory(omxId);
				return;
			}
		}
		throw new NoSuchElementException(omxId + " is not a known stock.");
	}

	public void rebuildHistory()
			throws IOException, ParserConfigurationException, SAXException {
		for (String s : currentPortfolio.getStocksInPortfolio()) {
			rebuildHistory(s);
		}
	}
	
	public void rebuildHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				m.rebuildHistory(omxId);
				return;
			}
		}
		throw new NoSuchElementException(omxId + " is not a known stock.");
	}
	
	public Double[][] getHistory(int historyType) {
		return getHistory(historyType, 500);
	}
	
	public Double[][] getHistory(int historyType, int nbrOfDays) {
		Double[][] result = new Double[currentPortfolio.size()+1][];
		int i=1;
		for (String s : currentPortfolio.getStocksInPortfolio()) { 
			for (Market m : markets) {
				if (m.contains(s)) {
					if (result[0] == null)
						result[0] = m.getStock(s).getHistory(nbrOfDays)[0];
					result[i++] = m.getStock(s).getHistory(nbrOfDays)[historyType];
					break;
				}
			}
		}
		return result;
	}
	
	private Portfolio buildDefaultPortfolio()
			throws IOException, ParserConfigurationException, SAXException, NamingException {
		Portfolio result = new Portfolio("default", Currency.SEK, 10000);
		defaultPortfolio = currentPortfolio = result;
		addStockToPortfolio("SSE3966");
		addStockToPortfolio("SSE18634");
		addStockToPortfolio("SSE402");
		addStockToPortfolio("SSE3524");
		addStockToPortfolio("SSE101");
		return result;
	}
	
	public double getLastValue(String omxId) {
		for (Market m : markets) {
			if (m.contains(omxId)) {
				return m.getStock(omxId).getHistory(1)[4][0];
			}
		}
		return 0;
	}

	public double getPortfolioValue() {
		double result=0;
		Double[][] values = getHistory(4, 1);
		Integer[] nbrOf = currentPortfolio.getShareDistribution();
		
		for(int i=0; i<nbrOf.length; i++) {
			result += nbrOf[i] * values[i+1][0];
		}
		
		return result;
	}
	
	public double getPortfolioLiquid() {
		return currentPortfolio.getLiquidAsset();
	}
	
	public void setPortfolioLiquid(double value) {
		currentPortfolio.setLiquidAsset(value);
	}
	
	public String[] getStockNames() {
		String[] stocks = currentPortfolio.getStocksInPortfolio();
		String[] result = new String[stocks.length];
		for (int i=0; i<stocks.length; i++) {
			for (Market m : markets) {
				if (m.contains(stocks[i])) {
					result[i] = m.getStock(stocks[i]).getFullName();
					break;
				}
			}
		}
		return result;
	}
	
	public String[] getShortNames() {
		String[] stocks = currentPortfolio.getStocksInPortfolio();
		String[] result = new String[stocks.length];
		for (int i=0; i<stocks.length; i++) {
			for (Market m : markets) {
				if (m.contains(stocks[i])) {
					result[i] = m.getStock(stocks[i]).getShortName();
					break;
				}
			}
		}
		return result;
	}

	public String[] getStockIds() {
		return currentPortfolio.getStocksInPortfolio();
	}
}
