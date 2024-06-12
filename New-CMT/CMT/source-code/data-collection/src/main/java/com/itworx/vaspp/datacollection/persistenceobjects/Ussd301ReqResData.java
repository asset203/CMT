package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class Ussd301ReqResData  extends PersistenceObject{

	public Date dateTime;
	
	public double noOfRequests;
	
	public double noOfResponses;
	
	public long shortCode;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getNoOfRequests() {
		return noOfRequests;
	}

	public void setNoOfRequests(double noOfRequests) {
		this.noOfRequests = noOfRequests;
	}

	public double getNoOfResponses() {
		return noOfResponses;
	}

	public void setNoOfResponses(double noOfResponses) {
		this.noOfResponses = noOfResponses;
	}

	public long getShortCode() {
		return shortCode;
	}

	public void setShortCode(long shortCode) {
		this.shortCode = shortCode;
	}
}