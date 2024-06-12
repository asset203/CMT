package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPRequestsUpdatesData extends PersistenceObject {
	
	public Date dateTime;
	
	public double transactionId;
	
	public double successNumber;
	
	public double failureNumber;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getFailureNumber() {
		return failureNumber;
	}

	public void setFailureNumber(double failureNumber) {
		this.failureNumber = failureNumber;
	}

	public double getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(double successNumber) {
		this.successNumber = successNumber;
	}

	public double getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(double transactionId) {
		this.transactionId = transactionId;
	}

}
