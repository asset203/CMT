/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;


public class AutoCallCollectDeActiveSubData extends PersistenceObject {

	public Date dateTime;
	
	public double deactiveSub;

	public double getDeactiveSub() {
		return deactiveSub;
	}

	public void setDeactiveSub(double deactiveSub) {
		this.deactiveSub = deactiveSub;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



	
}
