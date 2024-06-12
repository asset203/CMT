package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DiameterInterfaceQualityData extends PersistenceObject{
	
	public Date dateTime;
	
	public long totalReceived;
	public long totalAnswered ;
	
	public double topReceived;
	public double topAnswered ;
	
	public String connectionId;
	public String applicationName;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTopAnswered() {
		return topAnswered;
	}
	public void setTopAnswered(double topAnswered) {
		this.topAnswered = topAnswered;
	}
	
	public double getTopReceived() {
		return topReceived;
	}
	public void setTopReceived(double topReceived) {
		this.topReceived = topReceived;
	}
	public long getTotalAnswered() {
		return totalAnswered;
	}
	public void setTotalAnswered(long totalAnswered) {
		this.totalAnswered = totalAnswered;
	}
	
	public long getTotalReceived() {
		return totalReceived;
	}
	public void setTotalReceived(long totalReceived) {
		this.totalReceived = totalReceived;
	}
	
	

}
