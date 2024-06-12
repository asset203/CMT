package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class KPIDailyCountersData  extends PersistenceObject{
	public Date dateTime;
	public String counterName;
	public double counterValue;
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public double getCounterValue() {
		return counterValue;
	}
	public void setCounterValue(double counterValue) {
		this.counterValue = counterValue;
	}
public KPIDailyCountersData()
{}
}
