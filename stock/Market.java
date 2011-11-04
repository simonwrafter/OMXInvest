package stock;

import java.io.*;
import java.net.*;
import java.util.*;

import net.htmlparser.jericho.*;

public class Market {
	private Map<String, Stock> availableStocks;
	private String listName;
	
	public Market(String market, String capital) throws MalformedURLException, IOException {
		buildStockMap(market, capital);
	}
	
	public String getListName() {
		return listName;
	}
	
	public void rebuildHistory(String omxId) throws MalformedURLException, IOException {
		availableStocks.get(omxId).rebuildHistory();
	}
	
	public void updateHistory(String omxId) throws MalformedURLException, IOException {
		availableStocks.get(omxId).updateHistory();
	}
	
	public Stock getStock(String omxId) {
		return availableStocks.get(omxId);
	}
	
	private void buildStockMap(String market, String cap)
			throws MalformedURLException, IOException {
		
		int[] index = MarketData.marketIndex(market, cap);
		
		Source omxStockSource = new Source(new URL(MarketData.buildListURL(index[0], index[1])));
		listName = MarketData.arrayMarkets[index[0]] + " " + MarketData.arrayCapital[index[1]];
		
		for (Element e : omxStockSource.getAllElements("inst")) {
			availableStocks.put(e.getAttributeValue("id"), buildStockData(e, listName));
		}
	}
	
	private Stock buildStockData(Element e, String listName)
			throws MalformedURLException, IOException {
		return new Stock(
				e.getAttributeValue("id"),
				e.getAttributeValue("nm"),
				e.getAttributeValue("fnm"),
				e.getAttributeValue("isin"),
				listName,
				e.getAttributeValue("cr"));
	}
}
