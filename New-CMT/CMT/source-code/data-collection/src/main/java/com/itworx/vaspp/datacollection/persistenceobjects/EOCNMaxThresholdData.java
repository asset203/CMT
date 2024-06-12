package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNMaxThresholdData extends PersistenceObject {
	public Date dateTime;
	public String minit;
	public String second;
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public double reqCount;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getMinit() {
		return minit;
	}
	public void setMinit(String minit) {
		this.minit = minit;
	}
	
	public double getReqCount() {
		return reqCount;
	}
	public void setReqCount(double reqCount) {
		this.reqCount = reqCount;
	}
	public EOCNMaxThresholdData()
	{}
}
