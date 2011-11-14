package stock;

public enum Currency {
	SEK, DKK, EUR, ISK;
	
	public static Currency getCurrency(String currency) {
		if (currency.equals("SEK")) {
			return SEK;
		}
		if (currency.equals("DKK")) {
			return DKK;
		}
		if (currency.equals("EUR")) {
			return EUR;
		}
		if (currency.equals("ISK")) {
			return ISK;
		}
		
		return null;
	}
}
