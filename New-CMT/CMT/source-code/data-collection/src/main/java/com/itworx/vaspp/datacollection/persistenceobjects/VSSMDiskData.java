package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VSSMDiskData  extends PersistenceObject{

	public Date dateTime;
	public String device;
	public double persBusyMin;
	public double persBusyMax;
	public double persBusyAvg;
	public double persWaitMin;
	public double persWaitMax;
	public double persWaitAvg;
	public double avgServTimeMin;
	public double avgServTimeMax;
	public double avgServTimeAvg;
	public double avgWaitTimeMin ;
	public double avgWaitTimeMax ;
	public double avgWaitTimeAvg;
	public double avgNoIoMin ;
	public double avgNoIoMax ;
	public double avgNoIoAvg ;
	public double kwSMin ;
	public double kwSMax;
	public double kwSAvg;
	public double KrSMin;
	public double KrSMax ;
	public double KrSAvg;
	public VSSMDiskData()
	{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public double getPersBusyMin() {
		return persBusyMin;
	}
	public void setPersBusyMin(double persBusyMin) {
		this.persBusyMin = persBusyMin;
	}
	public double getPersBusyMax() {
		return persBusyMax;
	}
	public void setPersBusyMax(double persBusyMax) {
		this.persBusyMax = persBusyMax;
	}
	public double getPersBusyAvg() {
		return persBusyAvg;
	}
	public void setPersBusyAvg(double persBusyAvg) {
		this.persBusyAvg = persBusyAvg;
	}
	public double getPersWaitMin() {
		return persWaitMin;
	}
	public void setPersWaitMin(double persWaitMin) {
		this.persWaitMin = persWaitMin;
	}
	public double getPersWaitMax() {
		return persWaitMax;
	}
	public void setPersWaitMax(double persWaitMax) {
		this.persWaitMax = persWaitMax;
	}
	public double getPersWaitAvg() {
		return persWaitAvg;
	}
	public void setPersWaitAvg(double persWaitAvg) {
		this.persWaitAvg = persWaitAvg;
	}
	public double getAvgServTimeMin() {
		return avgServTimeMin;
	}
	public void setAvgServTimeMin(double avgServTimeMin) {
		this.avgServTimeMin = avgServTimeMin;
	}
	public double getAvgServTimeMax() {
		return avgServTimeMax;
	}
	public void setAvgServTimeMax(double avgServTimeMax) {
		this.avgServTimeMax = avgServTimeMax;
	}
	public double getAvgServTimeAvg() {
		return avgServTimeAvg;
	}
	public void setAvgServTimeAvg(double avgServTimeAvg) {
		this.avgServTimeAvg = avgServTimeAvg;
	}
	public double getAvgWaitTimeMin() {
		return avgWaitTimeMin;
	}
	public void setAvgWaitTimeMin(double avgWaitTimeMin) {
		this.avgWaitTimeMin = avgWaitTimeMin;
	}
	public double getAvgWaitTimeMax() {
		return avgWaitTimeMax;
	}
	public void setAvgWaitTimeMax(double avgWaitTimeMax) {
		this.avgWaitTimeMax = avgWaitTimeMax;
	}
	public double getAvgWaitTimeAvg() {
		return avgWaitTimeAvg;
	}
	public void setAvgWaitTimeAvg(double avgWaitTimeAvg) {
		this.avgWaitTimeAvg = avgWaitTimeAvg;
	}
	public double getAvgNoIoMin() {
		return avgNoIoMin;
	}
	public void setAvgNoIoMin(double avgNoIoMin) {
		this.avgNoIoMin = avgNoIoMin;
	}
	public double getAvgNoIoMax() {
		return avgNoIoMax;
	}
	public void setAvgNoIoMax(double avgNoIoMax) {
		this.avgNoIoMax = avgNoIoMax;
	}
	public double getAvgNoIoAvg() {
		return avgNoIoAvg;
	}
	public void setAvgNoIoAvg(double avgNoIoAvg) {
		this.avgNoIoAvg = avgNoIoAvg;
	}
	public double getKwSMin() {
		return kwSMin;
	}
	public void setKwSMin(double kwSMin) {
		this.kwSMin = kwSMin;
	}
	public double getKwSMax() {
		return kwSMax;
	}
	public void setKwSMax(double kwSMax) {
		this.kwSMax = kwSMax;
	}
	public double getKwSAvg() {
		return kwSAvg;
	}
	public void setKwSAvg(double kwSAvg) {
		this.kwSAvg = kwSAvg;
	}
	public double getKrSMin() {
		return KrSMin;
	}
	public void setKrSMin(double krSMin) {
		KrSMin = krSMin;
	}
	public double getKrSMax() {
		return KrSMax;
	}
	public void setKrSMax(double krSMax) {
		KrSMax = krSMax;
	}
	public double getKrSAvg() {
		return KrSAvg;
	}
	public void setKrSAvg(double krSAvg) {
		KrSAvg = krSAvg;
	}
	
	
	
}
