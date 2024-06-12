package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class FamilyTransactionsCounterData extends PersistenceObject{
	public Date dateTime;
	public String clientName;
	public String transactionTypeName;
	public double clientEntryCount;
	public double prepaidSuccessReqs;
	public double postpaidSuccessReqs;
	public double totalSuccessReqs;
	public double failedReqs;
	
	public double getClientEntryCount() {
		return clientEntryCount;
	}
	public void setClientEntryCount(double clientEntryCount) {
		this.clientEntryCount = clientEntryCount;
	}
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
	public double getFailedReqs() {
		return failedReqs;
	}
	public void setFailedReqs(double failedReqs) {
		this.failedReqs = failedReqs;
	}
	public double getPostpaidSuccessReqs() {
		return postpaidSuccessReqs;
	}
	public void setPostpaidSuccessReqs(double postpaidSuccessReqs) {
		this.postpaidSuccessReqs = postpaidSuccessReqs;
	}
	public double getPrepaidSuccessReqs() {
		return prepaidSuccessReqs;
	}
	public void setPrepaidSuccessReqs(double prepaidSuccessReqs) {
		this.prepaidSuccessReqs = prepaidSuccessReqs;
	}
	public double getTotalSuccessReqs() {
		return totalSuccessReqs;
	}
	public void setTotalSuccessReqs(double totalSuccessReqs) {
		this.totalSuccessReqs = totalSuccessReqs;
	}
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}
}
