package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MCKCallData extends PersistenceObject{
public Date dateTime;
	public double transType;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTransType() {
		return transType;
	}
	public void setTransType(double transType) {
		this.transType = transType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public MCKCallData(){}
}
