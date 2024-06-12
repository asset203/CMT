/* 
 * File:       RBT_Data.java
 * Date        Author          Changes
 * 19/03/2007  Eshraq Essam	   Created
 * 
 * Persistence class for RBT Usage Info
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class RBT_Audio extends PersistenceObject {

	public Date dateTime;

	public long second;

	public long totalCalls;
	
	public RBT_Audio() {
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getSecond() {
		return second;
	}

	public void setSecond(long second) {
		this.second = second;
	}

	public long getTotalCalls() {
		return totalCalls;
	}

	public void setTotalCalls(long totalCalls) {
		this.totalCalls = totalCalls;
	}

	
}