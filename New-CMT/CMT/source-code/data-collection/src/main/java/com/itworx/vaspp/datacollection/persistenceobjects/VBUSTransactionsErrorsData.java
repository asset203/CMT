package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VBUSTransactionsErrorsData extends PersistenceObject{
	
	public Date dateTime;
	public String sdpName;
	public String hlrName;
	public String commandType;
	public String errorDescription;
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
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getHlrName() {
		return hlrName;
	}
	public void setHlrName(String hlrName) {
		this.hlrName = hlrName;
	}
	public String getSdpName() {
		return sdpName;
	}
	public void setSdpName(String sdpName) {
		this.sdpName = sdpName;
	}
	public String getCommandType() {
		return commandType;
	}
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

}
