package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class USSDNetworkInitiatedData   extends PersistenceObject{
	public Date dateTime;
	public long requests;
	
	public void setDateTime(Date dateTime)
	{
		this.dateTime=dateTime;
	}
	public Date getdateTime()
	{
		return this.dateTime;
	}
	public void setRequests(long requests)
	{
		this.requests=requests;
	}
	public long getRequests()
	{
		return this.requests;
	}
	
	
}
