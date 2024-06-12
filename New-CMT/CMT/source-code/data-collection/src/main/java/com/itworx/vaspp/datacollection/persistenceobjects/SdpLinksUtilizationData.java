package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SdpLinksUtilizationData extends PersistenceObject{

	public Date dateTime;
	public String tsp;
	public String sdp;
	public double maxSent;  
	public double minSent;
	public double averageSent;
	public double maxReceived;  
	public double minReceived;
	public double averageReceived;
	
	public double getAverageReceived() {
		return averageReceived;
	}
	public void setAverageReceived(double averageReceived) {
		this.averageReceived = averageReceived;
	}
	public double getAverageSent() {
		return averageSent;
	}
	public void setAverageSent(double averageSent) {
		this.averageSent = averageSent;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMaxReceived() {
		return maxReceived;
	}
	public void setMaxReceived(double maxReceived) {
		this.maxReceived = maxReceived;
	}
	public double getMaxSent() {
		return maxSent;
	}
	public void setMaxSent(double maxSent) {
		this.maxSent = maxSent;
	}
	public double getMinReceived() {
		return minReceived;
	}
	public void setMinReceived(double minReceived) {
		this.minReceived = minReceived;
	}
	public double getMinSent() {
		return minSent;
	}
	public void setMinSent(double minSent) {
		this.minSent = minSent;
	}
	public String getSdp() {
		return sdp;
	}
	public void setSdp(String sdp) {
		this.sdp = sdp;
	}
	public String getTsp() {
		return tsp;
	}
	public void setTsp(String tsp) {
		this.tsp = tsp;
	}
}
