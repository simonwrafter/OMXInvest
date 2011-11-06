package userInterface;

import java.awt.*;
import javax.swing.*;
import util.*;

public class MainPanel extends JPanel {
	private JTable dataTable;
	private PortfolioView view;
	
	public MainPanel(PortfolioView view) {
		this.view = view;
		dataTable = new JTable(Matrix.transpose(view.getPortfolioHistory()), 
				InvestDate.addDateHeader(view.getCurrentPortfolio().getStocksInPortfolio()));
        dataTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        dataTable.setFillsViewportHeight(true);
		JScrollPane jsp = new JScrollPane(dataTable);
		add(jsp);
	}
	
	public void showHistory() {
		
	}
	
	public void showOptimization() {
		
	}
	
	public void showMarkets() {
		
	}
	
	public void showHome() {
		
	}
}
