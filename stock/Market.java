package stock;

import java.io.*;
import java.net.*;
import java.util.*;

import net.htmlparser.jericho.*;

public class Market implements Comparable<Market> {
	private SortedMap<String, Stock> availableStocks;
	private String listName;
	
	public Market(String market, String capital) throws MalformedURLException, IOException {
		int[] i = MarketData.marketIndex(market, capital);
		listName = MarketData.arrayMarkets[i[0]] + MarketData.arrayCapital[i[1]]; 
		availableStocks = new TreeMap<String, Stock>();
		buildStockMap(market, capital);
	}
	
	public String getListName() {
		return listName;
	}
	
	public SortedMap<String, Stock> getMarketMap() {
		return availableStocks;
	}
	
	public boolean contains(String omxId) {
		return availableStocks.containsKey(omxId);
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
			availableStocks.put(e.getAttributeValue("id"), buildStock(e, listName));
		}
		System.out.println(availableStocks);
	}
	
	private Stock buildStock(Element e, String listName)
			throws MalformedURLException, IOException {
		return new Stock(
				e.getAttributeValue("id"),
				e.getAttributeValue("nm"),
				e.getAttributeValue("fnm"),
				e.getAttributeValue("isin"),
				listName,
				e.getAttributeValue("cr"));
	}

	@Override
	public int compareTo(Market o) {
		return listName.compareTo(o.listName);
	}
}
