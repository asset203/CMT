package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AIRRefillTransData extends PersistenceObject{
	public Date dateTime;
	public String service;
	public double transCount;
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
	public double getTransCount() {
		return transCount;
	}
	public void setTransCount(double transCount) {
		this.transCount = transCount;
	}
public AIRRefillTransData()
{}
}
