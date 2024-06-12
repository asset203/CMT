/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class MCKQueuedData extends PersistenceObject {

	public Date date_time;
	
	public long calls;
	
	public long divert;
	
	public long sms;

	public long getCalls() {
		return calls;
	}

	public void setCalls(long calls) {
		this.calls = calls;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public long getDivert() {
		return divert;
	}

	public void setDivert(long divert) {
		this.divert = divert;
	}

	public long getSms() {
		return sms;
	}

	public void setSms(long sms) {
		this.sms = sms;
	}
}
