package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VRSHCallsData extends PersistenceObject {
 public Date dateTime;
 public String serviceType;
 public String serviceKey;
 public double callCount;
 public void setDateTime(Date dateTime)
 {
	 this.dateTime=dateTime;
 }
 public Date getDateTime()
 {
	 return this.dateTime;
	
 }
 public void setServiceType(String serviceType)
 {
	 this.serviceType=serviceType;
 }
 public String getServiceType()
 {return this.serviceType;}
 public void setServiceKey(String serviceKey)
 {
	 this.serviceKey=serviceKey;
 }
 public String getServiceKey()
 {return this.serviceKey;}
 public void setCallCount(double callCount)
 {
	 this.callCount=callCount;
 }
 public double getCallCount()
 {return this.callCount;}
}
