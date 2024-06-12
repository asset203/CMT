package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNCipIpCountersData extends PersistenceObject{

	public Date dateTime;
	public String destRealm;
	public String chargeContext;
	public double requestAttempts;
	public double receivedResponses;
	public double noResponses;
	
	public String getChargeContext() {
		return chargeContext;
	}
	public void setChargeContext(String chargeContext) {
		this.chargeContext = chargeContext;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDestRealm() {
		return destRealm;
	}
	public void setDestRealm(String destRealm) {
		this.destRealm = destRealm;
	}
	public double getNoResponses() {
		return noResponses;
	}
	public void setNoResponses(double noResponses) {
		this.noResponses = noResponses;
	}
	public double getReceivedResponses() {
		return receivedResponses;
	}
	public void setReceivedResponses(double receivedResponses) {
		this.receivedResponses = receivedResponses;
	}
	public double getRequestAttempts() {
		return requestAttempts;
	}
	public void setRequestAttempts(double requestAttempts) {
		this.requestAttempts = requestAttempts;
	}
	
	
}
