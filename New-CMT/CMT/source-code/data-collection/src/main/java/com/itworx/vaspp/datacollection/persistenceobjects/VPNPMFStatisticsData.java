package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class VPNPMFStatisticsData extends PersistenceObject{
	public Date dateTime;
	public String pgfStatistics;
	public String processorName;
	public String counterName;
	public double minCounterValue;
	public double maxCounterValue;
	public double avgCounterValue;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getPgfStatistics() {
		return pgfStatistics;
	}
	public void setPgfStatistics(String pgfStatistics) {
		this.pgfStatistics = pgfStatistics;
	}
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public double getMinCounterValue() {
		return minCounterValue;
	}
	public void setMinCounterValue(double minCounterValue) {
		this.minCounterValue = minCounterValue;
	}
	public double getMaxCounterValue() {
		return maxCounterValue;
	}
	public void setMaxCounterValue(double maxCounterValue) {
		this.maxCounterValue = maxCounterValue;
	}
	public double getAvgCounterValue() {
		return avgCounterValue;
	}
	public void setAvgCounterValue(double avgCounterValue) {
		this.avgCounterValue = avgCounterValue;
	}
public VPNPMFStatisticsData()
{
}
}
