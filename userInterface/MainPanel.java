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

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import stock.Investments;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 9193463064365388089L;
	private Tables tables;
	private PortfolioView view;
	private Investments invest;
	
	public MainPanel(Investments invest, PortfolioView view) {
		super(new GridLayout(1,0));
		this.view = view;
		this.invest = invest;
		tables = new Tables(invest);
		add(tables.getHomeTable(view));
	}
	
	public void showHome() {
		remove(0);
		add(tables.getHomeTable(view));
		updateUI();
	}
	
	public void showHistory() {
		remove(0);
		add(tables.getHistoryTable());
		updateUI();
	}
	
	public void showOptimization() {
		remove(0);
		add(tables.getOptimizationTable(this));
		updateUI();
	}
	
	public void showMarkets() {
		remove(0);
		add(tables.getMarketTable());
		updateUI();
	}
	
	public void updateOptimization() {
		Double[] personal = tables.getPersonal();
		Double value = invest.getPortfolioValueSum() + invest.getPortfolioLiquid();
		JTable table = (JTable) ((JViewport) ((JScrollPane) ((JPanel) getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0);
		Double[][] histories = invest.getHistory(4, 1);
		for (int i=0; i<personal.length; i++) {
			table.setValueAt(new Integer((int) Math.round(personal[i] * value / histories[i+1][0])), i, 2);
		}
		table.setValueAt(invest.getLambda(), 1, 5);
	}
}
