package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SwitchDivertAvgTimeData extends PersistenceObject{
	public Date dateTime;
	public double avgActionTime;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getAvgActionTime() {
		return avgActionTime;
	}
	public void setAvgActionTime(double avgActionTime) {
		this.avgActionTime = avgActionTime;
	}
public SwitchDivertAvgTimeData()
{}
}
