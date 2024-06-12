package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class ChargingInterServiceDate extends PersistenceObject {
public double serviceID;
public String serviceName;
public void setServiceID(double serviceID)
{
	this.serviceID=serviceID;
}
public double getServiceID()
{
	return this.serviceID=serviceID;
}
public void setServiceName(String serviceName)
{
	this.serviceName=serviceName;
	}
public String getServiceName()
{
	return this.serviceName=serviceName;
	}
}
