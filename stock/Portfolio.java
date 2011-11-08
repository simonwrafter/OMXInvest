package stock;

import java.util.*;

public class Portfolio {
	private SortedMap<String, Integer> stockmap;
	private double liquidAsset;
	private String name;
	private double lambda;
	
	public Portfolio(String name) {
		this(name, 0);
	}
	
	public Portfolio(String name, double liquid) {
		this.name = name;
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
}
