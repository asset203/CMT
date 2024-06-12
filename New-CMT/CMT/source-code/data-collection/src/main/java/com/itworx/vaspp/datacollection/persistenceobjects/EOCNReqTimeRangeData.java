package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class EOCNReqTimeRangeData extends PersistenceObject{
	public Date dateTime;
	public String rangeName;
	public double reqTimeCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public double getReqTimeCount() {
		return reqTimeCount;
	}
	public void setReqTimeCount(double reqTimeCount) {
		this.reqTimeCount = reqTimeCount;
	}
public EOCNReqTimeRangeData()
{
}
}
