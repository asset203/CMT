package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class SDBEventErrorData extends PersistenceObject{
	public SDBEventErrorData()
	{}
	public Date dateTime;
	public String moduleName;
	public String event;
	public String info;
	public double count;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	

	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}

}
