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

public class PopUpQuestion extends JOptionPane {
	private static final long serialVersionUID = 4211870191745791511L;
	
	public static String getString(String question, String text) {
		String s = (String) showInputDialog(null, question, "OMXInvest", JOptionPane.QUESTION_MESSAGE, null, null, text);
		return (s==null || s.isEmpty()) ? null : s;
	}
	
	public static String getString(String question) {
		return getString(question, null);
	}
	
	public static Integer getInteger(String question) {
		boolean doAgain = true;
		Integer result = null;
		while (doAgain) {
			try {
				String i = getString(question, "0");
				result = i == null ? null : new Integer(i);
				doAgain = false;
			} catch (NumberFormatException nfe) {
				showMessageDialog(null, "Please try again with a valid number", "OMXInvest", JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}
	
	public static Double getDouble(String question) {
		return getDouble(question, 0.);
	}
	
	public static Double getDouble(String question, Double amount) {
		boolean doAgain = true;
		Double result = null;
		while (doAgain) {
			try {
				String d = getString(question, amount.toString());
				result = d == null ? null : new Double(d);
				doAgain = false;
			} catch (NumberFormatException nfe) {
				showMessageDialog(null, "Please try again with a valid number", "OMXInvest", JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}
	
	public static Object dropDownOptions(String question, Object[] options, int initial) {
		return showInputDialog(null, question, "OMXInvest", QUESTION_MESSAGE, null, options, options[initial]);
	}
	
	public static <T> Object dropDownOptions(String question, SortedSet<T> set) {
		Object[] array = new Object[set.size()];
		int i=0;
		for(T t : set) { array[i++] = t.toString(); }
		return dropDownOptions(question, array, 0);
	}
	
	public static void infoPopUp(String s) {
		showMessageDialog(null, s, "OMXInvest", INFORMATION_MESSAGE);
	}
	
	public static void errorPopUp(String s) {
		showMessageDialog(null, s, "OMXInvest", ERROR_MESSAGE);
	}
}
