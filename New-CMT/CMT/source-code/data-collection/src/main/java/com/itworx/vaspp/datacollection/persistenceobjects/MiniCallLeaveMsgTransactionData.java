package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallLeaveMsgTransactionData extends PersistenceObject{
	
	public double sendOffNetNo;
	
	public Date dateTime;
	
	public double sendOnNetNo;
	
	public double avgDuration;
	
	public double flowOneNumber;
	
	public double leaveMsgSubNo;
	
	public double unleaveNumber;

	public double getSendOffNetNo() {
		return sendOffNetNo;
	}

	public void setSendOffNetNo(double sendOffNetNo) {
		this.sendOffNetNo = sendOffNetNo;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getSendOnNetNo() {
		return sendOnNetNo;
	}

	public void setSendOnNetNo(double sendOnNetNo) {
		this.sendOnNetNo = sendOnNetNo;
	}

	public double getAvgDuration() {
		return avgDuration;
	}

	public void setAvgDuration(double avgDuration) {
		this.avgDuration = avgDuration;
	}

	public double getFlowOneNumber() {
		return flowOneNumber;
	}

	public void setFlowOneNumber(double flowOneNumber) {
		this.flowOneNumber = flowOneNumber;
	}

	public double getLeaveMsgSubNo() {
		return leaveMsgSubNo;
	}

	public void setLeaveMsgSubNo(double leaveMsgSubNo) {
		this.leaveMsgSubNo = leaveMsgSubNo;
	}

	public double getUnleaveNumber() {
		return unleaveNumber;
	}

	public void setUnleaveNumber(double unleaveNumber) {
		this.unleaveNumber = unleaveNumber;
	}

	
}
