package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CCATAirTransLimitData  extends PersistenceObject{
	public Date dateTime;
	public double userId;
	public double limitId;
	public double currValue;
	public CCATAirTransLimitData()
	{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getUserId() {
		return userId;
	}
	public void setUserId(double userId) {
		this.userId = userId;
	}
	public double getLimitId() {
		return limitId;
	}
	public void setLimitId(double limitId) {
		this.limitId = limitId;
	}
	public double getCurrValue() {
		return currValue;
	}
	public void setCurrValue(double currValue) {
		this.currValue = currValue;
	}
	
}
