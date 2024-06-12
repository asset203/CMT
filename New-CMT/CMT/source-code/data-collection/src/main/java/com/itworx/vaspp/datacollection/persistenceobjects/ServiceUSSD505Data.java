package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ServiceUSSD505Data extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public double reqCount;
	public double succReqCount;
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
	public double getReqCount() {
		return reqCount;
	}
	public void setReqCount(double reqCount) {
		this.reqCount = reqCount;
	}
	public double getSuccReqCount() {
		return succReqCount;
	}
	public void setSuccReqCount(double succReqCount) {
		this.succReqCount = succReqCount;
	}
public ServiceUSSD505Data()
{}
}
