package userInterface;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import stock.Investments;
import stock.Market;
import stock.Stock;

public class MarketPanel extends AbstractDataPanel {
	private static final long serialVersionUID = 871497467388558453L;

	public MarketPanel(Investments invest)
			throws ParserConfigurationException {
		super(invest);
	}
	
	@Override
	public void updatePanel() {
		Collection<Market> markets = invest.getMarketSet();
		int x = markets.size()*3;
		int y = 0;
		for(Market m : markets) {
			y = y < m.getMarketMap().size() ? m.getMarketMap().size() : y; 
		}
		Object[] header = new Object[x];
		Object[][] data = new Object[y][x];

		Arrays.fill(header, "");
		for (Object[] o : data) {
			Arrays.fill(o, "");
		}
		x=0;
		for (Market m : markets) {
			y=0;
			header[x] = m.getName();
			for(Map.Entry<String, Stock> e : m.getMarketMap().entrySet()) {
				Stock s = e.getValue();
				data[y][x] = s.getFullName();
				data[y][x+1] = s.getShortName();
				data[y][x+2] = s.getOmxId();
				y++;
			}
			x += 3;
		}
		model.setDataVector(data, header);
	}
}