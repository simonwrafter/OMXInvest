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

import java.awt.FlowLayout;
import javax.swing.JPanel;

public class MainCommandPanel extends JPanel {
	private static final long serialVersionUID = -7610361891529600959L;

	public MainCommandPanel(PortfolioView view) {
		setLayout(new FlowLayout());
		add(new Button(Actions.HOME, "Home", "Show portfolio info", view));
		add(new Button(Actions.HISTORY, "History", "Show history", view));
		add(new Button(Actions.OPTIMAL, "Optimization", "Show optimal portfolio",view));
		add(new Button(Actions.MARKET, "Markets and Stocks", "Show all markets and their list of stocks", view));
	}
}
