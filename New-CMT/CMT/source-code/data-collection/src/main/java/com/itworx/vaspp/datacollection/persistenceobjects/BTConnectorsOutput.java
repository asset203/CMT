package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BTConnectorsOutput  extends PersistenceObject{
	
	public Date dateTime;
	public String responseStatus;
	public long requestCount;
	public long responseCount;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public long getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}
	public long getResponseCount() {
		return responseCount;
	}
	public void setResponseCount(long responseCount) {
		this.responseCount = responseCount;
	}
}
