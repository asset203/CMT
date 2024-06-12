package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BrokenJobsData extends PersistenceObject{
	
	public String shema;
	
	public long jobID;
	
	public Date dateTime;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getJobID() {
		return jobID;
	}

	public void setJobID(long jobID) {
		this.jobID = jobID;
	}

	public String getShema() {
		return shema;
	}

	public void setShema(String shema) {
		this.shema = shema;
	}

}
