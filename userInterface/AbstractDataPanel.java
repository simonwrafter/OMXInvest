package userInterface;


import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import stock.Investments;

public abstract class AbstractDataPanel extends JPanel {
	private static final long serialVersionUID = -6868216874208890404L;
	protected Investments invest;
	protected DefaultTableModel model;

	public AbstractDataPanel(Investments invest) {
		super(new BorderLayout());
		this.invest = invest;
		
		model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		updatePanel();
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		this.add(new JScrollPane(table), BorderLayout.CENTER);
	}

	abstract public void updatePanel();
}
