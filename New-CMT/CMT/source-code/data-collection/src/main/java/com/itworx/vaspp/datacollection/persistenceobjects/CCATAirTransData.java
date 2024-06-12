package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CCATAirTransData extends PersistenceObject{
	public Date dateTime;
	public String userName;
	public String transName;
	public String transStatus;
	public double duration;
public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
public CCATAirTransData()
{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	
	
}
