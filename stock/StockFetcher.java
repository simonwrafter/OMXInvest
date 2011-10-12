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

import net.htmlparser.jericho.*;

/**
 * @author simon
 *
 */
public class StockFetcher {
	
	//		LC			,MC		,SC
	private final String[][] listNames = {
			{"L:10214","L:10216","L:10218","L:10150"}, // nordic (EUR)
			{"L:10220","L:10222","L:10224"}, // cop (L:3244)
			{"L:10208","L:10210","L:10212"}, // sto (L:3236)
			{"L:10196","L:10198","L:10200"}, // hel (L:3228)
			{"L:10238","L:10240","L:10242"}  // isk (L:3498)
	};
	
	private final String stockListURL = 
			"http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?" +
			"SubSystem=Prices" +
			"&Action=Search" +
			"&inst.an=nm,fnm,isin,tp,cr" +
			"&InstrumentType=S" +
			"";
	
	
	// FOR TESTING PURPOSES!! WILL BE REMOVED AT SOME POINT!
	public static void main(String[] args) {
		new StockFetcher();
	}
	
	
	private Source omxStockList;
	private Map<String, StockData> stockMap;
	
	
	public StockFetcher() {
		omxStockList = new Source(stockListURL);
		stockMap = buildMap();
	}
	
	
	
	
	private HashMap<String, StockData> buildMap() {
		return null;
		
	}




			
	
}
