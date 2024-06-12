package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VMSInfoData extends PersistenceObject{
	public Date dateTime;
	public String levelId;
	public String missCallNotiMask;
	public double status;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getMissCallNotiMask() {
		return missCallNotiMask;
	}
	public void setMissCallNotiMask(String missCallNotiMask) {
		this.missCallNotiMask = missCallNotiMask;
	}
	public double getStatus() {
		return status;
	}
	public void setStatus(double status) {
		this.status = status;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public VMSInfoData()
{}
}
