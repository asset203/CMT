package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ServiceUssdRxData extends PersistenceObject{
	public Date dateTime;	
	public String msisdn;
	public String status;
	public double  transCount;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTransCount() {
		return transCount;
	}
	public void setTransCount(double transCount) {
		this.transCount = transCount;
	}
public ServiceUssdRxData()
{}
}
