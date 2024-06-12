package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DataSessionsDistributionData extends PersistenceObject{
	
	public Date dateTime;
	
	public double avg2G;
	
	public double avg3G;
	
	public double avgMMS;
	
	public double avgInternet;
	
	public double avgWAP;

	public double getAvg2G() {
		return avg2G;
	}

	public void setAvg2G(double avg2G) {
		this.avg2G = avg2G;
	}

	public double getAvg3G() {
		return avg3G;
	}

	public void setAvg3G(double avg3G) {
		this.avg3G = avg3G;
	}

	public double getAvgInternet() {
		return avgInternet;
	}

	public void setAvgInternet(double avgInternet) {
		this.avgInternet = avgInternet;
	}

	public double getAvgMMS() {
		return avgMMS;
	}

	public void setAvgMMS(double avgMMS) {
		this.avgMMS = avgMMS;
	}

	public double getAvgWAP() {
		return avgWAP;
	}

	public void setAvgWAP(double avgWAP) {
		this.avgWAP = avgWAP;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
