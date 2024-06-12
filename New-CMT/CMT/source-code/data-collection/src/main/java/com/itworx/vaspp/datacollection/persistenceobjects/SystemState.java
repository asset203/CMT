package com.itworx.vaspp.datacollection.persistenceobjects;

import java.sql.Timestamp;


public class SystemState extends PersistenceObject{
	public Timestamp dateTime;
	public long processResidentMemory;
	public long processMemory;
	public long processCpuUsage;
	
	public Timestamp getDateTime() {
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	public long getProcessResidentMemory() {
		return processResidentMemory;
	}
	public void setProcessResidentMemory(long processResidentMemory) {
		this.processResidentMemory = processResidentMemory;
	}
	public long getProcessMemory() {
		return processMemory;
	}
	public void setProcessMemory(long processMemory) {
		this.processMemory = processMemory;
	}
	public long getProcessCpuUsage() {
		return processCpuUsage;
	}
	public void setProcessCpuUsage(long processCpuUsage) {
		this.processCpuUsage = processCpuUsage;
	}

	
	
}
