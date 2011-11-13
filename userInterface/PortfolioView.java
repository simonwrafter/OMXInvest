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

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import stock.Investments;
import stock.Market;
import stock.Portfolio;

public class PortfolioView {
	private CommandPanel commandPanel;
	private JMenu portfolioMenu;
	private MainPanel mainPanel;
	private JFrame frame;
	private Investments investments;
	
	public PortfolioView()
			throws IOException, ParserConfigurationException, SAXException {
		investments = new Investments();
		
		frame = new JFrame("Invest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandPanel = new CommandPanel(this);
		frame.add(commandPanel, BorderLayout.SOUTH);
		
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new JMenuItem(investments.getCurrentPortfolio().getName()));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(portfolioMenu);
		
		mainPanel = new MainPanel(this);
		
		frame.add(mainPanel);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
	}
	
	public SortedSet<Market> getMarketSet() {
		return investments.getMarketSet();
	}
	
	public Portfolio getCurrentPortfolio() {
		return investments.getCurrentPortfolio();
	}
	
	public Double[][] getPortfolioHistory() {
		return investments.getHistory(4);
	}
	
	public Double[][] getPortfolioHistory(int nbrOfDays) {
		return investments.getHistory(4, nbrOfDays);
	}
	
	public void showPanel(Panel panel) {
		switch (panel) {
		case HOME:
			mainPanel.showHome();
			break;
		case HISTORY:
			mainPanel.showHistory();
			break;
		case OPTIMAL:
			mainPanel.showOptimization();
			break;
		case MARKET:
			mainPanel.showMarkets();
			break;
		case NEWS:
			mainPanel.showNews();
			break;
		}
	}
	
	public double portfolioValue() {
		return investments.getPortfolioValue();
	}
	
	public double getPortfolioLiquid() {
		return investments.getPortfolioLiquid();
	}
	
	public void setPortfolioLiquid(double value) {
		investments.setPortfolioLiquid(value);
	}
	
	public String[] getStockNames() {
		return investments.getStockNames();
	}
	
	public String[] getShortNames() {
		return investments.getShortNames();
	}
	
	public void updateOptimization() {
		mainPanel.updateOptimization();
	}
}
