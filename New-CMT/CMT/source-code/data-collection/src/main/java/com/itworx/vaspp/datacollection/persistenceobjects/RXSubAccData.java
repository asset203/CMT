/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class RXSubAccData extends PersistenceObject {

	public Date date_time;
	public String tier;
	public double requests;
	public double sc;
	public String schemaID;
	public String schemaDescription;
	public double channelID;
	
	public String channelName;
	
	
	
	
	public double getChannelID() {
		return channelID;
	}

	public void setChannelID(double channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public double getSc() {
		return sc;
	}

	public void setSc(double sc) {
		this.sc = sc;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public double getRequests() {
		return requests;
	}

	public void setRequests(double requests) {
		this.requests = requests;
	}
}
