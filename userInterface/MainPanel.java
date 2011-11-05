package userInterface;

import java.awt.*;
import javax.swing.*;
import stock.*;

public class MainPanel extends JPanel {
	private JTable dataTable;
	private PortfolioView view;
	
	public MainPanel(PortfolioView view) {
		this.view = view;
		dataTable = new JTable(20,15);
		JScrollPane jsp = new JScrollPane();
		add(jsp.add(dataTable));
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
