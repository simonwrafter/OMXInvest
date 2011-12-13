package userInterface;

import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import stock.Investments;

public class EventPanel extends AbstractDataPanel {
	private static final long serialVersionUID = -2631309722541810275L;
	
	public EventPanel(Investments invest) throws ParserConfigurationException {
		super(invest);
	}
	
	@Override
	public void updatePanel() {
		Object[] header = new Object[] {"Date", "Time", "Mesage", "Change", "Value of Change", "Liquid", "Invested Value"};
		Object[][] data = Arrays.copyOf(invest.getPortfolioEvents(), invest.nbrOfEvents());
		
		model.setDataVector(data, header);
		updateUI();
	}
}
