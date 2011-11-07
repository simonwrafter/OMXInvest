package userInterface;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 9193463064365388089L;
	private Tables tables;
	private JScrollPane scrollPane;
	
	public MainPanel(PortfolioView view) {
		super(new GridLayout(1,0));
		tables = new Tables(view);
		
		scrollPane = new JScrollPane();
		showHistory();
		add(scrollPane);
	}
	
	public void showHistory() {
		scrollPane.setViewportView(tables.getHistoryTable());
	}
	
	public void showOptimization() {
		scrollPane.setViewportView(tables.getOptimizationTable());
	}
	
	public void showMarkets() {
		scrollPane.setViewportView(tables.getMarketTable());
	}
	
	public void showHome() {
		scrollPane.setViewportView(tables.getHomeTable());
	}
}
