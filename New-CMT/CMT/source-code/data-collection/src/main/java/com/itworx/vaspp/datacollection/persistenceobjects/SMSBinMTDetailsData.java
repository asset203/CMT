package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinMTDetailsData extends PersistenceObject{
	public Date dateTime;
	public String header;
	public String delivResult;
	public String origContry;
	public String origNw;
	public String termMsc;
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
	public String getDelivResult() {
		return delivResult;
	}
	public void setDelivResult(String delivResult) {
		this.delivResult = delivResult;
	}
	public String getOrigContry() {
		return origContry;
	}
	public void setOrigContry(String origContry) {
		this.origContry = origContry;
	}
	public String getOrigNw() {
		return origNw;
	}
	public void setOrigNw(String origNw) {
		this.origNw = origNw;
	}
	public String getTermMsc() {
		return termMsc;
	}
	public void setTermMsc(String termMsc) {
		this.termMsc = termMsc;
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
public SMSBinMTDetailsData()
{
}
}
