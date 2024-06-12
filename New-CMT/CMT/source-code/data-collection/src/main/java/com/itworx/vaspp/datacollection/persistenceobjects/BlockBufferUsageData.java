/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class BlockBufferUsageData extends PersistenceObject {

	public Date date_time;
	
	public String blockstatus;
	
	public long count;
	
	
	/**
	 * 
	 */
	public BlockBufferUsageData() {
		// TODO Auto-generated constructor stub
	}


	public String getBlockstatus() {
		return blockstatus;
	}


	public void setBlockstatus(String blockstatus) {
		this.blockstatus = blockstatus;
	}


	public long getCount() {
		return count;
	}


	public void setCount(long count) {
		this.count = count;
	}


	public Date getDate_time() {
		return date_time;
	}


	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

}
