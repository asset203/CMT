package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class INAPInterfaceQualityData extends PersistenceObject{

	public Date dateTime;
	
	public long totalOfTotalRequests;
	public long totalOfFailedRequests;
	public long totalOfTotalResults;
	public long totalOfFailedResults;
	
	public double topOfTotalRequests;
	public double topOfFailedRequests;
	public double topOfTotalResults;
	public double topOfFailedResults;
	
	public String sdpId;

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

	public double getTopOfFailedRequests() {
		return topOfFailedRequests;
	}

	public void setTopOfFailedRequests(double topOfFailedRequests) {
		this.topOfFailedRequests = topOfFailedRequests;
	}

	public double getTopOfFailedResults() {
		return topOfFailedResults;
	}

	public void setTopOfFailedResults(double topOfFailedResults) {
		this.topOfFailedResults = topOfFailedResults;
	}

	public double getTopOfTotalRequests() {
		return topOfTotalRequests;
	}

	public void setTopOfTotalRequests(double topOfTotalRequests) {
		this.topOfTotalRequests = topOfTotalRequests;
	}

	public double getTopOfTotalResults() {
		return topOfTotalResults;
	}

	public void setTopOfTotalResults(double topOfTotalResults) {
		this.topOfTotalResults = topOfTotalResults;
	}

	public long getTotalOfFailedRequests() {
		return totalOfFailedRequests;
	}

	public void setTotalOfFailedRequests(long totalOfFailedRequests) {
		this.totalOfFailedRequests = totalOfFailedRequests;
	}

	public long getTotalOfFailedResults() {
		return totalOfFailedResults;
	}

	public void setTotalOfFailedResults(long totalOfFailedResults) {
		this.totalOfFailedResults = totalOfFailedResults;
	}

	public long getTotalOfTotalRequests() {
		return totalOfTotalRequests;
	}

	public void setTotalOfTotalRequests(long totalOfTotalRequests) {
		this.totalOfTotalRequests = totalOfTotalRequests;
	}

	public long getTotalOfTotalResults() {
		return totalOfTotalResults;
	}

	public void setTotalOfTotalResults(long totalOfTotalResults) {
		this.totalOfTotalResults = totalOfTotalResults;
	}

	
}
