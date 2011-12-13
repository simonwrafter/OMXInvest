package userInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import stock.Investments;

public class HomePanel extends AbstractDataPanel {
	private static final long serialVersionUID = -2461367753369663611L;
	private PortfolioView view;
	
	public HomePanel(PortfolioView view, Investments invest)
			throws ParserConfigurationException {
		super(invest);
		this.view = view;
		this.add(new HomeCommandPanel(), BorderLayout.SOUTH);
	}
	
	@Override
	public void updatePanel()
			throws ParserConfigurationException {
		Object[] header = {"omxId", "name", "short name", "nbrOf", "last buy value", "last sell value", "total", ""};
		String[] omxIds = invest.getStockIds();
		String[] stocks = invest.getStockNames();
		String[] shortName = invest.getShortNames();
		Integer[] nbrOf = invest.getShareDistribution();
		Double[] buy = invest.getLatestBuy();
		Double[] sell = invest.getLatestSell();
		
		int size = Math.max(invest.size(), 6);
		Object[][] data = new Object[size][8];
		
		for (Object[] o : data)
			Arrays.fill(o, "");
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = omxIds[i];
			data[i][1] = stocks[i];
			data[i][2] = shortName[i];
			data[i][3] = nbrOf[i];
			data[i][4] = String.format("%.02f", buy[i]);
			data[i][5] = String.format("%.02f", sell[i]);
			data[i][6] = String.format("%.02f", nbrOf[i]*sell[i]);
			data[i][7] = "";
		}
		
		data[0][7] = "currency";
		data[1][7] = invest.getCurrency();
		data[2][7] = "lambda";
		data[3][7] = String.format("%.02f", invest.getLambda());
		data[4][7] = "liquid";
		data[5][7] = String.format("%.02f", invest.getLiquid());
		
		model.setDataVector(data, header);
		updateUI();
	}
	
	public class HomeCommandPanel extends JPanel {
		private static final long serialVersionUID = 7558744166697327417L;

		public HomeCommandPanel() {
			setLayout(new FlowLayout());
			add(new Button(Actions.ADD, "Add Stock", "Add company to portfolio", view));
			add(new Button(Actions.REMOVE, "Remove Stock", "Remove company from portfolio", view));
			add(new Button(Actions.BUY, "Buy Shares", "Buy Shares", view));
			add(new Button(Actions.SELL, "Sell Shares", "Sell Shares", view));
			add(new Button(Actions.LIQUID, "Liquid Assets", "Edit Liquid Assets", view));
		}
	}
}
