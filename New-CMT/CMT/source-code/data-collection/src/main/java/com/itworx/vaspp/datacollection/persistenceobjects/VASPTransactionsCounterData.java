package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPTransactionsCounterData extends PersistenceObject{
	
	public Date dateTime;
	public String userDesc;
	public String transDesc;
	public double totalTrans;
	public double totalSuccTrans;
	public double totalFailTrans;
	public double totalFailUserTrans;
	public double totalFailSystemTrans;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTotalFailSystemTrans() {
		return totalFailSystemTrans;
	}
	public void setTotalFailSystemTrans(double totalFailSystemTrans) {
		this.totalFailSystemTrans = totalFailSystemTrans;
	}
	public double getTotalFailTrans() {
		return totalFailTrans;
	}
	public void setTotalFailTrans(double totalFailTrans) {
		this.totalFailTrans = totalFailTrans;
	}
	public double getTotalFailUserTrans() {
		return totalFailUserTrans;
	}
	public void setTotalFailUserTrans(double totalFailUserTrans) {
		this.totalFailUserTrans = totalFailUserTrans;
	}
	public double getTotalSuccTrans() {
		return totalSuccTrans;
	}
	public void setTotalSuccTrans(double totalSuccTrans) {
		this.totalSuccTrans = totalSuccTrans;
	}
	public double getTotalTrans() {
		return totalTrans;
	}
	public void setTotalTrans(double totalTrans) {
		this.totalTrans = totalTrans;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	
	
	
}
