package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VMSCallsData extends PersistenceObject{
	public Date dateTime;
	public String cos;
	public String serviceType;
	public String opType;
	public String callReason;
	public String greetingType;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCos() {
		return cos;
	}
	public void setCos(String cos) {
		this.cos = cos;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getCallReason() {
		return callReason;
	}
	public void setCallReason(String callReason) {
		this.callReason = callReason;
	}
	public String getGreetingType() {
		return greetingType;
	}
	public void setGreetingType(String greetingType) {
		this.greetingType = greetingType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public VMSCallsData()
{}
}
