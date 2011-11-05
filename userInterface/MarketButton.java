package userInterface;

import java.awt.event.*;

import javax.swing.JButton;

public class MarketButton extends JButton implements ActionListener {
	private PortfolioView view;
	
	public MarketButton(PortfolioView view) {
		super("Markets and Stocks");
		this.view = view;
		addActionListener(this);
		this.setToolTipText("Markets and Stocks.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		view.showMarkets();
	}

}
