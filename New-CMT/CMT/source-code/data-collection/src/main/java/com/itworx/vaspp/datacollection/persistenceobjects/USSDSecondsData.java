package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class USSDSecondsData extends PersistenceObject {
	public Date date;
	public long count;
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
