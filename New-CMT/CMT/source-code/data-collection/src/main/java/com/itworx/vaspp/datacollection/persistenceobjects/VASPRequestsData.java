package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPRequestsData extends PersistenceObject {

	public Date  egyptStandardTime;
	public long  requestsTotal;
	public long  requestsSucceeded;
	public long  requestsQueued;
	
	
	public VASPRequestsData()
	{ 
		
	}

	public Date getEgyptStandardTime() {
		return egyptStandardTime;
	}


	public void setEgyptStandardTime(Date egyptStandardTime) {
		this.egyptStandardTime = egyptStandardTime;
	}


	public long getRequestsQueued() {
		return requestsQueued;
	}


	public void setRequestsQueued(long requestsQueued) {
		this.requestsQueued = requestsQueued;
	}


	public long getRequestsSucceeded() {
		return requestsSucceeded;
	}


	public void setRequestsSucceeded(long requestsSucceeded) {
		this.requestsSucceeded = requestsSucceeded;
	}


	public long getRequestsTotal() {
		return requestsTotal;
	}


	public void setRequestsTotal(long requestsTotal) {
		this.requestsTotal = requestsTotal;
	}
}
