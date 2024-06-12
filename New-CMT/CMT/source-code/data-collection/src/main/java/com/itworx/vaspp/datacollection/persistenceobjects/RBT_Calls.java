/* 
 * File:       RBT_Calls.java
 * Date        Author          Changes
 * 04/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for RBT Calls Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class RBT_Calls extends PersistenceObject {
	
  public Date time;
  public String Switch;
  public String terminatingNode;
  public String route;
  public double devices;
  public double blocked;
  public double OGTraffic;
  public double ICTraffic;
  public double OGCalls;
  public double ICCalls;
  public double OGAnswered;
  public double ICAnswered;
  public double utilization;   

	public RBT_Calls() {
	}

  public void setTime(Date time)
  {
    this.time = time;
  }


  public Date getTime()
  {
    return time;
  }


  public void setSwitch(String Switch)
  {
    this.Switch = Switch;
  }


  public String getSwitch()
  {
    return Switch;
  }


  public void setTerminatingNode(String terminatingNode)
  {
    this.terminatingNode = terminatingNode;
  }


  public String getTerminatingNode()
  {
    return terminatingNode;
  }


  public void setRoute(String route)
  {
    this.route = route;
  }


  public String getRoute()
  {
    return route;
  }


  public void setDevices(double devices)
  {
    this.devices = devices;
  }


  public double getDevices()
  {
    return devices;
  }


  public void setBlocked(double blocked)
  {
    this.blocked = blocked;
  }


  public double getBlocked()
  {
    return blocked;
  }


  public void setOGTraffic(double OGTraffic)
  {
    this.OGTraffic = OGTraffic;
  }


  public double getOGTraffic()
  {
    return OGTraffic;
  }


  public void setICTraffic(double ICTraffic)
  {
    this.ICTraffic = ICTraffic;
  }


  public double getICTraffic()
  {
    return ICTraffic;
  }


  public void setOGCalls(double OGCalls)
  {
    this.OGCalls = OGCalls;
  }


  public double getOGCalls()
  {
    return OGCalls;
  }


  public void setICCalls(double ICCalls)
  {
    this.ICCalls = ICCalls;
  }


  public double getICCalls()
  {
    return ICCalls;
  }


  public void setOGAnswered(double OGAnswered)
  {
    this.OGAnswered = OGAnswered;
  }


  public double getOGAnswered()
  {
    return OGAnswered;
  }


  public void setICAnswered(double ICAnswered)
  {
    this.ICAnswered = ICAnswered;
  }


  public double getICAnswered()
  {
    return ICAnswered;
  }


  public void setUtilization(double utilization)
  {
    this.utilization = utilization;
  }


  public double getUtilization()
  {
    return utilization;
  }


 


}