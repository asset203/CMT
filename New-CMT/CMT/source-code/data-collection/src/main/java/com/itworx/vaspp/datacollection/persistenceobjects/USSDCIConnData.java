package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class USSDCIConnData extends PersistenceObject{
	public Date dateTime;
	public String status;
	public double reqCount;
	public double respCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getReqCount() {
		return reqCount;
	}
	public void setReqCount(double reqCount) {
		this.reqCount = reqCount;
	}
	public double getRespCount() {
		return respCount;
	}
	public void setRespCount(double respCount) {
		this.respCount = respCount;
	}
public USSDCIConnData()
{}
}
