/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class SDPSnapshotData extends PersistenceObject {

	public String misdnstart;
	
	public String misdnend;
	
	public String serviceclass;
	
	public long balance;
	
	public long validity;
	
	public long grace;
	
	public long aftergrace;
	
	public long c_all;
	
	public Date date_time;
	
	/**
	 * 
	 */
	public SDPSnapshotData() {
		// TODO Auto-generated constructor stub
	}

	public long getAftergrace() {
		return aftergrace;
	}

	public void setAftergrace(long aftergrace) {
		this.aftergrace = aftergrace;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getC_all() {
		return c_all;
	}

	public void setC_all(long c_all) {
		this.c_all = c_all;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public long getGrace() {
		return grace;
	}

	public void setGrace(long grace) {
		this.grace = grace;
	}

	public String getMisdnend() {
		return misdnend;
	}

	public void setMisdnend(String misdnend) {
		this.misdnend = misdnend;
	}

	public String getMisdnstart() {
		return misdnstart;
	}

	public void setMisdnstart(String misdnstart) {
		this.misdnstart = misdnstart;
	}

	public String getServiceclass() {
		return serviceclass;
	}

	public void setServiceclass(String serviceclass) {
		this.serviceclass = serviceclass;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

}
