package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallMsgTransactionData extends PersistenceObject{
		
	public Date dateTime;
	
	public double leaveMsgNum;
	
	public double leaveMsgerNum;
	
	public double transmitNum;
	
	public double transmiterNum;
	
	public double restoreNum;
	
	public double restoreerNum;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getLeaveMsgNum() {
		return leaveMsgNum;
	}

	public void setLeaveMsgNum(double leaveMsgNum) {
		this.leaveMsgNum = leaveMsgNum;
	}

	public double getTransmitNum() {
		return transmitNum;
	}

	public void setTransmitNum(double transmitNum) {
		this.transmitNum = transmitNum;
	}

	public double getTransmiterNum() {
		return transmiterNum;
	}

	public void setTransmiterNum(double transmiterNum) {
		this.transmiterNum = transmiterNum;
	}

	public double getRestoreNum() {
		return restoreNum;
	}

	public void setRestoreNum(double restoreNum) {
		this.restoreNum = restoreNum;
	}

	public double getRestoreerNum() {
		return restoreerNum;
	}

	public void setRestoreerNum(double restoreerNum) {
		this.restoreerNum = restoreerNum;
	}

	public double getLeaveMsgerNum() {
		return leaveMsgerNum;
	}

	public void setLeaveMsgerNum(double leaveMsgerNum) {
		this.leaveMsgerNum = leaveMsgerNum;
	}
	
}
