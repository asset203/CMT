package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class USSDConnectorsDurationData extends PersistenceObject{

	public Date date;
	
	public String connectorName;
	
	public long minDuration;
	
	public long maxDuration;
	
	public long avgDuration;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public long getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(long minDuration) {
		this.minDuration = minDuration;
	}

	public long getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}

	public long getAvgDuration() {
		return avgDuration;
	}

	public void setAvgDuration(long avgDuration) {
		this.avgDuration = avgDuration;
	}
	
	
	

}
