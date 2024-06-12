package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CSAirErrorsData extends PersistenceObject {
	public Date dateTime;
	public double maxNoConnReach;
	public double rejectOverload;
	public double sdpError;
	public double internalError;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getInternalError() {
		return internalError;
	}
	public void setInternalError(double internalError) {
		this.internalError = internalError;
	}
	public double getMaxNoConnReach() {
		return maxNoConnReach;
	}
	public void setMaxNoConnReach(double maxNoConnReach) {
		this.maxNoConnReach = maxNoConnReach;
	}
	public double getRejectOverload() {
		return rejectOverload;
	}
	public void setRejectOverload(double rejectOverload) {
		this.rejectOverload = rejectOverload;
	}
	public double getSdpError() {
		return sdpError;
	}
	public void setSdpError(double sdpError) {
		this.sdpError = sdpError;
	}
}