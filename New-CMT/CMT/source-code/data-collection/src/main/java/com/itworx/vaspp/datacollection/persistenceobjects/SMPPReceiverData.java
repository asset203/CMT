package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMPPReceiverData extends PersistenceObject{
	
	public Date dateTime;
	public String application;
	public String shortCode;
	public double msisdnsNumber;
	public double messagesNumber;
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMessagesNumber() {
		return messagesNumber;
	}
	public void setMessagesNumber(double messagesNumber) {
		this.messagesNumber = messagesNumber;
	}
	public double getMsisdnsNumber() {
		return msisdnsNumber;
	}
	public void setMsisdnsNumber(double msisdnsNumber) {
		this.msisdnsNumber = msisdnsNumber;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

}
