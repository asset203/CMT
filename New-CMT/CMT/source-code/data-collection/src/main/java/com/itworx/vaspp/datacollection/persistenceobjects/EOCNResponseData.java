package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNResponseData extends PersistenceObject{
	public Date dateTime;
	public String responseCode;
	public String responseDesc;
	public double responseCount;
	public EOCNResponseData()
	{}
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDesc() {
		return responseDesc;
	}
	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}
	public double getResponseCount() {
		return responseCount;
	}
	public void setResponseCount(double responseCount) {
		this.responseCount = responseCount;
	}

}
