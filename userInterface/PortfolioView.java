package userInterface;

import java.awt.*;
import javax.swing.*;

import stock.*;

public class PortfolioView {
	private Portfolio portfolio;
	private CommandPanel commandPanel;
	private JMenu fractalMenu;
	
	PortfolioView() {
		portfolio = new Portfolio("default");
		
		JFrame frame = new JFrame("Invest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandPanel = new CommandPanel(this);
		
		frame.add(commandPanel, BorderLayout.SOUTH);
		
		fractalMenu = new JMenu("Portfolios");
		fractalMenu.add(new JMenuItem(portfolio.getName()));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fractalMenu);
		
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
	}

	public void showHistory() {
		// TODO Auto-generated method stub
	}

	public void showOptimization() {
		// TODO Auto-generated method stub
	}
}
