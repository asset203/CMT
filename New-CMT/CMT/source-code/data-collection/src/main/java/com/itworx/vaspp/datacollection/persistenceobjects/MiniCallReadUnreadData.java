package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallReadUnreadData extends PersistenceObject {

	public Date dateTime;
	public String mailTitle;
	public double mailStatus;
	public double count;
	
	public MiniCallReadUnreadData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public double getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(double mailStatus) {
		this.mailStatus = mailStatus;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
}
