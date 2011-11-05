package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class HomeButton extends JButton implements ActionListener {
	private PortfolioView view;

	public HomeButton(PortfolioView view) {
		super("Home");
		this.view = view;
		addActionListener(this);
		this.setToolTipText("Show Portfolios");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		view.showHome();
	}
}
