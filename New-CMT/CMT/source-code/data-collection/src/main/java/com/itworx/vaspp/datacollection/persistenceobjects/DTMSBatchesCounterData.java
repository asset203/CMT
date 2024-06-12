package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class DTMSBatchesCounterData extends PersistenceObject{
	
	public Date dateTime;
	public String userId;
	public double msisdnCount;
	public double successMsisdnCount;
	public double failMsisdnCount;
	public double messageCount;
	public double realMessageCount;
	public double batchCount;
	public double successBatchCount;
	public double failBatchCount;
	public double retryCount;
	public double batchDuration;
	public double msisdnDuration;
	public double avgDuration;
	public double maxDuration;
	public double minDuration;
	
	public double getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(double avgDuration) {
		this.avgDuration = avgDuration;
	}
	public double getBatchCount() {
		return batchCount;
	}
	public void setBatchCount(double batchCount) {
		this.batchCount = batchCount;
	}
	public double getBatchDuration() {
		return batchDuration;
	}
	public void setBatchDuration(double batchDuration) {
		this.batchDuration = batchDuration;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getFailBatchCount() {
		return failBatchCount;
	}
	public void setFailBatchCount(double failBatchCount) {
		this.failBatchCount = failBatchCount;
	}
	public double getFailMsisdnCount() {
		return failMsisdnCount;
	}
	public void setFailMsisdnCount(double failMsisdnCount) {
		this.failMsisdnCount = failMsisdnCount;
	}
	public double getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}
	public double getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(double messageCount) {
		this.messageCount = messageCount;
	}
	public double getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(double minDuration) {
		this.minDuration = minDuration;
	}
	public double getMsisdnCount() {
		return msisdnCount;
	}
	public void setMsisdnCount(double msisdnCount) {
		this.msisdnCount = msisdnCount;
	}
	public double getRealMessageCount() {
		return realMessageCount;
	}
	public void setRealMessageCount(double realMessageCount) {
		this.realMessageCount = realMessageCount;
	}
	public double getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(double retryCount) {
		this.retryCount = retryCount;
	}
	public double getSuccessBatchCount() {
		return successBatchCount;
	}
	public void setSuccessBatchCount(double successBatchCount) {
		this.successBatchCount = successBatchCount;
	}
	public double getSuccessMsisdnCount() {
		return successMsisdnCount;
	}
	public void setSuccessMsisdnCount(double successMsisdnCount) {
		this.successMsisdnCount = successMsisdnCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getMsisdnDuration() {
		return msisdnDuration;
	}
	public void setMsisdnDuration(double msisdnDuration) {
		this.msisdnDuration = msisdnDuration;
	}

	
}
