package userInterface;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.*;

import stock.*;

public class PortfolioView {
	private Portfolio currentPortfolio;
	private CommandPanel commandPanel;
	private JMenu portfolioMenu;
	private MainPanel mainPanel;
	private Investments investments;
	
	PortfolioView() {
		try {
			investments = new Investments();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		currentPortfolio = investments.getDefaultPortfolio();
		
		JFrame frame = new JFrame("Invest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandPanel = new CommandPanel(this);
		
		frame.add(commandPanel, BorderLayout.SOUTH);
		
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new JMenuItem(currentPortfolio.getName()));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(portfolioMenu);
		
		mainPanel = new MainPanel(this);
		
		frame.add(mainPanel);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
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
