package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class HostCpuLoadData extends PersistenceObject{
	public Date dateTime;
	public double avgLoadAvg1;
	public double maxLoadAvg1;
	public double avgLoadAvg5;
	public double maxLoadAvg5;
	public double avgLoadAvg15;
	public double maxLoadAvg15;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getAvgLoadAvg1() {
		return avgLoadAvg1;
	}
	public void setAvgLoadAvg1(double avgLoadAvg1) {
		this.avgLoadAvg1 = avgLoadAvg1;
	}
	public double getMaxLoadAvg1() {
		return maxLoadAvg1;
	}
	public void setMaxLoadAvg1(double maxLoadAvg1) {
		this.maxLoadAvg1 = maxLoadAvg1;
	}
	public double getAvgLoadAvg5() {
		return avgLoadAvg5;
	}
	public void setAvgLoadAvg5(double avgLoadAvg5) {
		this.avgLoadAvg5 = avgLoadAvg5;
	}
	public double getMaxLoadAvg5() {
		return maxLoadAvg5;
	}
	public void setMaxLoadAvg5(double maxLoadAvg5) {
		this.maxLoadAvg5 = maxLoadAvg5;
	}
	public double getAvgLoadAvg15() {
		return avgLoadAvg15;
	}
	public void setAvgLoadAvg15(double avgLoadAvg15) {
		this.avgLoadAvg15 = avgLoadAvg15;
	}
	public double getMaxLoadAvg15() {
		return maxLoadAvg15;
	}
	public void setMaxLoadAvg15(double maxLoadAvg15) {
		this.maxLoadAvg15 = maxLoadAvg15;
	}
public HostCpuLoadData()
{
}
}
