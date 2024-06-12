package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPTransactionsErrorsData extends PersistenceObject{
	public Date dateTime;
	public String userDesc;
	public String transDesc;
	public String errorType;
	public String errorDesc;
	public double errorCount;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(double errorCount) {
		this.errorCount = errorCount;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	
}
