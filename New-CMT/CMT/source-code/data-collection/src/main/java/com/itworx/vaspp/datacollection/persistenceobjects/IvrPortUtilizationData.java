package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class IvrPortUtilizationData extends PersistenceObject {
	
	public Date dateTime;
	public String ivrSwitch;
	public String routeName;	
	public double maxUtilization;  
	public double mht;
	public double asr;
	public double blocking;
	public double bhcaCalls;
	public String terminatingNode;
	
	public double getAsr() {
		return asr;
	}
	public void setAsr(double asr) {
		this.asr = asr;
	}
	public double getBhcaCalls() {
		return bhcaCalls;
	}
	public void setBhcaCalls(double bhcaCalls) {
		this.bhcaCalls = bhcaCalls;
	}
	public double getBlocking() {
		return blocking;
	}
	public void setBlocking(double blocking) {
		this.blocking = blocking;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getIvrSwitch() {
		return ivrSwitch;
	}
	public void setIvrSwitch(String ivrSwitch) {
		this.ivrSwitch = ivrSwitch;
	}
	public double getMaxUtilization() {
		return maxUtilization;
	}
	public void setMaxUtilization(double maxUtilization) {
		this.maxUtilization = maxUtilization;
	}
	public double getMht() {
		return mht;
	}
	public void setMht(double mht) {
		this.mht = mht;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getTerminatingNode() {
		return terminatingNode;
	}
	public void setTerminatingNode(String terminatingNode) {
		this.terminatingNode = terminatingNode;
	}
	
	
}
