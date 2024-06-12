package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;

public class SMSC_Types extends PersistenceObject
{

  public Date day;
  public long count;
  public String type;
  
  public SMSC_Types()
  {
  }


  public void setCount(long count)
  {
    this.count = count;
  }


  public long getCount()
  {
    return count;
  }


  public void setType(String type)
  {
    this.type = type;
  }


  public String getType()
  {
    return type;
  }


  public void setDay(Date day)
  {
    this.day = day;
  }


  public Date getDay()
  {
    return day;
  }


  
}