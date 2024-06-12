package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSSRIRActionData extends PersistenceObject{
	public Date dateTime;
	public String sriRouAction;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSriRouAction() {
		return sriRouAction;
	}
	public void setSriRouAction(String sriRouAction) {
		this.sriRouAction = sriRouAction;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSSRIRActionData()
{}
}
