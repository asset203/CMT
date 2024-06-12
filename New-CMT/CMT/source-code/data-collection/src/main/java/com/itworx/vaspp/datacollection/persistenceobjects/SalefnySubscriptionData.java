package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SalefnySubscriptionData extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public double requests;
	public double tier1Count;
	public double tiere2Count;
	public double tiere3Count;
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
	public double getRequests() {
		return requests;
	}
	public void setRequests(double requests) {
		this.requests = requests;
	}
	public double getTier1Count() {
		return tier1Count;
	}
	public void setTier1Count(double tier1Count) {
		this.tier1Count = tier1Count;
	}
	public double getTiere2Count() {
		return tiere2Count;
	}
	public void setTiere2Count(double tiere2Count) {
		this.tiere2Count = tiere2Count;
	}
	public double getTiere3Count() {
		return tiere3Count;
	}
	public void setTiere3Count(double tiere3Count) {
		this.tiere3Count = tiere3Count;
	}
public SalefnySubscriptionData()
{
}
}
