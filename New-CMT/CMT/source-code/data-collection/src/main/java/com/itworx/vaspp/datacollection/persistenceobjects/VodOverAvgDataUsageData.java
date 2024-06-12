package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VodOverAvgDataUsageData extends PersistenceObject{
	public Date dateTime;
	public double totalUniqueUser;
	public double avgUsage;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTotalUniqueUser() {
		return totalUniqueUser;
	}
	public void setTotalUniqueUser(double totalUniqueUser) {
		this.totalUniqueUser = totalUniqueUser;
	}
	public double getAvgUsage() {
		return avgUsage;
	}
	public void setAvgUsage(double avgUsage) {
		this.avgUsage = avgUsage;
	}
public VodOverAvgDataUsageData()
{
}
}
