/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class DBAFilesData extends PersistenceObject {

	public String tablespace;
	
	public String filename;
	
	public long filesize;
	
	public Date date_time;
	/**
	 * 
	 */
	public DBAFilesData() {
		// TODO Auto-generated constructor stub
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getTablespace() {
		return tablespace;
	}

	public void setTablespace(String tablespace) {
		this.tablespace = tablespace;
	}

}
