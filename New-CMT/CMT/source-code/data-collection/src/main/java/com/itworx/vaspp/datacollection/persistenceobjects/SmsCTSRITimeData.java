package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsCTSRITimeData extends PersistenceObject {
	public Date dateTime;
	public String name;
	public String value;
	public String time;
	public String smsNodeName;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSmsNodeName() {
		return smsNodeName;
	}
	public void setSmsNodeName(String smsNodeName) {
		this.smsNodeName = smsNodeName;
	}
public SmsCTSRITimeData
(){}
}
