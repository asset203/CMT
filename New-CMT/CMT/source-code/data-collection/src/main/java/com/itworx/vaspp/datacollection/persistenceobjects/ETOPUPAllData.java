package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ETOPUPAllData extends PersistenceObject{
	public Date dateTime;
	public String type;
	public String source;
	public String channelType;
	public String status;
	public String errorReason;
	public double count;
	public String nodeName;
	public String senderCat;
	public String area;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getSenderCat() {
		return senderCat;
	}
	public void setSenderCat(String senderCat) {
		this.senderCat = senderCat;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
public ETOPUPAllData()
{
}
}
