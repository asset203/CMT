package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class QueuedSmsData extends PersistenceObject{
	public Date dateTime;
	public String roameryType; 
	public double  smsCount; 
	public QueuedSmsData()
	{}
	public double getSmsCount() {
		return smsCount;
	}
	public void setSmsCount(double smsCount) {
		this.smsCount = smsCount;
	}
	
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getRoameryType() {
		return roameryType;
	}
	public void setRoameryType(String roameryType) {
		this.roameryType = roameryType;
	}
	
	
	
	
}
