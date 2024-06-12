/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class SDPNewLoadRegData extends PersistenceObject {

	
	public Date date_time;
	
	public long rejected;
	
	public long rejection_event;
	
	
	public Date getDate_time() {
		return date_time;
	}


	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}


	public long getRejected() {
		return rejected;
	}


	public void setRejected(long rejected) {
		this.rejected = rejected;
	}


	public long getRejection_event() {
		return rejection_event;
	}


	public void setRejection_event(long rejection_event) {
		this.rejection_event = rejection_event;
	}


	/**
	 * 
	 */
	public SDPNewLoadRegData() {
		// TODO Auto-generated constructor stub
	}

}
