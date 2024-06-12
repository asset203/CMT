package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MckHttpReqsOutput extends PersistenceObject {

	public Date dateTime;
	public String callForwardCondition;
	public String clir;
	public String requestStatus;
	public long counter;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCallForwardCondition() {
		return callForwardCondition;
	}
	public void setCallForwardCondition(String callForwardCondition) {
		this.callForwardCondition = callForwardCondition;
	}
	public String getClir() {
		return clir;
	}
	public void setClir(String clir) {
		this.clir = clir;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public long getCounter() {
		return counter;
	}
	public void setCounter(long counter) {
		this.counter = counter;
	}
	
	
}
