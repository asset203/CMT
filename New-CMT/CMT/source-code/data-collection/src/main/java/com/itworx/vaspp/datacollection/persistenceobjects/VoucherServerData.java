/* 
 * File:       VoucherServerData.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 * 
 * Persistence class for Voucher Server Data
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoucherServerData extends PersistenceObject {

	public Date dateTime; 
	
	public  long getVouchDetails_2_1_In;
	
	public  long reserveVouch_2_1_In; 
	
	public  long reserveVouch_2_1_OutSucc; 
	
	public  long endReserv_2_1_CommitSucc; 
	
	public  long endReserv_2_1_Rollback;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getEndReserv_2_1_CommitSucc() {
		return endReserv_2_1_CommitSucc;
	}

	public void setEndReserv_2_1_CommitSucc(long endReserv_2_1_CommitSucc) {
		this.endReserv_2_1_CommitSucc = endReserv_2_1_CommitSucc;
	}

	public long getEndReserv_2_1_Rollback() {
		return endReserv_2_1_Rollback;
	}

	public void setEndReserv_2_1_Rollback(long endReserv_2_1_Rollback) {
		this.endReserv_2_1_Rollback = endReserv_2_1_Rollback;
	}

	public long getGetVouchDetails_2_1_In() {
		return getVouchDetails_2_1_In;
	}

	public void setGetVouchDetails_2_1_In(long getVouchDetails_2_1_In) {
		this.getVouchDetails_2_1_In = getVouchDetails_2_1_In;
	}

	public long getReserveVouch_2_1_In() {
		return reserveVouch_2_1_In;
	}

	public void setReserveVouch_2_1_In(long reserveVouch_2_1_In) {
		this.reserveVouch_2_1_In = reserveVouch_2_1_In;
	}

	public long getReserveVouch_2_1_OutSucc() {
		return reserveVouch_2_1_OutSucc;
	}

	public void setReserveVouch_2_1_OutSucc(long reserveVouch_2_1_OutSucc) {
		this.reserveVouch_2_1_OutSucc = reserveVouch_2_1_OutSucc;
	}
	
	
}
