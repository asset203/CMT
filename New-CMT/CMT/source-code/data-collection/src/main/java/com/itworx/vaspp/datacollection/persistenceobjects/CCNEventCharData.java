package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CCNEventCharData extends PersistenceObject{
	public Date dateTime;
	public String telServiceCode;
	public String node;
	public String serviceClass;
	public String cdrType;
	public String trafficCase;
	public String shortCode;
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public double volume;
	public double charge;
	public double interrogation;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTelServiceCode() {
		return telServiceCode;
	}
	public void setTelServiceCode(String telServiceCode) {
		this.telServiceCode = telServiceCode;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	public String getCdrType() {
		return cdrType;
	}
	public void setCdrType(String cdrType) {
		this.cdrType = cdrType;
	}
	public String getTrafficCase() {
		return trafficCase;
	}
	public void setTrafficCase(String trafficCase) {
		this.trafficCase = trafficCase;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public double getInterrogation() {
		return interrogation;
	}
	public void setInterrogation(double interrogation) {
		this.interrogation = interrogation;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public CCNEventCharData()
{}
}
