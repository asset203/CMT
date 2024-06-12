package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMPPMessagesData extends PersistenceObject{
	
	public Date sendTime;
	public Date recieveTime;
	public String appId;
	public String srcNumber;
	public String destNumber;
	public long status;
	public String respCommandStatus;
	public String getRespCommandStatus() {
		return respCommandStatus; 
	}
	public void setRespCommandStatus(String respCommandStatus) {
		this.respCommandStatus = respCommandStatus;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getDestNumber() {
		return destNumber;
	}
	public void setDestNumber(String destNumber) {
		this.destNumber = destNumber;
	}
	public Date getRecieveTime() {
		return recieveTime;
	}
	public void setRecieveTime(Date recieveTime) {
		this.recieveTime = recieveTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getSrcNumber() {
		return srcNumber;
	}
	public void setSrcNumber(String srcNumber) {
		this.srcNumber = srcNumber;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
}
