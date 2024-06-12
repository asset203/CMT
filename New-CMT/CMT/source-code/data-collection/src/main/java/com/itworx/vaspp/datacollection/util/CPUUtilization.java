package com.itworx.vaspp.datacollection.util;

public class CPUUtilization {
	
	private String dateTime;
	private double minIdle;
	private double maxIdle;
	private double avgIdle;
	private double minUser;
	private double maxUser;
	private double avgUser;
	private double minSystem;
	private double maxSystem;
	private double avgSystem;
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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
	public double getMinSystem() {
		return minSystem;
	}
	public void setMinSystem(double minSystem) {
		this.minSystem = minSystem;
	}
	public double getMaxSystem() {
		return maxSystem;
	}
	public void setMaxSystem(double maxSystem) {
		this.maxSystem = maxSystem;
	}
	public double getAvgSystem() {
		return avgSystem;
	}
	public void setAvgSystem(double avgSystem) {
		this.avgSystem = avgSystem;
	}

}
