package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNINAPCipQualityData extends PersistenceObject{

	public Date dateTime;
	public String sdpId;
	public double cipTotalRequests;
	public double cipTotalResults;
	public double cipFailedResults;
	public double cipTopTotalRequests;
	public double cipTopTotalResults;
	public double cipTopFailedResults;
	
	public double getCipFailedResults() {
		return cipFailedResults;
	}
	public void setCipFailedResults(double cipFailedResults) {
		this.cipFailedResults = cipFailedResults;
	}
	public double getCipTopFailedResults() {
		return cipTopFailedResults;
	}
	public void setCipTopFailedResults(double cipTopFailedResults) {
		this.cipTopFailedResults = cipTopFailedResults;
	}
	public double getCipTopTotalRequests() {
		return cipTopTotalRequests;
	}
	public void setCipTopTotalRequests(double cipTopTotalRequests) {
		this.cipTopTotalRequests = cipTopTotalRequests;
	}
	public double getCipTopTotalResults() {
		return cipTopTotalResults;
	}
	public void setCipTopTotalResults(double cipTopTotalResults) {
		this.cipTopTotalResults = cipTopTotalResults;
	}
	public double getCipTotalRequests() {
		return cipTotalRequests;
	}
	public void setCipTotalRequests(double cipTotalRequests) {
		this.cipTotalRequests = cipTotalRequests;
	}
	public double getCipTotalResults() {
		return cipTotalResults;
	}
	public void setCipTotalResults(double cipTotalResults) {
		this.cipTotalResults = cipTotalResults;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	
	
}
