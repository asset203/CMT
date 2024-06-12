package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;
public class USSDConnectorsTransactionsData extends PersistenceObject{
	
public Date date;
	
	public long responseNumber;
	
	public long requestNumber;
	
	public String connectorName;
	 
	public long succeedResponses;
	
	public long otherResponses;
	
	


	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public long getSucceedResponses() {
		return succeedResponses;
	}

	public void setSucceedResponses(long succeedResponses) {
		this.succeedResponses = succeedResponses;
	}

	public long getOtherResponses() {
		return otherResponses;
	}

	public void setOtherResponses(long otherResponses) {
		this.otherResponses = otherResponses;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getResponseNumber() {
		return responseNumber;
	}

	public void setResponseNumber(long responseNumber) {
		this.responseNumber = responseNumber;
	}

	public long getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(long requestNumber) {
		this.requestNumber = requestNumber;
	}




}
