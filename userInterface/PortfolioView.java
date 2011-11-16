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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import stock.Investments;
import stock.Market;
import stock.Portfolio;

public class PortfolioView extends JFrame implements WindowListener {
	private static final long serialVersionUID = -7129233116646387153L;
	private MainCommandPanel mainCommandPanel;
	private JMenu portfolioMenu;
	private MainPanel mainPanel;
	private Investments investments;
	
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
		
		portfolioMenu = new JMenu("Portfolios");
		portfolioMenu.add(new JMenuItem(investments.getCurrentPortfolio().getName()));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(portfolioMenu);
		
		mainPanel = new MainPanel(this);
		
		this.add(mainPanel);
		this.setJMenuBar(menuBar);
		
		this.addWindowListener(this);
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
		System.out.println("Done!");
	}
	
	public Collection<Market> getMarketSet() {
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
		case ADD:
			final String omxId_add = JOptionPane.showInputDialog("Type omxId of stock to add to portfolio:").toUpperCase();
			if (omxId_add == null) {
				break;
			}
			try {
				investments.addStockToPortfolio(omxId_add);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Likely an input error, please try again",
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			} catch (NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			} catch (NamingException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			mainPanel.showHome();
			break;
		case REMOVE:
			String omxId_remove = JOptionPane.showInputDialog("Type omxId of stock to remove from portfolio:").toUpperCase();
			if (omxId_remove == null) {
				break;
			}
			try {
				investments.removeStockfromPortfolio(omxId_remove);
			} catch (NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"OMXInvest", JOptionPane.ERROR_MESSAGE);
			}
			mainPanel.showHome();
			break;
		case BUY:
			String omxId_buy = JOptionPane.showInputDialog("Type omxId of stock to buy shares in:").toUpperCase();
			if (omxId_buy == null) {
				break;
			}
			if (!getCurrentPortfolio().contains(omxId_buy)) {
				break;
			}
			int nbrToBuy = new Integer(JOptionPane.showInputDialog("How many shares do you wish to buy?"));
			getCurrentPortfolio().buy(omxId_buy, nbrToBuy, investments.getLastValue(omxId_buy));
			mainPanel.showHome();
			break;
		case SELL:
			String omxId_sell = JOptionPane.showInputDialog("Type omxId of stock to sell shares of:").toUpperCase();
			if (omxId_sell == null) {
				break;
			}
			if (!getCurrentPortfolio().contains(omxId_sell)) {
				break;
			}
			int nbrToSell = new Integer(JOptionPane.showInputDialog("How many shares do you wish to sell?"));
			getCurrentPortfolio().sell(omxId_sell, nbrToSell, investments.getLastValue(omxId_sell));
			mainPanel.showHome();
			mainPanel.showHome();
			break;
		case LIQUID:
			String asset = JOptionPane.showInputDialog("Set liquid asset to:");
			if (asset == null) {
				break;
			}
			getCurrentPortfolio().setLiquidAsset(new Double(asset));
			mainPanel.showHome();
			break;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		investments.save();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}
}
