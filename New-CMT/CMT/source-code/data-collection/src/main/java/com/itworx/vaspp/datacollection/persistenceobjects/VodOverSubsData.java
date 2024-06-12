package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VodOverSubsData extends PersistenceObject {
	public Date dateTime;
	public double subscription;
	public double netSubs;
	public double subs;
	public double unSubs;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getSubscription() {
		return subscription;
	}
	public void setSubscription(double subscription) {
		this.subscription = subscription;
	}
	public double getNetSubs() {
		return netSubs;
	}
	public void setNetSubs(double netSubs) {
		this.netSubs = netSubs;
	}
	public double getSubs() {
		return subs;
	}
	public void setSubs(double subs) {
		this.subs = subs;
	}
	public double getUnSubs() {
		return unSubs;
	}
	public void setUnSubs(double unSubs) {
		this.unSubs = unSubs;
	}
public VodOverSubsData()
{
}
}
