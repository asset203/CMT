package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EmptyblackListCount extends PersistenceObject{
	public Date dateTime;
	public double count;
	public EmptyblackListCount()
	{}
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
