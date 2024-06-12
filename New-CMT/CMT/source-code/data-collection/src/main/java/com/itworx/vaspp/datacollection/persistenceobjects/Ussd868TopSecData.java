package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class Ussd868TopSecData extends PersistenceObject{
	
	public Date dateTime;
	
	public double topNoOfRequests;
	
	public long shortCode;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getShortCode() {
		return shortCode;
	}

	public void setShortCode(long shortCode) {
		this.shortCode = shortCode;
	}

	public double getTopNoOfRequests() {
		return topNoOfRequests;
	}

	public void setTopNoOfRequests(double topNoOfRequests) {
		this.topNoOfRequests = topNoOfRequests;
	}

}
