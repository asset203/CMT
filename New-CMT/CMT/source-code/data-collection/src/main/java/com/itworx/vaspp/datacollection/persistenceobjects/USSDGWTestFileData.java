package com.itworx.vaspp.datacollection.persistenceobjects;

public class USSDGWTestFileData extends PersistenceObject
{

	public String dateTime;
	
	public String msisdn;
	
	public String shortCode;

	public String lineType;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	
	
}
