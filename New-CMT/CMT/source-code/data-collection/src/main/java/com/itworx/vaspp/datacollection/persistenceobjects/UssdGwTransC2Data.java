package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class UssdGwTransC2Data extends PersistenceObject{
public Date date;
	
    public long responseNumberPerShortCode;
	
	public long requestNumberPerShortCode;
	
	public String shortCode;
public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getResponseNumberPerShortCode() {
		return responseNumberPerShortCode;
	}
	public void setResponseNumberPerShortCode(long responseNumberPerShortCode) {
		this.responseNumberPerShortCode = responseNumberPerShortCode;
	}
	public long getRequestNumberPerShortCode() {
		return requestNumberPerShortCode;
	}
	public void setRequestNumberPerShortCode(long requestNumberPerShortCode) {
		this.requestNumberPerShortCode = requestNumberPerShortCode;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
public UssdGwTransC2Data()
{
	}
}
