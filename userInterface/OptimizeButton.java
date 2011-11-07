package userInterface;

import java.awt.event.*;
import javax.swing.*;

public class OptimizeButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 8232892584777619000L;
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
