package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMPPResponseTimeData extends PersistenceObject{
	
	public Date dateTime;
	public double maxDequeue;
	public double minDequeue;
	public double avgDequeue;
	public double maxSend;
	public double minSend;
	public double avgSend;
	public double maxRouting;
	public double minRouting;
	public double avgRouting;
	public double maxSms;
	public double minSms;
	public double avgSms;
	public String applicationName;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public double getAvgDequeue() {
		return avgDequeue;
	}
	public void setAvgDequeue(double avgDequeue) {
		this.avgDequeue = avgDequeue;
	}
	public double getAvgRouting() {
		return avgRouting;
	}
	public void setAvgRouting(double avgRouting) {
		this.avgRouting = avgRouting;
	}
	public double getAvgSend() {
		return avgSend;
	}
	public void setAvgSend(double avgSend) {
		this.avgSend = avgSend;
	}
	public double getAvgSms() {
		return avgSms;
	}
	public void setAvgSms(double avgSms) {
		this.avgSms = avgSms;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMaxDequeue() {
		return maxDequeue;
	}
	public void setMaxDequeue(double maxDequeue) {
		this.maxDequeue = maxDequeue;
	}
	public double getMaxRouting() {
		return maxRouting;
	}
	public void setMaxRouting(double maxRouting) {
		this.maxRouting = maxRouting;
	}
	public double getMaxSend() {
		return maxSend;
	}
	public void setMaxSend(double maxSend) {
		this.maxSend = maxSend;
	}
	public double getMaxSms() {
		return maxSms;
	}
	public void setMaxSms(double maxSms) {
		this.maxSms = maxSms;
	}
	public double getMinDequeue() {
		return minDequeue;
	}
	public void setMinDequeue(double minDequeue) {
		this.minDequeue = minDequeue;
	}
	public double getMinRouting() {
		return minRouting;
	}
	public void setMinRouting(double minRouting) {
		this.minRouting = minRouting;
	}
	public double getMinSend() {
		return minSend;
	}
	public void setMinSend(double minSend) {
		this.minSend = minSend;
	}
	public double getMinSms() {
		return minSms;
	}
	public void setMinSms(double minSms) {
		this.minSms = minSms;
	}

}
