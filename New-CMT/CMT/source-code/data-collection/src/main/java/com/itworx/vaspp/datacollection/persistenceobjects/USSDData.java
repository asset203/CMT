/* 
 * File:       VMSData.java
 * Date        Author          Changes
 * 01/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for VMS Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class USSDData extends PersistenceObject {

	public Date date;

	public long mobinilRequests;

	public long vodafoneRequests;
	
	public long etisalatRequests;

	public long successfullRequests;
  
	public long unsuccessfullRequests;
  
	public long totalRequests;

	public USSDData() {
	}

	
	

  public void setDate(Date date)
  {
    this.date = date;
  }


  public Date getDate()
  {
    return date;
  }


  public void setMobinilRequests(long mobinilRequests)
  {
    this.mobinilRequests = mobinilRequests;
  }


  public long getMobinilRequests()
  {
    return mobinilRequests;
  }


  public void setVodafoneRequests(long vodafoneRequests)
  {
    this.vodafoneRequests = vodafoneRequests;
  }


  public long getVodafoneRequests()
  {
    return vodafoneRequests;
  }


  public void setSuccessfullRequests(long successfullRequests)
  {
    this.successfullRequests = successfullRequests;
  }


  public long getSuccessfullRequests()
  {
    return successfullRequests;
  }


  public void setUnsuccessfullRequests(long unsuccessfullRequests)
  {
    this.unsuccessfullRequests = unsuccessfullRequests;
  }


  public long getUnsuccessfullRequests()
  {
    return unsuccessfullRequests;
  }


  public void setTotalRequests(long totalRequests)
  {
    this.totalRequests = totalRequests;
  }


  public long getTotalRequests()
  {
    return totalRequests;
  }




public long getEtisalatRequests() {
	return etisalatRequests;
}




public void setEtisalatRequests(long etisalatRequests) {
	this.etisalatRequests = etisalatRequests;
}



}