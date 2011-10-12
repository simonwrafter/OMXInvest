/*
 * Copyright (c) 2011, Simon Wrafter <simon.wrafter@gmail.com>
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

import java.util.*;
import java.io.IOException;
import java.net.*;

import net.htmlparser.jericho.*;

public class StockFetcher {

	//			LC			MC		SC			?
	private final String[][] arrayLists = {
			//{"L:10214","L:10216","L:10218","L:10150"}, // nordic (EUR)
			{"L:10220","L:10222","L:10224"}, // cop (L:3244)
			{"L:10208","L:10210","L:10212"}, // sto (L:3236)
			{"L:10196","L:10198","L:10200"}, // hel (L:3228)
	};
	private final String[] arrayMarkets = {/* "Nordic", */ "Copenhagen", "Stockholm", "Helsinki"};
	private final String[] arrayCapital = {"Large Cap", "Medium Cap", "Small Cap" /* , "??" */};

	private final String stockListURL = 
			"http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?" +
					"SubSystem=Prices" +
					"&Action=Search" +
					"&inst.an=nm,fnm,isin,tp,cr" +
					"&InstrumentType=S" +
					"&List=" +
					"";

	//private Source omxStockList;
	private Map<String, StockData> stockMap;

	public StockFetcher() {
		System.out.println("constructor");
		stockMap = buildMap();
	}


	// FOR TESTING PURPOSES!! WILL BE REMOVED AT SOME POINT!
	public static void main(String[] args) {
		System.out.println("main");
		new StockFetcher().run();
		System.out.println("done");
	}
	private void run() {
		print();
	}
	// FOR TESTING PURPOSES!! WILL BE REMOVED AT SOME POINT!
	
	private void print() {
		for (StockData s : stockMap.values())
			System.out.println(s);
	}
	
	private Map<String, StockData> buildMap() {
		System.out.println("builder");
		Map<String, StockData> returnMap = new HashMap<String, StockData>();

		for (int i=0; i<3; i++) { //market
			for (int j=0; j<3; j++) { //cap
				System.out.println(i + " " + j);
				Source omxStockSource = null;
				try {
					omxStockSource = new Source(new URL(stockListURL + arrayLists[i][j]));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				List<Element> elist = omxStockSource.getAllElements("inst");

				for (Element e : elist) {
					returnMap.put(e.getAttributeValue("id"), 
									new StockData(
											e.getAttributeValue("id"),
											e.getAttributeValue("nm"),
											e.getAttributeValue("fnm"),
											e.getAttributeValue("isin"),
											arrayMarkets[i] + " " + arrayCapital[j],
											e.getAttributeValue("cr")));
				}
			}
		}
		return returnMap;
	}
}
