/* 
 * File:       DWH_Transactions.java
 * Date        Author          Changes
 * 07/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for DWH Transactions Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DWH_Transactions extends PersistenceObject {
	
	public double count;
  public String type;

	public DWH_Transactions() {
	}

  public void setCount(double count)
  {
    this.count = count;
  }


  public double getCount()
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

}