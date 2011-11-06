package util;

import java.text.ParseException;

public class InvestDate {
	
	public static String today() {
		return date(0);
	}
	
	public static String date(int daysAgo) {
		long epoch = System.currentTimeMillis() - (daysAgo * 86400000L);;
		return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(epoch));
	}
	
	public static String todayNoDash(int daysAgo) {
		return dateNoDash(0);
	}
	
	public static String dateNoDash(int daysAgo) {
		long epoch = System.currentTimeMillis() - (daysAgo * 86400000L);
		return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(epoch));
	}
	
	public static int nbrOfPastDays(String dateWithDash) throws ParseException {
		long time = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateWithDash).getTime();
		return (int) (System.currentTimeMillis() - time) / (1000*60*60*24);
	}
	
	public static String makeDateString(int dateNoDash) {
		int di = (int) dateNoDash;
		return String.format("%d-%d-%d", di/10000, (di/100)%100, di%100);
	}
	
	public static int makeDateInt(String dateWithDash) {
		return Integer.valueOf(new StringBuilder(dateWithDash).deleteCharAt(4).deleteCharAt(6).toString());
	}
	
	public static Object[] addDateHeader(Object[] array) {
		Object[] result = new Object[array.length + 1];
		result[0] = "Date";
		for (int i=0; i < array.length; i++) {
			result[i+1] = array[i]; 
		}
		return result;
	}
}
