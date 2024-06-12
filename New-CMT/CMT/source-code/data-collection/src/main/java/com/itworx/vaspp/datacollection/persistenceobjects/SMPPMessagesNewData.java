package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMPPMessagesNewData extends PersistenceObject{
	
	public Date dateTime;
	public long inqueue;
	public long dequeue;
	public long send;
	public long successZero;
	public long successOne;
	public long successTwo;
	public long successThree;
	public long successFour;
	public long successFive;
	public long failure;	
	public String applicationName;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getFailure() {
		return failure;
	}
	public void setFailure(long failure) {
		this.failure = failure;
	}
	public long getDequeue() {
		return dequeue;
	}
	public void setDequeue(long dequeue) {
		this.dequeue = dequeue;
	}
	public long getInqueue() {
		return inqueue;
	}
	public void setInqueue(long inqueue) {
		this.inqueue = inqueue;
	}
	public long getSend() {
		return send;
	}
	public void setSend(long send) {
		this.send = send;
	}
	public long getSuccessFive() {
		return successFive;
	}
	public void setSuccessFive(long successFive) {
		this.successFive = successFive;
	}
	public long getSuccessFour() {
		return successFour;
	}
	public void setSuccessFour(long successFour) {
		this.successFour = successFour;
	}
	public long getSuccessOne() {
		return successOne;
	}
	public void setSuccessOne(long successOne) {
		this.successOne = successOne;
	}
	public long getSuccessThree() {
		return successThree;
	}
	public void setSuccessThree(long successThree) {
		this.successThree = successThree;
	}
	public long getSuccessTwo() {
		return successTwo;
	}
	public void setSuccessTwo(long successTwo) {
		this.successTwo = successTwo;
	}
	public long getSuccessZero() {
		return successZero;
	}
	public void setSuccessZero(long successZero) {
		this.successZero = successZero;
	}

}
