package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class TotalEAIErrorData extends PersistenceObject {
	public Date dateTime;
	public String componenet;
	public String service;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getComponenet() {
		return componenet;
	}
	public void setComponenet(String componenet) {
		this.componenet = componenet;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public TotalEAIErrorData()
{
}
}
