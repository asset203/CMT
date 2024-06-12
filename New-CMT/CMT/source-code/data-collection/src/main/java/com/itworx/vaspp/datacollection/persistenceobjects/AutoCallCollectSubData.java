/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;


public class AutoCallCollectSubData extends PersistenceObject {

	public Date dateTime;
	
	public double totalSub;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getTotalSub() {
		return totalSub;
	}

	public void setTotalSub(double totalSub) {
		this.totalSub = totalSub;
	}

	
}
