package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RamadanPromoReqData extends PersistenceObject{
	public Date dateTime;
	public double requestType;
	public double requestStatus;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getRequestType() {
		return requestType;
	}
	public void setRequestType(double requestType) {
		this.requestType = requestType;
	}
	public double getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(double requestStatus) {
		this.requestStatus = requestStatus;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public RamadanPromoReqData()
{
}
}
