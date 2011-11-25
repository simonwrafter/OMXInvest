package userInterface;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import stock.Investments;
import stock.Market;
import stock.Stock;

public class MarketPanel extends JPanel {
	private static final long serialVersionUID = 871497467388558453L;
	private Investments invest;
	private DefaultTableModel model;

	public MarketPanel(Investments invest) {
		super(new BorderLayout());
		this.invest = invest;
		
		model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		updatePanel();
		
		for (int i = 0; i < invest.nbrOfMarkets()*3; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i%3 == 0) {
				column.setPreferredWidth(175);
			} else {
				column.setPreferredWidth(75);
			}
		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		this.add(new JScrollPane(table));
	}
	
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