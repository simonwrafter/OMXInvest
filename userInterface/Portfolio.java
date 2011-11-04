package userInterface;

import java.util.*;

public class Portfolio {
	private SortedMap<String, Integer> stockmap;
	double liquidAsset;
	
	public Portfolio() {
		stockmap = new TreeMap<String, Integer>();
		liquidAsset = 0;
	}
	
	public boolean add(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return false;
		}
		stockmap.put(omxId, 0);
		return true;
	}
	
	public boolean buy(String omxId, int nbrOfStocks) {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, stockmap.get(omxId) + nbrOfStocks);
			return true;
		}
		return false;
	}
	
	public boolean sell(String omxId, int nbrOfStocks) {
		if (stockmap.containsKey(omxId) && nbrOfStocks<0) {
			stockmap.put(omxId, stockmap.get(omxId) - nbrOfStocks);
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
	
	public double remove(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return stockmap.remove(omxId);
		}
		return 0;
	}
	
	public String[] stocksInPortfolio() {
		Set<Map.Entry<String, Integer>> ent = stockmap.entrySet();
		String[] result = new String[ent.size()];
		int i = 0;
		for (Map.Entry<String, Integer> me : ent)
			result[i++] = me.getKey();
		return result;
	}
	
	public int[] shareDistribution() {
		Set<Map.Entry<String, Integer>> ent = stockmap.entrySet();
		int[] result = new int[ent.size()];
		int i = 0;
		for (Map.Entry<String, Integer> me : ent)
			result[i++] = me.getValue();
		return result;
	}
}
