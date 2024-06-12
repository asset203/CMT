package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VPNCountersData extends PersistenceObject{
	
	public Date dateTime;
	
	public double counterId;
	
	public double counterValue;

	public double getCounterId() {
		return counterId;
	}

	public void setCounterId(double counterId) {
		this.counterId = counterId;
	}

	public double getCounterValue() {
		return counterValue;
	}

	public void setCounterValue(double counterValue) {
		this.counterValue = counterValue;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
