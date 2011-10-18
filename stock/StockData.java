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
	
	/*
	 * Ex:
	 * 
	 * ip="0.198566"		11
	 * 
	 * dt="2001-05-02"		0
	 * lp="736.00"			2
	 * hp="755.00"			1
	 * cp="744.00"			4
	 * avp=""				21
	 * tv="175321"			8
	 * nt="305"				9
	 * to="131232018"		10
	 * 
	 */
	
	public StockData(String omxId, String shortName, String fullName, String ISIN, String market, String currency) {
		this.omxId = omxId;
		this.shortName = shortName;
		this.fullName = fullName;
		this.ISIN = ISIN;
		this.market = market;
		this.currency = currency;
		histValue = new double[8][500];
		rebuildHistory();
	}
	
	public void updateHistory() throws MalformedURLException, IOException {
		int lastDate = new Double(histValue[0][499]).intValue();
		int today = new Integer(InvestDate.dateNoDash(0));
		if (lastDate != today) {
			Source histSource = null;
//			try {
				histSource = new Source(new URL(MarketData.buildHistoryURL(omxId, InvestDate.makeDateString(lastDate))));
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//				System.exit(1);
//			} catch (IOException e) {
//				try {
//					wait(0, 1000000000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				updateHistory();
//				return;
//			}
			
			List<Element> elist = histSource.getAllElements("hi");
			int esize = elist.size() - 1;
			
			int pos;
			for (pos=0; pos < 501-esize; pos++) {
				histValue[pos] = histValue[pos+esize-1];
			}
			
			Iterator<Element> itr = elist.iterator();
			while (pos<500 && itr.hasNext()) {
				makeHistoryRow(itr.next(), pos++);
			}
		}
	}
	
	public void rebuildHistory() {
		Source histSource = null;
		
		System.out.print(fullName);

		try {
			histSource = new Source(new URL(MarketData.buildHistoryURL(omxId, 735)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			try {
				wait(0, 1000000000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			rebuildHistory();
			return;
		}
		System.out.print(" down");
		Iterator<Element> itr = histSource.getAllElements("hi").iterator();
		int j=0;
		while (j<500 && itr.hasNext()) {
			makeHistoryRow(itr.next(), j++);
		}
		System.out.println(" done");
	}
	
	private void makeHistoryRow(Element e, int i) {
		histValue[0][i] = Double.valueOf(new StringBuilder(
				e.getAttributeValue("dt")).deleteCharAt(4).deleteCharAt(6).toString());
		
		String ip = e.getAttributeValue("ip");
		String lp = e.getAttributeValue("lp");
		String hp = e.getAttributeValue("hp");
		String cp = e.getAttributeValue("cp");
		String avp = e.getAttributeValue("avp");
		String tv = e.getAttributeValue("tv");
		String nt = e.getAttributeValue("nt");
		String to = e.getAttributeValue("to");

		double factor = (ip.isEmpty()) ? Double.NaN : Double.parseDouble(ip);

		histValue[1][i] = (lp.isEmpty()) ? Double.NaN : Double.parseDouble(lp)*factor; 
		histValue[2][i] = (hp.isEmpty()) ? Double.NaN : Double.parseDouble(hp)*factor;
		histValue[3][i] = (cp.isEmpty()) ? Double.NaN : Double.parseDouble(cp)*factor;
		histValue[4][i] = (avp.isEmpty()) ? Double.NaN : Double.parseDouble(avp)*factor;
		histValue[5][i] = (tv.isEmpty()) ? Double.NaN : Double.parseDouble(tv);
		histValue[6][i] = (nt.isEmpty()) ? Double.NaN : Double.parseDouble(nt);
		histValue[7][i] = (to.isEmpty()) ? Double.NaN : Double.parseDouble(to);
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
