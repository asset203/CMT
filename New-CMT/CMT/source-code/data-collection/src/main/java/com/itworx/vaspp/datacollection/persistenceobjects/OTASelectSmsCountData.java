package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OTASelectSmsCountData  extends PersistenceObject{
    public Date dateTime;
	public double incommSmsCount;
	public double submitSmsCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getIncommSmsCount() {
		return incommSmsCount;
	}
	public void setIncommSmsCount(double incommSmsCount) {
		this.incommSmsCount = incommSmsCount;
	}
	public double getSubmitSmsCount() {
		return submitSmsCount;
	}
	public void setSubmitSmsCount(double submitSmsCount) {
		this.submitSmsCount = submitSmsCount;
	}
public OTASelectSmsCountData()
{}
}
