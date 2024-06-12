package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class EngezlyUsageLogDData extends PersistenceObject{
	public Date dateTime;
	public String service;
	public double distinctUser;
	public double usage;
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
	public double getDistinctUser() {
		return distinctUser;
	}
	public void setDistinctUser(double distinctUser) {
		this.distinctUser = distinctUser;
	}
	public double getUsage() {
		return usage;
	}
	public void setUsage(double usage) {
		this.usage = usage;
	}
public EngezlyUsageLogDData()
{
}
}
