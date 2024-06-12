package com.itworx.vaspp.datacollection.util.converters;

import java.util.Comparator;

public class CounterComparator implements Comparator{
	
	public int compare(Object o1, Object o2)
	  {
		Long result = Long.valueOf(((TopCounter)o1).getMaxValue() - ((TopCounter)o2).getMaxValue());		
	    return result.intValue();
	  }
	  public boolean equals(Object obj)
	  {
	    return false;
	  }

}
