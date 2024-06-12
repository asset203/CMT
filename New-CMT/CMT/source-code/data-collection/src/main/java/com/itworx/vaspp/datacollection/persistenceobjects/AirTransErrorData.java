package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AirTransErrorData  extends PersistenceObject{
public Date dateTime;
	public double reqTypeId;
	public double forcAirServerId;
	public double statusCode;
	public double count;
	public double nodeId;
	
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getReqTypeId() {
		return reqTypeId;
	}

	public void setReqTypeId(double reqTypeId) {
		this.reqTypeId = reqTypeId;
	}

	public double getForcAirServerId() {
		return forcAirServerId;
	}

	public void setForcAirServerId(double forcAirServerId) {
		this.forcAirServerId = forcAirServerId;
	}

	public double getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(double statusCode) {
		this.statusCode = statusCode;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getNodeId() {
		return nodeId;
	}

	public void setNodeId(double nodeId) {
		this.nodeId = nodeId;
	}

	public AirTransErrorData()
	{}

}
