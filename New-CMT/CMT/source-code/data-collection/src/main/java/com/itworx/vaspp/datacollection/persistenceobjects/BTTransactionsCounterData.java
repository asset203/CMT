package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BTTransactionsCounterData extends PersistenceObject {
	
	public Date dateTime;
	public String transDesc;
	public String transResultDesc;
	public double distribution;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getDistribution() {
		return distribution;
	}
	public void setDistribution(double distribution) {
		this.distribution = distribution;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getTransResultDesc() {
		return transResultDesc;
	}
	public void setTransResultDesc(String transResultDesc) {
		this.transResultDesc = transResultDesc;
	}
	
	

}
