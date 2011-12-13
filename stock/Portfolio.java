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

import java.io.Serializable;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import util.InvestDate;

public class Portfolio implements Comparable<Portfolio>, Serializable {
	private static final long serialVersionUID = -4320867018986118171L;
	private SortedMap<String, Integer> stockmap;
	private double liquidAsset;
	private String name;
	private double lambda;
	private Currency currency;
	private PortfolioEvents events;
	private Investments invest;
	
	private Double[] latestBuy;
	private Double[] latestSell;
	
	public Portfolio(String name, Currency currency, Investments invest)
			throws ParserConfigurationException {
		this(name, currency, 0, invest);
	}
	
	public Portfolio(String name, Currency currency, double liquid, Investments invest)
			throws ParserConfigurationException {
		this.name = name;
		this.currency = currency;
		this.stockmap = new TreeMap<String, Integer>();
		this.invest = invest;
		this.liquidAsset = liquid;
		this.events = new PortfolioEvents();
		this.latestBuy = null;
		this.latestSell = null;
		setLambda(0.3);
	}
	
	public void updateLatestBuy()
			throws ParserConfigurationException {
		latestBuy = invest.getLastValue(2);
	}
	
	public void updateLatestSell()
			throws ParserConfigurationException {
		latestSell = invest.getLastValue(3);
	}
	
	public Double[] getLatestBuy() {
		return latestBuy;
	}
	
	public Double[] getLatestSell() {
		return latestSell;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public double getLiquidAsset() {
		return liquidAsset;
	}
	
	public void setLiquidAsset(double liquidAsset)
			throws ParserConfigurationException {
		double old = this.liquidAsset;
		this.liquidAsset = liquidAsset;
		events.addEvent("set liquid asset", old-liquidAsset, liquidAsset);
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public boolean contains(String omxId) {
		return stockmap.containsKey(omxId);
	}
	
	public boolean add(String omxId)
			throws ParserConfigurationException {
		if (stockmap.containsKey(omxId)) {
			return false;
		}
		stockmap.put(omxId, 0);
		events.addEvent("Stock added to portfolio", omxId, invest.getShortName(omxId));
		updateLatestBuy();
		updateLatestSell();
		return true;
	}
	
	public double remove(String omxId) throws ParserConfigurationException {
		if (stockmap.containsKey(omxId)) {
			events.addEvent("Stock removed from portfolio", omxId, invest.getShortName(omxId));
			updateLatestBuy();
			updateLatestSell();
			return stockmap.remove(omxId);
		}
		return 0;
	}
	
	public boolean buy(String omxId, int nbrOfStocks, double price)
			throws ParserConfigurationException {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, stockmap.get(omxId) + nbrOfStocks);
			liquidAsset -= nbrOfStocks * price;
			events.addEvent("shares bought", nbrOfStocks, nbrOfStocks * price);
			return true;
		}
		return false;
	}
	
	public boolean sell(String omxId, int nbrOfStocks, double price)
			throws ParserConfigurationException {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, stockmap.get(omxId) - nbrOfStocks);
			liquidAsset += nbrOfStocks * price;
			events.addEvent("shares sold", -1 * nbrOfStocks, nbrOfStocks * price);
			return true;
		}
		return false;
	}
	
	public int setOwn(String omxId, int nbrOfStocks, double price)
			throws ParserConfigurationException {
		if (stockmap.containsKey(omxId)) {
			int old = stockmap.get(omxId);
			int change = nbrOfStocks - old;
			if (change<0) {
				sell(omxId, change, price);
			} else if (change>0) {
				buy(omxId, change, price);
			}
			return change;
		}
		return 0;
	}
	
	public int size() {
		return stockmap.size();
	}
	
	public String[] getStocksInPortfolio() {
		Set<String> ids = stockmap.keySet();
		String[] result = new String[ids.size()];
		int i = 0;
		for (String s : ids)
			result[i++] = s;
		return result;
	}
	
	public Integer[] getShareDistribution() {
		Set<Map.Entry<String, Integer>> ent = stockmap.entrySet();
		Integer[] result = new Integer[ent.size()];
		int i = 0;
		for (Map.Entry<String, Integer> me : ent)
			result[i++] = me.getValue();
		return result;
	}
	
	public Object[][] getEvents() {
		return events.events;
	}
	
	public int nbrOfEvents() {
		return events.size;
	}

	@Override
	public int compareTo(Portfolio o) {
		return name.compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	private class PortfolioEvents implements Serializable{
		private static final long serialVersionUID = 4801644126557869302L;
		private Object[][] events;
		int size;
		/* 
		 * vector	meaning
		 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		 * {
		 *  [][0] = date
		 *  [][1] = time
		 *  [][2] = message
		 *  [][3] = change
		 *  [][4] = change value
		 *  [][5] = liquid
		 *  [][6] = invested value
		 * }
		 */
		
		private PortfolioEvents()
				throws ParserConfigurationException {
			this.events = new Object[10][7];
			this.size = 1;
			events[0] = new Object[] {InvestDate.today(), InvestDate.currentTime(), 
					"initiate portfolio", liquidAsset, liquidAsset,
					liquidAsset, 0};
		}
		
		private void addEvent(String message, Object change, Object changeValue)
				throws ParserConfigurationException {
			events[size++] = new Object[] {InvestDate.today(), InvestDate.currentTime(),
					message, change, changeValue,
					getLiquidAsset(), invest.getValueSum()};
			if (size >= events.length) {
				Arrays.copyOf(events, size*2);
			}
		}
	}
}
