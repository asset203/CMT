package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AirNodeMapData  extends PersistenceObject{
	public Date dateTime;
	public double nodeId;
	public double airId;
	public String airName;
	public String airIp;
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
	public double getAirId() {
		return airId;
	}
	public void setAirId(double airId) {
		this.airId = airId;
	}
	public String getAirName() {
		return airName;
	}
	public void setAirName(String airName) {
		this.airName = airName;
	}
	public String getAirIp() {
		return airIp;
	}
	public void setAirIp(String airIp) {
		this.airIp = airIp;
	}
	public AirNodeMapData()
	{
		
	}

}
