package userInterface;

import javax.xml.parsers.ParserConfigurationException;

import stock.Investments;
import util.InvestDate;
import util.InvestMatrix;

public class HistoryPanel extends AbstractDataPanel {
	private static final long serialVersionUID = -8300239271253271499L;
	
	public HistoryPanel(Investments invest) throws ParserConfigurationException {
		super(invest);
	}
	
	@Override
	public void updatePanel() {
		Object[] header = InvestDate.addDateHeader(invest.getShortNames());
		Object[][] data = InvestMatrix.transpose(invest.getHistory(4));

		for(Object[] o : data) {
			o[0] = InvestDate.dateWithDash(((Double) o[0]).intValue());
			for(int i=1;i<o.length;i++) {
				o[i] = String.format("%.02f", o[i]);
			}
		}
		
		model.setDataVector(data, header);
		updateUI();
	}
}
