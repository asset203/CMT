package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RamadanPromoPrizeData extends PersistenceObject{
	public Date dateTime;
	public Date prizHour;
	public double prizeStatus;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public Date getPrizHour() {
		return prizHour;
	}
	public void setPrizHour(Date prizHour) {
		this.prizHour = prizHour;
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
public RamadanPromoPrizeData()
{
}
}
