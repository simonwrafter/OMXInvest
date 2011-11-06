package userInterface;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import stock.*;

public class PortfolioView {
	private CommandPanel commandPanel;
	private JMenu portfolioMenu;
	private MainPanel mainPanel;
	private Investments investments;
	
	PortfolioView() throws MalformedURLException, IOException {
		investments = new Investments();
		
		JFrame frame = new JFrame("Invest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandPanel = new CommandPanel(this);
		
		frame.add(commandPanel, BorderLayout.SOUTH);
		
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new JMenuItem(investments.getCurrentPortfolio().getName()));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(portfolioMenu);
		
		mainPanel = new MainPanel(this);
		
		frame.add(mainPanel);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
	}
	
	public Portfolio getCurrentPortfolio() {
		return investments.getCurrentPortfolio();
	}
	
	public Object[][] getPortfolioHistory() {
		return investments.getHistory(4);
	}
	
	public Object[][] getPortfolioHistory(int nbrOfDays) {
		return investments.getHistory(4, nbrOfDays);
	}
	
	public void showHistory() {
		mainPanel.showHistory();
	}

	public void showOptimization() {
		mainPanel.showOptimization();
	}

	public void showMarkets() {
		mainPanel.showMarkets();
	}

	public void showHome() {
		mainPanel.showHome();
	}
}
