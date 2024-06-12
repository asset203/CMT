package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class HLRGateWayData extends PersistenceObject{
	public Date dateTime;
	public String transactionType;
	public String hlrFlag;
	public String appUser;
	public String response;
	public String extResponse;
	public String getExtResponse() {
		return extResponse;
	}
	public void setExtResponse(String extResponse) {
		this.extResponse = extResponse;
	}
	public HLRGateWayData()
	{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getHlrFlag() {
		return hlrFlag;
	}
	public void setHlrFlag(String hlrFlag) {
		this.hlrFlag = hlrFlag;
	}
	public String getAppUser() {
		return appUser;
	}
	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	

}
