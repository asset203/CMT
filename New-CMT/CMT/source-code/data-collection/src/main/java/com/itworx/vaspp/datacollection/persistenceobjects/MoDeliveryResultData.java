package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MoDeliveryResultData extends PersistenceObject{
	
	public Date dateTime;
	
	public double success;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getSuccess() {
		return success;
	}

	public void setSuccess(double success) {
		this.success = success;
	}

}
