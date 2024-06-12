package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinAODetailsData extends PersistenceObject{
	public Date dateTime;
	public String header;
	public String subResult;
	public String appName;
	public String termMsisdnRange;
	public String termCountry;
	public String termNw;
	public String delivResult;
	public String prevAttempts;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getSubResult() {
		return subResult;
	}
	public void setSubResult(String subResult) {
		this.subResult = subResult;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTermMsisdnRange() {
		return termMsisdnRange;
	}
	public void setTermMsisdnRange(String termMsisdnRange) {
		this.termMsisdnRange = termMsisdnRange;
	}
	public String getTermCountry() {
		return termCountry;
	}
	public void setTermCountry(String termCountry) {
		this.termCountry = termCountry;
	}
	public String getTermNw() {
		return termNw;
	}
	public void setTermNw(String termNw) {
		this.termNw = termNw;
	}
	public String getDelivResult() {
		return delivResult;
	}
	public void setDelivResult(String delivResult) {
		this.delivResult = delivResult;
	}
	public String getPrevAttempts() {
		return prevAttempts;
	}
	public void setPrevAttempts(String prevAttempts) {
		this.prevAttempts = prevAttempts;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSBinAODetailsData()
{
}
}
