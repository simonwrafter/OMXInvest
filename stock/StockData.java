package stock;

public class StockData {
	
	private String omxId;
	private String shortName;
	private String fullName;
	private String ISIN;
	private String market;
	private String currency;
	/**
	 * @return the omxId
	 */
	public String getOmxId() {
		return omxId;
	}
	/**
	 * @param omxId the omxId to set
	 */
	public void setOmxId(String omxId) {
		this.omxId = omxId;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the iSIN
	 */
	public String getISIN() {
		return ISIN;
	}
	/**
	 * @param iSIN the iSIN to set
	 */
	public void setISIN(String ISIN) {
		this.ISIN = ISIN;
	}
	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}
	/**
	 * @param market the market to set
	 */
	public void setMarket(String market) {
		this.market = market;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
