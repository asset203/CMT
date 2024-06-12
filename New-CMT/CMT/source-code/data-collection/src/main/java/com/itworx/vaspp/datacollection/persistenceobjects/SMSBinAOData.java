package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinAOData extends PersistenceObject{
	public Date dateTime;
	public String appName;
	public double cout;
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
	public double getCout() {
		return cout;
	}
	public void setCout(double cout) {
		this.cout = cout;
	}
public SMSBinAOData()
{
	}
}
