package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class EocnBrowsersReqRespData extends PersistenceObject{
	
	public Date dateTime;
	public double requests;
	public double responses;
	public String shortCode;
	
	public EocnBrowsersReqRespData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getRequests() {
		return requests;
	}

	public void setRequests(double requests) {
		this.requests = requests;
	}

	public double getResponses() {
		return responses;
	}

	public void setResponses(double responses) {
		this.responses = responses;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	
}
