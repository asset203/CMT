package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class OMNTransactionsErrorsData extends PersistenceObject{
	
	public Date dateTime;
	public String operationType;
	public double errorType;
	public double errorValue;
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
	public double getErrorType() {
		return errorType;
	}
	public void setErrorType(double errorType) {
		this.errorType = errorType;
	}
	public double getErrorValue() {
		return errorValue;
	}
	public void setErrorValue(double errorValue) {
		this.errorValue = errorValue;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
}
