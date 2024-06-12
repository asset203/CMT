/* 
 * File:       IVRForecast.java
 * Date        Author          Changes
 * 20/02/2006  Nayera Mohamed  Created
 * 
 * Persistence class for IVR Forecast Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class IVRForecast extends PersistenceObject {
	public long id;

	public Date month;

	public double no_of_subscribers;

	public IVRForecast() {
	}

	public void setNo_of_subscribers(double no_of_subscribers) {
		this.no_of_subscribers = no_of_subscribers;
	}

	public double getNo_of_subscribers() {
		return no_of_subscribers;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Date getMonth() {
		return month;
	}
}