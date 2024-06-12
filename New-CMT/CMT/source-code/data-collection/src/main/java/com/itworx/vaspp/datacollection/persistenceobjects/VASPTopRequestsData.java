package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPTopRequestsData extends PersistenceObject {

	public Date  egyptStandardTime;
	public long  requestsExecuting;
	public long  requestsQueued;
	public long  waitingTime;
	public long  executingTime;
	
	public VASPTopRequestsData()
	{ 
		
	}

	public Date getEgyptStandardTime() {
		return egyptStandardTime;
	}


	public void setEgyptStandardTime(Date egyptStandardTime) {
		this.egyptStandardTime = egyptStandardTime;
	}


	public long getExecutingTime() {
		return executingTime;
	}


	public void setExecutingTime(long executingTime) {
		this.executingTime = executingTime;
	}


	public long getRequestsExecuting() {
		return requestsExecuting;
	}


	public void setRequestsExecuting(long requestsExecuting) {
		this.requestsExecuting = requestsExecuting;
	}


	public long getRequestsQueued() {
		return requestsQueued;
	}


	public void setRequestsQueued(long requestsQueued) {
		this.requestsQueued = requestsQueued;
	}


	public long getWaitingTime() {
		return waitingTime;
	}


	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}


}
