package userInterface;

import java.awt.*;
import javax.swing.*;

public class CommandPanel extends JPanel {
	public CommandPanel(PortfolioView view) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new HomeButton(view));
		add(new HistoryButton(view));
		add(new OptimizeButton(view));
		add(new MarketButton(view));
	}
}
