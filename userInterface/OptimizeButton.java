package userInterface;

import java.awt.event.*;
import javax.swing.*;

public class OptimizeButton extends JButton implements ActionListener {
	private PortfolioView view;
	
	public OptimizeButton(PortfolioView view) {
		super("Optimization");
		this.view = view;
		addActionListener(this);
		this.setToolTipText("Visar optimal portfölj.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		view.showOptimization();
	}
	
}
