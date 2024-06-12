package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*; 
public class CharIntSqrRespStatusData extends PersistenceObject{
	public Date dateTime;
	public String responseStatus;
	public double respCount;
	public double avgRespStatus;
						
public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public double getRespCount() {
		return respCount;
	}

	public void setRespCount(double respCount) {
		this.respCount = respCount;
	}

	public double getAvgRespStatus() {
		return avgRespStatus;
	}

	public void setAvgRespStatus(double avgRespStatus) {
		this.avgRespStatus = avgRespStatus;
	}

public CharIntSqrRespStatusData()
{
	}
}
