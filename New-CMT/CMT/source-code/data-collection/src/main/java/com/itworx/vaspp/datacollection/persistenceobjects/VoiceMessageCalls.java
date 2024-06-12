
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.math.BigDecimal;
import java.util.Date;

public class VoiceMessageCalls extends PersistenceObject {
	
  public Date time;
  public String Switch;
  public String terminatingNode;
  public double calls;
  public double answered;
  public double congested;
  public double utilization;
  
  


	public VoiceMessageCalls() {
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


public double getAnswered() {
	return answered;
}


public void setAnswered(double answered) {
	this.answered = answered;
}


public double getCalls() {
	return calls;
}


public void setCalls(double calls) {
	this.calls = calls;
}


public double getCongested() {
	return congested;
}


public void setCongested(double congested) {
	this.congested = congested;
}


public double getUtilization() {
	return utilization;
}


public void setUtilization(double utilization) {
	this.utilization = utilization;
}


 


}