package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MoRejectCauseData extends PersistenceObject{

	public Date dateTime;
	
	public String value;
	
	public double vcount;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getVcount() {
		return vcount;
	}

	public void setVcount(double vcount) {
		this.vcount = vcount;
	}

	
}
