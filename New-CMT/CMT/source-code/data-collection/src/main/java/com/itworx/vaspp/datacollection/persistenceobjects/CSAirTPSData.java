package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CSAirTPSData  extends PersistenceObject {
	
	public Date dateTime;
	public double maxTPS;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMaxTPS() {
		return maxTPS;
	}
	public void setMaxTPS(double maxTPS) {
		this.maxTPS = maxTPS;
	}
}