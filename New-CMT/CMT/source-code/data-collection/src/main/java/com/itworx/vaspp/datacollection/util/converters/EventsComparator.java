package com.itworx.vaspp.datacollection.util.converters;
import java.util.Comparator;

public class EventsComparator implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    return ((SecondEvent)o1).getCount() - ((SecondEvent)o2).getCount();
  }
  public boolean equals(Object obj)
  {
    return false;
  }
}