package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;

public class SMSC_Failures extends PersistenceObject
{

  public Date dateTime;
  public long count;
  public String reason;
  
  public SMSC_Failures()
  {
  }

  public void setDateTime(Date dateTime)
  {
    this.dateTime = dateTime;
  }


  public Date getDateTime()
  {
    return dateTime;
  }


  public void setCount(long count)
  {
    this.count = count;
  }


  public long getCount()
  {
    return count;
  }


  public void setReason(String reason)
  {
    this.reason = reason;
  }


  public String getReason()
  {
    return reason;
  }
}