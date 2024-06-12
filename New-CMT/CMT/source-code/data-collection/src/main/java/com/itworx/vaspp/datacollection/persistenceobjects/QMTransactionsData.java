package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class QMTransactionsData extends PersistenceObject{
	public Date dateTime;
	public String apn;
	public String cc;
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String packageID;
	public String barrierType;
	public double durationVolume;
	public double dataVolume;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	
	public String getPackageID() {
		return packageID;
	}
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	public String getBarrierType() {
		return barrierType;
	}
	public void setBarrierType(String barrierType) {
		this.barrierType = barrierType;
	}
	public double getDurationVolume() {
		return durationVolume;
	}
	public void setDurationVolume(double durationVolume) {
		this.durationVolume = durationVolume;
	}
	public double getDataVolume() {
		return dataVolume;
	}
	public void setDataVolume(double dataVolume) {
		this.dataVolume = dataVolume;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public QMTransactionsData()
{}
}
