package userInterface;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableColumn;

import util.*;

public class HistoryTable {
	private PortfolioView view;
	
	public HistoryTable(PortfolioView view) { 
		this.view = view;
	}
	
	public JTable getTable() {
		Object[] header = InvestDate.addDateHeader(view.getCurrentPortfolio().getStocksInPortfolio());
		Object[][] data = Matrix.transpose(view.getPortfolioHistory());
		
		JTable table = new JTable(data, header);
		
		for (int i = 0; i < header.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(80);
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		return table;
	}
}
