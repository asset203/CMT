package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class PleaseCallTransData extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public double apartyCount;
	public double bpartyCount;
	public double succMsgCount;
    public double getSuccMsgCount() {
		return succMsgCount;
	}
	public void setSuccMsgCount(double succMsgCount) {
		this.succMsgCount = succMsgCount;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public double getApartyCount() {
		return apartyCount;
	}
	public void setApartyCount(double apartyCount) {
		this.apartyCount = apartyCount;
	}
	public double getBpartyCount() {
		return bpartyCount;
	}
	public void setBpartyCount(double bpartyCount) {
		this.bpartyCount = bpartyCount;
	}
public PleaseCallTransData()
{
}
}
