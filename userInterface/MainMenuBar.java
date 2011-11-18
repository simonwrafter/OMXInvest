package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;
import java.util.LinkedList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import stock.Portfolio;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = -7643064728379040865L;
	private PortfolioView view;
	private JMenu portfolioMenu;
	private LinkedList<MainMenuItem> items;
	
	public MainMenuBar(PortfolioView view) {
		super();
		this.view = view;
		this.items = new LinkedList<MainMenuItem>();
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new MainMenuItem(Actions.NEW, "New Portfolio", "Add new portfolio"));
		portfolioMenu.add(new MainMenuItem(Actions.DELETE, "Remove Portfolio", "Remove portfolio"));
		portfolioMenu.add(new MainMenuItem(Actions.EDIT, "Edit Portfolio", "Change name of current portfolio"));
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
		MainMenuItem mmi = new MainMenuItem(Actions.SWITCH, portfolio.getName(), "change portfolio");
		portfolioMenu.add(mmi);
		items.add(mmi);
	}

	public void removePortfolio(String name) {
		ListIterator<MainMenuItem> li = items.listIterator();
		while (li.hasNext()) {
			MainMenuItem mmi = li.next();
			if (mmi.getText().equals(name)) {
				portfolioMenu.remove(mmi);
				li.remove();
			}
		}
	}
}
