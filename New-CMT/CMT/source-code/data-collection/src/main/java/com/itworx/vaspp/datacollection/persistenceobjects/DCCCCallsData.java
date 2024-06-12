package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class DCCCCallsData extends PersistenceObject{
	public Date dateTime;
	public  String serviceNo;
	public double count;
	public double minDuration;
	public double maxDuration;
	public double avgDuration;
	public double releaseCause;
	
	
	public double getReleaseCause() {
		return releaseCause;
	}
	public void setReleaseCause(double releaseCause) {
		this.releaseCause = releaseCause;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getServiceNo() {
		return serviceNo;
	}
	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public double getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(double minDuration) {
		this.minDuration = minDuration;
	}
	public double getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}
	public double getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(double avgDuration) {
		this.avgDuration = avgDuration;
	}
	public DCCCCallsData ()
{}
}
