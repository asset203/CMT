package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class ASMStorageData extends PersistenceObject{
	public Date dateTime;
	public double queueNumber;
	public String queueName;
	public double queueCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getQueueNumber() {
		return queueNumber;
	}
	public void setQueueNumber(double queueNumber) {
		this.queueNumber = queueNumber;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public double getQueueCount() {
		return queueCount;
	}
	public void setQueueCount(double queueCount) {
		this.queueCount = queueCount;
	}
public ASMStorageData()
{}
}
