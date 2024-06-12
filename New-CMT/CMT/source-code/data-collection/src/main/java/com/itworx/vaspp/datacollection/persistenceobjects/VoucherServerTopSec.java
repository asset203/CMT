/* 
 * File:       VoucherServerTopSec.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 * 
 * Persistence class for Voucher Server Top Sec
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VoucherServerTopSec extends PersistenceObject {

	public String dateTime;
	
	public Date topSecDate;

	public double getVoucherDetails;

	public double reserveVoucherIn;

	public double reserveVoucherOutSuccess;

	public double endReservationCommitOutSuccess;
	
	public double endReservationRollbackInValid;
	
		
	public VoucherServerTopSec() {
	}


	public String getDateTime() {
		return dateTime;
	}


	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}


	public double getEndReservationCommitOutSuccess() {
		return endReservationCommitOutSuccess;
	}


	public void setEndReservationCommitOutSuccess(
			double endReservationCommitOutSuccess) {
		this.endReservationCommitOutSuccess = endReservationCommitOutSuccess;
	}


	public double getEndReservationRollbackInValid() {
		return endReservationRollbackInValid;
	}


	public void setEndReservationRollbackInValid(double endReservationRollbackInValid) {
		this.endReservationRollbackInValid = endReservationRollbackInValid;
	}


	public double getGetVoucherDetails() {
		return getVoucherDetails;
	}


	public void setGetVoucherDetails(double getVoucherDetails) {
		this.getVoucherDetails = getVoucherDetails;
	}


	public double getReserveVoucherIn() {
		return reserveVoucherIn;
	}


	public void setReserveVoucherIn(double reserveVoucherIn) {
		this.reserveVoucherIn = reserveVoucherIn;
	}


	public double getReserveVoucherOutSuccess() {
		return reserveVoucherOutSuccess;
	}


	public void setReserveVoucherOutSuccess(double reserveVoucherOutSuccess) {
		this.reserveVoucherOutSuccess = reserveVoucherOutSuccess;
	}


	public Date getTopSecDate() {
		return topSecDate;
	}


	public void setTopSecDate(Date topSecDate) {
		this.topSecDate = topSecDate;
	}

	

	
}