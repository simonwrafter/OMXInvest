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

import net.htmlparser.jericho.*;

public class StockData {

	private String omxId;
	private String shortName;
	private String fullName;
	private String ISIN;
	private String market;
	private String currency;
	private double[][] histValue; // {[0][]=date, [1][]=low, [2][]=high, [3][]=close, [4][]=volume, [5][]=trades}

	public StockData(String omxId, String shortName, String fullName, String ISIN, String market, String currency) {
		this.omxId = omxId;
		this.shortName = shortName;
		this.fullName = fullName;
		this.ISIN = ISIN;
		this.market = market;
		this.currency = currency;
		buildHistory();
	}

	private void buildHistory() {
		histValue = new double[6][500];
		Source histSource = null;

		try {
			histSource = new Source(new URL(MarketData.buildHistoryURL(omxId)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Iterator<Element> itr = histSource.getAllElements("hi").iterator();
		int j=0;
		while(j<500 && itr.hasNext()) {
			makeHistoryRow(itr.next(), j++);
		}
	}
	
	private void makeHistoryRow(Element e, int i) {
		histValue[0][i] = Double.valueOf(
				new StringBuilder(e.getAttributeValue("dt"))
				.deleteCharAt(4).deleteCharAt(6).toString());
		
		try {
			String lp = (e.getAttributeValue("lp").equalsIgnoreCase("")) ? "0" : e.getAttributeValue("lp").toString();
			String hp = (e.getAttributeValue("hp").equalsIgnoreCase("")) ? "0" : e.getAttributeValue("hp").toString();
			String cp = (e.getAttributeValue("cp").equalsIgnoreCase("")) ? "0" : e.getAttributeValue("cp").toString();
			String tv = (e.getAttributeValue("tv").equalsIgnoreCase("")) ? "0" : e.getAttributeValue("tv").toString();
			String nt = (e.getAttributeValue("nt").equalsIgnoreCase("")) ? "0" : e.getAttributeValue("nt").toString();
			
			histValue[1][i] = Double.valueOf(lp);
			histValue[2][i] = Double.valueOf(hp);
			histValue[3][i] = Double.valueOf(cp);
			histValue[4][i] = Double.valueOf(tv);
			histValue[5][i] = Double.valueOf(nt);
		} catch (Exception e2) {
			System.out.println(e);
		}
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
		StockData other = (StockData) obj;
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
}
