package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNTEMPData extends PersistenceObject {

	public Date dateTime;
	public double tac;
	public double dataVolume;
	public double finalCharge;
	public double count ;
	
	public CCNTEMPData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getTac() {
		return tac;
	}

	public void setTac(double tac) {
		this.tac = tac;
	}

	public double getDataVolume() {
		return dataVolume;
	}

	public void setDataVolume(double dataVolume) {
		this.dataVolume = dataVolume;
	}

	public double getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(double finalCharge) {
		this.finalCharge = finalCharge;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	
	
}
