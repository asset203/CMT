package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallBroadcastReceivedData extends PersistenceObject {
	
	public Date dateTime;
	public String taskId;
	public String operReturn;
	public double count;
	
	public MiniCallBroadcastReceivedData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOperReturn() {
		return operReturn;
	}

	public void setOperReturn(String operReturn) {
		this.operReturn = operReturn;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	
	
}
