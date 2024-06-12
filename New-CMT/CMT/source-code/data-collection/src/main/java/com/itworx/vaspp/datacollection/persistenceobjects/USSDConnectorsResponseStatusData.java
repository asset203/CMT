package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class USSDConnectorsResponseStatusData extends PersistenceObject{

	public Date date;
	
	public String connectorName;
	 
	public String responseStatus;
	
	public long statusCount;

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

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public long getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(long statusCount) {
		this.statusCount = statusCount;
	}
	
	
	
}
