package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VRBTSongSelectionData  extends PersistenceObject{
	public Date dateTime;
	public String channelName;
	public double request;
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
	public double getRequest() {
		return request;
	}
	public void setRequest(double request) {
		this.request = request;
	}
public VRBTSongSelectionData()
	{}
}
