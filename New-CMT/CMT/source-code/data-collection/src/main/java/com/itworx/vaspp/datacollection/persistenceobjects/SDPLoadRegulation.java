/* 
 * File:       VoucherServerTopSec.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 * 
 * Persistence class for Voucher Server Top Sec
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SDPLoadRegulation extends PersistenceObject {

	
	public Date sdpLoadDate;

	public long noOfCalls;

	public long noOfRejected;

	public long counterOfOccurrences;

	public long maxCalls;
	
	public long rejectedOfMaxCalls;
	
	public String timeOfMaxCalls;
	
		
	public long getCounterOfOccurrences() {
		return counterOfOccurrences;
	}


	public void setCounterOfOccurrences(long counterOfOccurrences) {
		this.counterOfOccurrences = counterOfOccurrences;
	}


	public long getMaxCalls() {
		return maxCalls;
	}


	public void setMaxCalls(long maxCalls) {
		this.maxCalls = maxCalls;
	}


	public long getNoOfCalls() {
		return noOfCalls;
	}


	public void setNoOfCalls(long noOfCalls) {
		this.noOfCalls = noOfCalls;
	}


	public long getNoOfRejected() {
		return noOfRejected;
	}


	public void setNoOfRejected(long noOfRejected) {
		this.noOfRejected = noOfRejected;
	}


	public long getRejectedOfMaxCalls() {
		return rejectedOfMaxCalls;
	}


	public void setRejectedOfMaxCalls(long rejectedOfMaxCalls) {
		this.rejectedOfMaxCalls = rejectedOfMaxCalls;
	}


	public Date getSdpLoadDate() {
		return sdpLoadDate;
	}


	public void setSdpLoadDate(Date sdpLoadDate) {
		this.sdpLoadDate = sdpLoadDate;
	}


	public String getTimeOfMaxCalls() {
		return timeOfMaxCalls;
	}


	public void setTimeOfMaxCalls(String timeOfMaxCalls) {
		this.timeOfMaxCalls = timeOfMaxCalls;
	}


	public SDPLoadRegulation() {
	}
	

	
}