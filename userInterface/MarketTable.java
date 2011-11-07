package userInterface;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import stock.*;

public class MarketTable {
	private PortfolioView view;
	
	public MarketTable(PortfolioView view) {
		this.view = view;
	}
	
	public JTable getTable() {
		SortedSet<Market> set = view.getMarketSet();
		int x = set.size()*3;
		int y = 0;
		for(Market m : set) {
			y = y < m.getMarketMap().size() ? m.getMarketMap().size() : y; 
		}
		Object[] header = new Object[x];
		Object[][] data = new Object[y][x];
		
		Arrays.fill(header, "");
		for (Object[] o : data)
			Arrays.fill(o, "");
		
		int xx=0;
		for (Market m : set) {
			int yy=0;
			header[xx] = m.getListName();
			for(Map.Entry<String, Stock> e : m.getMarketMap().entrySet()) {
				Stock s = e.getValue();
				data[yy][xx] = s.getFullName();
				data[yy][xx+1] = s.getShortName();
				data[yy][xx+2] = s.getOmxId();
				yy++;
			}
			xx += 3;
		}
		
		JTable table = new JTable(data, header);
		table.setMinimumSize(new Dimension(x*50, y*15));
		
		return table;
	}
}
