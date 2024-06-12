package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ServiceCallCollectData  extends PersistenceObject{
	public Date dateTime;
	public String serviceType;
	public String causeValue;
	public double msisdn;
	public double count;
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getCauseValue() {
		return causeValue;
	}
	public void setCauseValue(String causeValue) {
		this.causeValue = causeValue;
	}
	public double getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(double msisdn) {
		this.msisdn = msisdn;
	}
	public ServiceCallCollectData()
{}
}
