package userInterface;

import java.util.*;

public class Portfolio {
	private Map<String, Double> stockmap;
	
	public Portfolio() {
		stockmap = new HashMap<String, Double>();
	}
	
	public boolean add(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return false;
		}
		stockmap.put(omxId, 0.);
		return true;
	}
	
	public boolean buy(String omxId, double nbrOfStocks) {
		if (stockmap.containsKey(omxId) && nbrOfStocks>0) {
			stockmap.put(omxId, nbrOfStocks);
			return true;
		}
		return false;
	}
	
	public boolean sell(String omxId, double nbrOfStocks) {
		if (stockmap.containsKey(omxId) && nbrOfStocks<0) {
			stockmap.put(omxId, nbrOfStocks);
			return true;
		}
		return false;
	}
	
	public double remove(String omxId) {
		if (stockmap.containsKey(omxId)) {
			return stockmap.remove(omxId);
		}
		return 0;
	}
}
