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
import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Market implements Comparable<Market>, Serializable {
	private static final long serialVersionUID = 1283431577549478467L;
	private SortedMap<String, Stock> availableStocks;
	private String listName;
	
	public Market(String market, String capital)
			throws ParserConfigurationException, SAXException, IOException {
		int[] i = MarketData.marketIndex(market, capital);
		listName = MarketData.arrayMarkets[i[0]] + " " + MarketData.arrayCapital[i[1]]; 
		availableStocks = new TreeMap<String, Stock>();
		buildStockMap(market, capital);
	}
	
	public String getName() {
		return listName;
	}
	
	public SortedMap<String, Stock> getMarketMap() {
		return new TreeMap<String, Stock>(availableStocks);
	}
	
	public void updateMarket(String market, String cap)
			throws ParserConfigurationException, SAXException, IOException {
		int[] index = MarketData.marketIndex(market, cap);
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		boolean success = false;
		Document doc = null;
		int times = 0;
		while (!success && times <= 5) {
			try {
				doc = db.parse(MarketData.buildListURL(index[0], index[1]));
				success = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			times += 1;
		}
		NodeList nl = doc.getElementsByTagName("inst");

		for (int i=0; i<nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			if (!availableStocks.containsKey(e.getAttribute("id")))
				availableStocks.put(e.getAttribute("id"), buildStock(e));
		}
	}
	
	public boolean contains(String omxId) {
		if (!availableStocks.isEmpty() && omxId.substring(0, 3).equals(availableStocks.firstKey().substring(0, 3)))
			return availableStocks.containsKey(omxId);
		return false;
	}
	
	public boolean rebuildHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		Stock s = getStock(omxId);
		if (s == null) { return false; }
		s.rebuildHistory();
		return true;
	}
	
	public boolean updateHistory(String omxId)
			throws IOException, ParserConfigurationException, SAXException {
		Stock s = getStock(omxId);
		if (s == null) { return false; }
		s.updateHistory();
		return true;
	}
	
	public Stock getStock(String omxId) {
		if (omxId.substring(0, 3).equals(availableStocks.firstKey().substring(0, 3)))
			return availableStocks.get(omxId);
		return null;
	}
	
	private void buildStockMap(String market, String cap)
			throws ParserConfigurationException, SAXException, IOException {
		int[] index = MarketData.marketIndex(market, cap);
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		boolean success = false;
		Document doc = null;
		int times = 0;
		while (!success && times <= 5) {
			try {
				doc = db.parse(MarketData.buildListURL(index[0], index[1]));
				success = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			times += 1;
		}
		NodeList nl = doc.getElementsByTagName("inst");
		
		for (int i=0; i<nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			availableStocks.put(e.getAttribute("id"), buildStock(e));
		}
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
	
	@Override
	public String toString() {
		return listName;
	}
}
