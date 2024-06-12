package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VASPWaitingExecutingTimeData extends PersistenceObject 
{
	public Date dateTime;
	
	public double maxRequestExecutionTime;
	
	public double minRequestExecutionTime;
	
	public double avgRequestExecutionTime;
	
	public double maxRequestWaitTime ;
	
	public double minRequestWaitTime ;
	
	public double avgRequestWaitTime ;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getAvgRequestExecutionTime() {
		return avgRequestExecutionTime;
	}

	public void setAvgRequestExecutionTime(double avgRequestExecutionTime) {
		this.avgRequestExecutionTime = avgRequestExecutionTime;
	}

	public double getAvgRequestWaitTime() {
		return avgRequestWaitTime;
	}

	public void setAvgRequestWaitTime(double avgRequestWaitTime) {
		this.avgRequestWaitTime = avgRequestWaitTime;
	}

	public double getMaxRequestExecutionTime() {
		return maxRequestExecutionTime;
	}

	public void setMaxRequestExecutionTime(double maxRequestExecutionTime) {
		this.maxRequestExecutionTime = maxRequestExecutionTime;
	}

	public double getMaxRequestWaitTime() {
		return maxRequestWaitTime;
	}

	public void setMaxRequestWaitTime(double maxRequestWaitTime) {
		this.maxRequestWaitTime = maxRequestWaitTime;
	}

	public double getMinRequestExecutionTime() {
		return minRequestExecutionTime;
	}

	public void setMinRequestExecutionTime(double minRequestExecutionTime) {
		this.minRequestExecutionTime = minRequestExecutionTime;
	}

	public double getMinRequestWaitTime() {
		return minRequestWaitTime;
	}

	public void setMinRequestWaitTime(double minRequestWaitTime) {
		this.minRequestWaitTime = minRequestWaitTime;
	}

	
}
