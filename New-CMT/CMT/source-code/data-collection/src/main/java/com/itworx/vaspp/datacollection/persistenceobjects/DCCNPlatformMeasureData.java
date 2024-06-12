package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class DCCNPlatformMeasureData extends PersistenceObject{
	public Date dateTime;
	public String processorName;
	public double avgCpuLoad;
	public double minCpuLoad;
	public double maxCpuLoad;
	public double avgMemUsage;
	public double minMemUsage;
	public double maxMemUsage;
    public double maxMaxCpuLoad;
    public double avgMaxCpuLoad;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	public double getAvgCpuLoad() {
		return avgCpuLoad;
	}
	public void setAvgCpuLoad(double avgCpuLoad) {
		this.avgCpuLoad = avgCpuLoad;
	}
	public double getMinCpuLoad() {
		return minCpuLoad;
	}
	public void setMinCpuLoad(double minCpuLoad) {
		this.minCpuLoad = minCpuLoad;
	}
	public double getMaxCpuLoad() {
		return maxCpuLoad;
	}
	public void setMaxCpuLoad(double maxCpuLoad) {
		this.maxCpuLoad = maxCpuLoad;
	}
	public double getAvgMemUsage() {
		return avgMemUsage;
	}
	public void setAvgMemUsage(double avgMemUsage) {
		this.avgMemUsage = avgMemUsage;
	}
	public double getMinMemUsage() {
		return minMemUsage;
	}
	public void setMinMemUsage(double minMemUsage) {
		this.minMemUsage = minMemUsage;
	}
	public double getMaxMemUsage() {
		return maxMemUsage;
	}
	public void setMaxMemUsage(double maxMemUsage) {
		this.maxMemUsage = maxMemUsage;
	}
    public double getMaxMaxCpuLoad() {
        return maxMaxCpuLoad;
    }
    public void setMaxMaxCpuLoad(double maxMaxCpuLoad) {
        this.maxMaxCpuLoad = maxMaxCpuLoad;
    }
    public double getAvgMaxCpuLoad() {
        return avgMaxCpuLoad;
    }
    public void setAvgMaxCpuLoad(double avgMaxCpuLoad) {
        this.avgMaxCpuLoad = avgMaxCpuLoad;
    }

    public DCCNPlatformMeasureData()
	{}

}
