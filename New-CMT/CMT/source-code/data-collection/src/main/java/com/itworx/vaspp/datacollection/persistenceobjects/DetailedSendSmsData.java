package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class DetailedSendSmsData extends PersistenceObject {

	
	public Date dateTime;
	public double smsCount;
	public String roameryType;
	public double smsID;
	public String smsName;
	public String requestStatus;
	
	
	public Date getDateTime() {
		return dateTime;
	}


	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


	public double getSmsCount() {
		return smsCount;
	}


	public void setSmsCount(double smsCount) {
		this.smsCount = smsCount;
	}


	public String getRoameryType() {
		return roameryType;
	}


	public void setRoameryType(String roameryType) {
		this.roameryType = roameryType;
	}


	public double getSmsID() {
		return smsID;
	}


	public void setSmsID(double smsID) {
		this.smsID = smsID;
	}


	public String getSmsName() {
		return smsName;
	}


	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}


	public String getRequestStatus() {
		return requestStatus;
	}


	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}


	public DetailedSendSmsData()
	{}
	


	
}
