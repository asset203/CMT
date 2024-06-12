package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class IVRShortCodesData extends PersistenceObject{
	public Date dateTime;
	public String trd;
	public double callAttempts;
	public double failedCalls;
	public double answeredCalls;
	public String connectedSwitch;
	public String shortCode;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTrd() {
		return trd;
	}
	public void setTrd(String trd) {
		this.trd = trd;
	}
	public double getCallAttempts() {
		return callAttempts;
	}
	public void setCallAttempts(double callAttempts) {
		this.callAttempts = callAttempts;
	}
	public double getFailedCalls() {
		return failedCalls;
	}
	public void setFailedCalls(double failedCalls) {
		this.failedCalls = failedCalls;
	}
	public double getAnsweredCalls() {
		return answeredCalls;
	}
	public void setAnsweredCalls(double answeredCalls) {
		this.answeredCalls = answeredCalls;
	}
	public String getConnectedSwitch() {
		return connectedSwitch;
	}
	public void setConnectedSwitch(String connectedSwitch) {
		this.connectedSwitch = connectedSwitch;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
public IVRShortCodesData()
{
}
}
