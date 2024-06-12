package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CallFilterationCallsData  extends PersistenceObject{
	public CallFilterationCallsData()
	{}
	public Date dateTime;
	public long bNumberCount;
	public long totalCalls;
	public long chargedAnnCount;
	public long nonChargedAccount;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getbNumberCount() {
		return bNumberCount;
	}
	public void setbNumberCount(long bNumberCount) {
		this.bNumberCount = bNumberCount;
	}
	public long getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(long totalCalls) {
		this.totalCalls = totalCalls;
	}
	public long getChargedAnnCount() {
		return chargedAnnCount;
	}
	public void setChargedAnnCount(long chargedAnnCount) {
		this.chargedAnnCount = chargedAnnCount;
	}
	public long getNonChargedAccount() {
		return nonChargedAccount;
	}
	public void setNonChargedAccount(long nonChargedAccount) {
		this.nonChargedAccount = nonChargedAccount;
	}

}
