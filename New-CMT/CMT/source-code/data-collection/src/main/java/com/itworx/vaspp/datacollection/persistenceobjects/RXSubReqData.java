/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class RXSubReqData extends PersistenceObject {

	public Date hour;
	
	public String channel;
	
	public double requests;
	
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getHour() {
		return hour;
	}

	public void setHour(Date hour) {
		this.hour = hour;
	}

	public double getRequests() {
		return requests;
	}

	public void setRequests(double requests) {
		this.requests = requests;
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

	
}
