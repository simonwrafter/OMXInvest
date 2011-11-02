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

import java.io.*;
import java.net.*;
import java.util.*;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import util.InvestDate;

public class Stock {

	private String omxId;
	private String shortName;
	private String fullName;
	private String ISIN;
	private String market;
	private String currency;
	private double[][] histValue; 
	/* 
	 * vector	meaning		key	  hi.a
	 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	 * {
	 *  ----- = factor		ip  - 11
	 *  [0][] = date		dt  -  0
	 *  [1][] = low			lp  -  2
	 *  [2][] = high		hp  -  1
	 *  [3][] = close		cp  -  4
	 *  [4][] = average		avp - 21
	 *  [5][] = volume		tv  -  8
	 *  [6][] = trades		nt  -  9
	 *  [7][] = turnover	to  - 10 
	 * } 
	 *  
	 */
	
	public Stock(String omxId, String shortName, String fullName, String ISIN, String market, String currency)
			throws MalformedURLException, IOException {
		this.omxId = omxId;
		this.shortName = shortName;
		this.fullName = fullName;
		this.ISIN = ISIN;
		this.market = market;
		this.currency = currency;
		histValue = new double[8][500];
	}

	public String getOmxId() {
		return omxId;
	}

	public String getShortName() {
		return shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getISIN() {
		return ISIN;
	}

	public String getMarket() {
		return market;
	}

	public String getCurrency() {
		return currency;
	}
	
	public double[][] getHistory() {
		return getHistory(500);
	}
	
	public double[][] getHistory(int days) {
		return Arrays.copyOfRange(histValue, 500-days, 500);
	}
	
	public void rebuildHistory() throws MalformedURLException, IOException {
		Source histSource = new Source(new URL(MarketData.buildHistoryURL(omxId, 735)));
		Iterator<Element> itr = histSource.getAllElements("hi").iterator();
		histValue = new double[8][500];
		for (int j=0; j<500 && itr.hasNext(); j++) {
			makeHistoryRow(histValue, itr.next(), j);
		}
	}
	
	public void updateHistory() throws MalformedURLException, IOException {
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
			histValue = newHistValue;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s [omxId=%s, shortName=%s, ISIN=%s, market=%s, currency=%s]",
				fullName, omxId, shortName, ISIN, market, currency);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ISIN == null) ? 0 : ISIN.hashCode());
		result = prime * result + ((omxId == null) ? 0 : omxId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Stock other = (Stock) obj;
		if (ISIN == null) {
			if (other.ISIN != null) {
				return false;
			}
		} else if (!ISIN.equals(other.ISIN)) {
			return false;
		}
		if (omxId == null) {
			if (other.omxId != null) {
				return false;
			}
		} else if (!omxId.equals(other.omxId)) {
			return false;
		}
		return true;
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
