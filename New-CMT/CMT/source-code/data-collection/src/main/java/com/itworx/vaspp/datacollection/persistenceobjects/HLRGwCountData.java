package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class HLRGwCountData extends PersistenceObject{
	public Date dateTime;
	public String appUser;
	public String transactionType;
	public String hlrNode;
	public String response;
	public double  transactionCount;
	public double getTransactionCount() {
		return transactionCount;
	}
	public void setTransactionCount(double transactionCount) {
		this.transactionCount = transactionCount;
	}
	public HLRGwCountData()
	{}
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getAppUser() {
		return appUser;
	}
	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getHlrNode() {
		return hlrNode;
	}
	public void setHlrNode(String hlrNode) {
		this.hlrNode = hlrNode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	

}
