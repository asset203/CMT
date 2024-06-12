package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class IvrServiceUtilizationData extends PersistenceObject {
	
	public Date dateTime;
	public String service;
	public double calls;  
	public double unsuccessfulCalls;
	public double answeredCalls;
	public double getAnsweredCalls() {
		return answeredCalls;
	}
	public void setAnsweredCalls(double answeredCalls) {
		this.answeredCalls = answeredCalls;
	}
	public double getCalls() {
		return calls;
	}
	public void setCalls(double calls) {
		this.calls = calls;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public double getUnsuccessfulCalls() {
		return unsuccessfulCalls;
	}
	public void setUnsuccessfulCalls(double unsuccessfulCalls) {
		this.unsuccessfulCalls = unsuccessfulCalls;
	}

}
