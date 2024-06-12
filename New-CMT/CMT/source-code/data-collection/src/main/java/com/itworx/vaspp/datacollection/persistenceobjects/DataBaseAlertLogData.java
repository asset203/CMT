package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DataBaseAlertLogData extends PersistenceObject{
	
	public Date dateTime;
	
	public String oraCode;
	
	public String oraDescription;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getOraCode() {
		return oraCode;
	}

	public void setOraCode(String oraCode) {
		this.oraCode = oraCode;
	}

	public String getOraDescription() {
		return oraDescription;
	}

	public void setOraDescription(String oraDescription) {
		this.oraDescription = oraDescription;
	}
	
	


}
