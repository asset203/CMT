package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class IVRCalls868Data extends PersistenceObject{
	public Date dateTime;
	public String transType;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public IVRCalls868Data()
		{
		}
}
