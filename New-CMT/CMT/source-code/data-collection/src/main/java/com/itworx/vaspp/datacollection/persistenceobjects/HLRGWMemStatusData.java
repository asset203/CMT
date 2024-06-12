package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class HLRGWMemStatusData extends PersistenceObject{
	public Date dateTime;
	public double minFreeMem;
	public double maxFreeMem;
	public double avgFreeMem;
	public double minFreeSwap;
	public double maxFreeSwap;
	public double avgFreeSwap;
	public double avgMemUtiliz;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMinFreeMem() {
		return minFreeMem;
	}
	public void setMinFreeMem(double minFreeMem) {
		this.minFreeMem = minFreeMem;
	}
	public double getMaxFreeMem() {
		return maxFreeMem;
	}
	public void setMaxFreeMem(double maxFreeMem) {
		this.maxFreeMem = maxFreeMem;
	}
	public double getAvgFreeMem() {
		return avgFreeMem;
	}
	public void setAvgFreeMem(double avgFreeMem) {
		this.avgFreeMem = avgFreeMem;
	}
	public double getMinFreeSwap() {
		return minFreeSwap;
	}
	public void setMinFreeSwap(double minFreeSwap) {
		this.minFreeSwap = minFreeSwap;
	}
	public double getMaxFreeSwap() {
		return maxFreeSwap;
	}
	public void setMaxFreeSwap(double maxFreeSwap) {
		this.maxFreeSwap = maxFreeSwap;
	}
	public double getAvgFreeSwap() {
		return avgFreeSwap;
	}
	public void setAvgFreeSwap(double avgFreeSwap) {
		this.avgFreeSwap = avgFreeSwap;
	}
	public double getAvgMemUtiliz() {
		return avgMemUtiliz;
	}
	public void setAvgMemUtiliz(double avgMemUtiliz) {
		this.avgMemUtiliz = avgMemUtiliz;
	}
public HLRGWMemStatusData()
{
}
}
