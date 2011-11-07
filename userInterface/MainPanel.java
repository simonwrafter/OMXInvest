package userInterface;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 9193463064365388089L;
	private HistoryTable historyTable;
	private OptimizedTable optimizedTable;
	private MarketTable marketTable;
	private HomeTable homeTable;
	private JScrollPane scrollPane;
	
	public MainPanel(PortfolioView view) {
		super(new GridLayout(1,0));
		historyTable = new HistoryTable(view);
		optimizedTable = new OptimizedTable(view);
		marketTable = new MarketTable(view);
		homeTable = new HomeTable(view);
		scrollPane = new JScrollPane();
		showHistory();
		add(scrollPane);
	}
	
	public void showHistory() {
		scrollPane.setViewportView(historyTable.getTable());
	}
	
	public void showOptimization() {
		scrollPane.setViewportView(optimizedTable.getTable());
	}
	
	public void showMarkets() {
		scrollPane.setViewportView(marketTable.getTable());
	}
	
	public void showHome() {
		scrollPane.setViewportView(homeTable.getTable());
	}
}
