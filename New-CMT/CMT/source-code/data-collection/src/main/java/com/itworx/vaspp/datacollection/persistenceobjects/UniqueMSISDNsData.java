package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class UniqueMSISDNsData extends PersistenceObject{
	
	public Date dateTime;
	
	public long uniqueMSISDNs;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getUniqueMSISDNs() {
		return uniqueMSISDNs;
	}

	public void setUniqueMSISDNs(long uniqueMSISDNs) {
		this.uniqueMSISDNs = uniqueMSISDNs;
	}

}
