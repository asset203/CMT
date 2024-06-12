package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSLgpStatsData extends PersistenceObject{
	public Date dateTime;
	public String header;
	public String rejCause;
	public String subResult;
	public String origCountry;
	public String origNet;
	public String appName;
	public String appSC;
	public String termCountry;
	public String termNw;
	public String sriAction;
	public String sriQueryResult;
	public String deliveryResult;
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
	public String getRejCause() {
		return rejCause;
	}
	public void setRejCause(String rejCause) {
		this.rejCause = rejCause;
	}
	public String getSubResult() {
		return subResult;
	}
	public void setSubResult(String subResult) {
		this.subResult = subResult;
	}
	public String getOrigCountry() {
		return origCountry;
	}
	public void setOrigCountry(String origCountry) {
		this.origCountry = origCountry;
	}
	public String getOrigNet() {
		return origNet;
	}
	public void setOrigNet(String origNet) {
		this.origNet = origNet;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppSC() {
		return appSC;
	}
	public void setAppSC(String appSC) {
		this.appSC = appSC;
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
	public String getSriAction() {
		return sriAction;
	}
	public void setSriAction(String sriAction) {
		this.sriAction = sriAction;
	}
	public String getSriQueryResult() {
		return sriQueryResult;
	}
	public void setSriQueryResult(String sriQueryResult) {
		this.sriQueryResult = sriQueryResult;
	}
	public String getDeliveryResult() {
		return deliveryResult;
	}
	public void setDeliveryResult(String deliveryResult) {
		this.deliveryResult = deliveryResult;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSLgpStatsData()
{
}
}
