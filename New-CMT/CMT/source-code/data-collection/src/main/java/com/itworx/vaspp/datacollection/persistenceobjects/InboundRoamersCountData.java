package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class InboundRoamersCountData extends PersistenceObject{
	
	public Date dateTime;
	public double currentRoamerCount;
	public String countryName;
	public String roamerName;
	
	
	public InboundRoamersCountData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getCurrentRoamerCount() {
		return currentRoamerCount;
	}

	public void setCurrentRoamerCount(double currentRoamerCount) {
		this.currentRoamerCount = currentRoamerCount;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRoamerName() {
		return roamerName;
	}

	public void setRoamerName(String roamerName) {
		this.roamerName = roamerName;
	}
	

}
