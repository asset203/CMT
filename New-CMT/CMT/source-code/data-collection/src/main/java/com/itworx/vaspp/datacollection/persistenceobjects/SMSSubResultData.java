package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSSubResultData extends PersistenceObject{
	public Date dateTime;
	public String subResultType;					
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSubResultType() {
		return subResultType;
	}
	public void setSubResultType(String subResultType) {
		this.subResultType = subResultType;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSSubResultData(){}
}
