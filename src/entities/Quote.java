package entities;

import java.io.Serializable;

public class Quote implements Serializable{
	//Date,Open,High,Low,Close,Volume,Adj Close
	//2013-10-18,13.85,13.90,13.83,13.83,10853100,13.83
	private static final long serialVersionUID = 1L;
	private String date;
	private String time;
	private String companyName;
	private double openValue;
	private double highValue;
	private double lowerValue;
	private double closeValue;
	private long volume;
	private double adjCloseValue;
	
	public Quote(){
		this.date = null;
		this.time = null;
		this.setCompanyName(null);
		this.openValue = 0.0;
		this.highValue = 0.0;
		this.lowerValue = 0.0;
		this.closeValue = 0.0;
		this.volume = 0;
		this.adjCloseValue = 0.0;		
	}
	
	public Quote(String date, double openValue, double highValue, double lowerValue,
			double closeValue, long volume, double adjCloseValue){
		this.date = date;
		this.openValue = openValue;
		this.highValue = highValue;
		this.lowerValue = lowerValue;
		this.closeValue = closeValue;
		this.volume = volume;
		this.adjCloseValue = adjCloseValue;		
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getOpenValue() {
		return openValue;
	}
	public void setOpenValue(double openValue) {
		this.openValue = openValue;
	}
	public double getHighValue() {
		return highValue;
	}
	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}
	public double getLowerValue() {
		return lowerValue;
	}
	public void setLowerValue(double lowerValue) {
		this.lowerValue = lowerValue;
	}
	public double getCloseValue() {
		return closeValue;
	}
	public void setCloseValue(double closeValue) {
		this.closeValue = closeValue;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public double getAdjCloseValue() {
		return adjCloseValue;
	}
	public void setAdjCloseValue(double adjCloseValue) {
		this.adjCloseValue = adjCloseValue;
	}
	@Override
	public String toString(){
		return "{Date: " + this.date + "; " + 
				"Open: " + this.openValue + "; "+
				"High: " + this.highValue + "; " + 
				"Low: " + this.lowerValue + "; " + 
				"Close: " + this.closeValue + "; " + 
				"Volume: " + this.volume + "; " + 
				"Adj Close: " + this.adjCloseValue + "}\n";
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String toStringDisplay(){
		return "Company: " + getCompanyName()+
				"\nDate: " + this.date +  
				"\nValue per share: " + this.closeValue +  
				" $\nVolume: " + this.volume ;
	}
	
}
