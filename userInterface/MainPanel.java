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
import javax.swing.JScrollPane;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 9193463064365388089L;
	private Tables tables;
	private JScrollPane scrollPane;
	
	public MainPanel(PortfolioView view) {
		super(new GridLayout(1,0));
		tables = new Tables(view);
		
		scrollPane = new JScrollPane();
		showHome();
		add(scrollPane);
	}
	
	public void showHistory() {
		scrollPane.setViewportView(tables.getHistoryTable());
	}
	
	public void showOptimization() {
		scrollPane.setViewportView(tables.getOptimizationTable());
	}
	
	public void showMarkets() {
		scrollPane.setViewportView(tables.getMarketTable());
	}
	
	public void showHome() {
		scrollPane.setViewportView(tables.getHomeTable());
	}
}
