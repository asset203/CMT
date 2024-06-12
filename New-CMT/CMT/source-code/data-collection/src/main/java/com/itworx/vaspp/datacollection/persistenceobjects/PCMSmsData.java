package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class PCMSmsData  extends PersistenceObject{
	public Date dateTime;
	public String textMsg;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTextMsg() {
		return textMsg;
	}
	public void setTextMsg(String textMsg) {
		this.textMsg = textMsg;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public PCMSmsData()
{}
}
