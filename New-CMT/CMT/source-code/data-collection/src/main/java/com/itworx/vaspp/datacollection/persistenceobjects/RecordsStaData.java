package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RecordsStaData extends PersistenceObject{
	public Date dateTime;
	public double nodeId;
	public double recordsIn;
	public double recordsOut;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getNodeId() {
		return nodeId;
	}
	public void setNodeId(double nodeId) {
		this.nodeId = nodeId;
	}
	public double getRecordsIn() {
		return recordsIn;
	}
	public void setRecordsIn(double recordsIn) {
		this.recordsIn = recordsIn;
	}
	public double getRecordsOut() {
		return recordsOut;
	}
	public void setRecordsOut(double recordsOut) {
		this.recordsOut = recordsOut;
	}
public RecordsStaData()
{}
}
