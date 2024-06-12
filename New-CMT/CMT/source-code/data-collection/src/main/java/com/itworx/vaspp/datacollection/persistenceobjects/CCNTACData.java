package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNTACData extends PersistenceObject {

	public Date dateTime;
	public double tac;
	public double dataVolume;
	public double finalCharge;
	public double count;
	public String marketingName;
	public String manufactureName;
	public String tacType;
	
	public CCNTACData(){}

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

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public String getTacType() {
		return tacType;
	}

	public void setTacType(String tacType) {
		this.tacType = tacType;
	}
}
