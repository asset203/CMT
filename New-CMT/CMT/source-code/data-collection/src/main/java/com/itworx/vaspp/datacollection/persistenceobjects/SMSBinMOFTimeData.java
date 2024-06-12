package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinMOFTimeData extends PersistenceObject{
	public Date dateTime;
	public String origMSC;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getOrigMSC() {
		return origMSC;
	}
	public void setOrigMSC(String origMSC) {
		this.origMSC = origMSC;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public SMSBinMOFTimeData()
	{}
}
