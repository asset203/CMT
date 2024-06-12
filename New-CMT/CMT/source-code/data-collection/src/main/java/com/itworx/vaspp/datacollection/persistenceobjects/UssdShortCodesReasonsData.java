package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class UssdShortCodesReasonsData  extends PersistenceObject{
	
	public Date dateTime;
	
	public String responseReason;
	
	public double noOfReasons;
	
	public String shortCode;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getNoOfReasons() {
		return noOfReasons;
	}

	public void setNoOfReasons(double noOfReasons) {
		this.noOfReasons = noOfReasons;
	}

	public String getResponseReason() {
		return responseReason;
	}

	public void setResponseReason(String responseReason) {
		this.responseReason = responseReason;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

}
