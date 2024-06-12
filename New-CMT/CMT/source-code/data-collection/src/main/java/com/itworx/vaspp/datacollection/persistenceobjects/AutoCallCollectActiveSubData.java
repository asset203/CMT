/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;


public class AutoCallCollectActiveSubData extends PersistenceObject {

	public Date dateTime;
	
	public double activeSub;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getActiveSub() {
		return activeSub;
	}

	public void setActiveSub(double activeSub) {
		this.activeSub = activeSub;
	}


	
}
