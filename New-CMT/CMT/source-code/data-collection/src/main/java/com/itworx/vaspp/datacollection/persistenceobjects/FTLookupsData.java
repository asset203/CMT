package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class FTLookupsData extends PersistenceObject{
	public Date dateTime;
	public double lookupReq;
	public double lookupResp;
	public double maxLookup;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getLookupReq() {
		return lookupReq;
	}
	public void setLookupReq(double lookupReq) {
		this.lookupReq = lookupReq;
	}
	public double getLookupResp() {
		return lookupResp;
	}
	public void setLookupResp(double lookupResp) {
		this.lookupResp = lookupResp;
	}
	public double getMaxLookup() {
		return maxLookup;
	}
	public void setMaxLookup(double maxLookup) {
		this.maxLookup = maxLookup;
	}
public FTLookupsData()
{
}
}
