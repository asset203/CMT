package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CPUMemoryUtilizationData extends PersistenceObject{
	
	public Date dateTime;
	
	public double memoryUtilization;
	
	public double cpuUtilization;

	public double getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(double cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getMemoryUtilization() {
		return memoryUtilization;
	}

	public void setMemoryUtilization(double memoryUtilization) {
		this.memoryUtilization = memoryUtilization;
	}

	

}
