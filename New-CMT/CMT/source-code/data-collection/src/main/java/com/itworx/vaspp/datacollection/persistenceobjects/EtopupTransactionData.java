package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class EtopupTransactionData extends PersistenceObject{
	
	public Date dateTime;
	
	public double count;
	
	public String channelType;
	
	public String transactionType;
	
	public String senderCategory;
	
	public String source;
	
	public String status;
	
	public String errorReason;
	
	public String area;
    public double maxTps; 
	public double getMaxTps() {
		return maxTps;
	}

	public void setMaxTps(double maxTps) {
		this.maxTps = maxTps;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getSenderCategory() {
		return senderCategory;
	}

	public void setSenderCategory(String senderCategory) {
		this.senderCategory = senderCategory;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
		
	
}
