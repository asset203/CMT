package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNVoiceChargingCountersData extends PersistenceObject{
	
	public Date dateTime;
	public String counterName;
	public double routingValue;
	public double topSec;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public double getRoutingValue() {
		return routingValue;
	}
	public void setRoutingValue(double routingValue) {
		this.routingValue = routingValue;
	}
	public double getTopSec() {
		return topSec;
	}
	public void setTopSec(double topSec) {
		this.topSec = topSec;
	}
	public CCNVoiceChargingCountersData()
	{}
	
}
