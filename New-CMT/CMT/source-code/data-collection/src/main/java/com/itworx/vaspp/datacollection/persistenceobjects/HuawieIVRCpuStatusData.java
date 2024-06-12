package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class HuawieIVRCpuStatusData extends PersistenceObject {

	
	public Date dateTime;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public double getMinUser() {
		return minUser;
	}
	public void setMinUser(double minUser) {
		this.minUser = minUser;
	}
	public double getMaxUser() {
		return maxUser;
	}
	public void setMaxUser(double maxUser) {
		this.maxUser = maxUser;
	}
	public double getAvgUser() {
		return avgUser;
	}
	public void setAvgUser(double avgUser) {
		this.avgUser = avgUser;
	}
	public double getMinSys() {
		return minSys;
	}
	public void setMinSys(double minSys) {
		this.minSys = minSys;
	}
	public double getMaxSys() {
		return maxSys;
	}
	public void setMaxSys(double maxSys) {
		this.maxSys = maxSys;
	}
	public double getAvgSys() {
		return avgSys;
	}
	public void setAvgSys(double avgSys) {
		this.avgSys = avgSys;
	}
	public double getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(double minIdle) {
		this.minIdle = minIdle;
	}
	public double getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(double maxIdle) {
		this.maxIdle = maxIdle;
	}
	public double getAvgIdle() {
		return avgIdle;
	}
	public void setAvgIdle(double avgIdle) {
		this.avgIdle = avgIdle;
	}
	public String cpu;					
	public double minUser;
	public double maxUser;
	public double avgUser;
	public double minSys;
	public double maxSys;
	public double avgSys;
	public double minIdle;
	public double maxIdle;
	public double avgIdle;
	public HuawieIVRCpuStatusData()
	{
		
	}
}
