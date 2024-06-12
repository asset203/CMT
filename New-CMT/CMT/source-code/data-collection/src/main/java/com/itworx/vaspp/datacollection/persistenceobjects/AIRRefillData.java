package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AIRRefillData extends PersistenceObject {
	public Date dateTime;
	public String service;
	public String cdrnodeName;
	public String getCdrnodeName() {
		return cdrnodeName;
	}
	public void setCdrnodeName(String cdrnodeName) {
		this.cdrnodeName = cdrnodeName;
	}
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
public AIRRefillData()
{}
}
