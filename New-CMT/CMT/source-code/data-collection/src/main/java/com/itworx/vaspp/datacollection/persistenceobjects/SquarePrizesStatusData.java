package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SquarePrizesStatusData extends PersistenceObject{
	public Date dateTime;
	public Date prizeHour;
	public double prizeStatus;
	public double count;

public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getPrizeHour() {
		return prizeHour;
	}

	public void setPrizeHour(Date prizeHour) {
		this.prizeHour = prizeHour;
	}

	public double getPrizeStatus() {
		return prizeStatus;
	}

	public void setPrizeStatus(double prizeStatus) {
		this.prizeStatus = prizeStatus;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

public SquarePrizesStatusData()
{}
}
