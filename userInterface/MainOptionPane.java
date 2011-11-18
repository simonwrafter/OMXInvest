package userInterface;

import javax.swing.JOptionPane;

import stock.Currency;
import stock.Portfolio;

public class MainOptionPane extends JOptionPane {
	private static final long serialVersionUID = 4211870191745791511L;

	public static String getString(String question) {
		return showInputDialog(question).toUpperCase();
	}
	
	public static Integer getInteger(String question) {
		try {
			return new Integer(getString(question));
		} catch (NumberFormatException nfe) {
			showMessageDialog(null, "Please try again with a valid number",
					"OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Double getDouble(String question) {
		try {
			return new Double(getString(question));
		} catch (NumberFormatException nfe) {
			showMessageDialog(null, "Please try again with a valid number",
					"OMXInvest", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public static Portfolio makeNewPortfolio() {
		String name = getString("New portfolio name.");
		Currency currency = (Currency) showInputDialog(null,
				"Currency for new portfolio", "OMXInvest", QUESTION_MESSAGE,
				null, Currency.currencies, Currency.currencies[0]);
		Integer liquid = getInteger("Initial liquid assets");
		return new Portfolio(name, currency, liquid);
	}
}
