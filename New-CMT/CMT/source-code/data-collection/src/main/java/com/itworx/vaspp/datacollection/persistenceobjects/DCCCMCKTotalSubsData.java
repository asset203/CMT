package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class DCCCMCKTotalSubsData extends PersistenceObject {
	public Date dateTime;
	public String serviceType;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public DCCCMCKTotalSubsData()
{
}
}
