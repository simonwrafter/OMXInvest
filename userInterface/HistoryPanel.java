package userInterface;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import stock.Investments;
import util.InvestDate;
import util.InvestMatrix;

public class HistoryPanel extends JPanel {
	private static final long serialVersionUID = -8300239271253271499L;
	private Investments invest;
	private DefaultTableModel model;
	
	public HistoryPanel(Investments invest) {
		super(new BorderLayout());
		this.invest = invest;
		
		model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		updatePanel();
		
		for (int i = 0; i < invest.size(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(90);
			} else {
				column.setPreferredWidth(75);
			}
		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		
		this.add(new JScrollPane(table));
	}
	
	public void updatePanel() {
		Object[] header = InvestDate.addDateHeader(invest.getShortNames());
		Object[][] data = InvestMatrix.transpose(invest.getHistory(4));

		for(Object[] o : data) {
			o[0] = InvestDate.dateWithDash(((Double) o[0]).intValue());
			for(int i=1;i<o.length;i++) {
				o[i] = String.format("%.02f", o[i]);
			}
		}
		
		model.setDataVector(data, header);
		updateUI();
	}
}
