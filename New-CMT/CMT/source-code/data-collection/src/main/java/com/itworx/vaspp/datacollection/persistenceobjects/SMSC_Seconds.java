/* 
 * File:       SMSC_Seconds.java
 * Date        Author          Changes
 * 20/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for SMSC Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMSC_Seconds extends PersistenceObject {

	public Date time;

	public double count;

	

	public SMSC_Seconds() {
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getCount() {
		return count;
	}
}