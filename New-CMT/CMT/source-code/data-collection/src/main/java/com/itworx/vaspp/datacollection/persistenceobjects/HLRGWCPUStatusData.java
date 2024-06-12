package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class HLRGWCPUStatusData extends PersistenceObject{
	public Date dateTime;
	public double minSys;
	public double maxSys;
	public double avgSys;			
	public double minIdle;
	public double maxIdle;
	public double avgIdle;			
	public double minUsr;
	public double maxUsr;
	public double avgUsr;			
	public double minWio;
	public double maxWio;
	public double avgWio;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
	public double getMinUsr() {
		return minUsr;
	}
	public void setMinUsr(double minUsr) {
		this.minUsr = minUsr;
	}
	public double getMaxUsr() {
		return maxUsr;
	}
	public void setMaxUsr(double maxUsr) {
		this.maxUsr = maxUsr;
	}
	public double getAvgUsr() {
		return avgUsr;
	}
	public void setAvgUsr(double avgUsr) {
		this.avgUsr = avgUsr;
	}
	public double getMinWio() {
		return minWio;
	}
	public void setMinWio(double minWio) {
		this.minWio = minWio;
	}
	public double getMaxWio() {
		return maxWio;
	}
	public void setMaxWio(double maxWio) {
		this.maxWio = maxWio;
	}
	public double getAvgWio() {
		return avgWio;
	}
	public void setAvgWio(double avgWio) {
		this.avgWio = avgWio;
	}
public HLRGWCPUStatusData()
{
}
}
