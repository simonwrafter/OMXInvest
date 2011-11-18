package userInterface;

import javax.swing.JOptionPane;

import stock.Currency;
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
			showMessageDialog(null, "Please try again with a valid number",
					"OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Double getDouble(String question) {
		try {
			String d = getString(question, "0");
			return d == null ? null : new Double(d);
		} catch (NumberFormatException nfe) {
			showMessageDialog(null, "Please try again with a valid number",
					"OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Portfolio makeNewPortfolio() {
		String name = getString("New portfolio name.");
		if (name==null)
			return null;
		Currency currency = (Currency) showInputDialog(null,
				"Currency for new portfolio", "OMXInvest", QUESTION_MESSAGE,
				null, Currency.currencies, Currency.currencies[0]);
		if (currency==null)
			return null;
		Integer liquid = getInteger("Initial liquid assets");
		if (liquid==null)
			return null;
		return new Portfolio(name, currency, liquid);
	}
	
	public static void editPortfolioName(Portfolio portfolio) {
		String name = getString("New portfolio name.", portfolio.getName());
		if (name==null)
			return;
		portfolio.setName(name);
	}
}
