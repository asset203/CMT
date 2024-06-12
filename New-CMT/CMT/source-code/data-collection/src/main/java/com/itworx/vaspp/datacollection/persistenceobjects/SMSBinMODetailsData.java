package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinMODetailsData extends PersistenceObject{
	public Date dateTime;
	public String header;
	public String rejCause;
	public String subResukt;
	public String origMsc;
	public String termCountry;
	public String terNet;
	public String origMsisdnRange;
	public String termMsisdnRange;
	public String delivResult;
	public String prevAttempts;
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
	public String getRejCause() {
		return rejCause;
	}
	public void setRejCause(String rejCause) {
		this.rejCause = rejCause;
	}
	public String getSubResukt() {
		return subResukt;
	}
	public void setSubResukt(String subResukt) {
		this.subResukt = subResukt;
	}
	public String getOrigMsc() {
		return origMsc;
	}
	public void setOrigMsc(String origMsc) {
		this.origMsc = origMsc;
	}
	public String getTermCountry() {
		return termCountry;
	}
	public void setTermCountry(String termCountry) {
		this.termCountry = termCountry;
	}
	public String getTerNet() {
		return terNet;
	}
	public void setTerNet(String terNet) {
		this.terNet = terNet;
	}
	public String getOrigMsisdnRange() {
		return origMsisdnRange;
	}
	public void setOrigMsisdnRange(String origMsisdnRange) {
		this.origMsisdnRange = origMsisdnRange;
	}
	public String getTermMsisdnRange() {
		return termMsisdnRange;
	}
	public void setTermMsisdnRange(String termMsisdnRange) {
		this.termMsisdnRange = termMsisdnRange;
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
	public double count;
public SMSBinMODetailsData()
{
}
}
