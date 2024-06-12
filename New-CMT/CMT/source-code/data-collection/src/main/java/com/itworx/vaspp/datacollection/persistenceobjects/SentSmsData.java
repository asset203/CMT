package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class SentSmsData extends PersistenceObject{
	public Date dateTime;
	public double smsCount;
	public String roamerType;
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

	public String getRoamerType() {
		return roamerType;
	}

	public void setRoamerType(String roamerType) {
		this.roamerType = roamerType;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public SentSmsData()
	{}
}
