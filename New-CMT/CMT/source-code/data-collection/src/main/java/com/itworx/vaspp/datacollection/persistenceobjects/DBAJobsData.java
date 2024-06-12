package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DBAJobsData extends PersistenceObject{
	
	public Date date;
	
	public long jobID;
	
	public String user;
	
	public String what;
	
	public Date nextRunDate;
	
	public String interval;
	
	public Date lastRunDate;
	
	public long failures;
	
	public String broken;

	public String getBroken() {
		return broken;
	}

	public void setBroken(String broken) {
		this.broken = broken;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getFailures() {
		return failures;
	}

	public void setFailures(long failures) {
		this.failures = failures;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public long getJobID() {
		return jobID;
	}

	public void setJobID(long jobID) {
		this.jobID = jobID;
	}

	public Date getLastRunDate() {
		return lastRunDate;
	}

	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}

	public Date getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}
	
}
