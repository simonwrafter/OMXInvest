package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import stock.Portfolio;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = -7643064728379040865L;
	private PortfolioView view;
	private JMenu portfolioMenu;
	
	public MainMenuBar(PortfolioView view) {
		super();
		this.view = view;
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new MainMenuItem(Actions.NEW, "New Portfolio", "Add new portfolio"));
		portfolioMenu.add(new MainMenuItem(Actions.NEW, "Remove Portfolio", "Remove portfolio"));
		portfolioMenu.add(new MainMenuItem(Actions.NEW, "Edit Portfolio", "Edit existing portfolio"));
		portfolioMenu.addSeparator();
		for(Portfolio p : view.getPortfolios()) {
			addPortfolio(p);
		}
		this.add(portfolioMenu);
	}
	
	private class MainMenuItem extends JMenuItem implements ActionListener {
		private static final long serialVersionUID = 7086897211570015228L;
		private Actions action;
		private String text;
		
		private MainMenuItem(Actions action, String text, String toolTip) {
			super(text);
			this.text = text;
			this.action = action;
			this.setToolTipText(toolTip);
			this.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			view.actionHandler(action, text);
		}
	}

	public void addPortfolio(Portfolio portfolio) {
		portfolioMenu.add(new MainMenuItem(Actions.SWITCH, portfolio.getName(), "change portfolio"));
	}
}
