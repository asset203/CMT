package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VSSMInterfaceDate extends PersistenceObject {
	public Date dateTime;
	public String name;
	public double rkbSMin;
	public double rkbSMax;
	public double rkbSAvg;
	public double wkbSMin;
	public double wkbSMax;
	public double wkbSAvg;
	public double persUtilizeMin;
	public double persUtilizeMax;
	public double persUtilizeAvg;
	public double satMin;
	public double satMax;
	public double satAvg;
	
public VSSMInterfaceDate()
{}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRkbSMin() {
		return rkbSMin;
	}
	public void setRkbSMin(double rkbSMin) {
		this.rkbSMin = rkbSMin;
	}
	public double getRkbSMax() {
		return rkbSMax;
	}
	public void setRkbSMax(double rkbSMax) {
		this.rkbSMax = rkbSMax;
	}
	public double getRkbSAvg() {
		return rkbSAvg;
	}
	public void setRkbSAvg(double rkbSAvg) {
		this.rkbSAvg = rkbSAvg;
	}
	public double getWkbSMin() {
		return wkbSMin;
	}
	public void setWkbSMin(double wkbSMin) {
		this.wkbSMin = wkbSMin;
	}
	public double getWkbSMax() {
		return wkbSMax;
	}
	public void setWkbSMax(double wkbSMax) {
		this.wkbSMax = wkbSMax;
	}
	public double getWkbSAvg() {
		return wkbSAvg;
	}
	public void setWkbSAvg(double wkbSAvg) {
		this.wkbSAvg = wkbSAvg;
	}
	public double getPersUtilizeMin() {
		return persUtilizeMin;
	}
	public void setPersUtilizeMin(double persUtilizeMin) {
		this.persUtilizeMin = persUtilizeMin;
	}
	public double getPersUtilizeMax() {
		return persUtilizeMax;
	}
	public void setPersUtilizeMax(double persUtilizeMax) {
		this.persUtilizeMax = persUtilizeMax;
	}
	public double getPersUtilizeAvg() {
		return persUtilizeAvg;
	}
	public void setPersUtilizeAvg(double persUtilizeAvg) {
		this.persUtilizeAvg = persUtilizeAvg;
	}
	public double getSatMin() {
		return satMin;
	}
	public void setSatMin(double satMin) {
		this.satMin = satMin;
	}
	public double getSatMax() {
		return satMax;
	}
	public void setSatMax(double satMax) {
		this.satMax = satMax;
	}
	public double getSatAvg() {
		return satAvg;
	}
	public void setSatAvg(double satAvg) {
		this.satAvg = satAvg;
	}
	
}
