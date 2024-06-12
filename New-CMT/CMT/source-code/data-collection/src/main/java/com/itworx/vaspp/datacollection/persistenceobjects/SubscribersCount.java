package com.itworx.vaspp.datacollection.persistenceobjects;

/* 
 * File:       SCP_Signal.java
 * Date        Author          Changes
 * 11/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for HLR Signals Data
 */



import java.util.Date;

public class SubscribersCount extends PersistenceObject {
	public double count;
    public Date dateTime;
	public void setCount(double count)
	{
		this.count=count;
	}
	public double getCount()
	{
		return this.count;
	}
	public void setDateTime(Date dateTime)
	{
		this.dateTime=dateTime;
	}
	public Date getDateTime()
	{
		return this.dateTime;
	}
}