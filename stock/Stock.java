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

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.InvestDate;

public class Stock {

	private String omxId;
	private String shortName;
	private String fullName;
	private String ISIN;
	private String market;
	private Currency currency;
	private Double[][] histValue; 
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
	
	public Stock(String omxId, String shortName, String fullName, String ISIN, String market, Currency currency) {
		this.omxId = omxId;
		this.shortName = shortName;
		this.fullName = fullName;
		this.ISIN = ISIN;
		this.market = market;
		this.currency = currency;
		histValue = new Double[8][500];
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

	public Currency getCurrency() {
		return currency;
	}
	
	public Double[][] getHistory() {
		return getHistory(500);
	}
	
	public Double[][] getHistory(int days) {
		Double[][] result = new Double[8][];
		
		for (int i=0; i<8; i++) {
			result[i] = Arrays.copyOfRange(histValue[i], 0, days);
		}
		return result;
	}
	
	public void rebuildHistory() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse(MarketData.buildHistoryURL(omxId, 735));
		NodeList nl = doc.getElementsByTagName("hi");
		histValue = new Double[8][500];
		int len = nl.getLength()-1;
		for (int i=0; i<500; i++, len--) {
			if(!makeHistoryRow(histValue, nl.item(len), i))
				i--;
		}
	}
	
	public void updateHistory() throws IOException, ParserConfigurationException, SAXException {
		int lastDate = histValue[0][0].intValue();
		int today = new Integer(InvestDate.dateNoDash(0));
		if (lastDate != today) {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(MarketData.buildHistoryURL(omxId, InvestDate.makeDateString(lastDate)));
			NodeList nl = doc.getElementsByTagName("hi");
			Double[][] newHistValue = new Double[8][500];
			
			int i=nl.getLength()-1;
			for (int j = i; j>0; j--) {
				if(!makeHistoryRow(newHistValue, nl.item(j), i-j))
					i++;
			}
			for (int j=0; j+i<500; j++) {
				for(int k=0; k<8; k++) {
					newHistValue[k][j+i] = histValue[k][j];
				}
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
	
	private static boolean makeHistoryRow(Double[][] histValue, Node n, int i) {
		
		Element e = (Element) n;
		histValue[0][i] = (double) InvestDate.makeDateInt(e.getAttribute("dt"));
		
		String ip = e.getAttribute("ip"); //factor
		String lp = e.getAttribute("lp"); //low price
		String hp = e.getAttribute("hp"); //high price
		String cp = e.getAttribute("cp"); //closing price
		String avp = e.getAttribute("avp"); //average price
		String tv = e.getAttribute("tv"); //volume
		String nt = e.getAttribute("nt"); //trades
		String to = e.getAttribute("to"); //turnover
		
		if (ip.isEmpty() || lp.isEmpty() || cp.isEmpty() || avp.isEmpty() || tv.isEmpty() || nt.isEmpty() || to.isEmpty())
			return false;
		
		double factor = Double.parseDouble(ip);
		
		histValue[1][i] = Double.parseDouble(lp)*factor; 
		histValue[2][i] = Double.parseDouble(hp)*factor;
		histValue[3][i] = Double.parseDouble(cp)*factor;
		histValue[4][i] = Double.parseDouble(avp)*factor;
		histValue[5][i] = Double.parseDouble(tv);
		histValue[6][i] = Double.parseDouble(nt);
		histValue[7][i] = Double.parseDouble(to);
		
		return true;
	}
}
