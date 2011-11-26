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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Investments {
	private final String base = System.getenv("HOME");
	private final String portfolioSaveFile = base + "/portfolios.omx";
	private final String marketSaveFile = base + "/markets.omx";
	private SortedSet<Portfolio> portfolios;
	private SortedMap<String, Market> markets;
	private Portfolio currentPortfolio;

	public Investments(JLabel label)
			throws ParserConfigurationException, SAXException, IOException {
		portfolios = new TreeSet<Portfolio>();
		markets = new TreeMap<String, Market>();
		
		System.out.println(marketSaveFile);
		
		buildMarkets(false, label);
		buildPortfolios(label);
	}
	
	@SuppressWarnings("unchecked")
	private void buildMarkets(boolean forceWeb, JLabel label)
			throws ParserConfigurationException, SAXException, IOException {
		if (forceWeb) {
			buildMarketsInternet(label);
		} else {
			try {
				label.setText("Rebuilding markets from file...");
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(marketSaveFile));
				markets = (TreeMap<String, Market>) in.readObject();
				System.out.println("built markets from file");
			} catch (Exception e) {
				buildMarketsInternet(label);
			}
		}
	}
	
	private void buildMarketsInternet(JLabel label)
			throws ParserConfigurationException, SAXException, IOException {
		for (String m : MarketData.arrayMarkets) {
			for (String c : MarketData.arrayCapital) {
				String marketName = m + " " + c;
				label.setText(marketName);
				markets.put(marketName, new Market(m, c));
				System.out.println("built " + marketName + " from web");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buildPortfolios(JLabel label)
			throws IOException, ParserConfigurationException, SAXException {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(portfolioSaveFile));
			label.setText("Rebuilding portfolios from file...");
			portfolios = (TreeSet<Portfolio>) in.readObject();
			for (Portfolio p : portfolios) {
				for (String id : p.getStocksInPortfolio()) {
					updateHistory(id);
				}
			}
			currentPortfolio = portfolios.first();
			System.out.println("built portfolios from file");
		} catch (Exception e) {
			label.setText("Building a firstrun portfolio");
			buildDefaultPortfolio();
			System.out.println("built new portfolio");
		}
	}
	
	public Collection<Market> getMarketSet() {
		return markets.values();
	}
	
	public SortedSet<Portfolio> getPortfolios() {
		return new TreeSet<Portfolio>(portfolios);
	}
	
	public boolean addNewPortfolio(String name, Currency currency, double liquid) {
		return portfolios.add(new Portfolio(name, currency, liquid));
	}
	
	public boolean removePortfolio(String name) {
		Portfolio newP = new Portfolio(name, null);
		if (currentPortfolio.compareTo(newP) == 0) {
			setCurrentPortfolio(portfolios.first());
		}
		return portfolios.remove(newP);
	}
	
	private void buildDefaultPortfolio()
			throws IOException, ParserConfigurationException, SAXException {
		addNewPortfolio("default", Currency.SEK, 10000);
		setCurrentPortfolio(portfolios.first());
		addStockToPortfolio("SSE3966");
		addStockToPortfolio("SSE18634");
		addStockToPortfolio("SSE402");
		addStockToPortfolio("SSE3524");
		addStockToPortfolio("SSE101");
	}

	public Portfolio getCurrentPortfolio() {
		return currentPortfolio;
	}

	public Portfolio setCurrentPortfolio(Portfolio portfolio) {
		Portfolio p = currentPortfolio;
		currentPortfolio = portfolio;
		return p;
	}
	
	public Portfolio setCurrentPortfolio(String portfolio) {
		for (Portfolio p : portfolios) {
			if (p.getName().equals(portfolio)) {
				return setCurrentPortfolio(p);
			}
		}
		return setCurrentPortfolio(portfolios.first());
	}
	
	public Stock getStock(String omxId) {
		for (Market m : markets.values()) {
			if (m.contains(omxId)) {
				return m.getStock(omxId);
			}
		}
		return null;
	}
	
	private String getIdOfStock(String name) {
		int i=0;
		for (String s : getStockNames()) {
			if (s.equals(name))
				break;
			i++;
		}
		if (i >= currentPortfolio.size())
			return null;
		return getStockIds()[i];
	}
	
	public boolean addStockToPortfolio(String omxId) 
			throws IOException, ParserConfigurationException, SAXException {
		if (getCurrency(omxId).equals(currentPortfolio.getCurrency())) {
			rebuildHistory(omxId);
			currentPortfolio.add(omxId);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeStockfromPortfolio(String omxId)
			throws ParserConfigurationException {
		if (!currentPortfolio.contains(omxId)) {
			return false;
		}
		double value = currentPortfolio.remove(omxId);
		currentPortfolio.setLiquidAsset(value * getLastValue(omxId, 3) + currentPortfolio.getLiquidAsset());
		return true;
	}
	
	public boolean removeStockfromPortfolioByName(String name)
			throws ParserConfigurationException {
		String id = getIdOfStock(name);
		if (id == null) {return false;}
		return removeStockfromPortfolio(id);
	}
	
	public void updateMarkets() {
		for (String m : MarketData.arrayMarkets) {
			for (String c : MarketData.arrayCapital) {
				try {
					markets.get(m + " " + c).updateMarket(m, c);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateHistory()
			throws IOException, ParserConfigurationException, SAXException {
		for (String s : getStockIds()) {
			rebuildHistory(s);
		}
	}
	
	private boolean updateHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		for (Market m : markets.values()) {
			if (m.contains(omxId)) {
				m.updateHistory(omxId);
				return true;
			}
		}
		return false;
	}

	public void rebuildHistory()
			throws IOException, ParserConfigurationException, SAXException {
		for (String s : getStockIds()) {
			rebuildHistory(s);
		}
	}
	
	private boolean rebuildHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		for (Market m : markets.values()) {
			if (m.contains(omxId)) {
				m.rebuildHistory(omxId);
				return true;
			}
		}
		return false;
	}
	
	public Double[][] getHistory(int historyType) {
		return getHistory(historyType, 500);
	}
	
	public Double[][] getHistory(int historyType, int nbrOfDays) {
		if (size() == 0) {
			return new Double[][] {{0.0}};
		}
		Double[][] result = new Double[size()+1][];
		int i=1;
		for (String s : getStockIds()) { 
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
	
	public Double[] getLastValue(int type)
			throws ParserConfigurationException {
		Double[] result = new Double[size()];
		String[] ids = getStockIds();
		for (int i=0; i<size(); i++) {
			result[i] = getLastValue(ids[i], type);
		}
		return result;
	}
	
	public Double getLastValue(String omxId, int type)
			throws ParserConfigurationException {
		for (Market m : markets.values()) {
			if (m.contains(omxId)) {
				return m.getStock(omxId).getLastValues()[type];
			}
		}
		return null;
	}

	public String[] getStockIds() {
		return currentPortfolio.getStocksInPortfolio();
	}
	
	public String[] getStockNames() {
		String[] stocks = getStockIds();
		String[] result = new String[stocks.length];
		for (int i=0; i<stocks.length; i++) {
			result[i] = getStock(stocks[i]).getFullName();
		}
		return result;
	}
	
	public String[] getShortNames() {
		String[] stocks = getStockIds();
		String[] result = new String[stocks.length];
		for (int i=0; i<stocks.length; i++) {
			result[i] = getStock(stocks[i]).getShortName();
		}
		return result;
	}
	
	public Integer[] getShareDistribution() {
		return currentPortfolio.getShareDistribution();
	}
	
	public Double[] getValueDistribution() {
		Double[] dist = getValueByShare();
		double sum = getValueSum();
		for (int i=0; i<currentPortfolio.size();i++) {
			dist[i] /= sum;
		}
		return dist;
	}
	
	public Double[] getValueByShare() {
		Double[][] latest = getHistory(4, 1);
		Integer[] nbrOf = currentPortfolio.getShareDistribution();
		Double result[] = new Double[currentPortfolio.size()];
		for(int i=0; i<nbrOf.length; i++) {
			result[i] = nbrOf[i] * latest[i+1][0];
		}
		return result;
	}

	public double getValueSum() {
		double result=0;
		Double[] values = getValueByShare();
		for(int i=0; i<values.length; i++) {
			result += values[i];
		}
		return result;
	}
	
	public double getLiquid() {
		return currentPortfolio.getLiquidAsset();
	}
	
	public void setLiquid(double value) {
		currentPortfolio.setLiquidAsset(value);
	}

	public double getLambda() {
		return currentPortfolio.getLambda();
	}
	
	public void setLambda(double lambda) {
		currentPortfolio.setLambda(lambda);
	}
	
	private Currency getCurrency(String omxId) {
		Stock s = getStock(omxId);
		if (s == null) { return null; }
		return s.getCurrency();
	}
	
	public int size() {
		return currentPortfolio.size();
	}
	
	public boolean containsByName(String fullName) {
		for (String s : getStockNames())
			if (s.equals(fullName))
				return true;
		return false;
	}
	
	public boolean buy(String name, Integer nbrOfStocks)
			throws ParserConfigurationException {
		String id = getIdOfStock(name);
		return currentPortfolio.buy(id, nbrOfStocks, getLastValue(id, 2));
	}
	
	public boolean sell(String name, Integer nbrOfStocks)
			throws ParserConfigurationException {
		String id = getIdOfStock(name);
		return currentPortfolio.sell(id, nbrOfStocks, getLastValue(id, 3));
	}

	public String getPortfolioName() {
		return currentPortfolio.getName();
	}
	
	public void setPortfolioName(String name) {
		currentPortfolio.setName(name);
	}

	public Currency getCurrency() {
		return currentPortfolio.getCurrency();
	}
	
	public int nbrOfMarkets() {
		return markets.size();
	}
	
	public void save() {
		try {
			new ObjectOutputStream(new FileOutputStream(portfolioSaveFile)).writeObject(portfolios);
			System.out.println("saved portfolio");
			new ObjectOutputStream(new FileOutputStream(marketSaveFile)).writeObject(markets);
			System.out.println("save successful");
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("save failed");
		}
	}
}
