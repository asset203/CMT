package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CallFilterationSubsData  extends PersistenceObject{
	public Date dateTime;
	public String serviceName;
	public long totalSubs;
	public long whiteListSubs;
	public long blackListSubs;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public long getTotalSubs() {
		return totalSubs;
	}
	public void setTotalSubs(long totalSubs) {
		this.totalSubs = totalSubs;
	}
	public long getWhiteListSubs() {
		return whiteListSubs;
	}
	public void setWhiteListSubs(long whiteListSubs) {
		this.whiteListSubs = whiteListSubs;
	}
	public long getBlackListSubs() {
		return blackListSubs;
	}
	public void setBlackListSubs(long blackListSubs) {
		this.blackListSubs = blackListSubs;
	}
public CallFilterationSubsData()
{}
}
