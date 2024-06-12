/* 
 * File:       VMSData.java
 * Date        Author          Changes
 * 01/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for VMS Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VMSData extends PersistenceObject {

	public Date time;

	public long incoming;

	public long with_msg;

	public long no_msg;

	public VMSData() {
	}


	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setIncoming(long incoming) {
		this.incoming = incoming;
	}

	public long getIncoming() {
		return incoming;
	}

	public void setWith_msg(long with_msg) {
		this.with_msg = with_msg;
	}

	public long getWith_msg() {
		return with_msg;
	}

	public void setNo_msg(long no_msg) {
		this.no_msg = no_msg;
	}

	public long getNo_msg() {
		return no_msg;
	}

}