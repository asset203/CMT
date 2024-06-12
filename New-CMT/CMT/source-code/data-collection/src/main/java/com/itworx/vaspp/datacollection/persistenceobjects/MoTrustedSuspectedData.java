package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MoTrustedSuspectedData extends PersistenceObject{
	
	public Date dateTime;
	
	public double trusted;
	
	public double suspected;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getSuspected() {
		return suspected;
	}

	public void setSuspected(double suspected) {
		this.suspected = suspected;
	}

	public double getTrusted() {
		return trusted;
	}

	public void setTrusted(double trusted) {
		this.trusted = trusted;
	}

}
