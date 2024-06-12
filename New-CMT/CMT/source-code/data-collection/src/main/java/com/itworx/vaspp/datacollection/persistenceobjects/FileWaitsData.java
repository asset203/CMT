package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class FileWaitsData extends PersistenceObject{
	
	public Date date;
	
	public String fileName;
	
	public long waits;
	
	public long time;
	
	public double avgTime;

	public double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(double avgTime) {
		this.avgTime = avgTime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getWaits() {
		return waits;
	}

	public void setWaits(long waits) {
		this.waits = waits;
	}

}
