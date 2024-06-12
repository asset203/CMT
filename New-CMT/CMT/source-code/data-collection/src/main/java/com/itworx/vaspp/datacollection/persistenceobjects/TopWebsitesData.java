package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class TopWebsitesData extends PersistenceObject{
	
	public Date dateTime;
	
	public String websiteURL;
	
	public long websiteRequests;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getWebsiteRequests() {
		return websiteRequests;
	}

	public void setWebsiteRequests(long websiteRequests) {
		this.websiteRequests = websiteRequests;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}
	
}
