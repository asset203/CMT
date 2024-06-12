package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VNPCountersData  extends PersistenceObject {
	public Date dateTime;
	public String counterName;
	public double counterValue;
	
	
	public void setDateTime (Date dateTime)
	{
		this.dateTime=dateTime;
	}
	public Date getDateTime()
	{
		return this.dateTime;
		}
	public void setCounterName(String counterName)
	{
		this.counterName=counterName;
	}
	public String getCounterName()
	{
		return this.counterName;
	}
	public void setCounterValue(double counterValue)
	{
		this.counterValue=counterValue;
	}
	public double getCounterValue()
	{
		return this.counterValue;
	}
	

}
