package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class DCCCVmsSubsData extends PersistenceObject{
	public Date dateTime;
	public String subType;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public DCCCVmsSubsData()
{}
}
