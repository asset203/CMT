package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class TotalHeaderLogData extends PersistenceObject{
	public Date dateTime;
	public String componenet;
	public String service;
	public double count;
	public double avgProcTime;
	public double minProcTime;
	public double maxProcTime;
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
	public double getAvgProcTime() {
		return avgProcTime;
	}
	public void setAvgProcTime(double avgProcTime) {
		this.avgProcTime = avgProcTime;
	}
	public double getMinProcTime() {
		return minProcTime;
	}
	public void setMinProcTime(double minProcTime) {
		this.minProcTime = minProcTime;
	}
	public double getMaxProcTime() {
		return maxProcTime;
	}
	public void setMaxProcTime(double maxProcTime) {
		this.maxProcTime = maxProcTime;
	}
public TotalHeaderLogData()
{
}
}
