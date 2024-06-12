package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CCNCashingData extends PersistenceObject {
	public Date dateTime;
	public String ccn;
	public String cashSiza;
	public double cashEntries;
	public double noOfMsisdn;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCcn() {
		return ccn;
	}
	public void setCcn(String ccn) {
		this.ccn = ccn;
	}
	public String getCashSiza() {
		return cashSiza;
	}
	public void setCashSiza(String cashSiza) {
		this.cashSiza = cashSiza;
	}
	public double getCashEntries() {
		return cashEntries;
	}
	public void setCashEntries(double cashEntries) {
		this.cashEntries = cashEntries;
	}
	public double getNoOfMsisdn() {
		return noOfMsisdn;
	}
	public void setNoOfMsisdn(double noOfMsisdn) {
		this.noOfMsisdn = noOfMsisdn;
	}
public CCNCashingData()
{
}
}
