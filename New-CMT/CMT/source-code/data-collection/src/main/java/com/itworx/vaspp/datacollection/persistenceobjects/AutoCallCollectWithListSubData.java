/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;


public class AutoCallCollectWithListSubData extends PersistenceObject {

	public Date dateTime;
	
	public double withListSub;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getWithListSub() {
		return withListSub;
	}

	public void setWithListSub(double withListSub) {
		this.withListSub = withListSub;
	}

	
}
