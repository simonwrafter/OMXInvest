/*
 * Copyright © 2011, Simon Wrafter <simon.wrafter@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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
		
		JMenu admin = new JMenu("Admin");
		admin.add(new MainMenuItem(Actions.REBUILD_HISTORY, "Rebuild histories", "Refetch all histories of the internet"));
		admin.add(new MainMenuItem(Actions.UPDATE_MARKETS, "Rebuild markets", "Refetch all markets of the internet"));
		this.add(admin);
		
		JMenu about = new JMenu("More");
		about.add(new MainMenuItem(Actions.ABOUT, "About", "Who I am"));
		about.add(new MainMenuItem(Actions.ICS_LICENSE, "ICS license", "The ICS License text"));
		about.add(new MainMenuItem(Actions.JAMA_C, "JAMA © notice", "JAMA copyright notice"));
		this.add(about);
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
