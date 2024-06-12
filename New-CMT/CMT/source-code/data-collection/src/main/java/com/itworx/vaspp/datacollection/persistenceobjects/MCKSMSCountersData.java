package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MCKSMSCountersData extends PersistenceObject{
	public Date dateTime;
	public String sourceAdd;
	public String msgType;
	public String msgInfo;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSourceAdd() {
		return sourceAdd;
	}
	public void setSourceAdd(String sourceAdd) {
		this.sourceAdd = sourceAdd;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgInfo() {
		return msgInfo;
	}
	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public MCKSMSCountersData()
{
	
}
}
