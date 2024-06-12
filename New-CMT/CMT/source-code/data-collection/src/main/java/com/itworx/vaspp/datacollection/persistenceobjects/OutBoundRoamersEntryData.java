package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class OutBoundRoamersEntryData  extends PersistenceObject{
	public Date dateTime;
	public double roamerCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getRoamerCount() {
		return roamerCount;
	}
	public void setRoamerCount(double roamerCount) {
		this.roamerCount = roamerCount;
	}
public OutBoundRoamersEntryData()
{}

}
