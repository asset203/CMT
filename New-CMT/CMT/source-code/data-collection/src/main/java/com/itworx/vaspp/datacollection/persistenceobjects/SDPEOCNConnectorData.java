package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class SDPEOCNConnectorData  extends PersistenceObject{
	public Date dateTime;
	public double incomingReq;
	public double outgoingReq;
	public double successReq;
	public double failedReq;
	public double maxTransactionsPerSecond;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getIncomingReq() {
		return incomingReq;
	}
	public void setIncomingReq(double incomingReq) {
		this.incomingReq = incomingReq;
	}
	public double getOutgoingReq() {
		return outgoingReq;
	}
	public void setOutgoingReq(double outgoingReq) {
		this.outgoingReq = outgoingReq;
	}
	public double getSuccessReq() {
		return successReq;
	}
	public void setSuccessReq(double successReq) {
		this.successReq = successReq;
	}
	public double getFailedReq() {
		return failedReq;
	}
	public void setFailedReq(double failedReq) {
		this.failedReq = failedReq;
	}
	public double getMaxTransactionsPerSecond() {
		return maxTransactionsPerSecond;
	}
	public void setMaxTransactionsPerSecond(double maxTransactionsPerSecond) {
		this.maxTransactionsPerSecond = maxTransactionsPerSecond;
	}
	public SDPEOCNConnectorData()
	{}
}
