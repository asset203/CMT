package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoiceMailSubLogin extends PersistenceObject {

	public Date date;

	public double noOfLogons;

	public double noOfLoginSub;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getNoOfLoginSub() {
		return noOfLoginSub;
	}

	public void setNoOfLoginSub(double noOfLoginSub) {
		this.noOfLoginSub = noOfLoginSub;
	}

	public double getNoOfLogons() {
		return noOfLogons;
	}

	public void setNoOfLogons(double noOfLogons) {
		this.noOfLogons = noOfLogons;
	}

	public VoiceMailSubLogin() {
	}

}