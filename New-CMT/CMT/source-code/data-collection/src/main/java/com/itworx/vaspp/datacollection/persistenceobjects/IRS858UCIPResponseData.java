package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class IRS858UCIPResponseData extends PersistenceObject{

	public Date dateTime;
	public String prefix;
	public String airIP;
	public String transactionType;
	public String response;
	public double count;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getAirIP() {
		return airIP;
	}
	public void setAirIP(String airIP) {
		this.airIP = airIP;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	
	
}
