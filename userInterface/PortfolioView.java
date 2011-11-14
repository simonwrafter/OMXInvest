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
import java.awt.Dimension;
import java.io.IOException;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import stock.Investments;
import stock.Market;
import stock.Portfolio;

public class PortfolioView {
	private MainCommandPanel mainCommandPanel;
	private JMenu portfolioMenu;
	private MainPanel mainPanel;
	private JFrame frame;
	private Investments investments;
	
	public PortfolioView()
			throws IOException, ParserConfigurationException, SAXException {
		frame = new JFrame("Invest");
		JLabel label = new JLabel("Starting...", JLabel.CENTER);
		label.setPreferredSize(new Dimension(160, 100));
		frame.add(label);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		
		investments = new Investments();
		
		frame.setVisible(false);
		frame.remove(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainCommandPanel = new MainCommandPanel(this);
		frame.add(mainCommandPanel, BorderLayout.SOUTH);
		
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
	
	public void actionHandler(Actions actions) {
		switch (actions) {
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
		case ADD:
			//TODO popup to add stock to portfolio
			mainPanel.showHome();
			break;
		case REMOVE:
			//TODO popup to remove stock from portfolio
			mainPanel.showHome();
			break;
		case BUY:
			//TODO popup to buy shares to portfolio
			mainPanel.showHome();
			break;
		case SELL:
			//TODO popup to sell shares from portfolio
			mainPanel.showHome();
			break;
		case LIQUID:
			//TODO popup to set liquid asset in portfolio
			mainPanel.showHome();
			break;
		}
	}
	
	public void updateOptimization() {
		mainPanel.updateOptimization();
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
}
