package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MMSDetailsData extends PersistenceObject{
	public Date dateTime;
	public double oiwType;
	public double diwType;
	public double succIndicator;
	public double errorCause;
	public double messageClass;
	public double serviceType;
	public double senderChargingType;
	public double recChargType;
	public double sendPreStatus;
	public double recPreStatus;
	public double chargedParty;
	public double delivReport;
	//public String origImsi;
	public String origMmsc;
	//public String destImsi;
	public String destmmsc;
	public double delStatus;
	public double messageSize;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getOiwType() {
		return oiwType;
	}
	public void setOiwType(double oiwType) {
		this.oiwType = oiwType;
	}
	public double getDiwType() {
		return diwType;
	}
	public void setDiwType(double diwType) {
		this.diwType = diwType;
	}
	public double getSuccIndicator() {
		return succIndicator;
	}
	public void setSuccIndicator(double succIndicator) {
		this.succIndicator = succIndicator;
	}
	public double getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(double errorCause) {
		this.errorCause = errorCause;
	}
	public double getMessageClass() {
		return messageClass;
	}
	public void setMessageClass(double messageClass) {
		this.messageClass = messageClass;
	}
	public double getServiceType() {
		return serviceType;
	}
	public void setServiceType(double serviceType) {
		this.serviceType = serviceType;
	}
	public double getSenderChargingType() {
		return senderChargingType;
	}
	public void setSenderChargingType(double senderChargingType) {
		this.senderChargingType = senderChargingType;
	}
	public double getRecChargType() {
		return recChargType;
	}
	public void setRecChargType(double recChargType) {
		this.recChargType = recChargType;
	}
	public double getSendPreStatus() {
		return sendPreStatus;
	}
	public void setSendPreStatus(double sendPreStatus) {
		this.sendPreStatus = sendPreStatus;
	}
	public double getRecPreStatus() {
		return recPreStatus;
	}
	public void setRecPreStatus(double recPreStatus) {
		this.recPreStatus = recPreStatus;
	}
	public double getChargedParty() {
		return chargedParty;
	}
	public void setChargedParty(double chargedParty) {
		this.chargedParty = chargedParty;
	}
	public double getDelivReport() {
		return delivReport;
	}
	public void setDelivReport(double delivReport) {
		this.delivReport = delivReport;
	}
	/*public String getOrigImsi() {
		return origImsi;
	}
	public void setOrigImsi(String origImsi) {
		this.origImsi = origImsi;
	}*/
	public String getOrigMmsc() {
		return origMmsc;
	}
	public void setOrigMmsc(String origMmsc) {
		this.origMmsc = origMmsc;
	}
/*	public String getDestImsi() {
		return destImsi;
	}
	public void setDestImsi(String destImsi) {
		this.destImsi = destImsi;
	}*/
	public String getDestmmsc() {
		return destmmsc;
	}
	public void setDestmmsc(String destmmsc) {
		this.destmmsc = destmmsc;
	}
	public double getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(double delStatus) {
		this.delStatus = delStatus;
	}
	public double getMessageSize() {
		return messageSize;
	}
	public void setMessageSize(double messageSize) {
		this.messageSize = messageSize;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public MMSDetailsData()
{
}
}
