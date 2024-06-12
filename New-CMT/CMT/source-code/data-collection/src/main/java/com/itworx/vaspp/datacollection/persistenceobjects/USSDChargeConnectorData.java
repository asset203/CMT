package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class USSDChargeConnectorData extends PersistenceObject{
	public Date dateTime;
	public String respCode;
	public double reqCount;
	public double respCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public double getReqCount() {
		return reqCount;
	}
	public void setReqCount(double reqCount) {
		this.reqCount = reqCount;
	}
	public double getRespCount() {
		return respCount;
	}
	public void setRespCount(double respCount) {
		this.respCount = respCount;
	}
public USSDChargeConnectorData()
{
}
}
