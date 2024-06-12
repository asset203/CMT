package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class EOCNBrowsersSMSData extends PersistenceObject {
	
	public Date dateTime;
	public double smsTotal;
	public double dataTotal;
	
	public double getSmsTotal() {
		return smsTotal;
	}
	public void setSmsTotal(double smsTotal) {
		this.smsTotal = smsTotal;
	}
	public double getDataTotal() {
		return dataTotal;
	}
	public void setDataTotal(double dataTotal) {
		this.dataTotal = dataTotal;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	
}

