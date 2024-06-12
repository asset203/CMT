/* 
 * File:       MGA_SubscribersEvents.java
 * Date        Author          Changes
 * 17/04/2006  Nayera Mohamed  Created
 * 
 * Persistence class for MGA Subscribers Events
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

public class MGA_SubscribersEvents extends PersistenceObject {

	public String subscriberID;

	public String trafficEvent;

	public long count;

	public MGA_SubscribersEvents() {
	}

	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	public String getSubscriberID() {
		return subscriberID;
	}

	public void setTrafficEvent(String trafficEvent) {
		this.trafficEvent = trafficEvent;
	}

	public String getTrafficEvent() {
		return trafficEvent;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}
}