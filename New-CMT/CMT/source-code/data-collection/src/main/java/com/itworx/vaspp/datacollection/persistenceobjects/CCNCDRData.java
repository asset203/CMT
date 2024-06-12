package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNCDRData extends PersistenceObject {

	public Date dateTime;
	public String nodeId;
	public String apn;
	public String chargingChar;
	public double serviceClass;
	public String bearerType;
	public double dataVolume;
	public double finalCharge;
	public double count ;
	
	
	
	public CCNCDRData(){}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getChargingChar() {
		return chargingChar;
	}

	public void setChargingChar(String chargingChar) {
		this.chargingChar = chargingChar;
	}

	public double getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(double serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	public String getBearerType() {
		return bearerType;
	}

	public void setBearerType(String bearerType) {
		this.bearerType = bearerType;
	}

	public double getDataVolume() {
		return dataVolume;
	}

	public void setDataVolume(double dataVolume) {
		this.dataVolume = dataVolume;
	}

	public double getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(double finalCharge) {
		this.finalCharge = finalCharge;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	
	
}
