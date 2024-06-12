/* 
 * File:       MGA_CodesEvents.java
 * Date        Author          Changes
 * 18/04/2006  Nayera Mohamed  Created
 * 
 * Persistence class for MGA Codes Events
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

public class MGA_CodesEvents extends PersistenceObject {

	public String subscriberID;

	public String shortCode;

	public long originatingCount;

	public long destinationCount;

	public MGA_CodesEvents() {
	}

	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	public String getSubscriberID() {
		return subscriberID;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setOriginatingCount(long originatingCount) {
		this.originatingCount = originatingCount;
	}

	public long getOriginatingCount() {
		return originatingCount;
	}

	public void setDestinationCount(long destinationCount) {
		this.destinationCount = destinationCount;
	}

	public long getDestinationCount() {
		return destinationCount;
	}

}