package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class USSDGWC2DurationData extends PersistenceObject{
public Date date;
	
	public String shortCode;
	
	public long minDuration;
	
	public long maxDuration;
	
	public long avgDuration;
public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public long getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(long minDuration) {
		this.minDuration = minDuration;
	}
	public long getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}
	public long getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(long avgDuration) {
		this.avgDuration = avgDuration;
	}
public USSDGWC2DurationData()
{
}
}
