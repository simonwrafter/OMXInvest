package util;

import java.text.ParseException;

public class InvestDate {
	
	public static String today() {
		return date(0);
	}
	
	public static String date(int daysAgo) {
		long epoch = System.currentTimeMillis() - (daysAgo * 86400000L);
		return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(epoch));
	}
	
	public static String todayNoDash(int daysAgo) {
		return dateNoDash(0);
	}
	
	public static String dateNoDash(int daysAgo) {
		long epoch = System.currentTimeMillis() - (daysAgo * 86400000L);
		return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(epoch));
	}
	
	public static int nbrOfPastDays(String date) throws ParseException {
		long time = new java.text.SimpleDateFormat ("yyyy-MM-dd").parse(date).getTime();
		return (int) (System.currentTimeMillis() - time) / (1000*60*60*24);
	}
	
	public static String makeDateString(double date) {
		int di = (int) date;
		return String.format("%d-%d-%d", di/10000, (di/100)%100, di%100);
	}
}
