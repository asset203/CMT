/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class RXUnsubUnsuccSMSData extends PersistenceObject {

	public Date date_time;
	
	public double requests;
	public String schemaID;
	public String schemaDescription;

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