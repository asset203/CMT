package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class EOCNBrowsersResponsesData  extends PersistenceObject{
	public Date dateTime;
	public String errorCode;
	public double errorCount;
	
	public EOCNBrowsersResponsesData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public double getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(double errorCount) {
		this.errorCount = errorCount;
	}
	
	
}
