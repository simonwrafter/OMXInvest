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

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.xml.parsers.ParserConfigurationException;

import stock.Investments;

public class MainPanel extends JTabbedPane {
	private static final long serialVersionUID = 9193463064365388089L;
	private Investments invest;
	private HomePanel home;
	private EventPanel events;
	private HistoryPanel history;
	private OptimizationPanel optimal;
	private MarketPanel market;
	
	public MainPanel(Investments invest, PortfolioView view)
			throws ParserConfigurationException {
		super(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		this.invest = invest;
		
		home = new HomePanel(view, invest);
		events = new EventPanel(invest);
		history = new HistoryPanel(invest);
		optimal = new OptimizationPanel(this, invest);
		market = new MarketPanel(invest);
		
		addTab("Home", home);
		addTab("Events", events);
		addTab("History", history);
		addTab("Optimal", optimal);
		addTab("Market", market);
	}
	
	public void updatePersonalOptimization() throws ParserConfigurationException {
		Double[] personal = optimal.getPersonal();
		Double value = invest.getValueSum() + invest.getLiquid();
		JTable table = (JTable) ((JViewport) ((JScrollPane) optimal.getComponent(0)).getComponent(0)).getComponent(0);
		Double[][] histories = invest.getHistory(4, 1);
		for (int i=0; i<personal.length; i++) {
			table.setValueAt(new Integer((int) Math.round(personal[i] * value / histories[i+1][0])), i, 2);
		}
		((JTable) ((JViewport) ((JScrollPane) home.getComponent(0)).getComponent(0)).getComponent(0)).setValueAt(invest.getLambda(), 3, 6);
		table.setValueAt(invest.getLambda(), 1, 5);
	}
	
	public void updateHomePanel()
			throws ParserConfigurationException {
		home.updatePanel();
	}
	
	public void updateEventsPanel() {
		events.updatePanel();
	}
	
	public void updateHistoryPanel() {
		history.updatePanel();
	}
	
	public void updateOptimizationPanel()
			throws ParserConfigurationException {
		optimal.updatePanel();
	}
	
	public void updateMarketPanel() {
		market.updatePanel();
	}
}
