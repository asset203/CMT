/* 
 * File:       MCKAccessData.java
 * Date        Author          Changes
 * 13/06/2006  Nayera Mohamed  Created
 * 
 * Persistence class for MCK Access Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MCKAccessData extends PersistenceObject {

	public Date lastAccess;
	public double subscribersCount;

	public MCKAccessData() {
	}


  public void setLastAccess(Date lastAccess)
  {
    this.lastAccess = lastAccess;
  }


  public Date getLastAccess()
  {
    return lastAccess;
  }


  public void setSubscribersCount(double subscribersCount)
  {
    this.subscribersCount = subscribersCount;
  }


  public double getSubscribersCount()
  {
    return subscribersCount;
  }


  
}