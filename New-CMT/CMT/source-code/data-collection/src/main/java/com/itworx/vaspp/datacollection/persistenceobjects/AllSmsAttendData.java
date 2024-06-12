package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AllSmsAttendData extends PersistenceObject{
	public Date dateTime;					
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public AllSmsAttendData()
{}
}
