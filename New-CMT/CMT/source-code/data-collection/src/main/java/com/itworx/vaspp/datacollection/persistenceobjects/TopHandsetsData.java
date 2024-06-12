package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class TopHandsetsData extends PersistenceObject{
	
	public Date dateTime;
	
	public String handsetType;
	
	public long numberOfHandsets;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getHandsetType() {
		return handsetType;
	}

	public void setHandsetType(String handsetType) {
		this.handsetType = handsetType;
	}

	public long getNumberOfHandsets() {
		return numberOfHandsets;
	}

	public void setNumberOfHandsets(long numberOfHandsets) {
		this.numberOfHandsets = numberOfHandsets;
	}

	

}
