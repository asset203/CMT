package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class EngezlySubData extends PersistenceObject{
	public Date dateTime;
	public double downloads;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getDownloads() {
		return downloads;
	}
	public void setDownloads(double downloads) {
		this.downloads = downloads;
	}
public EngezlySubData()
{
}
}
