package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SDPHWMonitoringData extends PersistenceObject{
	public Date dateTime;
	public double minMem;
	public double maxMem;
	public double avgMem;
	public double minCpu;
	public double maxCpu;
	public double avgCpu;
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMinMem() {
		return minMem;
	}
	public void setMinMem(double minMem) {
		this.minMem = minMem;
	}
	public double getMaxMem() {
		return maxMem;
	}
	public void setMaxMem(double maxMem) {
		this.maxMem = maxMem;
	}
	public double getAvgMem() {
		return avgMem;
	}
	public void setAvgMem(double avgMem) {
		this.avgMem = avgMem;
	}
	public double getMinCpu() {
		return minCpu;
	}
	public void setMinCpu(double minCpu) {
		this.minCpu = minCpu;
	}
	public double getMaxCpu() {
		return maxCpu;
	}
	public void setMaxCpu(double maxCpu) {
		this.maxCpu = maxCpu;
	}
	public double getAvgCpu() {
		return avgCpu;
	}
	public void setAvgCpu(double avgCpu) {
		this.avgCpu = avgCpu;
	}
public SDPHWMonitoringData()
{}
}
