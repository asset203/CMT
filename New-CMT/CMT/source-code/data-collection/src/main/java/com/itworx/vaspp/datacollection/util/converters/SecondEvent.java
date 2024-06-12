package com.itworx.vaspp.datacollection.util.converters;
import java.util.Date;

public class SecondEvent
{
    private int count;
    private Date date;
    
    SecondEvent(int c,Date d)
    {
      count = c;
      date = d;
    }
    
    public int getCount()
    {
      return count;
    }
    
    public Date getDate()
    {
      return date;
    }
    public boolean equals(SecondEvent event)
    {
      return event.getCount()==this.getCount();
    }
}