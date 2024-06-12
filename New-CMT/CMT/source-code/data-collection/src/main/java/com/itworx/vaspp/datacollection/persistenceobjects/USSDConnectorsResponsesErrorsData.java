package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class USSDConnectorsResponsesErrorsData extends PersistenceObject
{
	
	public Date date;
	
	public String connectorName;
	 
	public String errorCode;
	
	public String errorDescription ;
	
	public long errorCount;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}
	
	
	

}
