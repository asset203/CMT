package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class PerformanceSGAFreePoolData extends PersistenceObject{
	
	public Date dateTime;
	
	public String pool;
	
	public String poolName;
	
	public long allocated;
	
	public long free;
	
	public double freesPrce;

	public long getAllocated() {
		return allocated;
	}

	public void setAllocated(long allocated) {
		this.allocated = allocated;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getFree() {
		return free;
	}

	public void setFree(long free) {
		this.free = free;
	}

	public double getFreesPrce() {
		return freesPrce;
	}

	public void setFreesPrce(double freesPrce) {
		this.freesPrce = freesPrce;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
}
