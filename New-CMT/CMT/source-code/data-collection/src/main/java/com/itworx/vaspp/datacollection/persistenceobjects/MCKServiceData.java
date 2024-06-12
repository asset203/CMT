/* 
 * File:       MCKServiceData.java
 * Date        Author          Changes
 * 13/06/2006  Nayera Mohamed  Created
 * 
 * Persistence class for MCK Service Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MCKServiceData extends PersistenceObject {

	public double serviceCount;
	public double serviceClass;

	public MCKServiceData() {
	}


  public void setServiceCount(double serviceCount)
  {
    this.serviceCount = serviceCount;
  }


  public double getServiceCount()
  {
    return serviceCount;
  }


  public void setServiceClass(double serviceClass)
  {
    this.serviceClass = serviceClass;
  }


  public double getServiceClass()
  {
    return serviceClass;
  }
  
  
}