package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MCKInfoData extends PersistenceObject {
	public Date dateTime;
	public String levelId;
	public String misscallNotiMask;
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
	public String getMisscallNotiMask() {
		return misscallNotiMask;
	}
	public void setMisscallNotiMask(String misscallNotiMask) {
		this.misscallNotiMask = misscallNotiMask;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public MCKInfoData()
{}
}
