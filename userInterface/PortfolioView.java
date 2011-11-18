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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import stock.Investments;
import stock.Market;
import stock.Portfolio;

public class PortfolioView extends JFrame implements WindowListener {
	private static final long serialVersionUID = -7129233116646387153L;
	private MainCommandPanel mainCommandPanel;
	private MainMenuBar mainMenuBar;
	private MainPanel mainPanel;
	private Investments investments;
	private Actions	currentView;
	
	public PortfolioView()
			throws IOException, ParserConfigurationException, SAXException, NamingException {
		super("OMXInvest");
		JLabel label = new JLabel("Starting...", JLabel.CENTER);
		label.setPreferredSize(new Dimension(480, 300));
		this.add(label);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
		
		investments = new Investments(label);
		
		this.setVisible(false);
		this.remove(label);
		
		mainCommandPanel = new MainCommandPanel(this);
		this.add(mainCommandPanel, BorderLayout.SOUTH);
		
		mainPanel = new MainPanel(this);
		this.add(mainPanel, BorderLayout.CENTER);
		currentView = Actions.HOME;
		
		mainMenuBar = new MainMenuBar(this);
		this.setJMenuBar(mainMenuBar);
		
		this.addWindowListener(this);
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
		System.out.println("Done!");
	}
	
	public Collection<Market> getMarketSet() {
		return investments.getMarketSet();
	}
	
	public Collection<Portfolio> getPortfolios() {
		return investments.getPortfolios();
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
	
	public void updateOptimization() {
		mainPanel.updateOptimization();
	}
	
	public double portfolioValue() {
		return investments.getPortfolioValueSum();
	}
	
	public Double[] getPortfolioDistribution() {
		return investments.getPortfolioDistribution();
	}
	
	public double getPortfolioLiquid() {
		return investments.getPortfolioLiquid();
	}
	
	public void setPortfolioLiquid(double value) {
		investments.setPortfolioLiquid(value);
	}

	public String[] getStockIds() {
		return investments.getStockIds();
	}
	
	public String[] getStockNames() {
		return investments.getStockNames();
	}
	
	public String[] getShortNames() {
		return investments.getShortNames();
	}
	
	public void actionHandler(Actions actions) {
		actionHandler(actions, null);
	}
	
	public void actionHandler(Actions actions, String additionalInfo) {
		switch (actions) {
		case HOME:
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case HISTORY:
			mainPanel.showHistory();
			currentView = Actions.HISTORY;
			break;
		case OPTIMAL:
			mainPanel.showOptimization();
			currentView = Actions.OPTIMAL;
			break;
		case MARKET:
			mainPanel.showMarkets();
			currentView = Actions.MARKET;
			break;
		case ADD:
			String omxId_add = MainOptionPane.getString("Type omxId of stock to add to portfolio:");
			if (omxId_add == null) { break; }
			omxId_add = omxId_add.toUpperCase();
			try {
				investments.addStockToPortfolio(omxId_add);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			}
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case REMOVE:
			String omxId_remove = MainOptionPane.getString("Type omxId of stock to remove from portfolio:");
			if (omxId_remove == null) { break; }
			omxId_remove = omxId_remove.toUpperCase();
			try {
				investments.removeStockfromPortfolio(omxId_remove);
			} catch (NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			}
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case BUY:
			String omxId_buy = MainOptionPane.getString("Type omxId of stock to buy shares in:");
			if (omxId_buy == null) { break; }
			omxId_buy = omxId_buy.toUpperCase();
			if (!getCurrentPortfolio().contains(omxId_buy)) { break; }
			getCurrentPortfolio().buy(omxId_buy,
					MainOptionPane.getInteger("How many shares do you wish to buy?"),
					investments.getLastValue(omxId_buy));
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case SELL:
			String omxId_sell = MainOptionPane.getString("Type omxId of stock to sell shares of:");
			if (omxId_sell == null) { break; }
			omxId_sell = omxId_sell.toUpperCase();
			if (!getCurrentPortfolio().contains(omxId_sell)) { break; }
			getCurrentPortfolio().sell(omxId_sell,
					MainOptionPane.getInteger("How many shares do you wish to sell?"),
					investments.getLastValue(omxId_sell));
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case LIQUID:
			Double asset = MainOptionPane.getDouble("Set liquid asset to:");
			if (asset == null) { break; }
			getCurrentPortfolio().setLiquidAsset(asset);
			mainPanel.showHome();
			currentView = Actions.HOME;
			break;
		case NEW:
			Portfolio portfolio = MainOptionPane.makeNewPortfolio();
			if (portfolio == null) { break; }
			if (investments.addNewPortfolio(portfolio)) {
				mainMenuBar.addPortfolio(portfolio);
				actionHandler(Actions.SWITCH, portfolio.getName());
			}
			break;
		case DELETE:
			String name = MainOptionPane.getString("Name of portfolio to remove");
			if (name == null) { break; }
			if (investments.getPortfolios().size() > 1 && investments.removePortfolio(name)) {
				mainMenuBar.removePortfolio(name);
				actionHandler(Actions.SWITCH, "");
			}
			break;
		case EDIT:
			String oldName = getCurrentPortfolio().getName();
			MainOptionPane.editPortfolioName(getCurrentPortfolio());
			mainMenuBar.removePortfolio(oldName);
			mainMenuBar.addPortfolio(getCurrentPortfolio());
			break;
		case SWITCH:
			investments.setCurrentPortfolio(additionalInfo);
			actionHandler(currentView);
			break;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.setVisible(false);
		investments.save();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// Auto-generated method stub
	}
}
