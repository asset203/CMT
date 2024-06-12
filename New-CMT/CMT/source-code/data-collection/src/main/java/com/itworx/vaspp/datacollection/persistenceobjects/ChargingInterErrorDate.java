package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class ChargingInterErrorDate extends PersistenceObject {
	public Date dateTime;
	public double clientID;
	public double moduleTypeID;
	public double serviceID;
	public double errorCode;
	public double count;
	public double serverID;

	public double getServerID() {
		return serverID;
	}

	public void setServerID(double serverID) {
		this.serverID = serverID;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setClientID(double clientID) {
		this.clientID = clientID;
	}

	public double getClientID() {
		return this.clientID;
	}

	public void setModuleTypeID(double moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
	}

	public double getModuleTypeID() {
		return this.moduleTypeID;
	}

	public void setServiceID(double serviceID) {
		this.serviceID = serviceID;
	}

	public double getServiceID() {
		return this.serviceID;
	}

	public void setErrorCode(double errorCode) {
		this.errorCode = errorCode;
	}

	public double getErrorCode() {
		return this.errorCode;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getCount() {
		return this.count;
	}

}
