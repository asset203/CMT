/* 
 * File:       RBT_Data.java
 * Date        Author          Changes
 * 29/01/2006  Nayera Mohamed  Created
 * 
 * Persistence class for RBT Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class RBT_Data extends PersistenceObject {

	public Date time;

	public long user;

	public long system;

	public long wio;

	public long idle;

	public RBT_Data() {
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getUser() {
		return user;
	}

	public void setSystem(long system) {
		this.system = system;
	}

	public long getSystem() {
		return system;
	}

	public void setWio(long wio) {
		this.wio = wio;
	}

	public long getWio() {
		return wio;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

	public long getIdle() {
		return idle;
	}


	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

}