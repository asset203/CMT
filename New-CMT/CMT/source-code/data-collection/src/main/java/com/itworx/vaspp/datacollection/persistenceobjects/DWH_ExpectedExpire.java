/* 
 * File:       DWH_ExpectedExpire.java
 * Date        Author          Changes
 * 04/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for DWH Expected to Expire Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DWH_ExpectedExpire extends PersistenceObject {
	
	public double expectedCount;

	public DWH_ExpectedExpire() {
	}

  public void setExpectedCount(double expectedCount)
  {
    this.expectedCount = expectedCount;
  }


  public double getExpectedCount()
  {
    return expectedCount;
  }

}