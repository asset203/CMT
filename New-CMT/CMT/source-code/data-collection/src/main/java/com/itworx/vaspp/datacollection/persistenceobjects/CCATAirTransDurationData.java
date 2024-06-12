package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CCATAirTransDurationData  extends PersistenceObject{
	
	public Date dateTime;
	public String transName;
	public double minDuration;
	public double maxDuration;
	public double avgDuration;
public CCATAirTransDurationData()
{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
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
	
}
