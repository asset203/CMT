package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OffLineMemUtilizeData extends PersistenceObject{
	public Date dateTime;
	public double minMemUtilize;
	public double maxMemUtilize;
	public double avgMemUtilize;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMinMemUtilize() {
		return minMemUtilize;
	}
	public void setMinMemUtilize(double minMemUtilize) {
		this.minMemUtilize = minMemUtilize;
	}
	public double getMaxMemUtilize() {
		return maxMemUtilize;
	}
	public void setMaxMemUtilize(double maxMemUtilize) {
		this.maxMemUtilize = maxMemUtilize;
	}
	public double getAvgMemUtilize() {
		return avgMemUtilize;
	}
	public void setAvgMemUtilize(double avgMemUtilize) {
		this.avgMemUtilize = avgMemUtilize;
	}
public OffLineMemUtilizeData()
{
}
}
