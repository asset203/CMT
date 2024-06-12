/* 
 * File:       VoucherServerData.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 * 
 * Persistence class for Voucher Server Data
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoucherServerInfo extends PersistenceObject {

	public Date dateTime; 
	
	public  long filesCount;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getFilesCount() {
		return filesCount;
	}

	public void setFilesCount(long filesCount) {
		this.filesCount = filesCount;
	}
	
		
}
