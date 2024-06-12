/* 
 * File:       DWH_RBT.java
 * Date        Author          Changes
 * 04/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for DWH RBT Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DWH_RBT extends PersistenceObject {
	
	public double subscriberCount;
  public double increaseCount;

	public DWH_RBT() {
	}


  public void setSubscriberCount(double subscriberCount)
  {
    this.subscriberCount = subscriberCount;
  }


  public double getSubscriberCount()
  {
    return subscriberCount;
  }


  public void setIncreaseCount(double increaseCount)
  {
    this.increaseCount = increaseCount;
  }


  public double getIncreaseCount()
  {
    return increaseCount;
  }

	

}