package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RxSubProccTimeData extends PersistenceObject{
	public Date dateTime;
	public double minDuration;
	public double maxDuration;
	public double avgDuration;
	public String schemaID;
	public String schemaDescription;
	
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(double minDuration) {
		this.minDuration = minDuration;
	}
	public double getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}
	public double getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(double avgDuration) {
		this.avgDuration = avgDuration;
	}
		
	public String getSchemaID() {
		return schemaID;
	}
	public void setSchemaID(String schemaID) {
		this.schemaID = schemaID;
	}
	public String getSchemaDescription() {
		return schemaDescription;
	}
	public void setSchemaDescription(String schemaDescription) {
		this.schemaDescription = schemaDescription;
	}
public RxSubProccTimeData()
{
}
}
