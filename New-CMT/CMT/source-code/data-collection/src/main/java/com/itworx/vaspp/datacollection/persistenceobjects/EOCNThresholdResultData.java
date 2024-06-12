package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNThresholdResultData extends PersistenceObject {
	public Date dateTime;
	public double noOfSecThreshold;
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getNoOfSecThreshold() {
		return noOfSecThreshold;
	}
	public void setNoOfSecThreshold(double noOfSecThreshold) {
		this.noOfSecThreshold = noOfSecThreshold;
	}
	public EOCNThresholdResultData()
	{}
}
