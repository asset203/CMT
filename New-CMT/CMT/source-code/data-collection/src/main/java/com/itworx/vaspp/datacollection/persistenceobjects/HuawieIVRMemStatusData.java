package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class HuawieIVRMemStatusData extends PersistenceObject{
	public Date dateTime;
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getMinReal() {
		return minReal;
	}

	public void setMinReal(double minReal) {
		this.minReal = minReal;
	}

	public double getMaxReal() {
		return maxReal;
	}

	public void setMaxReal(double maxReal) {
		this.maxReal = maxReal;
	}

	public double getAvgReal() {
		return avgReal;
	}

	public void setAvgReal(double avgReal) {
		this.avgReal = avgReal;
	}

	public double getMinVirtual() {
		return minVirtual;
	}

	public void setMinVirtual(double minVirtual) {
		this.minVirtual = minVirtual;
	}

	public double getMaxVirtual() {
		return maxVirtual;
	}

	public void setMaxVirtual(double maxVirtual) {
		this.maxVirtual = maxVirtual;
	}

	public double getAvgVirtual() {
		return avgVirtual;
	}

	public void setAvgVirtual(double avgVirtual) {
		this.avgVirtual = avgVirtual;
	}

	public double getMinFree() {
		return minFree;
	}

	public void setMinFree(double minFree) {
		this.minFree = minFree;
	}

	public double getMaxFree() {
		return maxFree;
	}

	public void setMaxFree(double maxFree) {
		this.maxFree = maxFree;
	}

	public double getAvgFree() {
		return avgFree;
	}

	public void setAvgFree(double avgFree) {
		this.avgFree = avgFree;
	}

	public double minReal;
	public double maxReal;
	public double avgReal;
	public double minVirtual;
	public double maxVirtual;
	public double avgVirtual;
	public double minFree;
	public double maxFree;
	public double avgFree;
	
	public HuawieIVRMemStatusData()
{}
}
