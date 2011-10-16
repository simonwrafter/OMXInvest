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

public class MarketData {
//		  LC		MC		  SC		?
	public static final String[][] matrixLists = {
//		{"L:10214","L:10216","L:10218","L:10150"},	// nordic (EUR)
		{"L:10220","L:10222","L:10224"},			// cop (L:3244)
		{"L:10208","L:10210","L:10212"},			// sto (L:3236)
		{"L:10196","L:10198","L:10200"},			// hel (L:3228)
	};
	public static final String[] arrayMarkets = {/* "Nordic", */ "Copenhagen", "Stockholm", "Helsinki"};
	public static final String[] arrayCapital = {"Large Cap", "Medium Cap", "Small Cap" /* , "??" */};
	
	
	public static final String proxyURL = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?";
	
	public static final String listURL = "SubSystem=Prices&Action=Search&inst.an=nm,fnm,isin,tp,cr&InstrumentType=S&List=";
	
	public static final String historyURLstart = "SubSystem=History&Action=GetDataSeries&Instrument=";
	public static final String fromDate = "&fromDate=";
	public static final String toDate = "&toDate=";
	
	public static final String newsURL = "https://newsclient.omxgroup.com/cdsPublic/viewDisclosure.action?";
	public static final String newsId = "disclosureId=";
	public static final String newslang = "&lang=";
	
	public static final String historyAdjusted = "&historyadjusted=true"; // not right :(
	public static final String realtime = "&realtime=true"; // might come in handy
	
	/*
	 * Avista: 
	 *	http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?SubSystem=Prices&Action=GetInstrument&Instrument=SSE3966&inst__a=0,1,2,37,20,21,23,24,35,36,39,10&Exception=false&ext_xslt=inst_table.xsl&ext_xslt_lang=sv&ext_xslt_tableId=avistaTable&ext_xslt_options=noflag,nolink
	 *
	 * history graph:
	 *  http://www.nasdaqomxnordic.com/charting/ChartingBin.aspx?width=630&height=152&instid=SSE3966&dtype=history&fromdate=2011-01-01&todate=2011-10-15&showdeftitle=true&historyadjusted=true
	 * 
	 */
	
	public static final String buildListURL(String stockListOMXid) {
		return proxyURL + listURL + stockListOMXid;
	}
	
	public static final String buildListURL(int market, int cap) {
		return buildListURL(matrixLists[market][cap]);
	}
	
	public static final String buildHistoryURL(String omxID) {
		return proxyURL + historyURLstart + omxID + historyAdjusted + fromDate + date(735) + toDate + date(0);
	}
	
	//---------- privates

	
	private static String date(int daysAgo) {
		long epoch = System.currentTimeMillis() - (daysAgo * 86400000L);
		return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(epoch));
	}
}
