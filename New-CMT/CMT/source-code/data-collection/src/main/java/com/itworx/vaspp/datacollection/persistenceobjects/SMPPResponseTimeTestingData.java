package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMPPResponseTimeTestingData extends PersistenceObject{
	
	public Date dateTime;
	public String applicationName;
	public String messageId;
	public String inqueueTime;
	public String dequeueTime;
	public String sendTime;
	public String confirmationTime;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getConfirmationTime() {
		return confirmationTime;
	}
	public void setConfirmationTime(String confirmationTime) {
		this.confirmationTime = confirmationTime;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDequeueTime() {
		return dequeueTime;
	}
	public void setDequeueTime(String dequeueTime) {
		this.dequeueTime = dequeueTime;
	}
	public String getInqueueTime() {
		return inqueueTime;
	}
	public void setInqueueTime(String inqueueTime) {
		this.inqueueTime = inqueueTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	
}
