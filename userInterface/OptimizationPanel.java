package userInterface;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import stock.Investments;
import util.CalcModels;

public class OptimizationPanel extends JPanel {
	private static final long serialVersionUID = 841107539929338322L;
	private Investments invest;
	private MainPanel panel;
	private DefaultTableModel model;
	private Double[] minRisk;
	private Double[] maxGrowth;

	public OptimizationPanel(MainPanel panel, Investments invest) {
		super(new BorderLayout());
		this.invest = invest;
		this.panel = panel;
		
		model = new DefaultTableModel();
		JTable table = new JTable(model);
		
		updatePanel();
		
		for (int i = 0; i < 6; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(75);
			} else {
				column.setPreferredWidth(80);
			}
		}
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);

		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(new LambdaSlider(), BorderLayout.SOUTH);
	}

	public void updatePanel() {
		String[] stocks = invest.getShortNames();
		int width = 6;
		int height = Math.max(stocks.length, 5);

		Object[] header = {"name", "min risk", "personal", "max growth", "", ""};
		Object[][] data = new Object[height][width];
		
		Double portfolioValue = invest.getPortfolioValueSum();
		Double portfolioLiquid = invest.getLiquid();
		Double[][] histories;
		histories = invest.getHistory(4);
		Double[][] coV;
		
		if (invest.size() > 0) {
			coV = CalcModels.covariance(histories);
			minRisk = CalcModels.optimizeLowRisk(coV);
			maxGrowth = CalcModels.optimizeHighGrowth(coV, CalcModels.portfolioExpectedValue(histories));
		} else {
			minRisk = new Double[0];
			maxGrowth = new Double[0];
		}
		Double[] personal = getPersonal();

		
		for (Object[] o : data) {
			Arrays.fill(o, "");
		}
		
		for (int i=0; i<stocks.length; i++) {
			data[i][0] = stocks[i];
			data[i][1] = new Integer((int) Math.round(minRisk[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][2] = new Integer((int) Math.round(personal[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][3] = new Integer((int) Math.round(maxGrowth[i] * (portfolioValue + portfolioLiquid) / histories[i+1][0]));
			data[i][4] = "";
			data[i][5] = "";
		}

		data[0][5] = "Lambda";
		data[1][5] = invest.getLambda();

		model.setDataVector(data, header);
		updateUI();
	}
	
	public Double[] getPersonal() {
		return CalcModels.personalPortfolio(minRisk, maxGrowth, invest.getLambda());
	}
	
	private class LambdaSlider extends JSlider implements ChangeListener {
		private static final long serialVersionUID = 3962195519446513896L;
		
		private LambdaSlider() {
			super(0,100);
			setValue((int) Math.round(invest.getLambda()*100));
			setMajorTickSpacing(20);
			setMinorTickSpacing(5);
			
			Hashtable<Integer, JComponent> dict = new Hashtable<Integer, JComponent>();
			dict.put(0, new JLabel("Minimal Risk"));
			dict.put(100, new JLabel("Maximum Growth"));
			setLabelTable(dict);
			setPaintTicks(true);
			setPaintLabels(true);
			addChangeListener(this);
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			invest.setLambda(getValue() / 100.0);
			panel.updatePersonalOptimization();
		}
	}
}
