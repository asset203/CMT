package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ECNSubscribersData extends PersistenceObject{
    public Date dateTime;
	public double noOfSub;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getNoOfSub() {
		return noOfSub;
	}
	public void setNoOfSub(double noOfSub) {
		this.noOfSub = noOfSub;
	}
public ECNSubscribersData()
{}
}
