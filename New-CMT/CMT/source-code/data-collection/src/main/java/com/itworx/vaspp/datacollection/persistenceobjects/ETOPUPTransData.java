package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ETOPUPTransData extends PersistenceObject{
	public Date dateTime;
	public String errorReason;
	public String status;
	public String userId;
	public String senderCat;
	public String source;
	public String channelType;
	public String type;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSenderCat() {
		return senderCat;
	}
	public void setSenderCat(String senderCat) {
		this.senderCat = senderCat;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
public ETOPUPTransData()
{}
}
