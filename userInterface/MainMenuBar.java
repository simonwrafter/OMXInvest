package userInterface;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = -7643064728379040865L;
	private PortfolioView view;
	private JMenu portfolioMenu;
	
	public MainMenuBar(PortfolioView view) {
		super();
		this.view = view;
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new JMenuItem(view.getCurrentPortfolio().getName()));
		this.add(portfolioMenu);
	}
}
