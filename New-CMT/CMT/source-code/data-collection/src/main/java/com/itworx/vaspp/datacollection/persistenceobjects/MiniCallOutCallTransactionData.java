package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallOutCallTransactionData extends PersistenceObject{
	
	public Date dateTime;
	
	public double vmsOutCallSucNo;
	
	public double vmsOutCallFailNo;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getVmsOutCallSucNo() {
		return vmsOutCallSucNo;
	}

	public void setVmsOutCallSucNo(double vmsOutCallSucNo) {
		this.vmsOutCallSucNo = vmsOutCallSucNo;
	}

	public double getVmsOutCallFailNo() {
		return vmsOutCallFailNo;
	}

	public void setVmsOutCallFailNo(double vmsOutCallFailNo) {
		this.vmsOutCallFailNo = vmsOutCallFailNo;
	}
	

	
}
