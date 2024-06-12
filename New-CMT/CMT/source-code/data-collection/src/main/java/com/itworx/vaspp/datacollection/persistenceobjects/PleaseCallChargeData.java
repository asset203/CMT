package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class PleaseCallChargeData extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public double chargedTransCount;
	public double succTransCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getChargedTransCount() {
		return chargedTransCount;
	}
	public void setChargedTransCount(double chargedTransCount) {
		this.chargedTransCount = chargedTransCount;
	}
	public double getSuccTransCount() {
		return succTransCount;
	}
	public void setSuccTransCount(double succTransCount) {
		this.succTransCount = succTransCount;
	}
public PleaseCallChargeData()
{
}
}
