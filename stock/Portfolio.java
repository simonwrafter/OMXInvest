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

import java.util.SortedMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Portfolio {
	private SortedMap<String, Integer> stockmap;
	private double liquidAsset;
	private String name;
	private double lambda;
	private Currency currency;
	
	public Portfolio(String name, Currency currency) {
		this(name, currency,  0);
	}
	
	public Portfolio(String name, Currency currency, double liquid) {
		this.name = name;
		this.currency = currency;
		stockmap = new TreeMap<String, Integer>();
		setLiquidAsset(liquid);
		setLambda(0.3);
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
	
	public void setLiquidAsset(double liquidAsset) {
		this.liquidAsset = liquidAsset;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public boolean add(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return false;
		}
		stockmap.put(omxId, 0);
		return true;
	}
	
	public double remove(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return stockmap.remove(omxId);
		}
		return 0;
	}
	
	public boolean buy(String omxId, int nbrOfStocks, double price) {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, stockmap.get(omxId) + nbrOfStocks);
			liquidAsset -= nbrOfStocks * price;
			return true;
		}
		return false;
	}
	
	public boolean sell(String omxId, int nbrOfStocks, double price) {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, stockmap.get(omxId) - nbrOfStocks);
			liquidAsset += nbrOfStocks * price;
			return true;
		}
		return false;
	}
	
	public int setOwn(String omxId, int nbrOfStocks) {
		if (stockmap.containsKey(omxId)) {
			int old = stockmap.get(omxId);
			stockmap.put(omxId, nbrOfStocks);
			return nbrOfStocks - old;
		}
		return 0;
	}
	
	public int size() {
		return stockmap.size();
	}
	
	public String[] getStocksInPortfolio() {
		Set<Map.Entry<String, Integer>> ent = stockmap.entrySet();
		String[] result = new String[ent.size()];
		int i = 0;
		for (Map.Entry<String, Integer> me : ent)
			result[i++] = me.getKey();
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

	public boolean contains(String omxId) {
		return stockmap.containsKey(omxId);
	}
}
