package userInterface;

import javax.swing.*;

import util.*;

public class HistoryTable {
	private PortfolioView view;
	
	public HistoryTable(PortfolioView view) { 
		this.view = view;
	}
	
	public JTable getTable() {
		return new JTable(Matrix.transpose(view.getPortfolioHistory()),
				view.getCurrentPortfolio().getStocksInPortfolio());
	}
}
