package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoiceMailDeposit extends PersistenceObject {

	public Date date;

	public double msgDeposit;
	
	/*
	 * Because My sql returns date in date format only if
	 * the query contains distinct keyword
	 */
	public double temp;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

	public double getMsgDeposit() {
		return msgDeposit;
	}

	public void setMsgDeposit(double msgDeposit) {
		this.msgDeposit = msgDeposit;
	}

	public VoiceMailDeposit() {
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

}