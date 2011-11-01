package userInterface;

import stock.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class MarketHolder {
	private Map<String, StockData> availableStocks;
	private boolean[][] addedMarkets;
	
	public MarketHolder() {
		availableStocks = new HashMap<String, StockData>();
		addedMarkets = new boolean[4][4];
	}
	
	public boolean addMarket(String market, String cap) throws MalformedURLException, IOException {
		int[] index;
		index = MarketData.marketIndex(market, cap);

		if (!addedMarkets[index[0]][index[1]]) {
			availableStocks.putAll(StockFetcher.buildStockMap(market, cap));
			return true;
		}
		return false;
	}
	
	public void rebuildHistory(String omxId) throws MalformedURLException, IOException {
		availableStocks.get(omxId).rebuildHistory();
	}
	
	public void updateHistory(String omxId) throws MalformedURLException, IOException {
		availableStocks.get(omxId).updateHistory();
	}
	
	public StockData getStock(String omxId) {
		return availableStocks.get(omxId);
	}
}
