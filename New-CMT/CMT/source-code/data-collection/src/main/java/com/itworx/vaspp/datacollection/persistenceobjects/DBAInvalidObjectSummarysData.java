package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DBAInvalidObjectSummarysData extends PersistenceObject{
	
	public Date dateTime;
	
	public String owner;
	
	public String objectType;
	
	public long count;
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


}
