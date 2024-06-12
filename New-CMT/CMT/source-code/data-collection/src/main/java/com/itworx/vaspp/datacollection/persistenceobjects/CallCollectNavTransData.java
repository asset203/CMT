package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CallCollectNavTransData extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public CallCollectNavTransData()
{
	}
}
