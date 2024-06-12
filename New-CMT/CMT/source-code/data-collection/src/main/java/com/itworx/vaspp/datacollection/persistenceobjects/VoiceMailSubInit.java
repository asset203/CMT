
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoiceMailSubInit extends PersistenceObject {
	
  public Date date;
  
  public double subCount;
  
  /*
	 * Because My sql returns date in date format only if
	 * the query contains distinct keyword
	 */
	public double temp;
	
  public String component;
  
	public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}

public VoiceMailSubInit() {
	}

public String getComponent() {
	return component;
}

public void setComponent(String component) {
	this.component = component;
}

public double getSubCount() {
	return subCount;
}

public void setSubCount(double subCount) {
	this.subCount = subCount;
}

public double getTemp() {
	return temp;
}

public void setTemp(double temp) {
	this.temp = temp;
}



}