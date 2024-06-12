package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class ChargingInterClientDate extends PersistenceObject{
public double clientID;
public String clientName;
public void setClientID(double clientID)
{
 this.clientID=clientID;	
}
public double getClientID()
{
return this.clientID;	
}
public void setClientName(String clientName)
{
	this.clientName=clientName;
	}
public String  getClientName()
{
return this.clientName;	
}
}
