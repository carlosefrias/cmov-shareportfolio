package database;

import java.io.Serializable;

public class SimpleQuoteDB implements Serializable {
	private static final long serialVersionUID = -4179572179131849053L;
	private String companyName;
	private int shareNumber;
	private double currentValue;
	
	public SimpleQuoteDB(){
		this.companyName = null;
		this.shareNumber = 0;
		this.currentValue = 0.0;
	}
	public SimpleQuoteDB(String cn, int sn, double cv) {
		this.setCompanyName(cn);
		this.setShareNumber(sn);
		this.setCurrentValue(cv);
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getShareNumber() {
		return shareNumber;
	}

	public void setShareNumber(int shareNumber) {
		this.shareNumber = shareNumber;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	@Override
	public String toString() {
		return "" + this.companyName + ": " + this.currentValue + ": "
				+ this.shareNumber;
	}

}
