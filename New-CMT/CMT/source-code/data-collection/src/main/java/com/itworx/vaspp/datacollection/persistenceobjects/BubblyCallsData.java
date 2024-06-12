package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class BubblyCallsData extends PersistenceObject{
	public Date dateTime;
	public String dialedNum;
	public String callType;
	public String callDuration;
	public String callCompReason;
	public double serviceName;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDialedNum() {
		return dialedNum;
	}
	public void setDialedNum(String dialedNum) {
		this.dialedNum = dialedNum;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getCallCompReason() {
		return callCompReason;
	}
	public void setCallCompReason(String callCompReason) {
		this.callCompReason = callCompReason;
	}
	public double getServiceName() {
		return serviceName;
	}
	public void setServiceName(double serviceName) {
		this.serviceName = serviceName;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public BubblyCallsData()
{
}
}
