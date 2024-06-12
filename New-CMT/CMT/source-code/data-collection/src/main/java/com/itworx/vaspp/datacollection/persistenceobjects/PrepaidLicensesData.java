package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class PrepaidLicensesData extends PersistenceObject{
	
	public Date entryDay;
	
	public long maxConcurrent;
	
	public long unreservedTokensInUse;
	
	public String hostName;
	
	public long numberOfTokensUsed;

	public Date getEntryDay() {
		return entryDay;
	}

	public void setEntryDay(Date entryDay) {
		this.entryDay = entryDay;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public long getMaxConcurrent() {
		return maxConcurrent;
	}

	public void setMaxConcurrent(long maxConcurrent) {
		this.maxConcurrent = maxConcurrent;
	}

	public long getNumberOfTokensUsed() {
		return numberOfTokensUsed;
	}

	public void setNumberOfTokensUsed(long numberOfTokensUsed) {
		this.numberOfTokensUsed = numberOfTokensUsed;
	}

	public long getUnreservedTokensInUse() {
		return unreservedTokensInUse;
	}

	public void setUnreservedTokensInUse(long unreservedTokensInUse) {
		this.unreservedTokensInUse = unreservedTokensInUse;
	}
	

}
