package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VSSMCpuData extends PersistenceObject {
	
	public Date dateTime;
	public double persUserMin;
	public double persUserMax;
	public double persUserAvg;
	public double persSysMin;
	public double persSysMax;
	public double persSysAvg;
	public double persWioMin;
	public double persWioMax;
	public double persWioAvg;
	public double persIdleMin;
	public double persIdleMax;
	public double persIdleAvg;
	public VSSMCpuData()
	{}
	
	
	public void setDateTime(Date dateTime)
	{
		this.dateTime=dateTime;
	}
	public Date getDateTime()
	{
		return this.dateTime;
	}
	
	public double getPersUserMin() {
		return persUserMin;
	}


	public void setPersUserMin(double persUserMin) {
		this.persUserMin = persUserMin;
	}


	public double getPersUserMax() {
		return persUserMax;
	}


	public void setPersUserMax(double persUserMax) {
		this.persUserMax = persUserMax;
	}


	public double getPersUserAvg() {
		return persUserAvg;
	}


	public void setPersUserAvg(double persUserAvg) {
		this.persUserAvg = persUserAvg;
	}


	public double getPersSysMin() {
		return persSysMin;
	}


	public void setPersSysMin(double persSysMin) {
		this.persSysMin = persSysMin;
	}


	public double getPersSysMax() {
		return persSysMax;
	}


	public void setPersSysMax(double persSysMax) {
		this.persSysMax = persSysMax;
	}


	public double getPersSysAvg() {
		return persSysAvg;
	}


	public void setPersSysAvg(double persSysAvg) {
		this.persSysAvg = persSysAvg;
	}


	public double getPersWioMin() {
		return persWioMin;
	}


	public void setPersWioMin(double persWioMin) {
		this.persWioMin = persWioMin;
	}


	public double getPersWioMax() {
		return persWioMax;
	}


	public void setPersWioMax(double persWioMax) {
		this.persWioMax = persWioMax;
	}


	public double getPersWioAvg() {
		return persWioAvg;
	}


	public void setPersWioAvg(double persWioAvg) {
		this.persWioAvg = persWioAvg;
	}


	public double getPersIdleMin() {
		return persIdleMin;
	}


	public void setPersIdleMin(double persIdleMin) {
		this.persIdleMin = persIdleMin;
	}


	public double getPersIdleMax() {
		return persIdleMax;
	}


	public void setPersIdleMax(double persIdleMax) {
		this.persIdleMax = persIdleMax;
	}


	public double getPersIdleAvg() {
		return persIdleAvg;
	}


	public void setPersIdleAvg(double persIdleAvg) {
		this.persIdleAvg = persIdleAvg;
	}
	
	
}
