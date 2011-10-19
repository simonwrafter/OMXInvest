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
	public static final String[][] matrixLists = {
		//	 LC			MC		   SC		FN
		{ "L:10220", "L:10222", "L:10224", "127" },	//CSE
		{ "L:10208", "L:10210", "L:10212",  "27" },	//STO
		{ "L:10196", "L:10198", "L:10200", "146" },	//HEL
		{ "L:10238", "L:10240", "L:10242", "136" },	//ICE
	};
	public static final String  NordicLCExtra = "L:10150";
	public static final String  NordicLCExtraName = "Nordic Lage Cap Extras";
	public static final String[] arrayMarkets = { "Copenhagen", "Stockholm", "Helsinki", "Iceland" };
	public static final String[] arrayCapital = { "Large Cap", "Medium Cap", "Small Cap" , "First North" };
	
	public static final String proxyURL = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?";
	
	public static final String listQuestion = "SubSystem=Prices&Action=Search&inst.an=nm,fnm,isin,tp,cr&InstrumentType=S&List=";
	public static final String historyQuestion = "SubSystem=History&Action=GetDataSeries&Instrument=";
	
	public static final String fromDate = "&fromDate=";
	public static final String toDate = "&toDate=";
	public static final String historyValues = "&hi.a=0,2,1,4,21,8,9,10,11"; //0,1,2,4,5,6,7,8,9,10,11,21,32,33,34,35,36,37,38,39,41,42,43,44,45,55,56"; // not sure what they all mean :/
	
	public static final String newsURL = "https://newsclient.omxgroup.com/cdsPublic/viewDisclosure.action?";
	public static final String newsId = "disclosureId=";
	public static final String newslang = "&lang=";
	
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
		return proxyURL + listQuestion + stockListOMXid;
	}
	
	public static final String buildListURL(int market, int cap) {
		return buildListURL(matrixLists[market][cap]);
	}
	
	public static final String buildHistoryURL(String omxID, int days) {
		return buildHistoryURL(omxID, InvestDate.date(days));
	}
	
	public static final String buildHistoryURL(String omxID, String date) {
		return proxyURL + historyQuestion + omxID + fromDate + date + historyValues;
	}
}
