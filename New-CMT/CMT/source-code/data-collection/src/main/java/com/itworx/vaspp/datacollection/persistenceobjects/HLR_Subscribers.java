/* 
 * File:       HLR_Subscribers.java
 * Date        Author          Changes
 * 07/02/2006  Nayera Mohamed  Created
 * 
 * Persistence class for HLR Subscribers Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class HLR_Subscribers extends PersistenceObject {
	
	public long allocated;

	public long registered;

	public long valid;
	
	public Date dateTime;

	public HLR_Subscribers() {
	}

	public void setAllocated(long allocated) {
		this.allocated = allocated;
	}

	public long getAllocated() {
		return allocated;
	}

	public void setRegistered(long registered) {
		this.registered = registered;
	}

	public long getRegistered() {
		return registered;
	}

	public long getValid() {
		return valid;
	}

	public void setValid(long valid) {
		this.valid = valid;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



}