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

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Market implements Comparable<Market> {
	private SortedMap<String, Stock> availableStocks;
	private String listName;
	
	public Market(String market, String capital)
			throws ParserConfigurationException, SAXException, IOException {
		int[] i = MarketData.marketIndex(market, capital);
		listName = MarketData.arrayMarkets[i[0]] + " " + MarketData.arrayCapital[i[1]]; 
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
	
	public void rebuildHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		availableStocks.get(omxId).rebuildHistory();
	}
	
	public void updateHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		availableStocks.get(omxId).updateHistory();
	}
	
	public Stock getStock(String omxId) {
		return availableStocks.get(omxId);
	}
	
	private void buildStockMap(String market, String cap)
			throws ParserConfigurationException, SAXException, IOException {
		int[] index = MarketData.marketIndex(market, cap);
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse(MarketData.buildListURL(index[0], index[1]));
		NodeList nl = doc.getElementsByTagName("inst");
		
		for (int i=0; i<nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			availableStocks.put(e.getAttribute("id"), buildStock(e));
		}
		System.out.println(availableStocks);
	}
	
	private Stock buildStock(Element e) {
		return new Stock(
				e.getAttribute("id"),
				e.getAttribute("nm"),
				e.getAttribute("fnm"),
				e.getAttribute("isin"),
				listName,
				Currency.getCurrency(e.getAttribute("cr")));
	}

	@Override
	public int compareTo(Market o) {
		return listName.compareTo(o.listName);
	}
}
