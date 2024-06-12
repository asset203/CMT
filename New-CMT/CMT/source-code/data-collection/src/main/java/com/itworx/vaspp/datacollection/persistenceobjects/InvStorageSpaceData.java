package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class InvStorageSpaceData extends PersistenceObject{
	public Date dateTime;
	public double capacity;
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double avgUsedSpace;
	public double maxUsedSpace;
	public double avgFreeSpace;
	public double minFreeSpace;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public double getAvgUsedSpace() {
		return avgUsedSpace;
	}
	public void setAvgUsedSpace(double avgUsedSpace) {
		this.avgUsedSpace = avgUsedSpace;
	}
	public double getMaxUsedSpace() {
		return maxUsedSpace;
	}
	public void setMaxUsedSpace(double maxUsedSpace) {
		this.maxUsedSpace = maxUsedSpace;
	}
	public double getAvgFreeSpace() {
		return avgFreeSpace;
	}
	public void setAvgFreeSpace(double avgFreeSpace) {
		this.avgFreeSpace = avgFreeSpace;
	}
	public double getMinFreeSpace() {
		return minFreeSpace;
	}
	public void setMinFreeSpace(double minFreeSpace) {
		this.minFreeSpace = minFreeSpace;
	}
public InvStorageSpaceData()
{
}
}
