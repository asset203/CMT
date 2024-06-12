package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallBroadcastMsgTransactionData extends PersistenceObject{
	
	public double addresseeNumber;
	
	public Date dateTime;
	
	public String broadcastUser;
	
	public String messageName;
	
	public String messageType;

	public double getAddresseeNumber() {
		return addresseeNumber;
	}

	public void setAddresseeNumber(double addresseeNumber) {
		this.addresseeNumber = addresseeNumber;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getBroadcastUser() {
		return broadcastUser;
	}

	public void setBroadcastUser(String broadcastUser) {
		this.broadcastUser = broadcastUser;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}
