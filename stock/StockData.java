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

public class StockData {

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
	
	public StockData(String omxId, String shortName, String fullName, String ISIN, String market, String currency)
			throws MalformedURLException, IOException {
		this.omxId = omxId;
		this.shortName = shortName;
		this.fullName = fullName;
		this.ISIN = ISIN;
		this.market = market;
		this.currency = currency;
		histValue = new double[8][500];
	}
	
	public void rebuildHistory() throws MalformedURLException, IOException {
		histValue = StockFetcher.rebuildHistory(omxId);
	}
	
	public void updateHistory() throws MalformedURLException, IOException {
		histValue = StockFetcher.updateHistory(histValue, omxId);
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
