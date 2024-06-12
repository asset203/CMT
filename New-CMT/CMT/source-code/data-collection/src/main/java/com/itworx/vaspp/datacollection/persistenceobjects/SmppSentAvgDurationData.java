package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmppSentAvgDurationData  extends PersistenceObject{
	public Date dateTime;
	public double minDuration;
	public double maxDuration;
	public double avgDuration;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
public SmppSentAvgDurationData()
{
}
}
