/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author Ahmad.Abushady
 *
 */
public class AODeliveryResData extends PersistenceObject {

	public Date date_time;
	
	public long success;

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}
	
}
