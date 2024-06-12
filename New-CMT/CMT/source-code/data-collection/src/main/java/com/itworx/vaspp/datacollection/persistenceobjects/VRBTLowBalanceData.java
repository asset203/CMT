package com.itworx.vaspp.datacollection.persistenceobjects;
import  java.util.Date;
public class VRBTLowBalanceData extends PersistenceObject{
	public Date dateTime;
	public String channelName;
	public double requests;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public double getRequests() {
		return requests;
	}
	public void setRequests(double requests) {
		this.requests = requests;
	}
	public VRBTLowBalanceData()
	{}
}
