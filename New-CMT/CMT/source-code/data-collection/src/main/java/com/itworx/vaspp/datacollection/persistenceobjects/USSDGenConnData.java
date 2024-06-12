package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class USSDGenConnData extends PersistenceObject{
	public Date dateTime;
	public String shortCode;
	public String status;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public USSDGenConnData()
{
}
}
