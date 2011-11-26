package userInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.JPanel;

import stock.Investments;

public class HomePanel extends AbstractDataPanel {
	private static final long serialVersionUID = -2461367753369663611L;
	private PortfolioView view;
	
	public HomePanel(PortfolioView view, Investments invest) {
		super(invest);
		this.view = view;
		this.add(new HomeCommandPanel(), BorderLayout.SOUTH);
	}
	
	@Override
	public void updatePanel() {
		Object[] header = {"omxId", "name", "short name", "nbrOf", "last value", "total", ""};
		String[] omxIds = invest.getStockIds();
		String[] stocks = invest.getStockNames();
		String[] shortName = invest.getShortNames();
		Integer[] nbrOf = invest.getShareDistribution();
		Double[][] history = invest.getHistory(4, 1);
		
		int size = Math.max(invest.size(), 6);
		Object[][] data = new Object[size][7];
		
		for (Object[] o : data)
			Arrays.fill(o, "");
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = omxIds[i];
			data[i][1] = stocks[i];
			data[i][2] = shortName[i];
			data[i][3] = nbrOf[i];
			data[i][4] = String.format("%.02f", history[i+1][0]);
			data[i][5] = String.format("%.02f", nbrOf[i]*history[i+1][0]);
			data[i][6] = "";
		}
		
		data[0][6] = "currency";
		data[1][6] = invest.getCurrency();
		data[2][6] = "lambda";
		data[3][6] = invest.getLambda();
		data[4][6] = "liquid";
		data[5][6] = invest.getLiquid();
		
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
