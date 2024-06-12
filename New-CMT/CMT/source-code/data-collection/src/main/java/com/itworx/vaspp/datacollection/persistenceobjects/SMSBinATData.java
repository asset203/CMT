package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinATData extends PersistenceObject{
	public Date dateTime;
	public String appName;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSBinATData()
{
}
}
