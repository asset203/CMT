package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallCounterData extends PersistenceObject{
	public Date dateTime;
	public  String userAction;
	public  double channel;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getUserAction() {
		return userAction;
	}
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	public double getChannel() {
		return channel;
	}
	public void setChannel(double channel) {
		this.channel = channel;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public MiniCallCounterData()
	{}
	
	
	

}
