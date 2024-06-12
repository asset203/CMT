/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class FileIOEffeciencyData extends PersistenceObject {

	public String tablespace;
	
	public String filename;
	
	public long readsrights;
	
	public double effeciency;
	
	public Date date_time;
	
	/**
	 * 
	 */
	public FileIOEffeciencyData() {
		// TODO Auto-generated constructor stub
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public double getEffeciency() {
		return effeciency;
	}

	public void setEffeciency(double effeciency) {
		this.effeciency = effeciency;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getReadsrights() {
		return readsrights;
	}

	public void setReadsrights(long readsrights) {
		this.readsrights = readsrights;
	}

	public String getTablespace() {
		return tablespace;
	}

	public void setTablespace(String tablespace) {
		this.tablespace = tablespace;
	}

}
