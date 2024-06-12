package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsSystemData extends PersistenceObject {
	public Date dateTime;
	public String smsNodeName;
	public String time;
	public String avail;
	public String capacity;
	public String fileSyst;
	public String mounted;
	public String size;
	public String used;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSmsNodeName() {
		return smsNodeName;
	}
	public void setSmsNodeName(String smsNodeName) {
		this.smsNodeName = smsNodeName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAvail() {
		return avail;
	}
	public void setAvail(String avail) {
		this.avail = avail;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getFileSyst() {
		return fileSyst;
	}
	public void setFileSyst(String fileSyst) {
		this.fileSyst = fileSyst;
	}
	public String getMounted() {
		return mounted;
	}
	public void setMounted(String mounted) {
		this.mounted = mounted;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
public SmsSystemData()
{}
}
