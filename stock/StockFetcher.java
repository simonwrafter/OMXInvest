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

	//private Source omxStockList;
	private Map<String, StockData> stockMap;

	public StockFetcher() {
		System.out.println("constructor");
		stockMap = buildMap();
	}


	// FOR TESTING PURPOSES!! WILL BE REMOVED AT SOME POINT!
	public static void main(String[] args) {				//
		System.out.println("main");							//
		new StockFetcher().run();							//
		System.out.println("done");							//
	}														//
	private void run() {									//
		//print();											//
	}														//
	// FOR TESTING PURPOSES!! WILL BE REMOVED AT SOME POINT!
	
	private void print() {
		for (StockData s : stockMap.values())
			System.out.println(s);
	}
	
	private Map<String, StockData> buildMap() {
		System.out.println("builder");
		Map<String, StockData> returnMap = new HashMap<String, StockData>();

		for (int market=0; market<3; market++) { //market
			for (int cap=0; cap<3; cap++) { //cap
				Source omxStockSource = null;
				try {
					omxStockSource = new Source(new URL(MarketData.buildListURL(market, cap)));
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.exit(1);
				} catch (IOException e) {
					e.printStackTrace();
					return buildMap();
				}
				
				System.out.println(market + " " + cap);
				for (Element e : omxStockSource.getAllElements("inst")) {
					returnMap.put(e.getAttributeValue("id"), buildStockData(e, market, cap));
				}
			}
		}
		return returnMap;
	}
	
	private StockData buildStockData(Element e, int market, int cap) {
		return new StockData(
				e.getAttributeValue("id"),
				e.getAttributeValue("nm"),
				e.getAttributeValue("fnm"),
				e.getAttributeValue("isin"),
				MarketData.arrayMarkets[market] + " " + MarketData.arrayCapital[cap],
				e.getAttributeValue("cr"));
	}
}
