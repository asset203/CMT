package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MiniCallBroadcastSentData extends PersistenceObject{

	public Date dateTime;
	public String taskId;
	public String taskName;
	public double keyStoke;
	public double forward;
	public double broadcastCount;
	public double successCount;
	
	public MiniCallBroadcastSentData(){}

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

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public double getKeyStoke() {
		return keyStoke;
	}

	public void setKeyStoke(double keyStoke) {
		this.keyStoke = keyStoke;
	}

	public double getForward() {
		return forward;
	}

	public void setForward(double forward) {
		this.forward = forward;
	}

	public double getBroadcastCount() {
		return broadcastCount;
	}

	public void setBroadcastCount(double broadcastCount) {
		this.broadcastCount = broadcastCount;
	}

	public double getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(double successCount) {
		this.successCount = successCount;
	}

}
