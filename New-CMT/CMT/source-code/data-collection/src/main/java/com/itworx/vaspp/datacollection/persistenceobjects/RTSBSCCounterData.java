package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RTSBSCCounterData extends PersistenceObject{
	public Date dateTime;
	public String counterName;
	public String source;
	public double counterValue;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getCounterValue() {
		return counterValue;
	}
	public void setCounterValue(double counterValue) {
		this.counterValue = counterValue;
	}
public RTSBSCCounterData()
{
}
}
