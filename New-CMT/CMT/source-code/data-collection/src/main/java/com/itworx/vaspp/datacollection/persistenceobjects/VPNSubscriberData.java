package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VPNSubscriberData extends PersistenceObject{
	
	public Date time;
	
	public long subscribers;

	public long getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(long subscribers) {
		this.subscribers = subscribers;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
