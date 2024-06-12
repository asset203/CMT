package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSDeliveryResultData extends PersistenceObject{
	public Date dateTime;
	public String deliveryResult;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDeliveryResult() {
		return deliveryResult;
	}
	public void setDeliveryResult(String deliveryResult) {
		this.deliveryResult = deliveryResult;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSDeliveryResultData()
{
}
}
