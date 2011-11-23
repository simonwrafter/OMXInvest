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

import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import stock.Investments;

public class LambdaSlider extends JSlider {
	private static final long serialVersionUID = 3962195519446513896L;
	private Investments invest;
	private MainPanel panel;
	
	public LambdaSlider(Investments invest, MainPanel panel) {
		super(0,100);
		this.invest = invest;
		this.panel = panel;
		setValue((int) Math.round(invest.getLambda()*100));
		setMajorTickSpacing(20);
		setMinorTickSpacing(5);
		
		Hashtable<Integer, JComponent> dict = new Hashtable<Integer, JComponent>();
		dict.put(0, new JLabel("Minimal Risk"));
		dict.put(100, new JLabel("Maximum Growth"));
		setLabelTable(dict);
		setPaintTicks(true);
		setPaintLabels(true);
		addChangeListener(new SL());
	}
	
	private class SL implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			invest.setLambda(getValue() / 100.0);
			panel.updateOptimization();
		}
	}
}
