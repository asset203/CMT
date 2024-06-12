package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RedRoamAvgTimeData extends PersistenceObject{
public Date dateTime;
	public double startTimeMin;
	public double startTimeMax;
	public double startTimeavg;
	public double fstLegTimeMin;
	public double fstLegTimeMax;
	public double fstLegTimeAvg;
	public double sndLegTimeMin;
	public double sndLegTimeMax;
	public double sndLegTimeAvg;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getStartTimeMin() {
		return startTimeMin;
	}
	public void setStartTimeMin(double startTimeMin) {
		this.startTimeMin = startTimeMin;
	}
	public double getStartTimeMax() {
		return startTimeMax;
	}
	public void setStartTimeMax(double startTimeMax) {
		this.startTimeMax = startTimeMax;
	}
	public double getStartTimeavg() {
		return startTimeavg;
	}
	public void setStartTimeavg(double startTimeavg) {
		this.startTimeavg = startTimeavg;
	}
	public double getFstLegTimeMin() {
		return fstLegTimeMin;
	}
	public void setFstLegTimeMin(double fstLegTimeMin) {
		this.fstLegTimeMin = fstLegTimeMin;
	}
	public double getFstLegTimeMax() {
		return fstLegTimeMax;
	}
	public void setFstLegTimeMax(double fstLegTimeMax) {
		this.fstLegTimeMax = fstLegTimeMax;
	}
	public double getFstLegTimeAvg() {
		return fstLegTimeAvg;
	}
	public void setFstLegTimeAvg(double fstLegTimeAvg) {
		this.fstLegTimeAvg = fstLegTimeAvg;
	}
	public double getSndLegTimeMin() {
		return sndLegTimeMin;
	}
	public void setSndLegTimeMin(double sndLegTimeMin) {
		this.sndLegTimeMin = sndLegTimeMin;
	}
	public double getSndLegTimeMax() {
		return sndLegTimeMax;
	}
	public void setSndLegTimeMax(double sndLegTimeMax) {
		this.sndLegTimeMax = sndLegTimeMax;
	}
	public double getSndLegTimeAvg() {
		return sndLegTimeAvg;
	}
	public void setSndLegTimeAvg(double sndLegTimeAvg) {
		this.sndLegTimeAvg = sndLegTimeAvg;
	}
public RedRoamAvgTimeData()
{
}
}
