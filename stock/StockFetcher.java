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

import java.util.*;
import java.io.IOException;
import java.net.*;

import util.InvestDate;

import net.htmlparser.jericho.*;

public class StockFetcher {

	public static Map<String, StockData> buildStockMap(String market, String cap)
			throws MalformedURLException, IOException {

		Map<String, StockData> returnMap = new HashMap<String, StockData>(620);
		int mIndex = -1;
		int cIndex = -1;
		
		if (market.isEmpty() || cap.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int i=0; i<MarketData.arrayMarkets.length; i++) {
			if (MarketData.arrayMarkets[i].startsWith(market)) {
				mIndex = i;
				break;
			}
		}
		for (int i=0; i<MarketData.arrayCapital.length; i++) {
			if (MarketData.arrayCapital[i].startsWith(market)) {
				cIndex = i;
				break;
			}
		}
		if (mIndex == -1 || cIndex == -1) {
			throw new NoSuchElementException();
		}
		
		Source omxStockSource = new Source(new URL(MarketData.buildListURL(mIndex, cIndex)));
		String listName = MarketData.arrayMarkets[mIndex] + " " + MarketData.arrayCapital[cIndex];
		
		for (Element e : omxStockSource.getAllElements("inst")) {
			returnMap.put(e.getAttributeValue("id"), buildStockData(e, listName));
		}
		return returnMap;
	}
	
	private static StockData buildStockData(Element e, String listName)
			throws MalformedURLException, IOException {
		return new StockData(
				e.getAttributeValue("id"),
				e.getAttributeValue("nm"),
				e.getAttributeValue("fnm"),
				e.getAttributeValue("isin"),
				listName,
				e.getAttributeValue("cr"));
	}
	
	protected static double[][] updateHistory(double[][] histValue, String omxId) throws MalformedURLException, IOException {
		double[][] newHistValue = new double[8][500];
		int lastDate = new Double(histValue[0][499]).intValue();
		int today = new Integer(InvestDate.dateNoDash(0));
		if (lastDate != today) {
			Source histSource = new Source(new URL(
					MarketData.buildHistoryURL(omxId, InvestDate.makeDateString(lastDate))));

			List<Element> elist = histSource.getAllElements("hi");
			int esize = elist.size() - 1;

			for (int i=0; i<8; i++) {
				newHistValue[i] = Arrays.copyOf(Arrays.copyOfRange(histValue[i], esize, 500), 500);
			}
			Iterator<Element> itr = histSource.getAllElements("hi").iterator();
			for (int i = 499-esize; i<500 && itr.hasNext();i++) {
				makeHistoryRow(newHistValue, itr.next(), i);
			}
		}
		return newHistValue;
	}

	protected static double[][] rebuildHistory(String omxId) throws MalformedURLException, IOException {
		Source histSource = new Source(new URL(MarketData.buildHistoryURL(omxId, 735)));
		Iterator<Element> itr = histSource.getAllElements("hi").iterator();
		double[][] histValue = new double[8][500];
		for (int j=0; j<500 && itr.hasNext(); j++) {
			makeHistoryRow(histValue, itr.next(), j);
		}
		return histValue;
	}

	private static void makeHistoryRow(double[][] histValue, Element e, int i) {
		histValue[0][i] = Double.valueOf(new StringBuilder(
				e.getAttributeValue("dt")).deleteCharAt(4).deleteCharAt(6).toString());

		String ip = e.getAttributeValue("ip"); //factor
		String lp = e.getAttributeValue("lp"); //low price
		String hp = e.getAttributeValue("hp"); //high price
		String cp = e.getAttributeValue("cp"); //closing price
		String avp = e.getAttributeValue("avp"); //average price
		String tv = e.getAttributeValue("tv"); //volume
		String nt = e.getAttributeValue("nt"); //trades
		String to = e.getAttributeValue("to"); //turnover

		double factor = (ip.isEmpty()) ? Double.NaN : Double.parseDouble(ip);

		histValue[1][i] = (lp.isEmpty()) ? Double.NaN : Double.parseDouble(lp)*factor; 
		histValue[2][i] = (hp.isEmpty()) ? Double.NaN : Double.parseDouble(hp)*factor;
		histValue[3][i] = (cp.isEmpty()) ? Double.NaN : Double.parseDouble(cp)*factor;
		histValue[4][i] = (avp.isEmpty()) ? Double.NaN : Double.parseDouble(avp)*factor;
		histValue[5][i] = (tv.isEmpty()) ? Double.NaN : Double.parseDouble(tv);
		histValue[6][i] = (nt.isEmpty()) ? Double.NaN : Double.parseDouble(nt);
		histValue[7][i] = (to.isEmpty()) ? Double.NaN : Double.parseDouble(to);
	}
}