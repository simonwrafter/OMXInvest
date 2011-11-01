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

import java.util.NoSuchElementException;

import util.InvestDate;

public class MarketData {
	protected static final String[][] matrixLists = {
		//	 LC			MC		   SC		FN
		{ "L:10220", "L:10222", "L:10224", "127" },	//CSE
		{ "L:10208", "L:10210", "L:10212",  "27" },	//STO
		{ "L:10196", "L:10198", "L:10200", "146" },	//HEL
		{ "L:10238", "L:10240", "L:10242", "136" },	//ICE
	};
	protected static final String  NordicLCExtra = "L:10150";
	protected static final String  NordicLCExtraName = "Nordic Lage Cap Extras";
	
	protected static final String[] arrayMarkets = { "Copenhagen", "Stockholm", "Helsinki", "Iceland" };
	protected static final String[] arrayCapital = { "Large Cap", "Medium Cap", "Small Cap" , "First North" };
	
	//These could be real fun!!
	protected static final String[] nordicIndexes = { "SE0001809476", "DX0000001376", "SE0000337842", "FI0008900212", "IS0000018885" };
	protected static final String[] firstNorthIndexes = { "SE0002229377", "SE0002229385", "SE0002229393", "SE0002229401", "SE0002229419", "SE0002229427", "SE0002229435", "SE0002229443", "SE0002229450", "SE0002229468", "SE0001718719", "SE0001718727", "SE0001910944", "SE0001718701"  };
	protected static final String[] sectorIndexes = { "SE0001775834", "SE0001775859", "SE0001775867", "SE0001775883", "SE0001776196", "SE0001776212", "SE0001776238", "SE0001776253", "SE0001776279", "SE0001776295" };
	protected static final String[] morgageRates = { "SE0003077585", "SE0003077593", "SE0003077577","SE0003330687","SE0003332931" };
		
	private static final String proxyURL = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?";
	
	private static final String listQuestion = "SubSystem=Prices&Action=Search&inst.an=nm,fnm,isin,tp,cr&InstrumentType=S&List=";
	private static final String historyQuestion = "SubSystem=History&Action=GetDataSeries&Instrument=";
	private static final String derivQuestion = "SubSystem=Prices&Action=GetDerivatives&Instrument=";
	private static final String fromDate = "&fromDate=";
	private static final String toDate = "&toDate=";
	private static final String historyValues = "&hi.a=0,2,1,4,21,8,9,10,11"; //"0,1,2,4,5,6,7,8,9,10,11,21,32,33,34,35,36,37,38,39,41,42,43,44,45,55,56"; // not sure what they all mean :/
	private static final String derivValues = "&inst.an=id,nm,isin,bp,ap,hp,lp,lsp,tv,ed";
	
	private static final String newsURL = "https://newsclient.omxgroup.com/cdsPublic/viewDisclosure.action?";
	private static final String newsId = "disclosureId=";
	private static final String newslang = "&lang=";
	
	private static final String realtime = "&realtime=true"; // might come in handy
	
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
	
	public static final String buildDerivativeURL(String omxId) {
		return proxyURL + derivQuestion + omxId + derivValues;
	}
	
	public static final int[] marketIndex(String market, String cap) {
		int[] index = new int[2];
		index[0] = -1;
		index[1] = -1;
		
		if (market.isEmpty() || cap.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int i=0; i<arrayMarkets.length; i++) {
			if (arrayMarkets[i].startsWith(market)) {
				index[0] = i;
				break;
			}
		}
		for (int i=0; i<arrayCapital.length; i++) {
			if (arrayCapital[i].startsWith(market)) {
				index[1] = i;
				break;
			}
		}
		if (index[0] == -1 || index[1] == -1) {
			throw new NoSuchElementException();
		}
		return index;
	}
}
