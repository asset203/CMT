/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author Ahmad.Abushady
 *
 */
public class AOAppNameData extends PersistenceObject {

	public Date date_time;
	
	public String value;
	
	public long vcount;

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getVcount() {
		return vcount;
	}

	public void setVcount(long vcount) {
		this.vcount = vcount;
	}

	
}
