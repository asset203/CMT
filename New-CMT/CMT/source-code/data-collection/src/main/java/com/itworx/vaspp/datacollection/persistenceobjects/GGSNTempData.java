package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class GGSNTempData extends PersistenceObject{
	public Date dateTime;
	public double tak;
	public double upLink;
	public double downLink;
	public double count;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTak() {
		return tak;
	}
	public void setTak(double tak) {
		this.tak = tak;
	}
	public double getUpLink() {
		return upLink;
	}
	public void setUpLink(double upLink) {
		this.upLink = upLink;
	}
	public double getDownLink() {
		return downLink;
	}
	public void setDownLink(double downLink) {
		this.downLink = downLink;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	
public GGSNTempData()
{}
}
