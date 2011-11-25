package userInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import stock.Investments;

public class HomePanel extends JPanel {
	private static final long serialVersionUID = -2461367753369663611L;
	private Investments invest;
	private PortfolioView view;
	private DefaultTableModel model;
	
	public HomePanel(PortfolioView view, Investments invest) {
		super(new BorderLayout());
		this.invest = invest;
		this.view = view;

		model = new DefaultTableModel();
		JTable table = new JTable(model);

		updatePanel();
		
		for (int i = 0; i < 7; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 1) {
				column.setPreferredWidth(90);
			} else {
				column.setPreferredWidth(75);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(new HomeCommandPanel(), BorderLayout.SOUTH);
	}
	
	public void updatePanel() {
		Object[] header = {"omxId", "name", "short name", "nbrOf", "last value", "total", ""};
		String[] omxIds = invest.getStockIds();
		String[] stocks = invest.getStockNames();
		String[] shortName = invest.getShortNames();
		Integer[] nbrOf = invest.getPortfolioNbrOfShares();
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
			data[i][4] = history[i+1][0];
			data[i][5] = nbrOf[i]*history[i+1][0];
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
