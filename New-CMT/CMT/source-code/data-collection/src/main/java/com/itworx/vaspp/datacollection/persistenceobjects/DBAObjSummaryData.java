/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class DBAObjSummaryData extends PersistenceObject {

	
	public String owner;
	
	public String objecttype;
	
	public long objectcount;
	
	public Date date_time;
	
	/**
	 * 
	 */
	public DBAObjSummaryData() {
		// TODO Auto-generated constructor stub
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public long getObjectcount() {
		return objectcount;
	}

	public void setObjectcount(long objectcount) {
		this.objectcount = objectcount;
	}

	public String getObjecttype() {
		return objecttype;
	}

	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
