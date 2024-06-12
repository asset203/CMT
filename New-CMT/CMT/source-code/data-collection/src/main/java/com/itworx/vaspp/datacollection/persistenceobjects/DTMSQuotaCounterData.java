package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DTMSQuotaCounterData extends PersistenceObject{
	
	public Date dateTime;
	public String userId;
	public double userMsgQuota;
	public double userSentMsg;
	public double batchMsgQuota;
	public double batchSentMsg;
	
	public double getBatchMsgQuota() {
		return batchMsgQuota;
	}
	public void setBatchMsgQuota(double batchMsgQuota) {
		this.batchMsgQuota = batchMsgQuota;
	}
	public double getBatchSentMsg() {
		return batchSentMsg;
	}
	public void setBatchSentMsg(double batchSentMsg) {
		this.batchSentMsg = batchSentMsg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getUserMsgQuota() {
		return userMsgQuota;
	}
	public void setUserMsgQuota(double userMsgQuota) {
		this.userMsgQuota = userMsgQuota;
	}
	public double getUserSentMsg() {
		return userSentMsg;
	}
	public void setUserSentMsg(double userSentMsg) {
		this.userSentMsg = userSentMsg;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
