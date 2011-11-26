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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import stock.Currency;
import stock.Investments;

public class PortfolioView extends JFrame implements WindowListener {
	private static final long serialVersionUID = -7129233116646387153L;
	private MenuBar mainMenuBar;
	private MainPanel mainPanel;
	private Investments investments;

	public PortfolioView()
			throws IOException, ParserConfigurationException, SAXException {
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

		mainPanel = new MainPanel(investments, this);
		this.add(mainPanel, BorderLayout.CENTER);

		mainMenuBar = new MenuBar(this, investments);
		this.setJMenuBar(mainMenuBar);

		this.addWindowListener(this);
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
		System.out.println("Done!");
	}

	public void actionHandler(Actions actions)
			throws ParserConfigurationException {
		actionHandler(actions, null);
	}

	public void actionHandler(Actions actions, String additionalInfo)
			throws ParserConfigurationException {
		switch (actions) {
		case ADD:
			String omxId_add = PopUpQuestion.getString("Type omxId of stock to add to portfolio:");
			if (omxId_add != null) {
				omxId_add = omxId_add.toUpperCase();
				try {
					investments.addStockToPortfolio(omxId_add);
					mainPanel.updateHomePanel();
					mainPanel.updateHistoryPanel();
					mainPanel.updateOptimizationPanel();
				} catch (Exception e) {
					PopUpQuestion.errorPopUp("Adding " + omxId_add + " to portfolio failed!");
				}
			}
			break;
		case REMOVE:
			String omxName_remove = (String) PopUpQuestion.dropDownOptions("Choose stock to remove from portfolio:", investments.getStockNames(), 0);
			if (omxName_remove != null) {
				if (investments.removeStockfromPortfolioByName(omxName_remove)) {
					mainPanel.updateHomePanel();
					mainPanel.updateHistoryPanel();
					mainPanel.updateOptimizationPanel();
				}
			}
			break;
		case BUY:
			String omxName_buy = (String) PopUpQuestion.dropDownOptions("Stock to buy shares in:", investments.getStockNames(), 0);
			if (omxName_buy != null) {
				Integer nbrToBuy = PopUpQuestion.getInteger("How many shares do you wish to buy?");
				if (nbrToBuy != null) {
					if (investments.buy(omxName_buy, nbrToBuy)) {
						mainPanel.updateHomePanel();
					}
				}
			}
			break;
		case SELL:
			String omxName_sell = (String) PopUpQuestion.dropDownOptions("Stock to sell shares in:", investments.getStockNames(), 0);
			if (omxName_sell != null) {
				Integer nbrToSell = PopUpQuestion.getInteger("How many shares you wish to sell:");
				if (nbrToSell != null) {
					if (investments.sell(omxName_sell, nbrToSell)) {
						mainPanel.updateHomePanel();
					}
				}
			}
			break;
		case LIQUID:
			Double asset = PopUpQuestion.getDouble("Set liquid asset to:", investments.getLiquid());
			if (asset != null) {
				investments.setLiquid(asset);
				mainPanel.updateHomePanel();
				mainPanel.updateOptimizationPanel();
			}
			break;
		case NEW:
			String nameOfNewP = PopUpQuestion.getString("New portfolio name.");
			if (nameOfNewP==null) { break; }
			Currency currency = (Currency) PopUpQuestion.dropDownOptions("Currency for new portfolio", Currency.currencies, 0);
			if (currency==null) { break; }
			Integer liquid = PopUpQuestion.getInteger("Initial liquid assets");
			if (liquid==null) { break; }
			if (!investments.addNewPortfolio(nameOfNewP, currency, liquid)) {
				PopUpQuestion.errorPopUp("A portfolio with this name already exists!");
				break;
			}
			mainMenuBar.addPortfolio(nameOfNewP);
			actionHandler(Actions.SWITCH, nameOfNewP);
			break;
		case DELETE:
			String name = (String) PopUpQuestion.dropDownOptions("Name of portfolio to remove:", investments.getPortfolios());
			if (name == null) { break; }
			if (investments.getPortfolios().size() < 2) {
				PopUpQuestion.errorPopUp("You can not remove any more portfolios!");
				break;
			}
			if (!investments.removePortfolio(name)) {
				PopUpQuestion.errorPopUp("No portfolio with this name exists!");
				break;
			}
			mainMenuBar.removePortfolio(name);
			actionHandler(Actions.SWITCH);
			break;
		case EDIT:
			String oldName = investments.getPortfolioName();
			String newName = PopUpQuestion.getString("Type new name for portfolio:", oldName);
			if (newName != null && !newName.equals(oldName)) {
				investments.setPortfolioName(newName);
				mainMenuBar.removePortfolio(oldName);
				mainMenuBar.addPortfolio(newName);
			}
			break;
		case SWITCH:
			investments.setCurrentPortfolio(additionalInfo);
			mainPanel.updateHomePanel();
			mainPanel.updateHistoryPanel();
			mainPanel.updateOptimizationPanel();
			break;
		case REBUILD_HISTORY:
			try {
				investments.rebuildHistory();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mainPanel.updateHistoryPanel();
			break;
		case UPDATE_MARKETS:
			investments.updateMarkets();
			mainPanel.updateMarketPanel();
			break;
		case ABOUT:
			PopUpQuestion.infoPopUp(
					"This program, OMXInvest, is being developed by me, Simon Wrafter,\n" +
							"because it is good fun. Also it might come in handy, if not for me,\n" +
							"maybe for you or your friend.\n\n" +
							"If you find it to be useful or just like java programming or any other\n" +
							"valid reason, feel free to participate in what way you can.\n" +
							"Or just send me an e mail with a happy smiley! :D\n\n" +
							"OMXInvest is mainley licensed under the ICS license, which can be\n" +
							"found under 'ICS License' in the 'More' menu. The code from JAMA is\n" +
							"public domain, a copyright notice containing more information can\n" +
							"be found under 'JAMA © Notice' in the 'More' menu.\n\n" +
					"Code hosted at https://github.com/simonwrafter/OMXInvest");
			break;
		case ICS_LICENSE:
			PopUpQuestion.infoPopUp("Copyright (c) 2011, Simon Wrafter <simon.wrafter@gmail.com>\n\n" +
					"Permission to use, copy, modify, and/or distribute this software for any\n" +
					"purpose with or without fee is hereby granted, provided that the above\n" +
					"copyright notice and this permission notice appear in all copies.\n\n" +
					"THE SOFTWARE IS PROVIDED \"AS IS\" AND THE AUTHOR DISCLAIMS ALL WARRANTIES\n" +
					"WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF\n" +
					"MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR\n" +
					"ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES\n" +
					"WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN\n" +
					"ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF\n" +
					"OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.");
			break;
		case JAMA_C:
			PopUpQuestion.infoPopUp("JAMA Copyright Notice\n\n" +
					"This software is a cooperative product of The MathWorks and the National\n" +
					"Institute of Standards and Technology (NIST) which has been released to the\n" +
					"public domain. Neither The MathWorks nor NIST assumes any responsibility\n" +
					"whatsoever for its use by other parties, and makes no guarantees, expressed\n" +
					"or implied, about its quality, reliability, or any other characteristic.\n\n" +
					"Read more at http://math.nist.gov/javanumerics/jama/.");
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
