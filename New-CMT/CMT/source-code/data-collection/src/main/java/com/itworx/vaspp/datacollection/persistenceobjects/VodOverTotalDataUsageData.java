package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VodOverTotalDataUsageData extends PersistenceObject{
	public Date dateTime;
	public double totalDataUsage;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTotalDataUsage() {
		return totalDataUsage;
	}
	public void setTotalDataUsage(double totalDataUsage) {
		this.totalDataUsage = totalDataUsage;
	}
public VodOverTotalDataUsageData()
{
}
}
