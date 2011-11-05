package userInterface;

import java.awt.event.*;
import javax.swing.*;

public class HistoryButton extends JButton implements ActionListener {
	private PortfolioView view;

	public HistoryButton(PortfolioView view) {
		super("History");
		this.view = view;
		addActionListener(this);
		this.setToolTipText("Show Portfolio History");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		view.showHistory();
	}
}