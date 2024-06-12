package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class TopTenProceduresData extends PersistenceObject{
	
	public Date date;
	
	public String objectType;
	
	public String objectName;
	
	public long numberOfExecutions;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public long getNumberOfExecutions() {
		return numberOfExecutions;
	}

	public void setNumberOfExecutions(long numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}


	
}
