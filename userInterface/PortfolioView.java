package userInterface;

import javax.swing.JFrame;

import fractal.CommandPanel;

public class PortfolioView {
	private Portfolio[] portfolios;
	private CommandPanel commandPanel; 
	
	PortfolioView() {
		portfolios = new Portfolio[10];
		
		JFrame frame = new JFrame("Invest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandPanel = new CommandPanel(this);
	}
}
