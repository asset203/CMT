package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNConnectorsData extends PersistenceObject{
	public Date dateTime;
	public String sdpId;
	public double requests;
	public double responses;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	public double getRequests() {
		return requests;
	}
	public void setRequests(double requests) {
		this.requests = requests;
	}
	public double getResponses() {
		return responses;
	}
	public void setResponses(double responses) {
		this.responses = responses;
	}
public EOCNConnectorsData()
{}
}
