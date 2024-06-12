package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RedRoamCallsData extends PersistenceObject{
	public Date dateTime;
	public String aserviceClass;
	public String zoneId;
	public String ucipStatus;
	public String fstLegCallStatus;
	public String sndLegCallStatus;
	public String fstLegCallDuration;
	public String sndLegCallDuration;
	public String bridgeDyration;
	public String smsToaStatus;
	public String retryCount;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getAserviceClass() {
		return aserviceClass;
	}
	public void setAserviceClass(String aserviceClass) {
		this.aserviceClass = aserviceClass;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getUcipStatus() {
		return ucipStatus;
	}
	public void setUcipStatus(String ucipStatus) {
		this.ucipStatus = ucipStatus;
	}
	public String getFstLegCallStatus() {
		return fstLegCallStatus;
	}
	public void setFstLegCallStatus(String fstLegCallStatus) {
		this.fstLegCallStatus = fstLegCallStatus;
	}
	public String getSndLegCallStatus() {
		return sndLegCallStatus;
	}
	public void setSndLegCallStatus(String sndLegCallStatus) {
		this.sndLegCallStatus = sndLegCallStatus;
	}
	public String getFstLegCallDuration() {
		return fstLegCallDuration;
	}
	public void setFstLegCallDuration(String fstLegCallDuration) {
		this.fstLegCallDuration = fstLegCallDuration;
	}
	public String getSndLegCallDuration() {
		return sndLegCallDuration;
	}
	public void setSndLegCallDuration(String sndLegCallDuration) {
		this.sndLegCallDuration = sndLegCallDuration;
	}
	public String getBridgeDyration() {
		return bridgeDyration;
	}
	public void setBridgeDyration(String bridgeDyration) {
		this.bridgeDyration = bridgeDyration;
	}
	public String getSmsToaStatus() {
		return smsToaStatus;
	}
	public void setSmsToaStatus(String smsToaStatus) {
		this.smsToaStatus = smsToaStatus;
	}
	public String getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public RedRoamCallsData()
{
	}
}
