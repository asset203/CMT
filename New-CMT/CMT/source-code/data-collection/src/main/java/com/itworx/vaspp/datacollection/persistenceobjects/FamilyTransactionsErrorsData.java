package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class FamilyTransactionsErrorsData extends PersistenceObject{
	public Date dateTime;
	public String clientName;
	public String transactionTypeName;
	public String errorCode;
	public double errorCount;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
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
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}
}
