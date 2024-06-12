package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VRSHFCDData extends PersistenceObject {
public Date dateTime;
public String serviceKey;
public String responseType;
public String callType;
public double noOfCalls;
public double duration;
public void setDateTime(Date dateTime)
    {
	this.dateTime=dateTime;
	}
public Date getDateTime()
{
	return this.dateTime;
}
public void setServiceKey(String serviceKey)
{
	this.serviceKey=serviceKey;
	}
public String getServiceKey()
{
	return this.serviceKey;
	}
public void setResponseType(String responseType)
{
	this.responseType=responseType;
	}
public String getResponseType()
{
	return this.responseType;
	}
public void setCallType(String callType)
{
	this.callType=callType;
}
public String getCallType()
{
	return this.callType;}

public void setNoOfCalls(double noOfCalls)
{
	this.noOfCalls=noOfCalls;}
public double getNoOfCalls()
{
	return this.noOfCalls;}

public void setDuration(double duration)
{
	this.duration=duration;
	}
public double getDuration()
{
	return this.duration;}
}
