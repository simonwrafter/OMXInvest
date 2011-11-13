package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton implements ActionListener {
	private static final long serialVersionUID = 6296920004478842871L;
	private PortfolioView view;
	private Panel panel;
	
	public Button (Panel panel, String text, String toolTip, PortfolioView view) {
		super(text);
		this.panel = panel;
		setToolTipText(toolTip);
		this.view = view;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.showPanel(panel);
	}
}
