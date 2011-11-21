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

package userInterface;

import java.util.SortedSet;

import javax.swing.JOptionPane;

import stock.Currency;
import stock.HasName;
import stock.Portfolio;

public class MainOptionPane extends JOptionPane {
	private static final long serialVersionUID = 4211870191745791511L;
	
	public static String getString(String question, String text) {
		String s = showInputDialog(question, text);
		return (s==null || s.isEmpty()) ? null : s;
	}
	
	public static String getString(String question) {
		return getString(question, null);
	}
	
	public static Integer getInteger(String question) {
		try {
			String i = getString(question, "0");
			return i == null ? null : new Integer(i);
		} catch (NumberFormatException nfe) {
			showMessageDialog(null, "Please try again with a valid number", "OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Double getDouble(String question) {
		try {
			String d = getString(question, "0");
			return d == null ? null : new Double(d);
		} catch (NumberFormatException nfe) {
			showMessageDialog(null, "Please try again with a valid number", "OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Object dropDownOptions(String question, Object[] options, int initial) {
		return showInputDialog(null, question, "OMXInvest", QUESTION_MESSAGE, null, options, options[initial]);
	}
	
	public static <T extends HasName> Object dropDownOptions(String question, SortedSet<T> set) {
		Object[] array = new Object[set.size()];
		int i=0;
		for(T t : set) { array[i++] = t.getName(); }
		return dropDownOptions(question, array, 0);
	}
	
	public static Portfolio makeNewPortfolio() {
		String name = getString("New portfolio name.");
		if (name==null) return null;
		Currency currency = (Currency) dropDownOptions("Currency for new portfolio", Currency.currencies, 0);
		if (currency==null) return null;
		Integer liquid = getInteger("Initial liquid assets");
		if (liquid==null) return null;
		return new Portfolio(name, currency, liquid);
	}
	
	public static void editPortfolioName(Portfolio portfolio) {
		String name = getString("New portfolio name.", portfolio.getName());
		if (name==null) return;
		portfolio.setName(name);
	}
	
	public static void infoPopUp(String s) {
		showMessageDialog(null, s, "OMXInvest", INFORMATION_MESSAGE);
	}
	
	public static void errorPopUp(String s) {
		showMessageDialog(null, s, "OMXInvest", ERROR_MESSAGE);
	}
}
