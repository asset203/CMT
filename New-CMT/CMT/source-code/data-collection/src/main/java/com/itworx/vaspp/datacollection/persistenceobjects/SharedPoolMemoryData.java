/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class SharedPoolMemoryData extends PersistenceObject {

	public long poolsize;
	
	public long freebytes;
	
	public long percentfree;
	
	public Date date_time;
	/**
	 * 
	 */
	public SharedPoolMemoryData() {
		// TODO Auto-generated constructor stub
	}
	public Date getDate_time() {
		return date_time;
	}
	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}
	public long getFreebytes() {
		return freebytes;
	}
	public void setFreebytes(long freebytes) {
		this.freebytes = freebytes;
	}
	public long getPercentfree() {
		return percentfree;
	}
	public void setPercentfree(long percentfree) {
		this.percentfree = percentfree;
	}
	public long getPoolsize() {
		return poolsize;
	}
	public void setPoolsize(long poolsize) {
		this.poolsize = poolsize;
	}

}
