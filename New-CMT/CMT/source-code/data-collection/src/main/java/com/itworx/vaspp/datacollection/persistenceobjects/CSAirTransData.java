package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CSAirTransData extends PersistenceObject{
	public Date dateTime;
	public double sumTrans;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getSumTrans() {
		return sumTrans;
	}
	public void setSumTrans(double sumTrans) {
		this.sumTrans = sumTrans;
	}
}
