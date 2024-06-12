/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class RXSubRejData extends PersistenceObject {

	public Date date_time;
	
	public double rejected;
	
	public String reason;
	
	public double originalTier;
	
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

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public double getRejected() {
		return rejected;
	}

	public void setRejected(double rejected) {
		this.rejected = rejected;
	}

	public double getOriginalTier() {
		return originalTier;
	}

	public void setOriginalTier(double originalTier) {
		this.originalTier = originalTier;
	}
	
}
