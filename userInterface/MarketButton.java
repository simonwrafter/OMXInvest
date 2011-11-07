package userInterface;

import java.awt.event.*;
import javax.swing.*;

public class MarketButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 8890635804371213914L;
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
