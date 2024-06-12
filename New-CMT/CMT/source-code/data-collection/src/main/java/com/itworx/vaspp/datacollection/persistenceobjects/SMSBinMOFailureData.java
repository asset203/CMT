package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinMOFailureData extends PersistenceObject {
	public Date dateTime;
	public String deliveredMsg;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDeliveredMsg() {
		return deliveredMsg;
	}
	public void setDeliveredMsg(String deliveredMsg) {
		this.deliveredMsg = deliveredMsg;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSBinMOFailureData()
{}
}
