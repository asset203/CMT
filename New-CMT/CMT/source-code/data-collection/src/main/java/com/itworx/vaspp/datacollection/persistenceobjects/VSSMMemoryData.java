package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VSSMMemoryData extends PersistenceObject {
	public Date dateTime;
	public double memUtilizMin;
	public double memUtilizMax;
	public double memUtilizAvg;
	public double swapUtilizMin;
	public double swapUtilizMax;
	public double swapUtilizAvg;
	public VSSMMemoryData()
	{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMemUtilizMin() {
		return memUtilizMin;
	}
	public void setMemUtilizMin(double memUtilizMin) {
		this.memUtilizMin = memUtilizMin;
	}
	public double getMemUtilizMax() {
		return memUtilizMax;
	}
	public void setMemUtilizMax(double memUtilizMax) {
		this.memUtilizMax = memUtilizMax;
	}
	public double getMemUtilizAvg() {
		return memUtilizAvg;
	}
	public void setMemUtilizAvg(double memUtilizAvg) {
		this.memUtilizAvg = memUtilizAvg;
	}
	public double getSwapUtilizMin() {
		return swapUtilizMin;
	}
	public void setSwapUtilizMin(double swapUtilizMin) {
		this.swapUtilizMin = swapUtilizMin;
	}
	public double getSwapUtilizMax() {
		return swapUtilizMax;
	}
	public void setSwapUtilizMax(double swapUtilizMax) {
		this.swapUtilizMax = swapUtilizMax;
	}
	public double getSwapUtilizAvg() {
		return swapUtilizAvg;
	}
	public void setSwapUtilizAvg(double swapUtilizAvg) {
		this.swapUtilizAvg = swapUtilizAvg;
	}
	
}
