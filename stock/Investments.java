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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Investments {
	private final String portfolioSaveFile = "portfolios.pfo";
	private final String marketSaveFile= "markets.mkt";
	private SortedSet<Portfolio> portfolios;
	private SortedMap<String, Market> markets;
	private Portfolio defaultPortfolio;
	private Portfolio currentPortfolio;

	public Investments(JLabel label)
			throws ParserConfigurationException, SAXException, IOException, NamingException {
		portfolios = new TreeSet<Portfolio>();
		markets = new TreeMap<String, Market>();

		buildMarkets(false, label);
		buildPortfolios();
		label.setText("Fetching History");
	}
	
	@SuppressWarnings("unchecked")
	public void buildMarkets(boolean forceWeb, JLabel label) throws ParserConfigurationException, SAXException, IOException {
		if (forceWeb) {
			buildMarketsInternet(label);
		} else {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(marketSaveFile));
				markets = (TreeMap<String, Market>) in.readObject();
				System.out.println("built markets from file");
			} catch (Exception e) {
				buildMarketsInternet(label);
			}
		}
	}
	
	public void buildMarketsInternet(JLabel label) throws ParserConfigurationException, SAXException, IOException {
		for (String m : MarketData.arrayMarkets) {
			for (String c : MarketData.arrayCapital) {
				String marketName = m + " " + c;
				label.setText(marketName);
				markets.put(marketName, new Market(m, c));
				System.out.println("build " + marketName + " from web");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buildPortfolios()
			throws IOException, ParserConfigurationException, SAXException, NamingException {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(portfolioSaveFile));
			portfolios = (TreeSet<Portfolio>) in.readObject();
			for (Portfolio p : portfolios) {
				if (p.isDefaultPortfolio()) {
					currentPortfolio = defaultPortfolio = p;
				}
			}
			updateHistory();
			System.out.println("build portfolios from file");
		} catch (Exception e) {
			buildDefaultPortfolio();
			portfolios.add(defaultPortfolio);
			System.out.println("build new portfolio");
		}
	}
	
	public Collection<Market> getMarketSet() {
		return markets.values();
	}

	public Portfolio getDefaultPortfolio() {
		return defaultPortfolio;
	}

	public Portfolio setDefaultPortfolio(Portfolio portfolio) {
		Portfolio p = defaultPortfolio;
		defaultPortfolio = portfolio;
		p.setDefaultPortfolio(false);
		defaultPortfolio.setDefaultPortfolio(true);
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
		for (Market m : markets.values()) {
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
		for (Market m : markets.values()) {
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
		for (Market m : markets.values()) {
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
			for (Market m : markets.values()) {
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
		for (Market m : markets.values()) {
			if (m.contains(omxId)) {
				return m.getStock(omxId).getHistory(1)[4][0];
			}
		}
		return 0;
	}
	
	public Double[] getPortfolioValueDistributed() {
		Double[][] values = getHistory(4, 1);
		Integer[] nbrOf = currentPortfolio.getShareDistribution();
		Double result[] = new Double[currentPortfolio.size()];
		for(int i=0; i<nbrOf.length; i++) {
			result[i] = nbrOf[i] * values[i+1][0];
		}
		return result;
	}

	public double getPortfolioValueSum() {
		double result=0;
		Double[] values = getPortfolioValueDistributed();
		Integer[] nbrOf = currentPortfolio.getShareDistribution();
		for(int i=0; i<nbrOf.length; i++) {
			result += nbrOf[i] * values[i];
		}
		return result;
	}
	
	public Double[] getPortfolioDistribution() {
		Double[] dist = getPortfolioValueDistributed();
		double sum = getPortfolioValueSum();
		for (int i=0; i<currentPortfolio.size();i++) {
			dist[i] /= sum;
		}
		return dist;
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
			for (Market m : markets.values()) {
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
			for (Market m : markets.values()) {
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

	public void save() {
		try {
			new ObjectOutputStream(new FileOutputStream(portfolioSaveFile)).writeObject(portfolios);
			System.out.println("saved portfolio");
			new ObjectOutputStream(new FileOutputStream(marketSaveFile)).writeObject(markets);
			System.out.println("save successful");
		} catch (Exception e) {
			System.out.println("save failed");
		}
	}
}
