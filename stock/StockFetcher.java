package stock;

import java.util.*;

import net.htmlparser.jericho.*;

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
