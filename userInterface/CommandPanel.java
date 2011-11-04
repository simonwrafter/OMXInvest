package userInterface;

import java.awt.FlowLayout;

import javax.swing.*;

public class CommandPanel extends JPanel {
	public CommandPanel(PortfolioView pv) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new HistoryTable(pv));
	}
}
