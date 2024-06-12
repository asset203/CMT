package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallRetrievalsData extends PersistenceObject {
	
	public Date dateTime;
	public String calleeNo;
	public double count;
	
	public MiniCallRetrievalsData(){
		
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getCalleeNo() {
		return calleeNo;
	}

	public void setCalleeNo(String calleeNo) {
		this.calleeNo = calleeNo;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	

}
